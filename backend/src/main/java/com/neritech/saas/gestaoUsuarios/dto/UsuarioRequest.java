package com.neritech.saas.gestaoUsuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {

    @NotBlank(message = "Nome é obrigatório")
    @jakarta.validation.constraints.Size(max = 255)
    private String nomeCompleto;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @jakarta.validation.constraints.Size(max = 255)
    private String email;

    @jakarta.validation.constraints.Size(max = 128)
    private String senha; // Opcional na atualização e no convite

    private boolean ativo;
    private boolean bloqueado;

    private Set<Long> funcoesIds;
    
    // Perfil data
    private String cargo;
    private String departamento;
    private String telefone;
    private java.util.Map<String, Object> preferencias;
    private Integer versao;
}
