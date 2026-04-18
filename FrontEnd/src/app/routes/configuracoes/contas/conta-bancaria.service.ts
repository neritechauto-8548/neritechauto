import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export interface ContaBancariaRequest {
  bancoCodigo: string;
  bancoNome: string;
  agencia: string;
  conta: string;
  digitoConta?: string;
  tipoConta: string;
  titularConta: string;
  cpfCnpjTitular: string;
  saldoAtual?: number;
  limiteChequeEspecial?: number;
  dataUltimoSaldo?: string;
  ativo?: boolean;
}

export interface ContaBancariaResponse extends ContaBancariaRequest {
  id: number;
  empresaId: number;
  dataCadastro?: string;
  dataAtualizacao?: string;
}

@Injectable({ providedIn: 'root' })
export class ContaBancariaService {
  private readonly http = inject(HttpClient);
  private readonly base = environment.baseUrl + '/v1/financeiro/contas-bancarias';

  private getEmpresaIdParam(params?: Record<string, any>): HttpParams {
    let httpParams = new HttpParams();
    Object.entries(params || {}).forEach(([k, v]) => {
      if (v !== null && v !== undefined && `${v}` !== '') {
        httpParams = httpParams.set(k, String(v));
      }
    });
    return httpParams;
  }

  list(params?: { page?: number; size?: number; sort?: string; empresaId?: number }): Observable<Page<ContaBancariaResponse>> {
    const httpParams = this.getEmpresaIdParam(params);
    return this.http.get<Page<ContaBancariaResponse>>(this.base, { params: httpParams });
  }

  get(id: number, empresaId?: number): Observable<ContaBancariaResponse> {
    let httpParams = new HttpParams();
    if (empresaId) {
      httpParams = httpParams.set('empresaId', String(empresaId));
    }
    return this.http.get<ContaBancariaResponse>(`${this.base}/${id}`, { params: httpParams });
  }

  create(dto: ContaBancariaRequest, empresaId?: number): Observable<ContaBancariaResponse> {
    let httpParams = new HttpParams();
    if (empresaId) {
      httpParams = httpParams.set('empresaId', String(empresaId));
    }
    return this.http.post<ContaBancariaResponse>(this.base, dto, { params: httpParams });
  }

  update(id: number, dto: ContaBancariaRequest, empresaId?: number): Observable<ContaBancariaResponse> {
    let httpParams = new HttpParams();
    if (empresaId) {
      httpParams = httpParams.set('empresaId', String(empresaId));
    }
    return this.http.put<ContaBancariaResponse>(`${this.base}/${id}`, dto, { params: httpParams });
  }

  delete(id: number, empresaId?: number): Observable<void> {
    let httpParams = new HttpParams();
    if (empresaId) {
      httpParams = httpParams.set('empresaId', String(empresaId));
    }
    return this.http.delete<void>(`${this.base}/${id}`, { params: httpParams });
  }
}
