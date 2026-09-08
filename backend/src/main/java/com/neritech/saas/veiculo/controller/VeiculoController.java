package com.neritech.saas.veiculo.controller;

import com.neritech.saas.veiculo.dto.VeiculoRequest;
import com.neritech.saas.veiculo.dto.VeiculoResponse;
import com.neritech.saas.veiculo.dto.VeiculoSummaryResponse;
import com.neritech.saas.veiculo.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/veiculos")
public class VeiculoController {

    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('VEICULO_CRIAR')")
    public ResponseEntity<VeiculoResponse> create(@RequestBody @Valid VeiculoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('VEICULO_EDITAR')")
    public ResponseEntity<VeiculoResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid VeiculoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/resumo")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    public ResponseEntity<List<VeiculoSummaryResponse>> findSummaryByCliente(@RequestParam Long clienteId) {
        return ResponseEntity.ok(service.findByCliente(clienteId).stream()
                .map(VeiculoController::toSummary)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    public ResponseEntity<VeiculoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    public ResponseEntity<List<VeiculoResponse>> findAll(
            @RequestParam(required = false) Long clienteId) {
        if (clienteId != null) {
            return ResponseEntity.ok(service.findByCliente(clienteId));
        }
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/placa/{placa}")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    public ResponseEntity<VeiculoResponse> findByPlaca(@PathVariable String placa) {
        return service.findByPlaca(placa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/placa/{placa}/consulta-externa")
    @PreAuthorize("hasAuthority('VEICULO_CRIAR') or hasAuthority('VEICULO_EDITAR')")
    public ResponseEntity<VeiculoResponse> lookupExternalByPlaca(@PathVariable String placa) {
        return service.lookupExternalByPlaca(placa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasAuthority('VEICULO_EXCLUIR')")
    public ResponseEntity<VeiculoResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(service.deactivate(id));
    }

    @PatchMapping("/{id}/reativar")
    @PreAuthorize("hasAuthority('VEICULO_EDITAR')")
    public ResponseEntity<VeiculoResponse> reactivate(@PathVariable Long id) {
        return ResponseEntity.ok(service.reactivate(id));
    }

    /** Compatibilidade: DELETE apenas inativa e preserva histórico. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('VEICULO_EXCLUIR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private static VeiculoSummaryResponse toSummary(VeiculoResponse vehicle) {
        return new VeiculoSummaryResponse(
                vehicle.id(),
                vehicle.marcaNome(),
                vehicle.modeloNome(),
                vehicle.anoFabricacao(),
                vehicle.anoModelo(),
                maskPlate(vehicle.placa()),
                vehicle.status());
    }

    private static String maskPlate(String value) {
        if (value == null || value.isBlank()) return "Placa protegida";
        String normalized = value.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        if (normalized.length() < 4) return "Placa protegida";
        return normalized.substring(0, Math.min(3, normalized.length())) + "••" + normalized.substring(normalized.length() - 2);
    }
}
