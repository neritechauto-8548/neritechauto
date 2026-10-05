import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '@env/environment';

export interface DashboardDTO {
  totalClientes: number;
  osAbertas: number;
  osEmAndamento: number;
  osConcluidas: number;
  osCanceladas: number;
  faturamentoMes: number;
  despesasMes: number;
  lucroMes: number;
  ticketMedio: number;
  contasReceber: number;
  contasPagar: number;
  valoresVencidos: number;
  veiculosEmAtraso: number;
  historicoFaturamento: number[];
  historicoServicos: number[];
  historicoMeses: string[];
  abertosMes: number;
  abertosTotal: number;
  autorizadosMes: number;
  autorizadosTotal: number;
  canceladosMes: number;
  canceladosTotal: number;
  fechadosMes: number;
  fechadosTotal: number;
  entradasVeiculosMes: number;
  saidasVeiculosMes: number;
  periodo: string;
  inicio: string;
  fim: string;
  comparacao: string;
  faturamentoComparacao: number;
  osConcluidasComparacao: number;
  ticketMedioComparacao: number;
  comparacaoDisponivel: boolean;
  geradoEm: string;
  dadosParciais: boolean;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.baseUrl || '/api';

  getDashboardData(
    period: string,
    comparison: string,
    startDate?: string,
    endDate?: string,
  ) {
    let params = new HttpParams()
      .set('period', period)
      .set('comparison', comparison);

    if (period === 'custom' && startDate && endDate) {
      params = params.set('startDate', startDate).set('endDate', endDate);
    }

    return this.http.get<DashboardDTO>(`${this.apiUrl}/v1/dashboards/home`, { params });
  }
}
