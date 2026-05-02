import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { InputMaskModule } from 'primeng/inputmask';
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

interface NavTab { id: string; label: string; icon: string; }

@Component({
  selector: 'empresa-config',
  standalone: true,
  templateUrl: './empresa.html',
  imports: [CommonModule, FormsModule, MatIconModule, ToastModule, InputMaskModule],
})
export class EmpresaConfig implements OnInit {
  private service = inject(EmpresaService);
  private toast   = inject(MessageService);
  private storage = inject(LocalStorageService);
  private utilService = inject(UtilService);
  private confirmationService = inject(ConfirmationService);

  loading   = false;
  empresaId = this.getTenantId();
  activeTab = 'dados';

  tabs: NavTab[] = [
    { id: 'dados',     label: 'Dados da Oficina',      icon: 'storefront' },
    { id: 'endereco',  label: 'Endereço',               icon: 'location_on' },
    { id: 'cadastral', label: 'Dados Cadastrais',       icon: 'business_center' },
    { id: 'horarios',  label: 'Horários',               icon: 'schedule' },
    { id: 'regras',    label: 'Operacional',           icon: 'build' },
    { id: 'fiscal',    label: 'Dados Fiscais',          icon: 'receipt_long' },
    { id: 'email',     label: 'E-mail',                 icon: 'mail' },
    { id: 'whatsapp',  label: 'WhatsApp',               icon: 'whatsapp' },
    { id: 'sms',       label: 'SMS',                    icon: 'sms' },
  ];

  empresa: Empresa = { nomeFantasia: '', razaoSocial: '', cnpj: '' };
  tipoPessoa: 'Física' | 'Jurídica' = 'Jurídica';

  get docMask(): string {
    return this.tipoPessoa === 'Física' ? '999.999.999-99' : '**.***.***/****-99';
  }

  get docPlaceholder(): string {
    return this.tipoPessoa === 'Física' ? '000.000.000-00' : '00.000.000/0000-00';
  }

