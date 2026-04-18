package com.neritech.saas.financeiro.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.financeiro.domain.enums.StatusFatura;
import com.neritech.saas.financeiro.domain.enums.TipoFatura;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fin_faturas")
@Getter
@Setter
public class Fatura extends TenantEntity {

    @Column(name = "numero_fatura", nullable = false, length = 30)
    private String numeroFatura;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "ordem_servico_id")
    private Long ordemServicoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_fatura", length = 30)
    private TipoFatura tipoFatura;

    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "valor_servicos")
    private BigDecimal valorServicos = BigDecimal.ZERO;

    @Column(name = "valor_produtos")
    private BigDecimal valorProdutos = BigDecimal.ZERO;

    @Column(name = "valor_descontos")
    private BigDecimal valorDescontos = BigDecimal.ZERO;

    @Column(name = "valor_acrescimos")
    private BigDecimal valorAcrescimos = BigDecimal.ZERO;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "valor_pago")
    private BigDecimal valorPago = BigDecimal.ZERO;

    @Column(name = "valor_pendente")
    private BigDecimal valorPendente;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private StatusFatura status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forma_pagamento_id")
    private FormaPagamento formaPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condicao_pagamento_id")
    private CondicaoPagamento condicaoPagamento;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "observacoes_internas", columnDefinition = "TEXT")
    private String observacoesInternas;

    @Column(name = "nfe_numero", length = 20)
    private String nfeNumero;

    @Column(name = "nfe_chave", length = 44)
    private String nfeChave;

    @Column(name = "nfe_url_danfe", length = 500)
    private String nfeUrlDanfe;

    @Column(name = "data_envio_cliente")
    private LocalDateTime dataEnvioCliente;

    @Column(name = "enviada_por_email")
    private Boolean enviadaPorEmail = false;

    @Column(name = "enviada_por_whatsapp")
    private Boolean enviadaPorWhatsapp = false;

    @Column(name = "email_envio")
    private String emailEnvio;

    @Column(name = "whatsapp_envio", length = 20)
    private String whatsappEnvio;

    @Column(name = "boleto_nosso_numero", length = 50)
    private String boletoNossoNumero;

    @Column(name = "boleto_url", length = 500)
    private String boletoUrl;

    @Column(name = "pix_qr_code", columnDefinition = "TEXT")
    private String pixQrCode;

    @Column(name = "pix_codigo", length = 500)
    private String pixCodigo;

    @Column(name = "criado_por")
    private Long criadoPor;
}
