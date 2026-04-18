package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.dto.AgendamentoRequest;
import com.neritech.saas.agendamento.dto.AgendamentoResponse;
import org.mapstruct.*;

/**
 * Mapper para Agendamento
 */
@Mapper(componentModel = "spring")
public interface AgendamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipoAgendamento.id", source = "tipoAgendamentoId")
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    Agendamento toEntity(AgendamentoRequest request);

    @Mapping(target = "tipoAgendamentoId", source = "tipoAgendamento.id")
    @Mapping(target = "tipoAgendamentoNome", source = "tipoAgendamento.nome")
    AgendamentoResponse toResponse(Agendamento entity);
}
