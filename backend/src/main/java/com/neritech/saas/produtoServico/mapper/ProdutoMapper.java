package com.neritech.saas.produtoServico.mapper;

import org.mapstruct.*;

import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.dto.ProdutoRequest;
import com.neritech.saas.produtoServico.dto.ProdutoResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true) // Handled in service
    @Mapping(target = "categoria", ignore = true) // Handled in service
    @Mapping(target = "unidadeMedida", ignore = true) // Handled in service
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "quantidadeEstoque", ignore = true)
    Produto toEntity(ProdutoRequest request);

    @Mapping(source = "empresaId", target = "empresaId")
    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.nome", target = "categoriaNome")
    @Mapping(source = "unidadeMedida.id", target = "unidadeMedidaId")
    @Mapping(source = "unidadeMedida.sigla", target = "unidadeMedidaSigla")
    @Mapping(source = "quantidadeEstoque", target = "quantidadeEstoque")
    ProdutoResponse toResponse(Produto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "categoria", ignore = true) // Handled in service
    @Mapping(target = "unidadeMedida", ignore = true) // Handled in service
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "quantidadeEstoque", ignore = true)
    void updateEntityFromRequest(ProdutoRequest request, @MappingTarget Produto entity);
}
