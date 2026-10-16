import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../environments/environment';
import { Page } from './models/fornecedor.models';
import { PedidoFornecedorRequest, PedidoFornecedorResponse } from './models/compra.models';

@Injectable({ providedIn: 'root' })
export class PedidoFornecedorService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = `${environment.baseUrl}/v1/pedidos-fornecedor`;

  private scoped<T>(request: (tenant: number, headers: HttpHeaders) => Observable<T>): Observable<T> {
    let raw: any = this.storage.has('tenantId') ? this.storage.get('tenantId') : null;
    if (raw && typeof raw === 'object') raw = raw.id ?? raw.tenantId ?? raw.empresaId;
    const tenant = Number(raw);
    if (raw == null || raw === '' || !Number.isSafeInteger(tenant) || tenant <= 0) {
      return throwError(() => new Error('Empresa autenticada não identificada.'));
    }
    return request(tenant, new HttpHeaders({ 'X-Tenant-Id': String(tenant), Accept: 'application/json' }));
  }

  private query(tenant: number, params?: Record<string, any>): HttpParams {
    let query = new HttpParams().set('empresaId', tenant);
    for (const [key, value] of Object.entries(params || {})) {
      if (key !== 'empresaId' && value !== undefined && value !== null && String(value) !== '') {
        query = query.set(key, String(value));
      }
    }
    return query;
  }

  list(params?: Record<string, any>): Observable<Page<PedidoFornecedorResponse>> {
    return this.scoped((tenant, headers) =>
      this.http.get<Page<PedidoFornecedorResponse>>(this.base, {
        headers, params: this.query(tenant, params)
      }));
  }

  get(id: number | string): Observable<PedidoFornecedorResponse> {
    return this.scoped((tenant, headers) =>
      this.http.get<PedidoFornecedorResponse>(`${this.base}/${id}`, { headers }));
  }

  create(dto: PedidoFornecedorRequest): Observable<PedidoFornecedorResponse> {
    return this.scoped((tenant, headers) =>
      this.http.post<PedidoFornecedorResponse>(this.base, { ...dto, empresaId: tenant }, { headers }));
  }

  update(id: number | string, dto: PedidoFornecedorRequest): Observable<PedidoFornecedorResponse> {
    return this.scoped((tenant, headers) =>
      this.http.put<PedidoFornecedorResponse>(`${this.base}/${id}`,
        { ...dto, empresaId: tenant }, { headers }));
  }

  updateStatus(id: number | string, status: string): Observable<PedidoFornecedorResponse> {
    return this.scoped((tenant, headers) =>
      this.http.patch<PedidoFornecedorResponse>(`${this.base}/${id}/status`, null, {
        headers, params: this.query(tenant, { status })
      }));
  }

  delete(id: number | string): Observable<void> {
    return this.scoped((tenant, headers) =>
      this.http.delete<void>(`${this.base}/${id}`, { headers }));
  }
}
