package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.TokenApi;
import com.neritech.saas.integracao.dto.IntegracaoAtivaResponse;
import com.neritech.saas.integracao.dto.TokenApiRequest;
import com.neritech.saas.integracao.dto.TokenApiResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:31-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TokenApiMapperImpl implements TokenApiMapper {

    @Autowired
    private IntegracaoAtivaMapper integracaoAtivaMapper;

    @Override
    public TokenApi toEntity(TokenApiRequest request) {
        if ( request == null ) {
            return null;
        }

        TokenApi.TokenApiBuilder<?, ?> tokenApi = TokenApi.builder();

        tokenApi.ativo( request.ativo() );
        tokenApi.dataExpiracao( request.dataExpiracao() );
        tokenApi.escopos( request.escopos() );
        tokenApi.nome( request.nome() );
        tokenApi.tipo( request.tipo() );
        tokenApi.token( request.token() );

        return tokenApi.build();
    }

    @Override
    public TokenApiResponse toResponse(TokenApi entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        IntegracaoAtivaResponse integracao = null;
        String nome = null;
        String tipo = null;
        LocalDateTime dataExpiracao = null;
        boolean ativo = false;
        String escopos = null;

        id = entity.getId();
        integracao = integracaoAtivaMapper.toResponse( entity.getIntegracao() );
        nome = entity.getNome();
        tipo = entity.getTipo();
        dataExpiracao = entity.getDataExpiracao();
        ativo = entity.isAtivo();
        escopos = entity.getEscopos();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        TokenApiResponse tokenApiResponse = new TokenApiResponse( id, integracao, nome, tipo, dataExpiracao, ativo, escopos, createdAt, updatedAt );

        return tokenApiResponse;
    }

    @Override
    public void updateEntityFromRequest(TokenApiRequest request, TokenApi entity) {
        if ( request == null ) {
            return;
        }

        entity.setNome( request.nome() );
        entity.setToken( request.token() );
        entity.setDataExpiracao( request.dataExpiracao() );
        entity.setEscopos( request.escopos() );
        entity.setTipo( request.tipo() );
        entity.setAtivo( request.ativo() );
    }
}
