import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { RelatoriosService } from '../relatorios.service';

@Component({
  selector: 'app-relatorio-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule, PanelModule, ButtonModule],
  templateUrl: './relatorio-clientes.html',
  styleUrls: ['./relatorio-clientes.scss'],
})
export class RelatorioClientes {
  private relatoriosService = inject(RelatoriosService);
  titulo = 'Relatório de clientes';
  periodoOptions = ['Hoje', 'Últimos 7 dias', 'Este mês', 'Últimos 30 dias', 'Personalizado'];
  periodo = this.periodoOptions[2];
  dataInicial: string | null = null;
  dataFinal: string | null = null;
  consultaOptions = ['Todos', 'Ativos', 'Inativos', 'Com compras', 'Sem compras'];
  consulta = this.consultaOptions[0];

  gerarRelatorio() {
    console.log('Gerando relatório de clientes...');
    this.relatoriosService.gerarRelatorio('clientes', {}).subscribe({
      next: blob => this.relatoriosService.downloadBlob(blob, 'relatorio-clientes.pdf'),
      error: err => console.error(err),
    });
  }
}
