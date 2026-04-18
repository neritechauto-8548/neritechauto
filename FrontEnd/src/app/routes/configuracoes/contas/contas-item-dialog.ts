import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { Select } from 'primeng/select';
import { Checkbox } from 'primeng/checkbox';

@Component({
  selector: 'app-contas-item-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, Select, Checkbox],
  templateUrl: './contas-item-dialog.html',
})
export class ContasItemDialog {
  bancoCodigo = '';
  bancoNome = '';
  agencia = '';
  conta = '';
  digitoConta = '';
  tipoConta = 'CORRENTE';
  titularConta = '';
  cpfCnpjTitular = '';
  valorSaldoInicial: number | null = null;
  saldoAtual: number | null = null;
  limiteChequeEspecial: number | null = null;
  snAtivo = true;
  ativo = true;
  readonly = false;
  tipos = [
    { label: 'Corrente', value: 'CORRENTE' },
    { label: 'Poupança', value: 'POUPANCA' },
    { label: 'Investimento', value: 'INVESTIMENTO' },
  ];

  constructor(private ref: DynamicDialogRef, private config: DynamicDialogConfig) {
    const d = this.config.data || {};
    this.bancoCodigo = d.bancoCodigo || '';
    this.bancoNome = d.bancoNome || '';
    this.agencia = d.agencia || '';
    this.conta = d.conta || '';
    this.digitoConta = d.digitoConta || '';
    this.tipoConta = d.tipoConta || 'CORRENTE';
    this.titularConta = d.titularConta || '';
    this.cpfCnpjTitular = d.cpfCnpjTitular || '';
    this.valorSaldoInicial = d.valorSaldoInicial ?? d.saldoAtual ?? null;
    this.saldoAtual = d.saldoAtual ?? null;
    this.limiteChequeEspecial = d.limiteChequeEspecial ?? null;
    this.snAtivo = d.snAtivo !== false ? true : false;
    this.ativo = d.ativo !== false;
    this.readonly = !!d.readonly;
  }

  cancelar() {
    this.ref.close();
  }

  salvar() {
    this.ref.close({
      bancoCodigo: this.bancoCodigo,
      bancoNome: this.bancoNome,
      agencia: this.agencia,
      conta: this.conta,
      digitoConta: this.digitoConta,
      tipoConta: this.tipoConta,
      titularConta: this.titularConta,
      cpfCnpjTitular: this.cpfCnpjTitular,
      valorSaldoInicial: this.valorSaldoInicial,
      saldoAtual: this.saldoAtual,
      limiteChequeEspecial: this.limiteChequeEspecial,
      snAtivo: this.snAtivo,
      ativo: this.ativo,
    });
  }
}
