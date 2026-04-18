package com.neritech.saas.produtoServico.mapper;

import org.mapstruct.*;

import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.dto.FornecedorRequest;
import com.neritech.saas.produtoServico.dto.FornecedorResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FornecedorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true) // Handled in service
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    Fornecedor toEntity(FornecedorRequest request);

    @Mapping(source = "empresaId", target = "empresaId")
    FornecedorResponse toResponse(Fornecedor entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(FornecedorRequest request, @MappingTarget Fornecedor entity);
}
