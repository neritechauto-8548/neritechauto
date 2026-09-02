import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ItemOSProdutoResponse, ItemOSServicoResponse } from '../models/os.models';
import {
  OsChecklistItem,
  OsDiagnosticRequest,
  OsDiagnosticResponse,
  OsPhotoEvidence,
} from './os-operations.models';

@Injectable({ providedIn: 'root' })
export class OsOperationsService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.baseUrl;

  listProducts(osId: number): Observable<ItemOSProdutoResponse[]> {
    return this.http.get<ItemOSProdutoResponse[]>(`${this.api}/v1/itens-os-produtos/ordem-servico/${osId}`);
  }

  deleteProduct(itemId: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/v1/itens-os-produtos/${itemId}`);
  }

  listServices(osId: number): Observable<ItemOSServicoResponse[]> {
    return this.http.get<ItemOSServicoResponse[]>(`${this.api}/v1/itens-os-servicos/ordem-servico/${osId}`);
  }

  deleteService(itemId: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/v1/itens-os-servicos/${itemId}`);
  }

  listDiagnostics(osId: number): Observable<OsDiagnosticResponse[]> {
    return this.http.get<OsDiagnosticResponse[]>(`${this.api}/v1/diagnosticos/ordem-servico/${osId}`);
  }

  createDiagnostic(request: OsDiagnosticRequest): Observable<OsDiagnosticResponse> {
    return this.http.post<OsDiagnosticResponse>(`${this.api}/v1/diagnosticos`, request);
  }

  deleteDiagnostic(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/v1/diagnosticos/${id}`);
  }

  listChecklist(osId: number): Observable<OsChecklistItem[]> {
    return this.http.get<OsChecklistItem[]>(`${this.api}/v1/ordens-servico/os-checklist/ordem-servico/${osId}`);
  }

  updateChecklistItem(item: OsChecklistItem, feito: boolean): Observable<OsChecklistItem> {
    return this.http.put<OsChecklistItem>(`${this.api}/v1/ordens-servico/os-checklist/${item.id}`, {
      descricao: item.descricao ?? null,
      feito,
      ordem: item.ordem ?? null,
    });
  }

  listEvidence(osId: number): Observable<OsPhotoEvidence[]> {
    return this.http.get<OsPhotoEvidence[]>(`${this.api}/v1/ordens-servico/${osId}/fotos`);
  }

  uploadEvidence(osId: number, file: File, description?: string): Observable<OsPhotoEvidence> {
    const form = new FormData();
    form.append('file', file);
    if (description?.trim()) form.append('descricao', description.trim());
    return this.http.post<OsPhotoEvidence>(`${this.api}/v1/ordens-servico/${osId}/fotos`, form);
  }

  downloadEvidence(id: number): Observable<Blob> {
    return this.http.get(`${this.api}/v1/ordens-servico/fotos/${id}/download`, { responseType: 'blob' });
  }

  deleteEvidence(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/v1/ordens-servico/fotos/${id}`);
  }
}
