package com.neritech.saas.produtoServico.mapper;

import com.neritech.saas.produtoServico.domain.Promocao;
import com.neritech.saas.produtoServico.domain.enums.TipoPromocao;
import com.neritech.saas.produtoServico.dto.PromocaoRequest;
import com.neritech.saas.produtoServico.dto.PromocaoResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:27:02-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class PromocaoMapperImpl implements PromocaoMapper {

    @Override
    public Promocao toEntity(PromocaoRequest request) {
        if ( request == null ) {
            return null;
        }

        Promocao promocao = new Promocao();

        promocao.setNome( request.nome() );
        promocao.setDescricao( request.descricao() );
        promocao.setTipoPromocao( request.tipoPromocao() );
        promocao.setValorDesconto( request.valorDesconto() );
        promocao.setPercentualDesconto( request.percentualDesconto() );
        promocao.setQuantidadeLeve( request.quantidadeLeve() );
        promocao.setQuantidadePague( request.quantidadePague() );
        promocao.setValorMinimoPedido( request.valorMinimoPedido() );
        promocao.setDataInicio( request.dataInicio() );
        promocao.setDataFim( request.dataFim() );
        promocao.setLimiteUsoTotal( request.limiteUsoTotal() );
        promocao.setLimiteUsoCliente( request.limiteUsoCliente() );
        promocao.setCodigoCupom( request.codigoCupom() );
        promocao.setAplicacaoAutomatica( request.aplicacaoAutomatica() );
        promocao.setCategoriasAplicaveis( request.categoriasAplicaveis() );
        promocao.setProdutosAplicaveis( request.produtosAplicaveis() );
        promocao.setClientesAplicaveis( request.clientesAplicaveis() );
        promocao.setCanaisVenda( request.canaisVenda() );
        promocao.setObservacoes( request.observacoes() );
        promocao.setAtivo( request.ativo() );

        return promocao;
    }

    @Override
    public PromocaoResponse toResponse(Promocao entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        Long id = null;
        String nome = null;
        String descricao = null;
        TipoPromocao tipoPromocao = null;
        BigDecimal valorDesconto = null;
        BigDecimal percentualDesconto = null;
        Integer quantidadeLeve = null;
        Integer quantidadePague = null;
        BigDecimal valorMinimoPedido = null;
        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = null;
        Integer limiteUsoTotal = null;
        Integer limiteUsoCliente = null;
        Integer vezesUtilizada = null;
        String codigoCupom = null;
        Boolean aplicacaoAutomatica = null;
        String categoriasAplicaveis = null;
        String produtosAplicaveis = null;
        String clientesAplicaveis = null;
        String canaisVenda = null;
        String observacoes = null;
        Boolean ativo = null;

        empresaId = entity.getEmpresaId();
        id = entity.getId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        tipoPromocao = entity.getTipoPromocao();
        valorDesconto = entity.getValorDesconto();
        percentualDesconto = entity.getPercentualDesconto();
        quantidadeLeve = entity.getQuantidadeLeve();
        quantidadePague = entity.getQuantidadePague();
        valorMinimoPedido = entity.getValorMinimoPedido();
        dataInicio = entity.getDataInicio();
        dataFim = entity.getDataFim();
        limiteUsoTotal = entity.getLimiteUsoTotal();
        limiteUsoCliente = entity.getLimiteUsoCliente();
        vezesUtilizada = entity.getVezesUtilizada();
        codigoCupom = entity.getCodigoCupom();
        aplicacaoAutomatica = entity.getAplicacaoAutomatica();
        categoriasAplicaveis = entity.getCategoriasAplicaveis();
        produtosAplicaveis = entity.getProdutosAplicaveis();
        clientesAplicaveis = entity.getClientesAplicaveis();
        canaisVenda = entity.getCanaisVenda();
        observacoes = entity.getObservacoes();
        ativo = entity.getAtivo();

        PromocaoResponse promocaoResponse = new PromocaoResponse( id, empresaId, nome, descricao, tipoPromocao, valorDesconto, percentualDesconto, quantidadeLeve, quantidadePague, valorMinimoPedido, dataInicio, dataFim, limiteUsoTotal, limiteUsoCliente, vezesUtilizada, codigoCupom, aplicacaoAutomatica, categoriasAplicaveis, produtosAplicaveis, clientesAplicaveis, canaisVenda, observacoes, ativo );

        return promocaoResponse;
    }

    @Override
    public void updateEntityFromRequest(PromocaoRequest request, Promocao entity) {
        if ( request == null ) {
            return;
        }

        entity.setNome( request.nome() );
        entity.setDescricao( request.descricao() );
        entity.setTipoPromocao( request.tipoPromocao() );
        entity.setValorDesconto( request.valorDesconto() );
        entity.setPercentualDesconto( request.percentualDesconto() );
        entity.setQuantidadeLeve( request.quantidadeLeve() );
        entity.setQuantidadePague( request.quantidadePague() );
        entity.setValorMinimoPedido( request.valorMinimoPedido() );
        entity.setDataInicio( request.dataInicio() );
        entity.setDataFim( request.dataFim() );
        entity.setLimiteUsoTotal( request.limiteUsoTotal() );
        entity.setLimiteUsoCliente( request.limiteUsoCliente() );
        entity.setCodigoCupom( request.codigoCupom() );
        entity.setAplicacaoAutomatica( request.aplicacaoAutomatica() );
        entity.setCategoriasAplicaveis( request.categoriasAplicaveis() );
        entity.setProdutosAplicaveis( request.produtosAplicaveis() );
        entity.setClientesAplicaveis( request.clientesAplicaveis() );
        entity.setCanaisVenda( request.canaisVenda() );
        entity.setObservacoes( request.observacoes() );
        entity.setAtivo( request.ativo() );
    }
}
