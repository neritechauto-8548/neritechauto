package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.common.mail.EmailService;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.LogAcesso;
import com.neritech.saas.gestaoUsuarios.domain.SessaoUsuario;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.LoginResponse;
import com.neritech.saas.gestaoUsuarios.dto.RefreshTokenRequest;
import com.neritech.saas.gestaoUsuarios.repository.LogAcessoRepository;
import com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository;
import com.neritech.saas.gestaoUsuarios.repository.TentativaLoginRepository;
import com.neritech.saas.gestaoUsuarios.repository.TokenRecuperacaoSenhaRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.security.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceSessionTest {

    private static final String OLD_REFRESH = "refresh-antigo";
    private static final String NEW_REFRESH = "refresh-novo";
    private static final String NEW_ACCESS = "access-novo";

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private SessaoUsuarioRepository sessaoUsuarioRepository;
    @Mock private LogAcessoRepository logAcessoRepository;
    @Mock private TentativaLoginRepository tentativaLoginRepository;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private TokenRecuperacaoSenhaRepository tokenRepository;
    @Mock private EmailService emailService;
    @Mock private PasswordEncoder passwordEncoder;

    private AuthService service;

    @BeforeEach
    void setUp() {
        service = new AuthService(
                usuarioRepository,
                sessaoUsuarioRepository,
                logAcessoRepository,
                tentativaLoginRepository,
                jwtService,
                authenticationManager,
                tokenRepository,
                emailService,
                passwordEncoder);
    }

    @AfterEach
    void clearTenant() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("Refresh deve rotacionar access e refresh token na mesma sessão")
    void refreshRotacionaTokensERevogaOsAnteriores() {
        Usuario usuario = usuario(5L, 7L);
        SessaoUsuario sessao = SessaoUsuario.builder()
                .id(9L)
                .usuario(usuario)
                .empresaId(7L)
                .tokenSessao("access-antigo")
                .refreshToken(OLD_REFRESH)
                .ativo(true)
                .dataExpiracao(LocalDateTime.now().plusHours(1))
                .build();

        when(jwtService.extractUsername(OLD_REFRESH)).thenReturn(usuario.getEmail());
        when(sessaoUsuarioRepository.findByRefreshTokenAndAtivoTrue(OLD_REFRESH))
                .thenReturn(Optional.of(sessao));
        when(usuarioRepository.findByEmailIgnoreCase(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);
        when(jwtService.generateToken(anyMap(), any(UserDetails.class))).thenReturn(NEW_ACCESS);
        when(jwtService.generateRefreshToken(any(UserDetails.class))).thenReturn(NEW_REFRESH);
        when(jwtService.getExpirationTime()).thenReturn(60_000L);
        when(jwtService.getRefreshExpirationTime()).thenReturn(600_000L);
        when(sessaoUsuarioRepository.saveAndFlush(sessao)).thenReturn(sessao);
        when(logAcessoRepository.saveAndFlush(any(LogAcesso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoginResponse response = service.refreshToken(
                RefreshTokenRequest.builder().refreshToken(OLD_REFRESH).build());

        assertThat(response.getAccessToken()).isEqualTo(NEW_ACCESS);
        assertThat(response.getRefreshToken()).isEqualTo(NEW_REFRESH);
        assertThat(response.getEmpresaId()).isEqualTo(7L);
        assertThat(sessao.getTokenSessao()).isEqualTo(NEW_ACCESS);
        assertThat(sessao.getRefreshToken()).isEqualTo(NEW_REFRESH);
        assertThat(sessao.getDataExpiracao()).isAfter(LocalDateTime.now());
        assertThat(TenantContext.getCurrentTenant()).isNull();
        verify(sessaoUsuarioRepository).saveAndFlush(sessao);
    }

    @Test
    @DisplayName("Refresh token já rotacionado ou revogado não deve poder ser reutilizado")
    void refreshAntigoNaoPodeSerReutilizado() {
        when(jwtService.extractUsername(OLD_REFRESH)).thenReturn("usuario@oficina.com.br");
        when(sessaoUsuarioRepository.findByRefreshTokenAndAtivoTrue(OLD_REFRESH))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.refreshToken(
                RefreshTokenRequest.builder().refreshToken(OLD_REFRESH).build()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Sessão inválida");

        verify(jwtService, never()).generateRefreshToken(any());
        verify(jwtService, never()).generateToken(anyMap(), any());
        assertThat(TenantContext.getCurrentTenant()).isNull();
    }

    private Usuario usuario(Long id, Long empresaId) {
        return Usuario.builder()
                .id(id)
                .empresaId(empresaId)
                .email("usuario@oficina.com.br")
                .senha("senha-hash")
                .nomeCompleto("Usuário Teste")
                .ativo(true)
                .bloqueado(false)
                .deveTrocarSenha(false)
                .build();
    }
}
