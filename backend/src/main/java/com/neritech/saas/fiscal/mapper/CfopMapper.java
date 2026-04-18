package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.Cfop;
import com.neritech.saas.fiscal.dto.CfopRequest;
import com.neritech.saas.fiscal.dto.CfopResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CfopMapper {
    Cfop toEntity(CfopRequest request);

    CfopResponse toResponse(Cfop entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromRequest(CfopRequest request, @MappingTarget Cfop entity);
}
