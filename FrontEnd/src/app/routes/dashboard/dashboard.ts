import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NgApexchartsModule } from 'ng-apexcharts';
import { DashboardService, DashboardDTO } from './dashboard.service';

type DashboardView = 'padrao' | 'gerencial' | 'financeiro' | 'orcamento';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
  imports: [CommonModule, FormsModule, RouterModule, NgApexchartsModule],
})
export class Dashboard implements OnInit {
  private dashboardService = inject(DashboardService);
  private router = inject(Router);

  loading = true;
  error = false;
  data: DashboardDTO | null = null;
  selectedView: DashboardView = 'padrao';
  chartOptions: any = null;

  readonly viewOptions: { value: DashboardView; label: string }[] = [
    { value: 'padrao', label: 'Padrão' },
    { value: 'gerencial', label: 'Gerencial' },
    { value: 'financeiro', label: 'Financeiro' },
    { value: 'orcamento', label: 'Orçamento' },
  ];

  ngOnInit(): void {
    this.carregarDados();
  }

  get mesAnoAtualLabel(): string {
    const data = new Date();
    const meses = ['JAN', 'FEV', 'MAR', 'ABR', 'MAI', 'JUN', 'JUL', 'AGO', 'SET', 'OUT', 'NOV', 'DEZ'];
    return `${meses[data.getMonth()]}/${data.getFullYear()}`;
  }

  get saudacao(): string {
    const h = new Date().getHours();
    return h < 12 ? 'Bom dia' : h < 18 ? 'Boa tarde' : 'Boa noite';
  }

  get viewLabel(): string {
    return this.viewOptions.find(option => option.value === this.selectedView)?.label ?? 'Padrão';
  }

  get viewDescription(): string {
    const descriptions: Record<DashboardView, string> = {
      padrao: 'Uma visão equilibrada da operação, clientes e resultado.',
      gerencial: 'Indicadores para acompanhar ritmo, produtividade e resultado.',
      financeiro: 'Receita, despesas, resultado e valores que exigem atenção.',
      orcamento: 'Conversão, volume de orçamentos e impacto no faturamento.',
    };
    return descriptions[this.selectedView];
  }

  get primaryMetricLabel(): string {
    return {
      padrao: 'Faturamento no período',
      gerencial: 'Faturamento no período',
      financeiro: 'Receita no período',
      orcamento: 'Faturamento originado',
    }[this.selectedView];
  }

  get signalOneLabel(): string {
    return {
      padrao: 'Em atendimento',
      gerencial: 'Em atendimento',
      financeiro: 'A receber',
      orcamento: 'Aguardando aprovação',
    }[this.selectedView];
  }

  get signalOneValue(): number {
    return this.selectedView === 'financeiro'
      ? Number(this.data?.contasReceber ?? 0)
      : this.selectedView === 'orcamento'
        ? Number(this.data?.abertosMes ?? 0)
        : Number(this.data?.osEmAndamento ?? 0);
  }

  get signalOneDescription(): string {
    return {
      padrao: 'OS em andamento agora',
      gerencial: 'OS em andamento agora',
      financeiro: 'carteira em aberto',
      orcamento: 'orçamentos em análise',
    }[this.selectedView];
  }

  get signalTwoLabel(): string {
    return this.selectedView === 'orcamento' ? 'Autorizados' : this.selectedView === 'financeiro' ? 'Despesas' : 'Concluídas';
  }

  get signalTwoValue(): number {
    return this.selectedView === 'financeiro'
      ? Number(this.data?.despesasMes ?? 0)
      : this.selectedView === 'orcamento'
        ? Number(this.data?.autorizadosMes ?? 0)
        : Number(this.data?.osConcluidas ?? 0);
  }

  get signalTwoDescription(): string {
    return this.selectedView === 'financeiro'
      ? 'despesas no período'
      : this.selectedView === 'orcamento'
        ? 'orçamentos autorizados'
        : 'ordens concluídas no período';
  }

  get faturamentoCrescimentoPercentual(): number {
    const historico = this.data?.historicoFaturamento ?? [];
    if (historico.length < 2) return 0;
    const atual = Number(historico[historico.length - 1] ?? 0);
    const anterior = Number(historico[historico.length - 2] ?? 0);
    return anterior > 0 ? Math.round(((atual - anterior) / anterior) * 1000) / 10 : 0;
  }

  get periodoAtualLabel(): string {
    return new Intl.DateTimeFormat('pt-BR', { month: 'long', year: 'numeric' }).format(new Date());
  }

  get margemPercentual(): number {
    const faturamento = Number(this.data?.faturamentoMes ?? 0);
    return faturamento > 0 ? Math.round((Number(this.data?.lucroMes ?? 0) / faturamento) * 100) : 0;
  }

  get taxaConversaoOrcamento(): number {
    const abertos = Number(this.data?.abertosMes ?? 0);
    const fechados = Number(this.data?.fechadosMes ?? 0);
    return abertos > 0 ? Math.round((fechados / abertos) * 100) : 0;
  }

  get hasData(): boolean {
    if (!this.data) return false;
    return [
      this.data.totalClientes,
      this.data.osAbertas,
      this.data.osConcluidas,
      this.data.faturamentoMes,
      this.data.contasReceber,
      this.data.abertosTotal,
      this.data.autorizadosTotal,
      this.data.fechadosTotal,
    ].some(value => Number(value) > 0);
  }

  get metaEntregasPercentual(): number {
    return Math.min(Math.round(((this.data?.osConcluidas ?? 0) / 50) * 100), 100);
  }

  carregarDados(): void {
    this.loading = true;
    this.error = false;

    this.dashboardService.getDashboardData().subscribe({
      next: data => {
        this.data = data;
        this.chartOptions = this.buildChart(data);
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      },
    });
  }

  private buildChart(data: DashboardDTO): any {
    return {
      series: [
        { name: 'Faturamento', data: data.historicoFaturamento ?? [] },
        { name: 'Despesas', data: data.historicoDespesas ?? [] },
      ],
      chart: {
        type: 'area',
        height: 320,
        toolbar: { show: false },
        fontFamily: 'Inter, ui-sans-serif, system-ui, sans-serif',
        zoom: { enabled: false },
      },
      dataLabels: { enabled: false },
      stroke: { curve: 'smooth', width: 2.5 },
      colors: ['#2563eb', '#f59e0b'],
      fill: {
        type: 'gradient',
        gradient: { opacityFrom: 0.18, opacityTo: 0.02, stops: [0, 100] },
      },
      grid: { borderColor: '#e5e7eb', strokeDashArray: 4 },
      xaxis: {
        categories: data.historicoMeses ?? [],
        axisBorder: { show: false },
        axisTicks: { show: false },
      },
      yaxis: {
        labels: { formatter: (value: number) => this.formatCurrency(value) },
      },
      tooltip: {
        y: { formatter: (value: number) => this.formatCurrency(value) },
      },
      legend: { position: 'top', horizontalAlign: 'left' },
    };
  }

  barHeight(value: number | null | undefined): number {
    return Math.max(18, Math.round((Number(value ?? 0) / 140000) * 100));
  }

  formatCurrency(value: number | null | undefined): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
      maximumFractionDigits: 0,
    }).format(Number(value ?? 0));
  }

  selecionarVisao(view: DashboardView): void {
    this.selectedView = view;
  }

  navegarPara(route: string): void {
    this.router.navigateByUrl(route);
  }
}
