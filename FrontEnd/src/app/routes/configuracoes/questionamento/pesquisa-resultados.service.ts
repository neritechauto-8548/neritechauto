import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

export interface PesquisaRespostaItem { perguntaId: number; perguntaTexto: string; tipo: 'SN'|'AV'|'AB'; valor: string; }
export interface PesquisaResposta { id: number; questionarioId: number; clienteId: number; dataCadastro?: string; respostas: PesquisaRespostaItem[]; }
export interface PesquisaResumo { questionarioId: number; totalRespostas: number; }
export interface ClientePesquisa { id: number; nomeCompleto?: string; razaoSocial?: string; nomeFantasia?: string; }
export interface Pagina<T> { content: T[]; totalElements: number; number: number; size: number; }

@Injectable({ providedIn: 'root' })
export class PesquisaResultadosService {
  private readonly http = inject(HttpClient);
  private readonly url = environment.baseUrl + '/v1/comunicacao/questionarios';
  private readonly clientesUrl = environment.baseUrl + '/v1/clientes';

  resumo(id: number): Observable<PesquisaResumo> {
    return this.http.get<PesquisaResumo>(`${this.url}/${id}/respostas/resumo`);
  }
  listar(id: number, page: number, size = 10): Observable<Pagina<PesquisaResposta>> {
    const params = new HttpParams().set('page', String(page)).set('size', String(size))
      .set('sort', 'dataCadastro,desc');
    return this.http.get<Pagina<PesquisaResposta>>(`${this.url}/${id}/respostas`, { params });
  }
  buscarClientes(busca: string): Observable<Pagina<ClientePesquisa>> {
    const params = new HttpParams().set('busca', busca).set('size', '10').set('page', '0');
    return this.http.get<Pagina<ClientePesquisa>>(this.clientesUrl, { params });
  }
  registrar(questionarioId: number, input: {
    clienteId: number; chaveIdempotencia: string; respostas: { perguntaId: number; valor: string }[]
  }): Observable<PesquisaResposta> {
    return this.http.post<PesquisaResposta>(`${this.url}/${questionarioId}/respostas`, input);
  }
}
