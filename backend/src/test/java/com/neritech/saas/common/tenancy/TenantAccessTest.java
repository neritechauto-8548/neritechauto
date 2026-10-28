package com.neritech.saas.common.tenancy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import static org.assertj.core.api.Assertions.*;

class TenantAccessTest {
    @AfterEach void clearContext() { TenantContext.clear(); }

    @Test void rejectsMissingTenant() {
        assertThatThrownBy(TenantAccess::requireCurrent)
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN));
    }

    @Test void rejectsWrongEmpresaId() {
        TenantContext.setCurrentTenant(8L);
        assertThatThrownBy(() -> TenantAccess.require(9L))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test void rejectsInvalidOrMissingEmpresaId() {
        TenantContext.setCurrentTenant(8L);
        assertThatThrownBy(() -> TenantAccess.require(null)).isInstanceOf(ResponseStatusException.class);
        assertThatThrownBy(() -> TenantAccess.require(0L)).isInstanceOf(ResponseStatusException.class);
    }

    @Test void allowsAuthenticatedCompanyOnly() {
        TenantContext.setCurrentTenant(8L);
        assertThat(TenantAccess.require(8L)).isEqualTo(8L);
        assertThat(TenantAccess.requireCurrent()).isEqualTo(8L);
    }
}
