package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoConta;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ContaBancariaResponse(
        Long id,
        Long empresaId,
        String bancoCodigo,
        String bancoNome,
        String agencia,
        String conta,
        String digitoConta,
        TipoConta tipoConta,
        String titularConta,
        String cpfCnpjTitular,
        BigDecimal saldoAtual,
        BigDecimal limiteChequeEspecial,
        LocalDate dataUltimoSaldo,
        Boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
