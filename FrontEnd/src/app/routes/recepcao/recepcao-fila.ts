import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { DataViewState, NeriTechIcon, PageHeader } from '@shared';

@Component({
  standalone: true,
  selector: 'app-recepcao-fila',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule, RouterLink, PageHeader, NeriTechIcon, DataViewState],
  templateUrl: './recepcao-fila.html',
  styleUrl: './recepcao-fila.scss',
})
export class RecepcaoFila {
  busca = '';
  origem = '';
  prioridade = '';
  situacao = '';
  data = new Date().toISOString().slice(0, 10);

  get possuiFiltros(): boolean {
    return Boolean(this.busca || this.origem || this.prioridade || this.situacao);
  }

  limparFiltros(): void {
    this.busca = '';
    this.origem = '';
    this.prioridade = '';
    this.situacao = '';
  }
}
