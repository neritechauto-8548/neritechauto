package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.rh.domain.enums.TipoEscala;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "escalas_trabalho")
public class EscalaTrabalho extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "funcionario_id", nullable = false)
    private Long funcionarioId;

    @Column(name = "horario_trabalho_id")
    private Long horarioTrabalhoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_escala", length = 20)
    private TipoEscala tipoEscala;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "dias_trabalho")
    private Integer diasTrabalho;

    @Column(name = "dias_folga")
    private Integer diasFolga;

    @Column(name = "funcionarios_incluidos", columnDefinition = "jsonb")
    private String funcionariosIncluidos;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

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

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Long getHorarioTrabalhoId() {
        return horarioTrabalhoId;
    }

    public void setHorarioTrabalhoId(Long horarioTrabalhoId) {
        this.horarioTrabalhoId = horarioTrabalhoId;
    }

    public TipoEscala getTipoEscala() {
        return tipoEscala;
    }

    public void setTipoEscala(TipoEscala tipoEscala) {
        this.tipoEscala = tipoEscala;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Integer getDiasTrabalho() {
        return diasTrabalho;
    }

    public void setDiasTrabalho(Integer diasTrabalho) {
        this.diasTrabalho = diasTrabalho;
    }

    public Integer getDiasFolga() {
        return diasFolga;
    }

    public void setDiasFolga(Integer diasFolga) {
        this.diasFolga = diasFolga;
    }

    public String getFuncionariosIncluidos() {
        return funcionariosIncluidos;
    }

    public void setFuncionariosIncluidos(String funcionariosIncluidos) {
        this.funcionariosIncluidos = funcionariosIncluidos;
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
