import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

export interface AssinaturaStatus {
  plano: string;
  status: string;
  precoFormatado: string;
  proximaCobranca: string;
  stripeCustomerId: string;
  stripeSubscriptionId: string;
  stripeProductId: string;
  trial: boolean;
  inicioTrial: string;
  fimTrial: string;
}

export interface PortalResponse {
  url: string;
}

@Injectable({ providedIn: 'root' })
export class AssinaturaService {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.baseUrl}/v1/assinatura`;

  getStatus(empresaId: number): Observable<AssinaturaStatus> {
    return this.http.get<AssinaturaStatus>(`${this.apiUrl}/status/${empresaId}`);
  }

  getPortalUrl(empresaId: number, returnUrl: string): Observable<PortalResponse> {
    return this.http.post<PortalResponse>(`${this.apiUrl}/portal/${empresaId}`, { returnUrl });
  }
}
