import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { DatePickerModule } from 'primeng/datepicker';
import { DialogModule } from 'primeng/dialog';
import { InputMaskModule } from 'primeng/inputmask';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { ToastModule } from 'primeng/toast';
import { NgxPermissionsService } from 'ngx-permissions';
import { Observable, forkJoin, map, of, switchMap, finalize } from 'rxjs';

import { PageHeader } from '@shared';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { UtilService } from '@shared/services/util.service';
import { CnpjMaskDirective } from '@shared/directives/cnpj-mask';
import { isValidCnpj, isValidCpf } from '@shared/utils/validators';
import { ClientesService, ClienteRequestDTO, ClienteResponseDTO } from '../cliente/cliente.service';
import {
  ClienteResponse,
  ContatoClienteRequest,
  ContatoClienteResponse,
  EnderecoClienteRequest,
  EnderecoClienteResponse,
  OrigemCliente,
  StatusCliente,
  TipoCliente,
  TipoContato,
  TipoContatoLabels,
  getOrigemClienteOptions,
  getSexoOptions,
  Sexo,
} from '../models/cliente.models';

type SaveAction = 'stay' | 'back' | 'vehicle';
type ContactDraft = ContatoClienteRequest & { id?: number; clienteId?: number };

interface AddressDraft {
  id?: number;
  cep: string;
  logradouro: string;
  numero: string;
  complemento: string;
  bairro: string;
  cidade: string;
  estado: string;
  pais: string;
}

