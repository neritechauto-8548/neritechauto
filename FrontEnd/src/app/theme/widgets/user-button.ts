import { Component, DestroyRef, ViewEncapsulation, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink } from '@angular/router';

import { AuthService, User } from '@core';
import { environment } from '@env/environment';

/**
 * Perfil da topbar. Sempre renderiza uma imagem; não usa iniciais.
 * O retrato de fallback é apenas uma ilustração, nunca uma identidade de usuário.
 */
@Component({
  selector: 'app-user',
  standalone: true,
  imports: [MatMenuModule, RouterLink],
  encapsulation: ViewEncapsulation.None,
  template: `
    <button type="button" class="nt-profile-trigger" [matMenuTriggerFor]="profileMenu"
      aria-label="Abrir menu da conta" title="Minha conta">
      <span class="nt-profile-trigger-avatar">
        @if (!avatarUnavailable) {
          <img [src]="avatarUrl" alt="" loading="eager" decoding="async"
            (error)="onAvatarError($event)" />
        } @else {
          <svg class="nt-profile-fallback-icon" viewBox="0 0 24 24" aria-hidden="true">
            <circle cx="12" cy="8" r="3.5" /><path d="M5 21a7 7 0 0 1 14 0" />
          </svg>
        }
      </span>
      <span class="nt-profile-status" aria-hidden="true"></span>
    </button>

    <mat-menu #profileMenu="matMenu" class="nt-profile-panel" xPosition="before"
      yPosition="below" [overlapTrigger]="false" aria-label="Opções da conta">
      <section class="nt-profile-card" aria-label="Conta do usuário">
        <div class="nt-profile-eyebrow">USUÁRIO</div>
        <div class="nt-profile-identity">
          <span class="nt-profile-photo">
            @if (!avatarUnavailable) {
              <img [src]="avatarUrl" [alt]="hasRealAvatar ? 'Foto de perfil de ' + displayName : 'Retrato ilustrativo de perfil'"
                loading="lazy" decoding="async" (error)="onAvatarError($event)" />
            } @else {
              <svg class="nt-profile-fallback-icon" viewBox="0 0 24 24" aria-hidden="true">
                <circle cx="12" cy="8" r="3.5" /><path d="M5 21a7 7 0 0 1 14 0" />
              </svg>
            }
          </span>
          <strong class="nt-profile-name">{{ displayName }}</strong>
          <span class="nt-profile-role">{{ displayRole }}</span>
          <span class="nt-profile-company">NERITECH AUTO</span>
        </div>

        <div class="nt-profile-separator" role="separator"></div>
        <div class="nt-profile-actions">
          <button mat-menu-item routerLink="/profile/overview" class="nt-profile-action">
            <span class="nt-profile-action-content">
              <svg class="nt-profile-icon" viewBox="0 0 24 24" aria-hidden="true">
                <circle cx="12" cy="8" r="3.5" /><path d="M5 21v-1a7 7 0 0 1 14 0v1" />
              </svg>
              <span>Meu perfil</span>
              <svg class="nt-profile-chevron" viewBox="0 0 24 24" aria-hidden="true"><path d="m9 6 6 6-6 6" /></svg>
            </span>
          </button>
          <button mat-menu-item routerLink="/profile/settings" class="nt-profile-action">
            <span class="nt-profile-action-content">
              <svg class="nt-profile-icon" viewBox="0 0 24 24" aria-hidden="true">
                <circle cx="12" cy="12" r="3" /><path d="M19.4 15a1.8 1.8 0 0 0 .35 2l-1.85 1.85a1.8 1.8 0 0 0-2-.35l-.9.36a1.8 1.8 0 0 0-1.1 1.64h-2.6a1.8 1.8 0 0 0-1.1-1.64l-.9-.36a1.8 1.8 0 0 0-2 .35L5.45 17a1.8 1.8 0 0 0 .35-2l-.36-.9A1.8 1.8 0 0 0 3.8 13H3v-2h.8a1.8 1.8 0 0 0 1.64-1.1l.36-.9a1.8 1.8 0 0 0-.35-2L7.3 5.15a1.8 1.8 0 0 0 2 .35l.9-.36A1.8 1.8 0 0 0 11.3 3.5h2.6a1.8 1.8 0 0 0 1.1 1.64l.9.36a1.8 1.8 0 0 0 2-.35L19.75 7a1.8 1.8 0 0 0-.35 2l.36.9A1.8 1.8 0 0 0 21.4 11h.6v2h-.6a1.8 1.8 0 0 0-1.64 1.1Z" />
              </svg>
              <span>Configurações</span>
              <svg class="nt-profile-chevron" viewBox="0 0 24 24" aria-hidden="true"><path d="m9 6 6 6-6 6" /></svg>
            </span>
          </button>
          <button mat-menu-item (click)="openSupport()" class="nt-profile-action">
            <span class="nt-profile-action-content">
              <svg class="nt-profile-icon" viewBox="0 0 24 24" aria-hidden="true">
                <circle cx="12" cy="12" r="9" /><circle cx="12" cy="12" r="4" /><path d="m8.9 8.9-3.3-3.3m9.5 3.3 3.3-3.3m-9.5 9.5-3.3 3.3m9.5-3.3 3.3 3.3" />
              </svg>
              <span>Ajuda</span>
              <svg class="nt-profile-chevron" viewBox="0 0 24 24" aria-hidden="true"><path d="m9 6 6 6-6 6" /></svg>
            </span>
          </button>
        </div>

        <div class="nt-profile-separator" role="separator"></div>
        <div class="nt-profile-actions nt-profile-actions-last">
          <button mat-menu-item (click)="logout()" class="nt-profile-action nt-profile-logout">
            <span class="nt-profile-action-content">
              <svg class="nt-profile-icon" viewBox="0 0 24 24" aria-hidden="true">
                <path d="M10 5H5a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h5m4-14 6 7-6 7m6-7H9" />
              </svg>
              <span>Sair</span>
            </span>
          </button>
        </div>
      </section>
    </mat-menu>
  `,
  styles: [`
    .nt-profile-trigger{position:relative;display:grid;place-items:center;width:44px;height:44px;padding:4px;border:0;border-radius:12px;background:transparent;cursor:pointer;transition:background .15s ease}
    .nt-profile-trigger:hover{background:#f1f5f9}
    .nt-profile-trigger:focus-visible{outline:2px solid #2563eb;outline-offset:2px}
    .nt-profile-trigger-avatar{display:grid;place-items:center;width:36px;height:36px;overflow:hidden;border-radius:10px;border:2px solid #fff;box-shadow:0 0 0 1px #cbd5e1;background:#eff6ff}
    .nt-profile-trigger-avatar img,.nt-profile-photo img{display:block;width:100%;height:100%;object-fit:cover}
    .nt-profile-status{position:absolute;right:2px;bottom:2px;width:11px;height:11px;border:2px solid #fff;border-radius:50%;background:#10b981}
    .nt-profile-panel.mat-mdc-menu-panel{min-width:280px!important;max-width:300px!important;width:300px!important;margin-top:6px;overflow:hidden!important;border:1px solid #e2e8f0;border-radius:14px!important;background:#fff!important;box-shadow:0 18px 48px rgba(15,23,42,.14),0 2px 7px rgba(15,23,42,.05)!important}
    .nt-profile-panel .mat-mdc-menu-content{padding:0!important}
    .nt-profile-card{color:#0f172a;font-family:Inter,system-ui,Arial,sans-serif}
    .nt-profile-eyebrow{padding:14px 18px 10px;color:#64748b;font-size:10px;font-weight:700;letter-spacing:.13em}
    .nt-profile-identity{display:flex;align-items:center;flex-direction:column;gap:4px;padding:7px 16px 19px;text-align:center}
    .nt-profile-photo{display:grid;place-items:center;width:64px;height:64px;overflow:hidden;margin-bottom:6px;border:2px solid #fff;border-radius:50%;background:#f1f5f9;box-shadow:0 0 0 1px #e2e8f0}
    .nt-profile-name{max-width:100%;overflow:hidden;color:#0f172a;font-size:15px;font-weight:700;line-height:21px;text-overflow:ellipsis;white-space:nowrap}
    .nt-profile-role{color:#64748b;font-size:12px;line-height:18px}
    .nt-profile-company{color:#94a3b8;font-size:10px;font-weight:700;letter-spacing:.085em}
    .nt-profile-separator{height:1px;margin:0;background:#e2e8f0}
    .nt-profile-actions{padding:6px}
    .nt-profile-actions-last{padding-block:6px}
    .nt-profile-panel .nt-profile-action.mat-mdc-menu-item{min-height:42px;height:42px;border-radius:8px;background:transparent;color:#334155;transition:background .15s ease}
    .nt-profile-panel .nt-profile-action.mat-mdc-menu-item:hover{background:#f8fafc}
    .nt-profile-panel .nt-profile-action.mat-mdc-menu-item:focus-visible{outline:2px solid #2563eb;outline-offset:-2px;background:#eff6ff}
    .nt-profile-action .mat-mdc-menu-item-text{display:block;width:100%}
    .nt-profile-action-content{display:flex;align-items:center;gap:12px;width:100%;font-size:13px;font-weight:550}
    .nt-profile-panel .nt-profile-icon{display:block;flex-shrink:0;width:18px!important;height:18px!important;fill:none!important;stroke:currentColor!important;stroke-width:1.7!important;stroke-linecap:round;stroke-linejoin:round}
    .nt-profile-panel .nt-profile-chevron{display:block;flex-shrink:0;width:14px!important;height:14px!important;margin-left:auto;fill:none!important;stroke:#94a3b8!important;stroke-width:1.8!important;stroke-linecap:round;stroke-linejoin:round}
    .nt-profile-panel .nt-profile-logout{color:#b91c1c!important}
    .nt-profile-panel .nt-profile-logout:hover{background:#fff1f2!important}
    .nt-profile-fallback-icon{width:22px;height:22px;fill:none;stroke:#64748b;stroke-width:1.7;stroke-linecap:round}
    @media(max-width:420px){.nt-profile-panel.mat-mdc-menu-panel{max-width:calc(100vw - 24px)!important}}
    @media(prefers-reduced-motion:reduce){.nt-profile-trigger,.nt-profile-panel .nt-profile-action.mat-mdc-menu-item{transition:none}}
  `],
})
export class UserButton {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  readonly fallbackAvatar = '/images/avatar-profile-demo.webp';
  user: User = {};
  avatarFallbackActive = false;
  avatarUnavailable = false;

