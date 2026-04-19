package com.neritech.saas.common.tenancy;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;

/**
 * Interceptor para extrair o ID do tenant (empresa) do cabeÃ§alho da requisiÃ§Ã£o
 * e configurÃ¡-lo no TenantContext
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TenantInterceptor.class);
    private static final String TENANT_HEADER = "X-Tenant-Id";
    private final EntityManager entityManager;
    private final UsuarioRepository usuarioRepository;

    public TenantInterceptor(EntityManager entityManager, UsuarioRepository usuarioRepository) {
        this.entityManager = entityManager;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("TenantInterceptor processing request: {}", requestURI);
        log.debug("TenantInterceptor processing request: {}", requestURI);
        
        String tenantHeader = request.getHeader(TENANT_HEADER);
        String tenantParam = request.getParameter("tenantId");
        String empresaParam = request.getParameter("empresaId");
        String tenantValue = null;
        String tenantSource = null;

        if (tenantHeader != null && !tenantHeader.isEmpty()) {
            tenantValue = tenantHeader;
            tenantSource = "header";
        } else if ((tenantParam != null && !tenantParam.isEmpty()) || (empresaParam != null && !empresaParam.isEmpty())) {
            tenantValue = (tenantParam != null && !tenantParam.isEmpty()) ? tenantParam : empresaParam;
            tenantSource = (tenantParam != null && !tenantParam.isEmpty()) ? "param" : "empresaParam";
            if ("param".equals(tenantSource)) {
                log.warn("Missing X-Tenant-Id header for request: {}. Using tenantId from query param.", requestURI);
            } else {
                log.warn("Missing X-Tenant-Id header for request: {}. Using empresaId from query param.", requestURI);
            }
        } else {
            Long currentFromJwt = TenantContext.getCurrentTenant();
            if (currentFromJwt != null) {
                tenantValue = currentFromJwt.toString();
                tenantSource = "jwt";
                log.info("Resolved tenant from JWT claims for request: {}", requestURI);
            } else {
                // Permitir requisições públicas, de autenticação ou de erro sem tenantId
                if (requestURI.contains("/public/") || requestURI.contains("/error") || requestURI.contains("/auth/")) {
                    log.info("Public, Auth or Error request detected, skipping mandatory tenant check: {}", requestURI);
                    return true;
                }
                
                log.warn("Missing X-Tenant-Id header for request: {}", requestURI);
                response.sendError(HttpStatus.BAD_REQUEST.value(), "Cabeçalho X-Tenant-Id é obrigatório (ou informe ?tenantId=..., ou use JWT com empresaId)");
                return false;
            }
        }

        try {
            Long tenantId = Long.parseLong(tenantValue);
            log.debug("Resolved tenantId={} from {}", tenantId, tenantSource);
            TenantContext.setCurrentTenant(tenantId);

            // Habilita o filtro Hibernate para isolar por id_empresa
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("tenantFilter")
                   .setParameter("tenantId", tenantId);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                usuarioRepository.findByEmail(username).ifPresent(usuario -> {
                    if (usuario.getEmpresaId() == null || !usuario.getEmpresaId().equals(tenantId)) {
                        try {
                            response.sendError(HttpStatus.FORBIDDEN.value(), "Usuário não pertence à empresa informada");
                        } catch (Exception ignored) {}
                    }
                });
                if (response.isCommitted()) {
                    return false;
                }
            }

            return true;
        } catch (NumberFormatException e) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Valor invÃ¡lido para X-Tenant-Id");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Limpa o contexto do tenant apÃ³s a conclusÃ£o da requisiÃ§Ã£o
        TenantContext.clear();
    }
}
