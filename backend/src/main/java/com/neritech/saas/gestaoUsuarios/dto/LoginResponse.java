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
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long usuarioId;
    private Long empresaId;
    private String nomeCompleto;
    private String email;
    private Set<String> roles;
    private Set<String> permissoes;
    private LocalDateTime expiraEm;
    private Long expiresIn;
    private boolean primeiroAcesso;
    private boolean deveTrocarSenha;
}
