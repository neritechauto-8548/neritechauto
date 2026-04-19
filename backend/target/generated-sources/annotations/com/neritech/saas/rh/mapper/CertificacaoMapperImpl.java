package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.Certificacao;
import com.neritech.saas.rh.domain.enums.StatusCertificacao;
import com.neritech.saas.rh.dto.CertificacaoRequest;
import com.neritech.saas.rh.dto.CertificacaoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:48-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CertificacaoMapperImpl implements CertificacaoMapper {

    @Override
    public Certificacao toEntity(CertificacaoRequest request) {
        if ( request == null ) {
            return null;
        }

        Certificacao certificacao = new Certificacao();

        certificacao.setFuncionarioId( request.funcionarioId() );
        certificacao.setNomeCertificacao( request.nomeCertificacao() );
        certificacao.setEntidadeCertificadora( request.entidadeCertificadora() );
        certificacao.setCategoria( request.categoria() );
        certificacao.setNumeroCertificado( request.numeroCertificado() );
        certificacao.setDataEmissao( request.dataEmissao() );
        certificacao.setDataValidade( request.dataValidade() );
        certificacao.setTemValidade( request.temValidade() );
        certificacao.setCargaHoraria( request.cargaHoraria() );
        certificacao.setNotaObtida( request.notaObtida() );
        certificacao.setNotaMinimaAprovacao( request.notaMinimaAprovacao() );
        certificacao.setCustoCertificacao( request.custoCertificacao() );
        certificacao.setPagoPelaEmpresa( request.pagoPelaEmpresa() );
        certificacao.setArquivoCertificadoUrl( request.arquivoCertificadoUrl() );
        certificacao.setStatus( request.status() );
        certificacao.setReconhecidaEmpresa( request.reconhecidaEmpresa() );
        certificacao.setAdicionalSalarial( request.adicionalSalarial() );
        certificacao.setObservacoes( request.observacoes() );

        return certificacao;
    }

    @Override
    public CertificacaoResponse toResponse(Certificacao entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long funcionarioId = null;
        String nomeCertificacao = null;
        String entidadeCertificadora = null;
        String categoria = null;
        String numeroCertificado = null;
        LocalDate dataEmissao = null;
        LocalDate dataValidade = null;
        Boolean temValidade = null;
        Integer cargaHoraria = null;
        BigDecimal notaObtida = null;
        BigDecimal notaMinimaAprovacao = null;
        BigDecimal custoCertificacao = null;
        Boolean pagoPelaEmpresa = null;
        String arquivoCertificadoUrl = null;
        StatusCertificacao status = null;
        Boolean reconhecidaEmpresa = null;
        BigDecimal adicionalSalarial = null;
        String observacoes = null;
        Long cadastradoPor = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        funcionarioId = entity.getFuncionarioId();
        nomeCertificacao = entity.getNomeCertificacao();
        entidadeCertificadora = entity.getEntidadeCertificadora();
        categoria = entity.getCategoria();
        numeroCertificado = entity.getNumeroCertificado();
        dataEmissao = entity.getDataEmissao();
        dataValidade = entity.getDataValidade();
        temValidade = entity.getTemValidade();
        cargaHoraria = entity.getCargaHoraria();
        notaObtida = entity.getNotaObtida();
        notaMinimaAprovacao = entity.getNotaMinimaAprovacao();
        custoCertificacao = entity.getCustoCertificacao();
        pagoPelaEmpresa = entity.getPagoPelaEmpresa();
        arquivoCertificadoUrl = entity.getArquivoCertificadoUrl();
        status = entity.getStatus();
        reconhecidaEmpresa = entity.getReconhecidaEmpresa();
        adicionalSalarial = entity.getAdicionalSalarial();
        observacoes = entity.getObservacoes();
        cadastradoPor = entity.getCadastradoPor();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        CertificacaoResponse certificacaoResponse = new CertificacaoResponse( id, funcionarioId, nomeCertificacao, entidadeCertificadora, categoria, numeroCertificado, dataEmissao, dataValidade, temValidade, cargaHoraria, notaObtida, notaMinimaAprovacao, custoCertificacao, pagoPelaEmpresa, arquivoCertificadoUrl, status, reconhecidaEmpresa, adicionalSalarial, observacoes, cadastradoPor, dataCadastro, dataAtualizacao );

        return certificacaoResponse;
    }

    @Override
    public void updateEntity(CertificacaoRequest request, Certificacao entity) {
        if ( request == null ) {
            return;
        }

        if ( request.funcionarioId() != null ) {
            entity.setFuncionarioId( request.funcionarioId() );
        }
        if ( request.nomeCertificacao() != null ) {
            entity.setNomeCertificacao( request.nomeCertificacao() );
        }
        if ( request.entidadeCertificadora() != null ) {
            entity.setEntidadeCertificadora( request.entidadeCertificadora() );
        }
        if ( request.categoria() != null ) {
            entity.setCategoria( request.categoria() );
        }
        if ( request.numeroCertificado() != null ) {
            entity.setNumeroCertificado( request.numeroCertificado() );
        }
        if ( request.dataEmissao() != null ) {
            entity.setDataEmissao( request.dataEmissao() );
        }
        if ( request.dataValidade() != null ) {
            entity.setDataValidade( request.dataValidade() );
        }
        if ( request.temValidade() != null ) {
            entity.setTemValidade( request.temValidade() );
        }
        if ( request.cargaHoraria() != null ) {
            entity.setCargaHoraria( request.cargaHoraria() );
        }
        if ( request.notaObtida() != null ) {
            entity.setNotaObtida( request.notaObtida() );
        }
        if ( request.notaMinimaAprovacao() != null ) {
            entity.setNotaMinimaAprovacao( request.notaMinimaAprovacao() );
        }
        if ( request.custoCertificacao() != null ) {
            entity.setCustoCertificacao( request.custoCertificacao() );
        }
        if ( request.pagoPelaEmpresa() != null ) {
            entity.setPagoPelaEmpresa( request.pagoPelaEmpresa() );
        }
        if ( request.arquivoCertificadoUrl() != null ) {
            entity.setArquivoCertificadoUrl( request.arquivoCertificadoUrl() );
        }
        if ( request.status() != null ) {
            entity.setStatus( request.status() );
        }
        if ( request.reconhecidaEmpresa() != null ) {
            entity.setReconhecidaEmpresa( request.reconhecidaEmpresa() );
        }
        if ( request.adicionalSalarial() != null ) {
            entity.setAdicionalSalarial( request.adicionalSalarial() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
    }
}
