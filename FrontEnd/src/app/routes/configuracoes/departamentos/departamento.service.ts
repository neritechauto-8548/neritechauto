import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export interface DepartamentoContabioRequest {
  empresaId: number;
  descricao: string;
}

export interface DepartamentoContabioResponse {
  id: number;
  empresaId: number;
  empresaNome?: string;
  descricao: string;
  dataCadastro?: string;
  dataAtualizacao?: string;
}

@Injectable({ providedIn: 'root' })
export class DepartamentoService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/departamentos-contabio-empresa';

  private getTenantId(): string {
    let tenantId: any = this.storage.has('empresaId') ? this.storage.get('empresaId') :
                        this.storage.has('tenantId') ? this.storage.get('tenantId') : null;

    if (tenantId && typeof tenantId === 'object') {
      tenantId = tenantId.id || tenantId.tenantId || tenantId.empresaId || null;
    }
    
    if (!tenantId) {
      const tokenStr = localStorage.getItem('ng-matero-token');
      if (tokenStr) {
        try {
          const tokenObj = JSON.parse(tokenStr);
          if (tokenObj && tokenObj.access_token) {
            const payload = JSON.parse(atob(tokenObj.access_token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
            tenantId = payload.empresaId || payload.tenantId;
          }
        } catch (e) {}
      }
    }

    return String(tenantId || '');
  }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'X-Tenant-Id': this.getTenantId(),
      'Accept': 'application/json'
    });
  }

  list(params?: { page?: number; size?: number; sort?: string }): Observable<Page<DepartamentoContabioResponse>> {
    let httpParams = new HttpParams();
    httpParams = httpParams.set('empresaId', this.getTenantId());
    if (params) {
      Object.keys(params).forEach(key => {
        const val = (params as any)[key];
        if (val !== null && val !== undefined) {
          httpParams = httpParams.set(key, String(val));
        }
      });
    }
    return this.http.get<Page<DepartamentoContabioResponse>>(this.base, { params: httpParams, headers: this.getHeaders() });
  }

  listPorEmpresa(empresaId: number): Observable<DepartamentoContabioResponse[]> {
    return this.http.get<DepartamentoContabioResponse[]>(`${this.base}/empresa/${empresaId}`, { headers: this.getHeaders() });
  }

  getById(id: number): Observable<DepartamentoContabioResponse> {
    return this.http.get<DepartamentoContabioResponse>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  create(dto: DepartamentoContabioRequest): Observable<DepartamentoContabioResponse> {
    return this.http.post<DepartamentoContabioResponse>(this.base, dto, { headers: this.getHeaders() });
  }

  update(id: number, dto: DepartamentoContabioRequest): Observable<DepartamentoContabioResponse> {
    return this.http.put<DepartamentoContabioResponse>(`${this.base}/${id}`, dto, { headers: this.getHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }
}
