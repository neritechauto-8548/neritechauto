package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.TokenApi;
import com.neritech.saas.integracao.dto.TokenApiRequest;
import com.neritech.saas.integracao.dto.TokenApiResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { IntegracaoAtivaMapper.class })
public interface TokenApiMapper {
    @Mapping(target = "integracao", ignore = true)
    TokenApi toEntity(TokenApiRequest request);

    TokenApiResponse toResponse(TokenApi entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "integracao", ignore = true)
    void updateEntityFromRequest(TokenApiRequest request, @MappingTarget TokenApi entity);
}
