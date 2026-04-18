package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.NcmProduto;
import com.neritech.saas.fiscal.dto.NcmProdutoRequest;
import com.neritech.saas.fiscal.dto.NcmProdutoResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:05-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class NcmProdutoMapperImpl implements NcmProdutoMapper {

    @Override
    public NcmProduto toEntity(NcmProdutoRequest request) {
        if ( request == null ) {
            return null;
        }

        NcmProduto.NcmProdutoBuilder<?, ?> ncmProduto = NcmProduto.builder();

        ncmProduto.aliquotaIpi( request.aliquotaIpi() );
        ncmProduto.ativo( request.ativo() );
        ncmProduto.codigo( request.codigo() );
        ncmProduto.descricao( request.descricao() );

        return ncmProduto.build();
    }

    @Override
    public NcmProdutoResponse toResponse(NcmProduto entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String codigo = null;
        String descricao = null;
        Double aliquotaIpi = null;
        boolean ativo = false;

        id = entity.getId();
        codigo = entity.getCodigo();
        descricao = entity.getDescricao();
        aliquotaIpi = entity.getAliquotaIpi();
        ativo = entity.isAtivo();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        NcmProdutoResponse ncmProdutoResponse = new NcmProdutoResponse( id, codigo, descricao, aliquotaIpi, ativo, createdAt, updatedAt );

        return ncmProdutoResponse;
    }

    @Override
    public void updateEntityFromRequest(NcmProdutoRequest request, NcmProduto entity) {
        if ( request == null ) {
            return;
        }

        entity.setAtivo( request.ativo() );
        entity.setCodigo( request.codigo() );
        entity.setDescricao( request.descricao() );
        entity.setAliquotaIpi( request.aliquotaIpi() );
    }
}
