package com.neritech.saas.empresa.dto;

import java.time.LocalDateTime;

public record EmpresaAlteracaoResponse(
        String secao, String campo, String anterior, String novo, Long autor,
        LocalDateTime data, Integer versao) {
}
