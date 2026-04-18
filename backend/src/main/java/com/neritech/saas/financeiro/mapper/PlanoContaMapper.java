package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.PlanoConta;
import com.neritech.saas.financeiro.dto.PlanoContaRequest;
import com.neritech.saas.financeiro.dto.PlanoContaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanoContaMapper {
    @Mapping(target = "contaPai", ignore = true)
    PlanoConta toEntity(PlanoContaRequest request);

    @Mapping(target = "contaPaiNome", source = "contaPai.nome")
    PlanoContaResponse toResponse(PlanoConta entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contaPai", ignore = true)
    void updateEntityFromDTO(PlanoContaRequest request, @MappingTarget PlanoConta entity);
}
