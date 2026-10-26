package com.neritech.saas.trial.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "O celular é obrigatório")
    private String celular;

    @NotBlank(message = "O nome da empresa é obrigatório")
    private String nomeEmpresa;

    @NotBlank(message = "O CNPJ/CPF é obrigatório")
    private String cnpjOuCpf;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 12, max = 128, message = "A senha deve ter entre 12 e 128 caracteres")
    private String senha;

    @NotNull(message = "Selecione um plano")
    private Long planoId;

    @NotBlank(message = "A versão dos termos é obrigatória")
    private String versaoLegal;

    @jakarta.validation.constraints.AssertTrue(message = "O aceite dos termos é obrigatório")
    private boolean aceiteLegal;

    private String segmento;
}
