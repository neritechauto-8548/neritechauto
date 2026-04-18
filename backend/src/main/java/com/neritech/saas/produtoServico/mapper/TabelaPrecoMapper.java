package com.neritech.saas.produtoServico.mapper;

import org.mapstruct.*;

import com.neritech.saas.produtoServico.domain.TabelaPreco;
import com.neritech.saas.produtoServico.dto.TabelaPrecoRequest;
import com.neritech.saas.produtoServico.dto.TabelaPrecoResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TabelaPrecoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true) // Handled in service
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    TabelaPreco toEntity(TabelaPrecoRequest request);

    @Mapping(source = "empresaId", target = "empresaId")
    TabelaPrecoResponse toResponse(TabelaPreco entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(TabelaPrecoRequest request, @MappingTarget TabelaPreco entity);
}
