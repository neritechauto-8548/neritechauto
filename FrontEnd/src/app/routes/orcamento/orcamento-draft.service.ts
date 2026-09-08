import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, tap } from 'rxjs';

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

export interface OrcamentoVehicleSummary {
  id: number;
  marcaNome?: string | null;
  modeloNome?: string | null;
  anoFabricacao?: number | null;
  anoModelo?: number | null;
  maskedPlate: string;
  status?: string | null;
}

@Injectable({ providedIn: 'root' })
export class OrcamentoDraftService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/v1/orcamentos`;
  private readonly pendingCreationKeys = new Map<string, string>();

  create(request: OrcamentoDraftRequest): Observable<OrcamentoDraftResponse> {
    // Tenant e número comercial não fazem parte deste contrato por desenho.
    // A mesma carga mantém a chave após falha de rede para que um retry não
    // consuma outro número nem crie um segundo orçamento no servidor.
    const fingerprint = JSON.stringify(request);
    const idempotencyKey = this.pendingCreationKeys.get(fingerprint) ?? crypto.randomUUID();
    this.pendingCreationKeys.set(fingerprint, idempotencyKey);

    return this.http.post<OrcamentoDraftResponse>(this.base, request, {
      headers: { 'Idempotency-Key': idempotencyKey },
    }).pipe(
      tap({
        next: () => this.pendingCreationKeys.delete(fingerprint),
      }),
    );
  }

  listVehiclesForCustomer(clienteId: number): Observable<OrcamentoVehicleSummary[]> {
    const params = new HttpParams().set('clienteId', String(clienteId));
    return this.http.get<OrcamentoVehicleSummary[]>(`${environment.baseUrl}/v1/veiculos/resumo`, { params });
  }
}
