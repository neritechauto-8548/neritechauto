import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page, FornecedorRequest, FornecedorResponse } from './models/fornecedor.models';

@Injectable({ providedIn: 'root' })
export class FornecedorService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/fornecedores`;

  list(params?: Record<string, unknown>): Observable<Page<FornecedorResponse>> {
    let httpParams = new HttpParams();
    Object.entries(params || {}).forEach(([key, value]) => {
      if (value !== undefined && value !== null && `${value}` !== '') {
        httpParams = httpParams.set(key, String(value));
      }
    });
    return this.http.get<Page<FornecedorResponse>>(this.base, { params: httpParams });
  }

  get(id: number | string): Observable<FornecedorResponse> {
    return this.http.get<FornecedorResponse>(`${this.base}/${id}`);
  }

  create(dto: FornecedorRequest & { empresaId?: unknown }): Observable<FornecedorResponse> {
    return this.http.post<FornecedorResponse>(this.base, this.tenantNeutralPayload(dto));
  }

  update(
    id: number | string,
    dto: FornecedorRequest & { empresaId?: unknown }
  ): Observable<FornecedorResponse> {
    return this.http.put<FornecedorResponse>(`${this.base}/${id}`, this.tenantNeutralPayload(dto));
  }

  delete(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  private tenantNeutralPayload(dto: FornecedorRequest & { empresaId?: unknown }): FornecedorRequest {
    // Compatibilidade defensiva com componentes antigos: mesmo que ainda exista
    // empresaId num objeto local, ele nunca atravessa a fronteira HTTP.
    const { empresaId: _ignored, ...payload } = dto;
    return payload;
  }
}
