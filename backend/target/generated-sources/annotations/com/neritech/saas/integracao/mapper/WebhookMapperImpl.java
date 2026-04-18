package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.Webhook;
import com.neritech.saas.integracao.dto.WebhookRequest;
import com.neritech.saas.integracao.dto.WebhookResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:09-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class WebhookMapperImpl implements WebhookMapper {

    @Override
    public Webhook toEntity(WebhookRequest request) {
        if ( request == null ) {
            return null;
        }

        Webhook.WebhookBuilder<?, ?> webhook = Webhook.builder();

        webhook.ativo( request.ativo() );
        webhook.evento( request.evento() );
        webhook.headers( request.headers() );
        webhook.metodoHttp( request.metodoHttp() );
        webhook.nome( request.nome() );
        webhook.secretKey( request.secretKey() );
        webhook.tentativasMaximas( request.tentativasMaximas() );
        webhook.urlDestino( request.urlDestino() );

        return webhook.build();
    }

    @Override
    public WebhookResponse toResponse(Webhook entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        String urlDestino = null;
        String evento = null;
        String metodoHttp = null;
        String headers = null;
        boolean ativo = false;
        Integer tentativasMaximas = null;

        id = entity.getId();
        nome = entity.getNome();
        urlDestino = entity.getUrlDestino();
        evento = entity.getEvento();
        metodoHttp = entity.getMetodoHttp();
        headers = entity.getHeaders();
        ativo = entity.isAtivo();
        tentativasMaximas = entity.getTentativasMaximas();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        WebhookResponse webhookResponse = new WebhookResponse( id, nome, urlDestino, evento, metodoHttp, headers, ativo, tentativasMaximas, createdAt, updatedAt );

        return webhookResponse;
    }

    @Override
    public void updateEntityFromRequest(WebhookRequest request, Webhook entity) {
        if ( request == null ) {
            return;
        }

        entity.setNome( request.nome() );
        entity.setUrlDestino( request.urlDestino() );
        entity.setEvento( request.evento() );
        entity.setHeaders( request.headers() );
        entity.setSecretKey( request.secretKey() );
        entity.setMetodoHttp( request.metodoHttp() );
        entity.setAtivo( request.ativo() );
        entity.setTentativasMaximas( request.tentativasMaximas() );
    }
}
