package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.dto.*;
import com.neritech.saas.rh.service.CertificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/rh/certificacoes")
@RequiredArgsConstructor
@Tag(name = "CertificaÃ§Ãµes", description = "GestÃ£o de certificaÃ§Ãµes")
public class CertificacaoController {
    private final CertificacaoService service;

    @GetMapping("/funcionario/{funcionarioId}")
    @Operation(summary = "Listar certificaÃ§Ãµes do funcionÃ¡rio")
    public ResponseEntity<List<CertificacaoResponse>> findByFuncionario(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.findByFuncionario(funcionarioId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar certificaÃ§Ã£o por ID")
    public ResponseEntity<CertificacaoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar certificaÃ§Ã£o")
    public ResponseEntity<CertificacaoResponse> create(@Valid @RequestBody CertificacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar certificaÃ§Ã£o")
    public ResponseEntity<CertificacaoResponse> update(@PathVariable Long id,
            @Valid @RequestBody CertificacaoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir certificaÃ§Ã£o")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
