package com.neritech.saas.relatorios.controller;

import com.neritech.saas.relatorios.dto.LogAlteracaoRequest;
import com.neritech.saas.relatorios.dto.LogAlteracaoResponse;
import com.neritech.saas.relatorios.service.LogAlteracaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/logs-alteracoes")
@RequiredArgsConstructor
@Tag(name = "Logs de AlteraÃ§Ãµes", description = "Auditoria de alteraÃ§Ãµes de dados")
public class LogAlteracaoController {

    private final LogAlteracaoService logService;

    @PostMapping
    @Operation(summary = "Registrar log de alteraÃ§Ã£o")
    public ResponseEntity<LogAlteracaoResponse> create(@RequestBody LogAlteracaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logService.create(request));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar logs de alteraÃ§Ãµes por empresa")
    public ResponseEntity<List<LogAlteracaoResponse>> findByEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(logService.findByEmpresa(empresaId));
    }
}
