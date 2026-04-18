package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.CentroCusto;
import com.neritech.saas.financeiro.domain.ContaBancaria;
import com.neritech.saas.financeiro.domain.ContasPagar;
import com.neritech.saas.financeiro.domain.FormaPagamento;
import com.neritech.saas.financeiro.domain.PlanoConta;
import com.neritech.saas.financeiro.dto.ContasPagarRequest;
import com.neritech.saas.financeiro.dto.ContasPagarResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Primary
public class ContasPagarMapperManualImpl implements ContasPagarMapper {

    @Override
    public ContasPagar toEntity(ContasPagarRequest request) {
        if (request == null) return null;

        ContasPagar entity = new ContasPagar();
        entity.setDescricao(request.descricao());
        entity.setFornecedorId(request.fornecedorId());
        entity.setNumeroDocumento(request.numeroDocumento());
        entity.setNumeroTitulo(request.numeroTitulo());
        entity.setDataEmissao(request.dataEmissao());
        entity.setDataVencimento(request.dataVencimento());
        entity.setDataPagamento(request.dataPagamento());

        BigDecimal valorOriginal = request.valorOriginal();
        entity.setValorNominal(valorOriginal);

        BigDecimal valorPago = request.valorPago();
        entity.setValorPago(valorPago != null ? valorPago : BigDecimal.ZERO);

        BigDecimal valorJuros = request.valorJuros();
        entity.setValorJuros(valorJuros != null ? valorJuros : BigDecimal.ZERO);

        BigDecimal valorMulta = request.valorMulta();
        entity.setValorMulta(valorMulta != null ? valorMulta : BigDecimal.ZERO);

        BigDecimal valorDesconto = request.valorDesconto();
        entity.setValorDesconto(valorDesconto != null ? valorDesconto : BigDecimal.ZERO);

        BigDecimal saldoDevedor = request.saldoDevedor();
        entity.setValorPendente(saldoDevedor);

        entity.setStatus(request.status());
        entity.setTipoTitulo(request.tipoTitulo());
        entity.setObservacoes(request.observacoes());

        return entity;
    }

    @Override
    public ContasPagarResponse toResponse(ContasPagar entity) {
        if (entity == null) return null;

        Long id = entity.getId();
        Long empresaId = entity.getEmpresaId();
        String descricao = entity.getDescricao();
        Long fornecedorId = entity.getFornecedorId();
        String numeroDocumento = entity.getNumeroDocumento();
        LocalDate dataEmissao = entity.getDataEmissao();
        LocalDate dataVencimento = entity.getDataVencimento();
        LocalDate dataPagamento = entity.getDataPagamento();
        BigDecimal valorOriginal = entity.getValorNominal();
        BigDecimal valorPago = entity.getValorPago();
        BigDecimal valorJuros = entity.getValorJuros();
        BigDecimal valorMulta = entity.getValorMulta();
        BigDecimal valorDesconto = entity.getValorDesconto();
        BigDecimal saldoDevedor = entity.getValorPendente();

        Long formaPagamentoId = null;
        String formaPagamentoNome = null;
        FormaPagamento forma = entity.getFormaPagamento();
        if (forma != null) {
            formaPagamentoId = forma.getId();
            formaPagamentoNome = forma.getNome();
        }

        Long contaBancariaId = null;
        String contaBancariaNome = null;
        ContaBancaria conta = entity.getContaBancaria();
        if (conta != null) {
            contaBancariaId = conta.getId();
            contaBancariaNome = conta.getTitularConta();
        }

        Long centroCustoId = null;
        String centroCustoNome = null;
        CentroCusto centro = entity.getCentroCusto();
        if (centro != null) {
            centroCustoId = centro.getId();
            centroCustoNome = centro.getNome();
        }

        Long planoContasId = null;
        String planoContasNome = null;
        PlanoConta plano = entity.getPlanoContas();
        if (plano != null) {
            planoContasId = plano.getId();
            planoContasNome = plano.getNome();
        }

        String codigoBarras = null;
        String observacoes = entity.getObservacoes();
        LocalDateTime createdAt = entity.getDataCadastro();
        LocalDateTime updatedAt = entity.getDataAtualizacao();

        return new ContasPagarResponse(
                id,
                empresaId,
                descricao,
                fornecedorId,
                numeroDocumento,
                dataEmissao,
                dataVencimento,
                dataPagamento,
                null,
                valorOriginal,
                valorPago,
                valorJuros,
                valorMulta,
                valorDesconto,
                saldoDevedor,
                entity.getStatus(),
                entity.getTipoTitulo(),
                formaPagamentoId,
                formaPagamentoNome,
                contaBancariaId,
                contaBancariaNome,
                centroCustoId,
                centroCustoNome,
                planoContasId,
                planoContasNome,
                entity.getNumeroTitulo(),
                codigoBarras,
                observacoes,
                createdAt,
                updatedAt
        );
    }

    @Override
    public void updateEntityFromDTO(ContasPagarRequest request, ContasPagar entity) {
        if (request == null || entity == null) return;

        if (request.descricao() != null) entity.setDescricao(request.descricao());
        if (request.fornecedorId() != null) entity.setFornecedorId(request.fornecedorId());
        if (request.numeroDocumento() != null) entity.setNumeroDocumento(request.numeroDocumento());
        if (request.numeroTitulo() != null) entity.setNumeroTitulo(request.numeroTitulo());
        if (request.dataEmissao() != null) entity.setDataEmissao(request.dataEmissao());
        if (request.dataVencimento() != null) entity.setDataVencimento(request.dataVencimento());
        if (request.dataPagamento() != null) entity.setDataPagamento(request.dataPagamento());

        if (request.valorOriginal() != null) entity.setValorNominal(request.valorOriginal());
        if (request.valorPago() != null) entity.setValorPago(request.valorPago());
        if (request.valorJuros() != null) entity.setValorJuros(request.valorJuros());
        if (request.valorMulta() != null) entity.setValorMulta(request.valorMulta());
        if (request.valorDesconto() != null) entity.setValorDesconto(request.valorDesconto());
        if (request.saldoDevedor() != null) entity.setValorPendente(request.saldoDevedor());

        if (request.status() != null) entity.setStatus(request.status());
        if (request.tipoTitulo() != null) entity.setTipoTitulo(request.tipoTitulo());
        if (request.observacoes() != null) entity.setObservacoes(request.observacoes());
    }
}

