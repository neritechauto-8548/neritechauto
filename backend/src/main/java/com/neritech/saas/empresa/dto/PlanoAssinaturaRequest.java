package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.enums.SuporteTecnico;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PlanoAssinaturaRequest(
        @NotBlank @Size(max = 100) String nome,
        String descricao,
        @NotNull @DecimalMin("0.0") BigDecimal precoMensal,
        BigDecimal precoAnual,
        Integer maxUsuarios,
        Integer maxClientes,
        Integer maxVeiculos,
        Integer maxOsMes,
        Integer maxProdutos,
        Integer maxFornecedores,
        Boolean possuiAppMobile,
        Boolean possuiApi,
        Boolean possuiIntegracaoNfe,
        Boolean possuiBackupAutomatico,
        Integer storageGb,
        SuporteTecnico suporteTecnico,
        Integer periodoTesteDias,
        Boolean ativo) {
}
