package com.neritech.saas.empresa.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record SeguradoraRequest(
    @NotBlank(message = "CNPJ é obrigatório") @Size(max = 18) String cnpj,
    @NotBlank(message = "Razão social é obrigatória") @Size(max = 255) String razaoSocial,
    @Size(max = 255) String nomeFantasia,
    @Size(max = 50) String inscricaoEstadual,
    @Size(max = 50) String inscricaoMunicipal,
    @Email @Size(max = 255) String email,
    @Size(max = 4000) String observacoes,
    @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal margemRefatoramento,
    @Size(max = 500)
    @Pattern(regexp = "^(https?://[^\\s]+)?$", message = "O site deve começar com http:// ou https://")
    String site,
    Boolean retencaoIss,
    Boolean ativo
) {}
