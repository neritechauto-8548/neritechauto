package com.neritech.saas.financeiro.mapper;

import com.neritech.saas.financeiro.domain.Fatura;
import com.neritech.saas.financeiro.domain.enums.StatusFatura;
import com.neritech.saas.financeiro.domain.enums.TipoFatura;
import com.neritech.saas.financeiro.dto.FaturaRequest;
import com.neritech.saas.financeiro.dto.FaturaResponse;
import com.neritech.saas.financeiro.dto.ItemFaturaResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:30-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FaturaMapperImpl implements FaturaMapper {

    @Override
    public Fatura toEntity(FaturaRequest request) {
        if ( request == null ) {
            return null;
        }

        Fatura fatura = new Fatura();

        fatura.setClienteId( request.clienteId() );
        fatura.setDataEmissao( request.dataEmissao() );
        fatura.setDataVencimento( request.dataVencimento() );
        fatura.setNumeroFatura( request.numeroFatura() );
        fatura.setObservacoes( request.observacoes() );
        fatura.setObservacoesInternas( request.observacoesInternas() );
        fatura.setOrdemServicoId( request.ordemServicoId() );
        fatura.setStatus( request.status() );
        fatura.setTipoFatura( request.tipoFatura() );
        fatura.setValorAcrescimos( request.valorAcrescimos() );
        fatura.setValorDescontos( request.valorDescontos() );

        return fatura;
    }

    @Override
    public FaturaResponse toResponse(Fatura entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long empresaId = null;
        String numeroFatura = null;
        Long clienteId = null;
        Long ordemServicoId = null;
        TipoFatura tipoFatura = null;
        LocalDate dataEmissao = null;
        LocalDate dataVencimento = null;
        BigDecimal valorServicos = null;
        BigDecimal valorProdutos = null;
        BigDecimal valorDescontos = null;
        BigDecimal valorAcrescimos = null;
        BigDecimal valorTotal = null;
        BigDecimal valorPago = null;
        BigDecimal valorPendente = null;
        StatusFatura status = null;
        String observacoes = null;
        String observacoesInternas = null;
        String nfeNumero = null;
        String nfeChave = null;
        String nfeUrlDanfe = null;
        LocalDateTime dataEnvioCliente = null;
        Boolean enviadaPorEmail = null;
        Boolean enviadaPorWhatsapp = null;
        String emailEnvio = null;
        String whatsappEnvio = null;
        String boletoNossoNumero = null;
        String boletoUrl = null;
        String pixQrCode = null;
        String pixCodigo = null;

        id = entity.getId();
        empresaId = entity.getEmpresaId();
        numeroFatura = entity.getNumeroFatura();
        clienteId = entity.getClienteId();
        ordemServicoId = entity.getOrdemServicoId();
        tipoFatura = entity.getTipoFatura();
        dataEmissao = entity.getDataEmissao();
        dataVencimento = entity.getDataVencimento();
        valorServicos = entity.getValorServicos();
        valorProdutos = entity.getValorProdutos();
        valorDescontos = entity.getValorDescontos();
        valorAcrescimos = entity.getValorAcrescimos();
        valorTotal = entity.getValorTotal();
        valorPago = entity.getValorPago();
        valorPendente = entity.getValorPendente();
        status = entity.getStatus();
        observacoes = entity.getObservacoes();
        observacoesInternas = entity.getObservacoesInternas();
        nfeNumero = entity.getNfeNumero();
        nfeChave = entity.getNfeChave();
        nfeUrlDanfe = entity.getNfeUrlDanfe();
        dataEnvioCliente = entity.getDataEnvioCliente();
        enviadaPorEmail = entity.getEnviadaPorEmail();
        enviadaPorWhatsapp = entity.getEnviadaPorWhatsapp();
        emailEnvio = entity.getEmailEnvio();
        whatsappEnvio = entity.getWhatsappEnvio();
        boletoNossoNumero = entity.getBoletoNossoNumero();
        boletoUrl = entity.getBoletoUrl();
        pixQrCode = entity.getPixQrCode();
        pixCodigo = entity.getPixCodigo();

        Long formaPagamentoId = null;
        String formaPagamentoNome = null;
        Long condicaoPagamentoId = null;
        String condicaoPagamentoNome = null;
        List<ItemFaturaResponse> itens = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        FaturaResponse faturaResponse = new FaturaResponse( id, empresaId, numeroFatura, clienteId, ordemServicoId, tipoFatura, dataEmissao, dataVencimento, valorServicos, valorProdutos, valorDescontos, valorAcrescimos, valorTotal, valorPago, valorPendente, status, formaPagamentoId, formaPagamentoNome, condicaoPagamentoId, condicaoPagamentoNome, observacoes, observacoesInternas, nfeNumero, nfeChave, nfeUrlDanfe, dataEnvioCliente, enviadaPorEmail, enviadaPorWhatsapp, emailEnvio, whatsappEnvio, boletoNossoNumero, boletoUrl, pixQrCode, pixCodigo, itens, createdAt, updatedAt );

        return faturaResponse;
    }

    @Override
    public void updateEntityFromDTO(FaturaRequest request, Fatura entity) {
        if ( request == null ) {
            return;
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
        if ( request.numeroFatura() != null ) {
            entity.setNumeroFatura( request.numeroFatura() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.observacoesInternas() != null ) {
            entity.setObservacoesInternas( request.observacoesInternas() );
        }
        if ( request.ordemServicoId() != null ) {
            entity.setOrdemServicoId( request.ordemServicoId() );
        }
        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
        if ( request.tipoFatura() != null ) {
            entity.setTipoFatura( request.tipoFatura() );
        }
        if ( request.valorAcrescimos() != null ) {
            entity.setValorAcrescimos( request.valorAcrescimos() );
        }
        if ( request.valorDescontos() != null ) {
            entity.setValorDescontos( request.valorDescontos() );
        }
    }
}
