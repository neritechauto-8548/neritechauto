import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, defer, throwError } from 'rxjs';
import { empresaIdAtual } from '@shared/utils/empresa-context';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface FuncionarioRequest {
  empresaId: number;
  usuarioId?: number;
  matricula?: string;
  nomeCompleto: string;
  cpf: string;
  rg?: string;
  dataNascimento?: string;
  sexo?: string;
  escolaridade?: string;
  cargoId?: number;
  departamentoId?: number;
  dataAdmissao?: string;
  dataDemissao?: string;
  tipoContrato?: string;
  salarioBase?: number;
  registroProfissional?: string;
  tipoProfissional?: string;
  areaAtuacao?: string;
  comissaoPecas?: number;
  comissaoServicos?: number;
  linhasTransporte?: string;
  perfilProfissional?: string;
  valeTransporte?: number;
  status?: string;
  motivoInativacao?: string;
  cep?: string;
  logradouro?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  pisPasep?: string;
  carteiraTrabalho?: string;
  fotoFuncionarioUrl?: string;
  observacoes?: string;
  versao?: number;
}

export interface FuncionarioResponse {
  id: number;
  empresaId: number;
  usuarioId?: number;
  matricula: string;
  nomeCompleto: string;
  cpf: string;
  rg?: string;
  dataNascimento?: string;
  sexo?: string;
  escolaridade?: string;
  cargoId?: number;
  departamentoId?: number;
  cargoNome?: string;
  departamentoNome?: string;
  dataAdmissao?: string;
  dataDemissao?: string;
  tipoContrato?: string;
  salarioBase?: number;
  registroProfissional?: string;
  tipoProfissional?: string;
  areaAtuacao?: string;
  comissaoPecas?: number;
  comissaoServicos?: number;
  linhasTransporte?: string;
  perfilProfissional?: string;
  valeTransporte?: number;
  status?: 'ATIVO' | 'INATIVO' | 'AFASTADO' | 'DEMITIDO' | 'APOSENTADO';
  motivoInativacao?: string;
  cep?: string;
  logradouro?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  pisPasep?: string;
  carteiraTrabalho?: string;
  fotoFuncionarioUrl?: string;
  observacoes?: string;
  versao?: number;
}

@Injectable({ providedIn: 'root' })
export class FuncionarioService {
  private readonly http    = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base    = environment.baseUrl + '/v1/rh/funcionarios';

  private get tenantId(): string {
    return String(empresaIdAtual(this.storage) ?? '');
  }

  private withEmpresa<T>(request: () => Observable<T>): Observable<T> {
    return defer(() => empresaIdAtual(this.storage)
      ? request()
      : throwError(() => new Error('Não foi possível identificar sua empresa. Entre novamente.')));
  }

  private get headers(): HttpHeaders {
    return new HttpHeaders({ 'X-Tenant-Id': this.tenantId, 'Accept': 'application/json' });
  }

  list(params?: Record<string, any>): Observable<Page<FuncionarioResponse>> {
    let p = new HttpParams().set('empresaId', this.tenantId);
    if (params) Object.entries(params).forEach(([k, v]) => { if (k !== 'empresaId' && v != null) p = p.set(k, String(v)); });
    return this.withEmpresa(() => this.http.get<Page<FuncionarioResponse>>(this.base, { params: p, headers: this.headers }));
  }

  getById(id: number): Observable<FuncionarioResponse> {
    return this.withEmpresa(() => this.http.get<FuncionarioResponse>(`${this.base}/${id}`, { headers: this.headers }));
  }

  getByUsuarioId(usuarioId: number): Observable<FuncionarioResponse> {
    const params = new HttpParams().set('empresaId', this.tenantId);
    return this.withEmpresa(() => this.http.get<FuncionarioResponse>(`${this.base}/usuario/${usuarioId}`, { params, headers: this.headers }));
  }

  create(dto: FuncionarioRequest): Observable<FuncionarioResponse> {
    return this.withEmpresa(() => this.http.post<FuncionarioResponse>(this.base, { ...dto, empresaId: Number(this.tenantId) }, { headers: this.headers }));
  }

  update(id: number, dto: FuncionarioRequest): Observable<FuncionarioResponse> {
    return this.withEmpresa(() => this.http.put<FuncionarioResponse>(`${this.base}/${id}`, { ...dto, empresaId: Number(this.tenantId) }, { headers: this.headers }));
  }

  delete(id: number): Observable<void> {
    return this.withEmpresa(() => this.http.delete<void>(`${this.base}/${id}`, { headers: this.headers }));
  }

  history(id: number): Observable<{ content: Array<{ operacao: string; campos: string; valores: string; autor: number; data: string }> }> {
    return this.withEmpresa(() => this.http.get<{ content: Array<{ operacao: string; campos: string; valores: string; autor: number; data: string }> }>(
      `${this.base}/${id}/historico`, { params: { page: '0', size: '20' }, headers: this.headers }));
  }

  uploadFoto(id: number, file: File): Observable<FuncionarioResponse> {
    const formData = new FormData();
    formData.append('file', file);
    // Don't set Content-Type header for FormData, browser will set it with boundary
    const headers = new HttpHeaders({ 'X-Tenant-Id': this.tenantId, 'Accept': 'application/json' });
    return this.withEmpresa(() => this.http.post<FuncionarioResponse>(`${this.base}/${id}/foto`, formData, { headers }));
  }

  removerFoto(id: number): Observable<void> {
    return this.withEmpresa(() => this.http.delete<void>(`${this.base}/${id}/foto`, { headers: this.headers }));
  }
}
