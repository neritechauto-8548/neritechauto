package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.RecursoAgenda;
import com.neritech.saas.agendamento.dto.RecursoAgendaRequest;
import com.neritech.saas.agendamento.dto.RecursoAgendaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RecursoAgendaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    RecursoAgenda toEntity(RecursoAgendaRequest request);

    RecursoAgendaResponse toResponse(RecursoAgenda entity);
}
