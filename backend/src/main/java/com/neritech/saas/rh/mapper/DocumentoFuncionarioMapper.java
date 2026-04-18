package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.DocumentoFuncionario;
import com.neritech.saas.rh.dto.DocumentoFuncionarioRequest;
import com.neritech.saas.rh.dto.DocumentoFuncionarioResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DocumentoFuncionarioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "verificadoPor", ignore = true)
    @Mapping(target = "dataVerificacao", ignore = true)
    @Mapping(target = "cadastradoPor", ignore = true)
    DocumentoFuncionario toEntity(DocumentoFuncionarioRequest request);

    DocumentoFuncionarioResponse toResponse(DocumentoFuncionario entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "verificadoPor", ignore = true)
    @Mapping(target = "dataVerificacao", ignore = true)
    @Mapping(target = "cadastradoPor", ignore = true)
    void updateEntity(DocumentoFuncionarioRequest request, @MappingTarget DocumentoFuncionario entity);
}
