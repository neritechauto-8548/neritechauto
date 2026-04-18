package com.neritech.saas.trial.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrialRegisterRequest {

    @NotBlank(message = "O nome completo é obrigatório")
    private String nomeCompleto;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @NotBlank(message = "O nome da empresa é obrigatório")
    private String nomeEmpresa;

    @NotBlank(message = "O CNPJ/CPF é obrigatório")
    private String cnpjOuCpf;

    private String segmento;
}