  onPhoneInput(event: any) {
    let val = event.target.value.replace(/\D/g, '');
    if (val.length > 11) val = val.substring(0, 11);

    if (val.length > 10) {
      val = val.replace(/^(\d{2})(\d{5})(\d{4}).*/, '($1) $2-$3');
    } else if (val.length > 6) {
      val = val.replace(/^(\d{2})(\d{4})(\d{0,4}).*/, '($1) $2-$3');
    } else if (val.length > 2) {
      val = val.replace(/^(\d{2})(\d{0,5}).*/, '($1) $2');
    } else if (val.length > 0) {
      val = val.replace(/^(\d{0,2}).*/, '($1');
    }
    this.empresa.telefone = val;
  }

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
    permiteAgendamentoOnline: true, enviaLembreteAgendamento: true,
    margemLucroPadrao: 30, diasGarantiaPadrao: 90,
    tempoAgendamentoPadrao: 60, antecedenciaMinimaAgendamento: 24,
    tempoLembreteHoras: 24, timezone: 'America/Sao_Paulo',
    moeda: 'BRL', formatoData: 'dd/MM/yyyy', formatoHora: 'HH:mm'
  };

  configEmail: ConfiguracaoEmail = {
    empresaId: this.empresaId, provedorServico: 'SMTP_CUSTOMIZADO',
    criptografia: 'TLS', portaSmtp: 587, servidorSmtp: 'smtp.gmail.com', ativo: true
  };

  configWhatsapp: ConfiguracaoWhatsapp = {
    empresaId: this.empresaId, numeroWhatsapp: '',
    tipoIntegracao: 'WHATSAPP_BUSINESS_API', integracaoAtiva: false, chatbotAtivo: false, ambiente: 'HOMOLOGACAO'
  };

  configSms: ConfiguracaoSms = {
    empresaId: this.empresaId, provedorServico: 'TWILIO',
    limiteCaracteres: 160, ativo: false, ambiente: 'HOMOLOGACAO'
  };

  // ---- Select options ----
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
  timezones            = ['America/Sao_Paulo','America/Manaus','America/Fortaleza','America/Belem','America/Noronha','America/Boa_Vista'];

  ngOnInit() {
    const stored = this.storage.get('tenantId');
    this.empresaId = stored ? Number(stored) : 1;
    this.configFiscal.empresaId = this.configOficina.empresaId =
      this.configEmail.empresaId = this.configWhatsapp.empresaId =
      this.configSms.empresaId   = this.configEmpresa.empresaId =
      this.endereco.empresaId    = this.empresaId;
    this.loadData();
  }

  private pick<T>(raw: any): T | null {
    if (!raw) return null;
    if (Array.isArray(raw) && raw.length > 0) return raw[0];
    if (raw?.content?.length > 0) return raw.content[0];
    return Array.isArray(raw) ? null : raw as T;
  }

  private getTenantId(): number {
    try {
      let tenantId = this.storage.has('tenantId') ? this.storage.get('tenantId') : null;
      if (tenantId && typeof tenantId === 'object') {
        tenantId = tenantId.id || tenantId.tenantId || tenantId.empresaId || null;
      }
      const num = Number(tenantId);
      return isNaN(num) || num === 0 ? 1 : num;
    } catch { return 1; }
  }

  loadData() {
    this.loading = true;
    forkJoin({
      empresa:      this.service.getEmpresa(this.empresaId)            .pipe(catchError(() => of(null))),
      enderecos:    this.service.getEnderecos(this.empresaId)           .pipe(catchError(() => of(null))),
      configEmp:    this.service.getConfigEmpresa(this.empresaId)       .pipe(catchError(() => of(null))),
      fiscal:       this.service.getConfigFiscal(this.empresaId)        .pipe(catchError(() => of(null))),
      oficina:      this.service.getConfigOficina(this.empresaId)       .pipe(catchError(() => of(null))),
      email:        this.service.getConfigEmail(this.empresaId)         .pipe(catchError(() => of(null))),
      whatsapp:     this.service.getConfigWhatsapp(this.empresaId)      .pipe(catchError(() => of(null))),
      sms:          this.service.getConfigSms(this.empresaId)           .pipe(catchError(() => of(null))),
    }).pipe(finalize(() => this.loading = false))
      .subscribe(({ empresa, enderecos, configEmp, fiscal, oficina, email, whatsapp, sms }) => {
        if (empresa) {
          const clean = (empresa.cnpj || '').replace(/[^a-zA-Z0-9]/g, '');
          this.tipoPessoa = clean.length > 11 ? 'Jurídica' : 'Física';
          this.empresa = empresa;
        }

        if (this.pick(enderecos))        this.endereco      = { ...this.endereco, ...this.pick<EnderecoEmpresa>(enderecos) };
        if (this.pick(configEmp))        this.configEmpresa = { ...this.configEmpresa, ...this.pick<ConfiguracaoEmpresa>(configEmp) };
        if (this.pick(fiscal))           this.configFiscal  = { ...this.configFiscal,  ...this.pick<ConfiguracaoFiscal>(fiscal) };
        if (this.pick(oficina))          this.configOficina = { ...this.configOficina, ...this.pick<ConfiguracaoOficina>(oficina) };
        if (this.pick<ConfiguracaoEmail>(email))     this.configEmail    = { ...this.configEmail,    ...this.pick<ConfiguracaoEmail>(email) };
        if (this.pick<ConfiguracaoWhatsapp>(whatsapp)) this.configWhatsapp = { ...this.configWhatsapp, ...this.pick<ConfiguracaoWhatsapp>(whatsapp) };
        if (this.pick<ConfiguracaoSms>(sms))         this.configSms      = { ...this.configSms,      ...this.pick<ConfiguracaoSms>(sms) };

        // Ensure empresaId always set
        this.endereco.empresaId      = this.empresaId;
      });
  }

  onBlurCnpj() {
    const value = this.empresa.cnpj;
    if (!value) return;

    const isValid = this.tipoPessoa === 'Física' ? isValidCpf(value) : isValidCnpj(value);

    if (!isValid) {
      this.toast.add({
        severity: 'warn',
        summary: 'Documento Inválido',
        detail: `O ${this.tipoPessoa === 'Física' ? 'CPF' : 'CNPJ'} informado não é válido.`
      });
    }
  }

  buscarCep() {
    if (!this.endereco.cep || this.endereco.cep.length < 8) return;

    const temDados = !!(this.endereco.logradouro || this.endereco.bairro || this.endereco.cidade || this.endereco.estado);

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
          this.executarBuscaCep(this.endereco.cep);
        }
      });
    } else {
      this.executarBuscaCep(this.endereco.cep);
    }
  }

  private executarBuscaCep(cep: string) {
    this.utilService.buscarCep(cep).subscribe({
      next: (data) => {
        if (data.erro) {
          this.toast.add({ severity: 'error', summary: 'Erro', detail: 'CEP não encontrado.' });
          return;
        }
        this.endereco.logradouro = data.logradouro || '';
        this.endereco.bairro = data.bairro || '';
        this.endereco.cidade = data.localidade || '';
        this.endereco.estado = data.uf || '';

        this.toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Endereço preenchido automaticamente.' });
      },
      error: (err) => {
        console.error('Erro ao buscar CEP', err);
      }
    });
  }

  salvar() {
    this.loading = true;
    const requests: Record<string, any> = {};

    if (this.empresa.id) requests['empresa'] = this.service.updateEmpresa(this.empresa.id, this.empresa);
    requests['endereco']  = this.service.saveEndereco(this.endereco);
    requests['configEmp'] = this.service.saveConfigEmpresa(this.configEmpresa);
    requests['fiscal']    = this.service.saveConfigFiscal(this.configFiscal);
    requests['oficina']   = this.service.saveConfigOficina(this.configOficina);

    // Only send communication payloads if their respective functionality is marked as active
    if (this.configEmail?.ativo) {
      requests['email'] = this.service.saveConfigEmail(this.configEmail);
    }
    if (this.configWhatsapp?.integracaoAtiva) {
      requests['whatsapp']  = this.service.saveConfigWhatsapp(this.configWhatsapp);
    }
    if (this.configSms?.ativo) {
      requests['sms']       = this.service.saveConfigSms(this.configSms);
    }

    forkJoin(requests).pipe(finalize(() => this.loading = false)).subscribe({
      next: (res: any) => {
        this.toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Configurações salvas com sucesso!' });
        if (res.endereco)  this.endereco      = res.endereco;
        if (res.configEmp) this.configEmpresa = res.configEmp;
        if (res.fiscal)    this.configFiscal  = res.fiscal;
        if (res.oficina)   this.configOficina = res.oficina;
        if (res.email)     this.configEmail   = res.email;
        if (res.whatsapp)  this.configWhatsapp = res.whatsapp;
        if (res.sms)       this.configSms     = res.sms;
      },
      error: () => this.toast.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao salvar. Verifique os dados e tente novamente.' })
    });
  }
}
