package com.neritech.saas.estoque.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.comunicacao.domain.enums.TipoAlerta;
import com.neritech.saas.estoque.domain.enums.NivelPrioridade;
import com.neritech.saas.estoque.domain.enums.StatusAlerta;
import com.neritech.saas.produtoServico.domain.Produto;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_estoque")
public class AlertaEstoque extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alerta", nullable = false, length = 30)
    private TipoAlerta tipoAlerta;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_prioridade", length = 20)
    private NivelPrioridade nivelPrioridade;

    @Column(name = "quantidade_atual", precision = 10, scale = 2)
    private BigDecimal quantidadeAtual;

    @Column(name = "quantidade_referencia", precision = 10, scale = 2)
    private BigDecimal quantidadeReferencia;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "dias_parado")
    private Integer diasParado;

    @Column(name = "valor_envolvido", precision = 15, scale = 2)
    private BigDecimal valorEnvolvido;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusAlerta status = StatusAlerta.ATIVO;

    @Column(name = "usuario_responsavel")
    private Long usuarioResponsavel;

    @Column(name = "data_resolucao")
    private LocalDateTime dataResolucao;

    @Column(name = "observacoes_resolucao", columnDefinition = "TEXT")
    private String observacoesResolucao;

    @Column(name = "notificacao_enviada")
    private Boolean notificacaoEnviada = false;

    @Column(name = "data_notificacao")
    private LocalDateTime dataNotificacao;

    // Getters and Setters
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public TipoAlerta getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(TipoAlerta tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public NivelPrioridade getNivelPrioridade() {
        return nivelPrioridade;
    }

    public void setNivelPrioridade(NivelPrioridade nivelPrioridade) {
        this.nivelPrioridade = nivelPrioridade;
    }

    public BigDecimal getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(BigDecimal quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public BigDecimal getQuantidadeReferencia() {
        return quantidadeReferencia;
    }

    public void setQuantidadeReferencia(BigDecimal quantidadeReferencia) {
        this.quantidadeReferencia = quantidadeReferencia;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Integer getDiasParado() {
        return diasParado;
    }

    public void setDiasParado(Integer diasParado) {
        this.diasParado = diasParado;
    }

    public BigDecimal getValorEnvolvido() {
        return valorEnvolvido;
    }

    public void setValorEnvolvido(BigDecimal valorEnvolvido) {
        this.valorEnvolvido = valorEnvolvido;
    }

    public StatusAlerta getStatus() {
        return status;
    }

    public void setStatus(StatusAlerta status) {
        this.status = status;
    }

    public Long getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(Long usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    public LocalDateTime getDataResolucao() {
        return dataResolucao;
    }

    public void setDataResolucao(LocalDateTime dataResolucao) {
        this.dataResolucao = dataResolucao;
    }

    public String getObservacoesResolucao() {
        return observacoesResolucao;
    }

    public void setObservacoesResolucao(String observacoesResolucao) {
        this.observacoesResolucao = observacoesResolucao;
    }

    public Boolean getNotificacaoEnviada() {
        return notificacaoEnviada;
    }

    public void setNotificacaoEnviada(Boolean notificacaoEnviada) {
        this.notificacaoEnviada = notificacaoEnviada;
    }

    public LocalDateTime getDataNotificacao() {
        return dataNotificacao;
    }

    public void setDataNotificacao(LocalDateTime dataNotificacao) {
        this.dataNotificacao = dataNotificacao;
    }
}
