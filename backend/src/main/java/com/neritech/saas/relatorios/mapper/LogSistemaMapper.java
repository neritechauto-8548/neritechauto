package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.LogSistema;
import com.neritech.saas.relatorios.dto.LogSistemaRequest;
import com.neritech.saas.relatorios.dto.LogSistemaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LogSistemaMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataOcorrencia", ignore = true) // Set default in entity or service
    @Mapping(target = "resolvido", ignore = true)
    @Mapping(target = "dataResolucao", ignore = true)
    @Mapping(target = "responsavelResolucao", ignore = true)
    @Mapping(target = "observacoesResolucao", ignore = true)
    LogSistema toEntity(LogSistemaRequest request);

    LogSistemaResponse toResponse(LogSistema entity);
}
