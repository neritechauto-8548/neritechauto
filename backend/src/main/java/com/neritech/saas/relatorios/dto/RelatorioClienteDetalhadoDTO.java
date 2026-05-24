package com.neritech.saas.relatorios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioClienteDetalhadoDTO {
    // Basic client info
    private String nome;
    private String documento;
    private String email;
    private String origem;
    private String status;
    private String dataCadastro;

    // Address and phone
    private String endereco;
    private String telefones;

    // For Registered & Inactive reports
    private BigDecimal ticketMedio;
    private String ultimaVisita;
    private Long numeroOS; // Quantity of OS

    // For Visits report
    private String numeroOSVisita; // OS identifier (e.g. OS-1234)
    private String osDescricao; // Description of OS/Services
    private String dataOS; // Date of OS visit
    private BigDecimal valorGastoOS; // Cost of this specific visit
}
