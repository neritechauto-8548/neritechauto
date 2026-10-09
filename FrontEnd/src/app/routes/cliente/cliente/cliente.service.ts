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
  DocumentoClienteResponse,
  TipoCliente,
  StatusCliente
} from '../models/cliente.models';

@Injectable({ providedIn: 'root' })
export class ClientesService {
  private previewClientes: ClienteResponse[] = [
    { id: 1, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'Carlos Eduardo Silva', cpf: '529.982.247-25', email: 'carlos@example.com', status: StatusCliente.ATIVO },
    { id: 2, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'Mariana Oliveira', cpf: '111.444.777-35', email: 'mariana@example.com', status: StatusCliente.ATIVO },
    { id: 3, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'João Pedro Santos', cpf: '935.411.347-80', email: 'joao@example.com', status: StatusCliente.INATIVO },
    { id: 4, empresaId: 1, tipoCliente: TipoCliente.PESSOA_JURIDICA, razaoSocial: 'Transportes Alfa Ltda.', nomeFantasia: 'Alfa Transportes', cnpj: '11.222.333/0001-81', email: 'contato@alfa.example.com', status: StatusCliente.ATIVO },
    { id: 5, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'Fernanda Costa', cpf: '168.995.350-09', email: 'fernanda@example.com', status: StatusCliente.BLOQUEADO },
    { id: 6, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'Ricardo Almeida', cpf: '286.255.878-87', email: 'ricardo@example.com', status: StatusCliente.ATIVO },
    { id: 7, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'Patrícia Gomes', cpf: '935.411.347-80', email: 'patricia@example.com', status: StatusCliente.ATIVO },
    { id: 8, empresaId: 1, tipoCliente: TipoCliente.PESSOA_FISICA, nomeCompleto: 'André Martins', cpf: '168.995.350-09', email: 'andre@example.com', status: StatusCliente.ATIVO },
  ];
  private previewEnderecos: EnderecoClienteResponse[] = [];
  private previewContatos: ContatoClienteResponse[] = [
    { id: 1, clienteId: 1, tipoContato: 'CELULAR' as any, valor: '(81) 99999-1010' },
    { id: 2, clienteId: 2, tipoContato: 'WHATSAPP' as any, valor: '(81) 98888-2020' },
    { id: 3, clienteId: 4, tipoContato: 'TELEFONE_FIXO' as any, valor: '(81) 3333-4040' },
  ];
  private previewDocumentos: DocumentoClienteResponse[] = [];
  private previewNextId = 20;

  private readonly http = inject(HttpClient);
  private readonly base = environment.baseUrl;

  // Headers são gerenciados pelos interceptors (tenant-interceptor e token-interceptor)
  // Não precisamos adicionar manualmente X-Tenant-Id e Authorization

  // ========== CLIENTES ==========

