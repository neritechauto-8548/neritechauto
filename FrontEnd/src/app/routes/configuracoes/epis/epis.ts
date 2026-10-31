import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { A11yModule } from '@angular/cdk/a11y';
import { Subscription, Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { Epi, EpiRequest, EpiService } from './epi.service';

@Component({
  selector: 'app-epis',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, A11yModule],
  templateUrl: './epis.html'
})
export class EpisComponent implements OnInit, OnDestroy {
  private readonly service = inject(EpiService);
  private listSub?: Subscription;
  private readonly pesquisa$ = new Subject<string>();
  private readonly buscaSub = this.pesquisa$.pipe(debounceTime(250), distinctUntilChanged()).subscribe(() => this.pesquisar());
  alterarPesquisa(valor: string): void { this.pesquisa = valor; this.pesquisa$.next(valor); }
  registros: Epi[] = [];
  pesquisa = '';
  loading = false;
  saving = false;
  error = '';
  notice = '';
  total = 0;
  page = 0;
  totalPages = 1;
  drawer = false;
  selectedId: number | null = null;
  form: EpiRequest = { nome: '' };
  formError = '';

  ngOnInit(): void { this.carregar(); }
  ngOnDestroy(): void { this.listSub?.unsubscribe(); this.buscaSub.unsubscribe(); this.pesquisa$.complete(); }
  carregar(): void {
    this.listSub?.unsubscribe();
    this.loading = true;
    this.error = '';
    this.listSub = this.service.listar(this.pesquisa.trim(), this.page, 20).subscribe({
      next: res => {
        this.registros = res.content ?? [];
        this.total = res.totalElements ?? 0;
        this.totalPages = Math.max(1, res.totalPages ?? 1);
        this.loading = false;
      },
      error: err => {
        this.loading = false; this.registros = []; this.total = 0;
        this.error = err.status === 403 ? 'Você não tem permissão para consultar EPIs.' : 'Não foi possível consultar o catálogo de EPIs.';
      }
    });
  }
  pesquisar(): void { this.page = 0; this.carregar(); }
  mudarPagina(delta: number): void {
    const next = this.page + delta;
    if (next >= 0 && next < this.totalPages && !this.loading) { this.page = next; this.carregar(); }
  }
  novo(): void {
    this.selectedId = null; this.form = { nome: '' }; this.formError = ''; this.drawer = true;
  }
  editar(reg: Epi): void {
    this.selectedId = reg.id;
    this.form = { nome: reg.nome, numeroCa: reg.numeroCa, categoria: reg.categoria,
      fabricante: reg.fabricante, tamanho: reg.tamanho, especificacoes: reg.especificacoes,
      descricao: reg.descricao, versao: reg.versao };
    this.formError = ''; this.drawer = true;
  }
  fechar(): void { if (!this.saving) this.drawer = false; }
  salvar(): void {
    const nome = this.form.nome?.trim();
    if (!nome || nome.length < 2) { this.formError = 'Nome deve ter ao menos 2 caracteres.'; return; }
    this.saving = true; this.formError = '';
    const payload = { ...this.form, nome };
    const op = this.selectedId == null ? this.service.criar(payload) : this.service.atualizar(this.selectedId, payload);
    op.subscribe({
      next: () => { this.saving = false; this.drawer = false; this.notice = 'EPI salvo com sucesso.'; this.carregar(); },
      error: err => {
        this.saving = false;
        this.formError = err.status === 409 ? 'EPI duplicado ou alterado por outra pessoa; revise e recarregue.'
          : err.status === 403 ? 'Sem permissão para salvar EPIs.' : 'Não foi possível salvar. Verifique os dados e tente novamente.';
      }
    });
  }
  inativar(reg: Epi): void {
    if (!confirm('Inativar "' + reg.nome + '"? As entregas históricas permanecerão registradas.')) return;
    this.service.inativar(reg.id).subscribe({
      next: () => { this.notice = 'EPI inativado; entregas preservadas.'; this.carregar(); },
      error: err => { this.error = err.status === 403 ? 'Sem permissão para inativar.' : 'Erro ao inativar EPI.'; }
    });
  }
}
