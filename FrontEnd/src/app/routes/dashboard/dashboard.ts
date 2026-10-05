import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NgApexchartsModule } from 'ng-apexcharts';
import { HttpErrorResponse } from '@angular/common/http';

import { DashboardDTO, DashboardService } from './dashboard.service';

type DashboardView = 'standard' | 'managerial' | 'financial' | 'estimates';
type DashboardPeriod = 'today' | '7d' | '30d' | 'month' | 'custom';

interface DashboardKpi {
  label: string;
  value: string;
  hint: string;
  state?: 'normal' | 'warning' | 'danger';
  delta?: string;
  deltaTone?: 'positive' | 'negative' | 'neutral';
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
  imports: [CommonModule, FormsModule, RouterModule, NgApexchartsModule],
})
export class Dashboard implements OnInit {
  private readonly dashboardService = inject(DashboardService);
  private readonly router = inject(Router);

  readonly views: Array<{ id: DashboardView; label: string }> = [
    { id: 'standard', label: 'Padrão' },
    { id: 'managerial', label: 'Gerencial' },
    { id: 'financial', label: 'Financeiro' },
    { id: 'estimates', label: 'Orçamento' },
  ];

  selectedView: DashboardView = 'standard';
  selectedPeriod: DashboardPeriod = 'month';
  selectedComparison = 'previous';
  customStartDate = '';
  customEndDate = '';

  loading = true;
  error = false;
  forbidden = false;
  lastUpdated: Date | null = null;
  stats: DashboardDTO | null = null;

  chartOptions: any = {};

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    if (this.selectedPeriod === 'custom' && (!this.customStartDate || !this.customEndDate)) {
      return;
    }

    this.loading = true;
    this.error = false;
    this.forbidden = false;

