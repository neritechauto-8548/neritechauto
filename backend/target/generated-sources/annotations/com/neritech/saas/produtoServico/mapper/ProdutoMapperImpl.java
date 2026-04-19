package com.neritech.saas.produtoServico.mapper;

import com.neritech.saas.produtoServico.domain.CategoriaProduto;
import com.neritech.saas.produtoServico.domain.Produto;
import com.neritech.saas.produtoServico.domain.UnidadeMedida;
import com.neritech.saas.produtoServico.dto.ProdutoRequest;
import com.neritech.saas.produtoServico.dto.ProdutoResponse;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:46-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ProdutoMapperImpl implements ProdutoMapper {

    @Override
    public Produto toEntity(ProdutoRequest request) {
        if ( request == null ) {
            return null;
        }

        Produto produto = new Produto();

        produto.setCodigoInterno( request.codigoInterno() );
        produto.setCodigoBarras( request.codigoBarras() );
        produto.setCodigoFabricante( request.codigoFabricante() );
        produto.setNome( request.nome() );
        produto.setDescricao( request.descricao() );
        produto.setDescricaoDetalhada( request.descricaoDetalhada() );
        produto.setMarca( request.marca() );
        produto.setModelo( request.modelo() );
        produto.setAplicacao( request.aplicacao() );
        produto.setEspecificacoesTecnicas( request.especificacoesTecnicas() );
        produto.setPesoLiquido( request.pesoLiquido() );
        produto.setPesoBruto( request.pesoBruto() );
        produto.setDimensoesComprimento( request.dimensoesComprimento() );
        produto.setDimensoesLargura( request.dimensoesLargura() );
        produto.setDimensoesAltura( request.dimensoesAltura() );
        produto.setPrecoCusto( request.precoCusto() );
        produto.setPrecoVenda( request.precoVenda() );
        produto.setMargemLucroPercentual( request.margemLucroPercentual() );
        produto.setEstoqueMinimo( request.estoqueMinimo() );
        produto.setEstoqueMaximo( request.estoqueMaximo() );
        produto.setPontoReposicao( request.pontoReposicao() );
        produto.setControlaLote( request.controlaLote() );
        produto.setControlaValidade( request.controlaValidade() );
        produto.setDiasValidade( request.diasValidade() );
        produto.setNcm( request.ncm() );
        produto.setCest( request.cest() );
        produto.setOrigemProduto( request.origemProduto() );
        produto.setFotoPrincipalUrl( request.fotoPrincipalUrl() );
        produto.setGarantiaMeses( request.garantiaMeses() );
        produto.setObservacoes( request.observacoes() );
        produto.setAtivo( request.ativo() );
        produto.setDestaque( request.destaque() );
        produto.setPromocional( request.promocional() );
        produto.setPontosFidelidade( request.pontosFidelidade() );
        produto.setComissaoVendaPercentual( request.comissaoVendaPercentual() );

        return produto;
    }

    @Override
    public ProdutoResponse toResponse(Produto entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        Long categoriaId = null;
        String categoriaNome = null;
        Long unidadeMedidaId = null;
        String unidadeMedidaSigla = null;
        BigDecimal quantidadeEstoque = null;
        Long id = null;
        String codigoInterno = null;
        String codigoBarras = null;
        String codigoFabricante = null;
        String nome = null;
        String descricao = null;
        String descricaoDetalhada = null;
        String marca = null;
        String modelo = null;
        String aplicacao = null;
        String especificacoesTecnicas = null;
        BigDecimal pesoLiquido = null;
        BigDecimal pesoBruto = null;
        BigDecimal dimensoesComprimento = null;
        BigDecimal dimensoesLargura = null;
        BigDecimal dimensoesAltura = null;
        BigDecimal precoCusto = null;
        BigDecimal precoVenda = null;
        BigDecimal margemLucroPercentual = null;
        BigDecimal estoqueMinimo = null;
        BigDecimal estoqueMaximo = null;
        BigDecimal pontoReposicao = null;
        Boolean controlaLote = null;
        Boolean controlaValidade = null;
        Integer diasValidade = null;
        String ncm = null;
        String cest = null;
        String origemProduto = null;
        String fotoPrincipalUrl = null;
        Integer garantiaMeses = null;
        String observacoes = null;
        Boolean ativo = null;
        Boolean destaque = null;
        Boolean promocional = null;
        Integer pontosFidelidade = null;
        BigDecimal comissaoVendaPercentual = null;

        empresaId = entity.getEmpresaId();
        categoriaId = entityCategoriaId( entity );
        categoriaNome = entityCategoriaNome( entity );
        unidadeMedidaId = entityUnidadeMedidaId( entity );
        unidadeMedidaSigla = entityUnidadeMedidaSigla( entity );
        quantidadeEstoque = entity.getQuantidadeEstoque();
        id = entity.getId();
        codigoInterno = entity.getCodigoInterno();
        codigoBarras = entity.getCodigoBarras();
        codigoFabricante = entity.getCodigoFabricante();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        descricaoDetalhada = entity.getDescricaoDetalhada();
        marca = entity.getMarca();
        modelo = entity.getModelo();
        aplicacao = entity.getAplicacao();
        especificacoesTecnicas = entity.getEspecificacoesTecnicas();
        pesoLiquido = entity.getPesoLiquido();
        pesoBruto = entity.getPesoBruto();
        dimensoesComprimento = entity.getDimensoesComprimento();
        dimensoesLargura = entity.getDimensoesLargura();
        dimensoesAltura = entity.getDimensoesAltura();
        precoCusto = entity.getPrecoCusto();
        precoVenda = entity.getPrecoVenda();
        margemLucroPercentual = entity.getMargemLucroPercentual();
        estoqueMinimo = entity.getEstoqueMinimo();
        estoqueMaximo = entity.getEstoqueMaximo();
        pontoReposicao = entity.getPontoReposicao();
        controlaLote = entity.getControlaLote();
        controlaValidade = entity.getControlaValidade();
        diasValidade = entity.getDiasValidade();
        ncm = entity.getNcm();
        cest = entity.getCest();
        origemProduto = entity.getOrigemProduto();
        fotoPrincipalUrl = entity.getFotoPrincipalUrl();
        garantiaMeses = entity.getGarantiaMeses();
        observacoes = entity.getObservacoes();
        ativo = entity.getAtivo();
        destaque = entity.getDestaque();
        promocional = entity.getPromocional();
        pontosFidelidade = entity.getPontosFidelidade();
        comissaoVendaPercentual = entity.getComissaoVendaPercentual();

        ProdutoResponse produtoResponse = new ProdutoResponse( id, empresaId, categoriaId, categoriaNome, unidadeMedidaId, unidadeMedidaSigla, codigoInterno, codigoBarras, codigoFabricante, nome, descricao, descricaoDetalhada, marca, modelo, aplicacao, especificacoesTecnicas, pesoLiquido, pesoBruto, dimensoesComprimento, dimensoesLargura, dimensoesAltura, precoCusto, precoVenda, margemLucroPercentual, estoqueMinimo, estoqueMaximo, pontoReposicao, controlaLote, controlaValidade, diasValidade, ncm, cest, origemProduto, fotoPrincipalUrl, garantiaMeses, observacoes, ativo, destaque, promocional, pontosFidelidade, comissaoVendaPercentual, quantidadeEstoque );

        return produtoResponse;
    }

    @Override
    public void updateEntityFromRequest(ProdutoRequest request, Produto entity) {
        if ( request == null ) {
            return;
        }

        entity.setCodigoInterno( request.codigoInterno() );
        entity.setCodigoBarras( request.codigoBarras() );
        entity.setCodigoFabricante( request.codigoFabricante() );
        entity.setNome( request.nome() );
        entity.setDescricao( request.descricao() );
        entity.setDescricaoDetalhada( request.descricaoDetalhada() );
        entity.setMarca( request.marca() );
        entity.setModelo( request.modelo() );
        entity.setAplicacao( request.aplicacao() );
        entity.setEspecificacoesTecnicas( request.especificacoesTecnicas() );
        entity.setPesoLiquido( request.pesoLiquido() );
        entity.setPesoBruto( request.pesoBruto() );
        entity.setDimensoesComprimento( request.dimensoesComprimento() );
        entity.setDimensoesLargura( request.dimensoesLargura() );
        entity.setDimensoesAltura( request.dimensoesAltura() );
        entity.setPrecoCusto( request.precoCusto() );
        entity.setPrecoVenda( request.precoVenda() );
        entity.setMargemLucroPercentual( request.margemLucroPercentual() );
        entity.setEstoqueMinimo( request.estoqueMinimo() );
        entity.setEstoqueMaximo( request.estoqueMaximo() );
        entity.setPontoReposicao( request.pontoReposicao() );
        entity.setControlaLote( request.controlaLote() );
        entity.setControlaValidade( request.controlaValidade() );
        entity.setDiasValidade( request.diasValidade() );
        entity.setNcm( request.ncm() );
        entity.setCest( request.cest() );
        entity.setOrigemProduto( request.origemProduto() );
        entity.setFotoPrincipalUrl( request.fotoPrincipalUrl() );
        entity.setGarantiaMeses( request.garantiaMeses() );
        entity.setObservacoes( request.observacoes() );
        entity.setAtivo( request.ativo() );
        entity.setDestaque( request.destaque() );
        entity.setPromocional( request.promocional() );
        entity.setPontosFidelidade( request.pontosFidelidade() );
        entity.setComissaoVendaPercentual( request.comissaoVendaPercentual() );
    }

    private Long entityCategoriaId(Produto produto) {
        if ( produto == null ) {
            return null;
        }
        CategoriaProduto categoria = produto.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        Long id = categoria.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityCategoriaNome(Produto produto) {
        if ( produto == null ) {
            return null;
        }
        CategoriaProduto categoria = produto.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        String nome = categoria.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private Long entityUnidadeMedidaId(Produto produto) {
        if ( produto == null ) {
            return null;
        }
        UnidadeMedida unidadeMedida = produto.getUnidadeMedida();
        if ( unidadeMedida == null ) {
            return null;
        }
        Long id = unidadeMedida.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityUnidadeMedidaSigla(Produto produto) {
        if ( produto == null ) {
            return null;
        }
        UnidadeMedida unidadeMedida = produto.getUnidadeMedida();
        if ( unidadeMedida == null ) {
            return null;
        }
        String sigla = unidadeMedida.getSigla();
        if ( sigla == null ) {
            return null;
        }
        return sigla;
    }
}
