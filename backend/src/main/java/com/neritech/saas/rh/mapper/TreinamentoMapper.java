package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Treinamento;
import com.neritech.saas.rh.dto.TreinamentoRequest;
import com.neritech.saas.rh.dto.TreinamentoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TreinamentoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    Treinamento toEntity(TreinamentoRequest request);

    TreinamentoResponse toResponse(Treinamento entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntity(TreinamentoRequest request, @MappingTarget Treinamento entity);
}
