package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.CstProduto;
import com.neritech.saas.fiscal.dto.CstProdutoRequest;
import com.neritech.saas.fiscal.dto.CstProdutoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CstProdutoMapper {
    CstProduto toEntity(CstProdutoRequest request);

    CstProdutoResponse toResponse(CstProduto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromRequest(CstProdutoRequest request, @MappingTarget CstProduto entity);
}
