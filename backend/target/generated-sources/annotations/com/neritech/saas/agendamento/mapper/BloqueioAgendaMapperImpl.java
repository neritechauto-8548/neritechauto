package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.BloqueioAgenda;
import com.neritech.saas.agendamento.domain.enums.TipoBloqueio;
import com.neritech.saas.agendamento.dto.BloqueioAgendaRequest;
import com.neritech.saas.agendamento.dto.BloqueioAgendaResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:26:48-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class BloqueioAgendaMapperImpl implements BloqueioAgendaMapper {

    @Override
    public BloqueioAgenda toEntity(BloqueioAgendaRequest request) {
        if ( request == null ) {
            return null;
        }

        BloqueioAgenda bloqueioAgenda = new BloqueioAgenda();

        bloqueioAgenda.setAfetaTodosFuncionarios( request.afetaTodosFuncionarios() );
        bloqueioAgenda.setAtivo( request.ativo() );
        bloqueioAgenda.setRecorrente( request.recorrente() );
        bloqueioAgenda.setEmpresaId( request.empresaId() );
        bloqueioAgenda.setFuncionarioId( request.funcionarioId() );
        bloqueioAgenda.setTipoBloqueio( request.tipoBloqueio() );
        bloqueioAgenda.setDataInicio( request.dataInicio() );
        bloqueioAgenda.setDataFim( request.dataFim() );
        bloqueioAgenda.setHoraInicio( request.horaInicio() );
        bloqueioAgenda.setHoraFim( request.horaFim() );
        bloqueioAgenda.setDiasSemanaRecorrencia( request.diasSemanaRecorrencia() );
        bloqueioAgenda.setMotivo( request.motivo() );
        bloqueioAgenda.setFuncionariosAfetados( request.funcionariosAfetados() );
        bloqueioAgenda.setObservacoes( request.observacoes() );

        return bloqueioAgenda;
    }

    @Override
    public BloqueioAgendaResponse toResponse(BloqueioAgenda entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        Long funcionarioId = null;
        TipoBloqueio tipoBloqueio = null;
        LocalDate dataInicio = null;
        LocalDate dataFim = null;
        LocalTime horaInicio = null;
        LocalTime horaFim = null;
        Boolean recorrente = null;
        String diasSemanaRecorrencia = null;
        String motivo = null;
        Boolean afetaTodosFuncionarios = null;
        String funcionariosAfetados = null;
        String observacoes = null;
        Boolean ativo = null;
        LocalDateTime dataCadastro = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        funcionarioId = entity.getFuncionarioId();
        tipoBloqueio = entity.getTipoBloqueio();
        dataInicio = entity.getDataInicio();
        dataFim = entity.getDataFim();
        horaInicio = entity.getHoraInicio();
        horaFim = entity.getHoraFim();
        recorrente = entity.getRecorrente();
        diasSemanaRecorrencia = entity.getDiasSemanaRecorrencia();
        motivo = entity.getMotivo();
        afetaTodosFuncionarios = entity.getAfetaTodosFuncionarios();
        funcionariosAfetados = entity.getFuncionariosAfetados();
        observacoes = entity.getObservacoes();
        ativo = entity.getAtivo();
        dataCadastro = entity.getDataCadastro();

        BloqueioAgendaResponse bloqueioAgendaResponse = new BloqueioAgendaResponse( id, empresaId, funcionarioId, tipoBloqueio, dataInicio, dataFim, horaInicio, horaFim, recorrente, diasSemanaRecorrencia, motivo, afetaTodosFuncionarios, funcionariosAfetados, observacoes, ativo, dataCadastro );

        return bloqueioAgendaResponse;
    }
}
