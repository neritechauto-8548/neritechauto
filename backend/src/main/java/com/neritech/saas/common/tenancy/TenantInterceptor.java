package com.neritech.saas.common.tenancy;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Aplica o isolamento de tenant usando exclusivamente o contexto autenticado.
 *
 * <p>O header X-Tenant-Id existe apenas como seletor/compatibilidade de contexto:
 * ele nunca concede acesso a um tenant. No modelo atual, em que o usuario possui
 * uma empresa ativa por sessao, um header diferente do tenant autenticado e
 * rejeitado.</p>
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TenantInterceptor.class);
    private static final String TENANT_HEADER = "X-Tenant-Id";

    private final EntityManager entityManager;

    public TenantInterceptor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isRequestWithoutTenantContext(request)) {
            return true;
        }

        Long authenticatedTenantId = TenantContext.getCurrentTenant();
        if (authenticatedTenantId == null) {
            log.warn("Requisicao autenticada sem TenantContext confiavel: {} {}",
                    request.getMethod(), request.getRequestURI());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Contexto de empresa autenticado ausente");
            return false;
        }

        String requestedTenant = request.getHeader(TENANT_HEADER);
        if (StringUtils.hasText(requestedTenant)) {
            Long requestedTenantId;
            try {
                requestedTenantId = Long.valueOf(requestedTenant.trim());
            } catch (NumberFormatException ex) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "Valor invalido para X-Tenant-Id");
                return false;
            }

            if (!authenticatedTenantId.equals(requestedTenantId)) {
                log.warn("Tentativa de troca de tenant negada. tenantAutenticado={}, tenantSolicitado={}, uri={}",
                        authenticatedTenantId, requestedTenantId, request.getRequestURI());
                response.sendError(HttpStatus.FORBIDDEN.value(), "Contexto de empresa nao autorizado");
                return false;
            }
        }

        // Query params tenantId/empresaId nunca sao usados como fonte de autoridade.
        // Eles podem existir como filtros de negocio em endpoints legados, mas o
        // isolamento continua preso ao tenant derivado do token/sessao validos.
        enableTenantFilter(authenticatedTenantId);
        return true;
    }

    private void enableTenantFilter(Long tenantId) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("tenantFilter");
        filter.setParameter("tenantId", tenantId);
    }

    private boolean isRequestWithoutTenantContext(HttpServletRequest request) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();
        return uri.contains("/auth/")
                || uri.contains("/public/")
                || uri.endsWith("/error")
                || uri.contains("/v3/api-docs")
                || uri.contains("/api-docs")
                || uri.contains("/swagger-ui")
                || uri.contains("/empresas/") && uri.endsWith("/logo")
                || uri.contains("/produtos/") && uri.endsWith("/foto");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
    }
}
