import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../environments/environment';
import { Page, OrdemServicoRequest, OrdemServicoResponse, ItemOSProdutoRequest, ItemOSProdutoResponse, ItemOSServicoRequest, ItemOSServicoResponse, DiagnosticoRequest, DiagnosticoResponse } from './models/os.models';

@Injectable({ providedIn: 'root' })
export class OrdemServicoService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = `${environment.baseUrl}/v1/ordens-servico`;
  private readonly prodUrl = `${environment.baseUrl}/v1/itens-os-produtos`;
  private readonly servUrl = `${environment.baseUrl}/v1/itens-os-servicos`;
  private readonly diagUrl = `${environment.baseUrl}/v1/diagnosticos`;
  private readonly checklistUrl = `${environment.baseUrl}/v1/ordens-servico/checklists`;
  private readonly osChecklistUrl = `${environment.baseUrl}/v1/ordens-servico/os-checklist`;
  private readonly itChecklistUrl = `${environment.baseUrl}/v1/ordens-servico/it-checklist`;
  private readonly pagamentosUrl = `${environment.baseUrl}/v1/financeiro/pagamentos`;
  private readonly formasPagamentoUrl = `${environment.baseUrl}/v1/financeiro/formas-pagamento`;
  private readonly contasBancariasUrl = `${environment.baseUrl}/v1/financeiro/contas-bancarias`;
  private readonly faturasUrl = `${environment.baseUrl}/v1/financeiro/faturas`;
  private readonly osFotosUrl = `${environment.baseUrl}/v1/ordens-servico`;
  private readonly catalogProdUrl = `${environment.baseUrl}/v1/produtos`;
  private readonly catalogServUrl = `${environment.baseUrl}/v1/servicos`;

  private getTenantId(): number {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    return Number(tenantId);
  }

  // ========== ORDEM DE SERVIÇO ==========
  list(params?: { page?: number; size?: number; sort?: string; search?: string; dataTipo?: string; de?: string; ate?: string; status?: string }): Observable<Page<OrdemServicoResponse>> {
    const empresaId = this.getTenantId();
    let httpParams = new HttpParams();
    Object.entries(params || {}).forEach(([k, v]) => {
      if (v !== undefined && v !== null && `${v}` !== '') {
        httpParams = httpParams.set(k, String(v));
      }
    });
    return this.http.get<Page<OrdemServicoResponse>>(`${this.base}/empresa/${empresaId}`, {
      params: httpParams,
    });
  }

  getById(id: number | string): Observable<OrdemServicoResponse> {
    return this.http.get<any>(`${this.base}/${id}`).pipe(map((resp: any) => resp?.data ?? resp));
  }

  create(dto: Omit<OrdemServicoRequest, 'empresaId'>): Observable<OrdemServicoResponse> {
    const tenantId = this.getTenantId();
    const body: OrdemServicoRequest = { ...dto, empresaId: tenantId };
    return this.http.post<OrdemServicoResponse>(this.base, body);
  }

  update(id: number | string, dto: Omit<OrdemServicoRequest, 'empresaId'>): Observable<OrdemServicoResponse> {
    const tenantId = this.getTenantId();
    const body: OrdemServicoRequest = { ...dto, empresaId: tenantId };
    return this.http.put<OrdemServicoResponse>(`${this.base}/${id}`, body);
  }

  delete(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  imprimir(id: number): Observable<Blob> {
    return this.http.get(`${environment.baseUrl}/v1/relatorios/os/${id}`, {
      responseType: 'blob'
    });
  }

  // ========== CATALOGO ==========
  searchProdutos(query: string): Observable<Page<any>> {
    const empresaId = this.getTenantId();
    let params = new HttpParams().set('empresaId', String(empresaId));
    if (query) params = params.set('search', query);
    return this.http.get<Page<any>>(this.catalogProdUrl, { params });
  }

  searchServicos(query: string): Observable<Page<any>> {
    const empresaId = this.getTenantId();
    let params = new HttpParams().set('empresaId', String(empresaId));
    if (query) params = params.set('search', query);
    return this.http.get<Page<any>>(this.catalogServUrl, { params });
  }

  // ========== ITENS PRODUTOS ==========
  getProdutos(osId: number): Observable<ItemOSProdutoResponse[]> {
    return this.http.get<ItemOSProdutoResponse[]>(`${this.prodUrl}/ordem-servico/${osId}`);
  }

  addProduto(item: ItemOSProdutoRequest): Observable<ItemOSProdutoResponse> {
    return this.http.post<ItemOSProdutoResponse>(this.prodUrl, item);
  }

  updateProduto(id: number, item: ItemOSProdutoRequest): Observable<ItemOSProdutoResponse> {
    return this.http.put<ItemOSProdutoResponse>(`${this.prodUrl}/${id}`, item);
  }

  deleteProduto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.prodUrl}/${id}`);
  }

  // ========== ITENS SERVIÇOS ==========
  getServicos(osId: number): Observable<ItemOSServicoResponse[]> {
    return this.http.get<ItemOSServicoResponse[]>(`${this.servUrl}/ordem-servico/${osId}`);
  }

  addServico(item: ItemOSServicoRequest): Observable<ItemOSServicoResponse> {
    return this.http.post<ItemOSServicoResponse>(this.servUrl, item);
  }

  updateServico(id: number, item: ItemOSServicoRequest): Observable<ItemOSServicoResponse> {
    return this.http.put<ItemOSServicoResponse>(`${this.servUrl}/${id}`, item);
  }

  deleteServico(id: number): Observable<void> {
    return this.http.delete<void>(`${this.servUrl}/${id}`);
  }

  // ========== DIAGNÓSTICOS (SOLICITAÇÕES) ==========
  getDiagnosticos(osId: number): Observable<DiagnosticoResponse[]> {
    return this.http.get<DiagnosticoResponse[]>(`${this.diagUrl}/ordem-servico/${osId}`);
  }

  addDiagnostico(item: DiagnosticoRequest): Observable<DiagnosticoResponse> {
    return this.http.post<DiagnosticoResponse>(this.diagUrl, item);
  }

  updateDiagnostico(id: number, item: DiagnosticoRequest): Observable<DiagnosticoResponse> {
    return this.http.put<DiagnosticoResponse>(`${this.diagUrl}/${id}`, item);
  }

  deleteDiagnostico(id: number): Observable<void> {
    return this.http.delete<void>(`${this.diagUrl}/${id}`);
  }

  // ========== CHECKLISTS ==========
  listChecklists(query?: string): Observable<Page<any>> {
    const empresaId = this.getTenantId();
    let params = new HttpParams().set('empresaId', String(empresaId));
    if (query) params = params.set('search', query);
    return this.http.get<Page<any>>(this.checklistUrl, { params });
  }

  getOSChecklists(osId: number): Observable<any[]> {
    const empresaId = this.getTenantId();
    const params = new HttpParams().set('empresaId', String(empresaId));
    return this.http.get<any[]>(`${this.osChecklistUrl}/ordem-servico/${osId}`, { params });
  }

  addOSChecklist(ordemServicoId: number, checklistId: number): Observable<any> {
    const body = { ordemServicoId, checklistId };
    return this.http.post<any>(this.osChecklistUrl, body);
  }

  updateOSChecklistItem(id: number, data: { descricao?: string; feito?: boolean; ordem?: number }): Observable<any> {
    return this.http.put<any>(`${this.osChecklistUrl}/${id}`, data);
  }

  getChecklistModeloItens(checklistId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.itChecklistUrl}/checklist/${checklistId}`);
  }

  listFormasPagamento(): Observable<Page<any>> {
    const empresaId = this.getTenantId();
    const params = new HttpParams().set('empresaId', String(empresaId));
    return this.http.get<Page<any>>(this.formasPagamentoUrl, { params });
  }

  listContasBancarias(): Observable<Page<any>> {
    const empresaId = this.getTenantId();
    const params = new HttpParams().set('empresaId', String(empresaId));
    return this.http.get<Page<any>>(this.contasBancariasUrl, { params });
  }

  createPagamento(body: any): Observable<any> {
    const empresaId = this.getTenantId();
    const params = new HttpParams().set('empresaId', String(empresaId));
    return this.http.post<any>(this.pagamentosUrl, body, { params });
  }

  getFaturaPorOS(osId: number): Observable<any> {
    const empresaId = this.getTenantId();
    const params = new HttpParams().set('empresaId', String(empresaId));
    return this.http.get<any>(`${this.faturasUrl}/ordem-servico/${osId}`, { params })
      .pipe(
        map((resp: any) => resp?.data ?? resp),
        catchError(() => of(null))
      );
  }

  listPagamentosPorFatura(faturaId: number): Observable<Page<any>> {
    const empresaId = this.getTenantId();
    const params = new HttpParams().set('empresaId', String(empresaId));
    return this.http.get<Page<any>>(`${this.pagamentosUrl}/fatura/${faturaId}`, { params });
  }

  listOsFotos(osId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.osFotosUrl}/${osId}/fotos`);
  }

  uploadOsFoto(osId: number, file: File, descricao?: string): Observable<any> {
    const form = new FormData();
    form.append('file', file);
    if (descricao) form.append('descricao', descricao);
    return this.http.post<any>(`${this.osFotosUrl}/${osId}/fotos`, form);
  }

  deleteOsFoto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.osFotosUrl}/fotos/${id}`);
  }

  emitirNFePdf(osId: number): Observable<Blob> {
    const headers = new HttpHeaders({ Accept: 'application/pdf' });
    return this.http.get(`${this.osFotosUrl}/${osId}/nfe/pdf`, { responseType: 'blob', headers });
  }
}
