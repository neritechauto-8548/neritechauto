package com.neritech.saas.dashboard.controller;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

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
    public ResponseEntity<DashboardDTO> getDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        var currentUser = usuarioService.getCurrentUser();
        Long effectiveEmpresaId = currentUser != null ? currentUser.getEmpresaId() : null;
        if (effectiveEmpresaId == null) {
            return ResponseEntity.badRequest().build();
        }
        if (inicio == null && fim == null) {
            YearMonth currentMonth = YearMonth.now();
            inicio = currentMonth.atDay(1);
            fim = currentMonth.atEndOfMonth();
        } else if (inicio == null || fim == null || inicio.isAfter(fim)
                || fim.isAfter(LocalDate.now()) || ChronoUnit.DAYS.between(inicio, fim) > 366) {
            return ResponseEntity.badRequest().build();
        }
        TenantContext.setCurrentTenant(effectiveEmpresaId);
        return ResponseEntity.ok(dashboardService.getDashboardData(effectiveEmpresaId, inicio, fim));
    }
}
