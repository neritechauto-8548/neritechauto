import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

interface Pageable {
  page?: number;
  size?: number;
  sort?: string[] | string;
}

import { environment } from '../../../environments/environment';
import {
  ContasPagarRequest,
  ContasPagarResponse,
  ContasReceberRequest,
  ContasReceberResponse,
  DashboardFinanceiroDTO,
  AnexoTituloDTO
} from './models/financeiro.models';

@Injectable({
  providedIn: 'root',
})
export class FinanceiroService {
  private readonly http = inject(HttpClient);
  private readonly API_URL_PAGAR = `${environment.baseUrl}/v1/financeiro/contas-pagar`;
  private readonly API_URL_RECEBER = `${environment.baseUrl}/v1/financeiro/contas-receber`;
  private readonly API_URL_FLUXO = `${environment.baseUrl}/v1/financeiro/fluxo-caixa`;
  private readonly API_URL_FECHAMENTO = `${environment.baseUrl}/v1/financeiro/fechamento-caixa`;

  // Tenant nunca é escolhido pelo navegador. O backend deriva a empresa da sessão/JWT.

  // --- Contas a Pagar ---

  listPagar(query: Pageable): Observable<Page<ContasPagarResponse>> {
    let params = this.pageableParams(query);
    return this.http.get<Page<ContasPagarResponse>>(this.API_URL_PAGAR, { params });
  }

  getPagarById(id: number): Observable<ContasPagarResponse> {
    return this.http.get<ContasPagarResponse>(`${this.API_URL_PAGAR}/${id}`);
  }

  createPagar(request: ContasPagarRequest): Observable<ContasPagarResponse> {
    return this.http.post<ContasPagarResponse>(this.API_URL_PAGAR, request);
  }

  updatePagar(id: number, request: ContasPagarRequest): Observable<ContasPagarResponse> {
    return this.http.put<ContasPagarResponse>(`${this.API_URL_PAGAR}/${id}`, request);
  }

