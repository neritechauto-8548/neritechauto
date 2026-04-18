package com.neritech.saas.financeiro.dto;

import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.financeiro.domain.enums.AmbienteNfe;
import com.neritech.saas.financeiro.domain.enums.StatusNfe;
import com.neritech.saas.financeiro.domain.enums.TipoEmissaoNfe;
import com.neritech.saas.financeiro.domain.enums.TipoOperacaoNfe;
import com.neritech.saas.empresa.domain.enums.AmbienteNFe;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record NfeRequest(
                Long faturaId,
                String chaveNfe,
                String numeroNfe,
                String serieNfe,

                @NotNull(message = "Tipo de operaÃ§Ã£o Ã© obrigatÃ³rio") TipoOperacaoNfe tipoOperacao,

                StatusNfe status,
                AmbienteNfe ambiente,
                TipoEmissaoNfe tipoEmissao,
                LocalDateTime dataEmissao,
                LocalDateTime dataSaidaEntrada,
                BigDecimal valorTotalNota,
                BigDecimal valorTotalProdutos,
                BigDecimal valorTotalServicos,
                String xmlUrl,
                String danfeUrl,
                String protocoloAutorizacao,
                String mensagemRetorno,
                String observacoes) {
}
