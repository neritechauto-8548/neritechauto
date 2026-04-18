package com.neritech.saas.agendamento.domain;

import com.neritech.saas.agendamento.domain.enums.TipoBloqueio;
import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "agd_bloqueios_agenda")
@Getter
@Setter
public class BloqueioAgenda extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "funcionario_id")
    private Long funcionarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_bloqueio", length = 30, nullable = false)
    private TipoBloqueio tipoBloqueio;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fim")
    private LocalTime horaFim;

    @Column(name = "recorrente")
    private Boolean recorrente = false;

    @Column(name = "dias_semana_recorrencia", length = 7)
    private String diasSemanaRecorrencia;

    @Column(name = "motivo", columnDefinition = "TEXT", nullable = false)
    private String motivo;

    @Column(name = "afeta_todos_funcionarios")
    private Boolean afetaTodosFuncionarios = false;

    @Column(name = "funcionarios_afetados", columnDefinition = "TEXT")
    private String funcionariosAfetados;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "ativo")
    private Boolean ativo = true;

    // Campo criadoPor herdado de BaseEntity

    public Long getEmpresaId() {
        return this.empresaId;
    }
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    public Long getFuncionarioId() {
        return this.funcionarioId;
    }
    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }
    public TipoBloqueio getTipoBloqueio() {
        return this.tipoBloqueio;
    }
    public void setTipoBloqueio(TipoBloqueio tipoBloqueio) {
        this.tipoBloqueio = tipoBloqueio;
    }
    public LocalDate getDataInicio() {
        return this.dataInicio;
    }
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }
    public LocalDate getDataFim() {
        return this.dataFim;
    }
    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
    public LocalTime getHoraInicio() {
        return this.horaInicio;
    }
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    public LocalTime getHoraFim() {
        return this.horaFim;
    }
    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }
    public String getDiasSemanaRecorrencia() {
        return this.diasSemanaRecorrencia;
    }
    public void setDiasSemanaRecorrencia(String diasSemanaRecorrencia) {
        this.diasSemanaRecorrencia = diasSemanaRecorrencia;
    }
    public String getMotivo() {
        return this.motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    public String getFuncionariosAfetados() {
        return this.funcionariosAfetados;
    }
    public void setFuncionariosAfetados(String funcionariosAfetados) {
        this.funcionariosAfetados = funcionariosAfetados;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
