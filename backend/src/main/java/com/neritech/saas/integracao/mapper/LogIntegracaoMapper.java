package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.LogIntegracao;
import com.neritech.saas.integracao.dto.LogIntegracaoRequest;
import com.neritech.saas.integracao.dto.LogIntegracaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { IntegracaoAtivaMapper.class })
public interface LogIntegracaoMapper {
    @Mapping(target = "integracao", ignore = true)
    LogIntegracao toEntity(LogIntegracaoRequest request);

    LogIntegracaoResponse toResponse(LogIntegracao entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "integracao", ignore = true)
    void updateEntityFromRequest(LogIntegracaoRequest request, @MappingTarget LogIntegracao entity);
}
