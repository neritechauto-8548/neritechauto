import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges, SimpleChanges, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@core';
import { NeriTechIcon } from '../../../shared/components';
import { OsComment, OsJournalState } from './os-journal.models';
import { OsJournalService } from './os-journal.service';

@Component({
  selector: 'os-journal-panel',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule, NeriTechIcon],
  templateUrl: './os-journal-panel.html',
  styleUrl: './os-journal-panel.scss',
})
export class OsJournalPanel implements OnChanges {
  private readonly service = inject(OsJournalService);
  private readonly auth = inject(AuthService);
  private readonly cdr = inject(ChangeDetectorRef);

  @Input({ required: true }) osId!: number;

  readonly maxLength = 2000;
  state: OsJournalState = 'idle';
  comments: OsComment[] = [];
  draft = '';
  message = '';
  saving = false;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['osId'] && Number.isInteger(this.osId) && this.osId > 0) {
      this.load();
    }
  }

  get canRead(): boolean {
    return this.hasPermission('OS_COMENTARIOS') || this.hasPermission('OS_COMENTARIOS_OUTROS');
  }

  get canComment(): boolean {
    return this.hasPermission('OS_COMENTARIOS');
  }

  get remaining(): number {
    return this.maxLength - this.draft.length;
  }

  load(): void {
    if (!this.canRead) {
      this.state = 'forbidden';
      this.message = 'Seu perfil não possui permissão para visualizar o diário desta Ordem de Serviço.';
      this.comments = [];
      return;
    }

    this.state = 'loading';
    this.message = '';
    this.service.list(this.osId).subscribe({
      next: comments => {
        this.comments = comments ?? [];
        this.state = 'ready';
        this.cdr.markForCheck();
      },
      error: error => {
        this.state = error?.status === 403 ? 'forbidden' : 'error';
        this.message = error?.status === 403
          ? 'Seu perfil não possui permissão para visualizar o diário desta Ordem de Serviço.'
          : 'Não foi possível carregar o diário operacional desta OS.';
        this.cdr.markForCheck();
      },
    });
  }

  submit(): void {
    const content = this.draft.trim();
    if (!this.canComment || !content || this.saving || content.length > this.maxLength) return;

    this.saving = true;
    this.message = '';
    this.service.create(this.osId, { content }).subscribe({
      next: created => {
        this.comments = [created, ...this.comments];
        this.draft = '';
        this.saving = false;
        this.state = 'ready';
        this.cdr.markForCheck();
      },
      error: error => {
        this.saving = false;
        this.message = error?.error?.message || error?.message || 'Não foi possível registrar o comentário.';
        this.cdr.markForCheck();
      },
    });
  }

  trackById(_: number, comment: OsComment): number {
    return comment.id;
  }

  initials(name?: string | null): string {
    const normalized = name?.trim();
    if (!normalized) return 'U';
    return normalized.split(/\s+/).slice(0, 2).map(part => part.charAt(0)).join('').toUpperCase();
  }

  private hasPermission(permission: string): boolean {
    const user = this.auth.snapshot();
    const permissions = (user.permissions ?? []).map(value => String(value));
    const roles = (user.roles ?? []).map(value => String(value));
    return permissions.includes(permission) || roles.includes('ADMIN') || roles.includes('ROLE_ADMIN');
  }
}
