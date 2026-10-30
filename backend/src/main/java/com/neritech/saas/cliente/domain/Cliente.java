package com.neritech.saas.cliente.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.cliente.domain.enums.OrigemCliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.rh.domain.enums.Sexo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;

/**
 * Entidade que representa um Cliente no sistema.
 * Pode ser Pessoa Física (CPF) ou Jurídica (CNPJ).
 *
 * <p>
 * Esta entidade é multi-tenant (estende {@link TenantEntity}), garantindo
 * isolamento de dados por empresa.
 * </p>
 *
 * @see TenantEntity
 */
@Entity
@Table(name = "cliente", uniqueConstraints = {
    @UniqueConstraint(name = "uc_cliente_cpf_empresa", columnNames = {"cpf", "empresa_id"}),
    @UniqueConstraint(name = "uc_cliente_cnpj_empresa", columnNames = {"cnpj", "empresa_id"})
})
public class Cliente extends TenantEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false, length = 20)
    private TipoCliente tipoCliente;

    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;

    @Column(name = "nome_fantasia", length = 255)
    private String nomeFantasia;

    @Column(name = "razao_social", length = 255)
    private String razaoSocial;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "cnpj", length = 18)
    private String cnpj;

    @Column(name = "inscricao_estadual", length = 20)
    private String inscricaoEstadual;

    @Column(name = "inscricao_municipal", length = 20)
    private String inscricaoMunicipal;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", length = 1)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem_cliente", length = 20)
    private OrigemCliente origemCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusCliente status;

    @Column(name = "observacoes_gerais", columnDefinition = "TEXT")
    private String observacoesGerais;

    @Column(name = "documento_estrangeiro", length = 80)
    private String documentoEstrangeiro;
    public String getDocumentoEstrangeiro() { return documentoEstrangeiro; }
    public void setDocumentoEstrangeiro(String value) { this.documentoEstrangeiro = value; }

    @Column(name = "pais", length = 80)
    private String pais;
    public String getPais() { return pais; }
    public void setPais(String value) { this.pais = value; }

    @Column(name = "estado_civil", length = 30)
    private String estadoCivil;
    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String value) { this.estadoCivil = value; }

    @Column(name = "profissao", length = 120)
    private String profissao;
    public String getProfissao() { return profissao; }
    public void setProfissao(String value) { this.profissao = value; }

    @Column(name = "numero_apolice", length = 80)
    private String numeroApolice;
    public String getNumeroApolice() { return numeroApolice; }
    public void setNumeroApolice(String value) { this.numeroApolice = value; }

    @Column(name = "limite_credito", precision = 14, scale = 2)
    private java.math.BigDecimal limiteCredito;
    public java.math.BigDecimal getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(java.math.BigDecimal value) { this.limiteCredito = value; }

    @Column(name = "como_nos_conheceu", length = 100)
    private String comoNosConheceu;
    public String getComoNosConheceu() { return comoNosConheceu; }
    public void setComoNosConheceu(String value) { this.comoNosConheceu = value; }

    @Column(name = "site", length = 255)
    private String site;
    public String getSite() { return site; }
    public void setSite(String value) { this.site = value; }

    @Column(name = "locadora_veiculos")
    private Boolean locadoraVeiculos;
    public Boolean getLocadoraVeiculos() { return locadoraVeiculos; }
    public void setLocadoraVeiculos(Boolean value) { this.locadoraVeiculos = value; }

    @Column(name = "orgao_publico")
    private Boolean orgaoPublico;
    public Boolean getOrgaoPublico() { return orgaoPublico; }
    public void setOrgaoPublico(Boolean value) { this.orgaoPublico = value; }

    @Column(name = "retencao_iss")
    private Boolean retencaoIss;
    public Boolean getRetencaoIss() { return retencaoIss; }
    public void setRetencaoIss(Boolean value) { this.retencaoIss = value; }

    @Column(name = "retencao_irrf_csll")
    private Boolean retencaoIrrfCsll;
    public Boolean getRetencaoIrrfCsll() { return retencaoIrrfCsll; }
    public void setRetencaoIrrfCsll(Boolean value) { this.retencaoIrrfCsll = value; }

    @Column(name = "alerta_lgpd")
    private Boolean alertaLgpd;
    public Boolean getAlertaLgpd() { return alertaLgpd; }
    public void setAlertaLgpd(Boolean value) { this.alertaLgpd = value; }

    @Column(name = "autorizacao_lgpd")
    private Boolean autorizacaoLgpd;
    public Boolean getAutorizacaoLgpd() { return autorizacaoLgpd; }
    public void setAutorizacaoLgpd(Boolean value) { this.autorizacaoLgpd = value; }

    @Column(name = "rg", length = 30)
    private String rg;

    public String getRg() { return rg; }
    public void setRg(String value) { this.rg = value; }

    @Column(name = "categoria", length = 60)
    private String categoria;

    public String getCategoria() { return categoria; }
    public void setCategoria(String value) { this.categoria = value; }

    @Column(name = "indicador_isencao", length = 40)
    private String indicadorIsencao;

    public String getIndicadorIsencao() { return indicadorIsencao; }
    public void setIndicadorIsencao(String value) { this.indicadorIsencao = value; }

    @Column(name = "email_secundario", length = 100)
    private String emailSecundario;

    public String getEmailSecundario() { return emailSecundario; }
    public void setEmailSecundario(String value) { this.emailSecundario = value; }

    @Column(name = "outros_contatos", length = 255)
    private String outrosContatos;

    public String getOutrosContatos() { return outrosContatos; }
    public void setOutrosContatos(String value) { this.outrosContatos = value; }

    @Column(name = "enviar_noticia")
    private Boolean enviarNoticia;

    public Boolean getEnviarNoticia() { return enviarNoticia; }
    public void setEnviarNoticia(Boolean value) { this.enviarNoticia = value; }

    @Column(name = "classificacao")
    private Integer classificacao;

    public Integer getClassificacao() { return classificacao; }
    public void setClassificacao(Integer value) { this.classificacao = value; }

    @Column(name = "limite_desconto_pecas")
    private java.math.BigDecimal limiteDescontoPecas;

    public java.math.BigDecimal getLimiteDescontoPecas() { return limiteDescontoPecas; }
    public void setLimiteDescontoPecas(java.math.BigDecimal value) { this.limiteDescontoPecas = value; }

    @Column(name = "limite_desconto_servicos")
    private java.math.BigDecimal limiteDescontoServicos;

    public java.math.BigDecimal getLimiteDescontoServicos() { return limiteDescontoServicos; }
    public void setLimiteDescontoServicos(java.math.BigDecimal value) { this.limiteDescontoServicos = value; }

    @Column(name = "bloquear_se_atrasar")
    private java.math.BigDecimal bloquearSeAtrasar;

    public java.math.BigDecimal getBloquearSeAtrasar() { return bloquearSeAtrasar; }
    public void setBloquearSeAtrasar(java.math.BigDecimal value) { this.bloquearSeAtrasar = value; }

    // Manual setters for specific fields (Lombok workaround)
    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public void setOrigemCliente(OrigemCliente origemCliente) {
        this.origemCliente = origemCliente;
    }

    public void setStatus(StatusCliente status) {
        this.status = status;
    }

    public void setObservacoesGerais(String observacoesGerais) {
        this.observacoesGerais = observacoesGerais;
    }

    // Getters
    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public OrigemCliente getOrigemCliente() {
        return origemCliente;
    }

    public StatusCliente getStatus() {
        return status;
    }

    public String getObservacoesGerais() {
        return observacoesGerais;
    }
}
