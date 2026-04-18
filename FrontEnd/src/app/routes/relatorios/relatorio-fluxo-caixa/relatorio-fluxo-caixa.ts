import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { Menu } from 'primeng/menu';

@Component({
  selector: 'app-relatorio-fluxo-caixa',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, PanelModule, ButtonModule, MenuModule],
  templateUrl: './relatorio-fluxo-caixa.html',
  styleUrls: ['./relatorio-fluxo-caixa.scss'],
})
export class RelatorioFluxoCaixa {
  @ViewChild('menu') menu!: Menu;

  period = 'Mensal';
  year = 2025;
  monthIndex = 10; // 0=Jan ... 10=Nov
  account = 'Todas';
  mode = 'Previsto e Realizado';

  days: number[] = [];
  categories = [
    'Saldo do mês anterior',
    'Outras receitas',
    'Receitas de vendas',
    'Receitas de serviços',
    'Total de receitas',
    'Outras despesas',
    'Despesas com comissões',
    'Total de despesas',
    'Saldo',
  ];

  menuItems = [
    { label: 'Baixar como ...', icon: 'pi pi-download', command: () => this.export('download') },
    { label: 'Salvar como ...', icon: 'pi pi-save', command: () => this.export('save') },
    { label: 'Anotar ...', icon: 'pi pi-pencil', command: () => this.annotate() },
    { label: 'Imprimir', icon: 'pi pi-print', command: () => this.print() },
  ];

  constructor() { this.updateDays(); }

  get isDaily() { return this.period === 'Diário'; }

  setPeriod(p: 'Diário' | 'Mensal') {
    this.period = p;
    this.updateDays();
  }

  setMonth(idx: number) {
    this.monthIndex = idx;
    this.updateDays();
  }

  updateDays() {
    const count = new Date(this.year, this.monthIndex + 1, 0).getDate();
    this.days = Array.from({ length: count }, (_, i) => i + 1);
  }

  value(cat: string, d: number, type: 'previsto' | 'realizado'): number {
    // Demonstração simples: receita 100 no dia 1; despesa -4900 no dia 1
    const isReceita = cat === 'Outras receitas' || cat === 'Total de receitas';
    const isDespesa = cat === 'Outras despesas' || cat === 'Total de despesas' || cat === 'Saldo';
    if (d === 1) {
      if (isReceita && type === 'realizado') return 100;
      if (isDespesa && type === 'realizado') return -4900;
    }
    return 0;
  }

  formatBR(v: number) {
    return v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }

  buscar() {
    alert(`Buscar ${this.period} ${this.year} | ${this.account} | ${this.mode} (demo)`);
  }
  export(type: string) { alert(`Exportar tabela/gráfico como ${type} (demo)`); }
  annotate() { alert('Abrir anotação (demo)'); }
  print() { alert('Preparar impressão (demo)'); }
}