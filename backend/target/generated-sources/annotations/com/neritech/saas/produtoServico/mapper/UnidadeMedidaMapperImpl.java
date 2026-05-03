package com.neritech.saas.produtoServico.mapper;

import com.neritech.saas.produtoServico.domain.UnidadeMedida;
import com.neritech.saas.produtoServico.dto.UnidadeMedidaRequest;
import com.neritech.saas.produtoServico.dto.UnidadeMedidaResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:26:57-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UnidadeMedidaMapperImpl implements UnidadeMedidaMapper {

    @Override
    public UnidadeMedida toEntity(UnidadeMedidaRequest request) {
        if ( request == null ) {
            return null;
        }

        UnidadeMedida.UnidadeMedidaBuilder<?, ?> unidadeMedida = UnidadeMedida.builder();

        unidadeMedida.ativo( request.ativo() );
        unidadeMedida.nome( request.nome() );
        unidadeMedida.sigla( request.sigla() );

        return unidadeMedida.build();
    }

    @Override
    public UnidadeMedidaResponse toResponse(UnidadeMedida entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        String sigla = null;
        Boolean ativo = null;

        id = entity.getId();
        nome = entity.getNome();
        sigla = entity.getSigla();
        ativo = entity.getAtivo();

        UnidadeMedidaResponse unidadeMedidaResponse = new UnidadeMedidaResponse( id, nome, sigla, ativo );

        return unidadeMedidaResponse;
    }

    @Override
    public void updateEntityFromRequest(UnidadeMedidaRequest request, UnidadeMedida entity) {
        if ( request == null ) {
            return;
        }

        entity.setAtivo( request.ativo() );
        entity.setNome( request.nome() );
        entity.setSigla( request.sigla() );
    }
}
