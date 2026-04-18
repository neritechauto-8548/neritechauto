package com.neritech.saas.empresa.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.empresa.domain.enums.PorteEmpresa;
import com.neritech.saas.empresa.domain.enums.RegimeTributario;
import com.neritech.saas.empresa.domain.enums.SituacaoCadastral;
import com.neritech.saas.empresa.domain.enums.TipoEstabelecimento;
import com.neritech.saas.empresa.domain.Empresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "configuracoes_empresa", uniqueConstraints = {
        @UniqueConstraint(name = "uk_configuracao_empresa", columnNames = "empresa_id")
})
public class ConfiguracaoEmpresa extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "regime_tributario", length = 20)
    @Enumerated(EnumType.STRING)
    private RegimeTributario regimeTributario;

    @Size(max = 10, message = "O cÃ³digo CNAE principal deve ter no mÃ¡ximo 10 caracteres")
    @Column(name = "codigo_cnae_principal", length = 10)
    private String codigoCnaePrincipal;

    @Column(name = "codigo_cnae_secundario", columnDefinition = "TEXT")
    private String codigoCnaeSecundario;

    @DecimalMin(value = "0.0", message = "O capital social deve ser maior ou igual a zero")
    @Column(name = "capital_social", precision = 15, scale = 2)
    private BigDecimal capitalSocial;

    @Column(name = "porte_empresa", length = 20)
    @Enumerated(EnumType.STRING)
    private PorteEmpresa porteEmpresa;

    @Column(name = "tipo_estabelecimento", length = 20)
    @Enumerated(EnumType.STRING)
    private TipoEstabelecimento tipoEstabelecimento;

    @Column(name = "situacao_cadastral", length = 20)
    @Enumerated(EnumType.STRING)
    private SituacaoCadastral situacaoCadastral;

    @Column(name = "data_situacao_cadastral")
    private LocalDate dataSituacaoCadastral;

    @Size(max = 255, message = "O motivo da situaÃ§Ã£o cadastral deve ter no mÃ¡ximo 255 caracteres")
    @Column(name = "motivo_situacao_cadastral", length = 255)
    private String motivoSituacaoCadastral;

    @Size(max = 255, message = "A situaÃ§Ã£o especial deve ter no mÃ¡ximo 255 caracteres")
    @Column(name = "situacao_especial", length = 255)
    private String situacaoEspecial;

    @Column(name = "data_situacao_especial")
    private LocalDate dataSituacaoEspecial;

    public ConfiguracaoEmpresa() {
    }

    // Getters and Setters

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public RegimeTributario getRegimeTributario() {
        return regimeTributario;
    }

    public void setRegimeTributario(RegimeTributario regimeTributario) {
        this.regimeTributario = regimeTributario;
    }

    public String getCodigoCnaePrincipal() {
        return codigoCnaePrincipal;
    }

    public void setCodigoCnaePrincipal(String codigoCnaePrincipal) {
        this.codigoCnaePrincipal = codigoCnaePrincipal;
    }

    public String getCodigoCnaeSecundario() {
        return codigoCnaeSecundario;
    }

    public void setCodigoCnaeSecundario(String codigoCnaeSecundario) {
        this.codigoCnaeSecundario = codigoCnaeSecundario;
    }

    public BigDecimal getCapitalSocial() {
        return capitalSocial;
    }

    public void setCapitalSocial(BigDecimal capitalSocial) {
        this.capitalSocial = capitalSocial;
    }

    public PorteEmpresa getPorteEmpresa() {
        return porteEmpresa;
    }

    public void setPorteEmpresa(PorteEmpresa porteEmpresa) {
        this.porteEmpresa = porteEmpresa;
    }

    public TipoEstabelecimento getTipoEstabelecimento() {
        return tipoEstabelecimento;
    }

    public void setTipoEstabelecimento(TipoEstabelecimento tipoEstabelecimento) {
        this.tipoEstabelecimento = tipoEstabelecimento;
    }

    public SituacaoCadastral getSituacaoCadastral() {
        return situacaoCadastral;
    }

    public void setSituacaoCadastral(SituacaoCadastral situacaoCadastral) {
        this.situacaoCadastral = situacaoCadastral;
    }

    public LocalDate getDataSituacaoCadastral() {
        return dataSituacaoCadastral;
    }

    public void setDataSituacaoCadastral(LocalDate dataSituacaoCadastral) {
        this.dataSituacaoCadastral = dataSituacaoCadastral;
    }

    public String getMotivoSituacaoCadastral() {
        return motivoSituacaoCadastral;
    }

    public void setMotivoSituacaoCadastral(String motivoSituacaoCadastral) {
        this.motivoSituacaoCadastral = motivoSituacaoCadastral;
    }

    public String getSituacaoEspecial() {
        return situacaoEspecial;
    }

    public void setSituacaoEspecial(String situacaoEspecial) {
        this.situacaoEspecial = situacaoEspecial;
    }

    public LocalDate getDataSituacaoEspecial() {
        return dataSituacaoEspecial;
    }

    public void setDataSituacaoEspecial(LocalDate dataSituacaoEspecial) {
        this.dataSituacaoEspecial = dataSituacaoEspecial;
    }
}
