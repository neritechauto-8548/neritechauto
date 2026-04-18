package com.neritech.saas.produtoServico.mapper;

import org.mapstruct.*;

import com.neritech.saas.produtoServico.domain.UnidadeMedida;
import com.neritech.saas.produtoServico.dto.UnidadeMedidaRequest;
import com.neritech.saas.produtoServico.dto.UnidadeMedidaResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UnidadeMedidaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    UnidadeMedida toEntity(UnidadeMedidaRequest request);

    UnidadeMedidaResponse toResponse(UnidadeMedida entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    void updateEntityFromRequest(UnidadeMedidaRequest request, @MappingTarget UnidadeMedida entity);
}
