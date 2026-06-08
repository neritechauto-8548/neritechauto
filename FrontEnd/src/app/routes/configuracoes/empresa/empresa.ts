import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputMaskModule } from 'primeng/inputmask';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { DatePickerModule } from 'primeng/datepicker';
import { CheckboxModule } from 'primeng/checkbox';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { TabsModule } from 'primeng/tabs';
import { PanelModule } from 'primeng/panel';
import { DividerModule } from 'primeng/divider';
import { MessageModule } from 'primeng/message';
import { PasswordModule } from 'primeng/password';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { RadioButtonModule } from 'primeng/radiobutton';
import { TextareaModule } from 'primeng/textarea';
import { TooltipModule } from 'primeng/tooltip';
import { AccordionModule } from 'primeng/accordion';
import { FieldsetModule } from 'primeng/fieldset';
import { EmpresaService } from './services/empresa.service';
import {
  Empresa, EnderecoEmpresa, ConfiguracaoEmpresa,
  ConfiguracaoFiscal, ConfiguracaoOficina,
  ConfiguracaoEmail, ConfiguracaoWhatsapp, ConfiguracaoSms
} from './models/empresa.models';
import { LocalStorageService } from '@shared/services/storage.service';
import { isValidCpf, isValidCnpj } from '@shared/utils/validators';
import { UtilService } from '@shared/services/util.service';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { catchError, finalize, forkJoin, of } from 'rxjs';

@Component({
  selector: 'empresa-config',
  standalone: true,
  templateUrl: './empresa.html',
  styleUrl: './empresa.scss',
  imports: [
    FormsModule,
    ToastModule,
    InputMaskModule,
    InputTextModule,
    SelectModule,
    DatePickerModule,
    CheckboxModule,
    InputNumberModule,
    ButtonModule,
    ToggleSwitchModule,
    TabsModule,
    PanelModule,
    DividerModule,
    MessageModule,
    PasswordModule,
    InputGroupModule,
    InputGroupAddonModule,
    RadioButtonModule,
    TextareaModule,
    TooltipModule,
    AccordionModule,
    FieldsetModule
  ],
  providers: [MessageService]
})
export class EmpresaConfig implements OnInit {
  private service             = inject(EmpresaService);
  private toast               = inject(MessageService);
  private storage             = inject(LocalStorageService);
  private utilService         = inject(UtilService);
  private confirmationService = inject(ConfirmationService);

  loading   = false;
  empresaId = this.getTenantId();
  activeTab = 'dados';

  // ── Models ──────────────────────────────────────────────────────
  empresa: Empresa = { nomeFantasia: '', razaoSocial: '', cnpj: '' };
  tipoPessoa: 'Física' | 'Jurídica' = 'Jurídica';

  endereco: EnderecoEmpresa = {
    empresaId: this.empresaId, cep: '', logradouro: '', numero: '',
    bairro: '', cidade: '', estado: '', tipoEndereco: 'MATRIZ', principal: true, ativo: true
  };

  configEmpresa: ConfiguracaoEmpresa = {
    empresaId: this.empresaId,
    regimeTributario: 'SIMPLES_NACIONAL',
    situacaoCadastral: 'ATIVA',
    porteEmpresa: 'ME',
    tipoEstabelecimento: 'MATRIZ'
  };

  configFiscal: ConfiguracaoFiscal = {
    empresaId: this.empresaId, regimeTributario: 'SIMPLES_NACIONAL', ambienteNfe: 'HOMOLOGACAO'
  };

  configOficina: ConfiguracaoOficina = {
    empresaId: this.empresaId, funcionaDomingo: false,
    possuiIntervalo: false, inicioIntervalo: '', fimIntervalo: '',
    permiteAgendamentoOnline: true, enviaLembreteAgendamento: true,
    margemLucroPadrao: 30, diasGarantiaPadrao: 90,
    tempoAgendamentoPadrao: 60, antecedenciaMinimaAgendamento: 24,
    tempoLembreteHoras: 24, timezone: 'America/Sao_Paulo',
    moeda: 'BRL', formatoData: 'dd/MM/yyyy', formatoHora: 'HH:mm',
    mostrarLogoCupom: false,
    mostrarAssinaturaOs: 'FUNCIONARIO',
    termoGarantiaPadrao: '',
    mensagemEnvioOsPadrao: '',
    mostrarExclusivoMensagemOs: false,
    mostrarRelatorios: 'NOME_FANTASIA',
    receberEmailRespostaQuestionario: true,
    descontoIncideComissao: false,
    atualizarPrecoCustoVendaAutomaticamente: false
  };

