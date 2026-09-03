import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  OsAdditionalCreateRequest,
  OsAdditionalPublicDecision,
  OsAdditionalPublicRequest,
  OsAdditionalRequest,
  OsAdditionalSubmitRequest,
  OsAdditionalSubmitResponse,
} from './os-additional.models';

@Injectable({ providedIn: 'root' })
export class OsAdditionalService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.baseUrl;

  list(orderId: number): Observable<OsAdditionalRequest[]> {
    return this.http.get<OsAdditionalRequest[]>(`${this.api}/v1/ordens-servico/${orderId}/additional-requests`)
      .pipe(map(response => this.unwrap(response) ?? []));
  }

  create(orderId: number, request: OsAdditionalCreateRequest): Observable<OsAdditionalRequest> {
    return this.http.post<OsAdditionalRequest>(`${this.api}/v1/ordens-servico/${orderId}/additional-requests`, request)
      .pipe(map(response => this.unwrap(response)));
  }

  update(id: number, request: OsAdditionalCreateRequest): Observable<OsAdditionalRequest> {
    return this.http.patch<OsAdditionalRequest>(`${this.api}/v1/additional-requests/${id}`, request)
      .pipe(map(response => this.unwrap(response)));
  }

  submit(id: number, request: OsAdditionalSubmitRequest): Observable<OsAdditionalSubmitResponse> {
    return this.http.post<OsAdditionalSubmitResponse>(`${this.api}/v1/additional-requests/${id}/submit`, request)
      .pipe(map(response => this.unwrap(response)));
  }

  revoke(id: number): Observable<OsAdditionalRequest> {
    return this.http.post<OsAdditionalRequest>(`${this.api}/v1/additional-requests/${id}/revoke`, null)
      .pipe(map(response => this.unwrap(response)));
  }

  publicFind(token: string): Observable<OsAdditionalPublicRequest> {
    return this.http.get<OsAdditionalPublicRequest>(`${this.api}/public/v1/additional-approvals/${encodeURIComponent(token)}`)
      .pipe(map(response => this.unwrap(response)));
  }

  publicDecide(token: string, request: OsAdditionalPublicDecision): Observable<OsAdditionalPublicRequest> {
    return this.http.post<OsAdditionalPublicRequest>(
      `${this.api}/public/v1/additional-approvals/${encodeURIComponent(token)}/decision`, request
    ).pipe(map(response => this.unwrap(response)));
  }

  private unwrap<T>(response: T): T {
    return (response as any)?.data ?? response;
  }
}
