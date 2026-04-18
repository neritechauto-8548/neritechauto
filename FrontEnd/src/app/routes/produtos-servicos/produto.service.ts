import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../environments/environment';
import { Page, ProdutoRequest, ProdutoResponse } from './models/produto.models';

@Injectable({ providedIn: 'root' })
export class ProdutoService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = `${environment.baseUrl}/v1/produtos`;

  private getHeaders(): HttpHeaders {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') {
      tenantId = '1';
    }
    return new HttpHeaders({
      'X-Tenant-Id': String(tenantId),
      'Accept': 'application/json',
    });
  }

  private getEmpresaIdParam(params?: Record<string, any>): HttpParams {
    let httpParams = new HttpParams();
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') {
      tenantId = '1';
    }
    httpParams = httpParams.set('empresaId', String(tenantId));
    Object.entries(params || {}).forEach(([k, v]) => {
      if (v !== undefined && v !== null && `${v}` !== '') {
        httpParams = httpParams.set(k, String(v));
      }
    });
    return httpParams;
  }

  list(params?: Record<string, any>): Observable<Page<ProdutoResponse>> {
    return this.http.get<Page<ProdutoResponse>>(this.base, {
      params: this.getEmpresaIdParam(params),
      headers: this.getHeaders(),
    });
  }

  get(id: number | string): Observable<ProdutoResponse> {
    return this.http.get<ProdutoResponse>(`${this.base}/${id}`, {
      headers: this.getHeaders(),
    });
  }

  create(dto: Omit<ProdutoRequest, 'empresaId'>): Observable<ProdutoResponse> {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') {
      tenantId = '1';
    }
    const body: ProdutoRequest = { ...dto, empresaId: Number(tenantId) };
    return this.http.post<ProdutoResponse>(this.base, body, {
      headers: this.getHeaders(),
    });
  }

  update(id: number | string, dto: Omit<ProdutoRequest, 'empresaId'>): Observable<ProdutoResponse> {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') {
      tenantId = '1';
    }
    const body: ProdutoRequest = { ...dto, empresaId: Number(tenantId) };
    return this.http.put<ProdutoResponse>(`${this.base}/${id}`, body, {
      headers: this.getHeaders(),
      params: this.getEmpresaIdParam(),
    });
  }

  delete(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, {
      headers: this.getHeaders(),
      params: this.getEmpresaIdParam(),
    });
  }

  // ========== IMPRESSAO (JASPER) ==========

  private getPdfHeaders(): HttpHeaders {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') {
      tenantId = '1';
    }
    return new HttpHeaders({
      'X-Tenant-Id': String(tenantId),
      'Accept': 'application/pdf',
    });
  }

  printEstoque(): Observable<Blob> {
    const url = `${environment.baseUrl}/v1/relatorios/estoque`;
    return this.http.get(url, { headers: this.getPdfHeaders(), responseType: 'blob' });
  }

  printEtiquetas(): Observable<Blob> {
    const url = `${environment.baseUrl}/v1/relatorios/estoque/etiquetas`;
    return this.http.get(url, { headers: this.getPdfHeaders(), responseType: 'blob' });
  }

  // ========== AUXILIARES ==========

  listCategorias(): Observable<any[]> {
    // Ajustar path se necessario. Assumindo /v1/categorias-produtos
    const url = `${environment.baseUrl}/v1/categorias-produtos`;
    return this.http.get<any[]>(url, {
        headers: this.getHeaders(),
        params: this.getEmpresaIdParam().set('size', '1000') // Trazer todas
    });
  }

  listUnidades(): Observable<any[]> {
    // Ajustar path se necessario. Assumindo /v1/unidades-medida
    const url = `${environment.baseUrl}/v1/unidades-medida`;
    return this.http.get<any[]>(url, {
        headers: this.getHeaders(),
        params: this.getEmpresaIdParam().set('size', '1000')
    });
  }
}
