package com.neritech.saas.gestaoUsuarios.controller;

import com.neritech.saas.gestaoUsuarios.dto.PermissaoResponse;
import com.neritech.saas.gestaoUsuarios.service.PermissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/permissoes")
@RequiredArgsConstructor
@Tag(name = "Permissões", description = "Gestão de permissões do sistema")
public class PermissaoController {

    private final PermissaoService permissaoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar permissões", description = "Retorna todas as permissões disponíveis")
    public ResponseEntity<List<PermissaoResponse>> findAll() {
        return ResponseEntity.ok(permissaoService.findAll().stream()
                .map(p -> new PermissaoResponse(p.getId(), p.getChave(), p.getDescricao(), p.getValor()))
                .sorted(java.util.Comparator.comparing(PermissaoResponse::chave).thenComparing(PermissaoResponse::valor))
                .toList());
    }
}
