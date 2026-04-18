package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.TipoLocalizacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record LocalizacaoEstoqueRequest(
        @NotNull(message = "O ID da empresa Ã© obrigatÃ³rio") Long empresaId,

        @NotBlank(message = "O cÃ³digo Ã© obrigatÃ³rio") @Size(max = 20, message = "O cÃ³digo deve ter no mÃ¡ximo 20 caracteres") String codigo,

        @NotBlank(message = "O nome Ã© obrigatÃ³rio") @Size(max = 100, message = "O nome deve ter no mÃ¡ximo 100 caracteres") String nome,

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
