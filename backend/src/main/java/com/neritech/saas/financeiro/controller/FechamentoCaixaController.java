package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.FechamentoCaixaRequest;
import com.neritech.saas.financeiro.dto.FechamentoCaixaResponse;
import com.neritech.saas.financeiro.service.FechamentoCaixaService;
import com.neritech.saas.gestaoUsuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping({"/v1/financeiro/fechamento-caixa", "/api/v1/financeiro/fechamento-caixa"})
@RequiredArgsConstructor
@Tag(name = "Fechamento de Caixa", description = "Gestão de fechamento de caixa")
public class FechamentoCaixaController {

    private final FechamentoCaixaService service;
    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('FIN_VIS_CAIXA')")
    @Operation(summary = "Listar fechamentos de caixa")
    public ResponseEntity<Page<FechamentoCaixaResponse>> findAll(
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(currentEmpresaId(), dataInicio, dataFim, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_VIS_CAIXA')")
    @Operation(summary = "Buscar fechamento por ID")
    public ResponseEntity<FechamentoCaixaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id, currentEmpresaId()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('FIN_FECHAMENTO')")
    @Operation(summary = "Criar fechamento de caixa")
    public ResponseEntity<FechamentoCaixaResponse> create(
            @Valid @RequestBody FechamentoCaixaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(currentEmpresaId(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_FECHAMENTO')")
    @Operation(summary = "Atualizar fechamento de caixa")
    public ResponseEntity<FechamentoCaixaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody FechamentoCaixaRequest request) {
        return ResponseEntity.ok(service.update(id, currentEmpresaId(), request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_FECHAMENTO')")
    @Operation(summary = "Excluir fechamento de caixa")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id, currentEmpresaId());
        return ResponseEntity.noContent().build();
    }

    private Long currentEmpresaId() {
        return usuarioService.getCurrentUser().getEmpresaId();
    }
}
