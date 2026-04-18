package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.Imposto;
import com.neritech.saas.financeiro.dto.ImpostoRequest;
import com.neritech.saas.financeiro.dto.ImpostoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImpostoMapper {
    Imposto toEntity(ImpostoRequest request);

    ImpostoResponse toResponse(Imposto entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ImpostoRequest request, @MappingTarget Imposto entity);
}
