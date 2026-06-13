package com.neritech.saas.financeiro.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LogAlteracaoDTO {
    private Long id;
    private String operacao;
    private String camposAlterados;
    private String usuarioResponsavelNome;
    private LocalDateTime dataAlteracao;
    private String motivoAlteracao;
}
