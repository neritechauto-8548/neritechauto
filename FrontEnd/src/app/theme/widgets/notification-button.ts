import { Component, OnInit, inject, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { RouterLink } from '@angular/router';
import { NotificacaoService, NotificacaoSistemaResponse } from '@core/services/notificacao.service';
import { AuthService } from '@core';
import { Subscription, interval } from 'rxjs';

@Component({
  selector: 'app-notification',
  template: `
    <button mat-icon-button [matMenuTriggerFor]="menu" (click)="loadNotifications()"
      class="!text-slate-600 dark:!text-slate-300 hover:!text-slate-900 dark:hover:!text-white hover:!bg-slate-100 dark:hover:!bg-slate-800 !transition-colors !duration-200">
      <mat-icon [matBadge]="unreadCount" [matBadgeHidden]="unreadCount === 0" 
        matBadgeColor="warn" class="!text-[22px] !w-[22px] !h-[22px]">notifications</mat-icon>
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
          <button (click)="markAllAsRead()" class="text-[11px] font-bold text-indigo-600 hover:text-indigo-700 transition-colors">Marcar todas como lidas</button>
        </div>

        <!-- Notification List -->
        <div class="max-h-[350px] overflow-y-auto custom-scrollbar">
          @if (loading) {
            <div class="py-12 flex flex-col items-center justify-center gap-2">
               <div class="w-5 h-5 border-2 border-indigo-500 border-t-transparent rounded-full animate-spin"></div>
               <span class="text-[11px] text-slate-400 font-medium">Carregando...</span>
            </div>
          } @else if (notifications.length > 0) {
            @for (item of notifications; track item.id) {
              <div (click)="markAsRead(item)" 
                [class]="!item.lida ? 'bg-slate-50/40' : 'hover:bg-slate-50/60'"
                class="px-4 py-3 border-b border-slate-100 last:border-0 flex gap-3 cursor-pointer transition-all relative group">
                
                <!-- Status Dot -->
                @if (!item.lida) {
                  <span class="absolute left-1.5 top-1/2 -translate-y-1/2 w-1.5 h-1.5 bg-indigo-600 rounded-full shadow-[0_0_8px_rgba(79,70,229,0.5)]"></span>
                }

                <!-- Icon Container -->
                <div [class]="getIconBg(item.tipoNotificacao)" 
                  class="w-9 h-9 rounded-xl flex items-center justify-center shrink-0 shadow-sm border border-black/5">
                  <mat-icon [class]="getIconColor(item.tipoNotificacao)" class="!text-[18px] !w-[18px] !h-[18px]">{{ getIcon(item.tipoNotificacao) }}</mat-icon>
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
              </div>
            }
          } @else {
            <div class="py-12 flex flex-col items-center justify-center gap-3">
              <div class="w-12 h-12 rounded-full bg-slate-50 flex items-center justify-center">
                <mat-icon class="text-slate-300">notifications_none</mat-icon>
              </div>
              <span class="text-xs text-slate-400 font-medium">Nenhuma nova notificação</span>
            </div>
          }
        </div>

        <!-- Footer -->
        <div class="p-1 px-4 py-2 border-t border-slate-100 bg-slate-50/30 flex items-center justify-center">
          <button routerLink="/configuracoes/notificacoes" mat-button class="!text-[11.5px] !font-bold !text-slate-500 hover:!text-indigo-600 !w-full !rounded-lg">
            Ver todas as notificações
          </button>
        </div>
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
    .line-clamp-2 {
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
  `],
  standalone: true,
  imports: [CommonModule, MatBadgeModule, MatButtonModule, MatIconModule, MatMenuModule, RouterLink],
})
export class NotificationButton implements OnInit, OnDestroy {
  private readonly notificationService = inject(NotificacaoService);
  private readonly auth = inject(AuthService);
  private pollingSub?: Subscription;

  notifications: NotificacaoSistemaResponse[] = [];
  unreadCount = 0;
  loading = false;
  usuarioId?: number | string | null;

  ngOnInit(): void {
    this.auth.user().subscribe(user => {
      if (user && user.id) {
        this.usuarioId = user.id;
        this.loadNotifications();
        // Polling a cada 2 minutos para notificações reais
        this.pollingSub = interval(120000).subscribe(() => this.loadNotifications(false));
      }
    });
  }

  ngOnDestroy(): void {
    this.pollingSub?.unsubscribe();
  }

  loadNotifications(showLoading = true): void {
    if (!this.usuarioId) return;
    
    if (showLoading) this.loading = true;
    this.notificationService.findByUsuario(this.usuarioId).subscribe({
      next: (res) => {
        this.notifications = res.content || [];
        this.unreadCount = this.notifications.filter(n => !n.lida).length;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  markAsRead(item: NotificacaoSistemaResponse): void {
    if (item.lida) return;
    this.notificationService.marcarComoLida(item.id, item).subscribe(() => {
      item.lida = true;
      this.unreadCount = this.notifications.filter(n => !n.lida).length;
    });
  }

  markAllAsRead(): void {
    // Para simplificar, percorre todos e marca como lida
    this.notifications.filter(n => !n.lida).forEach(n => this.markAsRead(n));
  }

  getIcon(type: string): string {
    switch (type) {
      case 'SUCESSO': return 'check_circle';
      case 'AVISO': return 'warning';
      case 'ERRO': return 'error';
      default: return 'info';
    }
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
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / 60000);

    if (diffMins < 1) return 'Agora';
    if (diffMins < 60) return `${diffMins}m`;
    
    const diffHours = Math.floor(diffMins / 60);
    if (diffHours < 24) return `${diffHours}h`;
    
    return date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' });
  }
}


