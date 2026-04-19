package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.ConciliacaoBancaria;
import com.neritech.saas.financeiro.domain.ContaBancaria;
import com.neritech.saas.financeiro.domain.enums.StatusConciliacao;
import com.neritech.saas.financeiro.dto.ConciliacaoBancariaRequest;
import com.neritech.saas.financeiro.dto.ConciliacaoBancariaResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:53-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ConciliacaoBancariaMapperImpl implements ConciliacaoBancariaMapper {

    @Override
    public ConciliacaoBancaria toEntity(ConciliacaoBancariaRequest request) {
        if ( request == null ) {
            return null;
        }

        ConciliacaoBancaria conciliacaoBancaria = new ConciliacaoBancaria();

        conciliacaoBancaria.setStatus( request.status() );
        conciliacaoBancaria.setObservacoes( request.observacoes() );
        conciliacaoBancaria.setArquivoExtratoUrl( request.arquivoExtratoUrl() );

        return conciliacaoBancaria;
    }

    @Override
    public ConciliacaoBancariaResponse toResponse(ConciliacaoBancaria entity) {
        if ( entity == null ) {
            return null;
        }

        String contaBancariaNome = null;
        Long id = null;
        StatusConciliacao status = null;
        String arquivoExtratoUrl = null;
        String observacoes = null;

        contaBancariaNome = entityContaBancariaBancoNome( entity );
        id = entity.getId();
        status = entity.getStatus();
        arquivoExtratoUrl = entity.getArquivoExtratoUrl();
        observacoes = entity.getObservacoes();

        Long empresaId = null;
        Long contaBancariaId = null;
        LocalDate dataConciliacao = null;
        BigDecimal saldoSistema = null;
        BigDecimal saldoBanco = null;
        BigDecimal diferenca = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        ConciliacaoBancariaResponse conciliacaoBancariaResponse = new ConciliacaoBancariaResponse( id, empresaId, contaBancariaId, contaBancariaNome, dataConciliacao, saldoSistema, saldoBanco, diferenca, status, arquivoExtratoUrl, observacoes, createdAt, updatedAt );

        return conciliacaoBancariaResponse;
    }

    @Override
    public void updateEntityFromDTO(ConciliacaoBancariaRequest request, ConciliacaoBancaria entity) {
        if ( request == null ) {
            return;
        }

        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.arquivoExtratoUrl() != null ) {
            entity.setArquivoExtratoUrl( request.arquivoExtratoUrl() );
        }
    }

    private String entityContaBancariaBancoNome(ConciliacaoBancaria conciliacaoBancaria) {
        if ( conciliacaoBancaria == null ) {
            return null;
        }
        ContaBancaria contaBancaria = conciliacaoBancaria.getContaBancaria();
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