    this.dashboardService
      .getDashboardData(this.selectedPeriod, this.selectedComparison, this.customStartDate, this.customEndDate)
      .subscribe({
        next: data => {
          this.stats = data;
          this.lastUpdated = data.geradoEm ? new Date(data.geradoEm) : new Date();
          this.buildChart();
          this.loading = false;
        },
        error: (error: HttpErrorResponse) => {
          this.loading = false;
          this.error = true;
          this.forbidden = error.status === 403;
        },
      });
  }

  selectView(view: DashboardView): void {
    if (this.selectedView === view) return;
    this.selectedView = view;
    this.buildChart();
  }

  onPeriodChange(): void {
    if (this.selectedPeriod !== 'custom') {
      this.loadDashboard();
    }
  }

  onCustomDateChange(): void {
    if (
      this.selectedPeriod === 'custom' &&
      this.customStartDate &&
      this.customEndDate &&
      this.customStartDate <= this.customEndDate
    ) {
      this.loadDashboard();
    }
  }

  onComparisonChange(): void {
    this.loadDashboard();
  }

  retry(): void {
    this.loadDashboard();
  }

  get subtitle(): string {
    switch (this.selectedView) {
      case 'managerial': return 'Desempenho comercial e operacional da oficina.';
      case 'financial': return 'Visão consolidada da situação financeira.';
      case 'estimates': return 'Acompanhamento do funil de orçamentos.';
      default: return 'Visão resumida da operação da oficina.';
    }
  }

  get periodLabel(): string {
    if (this.stats?.inicio && this.stats?.fim) {
      const start = new Date(`${this.stats.inicio}T00:00:00`);
      const end = new Date(`${this.stats.fim}T00:00:00`);
      if (this.selectedPeriod === 'custom') {
        return `${start.toLocaleDateString('pt-BR')} — ${end.toLocaleDateString('pt-BR')}`;
      }
    }

    switch (this.selectedPeriod) {
      case 'today': return 'Hoje';
      case '7d': return 'Últimos 7 dias';
      case '30d': return 'Últimos 30 dias';
      case 'custom': return 'Período personalizado';
      default: return 'Este mês';
    }
  }

  get comparisonLabel(): string {
    return this.selectedComparison === 'same' ? 'Mesmo período do ano anterior' : 'Período anterior';
  }

  get standardKpis(): DashboardKpi[] {
    const s = this.stats;
    return [
      {
        label: 'OS em andamento',
        value: this.formatNumber(s?.osEmAndamento),
        hint: 'Atendimentos ativos no período',
      },
      {
        label: 'OS concluídas',
        value: this.formatNumber(s?.osConcluidas),
        hint: 'No período selecionado',
        delta: this.countDelta(s?.osConcluidas, s?.osConcluidasComparacao),
        deltaTone: this.deltaTone(s?.osConcluidas, s?.osConcluidasComparacao),
      },
      { label: 'Clientes ativos', value: this.formatNumber(s?.totalClientes), hint: 'Base cadastral atual' },
      {
        label: 'Veículos em atraso',
        value: this.formatNumber(s?.veiculosEmAtraso),
        hint: 'Exigem acompanhamento',
        state: s?.veiculosEmAtraso ? 'warning' : 'normal',
      },
      {
        label: 'Ticket médio',
        value: this.formatCurrency(s?.ticketMedio),
        hint: 'Valor médio por OS concluída',
        delta: this.currencyDelta(s?.ticketMedio, s?.ticketMedioComparacao),
        deltaTone: this.deltaTone(s?.ticketMedio, s?.ticketMedioComparacao),
      },
    ];
  }

  get managerialKpis(): DashboardKpi[] {
    const s = this.stats;
    return [
      {
        label: 'Faturamento',
        value: this.formatCurrency(s?.faturamentoMes),
        hint: 'Recebimentos no período',
        delta: this.currencyDelta(s?.faturamentoMes, s?.faturamentoComparacao),
        deltaTone: this.deltaTone(s?.faturamentoMes, s?.faturamentoComparacao),
      },
      {
        label: 'Ticket médio',
        value: this.formatCurrency(s?.ticketMedio),
        hint: 'Valor médio por OS concluída',
        delta: this.currencyDelta(s?.ticketMedio, s?.ticketMedioComparacao),
        deltaTone: this.deltaTone(s?.ticketMedio, s?.ticketMedioComparacao),
      },
      {
        label: 'OS concluídas',
        value: this.formatNumber(s?.osConcluidas),
        hint: 'Produção no período',
        delta: this.countDelta(s?.osConcluidas, s?.osConcluidasComparacao),
        deltaTone: this.deltaTone(s?.osConcluidas, s?.osConcluidasComparacao),
      },
      { label: 'Clientes atendidos', value: this.formatNumber(s?.totalClientes), hint: 'Base ativa atual' },
    ];
  }

  get financialKpis(): DashboardKpi[] {
    const s = this.stats;
    return [
      {
        label: 'Contas a receber',
        value: this.formatCurrency(s?.contasReceber),
        hint: 'Carteira em aberto',
      },
      {
        label: 'Contas a pagar',
        value: this.formatCurrency(s?.contasPagar),
        hint: 'Compromissos em aberto',
      },
      {
        label: 'Valores vencidos',
        value: this.formatCurrency(s?.valoresVencidos),
        hint: 'Necessitam cobrança',
        state: s?.valoresVencidos ? 'danger' : 'normal',
      },
      { label: 'Resultado do período', value: this.formatCurrency(s?.lucroMes), hint: 'Receitas menos despesas' },
    ];
  }

  get estimateKpis(): DashboardKpi[] {
    const s = this.stats;
    return [
      { label: 'Abertos', value: this.formatNumber(s?.abertosMes), hint: 'No período selecionado' },
      { label: 'Autorizados', value: this.formatNumber(s?.autorizadosMes), hint: 'No período selecionado' },
      {
        label: 'Cancelados',
        value: this.formatNumber(s?.canceladosMes),
        hint: 'No período selecionado',
        state: s?.canceladosMes ? 'danger' : 'normal',
      },
      { label: 'Fechados', value: this.formatNumber(s?.fechadosMes), hint: 'No período selecionado' },
    ];
  }

  get currentKpis(): DashboardKpi[] {
    switch (this.selectedView) {
      case 'managerial': return this.managerialKpis;
      case 'financial': return this.financialKpis;
      case 'estimates': return this.estimateKpis;
      default: return this.standardKpis;
    }
  }

  get statusRows() {
    const s = this.stats;
    return [
      { label: 'Abertos', value: this.formatNumber(s?.abertosTotal), tone: 'neutral' },
      { label: 'Autorizados', value: this.formatNumber(s?.autorizadosTotal), tone: 'info' },
      { label: 'Cancelados', value: this.formatNumber(s?.canceladosTotal), tone: 'danger' },
      { label: 'Fechados', value: this.formatNumber(s?.fechadosTotal), tone: 'success' },
    ];
  }

  get financialRows() {
    const s = this.stats;
    return [
      { label: 'Contas a receber', value: this.formatCurrency(s?.contasReceber), detail: 'Em aberto' },
      { label: 'Contas a pagar', value: this.formatCurrency(s?.contasPagar), detail: 'Em aberto' },
      { label: 'Vencidos', value: this.formatCurrency(s?.valoresVencidos), detail: 'Cobrança necessária' },
      { label: 'Resultado', value: this.formatCurrency(s?.lucroMes), detail: 'Receita - despesa' },
    ];
  }

  openOperational(): void {
    this.router.navigate(['/os']);
  }

  openFinancial(): void {
    this.router.navigate(['/financeiro/receber']);
  }

  openEstimates(): void {
    this.router.navigate(['/orcamento/orcamento']);
  }

  private buildChart(): void {
    const s = this.stats;
    if (!s) return;

    this.chartOptions = {
      series: [
        { name: this.selectedView === 'financial' ? 'Recebimentos' : 'Faturamento', data: s.historicoFaturamento },
        { name: 'Serviços', data: s.historicoServicos },
      ],
      chart: {
        type: 'area',
        height: 300,
        toolbar: { show: false },
        fontFamily: 'Inter, system-ui, sans-serif',
      },
      colors: ['#2563EB', '#94A3B8'],
      dataLabels: { enabled: false },
      stroke: { width: 2, curve: 'smooth' },
      xaxis: {
        categories: s.historicoMeses,
        axisBorder: { show: false },
        axisTicks: { show: false },
        labels: { style: { colors: '#64748B', fontFamily: 'Inter, system-ui, sans-serif' } },
      },
      yaxis: {
        labels: {
          style: { colors: '#64748B', fontFamily: 'Inter, system-ui, sans-serif' },
          formatter: (value: number) => `R$ ${Math.round(value).toLocaleString('pt-BR')}`,
        },
      },
      grid: { borderColor: '#E2E8F0', strokeDashArray: 4 },
      legend: { position: 'top', horizontalAlign: 'left', fontFamily: 'Inter, system-ui, sans-serif' },
      fill: {
        type: 'gradient',
        gradient: { opacityFrom: 0.18, opacityTo: 0.02, stops: [0, 90, 100] },
      },
      tooltip: {
        y: {
          formatter: (value: number) =>
            `R$ ${value.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`,
        },
      },
    };
  }

  private deltaTone(current?: number, comparison?: number): 'positive' | 'negative' | 'neutral' {
    if (current == null || comparison == null || comparison === 0) return 'neutral';
    return current >= comparison ? 'positive' : 'negative';
  }

  private countDelta(current?: number, comparison?: number): string {
    if (current == null || comparison == null || !this.stats?.comparacaoDisponivel) return '';
    const diff = current - comparison;
    if (diff === 0) return '0 vs. comparação';
    return `${diff > 0 ? '+' : ''}${diff.toLocaleString('pt-BR')} vs. comparação`;
  }

  private currencyDelta(current?: number, comparison?: number): string {
    if (current == null || comparison == null || !this.stats?.comparacaoDisponivel) return '';
    if (comparison === 0) return current === 0 ? 'Sem variação' : 'Sem base para comparação';
    const percent = ((current - comparison) / Math.abs(comparison)) * 100;
    return `${percent > 0 ? '+' : ''}${percent.toFixed(1).replace('.', ',')}% vs. comparação`;
  }

  formatNumber(value: number | null | undefined): string {
    return (value ?? 0).toLocaleString('pt-BR');
  }

  formatCurrency(value: number | null | undefined): string {
    return (value ?? 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}
