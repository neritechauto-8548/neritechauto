import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../environments/environment';
import { Page } from './models/fornecedor.models';
import { PedidoFornecedorRequest, PedidoFornecedorResponse } from './models/compra.models';

@Injectable({ providedIn: 'root' })
export class PedidoFornecedorService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = `${environment.baseUrl}/v1/pedidos-fornecedor`;

  private getRobustTenantId(): string {
    let tenantId = this.storage.has('tenantId') ? this.storage.get('tenantId') : '1';
    if (tenantId && typeof tenantId === 'object') {
      tenantId = (tenantId as any).id || (tenantId as any).tenantId || (tenantId as any).empresaId || '1';
    }
    const val = String(tenantId);
    return (val.includes('[object') || val === 'undefined' || val === 'null') ? '1' : val;
  }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'X-Tenant-Id': this.getRobustTenantId(),
      'Accept': 'application/json',
    });
  }

  private getEmpresaIdParam(params?: Record<string, any>): HttpParams {
    let httpParams = new HttpParams();
    httpParams = httpParams.set('empresaId', this.getRobustTenantId());
    Object.entries(params || {}).forEach(([k, v]) => {
      if (v !== undefined && v !== null && `${v}` !== '') {
        httpParams = httpParams.set(k, String(v));
      }
    });
    return httpParams;
  }

  list(params?: Record<string, any>): Observable<Page<PedidoFornecedorResponse>> {
    return this.http.get<Page<PedidoFornecedorResponse>>(this.base, {
      params: this.getEmpresaIdParam(params),
      headers: this.getHeaders(),
    });
  }

  get(id: number | string): Observable<PedidoFornecedorResponse> {
    return this.http.get<PedidoFornecedorResponse>(`${this.base}/${id}`, {
      headers: this.getHeaders(),
    });
  }

  create(dto: PedidoFornecedorRequest): Observable<PedidoFornecedorResponse> {
    return this.http.post<PedidoFornecedorResponse>(this.base, dto, {
      headers: this.getHeaders(),
    });
  }

  update(id: number | string, dto: PedidoFornecedorRequest): Observable<PedidoFornecedorResponse> {
    return this.http.put<PedidoFornecedorResponse>(`${this.base}/${id}`, dto, {
      headers: this.getHeaders(),
      params: this.getEmpresaIdParam(),
    });
  }

  delete(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, {
      headers: this.getHeaders(),
      params: this.getEmpresaIdParam(),
    });
  }
}
