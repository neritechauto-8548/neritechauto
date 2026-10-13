import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, defer, throwError } from 'rxjs';
import { empresaIdAtual } from '@shared/utils/empresa-context';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';

import { Page } from '../categoria/categoria-produto.service';

export interface CargoResponse {
  id: number;
  empresaId: number;
  nome: string;
  codigoCbo?: string;
  salarioBaseMinimo?: number;
  salarioBaseMaximo?: number;
  temComissao?: boolean;
}

@Injectable({ providedIn: 'root' })
export class CargoService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/rh/cargos';

  private get tenantId(): string {
    return String(empresaIdAtual(this.storage) ?? '');
  }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'X-Tenant-Id': this.tenantId,
      'Accept': 'application/json'
    });
  }

  list(params?: any): Observable<Page<CargoResponse>> {
    let httpParams = new HttpParams().set('empresaId', this.tenantId);
    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== undefined && params[key] !== null) {
          httpParams = httpParams.set(key, String(params[key]));
        }
      });
    }

    return defer(() => empresaIdAtual(this.storage)
      ? this.http.get<Page<CargoResponse>>(this.base, { params: httpParams, headers: this.getHeaders() })
      : throwError(() => new Error('Não foi possível identificar sua empresa. Entre novamente.')));
  }
}
