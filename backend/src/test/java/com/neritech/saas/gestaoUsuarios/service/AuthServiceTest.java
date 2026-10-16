package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.LogAcesso;
import com.neritech.saas.gestaoUsuarios.domain.SessaoUsuario;
import com.neritech.saas.gestaoUsuarios.domain.TentativaLogin;
import com.neritech.saas.gestaoUsuarios.domain.TokenRecuperacaoSenha;
import com.neritech.saas.gestaoUsuarios.dto.ResetPasswordRequest;
import com.neritech.saas.gestaoUsuarios.repository.TokenRecuperacaoSenhaRepository;
import com.neritech.saas.common.mail.EmailService;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.mockito.ArgumentCaptor;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.LoginRequest;
import com.neritech.saas.gestaoUsuarios.dto.LoginResponse;
import com.neritech.saas.gestaoUsuarios.dto.RefreshTokenRequest;
import com.neritech.saas.gestaoUsuarios.repository.LogAcessoRepository;
import com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository;
import com.neritech.saas.gestaoUsuarios.repository.TentativaLoginRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SessaoUsuarioRepository sessaoUsuarioRepository;
    @Mock
    private LogAcessoRepository logAcessoRepository;
    @Mock
    private TentativaLoginRepository tentativaLoginRepository;
    @Mock
    private LoginFailureRecorder loginFailureRecorder;
    @Mock
    private RecoveryRequestThrottle recoveryRequestThrottle;
    @Mock
    private TokenRecuperacaoSenhaRepository tokenRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        usuario = TestDataBuilder.umUsuario().build();
        usuario.setId(42L);
        usuario.setEmpresaId(1L);
        loginRequest = TestDataBuilder.umLoginRequest().build();
        TenantContext.setCurrentTenant(1L);
    }

    @AfterEach
    void clearTenantAfterTest() {
        TenantContext.clear();
    }

    @Nested
    @DisplayName("Login")
    class LoginTests {

        @Test
        @DisplayName("Deve realizar login com sucesso quando credenciais são válidas")
        void deveRealizarLoginComSucesso() {
            // Arrange
            Authentication authentication = mock(Authentication.class);
            UserDetails userDetails = new User(usuario.getEmail(), usuario.getSenha(), Collections.emptyList());
            
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(usuarioRepository.findByEmailIgnoreCase(usuario.getEmail())).thenReturn(Optional.of(usuario));
            
            // Corrigido: generateToken recebe apenas UserDetails (ou Map + UserDetails)
            when(jwtService.generateToken(anyMap(), any(UserDetails.class))).thenReturn("access.token");
            // Corrigido: generateRefreshToken recebe apenas UserDetails
            when(jwtService.generateRefreshToken(any(UserDetails.class))).thenReturn("refresh.token");
            
            // Corrigido: getters adicionados ao JwtService
            when(jwtService.getExpirationTime()).thenReturn(3600000L);

            // Act
            LoginResponse response = authService.login(loginRequest);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getAccessToken()).isEqualTo("access.token");
            assertThat(response.getRefreshToken()).isEqualTo("refresh.token");
            
            verify(sessaoUsuarioRepository).saveAndFlush(any(SessaoUsuario.class));
            verify(logAcessoRepository).saveAndFlush(any(LogAcesso.class));
            verify(tentativaLoginRepository).saveAndFlush(any(TentativaLogin.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando credenciais são inválidas")
        void deveLancarExcecaoQuandoCredenciaisInvalidas() {
            // Arrange
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));
            // Login falho de usuário conhecido deve registrar o log na empresa correta.
            when(usuarioRepository.findByEmailIgnoreCase(loginRequest.getEmail())).thenReturn(Optional.of(usuario));

            // Act & Assert
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(BadCredentialsException.class);
            
            verify(loginFailureRecorder).record(eq(loginRequest), eq(usuario.getEmpresaId()));
        }

        @Test
        @DisplayName("Deve limitar novas autenticações após cinco tentativas falhas em 15 minutos")
        void deveBloquearUsuarioAposMultiplasTentativas() {
            // A política atual limita temporariamente; não altera o usuário de forma permanente.
            when(tentativaLoginRepository.countByEmailAndSucessoFalseAndDataTentativaAfter(
                    eq(loginRequest.getEmail()), any(LocalDateTime.class)))
                    .thenReturn(5L);

            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                    .hasMessageContaining("429 TOO_MANY_REQUESTS");

            verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }

    }

    @Nested
    @DisplayName("AUTH-002 Recuperação de acesso")
    class RecoveryTests {
        private static final String EMAIL = "recuperacao@oficina.com";

        @Test
        void contaInexistenteRetornaGenericoSemCriarToken() {
            when(emailService.isPasswordResetConfigured()).thenReturn(true);
            when(recoveryRequestThrottle.checkAndRecord(EMAIL, "127.0.0.1")).thenReturn(true);
            when(usuarioRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.empty());

            org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                    authService.recoverPassword(EMAIL, "127.0.0.1"));

            verifyNoInteractions(tokenRepository);
            verify(emailService, never()).sendPasswordResetLink(anyString(), anyString(), anyString());
        }

        @Test
        void tokenOpacoSomenteHashTrintaMinutosERevogaAnterior() throws Exception {
            usuario.setEmail(EMAIL);
            TokenRecuperacaoSenha previous = TokenRecuperacaoSenha.builder()
                    .token("old-hash").usuario(usuario).usado(false)
                    .dataExpiracao(LocalDateTime.now().plusMinutes(15)).build();
            when(emailService.isPasswordResetConfigured()).thenReturn(true);
            when(recoveryRequestThrottle.checkAndRecord(EMAIL, "127.0.0.1")).thenReturn(true);
            when(usuarioRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(usuario));
            when(tokenRepository.findByUsuarioIdAndUsadoFalse(usuario.getId())).thenReturn(List.of(previous));

            authService.recoverPassword(EMAIL, "127.0.0.1");

            org.junit.jupiter.api.Assertions.assertTrue(previous.getUsado());
            verify(tokenRepository).save(previous);
            ArgumentCaptor<TokenRecuperacaoSenha> entity = ArgumentCaptor.forClass(TokenRecuperacaoSenha.class);
            verify(tokenRepository).saveAndFlush(entity.capture());
            TokenRecuperacaoSenha saved = entity.getValue();
            ArgumentCaptor<String> rawToken = ArgumentCaptor.forClass(String.class);
            verify(emailService).sendPasswordResetLink(eq(EMAIL), eq(usuario.getNomeCompleto()), rawToken.capture());
            String raw = rawToken.getValue();
            String expectedHash = HexFormat.of().formatHex(
                    MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8)));
            org.junit.jupiter.api.Assertions.assertEquals(expectedHash, saved.getToken());
            org.junit.jupiter.api.Assertions.assertNotEquals(raw, saved.getToken());
            org.junit.jupiter.api.Assertions.assertTrue(saved.getDataExpiracao().isAfter(LocalDateTime.now().plusMinutes(29)));
            org.junit.jupiter.api.Assertions.assertTrue(saved.getDataExpiracao().isBefore(LocalDateTime.now().plusMinutes(31)));
            verify(logAcessoRepository).saveAndFlush(any(LogAcesso.class));
        }

        @Test
        void limiteEmailInibeEnvioMasNaoEnumeraConta() {
            when(emailService.isPasswordResetConfigured()).thenReturn(true);
            when(recoveryRequestThrottle.checkAndRecord(EMAIL, "127.0.0.1")).thenReturn(false);
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                    authService.recoverPassword(EMAIL, "127.0.0.1"));
            verifyNoInteractions(usuarioRepository, tokenRepository);
        }

        @Test
        void smtpNaoConfiguradoRetorna503IndependentementeDaConta() {
            when(emailService.isPasswordResetConfigured()).thenReturn(false);
            org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                    authService.recoverPassword(EMAIL, "127.0.0.1"))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> org.junit.jupiter.api.Assertions.assertEquals(
                            HttpStatus.SERVICE_UNAVAILABLE, ((ResponseStatusException) e).getStatusCode()));
            verifyNoInteractions(usuarioRepository, recoveryRequestThrottle);
        }

        @Test
        void falhaEntregaIndividualNaoRevelaExistenciaDaConta() {
            usuario.setEmail(EMAIL);
            when(emailService.isPasswordResetConfigured()).thenReturn(true);
            when(recoveryRequestThrottle.checkAndRecord(EMAIL, "127.0.0.1")).thenReturn(true);
            when(usuarioRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(usuario));
            doThrow(new MailSendException("smtp indisponível")).when(emailService)
                    .sendPasswordResetLink(eq(EMAIL), any(), anyString());

            org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                    authService.recoverPassword(EMAIL, "127.0.0.1"));
            verify(logAcessoRepository).saveAndFlush(any(LogAcesso.class));
        }

        @Test
        void tokenExpiradoEJaConsumidoNaoRedefinemSenha() throws Exception {
            String raw = "token-opaco-auth-002";
            String hash = HexFormat.of().formatHex(
                    MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8)));
            TokenRecuperacaoSenha expired = TokenRecuperacaoSenha.builder().token(hash)
                    .usuario(usuario).usado(false)
                    .dataExpiracao(LocalDateTime.now().minusMinutes(1)).build();
            when(tokenRepository.findWithLockByToken(hash)).thenReturn(Optional.of(expired));
            ResetPasswordRequest request = new ResetPasswordRequest();
            request.setToken(raw);
            request.setNovaSenha("Uma frase de senha longa e segura ✓");
            org.assertj.core.api.Assertions.assertThatThrownBy(() -> authService.resetPassword(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("expirou");

            expired.setUsado(true);
            org.assertj.core.api.Assertions.assertThatThrownBy(() -> authService.resetPassword(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("utilizado");
            verifyNoInteractions(passwordEncoder);
        }
    }

    @Nested
    @DisplayName("AUTH-003 Redefinição e revogação")
    class ResetPasswordTests {
        private ResetPasswordRequest request(String token, String password) {
            ResetPasswordRequest request = new ResetPasswordRequest();
            request.setToken(token);
            request.setNovaSenha(password);
            return request;
        }

        private String hash(String token) throws Exception {
            return java.util.HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        }

        @Test
        void weakOrPredictablePasswordReturns422WithoutTouchingTheToken() {
            for (String password : java.util.List.of("short", "123456789012", "AAAAAAAAAAAA", "password1234")) {
                assertThatThrownBy(() -> authService.resetPassword(request("token", password)))
                        .isInstanceOf(ResponseStatusException.class)
                        .satisfies(ex -> org.junit.jupiter.api.Assertions.assertEquals(
                                HttpStatus.UNPROCESSABLE_ENTITY, ((ResponseStatusException) ex).getStatusCode()));
            }
            verifyNoInteractions(tokenRepository, passwordEncoder);
        }

        @Test
        void invalidTokenDoesNotUpdateCredentials() throws Exception {
            when(tokenRepository.findWithLockByToken(hash("token-desconhecido"))).thenReturn(Optional.empty());
            assertThatThrownBy(() -> authService.resetPassword(
                    request("token-desconhecido", "Uma frase senha longa e segura ✓")))
                    .isInstanceOf(IllegalArgumentException.class);
            verifyNoInteractions(passwordEncoder, sessaoUsuarioRepository);
        }

        @Test
        void repeatedParallelLikeRequestsCannotReuseConsumedToken() throws Exception {
            String raw = "token-unico-que-deve-ser-inutilizado";
            TokenRecuperacaoSenha reset = TokenRecuperacaoSenha.builder()
                    .token(hash(raw)).usuario(usuario).usado(false)
                    .dataExpiracao(LocalDateTime.now().plusMinutes(30)).build();
            TokenRecuperacaoSenha another = TokenRecuperacaoSenha.builder()
                    .token("outro-hash").usuario(usuario).usado(false)
                    .dataExpiracao(LocalDateTime.now().plusMinutes(30)).build();
            SessaoUsuario session = SessaoUsuario.builder()
                    .usuario(usuario).empresaId(usuario.getEmpresaId())
                    .tokenSessao("old-access").refreshToken("old-refresh").ativo(true).build();
            when(tokenRepository.findWithLockByToken(hash(raw))).thenReturn(Optional.of(reset));
            when(tokenRepository.findByUsuarioIdAndUsadoFalse(usuario.getId())).thenReturn(java.util.List.of(another));
            when(sessaoUsuarioRepository.findByUsuarioIdAndAtivoTrue(usuario.getId()))
                    .thenReturn(java.util.List.of(session));
            when(passwordEncoder.encode(anyString())).thenReturn("bcrypt-encoded");
            authService.resetPassword(request(raw, "Minha frase-senha segura 2026 ✓"));
            org.junit.jupiter.api.Assertions.assertTrue(reset.getUsado());
            org.junit.jupiter.api.Assertions.assertTrue(another.getUsado());
            org.junit.jupiter.api.Assertions.assertFalse(session.getAtivo());
            org.junit.jupiter.api.Assertions.assertEquals("bcrypt-encoded", usuario.getSenha());
            verify(tokenRepository).saveAndFlush(reset);
            verify(tokenRepository).save(another);
            verify(sessaoUsuarioRepository).save(session);
            verify(logAcessoRepository).saveAndFlush(any(LogAcesso.class));

            assertThatThrownBy(() -> authService.resetPassword(request(raw, "Nova frase ainda mais segura 2026 ✓")))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("utilizado");
            verify(passwordEncoder).encode(anyString());
        }

        @Test
        void samePreviousPasswordIsRejected() throws Exception {
            String raw = "token-senha-anterior";
            TokenRecuperacaoSenha reset = TokenRecuperacaoSenha.builder().token(hash(raw))
                    .usuario(usuario).usado(false)
                    .dataExpiracao(LocalDateTime.now().plusMinutes(15)).build();
            when(tokenRepository.findWithLockByToken(hash(raw))).thenReturn(Optional.of(reset));
            when(passwordEncoder.matches(anyString(), eq(usuario.getSenha()))).thenReturn(true);
            assertThatThrownBy(() -> authService.resetPassword(request(raw, "Senha anterior mais de 12 caracteres")))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(ex -> org.junit.jupiter.api.Assertions.assertEquals(
                            HttpStatus.UNPROCESSABLE_ENTITY, ((ResponseStatusException) ex).getStatusCode()));
            verify(usuarioRepository, never()).saveAndFlush(any(Usuario.class));
            org.junit.jupiter.api.Assertions.assertFalse(reset.getUsado());
        }
    }

    @Nested
    @DisplayName("Refresh Token")
    class RefreshTokenTests {

        @Test
        @DisplayName("Deve rejeitar sessão pertencente a empresa diferente")
        void deveRejeitarRefreshDeOutraEmpresa() {
            RefreshTokenRequest request = TestDataBuilder.umRefreshTokenRequest().build();
            when(jwtService.extractUsername(request.getRefreshToken())).thenReturn(usuario.getEmail());
            when(usuarioRepository.findByEmailIgnoreCase(usuario.getEmail())).thenReturn(Optional.of(usuario));

            SessaoUsuario outraEmpresa = SessaoUsuario.builder()
                    .usuario(usuario)
                    .empresaId(2L)
                    .refreshToken(request.getRefreshToken())
                    .ativo(true)
                    .build();
            when(sessaoUsuarioRepository.findByRefreshToken(request.getRefreshToken()))
                    .thenReturn(Optional.of(outraEmpresa));

            assertThatThrownBy(() -> authService.refreshToken(request))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessageContaining("Sessão inválida");
            verify(jwtService, never()).generateToken(anyMap(), any(UserDetails.class));
        }

        @Test
        @DisplayName("Deve rejeitar sessão revogada")
        void deveRejeitarRefreshDeSessaoInativa() {
            RefreshTokenRequest request = TestDataBuilder.umRefreshTokenRequest().build();
            when(jwtService.extractUsername(request.getRefreshToken())).thenReturn(usuario.getEmail());
            when(usuarioRepository.findByEmailIgnoreCase(usuario.getEmail())).thenReturn(Optional.of(usuario));
            SessaoUsuario revogada = SessaoUsuario.builder()
                    .usuario(usuario)
                    .empresaId(usuario.getEmpresaId())
                    .refreshToken(request.getRefreshToken())
                    .ativo(false)
                    .build();
            when(sessaoUsuarioRepository.findByRefreshToken(request.getRefreshToken()))
                    .thenReturn(Optional.of(revogada));

            assertThatThrownBy(() -> authService.refreshToken(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Sessão inativa");
            verify(jwtService, never()).generateToken(anyMap(), any(UserDetails.class));
        }


        @Test
        @DisplayName("Deve renovar token com sucesso")
        void deveRenovarTokenComSucesso() {
            // Arrange
            RefreshTokenRequest request = TestDataBuilder.umRefreshTokenRequest().build();
            String email = usuario.getEmail();
            
            when(jwtService.extractUsername(request.getRefreshToken())).thenReturn(email);
            when(usuarioRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(usuario));
            when(jwtService.isTokenValid(eq(request.getRefreshToken()), any(UserDetails.class))).thenReturn(true);
            when(sessaoUsuarioRepository.findByRefreshToken(request.getRefreshToken())).thenReturn(Optional.of(SessaoUsuario.builder().usuario(usuario).empresaId(usuario.getEmpresaId()).refreshToken(request.getRefreshToken()).ativo(true).build()));
            when(jwtService.generateToken(anyMap(), any(UserDetails.class))).thenReturn("new.access.token");

            // Act
            LoginResponse response = authService.refreshToken(request);

            // Assert
            assertThat(response.getAccessToken()).isEqualTo("new.access.token");
            verify(logAcessoRepository).saveAndFlush(any(LogAcesso.class));
        }
    }
}
