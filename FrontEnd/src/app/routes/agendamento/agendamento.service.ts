import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AgendamentoRequest {
  id?: number;
  clienteId: number;
  veiculoId?: number | null;
  tipoAgendamentoId?: number | null;
  dataAgendamento: string;
  horaInicio: string;
  horaFim: string;
  duracaoEstimadaMinutos?: number;
  servicosSolicitados?: string;
  problemaRelatado?: string;
  observacoesCliente?: string;
  observacoesInternas?: string;
  mecanicoPreferidoId?: number | null;
  mecanicoAlocadoId?: number | null;
  recursosNecessarios?: string;
  status: string;
  confirmadoCliente?: boolean;
  metodoConfirmacao?: string | null;
  valorEstimado?: number | null;
  formaPagamentoPreferidaId?: number | null;
  canalAgendamento: string;
}

export interface AgendamentoResponse {
  id: number;
  empresaId: number;
  numeroAgendamento: string;
  clienteId: number;
  clienteNome?: string;
  veiculoId?: number | null;
  placaVeiculo?: string;
  tipoAgendamentoId?: number | null;
  tipoAgendamentoNome?: string;
  dataAgendamento: string;
  horaInicio: string;
  horaFim: string;
  duracaoEstimadaMinutos: number;
  servicosSolicitados?: string;
  problemaRelatado?: string;
  observacoesCliente?: string;
  observacoesInternas?: string;
  mecanicoPreferidoId?: number | null;
  mecanicoAlocadoId?: number | null;
  recursosNecessarios?: string;
  status: string;
  confirmadoCliente?: boolean;
  dataConfirmacao?: string | null;
  metodoConfirmacao?: string | null;
  lembreteEnviado?: boolean;
  dataLembrete?: string | null;
  chegadaCliente?: string | null;
  inicioAtendimento?: string | null;
  fimAtendimento?: string | null;
  avaliacaoAtendimento?: number | null;
  comentarioAvaliacao?: string | null;
  ordemServicoGeradaId?: number | null;
  valorEstimado?: number | null;
  formaPagamentoPreferidaId?: number | null;
  canalAgendamento: string;
  dataCadastro?: string;
}

@Injectable({ providedIn: 'root' })
export class AgendamentoService {
  private readonly http = inject(HttpClient);
  private readonly api = `${environment.baseUrl}/v1/agendamentos`;

  listPorEmpresa(): Observable<AgendamentoResponse[]> {
    // Nome mantido por compatibilidade; o tenant é resolvido exclusivamente no backend.
    return this.http.get<AgendamentoResponse[]>(this.api);
  }

  getById(id: number): Observable<AgendamentoResponse> {
    return this.http.get<AgendamentoResponse>(`${this.api}/${id}`);
  }

  create(data: AgendamentoRequest): Observable<AgendamentoResponse> {
    return this.http.post<AgendamentoResponse>(this.api, data);
  }

  update(id: number, data: AgendamentoRequest): Observable<AgendamentoResponse> {
    return this.http.put<AgendamentoResponse>(`${this.api}/${id}`, data);
  }

  /** DELETE é compatibilidade HTTP; o backend cancela logicamente e preserva histórico. */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