  list(filters: Record<string, any>): Observable<Page<ClienteResponse>> {
    if (environment.uxPreview) {
      const term = String(filters?.['busca'] || filters?.['search'] || filters?.['nome'] || '').trim().toLocaleLowerCase('pt-BR');
      const tipo = filters?.['tipoCliente'];
      const status = filters?.['status'];
      const filtered = this.previewClientes
        .filter(c => !term || [c.nomeCompleto, c.nomeFantasia, c.razaoSocial, c.cpf, c.cnpj, c.email]
          .some(v => String(v || '').toLocaleLowerCase('pt-BR').includes(term)))
        .filter(c => !tipo || c.tipoCliente === tipo)
        .filter(c => !status || c.status === status)
        .sort((a, b) => this.getClientName(a).localeCompare(this.getClientName(b), 'pt-BR'));
      const page = Math.max(0, Number(filters?.['page'] ?? 0));
      const size = Math.max(1, Number(filters?.['size'] ?? 10));
      const start = page * size;
      return of({
        content: filtered.slice(start, start + size),
        totalElements: filtered.length,
        totalPages: Math.ceil(filtered.length / size),
        number: page,
        size
      });
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

  private getClientName(c: ClienteResponse): string {
    return c.nomeCompleto || c.nomeFantasia || c.razaoSocial || '';
  }

  create(dto: ClienteRequest): Observable<ClienteResponse> {
    if (environment.uxPreview) {
      const created: ClienteResponse = { ...dto, id: this.previewNextId++, empresaId: 1, status: dto.status || StatusCliente.ATIVO };
      this.previewClientes = [...this.previewClientes, created];
      return of(created);
    }
    const url = `${this.base}/v1/clientes`;
    return this.http.post<ClienteResponse>(url, dto);
  }

  getById(id: number | string): Observable<ClienteResponse> {
    if (environment.uxPreview) {
      const cliente = this.previewClientes.find(c => c.id === Number(id));
      if (cliente) return of(cliente);
      throw new Error('Cliente demonstrativo não encontrado.');
    }
    const url = `${this.base}/v1/clientes/${id}`;
    return this.http.get<any>(url).pipe(map((resp: any) => resp?.data ?? resp));
  }

  update(id: number | string, dto: Partial<ClienteRequest>): Observable<ClienteResponse> {
    if (environment.uxPreview) {
      const numericId = Number(id);
      const existing = this.previewClientes.find(c => c.id === numericId);
      if (!existing) throw new Error('Cliente demonstrativo não encontrado.');
      const updated = { ...existing, ...dto, id: numericId, empresaId: existing.empresaId };
      this.previewClientes = this.previewClientes.map(c => c.id === numericId ? updated : c);
      return of(updated);
    }
    const url = `${this.base}/v1/clientes/${id}`;
    return this.http.put<ClienteResponse>(url, dto);
  }

  delete(id: number | string): Observable<void> {
    if (environment.uxPreview) {
      const numericId = Number(id);
      this.previewClientes = this.previewClientes.filter(c => c.id !== numericId);
      this.previewEnderecos = this.previewEnderecos.filter(e => e.clienteId !== numericId);
      this.previewContatos = this.previewContatos.filter(c => c.clienteId !== numericId);
      this.previewDocumentos = this.previewDocumentos.filter(d => d.clienteId !== numericId);
      return of(void 0);
    }
    const url = `${this.base}/v1/clientes/${id}`;
    return this.http.delete<void>(url);
  }

  // Alias para deleteCliente
  deleteCliente(id: number | string): Observable<void> {
    return this.delete(id);
  }


  // ========== ENDEREÇOS ==========

  listarEnderecos(clienteId: number | string): Observable<Page<EnderecoClienteResponse>> {
    if (environment.uxPreview) {
      const content = this.previewEnderecos.filter(e => e.clienteId === Number(clienteId));
      return of({ content, totalElements: content.length, totalPages: content.length ? 1 : 0, number: 0, size: Math.max(content.length, 10) });
    }
    const url = `${this.base}/v1/clientes/${clienteId}/enderecos`;
    return this.http.get<any>(url).pipe(map((resp: any) => resp?.data ?? resp));
  }

  buscarEndereco(clienteId: number | string, id: number | string): Observable<EnderecoClienteResponse> {
    if (environment.uxPreview) {
      const item = this.previewEnderecos.find(e => e.clienteId === Number(clienteId) && e.id === Number(id));
      if (item) return of(item);
      throw new Error('Endereço demonstrativo não encontrado.');
    }
    const url = `${this.base}/v1/clientes/${clienteId}/enderecos/${id}`;
    return this.http.get<EnderecoClienteResponse>(url);
  }

  criarEndereco(clienteId: number | string, endereco: EnderecoClienteRequest): Observable<EnderecoClienteResponse> {
    if (environment.uxPreview) {
      const created: EnderecoClienteResponse = { ...endereco, id: this.previewNextId++, clienteId: Number(clienteId) };
      this.previewEnderecos = [...this.previewEnderecos, created];
      return of(created);
    }
    const url = `${this.base}/v1/clientes/${clienteId}/enderecos`;
    return this.http.post<EnderecoClienteResponse>(url, endereco);
  }

  atualizarEndereco(
    clienteId: number | string,
    id: number | string,
    endereco: EnderecoClienteRequest
  ): Observable<EnderecoClienteResponse> {
    if (environment.uxPreview) {
      const updated: EnderecoClienteResponse = { ...endereco, id: Number(id), clienteId: Number(clienteId) };
      this.previewEnderecos = this.previewEnderecos.map(e => e.id === Number(id) && e.clienteId === Number(clienteId) ? updated : e);
      return of(updated);
    }
    const url = `${this.base}/v1/clientes/${clienteId}/enderecos/${id}`;
    return this.http.put<EnderecoClienteResponse>(url, endereco);
  }

  excluirEndereco(clienteId: number | string, id: number | string): Observable<void> {
    if (environment.uxPreview) {
      this.previewEnderecos = this.previewEnderecos.filter(e => !(e.clienteId === Number(clienteId) && e.id === Number(id)));
      return of(void 0);
    }
    const url = `${this.base}/v1/clientes/${clienteId}/enderecos/${id}`;
    return this.http.delete<void>(url);
  }

  // ========== CONTATOS ==========

  listarContatos(clienteId: number | string): Observable<Page<ContatoClienteResponse>> {
    if (environment.uxPreview) {
      const content = this.previewContatos.filter(c => c.clienteId === Number(clienteId));
      return of({ content, totalElements: content.length, totalPages: content.length ? 1 : 0, number: 0, size: Math.max(content.length, 10) });
    }
    const url = `${this.base}/v1/clientes/${clienteId}/contatos`;
    return this.http.get<any>(url).pipe(map((resp: any) => resp?.data ?? resp));
  }

  buscarContato(clienteId: number | string, id: number | string): Observable<ContatoClienteResponse> {
    if (environment.uxPreview) {
      const item = this.previewContatos.find(c => c.clienteId === Number(clienteId) && c.id === Number(id));
      if (item) return of(item);
      throw new Error('Contato demonstrativo não encontrado.');
    }
    const url = `${this.base}/v1/clientes/${clienteId}/contatos/${id}`;
    return this.http.get<ContatoClienteResponse>(url);
  }

  criarContato(clienteId: number | string, contato: ContatoClienteRequest): Observable<ContatoClienteResponse> {
    if (environment.uxPreview) {
      const valor = (contato as any).valor ?? (contato as any).contato ?? '';
      const created: ContatoClienteResponse = { tipoContato: contato.tipoContato, valor, id: this.previewNextId++, clienteId: Number(clienteId) };
      this.previewContatos = [...this.previewContatos, created];
      return of(created);
    }
    const url = `${this.base}/v1/clientes/${clienteId}/contatos`;
    return this.http.post<ContatoClienteResponse>(url, contato);
  }

  atualizarContato(
    clienteId: number | string,
    id: number | string,
    contato: ContatoClienteRequest
  ): Observable<ContatoClienteResponse> {
    if (environment.uxPreview) {
      const valor = (contato as any).valor ?? (contato as any).contato ?? '';
      const updated: ContatoClienteResponse = { ...contato, valor, id: Number(id), clienteId: Number(clienteId) };
      this.previewContatos = this.previewContatos.map(c => c.id === Number(id) && c.clienteId === Number(clienteId) ? updated : c);
      return of(updated);
    }
    const url = `${this.base}/v1/clientes/${clienteId}/contatos/${id}`;
    return this.http.put<ContatoClienteResponse>(url, contato);
  }

  excluirContato(clienteId: number | string, id: number | string): Observable<void> {
    if (environment.uxPreview) {
      this.previewContatos = this.previewContatos.filter(c => !(c.clienteId === Number(clienteId) && c.id === Number(id)));
      return of(void 0);
    }
    const url = `${this.base}/v1/clientes/${clienteId}/contatos/${id}`;
    return this.http.delete<void>(url);
  }

  // ========== DOCUMENTOS ==========

  listarDocumentos(clienteId: number | string): Observable<Page<DocumentoClienteResponse>> {
    if (environment.uxPreview) {
      const content = this.previewDocumentos.filter(d => d.clienteId === Number(clienteId));
      return of({ content, totalElements: content.length, totalPages: content.length ? 1 : 0, number: 0, size: Math.max(content.length, 10) });
    }
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

