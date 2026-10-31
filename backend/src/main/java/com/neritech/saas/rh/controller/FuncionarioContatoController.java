package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.service.FuncionarioContatoService;
import com.neritech.saas.rh.service.FuncionarioContatoService.Contato;
import com.neritech.saas.rh.service.FuncionarioContatoService.ContatoRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/rh/funcionarios/{funcionarioId}/contatos")
@PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
@RequiredArgsConstructor
public class FuncionarioContatoController {
    private final FuncionarioContatoService service;
    @GetMapping public List<Contato> listar(@PathVariable long funcionarioId) {
        return service.listar(funcionarioId);
    }
    @PostMapping public ResponseEntity<Contato> criar(@PathVariable long funcionarioId,
            @Valid @RequestBody ContatoRequest body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(funcionarioId,body));
    }
    @PutMapping("/{id}") public Contato atualizar(@PathVariable long funcionarioId,@PathVariable long id,
            @Valid @RequestBody ContatoRequest body) { return service.atualizar(funcionarioId,id,body); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> excluir(@PathVariable long funcionarioId,@PathVariable long id) {
        service.excluir(funcionarioId,id); return ResponseEntity.noContent().build();
    }
}
