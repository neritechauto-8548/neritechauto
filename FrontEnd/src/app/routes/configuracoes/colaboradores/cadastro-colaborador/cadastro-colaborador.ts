import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { TooltipModule } from 'primeng/tooltip';
import { LocalStorageService } from '@shared/services/storage.service';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';
import { TextareaModule } from 'primeng/textarea';
import { DatePickerModule } from 'primeng/datepicker';
import { CheckboxModule } from 'primeng/checkbox';
import { PasswordModule } from 'primeng/password';
import { MultiSelectModule } from 'primeng/multiselect';
import { InputMaskModule } from 'primeng/inputmask';
import { ToastModule } from 'primeng/toast';
import { TableModule } from 'primeng/table';
import { PanelModule } from 'primeng/panel';
import { MessageModule } from 'primeng/message';
import { DividerModule } from 'primeng/divider';
import { InputNumberModule } from 'primeng/inputnumber';
import { MessageService } from 'primeng/api';
import { SettingsService } from '@core';
import { FuncionarioService, FuncionarioRequest } from '../funcionario.service';
import { DepartamentoService } from '../../departamentos/departamento.service';
import { CargoService } from '../../cargos/cargo.service';
import { UsuarioService } from '../../usuario.service';
import { PermissaoService } from '../../permissoes/permissao.service';
import { SetorService } from '../../setores/setor.service';
import { RegraComissaoService, RegraComissao } from '../regra-comissao.service';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { UtilService } from '@shared/services/util.service';
import { isValidCpf } from '@shared/utils/validators';

