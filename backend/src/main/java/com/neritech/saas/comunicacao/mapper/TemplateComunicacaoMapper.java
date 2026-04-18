package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.TemplateComunicacao;
import com.neritech.saas.comunicacao.dto.TemplateComunicacaoRequest;
import com.neritech.saas.comunicacao.dto.TemplateComunicacaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TemplateComunicacaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    TemplateComunicacao toEntity(TemplateComunicacaoRequest request);

    TemplateComunicacaoResponse toResponse(TemplateComunicacao entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntity(TemplateComunicacaoRequest request, @MappingTarget TemplateComunicacao entity);
}
