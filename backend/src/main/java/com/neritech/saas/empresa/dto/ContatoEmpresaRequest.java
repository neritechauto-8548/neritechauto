package com.neritech.saas.empresa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContatoEmpresaRequest(
        @NotBlank @Pattern(regexp = "EMAIL|TELEFONE") String tipoContato,
        @NotBlank @Size(max = 160) String contato) {
}
