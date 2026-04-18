package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import com.neritech.saas.gestaoUsuarios.mapper.UsuarioMapper;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequest usuarioRequest;

    @BeforeEach
    void setUp() {
        usuario = TestDataBuilder.umUsuario().build();
        usuarioRequest = TestDataBuilder.umUsuarioRequest().build();
        TenantContext.setCurrentTenant(1L);
    }

    @Nested
    @DisplayName("Create")
    class CreateTests {

        @Test
        @DisplayName("Deve criar usuário com sucesso")
        void deveCriarUsuarioComSucesso() {
            // Arrange
            when(usuarioRepository.existsByEmailAndEmpresaId(anyString(), anyLong())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(usuarioMapper.toEntity(any(UsuarioRequest.class))).thenReturn(usuario);
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
            
            UsuarioResponse expectedResponse = new UsuarioResponse();
            expectedResponse.setId(1L);
            expectedResponse.setEmail(usuario.getEmail());
            when(usuarioMapper.toResponse(any(Usuario.class))).thenReturn(expectedResponse);

            // Act
            UsuarioResponse response = usuarioService.create(usuarioRequest);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getEmail()).isEqualTo(usuario.getEmail());
            verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando email já existe")
        void deveLancarExcecaoQuandoEmailExiste() {
            // Arrange
            when(usuarioRepository.existsByEmailAndEmpresaId(anyString(), anyLong())).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.create(usuarioRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Email já cadastrado");
            
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }
    }

    @Nested
    @DisplayName("FindById")
    class FindByIdTests {

        @Test
        @DisplayName("Deve retornar usuário quando id existe")
        void deveRetornarUsuarioQuandoIdExiste() {
            // Arrange
            when(usuarioRepository.findByIdAndEmpresaId(1L, 1L)).thenReturn(Optional.of(usuario));
            
            UsuarioResponse expectedResponse = new UsuarioResponse();
            expectedResponse.setId(1L);
            when(usuarioMapper.toResponse(usuario)).thenReturn(expectedResponse);

            // Act
            UsuarioResponse response = usuarioService.findById(1L);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando id não existe")
        void deveLancarExcecaoQuandoIdNaoExiste() {
            // Arrange
            when(usuarioRepository.findByIdAndEmpresaId(1L, 1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.findById(1L))
                    .isInstanceOf(RuntimeException.class); // Ou EntityNotFoundException se tiver
        }
    }
}
