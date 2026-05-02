import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { Select } from 'primeng/select';
import { Checkbox } from 'primeng/checkbox';
import { InputMaskModule } from 'primeng/inputmask';
import { MessageService } from 'primeng/api';
import { isValidCpf, isValidCnpj } from '@shared/utils/validators';
import { UtilService } from '@shared/services/util.service';

@Component({
  selector: 'app-contas-item-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, Select, Checkbox, InputMaskModule],
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

  applyCpfCnpjMask(v: string): string {
    const digits = v.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    if (digits.length <= 11) {
      // CPF: 999.999.999-99
      let out = digits.slice(0, 11);
      if (digits.length > 3) out = digits.slice(0, 3) + '.' + digits.slice(3);
      if (digits.length > 6) out = out.slice(0, 7) + '.' + digits.slice(6);
      if (digits.length > 9) out = out.slice(0, 11) + '-' + digits.slice(9);
      return out;
    } else {
      // CNPJ: AA.AAA.AAA/AAAA-99
      let out = digits.slice(0, 14);
      if (digits.length > 2) out = digits.slice(0, 2) + '.' + digits.slice(2);
      if (digits.length > 5) out = out.slice(0, 6) + '.' + digits.slice(5);
      if (digits.length > 8) out = out.slice(0, 10) + '/' + digits.slice(8);
      if (digits.length > 12) out = out.slice(0, 15) + '-' + digits.slice(12);
      return out;
    }
  }

  onCpfCnpjInput(event: any) {
    const val = event.target.value;
    this.cpfCnpjTitular = this.applyCpfCnpjMask(val);
  }

  constructor(
    private ref: DynamicDialogRef, 
    private config: DynamicDialogConfig,
    private messageService: MessageService,
    private utilService: UtilService
  ) {
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

  validarDocumento() {
    if (!this.cpfCnpjTitular) return;
    const digits = this.cpfCnpjTitular.replace(/[^a-zA-Z0-9]/g, '');
    const tipo = digits.length > 11 ? 'Jurídica' : 'Física';
    
    // Chama a validação do backend
    this.utilService.validarDocumento(digits, tipo).subscribe({
      next: (isValid) => {
        if (!isValid) {
          this.messageService.add({ 
            severity: 'warn', 
            summary: 'Documento Inválido', 
            detail: `O ${tipo === 'Física' ? 'CPF' : 'CNPJ'} informado não é válido.` 
          });
        }
      },
      error: (err) => console.error('Erro ao validar documento no backend', err)
    });
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
