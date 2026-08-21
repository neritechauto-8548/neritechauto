import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { DashboardDTO, DashboardService } from './dashboard.service';

interface DashboardMetric {
  label: string;
  value: string;
  helper: string;
  icon: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
  imports: [CommonModule, RouterLink],
})
export class Dashboard implements OnInit {
  private readonly dashboardService = inject(DashboardService);
  private readonly currency = new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  });

  loading = true;
  errorMessage = '';
  data?: DashboardDTO;

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading = true;
    this.errorMessage = '';

    this.dashboardService
      .getDashboardData()
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: data => {
          this.data = data;
        },
        error: () => {
          this.data = undefined;
          this.errorMessage =
            'Não foi possível carregar os indicadores agora. Tente novamente sem alterar o contexto da empresa.';
        },
      });
  }

  get primaryMetrics(): DashboardMetric[] {
    if (!this.data) {
      return [];
    }

    return [
      {
        label: 'Faturamento do mês',
        value: this.formatMoney(this.data.faturamentoMes),
        helper: 'Valor informado pelo domínio financeiro',
        icon: 'pi pi-wallet',
      },
      {
        label: 'Ticket médio',
        value: this.formatMoney(this.data.ticketMedio),
        helper: 'Métrica calculada no backend',
        icon: 'pi pi-chart-line',
      },
      {
        label: 'OS em aberto',
        value: this.formatNumber(this.data.osAbertas),
        helper: 'Ordens ativas no escopo autenticado',
        icon: 'pi pi-briefcase',
      },
      {
        label: 'OS concluídas',
        value: this.formatNumber(this.data.osConcluidas),
        helper: 'Total devolvido pelo domínio de OS',
        icon: 'pi pi-check-circle',
      },
      {
        label: 'Veículos em atraso',
        value: this.formatNumber(this.data.veiculosEmAtraso),
        helper: 'Itens que exigem atenção operacional',
        icon: 'pi pi-clock',
      },
      {
        label: 'Clientes ativos',
        value: this.formatNumber(this.data.totalClientes),
        helper: 'Base cadastral ativa no contrato atual',
        icon: 'pi pi-users',
      },
    ];
  }

  get operationalMetrics(): DashboardMetric[] {
    if (!this.data) {
      return [];
    }

    return [
      {
        label: 'Abertas no mês',
        value: this.formatNumber(this.data.abertosMes),
        helper: `${this.formatNumber(this.data.abertosTotal)} abertas no total`,
        icon: 'pi pi-folder-open',
      },
      {
        label: 'Autorizadas no mês',
        value: this.formatNumber(this.data.autorizadosMes),
        helper: `${this.formatNumber(this.data.autorizadosTotal)} autorizadas no total`,
        icon: 'pi pi-verified',
      },
      {
        label: 'Fechadas no mês',
        value: this.formatNumber(this.data.fechadosMes),
        helper: `${this.formatNumber(this.data.fechadosTotal)} fechadas no total`,
        icon: 'pi pi-lock',
      },
      {
        label: 'Canceladas no mês',
        value: this.formatNumber(this.data.canceladosMes),
        helper: `${this.formatNumber(this.data.canceladosTotal)} canceladas no total`,
        icon: 'pi pi-times-circle',
      },
      {
        label: 'Entradas de veículos',
        value: this.formatNumber(this.data.entradasVeiculosMes),
        helper: 'Movimento registrado no mês',
        icon: 'pi pi-sign-in',
      },
      {
        label: 'Saídas de veículos',
        value: this.formatNumber(this.data.saidasVeiculosMes),
        helper: 'Movimento registrado no mês',
        icon: 'pi pi-sign-out',
      },
    ];
  }

  get historyAvailable() {
    return Boolean(
      this.data?.historicoMeses?.length &&
        this.data?.historicoFaturamento?.length &&
        this.data?.historicoServicos?.length
    );
  }

  formatMoney(value: number | null | undefined) {
    return this.currency.format(Number(value ?? 0));
  }

  formatNumber(value: number | null | undefined) {
    return new Intl.NumberFormat('pt-BR').format(Number(value ?? 0));
  }
}
