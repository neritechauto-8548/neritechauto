package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.TipoComissao;
import com.neritech.saas.rh.domain.Funcionario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fin_comissoes_funcionarios")
@Getter
@Setter
public class ComissaoFuncionario extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @Column(name = "ordem_servico_id")
    private Long ordemServicoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fatura_id")
    private Fatura fatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_fatura_id")
    private ItemFatura itemFatura;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comissao", length = 30)
    private TipoComissao tipoComissao;

    @Column(name = "base_calculo", nullable = false)
    private BigDecimal baseCalculo;

    @Column(name = "percentual_comissao", nullable = false)
    private BigDecimal percentualComissao;

    @Column(name = "valor_comissao", nullable = false)
    private BigDecimal valorComissao;

    @Column(name = "data_competencia", nullable = false)
    private LocalDate dataCompetencia;

    @Column(name = "periodo_referencia", length = 7)
    private String periodoReferencia;

    @Column(name = "paga")
    private Boolean paga = false;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "valor_pago")
    private BigDecimal valorPago;

    @Column(name = "desconto_aplicado")
    private BigDecimal descontoAplicado = BigDecimal.ZERO;

    @Column(name = "motivo_desconto", columnDefinition = "TEXT")
    private String motivoDesconto;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "calculada_por")
    private Long calculadaPor;

    @Column(name = "data_calculo")
    private LocalDateTime dataCalculo = LocalDateTime.now();

    @Column(name = "aprovada_por")
    private Long aprovadaPor;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    public Funcionario getFuncionario() {
        return this.funcionario;
    }
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    public Long getOrdemServicoId() {
        return this.ordemServicoId;
    }
    public void setOrdemServicoId(Long ordemServicoId) {
        this.ordemServicoId = ordemServicoId;
    }
    public Fatura getFatura() {
        return this.fatura;
    }
    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }
    public ItemFatura getItemFatura() {
        return this.itemFatura;
    }
    public void setItemFatura(ItemFatura itemFatura) {
        this.itemFatura = itemFatura;
    }
    public TipoComissao getTipoComissao() {
        return this.tipoComissao;
    }
    public void setTipoComissao(TipoComissao tipoComissao) {
        this.tipoComissao = tipoComissao;
    }
    public BigDecimal getBaseCalculo() {
        return this.baseCalculo;
    }
    public void setBaseCalculo(BigDecimal baseCalculo) {
        this.baseCalculo = baseCalculo;
    }
    public BigDecimal getPercentualComissao() {
        return this.percentualComissao;
    }
    public void setPercentualComissao(BigDecimal percentualComissao) {
        this.percentualComissao = percentualComissao;
    }
    public BigDecimal getValorComissao() {
        return this.valorComissao;
    }
    public void setValorComissao(BigDecimal valorComissao) {
        this.valorComissao = valorComissao;
    }
    public LocalDate getDataCompetencia() {
        return this.dataCompetencia;
    }
    public void setDataCompetencia(LocalDate dataCompetencia) {
        this.dataCompetencia = dataCompetencia;
    }
    public String getPeriodoReferencia() {
        return this.periodoReferencia;
    }
    public void setPeriodoReferencia(String periodoReferencia) {
        this.periodoReferencia = periodoReferencia;
    }
    public LocalDate getDataPagamento() {
        return this.dataPagamento;
    }
    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
    public BigDecimal getValorPago() {
        return this.valorPago;
    }
    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }
    public String getMotivoDesconto() {
        return this.motivoDesconto;
    }
    public void setMotivoDesconto(String motivoDesconto) {
        this.motivoDesconto = motivoDesconto;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public Long getCalculadaPor() {
        return this.calculadaPor;
    }
    public void setCalculadaPor(Long calculadaPor) {
        this.calculadaPor = calculadaPor;
    }
    public Long getAprovadaPor() {
        return this.aprovadaPor;
    }
    public void setAprovadaPor(Long aprovadaPor) {
        this.aprovadaPor = aprovadaPor;
    }
    public LocalDateTime getDataAprovacao() {
        return this.dataAprovacao;
    }
    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }
}
