package com.neritech.saas.common.tenancy;

import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Boundary for operations that receive an empresaId through a request.
 * The authenticated tenant (JWT validated by the security filter) is authoritative.
 */
public final class TenantAccess {
    private TenantAccess() {}

    public static Long requireCurrent() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null || tenantId <= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Contexto de empresa autenticada ausente");
        }
        return tenantId;
    }

    public static Long require(Long requestedEmpresaId) {
        Long current = requireCurrent();
        if (!Objects.equals(current, requestedEmpresaId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Empresa fora do escopo autenticado");
        }
        return current;
    }
}
