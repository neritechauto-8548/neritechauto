package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.enums.TipoEndereco;
import java.time.LocalDateTime;

public record EnderecoEmpresaResponse(
        Long id,
        Long empresaId,
        String empresaNome,
        TipoEndereco tipoEndereco,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String pais,
        Boolean principal,
        Boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
