package com.neritech.saas.agendamento.mapper;

import com.neritech.saas.agendamento.domain.ListaEspera;
import com.neritech.saas.agendamento.dto.ListaEsperaRequest;
import com.neritech.saas.agendamento.dto.ListaEsperaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ListaEsperaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipoAgendamento.id", source = "tipoAgendamentoId")
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataNotificacao", ignore = true)
    @Mapping(target = "dataExpiracao", ignore = true)
    @Mapping(target = "posicaoLista", ignore = true)
    @Mapping(target = "cadastradoPor", ignore = true)
    @Mapping(target = "agendamentoGerado", ignore = true)
    ListaEspera toEntity(ListaEsperaRequest request);

    @Mapping(target = "tipoAgendamentoId", source = "tipoAgendamento.id")
    @Mapping(target = "tipoAgendamentoNome", source = "tipoAgendamento.nome")
    @Mapping(target = "agendamentoGeradoId", source = "agendamentoGerado.id")
    ListaEsperaResponse toResponse(ListaEspera entity);
}
