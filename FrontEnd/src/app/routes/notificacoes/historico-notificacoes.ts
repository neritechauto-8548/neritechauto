import { CommonModule } from '@angular/common';
import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { forkJoin } from 'rxjs';
import { NotificacaoService, NotificacaoSistemaResponse } from '@core/services/notificacao.service';

@Component({
  selector: 'app-historico-notificacoes', standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './historico-notificacoes.html', styleUrl: './historico-notificacoes.scss',
})
export class HistoricoNotificacoes implements OnInit {
  private readonly servico = inject(NotificacaoService);
  private readonly destruir = inject(DestroyRef);
  notificacoes: NotificacaoSistemaResponse[] = [];
  carregando = false;
  erro = '';
  busca = '';
  origem = '';
  estado: 'todas' | 'naoLidas' | 'lidas' = 'todas';
  periodo = '';

  ngOnInit(): void { this.carregar(); }
  get notificacoesFiltradas(): NotificacaoSistemaResponse[] {
    const busca = this.busca.trim().toLocaleLowerCase('pt-BR');
    return this.notificacoes.filter(n => {
      const texto = `${n.titulo} ${n.mensagem} ${n.origemSistema ?? ''} ${n.categoria ?? ''}`.toLocaleLowerCase('pt-BR');
      const criada = n.dataCadastro ? new Date(n.dataCadastro) : null;
      const dia = criada && !Number.isNaN(criada.getTime()) ? criada.toISOString().slice(0, 10) : '';
      return (!busca || texto.includes(busca)) && (!this.origem || this.rotuloOrigem(n) === this.origem)
        && (this.estado === 'todas' || (this.estado === 'lidas' ? n.lida : !n.lida))
        && (!this.periodo || dia === this.periodo);
    });
  }
  carregar(): void {
    this.carregando = true; this.erro = '';
    this.servico.findMine(0, 100).pipe(takeUntilDestroyed(this.destruir)).subscribe({
      next: pagina => { this.notificacoes = pagina?.content ?? []; this.carregando = false; },
      error: falha => { console.error('Falha ao carregar histórico de notificações.', { status: falha?.status }); this.erro = 'Não foi possível carregar o histórico. Tente novamente.'; this.carregando = false; },
    });
  }
  marcarTodasComoLidas(): void {
    const pendentes = this.notificacoesFiltradas.filter(n => !n.lida);
    if (!pendentes.length) return;
    this.carregando = true;
    forkJoin(pendentes.map(n => this.servico.marcarComoLida(n.id))).pipe(takeUntilDestroyed(this.destruir)).subscribe({
      next: salvas => { const porId = new Map(salvas.map(n => [n.id, n])); this.notificacoes = this.notificacoes.map(n => porId.get(n.id) ?? n); this.carregando = false; },
      error: falha => { console.error('Falha ao marcar notificações como lidas.', { status: falha?.status }); this.erro = 'Não foi possível confirmar a leitura de todas as notificações.'; this.carregando = false; },
    });
  }
  rotuloOrigem(n: NotificacaoSistemaResponse): string {
    const valor = `${n.origemSistema ?? ''} ${n.categoria ?? ''}`.toLocaleLowerCase('pt-BR');
    if (/ordem|servico|os/.test(valor)) return 'Ordem de serviço';
    if (/boletim|atualizacao/.test(valor)) return 'Nota de atualização';
    return 'Outros';
  }
  rotuloData(valor: string): string {
    const data = new Date(valor);
    if (Number.isNaN(data.getTime())) return '—';
    const dias = Math.floor((Date.now() - data.getTime()) / 86400000);
    if (dias < 1) return 'há menos de um dia';
    if (dias < 30) return `há ${dias} ${dias === 1 ? 'dia' : 'dias'}`;
    const meses = Math.floor(dias / 30);
    return `há ${meses} ${meses === 1 ? 'mês' : 'meses'}`;
  }
  marcarComoLida(n: NotificacaoSistemaResponse): void {
    if (n.lida) return;
    this.servico.marcarComoLida(n.id).pipe(takeUntilDestroyed(this.destruir)).subscribe({
      next: salva => { if (salva.lida) n.lida = true; },
      error: falha => { console.error('Falha ao salvar leitura da notificação.', { status: falha?.status }); this.erro = 'Não foi possível marcar a notificação como lida.'; },
    });
  }
  abrir(n: NotificacaoSistemaResponse): void { if (n.linkAcao) window.location.assign(n.linkAcao); }
}
