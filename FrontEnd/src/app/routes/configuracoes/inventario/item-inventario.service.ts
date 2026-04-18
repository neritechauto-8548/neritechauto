import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export type StatusItemInventario = 'PENDENTE' | 'CONTADO' | 'CONFERIDO' | 'AJUSTADO';

export interface ItemInventarioRequest {
  inventarioId: number;
  produtoId: number;
  localizacaoId?: number;
  loteNumero?: string;
  quantidadeSistema: number;
  quantidadeContada?: number;
  valorUnitario?: number;
  status?: StatusItemInventario;
  motivoDiferenca?: string;
  observacoes?: string;
  usuarioContagem?: number;
  usuarioConferencia?: number;
  fotoComprovanteUrl?: string;
}

export interface ItemInventarioResponse {
  id: number;
  inventarioId: number;
  produtoId: number;
  produtoNome?: string;
  localizacaoId?: number;
  localizacaoNome?: string;
  loteNumero?: string;
  quantidadeSistema: number;
  quantidadeContada?: number;
  diferenca?: number;
  valorUnitario?: number;
  valorTotalSistema?: number;
  valorTotalContado?: number;
  diferencaValor?: number;
  status?: StatusItemInventario;
  motivoDiferenca?: string;
  observacoes?: string;
  usuarioContagem?: number;
  dataContagem?: string;
  usuarioConferencia?: number;
  dataConferencia?: string;
  fotoComprovanteUrl?: string;
}

@Injectable({ providedIn: 'root' })
export class ItemInventarioService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/itens-inventario';

  private getHeaders(): HttpHeaders {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    return new HttpHeaders({
      'X-Tenant-Id': String(tenantId),
      'Accept': 'application/json'
    });
  }

  listPorInventario(inventarioId: number, params?: { page?: number; size?: number; sort?: string }): Observable<Page<ItemInventarioResponse>> {
    let httpParams = new HttpParams();
    if (params) {
      Object.entries(params).forEach(([k, v]) => {
        if (v !== null && v !== undefined && `${v}` !== '') httpParams = httpParams.set(k, String(v));
      });
    }
    return this.http.get<Page<ItemInventarioResponse>>(`${this.base}/inventario/${inventarioId}`, { params: httpParams, headers: this.getHeaders() });
  }

  listPorInventarioStatus(inventarioId: number, status: StatusItemInventario, params?: { page?: number; size?: number; sort?: string }): Observable<Page<ItemInventarioResponse>> {
    let httpParams = new HttpParams();
    if (params) {
      Object.entries(params).forEach(([k, v]) => {
        if (v !== null && v !== undefined && `${v}` !== '') httpParams = httpParams.set(k, String(v));
      });
    }
    return this.http.get<Page<ItemInventarioResponse>>(`${this.base}/inventario/${inventarioId}/status/${status}`, { params: httpParams, headers: this.getHeaders() });
  }

  getById(id: number): Observable<ItemInventarioResponse> {
    return this.http.get<ItemInventarioResponse>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  create(dto: ItemInventarioRequest): Observable<ItemInventarioResponse> {
    return this.http.post<ItemInventarioResponse>(this.base, dto, { headers: this.getHeaders() });
  }

  update(id: number, dto: ItemInventarioRequest): Observable<ItemInventarioResponse> {
    return this.http.put<ItemInventarioResponse>(`${this.base}/${id}`, dto, { headers: this.getHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }
}
