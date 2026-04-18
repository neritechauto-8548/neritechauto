import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from './categoria-produto.service'; // Reuse Page interface

export interface UnidadeMedidaRequest {
  nome: string;
  sigla: string;
  ativo?: boolean;
}

export interface UnidadeMedidaResponse {
  id: number;
  nome: string;
  sigla: string;
  ativo?: boolean;
}

@Injectable({ providedIn: 'root' })
export class UnidadeMedidaService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/unidades-medida';

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      Accept: 'application/json'
    });
  }

  list(params?: any): Observable<Page<UnidadeMedidaResponse>> {
    let httpParams = new HttpParams();

    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== null && params[key] !== undefined) {
          httpParams = httpParams.set(key, params[key]);
        }
      });
    }
    return this.http.get<Page<UnidadeMedidaResponse>>(this.base, { params: httpParams, headers: this.getHeaders() });
  }

  getById(id: number): Observable<UnidadeMedidaResponse> {
    return this.http.get<UnidadeMedidaResponse>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  create(dto: UnidadeMedidaRequest): Observable<UnidadeMedidaResponse> {
    return this.http.post<UnidadeMedidaResponse>(this.base, dto, { headers: this.getHeaders() });
  }

  update(id: number, dto: UnidadeMedidaRequest): Observable<UnidadeMedidaResponse> {
    return this.http.put<UnidadeMedidaResponse>(`${this.base}/${id}`, dto, { headers: this.getHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }
}
