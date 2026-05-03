package com.neritech.saas.garantia.mapper;

import com.neritech.saas.garantia.domain.ItemGarantia;
import com.neritech.saas.garantia.domain.TipoItemGarantia;
import com.neritech.saas.garantia.dto.ItemGarantiaRequest;
import com.neritech.saas.garantia.dto.ItemGarantiaResponse;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:26:40-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ItemGarantiaMapperImpl implements ItemGarantiaMapper {

    @Override
    public ItemGarantia toEntity(ItemGarantiaRequest request) {
        if ( request == null ) {
            return null;
        }

        ItemGarantia itemGarantia = new ItemGarantia();

        itemGarantia.setAtivo( request.ativo() );
        itemGarantia.setPercentualCobertura( request.percentualCobertura() );
        itemGarantia.setTipoItem( request.tipoItem() );
        itemGarantia.setServicoId( request.servicoId() );
        itemGarantia.setProdutoId( request.produtoId() );
        itemGarantia.setDescricaoItem( request.descricaoItem() );
        itemGarantia.setQuantidadeOriginal( request.quantidadeOriginal() );
        itemGarantia.setValorUnitarioOriginal( request.valorUnitarioOriginal() );
        itemGarantia.setValorTotalOriginal( request.valorTotalOriginal() );
        itemGarantia.setValorCobertura( request.valorCobertura() );
        itemGarantia.setCondicoesEspecificas( request.condicoesEspecificas() );
        itemGarantia.setDefeitoCoberto( request.defeitoCoberto() );
        itemGarantia.setDefeitoNaoCoberto( request.defeitoNaoCoberto() );
        itemGarantia.setPrazoAcionamentoDias( request.prazoAcionamentoDias() );

        return itemGarantia;
    }

    @Override
    public ItemGarantiaResponse toResponse(ItemGarantia entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        TipoItemGarantia tipoItem = null;
        Long servicoId = null;
        Long produtoId = null;
        String descricaoItem = null;
        BigDecimal quantidadeOriginal = null;
        BigDecimal valorUnitarioOriginal = null;
        BigDecimal valorTotalOriginal = null;
        BigDecimal percentualCobertura = null;
        BigDecimal valorCobertura = null;
        String condicoesEspecificas = null;
        String defeitoCoberto = null;
        String defeitoNaoCoberto = null;
        Integer prazoAcionamentoDias = null;
        BigDecimal quantidadeUtilizada = null;
        BigDecimal valorUtilizado = null;
        BigDecimal saldoDisponivel = null;
        String observacoes = null;
        Boolean ativo = null;

        id = entity.getId();
        tipoItem = entity.getTipoItem();
        servicoId = entity.getServicoId();
        produtoId = entity.getProdutoId();
        descricaoItem = entity.getDescricaoItem();
        quantidadeOriginal = entity.getQuantidadeOriginal();
        valorUnitarioOriginal = entity.getValorUnitarioOriginal();
        valorTotalOriginal = entity.getValorTotalOriginal();
        percentualCobertura = entity.getPercentualCobertura();
        valorCobertura = entity.getValorCobertura();
        condicoesEspecificas = entity.getCondicoesEspecificas();
        defeitoCoberto = entity.getDefeitoCoberto();
        defeitoNaoCoberto = entity.getDefeitoNaoCoberto();
        prazoAcionamentoDias = entity.getPrazoAcionamentoDias();
        quantidadeUtilizada = entity.getQuantidadeUtilizada();
        valorUtilizado = entity.getValorUtilizado();
        saldoDisponivel = entity.getSaldoDisponivel();
        observacoes = entity.getObservacoes();
        ativo = entity.getAtivo();

        Long garantiaId = null;

        ItemGarantiaResponse itemGarantiaResponse = new ItemGarantiaResponse( id, garantiaId, tipoItem, servicoId, produtoId, descricaoItem, quantidadeOriginal, valorUnitarioOriginal, valorTotalOriginal, percentualCobertura, valorCobertura, condicoesEspecificas, defeitoCoberto, defeitoNaoCoberto, prazoAcionamentoDias, quantidadeUtilizada, valorUtilizado, saldoDisponivel, observacoes, ativo );

        return itemGarantiaResponse;
    }

    @Override
    public void updateEntityFromDTO(ItemGarantiaRequest request, ItemGarantia entity) {
        if ( request == null ) {
            return;
        }

        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
        if ( request.percentualCobertura() != null ) {
            entity.setPercentualCobertura( request.percentualCobertura() );
        }
        if ( request.tipoItem() != null ) {
            entity.setTipoItem( request.tipoItem() );
        }
        if ( request.servicoId() != null ) {
            entity.setServicoId( request.servicoId() );
        }
        if ( request.produtoId() != null ) {
            entity.setProdutoId( request.produtoId() );
        }
        if ( request.descricaoItem() != null ) {
            entity.setDescricaoItem( request.descricaoItem() );
        }
        if ( request.quantidadeOriginal() != null ) {
            entity.setQuantidadeOriginal( request.quantidadeOriginal() );
        }
        if ( request.valorUnitarioOriginal() != null ) {
            entity.setValorUnitarioOriginal( request.valorUnitarioOriginal() );
        }
        if ( request.valorTotalOriginal() != null ) {
            entity.setValorTotalOriginal( request.valorTotalOriginal() );
        }
        if ( request.valorCobertura() != null ) {
            entity.setValorCobertura( request.valorCobertura() );
        }
        if ( request.condicoesEspecificas() != null ) {
            entity.setCondicoesEspecificas( request.condicoesEspecificas() );
        }
        if ( request.defeitoCoberto() != null ) {
            entity.setDefeitoCoberto( request.defeitoCoberto() );
        }
        if ( request.defeitoNaoCoberto() != null ) {
            entity.setDefeitoNaoCoberto( request.defeitoNaoCoberto() );
        }
        if ( request.prazoAcionamentoDias() != null ) {
            entity.setPrazoAcionamentoDias( request.prazoAcionamentoDias() );
        }
    }
}
