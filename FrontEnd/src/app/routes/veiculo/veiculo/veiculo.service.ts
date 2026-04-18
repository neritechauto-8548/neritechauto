import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

import {
  Page,
  VeiculoRequest,
  VeiculoResponse,
  MarcaVeiculoRequest,
  MarcaVeiculoResponse,
  ModeloVeiculoRequest,
  ModeloVeiculoResponse,
  AnoModeloResponse,
  TipoCombustivelResponse,
  FotoVeiculoRequest,
  FotoVeiculoResponse,
  DocumentoVeiculoRequest,
  DocumentoVeiculoResponse
} from '../models/veiculo.models';

@Injectable({ providedIn: 'root' })
export class VeiculoService {
  private readonly http = inject(HttpClient);
  private readonly base = environment.baseUrl;

  // Headers são gerenciados pelos interceptors (tenant-interceptor e token-interceptor)
  // Não precisamos adicionar manualmente X-Tenant-Id e Authorization

  // ========== VEÍCULOS ==========

  list(clienteId?: number): Observable<VeiculoResponse[]> {
    const url = `${this.base}/v1/veiculos`;
    let params = new HttpParams();
    if (clienteId !== undefined && clienteId !== null) {
      params = params.set('clienteId', String(clienteId));
    }
    return this.http.get<VeiculoResponse[]>(url, { params });
  }

  getById(id: number | string): Observable<VeiculoResponse> {
    const url = `${this.base}/v1/veiculos/${id}`;
    return this.http.get<VeiculoResponse>(url);
  }

  create(dto: VeiculoRequest): Observable<VeiculoResponse> {
    const url = `${this.base}/v1/veiculos`;
    return this.http.post<VeiculoResponse>(url, dto);
  }

  update(id: number | string, dto: VeiculoRequest): Observable<VeiculoResponse> {
    const url = `${this.base}/v1/veiculos/${id}`;
    return this.http.put<VeiculoResponse>(url, dto);
  }

  delete(id: number | string): Observable<void> {
    const url = `${this.base}/v1/veiculos/${id}`;
    return this.http.delete<void>(url);
  }

  // ========== MARCAS ==========

  listMarcas(filters?: Record<string, any>): Observable<Page<MarcaVeiculoResponse>> {
    const url = `${this.base}/v1/marcas-veiculos`;
    let params = new HttpParams();
    Object.entries(filters || {}).forEach(([k, v]) => {
      if (v !== undefined && v !== null && `${v}` !== '') {
        params = params.set(k, String(v));
      }
    });
    return this.http.get<Page<MarcaVeiculoResponse>>(url, { params });
  }

  getMarcaById(id: number | string): Observable<MarcaVeiculoResponse> {
    const url = `${this.base}/v1/marcas-veiculos/${id}`;
    return this.http.get<MarcaVeiculoResponse>(url);
  }

  createMarca(dto: MarcaVeiculoRequest): Observable<MarcaVeiculoResponse> {
    const url = `${this.base}/v1/marcas-veiculos`;
    return this.http.post<MarcaVeiculoResponse>(url, dto);
  }

  updateMarca(id: number | string, dto: MarcaVeiculoRequest): Observable<MarcaVeiculoResponse> {
    const url = `${this.base}/v1/marcas-veiculos/${id}`;
    return this.http.put<MarcaVeiculoResponse>(url, dto);
  }

  ativarMarca(id: number | string): Observable<void> {
    const url = `${this.base}/v1/marcas-veiculos/${id}/ativar`;
    return this.http.patch<void>(url, {});
  }

  desativarMarca(id: number | string): Observable<void> {
    const url = `${this.base}/v1/marcas-veiculos/${id}/desativar`;
    return this.http.patch<void>(url, {});
  }

  deleteMarca(id: number | string): Observable<void> {
    const url = `${this.base}/v1/marcas-veiculos/${id}`;
    return this.http.delete<void>(url);
  }

  // ========== MODELOS ==========

  listModelos(marcaId?: number): Observable<ModeloVeiculoResponse[]> {
    const url = `${this.base}/v1/modelos-veiculos`;
    let params = new HttpParams();
    if (marcaId !== undefined && marcaId !== null) {
      params = params.set('marcaId', String(marcaId));
    }
    return this.http.get<ModeloVeiculoResponse[]>(url, { params });
  }

  getModeloById(id: number | string): Observable<ModeloVeiculoResponse> {
    const url = `${this.base}/v1/modelos-veiculos/${id}`;
    return this.http.get<ModeloVeiculoResponse>(url);
  }

  createModelo(dto: ModeloVeiculoRequest): Observable<ModeloVeiculoResponse> {
    const url = `${this.base}/v1/modelos-veiculos`;
    return this.http.post<ModeloVeiculoResponse>(url, dto);
  }

