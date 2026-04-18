package com.neritech.saas.agendamento.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "agd_no_shows")
@Getter
@Setter
public class NoShow extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDate dataAgendamento;

    @Column(name = "hora_agendamento", nullable = false)
    private LocalTime horaAgendamento;

    @Column(name = "tempo_tolerancia_minutos")
    private Integer tempoToleranciaMinutos = 15;

    @Column(name = "tentativas_contato")
    private Integer tentativasContato = 0;

    @Column(name = "meio_contato_tentado", columnDefinition = "TEXT")
    private String meioContatoTentado;

    @Column(name = "reagendado")
    private Boolean reagendado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novo_agendamento_id")
    private Agendamento novoAgendamento;

    @Column(name = "taxa_no_show", precision = 6, scale = 2)
    private BigDecimal taxaNoShow = BigDecimal.ZERO;

    @Column(name = "taxa_aplicada")
    private Boolean taxaAplicada = false;

    @Column(name = "motivo_declarado", columnDefinition = "TEXT")
    private String motivoDeclarado;

    @Column(name = "justificativa_aceita")
    private Boolean justificativaAceita = false;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "registrado_por")
    private Long registradoPor;

    @Column(name = "data_registro")
    private LocalDateTime dataRegistro;

    public Agendamento getAgendamento() {
        return this.agendamento;
    }
    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }
    public Long getClienteId() {
        return this.clienteId;
    }
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    public LocalDate getDataAgendamento() {
        return this.dataAgendamento;
    }
    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }
    public LocalTime getHoraAgendamento() {
        return this.horaAgendamento;
    }
    public void setHoraAgendamento(LocalTime horaAgendamento) {
        this.horaAgendamento = horaAgendamento;
    }
    public String getMeioContatoTentado() {
        return this.meioContatoTentado;
    }
    public void setMeioContatoTentado(String meioContatoTentado) {
        this.meioContatoTentado = meioContatoTentado;
    }
    public Agendamento getNovoAgendamento() {
        return this.novoAgendamento;
    }
    public void setNovoAgendamento(Agendamento novoAgendamento) {
        this.novoAgendamento = novoAgendamento;
    }
    public String getMotivoDeclarado() {
        return this.motivoDeclarado;
    }
    public void setMotivoDeclarado(String motivoDeclarado) {
        this.motivoDeclarado = motivoDeclarado;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public Long getRegistradoPor() {
        return this.registradoPor;
    }
    public void setRegistradoPor(Long registradoPor) {
        this.registradoPor = registradoPor;
    }
    public LocalDateTime getDataRegistro() {
        return this.dataRegistro;
    }
    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
