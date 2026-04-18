package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.Orcamento;
import com.neritech.saas.ordemservico.dto.OrcamentoRequest;
import com.neritech.saas.ordemservico.dto.OrcamentoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrcamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    Orcamento toEntity(OrcamentoRequest request);

    @Mapping(source = "ordemServico.id", target = "ordemServicoId")
    @Mapping(source = "versao", target = "versaoRegistro")
    OrcamentoResponse toResponse(Orcamento entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntityFromRequest(OrcamentoRequest request, @MappingTarget Orcamento entity);
}
