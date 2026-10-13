import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { CargoResponse, CargoService } from './cargo.service';

@Component({
  selector: 'app-cargos',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="p-6 max-w-6xl mx-auto">
      <header class="mb-6">
        <h1 class="text-2xl font-semibold">Cargos</h1>
        <p class="text-sm text-slate-500">Cargos dos funcionários, separados dos perfis de acesso ao sistema.</p>
      </header>
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
              <tr *ngFor="let cargo of cargos" class="border-t border-slate-100">
                <td class="p-4 font-medium">{{ cargo.nome }}</td>
                <td class="p-4">{{ cargo.codigoCbo || '—' }}</td>
                <td class="p-4">{{ cargo.salarioBaseMinimo == null ? '—' : (cargo.salarioBaseMinimo | currency:'BRL') }}</td>
                <td class="p-4">{{ cargo.salarioBaseMaximo == null ? '—' : (cargo.salarioBaseMaximo | currency:'BRL') }}</td>
                <td class="p-4">{{ cargo.temComissao ? 'Sim' : 'Não' }}</td>
              </tr>
              <tr *ngIf="cargos.length === 0"><td colspan="5" class="p-8 text-center text-slate-500">Nenhum cargo cadastrado.</td></tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>`
})
export class CargosComponent implements OnInit {
  private readonly service = inject(CargoService);
  cargos: CargoResponse[] = [];
  loading = false;
  error = '';

  ngOnInit(): void { this.carregar(); }

  carregar(): void {
    this.loading = true;
    this.error = '';
    this.service.list({ size: 1000 }).subscribe({
      next: page => {
        this.cargos = page.content ?? [];
        this.loading = false;
      },
      error: () => {
        this.cargos = [];
        this.loading = false;
        this.error = 'Não foi possível carregar os cargos desta empresa.';
      }
    });
  }
}
