import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import {
  ConfiguracaoEmail,
  ConfiguracaoEmpresa,
  ConfiguracaoFiscal,
  ConfiguracaoOficina,
  ConfiguracaoSms,
  ConfiguracaoWhatsapp,
  Empresa,
  EnderecoEmpresa
} from '../models/empresa.models';

@Injectable({ providedIn: 'root' })
export class EmpresaService {
  private http = inject(HttpClient);

  /** Remove 'id' from the request body — it goes only in the URL path param */
  private body<T extends { id?: any }>(data: T): Omit<T, 'id'> {
    const { id, ...payload } = data as any;
    return payload;
  }

  // --- Empresa ---
  getEmpresa(id: number): Observable<Empresa> {
    return this.http.get<Empresa>(`/api/v1/empresas/${id}`);
  }
  updateEmpresa(id: number, data: Empresa): Observable<Empresa> {
    const payload = {
      nomeFantasia: data.nomeFantasia,
      razaoSocial: data.razaoSocial,
      cnpj: data.cnpj,
      inscricaoEstadual: data.inscricaoEstadual,
      inscricaoMunicipal: data.inscricaoMunicipal,
      email: data.email,
      telefone: data.telefone,
      site: data.site,
      dataAbertura: data.dataAbertura,
      observacoes: data.observacoes,
      ativo: data.ativo
    };
    return this.http.put<Empresa>(`/api/v1/empresas/${id}`, payload);
  }

  // --- Endereço Empresa ---
  getEnderecos(empresaId: number): Observable<EnderecoEmpresa[]> {
    return this.http.get<EnderecoEmpresa[]>(`/api/v1/enderecos-empresa/empresa/${empresaId}`);
  }
  saveEndereco(data: EnderecoEmpresa): Observable<EnderecoEmpresa> {
    const payload = {
      empresaId: data.empresaId,
      tipoEndereco: data.tipoEndereco,
      cep: data.cep,
      logradouro: data.logradouro,
      numero: data.numero,
      complemento: data.complemento,
      bairro: data.bairro,
      cidade: data.cidade,
      estado: data.estado,
      pais: data.pais,
      principal: data.principal,
      ativo: data.ativo
    };
    return data.id
      ? this.http.put<EnderecoEmpresa>(`/api/v1/enderecos-empresa/${data.id}`, payload)
      : this.http.post<EnderecoEmpresa>(`/api/v1/enderecos-empresa`, payload);
  }

  // --- Configuração Empresa (dados cadastrais) ---
  getConfigEmpresa(empresaId: number): Observable<ConfiguracaoEmpresa> {
    return this.http.get<ConfiguracaoEmpresa>(`/api/v1/configuracoes-empresa/empresa/${empresaId}`);
  }
  saveConfigEmpresa(data: ConfiguracaoEmpresa): Observable<ConfiguracaoEmpresa> {
    const payload = {
      empresaId: data.empresaId,
      regimeTributario: data.regimeTributario,
      codigoCnaePrincipal: data.codigoCnaePrincipal,
      codigoCnaeSecundario: data.codigoCnaeSecundario,
      capitalSocial: data.capitalSocial,
      porteEmpresa: data.porteEmpresa,
      tipoEstabelecimento: data.tipoEstabelecimento,
      situacaoCadastral: data.situacaoCadastral,
      dataSituacaoCadastral: data.dataSituacaoCadastral,
      motivoSituacaoCadastral: data.motivoSituacaoCadastral,
      situacaoEspecial: data.situacaoEspecial,
      dataSituacaoEspecial: data.dataSituacaoEspecial
    };
    return data.id
      ? this.http.put<ConfiguracaoEmpresa>(`/api/v1/configuracoes-empresa/${data.id}`, payload)
      : this.http.post<ConfiguracaoEmpresa>(`/api/v1/configuracoes-empresa`, payload);
  }

