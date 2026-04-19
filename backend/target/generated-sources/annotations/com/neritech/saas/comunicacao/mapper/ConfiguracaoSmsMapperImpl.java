package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ConfiguracaoSms;
import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.comunicacao.domain.enums.ProvedorSms;
import com.neritech.saas.comunicacao.dto.ConfiguracaoSmsRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoSmsResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:25-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ConfiguracaoSmsMapperImpl implements ConfiguracaoSmsMapper {

    @Override
    public ConfiguracaoSms toEntity(ConfiguracaoSmsRequest request) {
        if ( request == null ) {
            return null;
        }

        ConfiguracaoSms configuracaoSms = new ConfiguracaoSms();

        configuracaoSms.setAtivo( request.ativa() );
        configuracaoSms.setEmpresaId( request.empresaId() );
        configuracaoSms.setProvedorServico( request.provedorServico() );
        configuracaoSms.setApiEndpoint( request.apiEndpoint() );
        configuracaoSms.setApiKey( request.apiKey() );
        configuracaoSms.setApiSecret( request.apiSecret() );
        configuracaoSms.setUsuarioApi( request.usuarioApi() );
        configuracaoSms.setSenhaApi( request.senhaApi() );
        configuracaoSms.setRemetentePadrao( request.remetentePadrao() );
        configuracaoSms.setLimiteCaracteres( request.limiteCaracteres() );
        configuracaoSms.setCustoPorSms( request.custoPorSms() );
        configuracaoSms.setWebhookEntrega( request.webhookEntrega() );
        configuracaoSms.setWebhookResposta( request.webhookResposta() );
        configuracaoSms.setAmbiente( request.ambiente() );
        configuracaoSms.setObservacoes( request.observacoes() );

        return configuracaoSms;
    }

    @Override
    public ConfiguracaoSmsResponse toResponse(ConfiguracaoSms entity) {
        if ( entity == null ) {
            return null;
        }

        Boolean ativo = null;
        Long id = null;
        Long empresaId = null;
        ProvedorSms provedorServico = null;
        String apiEndpoint = null;
        String apiKey = null;
        String apiSecret = null;
        String usuarioApi = null;
        String remetentePadrao = null;
        Integer limiteCaracteres = null;
        BigDecimal custoPorSms = null;
        Integer creditosDisponiveis = null;
        String webhookEntrega = null;
        String webhookResposta = null;
        Ambiente ambiente = null;
        String observacoes = null;
        LocalDateTime dataConfiguracao = null;
        Long configuradoPor = null;
        LocalDateTime dataUltimaSincronizacao = null;

        ativo = entity.getAtivo();
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        provedorServico = entity.getProvedorServico();
        apiEndpoint = entity.getApiEndpoint();
        apiKey = entity.getApiKey();
        apiSecret = entity.getApiSecret();
        usuarioApi = entity.getUsuarioApi();
        remetentePadrao = entity.getRemetentePadrao();
        limiteCaracteres = entity.getLimiteCaracteres();
        custoPorSms = entity.getCustoPorSms();
        creditosDisponiveis = entity.getCreditosDisponiveis();
        webhookEntrega = entity.getWebhookEntrega();
        webhookResposta = entity.getWebhookResposta();
        ambiente = entity.getAmbiente();
        observacoes = entity.getObservacoes();
        dataConfiguracao = entity.getDataConfiguracao();
        configuradoPor = entity.getConfiguradoPor();
        dataUltimaSincronizacao = entity.getDataUltimaSincronizacao();

        ConfiguracaoSmsResponse configuracaoSmsResponse = new ConfiguracaoSmsResponse( id, empresaId, provedorServico, apiEndpoint, apiKey, apiSecret, usuarioApi, remetentePadrao, limiteCaracteres, custoPorSms, creditosDisponiveis, webhookEntrega, webhookResposta, ativo, ambiente, observacoes, dataConfiguracao, configuradoPor, dataUltimaSincronizacao );

        return configuracaoSmsResponse;
    }

    @Override
    public void updateEntity(ConfiguracaoSmsRequest request, ConfiguracaoSms entity) {
        if ( request == null ) {
            return;
        }

        entity.setAtivo( request.ativa() );
        entity.setEmpresaId( request.empresaId() );
        entity.setProvedorServico( request.provedorServico() );
        entity.setApiEndpoint( request.apiEndpoint() );
        entity.setApiKey( request.apiKey() );
        entity.setApiSecret( request.apiSecret() );
        entity.setUsuarioApi( request.usuarioApi() );
        entity.setSenhaApi( request.senhaApi() );
        entity.setRemetentePadrao( request.remetentePadrao() );
        entity.setLimiteCaracteres( request.limiteCaracteres() );
        entity.setCustoPorSms( request.custoPorSms() );
        entity.setWebhookEntrega( request.webhookEntrega() );
        entity.setWebhookResposta( request.webhookResposta() );
        entity.setAmbiente( request.ambiente() );
        entity.setObservacoes( request.observacoes() );
    }
}
