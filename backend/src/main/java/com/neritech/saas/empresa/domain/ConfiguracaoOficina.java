package com.neritech.saas.empresa.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.empresa.domain.Empresa;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "configuracoes_oficina", uniqueConstraints = {
        @UniqueConstraint(name = "uk_config_oficina_empresa", columnNames = "empresa_id")
})
public class ConfiguracaoOficina extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "horario_abertura_seg_sex")
    private LocalTime horarioAberturaSegSex;

    @Column(name = "horario_fechamento_seg_sex")
    private LocalTime horarioFechamentoSegSex;

    @Column(name = "horario_abertura_sabado")
    private LocalTime horarioAberturaSabado;

    @Column(name = "horario_fechamento_sabado")
    private LocalTime horarioFechamentoSabado;

    @Column(name = "funciona_domingo")
    private Boolean funcionaDomingo = false;

    @Column(name = "horario_abertura_domingo")
    private LocalTime horarioAberturaDomingo;

    @Column(name = "horario_fechamento_domingo")
    private LocalTime horarioFechamentoDomingo;

    @Column(name = "tempo_agendamento_padrao")
    private Integer tempoAgendamentoPadrao = 60;

    @Column(name = "antecedencia_minima_agendamento")
    private Integer antecedenciaMinimaAgendamento = 24;

    @Column(name = "permite_agendamento_online")
    private Boolean permiteAgendamentoOnline = true;

    @Column(name = "envia_lembrete_agendamento")
    private Boolean enviaLembreteAgendamento = true;

    @Column(name = "tempo_lembrete_horas")
    private Integer tempoLembreteHoras = 24;

    @Column(name = "margem_lucro_padrao", precision = 5, scale = 2)
    private BigDecimal margemLucroPadrao = new BigDecimal("30.00");

    @Column(name = "dias_garantia_padrao")
    private Integer diasGarantiaPadrao = 90;

    @Column(name = "moeda", length = 3)
    private String moeda = "BRL";

    @Column(name = "formato_data", length = 20)
    private String formatoData = "dd/MM/yyyy";

    @Column(name = "formato_hora", length = 20)
    private String formatoHora = "HH:mm";

    @Column(name = "timezone", length = 50)
    private String timezone = "America/Sao_Paulo";

    public ConfiguracaoOficina() {
    }

    // Getters and Setters
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public LocalTime getHorarioAberturaSegSex() {
        return horarioAberturaSegSex;
    }

    public void setHorarioAberturaSegSex(LocalTime horarioAberturaSegSex) {
        this.horarioAberturaSegSex = horarioAberturaSegSex;
    }

    public LocalTime getHorarioFechamentoSegSex() {
        return horarioFechamentoSegSex;
    }

    public void setHorarioFechamentoSegSex(LocalTime horarioFechamentoSegSex) {
        this.horarioFechamentoSegSex = horarioFechamentoSegSex;
    }

    public LocalTime getHorarioAberturaSabado() {
        return horarioAberturaSabado;
    }

    public void setHorarioAberturaSabado(LocalTime horarioAberturaSabado) {
        this.horarioAberturaSabado = horarioAberturaSabado;
    }

    public LocalTime getHorarioFechamentoSabado() {
        return horarioFechamentoSabado;
    }

    public void setHorarioFechamentoSabado(LocalTime horarioFechamentoSabado) {
        this.horarioFechamentoSabado = horarioFechamentoSabado;
    }

    public Boolean getFuncionaDomingo() {
        return funcionaDomingo;
    }

    public void setFuncionaDomingo(Boolean funcionaDomingo) {
        this.funcionaDomingo = funcionaDomingo;
    }

    public LocalTime getHorarioAberturaDomingo() {
        return horarioAberturaDomingo;
    }

    public void setHorarioAberturaDomingo(LocalTime horarioAberturaDomingo) {
        this.horarioAberturaDomingo = horarioAberturaDomingo;
    }

    public LocalTime getHorarioFechamentoDomingo() {
        return horarioFechamentoDomingo;
    }

    public void setHorarioFechamentoDomingo(LocalTime horarioFechamentoDomingo) {
        this.horarioFechamentoDomingo = horarioFechamentoDomingo;
    }

    public Integer getTempoAgendamentoPadrao() {
        return tempoAgendamentoPadrao;
    }

    public void setTempoAgendamentoPadrao(Integer tempoAgendamentoPadrao) {
        this.tempoAgendamentoPadrao = tempoAgendamentoPadrao;
    }

    public Integer getAntecedenciaMinimaAgendamento() {
        return antecedenciaMinimaAgendamento;
    }

    public void setAntecedenciaMinimaAgendamento(Integer antecedenciaMinimaAgendamento) {
        this.antecedenciaMinimaAgendamento = antecedenciaMinimaAgendamento;
    }

    public Boolean getPermiteAgendamentoOnline() {
        return permiteAgendamentoOnline;
    }

    public void setPermiteAgendamentoOnline(Boolean permiteAgendamentoOnline) {
        this.permiteAgendamentoOnline = permiteAgendamentoOnline;
    }

    public Boolean getEnviaLembreteAgendamento() {
        return enviaLembreteAgendamento;
    }

    public void setEnviaLembreteAgendamento(Boolean enviaLembreteAgendamento) {
        this.enviaLembreteAgendamento = enviaLembreteAgendamento;
    }

    public Integer getTempoLembreteHoras() {
        return tempoLembreteHoras;
    }

    public void setTempoLembreteHoras(Integer tempoLembreteHoras) {
        this.tempoLembreteHoras = tempoLembreteHoras;
    }

    public BigDecimal getMargemLucroPadrao() {
        return margemLucroPadrao;
    }

    public void setMargemLucroPadrao(BigDecimal margemLucroPadrao) {
        this.margemLucroPadrao = margemLucroPadrao;
    }

    public Integer getDiasGarantiaPadrao() {
        return diasGarantiaPadrao;
    }

    public void setDiasGarantiaPadrao(Integer diasGarantiaPadrao) {
        this.diasGarantiaPadrao = diasGarantiaPadrao;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public String getFormatoData() {
        return formatoData;
    }

    public void setFormatoData(String formatoData) {
        this.formatoData = formatoData;
    }

    public String getFormatoHora() {
        return formatoHora;
    }

    public void setFormatoHora(String formatoHora) {
        this.formatoHora = formatoHora;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
