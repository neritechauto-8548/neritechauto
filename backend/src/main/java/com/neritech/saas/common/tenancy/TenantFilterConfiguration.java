package com.neritech.saas.common.tenancy;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * ConfiguraÃ§Ã£o para registrar o interceptor de tenant e configurar o filtro Hibernate
 */
@Configuration
public class TenantFilterConfiguration implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(TenantFilterConfiguration.class);
    private final TenantInterceptor tenantInterceptor;
    private final EntityManager entityManager;

    public TenantFilterConfiguration(TenantInterceptor tenantInterceptor, EntityManager entityManager) {
        log.info("TenantFilterConfiguration bean created!");
        log.info("TenantInterceptor injected: {}", tenantInterceptor);
        log.info("EntityManager injected: {}", entityManager);
        this.tenantInterceptor = tenantInterceptor;
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void init() {
        log.info("TenantFilterConfiguration initialized successfully");
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        log.info("=== REGISTERING TENANT INTERCEPTOR ===");
        log.info("TenantInterceptor instance: {}", tenantInterceptor);
        registry.addInterceptor(tenantInterceptor)
                .addPathPatterns("/api/**", "/**")
                .excludePathPatterns(
                        "/auth/**",
                        "/api/auth/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/api-docs/**",
                        "/v1/ordens-servico/fotos/*/download",
                        "/api/v1/ordens-servico/fotos/*/download"
                );
        log.info("=== TENANT INTERCEPTOR REGISTERED SUCCESSFULLY ===");
    }

    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> argumentResolvers) {
        log.info("=== CONFIGURING PAGEABLE RESOLVER ===");
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true); // PÃ¡ginas comeÃ§am em 1 em vez de 0
        resolver.setMaxPageSize(100); // Tamanho mÃ¡ximo de pÃ¡gina
        argumentResolvers.add(resolver);
        log.info("=== PAGEABLE RESOLVER CONFIGURED SUCCESSFULLY ===");
    }

    /**
     * Ativa o filtro de tenant para a sessÃ£o Hibernate atual
     */
    public void enableTenantFilter() {
        Session session = entityManager.unwrap(Session.class);
        Long currentTenant = TenantContext.getCurrentTenant();
        
        if (currentTenant != null) {
            session.enableFilter("tenantFilter")
                   .setParameter("tenantId", currentTenant);
        }
    }
}
