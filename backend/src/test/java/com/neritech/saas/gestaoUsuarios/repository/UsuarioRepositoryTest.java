package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.BaseRepositoryTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve encontrar usuário por email e empresaId")
    void deveEncontrarUsuarioPorEmail() {
        // Arrange
        Usuario usuario = TestDataBuilder.umUsuario().build();
        usuarioRepository.save(usuario);

        // Act
        Optional<Usuario> found = usuarioRepository.findByEmailAndEmpresaId(usuario.getEmail(), usuario.getEmpresaId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(usuario.getEmail());
    }

    @Test
    @DisplayName("Deve verificar se email existe na empresa")
    void deveVerificarSeEmailExiste() {
        // Arrange
        Usuario usuario = TestDataBuilder.umUsuario().build();
        usuarioRepository.save(usuario);

        // Act
        boolean exists = usuarioRepository.existsByEmailAndEmpresaId(usuario.getEmail(), usuario.getEmpresaId());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Não deve encontrar usuário de outra empresa")
    void naoDeveEncontrarUsuarioDeOutraEmpresa() {
        // Arrange
        Usuario usuario = TestDataBuilder.umUsuario().comEmpresaId(2L).build();
        usuarioRepository.save(usuario);

        // Act
        Optional<Usuario> found = usuarioRepository.findByEmailAndEmpresaId(usuario.getEmail(), 1L);

        // Assert
        assertThat(found).isEmpty();
    }
}
