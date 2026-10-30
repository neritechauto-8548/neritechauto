package com.neritech.saas.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.neritech.saas.cliente.domain.enums.TipoContato;

@Schema(description = "Resposta de contato de cliente")
public class ContatoClienteResponse {
    private Long id;
    private Long clienteId;
    private TipoContato tipoContato;
    private String contato;
    private Boolean principal;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    private String nome;
    public String getNome() { return nome; }
    public void setNome(String value) { this.nome = value; }

    private String sexo;
    public String getSexo() { return sexo; }
    public void setSexo(String value) { this.sexo = value; }

    private java.time.LocalDate dataNascimento;
    public java.time.LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(java.time.LocalDate value) { this.dataNascimento = value; }

    private Boolean grupoPrincipal;
    public Boolean getGrupoPrincipal() { return grupoPrincipal; }
    public void setGrupoPrincipal(Boolean value) { this.grupoPrincipal = value; }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public TipoContato getTipoContato() {
        return tipoContato;
    }

    public void setTipoContato(TipoContato tipoContato) {
        this.tipoContato = tipoContato;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
