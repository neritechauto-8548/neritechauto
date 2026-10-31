import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { defer, Observable, throwError } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { empresaIdAtual } from '@shared/utils/empresa-context';
import { environment } from '../../../../environments/environment';

export interface Epi {
  id: number;
  nome: string;
  numeroCa?: string;
  categoria?: string;
  fabricante?: string;
  tamanho?: string;
  especificacoes?: string;
  descricao?: string;
  ativo: boolean;
  versao: number;
}
export type EpiRequest = Pick<Epi, 'nome'> & Partial<Omit<Epi, 'nome' | 'id' | 'ativo'>>;
export interface EpiEntrega {
  id: number;
  funcionarioId: number;
  epiId: number;
  epiNome: string;
  numeroCa?: string;
  dataEntrega: string;
  dataValidade?: string;
  observacao?: string;
  registradoEm: string;
}
export interface EpiEntregaRequest { epiId: number; dataEntrega: string; dataValidade?: string; observacao?: string; }
export interface EpiPage { content: Epi[]; totalElements: number; totalPages: number; }

@Injectable({ providedIn: 'root' })
export class EpiService {
  private http = inject(HttpClient);
  private storage = inject(LocalStorageService);
  private base = environment.baseUrl + '/v1/rh';

  private scope<T>(fn: (headers: HttpHeaders) => Observable<T>): Observable<T> {
    return defer(() => {
      const id = empresaIdAtual(this.storage);
      if (!id) return throwError(() => new Error('Empresa não identificada'));
      return fn(new HttpHeaders({ 'X-Tenant-Id': String(id) }));
    });
  }
  listar(q = '', page = 0, size = 20): Observable<EpiPage> {
    return this.scope(headers => this.http.get<EpiPage>(this.base + '/epis', {
      params: new HttpParams().set('q', q).set('page', page).set('size', size), headers
    }));
  }
  buscar(id: number): Observable<Epi> {
    return this.scope(headers => this.http.get<Epi>(this.base + '/epis/' + id, { headers }));
  }
  criar(req: EpiRequest): Observable<Epi> {
    return this.scope(headers => this.http.post<Epi>(this.base + '/epis', req, { headers }));
  }
  atualizar(id: number, req: EpiRequest): Observable<Epi> {
    return this.scope(headers => this.http.put<Epi>(this.base + '/epis/' + id, req, { headers }));
  }
  inativar(id: number): Observable<void> {
    return this.scope(headers => this.http.delete<void>(this.base + '/epis/' + id, { headers }));
  }
  entregas(funcionarioId: number): Observable<EpiEntrega[]> {
    return this.scope(headers => this.http.get<EpiEntrega[]>(this.base + '/funcionarios/' + funcionarioId + '/epis', { headers }));
  }
  entregar(funcionarioId: number, req: EpiEntregaRequest): Observable<EpiEntrega> {
    return this.scope(headers => this.http.post<EpiEntrega>(this.base + '/funcionarios/' + funcionarioId + '/epis', req, { headers }));
  }
}
