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

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!Number.isInteger(id) || id <= 0) {
      this.isLoading = false;
      this.loadError = true;
      return;
    }

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
    this.ngOnInit();
  }

  formatCurrency() {
    const amount = Number(this.budget?.total?.amount ?? 0);
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(amount);
  }

  statusLabel() {
    const status = this.budget?.status || 'RASCUNHO';
    return status.toLowerCase().replaceAll('_', ' ').replace(/^./, char => char.toUpperCase());
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
