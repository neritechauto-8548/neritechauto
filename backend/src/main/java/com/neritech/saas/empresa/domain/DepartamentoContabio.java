package com.neritech.saas.empresa.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "departamentos_contabio")
public class DepartamentoContabio extends BaseEntity {

    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotBlank
    @Size(max = 255)
    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;

    @Column(name = "detalhes", columnDefinition = "TEXT")
    private String detalhes;

    @Column(name = "tipo_operacao", length = 50)
    private String tipoOperacao;

    @Column(name = "supervisor_id")
    private Long supervisorId;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "ordem", nullable = false)
    private Integer ordem = 0;

    @Column(name = "tempo_padrao_minutos")
    private Integer tempoPadraoMinutos;

    @jakarta.persistence.Transient private Long numeroFuncionarios = 0L;
    @jakarta.persistence.Transient private Long numeroCargos = 0L;
    @jakarta.persistence.Transient private Long numeroServicos = 0L;

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDetalhes() { return detalhes; }
    public void setDetalhes(String detalhes) { this.detalhes = detalhes; }
    public String getTipoOperacao() { return tipoOperacao; }
    public void setTipoOperacao(String tipoOperacao) { this.tipoOperacao = tipoOperacao; }
    public Long getSupervisorId() { return supervisorId; }
    public void setSupervisorId(Long supervisorId) { this.supervisorId = supervisorId; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }
    public Integer getTempoPadraoMinutos() { return tempoPadraoMinutos; }
    public void setTempoPadraoMinutos(Integer tempoPadraoMinutos) { this.tempoPadraoMinutos = tempoPadraoMinutos; }
    public Long getNumeroFuncionarios() { return numeroFuncionarios; }
    public void setNumeroFuncionarios(Long value) { this.numeroFuncionarios = value; }
    public Long getNumeroCargos() { return numeroCargos; }
    public void setNumeroCargos(Long value) { this.numeroCargos = value; }
    public Long getNumeroServicos() { return numeroServicos; }
    public void setNumeroServicos(Long value) { this.numeroServicos = value; }
}

