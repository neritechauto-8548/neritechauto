import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export interface SituacaoRequest {
  empresaId: number;
  nmSituacao: string;
  dsSituacao?: string;
  corSituacao?: string;
}

export interface SituacaoResponse {
  id: number;
  empresaId: number;
  empresaNome?: string;
  nmSituacao: string;
  dsSituacao?: string;
  corSituacao?: string;
  dataCadastro?: string;
  dataAtualizacao?: string;
}

@Injectable({ providedIn: 'root' })
export class SituacaoService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/situacoes-empresa';

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

  list(params?: { page?: number; size?: number; sort?: string }): Observable<Page<SituacaoResponse>> {
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
    return this.http.get<Page<SituacaoResponse>>(this.base, { params: httpParams, headers: this.getHeaders() });
  }

  getById(id: number): Observable<SituacaoResponse> {
    return this.http.get<SituacaoResponse>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  create(dto: SituacaoRequest): Observable<SituacaoResponse> {
    return this.http.post<SituacaoResponse>(this.base, dto, { headers: this.getHeaders() });
  }

  update(id: number, dto: SituacaoRequest): Observable<SituacaoResponse> {
    return this.http.put<SituacaoResponse>(`${this.base}/${id}`, dto, { headers: this.getHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }
}
