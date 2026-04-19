package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.KpiOficina;
import com.neritech.saas.relatorios.dto.KpiOficinaRequest;
import com.neritech.saas.relatorios.dto.KpiOficinaResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:52-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class KpiOficinaMapperImpl implements KpiOficinaMapper {

    @Override
    public KpiOficina toEntity(KpiOficinaRequest request) {
        if ( request == null ) {
            return null;
        }

        KpiOficina.KpiOficinaBuilder kpiOficina = KpiOficina.builder();

        kpiOficina.ativo( request.getAtivo() );
        kpiOficina.calculadoAutomaticamente( request.getCalculadoAutomaticamente() );
        kpiOficina.categoria( request.getCategoria() );
        kpiOficina.corIndicador( request.getCorIndicador() );
        kpiOficina.dashboardPublico( request.getDashboardPublico() );
        kpiOficina.dataCalculo( request.getDataCalculo() );
        kpiOficina.descricao( request.getDescricao() );
        kpiOficina.formulaCalculo( request.getFormulaCalculo() );
        kpiOficina.frequenciaCalculo( request.getFrequenciaCalculo() );
        kpiOficina.nomeKpi( request.getNomeKpi() );
        kpiOficina.observacoes( request.getObservacoes() );
        kpiOficina.ordemExibicao( request.getOrdemExibicao() );
        kpiOficina.origemDados( request.getOrigemDados() );
        kpiOficina.percentualMetaAtingido( request.getPercentualMetaAtingido() );
        kpiOficina.periodoReferencia( request.getPeriodoReferencia() );
        kpiOficina.tendencia( request.getTendencia() );
        kpiOficina.unidadeMedida( request.getUnidadeMedida() );
        kpiOficina.valorAtual( request.getValorAtual() );
        kpiOficina.valorMeta( request.getValorMeta() );
        kpiOficina.valorPeriodoAnterior( request.getValorPeriodoAnterior() );
        kpiOficina.variacaoPeriodoAnterior( request.getVariacaoPeriodoAnterior() );

        return kpiOficina.build();
    }

    @Override
    public KpiOficinaResponse toResponse(KpiOficina entity) {
        if ( entity == null ) {
            return null;
        }

        KpiOficinaResponse kpiOficinaResponse = new KpiOficinaResponse();

        kpiOficinaResponse.setId( entity.getId() );
        kpiOficinaResponse.setEmpresaId( entity.getEmpresaId() );
        kpiOficinaResponse.setNomeKpi( entity.getNomeKpi() );
        kpiOficinaResponse.setCategoria( entity.getCategoria() );
        kpiOficinaResponse.setDescricao( entity.getDescricao() );
        kpiOficinaResponse.setFormulaCalculo( entity.getFormulaCalculo() );
        kpiOficinaResponse.setValorAtual( entity.getValorAtual() );
        kpiOficinaResponse.setValorMeta( entity.getValorMeta() );
        kpiOficinaResponse.setValorPeriodoAnterior( entity.getValorPeriodoAnterior() );
        kpiOficinaResponse.setUnidadeMedida( entity.getUnidadeMedida() );
        kpiOficinaResponse.setFrequenciaCalculo( entity.getFrequenciaCalculo() );
        kpiOficinaResponse.setPeriodoReferencia( entity.getPeriodoReferencia() );
        kpiOficinaResponse.setDataCalculo( entity.getDataCalculo() );
        kpiOficinaResponse.setPercentualMetaAtingido( entity.getPercentualMetaAtingido() );
        kpiOficinaResponse.setVariacaoPeriodoAnterior( entity.getVariacaoPeriodoAnterior() );
        kpiOficinaResponse.setTendencia( entity.getTendencia() );
        kpiOficinaResponse.setCorIndicador( entity.getCorIndicador() );
        kpiOficinaResponse.setOrigemDados( entity.getOrigemDados() );
        kpiOficinaResponse.setObservacoes( entity.getObservacoes() );
        kpiOficinaResponse.setAtivo( entity.getAtivo() );
        kpiOficinaResponse.setOrdemExibicao( entity.getOrdemExibicao() );
        kpiOficinaResponse.setDashboardPublico( entity.getDashboardPublico() );
        kpiOficinaResponse.setCalculadoAutomaticamente( entity.getCalculadoAutomaticamente() );
        kpiOficinaResponse.setDataProximaAtualizacao( entity.getDataProximaAtualizacao() );

        return kpiOficinaResponse;
    }

    @Override
    public void updateEntity(KpiOficina entity, KpiOficinaRequest request) {
        if ( request == null ) {
            return;
        }

        entity.setAtivo( request.getAtivo() );
        entity.setCalculadoAutomaticamente( request.getCalculadoAutomaticamente() );
        entity.setDashboardPublico( request.getDashboardPublico() );
        entity.setOrdemExibicao( request.getOrdemExibicao() );
        entity.setNomeKpi( request.getNomeKpi() );
        entity.setCategoria( request.getCategoria() );
        entity.setDescricao( request.getDescricao() );
        entity.setFormulaCalculo( request.getFormulaCalculo() );
        entity.setValorAtual( request.getValorAtual() );
        entity.setValorMeta( request.getValorMeta() );
        entity.setValorPeriodoAnterior( request.getValorPeriodoAnterior() );
        entity.setUnidadeMedida( request.getUnidadeMedida() );
        entity.setFrequenciaCalculo( request.getFrequenciaCalculo() );
        entity.setPeriodoReferencia( request.getPeriodoReferencia() );
        entity.setDataCalculo( request.getDataCalculo() );
        entity.setPercentualMetaAtingido( request.getPercentualMetaAtingido() );
        entity.setVariacaoPeriodoAnterior( request.getVariacaoPeriodoAnterior() );
        entity.setTendencia( request.getTendencia() );
        entity.setCorIndicador( request.getCorIndicador() );
        entity.setOrigemDados( request.getOrigemDados() );
        entity.setObservacoes( request.getObservacoes() );
        entity.setDataProximaAtualizacao( request.getDataProximaAtualizacao() );
    }
}
