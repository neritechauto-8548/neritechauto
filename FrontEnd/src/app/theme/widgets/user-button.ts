import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { debounceTime, tap } from 'rxjs';

import { AuthService, SettingsService, User } from '@core';
import { environment } from '@env/environment';
import { AboutSystemDialog } from './about-system';

@Component({
  selector: 'app-user',
  template: `
    <button [matMenuTriggerFor]="menu" class="group flex items-center p-1 rounded-xl hover:bg-slate-100/50 transition-all duration-200">
      <div class="relative">
        @if (user.avatar) {
          <img [src]="user.avatar" class="w-9 h-9 rounded-lg object-cover border border-slate-200 shadow-sm" [alt]="user.name">
        } @else {
          <div [class]="'w-9 h-9 rounded-lg flex items-center justify-center border border-white/20 shadow-sm ' + getAvatarColor()">
            <span class="text-[12px] font-bold text-white uppercase tracking-tighter">{{ getInitials() }}</span>
          </div>
        }
        <span class="absolute -bottom-0.5 -right-0.5 w-3 h-3 bg-emerald-500 border-2 border-white rounded-full shadow-sm"></span>
      </div>
    </button>

    <mat-menu #menu="matMenu" backdropClass="stripe-menu-backdrop" class="!rounded-2xl !border !border-slate-200 !shadow-[0_20px_50px_rgba(0,0,0,0.1)] !py-0 !overflow-hidden">
      <div class="min-w-[200px] flex flex-col bg-white">
        <!-- Menu Header (User Info) -->
        <div class="px-3 py-2.5 bg-slate-50/50 border-b border-slate-100 flex flex-col">
          <span class="text-xs font-bold text-slate-900 leading-tight">{{ user.name }}</span>
          <span class="text-[10px] text-slate-500 font-medium mt-0.5 uppercase tracking-wider">
            {{ user.funcoes && user.funcoes.length > 0 ? user.funcoes[0] : 'Usuário' }}
          </span>
        </div>

        <div class="p-1 flex flex-col">
          <button [routerLink]="['/configuracoes/colaboradores/cadastro', user.id]" [queryParams]="{ byUsuario: true }" mat-menu-item class="!rounded-lg hover:!bg-slate-50 !h-9 !leading-9 transition-all group">
            <mat-icon class="!text-slate-400 !mr-2.5 !text-[18px] group-hover:!text-slate-600 transition-colors">account_circle</mat-icon>
            <span class="!text-[13px] !font-medium !text-slate-700 group-hover:!text-slate-900">Meus Dados</span>
          </button>

          <button routerLink="/configuracoes/assinatura" mat-menu-item class="!rounded-lg hover:!bg-slate-50 !h-9 !leading-9 transition-all group">
            <mat-icon class="!text-slate-400 !mr-2.5 !text-[18px] group-hover:!text-slate-600 transition-colors">credit_card</mat-icon>
            <span class="!text-[13px] !font-medium !text-slate-700 group-hover:!text-slate-900">Minha Assinatura</span>
          </button>

          <button routerLink="/agendamento/calendario" mat-menu-item class="!rounded-lg hover:!bg-slate-50 !h-9 !leading-9 transition-all group">
            <mat-icon class="!text-slate-400 !mr-2.5 !text-[18px] group-hover:!text-slate-600 transition-colors">event</mat-icon>
            <span class="!text-[13px] !font-medium !text-slate-700 group-hover:!text-slate-900">Minha Agenda</span>
          </button>

          <button (click)="openSupport()" mat-menu-item class="!rounded-lg hover:!bg-slate-50 !h-9 !leading-9 transition-all group">
            <mat-icon class="!text-slate-400 !mr-2.5 !text-[18px] group-hover:!text-sky-500 transition-colors">help_outline</mat-icon>
            <span class="!text-[13px] !font-medium !text-slate-700 group-hover:!text-slate-900">Suporte</span>
          </button>

          <button mat-menu-item (click)="showAbout = true" class="!rounded-lg hover:!bg-slate-50 !h-9 !leading-9 transition-all group">
            <mat-icon class="!text-slate-400 !mr-2.5 !text-[18px] group-hover:!text-indigo-500 transition-colors">info</mat-icon>
            <span class="!text-[13px] !font-medium !text-slate-700 group-hover:!text-slate-900">Sobre o Sistema</span>
          </button>
        </div>

        <div class="h-px w-full bg-slate-100"></div>

        <div class="p-1">
          <button mat-menu-item (click)="logout()" class="!rounded-lg hover:!bg-rose-50 !h-9 !leading-9 transition-all group">
            <mat-icon class="!text-slate-400 !mr-2.5 !text-[18px] group-hover:!text-rose-500 transition-colors">logout</mat-icon>
            <span class="!text-[13px] !font-bold !text-slate-700 group-hover:!text-rose-600">Sair da Conta</span>
          </button>
        </div>
      </div>
    </mat-menu>

    <!-- Dialog Sobre o Sistema -->
    <app-about-system [(visible)]="showAbout" />
  `,
  imports: [RouterLink, MatButtonModule, MatIconModule, MatMenuModule, TranslateModule, AboutSystemDialog],
})
export class UserButton implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly settings = inject(SettingsService);

  showAbout = false;
  user!: User;

  ngOnInit(): void {
    this.auth
      .user()
      .pipe(
        tap(user => (this.user = user)),
        debounceTime(10)
      )
      .subscribe(() => this.cdr.detectChanges());
  }

  getInitials(): string {
    if (!this.user?.name) return 'AD';
    const names = this.user.name.split(' ');
    if (names.length > 1) {
      return (names[0][0] + names[names.length - 1][0]).toUpperCase();
    }
    return names[0].substring(0, 2).toUpperCase();
  }

  getAvatarColor(): string {
    // Retorna uma cor baseada no nome para consistência
    const colors = [
      'bg-indigo-500',
      'bg-emerald-500',
      'bg-blue-500',
      'bg-purple-500',
      'bg-rose-500',
      'bg-amber-500'
    ];
    const index = (this.user?.name?.length || 0) % colors.length;
    return colors[index];
  }

  logout() {
    this.auth.logout().subscribe(() => {
      this.router.navigateByUrl('/auth/login');
    });
  }

  openSupport() {
    window.open(environment.supportUrl, '_blank');
  }

  restore() {
    this.settings.reset();
    window.location.reload();
  }
}
