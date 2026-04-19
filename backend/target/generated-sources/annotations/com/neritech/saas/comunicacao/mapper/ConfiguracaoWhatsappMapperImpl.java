package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ConfiguracaoWhatsapp;
import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.comunicacao.domain.enums.TipoIntegracao;
import com.neritech.saas.comunicacao.dto.ConfiguracaoWhatsappRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoWhatsappResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:24-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ConfiguracaoWhatsappMapperImpl implements ConfiguracaoWhatsappMapper {

    @Override
    public ConfiguracaoWhatsapp toEntity(ConfiguracaoWhatsappRequest request) {
        if ( request == null ) {
            return null;
        }

        ConfiguracaoWhatsapp configuracaoWhatsapp = new ConfiguracaoWhatsapp();

        configuracaoWhatsapp.setEmpresaId( request.empresaId() );
        configuracaoWhatsapp.setTipoIntegracao( request.tipoIntegracao() );
        configuracaoWhatsapp.setProvedorServico( request.provedorServico() );
        configuracaoWhatsapp.setNumeroWhatsapp( request.numeroWhatsapp() );
        configuracaoWhatsapp.setTokenAcesso( request.tokenAcesso() );
        configuracaoWhatsapp.setWebhookUrl( request.webhookUrl() );
        configuracaoWhatsapp.setWebhookToken( request.webhookToken() );
        configuracaoWhatsapp.setPhoneNumberId( request.phoneNumberId() );
        configuracaoWhatsapp.setBusinessAccountId( request.businessAccountId() );
        configuracaoWhatsapp.setAppId( request.appId() );
        configuracaoWhatsapp.setAppSecret( request.appSecret() );
        configuracaoWhatsapp.setMensagensTemplateAprovadas( request.mensagensTemplateAprovadas() );
        configuracaoWhatsapp.setHorarioAtendimentoInicio( request.horarioAtendimentoInicio() );
        configuracaoWhatsapp.setHorarioAtendimentoFim( request.horarioAtendimentoFim() );
        configuracaoWhatsapp.setMensagemForaHorario( request.mensagemForaHorario() );
        configuracaoWhatsapp.setMensagemBoasVindas( request.mensagemBoasVindas() );
        configuracaoWhatsapp.setMensagemMenuPrincipal( request.mensagemMenuPrincipal() );
        configuracaoWhatsapp.setChatbotAtivo( request.chatbotAtivo() );
        configuracaoWhatsapp.setIntegracaoAtiva( request.integracaoAtiva() );
        configuracaoWhatsapp.setAmbiente( request.ambiente() );
        configuracaoWhatsapp.setObservacoes( request.observacoes() );

        return configuracaoWhatsapp;
    }

    @Override
    public ConfiguracaoWhatsappResponse toResponse(ConfiguracaoWhatsapp entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        TipoIntegracao tipoIntegracao = null;
        String provedorServico = null;
        String numeroWhatsapp = null;
        String webhookUrl = null;
        String phoneNumberId = null;
        String businessAccountId = null;
        String appId = null;
        String mensagensTemplateAprovadas = null;
        LocalTime horarioAtendimentoInicio = null;
        LocalTime horarioAtendimentoFim = null;
        String mensagemForaHorario = null;
        String mensagemBoasVindas = null;
        String mensagemMenuPrincipal = null;
        Boolean chatbotAtivo = null;
        Boolean integracaoAtiva = null;
        Ambiente ambiente = null;
        String observacoes = null;
        LocalDateTime dataConfiguracao = null;
        Long configuradoPor = null;
        LocalDateTime dataUltimaVerificacao = null;
        String statusVerificacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        tipoIntegracao = entity.getTipoIntegracao();
        provedorServico = entity.getProvedorServico();
        numeroWhatsapp = entity.getNumeroWhatsapp();
        webhookUrl = entity.getWebhookUrl();
        phoneNumberId = entity.getPhoneNumberId();
        businessAccountId = entity.getBusinessAccountId();
        appId = entity.getAppId();
        mensagensTemplateAprovadas = entity.getMensagensTemplateAprovadas();
        horarioAtendimentoInicio = entity.getHorarioAtendimentoInicio();
        horarioAtendimentoFim = entity.getHorarioAtendimentoFim();
        mensagemForaHorario = entity.getMensagemForaHorario();
        mensagemBoasVindas = entity.getMensagemBoasVindas();
        mensagemMenuPrincipal = entity.getMensagemMenuPrincipal();
        chatbotAtivo = entity.getChatbotAtivo();
        integracaoAtiva = entity.getIntegracaoAtiva();
        ambiente = entity.getAmbiente();
        observacoes = entity.getObservacoes();
        dataConfiguracao = entity.getDataConfiguracao();
        configuradoPor = entity.getConfiguradoPor();
        dataUltimaVerificacao = entity.getDataUltimaVerificacao();
        statusVerificacao = entity.getStatusVerificacao();

        ConfiguracaoWhatsappResponse configuracaoWhatsappResponse = new ConfiguracaoWhatsappResponse( id, empresaId, tipoIntegracao, provedorServico, numeroWhatsapp, webhookUrl, phoneNumberId, businessAccountId, appId, mensagensTemplateAprovadas, horarioAtendimentoInicio, horarioAtendimentoFim, mensagemForaHorario, mensagemBoasVindas, mensagemMenuPrincipal, chatbotAtivo, integracaoAtiva, ambiente, observacoes, dataConfiguracao, configuradoPor, dataUltimaVerificacao, statusVerificacao );

        return configuracaoWhatsappResponse;
    }

    @Override
    public void updateEntity(ConfiguracaoWhatsappRequest request, ConfiguracaoWhatsapp entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setTipoIntegracao( request.tipoIntegracao() );
        entity.setProvedorServico( request.provedorServico() );
        entity.setNumeroWhatsapp( request.numeroWhatsapp() );
        entity.setTokenAcesso( request.tokenAcesso() );
        entity.setWebhookUrl( request.webhookUrl() );
        entity.setWebhookToken( request.webhookToken() );
        entity.setPhoneNumberId( request.phoneNumberId() );
        entity.setBusinessAccountId( request.businessAccountId() );
        entity.setAppId( request.appId() );
        entity.setAppSecret( request.appSecret() );
        entity.setMensagensTemplateAprovadas( request.mensagensTemplateAprovadas() );
        entity.setHorarioAtendimentoInicio( request.horarioAtendimentoInicio() );
        entity.setHorarioAtendimentoFim( request.horarioAtendimentoFim() );
        entity.setMensagemForaHorario( request.mensagemForaHorario() );
        entity.setMensagemBoasVindas( request.mensagemBoasVindas() );
        entity.setMensagemMenuPrincipal( request.mensagemMenuPrincipal() );
        entity.setChatbotAtivo( request.chatbotAtivo() );
        entity.setIntegracaoAtiva( request.integracaoAtiva() );
        entity.setAmbiente( request.ambiente() );
        entity.setObservacoes( request.observacoes() );
    }
}
