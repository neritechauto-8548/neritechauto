import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface OrcamentoListCustomer {
  id: number;
  nome: string;
}

export interface OrcamentoListVehicle {
  id: number;
  descricao: string;
  placa: string;
}

export interface OrcamentoMoney {
  currency: 'BRL';
  amount: number;
}

export interface OrcamentoListItem {
  id: number;
  numero: string;
  versaoAtual: number | null;
  cliente: OrcamentoListCustomer | null;
  veiculo: OrcamentoListVehicle | null;
  status: string;
  total: OrcamentoMoney;
  validadeEm: string | null;
  responsavelId: number | null;
  comunicacaoStatus: string | null;
  proximaAcao: string;
  criadoEm: string | null;
  atualizadoEm: string | null;
  allowedActions: string[];
}

export interface OrcamentoListResponse {
  items: OrcamentoListItem[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  summaryAvailable: boolean;
  summaryUnavailableReason: string | null;
}

export interface OrcamentoListParams {
  q?: string;
  status?: string;
  page?: number;
  size?: number;
  sort?: string;
}

@Injectable({ providedIn: 'root' })
export class OrcamentoListService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.baseUrl}/v1/orcamentos`;

  list(filters: OrcamentoListParams = {}): Observable<OrcamentoListResponse> {
    let params = new HttpParams();
    Object.entries(filters).forEach(([key, value]) => {
      if (value !== undefined && value !== null && `${value}`.trim() !== '') {
        params = params.set(key, String(value));
      }
    });
    return this.http.get<OrcamentoListResponse>(this.baseUrl, { params });
  }

  getById(id: number): Observable<OrcamentoListItem> {
    return this.http.get<OrcamentoListItem>(`${this.baseUrl}/${id}`);
  }
}
