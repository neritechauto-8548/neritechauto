import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export interface ChecklistRequest {
  empresaId: number;
  dsChecklist: string;
  urlImagem?: string;
}

export interface ChecklistResponse {
  id: number;
  empresaId: number;
  dsChecklist: string;
  urlImagem?: string;
  dataCadastro?: string;
  dataAtualizacao?: string;
}

@Injectable({ providedIn: 'root' })
export class ChecklistService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/ordens-servico/checklists';

  private getHeaders(): HttpHeaders {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    return new HttpHeaders({
      'X-Tenant-Id': String(tenantId),
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    });
  }

  private getEmpresaIdParam(params?: Record<string, any>): HttpParams {
    let httpParams = new HttpParams();
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    httpParams = httpParams.set('empresaId', String(tenantId));
    if (params) {
      Object.keys(params).forEach(key => {
        const val = (params as any)[key];
        if (val !== null && val !== undefined) {
          httpParams = httpParams.set(key, String(val));
        }
      });
    }
    return httpParams;
  }

  list(params?: { page?: number; size?: number; sort?: string }): Observable<Page<ChecklistResponse>> {
    return this.http.get<Page<ChecklistResponse>>(this.base, { params: this.getEmpresaIdParam(params), headers: this.getHeaders() });
  }

  get(id: number): Observable<ChecklistResponse> {
    return this.http.get<ChecklistResponse>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  create(dto: ChecklistRequest): Observable<ChecklistResponse> {
    return this.http.post<ChecklistResponse>(this.base, dto, { params: this.getEmpresaIdParam(), headers: this.getHeaders() });
  }

  update(id: number, dto: ChecklistRequest): Observable<ChecklistResponse> {
    return this.http.put<ChecklistResponse>(`${this.base}/${id}`, dto, { params: this.getEmpresaIdParam(), headers: this.getHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { params: this.getEmpresaIdParam(), headers: this.getHeaders() });
  }
}
