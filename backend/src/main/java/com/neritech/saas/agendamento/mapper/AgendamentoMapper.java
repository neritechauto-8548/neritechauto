package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.Agendamento;
import com.neritech.saas.agendamento.dto.AgendamentoRequest;
import com.neritech.saas.agendamento.dto.AgendamentoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AgendamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "numeroAgendamento", ignore = true)
    @Mapping(target = "tipoAgendamento.id", source = "tipoAgendamentoId")
    @Mapping(target = "agendadoPor", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    Agendamento toEntity(AgendamentoRequest request);

    @Mapping(target = "tipoAgendamentoId", source = "tipoAgendamento.id")
    @Mapping(target = "tipoAgendamentoNome", source = "tipoAgendamento.nome")
    AgendamentoResponse toResponse(Agendamento entity);
}
