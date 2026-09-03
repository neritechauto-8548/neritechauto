import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges, SimpleChanges, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@core';
import { NeriTechIcon } from '../../../shared/components';
import {
  OS_ADDITIONAL_STATUS_LABELS,
  OsAdditionalCreateRequest,
  OsAdditionalItemDraft,
  OsAdditionalItemType,
  OsAdditionalLoadState,
  OsAdditionalOperation,
  OsAdditionalRequest,
} from './os-additional.models';
import { OsAdditionalService } from './os-additional.service';

interface DraftForm {
  title: string;
  reason: string;
  items: OsAdditionalItemDraft[];
}

interface SubmitForm {
  recipientName: string;
  channel: string;
  recipientMasked: string;
  expiresAt: string;
}

@Component({
  selector: 'os-additional-panel',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule, NeriTechIcon],
  templateUrl: './os-additional-panel.html',
  styleUrl: './os-additional-panel.scss',
})
export class OsAdditionalPanel implements OnChanges {
  private readonly service = inject(OsAdditionalService);
  private readonly auth = inject(AuthService);
  private readonly cdr = inject(ChangeDetectorRef);

  @Input({ required: true }) osId!: number;

  readonly statusLabels = OS_ADDITIONAL_STATUS_LABELS;
  readonly operations: Array<{ value: OsAdditionalOperation; label: string }> = [
    { value: 'ADD', label: 'Adicionar' }, { value: 'UPDATE', label: 'Alterar' }, { value: 'REMOVE', label: 'Remover' },
  ];
  readonly itemTypes: Array<{ value: OsAdditionalItemType; label: string }> = [
    { value: 'SERVICE', label: 'Serviço' }, { value: 'PRODUCT', label: 'Peça/produto' }, { value: 'OTHER', label: 'Outro' },
  ];

