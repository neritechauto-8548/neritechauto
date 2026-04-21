import { Component, OnInit, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
// PrimeNG
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TableModule } from 'primeng/table';

import { NgApexchartsModule, ChartComponent } from 'ng-apexcharts';
import { ApexOptions } from 'apexcharts';

// Usaremos apenas serviços essenciais ou mocks por enquanto
import { DashboardService, DashboardDTO } from './dashboard.service';
import { LocalStorageService } from '@shared/services/storage.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    CardModule,
    ButtonModule,
    TagModule,
    NgApexchartsModule,
    TableModule
  ],
})
export class Dashboard implements OnInit {
  private dashboardService = inject(DashboardService);
  private router = inject(Router);
  private storage = inject(LocalStorageService);

  @ViewChild('chart') chart!: ChartComponent;
  public chartOptions: any;

  // Métricas de Produção
  loading = true;
  stats: any = {};

  // Métricas do Topo (Default Fallbacks)
  ticketMedioGeral = 0;
  ticketMedioMes = 0;
  veiculosEntregues = 0;
  veiculosEmAtraso = 0;
  metaMesPercentual = 0;

  // KPIs comparisons
  kpiGeral = '0%';
  kpiMes = '0%';
  kpiEntregas = '0%';
  kpiAtraso = '0';

  // Pipeline de Orçamentos
  pipelineStats: any[] = [];

  // Indicadores Operacionais
  indicadoresOperacionais: any[] = [];

  // Visão Financeira
  financeiro: any = {
    contasReceber: { value: 0, label: 'Contas a Receber', sub: 'Carteira atualizada', color: 'text-emerald-600', iconBg: 'bg-emerald-500' },
    contasPagar: { value: 0, label: 'Contas a Pagar', sub: 'A vencer este mês', color: 'text-blue-600', iconBg: 'bg-blue-500' },
    vencidos: { value: 0, label: 'Valores Vencidos', sub: 'Cobrança necessária', color: 'text-rose-600', iconBg: 'bg-rose-600' },
    saldo: { value: 0, label: 'Saldo Previsto', sub: 'Projeção do mês', color: 'text-sky-600', iconBg: 'bg-sky-500' }
  };

  constructor() {
    this.initChart();
  }

  ngOnInit() {
    this.carregarDados();
  }

