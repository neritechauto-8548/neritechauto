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
    let empresaId = this.storage.get('tenantId');
    
    // Se tenantId for um objeto vazio (default do storage.get) ou não existir, tenta empresaId
    if (!empresaId || (typeof empresaId === 'object' && Object.keys(empresaId).length === 0)) {
      empresaId = this.storage.get('empresaId');
    }

    // Se ainda for um objeto, tenta extrair o id
    if (empresaId && typeof empresaId === 'object') {
      if (empresaId.id) {
        empresaId = empresaId.id;
      } else if (Object.keys(empresaId).length === 0) {
        empresaId = null;
      }
    }

    const params: any = {};
    if (empresaId && `${empresaId}` !== '' && `${empresaId}` !== '[object Object]') {
      params.empresaId = empresaId;
    }
    
    return this.http.get<DashboardDTO>(`${this.apiUrl}/dashboard`, { params });
  }
}
