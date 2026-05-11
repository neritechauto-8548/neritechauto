package com.neritech.saas.fiscal.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fis_notas_fiscais")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "ordem_servico_id")
    private Long ordemServicoId;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false, length = 3)
    private String serie;

    @Column(name = "chave_acesso", length = 44)
    private String chaveAcesso;

    @Column(nullable = false, length = 2)
    private String modelo; // 55=NF-e, 65=NFC-e

    @Column(name = "natureza_operacao", nullable = false, length = 60)
    private String naturezaOperacao;

    @Column(name = "tipo_operacao", nullable = false, length = 1)
    private String tipoOperacao; // 0=Entrada, 1=Saída

    @Column(nullable = false, length = 1)
    private String finalidade; // 1=Normal, 2=Complementar, 3=Ajuste, 4=Devolução

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusNfe status;

    @Column(name = "protocolo_autorizacao", length = 60)
    private String protocoloAutorizacao;

    @Column(name = "data_autorizacao")
    private LocalDateTime dataAutorizacao;

    @Column(name = "xml_enviado", columnDefinition = "TEXT")
    private String xmlEnviado;

    @Column(name = "xml_retorno", columnDefinition = "TEXT")
    private String xmlRetorno;

    @Column(name = "xml_proc_nfe", columnDefinition = "TEXT")
    private String xmlProcNfe;

    @Column(name = "motivo_rejeicao", columnDefinition = "TEXT")
    private String motivoRejeicao;

    // ── Destinatário ─────────────────────────────────────────────────────────
    @Column(name = "dest_nome", nullable = false, length = 60)
    private String destNome;

    @Column(name = "dest_cpf_cnpj", nullable = false, length = 14)
    private String destCpfCnpj;

    @Column(name = "dest_ie", length = 14)
    private String destIe;

    @Column(name = "dest_email", length = 60)
    private String destEmail;

    @Column(name = "dest_logradouro", length = 60)
    private String destLogradouro;

    @Column(name = "dest_numero", length = 60)
    private String destNumero;

    @Column(name = "dest_bairro", length = 60)
    private String destBairro;

    @Column(name = "dest_municipio", length = 60)
    private String destMunicipio;

    @Column(name = "dest_uf", length = 2)
    private String destUf;

    @Column(name = "dest_cep", length = 8)
    private String destCep;

    @Column(name = "dest_fone", length = 14)
    private String destFone;

    // ── Totais ────────────────────────────────────────────────────────────────
    @Column(name = "val_produtos", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valProdutos = BigDecimal.ZERO;

    @Column(name = "val_frete", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valFrete = BigDecimal.ZERO;

    @Column(name = "val_seguro", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valSeguro = BigDecimal.ZERO;

    @Column(name = "val_desconto", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valDesconto = BigDecimal.ZERO;

    @Column(name = "val_outras", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valOutras = BigDecimal.ZERO;

    @Column(name = "val_ipi", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valIpi = BigDecimal.ZERO;

    @Column(name = "val_icms", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valIcms = BigDecimal.ZERO;

    @Column(name = "val_pis", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valPis = BigDecimal.ZERO;

    @Column(name = "val_cofins", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valCofins = BigDecimal.ZERO;

    @Column(name = "val_total_nota", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valTotalNota = BigDecimal.ZERO;

    // ── Auditoria ─────────────────────────────────────────────────────────────
    @Column(name = "criado_em", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em", nullable = false)
    @UpdateTimestamp
    private LocalDateTime atualizadoEm;

    @Column(name = "criado_por", length = 100)
    private String criadoPor;

    @Version
    private Long versao;

    // ── Relacionamentos ───────────────────────────────────────────────────────
    @OneToMany(mappedBy = "nota", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ItemNota> itens = new ArrayList<>();

    @OneToMany(mappedBy = "nota", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<EventoNfe> eventos = new ArrayList<>();

    public enum StatusNfe {
        PENDENTE, AUTORIZADA, CANCELADA, REJEITADA, DENEGADA, PROCESSANDO
    }
}
