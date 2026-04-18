package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.dto.PlanoAssinaturaRequest;
import com.neritech.saas.empresa.dto.PlanoAssinaturaResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanoAssinaturaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    PlanoAssinatura toEntity(PlanoAssinaturaRequest request);

    PlanoAssinaturaResponse toResponse(PlanoAssinatura entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    void updateEntityFromRequest(PlanoAssinaturaRequest request, @MappingTarget PlanoAssinatura entity);
}
