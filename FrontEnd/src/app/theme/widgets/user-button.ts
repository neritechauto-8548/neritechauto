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
    <button mat-icon-button [matMenuTriggerFor]="menu" class="!text-slate-600 dark:!text-slate-300 hover:!text-slate-900 dark:hover:!text-white hover:!bg-slate-100 dark:hover:!bg-slate-800 !transition-colors !duration-200">
      <mat-icon class="!text-[20px] !w-[20px] !h-[20px]">account_circle</mat-icon>
    </button>

    <mat-menu #menu="matMenu" backdropClass="stripe-menu-backdrop" class="!rounded-xl !border !border-slate-100 !shadow-[0_10px_40px_-10px_rgba(0,0,0,0.1)] !py-2 !px-1">
      <div class="px-1 flex flex-col gap-0.5 min-w-[200px]">
        
        <div class="px-3 py-2 mb-1 border-b border-slate-100">
          <p class="text-xs font-bold text-slate-400 uppercase tracking-widest mb-0.5">Conta</p>
          <p class="text-sm font-semibold text-slate-800 truncate">{{ user?.name || 'Administrador' }}</p>
        </div>

        <button routerLink="/profile/overview" mat-menu-item class="!rounded-md hover:!bg-slate-50 !h-9 !leading-9 transition-colors">
          <mat-icon class="!text-slate-400 !mr-2 !text-[18px]">account_circle</mat-icon>
          <span class="!text-[13px] !font-semibold !text-slate-700">{{ 'profile' | translate }}</span>
        </button>
        
        <button routerLink="/profile/settings" mat-menu-item class="!rounded-md hover:!bg-slate-50 !h-9 !leading-9 transition-colors">
          <mat-icon class="!text-slate-400 !mr-2 !text-[18px]">settings</mat-icon>
          <span class="!text-[13px] !font-semibold !text-slate-700">{{ 'edit_profile' | translate }}</span>
        </button>
        
        <button mat-menu-item (click)="restore()" class="!rounded-md hover:!bg-slate-50 !h-9 !leading-9 transition-colors">
          <mat-icon class="!text-slate-400 !mr-2 !text-[18px]">restore</mat-icon>
          <span class="!text-[13px] !font-semibold !text-slate-700">{{ 'restore_defaults' | translate }}</span>
        </button>
        
        <div class="h-px w-full bg-slate-100 my-1"></div>
        
        <button mat-menu-item (click)="logout()" class="!rounded-md hover:!bg-red-50 !h-9 !leading-9 transition-colors group">
          <mat-icon class="!text-red-400 !mr-2 !text-[18px] group-hover:!text-red-600">exit_to_app</mat-icon>
          <span class="!text-[13px] !font-bold !text-red-500 group-hover:!text-red-700">{{ 'logout' | translate }}</span>
        </button>
      </div>
    </mat-menu>
  `,
  styles: `
    .avatar {
      width: 1.5rem;
      height: 1.5rem;
      border-radius: 50rem;
    }
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
