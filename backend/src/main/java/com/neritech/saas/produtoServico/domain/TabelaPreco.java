package com.neritech.saas.produtoServico.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.produtoServico.domain.enums.TipoTabelaPreco;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tabelas_precos")
public class TabelaPreco extends TenantEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tabela", length = 20)
    private TipoTabelaPreco tipoTabela;

    @Column(name = "grupo_cliente_id")
    private Long grupoClienteId;

    @Column(name = "margem_lucro_padrao", precision = 5, scale = 2)
    private BigDecimal margemLucroPadrao;

    @Column(name = "desconto_maximo_permitido", precision = 5, scale = 2)
    private BigDecimal descontoMaximoPermitido;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "padrao")
    private Boolean padrao = false;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

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

    public TipoTabelaPreco getTipoTabela() {
        return tipoTabela;
    }

    public void setTipoTabela(TipoTabelaPreco tipoTabela) {
        this.tipoTabela = tipoTabela;
    }

    public Long getGrupoClienteId() {
        return grupoClienteId;
    }

    public void setGrupoClienteId(Long grupoClienteId) {
        this.grupoClienteId = grupoClienteId;
    }

    public BigDecimal getMargemLucroPadrao() {
        return margemLucroPadrao;
    }

    public void setMargemLucroPadrao(BigDecimal margemLucroPadrao) {
        this.margemLucroPadrao = margemLucroPadrao;
    }

    public BigDecimal getDescontoMaximoPermitido() {
        return descontoMaximoPermitido;
    }

    public void setDescontoMaximoPermitido(BigDecimal descontoMaximoPermitido) {
        this.descontoMaximoPermitido = descontoMaximoPermitido;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getPadrao() {
        return padrao;
    }

    public void setPadrao(Boolean padrao) {
        this.padrao = padrao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
