package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.ComissaoFuncionarioRequest;
import com.neritech.saas.financeiro.dto.ComissaoFuncionarioResponse;
import com.neritech.saas.financeiro.service.ComissaoFuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') or hasAuthority('FIN_COMISSOES') or hasAuthority('GERAL_CONFIG_SISTEMA')")
@RequestMapping("/v1/financeiro/comissoes")
@RequiredArgsConstructor
@Tag(name = "ComissÃµes", description = "GestÃ£o de comissÃµes de funcionÃ¡rios")
public class ComissaoFuncionarioController {

    private final ComissaoFuncionarioService service;

    @GetMapping
    @Operation(summary = "Listar comissÃµes")
    public ResponseEntity<Page<ComissaoFuncionarioResponse>> findAll(
            @RequestParam Long empresaId,
            @RequestParam(required=false) String nome,
            @RequestParam(required=false) Long funcionarioId,
            @RequestParam(required=false) Long ordemServicoId,
            @RequestParam(required=false) Boolean paga,
            @RequestParam(required=false) java.time.LocalDate inicio,
            @RequestParam(required=false) java.time.LocalDate fim,
            Pageable pageable) {
        return ResponseEntity.ok(service.listar(empresaId, nome, funcionarioId, ordemServicoId,
                paga, inicio, fim, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar comissÃ£o por ID")
    public ResponseEntity<ComissaoFuncionarioResponse> findById(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.findById(id, empresaId));
    }

    @PostMapping
    @Operation(summary = "Criar comissÃ£o")
    public ResponseEntity<ComissaoFuncionarioResponse> create(
            @RequestParam Long empresaId,
            @Valid @RequestBody ComissaoFuncionarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(empresaId, request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar comissÃ£o")
    public ResponseEntity<ComissaoFuncionarioResponse> update(
            @PathVariable Long id,
            @RequestParam Long empresaId,
            @Valid @RequestBody ComissaoFuncionarioRequest request) {
        return ResponseEntity.ok(service.update(id, empresaId, request));
    }

    public record QuitarRequest(@jakarta.validation.constraints.NotNull java.time.LocalDate dataPagamento,
            String observacao, Boolean gerarContaPagar) {}

    @PostMapping("/{id}/aprovar")
    public ResponseEntity<ComissaoFuncionarioResponse> aprovar(@PathVariable Long id, @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.aprovar(id, empresaId));
    }

    @PostMapping("/{id}/quitar")
    public ResponseEntity<ComissaoFuncionarioResponse> quitar(@PathVariable Long id, @RequestParam Long empresaId,
            @Valid @RequestBody QuitarRequest request) {
        if (Boolean.TRUE.equals(request.gerarContaPagar())) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY, "Geração de contas a pagar ainda não integrada; operação recusada");
        }
        return ResponseEntity.ok(service.quitar(id, empresaId, request.dataPagamento(), request.observacao()));
    }

    @PostMapping("/{id}/estornar")
    public ResponseEntity<ComissaoFuncionarioResponse> estornar(@PathVariable Long id, @RequestParam Long empresaId) {
        return ResponseEntity.ok(service.estornar(id, empresaId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir comissÃ£o")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long empresaId) {
        service.delete(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}
