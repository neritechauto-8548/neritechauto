import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { forkJoin } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { NgxPermissionsModule } from 'ngx-permissions';

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
  imports: [CommonModule, FormsModule, DialogModule, ToastModule, ConfirmDialogModule, NgxPermissionsModule],
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
  private http = inject(HttpClient);
  private storage = inject(LocalStorageService);

  @Input() embed = false;
  // Toolbar
  funcionarioOptions = ['TODOS'];
  funcionarioSelecionado = this.funcionarioOptions[0];

  mecanicoMap = new Map<number, string>();

  get tenantId(): string {
    const v = this.storage.has('tenantId') ? (this.storage.get('tenantId') as any) : '7';
    return String(v && typeof v !== 'object' ? v : '7');
  }

  // Calendário
  hoje = new Date();
  corrente = new Date(this.hoje);
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
    const headers = new HttpHeaders({ 'X-Tenant-Id': this.tenantId, 'Accept': 'application/json' });
    forkJoin({
      funcionarios: this.funcionarioService.list({ size: 1000 }),
      mecanicos: this.http.get<any>(`${environment.baseUrl}/v1/rh/mecanicos`, { params: { size: '1000' }, headers })
    }).subscribe({
      next: (res: any) => {
        const funcList = res.funcionarios?.content || res.funcionarios || [];
        const mecList = res.mecanicos?.content || res.mecanicos || [];

        // Mapear funcionarioId -> nomeCompleto
        const funcMap = new Map<number, string>();
        funcList.forEach((f: any) => {
          if (f.nomeCompleto) {
            funcMap.set(f.id, f.nomeCompleto);
          }
        });

        // Montar a lista de nomes para o dropdown
        const nomes = funcList.map((f: any) => f.nomeCompleto).filter(Boolean);
        this.funcionarioOptions = ['TODOS', ...nomes];

        // Mapear mecanicoId -> nomeCompleto
        this.mecanicoMap.clear();
        mecList.forEach((m: any) => {
          const nome = funcMap.get(m.funcionarioId);
          if (nome) {
            this.mecanicoMap.set(m.id, nome);
          }
        });
      },
      error: (err) => console.error('Falha ao carregar funcionários e mecânicos', err)
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
    if (this.viewMode === 'mes') {
      return `${meses[this.corrente.getMonth()]} ${this.corrente.getFullYear()}`;
    } else if (this.viewMode === 'semana') {
      const inicio = new Date(this.corrente);
      inicio.setDate(this.corrente.getDate() - this.corrente.getDay());
      const fim = new Date(inicio);
      fim.setDate(inicio.getDate() + 6);
      
      const formatDia = (d: Date) => d.getDate();
      const formatMes = (d: Date) => meses[d.getMonth()].substring(0, 3);
      
      if (inicio.getMonth() === fim.getMonth()) {
        return `${formatDia(inicio)} a ${formatDia(fim)} de ${meses[inicio.getMonth()]} de ${inicio.getFullYear()}`;
      } else if (inicio.getFullYear() === fim.getFullYear()) {
        return `${formatDia(inicio)} de ${formatMes(inicio)}. a ${formatDia(fim)} de ${formatMes(fim)}. de ${inicio.getFullYear()}`;
      } else {
        return `${formatDia(inicio)} de ${formatMes(inicio)}. de ${inicio.getFullYear()} a ${formatDia(fim)} de ${formatMes(fim)}. de ${fim.getFullYear()}`;
      }
    } else {
      const diasSemana = [
        'domingo', 'segunda-feira', 'terça-feira', 'quarta-feira', 'quinta-feira', 'sexta-feira', 'sábado'
      ];
      return `${diasSemana[this.corrente.getDay()]} ${this.corrente.getDate()} de ${meses[this.corrente.getMonth()]} de ${this.corrente.getFullYear()}`;
    }
  }

  diasParaExibir(): Date[] {
    if (this.viewMode === 'mes') {
      const ano = this.corrente.getFullYear();
      const mes = this.corrente.getMonth();
      const primeiroDia = new Date(ano, mes, 1);
      const inicioGrid = new Date(primeiroDia);
      // Inicia no domingo da semana contendo o primeiro dia do mês
      inicioGrid.setDate(primeiroDia.getDate() - primeiroDia.getDay());

      const dias: Date[] = [];
      for (let i = 0; i < 42; i++) {
        const d = new Date(inicioGrid);
        d.setDate(inicioGrid.getDate() + i);
        dias.push(d);
      }
      return dias;
    } else if (this.viewMode === 'semana') {
      const diaDaSemana = this.corrente.getDay();
      const inicioSemana = new Date(this.corrente);
      inicioSemana.setDate(this.corrente.getDate() - diaDaSemana);

      const dias: Date[] = [];
      for (let i = 0; i < 7; i++) {
        const d = new Date(inicioSemana);
        d.setDate(inicioSemana.getDate() + i);
        dias.push(d);
      }
      return dias;
    } else {
      return [new Date(this.corrente)];
    }
  }

  diasDoMes(): Date[] {
    return this.diasParaExibir();
  }

  obterNomeDiaDaSemana(d: Date): string {
    const diasSemana = [
      'domingo', 'segunda-feira', 'terça-feira', 'quarta-feira', 'quinta-feira', 'sexta-feira', 'sábado'
    ];
    return diasSemana[d.getDay()];
  }

  getStatusBadgeClass(status: string | undefined): string {
    if (!status) return 'bg-slate-100 text-slate-700';

    switch (status.toUpperCase()) {
      case 'AGENDADO': return 'bg-blue-50 text-blue-700 border border-blue-100';
      case 'CONFIRMADO': return 'bg-emerald-50 text-emerald-700 border border-emerald-100';
      case 'EM_ANDAMENTO': return 'bg-sky-50 text-sky-700 border border-sky-100';
      case 'CONCLUIDO': return 'bg-emerald-50 text-emerald-700 border border-emerald-100';
      case 'REAGENDADO': return 'bg-amber-50 text-amber-700 border border-amber-100';
      case 'CANCELADO':
      case 'NAO_COMPARECEU': return 'bg-rose-50 text-rose-700 border border-rose-100';
      default: return 'bg-slate-100 text-slate-700 border border-slate-200';
    }
  }

  eventosDoDia(d: Date): EventoCalendario[] {
    let filteredList = this.eventos.filter(
      e =>
        e.inicio.getFullYear() === d.getFullYear() &&
        e.inicio.getMonth() === d.getMonth() &&
        e.inicio.getDate() === d.getDate()
    );

    if (this.funcionarioSelecionado && this.funcionarioSelecionado !== 'TODOS') {
      filteredList = filteredList.filter(e => {
        const allocatedMechanicId = e.agendamento.mecanicoAlocadoId || e.agendamento.mecanicoPreferidoId;
        if (!allocatedMechanicId) return false;
        
        const name = this.mecanicoMap.get(allocatedMechanicId);
        return name === this.funcionarioSelecionado;
      });
    }

    return filteredList;
  }

  onFilterChange() {
    // This empty method just exists to ensure Angular's change detection cycle 
    // runs fully and the calendar re-evaluates the `eventosDoDia` method.
    // We can also trigger a small state mutation if needed.
    this.hoje = new Date();
  }

  abrirEvento(e: EventoCalendario): void {
    this.eventoSelecionado = e;
    this.rawEventoSelecionado = e.agendamento;
    this.mostrarDialogEvento = true;
  }

  hojeClick(): void {
    this.hoje = new Date();
    this.corrente = new Date(this.hoje);
  }

  anterior(): void {
    if (this.viewMode === 'mes') {
      this.corrente = new Date(this.corrente.getFullYear(), this.corrente.getMonth() - 1, 1);
    } else if (this.viewMode === 'semana') {
      this.corrente = new Date(this.corrente.getFullYear(), this.corrente.getMonth(), this.corrente.getDate() - 7);
    } else {
      this.corrente = new Date(this.corrente.getFullYear(), this.corrente.getMonth(), this.corrente.getDate() - 1);
    }
  }

  proxima(): void {
    if (this.viewMode === 'mes') {
      this.corrente = new Date(this.corrente.getFullYear(), this.corrente.getMonth() + 1, 1);
    } else if (this.viewMode === 'semana') {
      this.corrente = new Date(this.corrente.getFullYear(), this.corrente.getMonth(), this.corrente.getDate() + 7);
    } else {
      this.corrente = new Date(this.corrente.getFullYear(), this.corrente.getMonth(), this.corrente.getDate() + 1);
    }
  }

  gravarAlteracoes(): void {
    if (!this.eventoSelecionado) return;
    this.router.navigate(['/agendamento/cadastro', { id: this.eventoSelecionado.id }]);
    this.mostrarDialogEvento = false;
  }

  criarOsParaAgendamentoSelecionado(): void {
    if (!this.eventoSelecionado) return;
    const a = this.eventoSelecionado.agendamento;
    if (a.veiculoId) {
      this.router.navigate(['/os/cadastro'], { queryParams: { veiculoId: a.veiculoId } });
    } else {
      this.router.navigate(['/os/cadastro'], { queryParams: { clienteId: a.clienteId } });
    }
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
