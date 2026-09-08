package com.neritech.saas.financeiro.controller;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.financeiro.dto.PagamentoRequest;
import com.neritech.saas.financeiro.dto.PagamentoResponse;
import com.neritech.saas.financeiro.service.PagamentoService;
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
@RequestMapping("/v1/financeiro/pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "Gestão de pagamentos")
public class PagamentoController {

    private final PagamentoService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('FIN_LISTAR_CONTAS','OS_NEG_PAGAMENTO')")
    @Operation(summary = "Listar pagamentos")
    public ResponseEntity<Page<PagamentoResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(TenantAccess.requireCurrentTenant(), pageable));
    }

    @GetMapping("/fatura/{faturaId}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('FIN_LISTAR_CONTAS','OS_NEG_PAGAMENTO')")
    @Operation(summary = "Listar pagamentos por fatura")
    public ResponseEntity<Page<PagamentoResponse>> findByFatura(
            @PathVariable Long faturaId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByFatura(TenantAccess.requireCurrentTenant(), faturaId, pageable));
    }

    @GetMapping("/ordem-servico/{osId}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('FIN_LISTAR_CONTAS','OS_NEG_PAGAMENTO')")
    @Operation(summary = "Listar pagamentos por ordem de serviço")
    public ResponseEntity<Page<PagamentoResponse>> findByOsId(
            @PathVariable Long osId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByOsId(TenantAccess.requireCurrentTenant(), osId, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('FIN_LISTAR_CONTAS','OS_NEG_PAGAMENTO')")
    @Operation(summary = "Buscar pagamento por ID")
    public ResponseEntity<PagamentoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id, TenantAccess.requireCurrentTenant()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('FIN_INC_CONTAS','OS_NEG_PAGAMENTO')")
    @Operation(summary = "Criar pagamento")
    public ResponseEntity<PagamentoResponse> create(@Valid @RequestBody PagamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(TenantAccess.requireCurrentTenant(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('FIN_EDIT_CONTA')")
    @Operation(summary = "Atualizar pagamento")
    public ResponseEntity<PagamentoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PagamentoRequest request) {
        return ResponseEntity.ok(service.update(id, TenantAccess.requireCurrentTenant(), request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('FIN_EXC_CONTAS')")
    @Operation(summary = "Excluir pagamento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id, TenantAccess.requireCurrentTenant());
        return ResponseEntity.noContent().build();
    }
}
