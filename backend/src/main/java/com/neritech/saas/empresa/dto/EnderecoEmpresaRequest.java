package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.enums.TipoEndereco;
import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EnderecoEmpresaRequest(
        @NotNull(message = "O ID da empresa Ã© obrigatÃ³rio") Long empresaId,

        TipoEndereco tipoEndereco,

        @NotBlank(message = "O CEP Ã© obrigatÃ³rio") @Size(max = 9) String cep,

        @NotBlank(message = "O logradouro Ã© obrigatÃ³rio") @Size(max = 255) String logradouro,

        @NotBlank(message = "O nÃºmero Ã© obrigatÃ³rio") @Size(max = 20) String numero,

        @Size(max = 100) String complemento,

        @NotBlank(message = "O bairro Ã© obrigatÃ³rio") @Size(max = 100) String bairro,

        @NotBlank(message = "A cidade Ã© obrigatÃ³ria") @Size(max = 100) String cidade,

        @NotBlank(message = "O estado Ã© obrigatÃ³rio") @Size(min = 2, max = 2) String estado,

        @Size(max = 50) String pais,

        Boolean principal,
        Boolean ativo) {
}
