import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CardModule } from 'primeng/card';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DatePickerModule } from 'primeng/datepicker';
import { FinanceiroService } from '../../financeiro/financeiro.service';

@Component({
  selector: 'app-relatorio-contas',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    CardModule,
    SelectModule,
    InputTextModule,
    ButtonModule,
    CheckboxModule,
    ToastModule,
    DatePickerModule
  ],
  providers: [MessageService],
  templateUrl: './relatorio-contas.html',
  styleUrls: ['./relatorio-contas.scss'],
})
export class RelatorioContas {
  private financeiroService = inject(FinanceiroService);
  private messageService = inject(MessageService);

  // Campos conforme imagem de referência
  dataDe = 'VENCIMENTO';
  dataDeOptions = [
    { label: 'Vencimento', value: 'VENCIMENTO' },
    { label: 'Emissão', value: 'EMISSAO' },
    { label: 'Pagamento', value: 'PAGAMENTO' },
  ];

  dataInicial: Date | null = null;
  dataFinal: Date | null = null;

  situacaoTipo = 'TODOS';
  situacaoTipoOptions = [
    { label: 'TODOS', value: 'TODOS' },
    { label: 'A RECEBER', value: 'A_RECEBER' },
    { label: 'A PAGAR', value: 'A_PAGAR' },
    { label: 'PENDENTE', value: 'PENDENTE' },
    { label: 'PAGO', value: 'PAGO' },
    { label: 'CANCELADO', value: 'CANCELADO' },
  ];

  departamento = 'TODOS';
  departamentoOptions = [
    { label: 'TODOS', value: 'TODOS' },
    { label: 'FINANCEIRO', value: 'FINANCEIRO' },
    { label: 'ADMINISTRATIVO', value: 'ADMINISTRATIVO' },
    { label: 'COMERCIAL', value: 'COMERCIAL' },
  ];

  ordenarPor = 'VENCIMENTO';
  ordenarOptions = [
    { label: 'DATA DE VENCIMENTO', value: 'VENCIMENTO' },
    { label: 'DATA DE EMISSÃO', value: 'EMISSAO' },
    { label: 'DATA DE PAGAMENTO', value: 'PAGAMENTO' },
    { label: 'CLIENTE', value: 'CLIENTE' },
    { label: 'VALOR', value: 'VALOR' },
  ];

  filtroAvancado = false;

  filtrarPor = 'TODOS';
  filtrarPorOptions = [
    { label: 'TODOS - NAO USAR FILTRO', value: 'TODOS' },
    { label: 'CODIGO', value: 'CODIGO' },
    { label: 'DESCRIÇÃO', value: 'DESCRICAO' },
    { label: 'CLIENTE', value: 'CLIENTE' },
    { label: 'DOCUMENTO', value: 'DOCUMENTO' },
    { label: 'DEPARTAMENTO', value: 'DEPARTAMENTO' },
  ];

  filtro = '';

  isLoading = false;

  // Helper para formatar Date para "yyyy-mm-dd"
  private formatDateToIso(date: Date | null | undefined): string | undefined {
    if (!date) return undefined;
    if (typeof date === 'string') return date;
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  gerarRelatorio() {
    this.isLoading = true;

    const query = {
      dataInicio: this.formatDateToIso(this.dataInicial),
      dataFim: this.formatDateToIso(this.dataFinal),
      dataDe: this.dataDe,
      situacaoTipo: this.situacaoTipo,
      departamento: this.departamento,
      ordenarPor: this.ordenarPor
    };

    this.financeiroService.imprimirContas(query).subscribe({
      next: (blob) => {
        this.isLoading = false;
        const fileURL = URL.createObjectURL(blob);
        window.open(fileURL, '_blank');
        
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Relatório aberto em nova aba!'
        });
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Erro ao gerar relatório', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: 'Falha ao gerar o relatório. Tente novamente mais tarde.'
        });
      }
    });
  }
}