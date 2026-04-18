package com.neritech.saas.rh.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cargos")
public class Cargo extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "codigo_cbo", length = 10)
    private String codigoCbo;

    @Column(name = "nivel_hierarquico")
    private Integer nivelHierarquico = 1;

    @Column(name = "cargo_superior_id")
    private Long cargoSuperiorId;

    @Column(name = "salario_base_minimo", precision = 10, scale = 2)
    private BigDecimal salarioBaseMinimo;

    @Column(name = "salario_base_maximo", precision = 10, scale = 2)
    private BigDecimal salarioBaseMaximo;

    @Column(name = "requisitos", columnDefinition = "TEXT")
    private String requisitos;

    @Column(name = "responsabilidades", columnDefinition = "TEXT")
    private String responsabilidades;

    @Column(name = "beneficios", columnDefinition = "TEXT")
    private String beneficios;

    @Column(name = "tem_comissao")
    private Boolean temComissao = false;

    @Column(name = "percentual_comissao_padrao", precision = 5, scale = 2)
    private BigDecimal percentualComissaoPadrao;

    @Column(name = "meta_vendas_mensal", precision = 10, scale = 2)
    private BigDecimal metaVendasMensal;

    @Column(name = "carga_horaria_semanal")
    private Integer cargaHorariaSemanal = 44;

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

    public String getCodigoCbo() {
        return codigoCbo;
    }

    public void setCodigoCbo(String codigoCbo) {
        this.codigoCbo = codigoCbo;
    }

    public Integer getNivelHierarquico() {
        return nivelHierarquico;
    }

    public void setNivelHierarquico(Integer nivelHierarquico) {
        this.nivelHierarquico = nivelHierarquico;
    }

    public Long getCargoSuperiorId() {
        return cargoSuperiorId;
    }

    public void setCargoSuperiorId(Long cargoSuperiorId) {
        this.cargoSuperiorId = cargoSuperiorId;
    }

    public BigDecimal getSalarioBaseMinimo() {
        return salarioBaseMinimo;
    }

    public void setSalarioBaseMinimo(BigDecimal salarioBaseMinimo) {
        this.salarioBaseMinimo = salarioBaseMinimo;
    }

    public BigDecimal getSalarioBaseMaximo() {
        return salarioBaseMaximo;
    }

    public void setSalarioBaseMaximo(BigDecimal salarioBaseMaximo) {
        this.salarioBaseMaximo = salarioBaseMaximo;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getResponsabilidades() {
        return responsabilidades;
    }

    public void setResponsabilidades(String responsabilidades) {
        this.responsabilidades = responsabilidades;
    }

    public String getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public Boolean getTemComissao() {
        return temComissao;
    }

    public void setTemComissao(Boolean temComissao) {
        this.temComissao = temComissao;
    }

    public BigDecimal getPercentualComissaoPadrao() {
        return percentualComissaoPadrao;
    }

    public void setPercentualComissaoPadrao(BigDecimal percentualComissaoPadrao) {
        this.percentualComissaoPadrao = percentualComissaoPadrao;
    }

    public BigDecimal getMetaVendasMensal() {
        return metaVendasMensal;
    }

    public void setMetaVendasMensal(BigDecimal metaVendasMensal) {
        this.metaVendasMensal = metaVendasMensal;
    }

    public Integer getCargaHorariaSemanal() {
        return cargaHorariaSemanal;
    }

    public void setCargaHorariaSemanal(Integer cargaHorariaSemanal) {
        this.cargaHorariaSemanal = cargaHorariaSemanal;
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
