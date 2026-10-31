package com.neritech.saas.produtoServico.dto;

import com.neritech.saas.produtoServico.domain.enums.TipoPessoa;
import jakarta.validation.constraints.*;

public record TransportadoraRequest(
        Long fornecedorId,
        @NotNull TipoPessoa tipoPessoa,
        @NotBlank @Size(max = 255) String nomeFantasia,
        @Size(max = 255) String razaoSocial,
        @Size(max = 14) String cpf,
        @Size(max = 18) String cnpj,
        @Size(max = 255) String contato,
        @Pattern(regexp = "^$|^[0-9+() .-]{10,20}$", message = "Telefone inválido") String telefone,
        @Email @Size(max = 255) String email,
        @Size(max = 1000) String endereco,
        @Pattern(regexp = "^$|^[0-9]{5}-?[0-9]{3}$", message = "CEP inválido") String cep,
        @Size(max = 100) String cidade,
        @Pattern(regexp = "^$|^[A-Z]{2}$", message = "UF inválida") String estado,
        @NotBlank @Pattern(regexp = "RODOVIARIO|OUTROS") String modalidade,
        Boolean ativo,
        @Size(max = 4000) String observacoes,
        @Size(max = 20) String inscricaoEstadual,
        @Size(max = 255) String logradouro,
        @Size(max = 30) String numero,
        @Size(max = 120) String bairro,
        @Size(max = 255) String complemento
) {}
