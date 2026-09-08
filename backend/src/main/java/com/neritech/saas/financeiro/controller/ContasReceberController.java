package com.neritech.saas.financeiro.controller;

import com.neritech.saas.financeiro.dto.AnexoTituloDTO;
import com.neritech.saas.financeiro.dto.ContasReceberRequest;
import com.neritech.saas.financeiro.dto.ContasReceberResponse;
import com.neritech.saas.financeiro.dto.DashboardFinanceiroDTO;
import com.neritech.saas.financeiro.service.ContasReceberService;
import com.neritech.saas.gestaoUsuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/v1/financeiro/contas-receber", "/api/v1/financeiro/contas-receber"})
@RequiredArgsConstructor
@Tag(name = "Contas a Receber", description = "Gestão de contas a receber")
public class ContasReceberController {

    private final ContasReceberService service;
    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('FIN_LISTAR_CONTAS')")
    @Operation(summary = "Listar contas a receber")
    public ResponseEntity<Page<ContasReceberResponse>> findAll(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false)
            @org.springframework.format.annotation.DateTimeFormat(
                    iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
            java.time.LocalDate dataInicio,
            @RequestParam(required = false)
            @org.springframework.format.annotation.DateTimeFormat(
                    iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
            java.time.LocalDate dataFim,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long contaBancariaId,
            @RequestParam(required = false) Long centroCustoId,
            @RequestParam(required = false) Long planoContasId,
            @RequestParam(required = false) Long formaPagamentoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(
                currentEmpresaId(),
                termo,
                dataInicio,
                dataFim,
                status,
                contaBancariaId,
                centroCustoId,
                planoContasId,
                formaPagamentoId,
                pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_LISTAR_CONTAS')")
    @Operation(summary = "Buscar conta a receber por ID")
    public ResponseEntity<ContasReceberResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id, currentEmpresaId()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('FIN_INC_CONTAS')")
    @Operation(summary = "Criar conta a receber")
    public ResponseEntity<ContasReceberResponse> create(
            @Valid @RequestBody ContasReceberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(currentEmpresaId(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_EDIT_CONTA')")
    @Operation(summary = "Atualizar conta a receber")
    public ResponseEntity<ContasReceberResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ContasReceberRequest request) {
        return ResponseEntity.ok(service.update(id, currentEmpresaId(), request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FIN_EXC_CONTAS')")
    @Operation(summary = "Excluir conta a receber")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id, currentEmpresaId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('FIN_LISTAR_CONTAS')")
    @Operation(summary = "Métricas do Dashboard Financeiro")
    public ResponseEntity<DashboardFinanceiroDTO> getDashboard() {
        return ResponseEntity.ok(service.getDashboard(currentEmpresaId()));
    }

    @PostMapping("/{id}/recebimentos")
    @PreAuthorize("hasAuthority('FIN_EDIT_CONTA')")
    @Operation(summary = "Registrar recebimento parcial/total")
    public ResponseEntity<ContasReceberResponse> receberTitulo(
            @PathVariable Long id,
            @RequestBody ContasReceberRequest request) {
        // MVP: mantém o fluxo existente de atualização, agora sempre preso ao tenant autenticado.
        return ResponseEntity.ok(service.update(id, currentEmpresaId(), request));
    }

    @PostMapping("/{id}/desfazer-quitacao")
    @PreAuthorize("hasAuthority('FIN_DESF_PAGAMENTO')")
    @Operation(summary = "Desfazer quitação (recebimento)")
    public ResponseEntity<ContasReceberResponse> desfazerQuitacao(@PathVariable Long id) {
        return ResponseEntity.ok(service.desfazerQuitacao(id, currentEmpresaId()));
    }

    @PostMapping("/{id}/renegociar")
    @PreAuthorize("hasAuthority('FIN_EDIT_CONTA')")
    @Operation(summary = "Renegociar um título")
    public ResponseEntity<Void> renegociarTitulo(
            @PathVariable Long id,
            @RequestBody Object request) {
        // Endpoint ainda é placeholder, mas valida a pertença do título ao tenant antes de responder.
        service.findById(id, currentEmpresaId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/anexos")
    @PreAuthorize("hasAuthority('FIN_EDIT_CONTA')")
    @Operation(summary = "Upload de anexo")
    public ResponseEntity<AnexoTituloDTO> uploadAnexo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        // Enquanto o armazenamento definitivo não estiver implementado, ao menos validamos
        // o título no tenant atual para impedir uso do endpoint com IDs de outra empresa.
        service.findById(id, currentEmpresaId());

        AnexoTituloDTO dto = new AnexoTituloDTO();
        dto.setId(1L);
        dto.setNomeArquivo(file.getOriginalFilename());
        dto.setTipoArquivo(file.getContentType());
        dto.setTamanhoBytes(file.getSize());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/anexos/{anexoId}/download")
    @PreAuthorize("hasAuthority('FIN_LISTAR_CONTAS')")
    @Operation(summary = "Download de anexo")
    public ResponseEntity<byte[]> downloadAnexo(
            @PathVariable Long id,
            @PathVariable Long anexoId) {
        service.findById(id, currentEmpresaId());
        return ResponseEntity.ok(new byte[0]);
    }

    private Long currentEmpresaId() {
        return usuarioService.getCurrentUser().getEmpresaId();
    }
}
