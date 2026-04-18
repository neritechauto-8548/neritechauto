package com.neritech.saas.agendamento.controller;

import com.neritech.saas.agendamento.dto.NoShowRequest;
import com.neritech.saas.agendamento.dto.NoShowResponse;
import com.neritech.saas.agendamento.service.NoShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/agendamentos/no-shows")
@RequiredArgsConstructor
public class NoShowController {

    private final NoShowService service;

    @PostMapping
    public ResponseEntity<NoShowResponse> criar(@Valid @RequestBody NoShowRequest request) {
        NoShowResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoShowResponse> buscarPorId(@PathVariable Long id) {
        NoShowResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<NoShowResponse>> listarPorCliente(@PathVariable Long clienteId) {
        List<NoShowResponse> responses = service.listarPorCliente(clienteId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
