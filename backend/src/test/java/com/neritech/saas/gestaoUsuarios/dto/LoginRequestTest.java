package com.neritech.saas.gestaoUsuarios.dto;

import com.neritech.saas.TestDataBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar login request válido")
    void deveValidarLoginRequestValido() {
        // Arrange
        LoginRequest request = TestDataBuilder.umLoginRequest().build();

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve falhar quando email é inválido")
    void deveFalharQuandoEmailInvalido() {
        // Arrange
        LoginRequest request = TestDataBuilder.umLoginRequest().comEmail("invalid-email").build();

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    @DisplayName("Deve falhar quando senha é vazia")
    void deveFalharQuandoSenhaVazia() {
        // Arrange
        LoginRequest request = TestDataBuilder.umLoginRequest().comSenha("").build();

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("senha"));
    }
}
