package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.Checklist;
import com.neritech.saas.ordemservico.dto.ChecklistRequest;
import com.neritech.saas.ordemservico.dto.ChecklistResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChecklistMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    Checklist toEntity(ChecklistRequest request);

    ChecklistResponse toResponse(Checklist entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(ChecklistRequest request, @MappingTarget Checklist entity);
}
