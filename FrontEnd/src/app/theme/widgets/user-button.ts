import { ChangeDetectorRef, Component, DestroyRef, OnInit, ViewEncapsulation, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink } from '@angular/router';
import { AuthService, User } from '@core';
import { environment } from '@env/environment';

@Component({
  selector: 'app-user',
  standalone: true,
  template: `
    <button type="button" class="nt-profile-trigger"
      [matMenuTriggerFor]="profileMenu"
      [attr.aria-label]="'Abrir menu do usuário: ' + displayName"
      aria-haspopup="menu">
      <span class="nt-profile-trigger-picture">
        <img [src]="avatarSrc" [alt]="'Foto de perfil: ' + displayName"
          (error)="onAvatarError()" decoding="async" />
        <span class="nt-profile-online-dot" aria-hidden="true"></span>
      </span>
    </button>

    <mat-menu #profileMenu="matMenu" class="nt-profile-menu-panel"
      xPosition="before" yPosition="below" [overlapTrigger]="false">
      <section class="nt-profile-card" aria-label="Conta do usuário">
        <div class="nt-profile-eyebrow">USUÁRIO</div>
        <div class="nt-profile-identity">
          <img class="nt-profile-large-avatar" [src]="avatarSrc"
            [alt]="'Foto de perfil: ' + displayName" (error)="onAvatarError()" decoding="async" />
          <div class="nt-profile-identity-name" [title]="displayName">{{ displayName }}</div>
          <div class="nt-profile-identity-role">{{ displayRole }}</div>
          @if (user.email) {
            <div class="nt-profile-identity-email" [title]="user.email">{{ user.email }}</div>
          }
          @if (isIllustrativeAvatar) {
            <div class="nt-profile-demo-label">Imagem ilustrativa · homologação</div>
          }
        </div>
        <div class="nt-profile-items" aria-label="Opções da conta">
          <button mat-menu-item class="nt-profile-action" routerLink="/profile/overview">
            <svg viewBox="0 0 24 24" aria-hidden="true"><circle cx="12" cy="8" r="4"/><path d="M5 21v-2a7 7 0 0 1 14 0v2"/></svg>
            <span>Meu perfil</span>
            <svg class="nt-profile-chevron" viewBox="0 0 24 24" aria-hidden="true"><path d="m9 6 6 6-6 6"/></svg>
          </button>
          <button mat-menu-item class="nt-profile-action" routerLink="/profile/settings">
            <svg viewBox="0 0 24 24" aria-hidden="true"><circle cx="12" cy="12" r="3"/><path d="M19 13.6v-3.2l-2.2-.7-.5-1.2 1-2-2.3-2.3-2 1-1.2-.5L11.1 2H8.9l-.7 2.2-1.2.5-2-1-2.3 2.3 1 2-.5 1.2-2.2.7v3.2l2.2.7.5 1.2-1 2 2.3 2.3 2-1 1.2.5.7 2.2h3.2l.7-2.2 1.2-.5 2 1 2.3-2.3-1-2 .5-1.2 2.2-.7Z" transform="translate(1 1) scale(.91)"/></svg>
            <span>Configurações</span>
            <svg class="nt-profile-chevron" viewBox="0 0 24 24" aria-hidden="true"><path d="m9 6 6 6-6 6"/></svg>
          </button>
          <button mat-menu-item class="nt-profile-action" (click)="openSupport()">
            <svg viewBox="0 0 24 24" aria-hidden="true"><circle cx="12" cy="12" r="9"/><circle cx="12" cy="12" r="4"/><path d="m5.6 5.6 3.6 3.6m5.6 5.6 3.6 3.6M18.4 5.6l-3.6 3.6m-5.6 5.6-3.6 3.6"/></svg>
            <span>Ajuda</span>
            <svg class="nt-profile-chevron" viewBox="0 0 24 24" aria-hidden="true"><path d="m9 6 6 6-6 6"/></svg>
          </button>
        </div>
        <div class="nt-profile-logout">
          <button mat-menu-item class="nt-profile-action nt-profile-action-danger" (click)="logout()">
            <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M10 5H5v14h5m7-11 4 4-4 4m4-4H9"/></svg>
            <span>Sair</span>
          </button>
        </div>
      </section>
    </mat-menu>
  `,
  styleUrl: './user-button.scss',
  imports: [RouterLink, MatMenuModule],
  encapsulation: ViewEncapsulation.None,
})
export class UserButton implements OnInit {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly destroyRef = inject(DestroyRef);

  readonly uxPreview = environment.uxPreview;
  user: User = {};
  avatarFailed = false;

  ngOnInit(): void {
    this.auth.user().pipe(takeUntilDestroyed(this.destroyRef)).subscribe(user => {
      this.user = user ?? {};
      this.avatarFailed = false;
      this.cdr.markForCheck();
    });
  }

  get displayName(): string {
    const name = this.user?.name || this.user?.nomeCompleto;
    return typeof name === 'string' && name.trim()
      ? name.trim()
      : (this.uxPreview ? 'Usuário de homologação' : 'Minha conta');
  }

  get displayRole(): string {
    const roles = this.user?.funcoes || this.user?.roles || [];
    const first = Array.isArray(roles) ? roles[0] : null;
    const raw = typeof first === 'string' ? first : first?.nome || first?.name;
    if (!raw || typeof raw !== 'string') return this.uxPreview ? 'Perfil de demonstração' : 'Usuário';
    return raw.replace(/^ROLE_/, '').replace(/[_-]/g, ' ');
  }

  get isIllustrativeAvatar(): boolean {
    return this.uxPreview && !(typeof this.user?.avatar === 'string' && this.user.avatar.trim());
  }

  get avatarSrc(): string {
    if (this.avatarFailed) return '/images/nt-profile-default.svg';
    const avatar = this.user?.avatar;
    if (typeof avatar === 'string' && avatar.trim()) return avatar.trim();
    return this.uxPreview ? '/images/nt-profile-demo.webp' : '/images/nt-profile-default.svg';
  }

  onAvatarError(): void {
    this.avatarFailed = true;
  }

  openSupport(): void {
    window.open(environment.supportUrl, '_blank', 'noopener,noreferrer');
  }

  logout(): void {
    if (this.uxPreview) {
      this.router.navigateByUrl('/auth/login');
      return;
    }
    this.auth.logout().subscribe({
      next: () => this.router.navigateByUrl('/auth/login'),
      error: () => this.router.navigateByUrl('/auth/login'),
    });
  }
}
