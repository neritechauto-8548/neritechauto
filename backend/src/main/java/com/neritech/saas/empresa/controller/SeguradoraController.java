package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.dto.SeguradoraRequest;
import com.neritech.saas.empresa.dto.SeguradoraResponse;
import com.neritech.saas.empresa.service.SeguradoraService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/seguradoras")
public class SeguradoraController {
    private final SeguradoraService service;

    public SeguradoraController(SeguradoraService service) { this.service = service; }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('OS_EDITAR')")
    public Page<SeguradoraResponse> listar(@RequestParam(required = false) String busca,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String nomeFantasia,
            @RequestParam(required = false) Boolean ativo, @PageableDefault(size = 20) Pageable page) {
        return service.listar(busca, cnpj, nome, nomeFantasia, ativo, page);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('OS_EDITAR')")
    public SeguradoraResponse obter(@PathVariable Long id) { return service.obter(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public SeguradoraResponse criar(@Valid @RequestBody SeguradoraRequest body) {
        return service.criar(body);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public SeguradoraResponse editar(@PathVariable Long id,
            @RequestHeader(value = "If-Match", required = false) Integer versao,
            @Valid @RequestBody SeguradoraRequest body) {
        return service.editar(id, body, versao);
    }

    /** A rota legada DELETE inativa o cadastro sem excluir OS ou vínculos. */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public void inativar(@PathVariable Long id) { service.inativar(id); }

    @GetMapping("/{id}/historico")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('OS_EDITAR')")
    public Page<SeguradoraService.Historico> historico(@PathVariable Long id,
            @PageableDefault(size = 20) Pageable page) {
        return service.historico(id, page);
    }

    @GetMapping("/os/{ordemId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('OS_EDITAR')")
    public ResponseEntity<SeguradoraService.VinculoOS> vinculoDaOS(@PathVariable Long ordemId) {
        var value = service.vinculoDaOS(ordemId);
        return value == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(value);
    }

    @PostMapping("/{id}/os/{ordemId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    @ResponseStatus(HttpStatus.CREATED)
    public SeguradoraService.VinculoOS vincular(@PathVariable Long id, @PathVariable Long ordemId,
            @RequestParam String tipo) {
        return service.vincular(id, ordemId, tipo);
    }

    @DeleteMapping("/{id}/os/{ordemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    public void desvincular(@PathVariable Long id, @PathVariable Long ordemId) {
        service.desvincular(id, ordemId);
    }
}
