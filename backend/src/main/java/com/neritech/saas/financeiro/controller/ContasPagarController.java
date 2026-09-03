package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.ContasPagarRequest;
import com.neritech.saas.financeiro.dto.ContasPagarResponse;
import com.neritech.saas.financeiro.service.ContasPagarService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/v1/financeiro/contas-pagar", "/api/v1/financeiro/contas-pagar"})
@RequiredArgsConstructor
@Tag(name = "Contas a Pagar", description = "Gestão de contas a pagar")
public class ContasPagarController {

    private final ContasPagarService service;
    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('FIN_LISTAR_CONTAS')")
    @Operation(summary = "Listar contas a pagar")
    public ResponseEntity<Page<ContasPagarResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(currentEmpresaId(), pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_LISTAR_CONTAS')")
    @Operation(summary = "Buscar conta a pagar por ID")
    public ResponseEntity<ContasPagarResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id, currentEmpresaId()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('FIN_INC_CONTAS')")
    @Operation(summary = "Criar conta a pagar")
    public ResponseEntity<ContasPagarResponse> create(@Valid @RequestBody ContasPagarRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(currentEmpresaId(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_EDIT_CONTA')")
    @Operation(summary = "Atualizar conta a pagar")
    public ResponseEntity<ContasPagarResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ContasPagarRequest request) {
        return ResponseEntity.ok(service.update(id, currentEmpresaId(), request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_EXC_CONTAS')")
    @Operation(summary = "Excluir conta a pagar")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id, currentEmpresaId());
        return ResponseEntity.noContent().build();
    }

    private Long currentEmpresaId() {
        return usuarioService.getCurrentUser().getEmpresaId();
    }
}
