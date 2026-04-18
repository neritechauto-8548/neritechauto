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

class UsuarioRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar usuario request válido")
    void deveValidarUsuarioRequestValido() {
        // Arrange
        UsuarioRequest request = TestDataBuilder.umUsuarioRequest().build();

        // Act
        Set<ConstraintViolation<UsuarioRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve falhar quando nome é vazio")
    void deveFalharQuandoNomeVazio() {
        // Arrange
        UsuarioRequest request = TestDataBuilder.umUsuarioRequest().comNomeCompleto("").build();

        // Act
        Set<ConstraintViolation<UsuarioRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nomeCompleto"));
    }
}