  // --- Configuração Fiscal ---
  getConfigFiscal(empresaId: number): Observable<ConfiguracaoFiscal> {
    return this.http.get<ConfiguracaoFiscal>(`/api/v1/configuracoes-fiscais/empresa/${empresaId}`);
  }
  saveConfigFiscal(data: ConfiguracaoFiscal): Observable<ConfiguracaoFiscal> {
    const payload = {
      empresaId: data.empresaId,
      regimeTributario: data.regimeTributario,
      anexoSimples: data.anexoSimples,
      aliquotaIss: data.aliquotaIss,
      aliquotaIcms: data.aliquotaIcms,
      aliquotaPis: data.aliquotaPis,
      aliquotaCofins: data.aliquotaCofins,
      aliquotaCsll: data.aliquotaCsll,
      aliquotaIrpj: data.aliquotaIrpj,
      codigoMunicipioPrestacao: data.codigoMunicipioPrestacao,
      cfopVendaDentroEstado: data.cfopVendaDentroEstado,
      cfopVendaForaEstado: data.cfopVendaForaEstado,
      cfopServico: data.cfopServico,
      serieNfe: data.serieNfe,
      numeracaoNfe: data.numeracaoNfe,
      ambienteNfe: data.ambienteNfe,
      certificadoDigitalA1: data.certificadoDigitalA1,
      senhaCertificado: data.senhaCertificado,
      validadeCertificado: data.validadeCertificado
    };
    return data.id
      ? this.http.put<ConfiguracaoFiscal>(`/api/v1/configuracoes-fiscais/${data.id}`, payload)
      : this.http.post<ConfiguracaoFiscal>(`/api/v1/configuracoes-fiscais`, payload);
  }

  // --- Configuração Oficina ---
  getConfigOficina(empresaId: number): Observable<ConfiguracaoOficina> {
    return this.http.get<ConfiguracaoOficina>(`/api/v1/configuracoes-oficina/empresa/${empresaId}`);
  }
  saveConfigOficina(data: ConfiguracaoOficina): Observable<ConfiguracaoOficina> {
    const payload = {
      empresaId: data.empresaId,
      horarioAberturaSegSex: data.horarioAberturaSegSex,
      horarioFechamentoSegSex: data.horarioFechamentoSegSex,
      horarioAberturaSabado: data.horarioAberturaSabado,
      horarioFechamentoSabado: data.horarioFechamentoSabado,
      funcionaDomingo: data.funcionaDomingo,
      horarioAberturaDomingo: data.horarioAberturaDomingo,
      horarioFechamentoDomingo: data.horarioFechamentoDomingo,
      tempoAgendamentoPadrao: data.tempoAgendamentoPadrao,
      antecedenciaMinimaAgendamento: data.antecedenciaMinimaAgendamento,
      permiteAgendamentoOnline: data.permiteAgendamentoOnline,
      enviaLembreteAgendamento: data.enviaLembreteAgendamento,
      tempoLembreteHoras: data.tempoLembreteHoras,
      margemLucroPadrao: data.margemLucroPadrao,
      diasGarantiaPadrao: data.diasGarantiaPadrao,
      moeda: data.moeda,
      formatoData: data.formatoData,
      formatoHora: data.formatoHora,
      timezone: data.timezone
    };
    return data.id
      ? this.http.put<ConfiguracaoOficina>(`/api/v1/configuracoes-oficina/${data.id}`, payload)
      : this.http.post<ConfiguracaoOficina>(`/api/v1/configuracoes-oficina`, payload);
  }

