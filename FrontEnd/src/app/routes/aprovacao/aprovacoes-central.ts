import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { DataViewState, NeriTechIcon, PageHeader } from '@shared';

@Component({
  standalone: true,
  selector: 'app-aprovacoes-central',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule, RouterLink, PageHeader, NeriTechIcon, DataViewState],
  templateUrl: './aprovacoes-central.html',
  styleUrl: './aprovacoes-central.scss',
})
export class AprovacoesCentral {
  readonly filas = [
    'Aguardando ação', 'Não enviado', 'Falha de entrega', 'Não visualizado',
    'Visualizado', 'Parcial', 'Aprovado', 'Recusado', 'Expirado',
  ];
  readonly indicadores = [
    { nome: 'Não visualizados', definicao: 'Versões entregues sem visualização confiável.' },
    { nome: 'Aguardando decisão', definicao: 'Visualizados sem decisão consolidada.' },
    { nome: 'Aprovações parciais', definicao: 'Escopo com itens aprovados e pendentes ou recusados.' },
    { nome: 'Aprovados prontos', definicao: 'Escopo elegível ainda não convertido em OS.' },
  ];

  filaAtiva = 'Aguardando ação';
  busca = '';
  unidade = '';
  responsavel = '';
  prioridade = '';
  validade = '';
  canal = '';
  filtrosAvancados = false;

  get filtrosAplicados(): number {
    return [this.busca, this.unidade, this.responsavel, this.prioridade, this.validade, this.canal]
      .filter(Boolean).length;
  }

  limparFiltros(): void {
    this.busca = '';
    this.unidade = '';
    this.responsavel = '';
    this.prioridade = '';
    this.validade = '';
    this.canal = '';
  }
}
