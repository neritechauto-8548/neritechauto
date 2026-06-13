import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { FornecedorService } from '../fornecedor.service';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { MatIconModule } from '@angular/material/icon';
import { isValidCpf, isValidCnpj } from '@shared/utils/validators';
import { InputNumberModule } from 'primeng/inputnumber';

@Component({
  selector: 'cadastro-fornecedor',
  standalone: true,
  templateUrl: './cadastro-fornecedor.html',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    SelectModule,
    InputTextModule,
    TextareaModule,
    ButtonModule,
    ToastModule,
    ConfirmDialogModule,
    MatIconModule,
    InputNumberModule
  ],
  providers: [MessageService, ConfirmationService],
})
export class CadastroFornecedor implements OnInit {
  private readonly location = inject(Location);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly service = inject(FornecedorService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);

  @Input() modalMode = false;
  @Output() saved = new EventEmitter<any>();
  @Output() canceled = new EventEmitter<void>();

  tiposPessoa = [
    { label: 'Física', value: 'FISICA' },
    { label: 'Jurídica', value: 'JURIDICA' },
  ];

  classificacoes = [
    { label: 'Classe A (Excelente)', value: 'A' },
    { label: 'Classe B (Ótimo)', value: 'B' },
    { label: 'Classe C (Bom)', value: 'C' },
    { label: 'Classe D (Regular)', value: 'D' },
    { label: 'Classe E (Ruim)', value: 'E' }
  ];

  model: any = {
    tipoPessoa: 'FISICA',
    classificacao: 'A',

    // Identificacao
    nomeFantasia: '',
    razaoSocial: '',
    cpf: '',
    cnpj: '',
    inscricaoEstadual: '',
    inscricaoMunicipal: '',

    // Contatos Digitais (Empresa)
    emailPrincipal: '',
    telefonePrincipal: '',
    celularPrincipal: '',
    website: '',

    // Contato Pessoal (Representante)
    nomeContato: '',
    cargoContato: '',
    emailContato: '',
    telefoneContato: '',

    // Endereco
    cep: '',
    cidade: '',
    estado: '',
    enderecoCompleto: '',

    // Financeiro
    prazoPagamentoDias: null,
    limiteCredito: null,
    descontoPadrao: null,
    condicoesEspeciais: '',

    // Bancario
    bancoNome: '',
    bancoAgencia: '',
    bancoConta: '',
    bancoPix: '',

    observacoes: '',
  };

  editingId?: number;

  get isFisica() {
    return this.model.tipoPessoa === 'FISICA';
  }

  get isJuridica() {
    return this.model.tipoPessoa === 'JURIDICA';
  }

  onTipoPessoaChange() {
    if (this.isFisica) {
      this.model.cnpj = '';
      this.model.inscricaoEstadual = '';
      this.model.inscricaoMunicipal = '';
      this.model.razaoSocial = '';
    } else {
      this.model.cpf = '';
    }
  }

  formatCpf(cpf: string | null): string {
    if (!cpf) return '';
    let raw = cpf.replace(/\D/g, '');
    if (raw.length > 11) raw = raw.substring(0, 11);
    let formatted = raw;
    if (raw.length > 3) formatted = raw.substring(0, 3) + '.' + raw.substring(3);
    if (raw.length > 6) formatted = formatted.substring(0, 7) + '.' + raw.substring(6);
    if (raw.length > 9) formatted = formatted.substring(0, 11) + '-' + raw.substring(9);
    return formatted;
  }

  formatCnpj(cnpj: string | null): string {
    if (!cnpj) return '';
    let raw = cnpj.replace(/[^A-Za-z0-9]/g, '').toUpperCase();
    if (raw.length > 14) raw = raw.substring(0, 14);
    let formatted = raw;
    if (raw.length > 2) formatted = raw.substring(0, 2) + '.' + raw.substring(2);
    if (raw.length > 5) formatted = formatted.substring(0, 6) + '.' + raw.substring(5);
    if (raw.length > 8) formatted = formatted.substring(0, 10) + '/' + raw.substring(8);
    if (raw.length > 12) formatted = formatted.substring(0, 15) + '-' + raw.substring(12);
    return formatted;
  }

  formatCep(cep: string | null): string {
    if (!cep) return '';
    let raw = cep.replace(/\D/g, '');
    if (raw.length > 8) raw = raw.substring(0, 8);
    let formatted = raw;
    if (raw.length > 5) formatted = raw.substring(0, 5) + '-' + raw.substring(5);
    return formatted;
  }

