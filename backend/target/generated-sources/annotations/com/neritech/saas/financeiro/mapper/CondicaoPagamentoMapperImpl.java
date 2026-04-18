package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.CondicaoPagamento;
import com.neritech.saas.financeiro.domain.enums.TipoCondicaoPagamento;
import com.neritech.saas.financeiro.dto.CondicaoPagamentoRequest;
import com.neritech.saas.financeiro.dto.CondicaoPagamentoResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:49-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class CondicaoPagamentoMapperImpl implements CondicaoPagamentoMapper {

    @Override
    public CondicaoPagamento toEntity(CondicaoPagamentoRequest request) {
        if ( request == null ) {
            return null;
        }

        CondicaoPagamento condicaoPagamento = new CondicaoPagamento();

        condicaoPagamento.setAtivo( request.ativo() );
        condicaoPagamento.setDescontoAVistaPercentual( request.descontoAVistaPercentual() );
        condicaoPagamento.setIntervaloDias( request.intervaloDias() );
        condicaoPagamento.setJurosParcelamentoPercentual( request.jurosParcelamentoPercentual() );
        condicaoPagamento.setNumeroParcelas( request.numeroParcelas() );
        condicaoPagamento.setPadrao( request.padrao() );
        condicaoPagamento.setValorEntradaPercentual( request.valorEntradaPercentual() );
        condicaoPagamento.setVencimentoPrimeiraParcelaDias( request.vencimentoPrimeiraParcelaDias() );
        condicaoPagamento.setNome( request.nome() );
        condicaoPagamento.setDescricao( request.descricao() );
        condicaoPagamento.setTipo( request.tipo() );
        condicaoPagamento.setFormasPagamentoAceitas( request.formasPagamentoAceitas() );
        condicaoPagamento.setObservacoes( request.observacoes() );

        return condicaoPagamento;
    }

    @Override
    public CondicaoPagamentoResponse toResponse(CondicaoPagamento entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String nome = null;
        String descricao = null;
        TipoCondicaoPagamento tipo = null;
        Integer numeroParcelas = null;
        Integer intervaloDias = null;
        BigDecimal valorEntradaPercentual = null;
        BigDecimal descontoAVistaPercentual = null;
        BigDecimal jurosParcelamentoPercentual = null;
        Integer vencimentoPrimeiraParcelaDias = null;
        String formasPagamentoAceitas = null;
        Boolean padrao = null;
        Boolean ativo = null;
        String observacoes = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        tipo = entity.getTipo();
        numeroParcelas = entity.getNumeroParcelas();
        intervaloDias = entity.getIntervaloDias();
        valorEntradaPercentual = entity.getValorEntradaPercentual();
        descontoAVistaPercentual = entity.getDescontoAVistaPercentual();
        jurosParcelamentoPercentual = entity.getJurosParcelamentoPercentual();
        vencimentoPrimeiraParcelaDias = entity.getVencimentoPrimeiraParcelaDias();
        formasPagamentoAceitas = entity.getFormasPagamentoAceitas();
        padrao = entity.getPadrao();
        ativo = entity.getAtivo();
        observacoes = entity.getObservacoes();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        CondicaoPagamentoResponse condicaoPagamentoResponse = new CondicaoPagamentoResponse( id, empresaId, nome, descricao, tipo, numeroParcelas, intervaloDias, valorEntradaPercentual, descontoAVistaPercentual, jurosParcelamentoPercentual, vencimentoPrimeiraParcelaDias, formasPagamentoAceitas, padrao, ativo, observacoes, createdAt, updatedAt );

        return condicaoPagamentoResponse;
    }

    @Override
    public void updateEntityFromDTO(CondicaoPagamentoRequest request, CondicaoPagamento entity) {
        if ( request == null ) {
            return;
        }

        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
        if ( request.descontoAVistaPercentual() != null ) {
            entity.setDescontoAVistaPercentual( request.descontoAVistaPercentual() );
        }
        if ( request.intervaloDias() != null ) {
            entity.setIntervaloDias( request.intervaloDias() );
        }
        if ( request.jurosParcelamentoPercentual() != null ) {
            entity.setJurosParcelamentoPercentual( request.jurosParcelamentoPercentual() );
        }
        if ( request.numeroParcelas() != null ) {
            entity.setNumeroParcelas( request.numeroParcelas() );
        }
        if ( request.padrao() != null ) {
            entity.setPadrao( request.padrao() );
        }
        if ( request.valorEntradaPercentual() != null ) {
            entity.setValorEntradaPercentual( request.valorEntradaPercentual() );
        }
        if ( request.vencimentoPrimeiraParcelaDias() != null ) {
            entity.setVencimentoPrimeiraParcelaDias( request.vencimentoPrimeiraParcelaDias() );
        }
        if ( request.nome() != null ) {
            entity.setNome( request.nome() );
        }
        if ( request.descricao() != null ) {
            entity.setDescricao( request.descricao() );
        }
        if ( request.tipo() != null ) {
            entity.setTipo( request.tipo() );
        }
        if ( request.formasPagamentoAceitas() != null ) {
            entity.setFormasPagamentoAceitas( request.formasPagamentoAceitas() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }
}
