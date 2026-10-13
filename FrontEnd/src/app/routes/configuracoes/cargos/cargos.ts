import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CargoResponse, CargoService } from './cargo.service';

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
        <label for="cargo-busca" class="text-sm font-medium">Buscar cargo</label>
        <input id="cargo-busca" type="search" [(ngModel)]="busca" placeholder="Nome ou CBO" class="border rounded-lg px-3 py-2 text-sm" />
        <span class="text-sm text-slate-500">{{ cargosFiltrados.length }} cargo(s) nesta página</span>
      </div>
      <div class="bg-white rounded-xl border border-slate-200 overflow-hidden">
        <div *ngIf="loading" class="p-8 text-center text-slate-500" role="status">Carregando cargos...</div>
        <div *ngIf="error && !loading" class="p-8 text-center" role="alert">
          <p class="text-red-700 mb-3">{{ error }}</p>
          <button type="button" class="px-4 py-2 border rounded-lg" (click)="carregar()">Tentar novamente</button>
        </div>
        <div *ngIf="!loading && !error" class="overflow-x-auto">
          <table class="w-full text-sm">
            <thead class="bg-slate-50 text-left">
              <tr><th scope="col" class="p-4">Cargo</th><th scope="col" class="p-4">CBO</th><th scope="col" class="p-4">Salário mínimo</th><th scope="col" class="p-4">Salário máximo</th><th scope="col" class="p-4">Comissão</th></tr>
            </thead>
            <tbody>
              <tr *ngFor="let cargo of cargosFiltrados" class="border-t border-slate-100">
                <td class="p-4 font-medium">{{ cargo.nome }}</td>
                <td class="p-4">{{ cargo.codigoCbo || '—' }}</td>
                <td class="p-4">{{ cargo.salarioBaseMinimo == null ? '—' : (cargo.salarioBaseMinimo | currency:'BRL') }}</td>
                <td class="p-4">{{ cargo.salarioBaseMaximo == null ? '—' : (cargo.salarioBaseMaximo | currency:'BRL') }}</td>
                <td class="p-4">{{ cargo.temComissao ? 'Sim' : 'Não' }}</td>
              </tr>
              <tr *ngIf="cargosFiltrados.length === 0"><td colspan="5" class="p-8 text-center text-slate-500">{{ busca ? 'Nenhum cargo corresponde à busca nesta página.' : 'Nenhum cargo cadastrado nesta página.' }}</td></tr>
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
