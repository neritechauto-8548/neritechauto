import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { DataViewState, NeriTechIcon, PageHeader } from '@shared';

@Component({
  standalone: true,
  selector: 'app-registrar-decisao',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule, RouterLink, PageHeader, NeriTechIcon, DataViewState],
  templateUrl: './registrar-decisao.html',
  styleUrl: './registrar-decisao.scss',
})
export class RegistrarDecisao {
  orcamento = '';
  versao = '';
  metodo = 'TELEFONE';
  recebidoEm = '';
  contato = '';
  evidencia = '';
  resumo = '';
  proximoRetorno = '';
  responsavel = '';
  atestacao = false;
  tentouRevisar = false;

  get contextoSelecionado(): boolean {
    return Boolean(this.orcamento.trim() && this.versao.trim());
  }

  get camposValidos(): boolean {
    return this.contextoSelecionado && Boolean(this.metodo && this.recebidoEm && this.contato.trim() && this.resumo.trim() && this.atestacao);
  }

  revisar(): void {
    this.tentouRevisar = true;
  }

  texto(valor: string): string {
    return valor.trim() || 'Não informado';
  }
}