  state: OsAdditionalLoadState = 'idle';
  message = '';
  actionMessage = '';
  busyKey?: string;
  requests: OsAdditionalRequest[] = [];
  selected?: OsAdditionalRequest;
  editorOpen = false;
  submitOpen = false;
  editingId?: number;
  generatedLink?: string;
  draft: DraftForm = this.emptyDraft();
  submitForm: SubmitForm = this.emptySubmit();

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['osId'] && Number.isInteger(this.osId) && this.osId > 0) this.load();
  }

  get canEdit(): boolean { return this.hasPermission('OS_EDITAR'); }
  get pendingCount(): number { return this.requests.filter(item => item.status === 'PENDENTE' || item.status === 'VISUALIZADA').length; }
  get approvedValue(): number { return this.requests.filter(item => item.status === 'APROVADA' || item.status === 'PARCIAL').reduce((sum, item) => sum + Number(item.amountDelta || 0), 0); }

  load(): void {
    this.state = 'loading';
    this.message = '';
    this.service.list(this.osId).subscribe({
      next: requests => {
        this.requests = requests ?? [];
        this.state = this.requests.length ? 'ready' : 'empty';
        if (this.selected) this.selected = this.requests.find(item => item.id === this.selected?.id) ?? this.requests[0];
        else this.selected = this.requests[0];
        this.cdr.markForCheck();
      },
      error: error => {
        this.state = error?.status === 403 ? 'forbidden' : 'error';
        this.message = error?.status === 403
          ? 'Seu perfil não possui permissão para visualizar solicitações adicionais.'
          : 'Não foi possível carregar as solicitações adicionais desta OS.';
        this.cdr.markForCheck();
      },
    });
  }

  select(request: OsAdditionalRequest): void {
    this.selected = request;
    this.actionMessage = '';
    this.generatedLink = undefined;
  }

  openCreate(): void {
    if (!this.canEdit) return;
    this.editingId = undefined;
    this.draft = this.emptyDraft();
    this.editorOpen = true;
    this.submitOpen = false;
    this.actionMessage = '';
  }

  openEdit(request: OsAdditionalRequest): void {
    if (!this.canEdit || !request.allowedActions.includes('EDIT')) return;
    this.editingId = request.id;
    this.draft = {
      title: request.title,
      reason: request.reason,
      items: request.items.map(item => ({
        operation: item.operation,
        itemType: item.itemType,
        sourceItemId: item.sourceItemId,
        catalogItemId: item.catalogItemId,
        description: item.description,
        quantity: item.quantity,
        unit: item.unit,
        amountDelta: item.amountDelta,
        timeDeltaMinutes: item.timeDeltaMinutes,
      })),
    };
    this.editorOpen = true;
    this.submitOpen = false;
    this.actionMessage = '';
  }

  closeEditor(): void {
    if (this.busyKey) return;
    this.editorOpen = false;
    this.editingId = undefined;
    this.draft = this.emptyDraft();
  }

  addItem(): void {
    this.draft.items = [...this.draft.items, this.emptyItem()];
  }

  removeItem(index: number): void {
    if (this.draft.items.length <= 1) return;
    this.draft.items = this.draft.items.filter((_, current) => current !== index);
  }

  saveDraft(): void {
    if (!this.canEdit || !this.isDraftValid() || this.busyKey) return;
    const payload: OsAdditionalCreateRequest = {
      title: this.draft.title.trim(), reason: this.draft.reason.trim(),
      items: this.draft.items.map(item => ({
        ...item,
        description: item.description.trim(),
        unit: item.unit?.trim() || null,
        quantity: Number(item.quantity),
        amountDelta: Number(item.amountDelta || 0),
        timeDeltaMinutes: Number(item.timeDeltaMinutes || 0),
      })),
    };
    this.busyKey = 'save';
    this.actionMessage = '';
    const request$ = this.editingId ? this.service.update(this.editingId, payload) : this.service.create(this.osId, payload);
    request$.subscribe({
      next: saved => {
        this.upsert(saved);
        this.selected = saved;
        this.busyKey = undefined;
        this.closeEditor();
        this.state = 'ready';
        this.cdr.markForCheck();
      },
      error: error => this.handleActionError(error, 'Não foi possível salvar o adicional.'),
    });
  }

  openSubmit(request: OsAdditionalRequest): void {
    if (!this.canEdit || !request.allowedActions.includes('SUBMIT')) return;
    this.selected = request;
    this.submitForm = this.emptySubmit();
    this.submitOpen = true;
    this.editorOpen = false;
    this.generatedLink = undefined;
    this.actionMessage = '';
  }

  closeSubmit(): void {
    if (this.busyKey) return;
    this.submitOpen = false;
  }

  submitForApproval(): void {
    if (!this.selected || !this.isSubmitValid() || this.busyKey) return;
    this.busyKey = 'submit';
    this.actionMessage = '';
    const expiresAt = this.submitForm.expiresAt.length === 16
      ? `${this.submitForm.expiresAt}:00`
      : this.submitForm.expiresAt;
    this.service.submit(this.selected.id, {
      recipientName: this.submitForm.recipientName.trim(),
      channel: this.submitForm.channel,
      recipientMasked: this.submitForm.recipientMasked.trim(),
      expiresAt,
    }).subscribe({
      next: result => {
        this.upsert(result.request);
        this.selected = result.request;
        this.generatedLink = `${window.location.origin}/aprovar-adicional/${result.approvalToken}`;
        this.busyKey = undefined;
        this.submitOpen = false;
        this.cdr.markForCheck();
      },
      error: error => this.handleActionError(error, 'Não foi possível gerar o link de aprovação.'),
    });
  }

  revoke(request: OsAdditionalRequest): void {
    if (!this.canEdit || !request.allowedActions.includes('REVOKE') || this.busyKey) return;
    this.busyKey = `revoke-${request.id}`;
    this.actionMessage = '';
    this.service.revoke(request.id).subscribe({
      next: updated => {
        this.upsert(updated);
        this.selected = updated;
        this.generatedLink = undefined;
        this.busyKey = undefined;
        this.cdr.markForCheck();
      },
      error: error => this.handleActionError(error, 'Não foi possível revogar a solicitação.'),
    });
  }

  async copyGeneratedLink(): Promise<void> {
    if (!this.generatedLink) return;
    try {
      await navigator.clipboard.writeText(this.generatedLink);
      this.actionMessage = 'Link seguro copiado. O token bruto não fica armazenado na tela após recarregar.';
    } catch {
      this.actionMessage = 'Não foi possível copiar automaticamente. Selecione o link exibido e copie manualmente.';
    }
    this.cdr.markForCheck();
  }

  statusLabel(request: OsAdditionalRequest): string { return this.statusLabels[request.status] ?? request.status; }
  statusTone(status: string): string {
    if (status === 'APROVADA') return 'success';
    if (status === 'PARCIAL' || status === 'PENDENTE' || status === 'VISUALIZADA') return 'warning';
    if (status === 'RECUSADA' || status === 'EXPIRADA' || status === 'REVOGADA' || status === 'CANCELADA') return 'danger';
    return 'neutral';
  }
  operationLabel(value: OsAdditionalOperation): string { return this.operations.find(item => item.value === value)?.label ?? value; }
  itemTypeLabel(value: OsAdditionalItemType): string { return this.itemTypes.find(item => item.value === value)?.label ?? value; }
  decisionLabel(value: string): string { return value === 'APPROVED' ? 'Aprovado' : value === 'REJECTED' ? 'Recusado' : 'Pendente'; }
  trackById(_: number, item: { id: number }): number { return item.id; }

  private upsert(request: OsAdditionalRequest): void {
    const exists = this.requests.some(item => item.id === request.id);
    this.requests = exists ? this.requests.map(item => item.id === request.id ? request : item) : [request, ...this.requests];
  }

  private isDraftValid(): boolean {
    return Boolean(this.draft.title.trim() && this.draft.reason.trim() && this.draft.items.length
      && this.draft.items.every(item => item.description.trim() && Number(item.quantity) > 0 && Number(item.timeDeltaMinutes || 0) >= 0));
  }

  private isSubmitValid(): boolean {
    if (!this.submitForm.recipientName.trim() || !this.submitForm.channel || !this.submitForm.recipientMasked.trim() || !this.submitForm.expiresAt) return false;
    return new Date(this.submitForm.expiresAt).getTime() > Date.now();
  }

  private handleActionError(error: any, fallback: string): void {
    this.busyKey = undefined;
    this.actionMessage = error?.error?.message || error?.error?.error?.message || error?.message || fallback;
    this.cdr.markForCheck();
  }

  private hasPermission(permission: string): boolean {
    const user = this.auth.snapshot();
    const roles = user.roles ?? [];
    return user.permissions?.includes(permission) === true || roles.some(role => role === 'ADMIN' || role === 'ROLE_ADMIN');
  }

  private emptyDraft(): DraftForm { return { title: '', reason: '', items: [this.emptyItem()] }; }
  private emptyItem(): OsAdditionalItemDraft {
    return { operation: 'ADD', itemType: 'SERVICE', description: '', quantity: 1, unit: 'un', amountDelta: 0, timeDeltaMinutes: 0 };
  }
  private emptySubmit(): SubmitForm { return { recipientName: '', channel: 'LINK_SEGURO', recipientMasked: '', expiresAt: '' }; }
}
