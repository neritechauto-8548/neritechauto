import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export type TipoFormaPagamento =
  | 'PIX'
  | 'CARTAO_CREDITO'
  | 'CARTAO_DEBITO'
  | 'BOLETO'
  | 'CHEQUE'
  | 'DINHEIRO'
  | 'OUTROS';

export interface FormaPagamentoRequest {
  nome: string;
  tipo: TipoFormaPagamento;
  aceitaParcelamento?: boolean;
  parcelasMaximas?: number;
  taxaAdministracao?: number;
  prazoRecebimentoDias?: number;
  contaBancariaId?: number;
  ativo?: boolean;
  padrao?: boolean;
  exigeAutorizacao?: boolean;
  limiteDiario?: number;
  observacoes?: string;
}

export interface FormaPagamentoResponse extends FormaPagamentoRequest {
  id: number;
  empresaId: number;
  contaBancariaNome?: string;
  createdAt?: string;
  updatedAt?: string;
}

@Injectable({ providedIn: 'root' })
export class FormaPagamentoService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/financeiro/formas-pagamento';

  private getHeaders(): HttpHeaders {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    return new HttpHeaders({
      'X-Tenant-Id': String(tenantId),
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    });
  }

  private getEmpresaIdParam(params?: Record<string, any>): HttpParams {
    let httpParams = new HttpParams();
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    httpParams = httpParams.set('empresaId', String(tenantId));
    Object.entries(params || {}).forEach(([k, v]) => {
      if (v !== null && v !== undefined && `${v}` !== '') {
        httpParams = httpParams.set(k, String(v));
      }
    });
    return httpParams;
  }

  list(params?: { page?: number; size?: number; sort?: string }): Observable<Page<FormaPagamentoResponse>> {
    return this.http.get<Page<FormaPagamentoResponse>>(this.base, {
      params: this.getEmpresaIdParam(params),
      headers: this.getHeaders(),
    });
  }

  get(id: number): Observable<FormaPagamentoResponse> {
    return this.http.get<FormaPagamentoResponse>(`${this.base}/${id}`, {
      params: this.getEmpresaIdParam(),
      headers: this.getHeaders(),
    });
  }

  create(dto: FormaPagamentoRequest): Observable<FormaPagamentoResponse> {
    return this.http.post<FormaPagamentoResponse>(this.base, dto, {
      params: this.getEmpresaIdParam(),
      headers: this.getHeaders(),
    });
  }

  update(id: number, dto: FormaPagamentoRequest): Observable<FormaPagamentoResponse> {
    return this.http.put<FormaPagamentoResponse>(`${this.base}/${id}`, dto, {
      params: this.getEmpresaIdParam(),
      headers: this.getHeaders(),
    });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, {
      params: this.getEmpresaIdParam(),
      headers: this.getHeaders(),
    });
  }
}
