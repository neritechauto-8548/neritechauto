package com.neritech.saas.security;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository;
import com.neritech.saas.gestaoUsuarios.domain.SessaoUsuario;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock JwtService jwtService;
    @Mock UserDetailsService userDetailsService;
    @Mock SessaoUsuarioRepository sessaoUsuarioRepository;
    @Mock FilterChain filterChain;

    private final String testJwt = "jwt-de-teste";
    private final String testEmail = "admin@empresa-a.teste";

    private void stubValidJwt(long empresaId, boolean isValid) {
        UserDetails details = User.withUsername(testEmail)
                .password("senha-hash").authorities("ROLE_ADMIN").build();
        when(jwtService.extractUsername(testJwt)).thenReturn(testEmail);
        doReturn(empresaId).when(jwtService).extractClaim(eq(testJwt), any());
        when(userDetailsService.loadUserByUsername(testEmail)).thenReturn(details);
        when(jwtService.isTokenValid(testJwt, details)).thenReturn(isValid);
    }

    private MockHttpServletRequest authenticatedRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/clientes");
        request.addHeader("Authorization", "Bearer " + testJwt);
        return request;
    }

    @Test
    void authenticatesOnlyWhenTokenSessionAndUserBelongToSameCompany() throws Exception {
        stubValidJwt(10L, true);
        SessaoUsuario sessao = mock(SessaoUsuario.class);
        Usuario usuario = mock(Usuario.class);
        when(sessao.getAtivo()).thenReturn(true);
        when(sessao.getEmpresaId()).thenReturn(10L);
        when(sessao.getUsuario()).thenReturn(usuario);
        when(usuario.getEmpresaId()).thenReturn(10L);
        when(usuario.getEmail()).thenReturn(testEmail);
        when(sessaoUsuarioRepository.findByTokenSessao(testJwt)).thenReturn(Optional.of(sessao));
        MockHttpServletRequest request = authenticatedRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        doAnswer(invocation -> {
            assertThat(TenantContext.getCurrentTenant()).isEqualTo(10L);
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
            return null;
        }).when(filterChain).doFilter(request, response);

        new JwtAuthenticationFilter(jwtService, userDetailsService, sessaoUsuarioRepository)
                .doFilter(request, response, filterChain);
        assertThat(TenantContext.getCurrentTenant()).isNull();
    }

    @Test
    void rejectsJwtClaimFromAnotherCompanyEvenWhenSessionIsActive() throws Exception {
        stubValidJwt(20L, true);
        SessaoUsuario sessao = mock(SessaoUsuario.class);
        when(sessao.getAtivo()).thenReturn(true);
        when(sessao.getEmpresaId()).thenReturn(10L);
        when(sessaoUsuarioRepository.findByTokenSessao(testJwt)).thenReturn(Optional.of(sessao));
        MockHttpServletRequest request = authenticatedRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        doAnswer(invocation -> {
            assertThat(TenantContext.getCurrentTenant()).isNull();
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
            return null;
        }).when(filterChain).doFilter(request, response);

        new JwtAuthenticationFilter(jwtService, userDetailsService, sessaoUsuarioRepository)
                .doFilter(request, response, filterChain);
        assertThat(TenantContext.getCurrentTenant()).isNull();
    }

    @Test
    void rejectsOldSessionAfterUserMovesToAnotherCompany() throws Exception {
        stubValidJwt(10L, true);
        SessaoUsuario sessao = mock(SessaoUsuario.class);
        Usuario usuario = mock(Usuario.class);
        when(sessao.getAtivo()).thenReturn(true);
        when(sessao.getEmpresaId()).thenReturn(10L);
        when(sessao.getUsuario()).thenReturn(usuario);
        when(usuario.getEmpresaId()).thenReturn(20L);
        when(sessaoUsuarioRepository.findByTokenSessao(testJwt)).thenReturn(Optional.of(sessao));
        MockHttpServletRequest request = authenticatedRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        doAnswer(invocation -> {
            assertThat(TenantContext.getCurrentTenant()).isNull();
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
            return null;
        }).when(filterChain).doFilter(request, response);

        new JwtAuthenticationFilter(jwtService, userDetailsService, sessaoUsuarioRepository)
                .doFilter(request, response, filterChain);
        assertThat(TenantContext.getCurrentTenant()).isNull();
    }

    @Test
    void rejectsInvalidJwtBeforeEstablishingCompanyContext() throws Exception {
        stubValidJwt(10L, false);
        MockHttpServletRequest request = authenticatedRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        doAnswer(invocation -> {
            assertThat(TenantContext.getCurrentTenant()).isNull();
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
            return null;
        }).when(filterChain).doFilter(request, response);

        new JwtAuthenticationFilter(jwtService, userDetailsService, sessaoUsuarioRepository)
                .doFilter(request, response, filterChain);
        assertThat(TenantContext.getCurrentTenant()).isNull();
    }

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
