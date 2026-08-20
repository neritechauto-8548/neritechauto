package com.neritech.saas.veiculo.controller;

import com.neritech.saas.veiculo.dto.VeiculoRequest;
import com.neritech.saas.veiculo.dto.VeiculoResponse;
import com.neritech.saas.veiculo.service.VeiculoService;
import jakarta.validation.Valid;
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('VEICULO_EXCLUIR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
