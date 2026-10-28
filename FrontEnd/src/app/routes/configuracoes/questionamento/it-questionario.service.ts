import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export type TipoItem = 'SN' | 'AV' | 'AB';
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
  private readonly base = environment.baseUrl + '/v1/comunicacao/it-questionario';
  // Interceptors attach JWT and authenticated tenant; never supply a default company.
  list(params?: { page?: number; size?: number; sort?: string }): Observable<Page<ItQuestionarioResponse>> {
    return this.http.get<Page<ItQuestionarioResponse>>(this.base, { params: params as any });
  }
  listPorQuestionario(questionarioId: number): Observable<ItQuestionarioResponse[]> {
    return this.http.get<ItQuestionarioResponse[]>(`${this.base}/questionario/${questionarioId}`);
  }
  getById(id: number): Observable<ItQuestionarioResponse> {
    return this.http.get<ItQuestionarioResponse>(`${this.base}/${id}`);
  }
  create(dto: ItQuestionarioRequest): Observable<ItQuestionarioResponse> {
    return this.http.post<ItQuestionarioResponse>(this.base, dto);
  }
  update(id: number, dto: ItQuestionarioRequest): Observable<ItQuestionarioResponse> {
    return this.http.put<ItQuestionarioResponse>(`${this.base}/${id}`, dto);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
