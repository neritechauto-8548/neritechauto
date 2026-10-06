import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { DataViewState, NeriTechIcon, NeriTechIconName, PageHeader } from '@shared';

interface AcessoPainel {
  titulo: string;
  descricao: string;
  rota: string;
  icone: NeriTechIconName;
}

interface MetricaPainel {
  rotulo: string;
  descricao: string;
  icone: NeriTechIconName;
}

interface DadosPainelHome {
  titulo: string;
  descricao: string;
  contexto: string;
  metricas: MetricaPainel[];
  acessos: AcessoPainel[];
  aviso: string;
}

@Component({
  standalone: true,
  selector: 'app-painel-home',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, PageHeader, NeriTechIcon, DataViewState],
  templateUrl: './painel-home.html',
  styleUrl: './painel-home.scss',
})
export class PainelHome {
  private readonly rotaAtiva = inject(ActivatedRoute);

  readonly dados = this.rotaAtiva.snapshot.data as DadosPainelHome;
}
