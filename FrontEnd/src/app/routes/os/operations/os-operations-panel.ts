import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnChanges,
  SimpleChanges,
  inject,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '@core';
import { NeriTechIcon } from '../../../shared/components';
import { ItemOSProdutoResponse, ItemOSServicoResponse } from '../models/os.models';
import {
  DIAGNOSTIC_URGENCY_OPTIONS,
  OsChecklistItem,
  OsChecklistModel,
  OsDiagnosticRequest,
  OsDiagnosticResponse,
  OsDiagnosticUrgency,
  OsOperationsTab,
  OsPhotoEvidence,
  OsSectionState,
  OsVehicleSystem,
  VEHICLE_SYSTEM_OPTIONS,
} from './os-operations.models';
import { OsOperationsService } from './os-operations.service';

interface DeleteTarget {
  kind: 'product' | 'service' | 'diagnostic' | 'evidence';
  id: number;
  label: string;
}

interface DiagnosticDraft {
  sistemaVeiculo: OsVehicleSystem | '';
  componenteEspecifico: string;
  problemaIdentificado: string;
  causaProvavel: string;
  solucaoRecomendada: string;
  urgencia: OsDiagnosticUrgency | '';
  impactoSeguranca: boolean;
  impactoDirigibilidade: boolean;
  testesRealizados: string;
  evidenciasEncontradas: string;
  custoEstimado?: number | null;
  tempoEstimadoReparo?: number | null;
  observacoes: string;
}

@Component({
  selector: 'os-operations-panel',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule, NeriTechIcon],
  templateUrl: './os-operations-panel.html',
  styleUrl: './os-operations-panel.scss',
})
export class OsOperationsPanel implements OnChanges {
  private readonly service = inject(OsOperationsService);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  @Input({ required: true }) osId!: number;
  @Input() initialTab: OsOperationsTab = 'scope';

  readonly urgencyOptions = DIAGNOSTIC_URGENCY_OPTIONS;
  readonly vehicleSystemOptions = VEHICLE_SYSTEM_OPTIONS;

  activeTab: OsOperationsTab = 'scope';
  products: ItemOSProdutoResponse[] = [];
  services: ItemOSServicoResponse[] = [];
  diagnostics: OsDiagnosticResponse[] = [];
  checklist: OsChecklistItem[] = [];
  checklistModels: OsChecklistModel[] = [];
  selectedChecklistModelId?: number;
  evidence: OsPhotoEvidence[] = [];

  scopeState: OsSectionState = 'idle';
  diagnosticsState: OsSectionState = 'idle';
  checklistState: OsSectionState = 'idle';
  evidenceState: OsSectionState = 'idle';

  scopeMessage = '';
  diagnosticsMessage = '';
  checklistMessage = '';
  evidenceMessage = '';
  actionMessage = '';
  busyKey?: string;
  deleteTarget?: DeleteTarget;

  diagnosticFormOpen = false;
  diagnosticDraft = this.emptyDiagnosticDraft();

