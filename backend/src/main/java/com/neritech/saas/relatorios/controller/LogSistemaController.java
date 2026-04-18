package com.neritech.saas.relatorios.controller;

import com.neritech.saas.relatorios.dto.LogSistemaRequest;
import com.neritech.saas.relatorios.dto.LogSistemaResponse;
import com.neritech.saas.relatorios.service.LogSistemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/logs-sistema")
@RequiredArgsConstructor
@Tag(name = "Logs do Sistema", description = "Consulta e registro de logs do sistema")
public class LogSistemaController {

    private final LogSistemaService logService;

    @PostMapping
    @Operation(summary = "Registrar log do sistema")
    public ResponseEntity<LogSistemaResponse> create(@RequestBody LogSistemaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logService.create(request));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar logs por empresa")
    public ResponseEntity<List<LogSistemaResponse>> findByEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(logService.findByEmpresa(empresaId));
    }
}
