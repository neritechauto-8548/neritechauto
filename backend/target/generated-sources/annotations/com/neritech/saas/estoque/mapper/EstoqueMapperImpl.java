package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.Estoque;
import com.neritech.saas.estoque.domain.enums.StatusEstoque;
import com.neritech.saas.estoque.dto.EstoqueRequest;
import com.neritech.saas.estoque.dto.EstoqueResponse;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import com.neritech.saas.produtoServico.domain.Produto;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:35-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class EstoqueMapperImpl implements EstoqueMapper {

    @Override
    public Estoque toEntity(EstoqueRequest request) {
        if ( request == null ) {
            return null;
        }

        Estoque estoque = new Estoque();

        estoque.setEmpresaId( request.empresaId() );
        estoque.setQuantidadeAtual( request.quantidadeAtual() );
        estoque.setPrecoCustoLote( request.precoCustoLote() );
        estoque.setNotaFiscalNumero( request.notaFiscalNumero() );
        estoque.setCertificadoQualidadeUrl( request.certificadoQualidadeUrl() );
        estoque.setStatus( request.status() );
        estoque.setMotivoBloqueio( request.motivoBloqueio() );
        estoque.setObservacoes( request.observacoes() );
        estoque.setUsuarioCadastro( request.usuarioCadastro() );

        return estoque;
    }

    @Override
    public EstoqueResponse toResponse(Estoque entity) {
        if ( entity == null ) {
            return null;
        }

        Long produtoId = null;
        String produtoNome = null;
        Long fornecedorId = null;
        String fornecedorNome = null;
        Long id = null;
        Long empresaId = null;
        BigDecimal quantidadeAtual = null;
        BigDecimal precoCustoLote = null;
        String notaFiscalNumero = null;
        String certificadoQualidadeUrl = null;
        StatusEstoque status = null;
        String motivoBloqueio = null;
        String observacoes = null;
        Long usuarioCadastro = null;

        produtoId = entityProdutoId( entity );
        produtoNome = entityProdutoNome( entity );
        fornecedorId = entityFornecedorId( entity );
        fornecedorNome = entityFornecedorNomeFantasia( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        quantidadeAtual = entity.getQuantidadeAtual();
        precoCustoLote = entity.getPrecoCustoLote();
        notaFiscalNumero = entity.getNotaFiscalNumero();
        certificadoQualidadeUrl = entity.getCertificadoQualidadeUrl();
        status = entity.getStatus();
        motivoBloqueio = entity.getMotivoBloqueio();
        observacoes = entity.getObservacoes();
        usuarioCadastro = entity.getUsuarioCadastro();

        EstoqueResponse estoqueResponse = new EstoqueResponse( id, empresaId, produtoId, produtoNome, quantidadeAtual, fornecedorId, fornecedorNome, precoCustoLote, notaFiscalNumero, certificadoQualidadeUrl, status, motivoBloqueio, observacoes, usuarioCadastro );

        return estoqueResponse;
    }

    @Override
    public void updateEntityFromRequest(EstoqueRequest request, Estoque entity) {
        if ( request == null ) {
            return;
        }

        entity.setEmpresaId( request.empresaId() );
        entity.setQuantidadeAtual( request.quantidadeAtual() );
        entity.setPrecoCustoLote( request.precoCustoLote() );
        entity.setNotaFiscalNumero( request.notaFiscalNumero() );
        entity.setCertificadoQualidadeUrl( request.certificadoQualidadeUrl() );
        entity.setStatus( request.status() );
        entity.setMotivoBloqueio( request.motivoBloqueio() );
        entity.setObservacoes( request.observacoes() );
        entity.setUsuarioCadastro( request.usuarioCadastro() );
    }

    private Long entityProdutoId(Estoque estoque) {
        if ( estoque == null ) {
            return null;
        }
        Produto produto = estoque.getProduto();
        if ( produto == null ) {
            return null;
        }
        Long id = produto.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityProdutoNome(Estoque estoque) {
        if ( estoque == null ) {
            return null;
        }
        Produto produto = estoque.getProduto();
        if ( produto == null ) {
            return null;
        }
        String nome = produto.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private Long entityFornecedorId(Estoque estoque) {
        if ( estoque == null ) {
            return null;
        }
        Fornecedor fornecedor = estoque.getFornecedor();
        if ( fornecedor == null ) {
            return null;
        }
        Long id = fornecedor.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityFornecedorNomeFantasia(Estoque estoque) {
        if ( estoque == null ) {
            return null;
        }
        Fornecedor fornecedor = estoque.getFornecedor();
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
