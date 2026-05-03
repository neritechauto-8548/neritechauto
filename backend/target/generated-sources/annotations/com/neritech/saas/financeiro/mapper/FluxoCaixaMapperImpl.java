package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.CentroCusto;
import com.neritech.saas.financeiro.domain.ContaBancaria;
import com.neritech.saas.financeiro.domain.FluxoCaixa;
import com.neritech.saas.financeiro.domain.enums.TipoMovimentacao;
import com.neritech.saas.financeiro.dto.FluxoCaixaRequest;
import com.neritech.saas.financeiro.dto.FluxoCaixaResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:26:51-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FluxoCaixaMapperImpl implements FluxoCaixaMapper {

    @Override
    public FluxoCaixa toEntity(FluxoCaixaRequest request) {
        if ( request == null ) {
            return null;
        }

        FluxoCaixa fluxoCaixa = new FluxoCaixa();

        fluxoCaixa.setDataMovimentacao( request.dataMovimento() );
        fluxoCaixa.setDescricao( request.descricao() );
        fluxoCaixa.setObservacoes( request.observacoes() );
        fluxoCaixa.setTipoMovimentacao( request.tipoMovimentacao() );
        fluxoCaixa.setValor( request.valor() );

        return fluxoCaixa;
    }

    @Override
    public FluxoCaixaResponse toResponse(FluxoCaixa entity) {
        if ( entity == null ) {
            return null;
        }

        String contaBancariaNome = null;
        String centroCustoNome = null;
        LocalDate dataMovimento = null;
        Long id = null;
        Long empresaId = null;
        String descricao = null;
        TipoMovimentacao tipoMovimentacao = null;
        BigDecimal valor = null;
        String observacoes = null;

        contaBancariaNome = entityContaBancariaBancoNome( entity );
        centroCustoNome = entityCentroCustoNome( entity );
        dataMovimento = entity.getDataMovimentacao();
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        descricao = entity.getDescricao();
        tipoMovimentacao = entity.getTipoMovimentacao();
        valor = entity.getValor();
        observacoes = entity.getObservacoes();

        Long planoContasId = null;
        String planoContasNome = null;
        Long pagamentoId = getPagamentoId(entity);
        Long recebimentoId = getRecebimentoId(entity);
        BigDecimal saldoAcumulado = null;
        Long contaBancariaId = null;
        Long centroCustoId = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        FluxoCaixaResponse fluxoCaixaResponse = new FluxoCaixaResponse( id, empresaId, dataMovimento, descricao, tipoMovimentacao, valor, saldoAcumulado, contaBancariaId, contaBancariaNome, centroCustoId, centroCustoNome, planoContasId, planoContasNome, pagamentoId, recebimentoId, observacoes, createdAt, updatedAt );

        return fluxoCaixaResponse;
    }

    @Override
    public void updateEntityFromDTO(FluxoCaixaRequest request, FluxoCaixa entity) {
        if ( request == null ) {
            return;
        }

        if ( request.dataMovimento() != null ) {
            entity.setDataMovimentacao( request.dataMovimento() );
        }
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.tipoMovimentacao() != null ) {
            entity.setTipoMovimentacao( request.tipoMovimentacao() );
        }
        if ( request.valor() != null ) {
            entity.setValor( request.valor() );
        }
    }

    private String entityContaBancariaBancoNome(FluxoCaixa fluxoCaixa) {
        if ( fluxoCaixa == null ) {
            return null;
        }
        ContaBancaria contaBancaria = fluxoCaixa.getContaBancaria();
        if ( contaBancaria == null ) {
            return null;
        }
        String bancoNome = contaBancaria.getBancoNome();
        if ( bancoNome == null ) {
            return null;
        }
        return bancoNome;
    }

    private String entityCentroCustoNome(FluxoCaixa fluxoCaixa) {
        if ( fluxoCaixa == null ) {
            return null;
        }
        CentroCusto centroCusto = fluxoCaixa.getCentroCusto();
        if ( centroCusto == null ) {
            return null;
        }
        String nome = centroCusto.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
