package com.neritech.saas.relatorios.controller;

import com.neritech.saas.relatorios.dto.ExportacaoRequest;
import com.neritech.saas.relatorios.dto.ExportacaoResponse;
import com.neritech.saas.relatorios.service.ExportacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/exportacoes")
@RequiredArgsConstructor
@Tag(name = "ExportaÃ§Ãµes", description = "Gerenciamento de exportaÃ§Ãµes de dados")
public class ExportacaoController {

    private final ExportacaoService exportacaoService;

    @PostMapping
    @Operation(summary = "Solicitar nova exportaÃ§Ã£o")
    public ResponseEntity<ExportacaoResponse> create(@RequestBody ExportacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exportacaoService.create(request));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar exportaÃ§Ãµes por empresa")
    public ResponseEntity<List<ExportacaoResponse>> findByEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(exportacaoService.findByEmpresa(empresaId));
    }
}
