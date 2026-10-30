import { Component, OnInit, inject, OnDestroy, DestroyRef, HostListener } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { environment } from '@env/environment';
import { CommonModule } from '@angular/common';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { NotificacaoService, NotificacaoSistemaResponse } from '@core/services/notificacao.service';
import { AuthService } from '@core';
import { Subscription, interval } from 'rxjs';

@Component({
  selector: 'app-notification',
  templateUrl: './notification-button.html',
  styleUrl: './notification-button.scss',
  standalone: true,
  imports: [CommonModule, MatBadgeModule, MatButtonModule, MatMenuModule],
})
export class NotificationButton implements OnInit, OnDestroy {
  private readonly notificationService = inject(NotificacaoService);
  private readonly auth = inject(AuthService);
  private readonly destroyRef = inject(DestroyRef);
  private pollingSub?: Subscription;
  private fetchSub?: Subscription;

  readonly uxPreview = environment.uxPreview;
  readonly pendingIds = new Set<number>();
  notifications: NotificacaoSistemaResponse[] = [];
  unreadCount = 0;
  loading = false;
  errorMessage = '';
  actionError = '';
  usuarioId?: number | string | null;
  panelOpen = false;
  settingsOpen = false;
  exampleVisible = false;
  showHistory = false;
  category: 'system' | 'schedule' = 'system';
  soundEnabled = true;
  balloonEnabled = true;
  browserEnabled = false;
  permissionMessage = '';

  get filteredNotifications(): NotificacaoSistemaResponse[] {
    return this.notifications.filter(item => {
      const schedule = /AGEND|SOLICIT/i.test(item.categoria ?? '');
      return (this.category === 'schedule' ? schedule : !schedule) && (this.showHistory || !item.lida);
    });
  }

  togglePanel(): void {
    this.panelOpen = !this.panelOpen;
    if (this.panelOpen) this.loadNotifications();
    else this.settingsOpen = false;
  }
  closePanel(): void { this.panelOpen = false; this.settingsOpen = false; this.exampleVisible = false; }
  toggleHistory(): void { this.showHistory = !this.showHistory; this.loadNotifications(); }

  @HostListener('document:keydown', ['$event'])
  onKeyboard(event: KeyboardEvent): void {
    const el = event.target as HTMLElement | null;
    const editing = el?.isContentEditable || ['INPUT','TEXTAREA','SELECT'].includes(el?.tagName ?? '');
    if (event.key === 'Escape' && (this.settingsOpen || this.panelOpen)) {
      event.preventDefault();
      if (this.settingsOpen) this.settingsOpen = false;
      else this.closePanel();
      return;
    }
    if (!editing && event.shiftKey && !event.ctrlKey && !event.metaKey && event.key.toLowerCase() === 'n') {
      event.preventDefault(); this.togglePanel();
    }
  }

  private prefKey(): string { return `nt-notifications-${this.usuarioId ?? 'guest'}`; }
  private restorePreferences(): void {
    this.soundEnabled = true; this.balloonEnabled = true; this.browserEnabled = false;
    if (!this.usuarioId || typeof window === 'undefined') return;
    try {
      const saved = JSON.parse(window.localStorage.getItem(this.prefKey()) ?? '{}');
      if (typeof saved.sound === 'boolean') this.soundEnabled = saved.sound;
      if (typeof saved.balloon === 'boolean') this.balloonEnabled = saved.balloon;
      this.browserEnabled = saved.browser === true && 'Notification' in window && Notification.permission === 'granted';
    } catch { /* Navegação privada ou armazenamento bloqueado. */ }
  }
  private savePreferences(): void {
    if (!this.usuarioId || typeof window === 'undefined') return;
    try {
      window.localStorage.setItem(this.prefKey(), JSON.stringify({sound:this.soundEnabled,balloon:this.balloonEnabled,browser:this.browserEnabled}));
    } catch { this.permissionMessage = 'As preferências não puderam ser salvas neste navegador.'; }
  }
  setPreference(key: 'sound' | 'balloon', enabled: boolean): void {
    if (key === 'sound') this.soundEnabled = enabled;
    else this.balloonEnabled = enabled;
    this.savePreferences();
  }
  showExample(): void { if (this.balloonEnabled) this.exampleVisible = true; }
  playSample(): void {
    if (!this.soundEnabled || typeof window === 'undefined') return;
    try {
      const AudioContextClass = window.AudioContext;
      if (!AudioContextClass) { this.permissionMessage = 'Este navegador não permite reproduzir áudio.'; return; }
      const context = new AudioContextClass();
      const oscillator = context.createOscillator(); const gain = context.createGain();
      oscillator.type = 'sine'; oscillator.frequency.value = 740;
      gain.gain.setValueAtTime(0.09, context.currentTime);
      gain.gain.exponentialRampToValueAtTime(0.001, context.currentTime + 0.18);
      oscillator.connect(gain); gain.connect(context.destination);
      oscillator.start(); oscillator.stop(context.currentTime + 0.18);
      oscillator.onended = () => { void context.close(); };
    } catch { this.permissionMessage = 'Não foi possível reproduzir o som neste navegador.'; }
  }
  async requestBrowserNotifications(enabled: boolean): Promise<void> {
    this.permissionMessage = '';
    if (!enabled) { this.browserEnabled = false; this.savePreferences(); return; }
    if (typeof window === 'undefined' || !('Notification' in window)) {
      this.browserEnabled = false;
      this.permissionMessage = 'Notificações do navegador indisponíveis neste ambiente.'; return;
    }
    try {
      const permission = await Notification.requestPermission();
      this.browserEnabled = permission === 'granted';
      if (!this.browserEnabled) this.permissionMessage = 'Permissão não concedida. Altere a permissão nas configurações do navegador.';
      this.savePreferences();
    } catch { this.browserEnabled = false; this.permissionMessage = 'O navegador não conseguiu solicitar a permissão.'; }
  }


