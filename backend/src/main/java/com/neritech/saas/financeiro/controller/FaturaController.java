package com.neritech.saas.financeiro.controller;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.financeiro.dto.FaturaRequest;
import com.neritech.saas.financeiro.dto.FaturaResponse;
import com.neritech.saas.financeiro.service.FaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/financeiro/faturas")
@RequiredArgsConstructor
@Tag(name = "Faturas", description = "Gestão de faturas")
public class FaturaController {

    private final FaturaService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('FIN_LISTAR_CONTAS','OS_NEG_PAGAMENTO')")
    @Operation(summary = "Listar faturas")
    public ResponseEntity<Page<FaturaResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(TenantAccess.requireCurrentTenant(), pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('FIN_LISTAR_CONTAS','OS_NEG_PAGAMENTO')")
    @Operation(summary = "Buscar fatura por ID")
    public ResponseEntity<FaturaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id, TenantAccess.requireCurrentTenant()));
    }

    @GetMapping("/ordem-servico/{osId}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('FIN_LISTAR_CONTAS','OS_NEG_PAGAMENTO')")
    @Operation(summary = "Buscar fatura por OS")
    public ResponseEntity<FaturaResponse> findByOrdemServico(@PathVariable Long osId) {
        try {
            return ResponseEntity.ok(service.findByOrdemServico(osId, TenantAccess.requireCurrentTenant()));
        } catch (jakarta.persistence.EntityNotFoundException ex) {
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('FIN_INC_CONTAS')")
    @Operation(summary = "Criar fatura")
    public ResponseEntity<FaturaResponse> create(@Valid @RequestBody FaturaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(TenantAccess.requireCurrentTenant(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('FIN_EDIT_CONTA')")
    @Operation(summary = "Atualizar fatura")
    public ResponseEntity<FaturaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody FaturaRequest request) {
        return ResponseEntity.ok(service.update(id, TenantAccess.requireCurrentTenant(), request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('FIN_EXC_CONTAS')")
    @Operation(summary = "Excluir fatura")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id, TenantAccess.requireCurrentTenant());
        return ResponseEntity.noContent().build();
    }
}
