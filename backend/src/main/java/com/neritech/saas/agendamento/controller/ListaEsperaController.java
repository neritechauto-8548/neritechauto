package com.neritech.saas.agendamento.controller;

import com.neritech.saas.agendamento.dto.ListaEsperaRequest;
import com.neritech.saas.agendamento.dto.ListaEsperaResponse;
import com.neritech.saas.agendamento.service.ListaEsperaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/agendamentos/lista-espera")
@RequiredArgsConstructor
public class ListaEsperaController {

    private final ListaEsperaService service;

    @PostMapping
    public ResponseEntity<ListaEsperaResponse> criar(@Valid @RequestBody ListaEsperaRequest request) {
        ListaEsperaResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaEsperaResponse> buscarPorId(@PathVariable Long id) {
        ListaEsperaResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<ListaEsperaResponse>> listarPorEmpresa(@PathVariable Long empresaId) {
        List<ListaEsperaResponse> responses = service.listarPorEmpresa(empresaId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
