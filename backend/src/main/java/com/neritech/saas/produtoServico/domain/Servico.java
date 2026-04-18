package com.neritech.saas.produtoServico.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicos")
public class Servico extends TenantEntity {

    @NotBlank
    @Size(max = 255)
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @NotNull
    @Column(name = "preco_base", precision = 10, scale = 2, nullable = false)
    private BigDecimal precoBase;

    @NotNull
    @Column(name = "custo", precision = 10, scale = 2, nullable = false)
    private BigDecimal custo;

    @Column(name = "instrucoes_execucao", columnDefinition = "TEXT")
    private String instrucoesExecucao;

    @Column(name = "ativo")
    private Boolean ativo = true;

    public Servico() {
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getPrecoBase() { return precoBase; }
    public void setPrecoBase(BigDecimal precoBase) { this.precoBase = precoBase; }

    public BigDecimal getCusto() { return custo; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }

    public String getInstrucoesExecucao() { return instrucoesExecucao; }
    public void setInstrucoesExecucao(String instrucoesExecucao) { this.instrucoesExecucao = instrucoesExecucao; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
