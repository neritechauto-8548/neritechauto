package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.service.PoliticasGeraisService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/politicas-gerais")
@PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
public class PoliticasGeraisController {
    private final PoliticasGeraisService service;
    public PoliticasGeraisController(PoliticasGeraisService service) { this.service = service; }

    @GetMapping
    public Page<PoliticasGeraisService.Politica> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String secao,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20) Pageable page) {
        return service.listar(busca, secao, ativo, page);
    }

    @GetMapping("/{chave}")
    public PoliticasGeraisService.Politica obter(@PathVariable String chave) {
        return service.obter(chave);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PoliticasGeraisService.Politica criar(@RequestBody PoliticasGeraisService.Alterar form) {
        return service.criar(form);
    }

    @PutMapping("/{chave}")
    public PoliticasGeraisService.Politica editar(@PathVariable String chave,
            @RequestHeader(value = "If-Match", required = false) Integer versao,
            @RequestBody PoliticasGeraisService.Alterar form) {
        if (form == null || !chave.equals(form.chave())) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Chave do corpo não corresponde à rota");
        }
        return service.editar(chave, form.valor(), versao);
    }

    @PatchMapping("/{chave}/status")
    public PoliticasGeraisService.Politica situacao(@PathVariable String chave,
            @RequestHeader(value = "If-Match", required = false) Integer versao,
            @RequestBody PoliticasGeraisService.Estado body) {
        if (body == null) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return service.situacao(chave, body.ativo(), versao);
    }

    @GetMapping("/{chave}/historico")
    public Page<PoliticasGeraisService.Alteracao> historico(@PathVariable String chave,
            @PageableDefault(size = 20) Pageable page) {
        return service.historico(chave, page);
    }
}
