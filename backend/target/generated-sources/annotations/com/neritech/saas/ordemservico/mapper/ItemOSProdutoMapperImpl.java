package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.ItemOSProduto;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.ItemOSProdutoRequest;
import com.neritech.saas.ordemservico.dto.ItemOSProdutoResponse;
import com.neritech.saas.produtoServico.domain.Fornecedor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:58-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ItemOSProdutoMapperImpl implements ItemOSProdutoMapper {

    @Override
    public ItemOSProduto toEntity(ItemOSProdutoRequest request) {
        if ( request == null ) {
            return null;
        }

        ItemOSProduto itemOSProduto = new ItemOSProduto();

        itemOSProduto.setProdutoId( request.produtoId() );
        itemOSProduto.setDescricao( request.descricao() );
        itemOSProduto.setQuantidade( request.quantidade() );
        itemOSProduto.setValorUnitario( request.valorUnitario() );
        itemOSProduto.setValorTotal( request.valorTotal() );
        itemOSProduto.setDescontoPercentual( request.descontoPercentual() );
        itemOSProduto.setDescontoValor( request.descontoValor() );
        itemOSProduto.setValorFinal( request.valorFinal() );
        itemOSProduto.setLoteNumero( request.loteNumero() );
        itemOSProduto.setLocalizacaoEstoqueId( request.localizacaoEstoqueId() );
        itemOSProduto.setPrecoCusto( request.precoCusto() );
        itemOSProduto.setGarantiaMeses( request.garantiaMeses() );
        itemOSProduto.setNumeroSerie( request.numeroSerie() );
        itemOSProduto.setObservacoes( request.observacoes() );
        itemOSProduto.setAprovadoCliente( request.aprovadoCliente() );

        return itemOSProduto;
    }

    @Override
    public ItemOSProdutoResponse toResponse(ItemOSProduto entity) {
        if ( entity == null ) {
            return null;
        }

        Long ordemServicoId = null;
        Long fornecedorId = null;
        Long id = null;
        Long produtoId = null;
        String descricao = null;
        BigDecimal quantidade = null;
        BigDecimal valorUnitario = null;
        BigDecimal valorTotal = null;
        BigDecimal descontoPercentual = null;
        BigDecimal descontoValor = null;
        BigDecimal valorFinal = null;
        String loteNumero = null;
        Long localizacaoEstoqueId = null;
        BigDecimal quantidadeReservada = null;
        BigDecimal quantidadeUtilizada = null;
        LocalDateTime dataReserva = null;
        LocalDateTime dataUtilizacao = null;
        BigDecimal precoCusto = null;
        BigDecimal margemLucroRealizada = null;
        Integer garantiaMeses = null;
        String numeroSerie = null;
        String observacoes = null;
        Boolean aprovadoCliente = null;
        LocalDateTime dataAprovacaoCliente = null;
        Boolean devolvido = null;
        BigDecimal quantidadeDevolvida = null;
        String motivoDevolucao = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;
        Integer versao = null;

        ordemServicoId = entityOrdemServicoId( entity );
        fornecedorId = entityFornecedorId( entity );
        id = entity.getId();
        produtoId = entity.getProdutoId();
        descricao = entity.getDescricao();
        quantidade = entity.getQuantidade();
        valorUnitario = entity.getValorUnitario();
        valorTotal = entity.getValorTotal();
        descontoPercentual = entity.getDescontoPercentual();
        descontoValor = entity.getDescontoValor();
        valorFinal = entity.getValorFinal();
        loteNumero = entity.getLoteNumero();
        localizacaoEstoqueId = entity.getLocalizacaoEstoqueId();
        quantidadeReservada = entity.getQuantidadeReservada();
        quantidadeUtilizada = entity.getQuantidadeUtilizada();
        dataReserva = entity.getDataReserva();
        dataUtilizacao = entity.getDataUtilizacao();
        precoCusto = entity.getPrecoCusto();
        margemLucroRealizada = entity.getMargemLucroRealizada();
        garantiaMeses = entity.getGarantiaMeses();
        numeroSerie = entity.getNumeroSerie();
        observacoes = entity.getObservacoes();
        aprovadoCliente = entity.getAprovadoCliente();
        dataAprovacaoCliente = entity.getDataAprovacaoCliente();
        devolvido = entity.getDevolvido();
        quantidadeDevolvida = entity.getQuantidadeDevolvida();
        motivoDevolucao = entity.getMotivoDevolucao();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();
        versao = entity.getVersao();

        ItemOSProdutoResponse itemOSProdutoResponse = new ItemOSProdutoResponse( id, ordemServicoId, produtoId, descricao, quantidade, valorUnitario, valorTotal, descontoPercentual, descontoValor, valorFinal, loteNumero, localizacaoEstoqueId, quantidadeReservada, quantidadeUtilizada, dataReserva, dataUtilizacao, fornecedorId, precoCusto, margemLucroRealizada, garantiaMeses, numeroSerie, observacoes, aprovadoCliente, dataAprovacaoCliente, devolvido, quantidadeDevolvida, motivoDevolucao, dataCadastro, dataAtualizacao, versao );

        return itemOSProdutoResponse;
    }

    @Override
    public void updateEntityFromRequest(ItemOSProdutoRequest request, ItemOSProduto entity) {
        if ( request == null ) {
            return;
        }

        if ( request.produtoId() != null ) {
            entity.setProdutoId( request.produtoId() );
        }
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.quantidade() != null ) {
            entity.setQuantidade( request.quantidade() );
        }
        if ( request.valorUnitario() != null ) {
            entity.setValorUnitario( request.valorUnitario() );
        }
        if ( request.valorTotal() != null ) {
            entity.setValorTotal( request.valorTotal() );
        }
        if ( request.descontoPercentual() != null ) {
            entity.setDescontoPercentual( request.descontoPercentual() );
        }
        if ( request.descontoValor() != null ) {
            entity.setDescontoValor( request.descontoValor() );
        }
        if ( request.valorFinal() != null ) {
            entity.setValorFinal( request.valorFinal() );
        }
        if ( request.loteNumero() != null ) {
            entity.setLoteNumero( request.loteNumero() );
        }
        if ( request.localizacaoEstoqueId() != null ) {
            entity.setLocalizacaoEstoqueId( request.localizacaoEstoqueId() );
        }
        if ( request.precoCusto() != null ) {
            entity.setPrecoCusto( request.precoCusto() );
        }
        if ( request.garantiaMeses() != null ) {
            entity.setGarantiaMeses( request.garantiaMeses() );
        }
        if ( request.numeroSerie() != null ) {
            entity.setNumeroSerie( request.numeroSerie() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.aprovadoCliente() != null ) {
            entity.setAprovadoCliente( request.aprovadoCliente() );
        }
    }

    private Long entityOrdemServicoId(ItemOSProduto itemOSProduto) {
        if ( itemOSProduto == null ) {
            return null;
        }
        OrdemServico ordemServico = itemOSProduto.getOrdemServico();
        if ( ordemServico == null ) {
            return null;
        }
        Long id = ordemServico.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityFornecedorId(ItemOSProduto itemOSProduto) {
        if ( itemOSProduto == null ) {
            return null;
        }
        Fornecedor fornecedor = itemOSProduto.getFornecedor();
        if ( fornecedor == null ) {
            return null;
        }
        Long id = fornecedor.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
