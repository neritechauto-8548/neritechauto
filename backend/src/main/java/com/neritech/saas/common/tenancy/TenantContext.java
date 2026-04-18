package com.neritech.saas.common.tenancy;

/**
 * Classe utilitÃ¡ria para armazenar e recuperar o ID do tenant (empresa) atual
 * usando ThreadLocal para garantir isolamento entre requisiÃ§Ãµes
 */
public class TenantContext {

    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
        // Construtor privado para evitar instanciaÃ§Ã£o
    }

    /**
     * Define o ID do tenant atual
     *
     * @param tenantId ID da empresa (tenant)
     */
    public static void setCurrentTenant(Long tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * ObtÃ©m o ID do tenant atual
     *
     * @return ID da empresa (tenant)
     */
    public static Long getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    /**
     * Limpa o ID do tenant atual
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
