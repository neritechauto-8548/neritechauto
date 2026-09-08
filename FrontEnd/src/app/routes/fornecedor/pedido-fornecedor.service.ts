import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page } from './models/fornecedor.models';
import { PedidoFornecedorRequest, PedidoFornecedorResponse } from './models/compra.models';

@Injectable({ providedIn: 'root' })
export class PedidoFornecedorService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/pedidos-fornecedor`;

  list(params?: Record<string, unknown>): Observable<Page<PedidoFornecedorResponse>> {
    let httpParams = new HttpParams();
    Object.entries(params || {}).forEach(([key, value]) => {
      if (key !== 'empresaId' && value !== undefined && value !== null && `${value}` !== '') {
        httpParams = httpParams.set(key, String(value));
      }
    });
    return this.http.get<Page<PedidoFornecedorResponse>>(this.base, { params: httpParams });
  }

  get(id: number | string): Observable<PedidoFornecedorResponse> {
    return this.http.get<PedidoFornecedorResponse>(`${this.base}/${id}`);
  }

  create(dto: PedidoFornecedorRequest): Observable<PedidoFornecedorResponse> {
    return this.http.post<PedidoFornecedorResponse>(this.base, this.tenantNeutralPayload(dto));
  }

  update(id: number | string, dto: PedidoFornecedorRequest): Observable<PedidoFornecedorResponse> {
    return this.http.put<PedidoFornecedorResponse>(`${this.base}/${id}`, this.tenantNeutralPayload(dto));
  }

  updateStatus(id: number | string, status: string): Observable<PedidoFornecedorResponse> {
    return this.http.patch<PedidoFornecedorResponse>(`${this.base}/${id}/status`, null, {
      params: new HttpParams().set('status', status),
    });
  }

  delete(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  private tenantNeutralPayload(dto: PedidoFornecedorRequest): Omit<PedidoFornecedorRequest, 'empresaId'> {
    const { empresaId: _ignored, ...payload } = dto;
    return payload;
  }
}
