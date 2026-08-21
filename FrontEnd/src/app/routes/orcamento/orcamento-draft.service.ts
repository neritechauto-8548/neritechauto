import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface OrcamentoDraftRequest {
  clienteId: number;
  veiculoId?: number;
  quilometragemEntrada?: number;
  responsavelId?: number;
  relatoCliente?: string;
  observacoesInternas?: string;
  observacoesCliente?: string;
}

export interface OrcamentoDraftResponse {
  id: number;
  numeroOrcamento: string;
  status: 'RASCUNHO' | string;
  clienteId: number;
  veiculoId?: number | null;
  criadoEm: string;
}

@Injectable({ providedIn: 'root' })
export class OrcamentoDraftService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/orcamentos`;

  create(request: OrcamentoDraftRequest): Observable<OrcamentoDraftResponse> {
    // Tenant e número comercial não fazem parte deste contrato por desenho.
    return this.http.post<OrcamentoDraftResponse>(this.base, request);
  }
}
