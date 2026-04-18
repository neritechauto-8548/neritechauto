package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.TipoPerda;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PerdaEstoqueResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        String loteNumero,
        BigDecimal quantidadePerdida,
        TipoPerda tipoPerda,
        String descricao,
        BigDecimal valorPerda,
        String responsavelPerda,
        Long localizacaoId,
        String localizacaoNome,
        Boolean foiSegurado,
        String numeroSinistro,
        BigDecimal valorIndenizado,
        LocalDate dataOcorrencia,
        LocalDate dataDescoberta,
        String boletimOcorrencia,
        String fotosComprovantesUrl,
        String observacoes,
        Long aprovadoPor,
        LocalDateTime dataAprovacao,
        Long usuarioCadastro) {
}
