import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LocalStorageService } from '@shared/services/storage.service';
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
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);
  private storage = inject(LocalStorageService);
  private apiUrl = environment.baseUrl || '/api';        

  getDashboardData() {
    const empresaId = this.storage.get('tenantId') ?? this.storage.get('empresaId');
    const params: any = {};
    if (empresaId !== undefined && empresaId !== null && 
`${empresaId}` !== '') {
      params.empresaId = empresaId;
    }
    return this.http.get<DashboardDTO>(`${this.apiUrl}/dashboard`, { params });
  }
}
