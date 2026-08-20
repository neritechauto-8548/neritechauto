package com.neritech.saas.security;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    private static final String TOKEN = "token-valido";
    private static final String EMAIL = "usuario@oficina.com.br";

    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService, usuarioRepository);
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + TOKEN);
        response = new MockHttpServletResponse();
        userDetails = new User(EMAIL, "senha", Collections.emptyList());
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Token valido deve usar empresa atual do usuario como tenant autoritativo")
    void tokenValidoUsaTenantDaIdentidadeAtual() throws Exception {
        Usuario usuario = Usuario.builder()
                .email(EMAIL)
                .senha("senha")
                .nomeCompleto("Usuario Teste")
                .empresaId(10L)
                .ativo(true)
                .bloqueado(false)
                .build();

        when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        when(jwtService.isTokenValid(TOKEN, userDetails)).thenReturn(true);
        when(usuarioRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(usuario));
        doReturn(10L).when(jwtService).extractClaim(eq(TOKEN), any());

        doAnswer(invocation -> {
            assertThat(TenantContext.getCurrentTenant()).isEqualTo(10L);
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
            assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo(EMAIL);
            return null;
        }).when(filterChain).doFilter(request, response);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(TenantContext.getCurrentTenant()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Claim empresaId divergente do vinculo atual deve ser rejeitado")
    void claimDeOutroTenantNaoAutentica() throws Exception {
        Usuario usuario = Usuario.builder()
                .email(EMAIL)
                .senha("senha")
                .nomeCompleto("Usuario Teste")
                .empresaId(10L)
                .ativo(true)
                .bloqueado(false)
                .build();

        when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        when(jwtService.isTokenValid(TOKEN, userDetails)).thenReturn(true);
        when(usuarioRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(usuario));
        doReturn(99L).when(jwtService).extractClaim(eq(TOKEN), any());

        doAnswer(invocation -> {
            assertThat(TenantContext.getCurrentTenant()).isNull();
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
            return null;
        }).when(filterChain).doFilter(request, response);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Token invalido nunca deve estabelecer tenant ou autenticacao")
    void tokenInvalidoNaoEstabeleceContexto() throws Exception {
        when(jwtService.extractUsername(TOKEN)).thenThrow(new IllegalArgumentException("token invalido"));

        doAnswer(invocation -> {
            assertThat(TenantContext.getCurrentTenant()).isNull();
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
            return null;
        }).when(filterChain).doFilter(request, response);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
