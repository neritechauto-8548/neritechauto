import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export type TipoInventario = 'GERAL' | 'PARCIAL' | 'CICLICO' | 'EMERGENCIAL';
export type StatusInventario = 'PLANEJADO' | 'EM_ANDAMENTO' | 'FINALIZADO' | 'CANCELADO';

export interface InventarioRequest {
  empresaId: number;
  codigo: string;
  descricao: string;
  tipoInventario?: TipoInventario;
  dataInicio: string;
  dataFim?: string;
  status?: StatusInventario;
  observacoes?: string;
}

export interface InventarioResponse {
  id: number;
  empresaId: number;
  codigo: string;
  descricao: string;
  tipoInventario?: TipoInventario;
  dataInicio: string;
  dataFim?: string;
  status?: StatusInventario;
  totalItensPlanejados?: number;
  totalItensContados?: number;
  totalDivergencias?: number;
  valorTotalSistema?: number;
  valorTotalContado?: number;
  diferencaValor?: number;
  observacoes?: string;
  finalizadoPor?: string;
}

@Injectable({ providedIn: 'root' })
export class InventarioService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/inventarios';

  private getHeaders(): HttpHeaders {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    return new HttpHeaders({
      'X-Tenant-Id': String(tenantId),
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });
  }

  listPorEmpresa(empresaId: number, params?: { page?: number; size?: number; sort?: string }): Observable<Page<InventarioResponse>> {
    let httpParams = new HttpParams();
    if (params) {
      Object.entries(params).forEach(([k, v]) => {
        if (v !== null && v !== undefined && `${v}` !== '') httpParams = httpParams.set(k, String(v));
      });
    }
    return this.http.get<Page<InventarioResponse>>(`${this.base}/empresa/${empresaId}`, { params: httpParams, headers: this.getHeaders() });
  }

  listPorEmpresaStatus(empresaId: number, status: StatusInventario, params?: { page?: number; size?: number; sort?: string }): Observable<Page<InventarioResponse>> {
    let httpParams = new HttpParams();
    if (params) {
      Object.entries(params).forEach(([k, v]) => {
        if (v !== null && v !== undefined && `${v}` !== '') httpParams = httpParams.set(k, String(v));
      });
    }
    return this.http.get<Page<InventarioResponse>>(`${this.base}/empresa/${empresaId}/status/${status}`, { params: httpParams, headers: this.getHeaders() });
  }

  getById(id: number): Observable<InventarioResponse> {
    return this.http.get<InventarioResponse>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  create(dto: InventarioRequest): Observable<InventarioResponse> {
    return this.http.post<InventarioResponse>(this.base, dto, { headers: this.getHeaders() });
  }

  update(id: number, dto: InventarioRequest): Observable<InventarioResponse> {
    return this.http.put<InventarioResponse>(`${this.base}/${id}`, dto, { headers: this.getHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }
}
