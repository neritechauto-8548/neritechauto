import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LocalStorageService } from '@shared/services/storage.service';

export interface AgendamentoRequest {
  id?: number;
  empresaId: number;
  clienteId: number;
  veiculoId: number;
  tipoAgendamentoId?: number;
  dataAgendamento: string; // YYYY-MM-DD
  horaInicio: string; // HH:mm:ss
  horaFim?: string; // HH:mm:ss
  duracaoEstimadaMinutos?: number;
  servicosSolicitados?: string;
  problemaRelatado?: string;
  observacoesCliente?: string;
  observacoesInternas?: string;
  status: string;
  canalAgendamento?: string;
}

export interface AgendamentoResponse {
  id: number;
  empresaId: number;
  numeroAgendamento: string;
  clienteId: number;
  clienteNome?: string; // Mapeado no frontend as vezes
  veiculoId: number;
  placaVeiculo?: string; // Mapeado no frontend as vezes
  tipoAgendamentoId: number;
  tipoAgendamentoNome: string;
  dataAgendamento: string;
  horaInicio: string;
  horaFim: string;
  duracaoEstimadaMinutos: number;
  servicosSolicitados: string;
  problemaRelatado: string;
  observacoesCliente: string;
  observacoesInternas: string;
  mecanicoPreferidoId: number;
  mecanicoAlocadoId: number;
  recursosNecessarios: string;
  status: string; // PENDENTE, CONFIRMADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO, NO_SHOW
  confirmadoCliente: boolean;
  dataConfirmacao: string;
  metodoConfirmacao: string;
  lembreteEnviado: boolean;
  dataLembrete: string;
  chegadaCliente: string;
  inicioAtendimento: string;
  fimAtendimento: string;
  avaliacaoAtendimento: number;
  comentarioAvaliacao: string;
  ordemServicoGeradaId: number;
  valorEstimado: number;
  formaPagamentoPreferidaId: number;
  canalAgendamento: string;
  dataCadastro: string;
}

@Injectable({
  providedIn: 'root',
})
export class AgendamentoService {
  private http = inject(HttpClient);
  private storage = inject(LocalStorageService);
  private api = `${environment.baseUrl}/v1/agendamentos`;

  get tenantId(): number {
    return this.storage.get('tenantId') || 1;
  }

  listPorEmpresa(): Observable<AgendamentoResponse[]> {
    return this.http.get<AgendamentoResponse[]>(`${this.api}/empresa/${this.tenantId}`);
  }

  getById(id: number): Observable<AgendamentoResponse> {
    return this.http.get<AgendamentoResponse>(`${this.api}/${id}`);
  }

  create(data: AgendamentoRequest): Observable<AgendamentoResponse> {
    data.empresaId = this.tenantId;
    return this.http.post<AgendamentoResponse>(this.api, data);
  }

  update(id: number, data: AgendamentoRequest): Observable<AgendamentoResponse> {
    data.empresaId = this.tenantId;
    return this.http.put<AgendamentoResponse>(`${this.api}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
