package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.TipoAgendamento;
import com.neritech.saas.agendamento.dto.TipoAgendamentoRequest;
import com.neritech.saas.agendamento.dto.TipoAgendamentoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TipoAgendamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    TipoAgendamento toEntity(TipoAgendamentoRequest request);

    TipoAgendamentoResponse toResponse(TipoAgendamento entity);
}
