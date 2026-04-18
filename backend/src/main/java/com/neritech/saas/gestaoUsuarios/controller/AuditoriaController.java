package com.neritech.saas.gestaoUsuarios.controller;

import com.neritech.saas.gestaoUsuarios.domain.LogAcesso;
import com.neritech.saas.gestaoUsuarios.repository.LogAcessoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/auditoria")
@RequiredArgsConstructor
@Tag(name = "Auditoria", description = "Logs de acesso e tentativas de login")
public class AuditoriaController {

    private final LogAcessoRepository logAcessoRepository;

    @GetMapping("/logs-acesso")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('AUDITORIA.READ')")
    @Operation(summary = "Listar logs de acesso", description = "Retorna logs de acesso de um usuário específico")
    public ResponseEntity<List<LogAcesso>> getLogsAcesso(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(logAcessoRepository.findByUsuarioIdOrderByDataEventoDesc(usuarioId));
    }
}
