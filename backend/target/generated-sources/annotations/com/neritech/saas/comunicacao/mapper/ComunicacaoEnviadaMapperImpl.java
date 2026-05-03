package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ComunicacaoEnviada;
import com.neritech.saas.comunicacao.domain.enums.DestinatarioTipo;
import com.neritech.saas.comunicacao.domain.enums.StatusComunicacao;
import com.neritech.saas.comunicacao.domain.enums.TipoComunicacao;
import com.neritech.saas.comunicacao.dto.ComunicacaoEnviadaRequest;
import com.neritech.saas.comunicacao.dto.ComunicacaoEnviadaResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:26:33-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ComunicacaoEnviadaMapperImpl implements ComunicacaoEnviadaMapper {

    @Override
    public ComunicacaoEnviada toEntity(ComunicacaoEnviadaRequest request) {
        if ( request == null ) {
            return null;
        }

        ComunicacaoEnviada comunicacaoEnviada = new ComunicacaoEnviada();

        comunicacaoEnviada.setEmpresaId( request.empresaId() );
        comunicacaoEnviada.setTemplateId( request.templateId() );
        comunicacaoEnviada.setCampanhaId( request.campanhaId() );
        comunicacaoEnviada.setTipoComunicacao( request.tipoComunicacao() );
        comunicacaoEnviada.setDestinatarioTipo( request.destinatarioTipo() );
        comunicacaoEnviada.setDestinatarioId( request.destinatarioId() );
        comunicacaoEnviada.setDestinatarioNome( request.destinatarioNome() );
        comunicacaoEnviada.setDestinatarioContato( request.destinatarioContato() );
        comunicacaoEnviada.setAssunto( request.assunto() );
        comunicacaoEnviada.setConteudo( request.conteudo() );
        comunicacaoEnviada.setAnexos( request.anexos() );
        comunicacaoEnviada.setAgendadaPara( request.agendadaPara() );
        comunicacaoEnviada.setStatus( request.status() );
        comunicacaoEnviada.setAutomatica( request.automatica() );
        comunicacaoEnviada.setOrdemServicoId( request.ordemServicoId() );
        comunicacaoEnviada.setAgendamentoId( request.agendamentoId() );
        comunicacaoEnviada.setFaturaId( request.faturaId() );
        comunicacaoEnviada.setUsuarioEnvio( request.usuarioEnvio() );

        return comunicacaoEnviada;
    }

    @Override
    public ComunicacaoEnviadaResponse toResponse(ComunicacaoEnviada entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        Long templateId = null;
        Long campanhaId = null;
        TipoComunicacao tipoComunicacao = null;
        DestinatarioTipo destinatarioTipo = null;
        Long destinatarioId = null;
        String destinatarioNome = null;
        String destinatarioContato = null;
        String assunto = null;
        String conteudo = null;
        String anexos = null;
        LocalDateTime agendadaPara = null;
        LocalDateTime dataEnvio = null;
        LocalDateTime dataEntrega = null;
        LocalDateTime dataLeitura = null;
        LocalDateTime dataClique = null;
        StatusComunicacao status = null;
        Integer tentativasEnvio = null;
        String erroEnvio = null;
        BigDecimal custoEnvio = null;
        String respostaDestinatario = null;
        LocalDateTime dataResposta = null;
        Integer avaliacaoConteudo = null;
        String motivoAvaliacao = null;
        Boolean automatica = null;
        Long ordemServicoId = null;
        Long agendamentoId = null;
        Long faturaId = null;
        Long usuarioEnvio = null;
        LocalDateTime dataCadastro = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        templateId = entity.getTemplateId();
        campanhaId = entity.getCampanhaId();
        tipoComunicacao = entity.getTipoComunicacao();
        destinatarioTipo = entity.getDestinatarioTipo();
        destinatarioId = entity.getDestinatarioId();
        destinatarioNome = entity.getDestinatarioNome();
        destinatarioContato = entity.getDestinatarioContato();
        assunto = entity.getAssunto();
        conteudo = entity.getConteudo();
        anexos = entity.getAnexos();
        agendadaPara = entity.getAgendadaPara();
        dataEnvio = entity.getDataEnvio();
        dataEntrega = entity.getDataEntrega();
        dataLeitura = entity.getDataLeitura();
        dataClique = entity.getDataClique();
        status = entity.getStatus();
        tentativasEnvio = entity.getTentativasEnvio();
        erroEnvio = entity.getErroEnvio();
        custoEnvio = entity.getCustoEnvio();
        respostaDestinatario = entity.getRespostaDestinatario();
        dataResposta = entity.getDataResposta();
        avaliacaoConteudo = entity.getAvaliacaoConteudo();
        motivoAvaliacao = entity.getMotivoAvaliacao();
        automatica = entity.getAutomatica();
        ordemServicoId = entity.getOrdemServicoId();
        agendamentoId = entity.getAgendamentoId();
        faturaId = entity.getFaturaId();
        usuarioEnvio = entity.getUsuarioEnvio();
        dataCadastro = entity.getDataCadastro();

        ComunicacaoEnviadaResponse comunicacaoEnviadaResponse = new ComunicacaoEnviadaResponse( id, empresaId, templateId, campanhaId, tipoComunicacao, destinatarioTipo, destinatarioId, destinatarioNome, destinatarioContato, assunto, conteudo, anexos, agendadaPara, dataEnvio, dataEntrega, dataLeitura, dataClique, status, tentativasEnvio, erroEnvio, custoEnvio, respostaDestinatario, dataResposta, avaliacaoConteudo, motivoAvaliacao, automatica, ordemServicoId, agendamentoId, faturaId, usuarioEnvio, dataCadastro );

        return comunicacaoEnviadaResponse;
    }

    @Override
    public void updateEntity(ComunicacaoEnviadaRequest request, ComunicacaoEnviada entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setTemplateId( request.templateId() );
        entity.setCampanhaId( request.campanhaId() );
        entity.setTipoComunicacao( request.tipoComunicacao() );
        entity.setDestinatarioTipo( request.destinatarioTipo() );
        entity.setDestinatarioId( request.destinatarioId() );
        entity.setDestinatarioNome( request.destinatarioNome() );
        entity.setDestinatarioContato( request.destinatarioContato() );
        entity.setAssunto( request.assunto() );
        entity.setConteudo( request.conteudo() );
        entity.setAnexos( request.anexos() );
        entity.setAgendadaPara( request.agendadaPara() );
        entity.setStatus( request.status() );
        entity.setAutomatica( request.automatica() );
        entity.setOrdemServicoId( request.ordemServicoId() );
        entity.setAgendamentoId( request.agendamentoId() );
        entity.setFaturaId( request.faturaId() );
        entity.setUsuarioEnvio( request.usuarioEnvio() );
    }
}
