package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.ItemFaturaResponse;
import com.neritech.saas.financeiro.service.ItemFaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/financeiro/itens-fatura")
@RequiredArgsConstructor
@Tag(name = "Itens da Fatura", description = "VisualizaÃ§Ã£o de itens da fatura")
public class ItemFaturaController {

    private final ItemFaturaService service;

    @GetMapping("/fatura/{faturaId}")
    @Operation(summary = "Listar itens por fatura")
    public ResponseEntity<List<ItemFaturaResponse>> findByFaturaId(@PathVariable Long faturaId) {
        return ResponseEntity.ok(service.findByFaturaId(faturaId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID")
    public ResponseEntity<ItemFaturaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
