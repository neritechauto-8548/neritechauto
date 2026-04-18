package com.neritech.saas.produtoServico.domain;

import com.neritech.saas.common.tenancy.TenantEntity;
import com.neritech.saas.produtoServico.domain.enums.ClassificacaoFornecedor;
import com.neritech.saas.produtoServico.domain.enums.TipoPessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;

@Entity
@Table(name = "fornecedores")
public class Fornecedor extends TenantEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", length = 10)
    private TipoPessoa tipoPessoa;

    @Column(name = "nome_fantasia", nullable = false, length = 255)
    private String nomeFantasia;

    @Column(name = "razao_social", length = 255)
    private String razaoSocial;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "cnpj", length = 18)
    private String cnpj;

    @Column(name = "inscricao_estadual", length = 20)
    private String inscricaoEstadual;

    @Column(name = "inscricao_municipal", length = 20)
    private String inscricaoMunicipal;

    @Column(name = "email_principal", length = 255)
    private String emailPrincipal;

    @Column(name = "telefone_principal", length = 20)
    private String telefonePrincipal;

    @Column(name = "celular_principal", length = 20)
    private String celularPrincipal;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "nome_contato", length = 255)
    private String nomeContato;

    @Column(name = "cargo_contato", length = 100)
    private String cargoContato;

    @Column(name = "email_contato", length = 255)
    private String emailContato;

    @Column(name = "telefone_contato", length = 20)
    private String telefoneContato;

    @Column(name = "endereco_completo", columnDefinition = "TEXT")
    private String enderecoCompleto;

    @Column(name = "cep", length = 9)
    private String cep;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "estado", length = 2)
    private String estado;

    @Column(name = "prazo_pagamento_dias")
    private Integer prazoPagamentoDias;

    @Column(name = "limite_credito", precision = 12, scale = 2)
    private java.math.BigDecimal limiteCredito;

    @Column(name = "desconto_padrao", precision = 5, scale = 2)
    private java.math.BigDecimal descontoPadrao;

    @Column(name = "condicoes_especiais", columnDefinition = "TEXT")
    private String condicoesEspeciais;

    @Column(name = "banco_nome", length = 100)
    private String bancoNome;

    @Column(name = "banco_agencia", length = 10)
    private String bancoAgencia;

    @Column(name = "banco_conta", length = 20)
    private String bancoConta;

    @Column(name = "banco_pix", length = 255)
    private String bancoPix;

    @Enumerated(EnumType.STRING)
    @Column(name = "classificacao", length = 1)
    private ClassificacaoFornecedor classificacao;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "ativo")
    private Boolean ativo = true;

    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
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

    public String getEmailPrincipal() {
        return emailPrincipal;
    }

    public void setEmailPrincipal(String emailPrincipal) {
        this.emailPrincipal = emailPrincipal;
    }

    public String getTelefonePrincipal() {
        return telefonePrincipal;
    }

    public void setTelefonePrincipal(String telefonePrincipal) {
        this.telefonePrincipal = telefonePrincipal;
    }

    public String getCelularPrincipal() {
        return celularPrincipal;
    }

    public void setCelularPrincipal(String celularPrincipal) {
        this.celularPrincipal = celularPrincipal;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getCargoContato() {
        return cargoContato;
    }

    public void setCargoContato(String cargoContato) {
        this.cargoContato = cargoContato;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getPrazoPagamentoDias() {
        return prazoPagamentoDias;
    }

    public void setPrazoPagamentoDias(Integer prazoPagamentoDias) {
        this.prazoPagamentoDias = prazoPagamentoDias;
    }

    public java.math.BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(java.math.BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public java.math.BigDecimal getDescontoPadrao() {
        return descontoPadrao;
    }

    public void setDescontoPadrao(java.math.BigDecimal descontoPadrao) {
        this.descontoPadrao = descontoPadrao;
    }

    public String getCondicoesEspeciais() {
        return condicoesEspeciais;
    }

    public void setCondicoesEspeciais(String condicoesEspeciais) {
        this.condicoesEspeciais = condicoesEspeciais;
    }

    public String getBancoNome() {
        return bancoNome;
    }

    public void setBancoNome(String bancoNome) {
        this.bancoNome = bancoNome;
    }

    public String getBancoAgencia() {
        return bancoAgencia;
    }

    public void setBancoAgencia(String bancoAgencia) {
        this.bancoAgencia = bancoAgencia;
    }

    public String getBancoConta() {
        return bancoConta;
    }

    public void setBancoConta(String bancoConta) {
        this.bancoConta = bancoConta;
    }

    public String getBancoPix() {
        return bancoPix;
    }

    public void setBancoPix(String bancoPix) {
        this.bancoPix = bancoPix;
    }

    public ClassificacaoFornecedor getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(ClassificacaoFornecedor classificacao) {
        this.classificacao = classificacao;
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
