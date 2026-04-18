package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.TipoJustificativa;
import com.neritech.saas.rh.domain.enums.TipoOcorrencia;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "faltas_atrasos")
public class FaltaAtraso extends BaseEntity {

    @Column(name = "funcionario_id", nullable = false)
    private Long funcionarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_ocorrencia", nullable = false, length = 10)
    private TipoOcorrencia tipoOcorrencia;

    @Column(name = "data_ocorrencia", nullable = false)
    private LocalDate dataOcorrencia;

    @Column(name = "horario_previsto")
    private LocalTime horarioPrevisto;

    @Column(name = "horario_real")
    private LocalTime horarioReal;

    @Column(name = "minutos_atraso")
    private Integer minutosAtraso;

    @Column(name = "horas_falta", precision = 4, scale = 2)
    private BigDecimal horasFalta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_justificativa", length = 20)
    private TipoJustificativa tipoJustificativa;

    @Column(name = "justificativa", columnDefinition = "TEXT")
    private String justificativa;

    @Column(name = "justificada")
    private Boolean justificada = false;

    @Column(name = "anexo_comprovante_url", length = 500)
    private String anexoComprovanteUrl;

    @Column(name = "desconto_aplicado")
    private Boolean descontoAplicado = false;

    @Column(name = "valor_desconto", precision = 8, scale = 2)
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @Column(name = "advertencia_aplicada")
    private Boolean advertenciaAplicada = false;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "registrado_por")
    private Long registradoPor;

    @Column(name = "aprovado_por")
    private Long aprovadoPor;

    @Column(name = "data_aprovacao")
    private LocalDate dataAprovacao;

    // Getters and Setters
    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public TipoOcorrencia getTipoOcorrencia() {
        return tipoOcorrencia;
    }

    public void setTipoOcorrencia(TipoOcorrencia tipoOcorrencia) {
        this.tipoOcorrencia = tipoOcorrencia;
    }

    public LocalDate getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(LocalDate dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public LocalTime getHorarioPrevisto() {
        return horarioPrevisto;
    }

    public void setHorarioPrevisto(LocalTime horarioPrevisto) {
        this.horarioPrevisto = horarioPrevisto;
    }

    public LocalTime getHorarioReal() {
        return horarioReal;
    }

    public void setHorarioReal(LocalTime horarioReal) {
        this.horarioReal = horarioReal;
    }

    public Integer getMinutosAtraso() {
        return minutosAtraso;
    }

    public void setMinutosAtraso(Integer minutosAtraso) {
        this.minutosAtraso = minutosAtraso;
    }

    public BigDecimal getHorasFalta() {
        return horasFalta;
    }

    public void setHorasFalta(BigDecimal horasFalta) {
        this.horasFalta = horasFalta;
    }

    public TipoJustificativa getTipoJustificativa() {
        return tipoJustificativa;
    }

    public void setTipoJustificativa(TipoJustificativa tipoJustificativa) {
        this.tipoJustificativa = tipoJustificativa;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Boolean getJustificada() {
        return justificada;
    }

    public void setJustificada(Boolean justificada) {
        this.justificada = justificada;
    }

    public String getAnexoComprovanteUrl() {
        return anexoComprovanteUrl;
    }

    public void setAnexoComprovanteUrl(String anexoComprovanteUrl) {
        this.anexoComprovanteUrl = anexoComprovanteUrl;
    }

    public Boolean getDescontoAplicado() {
        return descontoAplicado;
    }

    public void setDescontoAplicado(Boolean descontoAplicado) {
        this.descontoAplicado = descontoAplicado;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public Boolean getAdvertenciaAplicada() {
        return advertenciaAplicada;
    }

    public void setAdvertenciaAplicada(Boolean advertenciaAplicada) {
        this.advertenciaAplicada = advertenciaAplicada;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(Long registradoPor) {
        this.registradoPor = registradoPor;
    }

    public Long getAprovadoPor() {
        return aprovadoPor;
    }

    public void setAprovadoPor(Long aprovadoPor) {
        this.aprovadoPor = aprovadoPor;
    }

    public LocalDate getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDate dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }
}
