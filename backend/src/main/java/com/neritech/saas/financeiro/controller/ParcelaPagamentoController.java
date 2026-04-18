package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.ParcelaPagamentoResponse;
import com.neritech.saas.financeiro.service.ParcelaPagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/financeiro/parcelas-pagamento")
@RequiredArgsConstructor
@Tag(name = "Parcelas de Pagamento", description = "VisualizaÃ§Ã£o de parcelas de pagamento")
public class ParcelaPagamentoController {

    private final ParcelaPagamentoService service;

    @GetMapping("/pagamento/{pagamentoId}")
    @Operation(summary = "Listar parcelas por pagamento")
    public ResponseEntity<List<ParcelaPagamentoResponse>> findByPagamentoId(@PathVariable Long pagamentoId) {
        return ResponseEntity.ok(service.findByPagamentoId(pagamentoId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar parcela por ID")
    public ResponseEntity<ParcelaPagamentoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
