package com.neritech.saas.empresa.controller;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.dto.EmpresaRequest;
import com.neritech.saas.empresa.dto.EmpresaResponse;
import com.neritech.saas.empresa.mapper.EmpresaMapper;
import com.neritech.saas.empresa.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.neritech.saas.empresa.service.EmpresaLogoStorageService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Buscar empresa", description = "Busca uma empresa pelo ID")
    public ResponseEntity<EmpresaResponse> getById(@PathVariable Long id) {
        Empresa e = service.findById(id);
        return ResponseEntity.ok(EmpresaMapper.toResponse(e));
    }

    @GetMapping
    @Operation(summary = "Listar empresa", description = "Lista empresas com filtros opcionais de CNPJ e razÃ£o social")
    public ResponseEntity<Page<EmpresaResponse>> list(
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) String razaoSocial,
            Pageable pageable) {
        Page<Empresa> page = service.search(cnpj, razaoSocial, pageable);
        Page<EmpresaResponse> mapped = page.map(EmpresaMapper::toResponse);
        return ResponseEntity.ok(mapped);
    }

    @PostMapping
    @Operation(summary = "Criar empresa", description = "Cria uma nova empresa")
    public ResponseEntity<EmpresaResponse> create(@Valid @RequestBody EmpresaRequest request) {
        Empresa toCreate = EmpresaMapper.toEntity(request);
        Empresa created = service.create(toCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmpresaMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa existente")
    public ResponseEntity<EmpresaResponse> update(@PathVariable Long id, @Valid @RequestBody EmpresaRequest request) {
        Empresa current = service.findById(id);
        EmpresaMapper.updateEntity(current, request);
        Empresa saved = service.update(id, current);
        return ResponseEntity.ok(EmpresaMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar empresa", description = "Remove uma empresa pelo ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/logo")
    @Operation(summary = "Upload da logomarca", description = "Realiza o upload da imagem da logomarca da empresa")
    public ResponseEntity<EmpresaResponse> uploadLogo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String path = logoStorageService.store(id, file);
        Empresa saved = service.updateLogoPath(id, path);
        return ResponseEntity.ok(EmpresaMapper.toResponse(saved));
    }

    @GetMapping("/{id}/logo")
    @Operation(summary = "Obter logomarca da empresa", description = "Retorna a imagem da logomarca da empresa")
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
