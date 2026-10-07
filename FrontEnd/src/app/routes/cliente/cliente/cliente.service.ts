import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { of } from 'rxjs';
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
  private readonly previewClientes: ClienteResponse[] = [
    { id: 1, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'Carlos Eduardo Silva', cpf: '123.456.789-00', email: 'carlos@example.com', status: StatusCliente.ATIVO },
    { id: 2, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'Mariana Oliveira', cpf: '234.567.890-11', email: 'mariana@example.com', status: StatusCliente.ATIVO },
    { id: 3, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'João Pedro Santos', cpf: '345.678.901-22', email: 'joao@example.com', status: StatusCliente.ATIVO },
    { id: 4, empresaId: 1, tipoCliente: TipoCliente.PESSOA_JURIDICA, razaoSocial: 'Transportes Alfa Ltda.', nomeFantasia: 'Alfa Transportes', cnpj: '12.345.678/0001-90', email: 'contato@alfa.example.com', status: StatusCliente.ATIVO },
    { id: 5, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'Fernanda Costa', cpf: '456.789.012-33', email: 'fernanda@example.com', status: StatusCliente.ATIVO },
    { id: 6, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'Ricardo Almeida', cpf: '567.890.123-44', email: 'ricardo@example.com', status: StatusCliente.ATIVO },
    { id: 7, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'Patrícia Gomes', cpf: '678.901.234-55', email: 'patricia@example.com', status: StatusCliente.ATIVO },
    { id: 8, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'André Martins', cpf: '789.012.345-66', email: 'andre@example.com', status: StatusCliente.ATIVO },
  ];

  private readonly http = inject(HttpClient);
  private readonly base = environment.baseUrl;

  // Headers são gerenciados pelos interceptors (tenant-interceptor e token-interceptor)
  // Não precisamos adicionar manualmente X-Tenant-Id e Authorization

  // ========== CLIENTES ==========

  list(filters: Record<string, any>): Observable<Page<ClienteResponse>> {
    if (environment.uxPreview) {
      const term = String(filters?.['search'] || filters?.['nome'] || '').trim().toLowerCase();
      const content = this.previewClientes.filter(c =>
        !term || [c.nomeCompleto, c.nomeFantasia, c.razaoSocial, c.cpf, c.cnpj].some(v => String(v || '').toLowerCase().includes(term))
      );
      return of({ content, totalElements: content.length, totalPages: 1, number: 0, size: content.length || 10 });
    }
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
    if (environment.uxPreview) {
      return of(this.previewClientes.find(c => c.id === Number(id)) || this.previewClientes[0]);
    }
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

