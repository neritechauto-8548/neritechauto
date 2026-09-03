package com.neritech.saas.orcamento.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "estimate_kit_instantiations")
public class OrcamentoKitInstantiation extends TenantEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico orcamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_group_id", nullable = false)
    private OrcamentoServiceGroup group;

    @Column(name = "kit_origin_id", nullable = false)
    private Long kitOriginId;

    @Column(name = "kit_origin_version", nullable = false)
    private Integer kitOriginVersion;

    @Column(name = "idempotency_key", nullable = false, length = 200)
    private String idempotencyKey;

    @Column(name = "request_fingerprint", nullable = false, length = 64)
    private String requestFingerprint;

    public OrdemServico getOrcamento() { return orcamento; }
    public void setOrcamento(OrdemServico orcamento) { this.orcamento = orcamento; }
    public OrcamentoServiceGroup getGroup() { return group; }
    public void setGroup(OrcamentoServiceGroup group) { this.group = group; }
    public Long getKitOriginId() { return kitOriginId; }
    public void setKitOriginId(Long kitOriginId) { this.kitOriginId = kitOriginId; }
    public Integer getKitOriginVersion() { return kitOriginVersion; }
    public void setKitOriginVersion(Integer kitOriginVersion) { this.kitOriginVersion = kitOriginVersion; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getRequestFingerprint() { return requestFingerprint; }
    public void setRequestFingerprint(String requestFingerprint) { this.requestFingerprint = requestFingerprint; }
}
