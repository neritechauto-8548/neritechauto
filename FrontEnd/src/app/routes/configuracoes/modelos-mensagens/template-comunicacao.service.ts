import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';
import { Page } from '../categoria/categoria-produto.service';

/** Relacionado ao enum TipoTemplate no backend */
export type TipoTemplate = 'PUSH_NOTIFICATION' | 'EMAIL' | 'SMS' | 'WHATSAPP';

/** Relacionado ao enum CategoriaTemplate no backend */
export type CategoriaTemplate = 'PESQUISA_SATISFACAO' | 'STATUS_OS' | 'LEMBRETE' | 'ANIVERSARIO' | 'AGENDAMENTO' | 'COBRANCA' | 'OUTROS' | 'BOAS_VINDAS' | 'PROMOCIONAL';

export interface TemplateComunicacaoResponse {
  id: number;
  empresaId: number;
  nome: string;
  tipoTemplate: TipoTemplate;
  categoria: CategoriaTemplate;
  assunto?: string;
  conteudo: string;
  variaveisDisponiveis?: string;
  anexosPadrao?: string;
  ativo: boolean;
  padraoCategoria: boolean;
  idioma?: string;
  personalizavel: boolean;
  requerAprovacao: boolean;
  tags?: string;
  dataCadastro?: string;
  dataAtualizacao?: string;
}

export interface TemplateComunicacaoRequest {
  empresaId: number;
  nome: string;
  tipoTemplate: TipoTemplate;
  categoria: CategoriaTemplate;
  assunto?: string;
  conteudo: string;
  variaveisDisponiveis?: string;
  anexosPadrao?: string;
  ativo: boolean;
  padraoCategoria: boolean;
  idioma?: string;
  personalizavel: boolean;
  requerAprovacao: boolean;
  tags?: string;
}

@Injectable({
  providedIn: 'root'
})
export class TemplateComunicacaoService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.baseUrl}/v1/comunicacao/templates`;

  list(empresaId: number, params: { page: number; size: number; sort?: string }): Observable<Page<TemplateComunicacaoResponse>> {
    let httpParams = new HttpParams()
      .set('empresaId', String(empresaId))
      .set('page', String(params.page))
      .set('size', String(params.size));

    if (params.sort) {
      httpParams = httpParams.set('sort', params.sort);
    }

    return this.http.get<Page<TemplateComunicacaoResponse>>(this.baseUrl, { params: httpParams });
  }

  findById(id: number): Observable<TemplateComunicacaoResponse> {
    return this.http.get<TemplateComunicacaoResponse>(`${this.baseUrl}/${id}`);
  }

  create(data: TemplateComunicacaoRequest): Observable<TemplateComunicacaoResponse> {
    return this.http.post<TemplateComunicacaoResponse>(this.baseUrl, data);
  }

  update(id: number, data: TemplateComunicacaoRequest): Observable<TemplateComunicacaoResponse> {
    return this.http.put<TemplateComunicacaoResponse>(`${this.baseUrl}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
