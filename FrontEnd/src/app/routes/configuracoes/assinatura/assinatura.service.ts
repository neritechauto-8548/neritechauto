import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

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

  getStatus(empresaId: number): Observable<AssinaturaStatus> {
    return this.http.get<AssinaturaStatus>(`/api/v1/assinatura/status/${empresaId}`);
  }

  getPortalUrl(empresaId: number, returnUrl: string): Observable<PortalResponse> {
    return this.http.post<PortalResponse>(`/api/v1/assinatura/portal/${empresaId}`, { returnUrl });
  }
}
