package com.neritech.saas.common.tenancy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TenantContextTest {

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("Deve definir e recuperar tenantId")
    void deveDefinirERecuperarTenantId() {
        // Arrange
        Long tenantId = 123L;

        // Act
        TenantContext.setCurrentTenant(tenantId);

        // Assert
        assertThat(TenantContext.getCurrentTenant()).isEqualTo(tenantId);
    }

    @Test
    @DisplayName("Deve limpar tenantId")
    void deveLimparTenantId() {
        // Arrange
        TenantContext.setCurrentTenant(1L);

        // Act
        TenantContext.clear();

        // Assert
        assertThat(TenantContext.getCurrentTenant()).isNull();
    }
}
