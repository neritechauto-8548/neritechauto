package com.neritech.saas.estoque.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.estoque.domain.enums.StatusInventario;
import com.neritech.saas.estoque.domain.enums.TipoInventario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inventarios")
public class Inventario extends TenantEntity {

    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_inventario", length = 20)
    private TipoInventario tipoInventario;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusInventario status = StatusInventario.PLANEJADO;

    @Column(name = "total_itens_planejados")
    private Integer totalItensPlanejados;

    @Column(name = "total_itens_contados")
    private Integer totalItensContados;

    @Column(name = "total_divergencias")
    private Integer totalDivergencias;

    @Column(name = "valor_total_sistema", precision = 15, scale = 2)
    private BigDecimal valorTotalSistema;

    @Column(name = "valor_total_contado", precision = 15, scale = 2)
    private BigDecimal valorTotalContado;

    @Column(name = "diferenca_valor", precision = 15, scale = 2)
    private BigDecimal diferencaValor;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "finalizado_por")
    private Long finalizadoPor;

    // Getters and Setters

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoInventario getTipoInventario() {
        return tipoInventario;
    }

    public void setTipoInventario(TipoInventario tipoInventario) {
        this.tipoInventario = tipoInventario;
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

    public StatusInventario getStatus() {
        return status;
    }

    public void setStatus(StatusInventario status) {
        this.status = status;
    }

    public Integer getTotalItensPlanejados() {
        return totalItensPlanejados;
    }

    public void setTotalItensPlanejados(Integer totalItensPlanejados) {
        this.totalItensPlanejados = totalItensPlanejados;
    }

    public Integer getTotalItensContados() {
        return totalItensContados;
    }

    public void setTotalItensContados(Integer totalItensContados) {
        this.totalItensContados = totalItensContados;
    }

    public Integer getTotalDivergencias() {
        return totalDivergencias;
    }

    public void setTotalDivergencias(Integer totalDivergencias) {
        this.totalDivergencias = totalDivergencias;
    }

    public BigDecimal getValorTotalSistema() {
        return valorTotalSistema;
    }

    public void setValorTotalSistema(BigDecimal valorTotalSistema) {
        this.valorTotalSistema = valorTotalSistema;
    }

    public BigDecimal getValorTotalContado() {
        return valorTotalContado;
    }

    public void setValorTotalContado(BigDecimal valorTotalContado) {
        this.valorTotalContado = valorTotalContado;
    }

    public BigDecimal getDiferencaValor() {
        return diferencaValor;
    }

    public void setDiferencaValor(BigDecimal diferencaValor) {
        this.diferencaValor = diferencaValor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getFinalizadoPor() {
        return finalizadoPor;
    }

    public void setFinalizadoPor(Long finalizadoPor) {
        this.finalizadoPor = finalizadoPor;
    }
}
