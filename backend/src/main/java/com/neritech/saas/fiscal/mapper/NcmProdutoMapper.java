package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.NcmProduto;
import com.neritech.saas.fiscal.dto.NcmProdutoRequest;
import com.neritech.saas.fiscal.dto.NcmProdutoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NcmProdutoMapper {
    NcmProduto toEntity(NcmProdutoRequest request);

    NcmProdutoResponse toResponse(NcmProduto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromRequest(NcmProdutoRequest request, @MappingTarget NcmProduto entity);
}
