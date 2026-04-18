package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Comissao;
import com.neritech.saas.rh.dto.ComissaoRequest;
import com.neritech.saas.rh.dto.ComissaoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ComissaoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "aprovadaPor", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    @Mapping(target = "pagaPor", ignore = true)
    Comissao toEntity(ComissaoRequest request);

    ComissaoResponse toResponse(Comissao entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "aprovadaPor", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    @Mapping(target = "pagaPor", ignore = true)
    void updateEntity(ComissaoRequest request, @MappingTarget Comissao entity);
}
