package com.neritech.saas.gestaoUsuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.dto.LoginRequest;
import com.neritech.saas.gestaoUsuarios.dto.LoginResponse;
import com.neritech.saas.gestaoUsuarios.dto.RefreshTokenRequest;
import com.neritech.saas.gestaoUsuarios.service.AuthService;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.security.JwtService;
import com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "neritech.tenancy.web.enabled=false")
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Desabilita filtros de segurança para focar no controller
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private SessaoUsuarioRepository sessaoUsuarioRepository;

    @MockBean
    private JwtService jwtService; // Necessário pois pode ser injetado em algum lugar

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginRequest = TestDataBuilder.umLoginRequest().build();
        loginResponse = LoginResponse.builder()
                .accessToken("access.token")
                .refreshToken("refresh.token")
                .build();
    }

    @Test
    @DisplayName("POST /auth/login - Deve retornar 200 e token quando credenciais válidas")
    void deveRetornarTokenQuandoLoginSucesso() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access.token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh.token"));
    }

    @Test
    @DisplayName("POST /auth/login - Deve retornar 401 quando credenciais inválidas")
    void deveRetornar401QuandoLoginFalha() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /auth/refresh - Deve retornar novo token")
    void deveRetornarNovoToken() throws Exception {
        // Arrange
        RefreshTokenRequest request = TestDataBuilder.umRefreshTokenRequest().build();
        when(authService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/refresh")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access.token"));
    }

    @Test
    @DisplayName("AUTH-002 — endereço cadastrado ou desconhecido recebe resposta uniforme")
    void recoverPasswordNaoEnumeraConta() throws Exception {
        for (String email : new String[] { "usuario@exemplo.com", "naoexiste@exemplo.com" }) {
            mockMvc.perform(post("/auth/recover-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"" + email + "\"}"))
                    .andExpect(status().isOk());
            org.mockito.Mockito.verify(authService).recoverPassword(
                    org.mockito.ArgumentMatchers.eq(email),
                    org.mockito.ArgumentMatchers.anyString());
        }
    }

    @Test
    @DisplayName("AUTH-002 — ausência de provedor retorna 503 e IP vem do servlet, não do JSON")
    void recoverPasswordIndisponivelNaoAceitaIpForjado() throws Exception {
        org.mockito.Mockito.doThrow(new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE, "Recuperação indisponível"))
                .when(authService).recoverPassword(
                        org.mockito.ArgumentMatchers.anyString(),
                        org.mockito.ArgumentMatchers.anyString());

        mockMvc.perform(post("/auth/recover-password")
                .with(request -> { request.setRemoteAddr("192.0.2.1"); return request; })
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"usuario@exemplo.com\",\"ipAddress\":\"198.51.100.25\"}"))
                .andExpect(status().isServiceUnavailable());

        org.mockito.Mockito.verify(authService).recoverPassword("usuario@exemplo.com", "192.0.2.1");
    }

    @Test
    @DisplayName("AUTH-002 — e-mail obrigatório e formato inválido retornam 422")
    void recoverPasswordValidaEmail() throws Exception {
        for (String bad : new String[] { "invalido", "" }) {
            mockMvc.perform(post("/auth/recover-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"" + bad + "\"}"))
                    .andExpect(status().isUnprocessableEntity());
        }
        org.mockito.Mockito.verify(authService, org.mockito.Mockito.never()).recoverPassword(
                org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("AUTH-003 — senha abaixo de 12 caracteres retorna 422 sem consumir o token")
    void resetPasswordRejectsWeakInputsAtHttpBoundary() throws Exception {
        mockMvc.perform(post("/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"reset-token\",\"novaSenha\":\"123456\"}"))
                .andExpect(status().isUnprocessableEntity());
        org.mockito.Mockito.verify(authService, org.mockito.Mockito.never())
                .resetPassword(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("AUTH-003 — a resposta 200 só acontece após confirmação do serviço")
    void resetPasswordReturns200AfterServiceSucceeds() throws Exception {
        mockMvc.perform(post("/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"reset-token\",\"novaSenha\":\"Minha frase-senha longa e segura ✓\"}"))
                .andExpect(status().isOk());
        org.mockito.Mockito.verify(authService).resetPassword(
                org.mockito.ArgumentMatchers.argThat(request ->
                        "reset-token".equals(request.getToken())));
    }

    @Test
    @DisplayName("POST /auth/logout - Deve retornar 204")
    void deveRetornar204NoLogout() throws Exception {
        // Arrange
        doNothing().when(authService).logout(any());

        // Act & Assert
        mockMvc.perform(post("/auth/logout")
                .header("X-Tenant-Id", "1")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }
}
