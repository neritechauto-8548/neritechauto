package com.neritech.saas.ordemservico.mapper;

import com.neritech.saas.ordemservico.domain.Diagnostico;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.enums.SistemaVeiculo;
import com.neritech.saas.ordemservico.domain.enums.UrgenciaDiagnostico;
import com.neritech.saas.ordemservico.dto.DiagnosticoRequest;
import com.neritech.saas.ordemservico.dto.DiagnosticoResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:06-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class DiagnosticoMapperImpl implements DiagnosticoMapper {

    @Override
    public Diagnostico toEntity(DiagnosticoRequest request) {
        if ( request == null ) {
            return null;
        }

        Diagnostico diagnostico = new Diagnostico();

        diagnostico.setSistemaVeiculo( request.sistemaVeiculo() );
        diagnostico.setComponenteEspecifico( request.componenteEspecifico() );
        diagnostico.setProblemaIdentificado( request.problemaIdentificado() );
        diagnostico.setCausaProvavel( request.causaProvavel() );
        diagnostico.setSolucaoRecomendada( request.solucaoRecomendada() );
        diagnostico.setUrgencia( request.urgencia() );
        diagnostico.setImpactoSeguranca( request.impactoSeguranca() );
        diagnostico.setImpactoDirigibilidade( request.impactoDirigibilidade() );
        diagnostico.setCustoEstimado( request.custoEstimado() );
        diagnostico.setTempoEstimadoReparo( request.tempoEstimadoReparo() );
        diagnostico.setFerramentasNecessarias( request.ferramentasNecessarias() );
        diagnostico.setPecasNecessarias( request.pecasNecessarias() );
        diagnostico.setEvidenciasEncontradas( request.evidenciasEncontradas() );
        diagnostico.setTestesRealizados( request.testesRealizados() );
        diagnostico.setCodigoErro( request.codigoErro() );
        diagnostico.setMecanicoDiagnosticoId( request.mecanicoDiagnosticoId() );
        diagnostico.setFotosDiagnostico( request.fotosDiagnostico() );
        diagnostico.setVideosDiagnostico( request.videosDiagnostico() );
        diagnostico.setAprovadoCliente( request.aprovadoCliente() );
        diagnostico.setObservacoes( request.observacoes() );

        return diagnostico;
    }

    @Override
    public DiagnosticoResponse toResponse(Diagnostico entity) {
        if ( entity == null ) {
            return null;
        }

        Long ordemServicoId = null;
        Long id = null;
        SistemaVeiculo sistemaVeiculo = null;
        String componenteEspecifico = null;
        String problemaIdentificado = null;
        String causaProvavel = null;
        String solucaoRecomendada = null;
        UrgenciaDiagnostico urgencia = null;
        Boolean impactoSeguranca = null;
        Boolean impactoDirigibilidade = null;
        BigDecimal custoEstimado = null;
        Integer tempoEstimadoReparo = null;
        String ferramentasNecessarias = null;
        String pecasNecessarias = null;
        String evidenciasEncontradas = null;
        String testesRealizados = null;
        String codigoErro = null;
        Long mecanicoDiagnosticoId = null;
        LocalDateTime dataDiagnostico = null;
        String fotosDiagnostico = null;
        String videosDiagnostico = null;
        Boolean aprovadoCliente = null;
        LocalDateTime dataAprovacaoCliente = null;
        String observacoes = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;
        Integer versao = null;

        ordemServicoId = entityOrdemServicoId( entity );
        id = entity.getId();
        sistemaVeiculo = entity.getSistemaVeiculo();
        componenteEspecifico = entity.getComponenteEspecifico();
        problemaIdentificado = entity.getProblemaIdentificado();
        causaProvavel = entity.getCausaProvavel();
        solucaoRecomendada = entity.getSolucaoRecomendada();
        urgencia = entity.getUrgencia();
        impactoSeguranca = entity.getImpactoSeguranca();
        impactoDirigibilidade = entity.getImpactoDirigibilidade();
        custoEstimado = entity.getCustoEstimado();
        tempoEstimadoReparo = entity.getTempoEstimadoReparo();
        ferramentasNecessarias = entity.getFerramentasNecessarias();
        pecasNecessarias = entity.getPecasNecessarias();
        evidenciasEncontradas = entity.getEvidenciasEncontradas();
        testesRealizados = entity.getTestesRealizados();
        codigoErro = entity.getCodigoErro();
        mecanicoDiagnosticoId = entity.getMecanicoDiagnosticoId();
        dataDiagnostico = entity.getDataDiagnostico();
        fotosDiagnostico = entity.getFotosDiagnostico();
        videosDiagnostico = entity.getVideosDiagnostico();
        aprovadoCliente = entity.getAprovadoCliente();
        dataAprovacaoCliente = entity.getDataAprovacaoCliente();
        observacoes = entity.getObservacoes();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();
        versao = entity.getVersao();

        DiagnosticoResponse diagnosticoResponse = new DiagnosticoResponse( id, ordemServicoId, sistemaVeiculo, componenteEspecifico, problemaIdentificado, causaProvavel, solucaoRecomendada, urgencia, impactoSeguranca, impactoDirigibilidade, custoEstimado, tempoEstimadoReparo, ferramentasNecessarias, pecasNecessarias, evidenciasEncontradas, testesRealizados, codigoErro, mecanicoDiagnosticoId, dataDiagnostico, fotosDiagnostico, videosDiagnostico, aprovadoCliente, dataAprovacaoCliente, observacoes, dataCadastro, dataAtualizacao, versao );

        return diagnosticoResponse;
    }

    @Override
    public void updateEntityFromRequest(DiagnosticoRequest request, Diagnostico entity) {
        if ( request == null ) {
            return;
        }

        if ( request.sistemaVeiculo() != null ) {
            entity.setSistemaVeiculo( request.sistemaVeiculo() );
        }
        if ( request.componenteEspecifico() != null ) {
            entity.setComponenteEspecifico( request.componenteEspecifico() );
        }
        if ( request.problemaIdentificado() != null ) {
            entity.setProblemaIdentificado( request.problemaIdentificado() );
        }
        if ( request.causaProvavel() != null ) {
            entity.setCausaProvavel( request.causaProvavel() );
        }
        if ( request.solucaoRecomendada() != null ) {
            entity.setSolucaoRecomendada( request.solucaoRecomendada() );
        }
        if ( request.urgencia() != null ) {
            entity.setUrgencia( request.urgencia() );
        }
        if ( request.impactoSeguranca() != null ) {
            entity.setImpactoSeguranca( request.impactoSeguranca() );
        }
        if ( request.impactoDirigibilidade() != null ) {
            entity.setImpactoDirigibilidade( request.impactoDirigibilidade() );
        }
        if ( request.custoEstimado() != null ) {
            entity.setCustoEstimado( request.custoEstimado() );
        }
        if ( request.tempoEstimadoReparo() != null ) {
            entity.setTempoEstimadoReparo( request.tempoEstimadoReparo() );
        }
        if ( request.ferramentasNecessarias() != null ) {
            entity.setFerramentasNecessarias( request.ferramentasNecessarias() );
        }
        if ( request.pecasNecessarias() != null ) {
            entity.setPecasNecessarias( request.pecasNecessarias() );
        }
        if ( request.evidenciasEncontradas() != null ) {
            entity.setEvidenciasEncontradas( request.evidenciasEncontradas() );
        }
        if ( request.testesRealizados() != null ) {
            entity.setTestesRealizados( request.testesRealizados() );
        }
        if ( request.codigoErro() != null ) {
            entity.setCodigoErro( request.codigoErro() );
        }
        if ( request.mecanicoDiagnosticoId() != null ) {
            entity.setMecanicoDiagnosticoId( request.mecanicoDiagnosticoId() );
        }
        if ( request.fotosDiagnostico() != null ) {
            entity.setFotosDiagnostico( request.fotosDiagnostico() );
        }
        if ( request.videosDiagnostico() != null ) {
            entity.setVideosDiagnostico( request.videosDiagnostico() );
        }
        if ( request.aprovadoCliente() != null ) {
            entity.setAprovadoCliente( request.aprovadoCliente() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }

    private Long entityOrdemServicoId(Diagnostico diagnostico) {
        if ( diagnostico == null ) {
            return null;
        }
        OrdemServico ordemServico = diagnostico.getOrdemServico();
        if ( ordemServico == null ) {
            return null;
        }
        Long id = ordemServico.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
