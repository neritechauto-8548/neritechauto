package com.neritech.saas.estoque.mapper;

import com.neritech.saas.estoque.domain.Inventario;
import com.neritech.saas.estoque.domain.ItemInventario;
import com.neritech.saas.estoque.domain.LocalizacaoEstoque;
import com.neritech.saas.estoque.domain.enums.StatusItemInventario;
import com.neritech.saas.estoque.dto.ItemInventarioRequest;
import com.neritech.saas.estoque.dto.ItemInventarioResponse;
import com.neritech.saas.produtoServico.domain.Produto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:31-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ItemInventarioMapperImpl implements ItemInventarioMapper {

    @Override
    public ItemInventario toEntity(ItemInventarioRequest request) {
        if ( request == null ) {
            return null;
        }

        ItemInventario itemInventario = new ItemInventario();

        itemInventario.setLoteNumero( request.loteNumero() );
        itemInventario.setQuantidadeSistema( request.quantidadeSistema() );
        itemInventario.setQuantidadeContada( request.quantidadeContada() );
        itemInventario.setValorUnitario( request.valorUnitario() );
        itemInventario.setStatus( request.status() );
        itemInventario.setMotivoDiferenca( request.motivoDiferenca() );
        itemInventario.setObservacoes( request.observacoes() );
        itemInventario.setUsuarioContagem( request.usuarioContagem() );
        itemInventario.setUsuarioConferencia( request.usuarioConferencia() );
        itemInventario.setFotoComprovanteUrl( request.fotoComprovanteUrl() );

        return itemInventario;
    }

    @Override
    public ItemInventarioResponse toResponse(ItemInventario entity) {
        if ( entity == null ) {
            return null;
        }

        Long inventarioId = null;
        Long produtoId = null;
        String produtoNome = null;
        Long localizacaoId = null;
        String localizacaoNome = null;
        Long id = null;
        String loteNumero = null;
        BigDecimal quantidadeSistema = null;
        BigDecimal quantidadeContada = null;
        BigDecimal diferenca = null;
        BigDecimal valorUnitario = null;
        BigDecimal valorTotalSistema = null;
        BigDecimal valorTotalContado = null;
        BigDecimal diferencaValor = null;
        StatusItemInventario status = null;
        String motivoDiferenca = null;
        String observacoes = null;
        Long usuarioContagem = null;
        LocalDateTime dataContagem = null;
        Long usuarioConferencia = null;
        LocalDateTime dataConferencia = null;
        String fotoComprovanteUrl = null;

        inventarioId = entityInventarioId( entity );
        produtoId = entityProdutoId( entity );
        produtoNome = entityProdutoNome( entity );
        localizacaoId = entityLocalizacaoId( entity );
        localizacaoNome = entityLocalizacaoNome( entity );
        id = entity.getId();
        loteNumero = entity.getLoteNumero();
        quantidadeSistema = entity.getQuantidadeSistema();
        quantidadeContada = entity.getQuantidadeContada();
        diferenca = entity.getDiferenca();
        valorUnitario = entity.getValorUnitario();
        valorTotalSistema = entity.getValorTotalSistema();
        valorTotalContado = entity.getValorTotalContado();
        diferencaValor = entity.getDiferencaValor();
        status = entity.getStatus();
        motivoDiferenca = entity.getMotivoDiferenca();
        observacoes = entity.getObservacoes();
        usuarioContagem = entity.getUsuarioContagem();
        dataContagem = entity.getDataContagem();
        usuarioConferencia = entity.getUsuarioConferencia();
        dataConferencia = entity.getDataConferencia();
        fotoComprovanteUrl = entity.getFotoComprovanteUrl();

        ItemInventarioResponse itemInventarioResponse = new ItemInventarioResponse( id, inventarioId, produtoId, produtoNome, localizacaoId, localizacaoNome, loteNumero, quantidadeSistema, quantidadeContada, diferenca, valorUnitario, valorTotalSistema, valorTotalContado, diferencaValor, status, motivoDiferenca, observacoes, usuarioContagem, dataContagem, usuarioConferencia, dataConferencia, fotoComprovanteUrl );

        return itemInventarioResponse;
    }

    @Override
    public void updateEntityFromRequest(ItemInventarioRequest request, ItemInventario entity) {
        if ( request == null ) {
            return;
        }

        entity.setLoteNumero( request.loteNumero() );
        entity.setQuantidadeSistema( request.quantidadeSistema() );
        entity.setQuantidadeContada( request.quantidadeContada() );
        entity.setValorUnitario( request.valorUnitario() );
        entity.setStatus( request.status() );
        entity.setMotivoDiferenca( request.motivoDiferenca() );
        entity.setObservacoes( request.observacoes() );
        entity.setUsuarioContagem( request.usuarioContagem() );
        entity.setUsuarioConferencia( request.usuarioConferencia() );
        entity.setFotoComprovanteUrl( request.fotoComprovanteUrl() );
    }

    private Long entityInventarioId(ItemInventario itemInventario) {
        if ( itemInventario == null ) {
            return null;
        }
        Inventario inventario = itemInventario.getInventario();
        if ( inventario == null ) {
            return null;
        }
        Long id = inventario.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityProdutoId(ItemInventario itemInventario) {
        if ( itemInventario == null ) {
            return null;
        }
        Produto produto = itemInventario.getProduto();
        if ( produto == null ) {
            return null;
        }
        Long id = produto.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityProdutoNome(ItemInventario itemInventario) {
        if ( itemInventario == null ) {
            return null;
        }
        Produto produto = itemInventario.getProduto();
        if ( produto == null ) {
            return null;
        }
        String nome = produto.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private Long entityLocalizacaoId(ItemInventario itemInventario) {
        if ( itemInventario == null ) {
            return null;
        }
        LocalizacaoEstoque localizacao = itemInventario.getLocalizacao();
        if ( localizacao == null ) {
            return null;
        }
        Long id = localizacao.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityLocalizacaoNome(ItemInventario itemInventario) {
        if ( itemInventario == null ) {
            return null;
        }
        LocalizacaoEstoque localizacao = itemInventario.getLocalizacao();
        if ( localizacao == null ) {
            return null;
        }
        String nome = localizacao.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
