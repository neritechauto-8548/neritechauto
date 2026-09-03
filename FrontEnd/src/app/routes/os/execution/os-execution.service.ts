import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  OrdemServicoExecutionResponse,
  OsWorkSessionResponse,
} from '../models/os-execution.models';

@Injectable({ providedIn: 'root' })
export class OsExecutionService {
  private readonly http = inject(HttpClient);
  private readonly ordersUrl = `${environment.baseUrl}/v1/ordens-servico`;
  private readonly sessionsUrl = `${environment.baseUrl}/v1/work-sessions`;

  getExecution(osId: number): Observable<OrdemServicoExecutionResponse> {
    return this.http
      .get<OrdemServicoExecutionResponse>(`${this.ordersUrl}/${osId}/execution`)
      .pipe(map((response: any) => response?.data ?? response));
  }

  start(osId: number, serviceId: number, idempotencyKey: string): Observable<OsWorkSessionResponse> {
    const headers = new HttpHeaders({ 'Idempotency-Key': idempotencyKey });
    return this.http
      .post<OsWorkSessionResponse>(
        `${this.ordersUrl}/${osId}/services/${serviceId}/sessions/start`,
        null,
        { headers }
      )
      .pipe(map((response: any) => response?.data ?? response));
  }

  pause(
    sessionId: number,
    version: number,
    reason: string,
    note: string | null,
    idempotencyKey: string
  ): Observable<OsWorkSessionResponse> {
    const headers = this.commandHeaders(version, idempotencyKey);
    return this.http
      .post<OsWorkSessionResponse>(
        `${this.sessionsUrl}/${sessionId}/pause`,
        { reason, note },
        { headers }
      )
      .pipe(map((response: any) => response?.data ?? response));
  }

  resume(sessionId: number, version: number, idempotencyKey: string): Observable<OsWorkSessionResponse> {
    const headers = this.commandHeaders(version, idempotencyKey);
    return this.http
      .post<OsWorkSessionResponse>(`${this.sessionsUrl}/${sessionId}/resume`, null, { headers })
      .pipe(map((response: any) => response?.data ?? response));
  }

  finish(sessionId: number, version: number, idempotencyKey: string): Observable<OsWorkSessionResponse> {
    const headers = this.commandHeaders(version, idempotencyKey);
    return this.http
      .post<OsWorkSessionResponse>(`${this.sessionsUrl}/${sessionId}/finish`, null, { headers })
      .pipe(map((response: any) => response?.data ?? response));
  }

  private commandHeaders(version: number, idempotencyKey: string): HttpHeaders {
    return new HttpHeaders({
      'If-Match': String(version),
      'Idempotency-Key': idempotencyKey,
    });
  }
}
