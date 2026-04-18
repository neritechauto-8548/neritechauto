package com.neritech.saas.empresa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name = "EmpresaResponse", description = "Dados retornados de empresa")
public class EmpresaResponse {
    @Schema(description = "Identificador da empresa", example = "1")
    private Long id;
    @Schema(description = "Nome fantasia", example = "Oficina ABC")
    private String nomeFantasia;
    @Schema(description = "RazÃ£o social", example = "Oficina ABC LTDA")
    private String razaoSocial;
    @Schema(description = "CNPJ", example = "12345678000195")
    private String cnpj;
    @Schema(description = "InscriÃ§Ã£o estadual", example = "1234567890")
    private String inscricaoEstadual;
    @Schema(description = "InscriÃ§Ã£o municipal", example = "987654321")
    private String inscricaoMunicipal;
    @Schema(description = "E-mail", example = "contato@oficinaabc.com")
    private String email;
    @Schema(description = "Telefone", example = "(11) 99999-9999")
    private String telefone;
    @Schema(description = "Site", example = "https://oficinaabc.com")
    private String site;
    @Schema(description = "Data de abertura", example = "2020-01-01")
    private LocalDate dataAbertura;
    @Schema(description = "ObservaÃ§Ãµes", example = "Atende veÃ­culos leves e utilitÃ¡rios")
    private String observacoes;
    @Schema(description = "Ativo", example = "true")
    private Boolean ativo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
