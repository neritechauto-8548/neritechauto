package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Departamento;
import com.neritech.saas.rh.dto.DepartamentoRequest;
import com.neritech.saas.rh.dto.DepartamentoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepartamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    Departamento toEntity(DepartamentoRequest request);

    DepartamentoResponse toResponse(Departamento entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntity(DepartamentoRequest request, @MappingTarget Departamento entity);
}
