package com.neritech.saas.garantia.mapper;

import com.neritech.saas.garantia.domain.Garantia;
import com.neritech.saas.garantia.domain.StatusGarantia;
import com.neritech.saas.garantia.domain.TipoGarantia;
import com.neritech.saas.garantia.dto.GarantiaRequest;
import com.neritech.saas.garantia.dto.GarantiaResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T16:16:21-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class GarantiaMapperImpl implements GarantiaMapper {

    @Override
    public Garantia toEntity(GarantiaRequest request) {
        if ( request == null ) {
            return null;
        }

        Garantia garantia = new Garantia();

        garantia.setNumeroGarantia( request.numeroGarantia() );
        garantia.setClienteId( request.clienteId() );
        garantia.setVeiculoId( request.veiculoId() );
        garantia.setDataInicio( request.dataInicio() );
        garantia.setDataFim( request.dataFim() );
        garantia.setDiasGarantia( request.diasGarantia() );
        garantia.setStatus( request.status() );
        garantia.setValorOriginalServico( request.valorOriginalServico() );
        garantia.setValorCoberturaGarantia( request.valorCoberturaGarantia() );
        garantia.setKilometragemInicio( request.kilometragemInicio() );
        garantia.setKilometragemLimite( request.kilometragemLimite() );
        garantia.setCondicoesEspeciais( request.condicoesEspeciais() );
        garantia.setObservacoes( request.observacoes() );
        garantia.setCertificadoUrl( request.certificadoUrl() );
        garantia.setQrCodeVerificacao( request.qrCodeVerificacao() );

        return garantia;
    }

    @Override
    public GarantiaResponse toResponse(Garantia entity) {
        if ( entity == null ) {
            return null;
        }

        String tipoGarantiaNome = null;
        Long id = null;
        Long empresaId = null;
        String numeroGarantia = null;
        Long clienteId = null;
        Long veiculoId = null;
        LocalDate dataInicio = null;
        LocalDate dataFim = null;
        Integer diasGarantia = null;
        StatusGarantia status = null;
        BigDecimal valorOriginalServico = null;
        BigDecimal valorCoberturaGarantia = null;
        Integer kilometragemInicio = null;
        Integer kilometragemLimite = null;
        String condicoesEspeciais = null;
        String observacoes = null;
        String certificadoUrl = null;
        String qrCodeVerificacao = null;
        Long transferidaParaClienteId = null;
        LocalDateTime dataTransferencia = null;
        String motivoTransferencia = null;
        LocalDateTime dataRenovacao = null;
        Long canceladaPor = null;
        LocalDateTime dataCancelamento = null;
        String motivoCancelamento = null;
        Integer totalAcionamentos = null;
        BigDecimal valorTotalAcionamentos = null;
        LocalDateTime dataUltimoAcionamento = null;
        Boolean alertaVencimentoEnviado = null;
        LocalDateTime dataAlertaVencimento = null;
        LocalDateTime dataCadastro = null;
        Long emitidaPor = null;

        tipoGarantiaNome = entityTipoGarantiaNome( entity );
        id = entity.getId();
        empresaId = entity.getEmpresaId();
        numeroGarantia = entity.getNumeroGarantia();
        clienteId = entity.getClienteId();
        veiculoId = entity.getVeiculoId();
        dataInicio = entity.getDataInicio();
        dataFim = entity.getDataFim();
        diasGarantia = entity.getDiasGarantia();
        status = entity.getStatus();
        valorOriginalServico = entity.getValorOriginalServico();
        valorCoberturaGarantia = entity.getValorCoberturaGarantia();
        kilometragemInicio = entity.getKilometragemInicio();
        kilometragemLimite = entity.getKilometragemLimite();
        condicoesEspeciais = entity.getCondicoesEspeciais();
        observacoes = entity.getObservacoes();
        certificadoUrl = entity.getCertificadoUrl();
        qrCodeVerificacao = entity.getQrCodeVerificacao();
        transferidaParaClienteId = entity.getTransferidaParaClienteId();
        dataTransferencia = entity.getDataTransferencia();
        motivoTransferencia = entity.getMotivoTransferencia();
        dataRenovacao = entity.getDataRenovacao();
        canceladaPor = entity.getCanceladaPor();
        dataCancelamento = entity.getDataCancelamento();
        motivoCancelamento = entity.getMotivoCancelamento();
        totalAcionamentos = entity.getTotalAcionamentos();
        valorTotalAcionamentos = entity.getValorTotalAcionamentos();
        dataUltimoAcionamento = entity.getDataUltimoAcionamento();
        alertaVencimentoEnviado = entity.getAlertaVencimentoEnviado();
        dataAlertaVencimento = entity.getDataAlertaVencimento();
        dataCadastro = entity.getDataCadastro();
        emitidaPor = entity.getEmitidaPor();

        Long tipoGarantiaId = null;
        Long ordemServicoId = null;
        Long renovadaDeGarantiaId = null;

        GarantiaResponse garantiaResponse = new GarantiaResponse( id, empresaId, numeroGarantia, tipoGarantiaId, tipoGarantiaNome, ordemServicoId, clienteId, veiculoId, dataInicio, dataFim, diasGarantia, status, valorOriginalServico, valorCoberturaGarantia, kilometragemInicio, kilometragemLimite, condicoesEspeciais, observacoes, certificadoUrl, qrCodeVerificacao, transferidaParaClienteId, dataTransferencia, motivoTransferencia, renovadaDeGarantiaId, dataRenovacao, canceladaPor, dataCancelamento, motivoCancelamento, totalAcionamentos, valorTotalAcionamentos, dataUltimoAcionamento, alertaVencimentoEnviado, dataAlertaVencimento, dataCadastro, emitidaPor );

        return garantiaResponse;
    }

    @Override
    public void updateEntityFromDTO(GarantiaRequest request, Garantia entity) {
        if ( request == null ) {
            return;
        }

        if ( request.numeroGarantia() != null ) {
            entity.setNumeroGarantia( request.numeroGarantia() );
        }
        if ( request.clienteId() != null ) {
            entity.setClienteId( request.clienteId() );
        }
        if ( request.veiculoId() != null ) {
            entity.setVeiculoId( request.veiculoId() );
        }
        if ( request.dataInicio() != null ) {
            entity.setDataInicio( request.dataInicio() );
        }
        if ( request.dataFim() != null ) {
            entity.setDataFim( request.dataFim() );
        }
        if ( request.diasGarantia() != null ) {
            entity.setDiasGarantia( request.diasGarantia() );
        }
        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
        if ( request.valorOriginalServico() != null ) {
            entity.setValorOriginalServico( request.valorOriginalServico() );
        }
        if ( request.valorCoberturaGarantia() != null ) {
            entity.setValorCoberturaGarantia( request.valorCoberturaGarantia() );
        }
        if ( request.kilometragemInicio() != null ) {
            entity.setKilometragemInicio( request.kilometragemInicio() );
        }
        if ( request.kilometragemLimite() != null ) {
            entity.setKilometragemLimite( request.kilometragemLimite() );
        }
        if ( request.condicoesEspeciais() != null ) {
            entity.setCondicoesEspeciais( request.condicoesEspeciais() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.certificadoUrl() != null ) {
            entity.setCertificadoUrl( request.certificadoUrl() );
        }
        if ( request.qrCodeVerificacao() != null ) {
            entity.setQrCodeVerificacao( request.qrCodeVerificacao() );
        }
    }

    private String entityTipoGarantiaNome(Garantia garantia) {
        if ( garantia == null ) {
            return null;
        }
        TipoGarantia tipoGarantia = garantia.getTipoGarantia();
        if ( tipoGarantia == null ) {
            return null;
        }
        String nome = tipoGarantia.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
