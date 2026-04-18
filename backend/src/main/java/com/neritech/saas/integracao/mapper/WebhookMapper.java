package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.Webhook;
import com.neritech.saas.integracao.dto.WebhookRequest;
import com.neritech.saas.integracao.dto.WebhookResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WebhookMapper {
    Webhook toEntity(WebhookRequest request);

    WebhookResponse toResponse(Webhook entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromRequest(WebhookRequest request, @MappingTarget Webhook entity);
}
