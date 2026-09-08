import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { OrigemCliente, Page, StatusCliente, TipoCliente, TipoContato } from '../models/cliente.models';
import { StatusVeiculo } from '../../veiculo/models/veiculo.models';

export interface CustomerDetailSummary {
  id: number;
  displayName: string;
  type: TipoCliente;
  status: StatusCliente;
  maskedTaxId?: string | null;
  maskedEmail?: string | null;
  origin?: OrigemCliente | null;
  hasRelationshipNotes: boolean;
}

export interface CustomerContactSummary {
  id: number;
  tipoContato: TipoContato;
  maskedValue: string;
  principal: boolean;
}

export interface CustomerAddressSummary {
  id: number;
  locationSummary: string;
  maskedPostalCode: string;
  country: string;
}

export interface CustomerVehicleSummary {
  id: number;
  marcaNome?: string | null;
  modeloNome?: string | null;
  anoFabricacao?: number | null;
  anoModelo?: number | null;
  maskedPlate: string;
  status?: StatusVeiculo | null;
}

@Injectable({ providedIn: 'root' })
export class CustomerDetailReadService {
  private readonly http = inject(HttpClient);
  private readonly base = environment.baseUrl;

  getCustomer(id: number | string): Observable<CustomerDetailSummary> {
    return this.http.get<CustomerDetailSummary>(`${this.base}/v1/clientes/${id}/resumo`);
  }

  getContacts(id: number | string): Observable<Page<CustomerContactSummary>> {
    return this.http.get<Page<CustomerContactSummary>>(`${this.base}/v1/clientes/${id}/contatos/resumo`);
  }

  getAddresses(id: number | string): Observable<Page<CustomerAddressSummary>> {
    return this.http.get<Page<CustomerAddressSummary>>(`${this.base}/v1/clientes/${id}/enderecos/resumo`);
  }

  getVehicles(id: number | string): Observable<CustomerVehicleSummary[]> {
    const params = new HttpParams().set('clienteId', String(id));
    return this.http.get<CustomerVehicleSummary[]>(`${this.base}/v1/veiculos/resumo`, { params });
  }
}
