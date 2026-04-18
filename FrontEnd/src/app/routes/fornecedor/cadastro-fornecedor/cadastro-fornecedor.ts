import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
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
    MatIconModule
  ],
  providers: [MessageService, ConfirmationService],
})
export class CadastroFornecedor {
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

  constructor() {
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

  salvar() {
    const dto = this.toRequest(this.model);

    // Validação frontend para forçar os campos com asterisco
    if (!dto.razaoSocial || !dto.classificacao || !dto.tipoPessoa) {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Preencha os campos obrigatórios (*)'});
        return;
    }

    if (dto.tipoPessoa === 'FISICA' && !dto.cpf) {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'O CPF é obrigatório'});
        return;
    }

    if (dto.tipoPessoa === 'JURIDICA' && !dto.cnpj) {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'O CNPJ é obrigatório'});
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
      cpf: f?.cpf || '',
      cnpj: f?.cnpj || '',
      inscricaoEstadual: f?.inscricaoEstadual || '',
      inscricaoMunicipal: f?.inscricaoMunicipal || '',

      emailPrincipal: f?.emailPrincipal || '',
      telefonePrincipal: f?.telefonePrincipal || '',
      celularPrincipal: f?.celularPrincipal || '',
      website: f?.website || '',

      nomeContato: f?.nomeContato || '',
      cargoContato: f?.cargoContato || '',
      emailContato: f?.emailContato || '',
      telefoneContato: f?.telefoneContato || '',

      cep: f?.cep || '',
      cidade: f?.cidade || '',
      estado: f?.estado || '',
      enderecoCompleto: f?.enderecoCompleto || '',

      prazoPagamentoDias: f?.prazoPagamentoDias || null,
      limiteCredito: f?.limiteCredito || null,
      descontoPadrao: f?.descontoPadrao || null,
      condicoesEspeciais: f?.condicoesEspeciais || '',

      bancoNome: f?.bancoNome || '',
      bancoAgencia: f?.bancoAgencia || '',
      bancoConta: f?.bancoConta || '',
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
