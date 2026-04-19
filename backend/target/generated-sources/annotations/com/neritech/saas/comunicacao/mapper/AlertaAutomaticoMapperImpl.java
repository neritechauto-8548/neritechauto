package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.AlertaAutomatico;
import com.neritech.saas.comunicacao.domain.enums.FrequenciaVerificacao;
import com.neritech.saas.comunicacao.domain.enums.TipoAlerta;
import com.neritech.saas.comunicacao.dto.AlertaAutomaticoRequest;
import com.neritech.saas.comunicacao.dto.AlertaAutomaticoResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:13:02-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class AlertaAutomaticoMapperImpl implements AlertaAutomaticoMapper {

    @Override
    public AlertaAutomatico toEntity(AlertaAutomaticoRequest request) {
        if ( request == null ) {
            return null;
        }

        AlertaAutomatico alertaAutomatico = new AlertaAutomatico();

        alertaAutomatico.setEmpresaId( request.empresaId() );
        alertaAutomatico.setNomeAlerta( request.nomeAlerta() );
        alertaAutomatico.setDescricao( request.descricao() );
        alertaAutomatico.setTipoAlerta( request.tipoAlerta() );
        alertaAutomatico.setCondicoesDisparo( request.condicoesDisparo() );
        alertaAutomatico.setFrequenciaVerificacao( request.frequenciaVerificacao() );
        alertaAutomatico.setHorarioVerificacao( request.horarioVerificacao() );
        alertaAutomatico.setDiaSemanaVerificacao( request.diaSemanaVerificacao() );
        alertaAutomatico.setDiaMesVerificacao( request.diaMesVerificacao() );
        alertaAutomatico.setUsuariosNotificar( request.usuariosNotificar() );
        alertaAutomatico.setCanaisNotificacao( request.canaisNotificacao() );
        alertaAutomatico.setTemplateNotificacaoId( request.templateNotificacaoId() );
        alertaAutomatico.setMensagemPersonalizada( request.mensagemPersonalizada() );
        alertaAutomatico.setAtivo( request.ativo() );
        alertaAutomatico.setObservacoes( request.observacoes() );

        return alertaAutomatico;
    }

    @Override
    public AlertaAutomaticoResponse toResponse(AlertaAutomatico entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nomeAlerta = null;
        String descricao = null;
        TipoAlerta tipoAlerta = null;
        String condicoesDisparo = null;
        FrequenciaVerificacao frequenciaVerificacao = null;
        LocalTime horarioVerificacao = null;
        Integer diaSemanaVerificacao = null;
        Integer diaMesVerificacao = null;
        String usuariosNotificar = null;
        String canaisNotificacao = null;
        Long templateNotificacaoId = null;
        String mensagemPersonalizada = null;
        Boolean ativo = null;
        LocalDateTime ultimaExecucao = null;
        LocalDateTime proximaExecucao = null;
        Integer totalDisparos = null;
        Integer totalErros = null;
        String logExecucoes = null;
        String observacoes = null;
        Long criadoPor = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nomeAlerta = entity.getNomeAlerta();
        descricao = entity.getDescricao();
        tipoAlerta = entity.getTipoAlerta();
        condicoesDisparo = entity.getCondicoesDisparo();
        frequenciaVerificacao = entity.getFrequenciaVerificacao();
        horarioVerificacao = entity.getHorarioVerificacao();
        diaSemanaVerificacao = entity.getDiaSemanaVerificacao();
        diaMesVerificacao = entity.getDiaMesVerificacao();
        usuariosNotificar = entity.getUsuariosNotificar();
        canaisNotificacao = entity.getCanaisNotificacao();
        templateNotificacaoId = entity.getTemplateNotificacaoId();
        mensagemPersonalizada = entity.getMensagemPersonalizada();
        ativo = entity.getAtivo();
        ultimaExecucao = entity.getUltimaExecucao();
        proximaExecucao = entity.getProximaExecucao();
        totalDisparos = entity.getTotalDisparos();
        totalErros = entity.getTotalErros();
        logExecucoes = entity.getLogExecucoes();
        observacoes = entity.getObservacoes();
        criadoPor = entity.getCriadoPor();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        AlertaAutomaticoResponse alertaAutomaticoResponse = new AlertaAutomaticoResponse( id, empresaId, nomeAlerta, descricao, tipoAlerta, condicoesDisparo, frequenciaVerificacao, horarioVerificacao, diaSemanaVerificacao, diaMesVerificacao, usuariosNotificar, canaisNotificacao, templateNotificacaoId, mensagemPersonalizada, ativo, ultimaExecucao, proximaExecucao, totalDisparos, totalErros, logExecucoes, observacoes, criadoPor, dataCadastro, dataAtualizacao );

        return alertaAutomaticoResponse;
    }

    @Override
    public void updateEntity(AlertaAutomaticoRequest request, AlertaAutomatico entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setNomeAlerta( request.nomeAlerta() );
        entity.setDescricao( request.descricao() );
        entity.setTipoAlerta( request.tipoAlerta() );
        entity.setCondicoesDisparo( request.condicoesDisparo() );
        entity.setFrequenciaVerificacao( request.frequenciaVerificacao() );
        entity.setHorarioVerificacao( request.horarioVerificacao() );
        entity.setDiaSemanaVerificacao( request.diaSemanaVerificacao() );
        entity.setDiaMesVerificacao( request.diaMesVerificacao() );
        entity.setUsuariosNotificar( request.usuariosNotificar() );
        entity.setCanaisNotificacao( request.canaisNotificacao() );
        entity.setTemplateNotificacaoId( request.templateNotificacaoId() );
        entity.setMensagemPersonalizada( request.mensagemPersonalizada() );
        entity.setAtivo( request.ativo() );
        entity.setObservacoes( request.observacoes() );
    }
}
