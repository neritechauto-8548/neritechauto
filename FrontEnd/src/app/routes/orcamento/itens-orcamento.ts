import { CommonModule } from '@angular/common';
import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PageHeader } from '@shared';
import { SkeletonModule } from 'primeng/skeleton';
import {
  EMPTY,
  catchError,
  debounceTime,
  distinctUntilChanged,
  filter,
  finalize,
  forkJoin,
  switchMap,
  tap,
} from 'rxjs';

import {
  BudgetComposition,
  CatalogSearchItem,
  CatalogSearchResponse,
  CompositionGroup,
  OrcamentoCompositionService,
} from './orcamento-composition.service';
import { OrcamentoListItem, OrcamentoListService } from './orcamento-list.service';

@Component({
  selector: 'app-itens-orcamento',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, PageHeader, SkeletonModule],
  templateUrl: './itens-orcamento.html',
  styleUrl: './itens-orcamento.scss',
})
export class ItensOrcamentoComponent implements OnInit {
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

  readonly searchControl = new FormControl('', { nonNullable: true });
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
    if (!this.composition || !this.selectedGroupId || this.isSaving) return;
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

  formatCurrency(value: number | null | undefined) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(
      Number(value ?? 0)
    );
  }

  lineTypeLabel(type: string) {
    const labels: Record<string, string> = {
      PART: 'Peça',
      LABOR: 'Mão de obra',
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
    this.searchControl.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        tap(value => {
          this.mutationError = '';
          if (value.trim().length < 2) {
            this.catalogResponse = null;
            this.isSearching = false;
          }
        }),
        filter(value => value.trim().length >= 2),
        tap(() => (this.isSearching = true)),
        switchMap(value =>
          this.compositionService.searchCatalog(value.trim()).pipe(
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