  formatPhone(phone: string | null): string {
    if (!phone) return '';
    let raw = phone.replace(/\D/g, '');
    if (raw.length > 11) raw = raw.substring(0, 11);
    let formatted = raw;
    if (raw.length > 0) formatted = '(' + raw;
    if (raw.length > 2) formatted = formatted.substring(0, 3) + ') ' + raw.substring(2);
    if (raw.length > 6) {
      if (raw.length > 10) {
        formatted = formatted.substring(0, 10) + '-' + raw.substring(7);
      } else {
        formatted = formatted.substring(0, 9) + '-' + raw.substring(6);
      }
    }
    return formatted;
  }

  onCpfInput(event: any) {
    const formatted = this.formatCpf(event.target.value);
    this.model.cpf = formatted;
    event.target.value = formatted;
  }

  onCnpjInput(event: any) {
    const formatted = this.formatCnpj(event.target.value);
    this.model.cnpj = formatted;
    event.target.value = formatted;
  }

  onCepInput(event: any) {
    const formatted = this.formatCep(event.target.value);
    this.model.cep = formatted;
    event.target.value = formatted;
  }

  onPhoneInput(event: any, field: string) {
    const formatted = this.formatPhone(event.target.value);
    this.model[field] = formatted;
    event.target.value = formatted;
  }

  formatAgencia(ag: string | null): string {
    if (!ag) return '';
    let raw = ag.replace(/[^A-Za-z0-9]/g, '').toUpperCase();
    if (raw.length > 5) raw = raw.substring(0, 5);
    if (raw.length > 4) {
      return raw.substring(0, 4) + '-' + raw.substring(4);
    }
    return raw;
  }

  formatConta(cc: string | null): string {
    if (!cc) return '';
    let raw = cc.replace(/[^A-Za-z0-9]/g, '').toUpperCase();
    if (raw.length > 15) raw = raw.substring(0, 15);
    if (raw.length > 1) {
      return raw.substring(0, raw.length - 1) + '-' + raw.substring(raw.length - 1);
    }
    return raw;
  }

  onAgenciaInput(event: any) {
    const formatted = this.formatAgencia(event.target.value);
    this.model.bancoAgencia = formatted;
    event.target.value = formatted;
  }

  onContaInput(event: any) {
    const formatted = this.formatConta(event.target.value);
    this.model.bancoConta = formatted;
    event.target.value = formatted;
  }

  ngOnInit(): void {
    if (!this.modalMode) {
      this.route.paramMap.subscribe(pm => {
        const idParam = pm.get('id');
        if (idParam) {
          const id = Number(idParam);
          if (!Number.isNaN(id)) {
            this.editingId = id;
            this.service.get(id).subscribe({
              next: (f: any) => this.carregarFornecedor(f),
              error: () => {},
            });
          }
        }
      });
    }
  }

