import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { Menu } from 'primeng/menu';

@Component({
  selector: 'app-relatorio-receitas-despesas',
  standalone: true,
  imports: [CommonModule, RouterModule, PanelModule, ButtonModule, MenuModule],
  templateUrl: './relatorio-receitas-despesas.html',
  styleUrls: ['./relatorio-receitas-despesas.scss'],
})
export class RelatorioReceitasDespesas {
  @ViewChild('menu') menu!: Menu;

  menuItems = [
    { label: 'Baixar como ...', icon: 'pi pi-download', command: () => this.export('download') },
    { label: 'Salvar como ...', icon: 'pi pi-save', command: () => this.export('save') },
    { label: 'Anotar ...', icon: 'pi pi-pencil', command: () => this.annotate() },
    { label: 'Imprimir', icon: 'pi pi-print', command: () => this.print() },
  ];

  export(type: string) {
    alert(`Exportar gráfico como ${type} (demo)`);
  }
  annotate() {
    alert('Abrir anotação para este gráfico (demo).');
  }
  print() {
    alert('Preparar impressão do gráfico (demo).');
  }
}