import { CommonModule } from '@angular/common';
import { Component, DestroyRef, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PageHeader } from '@shared';
import { SkeletonModule } from 'primeng/skeleton';
import {
  EMPTY,
  catchError,
  combineLatest,
  debounceTime,
  distinctUntilChanged,
  filter,
  finalize,
  forkJoin,
  Observable,
  switchMap,
  startWith,
  tap,
} from 'rxjs';

import {
  BudgetComposition,
  CatalogSearchItem,
  CatalogSearchResponse,
  CompositionGroup,
  CompositionLine,
  OrcamentoCompositionService,
} from './orcamento-composition.service';
import { OrcamentoListItem, OrcamentoListService } from './orcamento-list.service';

type PendingDeletion =
  | { kind: 'group'; groupId: number; label: string }
  | { kind: 'line'; groupId: number; itemId: number; label: string };

@Component({
  selector: 'app-itens-orcamento',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, PageHeader, SkeletonModule],
  templateUrl: './itens-orcamento.html',
  styleUrl: './itens-orcamento.scss',
})
export class ItensOrcamentoComponent implements OnInit {
  @ViewChild('cancelDeletion') private cancelDeletionButton?: ElementRef<HTMLButtonElement>;
  @ViewChild('catalogSearchInput') private catalogSearchInput?: ElementRef<HTMLInputElement>;

  private readonly route = inject(ActivatedRoute);
  private readonly destroyRef = inject(DestroyRef);
  private readonly budgetService = inject(OrcamentoListService);
  private readonly compositionService = inject(OrcamentoCompositionService);

  budgetId = 0;
  budget: OrcamentoListItem | null = null;
  composition: BudgetComposition | null = null;
  isLoading = true;
  loadError = false;
  forbidden = false;
  conflict = false;
  isSaving = false;
  saveMessage = 'Sincronizado com o servidor';
  mutationError = '';
  showGroupForm = false;
  selectedGroupId: number | null = null;
  activeAddingCatalogId: number | null = null;
  catalogResponse: CatalogSearchResponse | null = null;
  isSearching = false;
  editingGroupId: number | null = null;
  editingLineId: number | null = null;
  pendingDeletion: PendingDeletion | null = null;

