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
import { TipoCliente, StatusCliente, TipoContato, TipoEndereco, TipoDocumento, ContatoClienteRequest, ContatoClienteResponse, EnderecoClienteRequest, DocumentoClienteRequest, DocumentoClienteResponse, getTipoContatoOptions, TipoContatoLabels, getStatusClienteOptions, StatusClienteLabels,
  Sexo, EstadoCivil, OrigemCliente, getSexoOptions, getEstadoCivilOptions, getOrigemClienteOptions
} from '../models/cliente.models';
import { VeiculoService } from '../../veiculo/veiculo/veiculo.service';
import { VeiculoRequest, VeiculoResponse, MarcaVeiculoResponse, ModeloVeiculoResponse, AnoModeloResponse, TipoCombustivelResponse, StatusVeiculo } from '../../veiculo/models/veiculo.models';
import { forkJoin } from 'rxjs';
import { NgxPermissionsService } from 'ngx-permissions';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { isValidCpf, isValidCnpj } from '@shared/utils/validators';
import { UtilService } from '@shared/services/util.service';
import { CnpjMaskDirective } from '@shared/directives/cnpj-mask';
import { environment } from '../../../../environments/environment';

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
    AutoCompleteModule,
    CnpjMaskDirective
  ],
})
export class CadastroCliente implements OnInit {
  readonly isUxPreview = environment.uxPreview;
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly clientesService = inject(ClientesService);
  private readonly storage = inject(LocalStorageService);
  private readonly messageService = inject(MessageService);
  private readonly utilService = inject(UtilService);
  private readonly veiculoService = inject(VeiculoService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly permissionsService = inject(NgxPermissionsService);

  @Input() modalMode = false;
  @Output() saved = new EventEmitter<any>();
  @Output() canceled = new EventEmitter<void>();

  activeIndex = 0;

  readonly tabs = [
      { label: 'Dados Gerais' },
      { label: 'Endereço' },
      { label: 'Contatos' },
      { label: 'Veículos' }
  ];
  savedClienteId: string | number | null = null;

  tiposPessoa = ['Física', 'Jurídica'];
  situacoes = getStatusClienteOptions();
  statusLabels = StatusClienteLabels;

  // Options for Dropdowns
  sexoOptions = getSexoOptions();
  estadoCivilOptions = getEstadoCivilOptions();
  origemOptions = getOrigemClienteOptions();

  // tipoContatoOptions removed (managed by getTipoContatoOptions)
  tipoEnderecoOptions = ['Residencial', 'Comercial', 'Cobrança', 'Entrega'];
  tipoDocumentoOptions = ['CPF', 'RG', 'CNH', 'CNPJ', 'Contrato Social', 'Comprovante de Residência', 'Outros'];
  readonly categoriaOptions = ['Comum', 'Locadora', 'Taxi - Cooperativa', 'Taxi - Individual', 'Taxi - Associadas', 'Executivo - Aplicativo Uber', 'Executivo - Aplicativo Outros', 'Executivo - Preto', 'Seguradora', 'Frotista', 'Particular'].map(value => ({ label: value, value }));
  readonly indicadorIsencaoOptions = [
    { label: 'Não contribuinte', value: 'NAO_CONTRIBUINTE' },
    { label: 'Contribuinte ICMS', value: 'CONTRIBUINTE_ICMS' },
    { label: 'Contribuinte isento de inscrição', value: 'CONTRIBUINTE_ISENTO' }
  ];
  readonly noticiaOptions = [{ label: 'Sim', value: true }, { label: 'Não', value: false }];

  model = {
    tipoPessoa: 'Física' as 'Física' | 'Jurídica',
    situacao: StatusCliente.ATIVO as StatusCliente,
    nomeRazao: '',
    cpfCnpj: '',
    inscricaoMunicipal: '', // Adicionado para ambas as pessoas
    rg: '', categoria: '', indicadorIsencao: '', emailSecundario: '', outrosContatos: '',
    enviarNoticia: false, classificacao: null as number | null,
    limiteDescontoPecas: null as number | null, limiteDescontoServicos: null as number | null,
    bloquearSeAtrasar: null as number | null,
    sexo: null as Sexo | null, // Added
    estadoCivil: null as EstadoCivil | null, // Added
    profissao: '',
    documentoEstrangeiro: '',
    numeroApolice: '',
    limiteCredito: null as number | null,
    comoNosConheceu: '',
    site: '',
    locadoraVeiculos: false,
    orgaoPublico: false,
    retencaoIss: false,
    retencaoIrrfCsll: false,
    alertaLgpd: false,
    autorizacaoLgpd: null as boolean | null,
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
    pais: 'Brasil',
    complemento: '',
    contatoResponsavel: '',
    enderecosExtras: [] as EnderecoExtra[],
    documentosExtras: [] as DocumentoExtra[],
    enderecoId: null as number | null, // ID do endereço principal para edição
  };

  formatDateForInput(value: Date | null): string {
    if (!value) return '';
    const y=value.getFullYear(),m=String(value.getMonth()+1).padStart(2,'0'),d=String(value.getDate()).padStart(2,'0');
    return [y,m,d].join('-');
  }
  onBirthDateInput(value: string): void {
    this.model.dataNascimento = /^\d{4}-\d{2}-\d{2}$/.test(value) ? new Date(value + 'T00:00:00') : null;
  }

  // Contatos (Lista local ser salva posteriormente)
  contatosList: ContatoClienteRequest[] = [];
  showContatoDialog = false;
  contatoForm: ContatoClienteRequest = this.getEmptyContato();
  contatoChannelOpen = false;
  contactFormError = '';
  get canSubmitContact(): boolean { return !!this.contatoForm.nome?.trim() && this.contatoChannelOpen && !!this.contatoForm.valor?.trim() && !this.isSaving; }
  contatoEditIndex: number | null = null; // Guardar se estamos editando um contato

  // Opções de Contato
  tipoContatoOptions = getTipoContatoOptions();
  TipoContatoLabels = TipoContatoLabels; // Para usar no template

  // DOCUMENTOS
  documentosList: DocumentoClienteResponse[] = [];
  documentosLoading = false;
  documentosError = false;
  documentosPage = 0;
  documentosTotal = 0;
  readonly documentosPageSize = 10;
  get documentosPages(): number {
    return Math.max(1, Math.ceil(this.documentosTotal / this.documentosPageSize));
  }
  isSavingDocuments = false;
  get canManageDocuments(): boolean {
    return !this.isUxPreview && !!this.permissionsService.getPermission('CLIENTE_EDITAR');
  }

  // VEÍCULOS
  veiculosList: VeiculoResponse[] = [];
  veiculosLoading = false;
  veiculosError = false;
  veiculoModel: Partial<VeiculoRequest> = {};
  marcasOptions: MarcaVeiculoResponse[] = [];
  modelosOptions: ModeloVeiculoResponse[] = [];
  anosModeloOptions: AnoModeloResponse[] = [];
  get anoModeloSelectOptions() {
    return this.anosModeloOptions.map(ano => ({ label: `${ano.anoFabricacao} / ${ano.anoModelo}`, value: ano.id }));
  }
  combustiveisOptions: TipoCombustivelResponse[] = [];
  showVeiculoDialog = false;
  isSavingVehicle = false;
  vehicleFormError = '';
  vehiclePreviewFailed = false;
  readonly vehicleImageFailures = new Set<number>();

  get selectedModeloImage(): string | undefined {
    return this.modelosOptions.find(modelo => modelo.id === this.veiculoModel.modeloId)?.imagemUrl || undefined;
  }

  get pageTitle(): string {
    return this.isEditMode ? 'Editar Cliente' : 'Novo Cliente';
  }

  get canSaveForm(): boolean {
    const allowed=this.isUxPreview || !!this.permissionsService.getPermission(this.isEditMode ? 'CLIENTE_EDITAR' : 'CLIENTE_CRIAR');
    if(!allowed)return false;
    if(!this.modalMode)return true;
    return this.model.tipoPessoa==='Física'
      ? !!this.model.nomeRazao.trim()
      : !!this.model.razaoSocial.trim() && !!this.model.nomeFantasia.trim();
  }

  get canDeleteClient(): boolean {
    return this.isUxPreview || !!this.permissionsService.getPermission('CLIENTE_EXCLUIR');
  }

  get canReadCommunication(): boolean {
    return !this.isUxPreview &&
      (!!this.permissionsService.getPermission('GERAL_USUARIO')
        || !!this.permissionsService.getPermission('ADMIN'));
  }

  abrirComunicacoesCliente(): void {
    if (!this.savedClienteId || !this.canReadCommunication) return;
    const clienteId = Number(this.savedClienteId);
    if (!Number.isSafeInteger(clienteId) || clienteId <= 0) return;
    this.router.navigate(['/cliente/crm/comunicacao'], { queryParams: { clienteId } });
  }

  isEditMode = false;
  submitted = false; // Flag para controle de validação visual
  isSaving = false; // Bloqueia submissões simultâneas e sinaliza o estado da operação
  isLoadingClient = false;
  clientLoadError = false;

  get contatoDialogTitle(): string {
    return this.contatoEditIndex !== null ? 'Editar contato' : 'Adicionar contato';
  }

  onTabKeydown(event: KeyboardEvent, index: number) {
    const direction = event.key === 'ArrowRight' ? 1 : event.key === 'ArrowLeft' ? -1 : 0;
    const target = event.key === 'Home' ? 0 : event.key === 'End' ? this.tabs.length - 1 :
      direction ? (index + direction + this.tabs.length) % this.tabs.length : -1;
    if (target < 0) return;
    event.preventDefault();
    this.activeIndex = target;
    document.getElementById(`nt-client-form-tab-${target}`)?.focus();
  }

  ngOnInit() {
    if (!this.isUxPreview) { this.loadMarcas(); this.loadCombustiveis(); } // Não exigir backend para homologar o cadastro
    // Verifica se há ID na rota para edição
    const uuid = this.route.snapshot.paramMap.get('uuid');
    if (uuid) {
      this.isEditMode = true;
      this.savedClienteId = uuid; // Pode ser string ou numérico
      this.loadClienteData(uuid);
    }
  }

  loadClienteData(id: string | number) {
    this.isLoadingClient = true;
    this.clientLoadError = false;
    this.clientesService.getById(id).subscribe({
      next: (data: ClienteResponseDTO) => {
        this.isLoadingClient = false;
        this.patchModel(data);
        // Carrega listas dependentes
        this.loadContatos(id);
        this.loadEnderecos(id);
        this.loadVeiculos(id);
      },
      error: (err) => {
        this.isLoadingClient = false;
        this.clientLoadError = true;
        console.error('Erro ao carregar cliente', err);
        // O interceptor global já mostrará o erro de rede.
      }
    });
  }

  patchModel(data: ClienteResponseDTO) {
    this.model.tipoPessoa = (data.tipoCliente === 'PESSOA_JURIDICA') ? 'Jurídica' : 'Física';
    this.model.situacao = (data.status as StatusCliente) || StatusCliente.ATIVO;
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
    this.model.rg = data.rg || '';
    this.model.categoria = data.categoria || '';
    this.model.indicadorIsencao = data.indicadorIsencao || '';
    this.model.emailSecundario = data.emailSecundario || '';
    this.model.outrosContatos = data.outrosContatos || '';
    this.model.enviarNoticia = data.enviarNoticia ?? false;
    this.model.classificacao = data.classificacao ?? null;
    this.model.limiteDescontoPecas = data.limiteDescontoPecas ?? null;
    this.model.limiteDescontoServicos = data.limiteDescontoServicos ?? null;
    this.model.bloquearSeAtrasar = data.bloquearSeAtrasar ?? null;
    this.model.documentoEstrangeiro = data.documentoEstrangeiro || '';
    this.model.pais = data.pais || 'Brasil';
    this.model.numeroApolice = data.numeroApolice || '';
    this.model.limiteCredito = data.limiteCredito ?? null;
    this.model.comoNosConheceu = data.comoNosConheceu || '';
    this.model.site = data.site || '';
    this.model.locadoraVeiculos = data.locadoraVeiculos ?? false;
    this.model.orgaoPublico = data.orgaoPublico ?? false;
    this.model.retencaoIss = data.retencaoIss ?? false;
    this.model.retencaoIrrfCsll = data.retencaoIrrfCsll ?? false;
    this.model.alertaLgpd = data.alertaLgpd ?? false;
    this.model.autorizacaoLgpd = data.autorizacaoLgpd ?? null;

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
          this.model.pais = principal.pais || 'Brasil';
      }
    });
  }

  loadDocumentos(id: string | number) {
    this.documentosLoading = true;
    this.documentosError = false;
    this.clientesService.listarDocumentos(id, this.documentosPage, this.documentosPageSize).subscribe({
      next: res => {
        this.documentosList = res.content || [];
        this.documentosTotal = res.totalElements || 0;
        this.documentosLoading = false;
      },
      error: () => {
        this.documentosList = [];
        this.documentosError = true;
        this.documentosLoading = false;
      }
    });
  }

  irParaPaginaDocumentos(page: number) {
    if (!this.savedClienteId || page < 0 || page * this.documentosPageSize >= this.documentosTotal) return;
    this.documentosPage = page;
    this.loadDocumentos(this.savedClienteId);
  }

  baixarDocumento(doc: DocumentoClienteResponse) {
    if (!this.savedClienteId || this.isUxPreview) return;
    this.clientesService.downloadDocumento(this.savedClienteId, doc.id).subscribe({
      next: blob => {
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = doc.arquivoNome || 'documento';
        link.click();
        setTimeout(() => URL.revokeObjectURL(url), 2000);
      },
      error: () => this.messageService.add({
        severity: 'error', summary: 'Download não realizado',
        detail: 'Não foi possível baixar este documento.'
      })
    });
  }

  excluirDocumentoCliente(doc: DocumentoClienteResponse) {
    if (!this.savedClienteId || !this.canManageDocuments) return;
    this.confirmationService.confirm({
      title: 'Excluir documento',
      message: 'Excluir este documento de forma permanente?',
      confirmText: 'Excluir documento',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (!confirmed || !this.savedClienteId) return;
      this.clientesService.excluirDocumento(this.savedClienteId, doc.id).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Excluído', detail: 'Documento removido.' });
          this.documentosPage = 0;
          this.loadDocumentos(this.savedClienteId!);
        },
        error: () => this.messageService.add({
          severity: 'error', summary: 'Exclusão não realizada',
          detail: 'Não foi possível excluir este documento.'
        })
      });
    });
  }

  ufs: string[] = [];

  // Tipo de Pessoa change
  onTipoPessoaChange() {
    // Limpa campos de acordo com tipo
    if (this.model.tipoPessoa === 'Física') {
      this.model.inscricaoEstadual = '';
    } else {
      this.model.dataNascimento = null;
      if(!this.model.indicadorIsencao)this.model.indicadorIsencao='CONTRIBUINTE_ICMS';
    }
    // Ajusta máscara do CPF/CNPJ
    this.model.cpfCnpj = this.applyCpfCnpjMask(this.stripNonDigits(this.model.cpfCnpj));
  }

  onDocumentoBlur() {
    if (!this.model.cpfCnpj) return;

    // No preview, a validação local permite testar o formulário sem API.
    if (this.isUxPreview) {
      const valido = this.model.tipoPessoa === 'Física'
        ? isValidCpf(this.model.cpfCnpj)
        : isValidCnpj(this.model.cpfCnpj);
      if (!valido) {
        this.messageService.add({
          severity: 'warn',
          summary: 'Documento inválido',
          detail: `O ${this.model.tipoPessoa === 'Física' ? 'CPF' : 'CNPJ'} informado não é válido.`
        });
      }
      return;
    }

    this.utilService.validarDocumento(this.model.cpfCnpj, this.model.tipoPessoa).subscribe({
      next: (isValid) => {
        if (!isValid) {
          this.messageService.add({
            severity: 'warn',
            summary: 'Atenção',
            detail: `O ${this.model.tipoPessoa === 'Física' ? 'CPF' : 'CNPJ'} informado não parece ser válido.`
          });
          return;
        }
        
        // Se for PJ e válido, busca na BrasilAPI para auto-preenchimento
        if (this.model.tipoPessoa === 'Jurídica' && this.stripNonDigits(this.model.cpfCnpj).length === 14 && !this.isEditMode) {
             this.buscarDadosCnpj(this.model.cpfCnpj);
        }
      },
      error: (err) => console.error('Erro ao validar documento no backend', err)
    });
  }

  private buscarDadosCnpj(cnpj: string) {
     this.utilService.buscarCnpj(cnpj).subscribe({
        next: (data) => {
            if (data.razao_social) {
                // Autocompleta se os campos estiverem vazios
                if (!this.model.razaoSocial) this.model.razaoSocial = data.razao_social;
                if (!this.model.nomeFantasia && data.nome_fantasia) this.model.nomeFantasia = data.nome_fantasia;
                
                if (data.cep && !this.model.cep) {
                    this.model.cep = data.cep;
                    this.model.logradouro = data.logradouro || '';
                    this.model.numero = data.numero || '';
                    this.model.complemento = data.complemento || '';
                    this.model.bairro = data.bairro || '';
                    this.model.cidade = data.municipio || '';
                    this.model.estado = data.uf || '';
                    this.model.uf = data.uf || '';
                    
                    this.messageService.add({ severity: 'success', summary: 'CNPJ Localizado', detail: 'Dados da empresa e endereço preenchidos automaticamente.' });
                } else {
                    this.messageService.add({ severity: 'success', summary: 'CNPJ Localizado', detail: 'Dados da empresa preenchidos automaticamente.' });
                }
            }
        },
        error: (err) => {
            console.error('Erro ao buscar CNPJ na BrasilAPI', err);
            // Silencioso para o usuário, permitindo digitação manual
        }
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
    const requiredPermission = this.isEditMode ? 'CLIENTE_EDITAR' : 'CLIENTE_CRIAR';
    if (!this.isUxPreview && !this.permissionsService.getPermission(requiredPermission)) {
      this.messageService.add({ severity: 'warn', summary: 'Sem permissão', detail: 'Seu perfil não pode salvar este cliente.' });
      return;
    }
    if (this.isSaving) return;
    this.submitted = true; // Ativa validação visual imediatamente

    // Validação simples alinhada ao backend
    const obrigatorios = this.modalMode
      ? (this.model.tipoPessoa === 'Física'
        ? [this.model.nomeRazao] : [this.model.razaoSocial, this.model.nomeFantasia])
      : (this.model.tipoPessoa === 'Física'
        ? [this.model.nomeRazao, this.model.cpfCnpj] : [this.model.razaoSocial, this.model.cpfCnpj]);

    const invalid = obrigatorios.some(v => !v);
    if (invalid) {
      this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'Preencha os campos obrigatórios indicados no formulário.' });
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

    if (this.model.limiteCredito !== null && this.model.limiteCredito < 0) {
      this.messageService.add({ severity:'error',summary:'Limite de crédito inválido',detail:'Informe um valor igual ou maior que zero.' });
      return;
    }

    // Validação Obrigatória de Endereço
    if (!this.modalMode && !this.validateEndereco()) {
        this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'Preencha todos os campos obrigatórios do endereço.' });
        // Se a aba não for a de endereço, talvez redirecionar?
        if (this.activeIndex !== 1) {
             this.activeIndex = 1; // Vai para aba endereço
        }
        return;
    }

    this.isSaving = true;

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
      status: this.model.situacao,
      observacoesGerais: this.model.observacao || undefined,
      email: this.model.email || undefined,
      rg: this.model.tipoPessoa === 'Física' ? (this.model.rg || undefined) : undefined,
      categoria: this.model.categoria || undefined,
      indicadorIsencao: this.model.indicadorIsencao || undefined,
      emailSecundario: this.model.emailSecundario || undefined,
      outrosContatos: this.model.outrosContatos || undefined,
      enviarNoticia: this.model.enviarNoticia,
      classificacao: this.model.classificacao ?? undefined,
      limiteDescontoPecas: this.model.limiteDescontoPecas ?? undefined,
      limiteDescontoServicos: this.model.limiteDescontoServicos ?? undefined,
      bloquearSeAtrasar: this.model.bloquearSeAtrasar ?? undefined,
      documentoEstrangeiro: this.model.documentoEstrangeiro || undefined,
      pais: this.model.pais || 'Brasil',
      numeroApolice: this.model.numeroApolice || undefined,
      limiteCredito: this.model.limiteCredito ?? undefined,
      comoNosConheceu: this.model.comoNosConheceu || undefined,
      site: this.model.site || undefined,
      locadoraVeiculos: this.model.locadoraVeiculos,
      orgaoPublico: this.model.orgaoPublico,
      retencaoIss: this.model.retencaoIss,
      retencaoIrrfCsll: this.model.retencaoIrrfCsll,
      alertaLgpd: this.model.alertaLgpd,
      autorizacaoLgpd: this.model.autorizacaoLgpd,
    };

    if (this.isEditMode && this.savedClienteId) {
      this.clientesService.update(this.savedClienteId, dto).subscribe({
        next: (res) => {
          if (this.modalMode) {
             this.salvarDadosAdicionais(this.savedClienteId!, true);
          } else {
             this.salvarDadosAdicionais(this.savedClienteId!, false);
          }
          // Salva/Atualiza dados extras (simplesmente chama salvarDadosAdicionais por enquanto)
          // Nota: idealmente teríamos lógica de diff para atualização, mas salvarDadosAdicionais cria novos.
          // Para MVP de edição, focar em salvar o principal.
        },
        error: (err) => {
             this.isSaving = false;
             console.error('Erro ao atualizar', err);
             this.messageService.add({ severity: 'error', summary: 'Não foi possível atualizar', detail: err.status === 409 ? 'Este CPF/CNPJ já está vinculado a outro cliente da empresa.' : 'Verifique os dados e tente novamente.' });
        }
      });
    } else {
        // Modo Criação (Lógica original)
        this.clientesService.create(dto).subscribe({
          next: (res: ClienteResponseDTO) => {
            const id = (res as any).id ?? null;
            this.savedClienteId = id;
            if (id) this.isEditMode = true;

            if (this.modalMode) {
              if (id) this.salvarDadosAdicionais(id, true);
              else {
                this.isSaving = false;
                this.saved.emit(res);
              }
            } else {
              if (id) {
                // Ao criar, se tiver ID, tenta salvar endereço também se preenchido
                this.salvarDadosAdicionais(id);
              } else {
                 this.isSaving = false;
                 this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Cliente salvo com sucesso!' });
                 // Mantém na mesma aba para o usuário decidir o próximo passo
              }
            }
          },
          error: (err: unknown) => {
            this.isSaving = false;
            console.error('Erro ao salvar cliente', err);
            this.messageService.add({ severity: 'error', summary: 'Não foi possível salvar', detail: (err as any)?.status === 409 ? 'Este CPF/CNPJ já está cadastrado para esta empresa.' : 'Verifique os dados e tente novamente.' });
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
        estado: this.model.estado,
        pais: this.model.pais || 'Brasil'
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
        const payload = { tipoContato: contato.tipoContato, contato: contato.valor,
          nome: contato.nome, sexo: contato.sexo, dataNascimento: contato.dataNascimento,
          grupoPrincipal: contato.grupoPrincipal ?? false };

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
          this.isSaving = false;
          if (this.modalMode) {
            this.saved.emit({ id: clienteId });
            return;
          }
          if (!omitSuccessToast) {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: this.isEditMode ? 'Cliente atualizado com sucesso!' : 'Cliente e dados cadastrados!' });
          }
          // Mantém na mesma aba
        },
        error: (err) => {
          this.isSaving = false;
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
      this.isSaving = false;
      if (this.modalMode) {
        this.saved.emit({ id: clienteId });
        return;
      }
      if (!omitSuccessToast) {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: this.isEditMode ? 'Cliente atualizado com sucesso!' : 'Cliente salvo com sucesso!' });
      }
      if (!this.isEditMode) this.activeIndex = 1;
    }
  }

  // ========== MÉTODOS DE CONTATO (Lista Local) ==========

  abrirDialogContato() {
    this.contatoForm = this.getEmptyContato();
    this.contatoChannelOpen = false;
    this.contactFormError = '';
    this.contatoEditIndex = null;
    this.showContatoDialog = true;
  }

  editarContatoLista(index: number) {
    const contato = this.contatosList[index];
    this.contatoForm = { ...contato, valor: (contato as any).contato || contato.valor };
    this.contatoChannelOpen = true;
    this.contactFormError = '';
    this.contatoEditIndex = index;
    this.showContatoDialog = true;
  }

  salvarContatoLista() {
    this.contactFormError = '';
    const valorSalvar = this.isPhoneType(this.contatoForm.tipoContato) 
      ? this.stripNonDigits(this.contatoForm.valor) 
      : this.contatoForm.valor;

    if (!this.contatoForm.nome?.trim()) {
      this.contactFormError='Informe o nome do contato.';
      this.messageService.add({ severity:'warn', summary:'Nome obrigatório', detail:'Informe o nome do contato.' });
      return;
    }
    if (!valorSalvar.trim()) {
      this.contactFormError='Adicione pelo menos um telefone, e-mail ou outro meio de contato.';
      this.messageService.add({ severity:'warn', summary:'Contato obrigatório', detail:'Informe telefone, e-mail ou outro contato.' });
      return;
    }
    const payload = {
      tipoContato: this.contatoForm.tipoContato, contato: valorSalvar,
      nome: this.contatoForm.nome?.trim() || undefined,
      sexo: this.contatoForm.sexo || undefined,
      dataNascimento: this.contatoForm.dataNascimento || undefined,
      grupoPrincipal: this.contatoForm.grupoPrincipal ?? false,
    };

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
      valor: '',
      nome: '', sexo: undefined, dataNascimento: undefined, grupoPrincipal: false
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
      if (!this.isUxPreview && !this.permissionsService.getPermission('CLIENTE_EXCLUIR')) {
          this.messageService.add({ severity: 'warn', summary: 'Sem permissão', detail: 'Seu perfil não pode excluir clientes.' });
          return;
      }
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
                          this.voltarParaLista();
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
    this.voltarParaLista();
  }

  cancelar() {
    if (this.modalMode) {
      this.canceled.emit();
    } else {
      this.voltarParaLista();
    }
  }

  private voltarParaLista() {
    this.router.navigate(['/cliente/listar'], { queryParams: this.route.snapshot.queryParams });
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
    if (this.isSavingDocuments || !this.canManageDocuments) {
      this.messageService.add({ severity: 'warn', summary: 'Sem permissão',
        detail: 'Você não pode enviar documentos para este cliente.' });
      return;
    }
    const docs = (this.model.documentosExtras || []).filter(d => !!d.arquivoFile);
    if (docs.some(d => !d.arquivoFile || d.arquivoFile.size > 5 * 1024 * 1024
       || !['application/pdf', 'image/jpeg', 'image/png'].includes(d.arquivoFile.type))) {
      this.messageService.add({ severity: 'warn', summary: 'Arquivo inválido',
        detail: 'Envie PDF, JPG ou PNG com até 5 MB.' });
      return;
    }
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

    this.isSavingDocuments = true;
    forkJoin(uploads).subscribe({
      next: () => {
        this.isSavingDocuments = false;
        this.model.documentosExtras = [];
        this.documentosPage = 0;
        this.loadDocumentos(clienteId);
        this.messageService.add({ severity: 'success', summary: 'Sucesso',
          detail: 'Documentos salvos com segurança.' });
      },
      error: () => {
        this.isSavingDocuments = false;
        this.loadDocumentos(clienteId);
        this.messageService.add({ severity: 'error', summary: 'Envio incompleto',
          detail: 'Verifique os arquivos cadastrados antes de tentar novamente; alguns podem ter sido salvos.' });
      }
    });
  }

  // ========== VEÍCULOS ==========

  loadVeiculos(id: string | number) {
      this.veiculosLoading = true;
      this.veiculosError = false;
      this.veiculoService.list(Number(id)).subscribe({
          next: (res) => {
            this.veiculosList = Array.isArray(res) ? res : [];
            this.vehicleImageFailures.clear();
            this.veiculosLoading = false;
          },
          error: () => {
            this.veiculosError = true;
            this.veiculosLoading = false;
          }
      });
  }

  openVehicle(vehicle: VeiculoResponse) {
    if (this.isUxPreview) {
      this.messageService.add({ severity: 'info', summary: 'Modo demonstrativo', detail: 'A edição de veículos não está disponível na prévia.' });
      return;
    }
    if (!this.isUxPreview && !this.permissionsService.getPermission('VEICULO_EDITAR')) {
      this.messageService.add({ severity: 'warn', summary: 'Sem permissão', detail: 'Seu perfil não pode editar veículos.' });
      return;
    }
    this.router.navigate(['/veiculo/editar', vehicle.id]);
  }

  addVehicle() {
    if (this.isUxPreview) {
      this.messageService.add({ severity: 'info', summary: 'Modo demonstrativo', detail: 'O cadastro de veículos não está disponível na prévia.' });
      return;
    }
    if (!this.savedClienteId) {
      this.messageService.add({ severity: 'info', summary: 'Salve o cliente primeiro', detail: 'Depois do cadastro, você poderá vincular um veículo.' });
      return;
    }
    if (!this.isUxPreview && !this.permissionsService.getPermission('VEICULO_CRIAR')) {
      this.messageService.add({ severity: 'warn', summary: 'Sem permissão', detail: 'Seu perfil não pode cadastrar veículos.' });
      return;
    }
    this.veiculoModel = {};
    this.modelosOptions = [];
    this.vehicleFormError = '';
    this.vehiclePreviewFailed = false;
    this.showVeiculoDialog = true;
  }

  loadMarcas() {
      this.veiculoService.listMarcas({ ativo: true, size: 1000 }).subscribe(res => {
          this.marcasOptions = res.content || [];
      });
  }

  loadCombustiveis() {
    this.veiculoService.listTiposCombustivel().subscribe({
      next: res => this.combustiveisOptions = res || [],
      error: () => this.vehicleFormError = 'Não foi possível carregar os combustíveis.'
    });
  }

  onMarcaChange() {
      this.vehicleFormError = '';
      const marcaId = this.veiculoModel.marcaId;
      this.veiculoModel.modeloId = undefined;
      this.veiculoModel.anoModeloId = undefined;
      this.modelosOptions = [];
      this.anosModeloOptions = [];
      this.vehiclePreviewFailed = false;
      if (marcaId) {
          this.veiculoService.listModelos(marcaId).subscribe({
            next: res => {
              if (this.veiculoModel.marcaId === marcaId) this.modelosOptions = res || [];
            },
            error: () => this.vehicleFormError = 'Não foi possível carregar os modelos. Tente selecionar a marca novamente.'
          });
      }
  }

  onModeloChange() {
    this.vehicleFormError = '';
    this.vehiclePreviewFailed = false;
    const modeloId = this.veiculoModel.modeloId;
    this.veiculoModel.anoModeloId = undefined;
    this.anosModeloOptions = [];
    if (modeloId) this.veiculoService.listAnosModelo(modeloId).subscribe({
      next: res => { if (this.veiculoModel.modeloId === modeloId) this.anosModeloOptions = res || []; },
      error: () => this.vehicleFormError = 'Não foi possível carregar os anos do modelo.'
    });
  }

  // ========== HELPERS ==========

  private formatDate(date: any): string | undefined {
      if (!date) return undefined;
      if (date instanceof Date) {
          const year = date.getFullYear();
          const month = String(date.getMonth() + 1).padStart(2, '0');
          const day = String(date.getDate()).padStart(2, '0');
          return `${year}-${month}-${day}`;
      }
      if (typeof date === 'string' && date.trim()) {
           return date.split('T')[0];
      }
      return undefined;
  }


  salvarVeiculo() {
      if (this.isSavingVehicle) return;
      this.vehicleFormError = '';
      if (this.isUxPreview) {
          this.vehicleFormError = 'O cadastro de veículos não está disponível no modo demonstrativo.';
          return;
      }
      if (!this.permissionsService.getPermission('VEICULO_CRIAR')) {
          this.vehicleFormError = 'Seu perfil não tem permissão para cadastrar veículos.';
          return;
      }
      const clienteId = Number(this.savedClienteId);
      if (!Number.isSafeInteger(clienteId) || clienteId <= 0) {
          this.vehicleFormError = 'Salve um cliente válido antes de adicionar veículos.';
          return;
      }
      const placa = (this.veiculoModel.placa || '').trim().toUpperCase();
      if (!placa || !this.veiculoModel.marcaId || !this.veiculoModel.modeloId) {
           this.vehicleFormError = 'Preencha placa, marca e modelo.';
           return;
      }

      if (!this.modelosOptions.some(modelo => modelo.id === this.veiculoModel.modeloId && modelo.marcaId === this.veiculoModel.marcaId)) {
          this.vehicleFormError = 'Selecione um modelo da marca escolhida.';
          return;
      }

      const dto: VeiculoRequest = {
          clienteId,
          placa,
          marcaId: this.veiculoModel.marcaId,
          modeloId: this.veiculoModel.modeloId,
          anoModeloId: this.veiculoModel.anoModeloId,
          combustivelId: this.veiculoModel.combustivelId,
          renavam: this.veiculoModel.renavam,
          chassi: this.veiculoModel.chassi,
          numeroMotor: this.veiculoModel.numeroMotor,
          corExterna: this.veiculoModel.corExterna,
          direcao: this.veiculoModel.direcao,
          cambio: this.veiculoModel.cambio,
          arCondicionado: this.veiculoModel.arCondicionado,
          portas: this.veiculoModel.portas,
          observacoes: this.veiculoModel.observacoes,
          status: StatusVeiculo.ATIVO // Default
          // Adicionar outros campos conforme necessário do form
      };

      this.isSavingVehicle = true;
      this.veiculoService.create(dto).subscribe({
          next: () => {
              this.isSavingVehicle = false;
              this.showVeiculoDialog = false;
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Veículo adicionado!' });
              this.veiculoModel = {}; // Reset
              this.loadVeiculos(this.savedClienteId!);
          },
          error: (err) => {
              this.isSavingVehicle = false;
              this.vehicleFormError = err.status === 409 ? 'Já existe um veículo com esta placa.' : 'Não foi possível salvar o veículo. Confira os dados e tente novamente.';
          }
      });
  }
}
