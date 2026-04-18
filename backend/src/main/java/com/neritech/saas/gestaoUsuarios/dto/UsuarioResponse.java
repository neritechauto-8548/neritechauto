package com.neritech.saas.gestaoUsuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;
    private Long empresaId;
    private String nomeCompleto;
    private String email;
    private boolean ativo;
    private boolean bloqueado;
    private LocalDateTime ultimoAcesso;
    
    private Set<String> funcoes;
    
    // Perfil data
    private String cargo;
    private String departamento;
    private String telefone;
    private String avatarUrl;
}
