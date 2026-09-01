package com.neritech.saas.security;

import com.neritech.saas.common.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        TenantContext.clear();
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve limpar TenantContext após requisição pública sem Bearer token")
    void deveLimparTenantContextAposRequisicaoPublica() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, (req, res) -> {
            // Simula login/recuperação de senha definindo o tenant durante a requisição.
            TenantContext.setCurrentTenant(42L);
            assertThat(TenantContext.getCurrentTenant()).isEqualTo(42L);
        });

        assertThat(TenantContext.getCurrentTenant()).isNull();
        verify(jwtService, never()).extractUsername(any());
    }

    @Test
    @DisplayName("Não deve confiar no empresaId de JWT inválido")
    void naoDeveConfiarNoTenantDeJwtInvalido() throws Exception {
        String token = "jwt-invalido";
        UserDetails userDetails = new User(
                "usuario@neritechauto.com.br",
                "senha",
                Collections.emptyList()
        );

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername(token)).thenReturn(userDetails.getUsername());
        when(userDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);

        AtomicReference<Long> tenantObservadoNoDownstream = new AtomicReference<>();

        filter.doFilter(request, response, (req, res) ->
                tenantObservadoNoDownstream.set(TenantContext.getCurrentTenant())
        );

        assertThat(tenantObservadoNoDownstream.get()).isNull();
        assertThat(TenantContext.getCurrentTenant()).isNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(jwtService, never()).extractClaim(any(), any());
    }
}
