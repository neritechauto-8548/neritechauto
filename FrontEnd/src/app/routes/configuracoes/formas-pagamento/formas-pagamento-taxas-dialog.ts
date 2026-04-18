import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';

type TipoPagamento = 'PIX' | 'Cartão' | 'Boleto' | 'Cheque' | 'Dinheiro' | 'Outros';

@Component({
  selector: 'formas-pagamento-taxas-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, SelectModule],
  templateUrl: './formas-pagamento-taxas-dialog.html',
})
export class FormasPagamentoTaxasDialog {
  nome = '';
  tipoPagamento: TipoPagamento = 'Outros';
  lancarAutomatico: 'SIM' | 'NAO' = 'NAO';
  atualizadoEm: Date | null = null;
  atualizadoEmText: string | undefined;

  // Estruturas de taxas por tipo
  pix = { taxaPercent: 0, valorPorTransacao: 0, diasRecebimento: 0 };
  cartao = { taxasPorMes: Array.from({ length: 24 }, () => 0) };
  outros = { taxaPercent: 0 };

  tipoOptions = [
    { label: 'PIX', value: 'PIX' },
    { label: 'Cartão', value: 'Cartão' },
    { label: 'Boleto', value: 'Boleto' },
    { label: 'Cheque', value: 'Cheque' },
    { label: 'Dinheiro', value: 'Dinheiro' },
    { label: 'Outros', value: 'Outros' },
  ];

  simNaoOptions = [
    { label: 'SIM', value: 'SIM' },
    { label: 'NAO', value: 'NAO' },
  ];

  constructor(private ref: DynamicDialogRef, private config: DynamicDialogConfig) {
    const data = config.data || {};
    this.nome = (data.nome as string) || '';
    this.tipoPagamento = (data.tipoPagamento as TipoPagamento) || 'Outros';
    this.lancarAutomatico = (data.lancarAutomatico as 'SIM' | 'NAO') || 'NAO';
    if (data.atualizadoEm) {
      const d = data.atualizadoEm as string;
      // Espera formato dd/MM/yyyy
      const [dd, mm, yyyy] = d.split('/').map(Number);
      if (!isNaN(dd) && !isNaN(mm) && !isNaN(yyyy)) this.atualizadoEm = new Date(yyyy, mm - 1, dd);
      this.atualizadoEmText = d;
    }
    const taxas = data.taxas || {};
    if (taxas.pix) this.pix = { ...this.pix, ...taxas.pix };
    if (taxas.cartao?.taxasPorMes && Array.isArray(taxas.cartao.taxasPorMes)) {
      const arr = taxas.cartao.taxasPorMes.slice(0, 24);
      this.cartao.taxasPorMes = [...this.cartao.taxasPorMes.map((v, i) => arr[i] ?? v)];
    }
    if (taxas.outros) this.outros = { ...this.outros, ...taxas.outros };
  }

  fechar() {
    this.ref.close();
  }

  salvar() {
    const atualizadoEmStr = this.atualizadoEm
      ? [this.atualizadoEm.getDate(), this.atualizadoEm.getMonth() + 1, this.atualizadoEm.getFullYear()]
          .map(v => String(v).padStart(2, '0'))
          .join('/')
      : this.atualizadoEmText;

    const taxas: any = {};
    if (this.tipoPagamento === 'PIX') taxas.pix = { ...this.pix };
    if (this.tipoPagamento === 'Cartão') taxas.cartao = { taxasPorMes: [...this.cartao.taxasPorMes] };
    if (this.tipoPagamento === 'Outros') taxas.outros = { ...this.outros };

    this.ref.close({
      nome: this.nome,
      tipoPagamento: this.tipoPagamento,
      lancarAutomatico: this.lancarAutomatico,
      atualizadoEm: atualizadoEmStr,
      taxas,
    });
  }
}