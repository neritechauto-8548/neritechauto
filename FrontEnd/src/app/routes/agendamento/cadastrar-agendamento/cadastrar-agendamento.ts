import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NgxPermissionsModule } from 'ngx-permissions';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { SkeletonModule } from 'primeng/skeleton';
import { TextareaModule } from 'primeng/textarea';
import { ToastModule } from 'primeng/toast';

import {
  AgendamentoRequest,
  AgendamentoService,
  AgendamentoVehicleSummary,
} from '../agendamento.service';
import { ClientesService } from '../../cliente/cliente/cliente.service';
import { StatusCliente } from '../../cliente/models/cliente.models';

@Component({
  selector: 'app-cadastrar-agendamento',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    InputTextModule,
    ButtonModule,
    ToastModule,
    MatIconModule,
    MatButtonModule,
    NgxPermissionsModule,
    SkeletonModule,
    SelectModule,
    TextareaModule,
  ],
  providers: [MessageService],
  templateUrl: './cadastrar-agendamento.html',
  styleUrls: ['./cadastrar-agendamento.scss'],
})
export class CadastrarAgendamento implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly messageService = inject(MessageService);
  private readonly agendamentoService = inject(AgendamentoService);
  private readonly clienteService = inject(ClientesService);

  titulo = 'Novo Agendamento';
  loading = false;
  idAgendamento: number | null = null;
  submitLoad = false;

  clientes: { label: string; value: number; status: StatusCliente }[] = [];
  veiculosFiltrados: { label: string; value: number }[] = [];

  readonly statusOptions = [
    { label: 'Agendado', value: 'AGENDADO' },
    { label: 'Confirmado', value: 'CONFIRMADO' },
    { label: 'Em Andamento', value: 'EM_ANDAMENTO' },
    { label: 'Concluído', value: 'CONCLUIDO' },
    { label: 'Cancelado', value: 'CANCELADO' },
    { label: 'Não Compareceu', value: 'NAO_COMPARECEU' },
    { label: 'Reagendado', value: 'REAGENDADO' },
  ];

  readonly canaisOptions = [
    { label: 'Telefone', value: 'TELEFONE' },
    { label: 'WhatsApp', value: 'WHATSAPP' },
    { label: 'Presencial', value: 'PRESENCIAL' },
    { label: 'Site', value: 'SITE' },
    { label: 'Indicação', value: 'INDICACAO' },
    { label: 'Aplicativo', value: 'APP' },
  ];

  agendamento: AgendamentoRequest = {
    clienteId: null as unknown as number,
    veiculoId: null,
    tipoAgendamentoId: null,
    dataAgendamento: '',
    horaInicio: '',
    horaFim: '',
    status: 'AGENDADO',
    canalAgendamento: 'PRESENCIAL',
    observacoesCliente: '',
    observacoesInternas: '',
    problemaRelatado: '',
  };

  dataSelecionada = '';
  hojeLimit = '';
  originalDataSelecionada = '';
  horaInicioSelecionada = '';
  horaFimSelecionada = '';

  ngOnInit(): void {
    const hoje = new Date();
    const y = hoje.getFullYear();
    const m = String(hoje.getMonth() + 1).padStart(2, '0');
    const d = String(hoje.getDate()).padStart(2, '0');
    this.hojeLimit = `${y}-${m}-${d}`;

    this.route.paramMap.subscribe(params => {
      const idStr = params.get('id');
      this.idAgendamento = idStr ? Number(idStr) : null;
      this.titulo = this.idAgendamento ? 'Editar Agendamento' : 'Novo Agendamento';

      this.carregarClientes(() => {
        if (this.idAgendamento) {
          this.carregarAgendamentoParaEdicao(this.idAgendamento);
          return;
        }

        const clienteId = Number(this.route.snapshot.queryParamMap.get('clienteId'));
        if (Number.isInteger(clienteId) && clienteId > 0 && this.clientes.some(c => c.value === clienteId)) {
          this.agendamento.clienteId = clienteId;
          this.onClienteSelecionado();
        }
      });
    });
  }

  carregarClientes(callback?: () => void): void {
    this.loading = true;
    this.clienteService.list({ page: 0, size: 100, status: StatusCliente.ATIVO }).subscribe({
      next: res => {
        this.clientes = (res.content || []).map(customer => ({
          label: customer.maskedTaxId
            ? `${customer.displayName} (${customer.maskedTaxId})`
            : customer.displayName,
          value: customer.id,
          status: customer.status,
        }));
        this.loading = false;
        callback?.();
      },
      error: () => {
        this.loading = false;
        this.clientes = [];
        this.messageService.add({
          severity: 'error',
          summary: 'Clientes indisponíveis',
          detail: 'Não foi possível carregar clientes autorizados para o agendamento.',
        });
      },
    });
  }

  onClienteSelecionado(callback?: () => void): void {
    const clienteId = Number(this.agendamento.clienteId);
    this.agendamento.veiculoId = null;
    this.veiculosFiltrados = [];

    if (!Number.isInteger(clienteId) || clienteId <= 0) {
      callback?.();
      return;
    }

    this.agendamentoService.listVehiclesForCustomer(clienteId).subscribe({
      next: vehicles => {
        this.veiculosFiltrados = vehicles
          .filter(vehicle => vehicle.status !== 'INATIVO')
          .map(vehicle => ({
            label: this.vehicleLabel(vehicle),
            value: vehicle.id,
          }));

        if (this.veiculosFiltrados.length === 1) {
          this.agendamento.veiculoId = this.veiculosFiltrados[0].value;
        }
        callback?.();
      },
      error: () => {
        this.veiculosFiltrados = [];
        callback?.();
        this.messageService.add({
          severity: 'info',
          summary: 'Veículos indisponíveis',
          detail: 'O cliente foi mantido; selecione o veículo depois quando a fonte estiver disponível.',
        });
      },
    });
  }

  carregarAgendamentoParaEdicao(id: number): void {
    this.loading = true;
    this.agendamentoService.getById(id).subscribe({
      next: res => {
        this.agendamento = {
          clienteId: res.clienteId,
          veiculoId: res.veiculoId ?? null,
          tipoAgendamentoId: res.tipoAgendamentoId ?? null,
          dataAgendamento: res.dataAgendamento,
          horaInicio: res.horaInicio,
          horaFim: res.horaFim,
          duracaoEstimadaMinutos: res.duracaoEstimadaMinutos,
          servicosSolicitados: res.servicosSolicitados,
          problemaRelatado: res.problemaRelatado,
          observacoesCliente: res.observacoesCliente,
          observacoesInternas: res.observacoesInternas,
          status: res.status,
          canalAgendamento: res.canalAgendamento,
        };

        this.dataSelecionada = res.dataAgendamento;
        this.originalDataSelecionada = res.dataAgendamento;
        this.horaInicioSelecionada = res.horaInicio?.substring(0, 5) || '';
        this.horaFimSelecionada = res.horaFim?.substring(0, 5) || '';

        const originalVehicleId = res.veiculoId ?? null;
        this.onClienteSelecionado(() => {
          this.agendamento.veiculoId = originalVehicleId;
          this.loading = false;
        });
      },
      error: () => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao buscar agendamento no contexto autenticado.' });
        this.cancelar();
      },
    });
  }

  cancelar(): void {
    this.router.navigate(['/agenda']);
  }

  salvar(): void {
    if (!this.agendamento.clienteId) return this.showError('Obrigatório', 'Selecione um cliente.');
    if (!this.dataSelecionada) return this.showError('Obrigatório', 'Informe a data do agendamento.');
    if (!this.horaInicioSelecionada) return this.showError('Obrigatório', 'Informe a hora inicial.');
    if (!this.horaFimSelecionada) return this.showError('Obrigatório', 'Informe a hora de término.');
    if (this.horaFimSelecionada <= this.horaInicioSelecionada) {
      return this.showError('Horário inválido', 'A hora de término deve ser posterior à hora de início.');
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const [year, month, day] = this.dataSelecionada.split('-').map(Number);
    const scheduledDate = new Date(year, month - 1, day);
    scheduledDate.setHours(0, 0, 0, 0);
    const dateChanged = !this.idAgendamento || this.dataSelecionada !== this.originalDataSelecionada;
    if (dateChanged && scheduledDate < today) {
      return this.showError('Data inválida', 'Não é permitido realizar agendamentos para datas retroativas.');
    }

    const startMinutes = this.timeToMinutes(this.horaInicioSelecionada);
    const endMinutes = this.timeToMinutes(this.horaFimSelecionada);

    const requestPayload: AgendamentoRequest = {
      clienteId: Number(this.agendamento.clienteId),
      veiculoId: this.agendamento.veiculoId ? Number(this.agendamento.veiculoId) : null,
      tipoAgendamentoId: this.agendamento.tipoAgendamentoId ? Number(this.agendamento.tipoAgendamentoId) : null,
      dataAgendamento: this.dataSelecionada,
      horaInicio: `${this.horaInicioSelecionada}:00`,
      horaFim: `${this.horaFimSelecionada}:00`,
      duracaoEstimadaMinutos: endMinutes - startMinutes,
      servicosSolicitados: this.clean(this.agendamento.servicosSolicitados),
      problemaRelatado: this.clean(this.agendamento.problemaRelatado),
      observacoesCliente: this.clean(this.agendamento.observacoesCliente),
      observacoesInternas: this.clean(this.agendamento.observacoesInternas),
      status: this.idAgendamento ? this.agendamento.status : 'AGENDADO',
      canalAgendamento: this.agendamento.canalAgendamento || 'PRESENCIAL',
    };

    this.submitLoad = true;
    const request$ = this.idAgendamento
      ? this.agendamentoService.update(this.idAgendamento, requestPayload)
      : this.agendamentoService.create(requestPayload);

    request$.subscribe({
      next: response => {
        this.submitLoad = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: this.idAgendamento ? 'Agendamento atualizado.' : 'Agendamento criado.',
        });

        if (!this.idAgendamento && response?.id) {
          this.idAgendamento = response.id;
          this.titulo = 'Editar Agendamento';
          this.router.navigate(['/agenda', response.id, 'editar'], { replaceUrl: true });
        }
      },
      error: err => this.handleError(err),
    });
  }

  excluirAgendamento(): void {
    if (!this.idAgendamento) return;
    if (!confirm('Cancelar este agendamento? O histórico será preservado.')) return;

    this.agendamentoService.delete(this.idAgendamento).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Cancelado', detail: 'Agendamento cancelado com histórico preservado.' });
        setTimeout(() => this.cancelar(), 600);
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível cancelar o agendamento.' }),
    });
  }

  private vehicleLabel(vehicle: AgendamentoVehicleSummary) {
    const name = [vehicle.marcaNome, vehicle.modeloNome].filter(Boolean).join(' ') || `Veículo #${vehicle.id}`;
    return [name, vehicle.maskedPlate, vehicle.anoModelo || vehicle.anoFabricacao].filter(Boolean).join(' · ');
  }

  private timeToMinutes(value: string) {
    const [hours, minutes] = value.split(':').map(Number);
    return hours * 60 + minutes;
  }

  private clean(value?: string) {
    const trimmed = (value || '').trim();
    return trimmed || undefined;
  }

  private handleError(err: any) {
    this.submitLoad = false;
    this.messageService.add({
      severity: 'error',
      summary: 'Não foi possível salvar',
      detail: err?.error?.message || 'Revise cliente, veículo, horário e tente novamente.',
    });
  }

  private showError(summary: string, detail: string) {
    this.messageService.add({ severity: 'warn', summary, detail });
  }
}
