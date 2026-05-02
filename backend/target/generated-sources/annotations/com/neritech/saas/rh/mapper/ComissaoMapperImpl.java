package com.neritech.saas.rh.mapper;

import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import com.neritech.saas.financeiro.domain.enums.TipoComissao;
import com.neritech.saas.rh.domain.Comissao;
import com.neritech.saas.rh.domain.enums.BaseCalculoComissao;
import com.neritech.saas.rh.dto.ComissaoRequest;
import com.neritech.saas.rh.dto.ComissaoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:21-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ComissaoMapperImpl implements ComissaoMapper {

    @Override
    public Comissao toEntity(ComissaoRequest request) {
        if ( request == null ) {
            return null;
        }

        Comissao comissao = new Comissao();

        comissao.setEmpresaId( request.empresaId() );
        comissao.setFuncionarioId( request.funcionarioId() );
        comissao.setTipoComissao( request.tipoComissao() );
        comissao.setPeriodoReferencia( request.periodoReferencia() );
        comissao.setBaseCalculo( request.baseCalculo() );
        comissao.setValorBase( request.valorBase() );
        comissao.setPercentualComissao( request.percentualComissao() );
        comissao.setValorComissao( request.valorComissao() );
        comissao.setMetaEstabelecida( request.metaEstabelecida() );
        comissao.setMetaAtingida( request.metaAtingida() );
        comissao.setPercentualMetaAtingido( request.percentualMetaAtingido() );
        comissao.setBonusMeta( request.bonusMeta() );
        comissao.setDescontoRetrabalho( request.descontoRetrabalho() );
        comissao.setValorLiquido( request.valorLiquido() );
        comissao.setDataCompetencia( request.dataCompetencia() );
        comissao.setDataPagamento( request.dataPagamento() );
        comissao.setStatusPagamento( request.statusPagamento() );
        comissao.setObservacoes( request.observacoes() );

        return comissao;
    }

    @Override
    public ComissaoResponse toResponse(Comissao entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        Long funcionarioId = null;
        TipoComissao tipoComissao = null;
        String periodoReferencia = null;
        BaseCalculoComissao baseCalculo = null;
        BigDecimal valorBase = null;
        BigDecimal percentualComissao = null;
        BigDecimal valorComissao = null;
        BigDecimal metaEstabelecida = null;
        BigDecimal metaAtingida = null;
        BigDecimal percentualMetaAtingido = null;
        BigDecimal bonusMeta = null;
        BigDecimal descontoRetrabalho = null;
        BigDecimal valorLiquido = null;
        LocalDate dataCompetencia = null;
        LocalDate dataPagamento = null;
        StatusPagamento statusPagamento = null;
        String observacoes = null;
        Long aprovadaPor = null;
        LocalDateTime dataAprovacao = null;
        Long pagaPor = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        funcionarioId = entity.getFuncionarioId();
        tipoComissao = entity.getTipoComissao();
        periodoReferencia = entity.getPeriodoReferencia();
        baseCalculo = entity.getBaseCalculo();
        valorBase = entity.getValorBase();
        percentualComissao = entity.getPercentualComissao();
        valorComissao = entity.getValorComissao();
        metaEstabelecida = entity.getMetaEstabelecida();
        metaAtingida = entity.getMetaAtingida();
        percentualMetaAtingido = entity.getPercentualMetaAtingido();
        bonusMeta = entity.getBonusMeta();
        descontoRetrabalho = entity.getDescontoRetrabalho();
        valorLiquido = entity.getValorLiquido();
        dataCompetencia = entity.getDataCompetencia();
        dataPagamento = entity.getDataPagamento();
        statusPagamento = entity.getStatusPagamento();
        observacoes = entity.getObservacoes();
        aprovadaPor = entity.getAprovadaPor();
        dataAprovacao = entity.getDataAprovacao();
        pagaPor = entity.getPagaPor();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        ComissaoResponse comissaoResponse = new ComissaoResponse( id, empresaId, funcionarioId, tipoComissao, periodoReferencia, baseCalculo, valorBase, percentualComissao, valorComissao, metaEstabelecida, metaAtingida, percentualMetaAtingido, bonusMeta, descontoRetrabalho, valorLiquido, dataCompetencia, dataPagamento, statusPagamento, observacoes, aprovadaPor, dataAprovacao, pagaPor, dataCadastro, dataAtualizacao );

        return comissaoResponse;
    }

    @Override
    public void updateEntity(ComissaoRequest request, Comissao entity) {
        if ( request == null ) {
            return;
        }

        if ( request.empresaId() != null ) {
            entity.setEmpresaId( request.empresaId() );
        }
        if ( request.funcionarioId() != null ) {
            entity.setFuncionarioId( request.funcionarioId() );
        }
        if ( request.tipoComissao() != null ) {
            entity.setTipoComissao( request.tipoComissao() );
        }
        if ( request.periodoReferencia() != null ) {
            entity.setPeriodoReferencia( request.periodoReferencia() );
        }
        if ( request.baseCalculo() != null ) {
            entity.setBaseCalculo( request.baseCalculo() );
        }
        if ( request.valorBase() != null ) {
            entity.setValorBase( request.valorBase() );
        }
        if ( request.percentualComissao() != null ) {
            entity.setPercentualComissao( request.percentualComissao() );
        }
        if ( request.valorComissao() != null ) {
            entity.setValorComissao( request.valorComissao() );
        }
        if ( request.metaEstabelecida() != null ) {
            entity.setMetaEstabelecida( request.metaEstabelecida() );
        }
        if ( request.metaAtingida() != null ) {
            entity.setMetaAtingida( request.metaAtingida() );
        }
        if ( request.percentualMetaAtingido() != null ) {
            entity.setPercentualMetaAtingido( request.percentualMetaAtingido() );
        }
        if ( request.bonusMeta() != null ) {
            entity.setBonusMeta( request.bonusMeta() );
        }
        if ( request.descontoRetrabalho() != null ) {
            entity.setDescontoRetrabalho( request.descontoRetrabalho() );
        }
        if ( request.valorLiquido() != null ) {
            entity.setValorLiquido( request.valorLiquido() );
        }
        if ( request.dataCompetencia() != null ) {
            entity.setDataCompetencia( request.dataCompetencia() );
        }
        if ( request.dataPagamento() != null ) {
            entity.setDataPagamento( request.dataPagamento() );
        }
        if ( request.statusPagamento() != null ) {
            entity.setStatusPagamento( request.statusPagamento() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }
}
