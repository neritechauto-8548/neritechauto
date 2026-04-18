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
    private String nomeCompleto;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    private String senha; // Opcional na atualização

    private boolean ativo;
    private boolean bloqueado;

    private Set<Long> funcoesIds;
    
    // Perfil data
    private String cargo;
    private String departamento;
    private String telefone;
}
