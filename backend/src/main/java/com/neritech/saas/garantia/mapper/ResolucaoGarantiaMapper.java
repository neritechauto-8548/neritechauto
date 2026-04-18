package com.neritech.saas.garantia.mapper;

import com.neritech.saas.garantia.domain.ResolucaoGarantia;
import com.neritech.saas.garantia.dto.ResolucaoGarantiaRequest;
import com.neritech.saas.garantia.dto.ResolucaoGarantiaResponse;
import org.mapstruct.*;

/**
 * Mapper MapStruct para ResolucaoGarantia
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResolucaoGarantiaMapper {

    @Mapping(target = "reclamacao", ignore = true)
    @Mapping(target = "novaGarantia", ignore = true)
    @Mapping(target = "funcionarioExecutor", ignore = true)
    ResolucaoGarantia toEntity(ResolucaoGarantiaRequest request);

    @Mapping(target = "funcionarioExecutorNome", source = "funcionarioExecutor.nomeCompleto")
    ResolucaoGarantiaResponse toResponse(ResolucaoGarantia entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "reclamacao", ignore = true)
    @Mapping(target = "novaGarantia", ignore = true)
    @Mapping(target = "funcionarioExecutor", ignore = true)
    void updateEntityFromDTO(ResolucaoGarantiaRequest request, @MappingTarget ResolucaoGarantia entity);
}
