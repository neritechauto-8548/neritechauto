import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NgApexchartsModule } from 'ng-apexcharts';
import { DashboardService, DashboardDTO } from './dashboard.service';
import { LocalStorageService } from '@shared/services/storage.service';

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
  private storage = inject(LocalStorageService);

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

    let empresaId = this.storage.get('tenantId');
    if (!empresaId || (typeof empresaId === 'object' && Object.keys(empresaId).length === 0)) {
      empresaId = this.storage.get('empresaId');
    }
    if (empresaId && typeof empresaId === 'object') {
      empresaId = empresaId.id ?? null;
    }

    this.dashboardService.getDashboardData(empresaId).subscribe({
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
      colors: ['#111827', '#94a3b8'],
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
