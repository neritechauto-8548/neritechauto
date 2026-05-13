import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface PermissaoResponse {
  id: number;
  chave: string;
  descricao: string;
  valor: string;
}

export interface FuncaoResponse {
  id: number;
  nome: string;
  descricao: string;
  sistema: boolean;
  ativo: boolean;
  permissoes: PermissaoResponse[];
}

export interface FuncaoRequest {
  nome: string;
  descricao?: string;
  ativo?: boolean;
  permissoes?: { id: number }[];
}

@Injectable({ providedIn: 'root' })
export class PermissaoService {
  private http = inject(HttpClient);

  listFuncoes(): Observable<FuncaoResponse[]> {
    return this.http.get<FuncaoResponse[]>('/api/v1/funcoes');
  }

  getFuncao(id: number): Observable<FuncaoResponse> {
    return this.http.get<FuncaoResponse>(`/api/v1/funcoes/${id}`);
  }

  createFuncao(req: FuncaoRequest): Observable<FuncaoResponse> {
    return this.http.post<FuncaoResponse>('/api/v1/funcoes', req);
  }

  updateFuncao(id: number, req: FuncaoRequest): Observable<FuncaoResponse> {
    return this.http.put<FuncaoResponse>(`/api/v1/funcoes/${id}`, req);
  }

  deleteFuncao(id: number): Observable<void> {
    return this.http.delete<void>(`/api/v1/funcoes/${id}`);
  }

  listPermissoes(): Observable<PermissaoResponse[]> {
    return this.http.get<PermissaoResponse[]>('/api/v1/permissoes');
  }
}
