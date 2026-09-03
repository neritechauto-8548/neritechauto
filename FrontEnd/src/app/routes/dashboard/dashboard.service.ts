import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
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
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.baseUrl || '/api';

  getDashboardData() {
    // Tenant e escopo derivam exclusivamente da sessao autenticada no backend.
    return this.http.get<DashboardDTO>(`${this.apiUrl}/dashboard`);
  }
}
