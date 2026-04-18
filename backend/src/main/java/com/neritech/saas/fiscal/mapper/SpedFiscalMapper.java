package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.SpedFiscal;
import com.neritech.saas.fiscal.dto.SpedFiscalRequest;
import com.neritech.saas.fiscal.dto.SpedFiscalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SpedFiscalMapper {
    SpedFiscal toEntity(SpedFiscalRequest request);

    SpedFiscalResponse toResponse(SpedFiscal entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "dataGeracao", ignore = true)
    void updateEntityFromRequest(SpedFiscalRequest request, @MappingTarget SpedFiscal entity);
}