  deletePagar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL_PAGAR}/${id}`);
  }

  // --- Contas a Receber ---

  listReceber(query: {
    termo?: string;
    dataInicio?: string;
    dataFim?: string;
    status?: string;
    contaBancariaId?: number;
    centroCustoId?: number;
    planoContasId?: number;
    formaPagamentoId?: number;
  } & Pageable): Observable<Page<ContasReceberResponse>> {
    let params = this.pageableParams(query);

    if (query.termo) params = params.set('termo', query.termo);
    if (query.dataInicio) params = params.set('dataInicio', query.dataInicio);
    if (query.dataFim) params = params.set('dataFim', query.dataFim);
    if (query.status) params = params.set('status', query.status);
    if (query.contaBancariaId != null) params = params.set('contaBancariaId', String(query.contaBancariaId));
    if (query.centroCustoId != null) params = params.set('centroCustoId', String(query.centroCustoId));
    if (query.planoContasId != null) params = params.set('planoContasId', String(query.planoContasId));
    if (query.formaPagamentoId != null) params = params.set('formaPagamentoId', String(query.formaPagamentoId));

    return this.http.get<Page<ContasReceberResponse>>(this.API_URL_RECEBER, { params });
  }

  getReceberById(id: number): Observable<ContasReceberResponse> {
    return this.http.get<ContasReceberResponse>(`${this.API_URL_RECEBER}/${id}`);
  }

  createReceber(request: ContasReceberRequest): Observable<ContasReceberResponse> {
    return this.http.post<ContasReceberResponse>(this.API_URL_RECEBER, request);
  }

  updateReceber(id: number, request: ContasReceberRequest): Observable<ContasReceberResponse> {
    return this.http.put<ContasReceberResponse>(`${this.API_URL_RECEBER}/${id}`, request);
  }

  deleteReceber(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL_RECEBER}/${id}`);
  }

  getDashboardReceber(): Observable<DashboardFinanceiroDTO> {
    return this.http.get<DashboardFinanceiroDTO>(`${this.API_URL_RECEBER}/dashboard`);
  }

  receberTitulo(id: number, request: unknown): Observable<ContasReceberResponse> {
    return this.http.post<ContasReceberResponse>(`${this.API_URL_RECEBER}/${id}/recebimentos`, request);
  }

  desfazerQuitacao(id: number): Observable<ContasReceberResponse> {
    return this.http.post<ContasReceberResponse>(`${this.API_URL_RECEBER}/${id}/desfazer-quitacao`, {});
  }

  renegociarTitulo(id: number, request: unknown): Observable<unknown> {
    return this.http.post<unknown>(`${this.API_URL_RECEBER}/${id}/renegociar`, request);
  }

  uploadAnexo(id: number, file: File): Observable<AnexoTituloDTO> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<AnexoTituloDTO>(`${this.API_URL_RECEBER}/${id}/anexos`, formData);
  }

  downloadAnexo(contaId: number, anexoId: number): Observable<Blob> {
    return this.http.get(`${this.API_URL_RECEBER}/${contaId}/anexos/${anexoId}/download`, {
      responseType: 'blob',
    });
  }

  // --- Auxiliares ---

  listFormasPagamento(): Observable<unknown> {
    return this.http.get(`${environment.baseUrl}/v1/financeiro/formas-pagamento`);
  }

  listCentrosCusto(): Observable<unknown> {
    return this.http.get(`${environment.baseUrl}/v1/financeiro/centros-custo`);
  }

  listPlanosConta(): Observable<unknown> {
    return this.http.get(`${environment.baseUrl}/v1/financeiro/plano-contas`);
  }

  listContasBancarias(): Observable<unknown> {
    return this.http.get(`${environment.baseUrl}/v1/financeiro/contas-bancarias`);
  }

  // --- Fluxo de Caixa ---

  listFluxoCaixa(query: {
    contaBancariaId?: number;
    centroCustoId?: number;
    dataInicio?: string;
    dataFim?: string;
    includeClosed?: boolean;
  } & Pageable): Observable<Page<unknown>> {
    let params = this.pageableParams(query);

    if (query.contaBancariaId != null) params = params.set('contaBancariaId', String(query.contaBancariaId));
    if (query.centroCustoId != null) params = params.set('centroCustoId', String(query.centroCustoId));
    if (query.dataInicio) params = params.set('dataInicio', query.dataInicio);
    if (query.dataFim) params = params.set('dataFim', query.dataFim);
    if (query.includeClosed != null) params = params.set('includeClosed', String(query.includeClosed));

    return this.http.get<Page<unknown>>(this.API_URL_FLUXO, { params });
  }

  createFluxoCaixa(request: {
    dataMovimento: string;
    descricao: string;
    tipoMovimentacao: 'ENTRADA' | 'SAIDA' | 'TRANSFERENCIA';
    valor: number;
    contaBancariaId?: number;
    centroCustoId?: number;
    observacoes?: string;
    recebimentoId?: number;
    pagamentoId?: number;
  }): Observable<unknown> {
    return this.http.post(this.API_URL_FLUXO, {
      dataMovimento: request.dataMovimento,
      descricao: request.descricao,
      tipoMovimentacao: request.tipoMovimentacao,
      valor: request.valor,
      saldoAcumulado: null,
      contaBancariaId: request.contaBancariaId,
      centroCustoId: request.centroCustoId,
      planoContasId: null,
      pagamentoId: request.pagamentoId,
      recebimentoId: request.recebimentoId,
      observacoes: request.observacoes,
    });
  }

  imprimirRelatorioFinanceiro(): Observable<Blob> {
    return this.http.get(`${environment.baseUrl}/v1/relatorios/financeiro`, { responseType: 'blob' });
  }

  imprimirContas(query: {
    dataInicio?: string;
    dataFim?: string;
    dataDe?: string;
    situacaoTipo?: string;
    departamento?: string;
    ordenarPor?: string;
  }): Observable<Blob> {
    let params = new HttpParams();
    if (query.dataInicio) params = params.set('dataInicio', query.dataInicio);
    if (query.dataFim) params = params.set('dataFim', query.dataFim);
    if (query.dataDe) params = params.set('dataDe', query.dataDe);
    if (query.situacaoTipo) params = params.set('situacaoTipo', query.situacaoTipo);
    if (query.departamento && query.departamento !== 'TODOS') {
      params = params.set('departamento', query.departamento);
    }
    if (query.ordenarPor) params = params.set('ordenarPor', query.ordenarPor);

    return this.http.get(`${environment.baseUrl}/v1/relatorios/financeiro`, {
      params,
      responseType: 'blob',
    });
  }

  imprimirFluxoCaixa(query: {
    dataInicio?: string;
    dataFim?: string;
    contaBancariaId?: number;
    centroCustoId?: number;
  }): Observable<Blob> {
    let params = new HttpParams();
    if (query.dataInicio) params = params.set('dataInicio', query.dataInicio);
    if (query.dataFim) params = params.set('dataFim', query.dataFim);
    if (query.contaBancariaId != null) params = params.set('contaBancariaId', String(query.contaBancariaId));
    if (query.centroCustoId != null) params = params.set('centroCustoId', String(query.centroCustoId));

    return this.http.get(`${environment.baseUrl}/v1/relatorios/caixa`, {
      params,
      responseType: 'blob',
    });
  }

  // --- Fechamento de Caixa ---

  listFechamentoCaixa(query: { dataInicio?: string; dataFim?: string } & Pageable): Observable<Page<unknown>> {
    let params = this.pageableParams(query);
    if (query.dataInicio) params = params.set('dataInicio', query.dataInicio);
    if (query.dataFim) params = params.set('dataFim', query.dataFim);

    return this.http.get<Page<unknown>>(this.API_URL_FECHAMENTO, { params });
  }

  getFechamentoCaixaById(id: number): Observable<unknown> {
    return this.http.get(`${this.API_URL_FECHAMENTO}/${id}`);
  }

  createFechamentoCaixa(request: unknown): Observable<unknown> {
    return this.http.post(this.API_URL_FECHAMENTO, request);
  }

  private pageableParams(query: Pageable): HttpParams {
    let params = new HttpParams()
      .set('page', String(query.page ?? 0))
      .set('size', String(query.size ?? 10));

    if (query.sort) {
      const sortValue = Array.isArray(query.sort) ? query.sort.join(',') : query.sort;
      params = params.set('sort', sortValue);
    }

    return params;
  }
}
