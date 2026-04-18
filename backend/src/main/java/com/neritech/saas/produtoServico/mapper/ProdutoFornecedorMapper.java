package com.neritech.saas.produtoServico.mapper;

import org.mapstruct.*;

import com.neritech.saas.produtoServico.domain.ProdutoFornecedor;
import com.neritech.saas.produtoServico.dto.ProdutoFornecedorRequest;
import com.neritech.saas.produtoServico.dto.ProdutoFornecedorResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoFornecedorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true) // Handled in service
    @Mapping(target = "fornecedor", ignore = true) // Handled in service
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    ProdutoFornecedor toEntity(ProdutoFornecedorRequest request);

    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.nome", target = "produtoNome")
    @Mapping(source = "fornecedor.id", target = "fornecedorId")
    @Mapping(source = "fornecedor.nomeFantasia", target = "fornecedorNome")
    ProdutoFornecedorResponse toResponse(ProdutoFornecedor entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "fornecedor", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(ProdutoFornecedorRequest request, @MappingTarget ProdutoFornecedor entity);
}
