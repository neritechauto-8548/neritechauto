package com.neritech.saas.cliente.dto;

import com.neritech.saas.cliente.domain.enums.TipoContato;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para criaÃ§Ã£o/atualizaÃ§Ã£o de contato de cliente")
public class ContatoClienteRequest {

    @Schema(description = "Tipo do contato", example = "EMAIL")
    @NotNull
    private TipoContato tipoContato;

    @Schema(description = "Valor do contato", example = "contato@exemplo.com")
    @NotBlank
    @Size(max = 255)
    private String contato;

    @Schema(description = "Se Ã© o contato principal do cliente")
    private Boolean principal;

    // Getters e Setters
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
}
