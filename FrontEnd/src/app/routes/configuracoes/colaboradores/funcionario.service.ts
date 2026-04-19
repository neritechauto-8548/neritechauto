import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
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
  matricula: string;
  nomeCompleto: string;
  cpf: string;
  rg?: string;
  dataNascimento?: string;
  sexo?: string;
  estadoCivil?: string;
  nacionalidade?: string;
  naturalidade?: string;
  nomeMae?: string;
  nomePai?: string;
  escolaridade?: string;
  profissao?: string;
  cargoId?: number;
  departamentoId?: number;
  dataAdmissao: string;
  dataDemissao?: string;
  tipoContrato?: string;
  salarioBase?: number;
  comissaoPercentual?: number;
  valeTransporte?: number;
  valeAlimentacao?: number;
  planoSaude?: boolean;
  planoOdontologico?: boolean;
  status?: string;
  motivoInativacao?: string;
  enderecoCompleto?: string;
  telefonePrincipal?: string;
  telefoneEmergencia?: string;
  contatoEmergenciaNome?: string;
  contatoEmergenciaParentesco?: string;
  emailPessoal?: string;
  bancoCodigo?: string;
  bancoAgencia?: string;
  bancoConta?: string;
  bancoTipoConta?: string;
  pisPasep?: string;
  tituloEleitor?: string;
  carteiraTrabalho?: string;
  certificadoReservista?: string;
  cnhNumero?: string;
  cnhCategoria?: string;
  cnhValidade?: string;
  fotoFuncionarioUrl?: string;
  observacoes?: string;
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
  estadoCivil?: string;
  nacionalidade?: string;
  naturalidade?: string;
  nomeMae?: string;
  nomePai?: string;
  escolaridade?: string;
  profissao?: string;
  cargoId?: number;
  departamentoId?: number;
  dataAdmissao?: string;
  dataDemissao?: string;
  tipoContrato?: string;
  salarioBase?: number;
  comissaoPercentual?: number;
  valeTransporte?: number;
  valeAlimentacao?: number;
  planoSaude?: boolean;
  planoOdontologico?: boolean;
  status?: 'ATIVO' | 'INATIVO' | 'AFASTADO' | 'DEMITIDO' | 'APOSENTADO';
  motivoInativacao?: string;
  enderecoCompleto?: string;
  telefonePrincipal?: string;
  telefoneEmergencia?: string;
  contatoEmergenciaNome?: string;
  contatoEmergenciaParentesco?: string;
  emailPessoal?: string;
  bancoCodigo?: string;
  bancoAgencia?: string;
  bancoConta?: string;
  bancoTipoConta?: string;
  pisPasep?: string;
  tituloEleitor?: string;
  carteiraTrabalho?: string;
  certificadoReservista?: string;
  cnhNumero?: string;
  cnhCategoria?: string;
  cnhValidade?: string;
  fotoFuncionarioUrl?: string;
  observacoes?: string;
}

@Injectable({ providedIn: 'root' })
export class FuncionarioService {
  private readonly http    = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base    = environment.baseUrl + '/v1/rh/funcionarios';

  private get tenantId(): string {
    const v = this.storage.has('tenantId') ? (this.storage.get('tenantId') as any) : '7';
    return String(v && typeof v !== 'object' ? v : '7');
  }

  private get headers(): HttpHeaders {
    return new HttpHeaders({ 'X-Tenant-Id': this.tenantId, 'Accept': 'application/json' });
  }

  list(params?: Record<string, any>): Observable<Page<FuncionarioResponse>> {
    let p = new HttpParams().set('empresaId', this.tenantId);
    if (params) Object.entries(params).forEach(([k, v]) => { if (v != null) p = p.set(k, String(v)); });
    return this.http.get<Page<FuncionarioResponse>>(this.base, { params: p, headers: this.headers });
  }

  getById(id: number): Observable<FuncionarioResponse> {
    return this.http.get<FuncionarioResponse>(`${this.base}/${id}`, { headers: this.headers });
  }

  getByUsuarioId(usuarioId: number): Observable<FuncionarioResponse> {
    const params = new HttpParams().set('empresaId', this.tenantId);
    return this.http.get<FuncionarioResponse>(`${this.base}/usuario/${usuarioId}`, { params, headers: this.headers });
  }

  create(dto: FuncionarioRequest): Observable<FuncionarioResponse> {
    return this.http.post<FuncionarioResponse>(this.base, dto, { headers: this.headers });
  }

  update(id: number, dto: FuncionarioRequest): Observable<FuncionarioResponse> {
    return this.http.put<FuncionarioResponse>(`${this.base}/${id}`, dto, { headers: this.headers });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`, { headers: this.headers });
  }
}