  constructor() {
    this.auth.user().pipe(takeUntilDestroyed(this.destroyRef)).subscribe(user => {
      this.user = user || {};
      this.avatarFallbackActive = false;
      this.avatarUnavailable = false;
    });
  }

  get hasRealAvatar(): boolean {
    return !!(this.user.avatar || this.user['avatarUrl']) && !this.avatarFallbackActive;
  }

  get avatarUrl(): string {
    if (!this.avatarFallbackActive) {
      const supplied = this.user.avatar || this.user['avatarUrl'];
      if (typeof supplied === 'string' && supplied.trim()) return supplied;
    }
    return this.fallbackAvatar;
  }

  get displayName(): string {
    return this.user.name?.trim() || this.user['nomeCompleto'] || (environment.uxPreview ? 'Conta de demonstração' : 'Minha conta');
  }

  get displayRole(): string {
    const value = this.user.funcoes?.[0] || this.user.roles?.[0];
    if (typeof value !== 'string') return environment.uxPreview ? 'Ambiente de homologação' : 'Usuário';
    if (/^(ROLE_)?ADMIN(ISTRADOR)?$/i.test(value)) return 'Administrador';
    return value.replace(/^ROLE_/, '').replace(/_/g, ' ');
  }

  onAvatarError(event: Event): void {
    // A foto da topbar e a do menu podem falhar simultaneamente.
    // Apenas a falha do próprio fallback deve ocultar a imagem.
    const failedUrl = (event.target as HTMLImageElement).currentSrc || '';
    if (failedUrl.includes('/images/avatar-profile-demo.webp')) {
      this.avatarUnavailable = true;
    } else {
      this.avatarFallbackActive = true;
    }
  }

  openSupport(): void {
    window.open(environment.supportUrl, '_blank', 'noopener,noreferrer');
  }

  logout(): void {
    if (environment.uxPreview) {
      this.router.navigateByUrl('/auth/login');
      return;
    }
    this.auth.logout().subscribe({
      next: () => this.router.navigateByUrl('/auth/login'),
    });
  }
}