  salvar() {
    const dto = this.toRequest(this.model);

    // Validação frontend para forçar os campos com asterisco
    if (!dto.razaoSocial || !dto.classificacao || !dto.tipoPessoa) {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Preencha os campos obrigatórios (*)'});
        return;
    }

    if (dto.tipoPessoa === 'FISICA') {
        if (!dto.cpf) {
            this.messageService.add({severity: 'error', summary: 'Erro', detail: 'O CPF é obrigatório'});
            return;
        }
        if (!isValidCpf(dto.cpf)) {
            this.messageService.add({severity: 'error', summary: 'Erro', detail: 'CPF inválido — verifique os dígitos'});
            return;
        }
    }

    if (dto.tipoPessoa === 'JURIDICA') {
        if (!dto.cnpj) {
            this.messageService.add({severity: 'error', summary: 'Erro', detail: 'O CNPJ é obrigatório'});
            return;
        }
        if (!isValidCnpj(dto.cnpj)) {
            this.messageService.add({severity: 'error', summary: 'Erro', detail: 'CNPJ inválido — verifique os caracteres e dígitos'});
            return;
        }
    }

    if (dto.cep && dto.cep.replace(/\D/g, '').length !== 8) {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'O CEP deve conter 8 dígitos'});
        return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (dto.emailPrincipal && !emailRegex.test(dto.emailPrincipal)) {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'O E-mail Corporativo está em formato inválido'});
        return;
    }
    if (dto.emailContato && !emailRegex.test(dto.emailContato)) {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'O E-mail do Representante está em formato inválido'});
        return;
    }

    if (dto.telefonePrincipal && dto.telefonePrincipal.replace(/\D/g, '').length < 10) {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'O Telefone Principal deve conter DDD e número válido'});
        return;
    }
    if (dto.celularPrincipal && dto.celularPrincipal.replace(/\D/g, '').length < 10) {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'O WhatsApp Oficial deve conter DDD e número válido'});
        return;
    }

    const obs = this.editingId != null
      ? this.service.update(this.editingId, dto)
      : this.service.create(dto);
    obs.subscribe({
      next: () => {
        if (this.modalMode) {
          this.saved.emit(dto);
        } else {
          this.messageService.add({severity: 'success', summary: 'Sucesso', detail: 'Fornecedor salvo com sucesso'});
          setTimeout(() => this.router.navigate(['/fornecedor/lista']), 500);
        }
      },
      error: () => {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Ocorreu um problema ao salvar os dados.'});
      }
    });
  }

  excluir() {
    if (!this.editingId) return;

    this.confirmationService.confirm({
      message: 'Tem certeza que deseja excluir permanentemente este fornecedor?',
      header: 'Confirmar Exclusão',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Excluir',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.service.delete(this.editingId!).subscribe({
          next: () => {
            if (this.modalMode) {
              this.canceled.emit();
            } else {
              this.messageService.add({severity: 'success', summary: 'Sucesso', detail: 'Fornecedor excluído'});
              setTimeout(() => this.router.navigate(['/fornecedor/lista']), 500);
            }
          },
          error: () => {
            this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Erro ao excluir, verifique as dependências.'});
          }
        });
      }
    });
  }

  cancelar() {
    if (this.modalMode) {
      this.canceled.emit();
    } else {
      this.location.back();
    }
  }

  private carregarFornecedor(f: any) {
    this.model = {
      tipoPessoa: f?.tipoPessoa || 'FISICA',
      classificacao: f?.classificacao || 'A',

      nomeFantasia: f?.nomeFantasia || '',
      razaoSocial: f?.razaoSocial || '',
      cpf: this.formatCpf(f?.cpf || ''),
      cnpj: this.formatCnpj(f?.cnpj || ''),
      inscricaoEstadual: f?.inscricaoEstadual || '',
      inscricaoMunicipal: f?.inscricaoMunicipal || '',

      emailPrincipal: f?.emailPrincipal || '',
      telefonePrincipal: this.formatPhone(f?.telefonePrincipal || ''),
      celularPrincipal: this.formatPhone(f?.celularPrincipal || ''),
      website: f?.website || '',

      nomeContato: f?.nomeContato || '',
      cargoContato: f?.cargoContato || '',
      emailContato: f?.emailContato || '',
      telefoneContato: this.formatPhone(f?.telefoneContato || ''),

      cep: this.formatCep(f?.cep || ''),
      cidade: f?.cidade || '',
      estado: f?.estado || '',
      enderecoCompleto: f?.enderecoCompleto || '',

      prazoPagamentoDias: f?.prazoPagamentoDias || null,
      limiteCredito: f?.limiteCredito || null,
      descontoPadrao: f?.descontoPadrao || null,
      condicoesEspeciais: f?.condicoesEspeciais || '',

      bancoNome: f?.bancoNome || '',
      bancoAgencia: this.formatAgencia(f?.bancoAgencia || ''),
      bancoConta: this.formatConta(f?.bancoConta || ''),
      bancoPix: f?.bancoPix || '',

      observacoes: f?.observacoes || '',
    };
  }

  private toRequest(m: any): any {
    // A API valida obrigatoriamente nomeFantasia + empresaId
    // Vamos garantir consistência minima
    return {
      empresaId: 1, // Fixando context de tenant por enquanto.
      tipoPessoa: m.tipoPessoa,
      classificacao: m.classificacao,

      nomeFantasia: m.nomeFantasia || (m.tipoPessoa === 'FISICA' ? m.razaoSocial || 'Não Informado' : 'Não Informado'),
      razaoSocial: m.razaoSocial || undefined,
      cpf: m.cpf || null,
      cnpj: m.cnpj || null,
      inscricaoEstadual: m.inscricaoEstadual || undefined,
      inscricaoMunicipal: m.inscricaoMunicipal || undefined,

      emailPrincipal: m.emailPrincipal || undefined,
      telefonePrincipal: m.telefonePrincipal || undefined,
      celularPrincipal: m.celularPrincipal || undefined,
      website: m.website || undefined,

      nomeContato: m.nomeContato || undefined,
      cargoContato: m.cargoContato || undefined,
      emailContato: m.emailContato || undefined,
      telefoneContato: m.telefoneContato || undefined,

      cep: m.cep || undefined,
      enderecoCompleto: m.enderecoCompleto || undefined,
      cidade: m.cidade || undefined,
      estado: m.estado || undefined,

      prazoPagamentoDias: m.prazoPagamentoDias || null,
      limiteCredito: m.limiteCredito || null,
      descontoPadrao: m.descontoPadrao || null,
      condicoesEspeciais: m.condicoesEspeciais || undefined,

      bancoNome: m.bancoNome || undefined,
      bancoAgencia: m.bancoAgencia || undefined,
      bancoConta: m.bancoConta || undefined,
      bancoPix: m.bancoPix || undefined,

      observacoes: m.observacoes || undefined,
      ativo: true,
    };
  }
}
