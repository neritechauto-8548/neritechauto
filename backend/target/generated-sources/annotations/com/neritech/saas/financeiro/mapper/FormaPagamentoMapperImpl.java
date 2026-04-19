package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ContaBancaria;
import com.neritech.saas.financeiro.domain.FormaPagamento;
import com.neritech.saas.financeiro.domain.enums.TipoFormaPagamento;
import com.neritech.saas.financeiro.dto.FormaPagamentoRequest;
import com.neritech.saas.financeiro.dto.FormaPagamentoResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T16:16:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FormaPagamentoMapperImpl implements FormaPagamentoMapper {

    @Override
    public FormaPagamento toEntity(FormaPagamentoRequest request) {
        if ( request == null ) {
            return null;
        }

        FormaPagamento formaPagamento = new FormaPagamento();

        formaPagamento.setAceitaParcelamento( request.aceitaParcelamento() );
        formaPagamento.setAtivo( request.ativo() );
        formaPagamento.setExigeAutorizacao( request.exigeAutorizacao() );
        formaPagamento.setPadrao( request.padrao() );
        formaPagamento.setParcelasMaximas( request.parcelasMaximas() );
        formaPagamento.setPrazoRecebimentoDias( request.prazoRecebimentoDias() );
        formaPagamento.setTaxaAdministracao( request.taxaAdministracao() );
        formaPagamento.setNome( request.nome() );
        formaPagamento.setTipo( request.tipo() );
        formaPagamento.setLimiteDiario( request.limiteDiario() );
        formaPagamento.setObservacoes( request.observacoes() );

        return formaPagamento;
    }

    @Override
    public FormaPagamentoResponse toResponse(FormaPagamento entity) {
        if ( entity == null ) {
            return null;
        }

        String contaBancariaNome = null;
        Long id = null;
        Long empresaId = null;
        String nome = null;
        TipoFormaPagamento tipo = null;
        Boolean aceitaParcelamento = null;
        Integer parcelasMaximas = null;
        BigDecimal taxaAdministracao = null;
        Integer prazoRecebimentoDias = null;
        Boolean ativo = null;
        Boolean padrao = null;
        Boolean exigeAutorizacao = null;
        BigDecimal limiteDiario = null;
        String observacoes = null;

        contaBancariaNome = entityContaBancariaBancoNome( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        nome = entity.getNome();
        tipo = entity.getTipo();
        aceitaParcelamento = entity.getAceitaParcelamento();
        parcelasMaximas = entity.getParcelasMaximas();
        taxaAdministracao = entity.getTaxaAdministracao();
        prazoRecebimentoDias = entity.getPrazoRecebimentoDias();
        ativo = entity.getAtivo();
        padrao = entity.getPadrao();
        exigeAutorizacao = entity.getExigeAutorizacao();
        limiteDiario = entity.getLimiteDiario();
        observacoes = entity.getObservacoes();

        Long contaBancariaId = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        FormaPagamentoResponse formaPagamentoResponse = new FormaPagamentoResponse( id, empresaId, nome, tipo, aceitaParcelamento, parcelasMaximas, taxaAdministracao, prazoRecebimentoDias, contaBancariaId, contaBancariaNome, ativo, padrao, exigeAutorizacao, limiteDiario, observacoes, createdAt, updatedAt );

        return formaPagamentoResponse;
    }

    @Override
    public void updateEntityFromDTO(FormaPagamentoRequest request, FormaPagamento entity) {
        if ( request == null ) {
            return;
        }

        if ( request.aceitaParcelamento() != null ) {
            entity.setAceitaParcelamento( request.aceitaParcelamento() );
        }
        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
        if ( request.exigeAutorizacao() != null ) {
            entity.setExigeAutorizacao( request.exigeAutorizacao() );
        }
        if ( request.padrao() != null ) {
            entity.setPadrao( request.padrao() );
        }
        if ( request.parcelasMaximas() != null ) {
            entity.setParcelasMaximas( request.parcelasMaximas() );
        }
        if ( request.prazoRecebimentoDias() != null ) {
            entity.setPrazoRecebimentoDias( request.prazoRecebimentoDias() );
        }
        if ( request.taxaAdministracao() != null ) {
            entity.setTaxaAdministracao( request.taxaAdministracao() );
        }
        if ( request.nome() != null ) {
            entity.setNome( request.nome() );
        }
        if ( request.tipo() != null ) {
            entity.setTipo( request.tipo() );
        }
        if ( request.limiteDiario() != null ) {
            entity.setLimiteDiario( request.limiteDiario() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }

    private String entityContaBancariaBancoNome(FormaPagamento formaPagamento) {
        if ( formaPagamento == null ) {
            return null;
        }
        ContaBancaria contaBancaria = formaPagamento.getContaBancaria();
        if ( contaBancaria == null ) {
            return null;
        }
        String bancoNome = contaBancaria.getBancoNome();
        if ( bancoNome == null ) {
            return null;
        }
        return bancoNome;
    }
}
