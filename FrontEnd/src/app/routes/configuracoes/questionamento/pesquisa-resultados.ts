import { CommonModule } from '@angular/common';
import { Component, Input, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { ClientePesquisa, PesquisaResposta, PesquisaResultadosService } from './pesquisa-resultados.service';

export interface PerguntaPesquisa { id?: number; texto: string; tipo: 'avaliacao' | 'simnao' | 'aberta'; }

@Component({
  selector: 'app-pesquisa-resultados',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pesquisa-resultados.html',
  styleUrls: ['./pesquisa-resultados.scss'],
})
export class PesquisaResultados implements OnInit {
  @Input({ required: true }) questionarioId!: number;
  @Input() perguntas: PerguntaPesquisa[] = [];
  @Input() titulo = '';

  private readonly api = inject(PesquisaResultadosService);
  private requestSeq = 0;
  loading = false;
  error = '';
  total = 0;
  page = 0;
  readonly pageSize = 10;
  registros: PesquisaResposta[] = [];

  formularioAberto = false;
  buscandoClientes = false;
  buscaCliente = '';
  clientes: ClientePesquisa[] = [];
  clienteId: number | null = null;
  respostas: Record<number, string> = {};
  formularioErro = '';
  enviando = false;
  private pendingIdempotencyKey: string | null = null;

  get paginas(): number { return Math.max(1, Math.ceil(this.total / this.pageSize)); }
  get podeRegistrar(): boolean { return this.perguntas.length > 0 && this.perguntas.every(p => p.id != null); }
  nomeCliente(c: ClientePesquisa): string {
    return c.nomeCompleto || c.razaoSocial || c.nomeFantasia || `Cliente #${c.id}`;
  }

  ngOnInit(): void { this.carregar(); }
  carregar(): void {
    const version = ++this.requestSeq;
    this.loading = true;
    this.error = '';
    forkJoin({
      resumo: this.api.resumo(this.questionarioId),
      pagina: this.api.listar(this.questionarioId, this.page, this.pageSize)
    }).subscribe({
      next: result => {
        if (version !== this.requestSeq) return;
        this.total = result.resumo.totalRespostas;
        this.registros = result.pagina.content || [];
        this.loading = false;
      },
      error: err => {
        if (version !== this.requestSeq) return;
        this.loading = false;
        this.error = err.status === 403
          ? 'Seu perfil não tem permissão para ver estas respostas.'
          : 'Não foi possível carregar as respostas. Tente novamente.';
        this.registros = [];
      },
    });
  }
  navegar(page: number): void {
    if (page < 0 || page >= this.paginas || page === this.page) return;
    this.page = page; this.carregar();
  }

  buscarClientes(): void {
    const busca = this.buscaCliente.trim();
    if (busca.length < 2) {
      this.formularioErro = 'Digite pelo menos dois caracteres para buscar o cliente.';
      return;
    }
    this.formularioErro = '';
    this.buscandoClientes = true;
    this.clientes = [];
    this.clienteId = null;
    this.api.buscarClientes(busca).subscribe({
      next: page => {
        this.clientes = page.content || [];
        this.buscandoClientes = false;
      },
      error: () => {
        this.buscandoClientes = false;
        this.formularioErro = 'Não foi possível buscar clientes nesta empresa.';
      }
    });
  }

  abrirFormulario(): void {
    this.formularioAberto = !this.formularioAberto;
    this.formularioErro = '';
  }

  registrar(): void {
    if (this.enviando || !this.podeRegistrar) return;
    if (this.clienteId == null || !this.clientes.some(c => c.id === this.clienteId)) {
      this.formularioErro = 'Selecione um cliente da lista de resultados.';
      return;
    }
    const respostas = this.perguntas.map(p => ({ perguntaId: p.id!, valor: (this.respostas[p.id!] || '').trim() }));
    if (respostas.some(r => !r.valor)) {
      this.formularioErro = 'Responda todas as perguntas antes de salvar.';
      return;
    }
    if (!this.pendingIdempotencyKey) this.pendingIdempotencyKey = crypto.randomUUID();
    this.enviando = true;
    this.formularioErro = '';
    this.api.registrar(this.questionarioId, {
      clienteId: this.clienteId, chaveIdempotencia: this.pendingIdempotencyKey, respostas
    }).subscribe({
      next: () => {
        this.enviando = false;
        this.pendingIdempotencyKey = null;
        this.formularioAberto = false;
        this.respostas = {};
        this.clienteId = null;
        this.clientes = [];
        this.buscaCliente = '';
        this.page = 0;
        this.carregar();
      },
      error: err => {
        this.enviando = false;
        this.formularioErro = err.status === 403
          ? 'Sem permissão para registrar respostas.'
          : err.status === 400
            ? 'Dados recusados. Verifique o cliente, o tipo de resposta e se o questionário está ativo.'
            : 'Falha ao salvar. Tente novamente sem criar uma nova resposta.';
      },
    });
  }
}
