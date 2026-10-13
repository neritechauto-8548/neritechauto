package com.neritech.saas.security;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock JwtService jwtService;
    @Mock UserDetailsService userDetailsService;
    @Mock SessaoUsuarioRepository sessaoUsuarioRepository;
    @Mock FilterChain filterChain;

    @AfterEach
    void cleanup() {
        TenantContext.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    void clearsCompanyContextAfterPublicLoginWithoutJwt() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        doAnswer(invocation -> {
            // A autenticacao publica estabelece a empresa dentro do proprio servico.
            TenantContext.setCurrentTenant(42L);
            return null;
        }).when(filterChain).doFilter(request, response);

        new JwtAuthenticationFilter(jwtService, userDetailsService, sessaoUsuarioRepository)
                .doFilter(request, response, filterChain);

        assertThat(TenantContext.getCurrentTenant()).isNull();
    }

    @Test
    void clearsCompanyContextEvenWhenPublicRequestFails() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/auth/refresh");
        MockHttpServletResponse response = new MockHttpServletResponse();
        TenantContext.setCurrentTenant(42L);
        doThrow(new ServletException("falha na requisicao"))
                .when(filterChain).doFilter(request, response);

        assertThatThrownBy(() -> new JwtAuthenticationFilter(
                jwtService, userDetailsService, sessaoUsuarioRepository)
                .doFilter(request, response, filterChain))
                .isInstanceOf(ServletException.class);
        assertThat(TenantContext.getCurrentTenant()).isNull();
    }
}
