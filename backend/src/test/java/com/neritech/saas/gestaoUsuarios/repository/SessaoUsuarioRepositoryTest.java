package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.BaseRepositoryTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.SessaoUsuario;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SessaoUsuarioRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private SessaoUsuarioRepository sessaoUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve encontrar sessão por refresh token")
    void deveEncontrarSessaoPorRefreshToken() {
        // Arrange
        Usuario usuario = TestDataBuilder.umUsuario().build();
        usuarioRepository.save(usuario);

        SessaoUsuario sessao = new SessaoUsuario();
        sessao.setUsuario(usuario);
        sessao.setRefreshToken("refresh.token");
        sessao.setIpAddress("127.0.0.1");
        sessao.setUserAgent("TestAgent");
        sessao.setDataExpiracao(LocalDateTime.now().plusHours(1));
        sessao.setEmpresaId(usuario.getEmpresaId());
        
        sessaoUsuarioRepository.save(sessao);

        // Act
        Optional<SessaoUsuario> found = sessaoUsuarioRepository.findByRefreshToken("refresh.token");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUsuario().getId()).isEqualTo(usuario.getId());
    }
}
