package com.neritech.saas.produtoServico.mapper;

import com.neritech.saas.produtoServico.domain.CategoriaProduto;
import com.neritech.saas.produtoServico.dto.CategoriaProdutoRequest;
import com.neritech.saas.produtoServico.dto.CategoriaProdutoResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:26:49-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CategoriaProdutoMapperImpl implements CategoriaProdutoMapper {

    @Override
    public CategoriaProduto toEntity(CategoriaProdutoRequest request) {
        if ( request == null ) {
            return null;
        }

        CategoriaProduto categoriaProduto = new CategoriaProduto();

        categoriaProduto.setNome( request.nome() );
        categoriaProduto.setAtivo( request.ativo() );

        return categoriaProduto;
    }

    @Override
    public CategoriaProdutoResponse toResponse(CategoriaProduto entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        Long id = null;
        String nome = null;
        Boolean ativo = null;

        empresaId = entity.getEmpresaId();
        id = entity.getId();
        nome = entity.getNome();
        ativo = entity.getAtivo();

        CategoriaProdutoResponse categoriaProdutoResponse = new CategoriaProdutoResponse( id, empresaId, nome, ativo );

        return categoriaProdutoResponse;
    }

    @Override
    public void updateEntityFromRequest(CategoriaProdutoRequest request, CategoriaProduto entity) {
        if ( request == null ) {
            return;
        }

        entity.setNome( request.nome() );
        entity.setAtivo( request.ativo() );
    }
}
