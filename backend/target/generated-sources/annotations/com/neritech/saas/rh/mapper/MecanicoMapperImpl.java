package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Mecanico;
import com.neritech.saas.rh.domain.enums.CapacidadeDiagnostico;
import com.neritech.saas.rh.domain.enums.NivelExperiencia;
import com.neritech.saas.rh.dto.MecanicoRequest;
import com.neritech.saas.rh.dto.MecanicoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:10-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class MecanicoMapperImpl implements MecanicoMapper {

    @Override
    public Mecanico toEntity(MecanicoRequest request) {
        if ( request == null ) {
            return null;
        }

        Mecanico mecanico = new Mecanico();

        mecanico.setFuncionarioId( request.funcionarioId() );
        mecanico.setCodigoMecanico( request.codigoMecanico() );
        mecanico.setNivelExperiencia( request.nivelExperiencia() );
        mecanico.setAnosExperiencia( request.anosExperiencia() );
        mecanico.setEspecialidadesPrincipais( request.especialidadesPrincipais() );
        mecanico.setCertificacoesAtivas( request.certificacoesAtivas() );
        mecanico.setProdutividadeMedia( request.produtividadeMedia() );
        mecanico.setQualidadeMedia( request.qualidadeMedia() );
        mecanico.setTempoMedioServico( request.tempoMedioServico() );
        mecanico.setTotalOsExecutadas( request.totalOsExecutadas() );
        mecanico.setTotalRetrabalho( request.totalRetrabalho() );
        mecanico.setPercentualRetrabalho( request.percentualRetrabalho() );
        mecanico.setAvaliacaoMediaCliente( request.avaliacaoMediaCliente() );
        mecanico.setCapacidadeDiagnostico( request.capacidadeDiagnostico() );
        mecanico.setPodeLiderarEquipe( request.podeLiderarEquipe() );
        mecanico.setAutorizadoTestDrive( request.autorizadoTestDrive() );
        mecanico.setAutorizadoEquipamentosEspeciais( request.autorizadoEquipamentosEspeciais() );
        mecanico.setMetaProdutividadeMensal( request.metaProdutividadeMensal() );
        mecanico.setComissaoOsPercentual( request.comissaoOsPercentual() );
        mecanico.setBonusQualidadePercentual( request.bonusQualidadePercentual() );
        mecanico.setObservacoesTecnicas( request.observacoesTecnicas() );
        mecanico.setAtivoExecucao( request.ativoExecucao() );
        mecanico.setDataUltimaAvaliacao( request.dataUltimaAvaliacao() );
        mecanico.setDataProximaAvaliacao( request.dataProximaAvaliacao() );

        return mecanico;
    }

    @Override
    public MecanicoResponse toResponse(Mecanico entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long funcionarioId = null;
        String codigoMecanico = null;
        NivelExperiencia nivelExperiencia = null;
        Integer anosExperiencia = null;
        String especialidadesPrincipais = null;
        String certificacoesAtivas = null;
        BigDecimal produtividadeMedia = null;
        BigDecimal qualidadeMedia = null;
        Integer tempoMedioServico = null;
        Integer totalOsExecutadas = null;
        Integer totalRetrabalho = null;
        BigDecimal percentualRetrabalho = null;
        BigDecimal avaliacaoMediaCliente = null;
        CapacidadeDiagnostico capacidadeDiagnostico = null;
        Boolean podeLiderarEquipe = null;
        Boolean autorizadoTestDrive = null;
        Boolean autorizadoEquipamentosEspeciais = null;
        BigDecimal metaProdutividadeMensal = null;
        BigDecimal comissaoOsPercentual = null;
        BigDecimal bonusQualidadePercentual = null;
        String observacoesTecnicas = null;
        Boolean ativoExecucao = null;
        LocalDate dataUltimaAvaliacao = null;
        LocalDate dataProximaAvaliacao = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        funcionarioId = entity.getFuncionarioId();
        codigoMecanico = entity.getCodigoMecanico();
        nivelExperiencia = entity.getNivelExperiencia();
        anosExperiencia = entity.getAnosExperiencia();
        especialidadesPrincipais = entity.getEspecialidadesPrincipais();
        certificacoesAtivas = entity.getCertificacoesAtivas();
        produtividadeMedia = entity.getProdutividadeMedia();
        qualidadeMedia = entity.getQualidadeMedia();
        tempoMedioServico = entity.getTempoMedioServico();
        totalOsExecutadas = entity.getTotalOsExecutadas();
        totalRetrabalho = entity.getTotalRetrabalho();
        percentualRetrabalho = entity.getPercentualRetrabalho();
        avaliacaoMediaCliente = entity.getAvaliacaoMediaCliente();
        capacidadeDiagnostico = entity.getCapacidadeDiagnostico();
        podeLiderarEquipe = entity.getPodeLiderarEquipe();
        autorizadoTestDrive = entity.getAutorizadoTestDrive();
        autorizadoEquipamentosEspeciais = entity.getAutorizadoEquipamentosEspeciais();
        metaProdutividadeMensal = entity.getMetaProdutividadeMensal();
        comissaoOsPercentual = entity.getComissaoOsPercentual();
        bonusQualidadePercentual = entity.getBonusQualidadePercentual();
        observacoesTecnicas = entity.getObservacoesTecnicas();
        ativoExecucao = entity.getAtivoExecucao();
        dataUltimaAvaliacao = entity.getDataUltimaAvaliacao();
        dataProximaAvaliacao = entity.getDataProximaAvaliacao();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        MecanicoResponse mecanicoResponse = new MecanicoResponse( id, funcionarioId, codigoMecanico, nivelExperiencia, anosExperiencia, especialidadesPrincipais, certificacoesAtivas, produtividadeMedia, qualidadeMedia, tempoMedioServico, totalOsExecutadas, totalRetrabalho, percentualRetrabalho, avaliacaoMediaCliente, capacidadeDiagnostico, podeLiderarEquipe, autorizadoTestDrive, autorizadoEquipamentosEspeciais, metaProdutividadeMensal, comissaoOsPercentual, bonusQualidadePercentual, observacoesTecnicas, ativoExecucao, dataUltimaAvaliacao, dataProximaAvaliacao, dataCadastro, dataAtualizacao );

        return mecanicoResponse;
    }

    @Override
    public void updateEntity(MecanicoRequest request, Mecanico entity) {
        if ( request == null ) {
            return;
        }

        if ( request.funcionarioId() != null ) {
            entity.setFuncionarioId( request.funcionarioId() );
        }
        if ( request.codigoMecanico() != null ) {
            entity.setCodigoMecanico( request.codigoMecanico() );
        }
        if ( request.nivelExperiencia() != null ) {
            entity.setNivelExperiencia( request.nivelExperiencia() );
        }
        if ( request.anosExperiencia() != null ) {
            entity.setAnosExperiencia( request.anosExperiencia() );
        }
        if ( request.especialidadesPrincipais() != null ) {
            entity.setEspecialidadesPrincipais( request.especialidadesPrincipais() );
        }
        if ( request.certificacoesAtivas() != null ) {
            entity.setCertificacoesAtivas( request.certificacoesAtivas() );
        }
        if ( request.produtividadeMedia() != null ) {
            entity.setProdutividadeMedia( request.produtividadeMedia() );
        }
        if ( request.qualidadeMedia() != null ) {
            entity.setQualidadeMedia( request.qualidadeMedia() );
        }
        if ( request.tempoMedioServico() != null ) {
            entity.setTempoMedioServico( request.tempoMedioServico() );
        }
        if ( request.totalOsExecutadas() != null ) {
            entity.setTotalOsExecutadas( request.totalOsExecutadas() );
        }
        if ( request.totalRetrabalho() != null ) {
            entity.setTotalRetrabalho( request.totalRetrabalho() );
        }
        if ( request.percentualRetrabalho() != null ) {
            entity.setPercentualRetrabalho( request.percentualRetrabalho() );
        }
        if ( request.avaliacaoMediaCliente() != null ) {
            entity.setAvaliacaoMediaCliente( request.avaliacaoMediaCliente() );
        }
        if ( request.capacidadeDiagnostico() != null ) {
            entity.setCapacidadeDiagnostico( request.capacidadeDiagnostico() );
        }
        if ( request.podeLiderarEquipe() != null ) {
            entity.setPodeLiderarEquipe( request.podeLiderarEquipe() );
        }
        if ( request.autorizadoTestDrive() != null ) {
            entity.setAutorizadoTestDrive( request.autorizadoTestDrive() );
        }
        if ( request.autorizadoEquipamentosEspeciais() != null ) {
            entity.setAutorizadoEquipamentosEspeciais( request.autorizadoEquipamentosEspeciais() );
        }
        if ( request.metaProdutividadeMensal() != null ) {
            entity.setMetaProdutividadeMensal( request.metaProdutividadeMensal() );
        }
        if ( request.comissaoOsPercentual() != null ) {
            entity.setComissaoOsPercentual( request.comissaoOsPercentual() );
        }
        if ( request.bonusQualidadePercentual() != null ) {
            entity.setBonusQualidadePercentual( request.bonusQualidadePercentual() );
        }
        if ( request.observacoesTecnicas() != null ) {
            entity.setObservacoesTecnicas( request.observacoesTecnicas() );
        }
        if ( request.ativoExecucao() != null ) {
            entity.setAtivoExecucao( request.ativoExecucao() );
        }
        if ( request.dataUltimaAvaliacao() != null ) {
            entity.setDataUltimaAvaliacao( request.dataUltimaAvaliacao() );
        }
        if ( request.dataProximaAvaliacao() != null ) {
            entity.setDataProximaAvaliacao( request.dataProximaAvaliacao() );
        }
    }
}
