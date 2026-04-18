package com.neritech.saas.dashboard.controller;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neritech.saas.dashboard.dto.DashboardDTO;
import com.neritech.saas.dashboard.service.DashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/dashboard", "/dashboard"})
@Tag(name = "Dashboard", description = "Indicadores e KPIs")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obter dados sumarizados do dashboard")
    public ResponseEntity<DashboardDTO> getDashboard(@RequestParam(required = false) Long empresaId) {
        Long effectiveEmpresaId = empresaId;
        if (effectiveEmpresaId == null) {
            var currentUser = usuarioService.getCurrentUser();
            effectiveEmpresaId = currentUser != null ? currentUser.getEmpresaId() : null;
        }
        if (effectiveEmpresaId == null) {
            return ResponseEntity.badRequest().build();
        }
        TenantContext.setCurrentTenant(effectiveEmpresaId);
        return ResponseEntity.ok(dashboardService.getDashboardData(effectiveEmpresaId));
    }
}
