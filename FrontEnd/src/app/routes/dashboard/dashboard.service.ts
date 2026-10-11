import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { of } from 'rxjs';
import { environment } from '@env/environment';

export interface DashboardDTO {
  totalClientes: number; osAbertas: number; osEmAndamento: number; osConcluidas: number; osCanceladas: number;
  faturamentoMes: number; despesasMes: number; lucroMes: number; ticketMedio: number;
  contasReceber: number; contasPagar: number; valoresVencidos: number; veiculosEmAtraso: number;
  historicoFaturamento: number[]; historicoDespesas: number[]; historicoMeses: string[];
  abertosMes: number; abertosTotal: number; autorizadosMes: number; autorizadosTotal: number;
  canceladosMes: number; canceladosTotal: number; fechadosMes: number; fechadosTotal: number;
  entradasVeiculosMes: number; saidasVeiculosMes: number;
  inicioPeriodo?: string; fimPeriodo?: string;
  faturamentoPeriodoAnterior?: number; despesasPeriodoAnterior?: number; lucroPeriodoAnterior?: number;
  abertosPeriodoAnterior?: number; autorizadosPeriodoAnterior?: number;
  canceladosPeriodoAnterior?: number; fechadosPeriodoAnterior?: number;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);
  private apiUrl = environment.baseUrl || '/api';

  getDashboardData(inicio?: string, fim?: string) {
    if (environment.uxPreview) {
      return of<DashboardDTO>({
        totalClientes: 428, osAbertas: 37, osEmAndamento: 24, osConcluidas: 86, osCanceladas: 4,
        faturamentoMes: 128450, despesasMes: 74280, lucroMes: 54170, ticketMedio: 1493.60,
        contasReceber: 46200, contasPagar: 31800, valoresVencidos: 6800, veiculosEmAtraso: 7,
        historicoFaturamento: [92000, 101500, 97000, 112300, 118900, 128450],
        historicoDespesas: [58000, 61200, 59500, 68100, 70400, 74280],
        historicoMeses: ['MAI', 'JUN', 'JUL', 'AGO', 'SET', 'OUT'],
        abertosMes: 37, abertosTotal: 37, autorizadosMes: 31, autorizadosTotal: 31,
        canceladosMes: 4, canceladosTotal: 4, fechadosMes: 86, fechadosTotal: 86,
        entradasVeiculosMes: 112, saidasVeiculosMes: 98,
        inicioPeriodo: inicio, fimPeriodo: fim,
        faturamentoPeriodoAnterior: 118900, despesasPeriodoAnterior: 70400, lucroPeriodoAnterior: 48500,
        abertosPeriodoAnterior: 33, autorizadosPeriodoAnterior: 28,
        canceladosPeriodoAnterior: 3, fechadosPeriodoAnterior: 79,
      });
    }

    let params = new HttpParams();
    if (inicio && fim) params = params.set('inicio', inicio).set('fim', fim);
    return this.http.get<DashboardDTO>(this.apiUrl + '/dashboard', { params });
  }
}
