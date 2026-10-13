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

  list(params?: Record<string, string | number | boolean | undefined>): Observable<Page<CargoResponse>> {
    return defer(() => {
      const empresaId = empresaIdAtual(this.storage);
      if (!empresaId) {
        return throwError(() => new Error('Não foi possível identificar sua empresa. Entre novamente.'));
      }
      let httpParams = new HttpParams().set('empresaId', String(empresaId));
      if (params) {
        Object.entries(params).forEach(([key, value]) => {
          if (key !== 'empresaId' && value !== undefined && value !== null) {
            httpParams = httpParams.set(key, String(value));
          }
        });
      }
      return this.http.get<Page<CargoResponse>>(this.base, {
        params: httpParams,
        headers: new HttpHeaders({ 'X-Tenant-Id': String(empresaId), 'Accept': 'application/json' })
      });
    });
  }
}
