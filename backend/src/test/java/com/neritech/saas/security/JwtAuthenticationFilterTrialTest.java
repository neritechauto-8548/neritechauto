package com.neritech.saas.security;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import com.neritech.saas.gestaoUsuarios.domain.SessaoUsuario;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTrialTest {

    @Mock JwtService jwtService;
    @Mock UserDetailsService userDetailsService;
    @Mock SessaoUsuarioRepository sessaoUsuarioRepository;
    @Mock AssinaturaEmpresaRepository assinaturaEmpresaRepository;
    @InjectMocks JwtAuthenticationFilter filter;

    @BeforeEach
    void resetContext() {
        clearContext();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }

    @Test
    void blocksBusinessApisAfterTrialExpires() throws Exception {
        setupSession();
        MockHttpServletRequest request = request("/v1/ordens-servico");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(402);
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void keepsSubscriptionCheckoutAvailableAfterTrialExpires() throws Exception {
        setupSession();
        MockHttpServletRequest request = request("/v1/assinatura/checkout/7");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(200);
        verify(chain).doFilter(request, response);
        verifyNoInteractions(assinaturaEmpresaRepository);
    }

    private void setupSession() {
        String email = "gestor@oficina.example";
        Usuario usuario = Usuario.builder().empresaId(7L).email(email).build();
        SessaoUsuario sessao = mock(SessaoUsuario.class);
        when(sessao.getAtivo()).thenReturn(true);
        when(sessao.getEmpresaId()).thenReturn(7L);
        when(sessao.getUsuario()).thenReturn(usuario);
        when(jwtService.extractUsername("valid-token")).thenReturn(email);
        doReturn(7L).when(jwtService).extractClaim(eq("valid-token"), any());
        when(userDetailsService.loadUserByUsername(email))
                .thenReturn(User.withUsername(email).password("unused").roles("ADMIN").build());
        when(jwtService.isTokenValid(eq("valid-token"), any())).thenReturn(true);
        when(sessaoUsuarioRepository.findByTokenSessao("valid-token")).thenReturn(Optional.of(sessao));

        AssinaturaEmpresa assinatura = new AssinaturaEmpresa();
        assinatura.setStatus(StatusAssinatura.TESTE);
        assinatura.setTrialEndsAt(LocalDateTime.now().minusMinutes(1));
        lenient().when(assinaturaEmpresaRepository.findFirstByEmpresaIdOrderByDataFimDesc(7L))
                .thenReturn(Optional.of(assinatura));
    }

    private MockHttpServletRequest request(String path) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", path);
        request.setServletPath(path);
        request.addHeader("Authorization", "Bearer valid-token");
        return request;
    }
}
