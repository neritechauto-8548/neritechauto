package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.NoShow;
import com.neritech.saas.agendamento.dto.NoShowRequest;
import com.neritech.saas.agendamento.dto.NoShowResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NoShowMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "agendamento.id", source = "agendamentoId")
    @Mapping(target = "novoAgendamento.id", source = "novoAgendamentoId")
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    NoShow toEntity(NoShowRequest request);

    @Mapping(target = "agendamentoId", source = "agendamento.id")
    @Mapping(target = "numeroAgendamento", source = "agendamento.numeroAgendamento")
    @Mapping(target = "novoAgendamentoId", source = "novoAgendamento.id")
    @Mapping(target = "numeroNovoAgendamento", source = "novoAgendamento.numeroAgendamento")
    NoShowResponse toResponse(NoShow entity);
}
