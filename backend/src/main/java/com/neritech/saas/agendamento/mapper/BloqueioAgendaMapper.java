package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.BloqueioAgenda;
import com.neritech.saas.agendamento.dto.BloqueioAgendaRequest;
import com.neritech.saas.agendamento.dto.BloqueioAgendaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BloqueioAgendaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    BloqueioAgenda toEntity(BloqueioAgendaRequest request);

    BloqueioAgendaResponse toResponse(BloqueioAgenda entity);
}
