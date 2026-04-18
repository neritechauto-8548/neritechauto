package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.Reagendamento;
import com.neritech.saas.agendamento.dto.ReagendamentoRequest;
import com.neritech.saas.agendamento.dto.ReagendamentoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReagendamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "agendamentoOriginal.id", source = "agendamentoOriginalId")
    @Mapping(target = "agendamentoNovo.id", source = "agendamentoNovoId")
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    Reagendamento toEntity(ReagendamentoRequest request);

    @Mapping(target = "agendamentoOriginalId", source = "agendamentoOriginal.id")
    @Mapping(target = "numeroAgendamentoOriginal", source = "agendamentoOriginal.numeroAgendamento")
    @Mapping(target = "agendamentoNovoId", source = "agendamentoNovo.id")
    @Mapping(target = "numeroAgendamentoNovo", source = "agendamentoNovo.numeroAgendamento")
    ReagendamentoResponse toResponse(Reagendamento entity);
}
