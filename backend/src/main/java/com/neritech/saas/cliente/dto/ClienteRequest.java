package com.neritech.saas.cliente.dto;

import com.neritech.saas.cliente.domain.enums.OrigemCliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.rh.domain.enums.Sexo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(name = "ClienteRequest", description = "Dados para criar/atualizar cliente")
public class ClienteRequest {
    @Schema(description = "Tipo do cliente", example = "PESSOA_FISICA")
    @NotNull
    private TipoCliente tipoCliente;

    @Schema(description = "Nome completo (PF)", example = "João da Silva")
    @NotBlank
    @Size(max = 255)
    private String nomeCompleto;

    @Schema(description = "Nome fantasia (PJ)", example = "Loja XPTO")
    private String nomeFantasia;

    @Schema(description = "Razão social (PJ)", example = "Loja XPTO LTDA")
    private String razaoSocial;

    @Schema(description = "Email do cliente", example = "joao@email.com")
    @Email
    @Size(max = 100)
    private String email;

    @Schema(description = "CPF (PF)", example = "123.456.789-00")
    private String cpf;

    @Schema(description = "CNPJ (PJ)", example = "12.345.678/0001-90")
    private String cnpj;

    @Schema(description = "Inscrição estadual", example = "1234567890")
    private String inscricaoEstadual;

    @Schema(description = "Inscrição municipal", example = "987654321")
    private String inscricaoMunicipal;

    @Schema(description = "Data de nascimento", example = "1990-05-20")
    private LocalDate dataNascimento;

    @Schema(description = "Sexo", example = "M")
    private Sexo sexo;

    @Schema(description = "Origem do cliente", example = "INDICACAO")
    private OrigemCliente origemCliente;

    @Schema(description = "Status do cliente", example = "ATIVO")
    private StatusCliente status;

    @Schema(description = "Observações gerais")
    private String observacoesGerais;

    @Size(max = 80)
        private String documentoEstrangeiro;
    public String getDocumentoEstrangeiro() { return documentoEstrangeiro; }
    public void setDocumentoEstrangeiro(String value) { this.documentoEstrangeiro = value; }

    @Size(max = 80)
        private String pais;
    public String getPais() { return pais; }
    public void setPais(String value) { this.pais = value; }

    @Size(max = 30)
        private String estadoCivil;
    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String value) { this.estadoCivil = value; }

    @Size(max = 120)
        private String profissao;
    public String getProfissao() { return profissao; }
    public void setProfissao(String value) { this.profissao = value; }

    @Size(max = 80)
        private String numeroApolice;
    public String getNumeroApolice() { return numeroApolice; }
    public void setNumeroApolice(String value) { this.numeroApolice = value; }

    @jakarta.validation.constraints.DecimalMin("0")
        private java.math.BigDecimal limiteCredito;
    public java.math.BigDecimal getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(java.math.BigDecimal value) { this.limiteCredito = value; }

    @Size(max = 100)
        private String comoNosConheceu;
    public String getComoNosConheceu() { return comoNosConheceu; }
    public void setComoNosConheceu(String value) { this.comoNosConheceu = value; }

    @Size(max = 255)
        private String site;
    public String getSite() { return site; }
    public void setSite(String value) { this.site = value; }

        private Boolean locadoraVeiculos;
    public Boolean getLocadoraVeiculos() { return locadoraVeiculos; }
    public void setLocadoraVeiculos(Boolean value) { this.locadoraVeiculos = value; }

        private Boolean orgaoPublico;
    public Boolean getOrgaoPublico() { return orgaoPublico; }
    public void setOrgaoPublico(Boolean value) { this.orgaoPublico = value; }

        private Boolean retencaoIss;
    public Boolean getRetencaoIss() { return retencaoIss; }
    public void setRetencaoIss(Boolean value) { this.retencaoIss = value; }

        private Boolean retencaoIrrfCsll;
    public Boolean getRetencaoIrrfCsll() { return retencaoIrrfCsll; }
    public void setRetencaoIrrfCsll(Boolean value) { this.retencaoIrrfCsll = value; }

        private Boolean alertaLgpd;
    public Boolean getAlertaLgpd() { return alertaLgpd; }
    public void setAlertaLgpd(Boolean value) { this.alertaLgpd = value; }

        private Boolean autorizacaoLgpd;
    public Boolean getAutorizacaoLgpd() { return autorizacaoLgpd; }
    public void setAutorizacaoLgpd(Boolean value) { this.autorizacaoLgpd = value; }

    private String rg;
    public String getRg() { return rg; }
    public void setRg(String value) { this.rg = value; }

    private String categoria;
    public String getCategoria() { return categoria; }
    public void setCategoria(String value) { this.categoria = value; }

    private String indicadorIsencao;
    public String getIndicadorIsencao() { return indicadorIsencao; }
    public void setIndicadorIsencao(String value) { this.indicadorIsencao = value; }

    @Email
    @Size(max = 100)
    private String emailSecundario;
    public String getEmailSecundario() { return emailSecundario; }
    public void setEmailSecundario(String value) { this.emailSecundario = value; }

    private String outrosContatos;
    public String getOutrosContatos() { return outrosContatos; }
    public void setOutrosContatos(String value) { this.outrosContatos = value; }

    private Boolean enviarNoticia;
    public Boolean getEnviarNoticia() { return enviarNoticia; }
    public void setEnviarNoticia(Boolean value) { this.enviarNoticia = value; }

    @jakarta.validation.constraints.Min(1)
    @jakarta.validation.constraints.Max(5)
    private Integer classificacao;
    public Integer getClassificacao() { return classificacao; }
    public void setClassificacao(Integer value) { this.classificacao = value; }

    @jakarta.validation.constraints.DecimalMin("0")
    @jakarta.validation.constraints.DecimalMax("100")
    private java.math.BigDecimal limiteDescontoPecas;
    public java.math.BigDecimal getLimiteDescontoPecas() { return limiteDescontoPecas; }
    public void setLimiteDescontoPecas(java.math.BigDecimal value) { this.limiteDescontoPecas = value; }

    @jakarta.validation.constraints.DecimalMin("0")
    @jakarta.validation.constraints.DecimalMax("100")
    private java.math.BigDecimal limiteDescontoServicos;
    public java.math.BigDecimal getLimiteDescontoServicos() { return limiteDescontoServicos; }
    public void setLimiteDescontoServicos(java.math.BigDecimal value) { this.limiteDescontoServicos = value; }

    @jakarta.validation.constraints.DecimalMin("0")
    private java.math.BigDecimal bloquearSeAtrasar;
    public java.math.BigDecimal getBloquearSeAtrasar() { return bloquearSeAtrasar; }
    public void setBloquearSeAtrasar(java.math.BigDecimal value) { this.bloquearSeAtrasar = value; }

    // Getters e Setters
    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public OrigemCliente getOrigemCliente() {
        return origemCliente;
    }

    public void setOrigemCliente(OrigemCliente origemCliente) {
        this.origemCliente = origemCliente;
    }

    public StatusCliente getStatus() {
        return status;
    }

    public void setStatus(StatusCliente status) {
        this.status = status;
    }

    public String getObservacoesGerais() {
        return observacoesGerais;
    }

    public void setObservacoesGerais(String observacoesGerais) {
        this.observacoesGerais = observacoesGerais;
    }
}
