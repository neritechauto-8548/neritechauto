package com.neritech.saas.veiculo.dto;

import com.neritech.saas.veiculo.domain.enums.StatusVeiculo;
import java.math.BigDecimal;
import java.time.LocalDate;

public record VeiculoResponse(
                Long id,
                Long clienteId,
                String clienteNome,
                Long marcaId,
                String marcaNome,
                Long modeloId,
                String modeloNome,
                String modeloImagemUrl,
                Long anoModeloId,
                Integer anoFabricacao,
                Integer anoModelo,
                Long combustivelId,
                String combustivelNome,
                String placa,
                String renavam,
                String chassi,
                String numeroMotor,
                String corExterna,
                String direcao,
                String cambio,
                Boolean arCondicionado,
                Integer portas,
                Integer quilometragemAtual,
                Integer quilometragemCadastro,
                LocalDate dataUltimaRevisao,
                Integer proximaRevisaoKm,
                LocalDate proximaRevisaoData,
                StatusVeiculo status,
                String observacoes) {
}