  selectedEvidenceFile?: File;
  evidenceDescription = '';

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['initialTab'] && this.initialTab) {
      this.activeTab = this.initialTab;
    }
    if (changes['osId'] && Number.isInteger(this.osId) && this.osId > 0) {
      this.activeTab = this.initialTab || 'scope';
      this.loadAll();
    }
  }

  get canManageItems(): boolean {
    return this.hasPermission('OS_INC_ITENS');
  }

  get canEditOrder(): boolean {
    return this.hasPermission('OS_EDITAR');
  }

  get canViewDiagnostics(): boolean {
    return this.hasPermission('OS_VIS_SOLICITACOES');
  }

  get canDeleteDiagnostics(): boolean {
    return this.hasPermission('OS_EXC_SOLICITACOES');
  }

  get canViewChecklist(): boolean {
    return this.hasPermission('OS_VIS_CHECKLIST');
  }

  get canAddChecklist(): boolean {
    return this.hasPermission('OS_ADC_CHECKLIST');
  }

  get canEditChecklist(): boolean {
    return this.hasPermission('OS_EDIT_CHECKLIST');
  }

  get canUploadEvidence(): boolean {
    return this.hasPermission('OS_ENV_FOTOS');
  }

  get checklistDone(): number {
    return this.checklist.filter(item => item.feito).length;
  }

  get checklistProgress(): number {
    if (!this.checklist.length) return 0;
    return Math.round((this.checklistDone / this.checklist.length) * 100);
  }

  get availableChecklistModels(): OsChecklistModel[] {
    const appliedIds = new Set(
      this.checklist
        .map(item => item.checklistModeloId)
        .filter((id): id is number => typeof id === 'number')
    );
    return this.checklistModels.filter(model => !appliedIds.has(model.id));
  }

  setTab(tab: OsOperationsTab): void {
    this.activeTab = tab;
    this.actionMessage = '';
    this.deleteTarget = undefined;
  }

  loadAll(): void {
    this.actionMessage = '';
    this.loadScope();
    this.loadDiagnostics();
    this.loadChecklist();
    this.loadEvidence();
  }

  loadScope(): void {
    this.scopeState = 'loading';
    this.scopeMessage = '';
    this.service.listProducts(this.osId).subscribe({
      next: products => {
        this.products = products ?? [];
        this.loadServicesAfterProducts();
      },
      error: error => this.resolveScopeError(error),
    });
  }

  private loadServicesAfterProducts(): void {
    this.service.listServices(this.osId).subscribe({
      next: services => {
        this.services = services ?? [];
        this.scopeState = 'ready';
        this.cdr.markForCheck();
      },
      error: error => this.resolveScopeError(error),
    });
  }

  loadDiagnostics(): void {
    if (!this.canViewDiagnostics) {
      this.diagnosticsState = 'forbidden';
      this.diagnosticsMessage = 'Seu perfil não possui permissão para visualizar solicitações e diagnósticos.';
      this.diagnostics = [];
      return;
    }

    this.diagnosticsState = 'loading';
    this.diagnosticsMessage = '';
    this.service.listDiagnostics(this.osId).subscribe({
      next: diagnostics => {
        this.diagnostics = diagnostics ?? [];
        this.diagnosticsState = 'ready';
        this.cdr.markForCheck();
      },
      error: error => {
        this.diagnosticsState = error?.status === 403 ? 'forbidden' : 'error';
        this.diagnosticsMessage = error?.status === 403
          ? 'Seu perfil não possui permissão para visualizar solicitações e diagnósticos.'
          : 'Não foi possível carregar os diagnósticos desta Ordem de Serviço.';
        this.cdr.markForCheck();
      },
    });
  }

  loadChecklist(): void {
    if (!this.canViewChecklist) {
      this.checklistState = 'forbidden';
      this.checklistMessage = 'Seu perfil não possui permissão para visualizar o checklist da OS.';
      this.checklist = [];
      return;
    }

    this.checklistState = 'loading';
    this.checklistMessage = '';
    this.service.listChecklist(this.osId).subscribe({
      next: checklist => {
        this.checklist = this.sortChecklist(checklist ?? []);
        this.checklistState = 'ready';
        if (this.canAddChecklist) this.loadChecklistModels();
        this.cdr.markForCheck();
      },
      error: error => {
        this.checklistState = error?.status === 403 ? 'forbidden' : 'error';
        this.checklistMessage = error?.status === 403
          ? 'Seu perfil não possui permissão para visualizar o checklist da OS.'
          : 'Não foi possível carregar o checklist desta Ordem de Serviço.';
        this.cdr.markForCheck();
      },
    });
  }

  private loadChecklistModels(): void {
    this.service.listChecklistModels().subscribe({
      next: page => {
        this.checklistModels = page?.content ?? [];
        if (this.selectedChecklistModelId && !this.availableChecklistModels.some(model => model.id === this.selectedChecklistModelId)) {
          this.selectedChecklistModelId = undefined;
        }
        this.cdr.markForCheck();
      },
      error: error => {
        this.checklistModels = [];
        if (error?.status !== 403) {
          this.actionMessage = 'O catálogo de checklists está temporariamente indisponível.';
        }
        this.cdr.markForCheck();
      },
    });
  }

  applyChecklist(): void {
    const checklistId = Number(this.selectedChecklistModelId);
    if (!this.canAddChecklist || !Number.isInteger(checklistId) || checklistId <= 0 || this.busyKey) return;

    this.busyKey = 'apply-checklist';
    this.actionMessage = '';
    this.service.applyChecklist(this.osId, checklistId).subscribe({
      next: checklist => {
        this.checklist = this.sortChecklist(checklist ?? []);
        this.selectedChecklistModelId = undefined;
        this.busyKey = undefined;
        this.cdr.markForCheck();
      },
      error: error => {
        this.busyKey = undefined;
        this.actionMessage = this.extractError(error, 'Não foi possível aplicar o checklist à Ordem de Serviço.');
        this.cdr.markForCheck();
      },
    });
  }

  loadEvidence(): void {
    this.evidenceState = 'loading';
    this.evidenceMessage = '';
    this.service.listEvidence(this.osId).subscribe({
      next: evidence => {
        this.evidence = evidence ?? [];
        this.evidenceState = 'ready';
        this.cdr.markForCheck();
      },
      error: error => {
        this.evidenceState = error?.status === 403 ? 'forbidden' : 'error';
        this.evidenceMessage = error?.status === 403
          ? 'Seu perfil não possui permissão para visualizar as evidências desta OS.'
          : 'Não foi possível carregar as evidências desta Ordem de Serviço.';
        this.cdr.markForCheck();
      },
    });
  }

  editScope(): void {
    if (!this.canEditOrder) return;
    void this.router.navigate(['/ordens-servico/cadastro', this.osId], {
      state: { returnUrl: `/ordens-servico/${this.osId}` },
    });
  }

  openDiagnosticForm(): void {
    if (!this.canEditOrder) return;
    this.diagnosticDraft = this.emptyDiagnosticDraft();
    this.diagnosticFormOpen = true;
    this.actionMessage = '';
  }

  closeDiagnosticForm(): void {
    this.diagnosticFormOpen = false;
    this.diagnosticDraft = this.emptyDiagnosticDraft();
  }

  submitDiagnostic(): void {
    const problem = this.diagnosticDraft.problemaIdentificado.trim();
    if (!problem || this.busyKey) return;

    const request: OsDiagnosticRequest = {
      ordemServicoId: this.osId,
      sistemaVeiculo: this.diagnosticDraft.sistemaVeiculo || null,
      componenteEspecifico: this.clean(this.diagnosticDraft.componenteEspecifico),
      problemaIdentificado: problem,
      causaProvavel: this.clean(this.diagnosticDraft.causaProvavel),
      solucaoRecomendada: this.clean(this.diagnosticDraft.solucaoRecomendada),
      urgencia: this.diagnosticDraft.urgencia || null,
      impactoSeguranca: this.diagnosticDraft.impactoSeguranca,
      impactoDirigibilidade: this.diagnosticDraft.impactoDirigibilidade,
      custoEstimado: this.diagnosticDraft.custoEstimado ?? null,
      tempoEstimadoReparo: this.diagnosticDraft.tempoEstimadoReparo ?? null,
      testesRealizados: this.clean(this.diagnosticDraft.testesRealizados),
      evidenciasEncontradas: this.clean(this.diagnosticDraft.evidenciasEncontradas),
      observacoes: this.clean(this.diagnosticDraft.observacoes),
    };

    this.busyKey = 'create-diagnostic';
    this.actionMessage = '';
    this.service.createDiagnostic(request).subscribe({
      next: created => {
        this.diagnostics = [created, ...this.diagnostics];
        this.diagnosticsState = 'ready';
        this.busyKey = undefined;
        this.closeDiagnosticForm();
        this.cdr.markForCheck();
      },
      error: error => {
        this.busyKey = undefined;
        this.actionMessage = this.extractError(error, 'Não foi possível registrar o diagnóstico.');
        this.cdr.markForCheck();
      },
    });
  }

  toggleChecklist(item: OsChecklistItem): void {
    if (!this.canEditChecklist || this.busyKey) return;
    const key = `checklist-${item.id}`;
    this.busyKey = key;
    this.actionMessage = '';
    this.service.updateChecklistItem(item, !item.feito).subscribe({
      next: updated => {
        this.checklist = this.checklist.map(current => current.id === updated.id ? updated : current);
        this.busyKey = undefined;
        this.cdr.markForCheck();
      },
      error: error => {
        this.busyKey = undefined;
        this.actionMessage = this.extractError(error, 'Não foi possível atualizar o item do checklist.');
        this.cdr.markForCheck();
      },
    });
  }

  onEvidenceFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedEvidenceFile = input.files?.[0];
    this.actionMessage = '';
  }

  uploadEvidence(): void {
    if (!this.canUploadEvidence || !this.selectedEvidenceFile || this.busyKey) return;
    this.busyKey = 'upload-evidence';
    this.actionMessage = '';
    this.service.uploadEvidence(this.osId, this.selectedEvidenceFile, this.evidenceDescription).subscribe({
      next: created => {
        this.evidence = [...this.evidence, created];
        this.selectedEvidenceFile = undefined;
        this.evidenceDescription = '';
        this.busyKey = undefined;
        this.cdr.markForCheck();
      },
      error: error => {
        this.busyKey = undefined;
        this.actionMessage = this.extractError(error, 'Não foi possível enviar a evidência.');
        this.cdr.markForCheck();
      },
    });
  }

  downloadEvidence(photo: OsPhotoEvidence): void {
    if (this.busyKey) return;
    this.busyKey = `download-${photo.id}`;
    this.actionMessage = '';
    this.service.downloadEvidence(photo.id).subscribe({
      next: blob => {
        const url = URL.createObjectURL(blob);
        const anchor = document.createElement('a');
        anchor.href = url;
        anchor.download = this.evidenceFileName(photo);
        anchor.click();
        URL.revokeObjectURL(url);
        this.busyKey = undefined;
        this.cdr.markForCheck();
      },
      error: error => {
        this.busyKey = undefined;
        this.actionMessage = this.extractError(error, 'Não foi possível baixar a evidência.');
        this.cdr.markForCheck();
      },
    });
  }

  requestDelete(kind: DeleteTarget['kind'], id: number, label: string): void {
    this.deleteTarget = { kind, id, label };
    this.actionMessage = '';
  }

  cancelDelete(): void {
    this.deleteTarget = undefined;
  }

  confirmDelete(): void {
    const target = this.deleteTarget;
    if (!target || this.busyKey) return;

    const allowed = target.kind === 'diagnostic'
      ? this.canDeleteDiagnostics
      : target.kind === 'evidence'
        ? this.canUploadEvidence
        : this.canManageItems;
    if (!allowed) return;

    this.busyKey = `delete-${target.kind}-${target.id}`;
    this.actionMessage = '';

    const request = target.kind === 'product'
      ? this.service.deleteProduct(target.id)
      : target.kind === 'service'
        ? this.service.deleteService(target.id)
        : target.kind === 'diagnostic'
          ? this.service.deleteDiagnostic(target.id)
          : this.service.deleteEvidence(target.id);

    request.subscribe({
      next: () => {
        if (target.kind === 'product') this.products = this.products.filter(item => item.id !== target.id);
        if (target.kind === 'service') this.services = this.services.filter(item => item.id !== target.id);
        if (target.kind === 'diagnostic') this.diagnostics = this.diagnostics.filter(item => item.id !== target.id);
        if (target.kind === 'evidence') this.evidence = this.evidence.filter(item => item.id !== target.id);
        this.busyKey = undefined;
        this.deleteTarget = undefined;
        this.cdr.markForCheck();
      },
      error: error => {
        this.busyKey = undefined;
        this.actionMessage = this.extractError(error, 'Não foi possível excluir o registro selecionado.');
        this.cdr.markForCheck();
      },
    });
  }

  urgencyLabel(value?: OsDiagnosticUrgency | null): string {
    return this.urgencyOptions.find(option => option.value === value)?.label ?? 'Não informada';
  }

  systemLabel(value?: OsVehicleSystem | null): string {
    return this.vehicleSystemOptions.find(option => option.value === value)?.label ?? 'Sistema não informado';
  }

  urgencyTone(value?: OsDiagnosticUrgency | null): string {
    if (value === 'CRITICA') return 'danger';
    if (value === 'ALTA') return 'warning';
    if (value === 'MEDIA') return 'info';
    return 'neutral';
  }

  serviceStatusLabel(value?: string | null): string {
    const labels: Record<string, string> = {
      PENDENTE: 'Pendente',
      NAO_INICIADO: 'Não iniciado',
      PRONTO: 'Pronto',
      EM_EXECUCAO: 'Em execução',
      PAUSADO: 'Pausado',
      CONCLUIDO: 'Concluído',
      CANCELADO: 'Cancelado',
    };
    return value ? labels[value] ?? value : 'Não iniciado';
  }

  fileSize(bytes?: number | null): string {
    if (!bytes || bytes <= 0) return 'Tamanho não informado';
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
  }

  evidenceFileName(photo: OsPhotoEvidence): string {
    const extension = photo.contentType?.split('/')[1]?.replace('jpeg', 'jpg') ?? 'arquivo';
    return `os-${this.osId}-evidencia-${photo.id}.${extension}`;
  }

  trackById(_: number, item: { id: number }): number {
    return item.id;
  }

  private sortChecklist(items: OsChecklistItem[]): OsChecklistItem[] {
    return [...items].sort((a, b) => (a.ordem ?? 0) - (b.ordem ?? 0));
  }

  private resolveScopeError(error: any): void {
    this.scopeState = error?.status === 403 ? 'forbidden' : 'error';
    this.scopeMessage = error?.status === 403
      ? 'Seu perfil não possui permissão para visualizar os itens desta Ordem de Serviço.'
      : 'Não foi possível carregar produtos e serviços desta Ordem de Serviço.';
    this.cdr.markForCheck();
  }

  private hasPermission(permission: string): boolean {
    const user = this.auth.snapshot();
    const roles = user.roles ?? [];
    return user.permissions?.includes(permission) === true
      || roles.some(role => role === 'ADMIN' || role === 'ROLE_ADMIN');
  }

  private clean(value?: string | null): string | null {
    const normalized = value?.trim();
    return normalized ? normalized : null;
  }

  private extractError(error: any, fallback: string): string {
    return error?.error?.message || error?.message || fallback;
  }

  private emptyDiagnosticDraft(): DiagnosticDraft {
    return {
      sistemaVeiculo: '',
      componenteEspecifico: '',
      problemaIdentificado: '',
      causaProvavel: '',
      solucaoRecomendada: '',
      urgencia: 'MEDIA',
      impactoSeguranca: false,
      impactoDirigibilidade: false,
      testesRealizados: '',
      evidenciasEncontradas: '',
      custoEstimado: null,
      tempoEstimadoReparo: null,
      observacoes: '',
    };
  }
}
