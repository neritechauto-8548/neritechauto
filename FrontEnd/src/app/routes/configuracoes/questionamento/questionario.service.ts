import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { empresaIdAtual } from '@shared/utils/empresa-context';
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

  private empresaId(): number | null { return empresaIdAtual(this.storage); }
  private missingTenant<T>(): Observable<T> {
    return throwError(() => new Error('Não foi possível identificar sua empresa. Entre novamente no sistema.'));
  }

  list(params?: { page?: number; size?: number; sort?: string }): Observable<Page<QuestionarioResponse>> {
    const tenantId = this.empresaId();
    if (!tenantId) return this.missingTenant();
    let httpParams = new HttpParams().set('empresaId', String(tenantId));
    Object.entries(params || {}).forEach(([key, value]) => {
      if (value !== null && value !== undefined) httpParams = httpParams.set(key, String(value));
    });
    return this.http.get<Page<QuestionarioResponse>>(this.base, { params: httpParams });
  }
  getById(id: number): Observable<QuestionarioResponse> {
    if (!this.empresaId()) return this.missingTenant();
    return this.http.get<QuestionarioResponse>(`${this.base}/${id}`);
  }
  create(dto: QuestionarioRequest): Observable<QuestionarioResponse> {
    const tenantId = this.empresaId();
    if (!tenantId) return this.missingTenant();
    return this.http.post<QuestionarioResponse>(this.base, { ...dto, empresaId: tenantId });
  }
  update(id: number, dto: QuestionarioRequest): Observable<QuestionarioResponse> {
    const tenantId = this.empresaId();
    if (!tenantId) return this.missingTenant();
    return this.http.put<QuestionarioResponse>(`${this.base}/${id}`, { ...dto, empresaId: tenantId });
  }
  delete(id: number): Observable<void> {
    if (!this.empresaId()) return this.missingTenant();
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
