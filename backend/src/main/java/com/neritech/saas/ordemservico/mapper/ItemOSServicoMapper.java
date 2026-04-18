package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.ItemOSServico;
import com.neritech.saas.ordemservico.dto.ItemOSServicoRequest;
import com.neritech.saas.ordemservico.dto.ItemOSServicoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ItemOSServicoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "versao", ignore = true)
    ItemOSServico toEntity(ItemOSServicoRequest request);

    @Mapping(source = "ordemServico.id", target = "ordemServicoId")
    ItemOSServicoResponse toResponse(ItemOSServico entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ordemServico", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    void updateEntityFromRequest(ItemOSServicoRequest request, @MappingTarget ItemOSServico entity);
}