  configEmail: ConfiguracaoEmail = {
    empresaId: this.empresaId, provedorServico: 'SMTP_CUSTOMIZADO',
    criptografia: 'TLS', portaSmtp: 587, servidorSmtp: 'smtp.gmail.com', ativo: true
  };

  emailTeste    = '';
  testingEmail  = false;
  uploadingLogo = false;

  configWhatsapp: ConfiguracaoWhatsapp = {
    empresaId: this.empresaId, numeroWhatsapp: '',
    tipoIntegracao: 'WHATSAPP_BUSINESS_API', integracaoAtiva: false, chatbotAtivo: false, ambiente: 'HOMOLOGACAO'
  };

  configSms: ConfiguracaoSms = {
    empresaId: this.empresaId, provedorServico: 'TWILIO',
    limiteCaracteres: 160, ativo: false, ambiente: 'HOMOLOGACAO'
  };

  // ── Options ─────────────────────────────────────────────────────
  tiposEndereco        = ['MATRIZ','FILIAL','DEPOSITO','ADMINISTRATIVO'];
  estadosBr            = ['AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO'];
  regimesTributarios   = ['SIMPLES_NACIONAL','LUCRO_PRESUMIDO','LUCRO_REAL'];
  portesEmpresa        = ['MEI','ME','EPP','MEDIO','GRANDE'];
  tiposEstabelecimento = ['MATRIZ','FILIAL'];
  situacoesCadastrais  = ['ATIVA','SUSPENSA','INAPTA','BAIXADA','NULA'];
  anexosSimples        = ['I','II','III','IV','V'];
  ambientesNfe         = ['HOMOLOGACAO','PRODUCAO'];
  provedoresEmail      = ['SMTP_CUSTOMIZADO','AWS_SES','SENDGRID','MAILGUN'];
  criptografias        = ['NONE','SSL','TLS'];
  tiposIntegracao      = ['WHATSAPP_BUSINESS_API','WEBHOOK','CHATBOT','TERCEIROS'];
  provedoresSms        = ['TWILIO','ZENVIA','INFOBIP','SINCH','NEXMO','TOTALVOICE'];

  // Opções Fiscais
  situacoesTributariasIcms = ['102 - Simples Nacional', '101 - Simples Nacional (Com crédito)', '00 - Tributada integralmente'];
  situacoesTributariasPis = ['01 - Operação Tributável', '04 - Operação Tributável (Monofásica)', '07 - Operação Isenta da Contribuição', '49 - Outras Operações de Saída', '99 - Outras Operações'];
  situacoesTributariasCofins = ['01 - Operação Tributável', '04 - Operação Tributável (Monofásica)', '07 - Operação Isenta da Contribuição', '49 - Outras Operações de Saída', '99 - Outras Operações'];
  regimesEspeciaisTributacao = ['Nenhum', 'Microempresa Municipal', 'Estimativa', 'Sociedade de Profissionais', 'Cooperativa', 'Microempresário Individual (MEI)', 'Microempresário e Empresa de Pequeno Porte (ME EPP)'];

  // ── Lifecycle ───────────────────────────────────────────────────
  ngOnInit(): void {
    const stored = this.storage.get('tenantId');
    this.empresaId = stored ? Number(stored) : 1;
    this.configFiscal.empresaId = this.configOficina.empresaId =
      this.configEmail.empresaId = this.configWhatsapp.empresaId =
      this.configSms.empresaId   = this.configEmpresa.empresaId =
      this.endereco.empresaId    = this.empresaId;
    this.loadData();
  }

  // ── Helpers ─────────────────────────────────────────────────────
  private pick<T>(raw: any): T | null {
    if (!raw) return null;
    if (Array.isArray(raw) && raw.length > 0) return raw[0];
    if (raw?.content?.length > 0) return raw.content[0];
    return Array.isArray(raw) ? null : raw as T;
  }

  private getTenantId(): number {
    try {
      let id = this.storage.has('tenantId') ? this.storage.get('tenantId') : null;
      if (id && typeof id === 'object') id = id.id || id.tenantId || id.empresaId || null;
      const n = Number(id);
      return isNaN(n) || n === 0 ? 1 : n;
    } catch { return 1; }
  }

