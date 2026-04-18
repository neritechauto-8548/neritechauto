import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export type TipoItem = 'SN' | 'AV';

export interface ItQuestionarioRequest {
  questionarioId: number;
  dsItQuestionario: string;
  tpItQuestionario: TipoItem;
}

export interface ItQuestionarioResponse {
  id: number;
  questionarioId: number;
  dsItQuestionario: string;
  tpItQuestionario: TipoItem;
  dataCadastro?: string;
  dataAtualizacao?: string;
}

@Injectable({ providedIn: 'root' })
export class ItQuestionarioService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/comunicacao/it-questionario';

  private getHeaders(): HttpHeaders {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    return new HttpHeaders({
      'X-Tenant-Id': String(tenantId),
      'Accept': 'application/json',
    });
  }

  list(params?: { page?: number; size?: number; sort?: string }): Observable<Page<ItQuestionarioResponse>> {
    return this.http.get<Page<ItQuestionarioResponse>>(this.base, { params: params as any, headers: this.getHeaders() });
  }

  listPorQuestionario(questionarioId: number): Observable<ItQuestionarioResponse[]> {
    return this.http.get<ItQuestionarioResponse[]>(`${this.base}/questionario/${questionarioId}`, { headers: this.getHeaders() });
  }

  getById(id: number): Observable<ItQuestionarioResponse> {
    return this.http.get<ItQuestionarioResponse>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  create(dto: ItQuestionarioRequest): Observable<ItQuestionarioResponse> {
    return this.http.post<ItQuestionarioResponse>(this.base, dto, { headers: this.getHeaders() });
  }

  update(id: number, dto: ItQuestionarioRequest): Observable<ItQuestionarioResponse> {
    return this.http.put<ItQuestionarioResponse>(`${this.base}/${id}`, dto, { headers: this.getHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }
}
