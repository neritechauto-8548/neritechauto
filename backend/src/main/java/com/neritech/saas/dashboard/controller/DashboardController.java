package com.neritech.saas.dashboard.controller;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.dashboard.dto.DashboardDTO;
import com.neritech.saas.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping({"/api/v1/dashboards/home", "/api/dashboard", "/dashboard"})
@Tag(name = "Dashboard", description = "Indicadores e KPIs do Início")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Obter os dados consolidados do Início")
    public ResponseEntity<DashboardDTO> getDashboard(
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(defaultValue = "previous") String comparison,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        Long empresaId = TenantContext.getCurrentTenant();
        if (empresaId == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        try {
            return ResponseEntity.ok(dashboardService.getDashboardData(empresaId, period, comparison, startDate, endDate));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
