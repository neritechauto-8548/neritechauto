package com.neritech.saas.fiscal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CertificadoDigitalRequest(
        @NotBlank(message = "O nome do arquivo Ã© obrigatÃ³rio") String nomeArquivo,

        @NotBlank(message = "A senha Ã© obrigatÃ³ria") String senha,

        @NotNull(message = "O arquivo Ã© obrigatÃ³rio") byte[] arquivo,

        @NotNull(message = "A data de validade Ã© obrigatÃ³ria") LocalDateTime dataValidade,

        String emissor,
        String serialNumber,
        boolean ativo) {
}
