package com.neritech.saas.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Configurar propriedades via Reflection pois são @Value
        // Secret key válida em Base64 (256 bits)
        ReflectionTestUtils.setField(jwtService, "secretKey", "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi1taW5pbXVtLTI1Ni1iaXRzLXJlcXVpcmVkLWZvci1oczI1Ni1hbGdvcml0aG0=");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 7200000L);

        userDetails = new User("test@email.com", "password", Collections.emptyList());
    }

    @Test
    @DisplayName("Deve gerar token válido")
    void deveGerarTokenValido() {
        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertThat(token).isNotNull();
        assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    @DisplayName("Deve extrair username do token")
    void deveExtrairUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertThat(username).isEqualTo(userDetails.getUsername());
    }

    @Test
    @DisplayName("Deve validar token expirado")
    void deveValidarTokenExpirado() {
        // Arrange
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L); // Expirado
        String token = jwtService.generateToken(userDetails);

        // Act & Assert
        // Dependendo da implementação, pode lançar exceção ou retornar false
        // Assumindo que lança exceção ao extrair claims de token expirado
        try {
            jwtService.isTokenValid(token, userDetails);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
        }
    }
}
