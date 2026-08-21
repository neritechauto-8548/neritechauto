package com.neritech.saas.cliente.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.neritech.saas.cliente.domain.ContatoCliente;
import com.neritech.saas.cliente.dto.ContatoClienteRequest;
import com.neritech.saas.cliente.dto.ContatoClienteResponse;
import com.neritech.saas.cliente.dto.ContatoClienteSummaryResponse;
import com.neritech.saas.cliente.mapper.ContatoClienteMapper;
import com.neritech.saas.cliente.service.ContatoClienteService;

@RestController
@RequestMapping("/v1/clientes/{clienteId}/contatos")
@Tag(name = "Contatos de Cliente", description = "Gerenciamento de contatos dos clientes")
public class ContatoClienteController {

    private final ContatoClienteService service;

    public ContatoClienteController(ContatoClienteService service) {
        this.service = service;
    }

    @GetMapping("/resumo")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Listar contatos mascarados para visão 360°")
    public Page<ContatoClienteSummaryResponse> listarResumo(@PathVariable Long clienteId, Pageable pageable) {
        return service.listarPorCliente(clienteId, pageable).map(ContatoClienteMapper::toSummaryResponse);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Listar contatos completos do cliente (legado)")
    public Page<ContatoClienteResponse> listar(@PathVariable Long clienteId, Pageable pageable) {
        Page<ContatoCliente> page = service.listarPorCliente(clienteId, pageable);
        return page.map(ContatoClienteMapper::toResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Buscar contato por ID")
    public ContatoClienteResponse buscar(@PathVariable Long clienteId, @PathVariable Long id) {
        ContatoCliente contato = service.buscarPorId(id);
        if (!contato.getCliente().getId().equals(clienteId)) {
            throw new IllegalArgumentException("Contato não pertence ao cliente informado");
        }
        return ContatoClienteMapper.toResponse(contato);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CLIENTE_CRIAR','CLIENTE_EDITAR')")
    @Operation(summary = "Criar contato para cliente")
    public ResponseEntity<ContatoClienteResponse> criar(
            @PathVariable Long clienteId,
            @Valid @RequestBody ContatoClienteRequest request) {
        ContatoCliente criado = service.criar(clienteId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ContatoClienteMapper.toResponse(criado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTE_EDITAR')")
    @Operation(summary = "Atualizar contato do cliente")
    public ContatoClienteResponse atualizar(
            @PathVariable Long clienteId,
            @PathVariable Long id,
            @Valid @RequestBody ContatoClienteRequest request) {
        ContatoCliente atualizado = service.atualizar(id, request);
        if (!atualizado.getCliente().getId().equals(clienteId)) {
            throw new IllegalArgumentException("Contato não pertence ao cliente informado");
        }
        return ContatoClienteMapper.toResponse(atualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTE_EDITAR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover contato do cliente")
    public void remover(@PathVariable Long clienteId, @PathVariable Long id) {
        ContatoCliente contato = service.buscarPorId(id);
        if (!contato.getCliente().getId().equals(clienteId)) {
            throw new IllegalArgumentException("Contato não pertence ao cliente informado");
        }
        service.remover(id);
    }
}
