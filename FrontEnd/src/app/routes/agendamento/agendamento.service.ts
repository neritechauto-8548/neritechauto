import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
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
  private previewAgendamentos: AgendamentoResponse[] = [
    this.preview(1, 'AGD-0001', 1, 1, '2026-10-07', '08:00:00', '09:30:00', 'CONFIRMADO', 'Revisão preventiva'),
    this.preview(2, 'AGD-0002', 2, 2, '2026-10-07', '09:30:00', '11:00:00', 'AGENDADO', 'Troca de óleo e filtros'),
    this.preview(3, 'AGD-0003', 3, 3, '2026-10-08', '08:30:00', '10:00:00', 'EM_ANDAMENTO', 'Diagnóstico de ruído'),
    this.preview(4, 'AGD-0004', 4, 4, '2026-10-08', '13:30:00', '15:00:00', 'CONFIRMADO', 'Alinhamento e balanceamento'),
    this.preview(5, 'AGD-0005', 5, 5, '2026-10-09', '10:00:00', '12:00:00', 'AGENDADO', 'Revisão dos 50.000 km'),
    this.preview(6, 'AGD-0006', 6, 6, '2026-10-10', '08:00:00', '09:00:00', 'AGENDADO', 'Troca de pastilhas'),
    this.preview(7, 'AGD-0007', 7, 7, '2026-10-12', '14:00:00', '16:30:00', 'CONFIRMADO', 'Inspeção completa'),
    this.preview(8, 'AGD-0008', 8, 8, '2026-10-13', '09:00:00', '10:30:00', 'PENDENTE', 'Avaliação do veículo'),
    this.preview(9, 'AGD-0009', 1, 1, '2026-10-15', '15:00:00', '16:00:00', 'CONCLUIDO', 'Retorno pós-serviço'),
    this.preview(10, 'AGD-0010', 2, 2, '2026-10-16', '11:00:00', '12:30:00', 'CANCELADO', 'Serviço cancelado pelo cliente'),
  ];

  private preview(
    id: number,
    numeroAgendamento: string,
    clienteId: number,
    veiculoId: number,
    dataAgendamento: string,
    horaInicio: string,
    horaFim: string,
    status: string,
    problemaRelatado: string
  ): AgendamentoResponse {
    return {
      id, empresaId: 1, numeroAgendamento, clienteId, veiculoId,
      tipoAgendamentoId: 1, tipoAgendamentoNome: 'Serviço na oficina',
      dataAgendamento, horaInicio, horaFim,
      duracaoEstimadaMinutos: 90, servicosSolicitados: problemaRelatado,
      problemaRelatado, observacoesCliente: '', observacoesInternas: '',
      mecanicoPreferidoId: 0, mecanicoAlocadoId: 0, recursosNecessarios: '',
      status, confirmadoCliente: status === 'CONFIRMADO',
      dataConfirmacao: '', metodoConfirmacao: '', lembreteEnviado: false, dataLembrete: '',
      chegadaCliente: '', inicioAtendimento: '', fimAtendimento: '',
      avaliacaoAtendimento: 0, comentarioAvaliacao: '', ordemServicoGeradaId: 0,
      valorEstimado: 0, formaPagamentoPreferidaId: 0, canalAgendamento: 'WHATSAPP',
      dataCadastro: '2026-10-01T10:00:00'
    };
  }

  private http = inject(HttpClient);
  private storage = inject(LocalStorageService);
  private api = `${environment.baseUrl}/v1/agendamentos`;

  get tenantId(): number {
    return this.storage.get('tenantId') || 1;
  }

  listPorEmpresa(): Observable<AgendamentoResponse[]> {
    if (environment.uxPreview) return of([...this.previewAgendamentos]);
    return this.http.get<AgendamentoResponse[]>(`${this.api}/empresa/${this.tenantId}`);
  }

  getById(id: number): Observable<AgendamentoResponse> {
    if (environment.uxPreview) {
      const item = this.previewAgendamentos.find(a => a.id === id);
      return of(item ?? this.preview(999, 'AGD-0999', 1, 1, '2026-10-20', '08:00:00', '09:00:00', 'AGENDADO', 'Novo atendimento'));
    }
    return this.http.get<AgendamentoResponse>(`${this.api}/${id}`);
  }

  create(data: AgendamentoRequest): Observable<AgendamentoResponse> {
    data.empresaId = this.tenantId;
    if (environment.uxPreview) {
      const id = Math.max(...this.previewAgendamentos.map(a => a.id), 0) + 1;
      const created = this.preview(
        id, `AGD-${String(id).padStart(4, '0')}`,
        Number(data.clienteId), Number(data.veiculoId),
        data.dataAgendamento, data.horaInicio, data.horaFim || data.horaInicio,
        data.status || 'AGENDADO', data.problemaRelatado || 'Atendimento'
      );
      this.previewAgendamentos.push(created);
      return of(created);
    }
    return this.http.post<AgendamentoResponse>(this.api, data);
  }

  update(id: number, data: AgendamentoRequest): Observable<AgendamentoResponse> {
    data.empresaId = this.tenantId;
    if (environment.uxPreview) {
      const index = this.previewAgendamentos.findIndex(a => a.id === id);
      const current = index >= 0 ? this.previewAgendamentos[index] : this.preview( id, `AGD-${String(id).padStart(4, '0')}`, Number(data.clienteId), Number(data.veiculoId), data.dataAgendamento, data.horaInicio, data.horaFim || data.horaInicio, data.status || 'AGENDADO', data.problemaRelatado || 'Atendimento');
      const updated = { ...current, ...data, id, clienteId: Number(data.clienteId), veiculoId: Number(data.veiculoId) } as AgendamentoResponse;
      if (index >= 0) this.previewAgendamentos[index] = updated;
      else this.previewAgendamentos.push(updated);
      return of(updated);
    }
    return this.http.put<AgendamentoResponse>(`${this.api}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    if (environment.uxPreview) {
      this.previewAgendamentos = this.previewAgendamentos.filter(a => a.id !== id);
      return of(void 0);
    }
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
