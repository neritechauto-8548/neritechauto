package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.AlertaAutomatico;
import com.neritech.saas.comunicacao.dto.AlertaAutomaticoRequest;
import com.neritech.saas.comunicacao.dto.AlertaAutomaticoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AlertaAutomaticoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "ultimaExecucao", ignore = true)
    @Mapping(target = "proximaExecucao", ignore = true)
    @Mapping(target = "totalDisparos", ignore = true)
    @Mapping(target = "totalErros", ignore = true)
    @Mapping(target = "logExecucoes", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    AlertaAutomatico toEntity(AlertaAutomaticoRequest request);

    AlertaAutomaticoResponse toResponse(AlertaAutomatico entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "ultimaExecucao", ignore = true)
    @Mapping(target = "proximaExecucao", ignore = true)
    @Mapping(target = "totalDisparos", ignore = true)
    @Mapping(target = "totalErros", ignore = true)
    @Mapping(target = "logExecucoes", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    void updateEntity(AlertaAutomaticoRequest request, @MappingTarget AlertaAutomatico entity);
}
