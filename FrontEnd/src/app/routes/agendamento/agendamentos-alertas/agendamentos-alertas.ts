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
import { SkeletonModule } from 'primeng/skeleton';
import { NgxPermissionsModule } from 'ngx-permissions';

import { AgendamentoService, AgendamentoResponse } from '../agendamento.service';
import { ClientesService } from '../../cliente/cliente/cliente.service';
import { ComunicacaoService, ComunicacaoEnviadaRequest } from '../comunicacao.service';
import { EmpresaService } from '../../configuracoes/empresa/services/empresa.service';
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
    RouterModule,
    SkeletonModule,
    NgxPermissionsModule
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
  private empresaService = inject(EmpresaService);

  agendamentos: AgendamentoResponse[] = [];
  loading = false;
  searchTerm = '';
  // trigger web pack
  _triggerCache = 1;

  oficinaNome = 'Sua Oficina';

  // Filtros de Período
  filtroPeriodo: 'todos' | 'dia' | 'semana' | 'mes' | 'custom' = 'todos';
  dataFiltroInicio = '';
  dataFiltroFim = '';

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

  get totalHoje(): number {
    const hojeStr = this.formatDateYYYYMMDD(new Date());
    return this.agendamentos.filter(a => a.dataAgendamento && a.dataAgendamento.substring(0, 10) === hojeStr).length;
  }

  get totalSemana(): number {
    const hoje = new Date();
    const startOfWeek = new Date(hoje);
    startOfWeek.setDate(hoje.getDate() - hoje.getDay()); // Sunday as first day
    startOfWeek.setHours(0,0,0,0);

    const endOfWeek = new Date(startOfWeek);
    endOfWeek.setDate(startOfWeek.getDate() + 6); // Saturday as last day
    endOfWeek.setHours(23,59,59,999);

    return this.agendamentos.filter(a => {
      if (!a.dataAgendamento) return false;
      const cleanDateStr = a.dataAgendamento.substring(0, 10);
      const parts = cleanDateStr.split('-');
      const dataA = new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
      return dataA >= startOfWeek && dataA <= endOfWeek;
    }).length;
  }

  get totalPendentes(): number {
    return this.agendamentos.filter(a => a.status === 'AGENDADO' || a.status === 'PENDENTE').length;
  }

  get totalConfirmados(): number {
    return this.agendamentos.filter(a => a.status === 'CONFIRMADO' || a.status === 'CONCLUIDO').length;
  }

  ngOnInit(): void {
    this.carregarDados();
    this.carregarNomeOficina();
  }

  carregarNomeOficina(): void {
    const idEmpresa = this.comunicacaoService.tenantId;
    this.empresaService.getEmpresa(idEmpresa).subscribe({
      next: (emp) => {
        if (emp && (emp.nomeFantasia || emp.razaoSocial)) {
          this.oficinaNome = emp.nomeFantasia || emp.razaoSocial;
        }
      },
      error: (err) => console.error('Erro ao buscar dados da empresa', err)
    });
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
    let list = this.agendamentos || [];

    // 1. Filtrar pelo período
    const hoje = new Date();
    hoje.setHours(0,0,0,0);

    if (this.filtroPeriodo === 'dia') {
      const hojeStr = this.formatDateYYYYMMDD(hoje);
      list = list.filter(a => a.dataAgendamento && a.dataAgendamento.substring(0, 10) === hojeStr);
    } else if (this.filtroPeriodo === 'semana') {
      const startOfWeek = new Date(hoje);
      startOfWeek.setDate(hoje.getDate() - hoje.getDay()); // Sunday as first day
      startOfWeek.setHours(0,0,0,0);

      const endOfWeek = new Date(startOfWeek);
      endOfWeek.setDate(startOfWeek.getDate() + 6); // Saturday as last day
      endOfWeek.setHours(23,59,59,999);

      list = list.filter(a => {
        if (!a.dataAgendamento) return false;
        const cleanDateStr = a.dataAgendamento.substring(0, 10);
        const parts = cleanDateStr.split('-');
        const dataA = new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
        return dataA >= startOfWeek && dataA <= endOfWeek;
      });
    } else if (this.filtroPeriodo === 'mes') {
      const mesAtual = hoje.getMonth();
      const anoAtual = hoje.getFullYear();
      list = list.filter(a => {
        if (!a.dataAgendamento) return false;
        const cleanDateStr = a.dataAgendamento.substring(0, 10);
        const parts = cleanDateStr.split('-');
        return Number(parts[0]) === anoAtual && (Number(parts[1]) - 1) === mesAtual;
      });
    } else if (this.filtroPeriodo === 'custom') {
      if (this.dataFiltroInicio) {
        const startParts = this.dataFiltroInicio.split('-');
        const startLimit = new Date(Number(startParts[0]), Number(startParts[1]) - 1, Number(startParts[2]));
        startLimit.setHours(0,0,0,0);
        list = list.filter(a => {
          if (!a.dataAgendamento) return false;
          const cleanDateStr = a.dataAgendamento.substring(0, 10);
          const parts = cleanDateStr.split('-');
          const dataA = new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
          return dataA >= startLimit;
        });
      }
      if (this.dataFiltroFim) {
        const endParts = this.dataFiltroFim.split('-');
        const endLimit = new Date(Number(endParts[0]), Number(endParts[1]) - 1, Number(endParts[2]));
        endLimit.setHours(23,59,59,999);
        list = list.filter(a => {
          if (!a.dataAgendamento) return false;
          const cleanDateStr = a.dataAgendamento.substring(0, 10);
          const parts = cleanDateStr.split('-');
          const dataA = new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
          return dataA <= endLimit;
        });
      }
    }

    const term = this.searchTerm.trim().toLowerCase();
    if (!term) return list;

    return list.filter(a =>
      (a.clienteNome || '').toLowerCase().includes(term) ||
      (a.numeroAgendamento || '').toLowerCase().includes(term) ||
      (a.tipoAgendamentoNome || '').toLowerCase().includes(term)
    );
  }

  private formatDateYYYYMMDD(d: Date): string {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${day}`;
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

  criarOsParaAgendamento(agendamento: AgendamentoResponse): void {
    if (agendamento.veiculoId) {
      this.router.navigate(['/os/cadastro'], { queryParams: { veiculoId: agendamento.veiculoId } });
    } else {
      this.router.navigate(['/os/cadastro'], { queryParams: { clienteId: agendamento.clienteId } });
    }
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
  alertaEnviarPorOptions = ['EMAIL', 'WHATSAPP'];
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
    this.alertaEmail = '';
    this.alertaCelular = '';

    if (record) {
      const tipoServico = record?.tipoAgendamentoNome ? `de ${record.tipoAgendamentoNome}` : 'de serviço';
      this.alertaMensagem = `Olá ${record?.clienteNome || ''},\n\nEste é um lembrete do seu agendamento ${tipoServico} conosco marcado para o dia ${this.formatDate(record?.dataAgendamento)} às ${record?.horaInicio}.\nPor favor, nos confirme se poderá comparecer.\n\nAtenciosamente,\n${this.oficinaNome}.`;

      // Buscar contatos ativamente do cadastro do Cliente
      this.clienteService.getById(record.clienteId).subscribe({
         next: (cliente: any) => {
            if (cliente) {
               this.alertaEmail = cliente.email || '';
            }
            // Buscar contatos telefônicos na sub-tabela de contatos
            this.clienteService.listarContatos(record.clienteId).subscribe({
               next: (contactsRes: any) => {
                  const contatos = contactsRes.content || [];
                  const wppOrCell = contatos.find((c: any) => c.tipoContato === 'WHATSAPP' || c.tipoContato === 'CELULAR');
                  if (wppOrCell) {
                     this.alertaCelular = wppOrCell.contato;
                  } else {
                     const fallbackCell = contatos.find((c: any) => c.tipoContato === 'TELEFONE_FIXO' || c.contato);
                     this.alertaCelular = fallbackCell ? fallbackCell.contato : '';
                  }
               },
               error: (err: any) => console.error('Aviso: Falha ao carregar contatos do cliente para o Alerta', err)
            });
         },
         error: (err: any) => console.error('Aviso: Falha ao carregar metadados do cliente para o Alerta', err)
      });
    } else {
      this.alertaMensagem = `Olá\n\nVocê possui um agendamento marcado conosco.\nPor favor, entre em contato para confirmar.\n\nAtenciosamente,\n${this.oficinaNome}.`;
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
    if (this.alertaEnviarPor === 'WHATSAPP' && !this.alertaCelular) {
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
      status: 'AGENDADA',
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
    if (!status) return 'nt-badge';

    switch (status.toUpperCase()) {
      case 'AGENDADO': return 'nt-badge nt-badge--info';
      case 'CONFIRMADO': return 'nt-badge nt-badge--success';
      case 'EM_ANDAMENTO': return 'nt-badge nt-badge--info';
      case 'CONCLUIDO': return 'nt-badge nt-badge--success';
      case 'REAGENDADO': return 'nt-badge nt-badge--warning';
      case 'CANCELADO':
      case 'NAO_COMPARECEU': return 'nt-badge nt-badge--danger';
      default: return 'nt-badge';
    }
  }
}