  // ── Data ────────────────────────────────────────────────────────
  loadData(): void {
    this.loading = true;
    forkJoin({
      empresa:   this.service.getEmpresa(this.empresaId)         .pipe(catchError(() => of(null))),
      enderecos: this.service.getEnderecos(this.empresaId)        .pipe(catchError(() => of(null))),
      configEmp: this.service.getConfigEmpresa(this.empresaId)    .pipe(catchError(() => of(null))),
      fiscal:    this.service.getConfigFiscal(this.empresaId)     .pipe(catchError(() => of(null))),
      oficina:   this.service.getConfigOficina(this.empresaId)    .pipe(catchError(() => of(null))),
      email:     this.service.getConfigEmail(this.empresaId)      .pipe(catchError(() => of(null))),
      whatsapp:  this.service.getConfigWhatsapp(this.empresaId)   .pipe(catchError(() => of(null))),
      sms:       this.service.getConfigSms(this.empresaId)        .pipe(catchError(() => of(null))),
    }).pipe(finalize(() => this.loading = false))
      .subscribe(({ empresa, enderecos, configEmp, fiscal, oficina, email, whatsapp, sms }) => {
        if (empresa) {
          const clean = (empresa.cnpj || '').replace(/[^a-zA-Z0-9]/g, '');
          this.tipoPessoa = clean.length > 11 ? 'Jurídica' : 'Física';
          this.empresa = empresa;
        }
        if (this.pick(enderecos))   this.endereco      = { ...this.endereco,      ...this.pick<EnderecoEmpresa>(enderecos) };
        if (this.pick(configEmp))   this.configEmpresa = { ...this.configEmpresa,  ...this.pick<ConfiguracaoEmpresa>(configEmp) };
        if (this.pick(fiscal))      this.configFiscal  = { ...this.configFiscal,   ...this.pick<ConfiguracaoFiscal>(fiscal) };
        if (this.pick(oficina))     this.configOficina = { ...this.configOficina,  ...this.pick<ConfiguracaoOficina>(oficina) };
        if (this.pick<ConfiguracaoEmail>(email))       this.configEmail    = { ...this.configEmail,    ...this.pick<ConfiguracaoEmail>(email) };
        if (this.pick<ConfiguracaoWhatsapp>(whatsapp)) this.configWhatsapp = { ...this.configWhatsapp, ...this.pick<ConfiguracaoWhatsapp>(whatsapp) };
        if (this.pick<ConfiguracaoSms>(sms))           this.configSms      = { ...this.configSms,      ...this.pick<ConfiguracaoSms>(sms) };
        this.endereco.empresaId = this.empresaId;
      });
  }

  // ── Events ──────────────────────────────────────────────────────
  onBlurCnpj(): void {
    const value = (this.empresa.cnpj || '').replace(/[^A-Z0-9]/gi, '');
    if (!value) return;
    const isCpf = value.length <= 11 && /^\d+$/.test(value);
    const isValid = isCpf ? isValidCpf(this.empresa.cnpj) : isValidCnpj(this.empresa.cnpj);
    if (!isValid) {
      this.toast.add({
        severity: 'warn',
        summary: 'Documento Inválido',
        detail: `O ${isCpf ? 'CPF' : 'CNPJ'} informado não é válido.`
      });
    }
  }

  /** Formata dinamicamente o documento como CPF (se numérico com <=11 dígitos) ou CNPJ alfanumérico */
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

