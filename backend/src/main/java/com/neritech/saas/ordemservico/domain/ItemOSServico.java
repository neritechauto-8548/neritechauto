package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.ordemservico.domain.enums.StatusExecucao;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "itens_os_servicos")
public class ItemOSServico extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @Column(name = "servico_id")
    private Long servicoId;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "quantidade", precision = 8, scale = 2)
    private BigDecimal quantidade = BigDecimal.ONE;

    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "desconto_percentual", precision = 5, scale = 2)
    private BigDecimal descontoPercentual = BigDecimal.ZERO;

    @Column(name = "desconto_valor", precision = 10, scale = 2)
    private BigDecimal descontoValor = BigDecimal.ZERO;

    @Column(name = "valor_final", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorFinal;

    @Column(name = "mecanico_executor_id")
    private Long mecanicoExecutorId;

    @Column(name = "tempo_execucao_previsto")
    private Integer tempoExecucaoPrevisto;

    @Column(name = "tempo_execucao_real")
    private Integer tempoExecucaoReal;

    @Column(name = "data_inicio_execucao")
    private LocalDateTime dataInicioExecucao;

    @Column(name = "data_fim_execucao")
    private LocalDateTime dataFimExecucao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_execucao", length = 20)
    private StatusExecucao statusExecucao;

    @Column(name = "garantia_dias")
    private Integer garantiaDias = 90;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "aprovado_cliente")
    private Boolean aprovadoCliente = false;

    @Column(name = "data_aprovacao_cliente")
    private LocalDateTime dataAprovacaoCliente;

    @Column(name = "comissao_percentual", precision = 5, scale = 2)
    private BigDecimal comissaoPercentual;

    @Column(name = "comissao_valor", precision = 10, scale = 2)
    private BigDecimal comissaoValor;

    @Column(name = "ordem_execucao")
    private Integer ordemExecucao = 0;

    // Getters and Setters
    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getDescontoPercentual() {
        return descontoPercentual;
    }

    public void setDescontoPercentual(BigDecimal descontoPercentual) {
        this.descontoPercentual = descontoPercentual;
    }

    public BigDecimal getDescontoValor() {
        return descontoValor;
    }

    public void setDescontoValor(BigDecimal descontoValor) {
        this.descontoValor = descontoValor;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

    public Long getMecanicoExecutorId() {
        return mecanicoExecutorId;
    }

    public void setMecanicoExecutorId(Long mecanicoExecutorId) {
        this.mecanicoExecutorId = mecanicoExecutorId;
    }

    public Integer getTempoExecucaoPrevisto() {
        return tempoExecucaoPrevisto;
    }

    public void setTempoExecucaoPrevisto(Integer tempoExecucaoPrevisto) {
        this.tempoExecucaoPrevisto = tempoExecucaoPrevisto;
    }

    public Integer getTempoExecucaoReal() {
        return tempoExecucaoReal;
    }

    public void setTempoExecucaoReal(Integer tempoExecucaoReal) {
        this.tempoExecucaoReal = tempoExecucaoReal;
    }

    public LocalDateTime getDataInicioExecucao() {
        return dataInicioExecucao;
    }

    public void setDataInicioExecucao(LocalDateTime dataInicioExecucao) {
        this.dataInicioExecucao = dataInicioExecucao;
    }

    public LocalDateTime getDataFimExecucao() {
        return dataFimExecucao;
    }

    public void setDataFimExecucao(LocalDateTime dataFimExecucao) {
        this.dataFimExecucao = dataFimExecucao;
    }

    public StatusExecucao getStatusExecucao() {
        return statusExecucao;
    }

    public void setStatusExecucao(StatusExecucao statusExecucao) {
        this.statusExecucao = statusExecucao;
    }

    public Integer getGarantiaDias() {
        return garantiaDias;
    }

    public void setGarantiaDias(Integer garantiaDias) {
        this.garantiaDias = garantiaDias;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getAprovadoCliente() {
        return aprovadoCliente;
    }

    public void setAprovadoCliente(Boolean aprovadoCliente) {
        this.aprovadoCliente = aprovadoCliente;
    }

    public LocalDateTime getDataAprovacaoCliente() {
        return dataAprovacaoCliente;
    }

    public void setDataAprovacaoCliente(LocalDateTime dataAprovacaoCliente) {
        this.dataAprovacaoCliente = dataAprovacaoCliente;
    }

    public BigDecimal getComissaoPercentual() {
        return comissaoPercentual;
    }

    public void setComissaoPercentual(BigDecimal comissaoPercentual) {
        this.comissaoPercentual = comissaoPercentual;
    }

    public BigDecimal getComissaoValor() {
        return comissaoValor;
    }

    public void setComissaoValor(BigDecimal comissaoValor) {
        this.comissaoValor = comissaoValor;
    }

    public Integer getOrdemExecucao() {
        return ordemExecucao;
    }

    public void setOrdemExecucao(Integer ordemExecucao) {
        this.ordemExecucao = ordemExecucao;
    }
}
