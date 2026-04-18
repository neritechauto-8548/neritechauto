package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.ItChecklist;
import com.neritech.saas.ordemservico.dto.ItChecklistRequest;
import com.neritech.saas.ordemservico.dto.ItChecklistResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, implementationName = "OrdemServico<CLASS_NAME>Impl")
public interface ItChecklistMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checklist", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    ItChecklist toEntity(ItChecklistRequest request);

    @Mapping(source = "checklist.id", target = "checkListId")
    ItChecklistResponse toResponse(ItChecklist entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checklist", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(ItChecklistRequest request, @MappingTarget ItChecklist entity);
}
