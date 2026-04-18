package com.neritech.saas.empresa.domain;

import com.neritech.saas.common.audit.BaseEntity;
import com.neritech.saas.empresa.domain.enums.SuporteTecnico;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "planos_assinatura")
public class PlanoAssinatura extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "preco_mensal", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoMensal;

    @Column(name = "preco_anual", precision = 10, scale = 2)
    private BigDecimal precoAnual;

    @Column(name = "max_usuarios")
    private Integer maxUsuarios = 5;

    @Column(name = "max_clientes")
    private Integer maxClientes = 1000;

    @Column(name = "max_veiculos")
    private Integer maxVeiculos = 5000;

    @Column(name = "max_os_mes")
    private Integer maxOsMes = 500;

    @Column(name = "max_produtos")
    private Integer maxProdutos = 10000;

    @Column(name = "max_fornecedores")
    private Integer maxFornecedores = 100;

    @Column(name = "possui_app_mobile")
    private Boolean possuiAppMobile = false;

    @Column(name = "possui_api")
    private Boolean possuiApi = false;

    @Column(name = "possui_integracao_nfe")
    private Boolean possuiIntegracaoNfe = false;

    @Column(name = "possui_backup_automatico")
    private Boolean possuiBackupAutomatico = true;

    @Column(name = "storage_gb")
    private Integer storageGb = 5;

    @Column(name = "suporte_tecnico", length = 20)
    @Enumerated(EnumType.STRING)
    private SuporteTecnico suporteTecnico;

    @Column(name = "periodo_teste_dias")
    private Integer periodoTesteDias = 15;

    @Column(name = "ativo")
    private Boolean ativo = true;

    public PlanoAssinatura() {
    }

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPrecoMensal() {
        return precoMensal;
    }

    public void setPrecoMensal(BigDecimal precoMensal) {
        this.precoMensal = precoMensal;
    }

    public BigDecimal getPrecoAnual() {
        return precoAnual;
    }

    public void setPrecoAnual(BigDecimal precoAnual) {
        this.precoAnual = precoAnual;
    }

    public Integer getMaxUsuarios() {
        return maxUsuarios;
    }

    public void setMaxUsuarios(Integer maxUsuarios) {
        this.maxUsuarios = maxUsuarios;
    }

    public Integer getMaxClientes() {
        return maxClientes;
    }

    public void setMaxClientes(Integer maxClientes) {
        this.maxClientes = maxClientes;
    }

    public Integer getMaxVeiculos() {
        return maxVeiculos;
    }

    public void setMaxVeiculos(Integer maxVeiculos) {
        this.maxVeiculos = maxVeiculos;
    }

    public Integer getMaxOsMes() {
        return maxOsMes;
    }

    public void setMaxOsMes(Integer maxOsMes) {
        this.maxOsMes = maxOsMes;
    }

    public Integer getMaxProdutos() {
        return maxProdutos;
    }

    public void setMaxProdutos(Integer maxProdutos) {
        this.maxProdutos = maxProdutos;
    }

    public Integer getMaxFornecedores() {
        return maxFornecedores;
    }

    public void setMaxFornecedores(Integer maxFornecedores) {
        this.maxFornecedores = maxFornecedores;
    }

    public Boolean getPossuiAppMobile() {
        return possuiAppMobile;
    }

    public void setPossuiAppMobile(Boolean possuiAppMobile) {
        this.possuiAppMobile = possuiAppMobile;
    }

    public Boolean getPossuiApi() {
        return possuiApi;
    }

    public void setPossuiApi(Boolean possuiApi) {
        this.possuiApi = possuiApi;
    }

    public Boolean getPossuiIntegracaoNfe() {
        return possuiIntegracaoNfe;
    }

    public void setPossuiIntegracaoNfe(Boolean possuiIntegracaoNfe) {
        this.possuiIntegracaoNfe = possuiIntegracaoNfe;
    }

    public Boolean getPossuiBackupAutomatico() {
        return possuiBackupAutomatico;
    }

    public void setPossuiBackupAutomatico(Boolean possuiBackupAutomatico) {
        this.possuiBackupAutomatico = possuiBackupAutomatico;
    }

    public Integer getStorageGb() {
        return storageGb;
    }

    public void setStorageGb(Integer storageGb) {
        this.storageGb = storageGb;
    }

    public SuporteTecnico getSuporteTecnico() {
        return suporteTecnico;
    }

    public void setSuporteTecnico(SuporteTecnico suporteTecnico) {
        this.suporteTecnico = suporteTecnico;
    }

    public Integer getPeriodoTesteDias() {
        return periodoTesteDias;
    }

    public void setPeriodoTesteDias(Integer periodoTesteDias) {
        this.periodoTesteDias = periodoTesteDias;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
