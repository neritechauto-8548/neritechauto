import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { Select } from 'primeng/select';
import { Checkbox } from 'primeng/checkbox';
import { InputMaskModule } from 'primeng/inputmask';
import { InputNumberModule } from 'primeng/inputnumber';
import { MessageService } from 'primeng/api';
import { isValidCpf, isValidCnpj } from '@shared/utils/validators';
import { UtilService } from '@shared/services/util.service';

@Component({
  selector: 'app-contas-item-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, Select, Checkbox, InputMaskModule, InputNumberModule],
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

  tipoPessoa: 'Física' | 'Jurídica' = 'Jurídica';

  formatarDocumento(event: Event): void {
    const input = event.target as HTMLInputElement;
    let raw = input.value.replace(/[^A-Z0-9]/gi, '').toUpperCase();
    if (raw.length > 14) raw = raw.substring(0, 14);

    let fmt = raw;
    const apenasNumeros = /^\d+$/.test(raw);

    if (apenasNumeros && raw.length <= 11) {
      // CPF: 999.999.999-99
      if (raw.length > 3)  fmt = raw.substring(0, 3) + '.' + raw.substring(3);
      if (raw.length > 6)  fmt = fmt.substring(0, 7) + '.' + raw.substring(6);
      if (raw.length > 9)  fmt = fmt.substring(0, 11) + '-' + raw.substring(9);
      this.tipoPessoa = 'Física';
    } else {
      // CNPJ alfanumérico (padrão jul/2026): AA.AAA.AAA/AAAA-DD
      if (raw.length > 2)  fmt = raw.substring(0, 2)  + '.' + raw.substring(2);
      if (raw.length > 5)  fmt = fmt.substring(0, 6)  + '.' + raw.substring(5);
      if (raw.length > 8)  fmt = fmt.substring(0, 10) + '/' + raw.substring(8);
      if (raw.length > 12) fmt = fmt.substring(0, 15) + '-' + raw.substring(12);
      this.tipoPessoa = 'Jurídica';
    }

    this.cpfCnpjTitular = fmt;
    input.value = fmt;
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
    const digits = this.cpfCnpjTitular.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
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
    const cod = this.bancoCodigo ? this.bancoCodigo.trim() : '';
    const nome = this.bancoNome ? this.bancoNome.trim() : '';
    const ag = this.agencia ? this.agencia.trim() : '';
    const ct = this.conta ? this.conta.trim() : '';
    const titular = this.titularConta ? this.titularConta.trim() : '';
    const doc = this.cpfCnpjTitular ? this.cpfCnpjTitular.trim() : '';
    
    if (!cod) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'O código do banco é obrigatório.' });
      return;
    }
    if (!nome) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'O nome do banco é obrigatório.' });
      return;
    }
    if (!ag) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'A agência é obrigatória.' });
      return;
    }
    if (!ct) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'O número da conta é obrigatório.' });
      return;
    }
    if (!titular) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'O nome do titular é obrigatório.' });
      return;
    }
    if (!doc) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'O CPF ou CNPJ do titular é obrigatório.' });
      return;
    }

    const digits = doc.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    if (digits.length !== 11 && digits.length !== 14) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'O CPF ou CNPJ do titular deve conter 11 (CPF) ou 14 (CNPJ) dígitos.' });
      return;
    }

    if (digits.length === 11) {
      const onlyNums = digits.replace(/[^0-9]/g, '');
      if (onlyNums.length !== 11 || !isValidCpf(onlyNums)) {
        this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'O CPF informado não é válido.' });
        return;
      }
    }

    if (digits.length === 14 && !isValidCnpj(digits)) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'O CNPJ informado não é válido.' });
      return;
    }

    this.ref.close({
      bancoCodigo: cod,
      bancoNome: nome,
      agencia: ag,
      conta: ct,
      digitoConta: this.digitoConta ? this.digitoConta.trim() : '',
      tipoConta: this.tipoConta,
      titularConta: titular,
      cpfCnpjTitular: doc,
      valorSaldoInicial: this.valorSaldoInicial,
      saldoAtual: this.saldoAtual,
      limiteChequeEspecial: this.limiteChequeEspecial,
      snAtivo: this.snAtivo,
      ativo: this.ativo,
    });
  }
}
