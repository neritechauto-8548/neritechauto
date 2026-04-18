import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';

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
    CommonModule, FormsModule, SelectModule, InputTextModule, ButtonModule,
    ToastModule, MatIconModule
  ],
  providers: [MessageService]
})
export class ComissoesComponent implements OnInit {
  private relatoriosService = inject(RelatoriosService);
  private funcionarioService = inject(FuncionarioService);
  private messageService = inject(MessageService);

  funcionarios: { label: string; value: number | null }[] = [];
  funcionario: number | null = null;
  dataInicial = new Date(new Date().getFullYear(), new Date().getMonth(), 1)
    .toISOString()
    .substring(0, 10);
  dataFinal = new Date().toISOString().substring(0, 10);

  gerando = false;
  resumoVisivel = false;
  linhas: LinhaComissao[] = [];

  // Totais calculados a partir das linhas
  get totalOrdens(): number { return this.linhas.reduce((s, l) => s + l.totalOrdens, 0); }
  get totalFaturado(): number { return this.linhas.reduce((s, l) => s + l.totalFaturado, 0); }
  get totalComissoes(): number { return this.linhas.reduce((s, l) => s + l.totalComissao, 0); }
  get totalFuncionarios(): number { return this.linhas.length; }

  ngOnInit() {
    this.carregarFuncionarios();
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

  gerarRelatorio(): void {
    if (!this.dataInicial || !this.dataFinal) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha a data inicial e final.' });
      return;
    }

    this.gerando = true;
    this.resumoVisivel = false;

    const payload: any = {};
    if (this.funcionario) {
      payload.funcionarioId = this.funcionario;
    }

    this.relatoriosService.gerarRelatorio('comissoes', payload).subscribe({
      next: blob => {
        this.relatoriosService.downloadBlob(blob, 'relatorio-comissoes.pdf');
        this.gerando = false;
        this.resumoVisivel = true;
        // Simulação de linhas de resumo (substituir quando API retornar dados estruturados)
        this.linhas = [];
        this.messageService.add({ severity: 'success', summary: 'Relatório gerado!', detail: 'PDF baixado com sucesso.' });
      },
      error: err => {
        console.error(err);
        this.gerando = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível gerar o relatório. Verifique o console.' });
      },
    });
  }

  formatCurrency(v: number): string {
    return (v ?? 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}