  readonly searchControl = new FormControl('', { nonNullable: true });
  readonly catalogTypeControl = new FormControl<'ALL' | 'KIT'>('ALL', { nonNullable: true });
  readonly groupForm = new FormGroup({
    title: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(3), Validators.maxLength(120)],
    }),
    customerDescription: new FormControl('', {
      nonNullable: true,
      validators: [Validators.maxLength(2000)],
    }),
    recommended: new FormControl(false, { nonNullable: true }),
  });
  readonly editGroupForm = new FormGroup({
    title: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(3), Validators.maxLength(120)],
    }),
    customerDescription: new FormControl('', {
      nonNullable: true,
      validators: [Validators.maxLength(2000)],
    }),
    internalNote: new FormControl('', {
      nonNullable: true,
      validators: [Validators.maxLength(4000)],
    }),
    recommended: new FormControl(false, { nonNullable: true }),
    visibility: new FormControl<'CUSTOMER_VISIBLE' | 'INTERNAL_ONLY'>('CUSTOMER_VISIBLE', {
      nonNullable: true,
    }),
  });
  readonly lineQuantityControl = new FormControl(1, {
    nonNullable: true,
    validators: [Validators.required, Validators.min(0.001), Validators.max(999_999_999)],
  });

  readonly tabs = [
    { label: 'Resumo', contract: 'ORC-003', route: '' },
    { label: 'Itens', contract: 'ORC-004', active: true },
    { label: 'Aprovação', contract: 'ORC-007' },
    { label: 'Comunicação', contract: 'ORC-005' },
    { label: 'Versões', contract: 'ORC-006' },
    { label: 'Atividade', contract: 'ORC-003' },
  ];

  ngOnInit() {
    this.configureCatalogSearch();
    this.load();
  }

  load() {
    this.budgetId = Number(this.route.snapshot.paramMap.get('id'));
    if (!Number.isInteger(this.budgetId) || this.budgetId <= 0) {
      this.isLoading = false;
      this.loadError = true;
      return;
    }

    this.isLoading = true;
    this.loadError = false;
    this.forbidden = false;
    this.conflict = false;
    forkJoin({
      budget: this.budgetService.getById(this.budgetId),
      composition: this.compositionService.get(this.budgetId),
    })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: result => {
          this.budget = result.budget;
          this.applyComposition(result.composition);
          this.isLoading = false;
        },
        error: error => {
          this.budget = null;
          this.composition = null;
          this.isLoading = false;
          this.loadError = true;
          this.forbidden = error?.status === 403;
        },
      });
  }

  selectGroup(group: CompositionGroup) {
    this.selectedGroupId = group.id;
    this.mutationError = '';
  }

  createGroup() {
    if (!this.composition || this.groupForm.invalid || this.isSaving) {
      this.groupForm.markAllAsTouched();
      return;
    }
    const value = this.groupForm.getRawValue();
    this.beginMutation('Criando grupo…');
    this.compositionService
      .createGroup(this.budgetId, {
        expectedRevision: this.composition.revision,
        title: value.title,
        customerDescription: value.customerDescription || null,
        recommended: value.recommended,
        visibility: 'CUSTOMER_VISIBLE',
      })
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => (this.isSaving = false))
      )
      .subscribe({
        next: composition => {
          this.applyComposition(composition, true);
          this.showGroupForm = false;
          this.groupForm.reset({ title: '', customerDescription: '', recommended: false });
          this.saveMessage = 'Grupo salvo e totais recalculados';
        },
        error: error => this.handleMutationError(error),
      });
  }

  addCatalogItem(item: CatalogSearchItem) {
    if (!this.composition || this.isSaving) return;
    if (item.lineType === 'KIT') {
      const idempotencyKey = `kit-${globalThis.crypto.randomUUID()}`;
      this.activeAddingCatalogId = item.id;
      this.beginMutation('Instanciando versão imutável do kit…');
      this.compositionService
        .instantiateKit(this.budgetId, item.id, idempotencyKey, {
          expectedRevision: this.composition.revision,
          quantity: 1,
          targetPosition: this.composition.groups.length,
        })
        .pipe(
          takeUntilDestroyed(this.destroyRef),
          finalize(() => {
            this.isSaving = false;
            this.activeAddingCatalogId = null;
          })
        )
        .subscribe({
          next: composition => {
            this.applyComposition(composition, true);
            this.saveMessage = `Kit v${item.catalogVersion ?? 0} instanciado por snapshot`;
          },
          error: error => this.handleMutationError(error),
        });
      return;
    }
    if (!this.selectedGroupId) return;
    this.activeAddingCatalogId = item.id;
    this.beginMutation('Aplicando preço canônico…');
    this.compositionService
      .addCatalogItem(this.budgetId, this.selectedGroupId, {
        expectedRevision: this.composition.revision,
        lineType: item.lineType,
        catalogItemId: item.id,
        quantity: 1,
      })
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => {
          this.isSaving = false;
          this.activeAddingCatalogId = null;
        })
      )
      .subscribe({
        next: composition => {
          this.applyComposition(composition);
          this.saveMessage = 'Item salvo com snapshot do catálogo';
        },
        error: error => this.handleMutationError(error),
      });
  }

  activateKitSearch() {
    this.catalogTypeControl.setValue('KIT');
    this.catalogResponse = null;
    setTimeout(() => this.catalogSearchInput?.nativeElement.focus());
  }

  clearCatalogType() {
    this.catalogTypeControl.setValue('ALL');
    this.catalogResponse = null;
    setTimeout(() => this.catalogSearchInput?.nativeElement.focus());
  }

  startEditGroup(group: CompositionGroup) {
    this.selectGroup(group);
    this.editingLineId = null;
    this.editingGroupId = group.id;
    this.editGroupForm.setValue({
      title: group.title,
      customerDescription: group.customerDescription ?? '',
      internalNote: group.internalNote ?? '',
      recommended: group.recommended,
      visibility: group.visibility,
    });
  }

  saveGroup(groupId: number) {
    if (!this.composition || this.editGroupForm.invalid || this.isSaving) {
      this.editGroupForm.markAllAsTouched();
      return;
    }
    const value = this.editGroupForm.getRawValue();
    this.runMutation(
      this.compositionService.updateGroup(this.budgetId, groupId, {
        expectedRevision: this.composition.revision,
        title: value.title,
        customerDescription: value.customerDescription || null,
        internalNote: value.internalNote || null,
        recommended: value.recommended,
        visibility: value.visibility,
      }),
      'Atualizando grupo…',
      'Grupo atualizado e totais recalculados',
      () => (this.editingGroupId = null)
    );
  }

  duplicateGroup(group: CompositionGroup) {
    if (!this.composition) return;
    this.runMutation(
      this.compositionService.duplicateGroup(this.budgetId, group.id, this.composition.revision),
      'Duplicando snapshots do grupo…',
      'Grupo duplicado sem reler preços do catálogo',
      composition => (this.selectedGroupId = composition.groups.at(-1)?.id ?? null)
    );
  }

  moveGroup(index: number, offset: -1 | 1) {
    if (!this.composition) return;
    const target = index + offset;
    if (target < 0 || target >= this.composition.groups.length) return;
    const orderedIds = this.composition.groups.map(group => group.id);
    [orderedIds[index], orderedIds[target]] = [orderedIds[target], orderedIds[index]];
    this.runMutation(
      this.compositionService.reorderGroups(this.budgetId, this.composition.revision, orderedIds),
      'Reordenando grupos…',
      'Ordem dos grupos salva no servidor'
    );
  }

  startEditLine(line: CompositionLine) {
    this.editingGroupId = null;
    this.editingLineId = line.id;
    this.lineQuantityControl.setValue(line.quantity);
  }

  saveLine(groupId: number, lineId: number) {
    if (!this.composition || this.lineQuantityControl.invalid || this.isSaving) {
      this.lineQuantityControl.markAsTouched();
      return;
    }
    this.runMutation(
      this.compositionService.updateLine(
        this.budgetId,
        groupId,
        lineId,
        this.composition.revision,
        this.lineQuantityControl.getRawValue()
      ),
      'Recalculando quantidade…',
      'Quantidade e disponibilidade recalculadas',
      () => (this.editingLineId = null)
    );
  }

  duplicateLine(groupId: number, line: CompositionLine) {
    if (!this.composition) return;
    this.runMutation(
      this.compositionService.duplicateLine(
        this.budgetId,
        groupId,
        line.id,
        this.composition.revision
      ),
      'Duplicando snapshot do item…',
      'Item duplicado com o snapshot comercial preservado'
    );
  }

  moveLine(group: CompositionGroup, index: number, offset: -1 | 1) {
    if (!this.composition) return;
    const target = index + offset;
    if (target < 0 || target >= group.lines.length) return;
    const orderedIds = group.lines.map(line => line.id);
    [orderedIds[index], orderedIds[target]] = [orderedIds[target], orderedIds[index]];
    this.runMutation(
      this.compositionService.reorderLines(
        this.budgetId,
        group.id,
        this.composition.revision,
        orderedIds
      ),
      'Reordenando itens…',
      'Ordem dos itens salva no servidor'
    );
  }

  requestDeleteGroup(group: CompositionGroup) {
    this.pendingDeletion = { kind: 'group', groupId: group.id, label: group.title };
    this.focusDeletionDialog();
  }

  requestDeleteLine(groupId: number, line: CompositionLine) {
    this.pendingDeletion = {
      kind: 'line',
      groupId,
      itemId: line.id,
      label: line.description,
    };
    this.focusDeletionDialog();
  }

  confirmDeletion() {
    if (!this.composition || !this.pendingDeletion) return;
    const pending = this.pendingDeletion;
    const request$ =
      pending.kind === 'group'
        ? this.compositionService.deleteGroup(
            this.budgetId,
            pending.groupId,
            this.composition.revision
          )
        : this.compositionService.deleteLine(
            this.budgetId,
            pending.groupId,
            pending.itemId,
            this.composition.revision
          );
    this.runMutation(
      request$,
      'Removendo da composição…',
      'Remoção concluída e totais recalculados',
      () => {
        this.pendingDeletion = null;
        this.editingGroupId = null;
        this.editingLineId = null;
      }
    );
  }

  formatCurrency(value: number | null | undefined) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(
      Number(value ?? 0)
    );
  }

  lineTypeLabel(type: string) {
    const labels: Record<string, string> = {
      PART: 'Peça',
      LABOR: 'Mão de obra',
      KIT: 'Kit',
      FEE: 'Taxa',
      SUBLET: 'Terceiro',
      DISCOUNT: 'Desconto',
      NOTE: 'Nota',
    };
    return labels[type] || type;
  }

  availabilityLabel(status: string) {
    const labels: Record<string, string> = {
      AVAILABLE: 'Disponível',
      PARTIAL: 'Disponibilidade parcial',
      NEEDED: 'Necessário',
      NOT_APPLICABLE: 'Não se aplica',
    };
    return labels[status] || status;
  }

  trackGroup(_: number, group: CompositionGroup) {
    return group.id;
  }

  private configureCatalogSearch() {
    combineLatest([
      this.searchControl.valueChanges.pipe(startWith(this.searchControl.value), debounceTime(300)),
      this.catalogTypeControl.valueChanges.pipe(startWith(this.catalogTypeControl.value)),
    ])
      .pipe(
        distinctUntilChanged(),
        tap(([value]) => {
          this.mutationError = '';
          if (value.trim().length < 2) {
            this.catalogResponse = null;
            this.isSearching = false;
          }
        }),
        filter(([value]) => value.trim().length >= 2),
        tap(() => (this.isSearching = true)),
        switchMap(([value, type]) =>
          this.compositionService
            .searchCatalog(value.trim(), type === 'KIT' ? 'KIT' : undefined)
            .pipe(
              catchError(() => {
                this.isSearching = false;
                this.catalogResponse = { query: value.trim(), items: [], truncated: false };
                this.mutationError = 'Não foi possível consultar o catálogo agora.';
                return EMPTY;
              })
            )
        ),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe(result => {
        this.catalogResponse = result;
        this.isSearching = false;
      });
  }

  private applyComposition(composition: BudgetComposition, selectLast = false) {
    this.composition = composition;
    this.conflict = false;
    this.mutationError = '';
    if (selectLast && composition.groups.length) {
      this.selectedGroupId = composition.groups.at(-1)?.id ?? null;
    } else if (
      this.selectedGroupId === null ||
      !composition.groups.some(group => group.id === this.selectedGroupId)
    ) {
      this.selectedGroupId = composition.groups[0]?.id ?? null;
    }
  }

  private beginMutation(message: string) {
    this.isSaving = true;
    this.conflict = false;
    this.mutationError = '';
    this.saveMessage = message;
  }

  private focusDeletionDialog() {
    setTimeout(() => this.cancelDeletionButton?.nativeElement.focus());
  }

  private runMutation(
    request$: Observable<BudgetComposition>,
    progressMessage: string,
    successMessage: string,
    afterSuccess?: (composition: BudgetComposition) => void
  ) {
    if (this.isSaving) return;
    this.beginMutation(progressMessage);
    request$
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => (this.isSaving = false))
      )
      .subscribe({
        next: composition => {
          this.applyComposition(composition);
          afterSuccess?.(composition);
          this.saveMessage = successMessage;
        },
        error: error => this.handleMutationError(error),
      });
  }

  private handleMutationError(error: { status?: number }) {
    if (error?.status === 409) {
      this.conflict = true;
      this.mutationError = 'Outra alteração foi salva. Recarregue antes de continuar.';
      this.saveMessage = 'Conflito detectado';
      return;
    }
    if (error?.status === 403) {
      this.mutationError = 'Seu perfil não pode editar esta composição.';
      this.saveMessage = 'Edição não autorizada';
      return;
    }
    this.mutationError = 'A alteração não foi salva. A composição anterior foi preservada.';
    this.saveMessage = 'Falha ao sincronizar';
  }
}
