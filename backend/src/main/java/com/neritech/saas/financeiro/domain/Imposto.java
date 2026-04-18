package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.financeiro.domain.enums.BaseCalculoImposto;
import com.neritech.saas.financeiro.domain.enums.TipoImposto;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "fin_impostos")
@Getter
@Setter
public class Imposto extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_imposto", length = 30)
    private TipoImposto tipoImposto;

    @Column(name = "nome_imposto", nullable = false, length = 100)
    private String nomeImposto;

    @Column(name = "aliquota_percentual", nullable = false)
    private BigDecimal aliquotaPercentual;

    @Enumerated(EnumType.STRING)
    @Column(name = "base_calculo", length = 30)
    private BaseCalculoImposto baseCalculo;

    @Column(name = "codigo_receita", length = 10)
    private String codigoReceita;

    @Column(name = "aplicavel_regime", columnDefinition = "jsonb")
    private String aplicavelRegime;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "data_inicio_vigencia", nullable = false)
    private LocalDate dataInicioVigencia;

    @Column(name = "data_fim_vigencia")
    private LocalDate dataFimVigencia;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "criado_por")
    private Long criadoPor;

    public Long getEmpresaId() {
        return this.empresaId;
    }
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    public TipoImposto getTipoImposto() {
        return this.tipoImposto;
    }
    public void setTipoImposto(TipoImposto tipoImposto) {
        this.tipoImposto = tipoImposto;
    }
    public String getNomeImposto() {
        return this.nomeImposto;
    }
    public void setNomeImposto(String nomeImposto) {
        this.nomeImposto = nomeImposto;
    }
    public BigDecimal getAliquotaPercentual() {
        return this.aliquotaPercentual;
    }
    public void setAliquotaPercentual(BigDecimal aliquotaPercentual) {
        this.aliquotaPercentual = aliquotaPercentual;
    }
    public BaseCalculoImposto getBaseCalculo() {
        return this.baseCalculo;
    }
    public void setBaseCalculo(BaseCalculoImposto baseCalculo) {
        this.baseCalculo = baseCalculo;
    }
    public String getCodigoReceita() {
        return this.codigoReceita;
    }
    public void setCodigoReceita(String codigoReceita) {
        this.codigoReceita = codigoReceita;
    }
    public String getAplicavelRegime() {
        return this.aplicavelRegime;
    }
    public void setAplicavelRegime(String aplicavelRegime) {
        this.aplicavelRegime = aplicavelRegime;
    }
    public LocalDate getDataInicioVigencia() {
        return this.dataInicioVigencia;
    }
    public void setDataInicioVigencia(LocalDate dataInicioVigencia) {
        this.dataInicioVigencia = dataInicioVigencia;
    }
    public LocalDate getDataFimVigencia() {
        return this.dataFimVigencia;
    }
    public void setDataFimVigencia(LocalDate dataFimVigencia) {
        this.dataFimVigencia = dataFimVigencia;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public Long getCriadoPor() {
        return this.criadoPor;
    }
    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }
}
