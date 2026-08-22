import { CommonModule, Location } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PageHeader } from '@shared';
import { SkeletonModule } from 'primeng/skeleton';

import { OrcamentoListItem, OrcamentoListService } from './orcamento-list.service';

@Component({
  selector: 'app-detalhe-orcamento',
  standalone: true,
  imports: [CommonModule, PageHeader, SkeletonModule],
  templateUrl: './detalhe-orcamento.html',
  styleUrl: './detalhe-orcamento.scss',
})
export class DetalheOrcamentoComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly location = inject(Location);
  private readonly service = inject(OrcamentoListService);

  budget: OrcamentoListItem | null = null;
  isLoading = true;
  loadError = false;
  forbidden = false;

  readonly tabs = [
    { label: 'Resumo', contract: 'ORC-003', active: true },
    { label: 'Itens', contract: 'ORC-004', active: false },
    { label: 'Aprovação', contract: 'ORC-007', active: false },
    { label: 'Comunicação', contract: 'ORC-005', active: false },
    { label: 'Versões', contract: 'ORC-006', active: false },
    { label: 'Atividade', contract: 'ORC-003', active: false },
  ];

  ngOnInit() {
    this.load();
  }

  get versionLabel() {
    const version = this.budget?.versaoAtual;
    return version && version > 0 ? `Versão ${version}` : 'Sem versão enviada';
  }

  get validityLabel() {
    return this.budget?.validadeEm ? 'Validade persistida' : 'Contrato de validade pendente';
  }

  get hasMutableCapability() {
    return Boolean(this.budget?.allowedActions?.some(action => action !== 'OPEN'));
  }

  private load() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!Number.isInteger(id) || id <= 0) {
      this.isLoading = false;
      this.loadError = true;
      return;
    }

    this.budget = null;
    this.isLoading = true;
    this.loadError = false;
    this.forbidden = false;

    this.service.getById(id).subscribe({
      next: budget => {
        this.budget = budget;
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

  tentarNovamente() {
    this.load();
  }

  formatCurrency() {
    const amount = Number(this.budget?.total?.amount ?? 0);
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(amount);
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
