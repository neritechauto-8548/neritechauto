package com.neritech.saas.veiculo.controller;

import com.neritech.saas.veiculo.domain.MarcaVeiculo;
import com.neritech.saas.veiculo.dto.MarcaVeiculoRequest;
import com.neritech.saas.veiculo.dto.MarcaVeiculoResponse;
import com.neritech.saas.veiculo.mapper.MarcaVeiculoMapper;
import com.neritech.saas.veiculo.service.MarcaVeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/marcas-veiculos")
@Tag(name = "Marcas de VeÃ­culos", description = "Gerenciamento de marcas de veÃ­culos")
public class MarcaVeiculoController {

    private final MarcaVeiculoService service;

    public MarcaVeiculoController(MarcaVeiculoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar uma nova marca de veÃ­culo")
    public ResponseEntity<MarcaVeiculoResponse> criar(@Valid @RequestBody MarcaVeiculoRequest request) {
        MarcaVeiculo marcaVeiculo = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MarcaVeiculoMapper.toResponse(marcaVeiculo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar marca de veÃ­culo por ID")
    public MarcaVeiculoResponse buscarPorId(@PathVariable Long id) {
        MarcaVeiculo marcaVeiculo = service.buscarPorId(id);
        return MarcaVeiculoMapper.toResponse(marcaVeiculo);
    }

    @GetMapping
    @Operation(summary = "Listar marcas de veículos com paginação")
    public Page<MarcaVeiculoResponse> listar(
            @RequestParam(required = false) String nome,
            Pageable pageable) {
        Page<MarcaVeiculo> page = service.listar(nome, pageable);
        return page.map(MarcaVeiculoMapper::toResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma marca de veículo existente")
    public MarcaVeiculoResponse atualizar(@PathVariable Long id, @Valid @RequestBody MarcaVeiculoRequest request) {
        MarcaVeiculo marcaVeiculo = service.atualizar(id, request);
        return MarcaVeiculoMapper.toResponse(marcaVeiculo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover uma marca de veículo (deleta do banco)")
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
