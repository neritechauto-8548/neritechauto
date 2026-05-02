package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.DevolucaoProduto;
import com.neritech.saas.estoque.domain.enums.AcaoDevolucao;
import com.neritech.saas.estoque.domain.enums.CondicaoProduto;
import com.neritech.saas.estoque.domain.enums.MotivoDevolucao;
import com.neritech.saas.estoque.dto.DevolucaoProdutoRequest;
import com.neritech.saas.estoque.dto.DevolucaoProdutoResponse;
import com.neritech.saas.produtoServico.domain.Produto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:28-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class DevolucaoProdutoMapperImpl implements DevolucaoProdutoMapper {

    @Override
    public DevolucaoProduto toEntity(DevolucaoProdutoRequest request) {
        if ( request == null ) {
            return null;
        }

        DevolucaoProduto devolucaoProduto = new DevolucaoProduto();

        devolucaoProduto.setOrdemServicoId( request.ordemServicoId() );
        devolucaoProduto.setClienteId( request.clienteId() );
        devolucaoProduto.setQuantidadeDevolvida( request.quantidadeDevolvida() );
        devolucaoProduto.setMotivoDevolucao( request.motivoDevolucao() );
        devolucaoProduto.setDescricaoMotivo( request.descricaoMotivo() );
        devolucaoProduto.setCondicaoProduto( request.condicaoProduto() );
        devolucaoProduto.setAcaoTomada( request.acaoTomada() );
        devolucaoProduto.setValorDevolvido( request.valorDevolvido() );
        devolucaoProduto.setAprovadoPor( request.aprovadoPor() );
        devolucaoProduto.setObservacoes( request.observacoes() );
        devolucaoProduto.setFotosProdutoUrl( request.fotosProdutoUrl() );
        devolucaoProduto.setUsuarioResponsavel( request.usuarioResponsavel() );

        return devolucaoProduto;
    }

    @Override
    public DevolucaoProdutoResponse toResponse(DevolucaoProduto entity) {
        if ( entity == null ) {
            return null;
        }

        Long produtoId = null;
        String produtoNome = null;
        Long id = null;
        Long ordemServicoId = null;
        Long clienteId = null;
        BigDecimal quantidadeDevolvida = null;
        MotivoDevolucao motivoDevolucao = null;
        String descricaoMotivo = null;
        CondicaoProduto condicaoProduto = null;
        AcaoDevolucao acaoTomada = null;
        BigDecimal valorDevolvido = null;
        Long aprovadoPor = null;
        LocalDateTime dataAprovacao = null;
        String observacoes = null;
        String fotosProdutoUrl = null;
        LocalDateTime dataDevolucao = null;
        Long usuarioResponsavel = null;

        produtoId = entityProdutoId( entity );
        produtoNome = entityProdutoNome( entity );
        id = entity.getId();
        ordemServicoId = entity.getOrdemServicoId();
        clienteId = entity.getClienteId();
        quantidadeDevolvida = entity.getQuantidadeDevolvida();
        motivoDevolucao = entity.getMotivoDevolucao();
        descricaoMotivo = entity.getDescricaoMotivo();
        condicaoProduto = entity.getCondicaoProduto();
        acaoTomada = entity.getAcaoTomada();
        valorDevolvido = entity.getValorDevolvido();
        aprovadoPor = entity.getAprovadoPor();
        dataAprovacao = entity.getDataAprovacao();
        observacoes = entity.getObservacoes();
        fotosProdutoUrl = entity.getFotosProdutoUrl();
        dataDevolucao = entity.getDataDevolucao();
        usuarioResponsavel = entity.getUsuarioResponsavel();

        DevolucaoProdutoResponse devolucaoProdutoResponse = new DevolucaoProdutoResponse( id, produtoId, produtoNome, ordemServicoId, clienteId, quantidadeDevolvida, motivoDevolucao, descricaoMotivo, condicaoProduto, acaoTomada, valorDevolvido, aprovadoPor, dataAprovacao, observacoes, fotosProdutoUrl, dataDevolucao, usuarioResponsavel );

        return devolucaoProdutoResponse;
    }

    @Override
    public void updateEntityFromRequest(DevolucaoProdutoRequest request, DevolucaoProduto entity) {
        if ( request == null ) {
            return;
        }

        entity.setOrdemServicoId( request.ordemServicoId() );
        entity.setClienteId( request.clienteId() );
        entity.setQuantidadeDevolvida( request.quantidadeDevolvida() );
        entity.setMotivoDevolucao( request.motivoDevolucao() );
        entity.setDescricaoMotivo( request.descricaoMotivo() );
        entity.setCondicaoProduto( request.condicaoProduto() );
        entity.setAcaoTomada( request.acaoTomada() );
        entity.setValorDevolvido( request.valorDevolvido() );
        entity.setAprovadoPor( request.aprovadoPor() );
        entity.setObservacoes( request.observacoes() );
        entity.setFotosProdutoUrl( request.fotosProdutoUrl() );
        entity.setUsuarioResponsavel( request.usuarioResponsavel() );
    }

    private Long entityProdutoId(DevolucaoProduto devolucaoProduto) {
        if ( devolucaoProduto == null ) {
            return null;
        }
        Produto produto = devolucaoProduto.getProduto();
        if ( produto == null ) {
            return null;
        }
        Long id = produto.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityProdutoNome(DevolucaoProduto devolucaoProduto) {
        if ( devolucaoProduto == null ) {
            return null;
        }
        Produto produto = devolucaoProduto.getProduto();
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
