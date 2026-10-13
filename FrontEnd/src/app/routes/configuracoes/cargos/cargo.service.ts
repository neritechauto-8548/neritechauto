import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, defer, throwError } from 'rxjs';
import { empresaIdAtual } from '@shared/utils/empresa-context';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';

import { Page } from '../categoria/categoria-produto.service';

export interface CargoRequest {
  nome: string;
  codigoCbo?: string;
  salarioBaseMinimo?: number;
  salarioBaseMaximo?: number;
  temComissao: boolean;
}

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


  private executar<T>(request: (empresaId: number, headers: HttpHeaders) => Observable<T>): Observable<T> {
    return defer(() => {
      const empresaId = empresaIdAtual(this.storage);
      if (!empresaId) return throwError(() => new Error('Não foi possível identificar sua empresa. Entre novamente.'));
      const headers = new HttpHeaders({ 'X-Tenant-Id': String(empresaId), 'Accept': 'application/json' });
      return request(empresaId, headers);
    });
  }

  criar(data: CargoRequest): Observable<CargoResponse> {
    return this.executar((empresaId, headers) =>
      this.http.post<CargoResponse>(this.base, { ...data, empresaId }, { headers }));
  }

  atualizar(id: number, data: CargoRequest): Observable<CargoResponse> {
    return this.executar((empresaId, headers) =>
      this.http.put<CargoResponse>(`${this.base}/${id}`, { ...data, empresaId }, { headers }));
  }

  excluir(id: number): Observable<void> {
    return this.executar((_empresaId, headers) =>
      this.http.delete<void>(`${this.base}/${id}`, { headers }));
  }

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
