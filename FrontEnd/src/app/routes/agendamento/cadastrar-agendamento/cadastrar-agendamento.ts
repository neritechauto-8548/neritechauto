import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';

// PrimeNG
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

// Material Icons
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

// Serviços
import { AgendamentoService, AgendamentoRequest, AgendamentoResponse } from '../agendamento.service';
import { ClientesService } from '../../cliente/cliente/cliente.service';
import { VeiculoService } from '../../veiculo/veiculo/veiculo.service';
import { ClienteResponse } from '../../cliente/models/cliente.models';
import { VeiculoResponse } from '../../veiculo/models/veiculo.models';

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
    MatButtonModule
  ],
  providers: [MessageService],
  templateUrl: './cadastrar-agendamento.html',
  styleUrls: ['./cadastrar-agendamento.scss'],
})
export class CadastrarAgendamento implements OnInit {
  // Injections
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private messageService = inject(MessageService);
  private agendamentoService = inject(AgendamentoService);
  private clienteService = inject(ClientesService);
  private veiculoService = inject(VeiculoService);

  // States
  titulo = 'Novo Agendamento';
  loading = false;
  idAgendamento: number | null = null;
  submitLoad = false;

  // Dropdowns Lists
  clientes: any[] = [];
  veiculos: any[] = [];
  veiculosFiltrados: any[] = [];

  // Options
  statusOptions = [
    { label: 'Agendado', value: 'AGENDADO' },
    { label: 'Confirmado', value: 'CONFIRMADO' },
    { label: 'Em Andamento', value: 'EM_ANDAMENTO' },
    { label: 'Concluído', value: 'CONCLUIDO' },
    { label: 'Cancelado', value: 'CANCELADO' },
    { label: 'Não Compareceu', value: 'NAO_COMPARECEU' },
    { label: 'Reagendado', value: 'REAGENDADO' }
  ];

  canaisOptions = [
    { label: 'Telefone', value: 'TELEFONE' },
    { label: 'WhatsApp', value: 'WHATSAPP' },
    { label: 'Presencial', value: 'PRESENCIAL' },
    { label: 'Site', value: 'SITE' },
    { label: 'Indicação', value: 'INDICACAO' },
    { label: 'Aplicativo', value: 'APP' }
  ];

  // Forms
  agendamento: AgendamentoRequest = {
    empresaId: 1, // Sera sobrescrito pelo getTenantId no Service
    clienteId: null as any,
    veiculoId: null as any,
    tipoAgendamentoId: null as any, // Nullable no DTO (pode ser hardcoded placeholder depois)
    dataAgendamento: '',
    horaInicio: '',
    horaFim: '',
    status: 'AGENDADO',
    canalAgendamento: 'PRESENCIAL',
    observacoesCliente: '',
    observacoesInternas: '',
    problemaRelatado: ''
  };

  // UI Helpers
  dataSelecionada: string = '';
  horaInicioSelecionada: string = '';
  horaFimSelecionada: string = '';

  ngOnInit(): void {
    // Pegar ID da URL se for edição
    this.route.paramMap.subscribe(params => {
      const idStr = params.get('id');
      if (idStr) {
        this.idAgendamento = Number(idStr);
        this.titulo = 'Editar Agendamento';
      }
      this.carregarListas(() => {
        if (this.idAgendamento) {
          this.carregarAgendamentoParaEdicao(this.idAgendamento);
        }
      });
    });
  }

  carregarListas(callback?: () => void): void {
    this.loading = true;

    // Busca clientes para o Dropdown
    this.clienteService.list({}).subscribe({
      next: (res: any) => {
        const list = res.content || res;
        this.clientes = list.map((c: any) => {
           let label = c.nome || c.nomeCompleto || c.nomeFantasia || c.razaoSocial || 'Cliente sem nome';
           if (c.cpfCnpj) label += ` (${c.cpfCnpj})`;
           return { label, value: c.id };
        });

        // Busca veiculos
        this.veiculoService.list().subscribe({
          next: (resV: any) => {
             const listV = resV.content || resV;
             this.veiculos = listV;
             this.loading = false;
             if (callback) callback();
          },
          error: () => this.loading = false
        });
      },
      error: () => this.loading = false
    });
  }

  onClienteSelecionado(): void {
    // Filtra os veículos do dropdown dependendo de qual cliente foi escolhido
    this.agendamento.veiculoId = null as any;
    if (this.agendamento.clienteId) {
      this.veiculosFiltrados = this.veiculos
        .filter(v => Number(v.clienteId) === Number(this.agendamento.clienteId))
        .map(v => ({
           label: `${v.placa} - ${v.modeloNome || v.chassi || 'S/ Modelo'}`,
           value: v.id
        }));

      // Se o cliente possuir apenas UM veículo cadastrado, autoseleciona.
      if (this.veiculosFiltrados.length === 1) {
          this.agendamento.veiculoId = this.veiculosFiltrados[0].value;
      }
    } else {
      this.veiculosFiltrados = [];
    }
  }

