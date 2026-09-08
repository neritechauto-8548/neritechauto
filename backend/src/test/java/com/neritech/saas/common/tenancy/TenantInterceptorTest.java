package com.neritech.saas.common.tenancy;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantInterceptorTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private Session session;
    @Mock
    private Filter filter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    private TenantInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new TenantInterceptor(entityManager);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("Deve negar rota protegida sem tenant autenticado")
    void deveNegarSemTenantAutenticado() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/v1/clientes");

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertThat(allowed).isFalse();
        verify(response).sendError(401, "Contexto de empresa autenticado ausente");
        verify(entityManager, never()).unwrap(Session.class);
    }

    @Test
    @DisplayName("Deve aceitar header somente quando corresponde ao tenant autenticado")
    void deveAceitarHeaderCorrespondente() throws Exception {
        TenantContext.setCurrentTenant(10L);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/v1/clientes");
        when(request.getHeader("X-Tenant-Id")).thenReturn("10");
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter("tenantFilter")).thenReturn(filter);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertThat(allowed).isTrue();
        verify(filter).setParameter("tenantId", 10L);
    }

    @Test
    @DisplayName("Deve negar header de tenant diferente do tenant autenticado")
    void deveNegarHeaderDeOutroTenant() throws Exception {
        TenantContext.setCurrentTenant(10L);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/v1/clientes/99");
        when(request.getHeader("X-Tenant-Id")).thenReturn("20");

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertThat(allowed).isFalse();
        verify(response).sendError(403, "Contexto de empresa nao autorizado");
        verify(entityManager, never()).unwrap(Session.class);
    }

    @Test
    @DisplayName("Query params tenantId e empresaId nao podem trocar tenant autenticado")
    void queryParamsNaoPodemTrocarTenant() throws Exception {
        TenantContext.setCurrentTenant(10L);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/v1/clientes");
        when(request.getHeader("X-Tenant-Id")).thenReturn(null);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter("tenantFilter")).thenReturn(filter);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertThat(allowed).isTrue();
        verify(filter).setParameter("tenantId", 10L);
        verify(request, never()).getParameter("tenantId");
        verify(request, never()).getParameter("empresaId");
    }
}
