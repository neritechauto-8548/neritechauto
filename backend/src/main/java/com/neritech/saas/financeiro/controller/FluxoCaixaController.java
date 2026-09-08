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

@RestController
@RequestMapping({"/v1/financeiro/fluxo-caixa", "/api/v1/financeiro/fluxo-caixa"})
@RequiredArgsConstructor
@Tag(name = "Fluxo de Caixa", description = "Gestão de fluxo de caixa")
public class FluxoCaixaController {

    private final FluxoCaixaService service;
    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('FIN_VIS_CAIXA')")
    @Operation(summary = "Listar lançamentos de fluxo de caixa")
    public ResponseEntity<Page<FluxoCaixaResponse>> findAll(
            @RequestParam(required = false) Long contaBancariaId,
            @RequestParam(required = false) Long centroCustoId,
            @RequestParam(required = false) java.time.LocalDate dataInicio,
            @RequestParam(required = false) java.time.LocalDate dataFim,
            @RequestParam(required = false, defaultValue = "false") Boolean includeClosed,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(
                currentEmpresaId(),
                contaBancariaId,
                centroCustoId,
                dataInicio,
                dataFim,
                includeClosed,
                pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_VIS_CAIXA')")
    @Operation(summary = "Buscar lançamento por ID")
    public ResponseEntity<FluxoCaixaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id, currentEmpresaId()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('FIN_INC_CONTAS')")
    @Operation(summary = "Criar lançamento")
    public ResponseEntity<FluxoCaixaResponse> create(@Valid @RequestBody FluxoCaixaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(currentEmpresaId(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_EDIT_CONTA')")
    @Operation(summary = "Atualizar lançamento")
    public ResponseEntity<FluxoCaixaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody FluxoCaixaRequest request) {
        return ResponseEntity.ok(service.update(id, currentEmpresaId(), request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_EXC_CONTAS')")
    @Operation(summary = "Excluir lançamento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id, currentEmpresaId());
        return ResponseEntity.noContent().build();
    }

    private Long currentEmpresaId() {
        return usuarioService.getCurrentUser().getEmpresaId();
    }
}
