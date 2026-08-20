package com.neritech.saas.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final String VALID_SECRET = "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi1taW5pbXVtLTI1Ni1iaXRzLXJlcXVpcmVkLWZvci1oczI1Ni1hbGdvcml0aG0=";

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", VALID_SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 7200000L);
        ReflectionTestUtils.setField(jwtService, "cachedKey", null);
        userDetails = new User("test@email.com", "password", Collections.emptyList());
    }

    @Test
    @DisplayName("Deve gerar token valido")
    void deveGerarTokenValido() {
        String token = jwtService.generateToken(userDetails);

        assertThat(token).isNotBlank();
        assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    @DisplayName("Deve extrair username do token")
    void deveExtrairUsername() {
        String token = jwtService.generateToken(userDetails);

        assertThat(jwtService.extractUsername(token)).isEqualTo(userDetails.getUsername());
    }

    @Test
    @DisplayName("Nao deve iniciar com segredo JWT ausente")
    void segredoAusenteFalhaRapido() {
        ReflectionTestUtils.setField(jwtService, "secretKey", " ");
        ReflectionTestUtils.setField(jwtService, "cachedKey", null);

        assertThatThrownBy(() -> jwtService.generateToken(userDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("JWT_SECRET nao configurado");
    }

    @Test
    @DisplayName("Nao deve aceitar segredo JWT Base64 curto")
    void segredoCurtoFalhaRapido() {
        ReflectionTestUtils.setField(jwtService, "secretKey", "dGVzdGU=");
        ReflectionTestUtils.setField(jwtService, "cachedKey", null);

        assertThatThrownBy(() -> jwtService.generateToken(userDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("pelo menos 256 bits");
    }

    @Test
    @DisplayName("Token expirado deve ser rejeitado")
    void deveRejeitarTokenExpirado() throws InterruptedException {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1L);
        String token = jwtService.generateToken(userDetails);
        Thread.sleep(5L);

        assertThatThrownBy(() -> jwtService.isTokenValid(token, userDetails))
                .isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
    }
}
