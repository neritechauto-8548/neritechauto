package com.neritech.saas.relatorios.controller;

import com.neritech.saas.relatorios.dto.MetricaPerformanceRequest;
import com.neritech.saas.relatorios.dto.MetricaPerformanceResponse;
import com.neritech.saas.relatorios.service.MetricaPerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/metricas")
@RequiredArgsConstructor
@Tag(name = "MÃ©tricas de Performance", description = "Monitoramento de mÃ©tricas")
public class MetricaPerformanceController {

    private final MetricaPerformanceService metricaService;

    @PostMapping
    @Operation(summary = "Registrar mÃ©trica")
    public ResponseEntity<MetricaPerformanceResponse> create(@RequestBody MetricaPerformanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metricaService.create(request));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar mÃ©tricas por empresa")
    public ResponseEntity<List<MetricaPerformanceResponse>> findByEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(metricaService.findByEmpresa(empresaId));
    }
}
