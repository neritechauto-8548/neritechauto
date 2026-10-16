package com.neritech.saas.empresa.controller;

import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.dto.EmpresaRequest;
import com.neritech.saas.empresa.dto.EmpresaResponse;
import com.neritech.saas.empresa.mapper.EmpresaMapper;
import com.neritech.saas.empresa.service.EmpresaService;
import com.neritech.saas.empresa.service.EmpresaAlteracaoService;
import com.neritech.saas.empresa.dto.EmpresaAlteracaoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import com.neritech.saas.empresa.service.EmpresaLogoStorageService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/v1/empresas")
@PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('ADM_EMPRESA')")
@Tag(name = "Empresas", description = "Endpoints para gestão de empresas")
public class EmpresaController {

    private final EmpresaService service;
    private final EmpresaLogoStorageService logoStorageService;
    private final EmpresaAlteracaoService auditoria;

    public EmpresaController(EmpresaService service, EmpresaLogoStorageService logoStorageService, EmpresaAlteracaoService auditoria) {
        this.service = service;
        this.logoStorageService = logoStorageService;
        this.auditoria = auditoria;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empresa", description = "Busca uma empresa pelo ID")
    public ResponseEntity<EmpresaResponse> getById(@PathVariable Long id) {
        Empresa e = service.findById(TenantAccess.require(id));
        return ResponseEntity.ok(EmpresaMapper.toResponse(e));
    }

    @GetMapping
    @Operation(summary = "Listar empresa", description = "Lista empresas com filtros opcionais de CNPJ e razÃ£o social")
    public ResponseEntity<Page<EmpresaResponse>> list(
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) String razaoSocial,
            Pageable pageable) {
        // A empresa da sessão é a única empresa acessível nesta API.
        // Não paginar uma consulta global para depois filtrá-la: isso expõe
        // os metadados (totalElements) de outras empresas.
        Empresa own = service.findById(TenantAccess.requireCurrent());
        boolean matchesCnpj = cnpj == null || cnpj.isBlank() || cnpj.equals(own.getCnpj());
        boolean matchesName = razaoSocial == null || razaoSocial.isBlank()
                || (own.getRazaoSocial() != null
                    && own.getRazaoSocial().toLowerCase(Locale.ROOT)
                        .contains(razaoSocial.toLowerCase(Locale.ROOT)));
        boolean matches = matchesCnpj && matchesName;
        List<EmpresaResponse> content = matches && pageable.getOffset() == 0
                ? List.of(EmpresaMapper.toResponse(own)) : List.of();
        return ResponseEntity.ok(new PageImpl<>(content, pageable, matches ? 1L : 0L));
    }

    @GetMapping("/{id}/historico")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('ADM_EMPRESA')")
    @Operation(summary = "Histórico versionado das alterações da empresa")
    public ResponseEntity<Page<EmpresaAlteracaoResponse>> historico(
            @PathVariable Long id, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(auditoria.historico(TenantAccess.require(id), page, size));
    }

    @PostMapping
    // Provisionamento de tenants exige um fluxo de cadastro separado e confiável.
    // Permissões atribuídas dentro de um tenant não podem criar outros tenants.
    @PreAuthorize("denyAll()")
    @Operation(summary = "Criar empresa", description = "Cria uma nova empresa")
    public ResponseEntity<EmpresaResponse> create(@Valid @RequestBody EmpresaRequest request) {
        Empresa toCreate = EmpresaMapper.toEntity(request);
        Empresa created = service.create(toCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmpresaMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('ADM_EMPRESA')")
    @Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa existente")
    public ResponseEntity<EmpresaResponse> update(@PathVariable Long id, @Valid @RequestBody EmpresaRequest request) {
        Empresa saved = service.updateCadastro(TenantAccess.require(id), request);
        return ResponseEntity.ok(EmpresaMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("denyAll()")
    @Operation(summary = "Deletar empresa", description = "Remove uma empresa pelo ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        TenantAccess.require(id);
        service.delete(id);
    }

    @PostMapping("/{id}/logo")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('ADM_EMPRESA')")
    @Operation(summary = "Upload da logomarca", description = "Realiza o upload da imagem da logomarca da empresa")
    public ResponseEntity<EmpresaResponse> uploadLogo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        TenantAccess.require(id); // Validar ANTES de persistir o arquivo.
        String path = logoStorageService.store(id, file);
        Empresa saved = service.updateLogoPath(id, path);
        return ResponseEntity.ok(EmpresaMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}/logo")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GERAL_CONFIG_SISTEMA') or hasAuthority('ADM_EMPRESA')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLogo(@PathVariable Long id) {
        TenantAccess.require(id);
        service.updateLogoPath(id, null);
    }

    @GetMapping("/{id}/logo")
    @Operation(summary = "Obter logomarca da empresa", description = "Retorna a imagem da logomarca da empresa")
    public ResponseEntity<org.springframework.core.io.Resource> getLogo(@PathVariable Long id) {
        Empresa e = service.findById(TenantAccess.require(id));
        if (e.getLogoPath() == null || e.getLogoPath().isBlank()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = logoStorageService.mediaType(e.getLogoPath());
        org.springframework.core.io.Resource r = logoStorageService.load(e.getLogoPath());
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .body(r);
    }
}
