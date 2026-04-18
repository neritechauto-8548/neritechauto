import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export interface LocalizacaoEstoqueResponse {
  id: number;
  empresaId: number;
  codigo: string;
  nome: string;
  descricao?: string;
  tipoLocalizacao?: string;
  setor?: string;
  corredor?: string;
  prateleira?: string;
  posicao?: string;
}

@Injectable({ providedIn: 'root' })
export class LocalizacaoEstoqueService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/localizacoes-estoque';

  private getHeaders(): HttpHeaders {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    return new HttpHeaders({
      'X-Tenant-Id': String(tenantId),
      'Accept': 'application/json'
    });
  }

  listAtivasPorEmpresa(empresaId: number, params?: { page?: number; size?: number; sort?: string }): Observable<Page<LocalizacaoEstoqueResponse>> {
    let httpParams = new HttpParams();
    if (params) {
      Object.entries(params).forEach(([k, v]) => {
        if (v !== null && v !== undefined && `${v}` !== '') httpParams = httpParams.set(k, String(v));
      });
    }
    return this.http.get<Page<LocalizacaoEstoqueResponse>>(`${this.base}/empresa/${empresaId}/ativas`, { params: httpParams, headers: this.getHeaders() });
  }

  listPorEmpresa(empresaId: number, params?: { page?: number; size?: number; sort?: string }): Observable<Page<LocalizacaoEstoqueResponse>> {
    let httpParams = new HttpParams();
    if (params) {
      Object.entries(params).forEach(([k, v]) => {
        if (v !== null && v !== undefined && `${v}` !== '') httpParams = httpParams.set(k, String(v));
      });
    }
    return this.http.get<Page<LocalizacaoEstoqueResponse>>(`${this.base}/empresa/${empresaId}`, { params: httpParams, headers: this.getHeaders() });
  }
}
