package com.neritech.saas.relatorios.controller;

import com.neritech.saas.relatorios.dto.ParametroRelatorioRequest;
import com.neritech.saas.relatorios.dto.RelatorioSalvoRequest;
import com.neritech.saas.relatorios.dto.RelatorioSalvoResponse;
import com.neritech.saas.relatorios.service.RelatorioSalvoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/relatorios")
@RequiredArgsConstructor
@Tag(name = "RelatÃ³rios Salvos", description = "Gerenciamento de relatÃ³rios salvos e parÃ¢metros")
public class RelatorioSalvoController {

    private final RelatorioSalvoService relatorioService;

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar relatÃ³rios por empresa")
    public ResponseEntity<List<RelatorioSalvoResponse>> findAllByEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(relatorioService.findAllByEmpresa(empresaId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar relatÃ³rio por ID")
    public ResponseEntity<RelatorioSalvoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(relatorioService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo relatÃ³rio")
    public ResponseEntity<RelatorioSalvoResponse> create(@RequestBody RelatorioSalvoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar relatÃ³rio")
    public ResponseEntity<RelatorioSalvoResponse> update(@PathVariable Long id,
            @RequestBody RelatorioSalvoRequest request) {
        return ResponseEntity.ok(relatorioService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir relatÃ³rio")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        relatorioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/parametros")
    @Operation(summary = "Adicionar parÃ¢metro ao relatÃ³rio")
    public ResponseEntity<Void> addParametro(@PathVariable Long id, @RequestBody ParametroRelatorioRequest request) {
        relatorioService.addParametro(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
