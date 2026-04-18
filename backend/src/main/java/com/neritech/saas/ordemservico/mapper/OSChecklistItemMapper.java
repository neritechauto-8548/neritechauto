package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.OSChecklistItem;
import com.neritech.saas.ordemservico.dto.OSChecklistItemRequest;
import com.neritech.saas.ordemservico.dto.OSChecklistItemResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OSChecklistItemMapper {

    @Mapping(source = "ordemServico.id", target = "ordemServicoId")
    @Mapping(source = "checklistModelo.id", target = "checklistModeloId")
    @Mapping(source = "itemModelo.id", target = "itemModeloId")
    OSChecklistItemResponse toResponse(OSChecklistItem entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "checklistModelo", ignore = true)
    @Mapping(target = "itemModelo", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(OSChecklistItemRequest request, @MappingTarget OSChecklistItem entity);
}
