package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.domain.LembreteAgendamento;
import com.neritech.saas.agendamento.domain.enums.StatusEnvioLembrete;
import com.neritech.saas.agendamento.domain.enums.TipoLembrete;
import com.neritech.saas.agendamento.dto.LembreteAgendamentoRequest;
import com.neritech.saas.agendamento.dto.LembreteAgendamentoResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:54-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class LembreteAgendamentoMapperImpl implements LembreteAgendamentoMapper {

    @Override
    public LembreteAgendamento toEntity(LembreteAgendamentoRequest request) {
        if ( request == null ) {
            return null;
        }

        LembreteAgendamento lembreteAgendamento = new LembreteAgendamento();

        lembreteAgendamento.setAgendamento( lembreteAgendamentoRequestToAgendamento( request ) );
        lembreteAgendamento.setAutomatico( request.automatico() );
        lembreteAgendamento.setTentativasEnvio( request.tentativasEnvio() );
        lembreteAgendamento.setTipoLembrete( request.tipoLembrete() );
        lembreteAgendamento.setDestinatario( request.destinatario() );
        lembreteAgendamento.setAssunto( request.assunto() );
        lembreteAgendamento.setMensagem( request.mensagem() );
        lembreteAgendamento.setDataAgendamentoEnvio( request.dataAgendamentoEnvio() );
        lembreteAgendamento.setStatusEnvio( request.statusEnvio() );
        lembreteAgendamento.setErroEnvio( request.erroEnvio() );
        lembreteAgendamento.setRespostaCliente( request.respostaCliente() );
        lembreteAgendamento.setDataResposta( request.dataResposta() );
        lembreteAgendamento.setCustoEnvio( request.custoEnvio() );
        lembreteAgendamento.setTemplateUsado( request.templateUsado() );
        lembreteAgendamento.setVariaveisTemplate( request.variaveisTemplate() );

        return lembreteAgendamento;
    }

    @Override
    public LembreteAgendamentoResponse toResponse(LembreteAgendamento entity) {
        if ( entity == null ) {
            return null;
        }

        Long agendamentoId = null;
        String numeroAgendamento = null;
        Long id = null;
        TipoLembrete tipoLembrete = null;
        String destinatario = null;
        String assunto = null;
        String mensagem = null;
        LocalDateTime dataAgendamentoEnvio = null;
        LocalDateTime dataEnvio = null;
        StatusEnvioLembrete statusEnvio = null;
        Integer tentativasEnvio = null;
        String erroEnvio = null;
        String respostaCliente = null;
        LocalDateTime dataResposta = null;
        BigDecimal custoEnvio = null;
        String templateUsado = null;
        String variaveisTemplate = null;
        Boolean automatico = null;
        LocalDateTime dataCadastro = null;

        agendamentoId = entityAgendamentoId( entity );
        numeroAgendamento = entityAgendamentoNumeroAgendamento( entity );
        id = entity.getId();
        tipoLembrete = entity.getTipoLembrete();
        destinatario = entity.getDestinatario();
        assunto = entity.getAssunto();
        mensagem = entity.getMensagem();
        dataAgendamentoEnvio = entity.getDataAgendamentoEnvio();
        dataEnvio = entity.getDataEnvio();
        statusEnvio = entity.getStatusEnvio();
        tentativasEnvio = entity.getTentativasEnvio();
        erroEnvio = entity.getErroEnvio();
        respostaCliente = entity.getRespostaCliente();
        dataResposta = entity.getDataResposta();
        custoEnvio = entity.getCustoEnvio();
        templateUsado = entity.getTemplateUsado();
        variaveisTemplate = entity.getVariaveisTemplate();
        automatico = entity.getAutomatico();
        dataCadastro = entity.getDataCadastro();

        LembreteAgendamentoResponse lembreteAgendamentoResponse = new LembreteAgendamentoResponse( id, agendamentoId, numeroAgendamento, tipoLembrete, destinatario, assunto, mensagem, dataAgendamentoEnvio, dataEnvio, statusEnvio, tentativasEnvio, erroEnvio, respostaCliente, dataResposta, custoEnvio, templateUsado, variaveisTemplate, automatico, dataCadastro );

        return lembreteAgendamentoResponse;
    }

    protected Agendamento lembreteAgendamentoRequestToAgendamento(LembreteAgendamentoRequest lembreteAgendamentoRequest) {
        if ( lembreteAgendamentoRequest == null ) {
            return null;
        }

        Agendamento agendamento = new Agendamento();

        agendamento.setId( lembreteAgendamentoRequest.agendamentoId() );

        return agendamento;
    }

    private Long entityAgendamentoId(LembreteAgendamento lembreteAgendamento) {
        if ( lembreteAgendamento == null ) {
            return null;
        }
        Agendamento agendamento = lembreteAgendamento.getAgendamento();
        if ( agendamento == null ) {
            return null;
        }
        Long id = agendamento.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityAgendamentoNumeroAgendamento(LembreteAgendamento lembreteAgendamento) {
        if ( lembreteAgendamento == null ) {
            return null;
        }
        Agendamento agendamento = lembreteAgendamento.getAgendamento();
        if ( agendamento == null ) {
            return null;
        }
        String numeroAgendamento = agendamento.getNumeroAgendamento();
        if ( numeroAgendamento == null ) {
            return null;
        }
        return numeroAgendamento;
    }
}
