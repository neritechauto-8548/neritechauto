package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.ItemOSProduto;
import com.neritech.saas.ordemservico.dto.ItemOSProdutoRequest;
import com.neritech.saas.ordemservico.dto.ItemOSProdutoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ItemOSProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "fornecedor", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    ItemOSProduto toEntity(ItemOSProdutoRequest request);

    @Mapping(source = "ordemServico.id", target = "ordemServicoId")
    @Mapping(source = "fornecedor.id", target = "fornecedorId")
    ItemOSProdutoResponse toResponse(ItemOSProduto entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "fornecedor", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntityFromRequest(ItemOSProdutoRequest request, @MappingTarget ItemOSProduto entity);
}
