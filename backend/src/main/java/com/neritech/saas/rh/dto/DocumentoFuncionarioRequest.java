package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.TipoDocumento;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record DocumentoFuncionarioRequest(
                @NotNull Long funcionarioId,
                @NotNull TipoDocumento tipoDocumento,
                @Size(max = 100) String numeroDocumento,
                @Size(max = 100) String orgaoEmissor,
                LocalDate dataEmissao,
                LocalDate dataValidade,
                @Size(max = 500) String arquivoUrl,
                @Size(max = 255) String arquivoNome,
                Integer arquivoTamanhoKb,
                String observacoes,
                Boolean obrigatorio,
                Boolean verificado) {
}
