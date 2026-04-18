import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page, ServicoRequest, ServicoResponse } from '../models/servico.models';

@Injectable({ providedIn: 'root' })
export class ServicoService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = `${environment.baseUrl}/v1/servicos`;

  private getTenantId(): string {
    const t = this.storage.has('tenantId') ? this.storage.get('tenantId') : '1';
    return (!t || typeof t === 'object') ? '1' : String(t);
  }

  private headers(): HttpHeaders {
    return new HttpHeaders({ 'X-Tenant-Id': this.getTenantId(), 'Accept': 'application/json' });
  }

  private params(extra?: Record<string, any>): HttpParams {
    let p = new HttpParams().set('empresaId', this.getTenantId());
    Object.entries(extra || {}).forEach(([k, v]) => {
      if (v !== undefined && v !== null && `${v}` !== '') p = p.set(k, String(v));
    });
    return p;
  }

  list(args?: Record<string, any>): Observable<Page<ServicoResponse>> {
    return this.http.get<Page<ServicoResponse>>(this.base, { params: this.params(args), headers: this.headers() });
  }

  get(id: number | string): Observable<ServicoResponse> {
    return this.http.get<ServicoResponse>(`${this.base}/${id}`, { headers: this.headers() });
  }

  create(dto: ServicoRequest): Observable<ServicoResponse> {
    return this.http.post<ServicoResponse>(this.base, dto, { headers: this.headers(), params: this.params() });
  }

  update(id: number | string, dto: ServicoRequest): Observable<ServicoResponse> {
    return this.http.put<ServicoResponse>(`${this.base}/${id}`, dto, { headers: this.headers(), params: this.params() });
  }

  delete(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.headers(), params: this.params() });
  }
}
