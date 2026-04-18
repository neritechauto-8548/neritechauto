package com.neritech.saas.veiculo.dto;

import com.neritech.saas.veiculo.domain.enums.StatusVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record VeiculoRequest(
                @NotNull(message = "O cliente Ã© obrigatÃ³rio") Long clienteId,

                Long marcaId,
                Long modeloId,
                Long anoModeloId,
                Long combustivelId,

                @NotBlank(message = "A placa Ã© obrigatÃ³ria") @Size(max = 10, message = "A placa deve ter no mÃ¡ximo 10 caracteres") String placa,

                @Size(max = 11, message = "O Renavam deve ter no mÃ¡ximo 11 caracteres") String renavam,

                @Size(max = 17, message = "O chassi deve ter no mÃ¡ximo 17 caracteres") String chassi,

                @Size(max = 50, message = "O nÃºmero do motor deve ter no mÃ¡ximo 50 caracteres") String numeroMotor,

                @Size(max = 50, message = "A cor externa deve ter no mÃ¡ximo 50 caracteres") String corExterna,

                Integer quilometragemAtual,
                Integer quilometragemCadastro,
                LocalDate dataUltimaRevisao,
                Integer proximaRevisaoKm,
                LocalDate proximaRevisaoData,
                StatusVeiculo status,
                String observacoes) {
}