@Component({
  selector: 'cadastro-cliente',
  standalone: true,
  templateUrl: './cadastro-cliente.html',
  styleUrls: ['./cadastro-cliente.scss'],
  imports: [
    CommonModule,
    FormsModule,
    PageHeader,
    SelectModule,
    InputTextModule,
    TextareaModule,
    DatePickerModule,
    ToastModule,
    DialogModule,
    InputMaskModule,
    CnpjMaskDirective,
  ],
})
export class CadastroCliente implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly clientesService = inject(ClientesService);
  private readonly messageService = inject(MessageService);
  private readonly utilService = inject(UtilService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly permissions = inject(NgxPermissionsService);

  @Input() modalMode = false;
  @Output() saved = new EventEmitter<ClienteResponse | null>();
  @Output() canceled = new EventEmitter<void>();

  readonly tabs = [
    { label: 'Dados básicos', icon: 'pi pi-user' },
    { label: 'Contatos', icon: 'pi pi-phone' },
    { label: 'Endereço', icon: 'pi pi-map-marker' },
    { label: 'Preferências', icon: 'pi pi-sliders-h' },
  ];

  readonly tipoClienteOptions = [
    { label: 'Pessoa Física', value: TipoCliente.PESSOA_FISICA },
    { label: 'Pessoa Jurídica', value: TipoCliente.PESSOA_JURIDICA },
  ];
  readonly sexoOptions = getSexoOptions();
  readonly origemOptions = getOrigemClienteOptions();
  readonly tipoContatoOptions = Object.values(TipoContato).map(value => ({
    label: TipoContatoLabels[value],
    value,
  }));

  activeIndex = 0;
  loading = false;
  saving = false;
  submitted = false;
  isEditMode = false;
  savedClienteId: string | number | null = null;
  originContext = '';
  private returnUrl = '/clientes';

  model = {
    tipoCliente: TipoCliente.PESSOA_FISICA,
    nomeCompleto: '',
    razaoSocial: '',
    nomeFantasia: '',
    cpfCnpj: '',
    email: '',
    dataNascimento: null as Date | null,
    sexo: null as Sexo | null,
    inscricaoEstadual: '',
    inscricaoMunicipal: '',
    origemCliente: null as OrigemCliente | null,
    observacoesGerais: '',
    status: StatusCliente.ATIVO,
  };

  address: AddressDraft = this.emptyAddress();
  contacts: ContactDraft[] = [];
  deletedContactIds: number[] = [];

  showContactDialog = false;
  contactEditIndex: number | null = null;
  contactForm: ContactDraft = this.emptyContact();

  get pageTitle() {
    return this.isEditMode ? 'Editar Cliente' : 'Novo Cliente';
  }

  get pageDescription() {
    return this.isEditMode
      ? 'Mantenha identidade, contatos, endereço e preferências sem perder o histórico do cliente.'
      : 'Cadastre o mínimo operacional e complemente os dados conforme a necessidade do atendimento.';
  }

  get isPessoaFisica() {
    return this.model.tipoCliente === TipoCliente.PESSOA_FISICA;
  }

  get canCreateVehicle() {
    return Boolean(this.permissions.getPermission('VEICULO_CRIAR'));
  }

  get canDeactivate() {
    return Boolean(this.permissions.getPermission('CLIENTE_EXCLUIR'));
  }

  get canReactivate() {
    return Boolean(this.permissions.getPermission('CLIENTE_EDITAR'));
  }

  get statusLabel() {
    if (this.model.status === StatusCliente.INATIVO) return 'Inativo';
    if (this.model.status === StatusCliente.BLOQUEADO) return 'Bloqueado';
    return 'Ativo';
  }

  get completionItems() {
    const hasIdentity = Boolean(this.currentDisplayName.trim());
    const hasContact = Boolean(this.model.email.trim() || this.contacts.some(c => this.contactValue(c).trim()));
    const hasAddress = this.hasAnyAddressValue();
    const hasDocument = Boolean(this.model.cpfCnpj.trim());

    return [
      { label: 'Identidade mínima', done: hasIdentity },
      { label: 'Contato operacional', done: hasContact },
      { label: 'Documento', done: hasDocument, optional: true },
      { label: 'Endereço', done: hasAddress, optional: true },
    ];
  }

  get completionPercent() {
    const required = this.completionItems.filter(item => !item.optional);
    const optional = this.completionItems.filter(item => item.optional);
    const requiredScore = required.filter(item => item.done).length * 35;
    const optionalScore = optional.filter(item => item.done).length * 15;
    return Math.min(100, requiredScore + optionalScore);
  }

  private get currentDisplayName() {
    return this.isPessoaFisica ? this.model.nomeCompleto : this.model.razaoSocial;
  }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('uuid');
    this.originContext = this.route.snapshot.queryParamMap.get('context') || '';
    const requestedReturnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
    if (requestedReturnUrl?.startsWith('/')) {
      this.returnUrl = requestedReturnUrl;
    }

    if (id) {
      this.isEditMode = true;
      this.savedClienteId = id;
      this.loadCustomer(id);
    }
  }

  onTipoClienteChange() {
    // Os campos de PF/PJ são independentes e não são apagados silenciosamente.
    // Apenas o documento compartilhado continua visível para revisão do usuário.
  }

  onDocumentoBlur() {
    const document = this.model.cpfCnpj.trim();
    if (!document) return;

    const valid = this.isPessoaFisica ? isValidCpf(document) : isValidCnpj(document);
    if (!valid) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Documento inválido',
        detail: `Revise o ${this.isPessoaFisica ? 'CPF' : 'CNPJ'} informado.`,
      });
      return;
    }

    if (!this.isPessoaFisica && !this.isEditMode && this.onlyDigits(document).length === 14) {
      this.lookupCnpj(document);
    }
  }

  save(action: SaveAction = 'stay') {
    if (this.saving) return;

    this.submitted = true;
    if (!this.validateCustomer()) return;
    if (!this.validateAddressIfStarted()) return;

    const dto = this.buildCustomerRequest();
    const request$ = this.isEditMode && this.savedClienteId
      ? this.clientesService.update(this.savedClienteId, dto)
      : this.clientesService.create(dto);

    this.saving = true;
    request$
      .pipe(
        switchMap(customer => {
          this.savedClienteId = customer.id;
          this.model.status = customer.status ?? this.model.status;
          return this.persistRelated(customer.id).pipe(map(() => customer));
        }),
        finalize(() => (this.saving = false))
      )
      .subscribe({
        next: customer => {
          this.isEditMode = true;
          this.deletedContactIds = [];
          this.messageService.add({
            severity: 'success',
            summary: 'Cliente salvo',
            detail: 'As informações foram salvas com sucesso.',
          });

          if (this.modalMode) {
            this.saved.emit(customer);
            return;
          }

          this.afterSave(customer.id, action);
        },
        error: error => {
          const detail = error?.error?.message || 'Não foi possível salvar o cliente. Revise os dados e tente novamente.';
          this.messageService.add({ severity: 'error', summary: 'Não foi possível salvar', detail });
        },
      });
  }

  saveAndVehicle() {
    if (!this.canCreateVehicle) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Acesso restrito',
        detail: 'Seu perfil não possui permissão para cadastrar veículos.',
      });
      return;
    }
    this.save('vehicle');
  }

  deactivate() {
    if (!this.savedClienteId || !this.canDeactivate || this.model.status === StatusCliente.INATIVO) return;

    this.confirmationService.confirm({
      title: 'Inativar cliente',
      message: 'O cliente deixará de aparecer como ativo, mas veículos, contatos e histórico serão preservados.',
      confirmText: 'Inativar cliente',
      cancelText: 'Cancelar',
      type: 'warning',
      icon: 'warning',
    }).subscribe(confirmed => {
      if (!confirmed || !this.savedClienteId) return;
      this.clientesService.deactivate(this.savedClienteId).subscribe({
        next: customer => {
          this.model.status = customer.status ?? StatusCliente.INATIVO;
          this.messageService.add({ severity: 'success', summary: 'Cliente inativado', detail: 'O histórico foi preservado.' });
        },
        error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível inativar o cliente.' }),
      });
    });
  }

  reactivate() {
    if (!this.savedClienteId || !this.canReactivate || this.model.status !== StatusCliente.INATIVO) return;

    this.clientesService.reactivate(this.savedClienteId).subscribe({
      next: customer => {
        this.model.status = customer.status ?? StatusCliente.ATIVO;
        this.messageService.add({ severity: 'success', summary: 'Cliente reativado', detail: 'O cadastro voltou ao estado ativo.' });
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível reativar o cliente.' }),
    });
  }

  openContactDialog(index?: number) {
    this.contactEditIndex = index ?? null;
    this.contactForm = index === undefined
      ? this.emptyContact()
      : { ...this.contacts[index], contato: this.contactValue(this.contacts[index]) };
    this.showContactDialog = true;
  }

  saveContactDraft() {
    const value = this.contactValue(this.contactForm).trim();
    if (!value) {
      this.messageService.add({ severity: 'warn', summary: 'Contato obrigatório', detail: 'Informe o valor do contato.' });
      return;
    }

    const normalized: ContactDraft = {
      ...this.contactForm,
      contato: this.isPhoneType(this.contactForm.tipoContato) ? this.onlyDigits(value) : value,
      valor: undefined,
    };

    if (normalized.principal) {
      this.contacts = this.contacts.map((contact, index) => ({
        ...contact,
        principal: this.contactEditIndex === index,
      }));
    }

    if (this.contactEditIndex === null) {
      this.contacts = [...this.contacts, normalized];
    } else {
      this.contacts = this.contacts.map((contact, index) => index === this.contactEditIndex ? normalized : contact);
    }

    this.showContactDialog = false;
    this.contactEditIndex = null;
  }

  removeContact(index: number) {
    const contact = this.contacts[index];
    if (contact?.id) {
      this.deletedContactIds = [...this.deletedContactIds, contact.id];
    }
    this.contacts = this.contacts.filter((_, current) => current !== index);
  }

  buscarCep() {
    const cep = this.onlyDigits(this.address.cep);
    if (cep.length !== 8) return;

    this.utilService.buscarCep(cep).subscribe({
      next: data => {
        if (data?.erro) {
          this.messageService.add({ severity: 'warn', summary: 'CEP não encontrado', detail: 'Você pode preencher o endereço manualmente.' });
          return;
        }
        this.address = {
          ...this.address,
          cep,
          logradouro: this.address.logradouro || data.logradouro || '',
          bairro: this.address.bairro || data.bairro || '',
          cidade: this.address.cidade || data.localidade || '',
          estado: this.address.estado || data.uf || '',
        };
      },
      error: () => {
        this.messageService.add({
          severity: 'info',
          summary: 'Consulta de CEP indisponível',
          detail: 'Continue preenchendo o endereço manualmente.',
        });
      },
    });
  }

  cancel() {
    if (this.modalMode) {
      this.canceled.emit();
      return;
    }
    this.router.navigateByUrl(this.returnUrl);
  }

  contactLabel(type: TipoContato) {
    return TipoContatoLabels[type] || 'Contato';
  }

  formatContact(contact: ContactDraft) {
    const value = this.contactValue(contact);
    if (!this.isPhoneType(contact.tipoContato)) return value;
    const digits = this.onlyDigits(value);
    if (digits.length === 10) return `(${digits.slice(0, 2)}) ${digits.slice(2, 6)}-${digits.slice(6)}`;
    if (digits.length >= 11) return `(${digits.slice(0, 2)}) ${digits.slice(2, 7)}-${digits.slice(7, 11)}`;
    return digits;
  }

  isPhoneType(type: TipoContato) {
    return [TipoContato.TELEFONE_FIXO, TipoContato.CELULAR, TipoContato.WHATSAPP, TipoContato.TELEGRAM].includes(type);
  }

  contactMask(type: TipoContato) {
    return type === TipoContato.TELEFONE_FIXO ? '(99) 9999-9999' : '(99) 99999-9999';
  }

  private loadCustomer(id: string | number) {
    this.loading = true;
    this.clientesService.getById(id).pipe(finalize(() => (this.loading = false))).subscribe({
      next: customer => {
        this.patchCustomer(customer);
        this.loadContacts(id);
        this.loadAddress(id);
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Cliente indisponível', detail: 'Não foi possível carregar este cliente.' });
      },
    });
  }

  private patchCustomer(customer: ClienteResponseDTO) {
    this.model.tipoCliente = customer.tipoCliente;
    this.model.nomeCompleto = customer.nomeCompleto || '';
    this.model.razaoSocial = customer.razaoSocial || customer.nomeCompleto || '';
    this.model.nomeFantasia = customer.nomeFantasia || '';
    this.model.cpfCnpj = customer.tipoCliente === TipoCliente.PESSOA_FISICA ? customer.cpf || '' : customer.cnpj || '';
    this.model.email = customer.email || '';
    this.model.dataNascimento = customer.dataNascimento ? new Date(`${customer.dataNascimento}T00:00:00`) : null;
    this.model.sexo = customer.sexo || null;
    this.model.inscricaoEstadual = customer.inscricaoEstadual || '';
    this.model.inscricaoMunicipal = customer.inscricaoMunicipal || '';
    this.model.origemCliente = customer.origemCliente || null;
    this.model.observacoesGerais = customer.observacoesGerais || '';
    this.model.status = customer.status || StatusCliente.ATIVO;
  }

  private loadContacts(id: string | number) {
    this.clientesService.listarContatos(id).subscribe({
      next: response => {
        this.contacts = (response.content || []).map((contact: ContatoClienteResponse) => ({
          ...contact,
          contato: contact.contato ?? contact.valor ?? '',
          valor: undefined,
        }));
      },
      error: () => {
        this.contacts = [];
      },
    });
  }

  private loadAddress(id: string | number) {
    this.clientesService.listarEnderecos(id).subscribe({
      next: response => {
        const address = response.content?.[0];
        if (!address) return;
        this.address = this.mapAddress(address);
      },
      error: () => {
        this.address = this.emptyAddress();
      },
    });
  }

  private buildCustomerRequest(): ClienteRequestDTO {
    const displayName = this.currentDisplayName.trim();
    const document = this.model.cpfCnpj.trim();

    return {
      tipoCliente: this.model.tipoCliente,
      // O backend atual ainda exige nomeCompleto; para PJ ele recebe a razão social como nome canônico compatível.
      nomeCompleto: this.isPessoaFisica ? displayName : displayName,
      razaoSocial: this.isPessoaFisica ? undefined : displayName,
      nomeFantasia: this.isPessoaFisica ? undefined : this.model.nomeFantasia.trim() || undefined,
      cpf: this.isPessoaFisica && document ? this.onlyDigits(document) : undefined,
      cnpj: !this.isPessoaFisica && document ? this.onlyAlphaNumeric(document) : undefined,
      email: this.model.email.trim() || undefined,
      dataNascimento: this.isPessoaFisica ? this.formatDate(this.model.dataNascimento) : undefined,
      sexo: this.isPessoaFisica ? this.model.sexo || undefined : undefined,
      inscricaoEstadual: !this.isPessoaFisica ? this.model.inscricaoEstadual.trim() || undefined : undefined,
      inscricaoMunicipal: !this.isPessoaFisica ? this.model.inscricaoMunicipal.trim() || undefined : undefined,
      origemCliente: this.model.origemCliente || undefined,
      observacoesGerais: this.model.observacoesGerais.trim() || undefined,
      status: this.isEditMode ? this.model.status : StatusCliente.ATIVO,
    };
  }

  private validateCustomer() {
    if (!this.currentDisplayName.trim()) {
      this.activeIndex = 0;
      this.messageService.add({
        severity: 'error',
        summary: 'Nome obrigatório',
        detail: `Informe ${this.isPessoaFisica ? 'o nome do cliente' : 'a razão social'}.`,
      });
      return false;
    }

    const document = this.model.cpfCnpj.trim();
    if (document) {
      const valid = this.isPessoaFisica ? isValidCpf(document) : isValidCnpj(document);
      if (!valid) {
        this.activeIndex = 0;
        this.messageService.add({
          severity: 'error',
          summary: 'Documento inválido',
          detail: `Revise o ${this.isPessoaFisica ? 'CPF' : 'CNPJ'} antes de salvar.`,
        });
        return false;
      }
    }
    return true;
  }

  private validateAddressIfStarted() {
    if (!this.hasAnyAddressValue()) return true;

    const required = [
      this.address.cep,
      this.address.logradouro,
      this.address.numero,
      this.address.bairro,
      this.address.cidade,
      this.address.estado,
    ];

    if (required.some(value => !value.trim())) {
      this.activeIndex = 2;
      this.messageService.add({
        severity: 'error',
        summary: 'Endereço incompleto',
        detail: 'Ao informar endereço, preencha CEP, logradouro, número, bairro, cidade e UF.',
      });
      return false;
    }
    return true;
  }

  private persistRelated(customerId: string | number): Observable<unknown[]> {
    const requests: Observable<unknown>[] = [];

    if (this.hasAnyAddressValue()) {
      const payload: EnderecoClienteRequest = {
        cep: this.onlyDigits(this.address.cep),
        logradouro: this.address.logradouro.trim(),
        numero: this.address.numero.trim(),
        complemento: this.address.complemento.trim() || undefined,
        bairro: this.address.bairro.trim(),
        cidade: this.address.cidade.trim(),
        estado: this.address.estado.trim().toUpperCase(),
        pais: this.address.pais.trim() || 'Brasil',
      };
      requests.push(
        this.address.id
          ? this.clientesService.atualizarEndereco(customerId, this.address.id, payload)
          : this.clientesService.criarEndereco(customerId, payload)
      );
    }

    for (const contact of this.contacts) {
      const payload: ContatoClienteRequest = {
        tipoContato: contact.tipoContato,
        contato: this.contactValue(contact).trim(),
        principal: Boolean(contact.principal),
      };
      requests.push(
        contact.id
          ? this.clientesService.atualizarContato(customerId, contact.id, payload)
          : this.clientesService.criarContato(customerId, payload)
      );
    }

    for (const contactId of this.deletedContactIds) {
      requests.push(this.clientesService.excluirContato(customerId, contactId));
    }

    return requests.length ? forkJoin(requests) : of([]);
  }

  private afterSave(customerId: number, action: SaveAction) {
    if (action === 'vehicle') {
      this.router.navigate(['/veiculos/cadastro'], { queryParams: { clienteId: customerId } });
      return;
    }
    if (action === 'back') {
      this.router.navigateByUrl(this.returnUrl);
      return;
    }

    this.router.navigate(['/clientes', customerId, 'editar'], { replaceUrl: true });
    this.loadContacts(customerId);
    this.loadAddress(customerId);
  }

  private lookupCnpj(cnpj: string) {
    this.utilService.buscarCnpj(cnpj).subscribe({
      next: data => {
        if (!data?.razao_social) return;
        this.model.razaoSocial = this.model.razaoSocial || data.razao_social || '';
        this.model.nomeFantasia = this.model.nomeFantasia || data.nome_fantasia || '';
        if (!this.hasAnyAddressValue() && data.cep) {
          this.address = {
            ...this.address,
            cep: data.cep || '',
            logradouro: data.logradouro || '',
            numero: data.numero || '',
            complemento: data.complemento || '',
            bairro: data.bairro || '',
            cidade: data.municipio || '',
            estado: data.uf || '',
          };
        }
        this.messageService.add({
          severity: 'info',
          summary: 'Dados sugeridos',
          detail: 'Revise os dados consultados antes de salvar. A fonte externa é apenas assistiva.',
        });
      },
      error: () => {
        // Consulta externa é opcional e nunca bloqueia digitação manual.
      },
    });
  }

  private contactValue(contact: ContatoClienteRequest) {
    return contact.contato ?? contact.valor ?? '';
  }

  private hasAnyAddressValue() {
    return Boolean(
      this.address.cep ||
      this.address.logradouro ||
      this.address.numero ||
      this.address.bairro ||
      this.address.cidade ||
      this.address.estado ||
      this.address.complemento
    );
  }

  private mapAddress(address: EnderecoClienteResponse): AddressDraft {
    return {
      id: address.id,
      cep: address.cep || '',
      logradouro: address.logradouro || '',
      numero: address.numero || '',
      complemento: address.complemento || '',
      bairro: address.bairro || '',
      cidade: address.cidade || '',
      estado: address.estado || '',
      pais: address.pais || 'Brasil',
    };
  }

  private emptyAddress(): AddressDraft {
    return {
      cep: '',
      logradouro: '',
      numero: '',
      complemento: '',
      bairro: '',
      cidade: '',
      estado: '',
      pais: 'Brasil',
    };
  }

  private emptyContact(): ContactDraft {
    return {
      tipoContato: TipoContato.CELULAR,
      contato: '',
      principal: this.contacts.length === 0,
    };
  }

  private onlyDigits(value: string) {
    return (value || '').replace(/\D+/g, '');
  }

  private onlyAlphaNumeric(value: string) {
    return (value || '').replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
  }

  private formatDate(date: Date | null) {
    if (!date) return undefined;
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
