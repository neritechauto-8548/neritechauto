import { Component, EventEmitter, Input, Output, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
// PrimeNG components
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { DatePickerModule } from 'primeng/datepicker';
import { TableModule } from 'primeng/table';
import { LocalStorageService } from '@shared/services/storage.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { TooltipModule } from 'primeng/tooltip';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputMaskModule } from 'primeng/inputmask';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ClientesService, ClienteRequestDTO, ClienteResponseDTO } from '../cliente/cliente.service';
import { TipoCliente, StatusCliente, TipoContato, TipoEndereco, TipoDocumento, ContatoClienteRequest, ContatoClienteResponse, EnderecoClienteRequest, DocumentoClienteRequest, getTipoContatoOptions, TipoContatoLabels,
  Sexo, EstadoCivil, OrigemCliente, getSexoOptions, getEstadoCivilOptions, getOrigemClienteOptions
} from '../models/cliente.models';
import { VeiculoService } from '../../veiculo/veiculo/veiculo.service';
import { VeiculoRequest, VeiculoResponse, MarcaVeiculoResponse, ModeloVeiculoResponse, StatusVeiculo } from '../../veiculo/models/veiculo.models';
import { forkJoin } from 'rxjs';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { isValidCpf, isValidCnpj } from '@shared/utils/validators';
import { UtilService } from '@shared/services/util.service';

interface ContatoExtra {
  tipoContato: string;
  valor: string;
}

interface EnderecoExtra {
  logradouro: string;
  numero: string;
  bairro: string;
  cidade: string;
  estado: string;
  cep: string;
}

interface DocumentoExtra {
  tipoDocumento: string;
  descricao: string;
  arquivoFile?: File | null;
}

@Component({
  selector: 'cadastro-cliente',
  standalone: true,
  templateUrl: './cadastro-cliente.html',
  styleUrls: ['./cadastro-cliente.scss'],
  imports: [
    CommonModule,
    FormsModule,
    SelectModule,
    InputTextModule,
    TextareaModule,
    DatePickerModule,
    TableModule,
    ToastModule,
    TooltipModule,
    DialogModule,
    TagModule,
    InputMaskModule,
    AutoCompleteModule
  ],
})
export class CadastroCliente implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly clientesService = inject(ClientesService);
  private readonly storage = inject(LocalStorageService);
  private readonly messageService = inject(MessageService);
  private readonly utilService = inject(UtilService);
  private readonly veiculoService = inject(VeiculoService);
  private readonly confirmationService = inject(ConfirmationService);

  @Input() modalMode = false;
  @Output() saved = new EventEmitter<any>();
  @Output() canceled = new EventEmitter<void>();

  activeIndex = 0;

  tabs = [
      { label: 'Geral', icon: 'pi pi-user' },
      { label: 'Endereço', icon: 'pi pi-map-marker' },
      { label: 'Contatos', icon: 'pi pi-phone' }
  ];
  savedClienteId: string | number | null = null;

  tiposPessoa = ['Física', 'Jurídica'];
  situacoes = ['Ativo', 'Inativo'];

  // Options for Dropdowns
  sexoOptions = getSexoOptions();
  estadoCivilOptions = getEstadoCivilOptions();
  origemOptions = getOrigemClienteOptions();

  // tipoContatoOptions removed (managed by getTipoContatoOptions)
  tipoEnderecoOptions = ['Residencial', 'Comercial', 'Cobrança', 'Entrega'];
  tipoDocumentoOptions = ['CPF', 'RG', 'CNH', 'CNPJ', 'Contrato Social', 'Comprovante de Residência', 'Outros'];

  model = {
    tipoPessoa: 'Física' as 'Física' | 'Jurídica',
    situacao: 'Ativo' as 'Ativo' | 'Inativo',
    nomeRazao: '',
    cpfCnpj: '',
    inscricaoMunicipal: '', // Adicionado para ambas as pessoas
    sexo: null as Sexo | null, // Added
    estadoCivil: null as EstadoCivil | null, // Added
    profissao: '',
    nomeFantasia: '',
    razaoSocial: '',
    inscricaoEstadual: '',
    dataNascimento: null as Date | null,
    origemCliente: null as OrigemCliente | null, // Added
    observacao: '',
    icmsIsento: false,
    telefonePrincipal: '',
    telefoneSecundario: '',
    email: '',
    cep: '',
    logradouro: '',
    numero: '',
    bairro: '',
    cidade: '',
    estado: '',
    uf: '',
    complemento: '',
    contatoResponsavel: '',
    enderecosExtras: [] as EnderecoExtra[],
    documentosExtras: [] as DocumentoExtra[],
    enderecoId: null as number | null, // ID do endereço principal para edição
  };

  // Contatos (Lista local ser salva posteriormente)
  contatosList: ContatoClienteRequest[] = [];
  showContatoDialog = false;
  contatoForm: ContatoClienteRequest = this.getEmptyContato();
  contatoEditIndex: number | null = null; // Guardar se estamos editando um contato

  // Opções de Contato
  tipoContatoOptions = getTipoContatoOptions();
  TipoContatoLabels = TipoContatoLabels; // Para usar no template

  // VEÍCULOS
  veiculosList: VeiculoResponse[] = [];
  veiculoModel: Partial<VeiculoRequest> = {};
  marcasOptions: MarcaVeiculoResponse[] = [];
  modelosOptions: ModeloVeiculoResponse[] = [];

  get pageTitle(): string {
    return this.isEditMode ? 'Editar Cliente' : 'Novo Cliente';
  }

  isEditMode = false;
  submitted = false; // Flag para controle de validação visual

  get contatoDialogTitle(): string {
    return 'Adicionar Contato';
  }

  ngOnInit() {
    this.loadMarcas(); // Carrega marcas para o combo de veículo
    // Verifica se há ID na rota para edição
    const uuid = this.route.snapshot.paramMap.get('uuid');
    if (uuid) {
      this.isEditMode = true;
      this.savedClienteId = uuid; // Pode ser string ou numérico
      this.loadClienteData(uuid);
    }
  }

  loadClienteData(id: string | number) {
    this.clientesService.getById(id).subscribe({
      next: (data: ClienteResponseDTO) => {
        this.patchModel(data);
        // Carrega listas dependentes
        this.loadContatos(id);
        this.loadEnderecos(id);
        this.loadDocumentos(id);
        this.loadVeiculos(id);
      },
      error: (err) => {
        console.error('Erro ao carregars cliente', err);
        // O interceptor global já mostrará o erro de rede.
      }
    });
  }

  patchModel(data: ClienteResponseDTO) {
    this.model.tipoPessoa = (data.tipoCliente === 'PESSOA_JURIDICA') ? 'Jurídica' : 'Física';
    this.model.situacao = (data.status === 'ATIVO') ? 'Ativo' : 'Inativo';
    this.model.origemCliente = data.origemCliente as OrigemCliente || null;

    if (this.model.tipoPessoa === 'Física') {
      this.model.nomeRazao = data.nomeCompleto || '';
      this.model.cpfCnpj = data.cpf || '';
      this.model.dataNascimento = data.dataNascimento ? new Date(data.dataNascimento + 'T00:00:00') : null;
      this.model.sexo = data.sexo as Sexo || null;
      this.model.estadoCivil = data.estadoCivil as EstadoCivil || null;
      this.model.profissao = data.profissao || '';
    } else {
      this.model.razaoSocial = data.razaoSocial || data.nomeCompleto || '';
      this.model.nomeFantasia = data.nomeFantasia || '';
      this.model.cpfCnpj = data.cnpj || '';
      this.model.inscricaoEstadual = data.inscricaoEstadual || '';
    }
    this.model.inscricaoMunicipal = data.inscricaoMunicipal || '';
    this.model.observacao = data.observacoesGerais || '';
    this.model.email = data.email || '';
  }

  loadContatos(id: string | number) {
    this.clientesService.listarContatos(id).subscribe(res => {
      this.contatosList = (res.content || []).map(c => ({
        ...c,
        valor: (c as any).valor || (c as any).contato // Compatibilidade
      }));
    });
  }

  loadEnderecos(id: string | number) {
    this.clientesService.listarEnderecos(id).subscribe(res => {
      const addresses = res.content || [];
      this.model.enderecosExtras = addresses.map((e: any) => ({
        logradouro: e.logradouro || '',
        numero: e.numero || '',
        bairro: e.bairro || '',
        cidade: e.cidade || '',
        estado: e.estado || '',
        cep: e.cep || ''
      })) as EnderecoExtra[];

      // Popula o formulário principal com o endereço principal (ou o primeiro disponível)
      const principal = addresses[0];
      if (principal) {
          this.model.enderecoId = principal.id || null;
          this.model.cep = principal.cep || '';
          this.model.logradouro = principal.logradouro || '';
          this.model.numero = principal.numero || '';
          this.model.complemento = principal.complemento || '';
          this.model.bairro = principal.bairro || '';
          this.model.cidade = principal.cidade || '';
          this.model.estado = principal.estado || '';
          this.model.uf = principal.estado || '';
      }
    });
  }

  loadDocumentos(id: string | number) {
    // Implementar se necessário, similar aos outros
  }

  ufs: string[] = [];

  // Tipo de Pessoa change
  onTipoPessoaChange() {
    // Limpa campos de acordo com tipo
    if (this.model.tipoPessoa === 'Física') {
      this.model.inscricaoEstadual = '';
    } else {
      this.model.dataNascimento = null;
    }
    // Ajusta máscara do CPF/CNPJ
    this.model.cpfCnpj = this.applyCpfCnpjMask(this.stripNonDigits(this.model.cpfCnpj));
  }

  onDocumentoBlur() {
    if (!this.model.cpfCnpj) return;

    this.utilService.validarDocumento(this.model.cpfCnpj, this.model.tipoPessoa).subscribe({
      next: (isValid) => {
        if (!isValid) {
          this.messageService.add({
            severity: 'warn',
            summary: 'Atenção',
            detail: `O ${this.model.tipoPessoa === 'Física' ? 'CPF' : 'CNPJ'} informado não parece ser válido.`
          });
        }
      },
      error: (err) => console.error('Erro ao validar documento no backend', err)
    });
  }

  // Máscara dinâmica CPF/CNPJ

  private stripNonDigits(v: string) {
    return (v || '').replace(/\D+/g, '');
  }

  private stripNonAlphanumeric(v: string) {
    return (v || '').replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
  }

  private applyCpfCnpjMask(digits: string) {
    if (this.model.tipoPessoa === 'Física') {
      // CPF: 000.000.000-00
      const d = digits.slice(0, 11);
      let out = d;
      if (d.length > 3) out = d.slice(0, 3) + '.' + d.slice(3);
      if (d.length > 6) out = out.slice(0, 7) + '.' + d.slice(6);
      if (d.length > 9) out = out.slice(0, 11) + '-' + d.slice(9);
      return out;
    } else {
      // CNPJ: AA.AAA.AAA/AAAA-00 (Alfanumérico)
      const d = digits.toUpperCase().slice(0, 14);
      let out = d;
      if (d.length > 2) out = d.slice(0, 2) + '.' + d.slice(2);
      if (d.length > 5) out = out.slice(0, 6) + '.' + d.slice(5);
      if (d.length > 8) out = out.slice(0, 10) + '/' + d.slice(8);
      if (d.length > 12) out = out.slice(0, 15) + '-' + d.slice(12);
      return out;
    }
  }

  // Ações
  salvar() {
    this.submitted = true; // Ativa validação visual imediatamente

    console.log('💾 salvar() chamado. Model:', this.model);
    console.log('📝 isEditMode:', this.isEditMode, 'ID:', this.savedClienteId);

    // Validação simples alinhada ao backend
    const obrigatorios =
      this.model.tipoPessoa === 'Física'
        ? [this.model.nomeRazao, this.model.cpfCnpj]
        : [this.model.razaoSocial, this.model.cpfCnpj];

    console.log('🔍 Campos obrigatórios preenchidos?', obrigatorios);

    const invalid = obrigatorios.some(v => !v);
    if (invalid) {
      console.warn('❌ Validação falhou. Campos vazios.');
      this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'Preencha os campos obrigatórios (Nome/Razão e CPF/CNPJ).' });
      return;
    }

    // Validação de CPF/CNPJ (Frontend)
    if (this.model.tipoPessoa === 'Física') {
        if (this.model.cpfCnpj && !isValidCpf(this.model.cpfCnpj)) {
            this.messageService.add({ severity: 'error', summary: 'CPF Inválido', detail: 'O número de CPF informado é matematicamente inválido.' });
            return;
        }
    } else {
        if (this.model.cpfCnpj && !isValidCnpj(this.model.cpfCnpj)) {
            this.messageService.add({ severity: 'error', summary: 'CNPJ Inválido', detail: 'O número de CNPJ informado é matematicamente inválido.' });
            return;
        }
    }

    // Validação Obrigatória de Endereço
    if (!this.validateEndereco()) {
        this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'Preencha todos os campos obrigatórios do endereço.' });
        // Se a aba não for a de endereço, talvez redirecionar?
        if (this.activeIndex !== 1) {
             this.activeIndex = 1; // Vai para aba endereço
        }
        return;
    }

    // Preparar DTO (reaproveita lógica existente)
    const dto: ClienteRequestDTO = {
      tipoCliente: this.model.tipoPessoa === 'Física' ? TipoCliente.PESSOA_FISICA : TipoCliente.PESSOA_JURIDICA,
      nomeCompleto: this.model.tipoPessoa === 'Física' ? (this.model.nomeRazao || undefined) : (this.model.razaoSocial || undefined),
      nomeFantasia: this.model.tipoPessoa === 'Jurídica' ? (this.model.nomeFantasia || undefined) : undefined,
      razaoSocial: this.model.tipoPessoa === 'Jurídica' ? (this.model.razaoSocial || undefined) : undefined,
      cpf: this.model.tipoPessoa === 'Física' ? this.stripNonDigits(this.model.cpfCnpj) : undefined,
      cnpj: this.model.tipoPessoa === 'Jurídica' ? this.stripNonAlphanumeric(this.model.cpfCnpj) : undefined,
      inscricaoEstadual: this.model.inscricaoEstadual || undefined,
      inscricaoMunicipal: this.model.inscricaoMunicipal || undefined,
      dataNascimento: this.model.tipoPessoa === 'Física' ? this.formatDate(this.model.dataNascimento) : undefined,
      sexo: this.model.tipoPessoa === 'Física' ? (this.model.sexo || undefined) : undefined,
      estadoCivil: this.model.tipoPessoa === 'Física' ? (this.model.estadoCivil || undefined) : undefined,
      profissao: this.model.tipoPessoa === 'Física' ? (this.model.profissao || undefined) : undefined,
      origemCliente: this.model.origemCliente || undefined,
      status: this.model.situacao === 'Ativo' ? StatusCliente.ATIVO : StatusCliente.INATIVO,
      observacoesGerais: this.model.observacao || undefined,
      email: this.model.email || undefined,
    };

    if (this.isEditMode && this.savedClienteId) {
      this.clientesService.update(this.savedClienteId, dto).subscribe({
        next: (res) => {
          this.messageService.add({ severity: 'success', summary: 'Atualizado', detail: 'Cliente atualizado com sucesso!' });
          if (this.modalMode) {
             this.saved.emit(res);
          } else {
             // Tenta salvar dados adicionais também no update
             this.salvarDadosAdicionais(this.savedClienteId!, true);
          }
          // Salva/Atualiza dados extras (simplesmente chama salvarDadosAdicionais por enquanto)
          // Nota: idealmente teríamos lógica de diff para atualização, mas salvarDadosAdicionais cria novos.
          // Para MVP de edição, focar em salvar o principal.
        },
        error: (err) => {
             console.error('Erro ao atualizar', err);
             // Interceptor trata o erro de rede
        }
      });
    } else {
        // Modo Criação (Lógica original)
        this.clientesService.create(dto).subscribe({
          next: (res: ClienteResponseDTO) => {
            const id = (res as any).id ?? null;
            this.savedClienteId = id;

            if (this.modalMode) {
              this.saved.emit(res);
            } else {
              if (id) {
                // Ao criar, se tiver ID, tenta salvar endereço também se preenchido
                this.salvarDadosAdicionais(id);
              } else {
                 this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Cliente salvo com sucesso!' });
                 // Mantém na mesma aba para o usuário decidir o próximo passo
              }
            }
          },
          error: (err: unknown) => {
            console.error('Erro ao salvar cliente', err);
            alert('Erro ao salvar cliente.');
          },
        });
    }
  }

  salvarDadosAdicionais(clienteId: string | number, omitSuccessToast = false) {
    const requests: any[] = [];

    // 1. Endereço Principal
    if (this.model.cep && this.model.logradouro) {
      const endereco: EnderecoClienteRequest = {
        cep: this.stripNonDigits(this.model.cep),
        logradouro: this.model.logradouro,
        numero: this.model.numero || 'S/N',
        complemento: this.model.complemento,
        bairro: this.model.bairro,
        cidade: this.model.cidade,
        estado: this.model.estado
      };

      if (this.model.enderecoId) {
          requests.push(this.clientesService.atualizarEndereco(clienteId, this.model.enderecoId, endereco));
      } else {
          requests.push(this.clientesService.criarEndereco(clienteId, endereco));
      }
    }

    // 2. Contatos da Lista
    if (this.contatosList.length > 0) {
      this.contatosList.forEach((contato: any) => {
        const payload = { tipoContato: contato.tipoContato, contato: contato.valor };

        if (contato.id) {
            requests.push(this.clientesService.atualizarContato(clienteId, contato.id, payload as any));
            return;
        }

        requests.push(this.clientesService.criarContato(clienteId, payload as any));
      });
    }

    if (requests.length > 0) {
      forkJoin(requests).subscribe({
        next: () => {
          if (!omitSuccessToast) {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Cliente e dados cadastrados!' });
          }
          // Mantém na mesma aba
        },
        error: (err) => {
          console.error('Erro ao salvar dados adicionais', err);

          // Tratamento de erro padronizado
          let detail = 'Erro ao salvar endereço/contato.';
          if (err.error && Array.isArray(err.error)) {
              // Se for lista de erros do backend
              detail = err.error.map((e: any) => `${e.campo}: ${e.erro}`).join(', ');
          } else if (err.error && err.error.message) {
              detail = err.error.message;
          }

          this.messageService.add({ severity: 'error', summary: 'Erro', detail });
          this.activeIndex = 1; // Fica na aba de endereço para corrigir
        }
      });
    } else {
      if (!omitSuccessToast) {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Cliente salvo com sucesso!' });
      }
      this.activeIndex = 1;
    }
  }

  // ========== MÉTODOS DE CONTATO (Lista Local) ==========

  abrirDialogContato() {
    this.contatoForm = this.getEmptyContato();
    this.contatoEditIndex = null;
    this.showContatoDialog = true;
  }

  editarContatoLista(index: number) {
    const contato = this.contatosList[index];
    this.contatoForm = { ...contato, valor: (contato as any).contato || contato.valor };
    this.contatoEditIndex = index;
    this.showContatoDialog = true;
  }

  salvarContatoLista() {
    const valorSalvar = this.isPhoneType(this.contatoForm.tipoContato) 
      ? this.stripNonDigits(this.contatoForm.valor) 
      : this.contatoForm.valor;

    const payload = { tipoContato: this.contatoForm.tipoContato, contato: valorSalvar };

    if (this.savedClienteId) {
      // Auto-save logic (Edição remota)
      const contatoId = this.contatoEditIndex !== null ? (this.contatosList[this.contatoEditIndex] as unknown as ContatoClienteResponse).id : null;

      if (contatoId) {
          // Atualiza o contato existente na API
          this.clientesService.atualizarContato(this.savedClienteId, contatoId, payload as any).subscribe({
              next: (res) => {
                  this.contatosList[this.contatoEditIndex!] = {
                      ...res,
                      valor: (res as any).contato || res.valor
                  };
                  this.contatosList = [...this.contatosList];
                  this.showContatoDialog = false;
                  this.messageService.add({ severity: 'success', summary: 'Salvo', detail: 'Contato atualizado!' });
              },
              error: (err) => {
                  console.error(err);
                  // Interceptor trata o erro de rede
              }
          });
      } else {
          // Cria novo contato na API
          this.clientesService.criarContato(this.savedClienteId, payload as any).subscribe({
            next: (res) => {
              this.contatosList.push({
                ...res,
                valor: (res as any).contato || res.valor
              });
              this.contatosList = [...this.contatosList];
              this.showContatoDialog = false;
              this.messageService.add({ severity: 'success', summary: 'Salvo', detail: 'Contato salvo automaticamente!' });
            },
            error: (err) => {
              console.error(err);
              // Interceptor trata o erro de rede
            }
          });
      }
    } else {
      // Local logic (Criação/Edição em memória, antes de salvar cliente)
      if (this.contatoEditIndex !== null) {
          this.contatosList[this.contatoEditIndex] = { ...this.contatoForm };
          this.messageService.add({ severity: 'success', summary: 'Atualizado', detail: 'Contato atualizado na lista (Salvar cliente p/ gravar)' });
      } else {
          this.contatosList.push({ ...this.contatoForm });
          this.messageService.add({ severity: 'success', summary: 'Adicionado', detail: 'Contato na lista (Salvar cliente p/ gravar)' });
      }
      this.contatosList = [...this.contatosList]; // Refresh reference
      this.showContatoDialog = false;
    }
  }

  removerContatoLista(index: number) {
    const contato = this.contatosList[index] as any;

    if (this.savedClienteId && contato.id) {
       // Auto-delete logic (Edição)
       // Simples confirmação via confirm nativo ou direto (usuário pediu agilidade)
       if (confirm('Tem certeza que deseja excluir este contato permanentemente?')) {
          this.clientesService.excluirContato(this.savedClienteId, contato.id).subscribe({
            next: () => {
              this.contatosList.splice(index, 1);
              this.contatosList = [...this.contatosList];
              this.messageService.add({ severity: 'success', summary: 'Excluído', detail: 'Contato removido do servidor.' });
            },
            error: (err) => {
              console.error(err);
              // Interceptor trata o erro de rede
            }
          });
       }
    } else {
       // Local remove
       this.contatosList.splice(index, 1);
       this.contatosList = [...this.contatosList];
    }
  }

  getTipoContatoLabel(tipo: TipoContato): string {
    return TipoContatoLabels[tipo];
  }

  private getEmptyContato(): ContatoClienteRequest {
    return {
      tipoContato: TipoContato.CELULAR,
      valor: ''
    };
  }


  private mapTipoEnderecoLabelToEnum(label: string): TipoEndereco {
    const l = (label || '').toUpperCase();
    if (l.includes('RESIDEN')) return TipoEndereco.RESIDENCIAL;
    if (l.includes('COMER')) return TipoEndereco.COMERCIAL;
    if (l.includes('COBRAN')) return TipoEndereco.COBRANCA;
    if (l.includes('ENTREGA')) return TipoEndereco.ENTREGA;
    return TipoEndereco.OUTROS;
  }

  private mapTipoDocumentoLabelToEnum(label: string): TipoDocumento {
    const l = (label || '').toUpperCase();
    if (l === 'CPF') return TipoDocumento.CPF;
    if (l === 'RG') return TipoDocumento.RG;
    if (l === 'CNH') return TipoDocumento.CNH;
    if (l === 'CNPJ') return TipoDocumento.CNPJ;
    if (l.includes('CONTRATO')) return TipoDocumento.CONTRATO_SOCIAL;
    if (l.includes('RESID')) return TipoDocumento.COMPROVANTE_RESIDENCIA;
    return TipoDocumento.OUTROS;
  }



  isPhoneType(tipo: any): boolean {
    return ['TELEFONE_FIXO', 'CELULAR', 'WHATSAPP', 'TELEGRAM'].includes(tipo);
  }

  getContactMask(tipo: any): string {
    if (tipo === 'TELEFONE_FIXO') return '(99) 9999-9999';
    if (['CELULAR', 'WHATSAPP', 'TELEGRAM'].includes(tipo)) return '(99) 99999-9999';
    return '';
  }

  getContactPlaceholder(tipo: any): string {
    const mask = this.getContactMask(tipo);
    if (!mask) return 'Digite o contato...';
    return mask.replace(/9/g, '0');
  }

  formatarContato(contato: any): string {
    const valor = contato.valor || (contato as any).contato || '';
    if (this.isPhoneType(contato.tipoContato)) {
       return this.applyPhoneMask(valor, contato.tipoContato);
    }
    return valor;
  }

  private applyPhoneMask(digits: string, tipo: any): string {
    const d = this.stripNonDigits(digits);
    if (tipo === 'TELEFONE_FIXO' && d.length >= 10) {
       return `(${d.slice(0, 2)}) ${d.slice(2, 6)}-${d.slice(6, 10)}`;
    }
    if (d.length >= 11) {
       return `(${d.slice(0, 2)}) ${d.slice(2, 7)}-${d.slice(7, 11)}`;
    }
    return d; // Retorna apenas os dígitos se não bater com tamanho padrão
  }

  validateEndereco(): boolean {
      const { cep, logradouro, numero, bairro, cidade, estado } = this.model;
      return !!(cep && logradouro && numero && bairro && cidade && estado);
  }

  salvarEnderecosExtras() {
    // submitted já foi setado true em salvar() se veio de lá, mas forçamos caso seja chamado isoladamente
    this.submitted = true;

    if (!this.savedClienteId) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Salve o cliente primeiro para habilitar os endereços.' });
      return;
    }

    // Validação de campos obrigatórios do endereço principal do formulário
    if (!this.validateEndereco()) {
        this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'Preencha todos os campos obrigatórios do endereço (CEP, Rua, Número, Bairro, Cidade, Estado).' });
        return;
    }

    const clienteId = this.savedClienteId;
    const payloads: EnderecoClienteRequest[] = (this.model.enderecosExtras || [])
      .filter(e => (e.logradouro || '').trim() && (e.numero || '').trim() && (e.cidade || '').trim() && (e.estado || '').trim() && (e.cep || '').trim() && (e.bairro || '').trim())
      .map(e => ({
        cep: e.cep,
        logradouro: e.logradouro,
        numero: e.numero,
        complemento: undefined,
        bairro: e.bairro,
        cidade: e.cidade,
        estado: e.estado
      }));

    if (!payloads.length) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha os campos obrigatórios dos endereços para salvar.' });
      return;
    }

    const requests = payloads.map(p => this.clientesService.criarEndereco(clienteId, p));
    forkJoin(requests).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Endereços salvos com sucesso!' });
      },
      error: err => {
        console.error('Erro ao salvar endereços', err);
        // Interceptor trata o erro de rede
      }
    });
  }

  excluirCliente() {
      if (!this.savedClienteId) return;

      this.confirmationService.confirm({
          title: 'Excluir Cliente',
          message: `Tem certeza que deseja excluir <span class="font-semibold text-slate-900">${this.model.nomeRazao || 'este cliente'}</span>? Esta ação não pode ser desfeita. Todos os contatos, endereços e veículos deste cliente também serão excluídos.`,
          confirmText: 'Excluir Definitivamente',
          cancelText: 'Cancelar',
          type: 'danger',
          icon: 'warning'
      }).subscribe(confirmed => {
          if (confirmed) {
              this.clientesService.deleteCliente(this.savedClienteId as number).subscribe({
                  next: () => {
                      this.messageService.add({ severity: 'success', summary: 'Excluído', detail: 'Cliente deletado com sucesso!' });
                      if (!this.modalMode) {
                          this.router.navigate(['/cliente']);
                      } else {
                          this.saved.emit(null);
                      }
                  },
                  error: (err: unknown) => {
                      console.error('Erro ao excluir cliente:', err);
                      // Interceptor trata o erro de rede
                  }
              });
          }
      });
  }

  voltar() {
    this.router.navigate(['/cliente']);
  }

  cancelar() {
    if (this.modalMode) {
      this.canceled.emit();
    } else {
      this.router.navigate(['/cliente']);
    }
  }



  adicionarEndereco() {
    this.model.enderecosExtras.push({
      logradouro: '',
      numero: '',
      bairro: '',
      cidade: '',
      estado: '',
      cep: ''
    });
  }

  removerEndereco(index: number) {
    this.model.enderecosExtras.splice(index, 1);
  }

  adicionarDocumento() {
    this.model.documentosExtras.push({
      tipoDocumento: 'CPF',
      descricao: '',
      arquivoFile: null,
    });
  }

  removerDocumento(index: number) {
    this.model.documentosExtras.splice(index, 1);
  }

  onDocumentoFileSelected(index: number, event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files && input.files.length ? input.files[0] : null;
    if (this.model.documentosExtras[index]) {
      this.model.documentosExtras[index].arquivoFile = file;
    }
  }


  buscarCep() {
    if (!this.model.cep || this.model.cep.length < 8) return;

    // Verifica se algum dado relevante já está preenchido
    const temDados = !!(this.model.logradouro || this.model.bairro || this.model.cidade || this.model.estado);

    if (temDados) {
        this.confirmationService.confirm({
            title: 'Alterar Endereço?',
            message: 'Os campos de endereço já estão preenchidos. Deseja sobrescrevê-los com os dados deste CEP?',
            confirmText: 'Sim, sobrescrever',
            cancelText: 'Não, manter atual',
            type: 'warning',
            icon: 'warning'
        }).subscribe(confirmed => {
            if (confirmed) {
                this.executarBuscaCep(this.model.cep);
            }
        });
    } else {
        this.executarBuscaCep(this.model.cep);
    }
  }

  private executarBuscaCep(cep: string) {
    this.utilService.buscarCep(cep).subscribe({
      next: (data) => {
        if (data.erro) {
          this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'CEP não encontrado.' });
          return;
        }
        this.model.logradouro = data.logradouro || '';
        this.model.bairro = data.bairro || '';
        this.model.cidade = data.localidade || '';
        this.model.estado = data.uf || '';
        this.model.uf = data.uf || '';

        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Endereço preenchido automaticamente.' });
      },
      error: (err) => {
        console.error('Erro ao buscar CEP', err);
        // O interceptor já mostrará o erro de rede
      }
    });
  }

  salvarDocumentosExtras() {
    if (!this.savedClienteId) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Salve o cliente primeiro para habilitar os documentos.' });
      return;
    }
    const clienteId = this.savedClienteId;
    const docs = (this.model.documentosExtras || []).filter(d => !!d.arquivoFile);

    if (!docs.length) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Adicione ao menos um documento com arquivo para salvar.' });
      return;
    }

    const uploads = docs.map(d => this.clientesService.uploadDocumento(
      clienteId,
      d.arquivoFile as File,
      d.tipoDocumento,
      d.descricao || undefined
    ));

    forkJoin(uploads).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Documentos enviados com sucesso!' });
      },
      error: err => {
        console.error('Erro ao enviar documentos', err);
        // Interceptor trata o erro de rede
      }
    });
  }

  // ========== VEÍCULOS ==========

  loadVeiculos(id: string | number) {
      // Cast to number if needed, service expects string/number usually fine but let's check
      this.veiculoService.list(Number(id)).subscribe({
          next: (res) => this.veiculosList = res,
          error: (err) => console.error('Erro ao carregar veículos', err)
      });
  }

  loadMarcas() {
      this.veiculoService.listMarcas({}).subscribe(res => {
          this.marcasOptions = res.content || [];
      });
  }

  onMarcaChange() {
      if (this.veiculoModel.marcaId) {
          this.veiculoService.listModelos(this.veiculoModel.marcaId).subscribe(res => {
              this.modelosOptions = res || [];
          });
      } else {
          this.modelosOptions = [];
      }
  }

  // ========== HELPERS ==========

  private formatDate(date: any): string | undefined {
      if (!date) return undefined;
      if (date instanceof Date) {
          return date.toISOString().split('T')[0];
      }
      if (typeof date === 'string' && date.trim()) {
           return date.split('T')[0];
      }
      return undefined;
  }


  salvarVeiculo() {
      if (!this.savedClienteId) {
          this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Salve o cliente primeiro antes de adicionar veículos.' });
          return;
      }
      if (!this.veiculoModel.placa || !this.veiculoModel.marcaId || !this.veiculoModel.modeloId) {
           this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha Placa, Marca e Modelo.' });
           return;
      }

      const dto: VeiculoRequest = {
          clienteId: Number(this.savedClienteId),
          placa: this.veiculoModel.placa,
          marcaId: this.veiculoModel.marcaId,
          modeloId: this.veiculoModel.modeloId,
          anoModeloId: this.veiculoModel.anoModeloId,
          renavam: this.veiculoModel.renavam,
          chassi: this.veiculoModel.chassi,
          corExterna: this.veiculoModel.corExterna,
          status: StatusVeiculo.ATIVO // Default
          // Adicionar outros campos conforme necessário do form
      };

      this.veiculoService.create(dto).subscribe({
          next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Veículo adicionado!' });
              this.veiculoModel = {}; // Reset
              this.loadVeiculos(this.savedClienteId!);
          },
          error: (err) => {
              console.error(err);
              // Interceptor trata o erro de rede
          }
      });
  }
}
