package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.TipoLocalizacao;
import java.math.BigDecimal;

public record LocalizacaoEstoqueResponse(
        Long id,
        Long empresaId,
        String codigo,
        String nome,
        String descricao,
        TipoLocalizacao tipoLocalizacao,
        String setor,
        String corredor,
        String prateleira,
        String posicao,
        BigDecimal capacidadeMaxima,
        Boolean temperaturaControlada,
        BigDecimal temperaturaMin,
        BigDecimal temperaturaMax,
        Boolean umidadeControlada,
        BigDecimal umidadeMin,
        BigDecimal umidadeMax,
        Boolean acessoRestrito,
        String usuariosAcesso,
        String observacoes,
        Boolean ativo) {
}
