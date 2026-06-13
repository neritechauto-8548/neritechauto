package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.*;
import com.neritech.saas.financeiro.dto.ContasReceberRequest;
import com.neritech.saas.empresa.domain.DepartamentoContabio;
import com.neritech.saas.financeiro.dto.ContasReceberResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Primary
public class ContasReceberMapperManualImpl implements ContasReceberMapper {

    @Override
    public ContasReceber toEntity(ContasReceberRequest request) {
        if (request == null) return null;

        ContasReceber entity = new ContasReceber();
        entity.setClienteId(request.clienteId());
        
        // Relationships are ignored here and handled in service
        
        entity.setDescricao(request.descricao());
        entity.setNumeroTitulo(request.numeroTitulo());
        
        entity.setDataEmissao(request.dataEmissao());
        entity.setDataVencimento(request.dataVencimento());
        
        // Map valorOriginal to valorNominal
        entity.setValorNominal(request.valorOriginal());
        
        entity.setValorPago(request.valorRecebido() != null ? request.valorRecebido() : BigDecimal.ZERO);
        entity.setValorJuros(request.valorJuros() != null ? request.valorJuros() : BigDecimal.ZERO);
        entity.setValorMulta(request.valorMulta() != null ? request.valorMulta() : BigDecimal.ZERO);
        entity.setValorDesconto(request.valorDesconto() != null ? request.valorDesconto() : BigDecimal.ZERO);
        entity.setValorPendente(request.saldoDevedor());
        
        entity.setStatus(request.status());
        entity.setTipoTitulo(request.tipoTitulo());
        entity.setObservacoes(request.observacoes());
        
        return entity;
    }

    @Override
    public ContasReceberResponse toResponse(ContasReceber entity) {
        if (entity == null) return null;

        Long id = entity.getId();
        Long empresaId = entity.getEmpresaId();
        String descricao = entity.getDescricao();
        Long clienteId = entity.getClienteId();
        
        Long faturaId = null;
        String faturaNumero = null;
        if (entity.getFatura() != null) {
            faturaId = entity.getFatura().getId();
            // faturaNumero is ignored in interface but we can try to map it if Fatura has generic number field, 
            // but for now let's keep it null as per interface potentially strictly following ignore
            // Interface says: @Mapping(target = "faturaNumero", ignore = true)
        }
        
        LocalDate dataEmissao = entity.getDataEmissao();
        LocalDate dataVencimento = entity.getDataVencimento();
        LocalDate dataRecebimento = null;
        if (entity.getRecebimentos() != null && !entity.getRecebimentos().isEmpty()) {
            dataRecebimento = entity.getRecebimentos().get(entity.getRecebimentos().size() - 1).getDataRecebimento();
        }
        
        BigDecimal valorOriginal = entity.getValorNominal();
        BigDecimal valorRecebido = entity.getValorPago();
        BigDecimal valorJuros = entity.getValorJuros();
        BigDecimal valorMulta = entity.getValorMulta();
        BigDecimal valorDesconto = entity.getValorDesconto();
        BigDecimal saldoDevedor = entity.getValorPendente();
        
        Long formaPagamentoId = null;
        String formaPagamentoNome = null;
        if (entity.getFormaPagamento() != null) {
            formaPagamentoId = entity.getFormaPagamento().getId();
            formaPagamentoNome = entity.getFormaPagamento().getNome();
        }
        
        Long contaBancariaId = null;
        String contaBancariaNome = null;
        if (entity.getContaBancaria() != null) {
            contaBancariaId = entity.getContaBancaria().getId();
            contaBancariaNome = entity.getContaBancaria().getBancoNome();
        }
        
        Long centroCustoId = null;
        String centroCustoNome = null;
        DepartamentoContabio centro = entity.getCentroCusto();
        if (centro != null) {
            centroCustoId = centro.getId();
            centroCustoNome = centro.getDescricao();
        }
        
        Long planoContasId = null;
        String planoContasNome = null;
        if (entity.getPlanoContas() != null) {
            planoContasId = entity.getPlanoContas().getId();
            planoContasNome = entity.getPlanoContas().getNome();
        }
        
        String numeroTitulo = entity.getNumeroTitulo();
        String observacoes = entity.getObservacoes();
        LocalDateTime createdAt = entity.getDataCadastro();
        LocalDateTime updatedAt = entity.getDataAtualizacao();

        return new ContasReceberResponse(
                id,
                empresaId,
                descricao,
                clienteId,
                faturaId,
                faturaNumero,
                dataEmissao,
                dataVencimento,
                dataRecebimento,
                valorOriginal,
                valorRecebido,
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
                numeroTitulo,
                observacoes,
                null, // recebimentos
                null, // anexos
                null, // historico
                createdAt,
                updatedAt
        );
    }

    @Override
    public void updateEntityFromDTO(ContasReceberRequest request, ContasReceber entity) {
        if (request == null || entity == null) return;

        if (request.clienteId() != null) entity.setClienteId(request.clienteId());
        
        // Relationships ignored
        
        if (request.descricao() != null) entity.setDescricao(request.descricao());
        if (request.numeroTitulo() != null) entity.setNumeroTitulo(request.numeroTitulo());
        
        if (request.dataEmissao() != null) entity.setDataEmissao(request.dataEmissao());
        if (request.dataVencimento() != null) entity.setDataVencimento(request.dataVencimento());
        
        if (request.valorOriginal() != null) entity.setValorNominal(request.valorOriginal());
        if (request.valorRecebido() != null) entity.setValorPago(request.valorRecebido());
        if (request.valorJuros() != null) entity.setValorJuros(request.valorJuros());
        if (request.valorMulta() != null) entity.setValorMulta(request.valorMulta());
        if (request.valorDesconto() != null) entity.setValorDesconto(request.valorDesconto());
        if (request.saldoDevedor() != null) entity.setValorPendente(request.saldoDevedor());
        
        if (request.status() != null) entity.setStatus(request.status());
        if (request.tipoTitulo() != null) entity.setTipoTitulo(request.tipoTitulo());
        if (request.observacoes() != null) entity.setObservacoes(request.observacoes());
    }
}
