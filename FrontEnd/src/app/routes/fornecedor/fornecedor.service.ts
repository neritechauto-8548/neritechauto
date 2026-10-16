import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../environments/environment';
import { Page, FornecedorRequest, FornecedorResponse, HistoricoFornecedor } from './models/fornecedor.models';

@Injectable({ providedIn: 'root' })
export class FornecedorService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = `${environment.baseUrl}/v1/fornecedores`;

  private getTenantId(): number | null {
    let raw: any = this.storage.has('tenantId') ? this.storage.get('tenantId') : null;
    if (raw && typeof raw === 'object') raw = raw.id ?? raw.tenantId ?? raw.empresaId;
    const id = Number(raw);
    return raw != null && raw !== '' && Number.isSafeInteger(id) && id > 0 ? id : null;
  }

  private tenant(): number {
    const id = this.getTenantId();
    if (!id) throw new Error('Sessão sem empresa autenticada');
    return id;
  }

  private headers(id: number): HttpHeaders {
    return new HttpHeaders({ 'X-Tenant-Id': String(id), Accept: 'application/json' });
  }

  private params(empresaId: number, values?: Record<string, any>): HttpParams {
    let params = new HttpParams().set('empresaId', empresaId);
    for (const [key, value] of Object.entries(values || {})) {
      if (key !== 'empresaId' && value !== undefined && value !== null && String(value) !== '') {
        params = params.set(key, String(value));
      }
    }
    return params;
  }

  private safe<T>(call: (id: number) => Observable<T>): Observable<T> {
    try { return call(this.tenant()); }
    catch { return throwError(() => new Error('Empresa autenticada não identificada. Entre novamente.')); }
  }

  list(params?: Record<string, any>): Observable<Page<FornecedorResponse>> {
    return this.safe(id => this.http.get<Page<FornecedorResponse>>(this.base, {
      params: this.params(id, params), headers: this.headers(id)
    }));
  }

  get(id: number | string): Observable<FornecedorResponse> {
    return this.safe(tenant => this.http.get<FornecedorResponse>(`${this.base}/${id}`, {
      headers: this.headers(tenant)
    }));
  }

  create(dto: Omit<FornecedorRequest, 'empresaId'>): Observable<FornecedorResponse> {
    return this.safe(id => this.http.post<FornecedorResponse>(this.base,
      { ...dto, empresaId: id }, { headers: this.headers(id) }));
  }

  update(id: number | string, dto: Omit<FornecedorRequest, 'empresaId'>): Observable<FornecedorResponse> {
    return this.safe(tenant => this.http.put<FornecedorResponse>(`${this.base}/${id}`,
      { ...dto, empresaId: tenant }, {
        headers: dto.versao != null
          ? this.headers(tenant).set('If-Match', String(dto.versao)) : this.headers(tenant)
      }));
  }

  historico(id: number | string, page = 0, size = 20): Observable<Page<HistoricoFornecedor>> {
    return this.safe(tenant => this.http.get<Page<HistoricoFornecedor>>(`${this.base}/${id}/historico`, {
      headers: this.headers(tenant), params: { page, size }
    }));
  }

  /** Rota legada DELETE inativa sem remover pedidos, cotações e documentos. */
  delete(id: number | string): Observable<void> {
    return this.safe(tenant => this.http.delete<void>(`${this.base}/${id}`, {
      headers: this.headers(tenant)
    }));
  }
}
