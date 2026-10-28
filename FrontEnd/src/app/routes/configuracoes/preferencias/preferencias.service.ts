import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

export type Chave = 'locale' | 'precisaoQuantidades' | 'validadeOrcamentoDias'
  | 'avisosAtivos' | 'retencaoHistoricoDias';
export interface Politica {
  chave: Chave;
  secao: string;
  titulo: string;
  valorAplicado: string;
  valorSalvo: string | null;
  origem: 'PADRAO' | 'EMPRESA' | 'PADRAO_INATIVA';
  ativo: boolean;
  versao: number | null;
  alteradoEm: string | null;
  responsavel: number | null;
}
export interface Integracao {
  canal: string;
  estado: 'NAO_CONFIGURADA' | 'CADASTRADA' | 'TESTADA';
}
export interface HistoricoPolitica {
  campo: string; anterior: string; novo: string;
  autor: number; data: string; versao: number;
}
export interface Page<T> {
  content: T[]; totalElements: number; totalPages: number; number: number; size: number;
}
@Injectable({ providedIn: 'root' })
export class PreferenciasService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/politicas-gerais`;

  listar(busca = '', secao = '', ativo?: boolean, page = 0, size = 20): Observable<Page<Politica>> {
    let params = new HttpParams().set('page', String(page)).set('size', String(size))
      .set('sort', 'chave,asc');
    if (busca.trim()) params = params.set('busca', busca.trim());
    if (secao) params = params.set('secao', secao);
    if (ativo !== undefined) params = params.set('ativo', String(ativo));
    return this.http.get<Page<Politica>>(this.base, { params });
  }
  listarIntegracoes(): Observable<Integracao[]> {
    return this.http.get<Integracao[]>(`${this.base}/integracoes`);
  }
  criar(chave: Chave, valor: string): Observable<Politica> {
    return this.http.post<Politica>(this.base, { chave, valor });
  }
  editar(chave: Chave, valor: string, versao: number): Observable<Politica> {
    return this.http.put<Politica>(`${this.base}/${chave}`, { chave, valor },
      { headers: { 'If-Match': String(versao) } });
  }
  status(chave: Chave, ativo: boolean, versao: number): Observable<Politica> {
    return this.http.patch<Politica>(`${this.base}/${chave}/status`, { ativo },
      { headers: { 'If-Match': String(versao) } });
  }
  historico(chave: Chave, page = 0): Observable<Page<HistoricoPolitica>> {
    return this.http.get<Page<HistoricoPolitica>>(`${this.base}/${chave}/historico`,
      { params: { page: String(page), size: '20' } });
  }
}
