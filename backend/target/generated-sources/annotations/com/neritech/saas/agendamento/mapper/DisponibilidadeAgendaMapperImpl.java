package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.DisponibilidadeAgenda;
import com.neritech.saas.agendamento.dto.DisponibilidadeAgendaRequest;
import com.neritech.saas.agendamento.dto.DisponibilidadeAgendaResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:24-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class DisponibilidadeAgendaMapperImpl implements DisponibilidadeAgendaMapper {

    @Override
    public DisponibilidadeAgenda toEntity(DisponibilidadeAgendaRequest request) {
        if ( request == null ) {
            return null;
        }

        DisponibilidadeAgenda disponibilidadeAgenda = new DisponibilidadeAgenda();

        disponibilidadeAgenda.setEmpresaId( request.empresaId() );
        disponibilidadeAgenda.setCapacidadeAtendimentos( request.capacidadeAtendimentos() );
        disponibilidadeAgenda.setDisponivel( request.disponivel() );
        disponibilidadeAgenda.setFuncionarioId( request.funcionarioId() );
        disponibilidadeAgenda.setDataDisponibilidade( request.dataDisponibilidade() );
        disponibilidadeAgenda.setDiaSemana( request.diaSemana() );
        disponibilidadeAgenda.setHoraInicio( request.horaInicio() );
        disponibilidadeAgenda.setHoraFim( request.horaFim() );
        disponibilidadeAgenda.setIntervaloAlmocoInicio( request.intervaloAlmocoInicio() );
        disponibilidadeAgenda.setIntervaloAlmocoFim( request.intervaloAlmocoFim() );
        disponibilidadeAgenda.setEspecialidadesDisponiveis( request.especialidadesDisponiveis() );
        disponibilidadeAgenda.setTiposAgendamentoAceitos( request.tiposAgendamentoAceitos() );
        disponibilidadeAgenda.setObservacoes( request.observacoes() );

        return disponibilidadeAgenda;
    }

    @Override
    public DisponibilidadeAgendaResponse toResponse(DisponibilidadeAgenda entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        Long funcionarioId = null;
        LocalDate dataDisponibilidade = null;
        Integer diaSemana = null;
        LocalTime horaInicio = null;
        LocalTime horaFim = null;
        LocalTime intervaloAlmocoInicio = null;
        LocalTime intervaloAlmocoFim = null;
        Boolean disponivel = null;
        Integer capacidadeAtendimentos = null;
        String especialidadesDisponiveis = null;
        String tiposAgendamentoAceitos = null;
        String observacoes = null;
        LocalDateTime dataCadastro = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        funcionarioId = entity.getFuncionarioId();
        dataDisponibilidade = entity.getDataDisponibilidade();
        diaSemana = entity.getDiaSemana();
        horaInicio = entity.getHoraInicio();
        horaFim = entity.getHoraFim();
        intervaloAlmocoInicio = entity.getIntervaloAlmocoInicio();
        intervaloAlmocoFim = entity.getIntervaloAlmocoFim();
        disponivel = entity.getDisponivel();
        capacidadeAtendimentos = entity.getCapacidadeAtendimentos();
        especialidadesDisponiveis = entity.getEspecialidadesDisponiveis();
        tiposAgendamentoAceitos = entity.getTiposAgendamentoAceitos();
        observacoes = entity.getObservacoes();
        dataCadastro = entity.getDataCadastro();

        DisponibilidadeAgendaResponse disponibilidadeAgendaResponse = new DisponibilidadeAgendaResponse( id, empresaId, funcionarioId, dataDisponibilidade, diaSemana, horaInicio, horaFim, intervaloAlmocoInicio, intervaloAlmocoFim, disponivel, capacidadeAtendimentos, especialidadesDisponiveis, tiposAgendamentoAceitos, observacoes, dataCadastro );

        return disponibilidadeAgendaResponse;
    }
}
