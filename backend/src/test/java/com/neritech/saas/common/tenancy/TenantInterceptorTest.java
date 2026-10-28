package com.neritech.saas.common.tenancy;

import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantInterceptorTest {

    @Mock EntityManager entityManager;
    @Mock UsuarioRepository usuarioRepository;
    @Mock Session session;
    @Mock Filter tenantFilter;

    @AfterEach
    void clearContext() {
        TenantContext.clear();
        SecurityContextHolder.clearContext();
    }

    private TenantInterceptor interceptor() {
        return new TenantInterceptor(entityManager, usuarioRepository);
    }

    @Test
    void rejectsHeaderWithDifferentAuthenticatedCompany() throws Exception {
        TenantContext.setCurrentTenant(10L);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/clientes");
        request.addHeader("X-Tenant-Id", "20");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThat(interceptor().preHandle(request, response, new Object())).isFalse();
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(TenantContext.getCurrentTenant()).isEqualTo(10L);
        verify(entityManager, never()).unwrap(Session.class);
    }

    @Test
    void allowsSameCompanyAndEnablesHibernateFilter() throws Exception {
        TenantContext.setCurrentTenant(10L);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter("tenantFilter")).thenReturn(tenantFilter);
        when(tenantFilter.setParameter("tenantId", 10L)).thenReturn(tenantFilter);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/clientes");
        request.addHeader("X-Tenant-Id", "10");

        assertThat(interceptor().preHandle(request, new MockHttpServletResponse(), new Object())).isTrue();
        verify(tenantFilter).setParameter("tenantId", 10L);
    }

    @Test
    void usesAuthenticatedCompanyWhenHeaderIsMissing() throws Exception {
        TenantContext.setCurrentTenant(10L);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter("tenantFilter")).thenReturn(tenantFilter);
        when(tenantFilter.setParameter("tenantId", 10L)).thenReturn(tenantFilter);

        assertThat(interceptor().preHandle(
                new MockHttpServletRequest("GET", "/v1/clientes"),
                new MockHttpServletResponse(), new Object())).isTrue();
        verify(tenantFilter).setParameter("tenantId", 10L);
    }

    @Test
    void rejectsMissingCompanyContext() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThat(interceptor().preHandle(
                new MockHttpServletRequest("GET", "/v1/clientes"), response, new Object())).isFalse();
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void rejectsInvalidCompanyIdentifier() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/clientes");
        request.addHeader("X-Tenant-Id", "not-a-number");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThat(interceptor().preHandle(request, response, new Object())).isFalse();
        assertThat(response.getStatus()).isEqualTo(400);
    }
}
