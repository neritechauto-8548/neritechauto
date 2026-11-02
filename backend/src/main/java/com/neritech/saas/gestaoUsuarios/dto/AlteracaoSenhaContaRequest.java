package com.neritech.saas.gestaoUsuarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlteracaoSenhaContaRequest(
        @NotBlank @Size(min = 12, max = 128) String senhaAtual,
        @NotBlank @Size(min = 12, max = 128) String novaSenha) {}
