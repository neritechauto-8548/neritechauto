import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { NeriTechIcon, PageHeader } from '@shared';

interface CheckInForm {
  origem: 'AGENDAMENTO' | 'ESPONTANEO';
  agendamentoId: string;
  cliente: string;
  veiculo: string;
  unidade: string;
  queixa: string;
  quilometragem: number | null;
  combustivel: string;
  chegada: string;
  prioridade: string;
  responsavel: string;
  previsaoAvaliacao: string;
  checklistRecepcao: boolean;
  observacoes: string;
}

@Component({
  standalone: true,
  selector: 'app-recepcao-check-in',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule, RouterLink, PageHeader, NeriTechIcon],
  templateUrl: './recepcao-check-in.html',
  styleUrl: './recepcao-check-in.scss',
})
export class RecepcaoCheckIn {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly etapas = ['Contexto', 'Chegada', 'Planejamento', 'Revisão'];
  etapaAtual = 0;
  maiorEtapaVisitada = 0;
  tentouAvancar = false;

  readonly formulario: CheckInForm = {
    origem: this.route.snapshot.paramMap.has('agendamentoId') ? 'AGENDAMENTO' : 'ESPONTANEO',
    agendamentoId: this.route.snapshot.paramMap.get('agendamentoId') || '',
    cliente: '',
    veiculo: '',
    unidade: '',
    queixa: '',
    quilometragem: null,
    combustivel: '',
    chegada: this.agoraLocal(),
    prioridade: 'NORMAL',
    responsavel: '',
    previsaoAvaliacao: '',
    checklistRecepcao: true,
    observacoes: '',
  };

  irParaEtapa(indice: number): void {
    if (indice <= this.maiorEtapaVisitada) {
      this.etapaAtual = indice;
      this.tentouAvancar = false;
    }
  }

  voltar(): void {
    if (this.etapaAtual === 0) {
      this.router.navigate(['/recepcao/fila']);
      return;
    }
    this.etapaAtual -= 1;
    this.tentouAvancar = false;
  }

  avancar(): void {
    this.tentouAvancar = true;
    if (!this.etapaValida(this.etapaAtual)) return;
    this.etapaAtual = Math.min(this.etapaAtual + 1, this.etapas.length - 1);
    this.maiorEtapaVisitada = Math.max(this.maiorEtapaVisitada, this.etapaAtual);
    this.tentouAvancar = false;
  }

  etapaValida(etapa: number): boolean {
    if (etapa === 0) {
      return Boolean(
        this.formulario.cliente.trim() &&
        this.formulario.veiculo.trim() &&
        this.formulario.unidade.trim() &&
        (this.formulario.origem !== 'AGENDAMENTO' || this.formulario.agendamentoId.trim())
      );
    }
    if (etapa === 1) {
      return Boolean(
        this.formulario.queixa.trim() &&
        this.formulario.quilometragem !== null &&
        this.formulario.quilometragem >= 0 &&
        this.formulario.combustivel &&
        this.formulario.chegada
      );
    }
    if (etapa === 2) return Boolean(this.formulario.responsavel.trim());
    return true;
  }

  textoOuPendente(valor: string | number | null): string {
    return valor === null || String(valor).trim() === '' ? 'Não informado' : String(valor);
  }

  private agoraLocal(): string {
    const agora = new Date();
    agora.setMinutes(agora.getMinutes() - agora.getTimezoneOffset());
    return agora.toISOString().slice(0, 16);
  }
}
