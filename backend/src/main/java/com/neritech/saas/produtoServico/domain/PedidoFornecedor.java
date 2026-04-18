package com.neritech.saas.produtoServico.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.produtoServico.domain.enums.StatusPedidoFornecedor;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "pedidos_fornecedor")
public class PedidoFornecedor extends TenantEntity {

    @Column(name = "fornecedor_id", nullable = false)
    private Long fornecedorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", insertable = false, updatable = false)
    private Fornecedor fornecedor;

    @Column(name = "numero_pedido", nullable = false)
    private Long numeroPedido;

    @Column(name = "responsavel", nullable = false, length = 255)
    private String responsavel;

    @Column(name = "data_previsao")
    private LocalDate dataPrevisao;

    @Column(name = "numero_nf", length = 100)
    private String numeroNf;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "descricao_interna", columnDefinition = "TEXT")
    private String descricaoInterna;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusPedidoFornecedor status = StatusPedidoFornecedor.PENDENTE;

    // Getters and Setters

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Long getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Long numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public LocalDate getDataPrevisao() {
        return dataPrevisao;
    }

    public void setDataPrevisao(LocalDate dataPrevisao) {
        this.dataPrevisao = dataPrevisao;
    }

    public String getNumeroNf() {
        return numeroNf;
    }

    public void setNumeroNf(String numeroNf) {
        this.numeroNf = numeroNf;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDescricaoInterna() {
        return descricaoInterna;
    }

    public void setDescricaoInterna(String descricaoInterna) {
        this.descricaoInterna = descricaoInterna;
    }

    public StatusPedidoFornecedor getStatus() {
        return status;
    }

    public void setStatus(StatusPedidoFornecedor status) {
        this.status = status;
    }
}
