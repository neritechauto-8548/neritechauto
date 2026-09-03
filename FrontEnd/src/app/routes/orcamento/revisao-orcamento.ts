import { CommonModule, Location } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PageHeader } from '@shared';
import { SkeletonModule } from 'primeng/skeleton';
import { forkJoin } from 'rxjs';

import {
  BudgetComposition,
  CompositionGroup,
  CompositionLine,
  OrcamentoCompositionService,
} from './orcamento-composition.service';
import { OrcamentoListItem, OrcamentoListService } from './orcamento-list.service';

@Component({
  selector: 'app-revisao-orcamento',
  standalone: true,
  imports: [CommonModule, RouterLink, PageHeader, SkeletonModule],
  templateUrl: './revisao-orcamento.html',
  styleUrl: './revisao-orcamento.scss',
})
export class RevisaoOrcamentoComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly location = inject(Location);
  private readonly listService = inject(OrcamentoListService);
  private readonly compositionService = inject(OrcamentoCompositionService);

  budgetId = 0;
  budget: OrcamentoListItem | null = null;
  composition: BudgetComposition | null = null;
  isLoading = true;
  loadError = false;
  forbidden = false;

  ngOnInit() {
    this.load();
  }

  get visibleGroups(): CompositionGroup[] {
    return (this.composition?.groups || []).filter(group => group.visibility === 'CUSTOMER_VISIBLE');
  }

  get visibleLines(): CompositionLine[] {
    return this.visibleGroups.flatMap(group => group.lines || []);
  }

  get isReady() {
    return Boolean(
      this.composition?.canReview &&
      this.composition.calculationStatus === 'CURRENT' &&
      this.composition.blockers.length === 0 &&
      this.visibleLines.length > 0
    );
  }

  get readinessLabel() {
    if (!this.composition) return 'Indisponível';
    if (this.composition.blockers.length > 0) return 'Revisão bloqueada';
    if (this.composition.calculationStatus !== 'CURRENT') return 'Cálculo pendente';
    if (this.visibleLines.length === 0) return 'Sem itens visíveis';
    return this.composition.canReview ? 'Pronto para comunicação' : 'Revisão pendente';
  }

  formatCurrency(value: number | null | undefined) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
      .format(Number(value ?? 0));
  }

  lineTypeLabel(type: string) {
    const labels: Record<string, string> = {
      PART: 'Peça',
      LABOR: 'Serviço',
      FEE: 'Taxa',
      SUBLET: 'Terceiro',
      DISCOUNT: 'Desconto',
      NOTE: 'Observação',
    };
    return labels[type] || type;
  }

  voltarParaItens() {
    this.router.navigate(['/orcamentos', this.budgetId, 'itens']);
  }

  voltar() {
    this.location.back();
  }

  private load() {
    this.budgetId = Number(this.route.snapshot.paramMap.get('id'));
    if (!Number.isInteger(this.budgetId) || this.budgetId <= 0) {
      this.isLoading = false;
      this.loadError = true;
      return;
    }

    this.isLoading = true;
    this.loadError = false;
    this.forbidden = false;

    forkJoin({
      budget: this.listService.getById(this.budgetId),
      composition: this.compositionService.get(this.budgetId),
    }).subscribe({
      next: result => {
        this.budget = result.budget;
        this.composition = result.composition;
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
}
