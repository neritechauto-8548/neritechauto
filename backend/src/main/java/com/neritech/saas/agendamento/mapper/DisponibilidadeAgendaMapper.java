package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.DisponibilidadeAgenda;
import com.neritech.saas.agendamento.dto.DisponibilidadeAgendaRequest;
import com.neritech.saas.agendamento.dto.DisponibilidadeAgendaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DisponibilidadeAgendaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    DisponibilidadeAgenda toEntity(DisponibilidadeAgendaRequest request);

    DisponibilidadeAgendaResponse toResponse(DisponibilidadeAgenda entity);
}
