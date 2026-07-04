package com.neritech.saas.dashboard.controller;

import com.neritech.saas.dashboard.dto.AdminDashboardMetricsResponse;
import com.neritech.saas.dashboard.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<AdminDashboardMetricsResponse> getMetrics() {
        return ResponseEntity.ok(adminDashboardService.getMetrics());
    }
}
