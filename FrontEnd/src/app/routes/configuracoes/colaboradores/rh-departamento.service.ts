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

  list(params?: any): Observable<Page<any>> {
    let p = new HttpParams().set('empresaId', this.tenantId);
    if (params) {
      Object.entries(params).forEach(([k, v]) => {
        if (v != null) p = p.set(k, String(v));
      });
    }
    return this.http.get<Page<any>>(this.base, { params: p, headers: this.getHeaders() });
  }
}
