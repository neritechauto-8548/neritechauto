package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Mecanico;
import com.neritech.saas.rh.dto.MecanicoRequest;
import com.neritech.saas.rh.dto.MecanicoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MecanicoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    Mecanico toEntity(MecanicoRequest request);

    MecanicoResponse toResponse(Mecanico entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntity(MecanicoRequest request, @MappingTarget Mecanico entity);
}
