import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { A11yModule } from '@angular/cdk/a11y';
import { HttpErrorResponse } from '@angular/common/http';
import { CargoRequest, CargoResponse, CargoService } from './cargo.service';
import { DepartamentoService } from '../departamentos/departamento.service';

@Component({
  selector: 'app-cargos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, A11yModule],

  template: \`
    <main class="min-h-screen bg-slate-50 px-4 py-5 sm:px-6 lg:px-8 space-y-5">
      <nav aria-label="Caminho de navegação" class="text-xs text-slate-500">Administração › Gestão de pessoas › Cargos</nav>
      <nav aria-label="Gestão de pessoas" class="flex flex-wrap items-center gap-1 rounded-xl border border-slate-200 bg-white p-1 shadow-sm">
        <a routerLink="/configuracoes/colaboradores" class="rounded-lg px-3 py-2 text-sm text-slate-700 hover:bg-slate-50">Funcionários</a>
        <span aria-current="page" class="rounded-lg bg-blue-100 px-3 py-2 text-sm font-semibold text-[#18328A]">Cargos</span>
        <a routerLink="/configuracoes/epis" class="rounded-lg px-3 py-2 text-sm text-slate-700 hover:bg-slate-50">EPIs</a>
        <a routerLink="/financeiro/comissoes" class="rounded-lg px-3 py-2 text-sm text-slate-700 hover:bg-slate-50">Comissões</a>
      </nav>
      <header class="flex flex-wrap items-center justify-between gap-3">
        <div><h1 class="text-2xl font-bold tracking-tight text-slate-950">Cargos</h1><p class="text-xs text-slate-500">Funções profissionais, sem conceder permissões de acesso ao sistema</p></div>
        <button type="button" (click)="abrirNovo()" class="rounded-lg bg-[#18328A] px-4 py-2.5 text-sm font-semibold text-white hover:bg-blue-900"><span aria-hidden="true">＋</span> Novo cargo</button>
      </header>
      <div class="flex flex-col gap-2 sm:flex-row">
        <label class="sr-only" for="cargo-busca">Pesquisar cargos</label>
        <input id="cargo-busca" type="search" [(ngModel)]="busca" (keyup.enter)="aplicarFiltros()" placeholder="Pesquisar cargos por descrição ou código"
          class="h-11 w-full rounded-lg border border-slate-300 bg-white px-4 text-sm outline-none focus:border-blue-700" />
        <select aria-label="Situação do cargo" [(ngModel)]="filtroAtivo" (change)="aplicarFiltros()" class="h-11 rounded-lg border border-slate-300 bg-white px-3 text-sm">
          <option value="">Todas as situações</option><option value="true">Ativos</option><option value="false">Inativos</option>
        </select>
        <button type="button" (click)="aplicarFiltros()" class="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm hover:bg-slate-50">Buscar</button>
      </div>
      <p *ngIf="actionMessage" role="status" class="rounded-lg border border-blue-200 bg-blue-50 p-3 text-sm text-blue-900">{{ actionMessage }}</p>
      <section aria-labelledby="cargos-table-title" class="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
        <h2 id="cargos-table-title" class="sr-only">Listagem de cargos</h2>
        <div *ngIf="loading" role="status" class="p-12 text-center text-sm text-slate-500">Carregando cargos...</div>
        <div *ngIf="error && !loading" role="alert" class="p-10 text-center text-sm text-red-700">{{ error }}
          <button type="button" (click)="carregar()" class="ml-2 underline">Tentar novamente</button>
        </div>
        <div *ngIf="!loading && !error" class="overflow-x-auto">
          <table class="w-full min-w-[620px] text-left text-sm">
            <thead class="border-b border-slate-200 text-slate-500"><tr>
              <th scope="col" class="px-4 py-4 font-medium">Descrição</th>
              <th scope="col" class="px-4 py-4 font-medium">Código CBO</th>
              <th scope="col" class="px-4 py-4 font-medium">Funcionários</th>
              <th scope="col" class="px-4 py-4 font-medium">Situação</th>
              <th scope="col" class="px-4 py-4 text-right font-medium">Ações</th>
            </tr></thead>
            <tbody class="divide-y divide-slate-200">
              <tr *ngFor="let cargo of cargosFiltrados" class="hover:bg-slate-50">
                <td class="px-4 py-4"><button type="button" (click)="abrirEdicao(cargo)" class="font-medium text-[#18328A] hover:underline">{{ cargo.nome }}</button></td>
                <td class="px-4 py-4 text-slate-600">{{ cargo.codigoCbo || 'Não informado' }}</td>
                <td class="px-4 py-4 text-slate-600">{{ cargo.numeroFuncionarios || 0 }}</td>
                <td class="px-4 py-4"><span [class]="cargo.ativo ? 'rounded-full bg-emerald-50 px-2 py-1 text-xs text-emerald-700' : 'rounded-full bg-slate-100 px-2 py-1 text-xs text-slate-600'">{{ cargo.ativo ? 'Ativo' : 'Inativo' }}</span></td>
                <td class="px-4 py-4 text-right">
                  <button type="button" (click)="abrirEdicao(cargo)" class="mr-3 text-sm text-[#18328A] hover:underline">Editar</button>
                  <button type="button" *ngIf="cargo.ativo" (click)="excluir(cargo)" class="text-sm text-red-700 hover:underline">Inativar</button>
                </td>
              </tr>
              <tr *ngIf="cargos.length === 0"><td colspan="5" class="p-12 text-center text-slate-500">Nenhum cargo encontrado.</td></tr>
            </tbody>
          </table>
        </div>
      </section>
      <nav aria-label="Paginação de cargos" class="flex items-center justify-between gap-3">
        <p class="text-xs text-slate-500">Página {{ pagina + 1 }} de {{ totalPaginas }}</p>
        <div class="flex gap-2"><button type="button" [disabled]="loading || pagina === 0" (click)="mudarPagina(-1)" class="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm disabled:opacity-40">Anterior</button>
        <button type="button" [disabled]="loading || pagina + 1 >= totalPaginas" (click)="mudarPagina(1)" class="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm disabled:opacity-40">Próximo</button></div>
      </nav>

      <div *ngIf="editando" class="fixed inset-0 z-[80] flex items-center justify-center p-3 sm:p-5">
        <button type="button" (click)="cancelarEdicao()" class="absolute inset-0 h-full w-full bg-black/55" aria-label="Fechar cadastro de cargo"></button>
        <section cdkTrapFocus [cdkTrapFocusAutoCapture]="true" (keydown.escape)="cancelarEdicao()" role="dialog" aria-modal="true"
          aria-labelledby="cargo-modal-title" class="relative w-full max-w-3xl overflow-hidden rounded-xl bg-white shadow-2xl">
          <form (ngSubmit)="salvar()" #cargoForm="ngForm" class="flex max-h-[92vh] flex-col">
            <header class="px-5 pt-5 pb-4">
              <h2 id="cargo-modal-title" class="text-base font-semibold text-slate-900">{{ cargoId ? 'Editar cargo' : 'Cadastro de cargo' }}</h2>
              <p class="mt-1 text-sm text-slate-500">Preencha as informações para {{ cargoId ? 'atualizar' : 'registrar' }} um cargo de trabalho</p>
            </header>
            <div class="flex-1 overflow-y-auto px-5 pb-4">
              <fieldset class="rounded-lg border border-slate-200 p-4">
                <legend class="px-2 text-sm font-semibold text-slate-500">Dados gerais</legend>
                <div class="grid grid-cols-1 gap-4 sm:grid-cols-[2fr_1fr]">
                  <label class="flex flex-col gap-2 text-sm font-medium text-slate-900">Descrição do cargo <span class="text-red-600">*</span>
                    <input name="nome" required maxlength="100" [(ngModel)]="form.nome" placeholder="Informe uma descrição do cargo"
                      class="h-11 w-full rounded-lg border border-slate-300 px-3 font-normal outline-none focus:border-blue-700" />
                  </label>
                  <label class="flex flex-col gap-2 text-sm font-medium text-slate-900">Código CBO <span class="text-red-600">*</span>
                    <input name="codigoCbo" required maxlength="10" [(ngModel)]="form.codigoCbo" placeholder="Informe um código CBO"
                      class="h-11 w-full rounded-lg border border-slate-300 px-3 font-normal outline-none focus:border-blue-700" />
                  </label>
                </div>
              </fieldset>
              <details class="mt-4 rounded-lg border border-slate-200 p-4">
                <summary class="cursor-pointer text-sm font-semibold text-slate-700">Dados complementares do cargo</summary>
                <div class="mt-4 grid grid-cols-1 gap-3 sm:grid-cols-2">
                  <label class="flex flex-col gap-1 text-sm">Área
                    <select name="areaId" [(ngModel)]="form.areaId" class="h-10 rounded-lg border border-slate-300 px-2"><option [ngValue]="undefined">Sem área</option><option *ngFor="let area of areas" [ngValue]="area.id">{{ area.descricao }}</option></select>
                  </label>
                  <label class="flex flex-col gap-1 text-sm">Nível hierárquico <input name="nivel" type="number" min="1" [(ngModel)]="form.nivelHierarquico" class="h-10 rounded-lg border border-slate-300 px-3" /></label>
                  <label class="flex flex-col gap-1 text-sm">Salário mínimo <input name="salarioMinimo" type="number" min="0" step="0.01" [(ngModel)]="form.salarioBaseMinimo" class="h-10 rounded-lg border border-slate-300 px-3" /></label>
                  <label class="flex flex-col gap-1 text-sm">Salário máximo <input name="salarioMaximo" type="number" min="0" step="0.01" [(ngModel)]="form.salarioBaseMaximo" class="h-10 rounded-lg border border-slate-300 px-3" /></label>
                  <label class="flex items-center gap-2 text-sm"><input name="temComissao" type="checkbox" [(ngModel)]="form.temComissao" /> Permite comissão</label>
                  <label class="flex items-center gap-2 text-sm"><input name="ativo" type="checkbox" [(ngModel)]="form.ativo" /> Cargo ativo</label>
                  <label class="flex flex-col gap-1 text-sm sm:col-span-2">Observações <textarea name="descricao" rows="2" [(ngModel)]="form.descricao" maxlength="2000" class="rounded-lg border border-slate-300 p-3"></textarea></label>
                  <label class="flex flex-col gap-1 text-sm sm:col-span-2">Responsabilidades <textarea name="responsabilidades" rows="2" [(ngModel)]="form.responsabilidades" maxlength="4000" class="rounded-lg border border-slate-300 p-3"></textarea></label>
                </div>
                <div *ngIf="cargoId && historico.length" class="mt-4 text-xs text-slate-500">
                  <strong>Histórico</strong><p *ngFor="let item of historico">{{ item.data | date:'short' }} — {{ item.operacao }}</p>
                </div>
              </details>
              <p *ngIf="formError" role="alert" class="mt-4 text-sm text-red-700">{{ formError }}</p>
            </div>
            <footer class="flex justify-end gap-3 border-t border-slate-200 px-5 py-4">
              <button type="button" (click)="cancelarEdicao()" [disabled]="salvando" class="rounded-lg border border-slate-300 px-4 py-2.5 text-sm">Cancelar</button>
              <button type="submit" [disabled]="salvando || cargoForm.invalid" class="rounded-lg bg-[#18328A] px-4 py-2.5 text-sm font-semibold text-white disabled:opacity-40">{{ salvando ? 'Salvando...' : (cargoId ? 'Salvar alterações' : 'Cadastrar cargo') }}</button>
            </footer>
          </form>
        </section>
      </div>
    </main>
  \`

})
export class CargosComponent implements OnInit {
  private readonly service = inject(CargoService);
  private readonly departamentoService = inject(DepartamentoService);
  cargos: CargoResponse[] = [];
  loading = false;
  error = '';
  busca = '';
  filtroAtivo = '';
  areas: Array<{ id: number; descricao: string }> = [];
  historico: Array<{ operacao: string; data: string }> = [];
  pagina = 0;
  totalPaginas = 1;
  private requestVersion = 0;
  editando = false;
  cargoId: number | null = null;
  salvando = false;
  formError = '';
  actionMessage = '';
  form: CargoRequest = { nome: '', temComissao: false, ativo: true };

  abrirNovo(): void {
    if (this.salvando) return;
    this.actionMessage = '';
    this.cargoId = null;
    this.form = { nome: '', temComissao: false, ativo: true };
    this.formError = '';
    this.historico = [];
    this.editando = true;
  }

  abrirEdicao(cargo: CargoResponse): void {
    if (this.salvando) return;
    this.actionMessage = '';
    this.cargoId = cargo.id;
    this.form = { nome: cargo.nome, descricao: cargo.descricao, codigoCbo: cargo.codigoCbo,
      areaId: cargo.areaId, nivelHierarquico: cargo.nivelHierarquico,
      responsabilidades: cargo.responsabilidades, ativo: cargo.ativo, versao: cargo.versao,
      salarioBaseMinimo: cargo.salarioBaseMinimo, salarioBaseMaximo: cargo.salarioBaseMaximo,
      temComissao: !!cargo.temComissao };
    this.formError = '';
    this.historico = [];
    this.service.historico(cargo.id).subscribe({ next: page => this.historico = page.content ?? [] });
    this.editando = true;
  }

  cancelarEdicao(): void { if (!this.salvando) this.editando = false; }

  salvar(): void {
    if (this.salvando) return;
    const nome = (this.form.nome || '').trim();
    if (nome.length < 2) { this.formError = 'Informe um nome com pelo menos 2 caracteres.'; return; }
    if (!this.form.codigoCbo?.trim()) { this.formError = 'Informe o código CBO do cargo.'; return; }
    const minimo = this.form.salarioBaseMinimo;
    const maximo = this.form.salarioBaseMaximo;
    if ([minimo, maximo].some(n => n != null && (!Number.isFinite(n) || n < 0)) ||
        (minimo != null && maximo != null && minimo > maximo)) {
      this.formError = 'Confira a faixa salarial informada.';
      return;
    }
    this.salvando = true;
    this.actionMessage = '';
    this.formError = '';
    const request = { ...this.form, nome, codigoCbo: this.form.codigoCbo?.trim() || undefined };
    const chamada = this.cargoId ? this.service.atualizar(this.cargoId, request) : this.service.criar(request);
    chamada.subscribe({
      next: () => {
        this.salvando = false;
        this.editando = false;
        this.actionMessage = 'Cargo salvo com sucesso.';
        this.carregar();
      },
      error: (error: HttpErrorResponse) => {
        this.salvando = false;
        this.formError = error.status === 403 ? 'Você não tem permissão para administrar cargos.'
          : error.status === 409 ? (error.error?.message || 'Conflito: recarregue ou revise nome e código.')
          : 'Não foi possível salvar o cargo. Tente novamente.';
      }
    });
  }

  excluir(cargo: CargoResponse): void {
    if (this.salvando || !confirm(`Inativar o cargo "${cargo.nome}"? Os colaboradores vinculados serão preservados.`)) return;
    this.salvando = true;
    this.actionMessage = '';
    this.service.excluir(cargo.id).subscribe({
      next: () => { this.salvando = false; this.actionMessage = 'Cargo inativado; vínculos preservados.'; this.carregar(); },
      error: () => { this.salvando = false; this.actionMessage = 'Não foi possível inativar o cargo.'; }
    });
  }


  get cargosFiltrados(): CargoResponse[] { return this.cargos; }
  aplicarFiltros(): void { this.pagina = 0; this.carregar(); }
  areaNome(id?: number): string { return this.areas.find(a => a.id === id)?.descricao || '—'; }

  mudarPagina(diferenca: number): void {
    const destino = this.pagina + diferenca;
    if (destino < 0 || destino >= this.totalPaginas || this.loading) return;
    this.pagina = destino;
    this.carregar();
  }

  ngOnInit(): void {
    this.departamentoService.list({ page: 0, size: 100 }).subscribe({ next: page => this.areas = page.content ?? [] });
    this.carregar();
  }

  carregar(): void {
    const version = ++this.requestVersion;
    this.loading = true;
    this.error = '';
    this.service.list({ page: this.pagina, size: 20, q: this.busca.trim() || undefined,
      ativo: this.filtroAtivo === '' ? undefined : this.filtroAtivo === 'true' }).subscribe({
      next: page => {
        if (version !== this.requestVersion) return;
        this.cargos = page.content ?? [];
        this.totalPaginas = Math.max(1, page.totalPages ?? 1);
        this.loading = false;
      },
      error: () => {
        if (version !== this.requestVersion) return;
        this.cargos = [];
        this.totalPaginas = 1;
        this.loading = false;
        this.error = 'Não foi possível carregar os cargos desta empresa.';
      }
    });
  }
}
