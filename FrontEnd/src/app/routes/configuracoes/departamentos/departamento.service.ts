import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';
import { empresaIdAtual } from '@shared/utils/empresa-context';

export interface DepartamentoContabioRequest {
  empresaId: number;
  descricao: string;
  codigo: string;
  detalhes?: string;
  tipoOperacao?: string;
  supervisorId?: number;
  ativo?: boolean;
  ordem?: number;
  tempoPadraoMinutos?: number;
  versao?: number;
}

export interface DepartamentoContabioResponse {
  id: number;
  empresaId: number;
  empresaNome?: string;
  descricao: string;
  codigo: string;
  detalhes?: string;
  tipoOperacao?: string;
  supervisorId?: number;
  ativo: boolean;
  ordem: number;
  tempoPadraoMinutos?: number;
  numeroFuncionarios: number;
  numeroCargos: number;
  numeroServicos: number;
  versao: number;
  dataCadastro?: string;
  dataAtualizacao?: string;
}

@Injectable({ providedIn: 'root' })
export class DepartamentoService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/departamentos-contabio-empresa';

  private getTenantId(): string {
    return String(empresaIdAtual(this.storage) ?? '');
  }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'X-Tenant-Id': this.getTenantId(),
      'Accept': 'application/json'
    });
  }

  list(params?: { page?: number; size?: number; sort?: string; q?: string; ativo?: boolean; tipo?: string }): Observable<Page<DepartamentoContabioResponse>> {
    let httpParams = new HttpParams();
    httpParams = httpParams.set('empresaId', this.getTenantId());
    if (params) {
      Object.keys(params).forEach(key => {
        const val = (params as any)[key];
        if (val !== null && val !== undefined) {
          httpParams = httpParams.set(key, String(val));
        }
      });
    }
    return this.http.get<Page<DepartamentoContabioResponse>>(this.base, { params: httpParams, headers: this.getHeaders() });
  }

  listPorEmpresa(empresaId: number): Observable<DepartamentoContabioResponse[]> {
    return this.http.get<DepartamentoContabioResponse[]>(`${this.base}/empresa/${empresaId}`, { headers: this.getHeaders() });
  }

  getById(id: number): Observable<DepartamentoContabioResponse> {
    return this.http.get<DepartamentoContabioResponse>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  create(dto: DepartamentoContabioRequest): Observable<DepartamentoContabioResponse> {
    return this.http.post<DepartamentoContabioResponse>(this.base, dto, { headers: this.getHeaders() });
  }

  update(id: number, dto: DepartamentoContabioRequest): Observable<DepartamentoContabioResponse> {
    return this.http.put<DepartamentoContabioResponse>(`${this.base}/${id}`, dto, { headers: this.getHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  historico(id: number): Observable<Page<any>> {
    return this.http.get<Page<any>>(`${this.base}/${id}/historico`, { params: { page: 0, size: 20 }, headers: this.getHeaders() });
  }
}
