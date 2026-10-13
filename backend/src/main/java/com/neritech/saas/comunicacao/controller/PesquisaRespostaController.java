package com.neritech.saas.comunicacao.controller;

import com.neritech.saas.comunicacao.dto.PesquisaRespostaRequest;
import com.neritech.saas.comunicacao.dto.PesquisaRespostaResponse;
import com.neritech.saas.comunicacao.dto.PesquisaResumoResponse;
import com.neritech.saas.comunicacao.service.PesquisaRespostaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comunicacao/questionarios/{questionarioId}/respostas")
public class PesquisaRespostaController {
    private final PesquisaRespostaService service;
    public PesquisaRespostaController(PesquisaRespostaService service) { this.service = service; }

    // Consulta de dados associados a clientes requer perfil gerencial.
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public Page<PesquisaRespostaResponse> listar(@PathVariable Long questionarioId, Pageable pageable) {
        return service.listar(questionarioId, pageable);
    }

    @GetMapping("/resumo")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public PesquisaResumoResponse resumo(@PathVariable Long questionarioId) {
        return service.resumo(questionarioId);
    }

    // Registro feito somente por usuário autenticado e autorizado.
    // Formulário público, link por cliente e distribuição não estão disponíveis ainda.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
    public PesquisaRespostaResponse registrar(@PathVariable Long questionarioId,
            @Valid @RequestBody PesquisaRespostaRequest input) {
        return service.registrar(questionarioId, input);
    }
}
