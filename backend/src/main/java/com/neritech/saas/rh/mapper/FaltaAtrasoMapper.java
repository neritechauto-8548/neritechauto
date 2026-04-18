package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.FaltaAtraso;
import com.neritech.saas.rh.dto.FaltaAtrasoRequest;
import com.neritech.saas.rh.dto.FaltaAtrasoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FaltaAtrasoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "aprovadoPor", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    FaltaAtraso toEntity(FaltaAtrasoRequest request);

    FaltaAtrasoResponse toResponse(FaltaAtraso entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "registradoPor", ignore = true)
    @Mapping(target = "aprovadoPor", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    void updateEntity(FaltaAtrasoRequest request, @MappingTarget FaltaAtraso entity);
}
