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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/dashboard", "/dashboard"})
@Tag(name = "Dashboard", description = "Indicadores e KPIs")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Obter dados sumarizados do dashboard")
    public ResponseEntity<DashboardDTO> getDashboard() {
        Long empresaId = TenantContext.getCurrentTenant();
        if (empresaId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(dashboardService.getDashboardData(empresaId));
    }
}
