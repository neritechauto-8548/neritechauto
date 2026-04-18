import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { forkJoin } from 'rxjs';

import { AgendamentoService, AgendamentoResponse } from '../agendamento.service';
import { FuncionarioService } from '../../configuracoes/colaboradores/funcionario.service';
import { ClientesService } from '../../cliente/cliente/cliente.service';

interface EventoCalendario {
  id: number;
  titulo: string;
  inicio: Date;
  termino: Date;
  agendamento: AgendamentoResponse;
}

@Component({
  selector: 'app-calendario-agendamento',
  standalone: true,
  imports: [CommonModule, FormsModule, DialogModule, ToastModule, ConfirmDialogModule],
  providers: [MessageService, ConfirmationService],
  templateUrl: './calendario-agendamento.html',
  styleUrls: ['./calendario-agendamento.scss'],
})
export class CalendarioAgendamento implements OnInit {
  private agendamentoService = inject(AgendamentoService);
  private funcionarioService = inject(FuncionarioService);
  private clienteService = inject(ClientesService);
  private confirmationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  private router = inject(Router);

  @Input() embed = false;
  // Toolbar
  funcionarioOptions = ['TODOS'];
  funcionarioSelecionado = this.funcionarioOptions[0];

  // Calendário
  hoje = new Date();
  corrente = new Date(this.hoje.getFullYear(), this.hoje.getMonth(), 1);
  viewMode: 'mes' | 'semana' | 'dia' = 'mes';

  eventos: EventoCalendario[] = [];

  // Popup de visualização
  mostrarDialogEvento = false;
  eventoSelecionado: EventoCalendario | null = null;
  rawEventoSelecionado: AgendamentoResponse | null = null;

  ngOnInit() {
    this.carregarFuncionarios();
    this.carregarEventos();
  }

  carregarFuncionarios() {
    this.funcionarioService.list({}).subscribe({
      next: (res: any) => {
        const items = res?.content || res;
        const nomes = Array.isArray(items) ? items.map((f: any) => f.nome).filter(Boolean) : [];
        this.funcionarioOptions = ['TODOS', ...nomes];
      },
      error: () => console.error('Falha ao carregar funcionários')
    });
  }

  carregarEventos() {
    forkJoin({
      agendamentos: this.agendamentoService.listPorEmpresa(),
      clientes: this.clienteService.list({})
    }).subscribe({
      next: res => {
        const listClientes = res.clientes.content || res.clientes;

        this.eventos = res.agendamentos.map(a => {
          // Mapear ClienteNome
          const cli: any = listClientes.find((c: any) => c.id === a.clienteId);
          a.clienteNome = cli ? (cli.nomeCompleto || cli.razaoSocial || 'Desconhecido') : 'Desconhecido';

          // Combinar Data e Hora
          const dataParts = a.dataAgendamento.split('-');
          const horaInicioParts = a.horaInicio.split(':');
          const horaFimParts = a.horaFim.split(':');

          const inicio = new Date(
            Number(dataParts[0]),
            Number(dataParts[1]) - 1,
            Number(dataParts[2]),
            Number(horaInicioParts[0]),
            Number(horaInicioParts[1])
          );
          const termino = new Date(
            Number(dataParts[0]),
            Number(dataParts[1]) - 1,
            Number(dataParts[2]),
            Number(horaFimParts[0]),
            Number(horaFimParts[1])
          );

          return {
            id: a.id,
            titulo: `${a.tipoAgendamentoNome || 'Agendamento'}`,
            inicio,
            termino,
            agendamento: a
          };
        });
      },
      error: err => console.error('Erro ao carregar agendamentos e clientes', err),
    });
  }

  criarAgendamento(): void {
    this.router.navigate(['/agendamento/cadastro']);
  }

  calendarioGeral(): void {
    // Nesta página já estamos no calendário geral; mantemos por consistência
  }

  get tituloMes(): string {
    const meses = [
      'janeiro',
      'fevereiro',
      'março',
      'abril',
      'maio',
      'junho',
      'julho',
      'agosto',
      'setembro',
      'outubro',
      'novembro',
      'dezembro',
    ];
    return `${meses[this.corrente.getMonth()]} ${this.corrente.getFullYear()}`;
  }

  diasDoMes(): Date[] {
    const ano = this.corrente.getFullYear();
    const mes = this.corrente.getMonth();
    const primeiroDia = new Date(ano, mes, 1);
    const inicioGrid = new Date(primeiroDia);
    inicioGrid.setDate(primeiroDia.getDate() - ((primeiroDia.getDay() + 6) % 7)); // inicia no domingo

    const dias: Date[] = [];
    for (let i = 0; i < 42; i++) {
      const d = new Date(inicioGrid);
      d.setDate(inicioGrid.getDate() + i);
      dias.push(d);
    }
    return dias;
  }

  eventosDoDia(d: Date): EventoCalendario[] {
    return this.eventos.filter(
      e =>
        e.inicio.getFullYear() === d.getFullYear() &&
        e.inicio.getMonth() === d.getMonth() &&
        e.inicio.getDate() === d.getDate()
    );
  }

  abrirEvento(e: EventoCalendario): void {
    this.eventoSelecionado = e;
    this.rawEventoSelecionado = e.agendamento;
    this.mostrarDialogEvento = true;
  }

  hojeClick(): void {
    this.corrente = new Date(this.hoje.getFullYear(), this.hoje.getMonth(), 1);
  }

  anterior(): void {
    this.corrente = new Date(this.corrente.getFullYear(), this.corrente.getMonth() - 1, 1);
  }

  proxima(): void {
    this.corrente = new Date(this.corrente.getFullYear(), this.corrente.getMonth() + 1, 1);
  }

  gravarAlteracoes(): void {
    if (!this.eventoSelecionado) return;
    this.router.navigate(['/agendamento/cadastro', { id: this.eventoSelecionado.id }]);
    this.mostrarDialogEvento = false;
  }

  excluir(): void {
    if (!this.eventoSelecionado) return;

    this.confirmationService.confirm({
      header: 'Confirmar Cancelamento',
      message: `Atenção! Realmente deseja cancelar o agendamento de ${this.rawEventoSelecionado?.clienteNome || 'este cliente'}?`,
      acceptLabel: 'Sim, Cancelar',
      rejectLabel: 'Não, Manter',
      acceptButtonStyleClass: 'p-button-danger',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.agendamentoService.delete(this.eventoSelecionado!.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Cancelado', detail: 'Agendamento cancelado com sucesso.' });
            this.carregarEventos();
            this.mostrarDialogEvento = false;
          },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao cancelar o agendamento.' })
        });
      }
    });
  }
}
