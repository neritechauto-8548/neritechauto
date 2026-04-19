package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.CentroCusto;
import com.neritech.saas.financeiro.domain.LancamentoContabil;
import com.neritech.saas.financeiro.domain.PlanoConta;
import com.neritech.saas.financeiro.dto.LancamentoContabilRequest;
import com.neritech.saas.financeiro.dto.LancamentoContabilResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:54-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class LancamentoContabilMapperImpl implements LancamentoContabilMapper {

    @Override
    public LancamentoContabil toEntity(LancamentoContabilRequest request) {
        if ( request == null ) {
            return null;
        }

        LancamentoContabil lancamentoContabil = new LancamentoContabil();

        lancamentoContabil.setNumeroLancamento( request.numeroLancamento() );
        lancamentoContabil.setDataLancamento( request.dataLancamento() );
        lancamentoContabil.setHistorico( request.historico() );

        return lancamentoContabil;
    }

    @Override
    public LancamentoContabilResponse toResponse(LancamentoContabil entity) {
        if ( entity == null ) {
            return null;
        }

        String contaDebitoNome = null;
        String contaCreditoNome = null;
        String centroCustoNome = null;
        Long id = null;
        Long empresaId = null;
        LocalDate dataLancamento = null;
        String numeroLancamento = null;
        String historico = null;

        contaDebitoNome = entityContaDebitoNome( entity );
        contaCreditoNome = entityContaCreditoNome( entity );
        centroCustoNome = entityCentroCustoNome( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        dataLancamento = entity.getDataLancamento();
        numeroLancamento = entity.getNumeroLancamento();
        historico = entity.getHistorico();

        String descricao = null;
        Long contaDebitoId = null;
        Long contaCreditoId = null;
        BigDecimal valor = null;
        Long centroCustoId = null;
        String documentoReferencia = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        LancamentoContabilResponse lancamentoContabilResponse = new LancamentoContabilResponse( id, empresaId, dataLancamento, numeroLancamento, descricao, contaDebitoId, contaDebitoNome, contaCreditoId, contaCreditoNome, valor, centroCustoId, centroCustoNome, historico, documentoReferencia, createdAt, updatedAt );

        return lancamentoContabilResponse;
    }

    @Override
    public void updateEntityFromDTO(LancamentoContabilRequest request, LancamentoContabil entity) {
        if ( request == null ) {
            return;
        }

        if ( request.numeroLancamento() != null ) {
            entity.setNumeroLancamento( request.numeroLancamento() );
        }
        if ( request.dataLancamento() != null ) {
            entity.setDataLancamento( request.dataLancamento() );
        }
        if ( request.historico() != null ) {
            entity.setHistorico( request.historico() );
        }
    }

    private String entityContaDebitoNome(LancamentoContabil lancamentoContabil) {
        if ( lancamentoContabil == null ) {
            return null;
        }
        PlanoConta contaDebito = lancamentoContabil.getContaDebito();
        if ( contaDebito == null ) {
            return null;
        }
        String nome = contaDebito.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private String entityContaCreditoNome(LancamentoContabil lancamentoContabil) {
        if ( lancamentoContabil == null ) {
            return null;
        }
        PlanoConta contaCredito = lancamentoContabil.getContaCredito();
        if ( contaCredito == null ) {
            return null;
        }
        String nome = contaCredito.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    private String entityCentroCustoNome(LancamentoContabil lancamentoContabil) {
        if ( lancamentoContabil == null ) {
            return null;
        }
        CentroCusto centroCusto = lancamentoContabil.getCentroCusto();
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
