package com.neritech.saas.veiculo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class MarcaVeiculoRequest {

    @NotBlank(message = "O nome da marca é obrigatório")
    @Size(max = 100, message = "O nome da marca deve ter no máximo 100 caracteres")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
