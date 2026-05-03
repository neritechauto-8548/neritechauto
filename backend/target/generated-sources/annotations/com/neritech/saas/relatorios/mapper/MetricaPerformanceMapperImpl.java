package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.MetricaPerformance;
import com.neritech.saas.relatorios.dto.MetricaPerformanceRequest;
import com.neritech.saas.relatorios.dto.MetricaPerformanceResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:26:50-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class MetricaPerformanceMapperImpl implements MetricaPerformanceMapper {

    @Override
    public MetricaPerformance toEntity(MetricaPerformanceRequest request) {
        if ( request == null ) {
            return null;
        }

        MetricaPerformance.MetricaPerformanceBuilder metricaPerformance = MetricaPerformance.builder();

        metricaPerformance.alertaGerado( request.getAlertaGerado() );
        metricaPerformance.benchmarkAnterior( request.getBenchmarkAnterior() );
        metricaPerformance.categoria( request.getCategoria() );
        metricaPerformance.contextoAdicional( request.getContextoAdicional() );
        metricaPerformance.dataMedicao( request.getDataMedicao() );
        metricaPerformance.limiteAlertaMax( request.getLimiteAlertaMax() );
        metricaPerformance.limiteAlertaMin( request.getLimiteAlertaMin() );
        metricaPerformance.metrica( request.getMetrica() );
        metricaPerformance.observacoes( request.getObservacoes() );
        metricaPerformance.periodoReferencia( request.getPeriodoReferencia() );
        metricaPerformance.servidor( request.getServidor() );
        metricaPerformance.tendencia( request.getTendencia() );
        metricaPerformance.unidadeMedida( request.getUnidadeMedida() );
        metricaPerformance.valorNumerico( request.getValorNumerico() );
        metricaPerformance.valorTexto( request.getValorTexto() );
        metricaPerformance.variacaoPercentual( request.getVariacaoPercentual() );
        metricaPerformance.versaoAplicacao( request.getVersaoAplicacao() );

        return metricaPerformance.build();
    }

    @Override
    public MetricaPerformanceResponse toResponse(MetricaPerformance entity) {
        if ( entity == null ) {
            return null;
        }

        MetricaPerformanceResponse metricaPerformanceResponse = new MetricaPerformanceResponse();

        metricaPerformanceResponse.setId( entity.getId() );
        metricaPerformanceResponse.setEmpresaId( entity.getEmpresaId() );
        metricaPerformanceResponse.setMetrica( entity.getMetrica() );
        metricaPerformanceResponse.setCategoria( entity.getCategoria() );
        metricaPerformanceResponse.setValorNumerico( entity.getValorNumerico() );
        metricaPerformanceResponse.setValorTexto( entity.getValorTexto() );
        metricaPerformanceResponse.setUnidadeMedida( entity.getUnidadeMedida() );
        metricaPerformanceResponse.setDataMedicao( entity.getDataMedicao() );
        metricaPerformanceResponse.setPeriodoReferencia( entity.getPeriodoReferencia() );
        metricaPerformanceResponse.setContextoAdicional( entity.getContextoAdicional() );
        metricaPerformanceResponse.setServidor( entity.getServidor() );
        metricaPerformanceResponse.setVersaoAplicacao( entity.getVersaoAplicacao() );
        metricaPerformanceResponse.setBenchmarkAnterior( entity.getBenchmarkAnterior() );
        metricaPerformanceResponse.setVariacaoPercentual( entity.getVariacaoPercentual() );
        metricaPerformanceResponse.setTendencia( entity.getTendencia() );
        metricaPerformanceResponse.setAlertaGerado( entity.getAlertaGerado() );
        metricaPerformanceResponse.setLimiteAlertaMin( entity.getLimiteAlertaMin() );
        metricaPerformanceResponse.setLimiteAlertaMax( entity.getLimiteAlertaMax() );
        metricaPerformanceResponse.setObservacoes( entity.getObservacoes() );

        return metricaPerformanceResponse;
    }

    @Override
    public void updateEntity(MetricaPerformance entity, MetricaPerformanceRequest request) {
        if ( request == null ) {
            return;
        }

        entity.setAlertaGerado( request.getAlertaGerado() );
        entity.setMetrica( request.getMetrica() );
        entity.setCategoria( request.getCategoria() );
        entity.setValorNumerico( request.getValorNumerico() );
        entity.setValorTexto( request.getValorTexto() );
        entity.setUnidadeMedida( request.getUnidadeMedida() );
        entity.setDataMedicao( request.getDataMedicao() );
        entity.setPeriodoReferencia( request.getPeriodoReferencia() );
        entity.setContextoAdicional( request.getContextoAdicional() );
        entity.setServidor( request.getServidor() );
        entity.setVersaoAplicacao( request.getVersaoAplicacao() );
        entity.setBenchmarkAnterior( request.getBenchmarkAnterior() );
        entity.setVariacaoPercentual( request.getVariacaoPercentual() );
        entity.setTendencia( request.getTendencia() );
        entity.setLimiteAlertaMin( request.getLimiteAlertaMin() );
        entity.setLimiteAlertaMax( request.getLimiteAlertaMax() );
        entity.setObservacoes( request.getObservacoes() );
    }
}
