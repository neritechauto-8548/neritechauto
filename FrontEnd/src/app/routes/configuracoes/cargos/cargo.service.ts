import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';

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

  private getHeaders(): HttpHeaders {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '7';
    if (!tenantId || typeof tenantId === 'object') {
      tenantId = '7';
    }
    return new HttpHeaders({
      'X-Tenant-Id': String(tenantId),
      'Accept': 'application/json'
    });
  }

  list(): Observable<CargoResponse[]> {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '7';
    if (!tenantId || typeof tenantId === 'object') tenantId = '7';

    // Backend exige empresaId explicitamente
    const params = new HttpParams().set('empresaId', String(tenantId));
    // Force rebuild
    return this.http.get<CargoResponse[]>(this.base, { params, headers: this.getHeaders() });
  }
}
