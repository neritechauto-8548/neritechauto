import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../environments/environment';
import { Page, FornecedorRequest, FornecedorResponse, HistoricoFornecedor } from './models/fornecedor.models';

export interface CanalFornecedor { id?: number; tipo: 'EMAIL' | 'CELULAR' | 'TELEFONE'; valor: string; whatsapp: boolean; }
export interface ContatoFornecedor {
  id?: number; nome: string; sexo: string; dataNascimento?: string | null;
  contatoPrincipal: boolean; estrangeiro?: boolean; meios: CanalFornecedor[];
}
export interface DevolucaoFornecedor {
  id: number; empresa: string; compra: number; descricao: string; chaveNfe: string | null; quantidade: number; valor: number;
}
export interface CreditoFornecedor {
  id: number; empresa: string; codigoCompra: number | null; numeroDocumento: string | null;
  lancamentoReferencia: string | null; conta: string | null; valor: number;
}
export interface LinhaImportacaoFornecedor {
  tipoPessoa: 'FISICA' | 'JURIDICA'; cpf: string; cnpj: string; razaoSocial: string;
  nomeFantasia: string; email: string; classificacao: string;
}


@Injectable({ providedIn: 'root' })
export class FornecedorService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = `${environment.baseUrl}/v1/fornecedores`;

  private getTenantId(): number | null {
    let raw: any = this.storage.has('tenantId') ? this.storage.get('tenantId') : null;
    if (raw && typeof raw === 'object') raw = raw.id ?? raw.tenantId ?? raw.empresaId;
    const id = Number(raw);
    return raw != null && raw !== '' && Number.isSafeInteger(id) && id > 0 ? id : null;
  }

  private tenant(): number {
    const id = this.getTenantId();
    if (!id) throw new Error('Sessão sem empresa autenticada');
    return id;
  }

  private headers(id: number): HttpHeaders {
    return new HttpHeaders({ 'X-Tenant-Id': String(id), Accept: 'application/json' });
  }

  private params(empresaId: number, values?: Record<string, any>): HttpParams {
    let params = new HttpParams().set('empresaId', empresaId);
    for (const [key, value] of Object.entries(values || {})) {
      if (key !== 'empresaId' && value !== undefined && value !== null && String(value) !== '') {
        params = params.set(key, String(value));
      }
    }
    return params;
  }

  private safe<T>(call: (id: number) => Observable<T>): Observable<T> {
    try { return call(this.tenant()); }
    catch { return throwError(() => new Error('Empresa autenticada não identificada. Entre novamente.')); }
  }

  list(params?: Record<string, any>): Observable<Page<FornecedorResponse>> {
    return this.safe(id => this.http.get<Page<FornecedorResponse>>(this.base, {
      params: this.params(id, params), headers: this.headers(id)
    }));
  }

  get(id: number | string): Observable<FornecedorResponse> {
    return this.safe(tenant => this.http.get<FornecedorResponse>(`${this.base}/${id}`, {
      headers: this.headers(tenant)
    }));
  }

  create(dto: Omit<FornecedorRequest, 'empresaId'>): Observable<FornecedorResponse> {
    return this.safe(id => this.http.post<FornecedorResponse>(this.base,
      { ...dto, empresaId: id }, { headers: this.headers(id) }));
  }

  update(id: number | string, dto: Omit<FornecedorRequest, 'empresaId'>): Observable<FornecedorResponse> {
    return this.safe(tenant => this.http.put<FornecedorResponse>(`${this.base}/${id}`,
      { ...dto, empresaId: tenant }, {
        headers: dto.versao != null
          ? this.headers(tenant).set('If-Match', String(dto.versao)) : this.headers(tenant)
      }));
  }

  historico(id: number | string, page = 0, size = 20): Observable<Page<HistoricoFornecedor>> {
    return this.safe(tenant => this.http.get<Page<HistoricoFornecedor>>(`${this.base}/${id}/historico`, {
      headers: this.headers(tenant), params: { page, size }
    }));
  }


  private referencia = `${environment.baseUrl}/v1/fornecedores/referencia`;

  planosContas(): Observable<Array<{ id: number; codigo: string; nome: string }>> {
    return this.safe(t => this.http.get<Array<{ id: number; codigo: string; nome: string }>>(
      `${this.referencia}/plano-contas`, { headers: this.headers(t) }));
  }
  contatos(fornecedorId: number): Observable<ContatoFornecedor[]> {
    return this.safe(t => this.http.get<ContatoFornecedor[]>(`${this.referencia}/${fornecedorId}/contatos`,
      { headers: this.headers(t) }));
  }
  criarContato(fornecedorId: number, dto: ContatoFornecedor): Observable<{ id: number }> {
    return this.safe(t => this.http.post<{ id: number }>(`${this.referencia}/${fornecedorId}/contatos`,
      dto, { headers: this.headers(t) }));
  }
  editarContato(fornecedorId: number, contatoId: number, dto: ContatoFornecedor): Observable<{ id: number }> {
    return this.safe(t => this.http.put<{ id: number }>(
      `${this.referencia}/${fornecedorId}/contatos/${contatoId}`, dto, { headers: this.headers(t) }));
  }
  excluirContato(fornecedorId: number, contatoId: number): Observable<void> {
    return this.safe(t => this.http.delete<void>(
      `${this.referencia}/${fornecedorId}/contatos/${contatoId}`, { headers: this.headers(t) }));
  }
  devolucoes(fornecedorId: number, busca = '', ordem = 'desc'): Observable<DevolucaoFornecedor[]> {
    return this.safe(t => this.http.get<DevolucaoFornecedor[]>(
      `${this.referencia}/${fornecedorId}/devolucoes`,
      { headers: this.headers(t), params: { busca, ordem } }));
  }
  creditos(fornecedorId: number, busca = '', ordem = 'desc'): Observable<CreditoFornecedor[]> {
    return this.safe(t => this.http.get<CreditoFornecedor[]>(
      `${this.referencia}/${fornecedorId}/creditos`,
      { headers: this.headers(t), params: { busca, ordem } }));
  }
  importar(linhas: LinhaImportacaoFornecedor[]): Observable<{ importados: number }> {
    return this.safe(t => this.http.post<{ importados: number }>(
      `${this.referencia}/importar`, linhas, { headers: this.headers(t) }));
  }

  /** Rota legada DELETE inativa sem remover pedidos, cotações e documentos. */
  delete(id: number | string): Observable<void> {
    return this.safe(tenant => this.http.delete<void>(`${this.base}/${id}`, {
      headers: this.headers(tenant)
    }));
  }
}
