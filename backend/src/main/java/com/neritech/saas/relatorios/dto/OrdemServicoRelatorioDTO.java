package com.neritech.saas.relatorios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServicoRelatorioDTO {
    // Dados da OS
    private String numeroOS;
    private String tipoOS; // Orçamento ou Ordem de Serviço
    private String status;
    private String dataAbertura;
    private String dataPromessa;
    private String dataEntrega;
    private String problemaRelatado;
    private String solucaoAplicada;
    private String observacoesCliente;

    // Dados do Cliente
    private String clienteNome;
    private String clienteDocumento;
    private String clienteTelefone;
    private String clienteEmail;
    private String clienteEndereco;

    // Dados do Veículo
    private String veiculoPlaca;
    private String veiculoModelo;
    private String veiculoMarca;
    private String veiculoAno;
    private String veiculoCor;
    private String veiculoKm;
    private String veiculoChassi;

    // Totais
    private BigDecimal valorServicos;
    private BigDecimal valorProdutos;
    private BigDecimal valorDesconto;
    private BigDecimal valorAcrescimo;
    private BigDecimal valorTotal;

    // Listas
    private List<ItemRelatorioDTO> servicos;
    private List<ItemRelatorioDTO> produtos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemRelatorioDTO {
        private String codigo;
        private String descricao;
        private BigDecimal quantidade;
        private BigDecimal valorUnitario;
        private BigDecimal valorTotal;
        private String tecnico; // Mecânico executor
    }
}
