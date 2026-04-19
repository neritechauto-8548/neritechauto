package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ItemFatura;
import com.neritech.saas.financeiro.domain.enums.TipoItemFatura;
import com.neritech.saas.financeiro.dto.ItemFaturaRequest;
import com.neritech.saas.financeiro.dto.ItemFaturaResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:35-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ItemFaturaMapperImpl implements ItemFaturaMapper {

    @Override
    public ItemFatura toEntity(ItemFaturaRequest request) {
        if ( request == null ) {
            return null;
        }

        ItemFatura itemFatura = new ItemFatura();

        itemFatura.setDescontoPercentual( request.descontoPercentual() );
        itemFatura.setDescontoValor( request.descontoValor() );
        itemFatura.setOrdemItem( request.ordemItem() );
        itemFatura.setTipoItem( request.tipoItem() );
        itemFatura.setServicoId( request.servicoId() );
        itemFatura.setProdutoId( request.produtoId() );
        itemFatura.setDescricao( request.descricao() );
        itemFatura.setQuantidade( request.quantidade() );
        itemFatura.setValorUnitario( request.valorUnitario() );
        itemFatura.setNcm( request.ncm() );
        itemFatura.setCfop( request.cfop() );
        itemFatura.setCstIcms( request.cstIcms() );
        itemFatura.setCstPis( request.cstPis() );
        itemFatura.setCstCofins( request.cstCofins() );
        itemFatura.setAliquotaIcms( request.aliquotaIcms() );
        itemFatura.setAliquotaPis( request.aliquotaPis() );
        itemFatura.setAliquotaCofins( request.aliquotaCofins() );
        itemFatura.setObservacoes( request.observacoes() );

        return itemFatura;
    }

    @Override
    public ItemFaturaResponse toResponse(ItemFatura entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        TipoItemFatura tipoItem = null;
        Long servicoId = null;
        Long produtoId = null;
        String descricao = null;
        BigDecimal quantidade = null;
        BigDecimal valorUnitario = null;
        BigDecimal valorTotal = null;
        BigDecimal descontoPercentual = null;
        BigDecimal descontoValor = null;
        BigDecimal valorLiquido = null;
        String ncm = null;
        String cfop = null;
        String cstIcms = null;
        String cstPis = null;
        String cstCofins = null;
        BigDecimal aliquotaIcms = null;
        BigDecimal aliquotaPis = null;
        BigDecimal aliquotaCofins = null;
        BigDecimal valorIcms = null;
        BigDecimal valorPis = null;
        BigDecimal valorCofins = null;
        String observacoes = null;
        Integer ordemItem = null;

        id = entity.getId();
        tipoItem = entity.getTipoItem();
        servicoId = entity.getServicoId();
        produtoId = entity.getProdutoId();
        descricao = entity.getDescricao();
        quantidade = entity.getQuantidade();
        valorUnitario = entity.getValorUnitario();
        valorTotal = entity.getValorTotal();
        descontoPercentual = entity.getDescontoPercentual();
        descontoValor = entity.getDescontoValor();
        valorLiquido = entity.getValorLiquido();
        ncm = entity.getNcm();
        cfop = entity.getCfop();
        cstIcms = entity.getCstIcms();
        cstPis = entity.getCstPis();
        cstCofins = entity.getCstCofins();
        aliquotaIcms = entity.getAliquotaIcms();
        aliquotaPis = entity.getAliquotaPis();
        aliquotaCofins = entity.getAliquotaCofins();
        valorIcms = entity.getValorIcms();
        valorPis = entity.getValorPis();
        valorCofins = entity.getValorCofins();
        observacoes = entity.getObservacoes();
        ordemItem = entity.getOrdemItem();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        ItemFaturaResponse itemFaturaResponse = new ItemFaturaResponse( id, tipoItem, servicoId, produtoId, descricao, quantidade, valorUnitario, valorTotal, descontoPercentual, descontoValor, valorLiquido, ncm, cfop, cstIcms, cstPis, cstCofins, aliquotaIcms, aliquotaPis, aliquotaCofins, valorIcms, valorPis, valorCofins, observacoes, ordemItem, createdAt, updatedAt );

        return itemFaturaResponse;
    }

    @Override
    public void updateEntityFromDTO(ItemFaturaRequest request, ItemFatura entity) {
        if ( request == null ) {
            return;
        }

        if ( request.descontoPercentual() != null ) {
            entity.setDescontoPercentual( request.descontoPercentual() );
        }
        if ( request.descontoValor() != null ) {
            entity.setDescontoValor( request.descontoValor() );
        }
        if ( request.ordemItem() != null ) {
            entity.setOrdemItem( request.ordemItem() );
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
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.quantidade() != null ) {
            entity.setQuantidade( request.quantidade() );
        }
        if ( request.valorUnitario() != null ) {
            entity.setValorUnitario( request.valorUnitario() );
        }
        if ( request.ncm() != null ) {
            entity.setNcm( request.ncm() );
        }
        if ( request.cfop() != null ) {
            entity.setCfop( request.cfop() );
        }
        if ( request.cstIcms() != null ) {
            entity.setCstIcms( request.cstIcms() );
        }
        if ( request.cstPis() != null ) {
            entity.setCstPis( request.cstPis() );
        }
        if ( request.cstCofins() != null ) {
            entity.setCstCofins( request.cstCofins() );
        }
        if ( request.aliquotaIcms() != null ) {
            entity.setAliquotaIcms( request.aliquotaIcms() );
        }
        if ( request.aliquotaPis() != null ) {
            entity.setAliquotaPis( request.aliquotaPis() );
        }
        if ( request.aliquotaCofins() != null ) {
            entity.setAliquotaCofins( request.aliquotaCofins() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }
}
