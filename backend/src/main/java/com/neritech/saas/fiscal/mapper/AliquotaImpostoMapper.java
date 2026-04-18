package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.AliquotaImposto;
import com.neritech.saas.fiscal.dto.AliquotaImpostoRequest;
import com.neritech.saas.fiscal.dto.AliquotaImpostoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AliquotaImpostoMapper {
    AliquotaImposto toEntity(AliquotaImpostoRequest request);

    @Mapping(source = "dataCadastro", target = "createdAt")
    @Mapping(source = "dataAtualizacao", target = "updatedAt")
    AliquotaImpostoResponse toResponse(AliquotaImposto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromRequest(AliquotaImpostoRequest request, @MappingTarget AliquotaImposto entity);
}
