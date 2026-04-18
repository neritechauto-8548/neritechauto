import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface CategoriaProdutoRequest {
  empresaId: number;
  nome: string; // Nome da categoria (Máx: 100 chars)
  ativo?: boolean; // Status de ativação
}

export interface CategoriaProdutoResponse {
  id: number;
  empresaId: number;
  nome: string;
  ativo?: boolean;
}

@Injectable({ providedIn: 'root' })
export class CategoriaProdutoService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/categorias-produtos';

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      Accept: 'application/json'
    });
  }

  list(params?: any): Observable<Page<CategoriaProdutoResponse>> {
    let httpParams = new HttpParams();

    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== null && params[key] !== undefined) {
          httpParams = httpParams.set(key, params[key]);
        }
      });
    }
    return this.http.get<Page<CategoriaProdutoResponse>>(this.base, { params: httpParams, headers: this.getHeaders() });
  }

  listRoots(): Observable<CategoriaProdutoResponse[]> {
    return this.http.get<CategoriaProdutoResponse[]>(`${this.base}/roots`, { headers: this.getHeaders() });
  }

  getById(id: number): Observable<CategoriaProdutoResponse> {
    return this.http.get<CategoriaProdutoResponse>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  create(dto: CategoriaProdutoRequest): Observable<CategoriaProdutoResponse> {
    return this.http.post<CategoriaProdutoResponse>(this.base, dto, { headers: this.getHeaders() });
  }

  update(id: number, dto: CategoriaProdutoRequest): Observable<CategoriaProdutoResponse> {
    return this.http.put<CategoriaProdutoResponse>(`${this.base}/${id}`, dto, { headers: this.getHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }
}
