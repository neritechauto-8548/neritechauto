package com.neritech.saas.empresa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record SeguradoraRequest(
    @NotBlank @Size(max = 255) String nomeFantasia,
    @Size(max = 255) String razaoSocial,
    @Size(max = 18) String cnpj,
    @Size(max = 60) String registroSusep,
    @Size(max = 255) String nomeContato,
    @Pattern(regexp = "^$|^[0-9+() .-]{10,20}$", message = "Telefone inválido") String telefone,
    @Email @Size(max = 255) String email,
    @Size(max = 500) String endereco,
    @Pattern(regexp = "CONVENIO|REEMBOLSO|AMBOS", message = "Tipo de convênio inválido") String tipoConvenio,
    @Size(max = 4000) String notas,
    Boolean ativo
) {}