  private initChart() {
    this.chartOptions = {
      series: [
        {
          name: 'Venda de Peças',
          data: [28, 29, 33, 36, 42, 40, 39, 45]
        },
        {
          name: 'Mão-de-obra de Serviços',
          data: [45, 52, 53, 49, 62, 58, 56, 68]
        }
      ],
      chart: {
        height: 350,
        type: 'area',
        toolbar: {
          show: false
        },
        fontFamily: 'Inter, sans-serif'
      },
      colors: ['#6366f1', '#10b981'], // Indigo-500 (Primary), Emerald-500 (Secondary)
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: 'smooth',
        width: 3
      },
      fill: {
        type: 'gradient',
        gradient: {
          shadeIntensity: 1,
          opacityFrom: 0.7,
          opacityTo: 0.2,
          stops: [0, 90, 100]
        }
      },
      xaxis: {
        categories: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago'],
        axisBorder: { show: false },
        axisTicks: { show: false },
        labels: {
          style: {
            colors: '#64748b', // slate-500
            fontFamily: 'Inter, sans-serif'
          }
        }
      },
      yaxis: {
        labels: {
          style: {
            colors: '#64748b', // slate-500
            fontFamily: 'Inter, sans-serif'
          }
        }
      },
      grid: {
        borderColor: '#e2e8f0', // slate-200
        strokeDashArray: 4,
        yaxis: {
          lines: {
            show: true
          }
        },
        padding: {
          top: 0,
          right: 0,
          bottom: 0,
          left: 10
        }
      },
      legend: {
        position: 'bottom',
        horizontalAlign: 'center'
      },
      tooltip: {
        x: {
          format: 'dd/MM/yy HH:mm'
        },
      },
    };
  }

  carregarDados() {
    this.loading = true;
    this.dashboardService.getDashboardData().subscribe({
      next: (data) => {
        this.stats = data;
        this.mapDataToUI(data);
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar dashboard', err);
        this.loading = false;
      }
    });
  }

  private mapDataToUI(data: DashboardDTO) {
    // Top Metrics
    this.ticketMedioGeral = data.ticketMedio || 0;
    this.veiculosEntregues = data.osConcluidas || 0;
    this.veiculosEmAtraso = data.veiculosEmAtraso || 0;

    // Meta (Exemplo: 50 veículos por mês)
    const metaEntregas = 50;
    this.metaMesPercentual = Math.min(Math.round((this.veiculosEntregues / metaEntregas) * 100), 100);

    // Update Chart with Real Data
    if (this.chartOptions) {
      this.chartOptions.series = [
        { name: 'Faturamento Total', data: data.historicoFaturamento },
        { name: 'Mão-de-obra', data: data.historicoServicos }
      ];
      this.chartOptions.xaxis.categories = data.historicoMeses;
    }

    // Pipeline
    this.pipelineStats = [
      { title: 'Abertas', count: data.osAbertas, value: 0, color: 'bg-slate-50', iconColor: 'text-slate-500', statusColor: 'bg-slate-500', tag: 'aberto' },
      { title: 'Em Andamento', count: data.osEmAndamento, value: 0, color: 'bg-orange-50', iconColor: 'text-orange-500', statusColor: 'bg-orange-500', tag: 'aguardando' },
      { title: 'Concluídas', count: data.osConcluidas, value: 100, color: 'bg-emerald-50', iconColor: 'text-emerald-500', statusColor: 'bg-emerald-500', tag: 'realizado' },
      { title: 'Canceladas', count: data.osCanceladas, value: 0, color: 'bg-rose-50', iconColor: 'text-rose-500', statusColor: 'bg-rose-500', tag: 'cancelado' }
    ];

    // Financeiro
    this.financeiro = {
      contasReceber: { value: data.contasReceber, label: 'Contas a Receber', sub: 'Carteira atualizada', color: 'text-emerald-600', iconBg: 'bg-emerald-500', rota: '/financeiro/receber' },
      contasPagar: { value: data.contasPagar, label: 'Contas a Pagar', sub: 'A vencer este mês', color: 'text-blue-600', iconBg: 'bg-blue-500', rota: '/financeiro/pagar' },
      vencidos: { value: data.valoresVencidos, label: 'Valores Vencidos', sub: 'Cobrança necessária', color: 'text-rose-600', iconBg: 'bg-rose-600', rota: '/financeiro/receber?status=VENCIDO' },
      saldo: { value: data.lucroMes, label: 'Lucro Líquido', sub: 'Projeção do mês', color: 'text-indigo-600', iconBg: 'bg-indigo-600', rota: '/financeiro' }
    };

    // Operacionais
    this.indicadoresOperacionais = [
      { title: 'OS Abertas', value: data.osAbertas, subtext: 'Em atendimento', action: 'Atuar agora', icon: 'pi-box', color: 'bg-orange-500', lightColor: 'bg-orange-100', rota: '/os' },
      { title: 'Faturamento', value: `R$ ${data.faturamentoMes.toLocaleString('pt-BR')}`, subtext: 'Referente ao mês', trend: '+12.1%', icon: 'pi-money-bill', color: 'bg-sky-500', lightColor: 'bg-sky-100', rota: '/financeiro' },
      { title: 'Clientes Ativos', value: data.totalClientes, subtext: 'Base cadastral', trend: '+5.2%', icon: 'pi-users', color: 'bg-blue-500', lightColor: 'bg-blue-100', rota: '/cliente' }
    ];
  }

  navegarPara(rota: string) {
    this.router.navigate([rota]);
  }

  exportarRelatorio() {
    // Simulação de exportação para PRD
    window.print();
  }
}
