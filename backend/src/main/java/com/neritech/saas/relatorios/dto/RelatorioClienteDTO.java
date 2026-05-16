package com.neritech.saas.relatorios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioClienteDTO {
    private String nome;
    private String documento;
    private String tipo;
    private String email;
    private String origem;
    private String status;
    private String dataCadastro;
    private String dataNascimento;
    private String sexo;
}
