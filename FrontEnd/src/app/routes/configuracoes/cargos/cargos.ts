import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CargoRequest, CargoResponse, CargoService } from './cargo.service';

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
        <button type="button" class="px-4 py-2 rounded-lg bg-indigo-600 text-white" (click)="abrirNovo()">Novo cargo</button>
        <label for="cargo-busca" class="text-sm font-medium">Buscar cargo</label>
        <input id="cargo-busca" type="search" [(ngModel)]="busca" placeholder="Nome ou CBO" class="border rounded-lg px-3 py-2 text-sm" />
        <span class="text-sm text-slate-500">{{ cargosFiltrados.length }} cargo(s) nesta página</span>
      </div>
      <form *ngIf="editando" (ngSubmit)="salvar()" class="bg-white rounded-xl border border-slate-200 p-5 mb-5 space-y-3" aria-label="Cadastro de cargo">
        <h2 class="font-semibold">{{ cargoId ? 'Editar cargo' : 'Novo cargo' }}</h2>
        <div class="flex flex-wrap gap-3">
          <label class="flex flex-col text-sm gap-1">Nome * <input name="nome" required maxlength="120" [(ngModel)]="form.nome" class="border rounded-lg px-3 py-2" /></label>
          <label class="flex flex-col text-sm gap-1">CBO <input name="codigoCbo" maxlength="12" [(ngModel)]="form.codigoCbo" class="border rounded-lg px-3 py-2" /></label>
          <label class="flex flex-col text-sm gap-1">Salário mínimo <input name="salarioMinimo" type="number" min="0" step="0.01" [(ngModel)]="form.salarioBaseMinimo" class="border rounded-lg px-3 py-2" /></label>
          <label class="flex flex-col text-sm gap-1">Salário máximo <input name="salarioMaximo" type="number" min="0" step="0.01" [(ngModel)]="form.salarioBaseMaximo" class="border rounded-lg px-3 py-2" /></label>
          <label class="flex items-center gap-2 text-sm"><input name="temComissao" type="checkbox" [(ngModel)]="form.temComissao" /> Permite comissão</label>
        </div>
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
              <tr><th scope="col" class="p-4">Cargo</th><th scope="col" class="p-4">CBO</th><th scope="col" class="p-4">Salário mínimo</th><th scope="col" class="p-4">Salário máximo</th><th scope="col" class="p-4">Comissão</th><th scope="col" class="p-4">Ações</th></tr>
            </thead>
            <tbody>
              <tr *ngFor="let cargo of cargosFiltrados" class="border-t border-slate-100">
                <td class="p-4 font-medium">{{ cargo.nome }}</td>
                <td class="p-4">{{ cargo.codigoCbo || '—' }}</td>
                <td class="p-4">{{ cargo.salarioBaseMinimo == null ? '—' : (cargo.salarioBaseMinimo | currency:'BRL') }}</td>
                <td class="p-4">{{ cargo.salarioBaseMaximo == null ? '—' : (cargo.salarioBaseMaximo | currency:'BRL') }}</td>
                <td class="p-4">{{ cargo.temComissao ? 'Sim' : 'Não' }}</td>
                <td class="p-4"><button type="button" class="underline mr-3" [disabled]="salvando" (click)="abrirEdicao(cargo)">Editar</button><button type="button" [disabled]="salvando" class="underline text-red-700" (click)="excluir(cargo)">Excluir</button></td>
              </tr>
              <tr *ngIf="cargosFiltrados.length === 0"><td colspan="6" class="p-8 text-center text-slate-500">{{ busca ? 'Nenhum cargo corresponde à busca nesta página.' : 'Nenhum cargo cadastrado nesta página.' }}</td></tr>
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
  cargos: CargoResponse[] = [];
  loading = false;
  error = '';
  busca = '';
  pagina = 0;
  totalPaginas = 1;
  private requestVersion = 0;
  editando = false;
  cargoId: number | null = null;
  salvando = false;
  formError = '';
  actionMessage = '';
  form: CargoRequest = { nome: '', temComissao: false };

  abrirNovo(): void {
    if (this.salvando) return;
    this.actionMessage = '';
    this.cargoId = null;
    this.form = { nome: '', temComissao: false };
    this.formError = '';
    this.editando = true;
  }

  abrirEdicao(cargo: CargoResponse): void {
    if (this.salvando) return;
    this.actionMessage = '';
    this.cargoId = cargo.id;
    this.form = { nome: cargo.nome, codigoCbo: cargo.codigoCbo,
      salarioBaseMinimo: cargo.salarioBaseMinimo, salarioBaseMaximo: cargo.salarioBaseMaximo,
      temComissao: !!cargo.temComissao };
    this.formError = '';
    this.editando = true;
  }

  salvar(): void {
    if (this.salvando) return;
    const nome = this.form.nome.trim();
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
    const request = { ...this.form, nome };
    const chamada = this.cargoId ? this.service.atualizar(this.cargoId, request) : this.service.criar(request);
    chamada.subscribe({
      next: () => {
        this.salvando = false;
        this.editando = false;
        this.actionMessage = 'Cargo salvo com sucesso.';
        this.carregar();
      },
      error: () => {
        this.salvando = false;
        this.formError = 'Não foi possível salvar o cargo. Confira sua permissão e tente novamente.';
      }
    });
  }

  excluir(cargo: CargoResponse): void {
    if (this.salvando || !confirm(`Excluir o cargo "${cargo.nome}"?`)) return;
    this.salvando = true;
    this.actionMessage = '';
    this.service.excluir(cargo.id).subscribe({
      next: () => { this.salvando = false; this.actionMessage = 'Cargo excluído.'; this.carregar(); },
      error: () => { this.salvando = false; this.actionMessage = 'Não foi possível excluir o cargo. Verifique vínculos e permissões.'; }
    });
  }


  get cargosFiltrados(): CargoResponse[] {
    const busca = this.busca.trim().toLocaleLowerCase('pt-BR');
    return busca ? this.cargos.filter(c => (c.nome + ' ' + (c.codigoCbo || '')).toLocaleLowerCase('pt-BR').includes(busca)) : this.cargos;
  }

  mudarPagina(diferenca: number): void {
    const destino = this.pagina + diferenca;
    if (destino < 0 || destino >= this.totalPaginas || this.loading) return;
    this.pagina = destino;
    this.busca = '';
    this.carregar();
  }

  ngOnInit(): void { this.carregar(); }

  carregar(): void {
    const version = ++this.requestVersion;
    this.loading = true;
    this.error = '';
    this.service.list({ page: this.pagina, size: 20 }).subscribe({
      next: page => {
        if (version !== this.requestVersion) return;
        this.cargos = page.content ?? [];
        this.totalPaginas = Math.max(1, page.totalPages ?? 1);
        this.loading = false;
      },
      error: () => {
        if (version !== this.requestVersion) return;
        this.cargos = [];
        this.loading = false;
        this.error = 'Não foi possível carregar os cargos desta empresa.';
      }
    });
  }
}
