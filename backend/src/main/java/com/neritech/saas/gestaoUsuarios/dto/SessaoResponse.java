package com.neritech.saas.gestaoUsuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoResponse {

    private Long sessaoId;
    private String ip;
    private String dispositivo;
    private LocalDateTime ultimoAcesso;
    private boolean ativo;
}
