package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.ordemservico.domain.enums.MetodoEnvio;
import com.neritech.saas.ordemservico.domain.enums.StatusOrcamento;
import com.neritech.saas.ordemservico.domain.enums.TipoOrcamento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "orcamentos")
public class Orcamento extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private OrdemServico ordemServico;

    @Column(name = "numero_orcamento", length = 20)
    private String numeroOrcamento;

    @Column(name = "versao")
    private Integer versao = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_orcamento", length = 20)
    private TipoOrcamento tipoOrcamento;

    @Column(name = "valor_servicos", precision = 10, scale = 2)
    private BigDecimal valorServicos = BigDecimal.ZERO;

    @Column(name = "valor_produtos", precision = 10, scale = 2)
    private BigDecimal valorProdutos = BigDecimal.ZERO;

    @Column(name = "valor_mao_obra", precision = 10, scale = 2)
    private BigDecimal valorMaoObra = BigDecimal.ZERO;

    @Column(name = "valor_desconto", precision = 10, scale = 2)
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @Column(name = "valor_acrescimo", precision = 10, scale = 2)
    private BigDecimal valorAcrescimo = BigDecimal.ZERO;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "prazo_validade_dias")
    private Integer prazoValidadeDias = 30;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "prazo_execucao_dias")
    private Integer prazoExecucaoDias;

    @Column(name = "data_prevista_conclusao")
    private LocalDate dataPrevistaConclusao;

    @Column(name = "condicoes_pagamento", columnDefinition = "TEXT")
    private String condicoesPagamento;

    @Column(name = "observacoes_comerciais", columnDefinition = "TEXT")
    private String observacoesComerciais;

    @Column(name = "termos_condicoes", columnDefinition = "TEXT")
    private String termosCondicoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusOrcamento status;

    @Column(name = "data_envio_cliente")
    private LocalDateTime dataEnvioCliente;

    @Column(name = "data_resposta_cliente")
    private LocalDateTime dataRespostaCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_envio", length = 20)
    private MetodoEnvio metodoEnvio;

    @Column(name = "aprovado_por_cliente")
    private String aprovadoPorCliente;

    @Column(name = "documento_cliente", length = 20)
    private String documentoCliente;

    @Column(name = "assinatura_digital_cliente", columnDefinition = "TEXT")
    private String assinaturaDigitalCliente;

    @Column(name = "ip_aprovacao", length = 45)
    private String ipAprovacao;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "motivo_rejeicao", columnDefinition = "TEXT")
    private String motivoRejeicao;

    @Column(name = "responsavel_orcamento_id")
    private Long responsavelOrcamentoId;

    // Getters and Setters
    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public String getNumeroOrcamento() {
        return numeroOrcamento;
    }

    public void setNumeroOrcamento(String numeroOrcamento) {
        this.numeroOrcamento = numeroOrcamento;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public TipoOrcamento getTipoOrcamento() {
        return tipoOrcamento;
    }

    public void setTipoOrcamento(TipoOrcamento tipoOrcamento) {
        this.tipoOrcamento = tipoOrcamento;
    }

    public BigDecimal getValorServicos() {
        return valorServicos;
    }

    public void setValorServicos(BigDecimal valorServicos) {
        this.valorServicos = valorServicos;
    }

    public BigDecimal getValorProdutos() {
        return valorProdutos;
    }

    public void setValorProdutos(BigDecimal valorProdutos) {
        this.valorProdutos = valorProdutos;
    }

    public BigDecimal getValorMaoObra() {
        return valorMaoObra;
    }

    public void setValorMaoObra(BigDecimal valorMaoObra) {
        this.valorMaoObra = valorMaoObra;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorAcrescimo() {
        return valorAcrescimo;
    }

    public void setValorAcrescimo(BigDecimal valorAcrescimo) {
        this.valorAcrescimo = valorAcrescimo;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getPrazoValidadeDias() {
        return prazoValidadeDias;
    }

    public void setPrazoValidadeDias(Integer prazoValidadeDias) {
        this.prazoValidadeDias = prazoValidadeDias;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Integer getPrazoExecucaoDias() {
        return prazoExecucaoDias;
    }

    public void setPrazoExecucaoDias(Integer prazoExecucaoDias) {
        this.prazoExecucaoDias = prazoExecucaoDias;
    }

    public LocalDate getDataPrevistaConclusao() {
        return dataPrevistaConclusao;
    }

    public void setDataPrevistaConclusao(LocalDate dataPrevistaConclusao) {
        this.dataPrevistaConclusao = dataPrevistaConclusao;
    }

    public String getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(String condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public String getObservacoesComerciais() {
        return observacoesComerciais;
    }

    public void setObservacoesComerciais(String observacoesComerciais) {
        this.observacoesComerciais = observacoesComerciais;
    }

    public String getTermosCondicoes() {
        return termosCondicoes;
    }

    public void setTermosCondicoes(String termosCondicoes) {
        this.termosCondicoes = termosCondicoes;
    }

    public StatusOrcamento getStatus() {
        return status;
    }

    public void setStatus(StatusOrcamento status) {
        this.status = status;
    }

    public LocalDateTime getDataEnvioCliente() {
        return dataEnvioCliente;
    }

    public void setDataEnvioCliente(LocalDateTime dataEnvioCliente) {
        this.dataEnvioCliente = dataEnvioCliente;
    }

    public LocalDateTime getDataRespostaCliente() {
        return dataRespostaCliente;
    }

    public void setDataRespostaCliente(LocalDateTime dataRespostaCliente) {
        this.dataRespostaCliente = dataRespostaCliente;
    }

    public MetodoEnvio getMetodoEnvio() {
        return metodoEnvio;
    }

    public void setMetodoEnvio(MetodoEnvio metodoEnvio) {
        this.metodoEnvio = metodoEnvio;
    }

    public String getAprovadoPorCliente() {
        return aprovadoPorCliente;
    }

    public void setAprovadoPorCliente(String aprovadoPorCliente) {
        this.aprovadoPorCliente = aprovadoPorCliente;
    }

    public String getDocumentoCliente() {
        return documentoCliente;
    }

    public void setDocumentoCliente(String documentoCliente) {
        this.documentoCliente = documentoCliente;
    }

    public String getAssinaturaDigitalCliente() {
        return assinaturaDigitalCliente;
    }

    public void setAssinaturaDigitalCliente(String assinaturaDigitalCliente) {
        this.assinaturaDigitalCliente = assinaturaDigitalCliente;
    }

    public String getIpAprovacao() {
        return ipAprovacao;
    }

    public void setIpAprovacao(String ipAprovacao) {
        this.ipAprovacao = ipAprovacao;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public String getMotivoRejeicao() {
        return motivoRejeicao;
    }

    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }

    public Long getResponsavelOrcamentoId() {
        return responsavelOrcamentoId;
    }

    public void setResponsavelOrcamentoId(Long responsavelOrcamentoId) {
        this.responsavelOrcamentoId = responsavelOrcamentoId;
    }
}