    this.empresa.cnpj = fmt;
    input.value = fmt;
  }

  buscarCep(): void {
    const cepLimpo = (this.endereco.cep || '').replace(/\D/g, '');
    if (cepLimpo.length < 8) {
      if (cepLimpo.length > 0) {
        this.toast.add({ severity: 'warn', summary: 'CEP inválido', detail: 'O CEP deve conter 8 dígitos.' });
      }
      return;
    }

    const temDados = !!(this.endereco.logradouro || this.endereco.bairro || this.endereco.cidade || this.endereco.estado);
    if (temDados) {
      this.confirmationService.confirm({
        title: 'Alterar Endereço?',
        message: 'Os campos já estão preenchidos. Deseja sobrescrevê-los com os dados deste CEP?',
        confirmText: 'Sim, sobrescrever',
        cancelText: 'Não, manter atual',
        type: 'warning', icon: 'warning'
      }).subscribe(ok => { if (ok) this.executarBuscaCep(this.endereco.cep); });
    } else {
      this.executarBuscaCep(this.endereco.cep);
    }
  }

  private executarBuscaCep(cep: string): void {
    this.utilService.buscarCep(cep).subscribe({
      next: (data) => {
        if (data.erro) { this.toast.add({ severity: 'error', summary: 'Erro', detail: 'CEP não encontrado.' }); return; }
        this.endereco.logradouro = data.logradouro || '';
        this.endereco.bairro     = data.bairro     || '';
        this.endereco.cidade     = data.localidade  || '';
        this.endereco.estado     = data.uf          || '';
        this.toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Endereço preenchido automaticamente.' });
      },
      error: (err) => console.error('Erro ao buscar CEP', err)
    });
  }

  // ── Validações ──────────────────────────────────────────────────

  /**
   * Valida todos os dados do formulário antes de salvar.
   * Retorna true se válido, false se inválido (já exibe o toast de erro).
   */
  private validarFormulario(): boolean {

    // ───── BLOCO 1: IDENTIFICAÇÃO DA EMPRESA ─────
    if (!this.empresa.nomeFantasia?.trim()) {
      this.toast.add({ severity: 'error', summary: 'Campo obrigatório', detail: 'O Nome Fantasia da oficina é obrigatório.' });
      this.activeTab = 'dados';
      return false;
    }
    if (this.empresa.nomeFantasia.trim().length < 2) {
      this.toast.add({ severity: 'error', summary: 'Nome inválido', detail: 'O Nome Fantasia deve ter pelo menos 2 caracteres.' });
      this.activeTab = 'dados';
      return false;
    }
    if (!this.empresa.razaoSocial?.trim()) {
      this.toast.add({ severity: 'error', summary: 'Campo obrigatório', detail: 'A Razão Social é obrigatória.' });
      this.activeTab = 'dados';
      return false;
    }
    if (this.empresa.razaoSocial.trim().length < 2) {
      this.toast.add({ severity: 'error', summary: 'Nome inválido', detail: 'A Razão Social deve ter pelo menos 2 caracteres.' });
      this.activeTab = 'dados';
      return false;
    }

    // ───── BLOCO 2: CPF / CNPJ ─────
    const docRaw = (this.empresa.cnpj || '').replace(/[^A-Z0-9]/gi, '').toUpperCase();
    if (!docRaw) {
      this.toast.add({ severity: 'error', summary: 'Campo obrigatório', detail: 'Informe o CPF (para autônomo) ou CNPJ da empresa.' });
      this.activeTab = 'dados';
      return false;
    }
    const isCpfDoc = docRaw.length <= 11 && /^\d+$/.test(docRaw);
    if (isCpfDoc) {
      if (docRaw.length !== 11) {
        this.toast.add({ severity: 'error', summary: 'CPF inválido', detail: 'O CPF deve conter 11 dígitos.' });
        this.activeTab = 'dados';
        return false;
      }
      if (!isValidCpf(this.empresa.cnpj)) {
        this.toast.add({ severity: 'error', summary: 'CPF inválido', detail: 'O CPF informado não é válido. Verifique os dígitos.' });
        this.activeTab = 'dados';
        return false;
      }
    } else {
      if (docRaw.length !== 14) {
        this.toast.add({ severity: 'error', summary: 'CNPJ inválido', detail: 'O CNPJ deve conter 14 caracteres.' });
        this.activeTab = 'dados';
        return false;
      }
      if (!isValidCnpj(this.empresa.cnpj)) {
        this.toast.add({ severity: 'error', summary: 'CNPJ inválido', detail: 'O CNPJ informado não é válido. Verifique os dígitos verificadores.' });
        this.activeTab = 'dados';
        return false;
      }
    }

    // ───── BLOCO 3: CONTATO ─────
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (this.empresa.email?.trim() && !emailRegex.test(this.empresa.email.trim())) {
      this.toast.add({ severity: 'error', summary: 'E-mail inválido', detail: 'O e-mail principal da empresa não é válido.' });
      this.activeTab = 'dados';
      return false;
    }
    if (this.empresa.site?.trim()) {
      const site = this.empresa.site.trim();
      if (!/^https?:\/\/.+\..+/.test(site)) {
        this.toast.add({ severity: 'warn', summary: 'Site inválido', detail: 'O site deve começar com http:// ou https:// (ex: https://www.oficina.com.br).' });
        this.activeTab = 'dados';
        return false;
      }
    }

    // ───── BLOCO 4: ENDEREÇO ─────
    const cepLimpo = (this.endereco.cep || '').replace(/\D/g, '');
    if (!cepLimpo || cepLimpo.length === 0) {
      this.toast.add({ severity: 'error', summary: 'Campo obrigatório', detail: 'O CEP do endereço é obrigatório.' });
      this.activeTab = 'dados';
      return false;
    }
    if (cepLimpo.length !== 8) {
      this.toast.add({ severity: 'error', summary: 'CEP inválido', detail: 'O CEP deve conter 8 dígitos.' });
      this.activeTab = 'dados';
      return false;
    }
    if (!this.endereco.logradouro?.trim()) {
      this.toast.add({ severity: 'error', summary: 'Campo obrigatório', detail: 'O Logradouro (rua/avenida) é obrigatório.' });
      this.activeTab = 'dados';
      return false;
    }
    if (!this.endereco.bairro?.trim()) {
      this.toast.add({ severity: 'error', summary: 'Campo obrigatório', detail: 'O Bairro é obrigatório.' });
      this.activeTab = 'dados';
      return false;
    }
    if (!this.endereco.cidade?.trim()) {
      this.toast.add({ severity: 'error', summary: 'Campo obrigatório', detail: 'A Cidade é obrigatória.' });
      this.activeTab = 'dados';
      return false;
    }

    // ───── BLOCO 5: HORÁRIOS ─────
    // Seg–Sex: se um campo preenchido, o outro é obrigatório
    const abrSegSex = this.configOficina.horarioAberturaSegSex;
    const fchSegSex = this.configOficina.horarioFechamentoSegSex;
    if (abrSegSex && !fchSegSex) {
      this.toast.add({ severity: 'error', summary: 'Horário incompleto', detail: 'Informe o horário de fechamento de Segunda a Sexta.' });
      this.activeTab = 'horarios';
      return false;
    }
    if (!abrSegSex && fchSegSex) {
      this.toast.add({ severity: 'error', summary: 'Horário incompleto', detail: 'Informe o horário de abertura de Segunda a Sexta.' });
      this.activeTab = 'horarios';
      return false;
    }
    if (abrSegSex && fchSegSex) {
      if (this.compararHorarios(abrSegSex, fchSegSex) >= 0) {
        this.toast.add({ severity: 'error', summary: 'Horário inválido', detail: 'O horário de fechamento (Seg–Sex) deve ser após a abertura.' });
        this.activeTab = 'horarios';
        return false;
      }
    }

    // Sábado: consistência
    const abrSab = this.configOficina.horarioAberturaSabado;
    const fchSab = this.configOficina.horarioFechamentoSabado;
    if (abrSab && !fchSab) {
      this.toast.add({ severity: 'error', summary: 'Horário incompleto', detail: 'Informe o horário de fechamento do Sábado.' });
      this.activeTab = 'horarios';
      return false;
    }
    if (!abrSab && fchSab) {
      this.toast.add({ severity: 'error', summary: 'Horário incompleto', detail: 'Informe o horário de abertura do Sábado.' });
      this.activeTab = 'horarios';
      return false;
    }
    if (abrSab && fchSab) {
      if (this.compararHorarios(abrSab, fchSab) >= 0) {
        this.toast.add({ severity: 'error', summary: 'Horário inválido', detail: 'O horário de fechamento do Sábado deve ser após a abertura.' });
        this.activeTab = 'horarios';
        return false;
      }
    }

    // Domingo: se funcionaDomingo, horários são obrigatórios
    if (this.configOficina.funcionaDomingo) {
      const abrDom = this.configOficina.horarioAberturaDomingo;
      const fchDom = this.configOficina.horarioFechamentoDomingo;
      if (!abrDom || !fchDom) {
        this.toast.add({ severity: 'error', summary: 'Horário obrigatório', detail: 'Informe os horários de abertura e fechamento do Domingo.' });
        this.activeTab = 'horarios';
        return false;
      }
      if (this.compararHorarios(abrDom, fchDom) >= 0) {
        this.toast.add({ severity: 'error', summary: 'Horário inválido', detail: 'O horário de fechamento do Domingo deve ser após a abertura.' });
        this.activeTab = 'horarios';
        return false;
      }
    }

    // Intervalo: se possuiIntervalo, início e fim são obrigatórios
    if (this.configOficina.possuiIntervalo) {
      if (!this.configOficina.inicioIntervalo || !this.configOficina.fimIntervalo) {
        this.toast.add({ severity: 'error', summary: 'Intervalo incompleto', detail: 'Informe o início e o fim do intervalo (almoço/pausa).' });
        this.activeTab = 'horarios';
        return false;
      }
      if (this.compararHorarios(this.configOficina.inicioIntervalo, this.configOficina.fimIntervalo) >= 0) {
        this.toast.add({ severity: 'error', summary: 'Intervalo inválido', detail: 'O fim do intervalo deve ser após o início.' });
        this.activeTab = 'horarios';
        return false;
      }
    }

    // ───── BLOCO 6: CONFIGURAÇÕES OPERACIONAIS ─────
    // Margem de lucro: 0–100%
    if (this.configOficina.margemLucroPadrao !== undefined &&
        this.configOficina.margemLucroPadrao !== null &&
        (this.configOficina.margemLucroPadrao < 0 || this.configOficina.margemLucroPadrao > 100)) {
      this.toast.add({ severity: 'error', summary: 'Margem inválida', detail: 'A margem de lucro padrão deve estar entre 0% e 100%.' });
      this.activeTab = 'configuracoes';
      return false;
    }

    // Garantia padrão: mínimo 0 dias
    if (this.configOficina.diasGarantiaPadrao !== undefined &&
        this.configOficina.diasGarantiaPadrao !== null &&
        this.configOficina.diasGarantiaPadrao < 0) {
      this.toast.add({ severity: 'error', summary: 'Garantia inválida', detail: 'Os dias de garantia padrão não podem ser negativos.' });
      this.activeTab = 'configuracoes';
      return false;
    }

    // Tempo de agendamento: mínimo 5 minutos
    if (this.configOficina.tempoAgendamentoPadrao !== undefined &&
        this.configOficina.tempoAgendamentoPadrao !== null &&
        this.configOficina.tempoAgendamentoPadrao < 5) {
      this.toast.add({ severity: 'error', summary: 'Duração inválida', detail: 'A duração mínima de agendamento deve ser de pelo menos 5 minutos.' });
      this.activeTab = 'configuracoes';
      return false;
    }

    // Moeda: deve ter exatamente 3 caracteres
    if (this.configOficina.moeda?.trim() && this.configOficina.moeda.trim().length !== 3) {
      this.toast.add({ severity: 'error', summary: 'Moeda inválida', detail: 'O código da moeda deve ter exatamente 3 caracteres (ex: BRL, USD).' });
      this.activeTab = 'configuracoes';
      return false;
    }

    // ───── BLOCO 7: E-MAIL SMTP ─────
    // (Sem validações obrigatórias — campos opcionais)


    // ───── BLOCO 8: DADOS FISCAIS ─────
    // CFOP: deve ser numérico de 4 dígitos quando preenchido
    const cfopRegex = /^\d{4}$/;
    if (this.configFiscal.cfopVendaDentroEstado?.trim() && !cfopRegex.test(this.configFiscal.cfopVendaDentroEstado.trim())) {
      this.toast.add({ severity: 'error', summary: 'CFOP inválido', detail: 'O CFOP Dentro do Estado deve conter exatamente 4 dígitos (ex: 5102).' });
      this.activeTab = 'fiscal';
      return false;
    }
    if (this.configFiscal.cfopVendaForaEstado?.trim() && !cfopRegex.test(this.configFiscal.cfopVendaForaEstado.trim())) {
      this.toast.add({ severity: 'error', summary: 'CFOP inválido', detail: 'O CFOP Fora do Estado deve conter exatamente 4 dígitos (ex: 6102).' });
      this.activeTab = 'fiscal';
      return false;
    }
    if (this.configFiscal.cfopServico?.trim() && !cfopRegex.test(this.configFiscal.cfopServico.trim())) {
      this.toast.add({ severity: 'error', summary: 'CFOP inválido', detail: 'O CFOP de Serviço deve conter exatamente 4 dígitos (ex: 5933).' });
      this.activeTab = 'fiscal';
      return false;
    }

    // Alíquotas: devem estar entre 0% e 100%
    const aliquotas: { valor: number | undefined, nome: string }[] = [
      { valor: this.configFiscal.aliquotaIcms,   nome: 'ICMS' },
      { valor: this.configFiscal.aliquotaPis,    nome: 'PIS' },
      { valor: this.configFiscal.aliquotaCofins, nome: 'COFINS' },
      { valor: this.configFiscal.aliquotaCsll,   nome: 'CSLL' },
      { valor: this.configFiscal.aliquotaIrpj,   nome: 'IRPJ' },
      { valor: this.configFiscal.aliquotaIss,    nome: 'ISS' },
    ];
    for (const aq of aliquotas) {
      if (aq.valor !== undefined && aq.valor !== null && (aq.valor < 0 || aq.valor > 100)) {
        this.toast.add({ severity: 'error', summary: 'Alíquota inválida', detail: `A alíquota de ${aq.nome} deve estar entre 0% e 100%.` });
        this.activeTab = 'fiscal';
        return false;
      }
    }

    // Código do município: deve ter 7 dígitos se preenchido
    const codMunicipio = this.configFiscal.codigoMunicipioPrestacao?.trim();
    if (codMunicipio && !/^\d{7}$/.test(codMunicipio)) {
      this.toast.add({ severity: 'error', summary: 'Código inválido', detail: 'O código do município deve conter exatamente 7 dígitos (ex: 3550308 para São Paulo).' });
      this.activeTab = 'fiscal';
      return false;
    }

    // Alerta: Ambiente de produção — NF-e
    if (this.configFiscal.ambienteNfe === 'PRODUCAO') {
      // Não bloqueia, mas alerta
      this.toast.add({
        severity: 'warn',
        summary: 'Ambiente de Produção',
        detail: 'Atenção: você está configurando a emissão de NF-e em ambiente de PRODUÇÃO. As notas emitidas serão fiscalmente válidas.'
      });
    }

    // Certificado digital: se preenchido, a senha é obrigatória
    if (this.configFiscal.certificadoDigitalA1?.trim() && !this.configFiscal.senhaCertificado?.trim()) {
      this.toast.add({ severity: 'error', summary: 'Senha obrigatória', detail: 'Informe a senha do certificado digital A1.' });
      this.activeTab = 'fiscal';
      return false;
    }

    // Validade do certificado: não pode estar expirado
    if (this.configFiscal.validadeCertificado) {
      const validade = new Date(this.configFiscal.validadeCertificado);
      const hoje = new Date();
      hoje.setHours(0, 0, 0, 0);
      if (validade < hoje) {
        this.toast.add({ severity: 'warn', summary: 'Certificado expirado', detail: 'A validade do certificado digital já passou. Renove o certificado para continuar emitindo notas.' });
        // Não bloqueia o salvamento mas alerta
      }
    }

    return true;
  }

  /**
   * Converte um valor de horário (Date ou string) para o formato HH:mm
   * exigido pelo backend Spring Boot (LocalTime).
   */
  private toHHmm(v: any): string | undefined {
    if (!v) return undefined;
    if (v instanceof Date) {
      const hh = String(v.getHours()).padStart(2, '0');
      const mm = String(v.getMinutes()).padStart(2, '0');
      return `${hh}:${mm}`;
    }
    // Se já for string no formato HH:mm ou HH:mm:ss, retorna apenas HH:mm
    const s = String(v);
    if (/^\d{2}:\d{2}/.test(s)) return s.substring(0, 5);
    return undefined;
  }

  /**
   * Compara dois horários (string HH:mm ou Date).
   * Retorna negativo se a < b, 0 se igual, positivo se a > b.
   */
  private compararHorarios(a: any, b: any): number {
    const toMinutos = (v: any): number => {
      if (!v) return 0;
      if (v instanceof Date) return v.getHours() * 60 + v.getMinutes();
      const parts = String(v).split(':');
      return parseInt(parts[0], 10) * 60 + parseInt(parts[1] || '0', 10);
    };
    return toMinutos(a) - toMinutos(b);
  }

  // ── Salvar ──────────────────────────────────────────────────────
  salvar(): void {
    // Executa todas as validações de frontend antes de chamar a API
    if (!this.validarFormulario()) return;

    this.loading = true;
    const reqs: Record<string, any> = {};

    if (this.empresa.id) reqs['empresa'] = this.service.updateEmpresa(this.empresa.id, this.empresa);
    reqs['endereco']  = this.service.saveEndereco(this.endereco);
    reqs['configEmp'] = this.service.saveConfigEmpresa(this.configEmpresa);
    reqs['fiscal']    = this.service.saveConfigFiscal(this.configFiscal);

    // Converte todos os campos de horário (Date → HH:mm) antes de enviar ao backend.
    // O p-datepicker retorna objetos Date completos; o Spring Boot espera LocalTime em HH:mm.
    const oficinaPayload = {
      ...this.configOficina,
      horarioAberturaSegSex:    this.toHHmm(this.configOficina.horarioAberturaSegSex),
      horarioFechamentoSegSex:  this.toHHmm(this.configOficina.horarioFechamentoSegSex),
      horarioAberturaSabado:    this.toHHmm(this.configOficina.horarioAberturaSabado),
      horarioFechamentoSabado:  this.toHHmm(this.configOficina.horarioFechamentoSabado),
      horarioAberturaDomingo:   this.toHHmm(this.configOficina.horarioAberturaDomingo),
      horarioFechamentoDomingo: this.toHHmm(this.configOficina.horarioFechamentoDomingo),
      inicioIntervalo:          this.toHHmm(this.configOficina.inicioIntervalo),
      fimIntervalo:             this.toHHmm(this.configOficina.fimIntervalo),
    };
    reqs['oficina'] = this.service.saveConfigOficina(oficinaPayload);

    if (this.configEmail?.ativo) {
      if (!this.configEmail.usuarioSmtp) this.configEmail.usuarioSmtp = this.configEmail.remetenteEmail;
      reqs['email'] = this.service.saveConfigEmail(this.configEmail);
    }

    forkJoin(reqs).pipe(finalize(() => this.loading = false)).subscribe({
      next: (res: any) => {
        this.toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Configurações salvas com sucesso!' });
        if (res.endereco)  this.endereco      = res.endereco;
        if (res.configEmp) this.configEmpresa = res.configEmp;
        if (res.fiscal)    this.configFiscal  = res.fiscal;
        if (res.oficina)   this.configOficina = res.oficina;
        if (res.email)     this.configEmail   = res.email;
      },
      error: (err: any) => {
        const tipo   = err?.error?.type;
        const msg    = err?.error?.message || err?.message || 'Erro ao salvar. Verifique os dados e tente novamente.';
        const status = err?.status;

        if (tipo === 'VALIDATION') {
          this.toast.add({ severity: 'error', summary: 'Erro de validação', detail: msg });
        } else if (tipo === 'BUSINESS_RULE') {
          this.toast.add({ severity: 'warn', summary: 'Regra de negócio', detail: msg });
        } else if (status === 409) {
          this.toast.add({ severity: 'warn', summary: 'Conflito de dados', detail: msg });
        } else if (status === 401 || status === 403) {
          this.toast.add({ severity: 'error', summary: 'Sem permissão', detail: 'Você não tem permissão para alterar as configurações da empresa.' });
        } else {
          this.toast.add({ severity: 'error', summary: 'Erro ao salvar', detail: msg });
        }
      }
    });
  }

  testarEmail(): void {
    if (!this.emailTeste?.trim()) {
      this.toast.add({ severity: 'warn', summary: 'Atenção', detail: 'Informe o e-mail de destino para o teste.' });
      return;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.emailTeste.trim())) {
      this.toast.add({ severity: 'error', summary: 'E-mail inválido', detail: 'Informe um endereço de e-mail válido para o teste.' });
      return;
    }
    if (!this.configEmail.servidorSmtp?.trim()) {
      this.toast.add({ severity: 'warn', summary: 'SMTP não configurado', detail: 'Configure o servidor SMTP antes de testar o envio.' });
      return;
    }

    this.testingEmail = true;
    if (!this.configEmail.usuarioSmtp) this.configEmail.usuarioSmtp = this.configEmail.remetenteEmail;

    this.service.testEmail(this.emailTeste, this.configEmail)
      .pipe(finalize(() => this.testingEmail = false))
      .subscribe({
        next: () => this.toast.add({ severity: 'success', summary: 'Sucesso', detail: 'E-mail de teste enviado com sucesso! Verifique a caixa de entrada.' }),
        error: (err) => {
          const msg = err.error?.message || err.message || 'Erro ao enviar e-mail de teste.';
          this.toast.add({ severity: 'error', summary: 'Falha no Teste', detail: msg });
        }
      });
  }

  getLogoUrl(): string {
    if (this.empresa.logoPath) {
      return `/api/v1/empresas/${this.empresaId}/logo?tenantId=${this.empresaId}&t=${new Date().getTime()}`;
    }
    return '';
  }

  onLogoSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    // Validação de formato
    const tiposPermitidos = ['image/png', 'image/jpeg', 'image/jpg', 'image/webp'];
    if (!tiposPermitidos.includes(file.type)) {
      this.toast.add({ severity: 'error', summary: 'Formato inválido', detail: 'Somente imagens PNG, JPG ou WebP são aceitas.' });
      return;
    }

    // Validação de tamanho (2MB)
    if (file.size > 2 * 1024 * 1024) {
      this.toast.add({ severity: 'error', summary: 'Arquivo muito grande', detail: 'A imagem deve ter no máximo 2MB.' });
      return;
    }

    this.uploadingLogo = true;
    this.service.uploadLogo(this.empresaId, file)
      .pipe(finalize(() => this.uploadingLogo = false))
      .subscribe({
        next: (res) => {
          this.empresa.logoPath = res.logoPath;
          this.toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Logomarca atualizada com sucesso!' });
        },
        error: (err) => {
          this.toast.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao fazer upload da logomarca.' });
        }
      });
  }

  removerLogo(): void {
    this.confirmationService.confirm({
      title: 'Remover Logomarca?',
      message: 'Tem certeza que deseja remover a logomarca da sua empresa?',
      confirmText: 'Sim, remover',
      cancelText: 'Não, manter',
      type: 'danger', icon: 'danger'
    }).subscribe(ok => {
      if (ok) {
        this.empresa.logoPath = '';
        this.service.updateEmpresa(this.empresaId, this.empresa).subscribe({
          next: () => {
            this.toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Logomarca removida com sucesso!' });
          },
          error: () => {
            this.toast.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao remover logomarca.' });
          }
        });
      }
    });
  }
}
