package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.IntegracaoAtiva;
import com.neritech.saas.integracao.dto.IntegracaoAtivaRequest;
import com.neritech.saas.integracao.dto.IntegracaoAtivaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IntegracaoAtivaMapper {
    IntegracaoAtiva toEntity(IntegracaoAtivaRequest request);

    IntegracaoAtivaResponse toResponse(IntegracaoAtiva entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromRequest(IntegracaoAtivaRequest request, @MappingTarget IntegracaoAtiva entity);
}
