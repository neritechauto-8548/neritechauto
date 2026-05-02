import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { CheckboxModule } from 'primeng/checkbox';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'formas-pagamento-item-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, SelectModule, CheckboxModule, MatIconModule],
  templateUrl: './formas-pagamento-item-dialog.html',
})
export class FormasPagamentoItemDialog {
  nome = '';
  tipo = 'Outros';
  aceitaParcelamento = false;
  parcelasMaximas: number | null = null;
  taxaAdministracao: number | null = null;
  prazoRecebimentoDias: number | null = null;
  contaBancariaId: number | null = null;
  ativo = true;
  padrao = false;
  exigeAutorizacao = false;
  limiteDiario: number | null = null;
  observacoes = '';

  tipoOptions = [
    { label: 'PIX', value: 'PIX' },
    { label: 'Cartão de Crédito', value: 'CARTAO_CREDITO' },
    { label: 'Cartão de Débito', value: 'CARTAO_DEBITO' },
    { label: 'Dinheiro', value: 'DINHEIRO' },
    { label: 'Cheque', value: 'CHEQUE' },
    { label: 'Boleto', value: 'BOLETO' },
    { label: 'Outros', value: 'OUTROS' },
  ];

  constructor(private ref: DynamicDialogRef, private config: DynamicDialogConfig) {
    const data = config.data || {};
    this.nome = data.nome || '';
    this.tipo = data.tipoEnum || data.tipo || 'OUTROS';
    this.aceitaParcelamento = !!config.data?.aceitaParcelamento;
    this.parcelasMaximas = config.data?.parcelasMaximas ?? null;
    this.taxaAdministracao = config.data?.taxaAdministracao ?? null;
    this.prazoRecebimentoDias = config.data?.prazoRecebimentoDias ?? null;
    this.contaBancariaId = config.data?.contaBancariaId ?? null;
    this.ativo = config.data?.ativo !== false;
    this.padrao = !!config.data?.padrao;
    this.exigeAutorizacao = !!config.data?.exigeAutorizacao;
    this.limiteDiario = config.data?.limiteDiario ?? null;
    this.observacoes = (config.data?.observacoes as string) || '';
  }

  fechar() {
    this.ref.close();
  }

  incluirItem() {
    this.ref.close({
      nome: this.nome,
      tipo: this.tipo,
      aceitaParcelamento: this.aceitaParcelamento,
      parcelasMaximas: this.parcelasMaximas,
      taxaAdministracao: this.taxaAdministracao,
      prazoRecebimentoDias: this.prazoRecebimentoDias,
      contaBancariaId: this.contaBancariaId,
      ativo: this.ativo,
      padrao: this.padrao,
      exigeAutorizacao: this.exigeAutorizacao,
      limiteDiario: this.limiteDiario,
      observacoes: this.observacoes,
    });
  }
}
