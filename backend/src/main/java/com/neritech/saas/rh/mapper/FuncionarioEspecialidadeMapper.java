package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.FuncionarioEspecialidade;
import com.neritech.saas.rh.dto.FuncionarioEspecialidadeRequest;
import com.neritech.saas.rh.dto.FuncionarioEspecialidadeResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FuncionarioEspecialidadeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    FuncionarioEspecialidade toEntity(FuncionarioEspecialidadeRequest request);

    FuncionarioEspecialidadeResponse toResponse(FuncionarioEspecialidade entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntity(FuncionarioEspecialidadeRequest request, @MappingTarget FuncionarioEspecialidade entity);
}