  ngOnInit(): void {
    this.auth.user().pipe(takeUntilDestroyed(this.destroyRef)).subscribe(user => {
      const nextId = user?.id ?? null;
      if (this.usuarioId === nextId) return;
      this.pollingSub?.unsubscribe();
      this.fetchSub?.unsubscribe();
      this.usuarioId = nextId;
      this.notifications = [];
      this.unreadCount = 0;
      this.pendingIds.clear();
      this.errorMessage = '';
      this.actionError = '';
      this.loading = false;
      this.panelOpen = false;
      this.settingsOpen = false;
      this.showHistory = false;
      this.restorePreferences();
      if (nextId && !this.uxPreview) {
        this.loadNotifications();
        this.pollingSub = interval(120000)
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe(() => this.loadNotifications(false));
      }
    });
  }

  ngOnDestroy(): void {
    this.pollingSub?.unsubscribe();
    this.fetchSub?.unsubscribe();
  }

  loadNotifications(showLoading = true): void {
    if (this.uxPreview || !this.usuarioId) return;
    this.fetchSub?.unsubscribe();
    this.loading = showLoading;
    this.errorMessage = '';
    const currentId = this.usuarioId;
    this.fetchSub = this.notificationService.findMine(0, this.showHistory ? 50 : 20)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: res => {
          if (currentId !== this.usuarioId) return;
          this.notifications = res?.content ?? [];
          this.unreadCount = this.notifications.filter(n => !n.lida).length;
          this.loading = false;
        },
        error: () => {
          if (currentId !== this.usuarioId) return;
          this.loading = false;
          this.errorMessage = 'Não foi possível carregar as notificações. Tente atualizar.';
        },
      });
  }

  markAsRead(item: NotificacaoSistemaResponse): void {
    if (this.uxPreview || !this.usuarioId || item.lida || this.pendingIds.has(item.id)) return;
    this.actionError = '';
    const currentId = this.usuarioId;
    this.pendingIds.add(item.id);
    this.notificationService.marcarComoLida(item.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: response => {
          this.pendingIds.delete(item.id);
          if (currentId !== this.usuarioId) return;
          const saved = this.notifications.find(n => n.id === item.id);
          if (saved && response?.lida === true) {
            saved.lida = true;
            saved.dataLeitura = response.dataLeitura;
            this.unreadCount = this.notifications.filter(n => !n.lida).length;
          } else {
            this.actionError = 'A leitura não foi confirmada pelo servidor. Tente novamente.';
          }
        },
        error: () => {
          this.pendingIds.delete(item.id);
          if (currentId === this.usuarioId) {
            this.actionError = 'Não foi possível marcar como lida. Tente novamente.';
          }
        },
      });
  }

  markAllAsRead(): void {
    this.notifications.filter(n => !n.lida).forEach(n => this.markAsRead(n));
  }

  getIconColor(type: string): string {
    switch (type) {
      case 'SUCESSO': return 'text-emerald-500';
      case 'AVISO': return 'text-amber-500';
      case 'ERRO': return 'text-rose-500';
      default: return 'text-indigo-500';
    }
  }

  getIconBg(type: string): string {
    switch (type) {
      case 'SUCESSO': return 'bg-emerald-50';
      case 'AVISO': return 'bg-amber-50';
      case 'ERRO': return 'bg-rose-50';
      default: return 'bg-indigo-50';
    }
  }

  formatTime(dateStr: string): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    if (Number.isNaN(date.getTime())) return '';
    const diffMins = Math.max(0, Math.floor((Date.now() - date.getTime()) / 60000));
    if (diffMins < 1) return 'Agora';
    if (diffMins < 60) return `${diffMins}m`;
    const diffHours = Math.floor(diffMins / 60);
    if (diffHours < 24) return `${diffHours}h`;
    return date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' });
  }
}
