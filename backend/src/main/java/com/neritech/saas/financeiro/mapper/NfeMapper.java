package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.Nfe;
import com.neritech.saas.financeiro.dto.NfeRequest;
import com.neritech.saas.financeiro.dto.NfeResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NfeMapper {
    @Mapping(target = "fatura", ignore = true)
    Nfe toEntity(NfeRequest request);

    @Mapping(target = "faturaNumero", source = "fatura.numeroFatura")
    NfeResponse toResponse(Nfe entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "fatura", ignore = true)
    void updateEntityFromDTO(NfeRequest request, @MappingTarget Nfe entity);
}
