package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ContaBancariaRequest(
        @NotBlank(message = "Código do banco é obrigatório") @Size(max = 5) String bancoCodigo,

        @NotBlank(message = "Nome do banco é obrigatário") @Size(max = 100) String bancoNome,

        @NotBlank(message = "Agência é obrigatória") @Size(max = 10) String agencia,

        @NotBlank(message = "Conta é obrigatória") @Size(max = 20) String conta,

        @Size(max = 2) String digitoConta,

        @NotNull(message = "Tipo de conta é obrigatório") TipoConta tipoConta,

        @NotBlank(message = "Titular da conta é obrigatório") String titularConta,

        @NotBlank(message = "CPF/CNPJ do titular é obrigatório") @Size(max = 18) String cpfCnpjTitular,

        BigDecimal saldoAtual,
        BigDecimal limiteChequeEspecial,
        LocalDate dataUltimoSaldo,
        Boolean ativo) {
}
