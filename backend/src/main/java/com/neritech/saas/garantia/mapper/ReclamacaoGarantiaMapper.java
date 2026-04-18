package com.neritech.saas.garantia.mapper;

import com.neritech.saas.garantia.domain.ReclamacaoGarantia;
import com.neritech.saas.garantia.dto.ReclamacaoGarantiaRequest;
import com.neritech.saas.garantia.dto.ReclamacaoGarantiaResponse;
import org.mapstruct.*;

/**
 * Mapper MapStruct para ReclamacaoGarantia
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReclamacaoGarantiaMapper {

    @Mapping(target = "garantia", ignore = true)
    @Mapping(target = "tecnicoResponsavel", ignore = true)
    ReclamacaoGarantia toEntity(ReclamacaoGarantiaRequest request);

    @Mapping(target = "tecnicoResponsavelNome", source = "tecnicoResponsavel.nomeCompleto")
    ReclamacaoGarantiaResponse toResponse(ReclamacaoGarantia entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "garantia", ignore = true)
    @Mapping(target = "tecnicoResponsavel", ignore = true)
    void updateEntityFromDTO(ReclamacaoGarantiaRequest request, @MappingTarget ReclamacaoGarantia entity);
}
