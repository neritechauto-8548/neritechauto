import { Injectable, signal } from '@angular/core';
import { User } from '@core/authentication/interface';
import { environment } from '@env/environment';

/** A single source of truth for the avatar across the topbar and account page.
 * Local photo previews are allowed in UX homologation only, never reported as persisted.
 */
@Injectable({ providedIn: 'root' })
export class ProfileAvatarService {
  private readonly previewUrl = signal<string | null>(null);
  private readonly failedUrl = signal<string | null>(null);

  readonly uxPreview = environment.uxPreview;

  src(user: User | null | undefined): string {
    const candidate = this.previewUrl()
      || this.avatarFromUser(user)
      || (this.uxPreview ? '/images/nt-profile-demo.webp' : '/images/nt-profile-default.svg');
    return candidate === this.failedUrl() ? '/images/nt-profile-default.svg' : candidate;
  }

  isIllustrative(user: User | null | undefined): boolean {
    return this.uxPreview && !this.previewUrl() && !this.avatarFromUser(user);
  }

  hasLocalPreview(): boolean {
    return !!this.previewUrl();
  }

  reportImageError(src: string): void {
    this.failedUrl.set(src);
  }

  selectLocalPreview(file: File): string | null {
    if (!this.uxPreview) return 'A alteração de foto requer a integração de upload segura no servidor.';
    if (!['image/jpeg', 'image/png', 'image/webp'].includes(file.type)) {
      return 'Escolha uma imagem JPG, PNG ou WebP.';
    }
    if (file.size > 1024 * 1024) return 'A imagem deve ter até 1 MB.';
    if (file.size === 0) return 'Selecione uma imagem válida.';
    this.clearLocalPreview();
    this.failedUrl.set(null);
    this.previewUrl.set(URL.createObjectURL(file));
    return null;
  }

  clearLocalPreview(): void {
    const previous = this.previewUrl();
    if (previous) URL.revokeObjectURL(previous);
    this.previewUrl.set(null);
    this.failedUrl.set(null);
  }

  private avatarFromUser(user: User | null | undefined): string | null {
    const value = user?.avatar || user?.avatarUrl;
    return typeof value === 'string' && value.trim() ? value.trim() : null;
  }
}
