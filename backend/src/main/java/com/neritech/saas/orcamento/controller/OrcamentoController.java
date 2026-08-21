package com.neritech.saas.orcamento.controller;

import com.neritech.saas.orcamento.dto.OrcamentoDraftRequest;
import com.neritech.saas.orcamento.dto.OrcamentoDraftResponse;
import com.neritech.saas.orcamento.service.OrcamentoDraftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orcamentos")
@Tag(name = "Orçamentos", description = "Contratos canônicos do módulo de Orçamentos")
public class OrcamentoController {

    private final OrcamentoDraftService service;

    public OrcamentoController(OrcamentoDraftService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('OS_INCLUIR')")
    @Operation(
            summary = "Criar rascunho de orçamento",
            description = "Cria um orçamento RASCUNHO. Tenant e número comercial são definidos pelo backend; IDs contextuais são revalidados.")
    public ResponseEntity<OrcamentoDraftResponse> create(@Valid @RequestBody OrcamentoDraftRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
}
