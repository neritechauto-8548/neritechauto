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
    public VeiculoResponse(Long id, Long clienteId, String clienteNome, Long marcaId, String marcaNome, Long modeloId, String modeloNome, String modeloImagemUrl, Long anoModeloId, Integer anoFabricacao, Integer anoModelo, Long combustivelId, String combustivelNome, String placa, String renavam, String chassi, String numeroMotor, String corExterna, String direcao, String cambio, Boolean arCondicionado, Integer portas, Integer quilometragemAtual, Integer quilometragemCadastro, LocalDate dataUltimaRevisao, Integer proximaRevisaoKm, LocalDate proximaRevisaoData, StatusVeiculo status, String observacoes) {
        this(id, clienteId, clienteNome, marcaId, marcaNome, modeloId, modeloNome, modeloImagemUrl, anoModeloId, anoFabricacao, anoModelo, combustivelId, combustivelNome, placa, renavam, chassi, numeroMotor, corExterna, direcao, cambio, arCondicionado, portas, quilometragemAtual, quilometragemCadastro, dataUltimaRevisao, proximaRevisaoKm, proximaRevisaoData, status, observacoes, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

}