  // --- Configuração Email ---
  getConfigEmail(empresaId: number): Observable<any> {
    return this.http.get<any>(`/api/v1/comunicacao/config/email?empresaId=${empresaId}`);
  }
  saveConfigEmail(data: ConfiguracaoEmail): Observable<ConfiguracaoEmail> {
    const payload = {
      empresaId: data.empresaId,
      provedorServico: data.provedorServico,
      servidorSmtp: data.servidorSmtp,
      portaSmtp: data.portaSmtp,
      usuarioSmtp: data.usuarioSmtp,
      senhaSmtp: data.senhaSmtp,
      criptografia: data.criptografia,
      remetenteNome: data.remetenteNome,
      remetenteEmail: data.remetenteEmail,
      emailResposta: data.emailResposta,
      emailCopiaOculta: data.emailCopiaOculta,
      apiKey: data.apiKey,
      apiSecret: data.apiSecret,
      dominioAutorizado: data.dominioAutorizado,
      limiteEnviosDia: data.limiteEnviosDia,
      webhookEntrega: data.webhookEntrega,
      webhookAbertura: data.webhookAbertura,
      webhookClique: data.webhookClique,
      assinaturaPadrao: data.assinaturaPadrao,
      templateCabecalho: data.templateCabecalho,
      templateRodape: data.templateRodape,
      ativo: data.ativo,
      observacoes: data.observacoes
    };
    return data.id
      ? this.http.put<ConfiguracaoEmail>(`/api/v1/comunicacao/config/email/${data.id}`, payload)
      : this.http.post<ConfiguracaoEmail>(`/api/v1/comunicacao/config/email`, payload);
  }

  // --- Configuração WhatsApp ---
  getConfigWhatsapp(empresaId: number): Observable<any> {
    return this.http.get<any>(`/api/v1/comunicacao/config/whatsapp?empresaId=${empresaId}`);
  }
  saveConfigWhatsapp(data: ConfiguracaoWhatsapp): Observable<ConfiguracaoWhatsapp> {
    const payload = {
      empresaId: data.empresaId,
      tipoIntegracao: data.tipoIntegracao,
      provedorServico: data.provedorServico,
      numeroWhatsapp: data.numeroWhatsapp,
      tokenAcesso: data.tokenAcesso,
      webhookUrl: data.webhookUrl,
      webhookToken: data.webhookToken,
      phoneNumberId: data.phoneNumberId,
      businessAccountId: data.businessAccountId,
      appId: data.appId,
      appSecret: data.appSecret,
      horarioAtendimentoInicio: data.horarioAtendimentoInicio,
      horarioAtendimentoFim: data.horarioAtendimentoFim,
      mensagemForaHorario: data.mensagemForaHorario,
      mensagemBoasVindas: data.mensagemBoasVindas,
      chatbotAtivo: data.chatbotAtivo,
      integracaoAtiva: data.integracaoAtiva,
      ambiente: data.ambiente,
      observacoes: data.observacoes
    };
    return data.id
      ? this.http.put<ConfiguracaoWhatsapp>(`/api/v1/comunicacao/config/whatsapp/${data.id}`, payload)
      : this.http.post<ConfiguracaoWhatsapp>(`/api/v1/comunicacao/config/whatsapp`, payload);
  }

  // --- Configuração SMS ---
  getConfigSms(empresaId: number): Observable<any> {
    return this.http.get<any>(`/api/v1/comunicacao/config/sms?empresaId=${empresaId}`);
  }
  saveConfigSms(data: ConfiguracaoSms): Observable<ConfiguracaoSms> {
    const payload = {
      empresaId: data.empresaId,
      provedorServico: data.provedorServico,
      apiEndpoint: data.apiEndpoint,
      apiKey: data.apiKey,
      apiSecret: data.apiSecret,
      usuarioApi: data.usuarioApi,
      senhaApi: data.senhaApi,
      remetentePadrao: data.remetentePadrao,
      limiteCaracteres: data.limiteCaracteres,
      custoPorSms: data.custoPorSms,
      webhookEntrega: data.webhookEntrega,
      webhookResposta: data.webhookResposta,
      ativo: data.ativo,
      ambiente: data.ambiente,
      observacoes: data.observacoes
    };
    return data.id
      ? this.http.put<ConfiguracaoSms>(`/api/v1/comunicacao/config/sms/${data.id}`, payload)
      : this.http.post<ConfiguracaoSms>(`/api/v1/comunicacao/config/sms`, payload);
  }
}
