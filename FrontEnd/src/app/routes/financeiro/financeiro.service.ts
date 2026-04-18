import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
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
  ContasReceberResponse
} from './models/financeiro.models';

@Injectable({
  providedIn: 'root',
})
export class FinanceiroService {
  private http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService as any);
  private readonly API_URL_PAGAR = `${environment.baseUrl}/v1/financeiro/contas-pagar`;
  private readonly API_URL_RECEBER = `${environment.baseUrl}/v1/financeiro/contas-receber`;
  private readonly API_URL_FLUXO = `${environment.baseUrl}/v1/financeiro/fluxo-caixa`;
  private readonly API_URL_FECHAMENTO = `${environment.baseUrl}/v1/financeiro/fechamento-caixa`;

  // --- Contas a Pagar ---

  private getTenantId(): number | null {
    try {
      const s: any = this.storage;
      let tenantId = s && s.has && s.has('tenantId') ? s.get('tenantId') : null;

      // Se for objeto, tenta extrair ID. Se for string que parece [object Object], limpa.
      if (tenantId && typeof tenantId === 'object') {
        tenantId = tenantId.id || tenantId.tenantId || tenantId.empresaId || null;
      }

      if (!tenantId || String(tenantId).includes('[object')) return 1; // Fallback para 1 se for inválido

      const num = Number(tenantId);
      return isNaN(num) ? 1 : num;
    } catch { return 1; }
  }

  listPagar(query: { empresaId?: number } & Pageable): Observable<Page<ContasPagarResponse>> {
    let params = new HttpParams()
      .set('page', String(query.page ?? 0))
      .set('size', String(query.size ?? 10));
    const empresaId = query.empresaId ?? this.getTenantId();
    if (empresaId != null) params = params.set('empresaId', String(empresaId));

    if (query.sort) {
      const sortValue = Array.isArray(query.sort) ? query.sort.join(',') : query.sort;
      params = params.set('sort', sortValue);
    }

    return this.http.get<Page<ContasPagarResponse>>(this.API_URL_PAGAR, { params });
  }

  getPagarById(id: number, empresaId = 1): Observable<ContasPagarResponse> {
      return this.http.get<ContasPagarResponse>(`${this.API_URL_PAGAR}/${id}`, { params: { empresaId } });
  }

  createPagar(request: ContasPagarRequest, empresaId = 1): Observable<ContasPagarResponse> {
      return this.http.post<ContasPagarResponse>(this.API_URL_PAGAR, request, { params: { empresaId } });
  }

  updatePagar(id: number, request: ContasPagarRequest, empresaId = 1): Observable<ContasPagarResponse> {
      return this.http.put<ContasPagarResponse>(`${this.API_URL_PAGAR}/${id}`, request, { params: { empresaId } });
  }

  deletePagar(id: number, empresaId = 1): Observable<void> {
      return this.http.delete<void>(`${this.API_URL_PAGAR}/${id}`, { params: { empresaId } });
  }


  // --- Contas a Receber ---

  listReceber(query: { empresaId?: number } & Pageable): Observable<Page<ContasReceberResponse>> {
    let params = new HttpParams()
      .set('page', String(query.page ?? 0))
      .set('size', String(query.size ?? 10));
    const empresaId = query.empresaId ?? this.getTenantId();
    if (empresaId != null) params = params.set('empresaId', String(empresaId));

    if (query.sort) {
      const sortValue = Array.isArray(query.sort) ? query.sort.join(',') : query.sort;
      params = params.set('sort', sortValue);
    }

    return this.http.get<Page<ContasReceberResponse>>(this.API_URL_RECEBER, { params });
  }

  getReceberById(id: number, empresaId = 1): Observable<ContasReceberResponse> {
    const params: any = {};
    const empresa = this.getTenantId();
    if (empresa != null) params.empresaId = String(empresa);
    return this.http.get<ContasReceberResponse>(`${this.API_URL_RECEBER}/${id}`, { params });
  }

  createReceber(request: ContasReceberRequest, empresaId = 1): Observable<ContasReceberResponse> {
    return this.http.post<ContasReceberResponse>(this.API_URL_RECEBER, request, { params: { empresaId } });
  }

  updateReceber(id: number, request: ContasReceberRequest, empresaId = 1): Observable<ContasReceberResponse> {
    return this.http.put<ContasReceberResponse>(`${this.API_URL_RECEBER}/${id}`, request, { params: { empresaId } });
  }

  deleteReceber(id: number, empresaId = 1): Observable<void> {
    return this.http.delete<void>(`${this.API_URL_RECEBER}/${id}`, { params: { empresaId } });
  }

  // --- Auxiliares ---

  listFormasPagamento(): Observable<any> {
    const empresa = this.getTenantId();
    const params = empresa != null ? new HttpParams().set('empresaId', String(empresa)) : new HttpParams().set('empresaId', '1');
    return this.http.get<any>(`${environment.baseUrl}/v1/financeiro/formas-pagamento`, { params });
  }

  listCentrosCusto(): Observable<any> {
    const empresa = this.getTenantId();
    const params = new HttpParams().set('empresaId', String(empresa ?? 1));
    return this.http.get<any>(`${environment.baseUrl}/v1/financeiro/centros-custo`, { params });
  }

  listPlanosConta(): Observable<any> {
    return this.http.get<any>(`${environment.baseUrl}/v1/financeiro/planos-conta`);
  }

  listContasBancarias(): Observable<any> {
    const empresa = this.getTenantId();
    const params = empresa != null ? new HttpParams().set('empresaId', String(empresa)) : undefined;
    return this.http.get<any>(`${environment.baseUrl}/v1/financeiro/contas-bancarias`, { params });
  }

  // --- Fluxo de Caixa ---
  listFluxoCaixa(query: { empresaId?: number; contaBancariaId?: number; centroCustoId?: number; dataInicio?: string; dataFim?: string } & Pageable): Observable<Page<any>> {
    let params = new HttpParams()
      .set('page', String(query.page ?? 0))
      .set('size', String(query.size ?? 10));
    const empresa = query.empresaId ?? this.getTenantId();
    if (empresa != null) params = params.set('empresaId', String(empresa));
    if (query.contaBancariaId != null) params = params.set('contaBancariaId', String(query.contaBancariaId));
    if (query.centroCustoId != null) params = params.set('centroCustoId', String(query.centroCustoId));
    if (query.dataInicio) params = params.set('dataInicio', query.dataInicio);
    if (query.dataFim) params = params.set('dataFim', query.dataFim);

    if (query.sort) {
      const sortValue = Array.isArray(query.sort) ? query.sort.join(',') : query.sort;
      params = params.set('sort', sortValue);
    }

    return this.http.get<Page<any>>(this.API_URL_FLUXO, { params });
  }

  createFluxoCaixa(request: { dataMovimento: string; descricao: string; tipoMovimentacao: 'ENTRADA' | 'SAIDA' | 'TRANSFERENCIA'; valor: number; contaBancariaId?: number; centroCustoId?: number; observacoes?: string; recebimentoId?: number; pagamentoId?: number }, empresaId?: number): Observable<any> {
    const params = empresaId != null ? new HttpParams().set('empresaId', String(empresaId)) : undefined;
    return this.http.post<any>(this.API_URL_FLUXO, {
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
      observacoes: request.observacoes
    }, { params });
  }

  imprimirRelatorioFinanceiro(): Observable<Blob> {
    const url = `${environment.baseUrl}/v1/relatorios/financeiro`;
    return this.http.get(url, { responseType: 'blob' });
  }

  // --- Fechamento de Caixa ---
  listFechamentoCaixa(query: { empresaId?: number; dataInicio?: string; dataFim?: string } & Pageable): Observable<Page<any>> {
    let params = new HttpParams()
      .set('page', String(query.page ?? 0))
      .set('size', String(query.size ?? 10));
    const empresa = query.empresaId ?? this.getTenantId();
    if (empresa != null) params = params.set('empresaId', String(empresa));
    if (query.dataInicio) params = params.set('dataInicio', query.dataInicio);
    if (query.dataFim) params = params.set('dataFim', query.dataFim);

    if (query.sort) {
      const sortValue = Array.isArray(query.sort) ? query.sort.join(',') : query.sort;
      params = params.set('sort', sortValue);
    }

    return this.http.get<Page<any>>(this.API_URL_FECHAMENTO, { params });
  }

  getFechamentoCaixaById(id: number, empresaId?: number): Observable<any> {
      const params = empresaId != null ? new HttpParams().set('empresaId', String(empresaId)) : undefined;
      return this.http.get<any>(`${this.API_URL_FECHAMENTO}/${id}`, { params });
  }

  createFechamentoCaixa(request: any, empresaId?: number): Observable<any> {
      const params = empresaId != null ? new HttpParams().set('empresaId', String(empresaId)) : undefined;
      return this.http.post<any>(this.API_URL_FECHAMENTO, request, { params });
  }
}
