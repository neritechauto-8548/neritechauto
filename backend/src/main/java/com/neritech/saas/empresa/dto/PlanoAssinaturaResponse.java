package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.enums.SuporteTecnico;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PlanoAssinaturaResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoMensal,
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
        Boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
