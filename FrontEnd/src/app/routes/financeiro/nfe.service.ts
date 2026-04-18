import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
interface Pageable {
  page?: number;
  size?: number;
  sort?: string[] | string;
}
import { environment } from '../../../environments/environment';
import { NfeRequest, NfeResponse } from './models/nfe.models';

@Injectable({
  providedIn: 'root',
})
export class NfeService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.baseUrl}/api/v1/financeiro/nfe`;

  list(query: { empresaId: number } & Pageable): Observable<Page<NfeResponse>> {
    let params = new HttpParams()
      .set('empresaId', String(query.empresaId))
      .set('page', String(query.page ?? 0))
      .set('size', String(query.size ?? 10));

    if (query.sort) {
      const sortValue = Array.isArray(query.sort) ? query.sort.join(',') : query.sort;
      params = params.set('sort', sortValue);
    }

    return this.http.get<Page<NfeResponse>>(this.API_URL, { params });
  }

  getById(id: number, empresaId = 1): Observable<NfeResponse> {
      return this.http.get<NfeResponse>(`${this.API_URL}/${id}`, { params: { empresaId } });
  }

  create(request: NfeRequest, empresaId = 1): Observable<NfeResponse> {
      return this.http.post<NfeResponse>(this.API_URL, request, { params: { empresaId } });
  }

  update(id: number, request: NfeRequest, empresaId = 1): Observable<NfeResponse> {
      return this.http.put<NfeResponse>(`${this.API_URL}/${id}`, request, { params: { empresaId } });
  }

  delete(id: number, empresaId = 1): Observable<void> {
      return this.http.delete<void>(`${this.API_URL}/${id}`, { params: { empresaId } });
  }

  // Simular transmição (mock backend logic if needed, but here just an update likely)
  transmitir(id: number, empresaId = 1): Observable<NfeResponse> {
      // Por enquanto apenas atualiza, mas poderia ter endpoint específico /transmitir
      return this.http.post<NfeResponse>(`${this.API_URL}/${id}/transmitir`, {}, { params: { empresaId } });
  }
}
