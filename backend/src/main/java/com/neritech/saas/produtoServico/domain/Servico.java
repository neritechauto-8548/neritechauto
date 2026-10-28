package com.neritech.saas.produtoServico.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicos")
public class Servico extends TenantEntity {

    @Size(max = 50)
    @Column(name = "codigo", length = 50)
    private String codigo;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @NotNull
    @Column(name = "area_id", nullable = false)
    private Long areaId;

    @NotBlank
    @Size(max = 30)
    @Column(name = "unidade", nullable = false, length = 30)
    private String unidade;

    @NotNull
    @DecimalMin(value = "0.01")
    @Column(name = "tempo_previsto_horas", precision = 8, scale = 2, nullable = false)
    private BigDecimal tempoPrevistoHoras;

    @NotNull
    @Column(name = "preco_base", precision = 10, scale = 2, nullable = false)
    private BigDecimal precoBase;

    @NotNull
    @Column(name = "custo", precision = 10, scale = 2, nullable = false)
    private BigDecimal custo;

    @DecimalMin(value = "0.0")
    @Column(name = "aliquota_iss", precision = 5, scale = 2)
    private BigDecimal aliquotaIss;

    @Size(max = 30)
    @Column(name = "codigo_servico_municipal", length = 30)
    private String codigoServicoMunicipal;

    @Column(name = "instrucoes_execucao", columnDefinition = "TEXT")
    private String instrucoesExecucao;

    @Column(name = "ativo")
    private Boolean ativo = true;

    public Servico() {
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public BigDecimal getTempoPrevistoHoras() { return tempoPrevistoHoras; }
    public void setTempoPrevistoHoras(BigDecimal tempoPrevistoHoras) { this.tempoPrevistoHoras = tempoPrevistoHoras; }

    public BigDecimal getPrecoBase() { return precoBase; }
    public void setPrecoBase(BigDecimal precoBase) { this.precoBase = precoBase; }

    public BigDecimal getCusto() { return custo; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }
    public BigDecimal getAliquotaIss() { return aliquotaIss; }
    public void setAliquotaIss(BigDecimal aliquotaIss) { this.aliquotaIss = aliquotaIss; }
    public String getCodigoServicoMunicipal() { return codigoServicoMunicipal; }
    public void setCodigoServicoMunicipal(String codigoServicoMunicipal) { this.codigoServicoMunicipal = codigoServicoMunicipal; }

    public String getInstrucoesExecucao() { return instrucoesExecucao; }
    public void setInstrucoesExecucao(String instrucoesExecucao) { this.instrucoesExecucao = instrucoesExecucao; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
