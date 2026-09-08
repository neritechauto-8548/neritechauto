import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class OsCommunicationService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.baseUrl}/v1/ordens-servico`;

  sendEmail(osId: number, email?: string | null): Observable<void> {
    let params = new HttpParams();
    const normalized = email?.trim();
    if (normalized) params = params.set('emailDestino', normalized);
    return this.http.post<void>(`${this.baseUrl}/${osId}/enviar-email`, null, { params });
  }
}
