import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { NeriTechIcon, PageHeader } from '@shared';

@Component({
  standalone: true,
  selector: 'app-nova-inspecao',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule, RouterLink, PageHeader, NeriTechIcon],
  templateUrl: './nova-inspecao.html',
  styleUrl: './nova-inspecao.scss',
})
export class NovaInspecao {
  origem = 'OS';
  vinculo = '';
  template = '';
  versaoTemplate = '';
  inspetor = '';
  revisor = '';
  prazo = '';
  visibilidade = 'INTERNA';
  prioridade = 'NORMAL';
  observacoes = '';
  tentouSalvar = false;

  get valida(): boolean {
    return Boolean(this.origem && this.vinculo.trim() && this.template.trim() && this.inspetor.trim());
  }

  revisar(): void {
    this.tentouSalvar = true;
  }

  texto(valor: string): string {
    return valor.trim() || 'Não informado';
  }
}
