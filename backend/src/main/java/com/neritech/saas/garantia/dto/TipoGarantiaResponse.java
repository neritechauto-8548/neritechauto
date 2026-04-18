package com.neritech.saas.garantia.dto;

import com.neritech.saas.garantia.domain.TipoCobertura;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta para TipoGarantia
 */
public record TipoGarantiaResponse(
        Long id,
        Long empresaId,
        String nome,
        String descricao,
        Integer prazoDias,
        TipoCobertura tipoCobertura,
        BigDecimal percentualCobertura,
        BigDecimal valorMaximoCobertura,
        String condicoesAplicacao,
        String restricoes,
        String documentacaoNecessaria,
        String processoAcionamento,
        Integer slaAtendimentoHoras,
        Boolean transferivel,
        Boolean renovavel,
        String custosAdicionais,
        String exclusoes,
        Boolean ativo,
        Boolean padraoServicos,
        Boolean padraoProdutos,
        LocalDateTime dataCadastro,
        Long criadoPor) {
}
