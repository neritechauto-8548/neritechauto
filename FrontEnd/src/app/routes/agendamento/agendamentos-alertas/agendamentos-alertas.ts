import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

// PrimeNG & Material Components
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';

import { AgendamentoService, AgendamentoResponse } from '../agendamento.service';
import { ClientesService } from '../../cliente/cliente/cliente.service';
import { ComunicacaoService, ComunicacaoEnviadaRequest } from '../comunicacao.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-agendamentos-alertas',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DialogModule,
    InputTextModule,
    ButtonModule,
    ToastModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    RouterModule
  ],
  providers: [MessageService],
  templateUrl: './agendamentos-alertas.html',
  styleUrls: ['./agendamentos-alertas.scss'],
})
export class AgendamentosAlertas implements OnInit {
  private router = inject(Router);
  private agendamentoService = inject(AgendamentoService);
  private clienteService = inject(ClientesService);
  private comunicacaoService = inject(ComunicacaoService);
  private messageService = inject(MessageService);

  agendamentos: AgendamentoResponse[] = [];
  loading = false;
  searchTerm = '';
  // trigger web pack
  _triggerCache = 1;

  // Paginação Frontend
  rows = 10;
  first = 0;

  get totalRecords() {
    return this.filtered.length;
  }

  get rangeStart() {
    return this.totalRecords === 0 ? 0 : this.first + 1;
  }

  get rangeEnd() {
    return Math.min(this.first + this.rows, this.totalRecords);
  }

  ngOnInit(): void {
    this.carregarDados();
  }

  carregarDados(): void {
    this.loading = true;

    forkJoin({
      agendamentos: this.agendamentoService.listPorEmpresa(),
      clientes: this.clienteService.list({})
    }).subscribe({
      next: (res: any) => {
        let list: AgendamentoResponse[] = [];
        const agendResponse = res.agendamentos;
        const cliResponse = res.clientes;

        if (Array.isArray(agendResponse)) list = agendResponse;
        else if (agendResponse && Array.isArray(agendResponse.content)) list = agendResponse.content;
        else if (agendResponse && Array.isArray(agendResponse.items)) list = agendResponse.items;

        const listClientes = cliResponse.content || cliResponse || [];

        // Mapear ClienteNome
        list.forEach(a => {
          const cli: any = listClientes.find((c: any) => c.id === a.clienteId);
          a.clienteNome = cli ? (cli.nome || cli.nomeCompleto || cli.nomeFantasia || cli.razaoSocial || 'Cliente sem nome') : 'Cliente não encontrado';
        });

        this.agendamentos = list;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao buscar agendamentos:', err);
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível carregar os agendamentos.' });
        this.loading = false;
      }
    });
  }

  // Filtragem
  get filtered() {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) return this.agendamentos || [];

