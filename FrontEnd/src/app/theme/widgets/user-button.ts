import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { debounceTime, tap } from 'rxjs';

import { AuthService, SettingsService, User } from '@core';

@Component({
  selector: 'app-user',
  template: `
    <button [matMenuTriggerFor]="menu" class="flex items-center gap-2 p-1 rounded-full hover:bg-slate-100 dark:hover:bg-slate-800 transition-all duration-200">
      <div class="relative">
        @if (user.avatar) {
          <img [src]="user.avatar" class="w-8 h-8 rounded-full object-cover border border-slate-200 dark:border-slate-700 shadow-sm" [alt]="user.name">
        } @else {
          <div [class]="'w-8 h-8 rounded-full flex items-center justify-center border border-white/20 shadow-sm ' + getAvatarColor()">
            <span class="text-[11px] font-bold text-white uppercase tracking-tighter">{{ getInitials() }}</span>
          </div>
        }
        <span class="absolute bottom-0 right-0 w-2.5 h-2.5 bg-emerald-500 border-2 border-white dark:border-slate-900 rounded-full"></span>
      </div>
    </button>

    <mat-menu #menu="matMenu" backdropClass="stripe-menu-backdrop" class="!rounded-xl !border !border-slate-100 !shadow-[0_10px_40px_-10px_rgba(0,0,0,0.1)] !py-2 !px-1">
      <div class="px-1 flex flex-col gap-0.5 min-w-[220px]">
        
        <div class="px-3 py-3 mb-1 bg-slate-50/50 dark:bg-slate-800/50 rounded-lg flex items-center gap-3">
          <div [class]="'w-10 h-10 rounded-full flex items-center justify-center font-bold text-white ' + getAvatarColor()">
            {{ getInitials() }}
          </div>
          <div class="flex flex-col min-w-0">
            <p class="text-[13px] font-extrabold text-slate-900 dark:text-white truncate leading-tight">{{ user.name || 'Administrador' }}</p>
            <p class="text-[11px] font-medium text-slate-500 truncate">{{ user.email || 'master@empresa.com' }}</p>
          </div>
        </div>

        <p class="px-3 py-2 text-[10px] font-bold text-slate-400 uppercase tracking-widest mt-1">Navegação</p>

        <button routerLink="/profile/overview" mat-menu-item class="!rounded-md hover:!bg-slate-50 !h-10 !leading-10 transition-colors">
          <mat-icon class="!text-slate-400 !mr-2 !text-[18px]">account_circle</mat-icon>
          <span class="!text-[13px] !font-semibold !text-slate-700">{{ 'profile' | translate }}</span>
        </button>
        
        <button routerLink="/profile/settings" mat-menu-item class="!rounded-md hover:!bg-slate-50 !h-10 !leading-10 transition-colors">
          <mat-icon class="!text-slate-400 !mr-2 !text-[18px]">settings</mat-icon>
          <span class="!text-[13px] !font-semibold !text-slate-700">{{ 'edit_profile' | translate }}</span>
        </button>
        
        <div class="h-px w-full bg-slate-100 my-1"></div>
        
        <button mat-menu-item (click)="logout()" class="!rounded-md hover:!bg-red-50 !h-10 !leading-10 transition-colors group">
          <mat-icon class="!text-red-400 !mr-2 !text-[18px] group-hover:!text-red-600">exit_to_app</mat-icon>
          <span class="!text-[13px] !font-bold !text-red-500 group-hover:!text-red-700">{{ 'logout' | translate }}</span>
        </button>
      </div>
    </mat-menu>
  `,
  imports: [RouterLink, MatButtonModule, MatIconModule, MatMenuModule, TranslateModule],
})
export class UserButton implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly settings = inject(SettingsService);

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

  restore() {
    this.settings.reset();
    window.location.reload();
  }
}
