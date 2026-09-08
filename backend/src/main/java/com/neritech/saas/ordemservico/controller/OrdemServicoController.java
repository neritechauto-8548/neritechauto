package com.neritech.saas.ordemservico.controller;

import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import com.neritech.saas.ordemservico.dto.OrdemServicoCockpitResponse;
import com.neritech.saas.ordemservico.dto.OrdemServicoRequest;
import com.neritech.saas.ordemservico.dto.OrdemServicoResponse;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import com.neritech.saas.ordemservico.service.OrdemServicoCockpitService;
import com.neritech.saas.ordemservico.service.OrdemServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/v1/ordens-servico", "/api/v1/ordens-servico"})
@Tag(name = "Ordens de Serviço", description = "Gerenciamento de ordens de serviço")
public class OrdemServicoController {

    private final OrdemServicoService service;
    private final OrdemServicoCockpitService cockpitService;
    private final OrdemServicoRepository repository;
    private final ClienteRepository clienteRepository;

    public OrdemServicoController(
            OrdemServicoService service,
            OrdemServicoCockpitService cockpitService,
            OrdemServicoRepository repository,
            ClienteRepository clienteRepository) {
        this.service = service;
        this.cockpitService = cockpitService;
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_INCLUIR')")
    @Operation(summary = "Criar nova ordem de serviço")
    public ResponseEntity<OrdemServicoResponse> create(@Valid @RequestBody OrdemServicoRequest request) {
        TenantAccess.requireCurrentTenant(request.empresaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PostMapping("/venda-balcao")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PDV_REALIZAR_VENDAS')")
    @Operation(summary = "Criar nova Venda Balcão (PDV)")
    public ResponseEntity<OrdemServicoResponse> criarVendaBalcao(
            @Valid @RequestBody com.neritech.saas.ordemservico.dto.VendaBalcaoRequest request) {
        TenantAccess.requireCurrentTenant(request.ordemServico().empresaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarVendaBalcao(request));
    }

    @GetMapping("/{id}/cockpit")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Buscar read model composto do cockpit da Ordem de Serviço")
    public ResponseEntity<OrdemServicoCockpitResponse> findCockpitById(@PathVariable Long id) {
        return ResponseEntity.ok(cockpitService.findById(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('GERAL_USUARIO','PDV_LISTAR_VENDAS')")
    @Operation(summary = "Buscar ordem de serviço por ID")
    public ResponseEntity<OrdemServicoResponse> findById(@PathVariable Long id) {
        OrdemServico owned = requireOwnedOrder(id);
        requirePdvReadIfSale(owned);
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/empresa/{empresaId}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('GERAL_USUARIO','PDV_LISTAR_VENDAS')")
    @Operation(summary = "Listar ordens de serviço da empresa autenticada")
    public ResponseEntity<Page<OrdemServicoResponse>> findByEmpresaId(
            @PathVariable Long empresaId,
            @RequestParam(required = false, defaultValue = "SERVICO") String tipo,
            @RequestParam(required = false) String search,
            Pageable pageable) {
        Long tenantId = TenantAccess.requireCurrentTenant(empresaId);
        if ("VENDA_PRODUTO".equalsIgnoreCase(tipo)) {
            requireAuthority("PDV_LISTAR_VENDAS");
        } else {
            requireAuthority("GERAL_USUARIO");
        }
        return ResponseEntity.ok(service.findByEmpresaId(tenantId, tipo, search, pageable));
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Listar ordens de serviço por cliente")
    public ResponseEntity<Page<OrdemServicoResponse>> findByClienteId(
            @PathVariable Long clienteId,
            Pageable pageable) {
        TenantAccess.requireCurrentTenant();
        if (!clienteRepository.existsByIdScoped(clienteId)) {
            throw new EntityNotFoundException("Cliente não encontrado para a empresa autenticada");
        }
        return ResponseEntity.ok(service.findByClienteId(clienteId, pageable));
    }

    @GetMapping("/empresa/{empresaId}/status/{statusId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Listar ordens de serviço por empresa e status")
    public ResponseEntity<Page<OrdemServicoResponse>> findByEmpresaIdAndStatusId(
            @PathVariable Long empresaId,
            @PathVariable Long statusId,
            Pageable pageable) {
        Long tenantId = TenantAccess.requireCurrentTenant(empresaId);
        return ResponseEntity.ok(service.findByEmpresaIdAndStatusId(tenantId, statusId, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR')")
    @Operation(summary = "Atualizar ordem de serviço")
    public ResponseEntity<OrdemServicoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody OrdemServicoRequest request) {
        TenantAccess.requireCurrentTenant(request.empresaId());
        requireOwnedOrder(id);
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EXCLUIR')")
    @Operation(summary = "Deletar ordem de serviço")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        requireOwnedOrder(id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/enviar-email")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('OS_EDITAR') or hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Enviar orçamento/OS por e-mail")
    public ResponseEntity<Void> enviarEmail(
            @PathVariable Long id,
            @RequestParam(required = false) String emailDestino) {
        requireOwnedOrder(id);
        service.enviarOrcamentoEmail(id, emailDestino);
        return ResponseEntity.ok().build();
    }

    private OrdemServico requireOwnedOrder(Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        return repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ordem de serviço não encontrada para a empresa autenticada"));
    }

    private void requirePdvReadIfSale(OrdemServico ordemServico) {
        if (ordemServico.getTipoOS() == TipoOS.VENDA_PRODUTO) {
            requireAuthority("PDV_LISTAR_VENDAS");
        } else {
            requireAuthority("GERAL_USUARIO");
        }
    }

    private void requireAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authorized = authentication != null
                && authentication.getAuthorities().stream().anyMatch(granted ->
                        authority.equals(granted.getAuthority()) || "ROLE_ADMIN".equals(granted.getAuthority()));
        if (!authorized) {
            throw new AccessDeniedException("Permissão necessária: " + authority);
        }
    }
}
