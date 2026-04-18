package com.neritech.saas.agendamento.domain;

import com.neritech.saas.agendamento.domain.enums.MotivoReagendamento;
import com.neritech.saas.agendamento.domain.enums.SolicitadoPor;
import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "agd_reagendamentos")
@Getter
@Setter
public class Reagendamento extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_original_id", nullable = false)
    private Agendamento agendamentoOriginal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_novo_id")
    private Agendamento agendamentoNovo;

    @Column(name = "data_original", nullable = false)
    private LocalDate dataOriginal;

    @Column(name = "hora_original", nullable = false)
    private LocalTime horaOriginal;

    @Column(name = "data_novo", nullable = false)
    private LocalDate dataNovo;

    @Column(name = "hora_novo", nullable = false)
    private LocalTime horaNovo;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_reagendamento", length = 30, nullable = false)
    private MotivoReagendamento motivoReagendamento;

    @Column(name = "descricao_motivo", columnDefinition = "TEXT")
    private String descricaoMotivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "solicitado_por", length = 30, nullable = false)
    private SolicitadoPor solicitadoPor;

    @Column(name = "taxa_reagendamento", precision = 6, scale = 2)
    private BigDecimal taxaReagendamento = BigDecimal.ZERO;

    @Column(name = "aprovado_cliente")
    private Boolean aprovadoCliente = false;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "usuario_responsavel")
    private Long usuarioResponsavel;

    @Column(name = "data_reagendamento")
    private LocalDateTime dataReagendamento;

    public Agendamento getAgendamentoOriginal() {
        return this.agendamentoOriginal;
    }
    public void setAgendamentoOriginal(Agendamento agendamentoOriginal) {
        this.agendamentoOriginal = agendamentoOriginal;
    }
    public Agendamento getAgendamentoNovo() {
        return this.agendamentoNovo;
    }
    public void setAgendamentoNovo(Agendamento agendamentoNovo) {
        this.agendamentoNovo = agendamentoNovo;
    }
    public LocalDate getDataOriginal() {
        return this.dataOriginal;
    }
    public void setDataOriginal(LocalDate dataOriginal) {
        this.dataOriginal = dataOriginal;
    }
    public LocalTime getHoraOriginal() {
        return this.horaOriginal;
    }
    public void setHoraOriginal(LocalTime horaOriginal) {
        this.horaOriginal = horaOriginal;
    }
    public LocalDate getDataNovo() {
        return this.dataNovo;
    }
    public void setDataNovo(LocalDate dataNovo) {
        this.dataNovo = dataNovo;
    }
    public LocalTime getHoraNovo() {
        return this.horaNovo;
    }
    public void setHoraNovo(LocalTime horaNovo) {
        this.horaNovo = horaNovo;
    }
    public MotivoReagendamento getMotivoReagendamento() {
        return this.motivoReagendamento;
    }
    public void setMotivoReagendamento(MotivoReagendamento motivoReagendamento) {
        this.motivoReagendamento = motivoReagendamento;
    }
    public String getDescricaoMotivo() {
        return this.descricaoMotivo;
    }
    public void setDescricaoMotivo(String descricaoMotivo) {
        this.descricaoMotivo = descricaoMotivo;
    }
    public SolicitadoPor getSolicitadoPor() {
        return this.solicitadoPor;
    }
    public void setSolicitadoPor(SolicitadoPor solicitadoPor) {
        this.solicitadoPor = solicitadoPor;
    }
    public LocalDateTime getDataAprovacao() {
        return this.dataAprovacao;
    }
    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public Long getUsuarioResponsavel() {
        return this.usuarioResponsavel;
    }
    public void setUsuarioResponsavel(Long usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }
    public LocalDateTime getDataReagendamento() {
        return this.dataReagendamento;
    }
    public void setDataReagendamento(LocalDateTime dataReagendamento) {
        this.dataReagendamento = dataReagendamento;
    }
}
