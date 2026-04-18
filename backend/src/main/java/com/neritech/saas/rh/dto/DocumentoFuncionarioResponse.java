package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.TipoDocumento;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DocumentoFuncionarioResponse(
                Long id,
                Long funcionarioId,
                TipoDocumento tipoDocumento,
                String numeroDocumento,
                String orgaoEmissor,
                LocalDate dataEmissao,
                LocalDate dataValidade,
                String arquivoUrl,
                String arquivoNome,
                Integer arquivoTamanhoKb,
                String observacoes,
                Boolean obrigatorio,
                Boolean verificado,
                Long verificadoPor,
                LocalDate dataVerificacao,
                Long cadastradoPor,
                LocalDateTime dataCadastro,
                LocalDateTime dataAtualizacao) {
}
