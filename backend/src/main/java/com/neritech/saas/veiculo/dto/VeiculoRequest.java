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
                @Size(max = 30) String direcao,
                @Size(max = 30) String cambio,
                Boolean arCondicionado,
                @jakarta.validation.constraints.Min(2) @jakarta.validation.constraints.Max(6) Integer portas,

                Integer quilometragemAtual,
                Integer quilometragemCadastro,
                LocalDate dataUltimaRevisao,
                Integer proximaRevisaoKm,
                LocalDate proximaRevisaoData,
                StatusVeiculo status,
                String observacoes,
                @jakarta.validation.constraints.Size(max = 40) String geracao,
                @jakarta.validation.constraints.Size(max = 80) String frota,
                @jakarta.validation.constraints.Size(max = 80) String tipoVeiculo,
                @jakarta.validation.constraints.Size(max = 2000) String acessorios,
                @jakarta.validation.constraints.DecimalMin("0") BigDecimal cilindrada,
                @jakarta.validation.constraints.Min(0) Integer quantidadeValvulas,
                @jakarta.validation.constraints.Min(0) Integer quantidadeCilindros,
                @jakarta.validation.constraints.Size(max = 100) String nomeMotor,
                @jakarta.validation.constraints.Size(max = 80) String codigoMotor,
                @jakarta.validation.constraints.Size(max = 180) String potenciasCv,
                @jakarta.validation.constraints.Size(max = 80) String linha,
                @jakarta.validation.constraints.Size(max = 80) String entradaAr,
                @jakarta.validation.constraints.Size(max = 80) String sistemaFreios,
                @jakarta.validation.constraints.Size(max = 80) String sistemaTracao,
                @jakarta.validation.constraints.Size(max = 80) String sistemaInjecao,
                @jakarta.validation.constraints.Size(max = 80) String configuracaoMotor,
                @jakarta.validation.constraints.Size(max = 80) String freioDianteiro,
                @jakarta.validation.constraints.Size(max = 80) String freioTraseiro
) {
    /** Compatibilidade com chamadas existentes que não informam os campos técnicos avançados. */
    public VeiculoRequest(Long clienteId, Long marcaId, Long modeloId, Long anoModeloId, Long combustivelId, String placa, String renavam, String chassi, String numeroMotor, String corExterna, String direcao, String cambio, Boolean arCondicionado, Integer portas, Integer quilometragemAtual, Integer quilometragemCadastro, LocalDate dataUltimaRevisao, Integer proximaRevisaoKm, LocalDate proximaRevisaoData, StatusVeiculo status, String observacoes) {
        this(clienteId, marcaId, modeloId, anoModeloId, combustivelId, placa, renavam, chassi, numeroMotor, corExterna, direcao, cambio, arCondicionado, portas, quilometragemAtual, quilometragemCadastro, dataUltimaRevisao, proximaRevisaoKm, proximaRevisaoData, status, observacoes, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

}
