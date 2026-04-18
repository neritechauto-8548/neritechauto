package com.neritech.saas.estoque.controller;

import com.neritech.saas.estoque.domain.enums.MotivoDevolucao;
import com.neritech.saas.estoque.dto.DevolucaoProdutoRequest;
import com.neritech.saas.estoque.dto.DevolucaoProdutoResponse;
import com.neritech.saas.estoque.service.DevolucaoProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/devolucoes-produtos")
@Tag(name = "DevoluÃ§Ãµes de Produtos", description = "Gerenciamento de devoluÃ§Ãµes de produtos")
public class DevolucaoProdutoController {

    private final DevolucaoProdutoService service;

    public DevolucaoProdutoController(DevolucaoProdutoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova devoluÃ§Ã£o")
    public ResponseEntity<DevolucaoProdutoResponse> create(@Valid @RequestBody DevolucaoProdutoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar devoluÃ§Ã£o por ID")
    public ResponseEntity<DevolucaoProdutoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/produto/{produtoId}")
    @Operation(summary = "Listar devoluÃ§Ãµes por produto")
    public ResponseEntity<Page<DevolucaoProdutoResponse>> findByProduto(
            @PathVariable Long produtoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByProduto(produtoId, pageable));
    }

    @GetMapping("/motivo/{motivo}")
    @Operation(summary = "Listar devoluÃ§Ãµes por motivo")
    public ResponseEntity<Page<DevolucaoProdutoResponse>> findByMotivo(
            @PathVariable MotivoDevolucao motivo,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByMotivo(motivo, pageable));
    }

    @GetMapping("/ordem-servico/{ordemServicoId}")
    @Operation(summary = "Listar devoluÃ§Ãµes por ordem de serviÃ§o")
    public ResponseEntity<Page<DevolucaoProdutoResponse>> findByOrdemServico(
            @PathVariable Long ordemServicoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findByOrdemServico(ordemServicoId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar devoluÃ§Ã£o")
    public ResponseEntity<DevolucaoProdutoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DevolucaoProdutoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir devoluÃ§Ã£o")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
