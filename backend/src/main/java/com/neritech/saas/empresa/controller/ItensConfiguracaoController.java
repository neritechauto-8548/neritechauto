package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.service.ItensConfiguracaoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/itens-configuracao")
@PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
public class ItensConfiguracaoController {
    private final ItensConfiguracaoService servico;
    public ItensConfiguracaoController(ItensConfiguracaoService servico) { this.servico = servico; }
    @GetMapping public List<ItensConfiguracaoService.Item> listar(@RequestParam String grupo, @RequestParam(required = false) String busca) { return servico.listar(grupo, busca); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ItensConfiguracaoService.Item criar(@RequestBody ItensConfiguracaoService.Alteracao alteracao) { return servico.criar(alteracao); }
    @PutMapping("/{id}") public ItensConfiguracaoService.Item editar(@PathVariable Long id, @RequestBody ItensConfiguracaoService.Alteracao alteracao) { return servico.editar(id, alteracao); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { servico.excluir(id); }
}
