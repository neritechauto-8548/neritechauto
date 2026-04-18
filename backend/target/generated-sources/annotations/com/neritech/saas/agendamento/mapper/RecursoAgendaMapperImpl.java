package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.RecursoAgenda;
import com.neritech.saas.agendamento.domain.enums.TipoRecurso;
import com.neritech.saas.agendamento.dto.RecursoAgendaRequest;
import com.neritech.saas.agendamento.dto.RecursoAgendaResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:16-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class RecursoAgendaMapperImpl implements RecursoAgendaMapper {

    @Override
    public RecursoAgenda toEntity(RecursoAgendaRequest request) {
        if ( request == null ) {
            return null;
        }

        RecursoAgenda recursoAgenda = new RecursoAgenda();

        recursoAgenda.setEmpresaId( request.empresaId() );
        recursoAgenda.setCapacidadeSimultanea( request.capacidadeSimultanea() );
        recursoAgenda.setCustoHora( request.custoHora() );
        recursoAgenda.setDisponivel( request.disponivel() );
        recursoAgenda.setRequerAgendamento( request.requerAgendamento() );
        recursoAgenda.setTempoCleanupMinutos( request.tempoCleanupMinutos() );
        recursoAgenda.setTempoSetupMinutos( request.tempoSetupMinutos() );
        recursoAgenda.setNomeRecurso( request.nomeRecurso() );
        recursoAgenda.setTipoRecurso( request.tipoRecurso() );
        recursoAgenda.setDescricao( request.descricao() );
        recursoAgenda.setLocalizacao( request.localizacao() );
        recursoAgenda.setObservacoes( request.observacoes() );
        recursoAgenda.setResponsavelId( request.responsavelId() );

        return recursoAgenda;
    }

    @Override
    public RecursoAgendaResponse toResponse(RecursoAgenda entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nomeRecurso = null;
        TipoRecurso tipoRecurso = null;
        String descricao = null;
        Integer capacidadeSimultanea = null;
        String localizacao = null;
        Boolean disponivel = null;
        Boolean requerAgendamento = null;
        Integer tempoSetupMinutos = null;
        Integer tempoCleanupMinutos = null;
        BigDecimal custoHora = null;
        String observacoes = null;
        Long responsavelId = null;
        LocalDateTime dataCadastro = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nomeRecurso = entity.getNomeRecurso();
        tipoRecurso = entity.getTipoRecurso();
        descricao = entity.getDescricao();
        capacidadeSimultanea = entity.getCapacidadeSimultanea();
        localizacao = entity.getLocalizacao();
        disponivel = entity.getDisponivel();
        requerAgendamento = entity.getRequerAgendamento();
        tempoSetupMinutos = entity.getTempoSetupMinutos();
        tempoCleanupMinutos = entity.getTempoCleanupMinutos();
        custoHora = entity.getCustoHora();
        observacoes = entity.getObservacoes();
        responsavelId = entity.getResponsavelId();
        dataCadastro = entity.getDataCadastro();

        RecursoAgendaResponse recursoAgendaResponse = new RecursoAgendaResponse( id, empresaId, nomeRecurso, tipoRecurso, descricao, capacidadeSimultanea, localizacao, disponivel, requerAgendamento, tempoSetupMinutos, tempoCleanupMinutos, custoHora, observacoes, responsavelId, dataCadastro );

        return recursoAgendaResponse;
    }
}