  updateModelo(id: number | string, dto: ModeloVeiculoRequest): Observable<ModeloVeiculoResponse> {
    const url = `${this.base}/v1/modelos-veiculos/${id}`;
    return this.http.put<ModeloVeiculoResponse>(url, dto);
  }

  deleteModelo(id: number | string): Observable<void> {
    const url = `${this.base}/v1/modelos-veiculos/${id}`;
    return this.http.delete<void>(url);
  }

  // ========== ANOS MODELO ==========

  listAnosModelo(modeloId: number): Observable<AnoModeloResponse[]> {
    const url = `${this.base}/v1/anos-modelo`;
    const params = new HttpParams().set('modeloId', String(modeloId));
    return this.http.get<AnoModeloResponse[]>(url, { params });
  }

  // ========== TIPOS COMBUSTÍVEL ==========

  listTiposCombustivel(): Observable<TipoCombustivelResponse[]> {
    const url = `${this.base}/v1/tipos-combustivel`;
    return this.http.get<TipoCombustivelResponse[]>(url);
  }

  // ========== FOTOS ==========

  listFotos(veiculoId: number): Observable<FotoVeiculoResponse[]> {
    const url = `${this.base}/v1/fotos-veiculos`;
    const params = new HttpParams().set('veiculoId', String(veiculoId));
    return this.http.get<FotoVeiculoResponse[]>(url, { params });
  }

  getFotoById(id: number | string): Observable<FotoVeiculoResponse> {
    const url = `${this.base}/v1/fotos-veiculos/${id}`;
    return this.http.get<FotoVeiculoResponse>(url);
  }

  createFoto(dto: FotoVeiculoRequest): Observable<FotoVeiculoResponse> {
    const url = `${this.base}/v1/fotos-veiculos`;
    return this.http.post<FotoVeiculoResponse>(url, dto);
  }

  updateFoto(id: number | string, dto: FotoVeiculoRequest): Observable<FotoVeiculoResponse> {
    const url = `${this.base}/v1/fotos-veiculos/${id}`;
    return this.http.put<FotoVeiculoResponse>(url, dto);
  }

  deleteFoto(id: number | string): Observable<void> {
    const url = `${this.base}/v1/fotos-veiculos/${id}`;
    return this.http.delete<void>(url);
  }

  // ========== DOCUMENTOS ==========

  listDocumentos(veiculoId: number): Observable<DocumentoVeiculoResponse[]> {
    const url = `${this.base}/v1/documentos-veiculos`;
    const params = new HttpParams().set('veiculoId', String(veiculoId));
    return this.http.get<DocumentoVeiculoResponse[]>(url, { params });
  }

  getDocumentoById(id: number | string): Observable<DocumentoVeiculoResponse> {
    const url = `${this.base}/v1/documentos-veiculos/${id}`;
    return this.http.get<DocumentoVeiculoResponse>(url);
  }

  createDocumento(dto: DocumentoVeiculoRequest): Observable<DocumentoVeiculoResponse> {
    const url = `${this.base}/v1/documentos-veiculos`;
    return this.http.post<DocumentoVeiculoResponse>(url, dto);
  }

  updateDocumento(id: number | string, dto: DocumentoVeiculoRequest): Observable<DocumentoVeiculoResponse> {
    const url = `${this.base}/v1/documentos-veiculos/${id}`;
    return this.http.put<DocumentoVeiculoResponse>(url, dto);
  }

  deleteDocumento(id: number | string): Observable<void> {
    const url = `${this.base}/v1/documentos-veiculos/${id}`;
    return this.http.delete<void>(url);
  }

  // ========== UPLOADS ==========

  uploadFoto(
    veiculoId: number | string,
    file: File,
    tipoFoto: string,
    descricao?: string
  ): Observable<FotoVeiculoResponse> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('veiculoId', String(veiculoId));
    formData.append('tipoFoto', tipoFoto);
    if (descricao) formData.append('descricao', descricao);

    const url = `${this.base}/v1/fotos-veiculos/upload`;
    return this.http.post<FotoVeiculoResponse>(url, formData);
  }

  uploadDocumento(
    veiculoId: number | string,
    file: File,
    tipoDocumento: string,
    observacoes?: string
  ): Observable<DocumentoVeiculoResponse> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('veiculoId', String(veiculoId));
    formData.append('tipoDocumento', tipoDocumento);
    if (observacoes) formData.append('observacoes', observacoes);

    const url = `${this.base}/v1/documentos-veiculos/upload`;
    return this.http.post<DocumentoVeiculoResponse>(url, formData);
  }
}
