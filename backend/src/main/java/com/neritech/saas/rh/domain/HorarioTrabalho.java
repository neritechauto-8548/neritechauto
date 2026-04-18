package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.TipoHorario;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "horarios_trabalho")
public class HorarioTrabalho extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_horario", length = 20)
    private TipoHorario tipoHorario;

    @Column(name = "hora_entrada_seg")
    private LocalTime horaEntradaSeg;

    @Column(name = "hora_saida_seg")
    private LocalTime horaSaidaSeg;

    @Column(name = "hora_entrada_ter")
    private LocalTime horaEntradaTer;

    @Column(name = "hora_saida_ter")
    private LocalTime horaSaidaTer;

    @Column(name = "hora_entrada_qua")
    private LocalTime horaEntradaQua;

    @Column(name = "hora_saida_qua")
    private LocalTime horaSaidaQua;

    @Column(name = "hora_entrada_qui")
    private LocalTime horaEntradaQui;

    @Column(name = "hora_saida_qui")
    private LocalTime horaSaidaQui;

    @Column(name = "hora_entrada_sex")
    private LocalTime horaEntradaSex;

    @Column(name = "hora_saida_sex")
    private LocalTime horaSaidaSex;

    @Column(name = "hora_entrada_sab")
    private LocalTime horaEntradaSab;

    @Column(name = "hora_saida_sab")
    private LocalTime horaSaidaSab;

    @Column(name = "hora_entrada_dom")
    private LocalTime horaEntradaDom;

    @Column(name = "hora_saida_dom")
    private LocalTime horaSaidaDom;

    @Column(name = "intervalo_almoco_minutos")
    private Integer intervaloAlmocoMinutos = 60;

    @Column(name = "carga_horaria_semanal")
    private Integer cargaHorariaSemanal = 44;

    @Column(name = "tolerancia_atraso_minutos")
    private Integer toleranciaAtrasoMinutos = 10;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "criado_por")
    private Long criadoPor;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoHorario getTipoHorario() {
        return tipoHorario;
    }

    public void setTipoHorario(TipoHorario tipoHorario) {
        this.tipoHorario = tipoHorario;
    }

    public LocalTime getHoraEntradaSeg() {
        return horaEntradaSeg;
    }

    public void setHoraEntradaSeg(LocalTime horaEntradaSeg) {
        this.horaEntradaSeg = horaEntradaSeg;
    }

    public LocalTime getHoraSaidaSeg() {
        return horaSaidaSeg;
    }

    public void setHoraSaidaSeg(LocalTime horaSaidaSeg) {
        this.horaSaidaSeg = horaSaidaSeg;
    }

    public LocalTime getHoraEntradaTer() {
        return horaEntradaTer;
    }

    public void setHoraEntradaTer(LocalTime horaEntradaTer) {
        this.horaEntradaTer = horaEntradaTer;
    }

    public LocalTime getHoraSaidaTer() {
        return horaSaidaTer;
    }

    public void setHoraSaidaTer(LocalTime horaSaidaTer) {
        this.horaSaidaTer = horaSaidaTer;
    }

    public LocalTime getHoraEntradaQua() {
        return horaEntradaQua;
    }

    public void setHoraEntradaQua(LocalTime horaEntradaQua) {
        this.horaEntradaQua = horaEntradaQua;
    }

    public LocalTime getHoraSaidaQua() {
        return horaSaidaQua;
    }

    public void setHoraSaidaQua(LocalTime horaSaidaQua) {
        this.horaSaidaQua = horaSaidaQua;
    }

    public LocalTime getHoraEntradaQui() {
        return horaEntradaQui;
    }

    public void setHoraEntradaQui(LocalTime horaEntradaQui) {
        this.horaEntradaQui = horaEntradaQui;
    }

    public LocalTime getHoraSaidaQui() {
        return horaSaidaQui;
    }

    public void setHoraSaidaQui(LocalTime horaSaidaQui) {
        this.horaSaidaQui = horaSaidaQui;
    }

    public LocalTime getHoraEntradaSex() {
        return horaEntradaSex;
    }

    public void setHoraEntradaSex(LocalTime horaEntradaSex) {
        this.horaEntradaSex = horaEntradaSex;
    }

    public LocalTime getHoraSaidaSex() {
        return horaSaidaSex;
    }

    public void setHoraSaidaSex(LocalTime horaSaidaSex) {
        this.horaSaidaSex = horaSaidaSex;
    }

    public LocalTime getHoraEntradaSab() {
        return horaEntradaSab;
    }

    public void setHoraEntradaSab(LocalTime horaEntradaSab) {
        this.horaEntradaSab = horaEntradaSab;
    }

    public LocalTime getHoraSaidaSab() {
        return horaSaidaSab;
    }

    public void setHoraSaidaSab(LocalTime horaSaidaSab) {
        this.horaSaidaSab = horaSaidaSab;
    }

    public LocalTime getHoraEntradaDom() {
        return horaEntradaDom;
    }

    public void setHoraEntradaDom(LocalTime horaEntradaDom) {
        this.horaEntradaDom = horaEntradaDom;
    }

    public LocalTime getHoraSaidaDom() {
        return horaSaidaDom;
    }

    public void setHoraSaidaDom(LocalTime horaSaidaDom) {
        this.horaSaidaDom = horaSaidaDom;
    }

    public Integer getIntervaloAlmocoMinutos() {
        return intervaloAlmocoMinutos;
    }

    public void setIntervaloAlmocoMinutos(Integer intervaloAlmocoMinutos) {
        this.intervaloAlmocoMinutos = intervaloAlmocoMinutos;
    }

    public Integer getCargaHorariaSemanal() {
        return cargaHorariaSemanal;
    }

    public void setCargaHorariaSemanal(Integer cargaHorariaSemanal) {
        this.cargaHorariaSemanal = cargaHorariaSemanal;
    }

    public Integer getToleranciaAtrasoMinutos() {
        return toleranciaAtrasoMinutos;
    }

    public void setToleranciaAtrasoMinutos(Integer toleranciaAtrasoMinutos) {
        this.toleranciaAtrasoMinutos = toleranciaAtrasoMinutos;
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
