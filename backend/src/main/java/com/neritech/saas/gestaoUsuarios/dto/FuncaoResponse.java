package com.neritech.saas.gestaoUsuarios.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record FuncaoResponse(Long id, Long empresaId, String nome, String descricao,
        Boolean sistema, Boolean ativo, long usuariosVinculados,
        Set<PermissaoResponse> permissoes, LocalDateTime dataAtualizacao, Integer versao) {}
