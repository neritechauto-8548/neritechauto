package com.neritech.saas.estoque.dto;

import com.neritech.saas.comunicacao.domain.enums.TipoAlerta;
import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.NivelPrioridade;
import com.neritech.saas.estoque.domain.enums.StatusAlerta;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AlertaEstoqueResponse(
        Long id,
        Long empresaId,
        Long produtoId,
        String produtoNome,
        TipoAlerta tipoAlerta,
        String descricao,
        NivelPrioridade nivelPrioridade,
        BigDecimal quantidadeAtual,
        BigDecimal quantidadeReferencia,
        LocalDate dataVencimento,
        Integer diasParado,
        BigDecimal valorEnvolvido,
        StatusAlerta status,
        Long usuarioResponsavel,
        LocalDateTime dataResolucao,
        String observacoesResolucao,
        Boolean notificacaoEnviada,
        LocalDateTime dataNotificacao) {
}
