import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';

interface YesNoStats { sim: number; nao: number; }
interface NpsStats { counts: number[]; total: number; detratores: number; passivos: number; promotores: number; nps: number; }

@Component({
  selector: 'app-relatorio-questionarios',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ButtonModule],
  templateUrl: './relatorio-questionarios.html',
  styleUrls: ['./relatorio-questionarios.scss'],
})
export class RelatorioQuestionarios {
  // Filtros
  dataInicial = '';
  dataFinal = '';
  questionarioId = '2';
  tipoRelatorio = 'Grafico geral';

  // Dados demo (zerados para coincidir com a imagem)
  q1: YesNoStats = { sim: 0, nao: 0 };
  q2: YesNoStats = { sim: 0, nao: 0 };

  nps: NpsStats = this.computeNps(Array(10).fill(0));
  comentarios: string[] = [];

  buscar() {
    // Mantém dados demo; aqui integrar serviço real futuramente
    this.nps = this.computeNps(this.nps.counts);
  }

  percentYesNo(s: YesNoStats, key: 'sim'|'nao'): string {
    const total = s.sim + s.nao;
    const val = s[key];
    const pct = total === 0 ? 0 : (val / total) * 100;
    return `${pct.toFixed(1)}%`;
  }

  private computeNps(counts: number[]): NpsStats {
    const total = counts.reduce((a, b) => a + b, 0);
    const detratores = counts.slice(0, 6).reduce((a,b)=>a+b,0);
    const passivos = counts.slice(6, 8).reduce((a,b)=>a+b,0);
    const promotores = counts.slice(8, 10).reduce((a,b)=>a+b,0);
    const nps = total === 0 ? 0 : ((promotores - detratores) / total) * 100;
    return { counts, total, detratores, passivos, promotores, nps };
  }
}