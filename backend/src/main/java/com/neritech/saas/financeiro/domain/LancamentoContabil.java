package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fin_lancamentos_contabeis")
@Getter
@Setter
public class LancamentoContabil extends BaseEntity {

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "numero_lancamento", nullable = false, length = 30)
    private String numeroLancamento;

    @Column(name = "data_lancamento", nullable = false)
    private LocalDate dataLancamento;

    @Column(name = "tipo_lancamento", nullable = false, length = 50)
    private String tipoLancamento;

    @Column(name = "documento_origem", length = 100)
    private String documentoOrigem;

    @Column(name = "historico", nullable = false, columnDefinition = "TEXT")
    private String historico;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_debito_id", nullable = false)
    private PlanoConta contaDebito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_credito_id", nullable = false)
    private PlanoConta contaCredito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_custo_id")
    private CentroCusto centroCusto;

    @Column(name = "usuario_lancamento", nullable = false)
    private Long usuarioLancamento;

    @Column(name = "auditoria_alteracao", columnDefinition = "jsonb")
    private String auditoriaAlteracao;

    public Long getEmpresaId() {
        return this.empresaId;
    }
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    public String getNumeroLancamento() {
        return this.numeroLancamento;
    }
    public void setNumeroLancamento(String numeroLancamento) {
        this.numeroLancamento = numeroLancamento;
    }
    public LocalDate getDataLancamento() {
        return this.dataLancamento;
    }
    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
    public String getTipoLancamento() {
        return this.tipoLancamento;
    }
    public void setTipoLancamento(String tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }
    public String getDocumentoOrigem() {
        return this.documentoOrigem;
    }
    public void setDocumentoOrigem(String documentoOrigem) {
        this.documentoOrigem = documentoOrigem;
    }
    public String getHistorico() {
        return this.historico;
    }
    public void setHistorico(String historico) {
        this.historico = historico;
    }
    public BigDecimal getValorTotal() {
        return this.valorTotal;
    }
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    public PlanoConta getContaDebito() {
        return this.contaDebito;
    }
    public void setContaDebito(PlanoConta contaDebito) {
        this.contaDebito = contaDebito;
    }
    public PlanoConta getContaCredito() {
        return this.contaCredito;
    }
    public void setContaCredito(PlanoConta contaCredito) {
        this.contaCredito = contaCredito;
    }
    public CentroCusto getCentroCusto() {
        return this.centroCusto;
    }
    public void setCentroCusto(CentroCusto centroCusto) {
        this.centroCusto = centroCusto;
    }
    public Long getUsuarioLancamento() {
        return this.usuarioLancamento;
    }
    public void setUsuarioLancamento(Long usuarioLancamento) {
        this.usuarioLancamento = usuarioLancamento;
    }
    public String getAuditoriaAlteracao() {
        return this.auditoriaAlteracao;
    }
    public void setAuditoriaAlteracao(String auditoriaAlteracao) {
        this.auditoriaAlteracao = auditoriaAlteracao;
    }
}
