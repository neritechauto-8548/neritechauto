package com.neritech.saas.orcamento.controller;

import com.neritech.saas.orcamento.dto.OrcamentoDraftRequest;
import com.neritech.saas.orcamento.dto.OrcamentoDraftResponse;
import com.neritech.saas.orcamento.dto.OrcamentoListResponse;
import com.neritech.saas.orcamento.dto.OrcamentoListItemResponse;
import com.neritech.saas.orcamento.dto.OrcamentoCompositionResponse;
import com.neritech.saas.orcamento.dto.OrcamentoCreateGroupRequest;
import com.neritech.saas.orcamento.dto.OrcamentoAddCatalogItemRequest;
import com.neritech.saas.orcamento.dto.OrcamentoCatalogSearchResponse;
import com.neritech.saas.orcamento.service.OrcamentoCompositionService;
import com.neritech.saas.orcamento.service.OrcamentoDraftService;
import com.neritech.saas.orcamento.service.OrcamentoQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orcamentos")
@Tag(name = "Orçamentos", description = "Contratos canônicos do módulo de Orçamentos")
public class OrcamentoController {

    private final OrcamentoDraftService service;
    private final OrcamentoQueryService queryService;
    private final OrcamentoCompositionService compositionService;

    public OrcamentoController(
            OrcamentoDraftService service,
            OrcamentoQueryService queryService,
            OrcamentoCompositionService compositionService) {
        this.service = service;
        this.queryService = queryService;
        this.compositionService = compositionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(
            summary = "Listar orçamentos",
            description = "Lista somente orçamentos do tenant autenticado em DTO minimizado, com filtros e paginação server-side.")
    public ResponseEntity<OrcamentoListResponse> list(
            @RequestParam(required = false, name = "q") String query,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "updatedAt,desc") String sort) {
        return ResponseEntity.ok(queryService.list(query, status, page, size, sort));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(
            summary = "Consultar resumo seguro do orçamento",
            description = "Retorna o mesmo read model minimizado da lista e nega registros de outro tenant ou de outro tipo.")
    public ResponseEntity<OrcamentoListItemResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.findById(id));
    }

    @GetMapping("/catalog")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Buscar pecas e servicos ativos sem expor custo")
    public ResponseEntity<OrcamentoCatalogSearchResponse> searchCatalog(@RequestParam(name = "q") String query) {
        return ResponseEntity.ok(compositionService.searchCatalog(query));
    }

    @GetMapping("/{id}/composition")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Consultar composicao canonica do orcamento")
    public ResponseEntity<OrcamentoCompositionResponse> getComposition(@PathVariable Long id) {
        return ResponseEntity.ok(compositionService.get(id));
    }

    @PostMapping("/{id}/composition/groups")
    @PreAuthorize("hasAuthority('OS_INCLUIR')")
    @Operation(summary = "Adicionar grupo ao draft do orcamento")
    public ResponseEntity<OrcamentoCompositionResponse> createGroup(
            @PathVariable Long id,
            @Valid @RequestBody OrcamentoCreateGroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compositionService.createGroup(id, request));
    }

    @PostMapping("/{id}/composition/groups/{groupId}/items")
    @PreAuthorize("hasAuthority('OS_INCLUIR')")
    @Operation(summary = "Adicionar snapshot de catalogo ao grupo")
    public ResponseEntity<OrcamentoCompositionResponse> addCatalogItem(
            @PathVariable Long id,
            @PathVariable Long groupId,
            @Valid @RequestBody OrcamentoAddCatalogItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(compositionService.addCatalogItem(id, groupId, request));
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
