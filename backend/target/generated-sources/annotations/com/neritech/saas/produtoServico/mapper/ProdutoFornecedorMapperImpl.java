package com.neritech.saas.produtoServico.mapper;

import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.domain.ProdutoFornecedor;
import com.neritech.saas.produtoServico.dto.ProdutoFornecedorRequest;
import com.neritech.saas.produtoServico.dto.ProdutoFornecedorResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:49-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ProdutoFornecedorMapperImpl implements ProdutoFornecedorMapper {

    @Override
    public ProdutoFornecedor toEntity(ProdutoFornecedorRequest request) {
        if ( request == null ) {
            return null;
        }

        ProdutoFornecedor produtoFornecedor = new ProdutoFornecedor();

        produtoFornecedor.setCodigoFornecedor( request.codigoFornecedor() );
        produtoFornecedor.setDescricaoFornecedor( request.descricaoFornecedor() );
        produtoFornecedor.setPrecoCusto( request.precoCusto() );
        produtoFornecedor.setPrecoCustoAnterior( request.precoCustoAnterior() );
        produtoFornecedor.setDataUltimoPreco( request.dataUltimoPreco() );
        produtoFornecedor.setPrazoEntregaDias( request.prazoEntregaDias() );
        produtoFornecedor.setQuantidadeMinima( request.quantidadeMinima() );
        produtoFornecedor.setDescontoQuantidade( request.descontoQuantidade() );
        produtoFornecedor.setMoeda( request.moeda() );
        produtoFornecedor.setPrincipal( request.principal() );
        produtoFornecedor.setAtivo( request.ativo() );
        produtoFornecedor.setObservacoes( request.observacoes() );

        return produtoFornecedor;
    }

    @Override
    public ProdutoFornecedorResponse toResponse(ProdutoFornecedor entity) {
        if ( entity == null ) {
            return null;
        }

        Long produtoId = null;
        String produtoNome = null;
        Long fornecedorId = null;
        String fornecedorNome = null;
        Long id = null;
        String codigoFornecedor = null;
        String descricaoFornecedor = null;
        BigDecimal precoCusto = null;
        BigDecimal precoCustoAnterior = null;
        LocalDate dataUltimoPreco = null;
        Integer prazoEntregaDias = null;
        BigDecimal quantidadeMinima = null;
        String descontoQuantidade = null;
        String moeda = null;
        Boolean principal = null;
        Boolean ativo = null;
        String observacoes = null;

        produtoId = entityProdutoId( entity );
        produtoNome = entityProdutoNome( entity );
        fornecedorId = entityFornecedorId( entity );
        fornecedorNome = entityFornecedorNomeFantasia( entity );
        id = entity.getId();
        codigoFornecedor = entity.getCodigoFornecedor();
        descricaoFornecedor = entity.getDescricaoFornecedor();
        precoCusto = entity.getPrecoCusto();
        precoCustoAnterior = entity.getPrecoCustoAnterior();
        dataUltimoPreco = entity.getDataUltimoPreco();
        prazoEntregaDias = entity.getPrazoEntregaDias();
        quantidadeMinima = entity.getQuantidadeMinima();
        descontoQuantidade = entity.getDescontoQuantidade();
        moeda = entity.getMoeda();
        principal = entity.getPrincipal();
        ativo = entity.getAtivo();
        observacoes = entity.getObservacoes();

        ProdutoFornecedorResponse produtoFornecedorResponse = new ProdutoFornecedorResponse( id, produtoId, produtoNome, fornecedorId, fornecedorNome, codigoFornecedor, descricaoFornecedor, precoCusto, precoCustoAnterior, dataUltimoPreco, prazoEntregaDias, quantidadeMinima, descontoQuantidade, moeda, principal, ativo, observacoes );

        return produtoFornecedorResponse;
    }

    @Override
    public void updateEntityFromRequest(ProdutoFornecedorRequest request, ProdutoFornecedor entity) {
        if ( request == null ) {
            return;
        }

        entity.setCodigoFornecedor( request.codigoFornecedor() );
        entity.setDescricaoFornecedor( request.descricaoFornecedor() );
        entity.setPrecoCusto( request.precoCusto() );
        entity.setPrecoCustoAnterior( request.precoCustoAnterior() );
        entity.setDataUltimoPreco( request.dataUltimoPreco() );
        entity.setPrazoEntregaDias( request.prazoEntregaDias() );
        entity.setQuantidadeMinima( request.quantidadeMinima() );
        entity.setDescontoQuantidade( request.descontoQuantidade() );
        entity.setMoeda( request.moeda() );
        entity.setPrincipal( request.principal() );
        entity.setAtivo( request.ativo() );
        entity.setObservacoes( request.observacoes() );
    }

    private Long entityProdutoId(ProdutoFornecedor produtoFornecedor) {
        if ( produtoFornecedor == null ) {
            return null;
        }
        Produto produto = produtoFornecedor.getProduto();
        if ( produto == null ) {
            return null;
        }
        Long id = produto.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityProdutoNome(ProdutoFornecedor produtoFornecedor) {
        if ( produtoFornecedor == null ) {
            return null;
        }
        Produto produto = produtoFornecedor.getProduto();
        if ( produto == null ) {
            return null;
        }
        String nome = produto.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private Long entityFornecedorId(ProdutoFornecedor produtoFornecedor) {
        if ( produtoFornecedor == null ) {
            return null;
        }
        Fornecedor fornecedor = produtoFornecedor.getFornecedor();
        if ( fornecedor == null ) {
            return null;
        }
        Long id = fornecedor.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityFornecedorNomeFantasia(ProdutoFornecedor produtoFornecedor) {
        if ( produtoFornecedor == null ) {
            return null;
        }
        Fornecedor fornecedor = produtoFornecedor.getFornecedor();
        if ( fornecedor == null ) {
            return null;
        }
        String nomeFantasia = fornecedor.getNomeFantasia();
        if ( nomeFantasia == null ) {
            return null;
        }
        return nomeFantasia;
    }
}
