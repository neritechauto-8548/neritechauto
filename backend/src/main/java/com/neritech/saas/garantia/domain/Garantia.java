package com.neritech.saas.garantia.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade que representa uma garantia emitida
 */
@Entity
@Table(name = "gar_garantias")
@Getter
@Setter
public class Garantia extends TenantEntity {

    @Column(name = "numero_garantia", unique = true, nullable = false, length = 30)
    private String numeroGarantia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_garantia_id", nullable = false)
    private TipoGarantia tipoGarantia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "veiculo_id", nullable = false)
    private Long veiculoId;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "dias_garantia", nullable = false)
    private Integer diasGarantia;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private StatusGarantia status;

    @Column(name = "valor_original_servico", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorOriginalServico;

    @Column(name = "valor_cobertura_garantia", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorCoberturaGarantia;

    @Column(name = "kilometragem_inicio")
    private Integer kilometragemInicio;

    @Column(name = "kilometragem_limite")
    private Integer kilometragemLimite;

    @Column(name = "condicoes_especiais", columnDefinition = "TEXT")
    private String condicoesEspeciais;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "certificado_url", length = 500)
    private String certificadoUrl;

    @Column(name = "qr_code_verificacao", columnDefinition = "TEXT")
    private String qrCodeVerificacao;

    @Column(name = "transferida_para_cliente_id")
    private Long transferidaParaClienteId;

    @Column(name = "data_transferencia")
    private LocalDateTime dataTransferencia;

    @Column(name = "motivo_transferencia", columnDefinition = "TEXT")
    private String motivoTransferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renovada_de_garantia_id")
    private Garantia renovadaDeGarantia;

    @Column(name = "data_renovacao")
    private LocalDateTime dataRenovacao;

    @Column(name = "cancelada_por")
    private Long canceladaPor;

    @Column(name = "data_cancelamento")
    private LocalDateTime dataCancelamento;

    @Column(name = "motivo_cancelamento", columnDefinition = "TEXT")
    private String motivoCancelamento;

    @Column(name = "total_acionamentos")
    private Integer totalAcionamentos = 0;

    @Column(name = "valor_total_acionamentos", precision = 10, scale = 2)
    private BigDecimal valorTotalAcionamentos = BigDecimal.ZERO;

    @Column(name = "data_ultimo_acionamento")
    private LocalDateTime dataUltimoAcionamento;

    @Column(name = "alerta_vencimento_enviado")
    private Boolean alertaVencimentoEnviado = false;

    @Column(name = "data_alerta_vencimento")
    private LocalDateTime dataAlertaVencimento;

    @Column(name = "emitida_por")
    private Long emitidaPor;
  
    public String getNumeroGarantia() {
        return this.numeroGarantia;
    }
    public void setNumeroGarantia(String numeroGarantia) {
        this.numeroGarantia = numeroGarantia;
    }
    public TipoGarantia getTipoGarantia() {
        return this.tipoGarantia;
    }
    public void setTipoGarantia(TipoGarantia tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
    }
    public OrdemServico getOrdemServico() {
        return this.ordemServico;
    }
    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }
    public Long getClienteId() {
        return this.clienteId;
    }
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    public Long getVeiculoId() {
        return this.veiculoId;
    }
    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }
    public LocalDate getDataInicio() {
        return this.dataInicio;
    }
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }
    public LocalDate getDataFim() {
        return this.dataFim;
    }
    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
    public Integer getDiasGarantia() {
        return this.diasGarantia;
    }
    public void setDiasGarantia(Integer diasGarantia) {
        this.diasGarantia = diasGarantia;
    }
    public StatusGarantia getStatus() {
        return this.status;
    }
    public void setStatus(StatusGarantia status) {
        this.status = status;
    }
    public BigDecimal getValorOriginalServico() {
        return this.valorOriginalServico;
    }
    public void setValorOriginalServico(BigDecimal valorOriginalServico) {
        this.valorOriginalServico = valorOriginalServico;
    }
    public BigDecimal getValorCoberturaGarantia() {
        return this.valorCoberturaGarantia;
    }
    public void setValorCoberturaGarantia(BigDecimal valorCoberturaGarantia) {
        this.valorCoberturaGarantia = valorCoberturaGarantia;
    }
    public Integer getKilometragemInicio() {
        return this.kilometragemInicio;
    }
    public void setKilometragemInicio(Integer kilometragemInicio) {
        this.kilometragemInicio = kilometragemInicio;
    }
    public Integer getKilometragemLimite() {
        return this.kilometragemLimite;
    }
    public void setKilometragemLimite(Integer kilometragemLimite) {
        this.kilometragemLimite = kilometragemLimite;
    }
    public String getCondicoesEspeciais() {
        return this.condicoesEspeciais;
    }
    public void setCondicoesEspeciais(String condicoesEspeciais) {
        this.condicoesEspeciais = condicoesEspeciais;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public String getCertificadoUrl() {
        return this.certificadoUrl;
    }
    public void setCertificadoUrl(String certificadoUrl) {
        this.certificadoUrl = certificadoUrl;
    }
    public String getQrCodeVerificacao() {
        return this.qrCodeVerificacao;
    }
    public void setQrCodeVerificacao(String qrCodeVerificacao) {
        this.qrCodeVerificacao = qrCodeVerificacao;
    }
    public Long getTransferidaParaClienteId() {
        return this.transferidaParaClienteId;
    }
    public void setTransferidaParaClienteId(Long transferidaParaClienteId) {
        this.transferidaParaClienteId = transferidaParaClienteId;
    }
    public LocalDateTime getDataTransferencia() {
        return this.dataTransferencia;
    }
    public void setDataTransferencia(LocalDateTime dataTransferencia) {
        this.dataTransferencia = dataTransferencia;
    }
    public String getMotivoTransferencia() {
        return this.motivoTransferencia;
    }
    public void setMotivoTransferencia(String motivoTransferencia) {
        this.motivoTransferencia = motivoTransferencia;
    }
    public Garantia getRenovadaDeGarantia() {
        return this.renovadaDeGarantia;
    }
    public void setRenovadaDeGarantia(Garantia renovadaDeGarantia) {
        this.renovadaDeGarantia = renovadaDeGarantia;
    }
    public LocalDateTime getDataRenovacao() {
        return this.dataRenovacao;
    }
    public void setDataRenovacao(LocalDateTime dataRenovacao) {
        this.dataRenovacao = dataRenovacao;
    }
    public Long getCanceladaPor() {
        return this.canceladaPor;
    }
    public void setCanceladaPor(Long canceladaPor) {
        this.canceladaPor = canceladaPor;
    }
    public LocalDateTime getDataCancelamento() {
        return this.dataCancelamento;
    }
    public void setDataCancelamento(LocalDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }
    public String getMotivoCancelamento() {
        return this.motivoCancelamento;
    }
    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }
    public LocalDateTime getDataUltimoAcionamento() {
        return this.dataUltimoAcionamento;
    }
    public void setDataUltimoAcionamento(LocalDateTime dataUltimoAcionamento) {
        this.dataUltimoAcionamento = dataUltimoAcionamento;
    }
    public LocalDateTime getDataAlertaVencimento() {
        return this.dataAlertaVencimento;
    }
    public void setDataAlertaVencimento(LocalDateTime dataAlertaVencimento) {
        this.dataAlertaVencimento = dataAlertaVencimento;
    }
    public Long getEmitidaPor() {
        return this.emitidaPor;
    }
    public void setEmitidaPor(Long emitidaPor) {
        this.emitidaPor = emitidaPor;
    }
}
