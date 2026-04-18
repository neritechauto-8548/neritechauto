package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Cargo;
import com.neritech.saas.rh.dto.CargoRequest;
import com.neritech.saas.rh.dto.CargoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CargoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    Cargo toEntity(CargoRequest request);

    CargoResponse toResponse(Cargo entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntity(CargoRequest request, @MappingTarget Cargo entity);
}
