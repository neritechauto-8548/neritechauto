package com.neritech.saas.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FiscalPlanAccessFilterTest {

    @Mock
    private PlanAccessService planAccessService;

    @Mock
    private FilterChain filterChain;

    private FiscalPlanAccessFilter filter;

    @BeforeEach
    void setUp() {
        filter = new FiscalPlanAccessFilter(planAccessService);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnForbiddenForAuthenticatedUserWithoutFiscalAccess() throws Exception {
        authenticate();
        when(planAccessService.hasFiscalAccess()).thenReturn(false);

        MockHttpServletRequest request = request("/v1/fiscal/cfop");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        assertEquals(403, response.getStatus());
        verify(planAccessService).hasFiscalAccess();
        verifyNoInteractions(filterChain);
    }

    @Test
    void shouldContinueForAuthenticatedUserWithFiscalAccess() throws Exception {
        authenticate();
        when(planAccessService.hasFiscalAccess()).thenReturn(true);

        MockHttpServletRequest request = request("/v1/fiscal/configuracao-nfe");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(planAccessService).hasFiscalAccess();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldProtectNfeRoutesOutsideFiscalPrefix() throws Exception {
        authenticate();
        when(planAccessService.hasFiscalAccess()).thenReturn(false);

        MockHttpServletRequest request = request("/v1/ordens-servico/123/nfe/emitir");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        assertEquals(403, response.getStatus());
        verify(planAccessService).hasFiscalAccess();
        verifyNoInteractions(filterChain);
    }

    @Test
    void shouldIgnoreNonFiscalRoutes() throws Exception {
        authenticate();

        MockHttpServletRequest request = request("/v1/clientes");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verifyNoInteractions(planAccessService);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldLetSpringSecurityHandleUnauthenticatedFiscalRequest() throws Exception {
        MockHttpServletRequest request = request("/v1/fiscal/cfop");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verifyNoInteractions(planAccessService);
        verify(filterChain).doFilter(request, response);
    }

    private void authenticate() {
        var authentication = new UsernamePasswordAuthenticationToken(
                "user@neritechauto.com.br",
                null,
                List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private MockHttpServletRequest request(String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);
        request.setRequestURI(uri);
        return request;
    }
}
