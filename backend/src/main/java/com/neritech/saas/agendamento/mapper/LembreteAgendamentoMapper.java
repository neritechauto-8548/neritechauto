package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.LembreteAgendamento;
import com.neritech.saas.agendamento.dto.LembreteAgendamentoRequest;
import com.neritech.saas.agendamento.dto.LembreteAgendamentoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LembreteAgendamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "agendamento.id", source = "agendamentoId")
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    LembreteAgendamento toEntity(LembreteAgendamentoRequest request);

    @Mapping(target = "agendamentoId", source = "agendamento.id")
    @Mapping(target = "numeroAgendamento", source = "agendamento.numeroAgendamento")
    LembreteAgendamentoResponse toResponse(LembreteAgendamento entity);
}
