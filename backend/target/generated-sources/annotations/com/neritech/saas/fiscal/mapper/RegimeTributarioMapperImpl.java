package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.RegimeTributario;
import com.neritech.saas.fiscal.dto.RegimeTributarioRequest;
import com.neritech.saas.fiscal.dto.RegimeTributarioResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:53-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class RegimeTributarioMapperImpl implements RegimeTributarioMapper {

    @Override
    public RegimeTributario toEntity(RegimeTributarioRequest request) {
        if ( request == null ) {
            return null;
        }

        RegimeTributario.RegimeTributarioBuilder<?, ?> regimeTributario = RegimeTributario.builder();

        regimeTributario.ativo( request.ativo() );
        regimeTributario.codigo( request.codigo() );
        regimeTributario.descricao( request.descricao() );
        regimeTributario.nome( request.nome() );

        return regimeTributario.build();
    }

    @Override
    public RegimeTributarioResponse toResponse(RegimeTributario entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String codigo = null;
        String nome = null;
        String descricao = null;
        boolean ativo = false;

        id = entity.getId();
        codigo = entity.getCodigo();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        ativo = entity.isAtivo();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        RegimeTributarioResponse regimeTributarioResponse = new RegimeTributarioResponse( id, codigo, nome, descricao, ativo, createdAt, updatedAt );

        return regimeTributarioResponse;
    }

    @Override
    public void updateEntityFromRequest(RegimeTributarioRequest request, RegimeTributario entity) {
        if ( request == null ) {
            return;
        }

        entity.setCodigo( request.codigo() );
        entity.setDescricao( request.descricao() );
        entity.setNome( request.nome() );
        entity.setAtivo( request.ativo() );
    }
}
