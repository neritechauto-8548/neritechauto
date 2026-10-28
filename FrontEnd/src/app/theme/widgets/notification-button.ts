import { Component, OnInit, inject, OnDestroy, DestroyRef } from '@angular/core';
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
  template: `
    <button mat-icon-button [matMenuTriggerFor]="menu" (click)="loadNotifications()" aria-label="Abrir notificações"
      class="!text-inherit hover:!bg-black/10 dark:hover:!bg-white/10 !transition-colors !duration-200">
      <span class="nt-notification-icon" [attr.data-count]="unreadCount" [class.has-count]="unreadCount > 0" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M18 9a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9M10 21h4"></path></svg></span>
    </button>

    <mat-menu #menu="matMenu" backdropClass="stripe-menu-backdrop" class="!rounded-2xl !border !border-slate-200 !shadow-[0_20px_50px_rgba(0,0,0,0.1)] !py-0 !overflow-hidden">
      <div class="min-w-[320px] max-w-[360px] flex flex-col bg-white" (click)="$event.stopPropagation()">
        
        <!-- Header -->
        <div class="px-4 py-3.5 bg-slate-50/50 border-b border-slate-100 flex items-center justify-between">
          <div class="flex items-center gap-2">
            <span class="text-sm font-bold text-slate-900">Notificações</span>
            @if (unreadCount > 0) {
              <span class="px-1.5 py-0.5 rounded-full bg-indigo-100 text-indigo-600 text-[10px] font-bold">{{ unreadCount }} novas</span>
            }
          </div>
          @if (!uxPreview && unreadCount > 0) {
            <button type="button" (click)="markAllAsRead()" [disabled]="loading" class="text-[11px] font-bold text-indigo-600 hover:text-indigo-700 transition-colors disabled:opacity-50">Marcar todas como lidas</button>
          }
        </div>

        <!-- Notification List -->
        @if (actionError) {
          <div class="px-4 py-2 text-xs text-rose-700 bg-rose-50" role="alert">{{ actionError }}</div>
        }
        <div class="max-h-[350px] overflow-y-auto custom-scrollbar">
          @if (uxPreview) {
            <div class="p-5 text-sm text-slate-600" role="status">Prévia visual — notificações reais indisponíveis sem autenticação e backend. Nenhuma ação será registrada.</div>
          } @else if (!usuarioId) {
            <div class="p-5 text-sm text-slate-600" role="status">Entre na sua conta para consultar notificações.</div>
          } @else if (loading) {
            <div class="py-12 flex flex-col items-center justify-center gap-2">
               <div class="w-5 h-5 border-2 border-indigo-500 border-t-transparent rounded-full animate-spin"></div>
               <span class="text-[11px] text-slate-400 font-medium">Carregando...</span>
            </div>
          } @else if (errorMessage) {
            <div class="p-5 text-sm text-rose-700" role="alert">{{ errorMessage }}</div>
          } @else if (notifications.length > 0) {
            @for (item of notifications; track item.id) {
              <button type="button" (click)="markAsRead(item)"
                [disabled]="item.lida || pendingIds.has(item.id)" 
                [attr.aria-label]="(item.lida ? 'Notificação lida: ' : 'Marcar notificação como lida: ') + item.titulo"
                [class]="!item.lida ? 'bg-slate-50/40' : 'hover:bg-slate-50/60'"
                class="w-full text-left px-4 py-3 border-0 border-b border-slate-100 last:border-0 flex gap-3 cursor-pointer transition-all relative group focus-visible:outline-2 focus-visible:outline-indigo-600 disabled:cursor-default">
                
                <!-- Status Dot -->
                @if (!item.lida) {
                  <span class="absolute left-1.5 top-1/2 -translate-y-1/2 w-1.5 h-1.5 bg-indigo-600 rounded-full shadow-[0_0_8px_rgba(79,70,229,0.5)]"></span>
                }

                <!-- Icon Container -->
                <div [class]="getIconBg(item.tipoNotificacao)" 
                  class="w-9 h-9 rounded-xl flex items-center justify-center shrink-0 shadow-sm border border-black/5">
                  <svg class="nt-inline-icon" [class]="getIconColor(item.tipoNotificacao)" viewBox="0 0 24 24" aria-hidden="true"><path d="M12 4v16M4 12h16"></path></svg>
                </div>

                <!-- Content -->
                <div class="flex flex-col gap-0.5 overflow-hidden">
                  <div class="flex items-center justify-between gap-2">
                    <span [class] ="!item.lida ? 'font-bold text-slate-800' : 'font-medium text-slate-600'" 
                      class="text-[13px] leading-tight truncate">{{ item.titulo }}</span>
                    <span class="text-[9px] text-slate-400 font-medium shrink-0">{{ formatTime(item.dataCadastro) }}</span>
                  </div>
                  <span class="text-[11.5px] text-slate-500 leading-snug line-clamp-2">{{ item.mensagem }}</span>
                </div>
              </button>
            }
          } @else {
            <div class="py-12 flex flex-col items-center justify-center gap-3">
              <div class="w-12 h-12 rounded-full bg-slate-50 flex items-center justify-center">
                <svg class="nt-empty-icon" viewBox="0 0 24 24" aria-hidden="true"><path d="M18 9a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9"></path></svg>
              </div>
              <span class="text-xs text-slate-400 font-medium">Nenhuma nova notificação</span>
            </div>
          }
        </div>

        @if (!uxPreview && usuarioId) {
          <div class="p-1 px-4 py-2 border-t border-slate-100 bg-slate-50/30 flex items-center justify-center">
            <button type="button" (click)="loadNotifications()" [disabled]="loading" mat-button class="!text-[11.5px] !font-bold !text-slate-500 hover:!text-indigo-600 !w-full !rounded-lg">Atualizar notificações</button>
          </div>
        }
      </div>
    </mat-menu>
  `,
  styles: [`
    :host ::ng-deep .mat-badge-content {
      background-color: #ef4444; /* Tailwind Red 500 */
      font-size: 9px;
      font-weight: 800;
      width: 16px;
      height: 16px;
      line-height: 16px;
    }
    .custom-scrollbar::-webkit-scrollbar {
      width: 4px;
    }
    .custom-scrollbar::-webkit-scrollbar-thumb {
      background: #e2e8f0;
      border-radius: 10px;
    }
    .nt-notification-icon{position:relative;display:grid;place-items:center}.nt-notification-icon svg,.nt-inline-icon,.nt-empty-icon{width:18px;height:18px;fill:none;stroke:currentColor;stroke-width:1.9;stroke-linecap:round;stroke-linejoin:round}.nt-notification-icon.has-count::after{content:attr(data-count);position:absolute;top:-6px;right:-7px;min-width:14px;height:14px;padding:0 3px;border:2px solid #fff;border-radius:999px;background:#dc2626;color:#fff;font:800 8px/10px Inter,sans-serif;display:grid;place-items:center}.nt-empty-icon{width:20px;height:20px;color:#cbd5e1}.line-clamp-2 {
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
  `],
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
    this.fetchSub = this.notificationService.findMine()
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
