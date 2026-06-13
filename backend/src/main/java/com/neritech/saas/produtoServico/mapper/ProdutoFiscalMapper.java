package com.neritech.saas.produtoServico.mapper;

import org.mapstruct.*;
import com.neritech.saas.produtoServico.domain.ProdutoFiscal;
import com.neritech.saas.produtoServico.dto.ProdutoFiscalRequest;
import com.neritech.saas.produtoServico.dto.ProdutoFiscalResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoFiscalMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    ProdutoFiscal toEntity(ProdutoFiscalRequest request);

    ProdutoFiscalResponse toResponse(ProdutoFiscal entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(ProdutoFiscalRequest request, @MappingTarget ProdutoFiscal entity);
}
