package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ConfiguracaoEmail;
import com.neritech.saas.comunicacao.domain.enums.Criptografia;
import com.neritech.saas.comunicacao.domain.enums.ProvedorEmail;
import com.neritech.saas.comunicacao.dto.ConfiguracaoEmailRequest;
import com.neritech.saas.comunicacao.dto.ConfiguracaoEmailResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:27-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ConfiguracaoEmailMapperImpl implements ConfiguracaoEmailMapper {

    @Override
    public ConfiguracaoEmail toEntity(ConfiguracaoEmailRequest request) {
        if ( request == null ) {
            return null;
        }

        ConfiguracaoEmail configuracaoEmail = new ConfiguracaoEmail();

        configuracaoEmail.setEmpresaId( request.empresaId() );
        configuracaoEmail.setProvedorServico( request.provedorServico() );
        configuracaoEmail.setServidorSmtp( request.servidorSmtp() );
        configuracaoEmail.setPortaSmtp( request.portaSmtp() );
        configuracaoEmail.setUsuarioSmtp( request.usuarioSmtp() );
        configuracaoEmail.setSenhaSmtp( request.senhaSmtp() );
        configuracaoEmail.setCriptografia( request.criptografia() );
        configuracaoEmail.setRemetenteNome( request.remetenteNome() );
        configuracaoEmail.setRemetenteEmail( request.remetenteEmail() );
        configuracaoEmail.setEmailResposta( request.emailResposta() );
        configuracaoEmail.setEmailCopiaOculta( request.emailCopiaOculta() );
        configuracaoEmail.setApiKey( request.apiKey() );
        configuracaoEmail.setApiSecret( request.apiSecret() );
        configuracaoEmail.setDominioAutorizado( request.dominioAutorizado() );
        configuracaoEmail.setLimiteEnviosDia( request.limiteEnviosDia() );
        configuracaoEmail.setWebhookEntrega( request.webhookEntrega() );
        configuracaoEmail.setWebhookAbertura( request.webhookAbertura() );
        configuracaoEmail.setWebhookClique( request.webhookClique() );
        configuracaoEmail.setAssinaturaPadrao( request.assinaturaPadrao() );
        configuracaoEmail.setTemplateCabecalho( request.templateCabecalho() );
        configuracaoEmail.setTemplateRodape( request.templateRodape() );
        configuracaoEmail.setAtivo( request.ativo() );
        configuracaoEmail.setObservacoes( request.observacoes() );

        return configuracaoEmail;
    }

    @Override
    public ConfiguracaoEmailResponse toResponse(ConfiguracaoEmail entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        ProvedorEmail provedorServico = null;
        String servidorSmtp = null;
        Integer portaSmtp = null;
        String usuarioSmtp = null;
        Criptografia criptografia = null;
        String remetenteNome = null;
        String remetenteEmail = null;
        String emailResposta = null;
        String emailCopiaOculta = null;
        String dominioAutorizado = null;
        Integer limiteEnviosDia = null;
        Integer enviosRealizadosHoje = null;
        String webhookEntrega = null;
        String webhookAbertura = null;
        String webhookClique = null;
        String assinaturaPadrao = null;
        String templateCabecalho = null;
        String templateRodape = null;
        Boolean ativo = null;
        Boolean testado = null;
        LocalDateTime dataUltimoTeste = null;
        String observacoes = null;
        LocalDateTime dataConfiguracao = null;
        Long configuradoPor = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        provedorServico = entity.getProvedorServico();
        servidorSmtp = entity.getServidorSmtp();
        portaSmtp = entity.getPortaSmtp();
        usuarioSmtp = entity.getUsuarioSmtp();
        criptografia = entity.getCriptografia();
        remetenteNome = entity.getRemetenteNome();
        remetenteEmail = entity.getRemetenteEmail();
        emailResposta = entity.getEmailResposta();
        emailCopiaOculta = entity.getEmailCopiaOculta();
        dominioAutorizado = entity.getDominioAutorizado();
        limiteEnviosDia = entity.getLimiteEnviosDia();
        enviosRealizadosHoje = entity.getEnviosRealizadosHoje();
        webhookEntrega = entity.getWebhookEntrega();
        webhookAbertura = entity.getWebhookAbertura();
        webhookClique = entity.getWebhookClique();
        assinaturaPadrao = entity.getAssinaturaPadrao();
        templateCabecalho = entity.getTemplateCabecalho();
        templateRodape = entity.getTemplateRodape();
        ativo = entity.getAtivo();
        testado = entity.getTestado();
        dataUltimoTeste = entity.getDataUltimoTeste();
        observacoes = entity.getObservacoes();
        dataConfiguracao = entity.getDataConfiguracao();
        configuradoPor = entity.getConfiguradoPor();

        ConfiguracaoEmailResponse configuracaoEmailResponse = new ConfiguracaoEmailResponse( id, empresaId, provedorServico, servidorSmtp, portaSmtp, usuarioSmtp, criptografia, remetenteNome, remetenteEmail, emailResposta, emailCopiaOculta, dominioAutorizado, limiteEnviosDia, enviosRealizadosHoje, webhookEntrega, webhookAbertura, webhookClique, assinaturaPadrao, templateCabecalho, templateRodape, ativo, testado, dataUltimoTeste, observacoes, dataConfiguracao, configuradoPor );

        return configuracaoEmailResponse;
    }

    @Override
    public void updateEntity(ConfiguracaoEmailRequest request, ConfiguracaoEmail entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setProvedorServico( request.provedorServico() );
        entity.setServidorSmtp( request.servidorSmtp() );
        entity.setPortaSmtp( request.portaSmtp() );
        entity.setUsuarioSmtp( request.usuarioSmtp() );
        entity.setSenhaSmtp( request.senhaSmtp() );
        entity.setCriptografia( request.criptografia() );
        entity.setRemetenteNome( request.remetenteNome() );
        entity.setRemetenteEmail( request.remetenteEmail() );
        entity.setEmailResposta( request.emailResposta() );
        entity.setEmailCopiaOculta( request.emailCopiaOculta() );
        entity.setApiKey( request.apiKey() );
        entity.setApiSecret( request.apiSecret() );
        entity.setDominioAutorizado( request.dominioAutorizado() );
        entity.setLimiteEnviosDia( request.limiteEnviosDia() );
        entity.setWebhookEntrega( request.webhookEntrega() );
        entity.setWebhookAbertura( request.webhookAbertura() );
        entity.setWebhookClique( request.webhookClique() );
        entity.setAssinaturaPadrao( request.assinaturaPadrao() );
        entity.setTemplateCabecalho( request.templateCabecalho() );
        entity.setTemplateRodape( request.templateRodape() );
        entity.setAtivo( request.ativo() );
        entity.setObservacoes( request.observacoes() );
    }
}
