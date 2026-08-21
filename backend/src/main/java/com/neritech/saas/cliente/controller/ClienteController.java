package com.neritech.saas.cliente.controller;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import com.neritech.saas.cliente.dto.ClienteDetailResponse;
import com.neritech.saas.cliente.dto.ClienteListResponse;
import com.neritech.saas.cliente.dto.ClienteRequest;
import com.neritech.saas.cliente.dto.ClienteResponse;
import com.neritech.saas.cliente.mapper.ClienteMapper;
import com.neritech.saas.cliente.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/clientes")
@Tag(name = "Clientes", description = "Gestão completa de clientes (Pessoas Físicas e Jurídicas)")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping("/{id}/resumo")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Resumo seguro do cliente", description = "Retorna identidade minimizada e PII mascarada para a visão 360°.")
    public ResponseEntity<ClienteDetailResponse> getSummary(@PathVariable Long id) {
        return ResponseEntity.ok(ClienteMapper.toDetailResponse(service.findById(id)));
    }

    @GetMapping("/{id}/edicao")
    @PreAuthorize("hasAuthority('CLIENTE_EDITAR')")
    @Operation(
            summary = "Carregar cliente para edição",
            description = "Retorna o contrato completo somente para o fluxo de edição autorizado. Novas superfícies de consulta devem usar /resumo.")
    public ResponseEntity<ClienteResponse> getForEdit(@PathVariable Long id) {
        return ResponseEntity.ok(ClienteMapper.toResponse(service.findById(id)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(
            summary = "Buscar cliente por ID (legado)",
            description = "Contrato completo mantido temporariamente para consumidores legados. Não deve ser usado por novas superfícies; migração para /resumo ou /edicao está em andamento.")
    public ResponseEntity<ClienteResponse> getById(
            @Parameter(description = "ID do cliente", required = true) @PathVariable Long id) {
        Cliente cliente = service.findById(id);
        return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Listar clientes", description = "Retorna clientes paginados com PII mascarada por padrão.")
    public Page<ClienteListResponse> search(
            @RequestParam(required = false) String nomeCompleto,
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) TipoCliente tipoCliente,
            @RequestParam(required = false) StatusCliente status,
            @org.springframework.data.web.PageableDefault(
                    size = 5,
                    sort = "nomeCompleto",
                    direction = org.springframework.data.domain.Sort.Direction.ASC) Pageable pageable) {
        return service.search(nomeCompleto, razaoSocial, cpf, cnpj, tipoCliente, status, pageable)
                .map(ClienteMapper::toListResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENTE_CRIAR')")
    @Operation(summary = "Criar cliente", description = "Cadastra um cliente ativo após validar identidade e regras de negócio.")
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteRequest request) {
        Cliente saved = service.create(ClienteMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTE_EDITAR')")
    @Operation(summary = "Atualizar cliente", description = "Atualiza dados cadastrais sem alterar o lifecycle/status do cliente.")
    public ResponseEntity<ClienteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        Cliente saved = service.update(id, request);
        return ResponseEntity.ok(ClienteMapper.toResponse(saved));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasAuthority('CLIENTE_EXCLUIR')")
    @Operation(summary = "Inativar cliente", description = "Inativa logicamente o cliente e preserva seus vínculos e histórico.")
    public ResponseEntity<ClienteResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(ClienteMapper.toResponse(service.deactivate(id)));
    }

    @PatchMapping("/{id}/reativar")
    @PreAuthorize("hasAuthority('CLIENTE_EDITAR')")
    @Operation(summary = "Reativar cliente", description = "Reativa um cliente previamente inativado dentro da empresa autenticada.")
    public ResponseEntity<ClienteResponse> reactivate(@PathVariable Long id) {
        return ResponseEntity.ok(ClienteMapper.toResponse(service.reactivate(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTE_EXCLUIR')")
    @Operation(summary = "Inativar cliente (compatibilidade)", description = "Endpoint legado mantido para compatibilidade. A operação é lógica e não remove histórico.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
