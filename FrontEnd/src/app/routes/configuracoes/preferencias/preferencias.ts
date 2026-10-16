import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { HistoricoPolitica, Politica, PreferenciasService } from './preferencias.service';

@Component({
  selector: 'nt-preferencias-gerais',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './preferencias.html'
})
export class PreferenciasGeraisComponent implements OnInit {
  private readonly api = inject(PreferenciasService);
  items: Politica[] = [];
  total = 0;
  page = 0;
  size = 20;
  busca = '';
  secao = '';
  status: 'todas' | 'ativas' | 'inativas' = 'todas';
  loading = false;
  saving = false;
  error = '';
  formError = '';
  notice = '';
  current: Politica | null = null;
  value = '';
  editing = false;
  history: HistoricoPolitica[] = [];
  historyTotal = 0;
  historyPage = 0;
  historyLoading = false;
  historyError = '';

  ngOnInit(): void { this.load(); }

  load(): void {
    if (this.loading) return;
    this.loading = true;
    this.error = '';
    this.api.listar(this.busca, this.secao,
      this.status === 'todas' ? undefined : this.status === 'ativas',
      this.page, this.size).pipe(finalize(() => this.loading = false)).subscribe({
      next: data => { this.items = data.content || []; this.total = data.totalElements || 0; },
      error: err => { this.items = []; this.total = 0; this.error = this.describe(err); }
    });
  }
  filtrar(): void { this.page = 0; this.load(); }
  alterarTamanho(size: number): void {
    if (![20, 50, 100].includes(Number(size))) return;
    this.size = Number(size); this.filtrar();
  }
  pagina(delta: number): void {
    const next = this.page + delta;
    if (next < 0 || next * this.size >= this.total) return;
    this.page = next; this.load();
  }
  editar(item: Politica): void {
    this.current = item;
    this.value = item.valorSalvo ?? item.valorAplicado;
    this.editing = true; this.formError = ''; this.notice = '';
    this.history = []; this.historyPage = 0; this.historyTotal = 0;
    this.carregarHistorico();
  }
  cancelar(): void { if (!this.saving) this.editing = false; }
  salvar(): void {
    if (!this.current || this.saving || !this.value) return;
    const item = this.current;
    const novoValor = String(this.value);
    const request = item.versao == null
      ? this.api.criar(item.chave, novoValor)
      : this.api.editar(item.chave, novoValor, item.versao);
    this.saving = true; this.formError = ''; this.notice = '';
    request.pipe(finalize(() => this.saving = false)).subscribe({
      next: data => {
        this.current = data; this.editing = false;
        this.notice = 'Preferência persistida e versionada no servidor. Alterações valem prospectivamente.';
        this.load();
      },
      error: err => { this.formError = this.describe(err); }
    });
  }
  alternarStatus(): void {
    if (!this.current || this.saving || this.current.versao == null) return;
    const item = this.current;
    const versao = item.versao;
    if (versao == null) return;
    this.saving = true; this.formError = '';
    this.api.status(item.chave, !item.ativo, versao)
      .pipe(finalize(() => this.saving = false)).subscribe({
        next: data => {
          this.current = data;
          this.value = data.valorSalvo ?? data.valorAplicado;
          this.notice = data.ativo ? 'Política ativada.' : 'Política inativada; valor padrão aplicado.';
          this.load();
          this.carregarHistorico(0);
        },
        error: err => { this.formError = this.describe(err); }
      });
  }
  carregarHistorico(page = 0): void {
    if (!this.current || this.historyLoading) return;
    this.historyLoading = true; this.historyError = '';
    this.api.historico(this.current.chave, page)
      .pipe(finalize(() => this.historyLoading = false)).subscribe({
        next: result => {
          this.history = result.content || [];
          this.historyPage = page;
          this.historyTotal = result.totalElements || 0;
        },
        error: err => { this.historyError = this.describe(err); }
      });
  }
  options(): { value: string; label: string }[] | null {
    if (!this.current) return null;
    switch (this.current.chave) {
      case 'locale': return [{ value: 'pt-BR', label: 'Português (Brasil)' },
        { value: 'en-US', label: 'Inglês (EUA)' }];
      case 'precisaoQuantidades': return Array.from({length: 5}, (_, i) =>
        ({ value: String(i), label: String(i) }));
      case 'avisosAtivos': return [{ value: 'true', label: 'Sim' }, { value: 'false', label: 'Não' }];
      default: return null;
    }
  }
  get alterou(): boolean {
    return !!this.current && String(this.value) !== (this.current.valorSalvo ?? this.current.valorAplicado);
  }
  get valorValido(): boolean {
    if (!this.current) return false;
    if (this.options()) return !!this.options()?.some(x => x.value === this.value);
    if (!/^\d+$/.test(this.value)) return false;
    const numberValue = Number(this.value);
    return this.current.chave === 'validadeOrcamentoDias'
      ? numberValue >= 1 && numberValue <= 365
      : numberValue >= 30 && numberValue <= 3650;
  }
  private describe(err: any): string {
    if (err?.status === 403) return 'Sem permissão de administrador para esta empresa (403).';
    if (err?.status === 409) return 'Alteração concorrente ou configuração duplicada (409). Recarregue antes de salvar.';
    if (err?.status === 422 || err?.status === 400) return 'Parâmetro inválido para a política selecionada.';
    if (err?.status === 0) return 'Servidor indisponível/offline. Confira sua conexão.';
    return 'Não foi possível carregar ou salvar preferências.';
  }
}