    return this.agendamentos.filter(a =>
      (a.clienteNome || '').toLowerCase().includes(term) ||
      (a.numeroAgendamento || '').toLowerCase().includes(term) ||
      (a.tipoAgendamentoNome || '').toLowerCase().includes(term)
    );
  }

  get pagedData() {
    const data = this.filtered;
    return data.slice(this.first, this.first + this.rows);
  }

  onSearch() {
    this.first = 0;
  }

  goPrev() {
    this.first = Math.max(0, this.first - this.rows);
  }

  goNext() {
    if (this.first + this.rows < this.totalRecords) {
      this.first += this.rows;
    }
  }

  // Menu de Tabela Actions
  currentRecord: AgendamentoResponse | null = null;
  setCurrent(record: AgendamentoResponse) {
    this.currentRecord = record;
  }

  // Ações Principais
  criarAgendamento(): void {
    this.router.navigate(['/agendamento/cadastro']);
  }

  abrirCalendario(): void {
    this.router.navigate(['/agendamento/calendario']);
  }

  editar(agendamento: AgendamentoResponse): void {
     // Usamos matrix parameters para manter a mesma rota base mas passando ID (Route: /agendamento/cadastro;id=1)
     this.router.navigate(['/agendamento/cadastro', { id: agendamento.id }]);
  }

  deletar(agendamento: AgendamentoResponse): void {
    if (confirm(`Atenção! Você deseja mesmo cancelar o Agendamento de ${agendamento.clienteNome}?`)) {
      this.agendamentoService.delete(agendamento.id).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Agendamento cancelado com sucesso.' });
          this.carregarDados();
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao cancelar o agendamento.' });
        }
      });
    }
  }

  // Dialog Criar Alerta
  mostrarDialogCriarAlerta = false;
  salvandoAlerta = false;
  alertaRecord: AgendamentoResponse | null = null;
  alertaNome = '';
  alertaEnviarPorOptions = ['EMAIL', 'SMS', 'WHATSAPP'];
  alertaEnviarPor = 'EMAIL';
  alertaAssunto = '';
  alertaEmail = '';
  alertaCelular = '';
  alertaMensagem = '';

  criarAlerta(): void {
    this.abrirDialogAlertaUnico();
  }

  abrirDialogAlertaUnico(record?: AgendamentoResponse): void {
    this.mostrarDialogCriarAlerta = true;
    this.alertaRecord = record || null;

    // Reset/Prefill com o agendamento selecionado ou vazio
    this.alertaNome = record?.clienteNome || '';
    this.alertaEnviarPor = 'EMAIL';
    this.alertaAssunto = `Lembrete de Agendamento: ${record?.tipoAgendamentoNome || 'Serviço'}`;
    this.alertaEmail = '';  // se backend mandasse email ppreencheriamos
    this.alertaCelular = '';

    if (record) {
      this.alertaMensagem = `Olá ${record?.clienteNome || ''},\n\nEste é um lembrete do seu agendamento (${record?.tipoAgendamentoNome}) conosco marcado para o dia ${this.formatDate(record?.dataAgendamento)} às ${record?.horaInicio}.\nPor favor, nos confirme se poderá comparecer.\n\nAtenciosamente,\nSua Oficina.`;

      // Buscar contatos ativamente do cadastro do Cliente
      this.clienteService.getById(record.clienteId).subscribe({
         next: (cliente: any) => {
            if (cliente) {
               // Preenche com o telefone ou email que vieram da API
               this.alertaCelular = cliente.telefoneVoz || cliente.telefoneWhatsapp || '';
               this.alertaEmail = cliente.emailPrincipal || cliente.emailSecundario || '';
            }
         },
         error: (err: any) => console.error('Aviso: Falha ao carregar metadados do cliente para o Alerta', err)
      });
    } else {
      this.alertaMensagem = 'Olá\n\nVocê possui um agendamento marcado conosco.\nPor favor, entre em contato para confirmar.\n\nAtenciosamente,\nSua Oficina.';
    }
  }

  cancelarCriarAlerta(): void {
    this.mostrarDialogCriarAlerta = false;
  }

  enviarAlerta(): void {
    // Validações básicas
    if (this.alertaEnviarPor === 'EMAIL' && !this.alertaEmail) {
       this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Por favor, informe um Email de destino adequado.' });
       return;
    }
    if ((this.alertaEnviarPor === 'SMS' || this.alertaEnviarPor === 'WHATSAPP') && !this.alertaCelular) {
       this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Por favor, informe um Celular de destino adequado.' });
       return;
    }
    if (!this.alertaMensagem) {
       this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'A mensagem não pode estar vazia.' });
       return;
    }

    this.salvandoAlerta = true;

    const payload: ComunicacaoEnviadaRequest = {
      empresaId: 1, // injetado dinamicamente internamente no service
      tipoComunicacao: this.alertaEnviarPor,
      destinatarioTipo: 'CLIENTE',
      destinatarioId: this.alertaRecord?.clienteId || 0,
      destinatarioNome: this.alertaNome || 'N/A',
      destinatarioContato: this.alertaEnviarPor === 'EMAIL' ? this.alertaEmail : this.alertaCelular,
      assunto: this.alertaEnviarPor === 'EMAIL' ? this.alertaAssunto : undefined,
      conteudo: this.alertaMensagem,
      agendamentoId: this.alertaRecord?.id, // Vincula a comunicacao c o agendamento!
      status: 'PENDENTE',
      automatica: false
    };

    this.comunicacaoService.enviarComunicacao(payload).subscribe({
      next: () => {
         this.salvandoAlerta = false;
         this.messageService.add({ severity: 'success', summary: 'Enviado', detail: `O Alerta via ${this.alertaEnviarPor} foi disparado com sucesso!` });
         this.mostrarDialogCriarAlerta = false;
      },
      error: (err) => {
         this.salvandoAlerta = false;
         console.error('Falha de envio', err);
         this.messageService.add({ severity: 'error', summary: 'Erro de Envio', detail: 'Ocorreu um problema ao enviar o alerta. Verifique os dados ou contate o suporte.' });
      }
    });
  }

  // UX Helpers
  formatDate(dateStr: string): string {
    if (!dateStr) return 'Não Definido';
    const d = new Date(dateStr);
    // Para resolver fuso: '2025-11-20T00:00:00'
    const dia = String(d.getDate() + 1).padStart(2,'0'); // hack tz se nao vier hh:mm
    const mes = String(d.getMonth() + 1).padStart(2,'0');
    return `${dia}/${mes}/${d.getFullYear()}`;
  }

  getStatusBadgeClass(status: string | undefined): string {
    if (!status) return 'bg-gray-100 text-gray-700 ring-gray-600/20';

    switch (status.toUpperCase()) {
      case 'AGENDADO': return 'bg-yellow-100 text-yellow-700 ring-yellow-600/20';
      case 'CONFIRMADO': return 'bg-blue-100 text-blue-700 ring-blue-600/20';
      case 'EM_ANDAMENTO': return 'bg-indigo-100 text-indigo-700 ring-indigo-600/20';
      case 'CONCLUIDO': return 'bg-green-100 text-green-700 ring-green-600/20';
      case 'REAGENDADO': return 'bg-orange-100 text-orange-700 ring-orange-600/20';
      case 'CANCELADO':
      case 'NAO_COMPARECEU': return 'bg-red-100 text-red-700 ring-red-600/20';
      default: return 'bg-gray-100 text-gray-700 ring-gray-600/20';
    }
  }
}
