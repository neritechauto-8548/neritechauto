import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NgApexchartsModule } from 'ng-apexcharts';
import { DashboardService, DashboardDTO } from './dashboard.service';
import { environment } from '@env/environment';

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

  readonly isUxPreview = environment.uxPreview;
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
    const numericValue = Number(value ?? 0);
    return Math.max(18, Math.min(100, Math.round((numericValue / 140000) * 100)));
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
