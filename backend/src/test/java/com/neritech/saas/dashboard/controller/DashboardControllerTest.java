package com.neritech.saas.dashboard.controller;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.dashboard.dto.DashboardDTO;
import com.neritech.saas.dashboard.service.DashboardService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController controller;

    @AfterEach
    void clearTenant() {
        TenantContext.clear();
    }

    @Test
    void shouldUseOnlyTenantResolvedByAuthenticatedContext() {
        TenantContext.setCurrentTenant(42L);
        DashboardDTO dashboard = mock(DashboardDTO.class);
        when(dashboardService.getDashboardData(42L)).thenReturn(dashboard);

        var response = controller.getDashboard();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(dashboard, response.getBody());
        verify(dashboardService).getDashboardData(42L);
    }

    @Test
    void shouldFailClosedWhenTrustedTenantIsMissing() {
        var response = controller.getDashboard();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verifyNoInteractions(dashboardService);
    }
}
