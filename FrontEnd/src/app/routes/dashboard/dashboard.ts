import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NgApexchartsModule } from 'ng-apexcharts';

import { DashboardDTO, DashboardService } from './dashboard.service';

type DashboardView = 'standard' | 'managerial' | 'financial' | 'estimates';

interface DashboardKpi {
  label: string;
  value: string;
  hint: string;
  state?: 'normal' | 'warning' | 'danger';
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
  selectedPeriod = 'month';
  selectedComparison = 'previous';
  selectedUnit = 'current';

  loading = true;
  error = false;
  lastUpdated: Date | null = null;
  stats: DashboardDTO | null = null;

  chartOptions: any = {};

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.loading = true;
    this.error = false;

    this.dashboardService.getDashboardData().subscribe({
      next: data => {
        this.stats = data;
        this.lastUpdated = new Date();
        this.buildChart();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.error = true;
      },
    });
  }

  selectView(view: DashboardView): void {
    if (this.selectedView === view) return;
    this.selectedView = view;
    this.buildChart();
  }

  onFilterChange(): void {
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
    switch (this.selectedPeriod) {
      case 'today': return 'Hoje';
      case '7d': return 'Últimos 7 dias';
      case '30d': return 'Últimos 30 dias';
      default: return 'Este mês';
    }
  }

  get standardKpis(): DashboardKpi[] {
    const s = this.stats;
    return [
      { label: 'OS em andamento', value: this.formatNumber(s?.osEmAndamento), hint: 'Atendimentos ativos' },
      { label: 'OS concluídas', value: this.formatNumber(s?.osConcluidas), hint: 'No período atual' },
      { label: 'Clientes ativos', value: this.formatNumber(s?.totalClientes), hint: 'Base cadastral' },
      { label: 'Veículos em atraso', value: this.formatNumber(s?.veiculosEmAtraso), hint: 'Exigem acompanhamento', state: s?.veiculosEmAtraso ? 'warning' : 'normal' },
      { label: 'Ticket médio', value: this.formatCurrency(s?.ticketMedio), hint: 'Valor médio por OS' },
    ];
  }

  get managerialKpis(): DashboardKpi[] {
    const s = this.stats;
    return [
      { label: 'Faturamento', value: this.formatCurrency(s?.faturamentoMes), hint: 'Recebimentos do mês' },
      { label: 'Ticket médio', value: this.formatCurrency(s?.ticketMedio), hint: 'Valor médio por OS' },
      { label: 'OS concluídas', value: this.formatNumber(s?.osConcluidas), hint: 'Produção no período' },
      { label: 'Clientes atendidos', value: this.formatNumber(s?.totalClientes), hint: 'Base atual' },
    ];
  }

  get financialKpis(): DashboardKpi[] {
    const s = this.stats;
    return [
      { label: 'Contas a receber', value: this.formatCurrency(s?.contasReceber), hint: 'Carteira em aberto' },
      { label: 'Contas a pagar', value: this.formatCurrency(s?.contasPagar), hint: 'Compromissos em aberto' },
      { label: 'Valores vencidos', value: this.formatCurrency(s?.valoresVencidos), hint: 'Necessitam cobrança', state: s?.valoresVencidos ? 'danger' : 'normal' },
      { label: 'Resultado do mês', value: this.formatCurrency(s?.lucroMes), hint: 'Receitas menos despesas' },
    ];
  }

  get estimateKpis(): DashboardKpi[] {
    const s = this.stats;
    return [
      { label: 'Abertos', value: this.formatNumber(s?.abertosMes), hint: 'No mês atual' },
      { label: 'Autorizados', value: this.formatNumber(s?.autorizadosMes), hint: 'No mês atual' },
      { label: 'Cancelados', value: this.formatNumber(s?.canceladosMes), hint: 'No mês atual', state: s?.canceladosMes ? 'danger' : 'normal' },
      { label: 'Fechados', value: this.formatNumber(s?.fechadosMes), hint: 'No mês atual' },
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

  formatNumber(value: number | null | undefined): string {
    return (value ?? 0).toLocaleString('pt-BR');
  }

  formatCurrency(value: number | null | undefined): string {
    return (value ?? 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}