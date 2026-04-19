package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.CentroCusto;
import com.neritech.saas.financeiro.domain.ContaBancaria;
import com.neritech.saas.financeiro.domain.ContasReceber;
import com.neritech.saas.financeiro.domain.FormaPagamento;
import com.neritech.saas.financeiro.domain.PlanoConta;
import com.neritech.saas.financeiro.domain.enums.StatusTitulo;
import com.neritech.saas.financeiro.domain.enums.TipoTitulo;
import com.neritech.saas.financeiro.dto.ContasReceberRequest;
import com.neritech.saas.financeiro.dto.ContasReceberResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T16:16:31-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ContasReceberMapperImpl implements ContasReceberMapper {

    @Override
    public ContasReceber toEntity(ContasReceberRequest request) {
        if ( request == null ) {
            return null;
        }

        ContasReceber contasReceber = new ContasReceber();

        contasReceber.setValorNominal( request.valorOriginal() );
        contasReceber.setClienteId( request.clienteId() );
        contasReceber.setDataEmissao( request.dataEmissao() );
        contasReceber.setDataVencimento( request.dataVencimento() );
        contasReceber.setNumeroTitulo( request.numeroTitulo() );
        contasReceber.setObservacoes( request.observacoes() );
        contasReceber.setStatus( request.status() );
        contasReceber.setTipoTitulo( request.tipoTitulo() );
        contasReceber.setValorDesconto( request.valorDesconto() );
        contasReceber.setValorJuros( request.valorJuros() );
        contasReceber.setValorMulta( request.valorMulta() );

        return contasReceber;
    }

    @Override
    public ContasReceberResponse toResponse(ContasReceber entity) {
        if ( entity == null ) {
            return null;
        }

        String formaPagamentoNome = null;
        String contaBancariaNome = null;
        String centroCustoNome = null;
        String planoContasNome = null;
        Long id = null;
        Long empresaId = null;
        Long clienteId = null;
        LocalDate dataEmissao = null;
        LocalDate dataVencimento = null;
        BigDecimal valorJuros = null;
        BigDecimal valorMulta = null;
        BigDecimal valorDesconto = null;
        StatusTitulo status = null;
        TipoTitulo tipoTitulo = null;
        String numeroTitulo = null;
        String observacoes = null;

        formaPagamentoNome = entityFormaPagamentoNome( entity );
        contaBancariaNome = entityContaBancariaBancoNome( entity );
        centroCustoNome = entityCentroCustoNome( entity );
        planoContasNome = entityPlanoContasNome( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        clienteId = entity.getClienteId();
        dataEmissao = entity.getDataEmissao();
        dataVencimento = entity.getDataVencimento();
        valorJuros = entity.getValorJuros();
        valorMulta = entity.getValorMulta();
        valorDesconto = entity.getValorDesconto();
        status = entity.getStatus();
        tipoTitulo = entity.getTipoTitulo();
        numeroTitulo = entity.getNumeroTitulo();
        observacoes = entity.getObservacoes();

        String faturaNumero = null;
        String descricao = null;
        Long faturaId = null;
        LocalDate dataRecebimento = null;
        BigDecimal valorOriginal = null;
        BigDecimal valorRecebido = null;
        BigDecimal saldoDevedor = null;
        Long formaPagamentoId = null;
        Long contaBancariaId = null;
        Long centroCustoId = null;
        Long planoContasId = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        ContasReceberResponse contasReceberResponse = new ContasReceberResponse( id, empresaId, descricao, clienteId, faturaId, faturaNumero, dataEmissao, dataVencimento, dataRecebimento, valorOriginal, valorRecebido, valorJuros, valorMulta, valorDesconto, saldoDevedor, status, tipoTitulo, formaPagamentoId, formaPagamentoNome, contaBancariaId, contaBancariaNome, centroCustoId, centroCustoNome, planoContasId, planoContasNome, numeroTitulo, observacoes, createdAt, updatedAt );

        return contasReceberResponse;
    }

    @Override
    public void updateEntityFromDTO(ContasReceberRequest request, ContasReceber entity) {
        if ( request == null ) {
            return;
        }

        if ( request.valorOriginal() != null ) {
            entity.setValorNominal( request.valorOriginal() );
        }
        if ( request.clienteId() != null ) {
            entity.setClienteId( request.clienteId() );
        }
        if ( request.dataEmissao() != null ) {
            entity.setDataEmissao( request.dataEmissao() );
        }
        if ( request.dataVencimento() != null ) {
            entity.setDataVencimento( request.dataVencimento() );
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
    }

    private String entityFormaPagamentoNome(ContasReceber contasReceber) {
        if ( contasReceber == null ) {
            return null;
        }
        FormaPagamento formaPagamento = contasReceber.getFormaPagamento();
        if ( formaPagamento == null ) {
            return null;
        }
        String nome = formaPagamento.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private String entityContaBancariaBancoNome(ContasReceber contasReceber) {
        if ( contasReceber == null ) {
            return null;
        }
        ContaBancaria contaBancaria = contasReceber.getContaBancaria();
        if ( contaBancaria == null ) {
            return null;
        }
        String bancoNome = contaBancaria.getBancoNome();
        if ( bancoNome == null ) {
            return null;
        }
        return bancoNome;
    }

    private String entityCentroCustoNome(ContasReceber contasReceber) {
        if ( contasReceber == null ) {
            return null;
        }
        CentroCusto centroCusto = contasReceber.getCentroCusto();
        if ( centroCusto == null ) {
            return null;
        }
        String nome = centroCusto.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private String entityPlanoContasNome(ContasReceber contasReceber) {
        if ( contasReceber == null ) {
            return null;
        }
        PlanoConta planoContas = contasReceber.getPlanoContas();
        if ( planoContas == null ) {
            return null;
        }
        String nome = planoContas.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
