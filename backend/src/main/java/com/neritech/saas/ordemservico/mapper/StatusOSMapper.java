package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.StatusOS;
import com.neritech.saas.ordemservico.dto.StatusOSRequest;
import com.neritech.saas.ordemservico.dto.StatusOSResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StatusOSMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sistema", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    StatusOS toEntity(StatusOSRequest request);

    StatusOSResponse toResponse(StatusOS entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sistema", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntityFromRequest(StatusOSRequest request, @MappingTarget StatusOS entity);
}
