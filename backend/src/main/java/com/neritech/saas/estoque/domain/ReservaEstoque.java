package com.neritech.saas.estoque.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.estoque.domain.enums.StatusReserva;
import com.neritech.saas.estoque.domain.enums.TipoReserva;
import com.neritech.saas.produtoServico.domain.Produto;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas_estoque")
public class ReservaEstoque extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "quantidade_reservada", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeReservada;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reserva", length = 30)
    private TipoReserva tipoReserva;

    @Column(name = "documento_id", nullable = false)
    private Long documentoId;

    @Column(name = "documento_tipo", nullable = false, length = 50)
    private String documentoTipo;

    @Column(name = "data_reserva")
    private LocalDateTime dataReserva;

    @Column(name = "data_validade_reserva")
    private LocalDateTime dataValidadeReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusReserva status = StatusReserva.ATIVA;

    @Column(name = "usuario_responsavel")
    private Long usuarioResponsavel;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "data_utilizacao")
    private LocalDateTime dataUtilizacao;

    @Column(name = "data_cancelamento")
    private LocalDateTime dataCancelamento;

    @Column(name = "motivo_cancelamento", columnDefinition = "TEXT")
    private String motivoCancelamento;

    // Getters and Setters
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidadeReservada() {
        return quantidadeReservada;
    }

    public void setQuantidadeReservada(BigDecimal quantidadeReservada) {
        this.quantidadeReservada = quantidadeReservada;
    }

    public TipoReserva getTipoReserva() {
        return tipoReserva;
    }

    public void setTipoReserva(TipoReserva tipoReserva) {
        this.tipoReserva = tipoReserva;
    }

    public Long getDocumentoId() {
        return documentoId;
    }

    public void setDocumentoId(Long documentoId) {
        this.documentoId = documentoId;
    }

    public String getDocumentoTipo() {
        return documentoTipo;
    }

    public void setDocumentoTipo(String documentoTipo) {
        this.documentoTipo = documentoTipo;
    }

    public LocalDateTime getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }

    public LocalDateTime getDataValidadeReserva() {
        return dataValidadeReserva;
    }

    public void setDataValidadeReserva(LocalDateTime dataValidadeReserva) {
        this.dataValidadeReserva = dataValidadeReserva;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public Long getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(Long usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataUtilizacao() {
        return dataUtilizacao;
    }

    public void setDataUtilizacao(LocalDateTime dataUtilizacao) {
        this.dataUtilizacao = dataUtilizacao;
    }

    public LocalDateTime getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(LocalDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }
}
