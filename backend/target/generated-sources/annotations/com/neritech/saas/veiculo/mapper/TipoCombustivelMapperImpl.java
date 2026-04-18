package com.neritech.saas.veiculo.mapper;

import com.neritech.saas.veiculo.domain.TipoCombustivel;
import com.neritech.saas.veiculo.dto.TipoCombustivelRequest;
import com.neritech.saas.veiculo.dto.TipoCombustivelResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:52-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class TipoCombustivelMapperImpl implements TipoCombustivelMapper {

    @Override
    public TipoCombustivel toEntity(TipoCombustivelRequest request) {
        if ( request == null ) {
            return null;
        }

        TipoCombustivel tipoCombustivel = new TipoCombustivel();

        tipoCombustivel.setNome( request.nome() );

        return tipoCombustivel;
    }

    @Override
    public TipoCombustivelResponse toResponse(TipoCombustivel entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String nome = null;

        id = entity.getId();
        nome = entity.getNome();

        TipoCombustivelResponse tipoCombustivelResponse = new TipoCombustivelResponse( id, nome );

        return tipoCombustivelResponse;
    }

    @Override
    public void updateEntityFromRequest(TipoCombustivelRequest request, TipoCombustivel entity) {
        if ( request == null ) {
            return;
        }

        entity.setNome( request.nome() );
    }
}
