package com.neritech.saas.cliente.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.neritech.saas.cliente.domain.EnderecoCliente;
import com.neritech.saas.cliente.dto.EnderecoClienteRequest;
import com.neritech.saas.cliente.dto.EnderecoClienteResponse;
import com.neritech.saas.cliente.mapper.EnderecoClienteMapper;
import com.neritech.saas.cliente.service.EnderecoClienteService;

@RestController
@RequestMapping("/v1/clientes/{clienteId}/enderecos")
@Tag(name = "EndereÃ§os de Clientes", description = "Endpoints para gestÃ£o de endereÃ§os dos clientes")
public class EnderecoClienteController {

    private final EnderecoClienteService service;

    public EnderecoClienteController(EnderecoClienteService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar endereÃ§os do cliente")
    public Page<EnderecoClienteResponse> list(@PathVariable Long clienteId, Pageable pageable) {
        return service.listByCliente(clienteId, pageable)
                .map(EnderecoClienteMapper::toResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar endereÃ§o por ID")
    public ResponseEntity<EnderecoClienteResponse> getById(@PathVariable Long clienteId, @PathVariable Long id) {
        EnderecoCliente e = service.findById(id);
        if (e.getCliente() == null || !e.getCliente().getId().equals(clienteId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(EnderecoClienteMapper.toResponse(e));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar endereÃ§o para cliente")
    public EnderecoClienteResponse create(@PathVariable Long clienteId,
            @Valid @RequestBody EnderecoClienteRequest request) {
        EnderecoCliente saved = service.create(clienteId, request);
        return EnderecoClienteMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar endereÃ§o do cliente")
    public ResponseEntity<EnderecoClienteResponse> update(@PathVariable Long clienteId,
            @PathVariable Long id,
            @Valid @RequestBody EnderecoClienteRequest request) {
        EnderecoCliente saved = service.update(clienteId, id, request);
        return ResponseEntity.ok(EnderecoClienteMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir endereÃ§o do cliente")
    public void delete(@PathVariable Long clienteId, @PathVariable Long id) {
        service.delete(clienteId, id);
    }
}