  carregarAgendamentoParaEdicao(id: number): void {
    this.loading = true;
    this.agendamentoService.getById(id).subscribe({
      next: (res) => {
        this.agendamento = { ...this.agendamento, ...res };

        // Setar vars de string p/ binding via nativo HTML5 datetime
        if (res.dataAgendamento) {
           this.dataSelecionada = res.dataAgendamento; // Format '2026-03-01'
        }
        if (res.horaInicio) {
           this.horaInicioSelecionada = res.horaInicio.substring(0, 5); // Format 'HH:mm'
        }
        if (res.horaFim) {
           this.horaFimSelecionada = res.horaFim.substring(0, 5); // Format 'HH:mm'
        }

        // Simular evento do dropdown pra carregar os carros filhos
        this.onClienteSelecionado();

        // Recuperar o ID do veiculo apos filtrar
        this.agendamento.veiculoId = res.veiculoId;

        this.loading = false;
      },
      error: (err) => {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Falha ao buscar agendamento.'});
        this.router.navigate(['/agendamento/agendamentos-alertas']); // voltar
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/agendamento/agendamentos-alertas']);
  }

  salvar(): void {
    // Validações simples
    if (!this.agendamento.clienteId) return this.showError('Obrigatório', 'Selecione um Cliente.');
    if (!this.dataSelecionada) return this.showError('Obrigatório', 'Informe a Data do Agendamento.');
    if (!this.horaInicioSelecionada) return this.showError('Obrigatório', 'Informe a Hora Inicial.');

    this.submitLoad = true;

    // Constrói payload estrito apenas com os campos que a API Java espera (evita erro Unrecognized field)
    const requestPayload: AgendamentoRequest = {
      empresaId: this.agendamento.empresaId,
      clienteId: Number(this.agendamento.clienteId),
      veiculoId: Number(this.agendamento.veiculoId),
      tipoAgendamentoId: this.agendamento.tipoAgendamentoId ? Number(this.agendamento.tipoAgendamentoId) : undefined,
      dataAgendamento: this.dataSelecionada,
      horaInicio: `${this.horaInicioSelecionada}:00`,
      horaFim: this.horaFimSelecionada ? `${this.horaFimSelecionada}:00` : undefined,
      duracaoEstimadaMinutos: this.agendamento.duracaoEstimadaMinutos,
      servicosSolicitados: this.agendamento.servicosSolicitados,
      problemaRelatado: this.agendamento.problemaRelatado,
      observacoesCliente: this.agendamento.observacoesCliente,
      observacoesInternas: this.agendamento.observacoesInternas,
      status: this.agendamento.status,
      canalAgendamento: this.agendamento.canalAgendamento
    };

    // Remove propriedades undefined para um JSON limpo
    Object.keys(requestPayload).forEach(key => {
      if ((requestPayload as any)[key] === undefined) {
        delete (requestPayload as any)[key];
      }
    });

    if (this.idAgendamento) {
       this.agendamentoService.update(this.idAgendamento, requestPayload).subscribe({
         next: () => this.handleSuccess('Agendamento Atualizado!'),
         error: err => this.handleError(err)
       });
    } else {
       this.agendamentoService.create(requestPayload).subscribe({
         next: (res: any) => {
            if (res && res.id) {
               this.idAgendamento = res.id;
               this.titulo = 'Editar Agendamento';
               // Adiciona o ID na URL para nao perder num reload
               this.router.navigate(['/agendamento/cadastro', { id: res.id }]);
            }
            this.handleSuccess('Agendamento Criado!');
         },
         error: err => this.handleError(err)
       });
    }
  }

  excluirAgendamento(): void {
    if (!this.idAgendamento) return;
    if (confirm('Atenção: Tem certeza que deseja excluir definitivamente este Agendamento?')) {
       this.agendamentoService.delete(this.idAgendamento).subscribe({
          next: () => {
             this.messageService.add({severity:'success', summary: 'Removido', detail: 'Registro excluído com sucesso!'});
             setTimeout(() => this.cancelar(), 1200); // Voltar pra lista após excluir
          },
          error: () => this.messageService.add({severity:'error', summary: 'Erro', detail: 'Houve uma falha ao tentar excluir.'})
       });
    }
  }

  // --- HELPERS ---
  private formatDateOnly(d: Date): string {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${day}`;
  }

  private formatTimeOnly(d: Date): string {
    const h = String(d.getHours()).padStart(2, '0');
    const m = String(d.getMinutes()).padStart(2, '0');
    return `${h}:${m}:00`; // Segundos default 00 backendsafe
  }

  private handleSuccess(msg: string) {
    this.submitLoad = false;
    this.messageService.add({severity:'success', summary: 'Sucesso', detail: msg});
  }

  private handleError(err: any) {
    this.submitLoad = false;
    console.error(err);
    this.messageService.add({severity:'error', summary: 'Erro', detail: 'Ocorreu um problema ao salvar. Verifique se o Cliente/Veiculo ja não estão agendados nessa data ou a conexao com servidor.'});
  }

  private showError(sum: string, det: string) {
    this.messageService.add({severity: 'warn', summary: sum, detail: det});
  }
}
