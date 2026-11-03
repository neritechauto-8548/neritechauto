package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.service.ConfiguracaoComplementarEmpresaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/configuracoes-complementares-empresa")
@PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('ADM_EMPRESA')")
public class ConfiguracaoComplementarEmpresaController {
    private final ConfiguracaoComplementarEmpresaService servico;

    public ConfiguracaoComplementarEmpresaController(ConfiguracaoComplementarEmpresaService servico) {
        this.servico = servico;
    }

    @GetMapping
    public List<ConfiguracaoComplementarEmpresaService.Configuracao> listar() {
        return servico.listar();
    }

    @PutMapping("/{secao}")
    public ConfiguracaoComplementarEmpresaService.Configuracao salvar(
            @PathVariable String secao,
            @RequestBody ConfiguracaoComplementarEmpresaService.Alteracao alteracao) {
        return servico.salvar(secao, alteracao);
    }
}