@Component({
  selector: 'cadastro-colaborador',
  standalone: true,
  templateUrl: './cadastro-colaborador.html',
  styleUrls: ['./cadastro-colaborador.scss'],
  imports: [
    CommonModule, FormsModule, MatIconModule, TooltipModule,
    InputTextModule, SelectModule, ButtonModule, TextareaModule, DatePickerModule, CheckboxModule,
    PasswordModule, MultiSelectModule, InputMaskModule, ToastModule, TableModule, PanelModule,
    InputGroupModule, InputGroupAddonModule, MessageModule, DividerModule, InputNumberModule
  ],
  providers: [MessageService]
})
export class CadastroColaborador implements OnInit {
  private readonly router   = inject(Router);
  private readonly route    = inject(ActivatedRoute);
  private readonly location = inject(Location);
  private readonly storage  = inject(LocalStorageService);
  private readonly settings = inject(SettingsService);
  private readonly messageService = inject(MessageService);
  private readonly funcionarioService  = inject(FuncionarioService);
  private readonly departamentoService = inject(DepartamentoService);
  private readonly cargoService        = inject(CargoService);
  private readonly usuarioService      = inject(UsuarioService);
  private readonly permissaoService    = inject(PermissaoService);
  private readonly setorService        = inject(SetorService);
  private readonly regraComissaoService = inject(RegraComissaoService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly utilService         = inject(UtilService);

  // ---- Acesso ao sistema ----
  temAcesso = false;
  usuarioId?: number;        // linked usuario id (loaded from funcionario.usuarioId)
  funcaoOptions: { label: string; value: number }[] = [];
  usuarioForm = {
    email: '',
    senha: '',
    funcoesIds: [] as number[],
    ativo: true,
    bloqueado: false,
    preferencias: {
      theme: 'light',
      primaryColor: 'indigo',
      primaryColorValue: '{indigo}',
      surfaceColor: 'slate',
      surfaceColorValue: '{slate}',
      menuThemeClass: 'bg-slate-800',
      topbarThemeClass: 'bg-blue-600',
      presetTheme: 'Aura',
      navPos: 'side',
      dir: 'ltr',
      showHeader: true,
      headerPos: 'above',
      showUserPanel: true,
      sidenavOpened: true,
      sidenavCollapsed: true,
      language: 'pt-BR',
    } as Record<string, any>
  };

  activeTab = 'pessoais';
  navTabs = [
    { id: 'pessoais',    label: 'Dados Pessoais',    icon: 'person' },
    { id: 'vinculo',     label: 'Vínculo',            icon: 'badge' },
    { id: 'comissao',    label: 'Regras de Comissão', icon: 'percent' },
    { id: 'documentos',  label: 'Documentos',         icon: 'description' },
    { id: 'bancario',    label: 'Dados Bancários',    icon: 'account_balance' },
    { id: 'usuario',     label: 'Acesso ao Sistema',  icon: 'lock' },
    { id: 'tema',        label: 'Tema',               icon: 'palette' },
  ];

  // ---- Opções de cores para o Tema ----
  primaryColorOptions = [
    { name: 'Esmeralda', value: 'emerald', hex: '#10b981' },
    { name: 'Verde', value: 'green', hex: '#22c55e' },
    { name: 'Limão', value: 'lime', hex: '#84cc16' },
    { name: 'Laranja', value: 'orange', hex: '#f97316' },
    { name: 'Âmbar', value: 'amber', hex: '#f59e0b' },
    { name: 'Amarelo', value: 'yellow', hex: '#eab308' },
    { name: 'Ciano', value: 'cyan', hex: '#06b6d4' },
    { name: 'Teal', value: 'teal', hex: '#14b8a6' },
    { name: 'Celeste', value: 'sky', hex: '#0ea5e9' },
    { name: 'Azul', value: 'blue', hex: '#3b82f6' },
    { name: 'Índigo', value: 'indigo', hex: '#6366f1' },
    { name: 'Violeta', value: 'violet', hex: '#8b5cf6' },
    { name: 'Púrpura', value: 'purple', hex: '#a855f7' },
    { name: 'Fúcsia', value: 'fuchsia', hex: '#d946ef' },
    { name: 'Rosa', value: 'pink', hex: '#ec4899' },
    { name: 'Rose', value: 'rose', hex: '#f43f5e' },
  ];

  surfaceColorOptions = [
    { name: 'Slate', value: 'slate', hex: '#64748b' },
    { name: 'Zinc', value: 'zinc', hex: '#71717a' },
    { name: 'Gray', value: 'gray', hex: '#808080' },
    { name: 'Neutral', value: 'neutral', hex: '#737373' },
    { name: 'Stone', value: 'stone', hex: '#78716c' },
  ];

  menuThemeOptions = [
    { name: 'Branco', value: '!bg-white !text-slate-800 border border-slate-200', hex: '#ffffff' },
    { name: 'Cinza', value: '!bg-[#343a40] !text-slate-100', hex: '#343a40' },
    { name: 'Marinho', value: '!bg-[#1a237e] !text-slate-100', hex: '#1a237e' },
    { name: 'Grafite', value: '!bg-[#263238] !text-slate-100', hex: '#263238' },
    { name: 'Marrom', value: '!bg-[#3e2723] !text-slate-100', hex: '#3e2723' },
    { name: 'Teal Escuro', value: '!bg-[#004d40] !text-slate-100', hex: '#004d40' },
    { name: 'Verde Escuro', value: '!bg-[#1b5e20] !text-slate-100', hex: '#1b5e20' },
    { name: 'Violeta', value: '!bg-[#4a148c] !text-slate-100', hex: '#4a148c' },
    { name: 'Ferrugem', value: '!bg-[#b71c1c] !text-slate-100', hex: '#b71c1c' },
    { name: 'Bordô', value: '!bg-[#880e4f] !text-slate-100', hex: '#880e4f' },
    { name: 'Roxo', value: '!bg-[#311b92] !text-slate-100', hex: '#311b92' },
    { name: 'Turquesa', value: '!bg-[#006064] !text-slate-100', hex: '#006064' },
  ];

  topbarThemeOptions = [
    { name: 'Azul Claro', value: '!bg-[#2196f3] !text-white', hex: '#2196f3' },
    { name: 'Grafite', value: '!bg-[#343a40] !text-white', hex: '#343a40' },
    { name: 'Branco', value: 'bg-white !text-slate-800 border border-slate-200', hex: '#ffffff' },
    { name: 'Azul', value: '!bg-[#1976d2] !text-white', hex: '#1976d2' },
    { name: 'Índigo', value: '!bg-[#3f51b5] !text-white', hex: '#3f51b5' },
    { name: 'Púrpura', value: '!bg-[#673ab7] !text-white', hex: '#673ab7' },
    { name: 'Rosa', value: '!bg-[#e91e63] !text-white', hex: '#e91e63' },
    { name: 'Ciano', value: '!bg-[#00bcd4] !text-white', hex: '#00bcd4' },
    { name: 'Teal', value: '!bg-[#009688] !text-white', hex: '#009688' },
    { name: 'Verde', value: '!bg-[#4caf50] !text-white', hex: '#4caf50' },
    { name: 'Verde Claro', value: '!bg-[#8bc34a] !text-white', hex: '#8bc34a' },
    { name: 'Limão', value: '!bg-[#cddc39] !text-slate-800', hex: '#cddc39' },
    { name: 'Amarelo', value: 'bg-[#ffeb3b] !text-slate-800', hex: '#ffeb3b' },
    { name: 'Âmbar', value: 'bg-[#ffc107] !text-slate-800', hex: '#ffc107' },
    { name: 'Laranja', value: '!bg-[#ff9800] !text-white', hex: '#ff9800' },
    { name: 'Laranja Escuro', value: '!bg-[#ff5722] !text-white', hex: '#ff5722' },
    { name: 'Marrom', value: '!bg-[#795548] !text-white', hex: '#795548' },
    { name: 'Cinza', value: '!bg-[#9e9e9e] !text-white', hex: '#9e9e9e' },
    { name: 'Cinza Azulado', value: '!bg-[#607d8b] !text-white', hex: '#607d8b' },
    { name: 'Padrão / Azul Escuro', value: '!bg-[#25324B] !text-white', hex: '#25324B' },
  ];

  selectPrimaryColor(color: string) {
    this.usuarioForm.preferencias.primaryColor = color;
    this.usuarioForm.preferencias.primaryColorValue = `{${color}}`;
    this.settings.setOptions({ ...this.settings.options, ...this.usuarioForm.preferencias });
  }

  presetOptions = [
    { name: 'Aura', value: 'Aura' },
    { name: 'Lara', value: 'Lara' }
  ];

  selectPreset(preset: string) {
    this.usuarioForm.preferencias.presetTheme = preset;
    this.settings.setOptions({ ...this.settings.options, ...this.usuarioForm.preferencias });
  }

  selectSurfaceColor(color: string) {
    this.usuarioForm.preferencias.surfaceColor = color;
    this.usuarioForm.preferencias.surfaceColorValue = `{${color}}`;
    this.settings.setOptions({ ...this.settings.options, ...this.usuarioForm.preferencias });
  }

  selectMenuTheme(themeClass: string) {
    this.usuarioForm.preferencias.menuThemeClass = themeClass;
    this.settings.setOptions({ ...this.settings.options, ...this.usuarioForm.preferencias });
  }

  selectTopbarTheme(themeClass: string) {
    this.usuarioForm.preferencias.topbarThemeClass = themeClass;
    this.settings.setOptions({ ...this.settings.options, ...this.usuarioForm.preferencias });
  }

  selectColorScheme(scheme: 'light' | 'dark' | 'auto') {
    this.usuarioForm.preferencias.theme = scheme;
    this.settings.setOptions({ ...this.settings.options, ...this.usuarioForm.preferencias });
  }

  setorOptions:        { label: string; value: number }[] = [];
  regrasComissao: RegraComissao[] = [];
  novaRegra: Partial<RegraComissao> = {
    sobreServico: 'NAO',
    sobreProdutos: 'NAO',
    faturamentoGeral: 'NAO',
    percentual: 0,
    ativo: true,
    dataInicio: new Date()
  };

  isEditMode = false;
  funcionarioId?: number;
  loading = false;

  // ---- Select options ----
  sexoOptions = [
    { label: 'Masculino', value: 'M' },
    { label: 'Feminino',  value: 'F' },
    { label: 'Outro',     value: 'O' },
  ];
  estadoCivilOptions = [
    { label: 'Solteiro',       value: 'SOLTEIRO' },
    { label: 'Casado',         value: 'CASADO' },
    { label: 'Divorciado',     value: 'DIVORCIADO' },
    { label: 'Viúvo',          value: 'VIUVO' },
    { label: 'União Estável',  value: 'UNIAO_ESTAVEL' },
  ];
  escolaridadeOptions = [
    { label: 'Fund. Incompleto',  value: 'FUNDAMENTAL_INCOMPLETO' },
    { label: 'Fund. Completo',    value: 'FUNDAMENTAL_COMPLETO' },
    { label: 'Médio Incompleto',  value: 'MEDIO_INCOMPLETO' },
    { label: 'Médio Completo',    value: 'MEDIO_COMPLETO' },
    { label: 'Superior Incompleto', value: 'SUPERIOR_INCOMPLETO' },
    { label: 'Superior Completo', value: 'SUPERIOR_COMPLETO' },
    { label: 'Pós-graduação',     value: 'POS_GRADUACAO' },
    { label: 'Mestrado',          value: 'MESTRADO' },
    { label: 'Doutorado',         value: 'DOUTORADO' },
  ];
  tipoContratoOptions = [
    { label: 'CLT',          value: 'CLT' },
    { label: 'PJ',           value: 'PJ' },
    { label: 'Estagiário',   value: 'ESTAGIARIO' },
    { label: 'Terceirizado', value: 'TERCEIRIZADO' },
    { label: 'Temporário',   value: 'TEMPORARIO' },
    { label: 'Freelancer',   value: 'FREELANCER' },
  ];
  statusOptions = [
    { label: 'Ativo',      value: 'ATIVO' },
    { label: 'Inativo',    value: 'INATIVO' },
    { label: 'Afastado',   value: 'AFASTADO' },
    { label: 'Demitido',   value: 'DEMITIDO' },
    { label: 'Aposentado', value: 'APOSENTADO' },
  ];
  tipoContaOptions = [
    { label: 'Corrente', value: 'CORRENTE' },
    { label: 'Poupança', value: 'POUPANCA' },
  ];
  cnhCategoriaOptions = ['A', 'B', 'AB', 'C', 'D', 'E', 'ACC'];
  departamentoOptions: { label: string; value: number }[] = [];
  cargoOptions:        { label: string; value: number }[] = [];

  // ---- Form model (UI state supports Date objects for p-datepicker) ----
  form: any = {
    nomeCompleto: '',
    cpf: '',
    rg: '',
    matricula: '',
    sexo: undefined,
    estadoCivil: undefined,
    nacionalidade: 'Brasileira',
    naturalidade: '',
    nomeMae: '',
    nomePai: '',
    escolaridade: undefined,
    profissao: '',
    cargoId: undefined,
    departamentoId: undefined,
    status: 'ATIVO',
    tipoContrato: undefined,
    salarioBase: undefined,
    comissaoPercentual: 0,
    valeTransporte: 0,
    valeAlimentacao: 0,
    planoSaude: false,
    planoOdontologico: false,
    motivoInativacao: '',
    enderecoCompleto: '',
    cep: '',
    logradouro: '',
    numero: '',
    complemento: '',
    bairro: '',
    cidade: '',
    estado: '',
    pais: 'Brasil',
    telefonePrincipal: '',
    telefoneEmergencia: '',
    contatoEmergenciaNome: '',
    contatoEmergenciaParentesco: '',
    emailPessoal: '',
    pisPasep: '',
    tituloEleitor: '',
    carteiraTrabalho: '',
    certificadoReservista: '',
    cnhNumero: '',
    cnhCategoria: '',
    bancoCodigo: '',
    bancoAgencia: '',
    bancoConta: '',
    bancoTipoConta: undefined,
    observacoes: '',
  };

  ngOnInit() {
    this.loadOptions();
    const id = this.route.snapshot.paramMap.get('id');
    const byUsuario = this.route.snapshot.queryParamMap.get('byUsuario') === 'true';

    if (id) {
      if (byUsuario) {
        this.isEditMode = true;
        this.usuarioId = Number(id);
        this.loadData(true);
      } else {
        this.isEditMode    = true;
        this.funcionarioId = Number(id);
        this.loadData(false);
      }
    }
  }

  loadOptions() {
    this.departamentoService.list({ page: 0, size: 1000, sort: 'descricao,asc' }).subscribe({
      next: (data: any) => {
        const dep = Array.isArray(data) ? data : (data.content ?? []);
        this.departamentoOptions = dep.map((d: any) => ({ label: d.descricao || d.nome, value: d.id }));
      },
    });
    this.cargoService.list({ size: 1000 }).subscribe({
      next: (data: any) => {
        const c = data.content ?? [];
        this.cargoOptions = c.map((c: any) => ({ label: c.nome, value: c.id }));
      },
    });
    this.permissaoService.listFuncoes().subscribe({
      next: (funcoes: any[]) => {
        this.funcaoOptions = funcoes.map((f: any) => ({ label: f.nome, value: f.id }));
      },
    });
    this.setorService.list({ size: 1000 }).subscribe({
      next: (data: any) => {
        const setores = data.content ?? [];
        this.setorOptions = setores.map((s: any) => ({ label: s.nome || s.descricao, value: s.id }));
      }
    });
  }

  loadData(byUsuario = false) {
    const request = byUsuario && this.usuarioId
      ? this.funcionarioService.getByUsuarioId(this.usuarioId)
      : this.funcionarioService.getById(this.funcionarioId!);

    request.subscribe({
      next: (data) => {
        this.funcionarioId = data.id; // Garante que temos o ID real do funcionario para updates subsequentes
        // carrega usuarioId do funcionario
        if (data.usuarioId) {
          this.usuarioId = data.usuarioId;
          this.temAcesso = true;
          this.usuarioService.get(data.usuarioId).subscribe({
            next: (u: any) => {
              this.usuarioForm.email     = u.email;
              this.usuarioForm.ativo     = u.ativo;
              this.usuarioForm.bloqueado = u.bloqueado;
              this.usuarioForm.funcoesIds = u.funcoesIds || [];
              if (u.preferencias) {
                this.usuarioForm.preferencias = {
                  ...this.usuarioForm.preferencias,
                  ...u.preferencias
                };
              }
            },
          });
        }
        this.form.nomeCompleto              = data.nomeCompleto;
        this.form.cpf                       = data.cpf;
        this.form.rg                        = data.rg;
        this.form.matricula                 = data.matricula;
        this.form.fotoFuncionarioUrl        = data.fotoFuncionarioUrl;
        this.form.dataNascimento            = data.dataNascimento ? new Date(data.dataNascimento + 'T00:00:00') : undefined;
        this.form.sexo                      = data.sexo as any;
        this.form.estadoCivil               = data.estadoCivil as any;
        this.form.nacionalidade             = data.nacionalidade;
        this.form.naturalidade              = data.naturalidade;
        this.form.nomeMae                   = data.nomeMae;
        this.form.nomePai                   = data.nomePai;
        this.form.escolaridade              = data.escolaridade as any;
        this.form.profissao                 = data.profissao;
        this.form.cargoId                   = data.cargoId;
        this.form.departamentoId            = data.departamentoId;
        this.form.dataAdmissao              = data.dataAdmissao ? new Date(data.dataAdmissao + 'T00:00:00') : undefined;
        this.form.dataDemissao              = data.dataDemissao ? new Date(data.dataDemissao + 'T00:00:00') : undefined;
        this.form.tipoContrato              = data.tipoContrato as any;
        this.form.status                    = data.status as any;
        this.form.salarioBase               = data.salarioBase as any;
        this.form.comissaoPercentual        = data.comissaoPercentual as any;
        this.form.valeTransporte            = data.valeTransporte as any;
        this.form.valeAlimentacao           = data.valeAlimentacao as any;
        this.form.planoSaude                = data.planoSaude;
        this.form.planoOdontologico         = data.planoOdontologico;
        this.form.motivoInativacao          = data.motivoInativacao;
        this.form.enderecoCompleto          = data.enderecoCompleto;
        this.form.cep                       = data.cep;
        this.form.logradouro                = data.logradouro;
        this.form.numero                    = data.numero;
        this.form.complemento               = data.complemento;
        this.form.bairro                    = data.bairro;
        this.form.cidade                    = data.cidade;
        this.form.estado                    = data.estado;
        this.form.pais                      = data.pais || 'Brasil';
        this.form.telefonePrincipal         = data.telefonePrincipal;
        this.form.telefoneEmergencia        = data.telefoneEmergencia;
        this.form.contatoEmergenciaNome     = data.contatoEmergenciaNome;
        this.form.contatoEmergenciaParentesco = data.contatoEmergenciaParentesco;
        this.form.emailPessoal              = data.emailPessoal;
        this.form.bancoCodigo               = data.bancoCodigo;
        this.form.bancoAgencia              = data.bancoAgencia;
        this.form.bancoConta                = data.bancoConta;
        this.form.bancoTipoConta            = data.bancoTipoConta as any;
        this.form.pisPasep                  = data.pisPasep;
        this.form.tituloEleitor             = data.tituloEleitor;
        this.form.carteiraTrabalho          = data.carteiraTrabalho;
        this.form.certificadoReservista     = data.certificadoReservista;
        this.form.cnhNumero                 = data.cnhNumero;
        this.form.cnhCategoria              = data.cnhCategoria;
        this.form.cnhValidade               = data.cnhValidade ? new Date(data.cnhValidade + 'T00:00:00') : undefined;
        this.form.observacoes               = data.observacoes;
        this.carregarRegrasComissao();
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar funcionário' }),
    });
  }

  private formatDate(d: any): string | undefined {
    if (!d) return undefined;
    if (d instanceof Date) return d.toISOString().split('T')[0];
    return String(d);
  }

  private get tenantId(): number {
    const v = this.storage.has('tenantId') ? this.storage.get('tenantId') : '7';
    return Number(v) || 7;
  }

  salvar() {
    // Limpa a máscara do CPF antes de validar (p-inputmask pode retornar string com '_' ou parcialmente preenchida)
    const cpfSomenteDigitos = (this.form.cpf || '').replace(/\D/g, '');

    if (!this.form.nomeCompleto?.trim()) {
      this.messageService.add({ severity: 'error', summary: 'Campo obrigatório', detail: 'Nome completo é obrigatório' });
      return;
    }
    if (!this.form.matricula?.trim()) {
      this.messageService.add({ severity: 'error', summary: 'Campo obrigatório', detail: 'Matrícula é obrigatória' });
      return;
    }
    if (!cpfSomenteDigitos || cpfSomenteDigitos.length === 0) {
      this.messageService.add({ severity: 'error', summary: 'Campo obrigatório', detail: 'CPF é obrigatório' });
      return;
    }
    if (cpfSomenteDigitos.length !== 11) {
      this.messageService.add({ severity: 'error', summary: 'CPF inválido', detail: 'O CPF deve conter 11 dígitos' });
      return;
    }
    if (!isValidCpf(this.form.cpf)) {
      this.messageService.add({ severity: 'error', summary: 'CPF inválido', detail: 'O CPF informado não é válido. Verifique os dígitos.' });
      return;
    }
    if (!this.form.dataAdmissao) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'Data de admissão é obrigatória' });
      return;
    }

