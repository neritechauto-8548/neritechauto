package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.LogAcesso;
import com.neritech.saas.gestaoUsuarios.domain.SessaoUsuario;
import com.neritech.saas.gestaoUsuarios.domain.TentativaLogin;
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

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        usuario = TestDataBuilder.umUsuario().build();
        loginRequest = TestDataBuilder.umLoginRequest().build();
        TenantContext.setCurrentTenant(1L);
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
            when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
            
            // Corrigido: generateToken recebe apenas UserDetails (ou Map + UserDetails)
            when(jwtService.generateToken(any(UserDetails.class))).thenReturn("access.token");
            // Corrigido: generateRefreshToken recebe apenas UserDetails
            when(jwtService.generateRefreshToken(any(UserDetails.class))).thenReturn("refresh.token");
            
            // Corrigido: getters adicionados ao JwtService
            when(jwtService.getExpirationTime()).thenReturn(3600000L);
            when(jwtService.getRefreshExpirationTime()).thenReturn(7200000L);

            // Act
            LoginResponse response = authService.login(loginRequest);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getAccessToken()).isEqualTo("access.token");
            assertThat(response.getRefreshToken()).isEqualTo("refresh.token");
            
            verify(sessaoUsuarioRepository).save(any(SessaoUsuario.class));
            verify(logAcessoRepository).save(any(LogAcesso.class));
            verify(tentativaLoginRepository).deleteByEmailAndEmpresaId(anyString(), anyLong());
        }

        @Test
        @DisplayName("Deve lançar exceção quando credenciais são inválidas")
        void deveLancarExcecaoQuandoCredenciaisInvalidas() {
            // Arrange
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));

            // Act & Assert
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(BadCredentialsException.class);
            
            verify(tentativaLoginRepository).save(any(TentativaLogin.class));
            verify(logAcessoRepository).save(any(LogAcesso.class));
        }

        @Test
        @DisplayName("Deve bloquear usuário após múltiplas tentativas falhas")
        void deveBloquearUsuarioAposMultiplasTentativas() {
            // Arrange
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));
            when(tentativaLoginRepository.countByEmailAndEmpresaIdAndSucessoFalseAndDataTentativaAfter(
                    anyString(), anyLong(), any(LocalDateTime.class)))
                    .thenReturn(5L); // Limite excedido
            when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

            // Act & Assert
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(BadCredentialsException.class);

            verify(usuarioRepository).save(argThat(u -> u.getBloqueado() == true));
        }
    }

    @Nested
    @DisplayName("Refresh Token")
    class RefreshTokenTests {

        @Test
        @DisplayName("Deve renovar token com sucesso")
        void deveRenovarTokenComSucesso() {
            // Arrange
            RefreshTokenRequest request = TestDataBuilder.umRefreshTokenRequest().build();
            String email = usuario.getEmail();
            
            when(jwtService.extractUsername(request.getRefreshToken())).thenReturn(email);
            when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
            when(jwtService.isTokenValid(eq(request.getRefreshToken()), any(UserDetails.class))).thenReturn(true);
            when(jwtService.generateToken(any(UserDetails.class))).thenReturn("new.access.token");
            when(jwtService.getExpirationTime()).thenReturn(3600000L);

            // Act
            LoginResponse response = authService.refreshToken(request);

            // Assert
            assertThat(response.getAccessToken()).isEqualTo("new.access.token");
            verify(logAcessoRepository).save(any(LogAcesso.class));
        }
    }
}
