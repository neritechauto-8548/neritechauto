package com.neritech.saas.agendamento.controller;

import com.neritech.saas.agendamento.dto.TipoAgendamentoRequest;
import com.neritech.saas.agendamento.dto.TipoAgendamentoResponse;
import com.neritech.saas.agendamento.service.TipoAgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/agendamentos/tipos")
@RequiredArgsConstructor
public class TipoAgendamentoController {

    private final TipoAgendamentoService service;

    @PostMapping
    public ResponseEntity<TipoAgendamentoResponse> criar(@Valid @RequestBody TipoAgendamentoRequest request) {
        TipoAgendamentoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoAgendamentoResponse> buscarPorId(@PathVariable Long id) {
        TipoAgendamentoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<TipoAgendamentoResponse>> listarPorEmpresa(@PathVariable Long empresaId) {
        List<TipoAgendamentoResponse> responses = service.listarPorEmpresa(empresaId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoAgendamentoResponse> atualizar(@PathVariable Long id,
            @Valid @RequestBody TipoAgendamentoRequest request) {
        TipoAgendamentoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
