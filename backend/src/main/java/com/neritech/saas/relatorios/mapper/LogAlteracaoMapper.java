package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.LogAlteracao;
import com.neritech.saas.relatorios.dto.LogAlteracaoRequest;
import com.neritech.saas.relatorios.dto.LogAlteracaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LogAlteracaoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataAlteracao", ignore = true)
    @Mapping(target = "auditado", ignore = true)
    @Mapping(target = "dataAuditoria", ignore = true)
    @Mapping(target = "auditorResponsavel", ignore = true)
    @Mapping(target = "observacoesAuditoria", ignore = true)
    LogAlteracao toEntity(LogAlteracaoRequest request);

    LogAlteracaoResponse toResponse(LogAlteracao entity);
}
