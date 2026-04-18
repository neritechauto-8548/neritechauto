package com.neritech.saas.financeiro.dto;

import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.financeiro.domain.enums.AmbienteNfe;
import com.neritech.saas.financeiro.domain.enums.StatusNfe;
import com.neritech.saas.financeiro.domain.enums.TipoEmissaoNfe;
import com.neritech.saas.financeiro.domain.enums.TipoOperacaoNfe;
import com.neritech.saas.empresa.domain.enums.AmbienteNFe;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record NfeResponse(
                Long id,
                Long empresaId,
                Long faturaId,
                String faturaNumero,
                String chaveNfe,
                String numeroNfe,
                String serieNfe,
                TipoOperacaoNfe tipoOperacao,
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
                String observacoes,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}
