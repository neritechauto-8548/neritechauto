import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { DataViewState, NeriTechIcon, PageHeader } from '@shared';

@Component({
  standalone: true,
  selector: 'app-inspecoes-central',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule, RouterLink, PageHeader, NeriTechIcon, DataViewState],
  templateUrl: './inspecoes-central.html',
  styleUrl: './inspecoes-central.scss',
})
export class InspecoesCentral {
  readonly abas = ['Pendentes', 'Em execução', 'Em revisão', 'Concluídas'];
  abaAtiva = 'Pendentes';
  busca = '';
  unidade = '';
  origem = '';
  severidade = '';
  inspetor = '';
  evidencia = '';
  periodoInicio = '';
  periodoFim = '';
  filtrosAvancados = false;
  densidade: 'confortavel' | 'compacta' = 'confortavel';

  get filtrosAplicados(): number {
    return [this.busca, this.unidade, this.origem, this.severidade, this.inspetor, this.evidencia, this.periodoInicio, this.periodoFim].filter(Boolean).length;
  }

  limparFiltros(): void {
    this.busca = '';
    this.unidade = '';
    this.origem = '';
    this.severidade = '';
    this.inspetor = '';
    this.evidencia = '';
    this.periodoInicio = '';
    this.periodoFim = '';
  }
}
