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
  DocumentoClienteResponse,
  OrigemCliente,
  StatusCliente,
  TipoCliente,
  TipoContato
} from '../models/cliente.models';

export interface ClienteListResponseDTO {
  id: number;
  displayName: string;
  type: TipoCliente;
  maskedTaxId?: string | null;
  primaryContactSummary?: string | null;
  status: StatusCliente;
}

export interface ClienteDetailResponseDTO {
  id: number;
  displayName: string;
  type: TipoCliente;
  status: StatusCliente;
  maskedTaxId?: string | null;
  maskedEmail?: string | null;
  origin?: OrigemCliente | null;
  hasRelationshipNotes: boolean;
}

export interface ContatoClienteSummaryDTO {
  id: number;
  tipoContato: TipoContato;
  maskedValue: string;
  principal: boolean;
}

export interface EnderecoClienteSummaryDTO {
  id: number;
  locationSummary: string;
  maskedPostalCode: string;
  country: string;
}

@Injectable({ providedIn: 'root' })
export class ClientesService {
  private readonly http = inject(HttpClient);
  private readonly base = environment.baseUrl;

  list(filters: Record<string, any>): Observable<Page<ClienteListResponseDTO>> {
    const url = `${this.base}/v1/clientes`;
    let params = new HttpParams();
    Object.entries(filters || {}).forEach(([k, v]) => {
      if (v !== undefined && v !== null && `${v}` !== '') {
        params = params.set(k, String(v));
      }
    });
    return this.http.get<any>(url, { params }).pipe(map((resp: any) => resp?.data ?? resp));
  }

  getSummary(id: number | string): Observable<ClienteDetailResponseDTO> {
    return this.http.get<ClienteDetailResponseDTO>(`${this.base}/v1/clientes/${id}/resumo`);
  }

  listContactSummaries(clienteId: number | string): Observable<Page<ContatoClienteSummaryDTO>> {
    return this.http.get<Page<ContatoClienteSummaryDTO>>(`${this.base}/v1/clientes/${clienteId}/contatos/resumo`);
  }

  listAddressSummaries(clienteId: number | string): Observable<Page<EnderecoClienteSummaryDTO>> {
    return this.http.get<Page<EnderecoClienteSummaryDTO>>(`${this.base}/v1/clientes/${clienteId}/enderecos/resumo`);
  }

  create(dto: ClienteRequest): Observable<ClienteResponse> {
    return this.http.post<ClienteResponse>(`${this.base}/v1/clientes`, dto);
  }

  /** Contrato completo legado. Novas superfícies somente-leitura devem usar getSummary. */
  getById(id: number | string): Observable<ClienteResponse> {
    return this.http
      .get<any>(`${this.base}/v1/clientes/${id}`)
      .pipe(map((resp: any) => resp?.data ?? resp));
  }

  update(id: number | string, dto: Partial<ClienteRequest>): Observable<ClienteResponse> {
    return this.http.put<ClienteResponse>(`${this.base}/v1/clientes/${id}`, dto);
  }

  deactivate(id: number | string): Observable<ClienteResponse> {
    return this.http.patch<ClienteResponse>(`${this.base}/v1/clientes/${id}/inativar`, {});
  }

  reactivate(id: number | string): Observable<ClienteResponse> {
    return this.http.patch<ClienteResponse>(`${this.base}/v1/clientes/${id}/reativar`, {});
  }

