import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export interface QuestionarioRequest {
  empresaId: number;
  dsQuestionario: string;
  urlImagem?: string;
  snAtivo?: boolean;
}

export interface QuestionarioResponse {
  id: number;
  empresaId: number;
  dsQuestionario: string;
  urlImagem?: string;
  snAtivo?: boolean;
  dataCadastro?: string;
  dataAtualizacao?: string;
}

@Injectable({ providedIn: 'root' })
export class QuestionarioService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/comunicacao/questionarios';

  private getHeaders(): HttpHeaders {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    return new HttpHeaders({
      'X-Tenant-Id': String(tenantId),
      'Accept': 'application/json',
    });
  }

  list(params?: { page?: number; size?: number; sort?: string }): Observable<Page<QuestionarioResponse>> {
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
    return this.http.get<Page<QuestionarioResponse>>(this.base, { params: httpParams, headers: this.getHeaders() });
  }

  getById(id: number): Observable<QuestionarioResponse> {
    return this.http.get<QuestionarioResponse>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  create(dto: QuestionarioRequest): Observable<QuestionarioResponse> {
    return this.http.post<QuestionarioResponse>(this.base, dto, { headers: this.getHeaders() });
  }

  update(id: number, dto: QuestionarioRequest): Observable<QuestionarioResponse> {
    return this.http.put<QuestionarioResponse>(`${this.base}/${id}`, dto, { headers: this.getHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }
}
