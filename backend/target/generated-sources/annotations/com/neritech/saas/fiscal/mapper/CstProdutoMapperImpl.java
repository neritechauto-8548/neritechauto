package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.CstProduto;
import com.neritech.saas.fiscal.dto.CstProdutoRequest;
import com.neritech.saas.fiscal.dto.CstProdutoResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:54:01-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class CstProdutoMapperImpl implements CstProdutoMapper {

    @Override
    public CstProduto toEntity(CstProdutoRequest request) {
        if ( request == null ) {
            return null;
        }

        CstProduto.CstProdutoBuilder<?, ?> cstProduto = CstProduto.builder();

        cstProduto.ativo( request.ativo() );
        cstProduto.codigo( request.codigo() );
        cstProduto.descricao( request.descricao() );
        cstProduto.tipo( request.tipo() );

        return cstProduto.build();
    }

    @Override
    public CstProdutoResponse toResponse(CstProduto entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String codigo = null;
        String descricao = null;
        String tipo = null;
        boolean ativo = false;

        id = entity.getId();
        codigo = entity.getCodigo();
        descricao = entity.getDescricao();
        tipo = entity.getTipo();
        ativo = entity.isAtivo();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        CstProdutoResponse cstProdutoResponse = new CstProdutoResponse( id, codigo, descricao, tipo, ativo, createdAt, updatedAt );

        return cstProdutoResponse;
    }

    @Override
    public void updateEntityFromRequest(CstProdutoRequest request, CstProduto entity) {
        if ( request == null ) {
            return;
        }

        entity.setAtivo( request.ativo() );
        entity.setCodigo( request.codigo() );
        entity.setDescricao( request.descricao() );
        entity.setTipo( request.tipo() );
    }
}
