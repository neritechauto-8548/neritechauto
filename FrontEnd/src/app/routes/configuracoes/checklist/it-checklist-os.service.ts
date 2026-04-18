import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { Page } from '../categoria/categoria-produto.service';

export interface ItChecklistOsRequest {
  checkListId: number;
  dsItChecklist: string;
}

export interface ItChecklistOsResponse {
  id: number;
  checkListId: number;
  dsItChecklist: string;
  dataCadastro?: string;
  dataAtualizacao?: string;
}

@Injectable({ providedIn: 'root' })
export class ItChecklistOsService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/ordens-servico/it-checklist';

  private getHeaders(): HttpHeaders {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    return new HttpHeaders({
      'X-Tenant-Id': String(tenantId),
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    });
  }

  list(params?: { page?: number; size?: number; sort?: string }): Observable<Page<ItChecklistOsResponse>> {
    const httpParams = new HttpParams({
      fromObject: {
        ...(params || {}),
      } as any
    });
    return this.http.get<Page<ItChecklistOsResponse>>(this.base, { params: httpParams, headers: this.getHeaders() });
  }

  get(id: number): Observable<ItChecklistOsResponse> {
    return this.http.get<ItChecklistOsResponse>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }

  listPorChecklist(checklistId: number): Observable<ItChecklistOsResponse[]> {
    return this.http.get<ItChecklistOsResponse[]>(`${this.base}/checklist/${checklistId}`, { headers: this.getHeaders() });
  }

  create(dto: ItChecklistOsRequest): Observable<ItChecklistOsResponse> {
    return this.http.post<ItChecklistOsResponse>(this.base, dto, { headers: this.getHeaders() });
  }

  update(id: number, dto: ItChecklistOsRequest): Observable<ItChecklistOsResponse> {
    return this.http.put<ItChecklistOsResponse>(`${this.base}/${id}`, dto, { headers: this.getHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.getHeaders() });
  }
}
