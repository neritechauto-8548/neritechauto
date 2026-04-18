package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.PerdaEstoque;
import com.neritech.saas.estoque.dto.PerdaEstoqueRequest;
import com.neritech.saas.estoque.dto.PerdaEstoqueResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PerdaEstoqueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "localizacao", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    PerdaEstoque toEntity(PerdaEstoqueRequest request);

    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.nome", target = "produtoNome")
    @Mapping(source = "localizacao.id", target = "localizacaoId")
    @Mapping(source = "localizacao.nome", target = "localizacaoNome")
    PerdaEstoqueResponse toResponse(PerdaEstoque entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "localizacao", ignore = true)
    @Mapping(target = "dataAprovacao", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(PerdaEstoqueRequest request, @MappingTarget PerdaEstoque entity);
}
