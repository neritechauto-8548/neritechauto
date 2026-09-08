package com.neritech.saas.empresa.controller;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.dto.EmpresaRequest;
import com.neritech.saas.empresa.dto.EmpresaResponse;
import com.neritech.saas.empresa.mapper.EmpresaMapper;
import com.neritech.saas.empresa.service.EmpresaLogoStorageService;
import com.neritech.saas.empresa.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/empresas")
@Tag(name = "Empresas", description = "Endpoints para gestão de empresas")
public class EmpresaController {

    private final EmpresaService service;
    private final EmpresaLogoStorageService logoStorageService;

    public EmpresaController(EmpresaService service, EmpresaLogoStorageService logoStorageService) {
        this.service = service;
        this.logoStorageService = logoStorageService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_USUARIO')")
    @Operation(summary = "Buscar empresa", description = "Busca somente a empresa da sessão autenticada")
    public ResponseEntity<EmpresaResponse> getById(@PathVariable Long id) {
        Long tenantId = TenantAccess.requireCurrentTenant(id);
        return ResponseEntity.ok(EmpresaMapper.toResponse(service.findById(tenantId)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GERAL_CONFIG_SISTEMA')")
    @Operation(summary = "Listar empresa", description = "Retorna apenas a empresa da sessão autenticada")
    public ResponseEntity<Page<EmpresaResponse>> list(
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) String razaoSocial,
            Pageable pageable) {
        Long tenantId = TenantAccess.requireCurrentTenant();
        EmpresaResponse current = EmpresaMapper.toResponse(service.findById(tenantId));
        return ResponseEntity.ok(new PageImpl<>(List.of(current), pageable, 1));
    }

    @PostMapping
    @PreAuthorize("denyAll()")
    @Operation(
            summary = "Criar empresa",
            description = "Bloqueado para sessões tenant até existir autoridade explícita de backoffice da plataforma")
    public ResponseEntity<EmpresaResponse> create(@Valid @RequestBody EmpresaRequest request) {
        Empresa toCreate = EmpresaMapper.toEntity(request);
        Empresa created = service.create(toCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmpresaMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('GERAL_CONFIG_SISTEMA')")
    @Operation(summary = "Atualizar empresa", description = "Atualiza somente a empresa da sessão autenticada")
    public ResponseEntity<EmpresaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaRequest request) {
        Long tenantId = TenantAccess.requireCurrentTenant(id);
        Empresa current = service.findById(tenantId);
        EmpresaMapper.updateEntity(current, request);
        Empresa saved = service.update(tenantId, current);
        return ResponseEntity.ok(EmpresaMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("denyAll()")
    @Operation(
            summary = "Deletar empresa",
            description = "Bloqueado para sessões tenant; exclusão exige fluxo administrativo próprio da plataforma")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/logo")
    @PreAuthorize("hasAuthority('GERAL_CONFIG_SISTEMA')")
    @Operation(summary = "Upload da logomarca", description = "Atualiza a logomarca somente da empresa autenticada")
    public ResponseEntity<EmpresaResponse> uploadLogo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        Long tenantId = TenantAccess.requireCurrentTenant(id);
        String path = logoStorageService.store(tenantId, file);
        Empresa saved = service.updateLogoPath(tenantId, path);
        return ResponseEntity.ok(EmpresaMapper.toResponse(saved));
    }

    // A leitura da logomarca permanece pública porque é usada em superfícies externas.
    // Este endpoint não expõe dados cadastrais e já é explicitamente liberado no SecurityConfig.
    @GetMapping("/{id}/logo")
    @Operation(summary = "Obter logomarca da empresa", description = "Retorna a imagem pública da logomarca")
    public ResponseEntity<org.springframework.core.io.Resource> getLogo(@PathVariable Long id) {
        Empresa e = service.findById(id);
        if (e.getLogoPath() == null || e.getLogoPath().isBlank()) {
            return ResponseEntity.notFound().build();
        }
        org.springframework.core.io.Resource r = logoStorageService.load(e.getLogoPath());
        String contentType = "image/png";
        if (e.getLogoPath().toLowerCase().endsWith(".jpg") || e.getLogoPath().toLowerCase().endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (e.getLogoPath().toLowerCase().endsWith(".gif")) {
            contentType = "image/gif";
        } else if (e.getLogoPath().toLowerCase().endsWith(".svg")) {
            contentType = "image/svg+xml";
        }
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .body(r);
    }
}
