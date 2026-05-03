package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.Fatura;
import com.neritech.saas.financeiro.domain.Nfe;
import com.neritech.saas.financeiro.domain.enums.AmbienteNfe;
import com.neritech.saas.financeiro.domain.enums.StatusNfe;
import com.neritech.saas.financeiro.domain.enums.TipoEmissaoNfe;
import com.neritech.saas.financeiro.domain.enums.TipoOperacaoNfe;
import com.neritech.saas.financeiro.dto.NfeRequest;
import com.neritech.saas.financeiro.dto.NfeResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:27:03-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class NfeMapperImpl implements NfeMapper {

    @Override
    public Nfe toEntity(NfeRequest request) {
        if ( request == null ) {
            return null;
        }

        Nfe nfe = new Nfe();

        if ( request.numeroNfe() != null ) {
            nfe.setNumeroNfe( Long.parseLong( request.numeroNfe() ) );
        }
        if ( request.serieNfe() != null ) {
            nfe.setSerieNfe( Integer.parseInt( request.serieNfe() ) );
        }
        nfe.setChaveNfe( request.chaveNfe() );
        nfe.setTipoOperacao( request.tipoOperacao() );
        nfe.setDataEmissao( request.dataEmissao() );
        nfe.setDataSaidaEntrada( request.dataSaidaEntrada() );
        nfe.setTipoEmissao( request.tipoEmissao() );
        nfe.setAmbiente( request.ambiente() );
        nfe.setProtocoloAutorizacao( request.protocoloAutorizacao() );
        nfe.setDanfeUrl( request.danfeUrl() );

        return nfe;
    }

    @Override
    public NfeResponse toResponse(Nfe entity) {
        if ( entity == null ) {
            return null;
        }

        String faturaNumero = null;
        Long id = null;
        Long empresaId = null;
        String chaveNfe = null;
        String numeroNfe = null;
        String serieNfe = null;
        TipoOperacaoNfe tipoOperacao = null;
        AmbienteNfe ambiente = null;
        TipoEmissaoNfe tipoEmissao = null;
        LocalDateTime dataEmissao = null;
        LocalDateTime dataSaidaEntrada = null;
        String danfeUrl = null;
        String protocoloAutorizacao = null;

        faturaNumero = entityFaturaNumeroFatura( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        chaveNfe = entity.getChaveNfe();
        if ( entity.getNumeroNfe() != null ) {
            numeroNfe = String.valueOf( entity.getNumeroNfe() );
        }
        if ( entity.getSerieNfe() != null ) {
            serieNfe = String.valueOf( entity.getSerieNfe() );
        }
        tipoOperacao = entity.getTipoOperacao();
        ambiente = entity.getAmbiente();
        tipoEmissao = entity.getTipoEmissao();
        dataEmissao = entity.getDataEmissao();
        dataSaidaEntrada = entity.getDataSaidaEntrada();
        danfeUrl = entity.getDanfeUrl();
        protocoloAutorizacao = entity.getProtocoloAutorizacao();

        Long faturaId = null;
        StatusNfe status = null;
        BigDecimal valorTotalNota = null;
        BigDecimal valorTotalProdutos = null;
        BigDecimal valorTotalServicos = null;
        String xmlUrl = null;
        String mensagemRetorno = null;
        String observacoes = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        NfeResponse nfeResponse = new NfeResponse( id, empresaId, faturaId, faturaNumero, chaveNfe, numeroNfe, serieNfe, tipoOperacao, status, ambiente, tipoEmissao, dataEmissao, dataSaidaEntrada, valorTotalNota, valorTotalProdutos, valorTotalServicos, xmlUrl, danfeUrl, protocoloAutorizacao, mensagemRetorno, observacoes, createdAt, updatedAt );

        return nfeResponse;
    }

    @Override
    public void updateEntityFromDTO(NfeRequest request, Nfe entity) {
        if ( request == null ) {
            return;
        }

        if ( request.numeroNfe() != null ) {
            entity.setNumeroNfe( Long.parseLong( request.numeroNfe() ) );
        }
        if ( request.serieNfe() != null ) {
            entity.setSerieNfe( Integer.parseInt( request.serieNfe() ) );
        }
        if ( request.chaveNfe() != null ) {
            entity.setChaveNfe( request.chaveNfe() );
        }
        if ( request.tipoOperacao() != null ) {
            entity.setTipoOperacao( request.tipoOperacao() );
        }
        if ( request.dataEmissao() != null ) {
            entity.setDataEmissao( request.dataEmissao() );
        }
        if ( request.dataSaidaEntrada() != null ) {
            entity.setDataSaidaEntrada( request.dataSaidaEntrada() );
        }
        if ( request.tipoEmissao() != null ) {
            entity.setTipoEmissao( request.tipoEmissao() );
        }
        if ( request.ambiente() != null ) {
            entity.setAmbiente( request.ambiente() );
        }
        if ( request.protocoloAutorizacao() != null ) {
            entity.setProtocoloAutorizacao( request.protocoloAutorizacao() );
        }
        if ( request.danfeUrl() != null ) {
            entity.setDanfeUrl( request.danfeUrl() );
        }
    }

    private String entityFaturaNumeroFatura(Nfe nfe) {
        if ( nfe == null ) {
            return null;
        }
        Fatura fatura = nfe.getFatura();
        if ( fatura == null ) {
            return null;
        }
        String numeroFatura = fatura.getNumeroFatura();
        if ( numeroFatura == null ) {
            return null;
        }
        return numeroFatura;
    }
}
