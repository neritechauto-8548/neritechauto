package com.neritech.saas.relatorios.controller;

import com.neritech.saas.relatorios.dto.KpiOficinaRequest;
import com.neritech.saas.relatorios.dto.KpiOficinaResponse;
import com.neritech.saas.relatorios.service.KpiOficinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/kpis")
@RequiredArgsConstructor
@Tag(name = "KPIs da Oficina", description = "Gerenciamento de indicadores de performance")
public class KpiOficinaController {

    private final KpiOficinaService kpiService;

    @PostMapping
    @Operation(summary = "Criar KPI")
    public ResponseEntity<KpiOficinaResponse> create(@RequestBody KpiOficinaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(kpiService.create(request));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar KPIs por empresa")
    public ResponseEntity<List<KpiOficinaResponse>> findByEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(kpiService.findByEmpresa(empresaId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar KPI")
    public ResponseEntity<KpiOficinaResponse> update(@PathVariable Long id, @RequestBody KpiOficinaRequest request) {
        return ResponseEntity.ok(kpiService.update(id, request));
    }
}
