package com.neritech.saas.rh.controller;

import com.neritech.saas.rh.service.EpiService;
import com.neritech.saas.rh.service.EpiService.EpiRequest;
import com.neritech.saas.rh.service.EpiService.EpiResponse;
import com.neritech.saas.rh.service.EpiService.EntregaRequest;
import com.neritech.saas.rh.service.EpiService.EntregaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/rh")
@PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA')")
@RequiredArgsConstructor
public class EpiController {
    private final EpiService service;

    @GetMapping("/epis")
    public Page<EpiResponse> listar(@RequestParam(required=false) String q,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="20") int size) {
        return service.listar(q, page, size);
    }

    @GetMapping("/epis/{id}")
    public EpiResponse buscar(@PathVariable long id) { return service.buscar(id); }

    @PostMapping("/epis")
    public ResponseEntity<EpiResponse> criar(@Valid @RequestBody EpiRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @PutMapping("/epis/{id}")
    public EpiResponse atualizar(@PathVariable long id, @Valid @RequestBody EpiRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/epis/{id}")
    public ResponseEntity<Void> inativar(@PathVariable long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/funcionarios/{id}/epis")
    public List<EntregaResponse> entregas(@PathVariable long id) {
        return service.entregas(id);
    }

    @PostMapping("/funcionarios/{id}/epis")
    public ResponseEntity<EntregaResponse> entregar(@PathVariable long id,
            @Valid @RequestBody EntregaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.entregar(id, request));
    }
}
