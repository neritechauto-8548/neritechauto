package com.neritech.saas.produtoServico.mapper;

import org.mapstruct.*;

import com.neritech.saas.produtoServico.domain.Servico;
import com.neritech.saas.produtoServico.dto.ServicoRequest;
import com.neritech.saas.produtoServico.dto.ServicoResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServicoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    Servico toEntity(ServicoRequest request);

    @Mapping(source = "empresaId", target = "empresaId")
    ServicoResponse toResponse(Servico entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(ServicoRequest request, @MappingTarget Servico entity);
}
