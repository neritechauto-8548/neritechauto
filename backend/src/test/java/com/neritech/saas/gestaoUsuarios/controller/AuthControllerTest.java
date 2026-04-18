package com.neritech.saas.gestaoUsuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.dto.LoginRequest;
import com.neritech.saas.gestaoUsuarios.dto.LoginResponse;
import com.neritech.saas.gestaoUsuarios.dto.RefreshTokenRequest;
import com.neritech.saas.gestaoUsuarios.service.AuthService;
import com.neritech.saas.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
        mockMvc.perform(post("/api/auth/login")
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
        mockMvc.perform(post("/api/auth/login")
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
        mockMvc.perform(post("/api/auth/refresh")
                .header("X-Tenant-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access.token"));
    }

    @Test
    @DisplayName("POST /auth/logout - Deve retornar 204")
    void deveRetornar204NoLogout() throws Exception {
        // Arrange
        doNothing().when(authService).logout(any());

        // Act & Assert
        mockMvc.perform(post("/api/auth/logout")
                .header("X-Tenant-Id", "1")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }
}
