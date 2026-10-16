import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

export type TipoPessoa = 'FISICA' | 'JURIDICA';
export type Modalidade = 'RODOVIARIO' | 'OUTROS';
export interface Pagina<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
export interface TransportadoraForm {
  fornecedorId?: number | null;
  tipoPessoa: TipoPessoa;
  nomeFantasia: string;
  razaoSocial?: string | null;
  cpf?: string | null;
  cnpj?: string | null;
  contato?: string | null;
  telefone?: string | null;
  email?: string | null;
  endereco?: string | null;
  cep?: string | null;
  cidade?: string | null;
  estado?: string | null;
  modalidade: Modalidade;
  ativo: boolean;
  observacoes?: string | null;
}
export interface Transportadora extends TransportadoraForm {
  id: number;
  empresaId: number;
  fornecedorId: number;
  versao: number;
  totalCompras: number;
  ultimaCompra?: string | null;
}
export interface HistoricoTransportadora {
  campo: string;
  anterior: string;
  novo: string;
  autor: number;
  data: string;
  versao: number;
}
export interface VinculoCompra {
  pedidoId: number;
  empresaId: number;
  fornecedorId: number;
  transportadoraId: number | null;
  nomeTransportadora?: string | null;
  numeroNf?: string | null;
  documentoRastreio?: string | null;
}

@Injectable({ providedIn: 'root' })
export class TransportadoraService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/transportadoras`;

  listar(page = 0, size = 20, busca = '', regiao = '', ativo?: boolean): Observable<Pagina<Transportadora>> {
    let params = new HttpParams().set('page', String(page)).set('size', String(size))
      .set('sort', 'id,desc');
    if (busca.trim()) params = params.set('busca', busca.trim());
    if (regiao.trim()) params = params.set('regiao', regiao.trim());
    if (ativo !== undefined) params = params.set('ativo', String(ativo));
    return this.http.get<Pagina<Transportadora>>(this.base, { params });
  }
  obter(id: number): Observable<Transportadora> {
    return this.http.get<Transportadora>(`${this.base}/${id}`);
  }
  criar(data: TransportadoraForm): Observable<Transportadora> {
    return this.http.post<Transportadora>(this.base, data);
  }
  editar(id: number, data: TransportadoraForm, versao: number): Observable<Transportadora> {
    return this.http.put<Transportadora>(`${this.base}/${id}`, data,
      { headers: { 'If-Match': String(versao) } });
  }
  inativar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
  historico(id: number, page = 0): Observable<Pagina<HistoricoTransportadora>> {
    return this.http.get<Pagina<HistoricoTransportadora>>(`${this.base}/${id}/historico`,
      { params: { page: String(page), size: '20' } });
  }
  consultarPedido(pedidoId: number): Observable<VinculoCompra> {
    return this.http.get<VinculoCompra>(`${this.base}/pedidos/${pedidoId}`);
  }
  vincularPedido(id: number, pedidoId: number, documentoRastreio: string): Observable<VinculoCompra> {
    return this.http.put<VinculoCompra>(`${this.base}/${id}/pedidos/${pedidoId}`,
      { documentoRastreio: documentoRastreio.trim() || null });
  }
  desvincularPedido(id: number, pedidoId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}/pedidos/${pedidoId}`);
  }
}
