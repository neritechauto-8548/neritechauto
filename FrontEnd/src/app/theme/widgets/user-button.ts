import { ChangeDetectorRef, Component, DestroyRef, HostListener, OnInit, ViewEncapsulation, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink } from '@angular/router';
import { AuthService, User, TokenService, SettingsService } from '@core';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '@env/environment';
import { ProfileAvatarService } from './profile-avatar.service';

@Component({
  selector: 'app-user',
  standalone: true,
  templateUrl: './user-button.html',
  styleUrl: './user-button.scss',
  imports: [RouterLink, MatMenuModule],
  encapsulation: ViewEncapsulation.None,
})
export class UserButton implements OnInit {
  private readonly auth = inject(AuthService);
  private readonly settings = inject(SettingsService);
  readonly siteUrl = 'https://neritechauto.com.br';
  activeDialog: 'shortcuts' | 'about' | null = null;
  get theme(): string { return this.settings.options.theme; }
  changeTheme(value: 'light' | 'auto' | 'dark'): void { this.settings.setTheme(value); }
  get initials(): string { return this.displayName.trim().slice(0, 2).toUpperCase(); }
  openDialog(kind: 'shortcuts' | 'about'): void { this.activeDialog = kind; }
  closeDialog(): void { this.activeDialog = null; }

  @HostListener('document:keydown', ['$event'])
  onKeydown(event: KeyboardEvent): void {
    const target = event.target as HTMLElement | null;
    const editable = target?.isContentEditable || ['INPUT', 'TEXTAREA', 'SELECT'].includes(target?.tagName ?? '');
    if (event.key === 'Escape' && this.activeDialog) { event.preventDefault(); this.closeDialog(); return; }
    if ((event.ctrlKey || event.metaKey) && !event.shiftKey && event.key === '/') {
      event.preventDefault(); this.activeDialog = this.activeDialog === 'shortcuts' ? null : 'shortcuts'; return;
    }
    if (editable || this.activeDialog) return;
    if (!event.ctrlKey && !event.metaKey && !event.altKey && (event.key === '[' || event.key === ']')) {
      event.preventDefault(); this.settings.setOptions({ sidenavCollapsed: event.key === '[' }); return;
    }
    if ((event.ctrlKey || event.metaKey) && event.key === ',') { event.preventDefault(); this.router.navigateByUrl('/profile/overview'); }
    if ((event.ctrlKey || event.metaKey) && event.key === '.') { event.preventDefault(); this.router.navigateByUrl('/configuracoes/empresa'); }
    if (event.shiftKey && !event.ctrlKey && !event.metaKey && event.key.toLowerCase() === 'o') {
      event.preventDefault(); this.router.navigateByUrl('/orcamento/orcamento');
    }
  }

  readonly avatar = inject(ProfileAvatarService);
  private readonly tokens = inject(TokenService);
  private readonly storage = inject(LocalStorageService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly destroyRef = inject(DestroyRef);

  readonly uxPreview = environment.uxPreview;
  user: User = {};

  ngOnInit(): void {
    this.auth.user().pipe(takeUntilDestroyed(this.destroyRef)).subscribe(user => {
      this.user = user ?? {};
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
    return this.avatar.isIllustrative(this.user);
  }

  get avatarSrc(): string {
    return this.avatar.src(this.user);
  }

  onAvatarError(): void {
    this.avatar.reportImageError(this.avatarSrc);
  }

  openSupport(): void {
    window.open(environment.supportUrl, '_blank', 'noopener,noreferrer');
  }

  logout(): void {
    if (this.uxPreview) {
      // Recarregamento completo desativa o flag calculado no bootstrap.
      window.location.assign('/auth/login');
      return;
    }
    this.auth.logout().subscribe({
      next: () => this.router.navigateByUrl('/auth/login'),
      error: () => {
        // Mesmo se a API estiver offline, encerrar a sessão local.
        this.tokens.clear();
        this.storage.remove('tenantId');
        this.storage.remove('empresaId');
        this.router.navigateByUrl('/auth/login');
      },
    });
  }
}
