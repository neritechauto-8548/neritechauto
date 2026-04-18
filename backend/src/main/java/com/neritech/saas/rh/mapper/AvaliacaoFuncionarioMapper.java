package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.AvaliacaoFuncionario;
import com.neritech.saas.rh.dto.AvaliacaoFuncionarioRequest;
import com.neritech.saas.rh.dto.AvaliacaoFuncionarioResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AvaliacaoFuncionarioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "aprovadaPor", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    AvaliacaoFuncionario toEntity(AvaliacaoFuncionarioRequest request);

    AvaliacaoFuncionarioResponse toResponse(AvaliacaoFuncionario entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "aprovadaPor", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    void updateEntity(AvaliacaoFuncionarioRequest request, @MappingTarget AvaliacaoFuncionario entity);
}
