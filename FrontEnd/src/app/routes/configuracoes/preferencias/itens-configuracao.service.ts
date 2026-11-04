import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

export type GrupoItemConfiguracao = 'CLIENTE_CATEGORIA' | 'CLIENTE_RECOMENDACAO' | 'FINANCEIRO_ETIQUETA' | 'VENDA_DIRETA_ETIQUETA' | 'ORDEM_SERVICO_ETIQUETA' | 'ORDEM_SERVICO_TAG_ITEM';
export interface ItemConfiguracao { id: number; grupo: GrupoItemConfiguracao; descricao: string; cor: string; atualizadoEm: string; }
export interface AlteracaoItemConfiguracao { grupo: GrupoItemConfiguracao; descricao: string; cor: string; }

@Injectable({ providedIn: 'root' })
export class ItensConfiguracaoService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/itens-configuracao`;
  listar(grupo: GrupoItemConfiguracao, busca = ''): Observable<ItemConfiguracao[]> {
    let parametros = new HttpParams().set('grupo', grupo);
    if (busca.trim()) parametros = parametros.set('busca', busca.trim());
    return this.http.get<ItemConfiguracao[]>(this.base, { params: parametros });
  }
  criar(item: AlteracaoItemConfiguracao): Observable<ItemConfiguracao> { return this.http.post<ItemConfiguracao>(this.base, item); }
  editar(id: number, item: AlteracaoItemConfiguracao): Observable<ItemConfiguracao> { return this.http.put<ItemConfiguracao>(`${this.base}/${id}`, item); }
  excluir(id: number, grupo: GrupoItemConfiguracao): Observable<void> { return this.http.delete<void>(`${this.base}/${id}`, { params: { grupo } }); }
}
