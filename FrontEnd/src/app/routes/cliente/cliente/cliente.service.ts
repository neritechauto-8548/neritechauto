import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import {
  Page,
  ClienteRequest,
  ClienteResponse,
  EnderecoClienteRequest,
  EnderecoClienteResponse,
  ContatoClienteRequest,
  ContatoClienteResponse,
  DocumentoClienteRequest,
  DocumentoClienteResponse
} from '../models/cliente.models';

@Injectable({ providedIn: 'root' })
export class ClientesService {
  private readonly http = inject(HttpClient);
  private readonly base = environment.baseUrl;

  // Headers são gerenciados pelos interceptors (tenant-interceptor e token-interceptor)
  // Não precisamos adicionar manualmente X-Tenant-Id e Authorization

  // ========== CLIENTES ==========

  list(filters: Record<string, any>): Observable<Page<ClienteResponse>> {
    const url = `${this.base}/v1/clientes`;
    let params = new HttpParams();
    Object.entries(filters || {}).forEach(([k, v]) => {
      if (v !== undefined && v !== null && `${v}` !== '') {
        params = params.set(k, String(v));
      }
    });
    return this.http.get<any>(url, { params }).pipe(map((resp: any) => resp?.data ?? resp));
  }

  create(dto: ClienteRequest): Observable<ClienteResponse> {
    const url = `${this.base}/v1/clientes`;
    return this.http.post<ClienteResponse>(url, dto);
  }

  getById(id: number | string): Observable<ClienteResponse> {
    const url = `${this.base}/v1/clientes/${id}`;
    return this.http.get<any>(url).pipe(map((resp: any) => resp?.data ?? resp));
  }

  update(id: number | string, dto: Partial<ClienteRequest>): Observable<ClienteResponse> {
    const url = `${this.base}/v1/clientes/${id}`;
    return this.http.put<ClienteResponse>(url, dto);
  }

  delete(id: number | string): Observable<void> {
    const url = `${this.base}/v1/clientes/${id}`;
    return this.http.delete<void>(url);
  }

  // Alias para deleteCliente
  deleteCliente(id: number | string): Observable<void> {
    return this.delete(id);
  }


  // ========== ENDEREÇOS ==========

  listarEnderecos(clienteId: number | string): Observable<Page<EnderecoClienteResponse>> {
    const url = `${this.base}/v1/clientes/${clienteId}/enderecos`;
    return this.http.get<any>(url).pipe(map((resp: any) => resp?.data ?? resp));
  }

  buscarEndereco(clienteId: number | string, id: number | string): Observable<EnderecoClienteResponse> {
    const url = `${this.base}/v1/clientes/${clienteId}/enderecos/${id}`;
    return this.http.get<EnderecoClienteResponse>(url);
  }

  criarEndereco(clienteId: number | string, endereco: EnderecoClienteRequest): Observable<EnderecoClienteResponse> {
    const url = `${this.base}/v1/clientes/${clienteId}/enderecos`;
    return this.http.post<EnderecoClienteResponse>(url, endereco);
  }

  atualizarEndereco(
    clienteId: number | string,
    id: number | string,
    endereco: EnderecoClienteRequest
  ): Observable<EnderecoClienteResponse> {
    const url = `${this.base}/v1/clientes/${clienteId}/enderecos/${id}`;
    return this.http.put<EnderecoClienteResponse>(url, endereco);
  }

  excluirEndereco(clienteId: number | string, id: number | string): Observable<void> {
    const url = `${this.base}/v1/clientes/${clienteId}/enderecos/${id}`;
    return this.http.delete<void>(url);
  }

  // ========== CONTATOS ==========

  listarContatos(clienteId: number | string): Observable<Page<ContatoClienteResponse>> {
    const url = `${this.base}/v1/clientes/${clienteId}/contatos`;
    return this.http.get<any>(url).pipe(map((resp: any) => resp?.data ?? resp));
  }

  buscarContato(clienteId: number | string, id: number | string): Observable<ContatoClienteResponse> {
    const url = `${this.base}/v1/clientes/${clienteId}/contatos/${id}`;
    return this.http.get<ContatoClienteResponse>(url);
  }

  criarContato(clienteId: number | string, contato: ContatoClienteRequest): Observable<ContatoClienteResponse> {
    const url = `${this.base}/v1/clientes/${clienteId}/contatos`;
    return this.http.post<ContatoClienteResponse>(url, contato);
  }

  atualizarContato(
    clienteId: number | string,
    id: number | string,
    contato: ContatoClienteRequest
  ): Observable<ContatoClienteResponse> {
    const url = `${this.base}/v1/clientes/${clienteId}/contatos/${id}`;
    return this.http.put<ContatoClienteResponse>(url, contato);
  }

  excluirContato(clienteId: number | string, id: number | string): Observable<void> {
    const url = `${this.base}/v1/clientes/${clienteId}/contatos/${id}`;
    return this.http.delete<void>(url);
  }

  // ========== DOCUMENTOS ==========

  listarDocumentos(clienteId: number | string): Observable<Page<DocumentoClienteResponse>> {
    const url = `${this.base}/v1/clientes/${clienteId}/documentos`;
    return this.http.get<any>(url).pipe(map((resp: any) => resp?.data ?? resp));
  }

  buscarDocumento(clienteId: number | string, id: number | string): Observable<DocumentoClienteResponse> {
    const url = `${this.base}/v1/clientes/${clienteId}/documentos/${id}`;
    return this.http.get<DocumentoClienteResponse>(url);
  }

  criarDocumento(
    clienteId: number | string,
    doc: DocumentoClienteRequest
  ): Observable<DocumentoClienteResponse> {
    const url = `${this.base}/v1/clientes/${clienteId}/documentos`;
    return this.http.post<DocumentoClienteResponse>(url, doc);
  }

  atualizarDocumento(
    clienteId: number | string,
    id: number | string,
    doc: DocumentoClienteRequest
  ): Observable<DocumentoClienteResponse> {
    const url = `${this.base}/v1/clientes/${clienteId}/documentos/${id}`;
    return this.http.put<DocumentoClienteResponse>(url, doc);
  }

  uploadDocumento(
    clienteId: number | string,
    file: File,
    tipoDocumento: string,
    descricao?: string
  ): Observable<DocumentoClienteResponse> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('tipoDocumento', tipoDocumento);
    if (descricao) {
      formData.append('descricao', descricao);
    }
    const url = `${this.base}/v1/clientes/${clienteId}/documentos/upload`;
    return this.http.post<DocumentoClienteResponse>(url, formData);
  }

  downloadDocumento(clienteId: number | string, id: number | string): Observable<Blob> {
    const url = `${this.base}/v1/clientes/${clienteId}/documentos/${id}/download`;
    return this.http.get(url, { responseType: 'blob' });
  }

  excluirDocumento(clienteId: number | string, id: number | string): Observable<void> {
    const url = `${this.base}/v1/clientes/${clienteId}/documentos/${id}`;
    return this.http.delete<void>(url);
  }

}

// Re-export legacy interfaces for backward compatibility
export interface ClienteResponseDTO extends ClienteResponse {}
export interface ClienteRequestDTO extends ClienteRequest {}
export { Page };

