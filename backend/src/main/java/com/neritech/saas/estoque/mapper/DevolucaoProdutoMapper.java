package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.DevolucaoProduto;
import com.neritech.saas.estoque.dto.DevolucaoProdutoRequest;
import com.neritech.saas.estoque.dto.DevolucaoProdutoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DevolucaoProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    @Mapping(target = "dataDevolucao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    DevolucaoProduto toEntity(DevolucaoProdutoRequest request);

    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.nome", target = "produtoNome")
    DevolucaoProdutoResponse toResponse(DevolucaoProduto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    @Mapping(target = "dataDevolucao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(DevolucaoProdutoRequest request, @MappingTarget DevolucaoProduto entity);
}
