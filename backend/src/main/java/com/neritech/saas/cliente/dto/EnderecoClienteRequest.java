package com.neritech.saas.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para criação/atualização de endereço de cliente")
public class EnderecoClienteRequest {

    @Schema(description = "CEP", example = "12345-678")
    @NotBlank
    @Size(max = 9)
    private String cep;

    @Schema(description = "Logradouro", example = "Rua das Flores")
    @NotBlank
    @Size(max = 255)
    private String logradouro;

    @Schema(description = "Número", example = "123")
    @NotBlank
    @Size(max = 20)
    private String numero;

    @Schema(description = "Complemento", example = "Apto 301")
    @Size(max = 100)
    private String complemento;

    @Schema(description = "Bairro", example = "Centro")
    @NotBlank
    @Size(max = 100)
    private String bairro;

    @Schema(description = "Cidade", example = "São Paulo")
    @NotBlank
    @Size(max = 100)
    private String cidade;

    @Schema(description = "Estado (UF)", example = "SP")
    @NotBlank
    @Size(max = 2)
    private String estado;

    @Schema(description = "País", example = "Brasil")
    @Size(max = 50)
    private String pais;

    // Getters e Setters
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
