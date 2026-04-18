package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.IntegracaoAtiva;
import com.neritech.saas.integracao.dto.IntegracaoAtivaRequest;
import com.neritech.saas.integracao.dto.IntegracaoAtivaResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:52-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class IntegracaoAtivaMapperImpl implements IntegracaoAtivaMapper {

    @Override
    public IntegracaoAtiva toEntity(IntegracaoAtivaRequest request) {
        if ( request == null ) {
            return null;
        }

        IntegracaoAtiva.IntegracaoAtivaBuilder<?, ?> integracaoAtiva = IntegracaoAtiva.builder();

        integracaoAtiva.ativo( request.ativo() );
        integracaoAtiva.configuracoes( request.configuracoes() );
        integracaoAtiva.descricao( request.descricao() );
        integracaoAtiva.nome( request.nome() );
        integracaoAtiva.tipo( request.tipo() );
        integracaoAtiva.urlBase( request.urlBase() );

        return integracaoAtiva.build();
    }

    @Override
    public IntegracaoAtivaResponse toResponse(IntegracaoAtiva entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        String tipo = null;
        String urlBase = null;
        String descricao = null;
        boolean ativo = false;
        String configuracoes = null;

        id = entity.getId();
        nome = entity.getNome();
        tipo = entity.getTipo();
        urlBase = entity.getUrlBase();
        descricao = entity.getDescricao();
        ativo = entity.isAtivo();
        configuracoes = entity.getConfiguracoes();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        IntegracaoAtivaResponse integracaoAtivaResponse = new IntegracaoAtivaResponse( id, nome, tipo, urlBase, descricao, ativo, configuracoes, createdAt, updatedAt );

        return integracaoAtivaResponse;
    }

    @Override
    public void updateEntityFromRequest(IntegracaoAtivaRequest request, IntegracaoAtiva entity) {
        if ( request == null ) {
            return;
        }

        entity.setAtivo( request.ativo() );
        entity.setNome( request.nome() );
        entity.setTipo( request.tipo() );
        entity.setUrlBase( request.urlBase() );
        entity.setDescricao( request.descricao() );
        entity.setConfiguracoes( request.configuracoes() );
    }
}
