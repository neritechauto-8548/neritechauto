package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.StatusCertificacao;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificacoes")
public class Certificacao extends BaseEntity {

    @Column(name = "funcionario_id", nullable = false)
    private Long funcionarioId;

    @Column(name = "nome_certificacao", nullable = false, length = 255)
    private String nomeCertificacao;

    @Column(name = "entidade_certificadora", nullable = false, length = 255)
    private String entidadeCertificadora;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "numero_certificado", length = 100)
    private String numeroCertificado;

    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(name = "tem_validade")
    private Boolean temValidade = true;

    @Column(name = "carga_horaria")
    private Integer cargaHoraria;

    @Column(name = "nota_obtida", precision = 4, scale = 2)
    private BigDecimal notaObtida;

    @Column(name = "nota_minima_aprovacao", precision = 4, scale = 2)
    private BigDecimal notaMinimaAprovacao;

    @Column(name = "custo_certificacao", precision = 8, scale = 2)
    private BigDecimal custoCertificacao;

    @Column(name = "pago_pela_empresa")
    private Boolean pagoPelaEmpresa = false;

    @Column(name = "arquivo_certificado_url", length = 500)
    private String arquivoCertificadoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusCertificacao status;

    @Column(name = "reconhecida_empresa")
    private Boolean reconhecidaEmpresa = true;

    @Column(name = "adicional_salarial", precision = 8, scale = 2)
    private BigDecimal adicionalSalarial = BigDecimal.ZERO;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "cadastrado_por")
    private Long cadastradoPor;

    // Getters and Setters
    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getNomeCertificacao() {
        return nomeCertificacao;
    }

    public void setNomeCertificacao(String nomeCertificacao) {
        this.nomeCertificacao = nomeCertificacao;
    }

    public String getEntidadeCertificadora() {
        return entidadeCertificadora;
    }

    public void setEntidadeCertificadora(String entidadeCertificadora) {
        this.entidadeCertificadora = entidadeCertificadora;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNumeroCertificado() {
        return numeroCertificado;
    }

    public void setNumeroCertificado(String numeroCertificado) {
        this.numeroCertificado = numeroCertificado;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Boolean getTemValidade() {
        return temValidade;
    }

    public void setTemValidade(Boolean temValidade) {
        this.temValidade = temValidade;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public BigDecimal getNotaObtida() {
        return notaObtida;
    }

    public void setNotaObtida(BigDecimal notaObtida) {
        this.notaObtida = notaObtida;
    }

    public BigDecimal getNotaMinimaAprovacao() {
        return notaMinimaAprovacao;
    }

    public void setNotaMinimaAprovacao(BigDecimal notaMinimaAprovacao) {
        this.notaMinimaAprovacao = notaMinimaAprovacao;
    }

    public BigDecimal getCustoCertificacao() {
        return custoCertificacao;
    }

    public void setCustoCertificacao(BigDecimal custoCertificacao) {
        this.custoCertificacao = custoCertificacao;
    }

    public Boolean getPagoPelaEmpresa() {
        return pagoPelaEmpresa;
    }

    public void setPagoPelaEmpresa(Boolean pagoPelaEmpresa) {
        this.pagoPelaEmpresa = pagoPelaEmpresa;
    }

    public String getArquivoCertificadoUrl() {
        return arquivoCertificadoUrl;
    }

    public void setArquivoCertificadoUrl(String arquivoCertificadoUrl) {
        this.arquivoCertificadoUrl = arquivoCertificadoUrl;
    }

    public StatusCertificacao getStatus() {
        return status;
    }

    public void setStatus(StatusCertificacao status) {
        this.status = status;
    }

    public Boolean getReconhecidaEmpresa() {
        return reconhecidaEmpresa;
    }

    public void setReconhecidaEmpresa(Boolean reconhecidaEmpresa) {
        this.reconhecidaEmpresa = reconhecidaEmpresa;
    }

    public BigDecimal getAdicionalSalarial() {
        return adicionalSalarial;
    }

    public void setAdicionalSalarial(BigDecimal adicionalSalarial) {
        this.adicionalSalarial = adicionalSalarial;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getCadastradoPor() {
        return cadastradoPor;
    }

    public void setCadastradoPor(Long cadastradoPor) {
        this.cadastradoPor = cadastradoPor;
    }
}
