import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export interface DepartamentoRHResponse {
  id: number;
  empresaId: number;
  nome: string;
  descricao?: string;
  codigo?: string;
  ativo?: boolean;
}

@Injectable({ providedIn: 'root' })
export class RHDepartamentoService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/rh/departamentos';

  private get tenantId(): string {
    const v = this.storage.has('tenantId') ? (this.storage.get('tenantId') as any) : '1';
    return String(v && typeof v !== 'object' ? v : '1');
  }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'X-Tenant-Id': this.tenantId,
      'Accept': 'application/json'
    });
  }

  list(params?: any): Observable<Page<DepartamentoRHResponse>> {
    let httpParams = new HttpParams().set('empresaId', this.tenantId);
    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== undefined && params[key] !== null) {
          httpParams = httpParams.set(key, String(params[key]));
        }
      });
    }

    return this.http.get<Page<DepartamentoRHResponse>>(this.base, { params: httpParams, headers: this.getHeaders() });
  }
}
