package com.neritech.saas.common.tenancy;

import org.springframework.security.access.AccessDeniedException;

/**
 * Operações fail-closed para endpoints/serviços legados que ainda recebem
 * identificadores de empresa em path/body.
 *
 * <p>O tenant corrente é preenchido pela autenticação. Um identificador enviado
 * pelo cliente pode ser validado contra esse contexto, mas nunca substituí-lo.</p>
 */
public final class TenantAccess {

    private TenantAccess() {
    }

    public static Long requireCurrentTenant() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new AccessDeniedException("Contexto de empresa autenticado ausente");
        }
        return tenantId;
    }

    public static Long requireCurrentTenant(Long requestedTenantId) {
        Long tenantId = requireCurrentTenant();
        if (requestedTenantId == null || !tenantId.equals(requestedTenantId)) {
            throw new AccessDeniedException("Empresa não autorizada para a sessão atual");
        }
        return tenantId;
    }
}
