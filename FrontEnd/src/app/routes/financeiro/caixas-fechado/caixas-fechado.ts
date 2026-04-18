import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MatIconModule } from '@angular/material/icon';
import { FinanceiroService } from '../financeiro.service';
import { MessageService, ConfirmationService } from 'primeng/api';

interface RegistroFechamento {
  cod: number;
  dataReferente: string;
  dataHoraOperacao: string;
  responsavel: string;
  saldoInicial: number;
  saldoFinal: number;
  totalEntradas: number;
  totalSaidas: number;
  situacao: string;
}

@Component({
  standalone: true,
  selector: 'app-caixas-fechado',
  imports: [
    CommonModule,
    FormsModule,
    SelectModule,
    InputTextModule,
    DialogModule,
    ToastModule,
    ConfirmDialogModule,
    MatIconModule
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './caixas-fechado.html',
  styleUrls: ['./caixas-fechado.scss'],
})
export class CaixasFechadoComponent implements OnInit {
  private service = inject(FinanceiroService);
  private messageService = inject(MessageService);

  // Dialog state
  detalhesDialogVisible = false;
  fechamentoSelecionado: RegistroFechamento | null = null;

  // Loading state
  loading = false;
  totalRecords = 0;

  // Toolbar
  imprimir() {
    this.messageService.add({ severity: 'info', summary: 'Impressão', detail: 'Gerando relatório para impressão...' });
  }
  imprimirCupom() {
    this.messageService.add({ severity: 'info', summary: 'Cupom', detail: 'Enviando fechamento para a impressora térmica...' });
  }

  // Filtros
  dataInicio = new Date().toISOString().substring(0, 10);
  dataFim = new Date().toISOString().substring(0, 10);
  responsavel = null;
  responsavelOptions = [
    { label: 'TODOS', value: null },
  ];

  registros: RegistroFechamento[] = [];

  ngOnInit() {
      this.buscar();
  }

  buscar() {
    this.loading = true;
    this.service.listFechamentoCaixa({
        dataInicio: this.dataInicio,
        dataFim: this.dataFim,
        page: 0,
        size: 50
    }).subscribe({
        next: (page: any) => {
            let content = page.content || [];

            this.registros = content.map((item: any) => ({
                cod: item.id,
                dataReferente: this.formatDate(item.dataAbertura) || 'Atual',
                dataHoraOperacao: this.formatDateTime(item.dataFechamento),
                responsavel: item.responsavelNome || `Usuário ${item.usuarioResponsavel}`,
                saldoInicial: item.saldoInicial || 0,
                saldoFinal: item.saldoFinal || 0,
                totalEntradas: item.totalEntradas || 0,
                totalSaidas: item.totalSaidas || 0,
                situacao: item.situacao || 'FECHADO'
            }));
            this.totalRecords = page.totalElements || this.registros.length;
            this.loading = false;
        },
        error: (err: any) => {
            console.error('Erro ao buscar fechamentos', err);
            this.loading = false;
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível buscar historico de fechamentos.' });
        }
    });
  }

  visualizarDetalhes(registro: RegistroFechamento) {
    this.fechamentoSelecionado = registro;
    this.detalhesDialogVisible = true;
  }

  formatDate(dateStr: string): string {
    if (!dateStr) return '';
    try {
      const date = new Date(dateStr);
      return date.toLocaleDateString('pt-BR');
    } catch {
      return dateStr;
    }
  }

  formatDateTime(dateStr: string): string {
    if (!dateStr) return '-';
    try {
      const date = new Date(dateStr);
      return date.toLocaleDateString('pt-BR') + ' ' + date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    } catch {
      return dateStr;
    }
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value || 0);
  }

  get totalSaldoInicial(): number {
    return this.registros.reduce((sum, r) => sum + (r.saldoInicial || 0), 0);
  }

  get totalSaldoFinal(): number {
    return this.registros.reduce((sum, r) => sum + (r.saldoFinal || 0), 0);
  }

  get totalEntradas(): number {
    return this.registros.reduce((sum, r) => sum + (r.totalEntradas || 0), 0);
  }

  get totalSaidas(): number {
    return this.registros.reduce((sum, r) => sum + (r.totalSaidas || 0), 0);
  }
}
