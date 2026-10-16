import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { CargoRequest, CargoResponse, CargoService } from './cargo.service';
import { DepartamentoService } from '../departamentos/departamento.service';

@Component({
  selector: 'app-cargos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="p-6 max-w-6xl mx-auto">
      <header class="mb-6">
        <h1 class="text-2xl font-semibold">Cargos</h1>
        <p class="text-sm text-slate-500">Cargos dos funcionários, separados dos perfis de acesso ao sistema.</p>
      </header>
      <div class="flex flex-wrap items-center gap-3 mb-4">
        <button type="button" class="px-4 py-2 rounded-lg bg-indigo-600 text-white" (click)="abrirNovo()"><i class="ti ti-plus" aria-hidden="true"></i> Novo cargo</button>
        <label for="cargo-busca" class="text-sm font-medium">Buscar cargo</label>
        <input id="cargo-busca" type="search" [(ngModel)]="busca" (keyup.enter)="aplicarFiltros()" placeholder="Nome ou código" class="border rounded-lg px-3 py-2 text-sm" />
        <select aria-label="Filtrar situação" [(ngModel)]="filtroAtivo" (change)="aplicarFiltros()" class="border rounded-lg px-3 py-2 text-sm"><option value="">Todos</option><option value="true">Ativos</option><option value="false">Inativos</option></select>
        <button type="button" class="border rounded-lg px-3 py-2" (click)="aplicarFiltros()">Buscar</button>
        <span class="text-sm text-slate-500">{{ cargos.length }} cargo(s) nesta página</span>
      </div>
      <form *ngIf="editando" (ngSubmit)="salvar()" class="bg-white rounded-xl border border-slate-200 p-5 mb-5 space-y-3" aria-label="Cadastro de cargo">
        <h2 class="font-semibold">{{ cargoId ? 'Editar cargo' : 'Novo cargo' }}</h2>
        <div class="flex flex-wrap gap-3">
          <label class="flex flex-col text-sm gap-1">Nome * <input name="nome" required maxlength="120" [(ngModel)]="form.nome" class="border rounded-lg px-3 py-2" /></label>
          <label class="flex flex-col text-sm gap-1">CBO <input name="codigoCbo" maxlength="12" [(ngModel)]="form.codigoCbo" class="border rounded-lg px-3 py-2" /></label>
          <label class="flex flex-col text-sm gap-1">Área <select name="areaId" [(ngModel)]="form.areaId" class="border rounded-lg px-3 py-2"><option [ngValue]="undefined">Sem área</option><option *ngFor="let area of areas" [ngValue]="area.id">{{ area.descricao }}</option></select></label>
          <label class="flex flex-col text-sm gap-1">Nível <input name="nivel" type="number" min="1" [(ngModel)]="form.nivelHierarquico" class="border rounded-lg px-3 py-2" /></label>
          <label class="flex flex-col text-sm gap-1">Salário mínimo <input name="salarioMinimo" type="number" min="0" step="0.01" [(ngModel)]="form.salarioBaseMinimo" class="border rounded-lg px-3 py-2" /></label>
          <label class="flex flex-col text-sm gap-1">Salário máximo <input name="salarioMaximo" type="number" min="0" step="0.01" [(ngModel)]="form.salarioBaseMaximo" class="border rounded-lg px-3 py-2" /></label>
          <label class="flex items-center gap-2 text-sm"><input name="temComissao" type="checkbox" [(ngModel)]="form.temComissao" /> Permite comissão</label>
          <label class="flex items-center gap-2 text-sm"><input name="ativo" type="checkbox" [(ngModel)]="form.ativo" /> Ativo</label>
        </div>
        <label class="flex flex-col text-sm gap-1">Descrição <textarea name="descricao" maxlength="2000" [(ngModel)]="form.descricao" class="border rounded-lg px-3 py-2"></textarea></label>
        <label class="flex flex-col text-sm gap-1">Responsabilidades <textarea name="responsabilidades" maxlength="4000" [(ngModel)]="form.responsabilidades" class="border rounded-lg px-3 py-2"></textarea></label>
        <p class="text-xs text-slate-500">Cargo organizacional não concede permissões. Perfis de acesso são administrados separadamente.</p>
        <section *ngIf="cargoId && historico.length" aria-labelledby="historico-cargo" class="border-t pt-3">
          <h3 id="historico-cargo" class="text-sm font-semibold">Alterações recentes</h3>
          <ul class="text-xs text-slate-600"><li *ngFor="let item of historico">{{ item.data | date:'short' }} — {{ item.operacao }}</li></ul>
        </section>
        <p *ngIf="formError" role="alert" class="text-red-700 text-sm">{{ formError }}</p>
        <div class="flex gap-2">
          <button type="submit" [disabled]="salvando" class="px-4 py-2 bg-indigo-600 text-white rounded-lg disabled:opacity-50">{{ salvando ? 'Salvando...' : 'Salvar cargo' }}</button>
          <button type="button" [disabled]="salvando" class="px-4 py-2 border rounded-lg" (click)="editando = false">Cancelar</button>
        </div>
      </form>
      <p *ngIf="actionMessage" role="status" class="mb-3 text-sm">{{ actionMessage }}</p>
      <div class="bg-white rounded-xl border border-slate-200 overflow-hidden">
        <div *ngIf="loading" class="p-8 text-center text-slate-500" role="status">Carregando cargos...</div>
        <div *ngIf="error && !loading" class="p-8 text-center" role="alert">
          <p class="text-red-700 mb-3">{{ error }}</p>
          <button type="button" class="px-4 py-2 border rounded-lg" (click)="carregar()">Tentar novamente</button>
        </div>
        <div *ngIf="!loading && !error" class="overflow-x-auto">
          <table class="w-full text-sm">
            <thead class="bg-slate-50 text-left">
              <tr><th scope="col" class="p-4">Cargo</th><th scope="col" class="p-4">Código</th><th scope="col" class="p-4">Área</th><th scope="col" class="p-4">Funcionários</th><th scope="col" class="p-4">Perfis de acesso</th><th scope="col" class="p-4">Ativo</th><th scope="col" class="p-4">Alterado</th><th scope="col" class="p-4">Ações</th></tr>
            </thead>
            <tbody>
              <tr *ngFor="let cargo of cargosFiltrados" class="border-t border-slate-100">
                <td class="p-4 font-medium">{{ cargo.nome }}</td>
                <td class="p-4">{{ cargo.codigoCbo || '—' }}</td>
                <td class="p-4">{{ areaNome(cargo.areaId) }}</td>
                <td class="p-4">{{ cargo.numeroFuncionarios || 0 }}</td>
                <td class="p-4 text-slate-500">Gerenciados separadamente</td>
                <td class="p-4">{{ cargo.ativo ? 'Sim' : 'Não' }}</td>
                <td class="p-4">{{ cargo.dataAtualizacao ? (cargo.dataAtualizacao | date:'short') : '—' }}</td>
                <td class="p-4"><button type="button" class="underline mr-3" [disabled]="salvando" (click)="abrirEdicao(cargo)">Editar</button><button type="button" *ngIf="cargo.ativo" [disabled]="salvando" class="underline text-red-700" (click)="excluir(cargo)">Inativar</button></td>
              </tr>
              <tr *ngIf="cargos.length === 0"><td colspan="8" class="p-8 text-center text-slate-500">Nenhum cargo encontrado.</td></tr>
            </tbody>
          </table>
        </div>
      </div>
      <nav class="flex items-center justify-between mt-4" aria-label="Paginação de cargos">
        <button type="button" class="border rounded-lg px-4 py-2 disabled:opacity-40" [disabled]="loading || pagina === 0" (click)="mudarPagina(-1)">Anterior</button>
        <span class="text-sm">Página {{ pagina + 1 }} de {{ totalPaginas }}</span>
        <button type="button" class="border rounded-lg px-4 py-2 disabled:opacity-40" [disabled]="loading || pagina + 1 >= totalPaginas" (click)="mudarPagina(1)">Próxima</button>
      </nav>
    </section>`
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

  salvar(): void {
    if (this.salvando) return;
    const nome = (this.form.nome || '').trim();
    if (nome.length < 2) { this.formError = 'Informe um nome com pelo menos 2 caracteres.'; return; }
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
    this.busca = '';
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
