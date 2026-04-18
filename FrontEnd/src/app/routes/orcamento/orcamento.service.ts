
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OrcamentoRequest, OrcamentoResponse } from './models/orcamento.models';

@Injectable({ providedIn: 'root' })
export class OrcamentoService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.baseUrl}/api/v1/orcamentos`;

  create(dto: OrcamentoRequest): Observable<OrcamentoResponse> {
    return this.http.post<OrcamentoResponse>(this.apiUrl, dto);
  }

  getById(id: number): Observable<OrcamentoResponse> {
    return this.http.get<OrcamentoResponse>(`${this.apiUrl}/${id}`);
  }

  getByOrdemServicoId(osId: number): Observable<OrcamentoResponse[]> {
    return this.http.get<OrcamentoResponse[]>(`${this.apiUrl}/ordem-servico/${osId}`);
  }

  update(id: number, dto: OrcamentoRequest): Observable<OrcamentoResponse> {
    return this.http.put<OrcamentoResponse>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
