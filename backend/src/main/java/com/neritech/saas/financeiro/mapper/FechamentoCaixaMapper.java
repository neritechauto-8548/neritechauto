package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.FechamentoCaixa;
import com.neritech.saas.financeiro.dto.FechamentoCaixaRequest;
import com.neritech.saas.financeiro.dto.FechamentoCaixaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface FechamentoCaixaMapper {
    FechamentoCaixa toEntity(FechamentoCaixaRequest request);

    @Mapping(target = "responsavelNome", ignore = true)
    FechamentoCaixaResponse toResponse(FechamentoCaixa entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(FechamentoCaixaRequest request, @MappingTarget FechamentoCaixa entity);
}
