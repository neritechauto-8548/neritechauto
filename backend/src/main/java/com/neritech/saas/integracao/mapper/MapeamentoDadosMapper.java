package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.MapeamentoDados;
import com.neritech.saas.integracao.dto.MapeamentoDadosRequest;
import com.neritech.saas.integracao.dto.MapeamentoDadosResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { IntegracaoAtivaMapper.class })
public interface MapeamentoDadosMapper {
    @Mapping(target = "integracao", ignore = true)
    MapeamentoDados toEntity(MapeamentoDadosRequest request);

    MapeamentoDadosResponse toResponse(MapeamentoDados entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "integracao", ignore = true)
    void updateEntityFromRequest(MapeamentoDadosRequest request, @MappingTarget MapeamentoDados entity);
}
