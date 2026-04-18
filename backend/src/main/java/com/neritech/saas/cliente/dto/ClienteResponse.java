package com.neritech.saas.cliente.dto;

import com.neritech.saas.cliente.domain.enums.OrigemCliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.rh.domain.enums.Sexo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name = "ClienteResponse", description = "Dados retornados do cliente")
public class ClienteResponse {
    @Schema(description = "Identificador do cliente", example = "1")
    private Long id;
    @Schema(description = "ID da empresa (tenant)", example = "10")
    private Long empresaId;
    @Schema(description = "Tipo do cliente", example = "PESSOA_FISICA")
    private TipoCliente tipoCliente;
    @Schema(description = "Nome completo", example = "João da Silva")
    private String nomeCompleto;
    @Schema(description = "Nome fantasia", example = "Loja XPTO")
    private String nomeFantasia;
    @Schema(description = "Razão social", example = "Loja XPTO LTDA")
    private String razaoSocial;
    @Schema(description = "Email do cliente", example = "joao@email.com")
    private String email;
    @Schema(description = "CPF", example = "123.456.789-00")
    private String cpf;
    @Schema(description = "CNPJ", example = "12.345.678/0001-90")
    private String cnpj;
    @Schema(description = "Inscrição estadual")
    private String inscricaoEstadual;
    @Schema(description = "Inscrição municipal")
    private String inscricaoMunicipal;
    @Schema(description = "Data de nascimento", example = "1990-05-20")
    private LocalDate dataNascimento;
    @Schema(description = "Sexo", example = "M")
    private Sexo sexo;
    @Schema(description = "Origem do cliente", example = "INDICACAO")
    private OrigemCliente origemCliente;
    @Schema(description = "Status", example = "ATIVO")
    private StatusCliente status;
    @Schema(description = "Observações gerais")
    private String observacoesGerais;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

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
