import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

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
  usuariosVinculados: number;
  dataAtualizacao?: string;
  versao: number;
}

export interface FuncaoRequest {
  nome: string;
  descricao?: string;
  ativo?: boolean;
  permissaoIds?: number[];
  versao?: number;
}

@Injectable({ providedIn: 'root' })
export class PermissaoService {
  private http = inject(HttpClient);
  private readonly base = environment.baseUrl;

  listFuncoes(): Observable<FuncaoResponse[]>;
  listFuncoes(params: Record<string, string | number | boolean | undefined>): Observable<Page<FuncaoResponse>>;
  listFuncoes(params?: Record<string, string | number | boolean | undefined>): Observable<Page<FuncaoResponse> | FuncaoResponse[]> {
    if (!params) {
      return this.http.get<Page<FuncaoResponse>>(`${this.base}/v1/funcoes`, { params: { page: 0, size: 100 } })
        .pipe(map(page => page.content ?? []));
    }
    const clean = Object.fromEntries(Object.entries(params ?? {}).filter(([,v]) => v !== undefined));
    return this.http.get<Page<FuncaoResponse>>(`${this.base}/v1/funcoes`, { params: clean as any });
  }

  getFuncao(id: number): Observable<FuncaoResponse> {
    return this.http.get<FuncaoResponse>(`${this.base}/v1/funcoes/${id}`);
  }

  createFuncao(req: FuncaoRequest): Observable<FuncaoResponse> {
    return this.http.post<FuncaoResponse>(`${this.base}/v1/funcoes`, req);
  }

  updateFuncao(id: number, req: FuncaoRequest): Observable<FuncaoResponse> {
    return this.http.put<FuncaoResponse>(`${this.base}/v1/funcoes/${id}`, req);
  }

  deleteFuncao(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/v1/funcoes/${id}`);
  }

  listPermissoes(): Observable<PermissaoResponse[]> {
    return this.http.get<PermissaoResponse[]>(`${this.base}/v1/permissoes`);
  }

  historico(id: number): Observable<Page<any>> {
    return this.http.get<Page<any>>(`${this.base}/v1/funcoes/${id}/historico`, { params: { page: 0, size: 20 } });
  }
}
