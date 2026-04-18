package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.NivelDominio;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "funcionarios_especialidades")
public class FuncionarioEspecialidade extends BaseEntity {

    @Column(name = "funcionario_id", nullable = false)
    private Long funcionarioId;

    @Column(name = "especialidade_id", nullable = false)
    private Long especialidadeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_dominio", length = 20)
    private NivelDominio nivelDominio;

    @Column(name = "data_certificacao")
    private LocalDate dataCertificacao;

    @Column(name = "numero_certificado", length = 100)
    private String numeroCertificado;

    @Column(name = "entidade_certificadora", length = 255)
    private String entidadeCertificadora;

    @Column(name = "data_validade_certificacao")
    private LocalDate dataValidadeCertificacao;

    @Column(name = "anexo_certificado_url", length = 500)
    private String anexoCertificadoUrl;

    @Column(name = "experiencia_anos")
    private Integer experienciaAnos = 0;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "criado_por")
    private Long criadoPor;

    // Getters and Setters
    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Long getEspecialidadeId() {
        return especialidadeId;
    }

    public void setEspecialidadeId(Long especialidadeId) {
        this.especialidadeId = especialidadeId;
    }

    public NivelDominio getNivelDominio() {
        return nivelDominio;
    }

    public void setNivelDominio(NivelDominio nivelDominio) {
        this.nivelDominio = nivelDominio;
    }

    public LocalDate getDataCertificacao() {
        return dataCertificacao;
    }

    public void setDataCertificacao(LocalDate dataCertificacao) {
        this.dataCertificacao = dataCertificacao;
    }

    public String getNumeroCertificado() {
        return numeroCertificado;
    }

    public void setNumeroCertificado(String numeroCertificado) {
        this.numeroCertificado = numeroCertificado;
    }

    public String getEntidadeCertificadora() {
        return entidadeCertificadora;
    }

    public void setEntidadeCertificadora(String entidadeCertificadora) {
        this.entidadeCertificadora = entidadeCertificadora;
    }

    public LocalDate getDataValidadeCertificacao() {
        return dataValidadeCertificacao;
    }

    public void setDataValidadeCertificacao(LocalDate dataValidadeCertificacao) {
        this.dataValidadeCertificacao = dataValidadeCertificacao;
    }

    public String getAnexoCertificadoUrl() {
        return anexoCertificadoUrl;
    }

    public void setAnexoCertificadoUrl(String anexoCertificadoUrl) {
        this.anexoCertificadoUrl = anexoCertificadoUrl;
    }

    public Integer getExperienciaAnos() {
        return experienciaAnos;
    }

    public void setExperienciaAnos(Integer experienciaAnos) {
        this.experienciaAnos = experienciaAnos;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }
}
