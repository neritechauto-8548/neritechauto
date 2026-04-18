package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ContasPagar;
import com.neritech.saas.financeiro.domain.enums.StatusTitulo;
import com.neritech.saas.financeiro.domain.enums.TipoTitulo;
import com.neritech.saas.financeiro.dto.ContasPagarRequest;
import com.neritech.saas.financeiro.dto.ContasPagarResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:35-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ContasPagarMapperImpl implements ContasPagarMapper {

    @Override
    public ContasPagar toEntity(ContasPagarRequest request) {
        if ( request == null ) {
            return null;
        }

        ContasPagar contasPagar = new ContasPagar();

        contasPagar.setDataEmissao( request.dataEmissao() );
        contasPagar.setDataPagamento( request.dataPagamento() );
        contasPagar.setDataVencimento( request.dataVencimento() );
        contasPagar.setDescricao( request.descricao() );
        contasPagar.setFornecedorId( request.fornecedorId() );
        contasPagar.setNumeroDocumento( request.numeroDocumento() );
        contasPagar.setNumeroTitulo( request.numeroTitulo() );
        contasPagar.setObservacoes( request.observacoes() );
        contasPagar.setStatus( request.status() );
        contasPagar.setTipoTitulo( request.tipoTitulo() );
        contasPagar.setValorDesconto( request.valorDesconto() );
        contasPagar.setValorJuros( request.valorJuros() );
        contasPagar.setValorMulta( request.valorMulta() );
        contasPagar.setValorPago( request.valorPago() );

        return contasPagar;
    }

    @Override
    public ContasPagarResponse toResponse(ContasPagar entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String descricao = null;
        Long fornecedorId = null;
        String numeroDocumento = null;
        LocalDate dataEmissao = null;
        LocalDate dataVencimento = null;
        LocalDate dataPagamento = null;
        BigDecimal valorPago = null;
        BigDecimal valorJuros = null;
        BigDecimal valorMulta = null;
        BigDecimal valorDesconto = null;
        StatusTitulo status = null;
        TipoTitulo tipoTitulo = null;
        String numeroTitulo = null;
        String observacoes = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        descricao = entity.getDescricao();
        fornecedorId = entity.getFornecedorId();
        numeroDocumento = entity.getNumeroDocumento();
        dataEmissao = entity.getDataEmissao();
        dataVencimento = entity.getDataVencimento();
        dataPagamento = entity.getDataPagamento();
        valorPago = entity.getValorPago();
        valorJuros = entity.getValorJuros();
        valorMulta = entity.getValorMulta();
        valorDesconto = entity.getValorDesconto();
        status = entity.getStatus();
        tipoTitulo = entity.getTipoTitulo();
        numeroTitulo = entity.getNumeroTitulo();
        observacoes = entity.getObservacoes();

        LocalDate dataAgendamento = null;
        BigDecimal valorOriginal = null;
        BigDecimal saldoDevedor = null;
        Long formaPagamentoId = null;
        String formaPagamentoNome = null;
        Long contaBancariaId = null;
        String contaBancariaNome = null;
        Long centroCustoId = null;
        String centroCustoNome = null;
        Long planoContasId = null;
        String planoContasNome = null;
        String codigoBarras = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        ContasPagarResponse contasPagarResponse = new ContasPagarResponse( id, empresaId, descricao, fornecedorId, numeroDocumento, dataEmissao, dataVencimento, dataPagamento, dataAgendamento, valorOriginal, valorPago, valorJuros, valorMulta, valorDesconto, saldoDevedor, status, tipoTitulo, formaPagamentoId, formaPagamentoNome, contaBancariaId, contaBancariaNome, centroCustoId, centroCustoNome, planoContasId, planoContasNome, numeroTitulo, codigoBarras, observacoes, createdAt, updatedAt );

        return contasPagarResponse;
    }

    @Override
    public void updateEntityFromDTO(ContasPagarRequest request, ContasPagar entity) {
        if ( request == null ) {
            return;
        }

        if ( request.dataEmissao() != null ) {
            entity.setDataEmissao( request.dataEmissao() );
        }
        if ( request.dataPagamento() != null ) {
            entity.setDataPagamento( request.dataPagamento() );
        }
        if ( request.dataVencimento() != null ) {
            entity.setDataVencimento( request.dataVencimento() );
        }
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.fornecedorId() != null ) {
            entity.setFornecedorId( request.fornecedorId() );
        }
        if ( request.numeroDocumento() != null ) {
            entity.setNumeroDocumento( request.numeroDocumento() );
        }
        if ( request.numeroTitulo() != null ) {
            entity.setNumeroTitulo( request.numeroTitulo() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
        if ( request.tipoTitulo() != null ) {
            entity.setTipoTitulo( request.tipoTitulo() );
        }
        if ( request.valorDesconto() != null ) {
            entity.setValorDesconto( request.valorDesconto() );
        }
        if ( request.valorJuros() != null ) {
            entity.setValorJuros( request.valorJuros() );
        }
        if ( request.valorMulta() != null ) {
            entity.setValorMulta( request.valorMulta() );
        }
        if ( request.valorPago() != null ) {
            entity.setValorPago( request.valorPago() );
        }
    }
}
