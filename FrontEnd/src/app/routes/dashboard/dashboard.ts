import { Component, OnInit, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
// PrimeNG
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';

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
    TableModule,
    TooltipModule
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

  // Novas Métricas de Status
  abertosMes = 0;
  abertosTotal = 0;
  autorizadosMes = 0;
  autorizadosTotal = 0;
  canceladosMes = 0;
  canceladosTotal = 0;
  fechadosMes = 0;
  fechadosTotal = 0;
  entradasVeiculosMes = 0;
  saidasVeiculosMes = 0;
  mesAnoAtualLabel = '';

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
    this.initMesAnoAtualLabel();
    this.carregarDados();
  }

  private initMesAnoAtualLabel() {
    const data = new Date();
    const meses = ['JAN', 'FEV', 'MAR', 'ABR', 'MAI', 'JUN', 'JUL', 'AGO', 'SET', 'OUT', 'NOV', 'DEZ'];
    this.mesAnoAtualLabel = `${meses[data.getMonth()]}/${data.getFullYear()}`;
  }

  public insightsChartOptions: any;

  private initChart() {
    this.chartOptions = {
      series: [
        {
          name: 'Peças',
          data: [2.8, 2.9, 3.3, 3.6, 4.2, 4.0]
        },
        {
          name: 'Serviços',
          data: [4.5, 5.2, 5.3, 4.9, 6.2, 5.8]
        }
      ],
      chart: {
        height: 350,
        type: 'area',
        toolbar: { show: false },
        fontFamily: 'Inter, sans-serif'
      },
      colors: ['#f59e0b', '#3b82f6'], // Amber for Peças, Blue for Serviços
      dataLabels: { enabled: false },
      stroke: { show: true, width: 3, curve: 'smooth' },
      xaxis: {
        categories: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun'],
        axisBorder: { show: false },
        axisTicks: { show: false },
        labels: {
          style: { colors: '#64748b', fontFamily: 'Inter, sans-serif' }
        }
      },
      yaxis: {
        labels: {
          style: { colors: '#64748b', fontFamily: 'Inter, sans-serif' }
        }
      },
      grid: {
        borderColor: '#e2e8f0', // slate-200
        strokeDashArray: 4,
        yaxis: { lines: { show: true } }
      },
      legend: { position: 'top', horizontalAlign: 'left', offsetX: 40 },
      fill: {
        type: 'gradient',
        gradient: {
          shadeIntensity: 1,
          opacityFrom: 0.3,
          opacityTo: 0.05,
          stops: [0, 90, 100]
        }
      },
      tooltip: {
        y: { formatter: function (val: any) { return 'R$ ' + val } }
      }
    };

    this.insightsChartOptions = {
      series: [76, 67, 61, 90, 85, 40, 20],
      chart: {
        type: 'radialBar',
        height: 300,
        offsetY: -20,
        fontFamily: 'Inter, sans-serif'
      },
      plotOptions: {
        radialBar: {
          startAngle: -90,
          endAngle: 90,
          track: {
            background: '#e7e7e7',
            strokeWidth: '97%',
            margin: 5, // margin is in pixels
            dropShadow: {
              enabled: false
            }
          },
          dataLabels: {
            name: { show: false },
            value: { offsetY: -2, fontSize: '22px' }
          }
        }
      },
      grid: {
        padding: { top: -10 }
      },
      fill: {
        type: 'gradient',
        gradient: {
          shade: 'light',
          shadeIntensity: 0.4,
          inverseColors: false,
          opacityFrom: 1,
          opacityTo: 1,
          stops: [0, 50, 53, 91]
        },
      },
      colors: ['#4338ca', '#3b82f6', '#0ea5e9', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'],
      labels: ['Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado', 'Domingo'],
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

    // Novas Métricas de Status
    this.abertosMes = data.abertosMes || 0;
    this.abertosTotal = data.abertosTotal || 0;
    this.autorizadosMes = data.autorizadosMes || 0;
    this.autorizadosTotal = data.autorizadosTotal || 0;
    this.canceladosMes = data.canceladosMes || 0;
    this.canceladosTotal = data.canceladosTotal || 0;
    this.fechadosMes = data.fechadosMes || 0;
    this.fechadosTotal = data.fechadosTotal || 0;
    this.entradasVeiculosMes = data.entradasVeiculosMes || 0;
    this.saidasVeiculosMes = data.saidasVeiculosMes || 0;

    // Meta (Exemplo: 50 veículos por mês)
    const metaEntregas = 50;
    this.metaMesPercentual = Math.min(Math.round((this.veiculosEntregues / metaEntregas) * 100), 100);

    // Update Chart with Real Data
    if (this.chartOptions) {
      this.chartOptions.series = [
        { name: 'Peças', data: data.historicoFaturamento },
        { name: 'Serviços', data: data.historicoServicos }
      ];
      this.chartOptions.xaxis.categories = data.historicoMeses;
    }

    // Pipeline
    this.pipelineStats = [
      { title: 'Abertas', count: data.osAbertas, value: 0, color: 'bg-slate-50', iconColor: 'text-slate-500', statusColor: 'bg-slate-500', tag: 'ABERTA' },
      { title: 'Em Andamento', count: data.osEmAndamento, value: 0, color: 'bg-orange-50', iconColor: 'text-orange-500', statusColor: 'bg-orange-500', tag: 'EM ANDAMENTO' },
      { title: 'Concluídas', count: data.osConcluidas, value: 100, color: 'bg-emerald-50', iconColor: 'text-emerald-500', statusColor: 'bg-emerald-500', tag: 'CONCLUIDA' },
      { title: 'Canceladas', count: data.osCanceladas, value: 0, color: 'bg-rose-50', iconColor: 'text-rose-500', statusColor: 'bg-rose-500', tag: 'CANCELADA' }
    ];

    // Financeiro
    this.financeiro = {
      contasReceber: { value: data.contasReceber, label: 'Contas a Receber', sub: 'Carteira atualizada', color: 'text-emerald-600', iconBg: 'bg-emerald-500', rota: '/financeiro/receber' },
      contasPagar: { value: data.contasPagar, label: 'Contas a Pagar', sub: 'A vencer este mês', color: 'text-blue-600', iconBg: 'bg-blue-500', rota: '/financeiro/pagar' },
      vencidos: { value: data.valoresVencidos, label: 'Valores Vencidos', sub: 'Cobrança necessária', color: 'text-rose-600', iconBg: 'bg-rose-600', rota: '/financeiro/receber?status=VENCIDO' },
      saldo: { value: data.lucroMes, label: 'Lucro Líquido', sub: 'Projeção do mês', color: 'text-indigo-600', iconBg: 'bg-indigo-600', rota: '/financeiro/receber' }
    };

    // Operacionais
    this.indicadoresOperacionais = [
      { title: 'OS Abertas', value: data.osAbertas, subtext: 'Em atendimento', action: 'Atuar agora', icon: 'pi-box', color: 'bg-orange-500', lightColor: 'bg-orange-100', rota: '/os?status=ABERTA' },
      { title: 'Faturamento', value: `R$ ${data.faturamentoMes.toLocaleString('pt-BR')}`, subtext: 'Referente ao mês', trend: '+12.1%', icon: 'pi-money-bill', color: 'bg-sky-500', lightColor: 'bg-sky-100', rota: '/financeiro/receber' },
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
