import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

export interface Seguradora {
  id: number;
  empresaId: number;
  cnpj: string;
  razaoSocial: string;
  nomeFantasia: string | null;
  inscricaoEstadual: string | null;
  inscricaoMunicipal: string | null;
  email: string | null;
  observacoes: string | null;
  margemRefatoramento: number | null;
  site: string | null;
  retencaoIss: boolean;
  ativo: boolean;
  versao: number;
  totalOS: number;
}

export type SeguradoraForm = Pick<Seguradora,
  'cnpj' | 'razaoSocial' | 'nomeFantasia' | 'inscricaoEstadual' | 'inscricaoMunicipal' |
  'email' | 'observacoes' | 'margemRefatoramento' | 'site' | 'retencaoIss' | 'ativo'>;

export interface FiltrosSeguradoras {
  cnpj?: string;
  nome?: string;
  nomeFantasia?: string;
}

export interface Pagina<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
export interface VinculoOS {
  seguradoraId: number;
  seguradora: string;
  ordemServicoId: number;
  tipoVinculo: 'CONVENIO' | 'REEMBOLSO';
}
export interface HistoricoSeguradora {
  campo: string;
  anterior: string;
  novo: string;
  autor: number;
  data: string;
  versao: number;
}

@Injectable({ providedIn: 'root' })
export class SeguradoraService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/seguradoras`;

  listar(page = 0, size = 20, busca = '', ativo?: boolean,
    filtros: FiltrosSeguradoras = {}): Observable<Pagina<Seguradora>> {
    let params = new HttpParams().set('page', page).set('size', size)
      .set('sort', 'id,desc');
    if (busca.trim()) params = params.set('busca', busca.trim());
    if (ativo !== undefined) params = params.set('ativo', String(ativo));
    if (filtros.cnpj?.trim()) params = params.set('cnpj', filtros.cnpj.replace(/\\D/g, ''));
    if (filtros.nome?.trim()) params = params.set('nome', filtros.nome.trim());
    if (filtros.nomeFantasia?.trim()) params = params.set('nomeFantasia', filtros.nomeFantasia.trim());
    return this.http.get<Pagina<Seguradora>>(this.base, { params });
  }
  obter(id: number): Observable<Seguradora> {
    return this.http.get<Seguradora>(`${this.base}/${id}`);
  }
  criar(payload: SeguradoraForm): Observable<Seguradora> {
    return this.http.post<Seguradora>(this.base, payload);
  }
  editar(id: number, payload: SeguradoraForm, versao: number): Observable<Seguradora> {
    return this.http.put<Seguradora>(`${this.base}/${id}`, payload,
      { headers: { 'If-Match': String(versao) } });
  }
  inativar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
  historico(id: number, page = 0): Observable<Pagina<HistoricoSeguradora>> {
    return this.http.get<Pagina<HistoricoSeguradora>>(`${this.base}/${id}/historico`,
      { params: { page, size: 20 } });
  }
  vinculoOS(ordemId: number): Observable<VinculoOS | null> {
    return this.http.get<VinculoOS | null>(`${this.base}/os/${ordemId}`);
  }
  vincular(id: number, ordemId: number, tipo: 'CONVENIO' | 'REEMBOLSO'): Observable<VinculoOS> {
    return this.http.post<VinculoOS>(`${this.base}/${id}/os/${ordemId}`, {},
      { params: { tipo } });
  }
  desvincular(id: number, ordemId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}/os/${ordemId}`);
  }
}
