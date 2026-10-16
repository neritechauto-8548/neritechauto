package com.neritech.saas.produtoServico.controller;

import com.neritech.saas.produtoServico.dto.*;
import com.neritech.saas.produtoServico.service.TransportadoraService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/transportadoras")
public class TransportadoraController {
    private final TransportadoraService service;
    public TransportadoraController(TransportadoraService service) { this.service = service; }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORN_LISTAR_PEDIDOS')")
    public Page<TransportadoraResponse> listar(@RequestParam(required = false) String busca,
            @RequestParam(required = false) String regiao, @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20) Pageable page) {
        return service.listar(busca, regiao, ativo, page);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORN_LISTAR_PEDIDOS')")
    public TransportadoraResponse obter(@PathVariable Long id) { return service.obter(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORN_INC_PEDIDOS')")
    public TransportadoraResponse criar(@Valid @RequestBody TransportadoraRequest req) {
        return service.criar(req);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORN_EDIT_PEDIDOS')")
    public TransportadoraResponse editar(@PathVariable Long id,
            @RequestHeader(value = "If-Match", required = false) Integer version,
            @Valid @RequestBody TransportadoraRequest req) {
        return service.editar(id, req, version);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORN_EDIT_PEDIDOS')")
    public void inativar(@PathVariable Long id) { service.inativar(id); }

    @GetMapping("/{id}/historico")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORN_LISTAR_PEDIDOS')")
    public Page<TransportadoraService.Historico> historico(@PathVariable Long id,
            @PageableDefault(size = 20) Pageable page) {
        return service.historico(id, page);
    }

    @GetMapping("/pedidos/{pedidoId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORN_LISTAR_PEDIDOS')")
    public TransportadoraPedidoResponse pedido(@PathVariable Long pedidoId) {
        return service.pedido(pedidoId);
    }

    @PutMapping("/{id}/pedidos/{pedidoId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORN_EDIT_PEDIDOS')")
    public TransportadoraPedidoResponse vincular(@PathVariable Long id, @PathVariable Long pedidoId,
            @Valid @RequestBody TransportadoraPedidoRequest req) {
        return service.vincular(id, pedidoId, req);
    }

    @DeleteMapping("/{id}/pedidos/{pedidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('FORN_EDIT_PEDIDOS')")
    public void desvincular(@PathVariable Long id, @PathVariable Long pedidoId) {
        service.desvincular(id, pedidoId);
    }
}
