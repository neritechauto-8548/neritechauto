import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../environments/environment';
import { Page, StatusOSRequest, StatusOSResponse } from './models/os.models';

@Injectable({ providedIn: 'root' })
export class StatusOSService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = `${environment.baseUrl}/v1/status-os`;

  private getTenantId(): number {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    return Number(tenantId);
  }

  list(filters?: Record<string, any>): Observable<Page<StatusOSResponse>> {
    const empresaId = this.getTenantId();
    let params = new HttpParams();
    Object.entries(filters || {}).forEach(([k, v]) => {
      if (v !== undefined && v !== null && `${v}` !== '') {
        params = params.set(k, String(v));
      }
    });
    return this.http.get<any>(`${this.base}/empresa/${empresaId}`, {
      params,
    }).pipe(map((resp: any) => resp?.data ?? resp));
  }

  getById(id: number | string): Observable<StatusOSResponse> {
    return this.http.get<any>(`${this.base}/${id}`).pipe(map((resp: any) => resp?.data ?? resp));
  }

  create(dto: Omit<StatusOSRequest, 'empresaId'>): Observable<StatusOSResponse> {
    const tenantId = this.getTenantId();
    const body: StatusOSRequest = { ...dto, empresaId: tenantId };
    return this.http.post<any>(this.base, body).pipe(map((resp: any) => resp?.data ?? resp));
  }

  update(id: number | string, dto: Omit<StatusOSRequest, 'empresaId'>): Observable<StatusOSResponse> {
    const tenantId = this.getTenantId();
    const body: StatusOSRequest = { ...dto, empresaId: tenantId };
    return this.http.put<any>(`${this.base}/${id}`, body).pipe(map((resp: any) => resp?.data ?? resp));
  }

  delete(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
