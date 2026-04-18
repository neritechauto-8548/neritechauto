package com.neritech.saas.empresa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(name = "EmpresaRequest", description = "Dados para criaÃ§Ã£o/atualizaÃ§Ã£o de empresa")
public class EmpresaRequest {
    @Schema(description = "Nome fantasia", example = "Oficina ABC")
    @Size(max = 255)
    private String nomeFantasia;

    @Schema(description = "RazÃ£o social", example = "Oficina ABC LTDA")
    @Size(max = 255)
    private String razaoSocial;

    @Schema(description = "CNPJ (somente dÃ­gitos)", example = "12345678000195", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(max = 20)
    private String cnpj;

    @Schema(description = "InscriÃ§Ã£o estadual", example = "1234567890")
    @Size(max = 30)
    private String inscricaoEstadual;

    @Schema(description = "InscriÃ§Ã£o municipal", example = "987654321")
    @Size(max = 30)
    private String inscricaoMunicipal;

    @Schema(description = "E-mail de contato", example = "contato@oficinaabc.com")
    @Email
    @Size(max = 120)
    private String email;

    @Schema(description = "Telefone de contato", example = "(11) 99999-9999")
    @Size(max = 30)
    private String telefone;

    @Schema(description = "Site da empresa", example = "https://oficinaabc.com")
    @Size(max = 120)
    private String site;

    @Schema(description = "Data de abertura", example = "2020-01-01")
    private LocalDate dataAbertura;

    @Schema(description = "ObservaÃ§Ãµes gerais", example = "Atende veÃ­culos leves e utilitÃ¡rios")
    @Size(max = 2000)
    private String observacoes;

    @Schema(description = "Indicador de ativo", example = "true")
    private Boolean ativo = true;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
