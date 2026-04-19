package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.LoteProduto;
import com.neritech.saas.estoque.dto.LoteProdutoRequest;
import com.neritech.saas.estoque.dto.LoteProdutoResponse;
import com.neritech.saas.produtoServico.domain.Produto;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T16:16:42-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class LoteProdutoMapperImpl implements LoteProdutoMapper {

    @Override
    public LoteProduto toEntity(LoteProdutoRequest request) {
        if ( request == null ) {
            return null;
        }

        LoteProduto loteProduto = new LoteProduto();

        loteProduto.setNumeroLote( request.numeroLote() );
        loteProduto.setDataFabricacao( request.dataFabricacao() );
        loteProduto.setDataValidade( request.dataValidade() );
        loteProduto.setQuantidadeInicial( request.quantidadeInicial() );

        return loteProduto;
    }

    @Override
    public LoteProdutoResponse toResponse(LoteProduto entity) {
        if ( entity == null ) {
            return null;
        }

        Long produtoId = null;
        String produtoNome = null;
        Long id = null;
        String numeroLote = null;
        LocalDate dataFabricacao = null;
        LocalDate dataValidade = null;
        BigDecimal quantidadeInicial = null;

        produtoId = entityProdutoId( entity );
        produtoNome = entityProdutoNome( entity );
        id = entity.getId();
        numeroLote = entity.getNumeroLote();
        dataFabricacao = entity.getDataFabricacao();
        dataValidade = entity.getDataValidade();
        quantidadeInicial = entity.getQuantidadeInicial();

        LoteProdutoResponse loteProdutoResponse = new LoteProdutoResponse( id, produtoId, produtoNome, numeroLote, dataFabricacao, dataValidade, quantidadeInicial );

        return loteProdutoResponse;
    }

    @Override
    public void updateEntityFromRequest(LoteProdutoRequest request, LoteProduto entity) {
        if ( request == null ) {
            return;
        }

        entity.setNumeroLote( request.numeroLote() );
        entity.setDataFabricacao( request.dataFabricacao() );
        entity.setDataValidade( request.dataValidade() );
        entity.setQuantidadeInicial( request.quantidadeInicial() );
    }

    private Long entityProdutoId(LoteProduto loteProduto) {
        if ( loteProduto == null ) {
            return null;
        }
        Produto produto = loteProduto.getProduto();
        if ( produto == null ) {
            return null;
        }
        Long id = produto.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityProdutoNome(LoteProduto loteProduto) {
        if ( loteProduto == null ) {
            return null;
        }
        Produto produto = loteProduto.getProduto();
        if ( produto == null ) {
            return null;
        }
        String nome = produto.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
