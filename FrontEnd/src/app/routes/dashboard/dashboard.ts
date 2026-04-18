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
import { DashboardService } from './dashboard.service';
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

  // Métricas do Topo
  ticketMedioGeral = 1847.50;
  ticketMedioMes = 1923.80;
  veiculosEntregues = 127;
  veiculosEmAtraso = 8;
  metaMesPercentual = 85;

  // KPIs comparisons
  kpiGeral = '+12.5%';
  kpiMes = '+8.3%';
  kpiEntregas = '85%';
  kpiAtraso = '-2';

  // Pipeline de Orçamentos
  pipelineStats = [
    { title: 'Em Análise', count: 31, value: 262, color: 'bg-slate-50', iconColor: 'text-slate-500', statusColor: 'bg-slate-500', tag: 'aberto' },
    { title: 'Em Aprovação', count: 71, value: 338, color: 'bg-orange-50', iconColor: 'text-orange-500', statusColor: 'bg-orange-500', tag: 'aguardando' },
    { title: 'Aprovados', count: 1, value: 19, color: 'bg-rose-50', iconColor: 'text-rose-500', statusColor: 'bg-rose-500', tag: 'aprovado' },
    { title: 'Concluídos', count: 18, value: 85, color: 'bg-emerald-50', iconColor: 'text-emerald-500', statusColor: 'bg-emerald-500', tag: 'realizado' }
  ];

  // Indicadores Operacionais
  indicadoresOperacionais = [
    { title: 'Entrada de Veículos', value: '142', subtext: 'Veículos atuais', trend: '+15.2%', icon: 'pi-arrow-down-left', color: 'bg-blue-500', lightColor: 'bg-blue-100' },
    { title: 'Saída de Veículos', value: '127', subtext: 'Veículos atuais', trend: '+12.1%', icon: 'pi-arrow-up-right', color: 'bg-sky-500', lightColor: 'bg-sky-100' },
    { title: 'Ordens de Serviço Abertas', value: '23', subtext: 'Em atendimento', action: 'Atuar agora', icon: 'pi-box', color: 'bg-orange-500', lightColor: 'bg-orange-100' }
  ];

  // Visão Financeira
  financeiro = {
    contasReceber: { value: 64047.81, label: 'Contas a Receber', sub: 'Carteira atualizada', color: 'text-emerald-600', iconBg: 'bg-emerald-500' },
    contasPagar: { value: 13086.64, label: 'Contas a Pagar', sub: 'A vencer este mês', color: 'text-blue-600', iconBg: 'bg-blue-500' },
    vencidos: { value: 2847.20, label: 'Valores Vencidos', sub: 'Cobrança necessária', color: 'text-rose-600', iconBg: 'bg-rose-600' },
    saldo: { value: 48113.97, label: 'Saldo Previsto', sub: 'Projeção do mês', color: 'text-sky-600', iconBg: 'bg-sky-500' }
  };

  constructor() {
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
      colors: ['#3b82f6', '#10b981'], // Blue-500 (Primary), Emerald-500 (Secondary)
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

  ngOnInit() {
    this.carregarDados();
  }

  carregarDados() {
    console.log('Dados do dashboard carregados');
  }

  navegarPara(rota: string) {
    this.router.navigate([rota]);
  }
}