  /** @deprecated Use deactivate. O endpoint DELETE legado também é lógico no backend. */
  delete(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.base}/v1/clientes/${id}`);
  }

  /** @deprecated Compatibilidade com telas antigas. */
  deleteCliente(id: number | string): Observable<void> {
    return this.delete(id);
  }

  listarEnderecos(clienteId: number | string): Observable<Page<EnderecoClienteResponse>> {
    return this.http
      .get<any>(`${this.base}/v1/clientes/${clienteId}/enderecos`)
      .pipe(map((resp: any) => resp?.data ?? resp));
  }

  buscarEndereco(clienteId: number | string, id: number | string): Observable<EnderecoClienteResponse> {
    return this.http.get<EnderecoClienteResponse>(`${this.base}/v1/clientes/${clienteId}/enderecos/${id}`);
  }

  criarEndereco(clienteId: number | string, endereco: EnderecoClienteRequest): Observable<EnderecoClienteResponse> {
    return this.http.post<EnderecoClienteResponse>(`${this.base}/v1/clientes/${clienteId}/enderecos`, endereco);
  }

  atualizarEndereco(
    clienteId: number | string,
    id: number | string,
    endereco: EnderecoClienteRequest
  ): Observable<EnderecoClienteResponse> {
    return this.http.put<EnderecoClienteResponse>(
      `${this.base}/v1/clientes/${clienteId}/enderecos/${id}`,
      endereco
    );
  }

  excluirEndereco(clienteId: number | string, id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.base}/v1/clientes/${clienteId}/enderecos/${id}`);
  }

  listarContatos(clienteId: number | string): Observable<Page<ContatoClienteResponse>> {
    return this.http
      .get<any>(`${this.base}/v1/clientes/${clienteId}/contatos`)
      .pipe(map((resp: any) => resp?.data ?? resp));
  }

  buscarContato(clienteId: number | string, id: number | string): Observable<ContatoClienteResponse> {
    return this.http.get<ContatoClienteResponse>(`${this.base}/v1/clientes/${clienteId}/contatos/${id}`);
  }

  criarContato(clienteId: number | string, contato: ContatoClienteRequest): Observable<ContatoClienteResponse> {
    return this.http.post<ContatoClienteResponse>(
      `${this.base}/v1/clientes/${clienteId}/contatos`,
      this.normalizeContactPayload(contato)
    );
  }

  atualizarContato(
    clienteId: number | string,
    id: number | string,
    contato: ContatoClienteRequest
  ): Observable<ContatoClienteResponse> {
    return this.http.put<ContatoClienteResponse>(
      `${this.base}/v1/clientes/${clienteId}/contatos/${id}`,
      this.normalizeContactPayload(contato)
    );
  }

  excluirContato(clienteId: number | string, id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.base}/v1/clientes/${clienteId}/contatos/${id}`);
  }

  listarDocumentos(clienteId: number | string): Observable<Page<DocumentoClienteResponse>> {
    return this.http
      .get<any>(`${this.base}/v1/clientes/${clienteId}/documentos`)
      .pipe(map((resp: any) => resp?.data ?? resp));
  }

  buscarDocumento(clienteId: number | string, id: number | string): Observable<DocumentoClienteResponse> {
    return this.http.get<DocumentoClienteResponse>(`${this.base}/v1/clientes/${clienteId}/documentos/${id}`);
  }

  criarDocumento(
    clienteId: number | string,
    doc: DocumentoClienteRequest
  ): Observable<DocumentoClienteResponse> {
    return this.http.post<DocumentoClienteResponse>(`${this.base}/v1/clientes/${clienteId}/documentos`, doc);
  }

  atualizarDocumento(
    clienteId: number | string,
    id: number | string,
    doc: DocumentoClienteRequest
  ): Observable<DocumentoClienteResponse> {
    return this.http.put<DocumentoClienteResponse>(
      `${this.base}/v1/clientes/${clienteId}/documentos/${id}`,
      doc
    );
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
    if (descricao) formData.append('descricao', descricao);
    return this.http.post<DocumentoClienteResponse>(
      `${this.base}/v1/clientes/${clienteId}/documentos/upload`,
      formData
    );
  }

  downloadDocumento(clienteId: number | string, id: number | string): Observable<Blob> {
    return this.http.get(`${this.base}/v1/clientes/${clienteId}/documentos/${id}/download`, {
      responseType: 'blob',
    });
  }

  excluirDocumento(clienteId: number | string, id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.base}/v1/clientes/${clienteId}/documentos/${id}`);
  }

  private normalizeContactPayload(contact: ContatoClienteRequest) {
    return {
      tipoContato: contact.tipoContato,
      contato: contact.contato ?? contact.valor ?? '',
      principal: Boolean(contact.principal),
    };
  }
}

export interface ClienteResponseDTO extends ClienteResponse {}
export interface ClienteRequestDTO extends ClienteRequest {}
export { Page };
