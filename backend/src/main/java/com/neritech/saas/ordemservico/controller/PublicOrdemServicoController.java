package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.ordemservico.dto.OrdemServicoResponse;
import com.neritech.saas.ordemservico.service.OrdemServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/public/ordem-servico", "/api/public/ordem-servico"})
@RequiredArgsConstructor
@Tag(name = "Consulta de O.S.", description = "Endpoints legados de consulta autenticada e restrita à empresa")
public class PublicOrdemServicoController {

    private final OrdemServicoService service;

    @GetMapping("/consulta")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Consultar O.S. por CPF e Número")
    public ResponseEntity<OrdemServicoResponse> consultar(
            @RequestParam String cpf,
            @RequestParam String numeroOs) {
        
        // Remove caracteres não numéricos do CPF para busca
        String cpfLimpo = cpf.replaceAll("\\D", "");
        
        return service.findByCpfAndNumeroOS(cpfLimpo, numeroOs)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Obter detalhes da O.S. na empresa autenticada")
    public ResponseEntity<OrdemServicoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
