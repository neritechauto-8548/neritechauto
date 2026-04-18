package com.neritech.saas.produtoServico.mapper;

import org.mapstruct.*;

import com.neritech.saas.produtoServico.domain.CategoriaProduto;
import com.neritech.saas.produtoServico.dto.CategoriaProdutoRequest;
import com.neritech.saas.produtoServico.dto.CategoriaProdutoResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoriaProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true) // Handled in service
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    CategoriaProduto toEntity(CategoriaProdutoRequest request);

    @Mapping(source = "empresaId", target = "empresaId")
    CategoriaProdutoResponse toResponse(CategoriaProduto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(CategoriaProdutoRequest request, @MappingTarget CategoriaProduto entity);
}
