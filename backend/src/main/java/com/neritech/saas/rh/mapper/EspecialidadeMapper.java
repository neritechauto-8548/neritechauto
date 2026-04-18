package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Especialidade;
import com.neritech.saas.rh.dto.EspecialidadeRequest;
import com.neritech.saas.rh.dto.EspecialidadeResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EspecialidadeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    Especialidade toEntity(EspecialidadeRequest request);

    EspecialidadeResponse toResponse(Especialidade entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntity(EspecialidadeRequest request, @MappingTarget Especialidade entity);
}
