package com.neritech.saas.veiculo.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tipos_combustivel")
public class TipoCombustivel extends BaseEntity {

    @NotBlank(message = "O nome ÃƒÂ© obrigatÃƒÂ³rio")
    @Size(max = 30, message = "O nome deve ter no mÃƒÂ¡ximo 30 caracteres")
    @Column(name = "nome", nullable = false, length = 30)
    private String nome;

    public TipoCombustivel() {
    }

    // Getters and Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

