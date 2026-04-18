package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.Sincronizacao;
import com.neritech.saas.integracao.dto.SincronizacaoRequest;
import com.neritech.saas.integracao.dto.SincronizacaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { IntegracaoAtivaMapper.class })
public interface SincronizacaoMapper {
    @Mapping(target = "integracao", ignore = true)
    Sincronizacao toEntity(SincronizacaoRequest request);

    SincronizacaoResponse toResponse(Sincronizacao entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "integracao", ignore = true)
    void updateEntityFromRequest(SincronizacaoRequest request, @MappingTarget Sincronizacao entity);
}