    // Validação de Endereço
    if (!this.form.cep || !this.form.cep.trim()) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'CEP é obrigatório' });
      return;
    }
    const cleanCep = this.form.cep.replace(/\D/g, '');
    if (cleanCep.length !== 8) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'CEP inválido' });
      return;
    }
    if (!this.form.logradouro || !this.form.logradouro.trim()) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'Logradouro é obrigatório' });
      return;
    }
    if (!this.form.bairro || !this.form.bairro.trim()) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'Bairro é obrigatório' });
      return;
    }
    if (!this.form.cidade || !this.form.cidade.trim()) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'Cidade é obrigatória' });
      return;
    }
    if (!this.form.estado || !this.form.estado.trim()) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'UF é obrigatória' });
      return;
    }

    // Validação de E-mail Pessoal
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (this.form.emailPessoal && this.form.emailPessoal.trim() && !emailRegex.test(this.form.emailPessoal)) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'E-mail pessoal inválido' });
      return;
    }

    // Validação de Idade Mínima
    if (this.form.dataNascimento) {
      const birthDate = new Date(this.form.dataNascimento);
      const today = new Date();
      let age = today.getFullYear() - birthDate.getFullYear();
      const m = today.getMonth() - birthDate.getMonth();
      if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }
      if (age < 16) {
        this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'O colaborador deve ter pelo menos 16 anos' });
        return;
      }
    }

    // Validação de Datas de Vínculo
    if (this.form.dataDemissao && this.form.dataAdmissao) {
      const adm = new Date(this.form.dataAdmissao);
      const dem = new Date(this.form.dataDemissao);
      if (dem < adm) {
        this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'A data de demissão não pode ser anterior à data de admissão' });
        return;
      }
    }

    // Validação de Motivo de Inativação
    if ((this.form.status === 'INATIVO' || this.form.status === 'DEMITIDO' || this.form.status === 'AFASTADO') && (!this.form.motivoInativacao || !this.form.motivoInativacao.trim())) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'O motivo de inativação é obrigatório para o status selecionado' });
      return;
    }

    // Validação Financeira
    if (this.form.salarioBase !== undefined && this.form.salarioBase !== null && this.form.salarioBase < 0) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'O salário base não pode ser negativo' });
      return;
    }
    if (this.form.valeTransporte !== undefined && this.form.valeTransporte !== null && this.form.valeTransporte < 0) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'O vale transporte não pode ser negativo' });
      return;
    }
    if (this.form.valeAlimentacao !== undefined && this.form.valeAlimentacao !== null && this.form.valeAlimentacao < 0) {
      this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'O vale alimentação não pode ser negativo' });
      return;
    }

    // Validação da CNH
    if (this.form.cnhNumero && this.form.cnhNumero.trim()) {
      if (!this.form.cnhCategoria) {
        this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'A categoria da CNH é obrigatória quando o número é informado' });
        return;
      }
      if (this.form.cnhValidade) {
        const val = new Date(this.form.cnhValidade);
        const today = new Date();
        today.setHours(0,0,0,0);
        if (val < today) {
          this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'A CNH informada está vencida' });
          return;
        }
      }
    }

    // Validação de Acesso ao Sistema
    if (this.temAcesso) {
      if (!this.usuarioForm.email || !this.usuarioForm.email.trim()) {
        this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'E-mail de login é obrigatório para habilitar o acesso' });
        return;
      }
      if (!emailRegex.test(this.usuarioForm.email)) {
        this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'E-mail de login inválido' });
        return;
      }
      if (!this.usuarioId && (!this.usuarioForm.senha || this.usuarioForm.senha.length < 6)) {
        this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'A senha de login é obrigatória e deve ter pelo menos 6 caracteres' });
        return;
      }
      if (this.usuarioForm.senha && this.usuarioForm.senha.length < 6) {
        this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'A nova senha deve ter pelo menos 6 caracteres' });
        return;
      }
      if (!this.usuarioForm.funcoesIds || this.usuarioForm.funcoesIds.length === 0) {
        this.messageService.add({ severity: 'error', summary: 'Atenção', detail: 'Selecione pelo menos uma função de acesso' });
        return;
      }
    }

    this.loading = true;
    const payload: FuncionarioRequest = {
      empresaId:                  this.tenantId,
      usuarioId:                  this.usuarioId,
      matricula:                  this.form.matricula!,
      nomeCompleto:               this.form.nomeCompleto!,
      cpf:                        this.form.cpf!,
      rg:                         this.form.rg,
      dataNascimento:             this.formatDate(this.form.dataNascimento),
      sexo:                       this.form.sexo as any,
      estadoCivil:                this.form.estadoCivil as any,
      nacionalidade:              this.form.nacionalidade,
      naturalidade:               this.form.naturalidade,
      nomeMae:                    this.form.nomeMae,
      nomePai:                    this.form.nomePai,
      escolaridade:               this.form.escolaridade as any,
      profissao:                  this.form.profissao,
      cargoId:                    this.form.cargoId,
      departamentoId:             this.form.departamentoId,
      dataAdmissao:               this.formatDate(this.form.dataAdmissao)!,
      dataDemissao:               this.formatDate(this.form.dataDemissao),
      tipoContrato:               this.form.tipoContrato as any,
      salarioBase:                this.form.salarioBase,
      comissaoPercentual:         this.form.comissaoPercentual,
      valeTransporte:             this.form.valeTransporte,
      valeAlimentacao:            this.form.valeAlimentacao,
      planoSaude:                 this.form.planoSaude,
      planoOdontologico:          this.form.planoOdontologico,
      status:                     (this.form.status ?? 'ATIVO') as any,
      motivoInativacao:           this.form.motivoInativacao,
      enderecoCompleto:           this.form.enderecoCompleto,
      cep:                        this.form.cep,
      logradouro:                 this.form.logradouro,
      numero:                     this.form.numero,
      complemento:                this.form.complemento,
      bairro:                     this.form.bairro,
      cidade:                     this.form.cidade,
      estado:                     this.form.estado,
      pais:                       this.form.pais,
      telefonePrincipal:          this.form.telefonePrincipal,
      telefoneEmergencia:         this.form.telefoneEmergencia,
      contatoEmergenciaNome:      this.form.contatoEmergenciaNome,
      contatoEmergenciaParentesco: this.form.contatoEmergenciaParentesco,
      emailPessoal:               this.form.emailPessoal,
      bancoCodigo:                this.form.bancoCodigo,
      bancoAgencia:               this.form.bancoAgencia,
      bancoConta:                 this.form.bancoConta,
      bancoTipoConta:             this.form.bancoTipoConta as any,
      pisPasep:                   this.form.pisPasep,
      tituloEleitor:              this.form.tituloEleitor,
      carteiraTrabalho:           this.form.carteiraTrabalho,
      certificadoReservista:      this.form.certificadoReservista,
      cnhNumero:                  this.form.cnhNumero,
      cnhCategoria:               this.form.cnhCategoria,
      cnhValidade:                this.formatDate(this.form.cnhValidade),
      observacoes:                this.form.observacoes,
    };

    const op = this.isEditMode && this.funcionarioId
      ? this.funcionarioService.update(this.funcionarioId, payload)
      : this.funcionarioService.create(payload);

    op.subscribe({
      next: (funcionario: any) => {
        const funcionarioId = funcionario.id;
        this.funcionarioId = funcionarioId;
        
        // Se tem acesso, cria/atualiza o usuario vinculado
        if (this.temAcesso && this.usuarioForm.email) {
          const usuReq = {
            nomeCompleto:  this.form.nomeCompleto || '',
            email:         this.usuarioForm.email,
            senha:         this.usuarioForm.senha || undefined,
            ativo:         this.usuarioForm.ativo,
            bloqueado:     this.usuarioForm.bloqueado,
            funcoesIds:    this.usuarioForm.funcoesIds,
            preferencias:  this.usuarioForm.preferencias,
          };
          
          const usuOp = this.usuarioId
            ? this.usuarioService.update(this.usuarioId, usuReq)
            : this.usuarioService.create(usuReq);
            
          usuOp.subscribe({
            next: (u: any) => {
              const newUsuarioId = u.id;
              
              // Se o usuario foi criado agora (não tínhamos usuarioId antes), vinculamos ao funcionario
              if (!this.usuarioId) {
                this.usuarioId = newUsuarioId;
                // Atualiza o funcionário com o novo usuarioId
                this.funcionarioService.update(funcionarioId, { ...payload, usuarioId: newUsuarioId }).subscribe({
                  next: () => {
                    this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Funcionário e acesso salvos!' });
                    this.finishSave(funcionarioId);
                  },
                  error: (err) => {
                    console.error('Erro ao vincular usuario ao funcionario', err);
                    this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Funcionário e usuário criados, mas houve um erro ao vinculá-los.' });
                    this.finishSave(funcionarioId);
                  }
                });
              } else {
                this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Funcionário e acesso atualizados!' });
                this.finishSave(funcionarioId);
              }
            },
            error: (err: any) => {
              this.loading = false;
              this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Funcionário salvo, mas erro ao salvar acesso: ' + (err?.error?.message || '') });
              this.finishSave(funcionarioId);
            },
          });
        } else {
          this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Funcionário salvo com sucesso!' });
          this.finishSave(funcionarioId);
        }
      },
      error: (err: any) => {
        this.loading = false;
        const msg = err?.error?.message || err?.error || 'Erro ao salvar funcionário';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: msg });
      },
    });
  }

  private finishSave(id: number) {
    this.loading = false;
    if (!this.isEditMode) {
      this.isEditMode = true;
      this.router.navigate(['/configuracoes/colaboradores/cadastro', id], { replaceUrl: true });
    }
  }

  excluir() {
    if (!this.funcionarioId) return;

    this.confirmationService.confirm({
      title: 'Excluir Colaborador',
      message: `Tem certeza que deseja excluir o colaborador <span class="font-semibold text-slate-900">${this.form.nomeCompleto || ''}</span>? Esta ação não pode ser desfeita.`,
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.loading = true;
        this.funcionarioService.delete(this.funcionarioId!).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Excluído', detail: 'Colaborador excluído com sucesso!' });
            this.router.navigate(['/configuracoes/colaboradores']);
          },
          error: (err: any) => {
            this.loading = false;
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir colaborador: ' + (err?.error?.message || '') });
          },
        });
      }
    });
  }

  cancelar() { this.location.back(); }

  private formatDateTime(d: any): string | undefined {
    if (!d) return undefined;
    if (d instanceof Date) {
      const pad = (n: number) => n.toString().padStart(2, '0');
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    }
    return String(d);
  }

  carregarRegrasComissao() {
    if (!this.funcionarioId) return;
    this.regraComissaoService.listarPorFuncionario(this.funcionarioId).subscribe({
      next: (res: any) => {
        this.regrasComissao = (res.content || []).map((r: any) => {
          return {
            ...r,
            dataInicio: r.dataInicio ? new Date(r.dataInicio) : null,
            dataFinal: r.dataFinal ? new Date(r.dataFinal) : null
          };
        });
      }
    });
  }

  adicionarRegraComissao() {
    if (!this.funcionarioId) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Salve o colaborador antes de adicionar regras' });
      return;
    }
    const regra: RegraComissao = {
      ...this.novaRegra,
      funcionarioId: this.funcionarioId,
      dataInicio: this.novaRegra.dataInicio ? this.formatDateTime(this.novaRegra.dataInicio) : undefined,
      dataFinal: this.novaRegra.dataFinal ? this.formatDateTime(this.novaRegra.dataFinal) : undefined
    } as any;

    this.regraComissaoService.criar(regra).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Regra adicionada' });
        this.novaRegra = { 
          sobreServico: 'NAO', 
          sobreProdutos: 'NAO', 
          faturamentoGeral: 'NAO', 
          percentual: 0, 
          ativo: true,
          dataInicio: new Date()
        };
        this.carregarRegrasComissao();
      },
      error: (err: any) => {
        console.error('Erro ao adicionar regra', err);
      }
    });
  }

  atualizarRegraComissao(regra: any) {
    const payload: RegraComissao = {
      ...regra,
      dataInicio: regra.dataInicio ? this.formatDateTime(regra.dataInicio) : undefined,
      dataFinal: regra.dataFinal ? this.formatDateTime(regra.dataFinal) : undefined
    };
    this.regraComissaoService.atualizar(regra.id, payload).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Regra de comissão atualizada' });
        this.carregarRegrasComissao();
      },
      error: (err: any) => {
        console.error('Erro ao atualizar regra', err);
        this.carregarRegrasComissao();
      }
    });
  }

  excluirRegraComissao(id: number) {
    this.confirmationService.confirm({
      title: 'Excluir Regra',
      message: 'Tem certeza que deseja excluir esta regra de comissão?',
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.regraComissaoService.excluir(id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Regra excluída' });
            this.carregarRegrasComissao();
          }
        });
      }
    });
  }

  // ---- Foto Upload ----
  uploadingFoto = false;

  getFotoUrl(): string {
    if (this.form.fotoFuncionarioUrl && this.funcionarioId) {
      return `/api/v1/rh/funcionarios/${this.funcionarioId}/foto?tenantId=${this.tenantId}&t=${new Date().getTime()}`;
    }
    return '';
  }

  onFotoSelected(event: any): void {
    if (!this.funcionarioId) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Salve o colaborador antes de adicionar uma foto.' });
      return;
    }
    const file = event.target.files[0];
    if (!file) return;

    if (file.size > 2 * 1024 * 1024) {
      this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'A imagem deve ter no máximo 2MB.' });
      return;
    }

    this.uploadingFoto = true;
    this.funcionarioService.uploadFoto(this.funcionarioId, file).subscribe({
      next: (res) => {
        this.uploadingFoto = false;
        this.form.fotoFuncionarioUrl = res.fotoFuncionarioUrl;
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Foto atualizada com sucesso!' });
      },
      error: (err) => {
        this.uploadingFoto = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao fazer upload da foto.' });
      }
    });
  }

  removerFoto(): void {
    this.confirmationService.confirm({
      title: 'Remover Foto?',
      message: 'Tem certeza que deseja remover a foto deste colaborador?',
      confirmText: 'Sim, remover',
      cancelText: 'Não, manter',
      type: 'danger', icon: 'warning'
    }).subscribe(ok => {
      if (ok && this.funcionarioId) {
        this.funcionarioService.removerFoto(this.funcionarioId).subscribe({
          next: () => {
            this.form.fotoFuncionarioUrl = '';
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Foto removida com sucesso!' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao remover foto.' });
          }
        });
      }
    });
  }

  // ---- Endereço / CEP ----
  estadosBr = ['AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO'];

  buscarCep(): void {
    if (!this.form.cep || this.form.cep.length < 8) return;

    const temDados = !!(this.form.logradouro || this.form.bairro || this.form.cidade || this.form.estado);
    if (temDados) {
      this.confirmationService.confirm({
        title: 'Alterar Endereço?',
        message: 'Os campos já estão preenchidos. Deseja sobrescrevê-los com os dados deste CEP?',
        confirmText: 'Sim, sobrescrever',
        cancelText: 'Não, manter atual',
        type: 'warning', icon: 'warning'
      }).subscribe(ok => { if (ok) this.executarBuscaCep(this.form.cep); });
    } else {
      this.executarBuscaCep(this.form.cep);
    }
  }

  private executarBuscaCep(cep: string): void {
    this.utilService.buscarCep(cep).subscribe({
      next: (data) => {
        if (data.erro) { this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'CEP não encontrado.' }); return; }
        this.form.logradouro = data.logradouro || '';
        this.form.bairro     = data.bairro     || '';
        this.form.cidade     = data.localidade  || '';
        this.form.estado     = data.uf          || '';
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Endereço preenchido automaticamente.' });
      },
      error: (err) => console.error('Erro ao buscar CEP', err)
    });
  }
}
