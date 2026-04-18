package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.RegimeTributario;
import com.neritech.saas.fiscal.dto.RegimeTributarioRequest;
import com.neritech.saas.fiscal.dto.RegimeTributarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RegimeTributarioMapper {
    RegimeTributario toEntity(RegimeTributarioRequest request);

    RegimeTributarioResponse toResponse(RegimeTributario entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromRequest(RegimeTributarioRequest request, @MappingTarget RegimeTributario entity);
}
