import { CommonModule } from '@angular/common';
import { Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CdkDragDrop, DragDropModule } from '@angular/cdk/drag-drop';
import { A11yModule } from '@angular/cdk/a11y';
import { NgxPermissionsService } from 'ngx-permissions';
import { MessageService } from 'primeng/api';
import { forkJoin } from 'rxjs';
import { Subscription } from 'rxjs';
import { AuthService } from '@core/authentication';
import { DataViewState } from '@shared';
import { OrdemServicoService } from '../os/ordem-servico.service';
import { OrdemServicoResponse } from '../os/models/os.models';
import { PatioAdjustment, PatioAlert, PatioAlertBoard, PatioAlertEvent, PatioBoard, PatioCard, PatioColumn, PatioFilters, PatioItem, PatioReservation, PatioReservationBoard, PatioResource, PatioService, PatioTeamMember, PatioTimer, PatioTvBoard } from './patio.service';

type PatioView = 'kanban' | 'lista' | 'agenda' | 'boxes' | 'alertas' | 'tv';

@Component({
  standalone: true,
  selector: 'app-patio',
  imports: [CommonModule, FormsModule, RouterLink, DragDropModule, A11yModule, DataViewState],
  templateUrl: './patio.html',
  styleUrl: './patio.scss',
})
export class PatioComponent implements OnInit, OnDestroy {
  @ViewChild('pageRoot') pageRoot?: ElementRef<HTMLElement>;
  private readonly api = inject(PatioService);
  private readonly os = inject(OrdemServicoService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly messages = inject(MessageService);
  private readonly permissions = inject(NgxPermissionsService);
  private readonly auth = inject(AuthService);
  private profileSubscription?: Subscription;
  private filterStorageKey: string | null = null;
  private scopeVersion = 0;
  private readonly pendingTimerKeys = new Map<string, string>();
  private pendingAdjustment: { itemId: number; timeId: number; delta: number; reason: string; key: string } | null = null;
  private pendingReservation: { resourceId: number; osId: number; start: string; end: string; key: string } | null = null;
  private pollTimer?: ReturnType<typeof setTimeout>;
  private searchTimer?: ReturnType<typeof setTimeout>;
  private displayTicker?: ReturnType<typeof setInterval>;
  private channel?: BroadcastChannel;
  private failures = 0;
  private reloadAfterCurrent = false;
  private returnFocusTo: HTMLElement | null = null;

  filters: PatioFilters = { search: '', statusId: null, responsavelId: null, prioridade: '', atrasado: null };
  board: PatioBoard | null = null;
  tvBoard: PatioTvBoard | null = null;
  resources: PatioResource[] = [];
  reservationBoard: PatioReservationBoard | null = null;
  selectedDay = new Date(Date.now() - new Date().getTimezoneOffset() * 60_000).toISOString().slice(0, 10);
  selectedResourceId: number | null = null;
  reservationStart = '';
  reservationEnd = '';
  reservationBusy = false;
  reservationBusyId: number | null = null;
  alertBoard: PatioAlertBoard | null = null;
  alertEvents: Record<number, PatioAlertEvent[]> = {};
  alertPage = 0;
  alertBusyId: number | null = null;
  alertReason = '';
  alertUntil = '';
  resourcesLoaded = false;
  view: PatioView = 'kanban';
  page = 0;
  size = 20;
  loading = true;
  refreshing = false;
  movingId: number | null = null;
  error: 'offline' | 'forbidden' | 'error' | null = null;
  expanded = false;
  nativeFullscreen = false;
  detailCard: PatioCard | null = null;
  detail: OrdemServicoResponse | null = null;
  detailServices: PatioItem[] = [];
  team: PatioTeamMember[] = [];
  assigningId: number | null = null;
  myEmployeeId: number | null = null;
  timersByItem: Record<number, PatioTimer[]> = {};
  adjustmentsByItem: Record<number, PatioAdjustment[]> = {};
  adjustDelta: number | null = null;
  adjustReason = '';
  adjustingId: number | null = null;
  timerReceivedAt: Record<number, number> = {};
  timerBusyItem: number | null = null;
  displayNow = Date.now();
  detailLoading = false;
  detailError = false;

  get canMove(): boolean {
    return !!(this.permissions.getPermission('ADMIN') || this.permissions.getPermission('OS_ALT_STATUS'));
  }
  get canAssign(): boolean {
    return !!(this.permissions.getPermission('ADMIN') || this.permissions.getPermission('OS_ALT_FUNCIONARIO'));
  }
  get canCreateOrder(): boolean {
    return !!(this.permissions.getPermission('ADMIN') || this.permissions.getPermission('OS_INCLUIR'));
  }
  get canReadClients(): boolean {
    return !!(this.permissions.getPermission('ADMIN') || this.permissions.getPermission('GERAL_USUARIO'));
  }
  get canOpenAgenda(): boolean {
    return !!(this.permissions.getPermission('ADMIN') || this.permissions.getPermission('GERAL_AGENDAMENTO_VISUALIZAR'));
  }
  get canManageResources(): boolean {
    return !!(this.permissions.getPermission('ADMIN') || this.permissions.getPermission('GERAL_AGENDAMENTO_EDITAR'));
  }
  get canAdjustTime(): boolean {
    return !!(this.permissions.getPermission('ADMIN') || this.permissions.getPermission('OS_EDITAR'));
  }
  get canManageAlerts(): boolean { return this.canAdjustTime; }
  get cards(): PatioCard[] { return this.board?.cards ?? []; }
  get columns(): PatioColumn[] { return this.board?.columns ?? []; }
  get lastPage(): boolean { return (this.page + 1) * this.size >= (this.board?.count ?? 0); }
  get newTabUrl(): string {
    return this.router.serializeUrl(this.router.createUrlTree(['/patio'],
      { queryParams: { layout: 'expanded', mode: this.view } }));
  }

  ngOnInit(): void {
    this.profileSubscription = this.auth.user().subscribe(user => {
      const rawTenant = user.empresaId ?? user.tenantId ?? user.idEmpresa ?? user.id_empresa;
      const tenant = Number(typeof rawTenant === 'object' ? rawTenant?.id : rawTenant);
      const key = user.id != null && Number.isSafeInteger(tenant) && tenant > 0
        ? `neritech-patio-filters:${tenant}:${user.id}` : null;
      if (key === this.filterStorageKey) return;
      this.filterStorageKey = key;
      this.scopeVersion++;
      this.pendingTimerKeys.clear(); this.pendingAdjustment = null; this.pendingReservation = null;
      this.refreshing = false; this.reloadAfterCurrent = false;
      this.board = null; this.tvBoard = null; this.resources = []; this.reservationBoard = null;
      this.alertBoard = null; this.alertEvents = {}; this.resourcesLoaded = false;
      this.detailCard = null; this.detail = null; this.detailServices = [];
      this.team = []; this.myEmployeeId = null; this.timersByItem = {}; this.adjustmentsByItem = {};
      this.filters = { search: '', statusId: null, responsavelId: null, prioridade: '', atrasado: null };
      this.page = 0; this.size = 20;
      if (key && typeof sessionStorage !== 'undefined') {
        try {
          const saved = JSON.parse(sessionStorage.getItem(key) || '{}');
          if (typeof saved.search === 'string' && saved.search.length <= 100) this.filters.search = saved.search;
          if (Number.isSafeInteger(saved.statusId) && saved.statusId > 0) this.filters.statusId = saved.statusId;
          if (Number.isSafeInteger(saved.responsavelId) && saved.responsavelId > 0) this.filters.responsavelId = saved.responsavelId;
          if (['BAIXA', 'NORMAL', 'ALTA', 'URGENTE'].includes(saved.prioridade)) this.filters.prioridade = saved.prioridade;
          if (typeof saved.atrasado === 'boolean') this.filters.atrasado = saved.atrasado;
          if ([20, 50, 100].includes(saved.size)) this.size = saved.size;
        } catch { sessionStorage.removeItem(key); }
      }
      if (key) {
        const scope = this.scopeVersion;
        this.api.team().subscribe({
          next: team => { if (scope === this.scopeVersion) this.team = team; },
          error: () => { if (scope === this.scopeVersion) this.team = []; },
        });
        this.reload();
      }
    });
    this.route.queryParamMap.subscribe(params => {
      this.expanded = params.get('layout') === 'expanded';
      const mode = params.get('mode');
      if (mode === 'kanban' || mode === 'lista' || mode === 'agenda' || mode === 'boxes' || mode === 'alertas' || mode === 'tv') this.view = mode;
    });
    if (typeof BroadcastChannel !== 'undefined') {
      this.channel = new BroadcastChannel('neritech-patio-os');
      this.channel.onmessage = event => {
        this.reload();
        if (this.detailCard && event.data?.osId === this.detailCard.osId) this.refreshDetailTimers();
      };
    }
    this.reload();
    this.displayTicker = setInterval(() => this.displayNow = Date.now(), 1000);
  }

  ngOnDestroy(): void {
    this.profileSubscription?.unsubscribe();
    clearTimeout(this.pollTimer);
    clearTimeout(this.searchTimer);
    clearInterval(this.displayTicker);
    this.channel?.close();
    if (document.fullscreenElement === this.pageRoot?.nativeElement) void document.exitFullscreen();
  }

  @HostListener('document:visibilitychange')
  onVisibility(): void {
    if (!document.hidden) { this.reload(); this.refreshDetailTimers(); }
    else clearTimeout(this.pollTimer);
  }
  @HostListener('window:focus')
  onFocus(): void { if (!document.hidden) this.reload(); }
  @HostListener('document:fullscreenchange')
  onFullscreenChange(): void { this.nativeFullscreen = document.fullscreenElement === this.pageRoot?.nativeElement; }
  @HostListener('document:keydown.escape', ['$event'])
  onEscape(event: KeyboardEvent): void {
    if (!this.detailCard) return;
    event.stopPropagation();
    this.closeDetail();
  }

  reload(): void {
    if (document.hidden) return;
    clearTimeout(this.pollTimer);
    if (this.refreshing) { this.reloadAfterCurrent = true; return; }
    this.refreshing = true;
    const scope = this.scopeVersion;
    if (this.view === 'tv') {
      this.api.tv().subscribe({
        next: result => { if (scope !== this.scopeVersion) return; this.tvBoard = result; this.success(); },
        error: error => { if (scope === this.scopeVersion) this.failure(error); },
      });
      return;
    }
    if (this.view === 'alertas') {
      this.api.alerts(this.alertPage, 20).subscribe({
        next: result => { if (scope !== this.scopeVersion) return; this.alertBoard = result; this.success(); },
        error: error => { if (scope === this.scopeVersion) this.failure(error); },
      });
      return;
    }
    if (this.view === 'boxes') {
      forkJoin({ resources: this.api.resources(), reservations: this.api.reservations(this.selectedDay) }).subscribe({
        next: result => {
          if (scope !== this.scopeVersion) return;
          this.resources = result.resources;
          this.reservationBoard = result.reservations;
          this.resourcesLoaded = true;
          this.success();
        },
        error: error => { if (scope === this.scopeVersion) this.failure(error); },
      });
      return;
    }
    this.api.board(this.filters, this.page, this.size).subscribe({
      next: board => {
        if (scope !== this.scopeVersion) return;
        this.board = board; this.success();
        if (this.detailCard) this.detailCard = board.cards.find(card => card.osId === this.detailCard?.osId) ?? this.detailCard;
      },
      error: error => { if (scope === this.scopeVersion) this.failure(error); },
    });
  }

  private success(): void {
    this.error = null; this.loading = false; this.refreshing = false; this.failures = 0;
    this.schedulePoll();
    this.flushQueuedReload();
  }

  private failure(error: { status: number }): void {
    this.error = error.status === 403 ? 'forbidden' : error.status === 0 ? 'offline' : 'error';
    if (error.status === 401 || error.status === 403) {
      this.board = null; this.tvBoard = null; this.resources = []; this.resourcesLoaded = false;
      this.reservationBoard = null;
      this.alertBoard = null; this.alertEvents = {};
      this.detailCard = null; this.detail = null; this.detailServices = [];
      this.team = []; this.timersByItem = {}; this.adjustmentsByItem = {};
    }
    this.loading = false; this.refreshing = false;
    this.failures = Math.min(this.failures + 1, 3);
    this.schedulePoll();
    this.flushQueuedReload();
  }

  private flushQueuedReload(): void {
    if (!this.reloadAfterCurrent) return;
    this.reloadAfterCurrent = false;
    this.reload();
  }

  private schedulePoll(): void {
    if (!document.hidden) this.pollTimer = setTimeout(() => this.reload(), 30_000 * (2 ** this.failures));
  }
  onSearch(): void { clearTimeout(this.searchTimer); this.searchTimer = setTimeout(() => this.applyFilters(), 300); }
  applyFilters(): void { this.page = 0; this.saveFilters(); this.reload(); }
  changePage(delta: number): void { this.page += delta; this.reload(); }
  changeSize(): void { this.page = 0; this.saveFilters(); this.reload(); }
  private saveFilters(): void {
    if (!this.filterStorageKey || typeof sessionStorage === 'undefined') return;
    try { sessionStorage.setItem(this.filterStorageKey, JSON.stringify({ ...this.filters, size: this.size })); }
    catch { /* Navegação e filtros continuam funcionais sem storage. */ }
  }
  selectView(view: PatioView): void {
    this.view = view;
    void this.router.navigate([], { relativeTo: this.route,
      queryParams: { mode: view }, queryParamsHandling: 'merge', replaceUrl: true });
    this.reload();
  }
  changeAlertPage(delta: number): void {
    this.alertPage = Math.max(0, this.alertPage + delta);
    this.reload();
  }
  loadAlertHistory(alertId: number): void {
    this.api.alertHistory(alertId).subscribe({
      next: events => this.alertEvents[alertId] = events,
      error: () => this.alertEvents[alertId] = [],
    });
  }
  alertAction(alert: PatioAlert, action: 'acknowledge' | 'snooze' | 'resolve'): void {
    if (!this.canManageAlerts || this.alertBusyId !== null) return;
    if (action === 'snooze' && (!this.alertUntil || this.alertReason.trim().length < 10)) return;
    this.alertBusyId = alert.id;
    this.api.alertAction(alert, action, this.alertReason.trim() || undefined,
      action === 'snooze' ? this.alertUntil : undefined).subscribe({
      next: () => {
        this.alertBusyId = null;
        this.alertReason = ''; this.alertUntil = '';
        this.messages.add({ severity: 'success', summary: 'Estado do alerta registrado',
          detail: action === 'resolve' ? 'Se a condição persistir, o alerta será reaberto.' : undefined });
        this.channel?.postMessage({ osId: alert.osId });
        this.reload();
      },
      error: error => {
        this.alertBusyId = null;
        this.messages.add({ severity: 'warn', summary: 'Ação recusada',
          detail: error.status === 409 ? 'O alerta foi alterado. Atualize e tente novamente.' :
            'Confira o estado, a severidade e o motivo do alerta.' });
        this.reload();
      },
    });
  }
  cardsFor(column: PatioColumn): PatioCard[] { return this.cards.filter(card => (card.statusId ?? 0) === column.id); }
  progress(card: PatioCard): number {
    return card.servicosTotal ? Math.round(card.servicosConcluidos * 100 / card.servicosTotal) : 0;
  }
  remaining(card: PatioCard): number { return Math.max(0, card.minutosPrevistos - card.minutosTrabalhados); }
  blocker(card: PatioCard): string | null {
    if (card.statusCodigo === 'AGUARDANDO_APROVACAO') return 'Aguardando aprovação';
    if (card.statusCodigo === 'AGUARDANDO_PECAS') return 'Aguardando peças';
    if (!card.responsavelId) return 'OS sem responsável';
    return null;
  }
  allowedTarget(card: PatioCard, column: PatioColumn): boolean {
    return this.canMove && ((card.statusCodigo === 'ABERTA' && column.codigo === 'DIAGNOSTICO') ||
      (card.statusCodigo === 'DIAGNOSTICO' && column.codigo === 'AGUARDANDO_APROVACAO'));
  }
  drop(event: CdkDragDrop<PatioColumn>): void {
    const card = event.item.data as PatioCard;
    if (card && this.allowedTarget(card, event.container.data)) this.move(card, event.container.data);
  }
  move(card: PatioCard, target: PatioColumn): void {
    if (!this.allowedTarget(card, target) || this.movingId !== null) return;
    this.movingId = card.osId;
    this.api.move(card, target.id).subscribe({
      next: () => {
        this.movingId = null;
        this.messages.add({ severity: 'success', summary: 'Etapa atualizada', detail: `OS ${card.numeroOS}` });
        this.channel?.postMessage({ osId: card.osId });
        this.reload();
      },
      error: error => {
        this.movingId = null;
        this.messages.add({ severity: 'warn', summary: 'Movimento recusado',
          detail: error.status === 409 ? 'A OS foi alterada por outra pessoa. O quadro será atualizado.' :
            'A transição não foi autorizada. Confira a etapa da OS.' });
        this.reload();
      },
    });
  }
  openDetail(card: PatioCard): void {
    if (!this.detailCard) this.returnFocusTo = document.activeElement instanceof HTMLElement ? document.activeElement : null;
    this.detailCard = card; this.detail = null; this.detailServices = [];
    this.detailLoading = true; this.detailError = false;
    this.api.myEmployee().subscribe({ next: id => this.myEmployeeId = id, error: () => this.myEmployeeId = null });
    if (this.canManageResources) this.api.resources().subscribe({ next: resources => this.resources = resources });
    this.os.getById(card.osId).subscribe({
      next: detail => { if (this.detailCard?.osId === card.osId) this.detail = detail; },
      error: () => { this.detailError = true; this.detailLoading = false; },
    });
    this.api.services(card.osId).subscribe({
      next: services => { if (this.detailCard?.osId === card.osId) {
        this.detailServices = services; this.detailLoading = false;
        services.forEach(item => { this.loadTimers(item.id); this.loadAdjustments(item.id); });
      } },
      error: () => { this.detailError = true; this.detailLoading = false; },
    });
  }
  private refreshDetailTimers(): void {
    const card = this.detailCard;
    if (!card) return;
    const scope = this.scopeVersion;
    this.api.services(card.osId).subscribe({ next: services => {
      if (scope !== this.scopeVersion || this.detailCard?.osId !== card.osId) return;
      this.detailServices = services;
      services.forEach(item => { this.loadTimers(item.id); this.loadAdjustments(item.id); });
    } });
  }
  loadTimers(itemId: number): void {
    const scope = this.scopeVersion;
    this.api.timers(itemId).subscribe({
      next: timers => {
        if (scope !== this.scopeVersion) return;
        this.timersByItem[itemId] = timers;
        const received = Date.now();
        timers.forEach(timer => this.timerReceivedAt[timer.id] = received);
      },
      error: () => { if (scope === this.scopeVersion) this.timersByItem[itemId] = []; },
    });
  }
  loadAdjustments(itemId: number): void {
    const scope = this.scopeVersion;
    this.api.adjustments(itemId).subscribe({
      next: adjustments => { if (scope === this.scopeVersion) this.adjustmentsByItem[itemId] = adjustments; },
      error: () => { if (scope === this.scopeVersion) this.adjustmentsByItem[itemId] = []; },
    });
  }
  adjustTime(item: PatioItem, timer: PatioTimer): void {
    if (!this.canAdjustTime || timer.estado !== 'FINALIZADO' || this.adjustingId !== null ||
        !this.adjustDelta || this.adjustReason.trim().length < 10) return;
    const reason = this.adjustReason.trim();
    const pending = this.pendingAdjustment;
    if (pending && (pending.itemId !== item.id || pending.timeId !== timer.id ||
        pending.delta !== this.adjustDelta || pending.reason !== reason)) {
      this.messages.add({ severity: 'warn', summary: 'Ajuste anterior pendente',
        detail: 'Reconcilie o ajuste anterior antes de registrar outro.' });
      return;
    }
    const key = pending?.key ?? crypto.randomUUID();
    this.pendingAdjustment = { itemId: item.id, timeId: timer.id, delta: this.adjustDelta, reason, key };
    this.adjustingId = timer.id;
    this.api.adjust(item, timer.id, this.adjustDelta, reason, key).subscribe({
      next: () => {
        this.pendingAdjustment = null;
        this.adjustingId = null;
        this.adjustDelta = null; this.adjustReason = '';
        this.messages.add({ severity: 'success', summary: 'Ajuste registrado' });
        this.loadAdjustments(item.id);
        if (this.detailCard) this.api.services(this.detailCard.osId).subscribe(services => this.detailServices = services);
        this.channel?.postMessage({ osId: this.detailCard?.osId });
        this.reload();
      },
      error: error => {
        this.adjustingId = null;
        const uncertain = error.status === 0 || error.status >= 500;
        if (!uncertain) this.pendingAdjustment = null;
        this.messages.add({ severity: 'warn', summary: uncertain ? 'Confirmação pendente' : 'Ajuste recusado',
          detail: uncertain ? 'Confirmação pendente: consulte o histórico e tente novamente com a mesma solicitação.' :
            error.status === 409 ? 'O serviço foi alterado. Recarregue e tente novamente.' :
            'Confira o motivo, o tempo e o apontamento finalizado.' });
        if (uncertain) this.loadAdjustments(item.id);
      },
    });
  }
  reservationsFor(resourceId: number): PatioReservation[] {
    return this.reservationBoard?.reservas.filter(reservation => reservation.recursoId === resourceId) ?? [];
  }
  occupiedCount(resourceId: number): number {
    return this.reservationsFor(resourceId).filter(reservation => reservation.estado === 'OCUPADO').length;
  }
  reservationAction(reservation: PatioReservation, action: 'occupy' | 'release' | 'cancel'): void {
    if (!this.canManageResources || this.reservationBusyId !== null) return;
    this.reservationBusyId = reservation.id;
    this.api.reservationAction(reservation, action).subscribe({
      next: () => {
        this.reservationBusyId = null;
        this.messages.add({ severity: 'success', summary: 'Recurso atualizado' });
        this.channel?.postMessage({ osId: reservation.osId });
        this.reload();
      },
      error: error => {
        this.reservationBusyId = null;
        this.messages.add({ severity: 'warn', summary: 'Operação recusada',
          detail: error.status === 409 ? 'Capacidade ou versão alterada. Atualize as reservas.' :
            'Confira o período, o estado da OS e a permissão.' });
        this.reload();
      },
    });
  }
  reserveForDetail(): void {
    if (!this.detailCard || !this.canManageResources || !this.selectedResourceId ||
        !this.reservationStart || !this.reservationEnd || this.reservationBusy) return;
    const resourceId = this.selectedResourceId;
    const osId = this.detailCard.osId;
    const pending = this.pendingReservation;
    if (pending && (pending.resourceId !== resourceId || pending.osId !== osId ||
        pending.start !== this.reservationStart || pending.end !== this.reservationEnd)) {
      this.messages.add({ severity: 'warn', summary: 'Reserva anterior pendente',
        detail: 'Reconcilie a reserva anterior antes de solicitar outra.' });
      return;
    }
    const key = pending?.key ?? crypto.randomUUID();
    this.pendingReservation = { resourceId, osId, start: this.reservationStart, end: this.reservationEnd, key };
    this.reservationBusy = true;
    this.api.reserve(resourceId, osId, this.reservationStart, this.reservationEnd, key).subscribe({
      next: () => {
        this.pendingReservation = null;
        this.reservationBusy = false;
        this.messages.add({ severity: 'success', summary: 'Recurso reservado' });
        this.channel?.postMessage({ osId: this.detailCard?.osId });
        this.reload();
      },
      error: error => {
        this.reservationBusy = false;
        const uncertain = error.status === 0 || error.status >= 500;
        if (!uncertain) this.pendingReservation = null;
        this.messages.add({ severity: 'warn', summary: uncertain ? 'Confirmação pendente' : 'Reserva recusada',
          detail: uncertain ? 'Confirmação pendente: confira as reservas e tente novamente com a mesma solicitação.' :
            error.status === 409 ? 'Capacidade esgotada ou agendamento legado incerto. Confira na Agenda.' :
            'Confira o recurso, a OS e o horário informado.' });
        if (uncertain) this.reload();
      },
    });
  }
  activeTimer(itemId: number): PatioTimer | undefined {
    return this.timersByItem[itemId]?.find(timer => timer.funcionarioId === this.myEmployeeId && timer.estado !== 'FINALIZADO');
  }
  seconds(timer: PatioTimer): number {
    return timer.segundosAtivos + (timer.estado === 'EM_EXECUCAO' ?
      Math.max(0, Math.floor((this.displayNow - (this.timerReceivedAt[timer.id] ?? this.displayNow)) / 1000)) : 0);
  }
  timerAction(item: PatioItem, action: 'start' | 'pause' | 'resume' | 'finish'): void {
    if (this.timerBusyItem !== null || item.mecanicoExecutorId !== this.myEmployeeId) return;
    const command = `${item.id}:${action}`;
    const key = this.pendingTimerKeys.get(command) ?? crypto.randomUUID();
    this.pendingTimerKeys.set(command, key);
    this.timerBusyItem = item.id;
    this.api.timerCommand(item.id, action, key).subscribe({
      next: () => {
        this.pendingTimerKeys.delete(command);
        this.timerBusyItem = null;
        this.messages.add({ severity: 'success', summary: 'Apontamento confirmado' });
        this.loadTimers(item.id);
        if (this.detailCard) this.api.services(this.detailCard.osId).subscribe(services => this.detailServices = services);
        this.channel?.postMessage({ osId: this.detailCard?.osId });
        this.reload();
      },
      error: error => {
        this.timerBusyItem = null;
        const uncertain = error.status === 0 || error.status >= 500;
        if (!uncertain) this.pendingTimerKeys.delete(command);
        this.messages.add({ severity: 'warn', summary: uncertain ? 'Confirmação pendente' : 'Apontamento recusado',
          detail: uncertain ? 'Confirmação pendente: confira o apontamento e tente novamente com a mesma ação.' :
            error.status === 409 ? 'Outro operador alterou este serviço. Os dados serão atualizados.' :
            'Verifique a atribuição, a aprovação e a etapa da OS.' });
        this.loadTimers(item.id);
      },
    });
  }
  assign(item: PatioItem, funcionarioId: number): void {
    if (!this.canAssign || !Number.isSafeInteger(funcionarioId) || this.assigningId !== null) return;
    this.assigningId = item.id;
    this.api.assign(item, funcionarioId).subscribe({
      next: () => {
        this.assigningId = null;
        this.messages.add({ severity: 'success', summary: 'Responsável atualizado' });
        if (this.detailCard) this.api.services(this.detailCard.osId).subscribe(services => this.detailServices = services);
        this.channel?.postMessage({ osId: this.detailCard?.osId });
        this.reload();
      },
      error: error => {
        this.assigningId = null;
        this.messages.add({ severity: 'warn', summary: 'Atribuição recusada',
          detail: error.status === 409 ? 'O serviço mudou. Recarregue o detalhe.' : 'Confira o funcionário e tente novamente.' });
        if (this.detailCard) this.api.services(this.detailCard.osId).subscribe(services => this.detailServices = services);
      },
    });
  }
  closeDetail(): void {
    this.detailCard = null; this.detail = null;
    const focusTarget = this.returnFocusTo;
    this.returnFocusTo = null;
    requestAnimationFrame(() => { if (focusTarget?.isConnected) focusTarget.focus(); });
  }
  expand(): void {
    this.expanded = true;
    void this.router.navigate([], { relativeTo: this.route,
      queryParams: { layout: 'expanded' }, queryParamsHandling: 'merge', replaceUrl: true });
  }
  restore(): void {
    if (document.fullscreenElement === this.pageRoot?.nativeElement) void document.exitFullscreen();
    this.expanded = false;
    void this.router.navigate([], { relativeTo: this.route,
      queryParams: { layout: null }, queryParamsHandling: 'merge', replaceUrl: true });
  }
  async fullscreen(): Promise<void> {
    const element = this.pageRoot?.nativeElement;
    if (!element?.requestFullscreen) { this.expand(); return; }
    try { await element.requestFullscreen(); }
    catch {
      this.expand();
      this.messages.add({ severity: 'warn', summary: 'Tela cheia indisponível',
        detail: 'A visualização expandida permanece disponível.' });
    }
  }
}
