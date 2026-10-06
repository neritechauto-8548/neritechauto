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
  historicoDespesas: number[];
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
  private http = inject(HttpClient);
  private apiUrl = environment.baseUrl || '/api';

  getDashboardData(empresaId?: string | number | null) {
    let params = new HttpParams();
    if (empresaId !== undefined && empresaId !== null && String(empresaId) !== '') {
      params = params.set('empresaId', String(empresaId));
    }
    return this.http.get<DashboardDTO>(`${this.apiUrl}/dashboard`, { params });
  }
}
