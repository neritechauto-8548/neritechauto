package com.neritech.saas.agendamento.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agd_disponibilidade_agenda")
@Getter
@Setter
public class DisponibilidadeAgenda extends TenantEntity {

    @Column(name = "funcionario_id", nullable = false)
    private Long funcionarioId;

    @Column(name = "data_disponibilidade", nullable = false)
    private LocalDate dataDisponibilidade;

    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    @Column(name = "intervalo_almoco_inicio")
    private LocalTime intervaloAlmocoInicio;

    @Column(name = "intervalo_almoco_fim")
    private LocalTime intervaloAlmocoFim;

    @Column(name = "disponivel")
    private Boolean disponivel = true;

    @Column(name = "capacidade_atendimentos")
    private Integer capacidadeAtendimentos = 1;

    @Column(name = "especialidades_disponiveis", columnDefinition = "TEXT")
    private String especialidadesDisponiveis;

    @Column(name = "tipos_agendamento_aceitos", columnDefinition = "TEXT")
    private String tiposAgendamentoAceitos;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    public Long getFuncionarioId() {
        return this.funcionarioId;
    }
    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }
    public LocalDate getDataDisponibilidade() {
        return this.dataDisponibilidade;
    }
    public void setDataDisponibilidade(LocalDate dataDisponibilidade) {
        this.dataDisponibilidade = dataDisponibilidade;
    }
    public Integer getDiaSemana() {
        return this.diaSemana;
    }
    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
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
    public LocalTime getIntervaloAlmocoInicio() {
        return this.intervaloAlmocoInicio;
    }
    public void setIntervaloAlmocoInicio(LocalTime intervaloAlmocoInicio) {
        this.intervaloAlmocoInicio = intervaloAlmocoInicio;
    }
    public LocalTime getIntervaloAlmocoFim() {
        return this.intervaloAlmocoFim;
    }
    public void setIntervaloAlmocoFim(LocalTime intervaloAlmocoFim) {
        this.intervaloAlmocoFim = intervaloAlmocoFim;
    }
    public String getEspecialidadesDisponiveis() {
        return this.especialidadesDisponiveis;
    }
    public void setEspecialidadesDisponiveis(String especialidadesDisponiveis) {
        this.especialidadesDisponiveis = especialidadesDisponiveis;
    }
    public String getTiposAgendamentoAceitos() {
        return this.tiposAgendamentoAceitos;
    }
    public void setTiposAgendamentoAceitos(String tiposAgendamentoAceitos) {
        this.tiposAgendamentoAceitos = tiposAgendamentoAceitos;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
