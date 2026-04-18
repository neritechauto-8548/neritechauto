package com.neritech.saas.comunicacao.mapper;

import com.neritech.saas.comunicacao.domain.ListaContatos;
import com.neritech.saas.comunicacao.dto.ListaContatosRequest;
import com.neritech.saas.comunicacao.dto.ListaContatosResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ListaContatosMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "totalContatos", ignore = true)
    @Mapping(target = "contatosAtivos", ignore = true)
    @Mapping(target = "dataUltimaAtualizacao", ignore = true)
    @Mapping(target = "proximaAtualizacao", ignore = true)
    @Mapping(target = "criadaPor", ignore = true)
    ListaContatos toEntity(ListaContatosRequest request);

    ListaContatosResponse toResponse(ListaContatos entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "totalContatos", ignore = true)
    @Mapping(target = "contatosAtivos", ignore = true)
    @Mapping(target = "dataUltimaAtualizacao", ignore = true)
    @Mapping(target = "proximaAtualizacao", ignore = true)
    @Mapping(target = "criadaPor", ignore = true)
    void updateEntity(ListaContatosRequest request, @MappingTarget ListaContatos entity);
}
