package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.FluxoCaixaRequest;
import com.neritech.saas.financeiro.dto.FluxoCaixaResponse;
import com.neritech.saas.financeiro.service.FluxoCaixaService;
import com.neritech.saas.gestaoUsuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/v1/financeiro/fluxo-caixa", "/api/v1/financeiro/fluxo-caixa"})
@RequiredArgsConstructor
@Tag(name = "Fluxo de Caixa", description = "GestÃ£o de fluxo de caixa")
public class FluxoCaixaController {

    private final FluxoCaixaService service;
    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar lanÃ§amentos de fluxo de caixa")
    public ResponseEntity<Page<FluxoCaixaResponse>> findAll(
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) Long contaBancariaId,
            @RequestParam(required = false) Long centroCustoId,
            @RequestParam(required = false) java.time.LocalDate dataInicio,
            @RequestParam(required = false) java.time.LocalDate dataFim,
            Pageable pageable) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.findAll(empresa, contaBancariaId, centroCustoId, dataInicio, dataFim, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lanÃ§amento por ID")
    public ResponseEntity<FluxoCaixaResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.findById(id, empresa));
    }

    @PostMapping
    @Operation(summary = "Criar lanÃ§amento")
    public ResponseEntity<FluxoCaixaResponse> create(
            @RequestParam(required = false) Long empresaId,
            @Valid @RequestBody FluxoCaixaRequest request) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresa, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lanÃ§amento")
    public ResponseEntity<FluxoCaixaResponse> update(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId,
            @Valid @RequestBody FluxoCaixaRequest request) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        return ResponseEntity.ok(service.update(id, empresa, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir lanÃ§amento")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam(required = false) Long empresaId) {
        Long empresa = empresaId != null ? empresaId : usuarioService.getCurrentUser().getEmpresaId();
        service.delete(id, empresa);
        return ResponseEntity.noContent().build();
    }
}
