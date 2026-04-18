package com.neritech.saas.relatorios.controller;

import com.neritech.saas.relatorios.domain.enums.StatusBackup;
import com.neritech.saas.relatorios.dto.BackupRequest;
import com.neritech.saas.relatorios.dto.BackupResponse;
import com.neritech.saas.relatorios.service.BackupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/backups")
@RequiredArgsConstructor
@Tag(name = "Backups", description = "Gerenciamento de backups")
public class BackupController {

    private final BackupService backupService;

    @PostMapping
    @Operation(summary = "Iniciar/Registrar novo backup")
    public ResponseEntity<BackupResponse> create(@RequestBody BackupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(backupService.create(request));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Listar backups por empresa")
    public ResponseEntity<List<BackupResponse>> findByEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(backupService.findByEmpresa(empresaId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do backup")
    public ResponseEntity<BackupResponse> updateStatus(@PathVariable Long id, @RequestParam StatusBackup status,
            @RequestParam(required = false) String erro) {
        return ResponseEntity.ok(backupService.updateStatus(id, status, erro));
    }
}
