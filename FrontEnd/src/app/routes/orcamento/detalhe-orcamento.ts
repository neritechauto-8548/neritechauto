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
  selector: 'app-detalhe-orcamento',
  standalone: true,
  imports: [CommonModule, RouterLink, PageHeader, SkeletonModule],
  templateUrl: './detalhe-orcamento.html',
  styleUrl: './detalhe-orcamento.scss',
})
export class DetalheOrcamentoComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly location = inject(Location);
  private readonly service = inject(OrcamentoListService);
  private readonly compositionService = inject(OrcamentoCompositionService);

  budget: OrcamentoListItem | null = null;
  composition: BudgetComposition | null = null;
  isLoading = true;
  loadError = false;
  forbidden = false;

  readonly tabs: { label: string; contract: string; active: boolean; route?: string }[] = [
    { label: 'Resumo', contract: 'ORC-003', active: true },
    { label: 'Itens', contract: 'ORC-004', active: false, route: 'itens' },
    { label: 'Aprovação', contract: 'ORC-007', active: false },
    { label: 'Comunicação', contract: 'ORC-005', active: false },
    { label: 'Versões', contract: 'ORC-006', active: false },
    { label: 'Atividade', contract: 'ORC-003', active: false },
  ];

  ngOnInit() {
    this.load();
  }

  get budgetId() {
    return this.budget?.id ?? null;
  }

  get versionLabel() {
    const version = this.budget?.versaoAtual;
    return version && version > 0 ? `Versão ${version}` : 'Rascunho sem versão enviada';
  }

  get validityLabel() {
    return this.budget?.validadeEm ? 'Validade persistida' : 'Validade comercial ainda não definida';
  }

  get canContinueEdit() {
    return Boolean(this.budget?.allowedActions?.includes('CONTINUE_EDIT'));
  }

  get hasMutableCapability() {
    return this.canContinueEdit || Boolean(this.budget?.allowedActions?.some(action => action !== 'OPEN'));
  }

  get calculationLabel() {
    switch (this.composition?.calculationStatus) {
      case 'CURRENT': return 'Cálculo atualizado';
      case 'PENDING': return 'Recalculando';
      case 'ERROR': return 'Revisão necessária';
      default: return 'Sem itens';
    }
  }

  get compositionReady() {
    return this.composition?.calculationStatus === 'CURRENT' && (this.composition?.lineCount ?? 0) > 0;
  }

  get explicitDiscountTotal() {
    return this.allLines.reduce((total, line) => total + Number(line.discountAmount || 0), 0);
  }

  get packageAdjustmentTotal() {
    return (this.composition?.groups || []).reduce(
      (total, group) => total + Number(group.packageAdjustmentAmount || 0),
      0
    );
  }

  get pendingDiscountApprovals() {
    return this.allLines.filter(line => line.discountAuthorityStatus === 'PENDING_APPROVAL').length;
  }

  get allLines(): CompositionLine[] {
    return (this.composition?.groups || []).flatMap(group => group.lines || []);
  }

  get visibleGroups(): CompositionGroup[] {
    return (this.composition?.groups || []).filter(group => group.visibility === 'CUSTOMER_VISIBLE');
  }

  private load() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!Number.isInteger(id) || id <= 0) {
      this.isLoading = false;
      this.loadError = true;
      return;
    }

    this.budget = null;
    this.composition = null;
    this.isLoading = true;
    this.loadError = false;
    this.forbidden = false;

    forkJoin({
      budget: this.service.getById(id),
      composition: this.compositionService.get(id),
    }).subscribe({
      next: result => {
        this.budget = result.budget;
        this.composition = result.composition;
        this.isLoading = false;
      },
      error: error => {
        this.isLoading = false;
        this.loadError = true;
        this.forbidden = error?.status === 403;
      },
    });
  }

  voltar() {
    const returnUrl = history.state?.returnUrl;
    if (typeof returnUrl === 'string' && returnUrl.startsWith('/orcamentos')) {
      this.router.navigateByUrl(returnUrl);
      return;
    }
    this.location.back();
  }

  abrirCliente() {
    if (this.budget?.cliente?.id) this.router.navigate(['/clientes', this.budget.cliente.id]);
  }

  abrirVeiculo() {
    if (this.budget?.veiculo?.id) this.router.navigate(['/veiculos', this.budget.veiculo.id]);
  }

  abrirComposicao() {
    if (this.budgetId) this.router.navigate(['/orcamentos', this.budgetId, 'itens']);
  }

  executarProximaAcao() {
    if (!this.budget) return;
    if (this.budget.proximaAcao === 'CONTINUAR_EDICAO') {
      this.abrirComposicao();
    }
  }

  tentarNovamente() {
    this.load();
  }

  formatCurrency(amount?: number | null) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
      .format(Number(amount ?? 0));
  }

  statusLabel() {
    const status = this.budget?.status || 'RASCUNHO';
    return status
      .toLowerCase()
      .replaceAll('_', ' ')
      .replace(/^./, char => char.toUpperCase());
  }

  statusClass() {
    switch (this.budget?.status) {
      case 'APROVADO':
      case 'CONVERTIDO':
        return 'object-status--success';
      case 'ENVIADO':
      case 'AGUARDANDO_APROVACAO':
      case 'PARCIAL':
        return 'object-status--warning';
      case 'RECUSADO':
      case 'EXPIRADO':
      case 'CANCELADO':
        return 'object-status--danger';
      default:
        return 'object-status--neutral';
    }
  }

  nextActionLabel() {
    const labels: Record<string, string> = {
      CONTINUAR_EDICAO: 'Continuar edição',
      ACOMPANHAR_APROVACAO: 'Acompanhar aprovação',
      CONVERTER_EM_OS: 'Converter em OS',
      REVISAR_DECISAO: 'Revisar decisão',
      REGISTRAR_FOLLOW_UP: 'Registrar follow-up',
      REVALIDAR: 'Revalidar proposta',
      ABRIR_OS: 'Abrir OS',
      CONSULTAR_HISTORICO: 'Consultar histórico',
      REVISAR_DETALHES: 'Revisar detalhes',
    };
    return labels[this.budget?.proximaAcao || ''] || 'Revisar detalhes';
  }
}
