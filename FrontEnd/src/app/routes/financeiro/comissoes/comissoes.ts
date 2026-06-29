import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SelectModule } from 'primeng/select';
import { DatePickerModule } from 'primeng/datepicker';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';
import { HttpClient } from '@angular/common/http';
import { LocalStorageService } from '@shared/services/storage.service';

import { RelatoriosService } from '../../relatorios/relatorios.service';
import { FuncionarioService } from '../../configuracoes/colaboradores/funcionario.service';

export interface LinhaComissao {
  funcionarioId: number;
  funcionarioNome: string;
  totalOrdens: number;
  totalFaturado: number;
  percentual: number;
  totalComissao: number;
}

@Component({
  standalone: true,
  selector: 'app-comissoes',
  templateUrl: './comissoes.html',
  imports: [
    CommonModule, FormsModule, SelectModule, DatePickerModule, ButtonModule,
    ToastModule, MatIconModule
  ],
  providers: [MessageService]
})
export class ComissoesComponent implements OnInit {
  private relatoriosService = inject(RelatoriosService);
  private funcionarioService = inject(FuncionarioService);
  private messageService = inject(MessageService);
  private http = inject(HttpClient);
  private storage = inject(LocalStorageService);

  funcionarios: { label: string; value: number | null }[] = [];
  funcionario: number | null = null;
  
  dataInicial: Date | null = new Date(new Date().getFullYear(), new Date().getMonth(), 1);
  dataFinal: Date | null = new Date();

  gerando = false;
  pdfLoading = false;
  resumoVisivel = false;
  linhas: LinhaComissao[] = [];

  // Totais calculados a partir das linhas
  get totalOrdens(): number { return this.linhas.reduce((s, l) => s + l.totalOrdens, 0); }
  get totalFaturado(): number { return this.linhas.reduce((s, l) => s + l.totalFaturado, 0); }
  get totalComissoes(): number { return this.linhas.reduce((s, l) => s + l.totalComissao, 0); }
  get totalFuncionarios(): number { return this.linhas.length; }

  ngOnInit() {
    this.carregarFuncionarios();
    this.gerarRelatorio();
  }

  carregarFuncionarios() {
    this.funcionarioService.list({ page: 0, size: 100 }).subscribe({
      next: data => {
        if (data.content) {
          this.funcionarios = data.content.map(f => ({ label: f.nomeCompleto, value: f.id }));
          this.funcionarios.unshift({ label: 'Todos os funcionários', value: null });
        }
      },
      error: err => console.error('Erro ao listar funcionários', err),
    });
  }

  private toISO(d: Date | null): string | undefined {
    if (!d) return undefined;
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${day}`;
  }

  gerarRelatorio(): void {
    if (!this.dataInicial || !this.dataFinal) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha a data inicial e final.' });
      return;
    }

    this.gerando = true;
    this.resumoVisivel = false;

    let empresaId = this.storage.get('tenantId')?.id || this.storage.get('tenantId') || this.storage.get('empresaId')?.id || this.storage.get('empresaId');
    if (empresaId && typeof empresaId === 'object') {
      empresaId = empresaId.id;
    }

    const params: any = {
      empresaId: empresaId || '7',
      page: 0,
      size: 10000
    };

    this.http.get<any>('/v1/financeiro/comissoes', { params }).subscribe({
      next: (res) => {
        const list = res.content || [];
        const iniStr = this.toISO(this.dataInicial);
        const fimStr = this.toISO(this.dataFinal);

        const filtered = list.filter((c: any) => {
          const matchesFunc = !this.funcionario || c.funcionario?.id === this.funcionario;
          const dateComp = c.dataCompetencia; // YYYY-MM-DD
          const matchesDate = (!iniStr || dateComp >= iniStr) && 
                              (!fimStr || dateComp <= fimStr);
          return matchesFunc && matchesDate;
        });

        // Group by employee to populate this.linhas
        const grupos: { [key: number]: LinhaComissao } = {};
        filtered.forEach((c: any) => {
          const fId = c.funcionario?.id || 0;
          const fNome = c.funcionario?.nomeCompleto || 'N/A';
          if (!grupos[fId]) {
            grupos[fId] = {
              funcionarioId: fId,
              funcionarioNome: fNome,
              totalOrdens: 0,
              totalFaturado: 0,
              percentual: c.percentualComissao || 0,
              totalComissao: 0
            };
          }
          grupos[fId].totalComissao += Number(c.valorComissao || 0);
          grupos[fId].totalFaturado += Number(c.baseCalculo || 0);
          if (c.ordemServicoId) {
            grupos[fId].totalOrdens += 1;
          }
        });

        this.linhas = Object.values(grupos);
        this.gerando = false;
        this.resumoVisivel = true;
      },
      error: (err) => {
        console.error(err);
        this.gerando = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar dados do relatório.' });
      }
    });
  }

  imprimirRelatorio(): void {
    this.pdfLoading = true;
    const payload: any = {};
    if (this.funcionario) {
      payload.funcionarioId = this.funcionario;
    }
    const iniStr = this.toISO(this.dataInicial);
    const fimStr = this.toISO(this.dataFinal);
    if (iniStr) {
      payload.dataInicio = iniStr;
    }
    if (fimStr) {
      payload.dataFim = fimStr;
    }

    this.relatoriosService.gerarRelatorio('comissoes', payload).subscribe({
      next: blob => {
        this.pdfLoading = false;
        this.relatoriosService.abrirBlobEmNovaAba(blob);
      },
      error: err => {
        console.error(err);
        this.pdfLoading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível gerar o PDF.' });
      }
    });
  }

  limparFiltros() {
    this.funcionario = null;
    this.dataInicial = new Date(new Date().getFullYear(), new Date().getMonth(), 1);
    this.dataFinal = new Date();
    this.gerarRelatorio();
  }

  formatCurrency(v: number): string {
    return (v ?? 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}
