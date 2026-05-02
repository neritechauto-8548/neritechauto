package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ParcelaPagamento;
import com.neritech.saas.financeiro.domain.enums.StatusPagamento;
import com.neritech.saas.financeiro.dto.ParcelaPagamentoRequest;
import com.neritech.saas.financeiro.dto.ParcelaPagamentoResponse;
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
public class ParcelaPagamentoMapperImpl implements ParcelaPagamentoMapper {

    @Override
    public ParcelaPagamento toEntity(ParcelaPagamentoRequest request) {
        if ( request == null ) {
            return null;
        }

        ParcelaPagamento parcelaPagamento = new ParcelaPagamento();

        parcelaPagamento.setDataPagamento( request.dataPagamento() );
        parcelaPagamento.setDataVencimento( request.dataVencimento() );
        parcelaPagamento.setNumeroParcela( request.numeroParcela() );
        parcelaPagamento.setObservacoes( request.observacoes() );
        parcelaPagamento.setStatus( request.status() );
        parcelaPagamento.setValorPago( request.valorPago() );
        parcelaPagamento.setValorParcela( request.valorParcela() );

        return parcelaPagamento;
    }

    @Override
    public ParcelaPagamentoResponse toResponse(ParcelaPagamento entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Integer numeroParcela = null;
        LocalDate dataVencimento = null;
        BigDecimal valorParcela = null;
        BigDecimal valorPago = null;
        LocalDate dataPagamento = null;
        StatusPagamento status = null;
        String observacoes = null;

        id = entity.getId();
        numeroParcela = entity.getNumeroParcela();
        dataVencimento = entity.getDataVencimento();
        valorParcela = entity.getValorParcela();
        valorPago = entity.getValorPago();
        dataPagamento = entity.getDataPagamento();
        status = entity.getStatus();
        observacoes = entity.getObservacoes();

        BigDecimal valorJuros = null;
        BigDecimal valorMulta = null;
        BigDecimal valorDesconto = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        ParcelaPagamentoResponse parcelaPagamentoResponse = new ParcelaPagamentoResponse( id, numeroParcela, dataVencimento, valorParcela, valorJuros, valorMulta, valorDesconto, valorPago, dataPagamento, status, observacoes, createdAt, updatedAt );

        return parcelaPagamentoResponse;
    }

    @Override
    public void updateEntityFromDTO(ParcelaPagamentoRequest request, ParcelaPagamento entity) {
        if ( request == null ) {
            return;
        }

        if ( request.dataPagamento() != null ) {
            entity.setDataPagamento( request.dataPagamento() );
        }
        if ( request.dataVencimento() != null ) {
            entity.setDataVencimento( request.dataVencimento() );
        }
        if ( request.numeroParcela() != null ) {
            entity.setNumeroParcela( request.numeroParcela() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
        if ( request.valorPago() != null ) {
            entity.setValorPago( request.valorPago() );
        }
        if ( request.valorParcela() != null ) {
            entity.setValorParcela( request.valorParcela() );
        }
    }
}
