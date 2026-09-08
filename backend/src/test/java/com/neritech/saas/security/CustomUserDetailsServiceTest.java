package com.neritech.saas.security;

import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.domain.Permissao;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    @DisplayName("Deve carregar usuário por username (email)")
    void deveCarregarUsuarioPorUsername() {
        Usuario usuario = TestDataBuilder.umUsuario().build();
        when(usuarioRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@email.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(usuario.getEmail());
        assertThat(userDetails.getPassword()).isEqualTo(usuario.getSenha());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("notfound@email.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("Nome de função contendo ADMIN não deve promover para ROLE_ADMIN")
    void nomeDeFuncaoComAdminNaoDevePromoverAutoridade() {
        Permissao permissao = Permissao.builder()
                .chave("cliente.visualizar")
                .valor("GERAL_USUARIO")
                .build();
        Funcao funcao = Funcao.builder()
                .nome("ADMINISTRADOR_FILIAL")
                .ativo(true)
                .empresaId(10L)
                .permissoes(Set.of(permissao))
                .build();
        Usuario usuario = Usuario.builder()
                .nomeCompleto("Gestor da filial")
                .email("gestor@oficina.com.br")
                .senha("senha")
                .empresaId(10L)
                .ativo(true)
                .bloqueado(false)
                .funcoes(Set.of(funcao))
                .build();
        when(usuarioRepository.findByEmailIgnoreCase(usuario.getEmail())).thenReturn(Optional.of(usuario));

        UserDetails details = userDetailsService.loadUserByUsername(usuario.getEmail());

        assertThat(details.getAuthorities())
                .extracting(authority -> authority.getAuthority())
                .contains("ROLE_ADMINISTRADOR_FILIAL", "GERAL_USUARIO")
                .doesNotContain("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Função inativa não deve conceder role nem permissão")
    void funcaoInativaNaoDeveConcederAutoridade() {
        Permissao permissao = Permissao.builder()
                .chave("cliente.criar")
                .valor("CLIENTE_CRIAR")
                .build();
        Funcao funcao = Funcao.builder()
                .nome("ATENDENTE")
                .ativo(false)
                .empresaId(10L)
                .permissoes(Set.of(permissao))
                .build();
        Usuario usuario = Usuario.builder()
                .nomeCompleto("Atendente")
                .email("atendente@oficina.com.br")
                .senha("senha")
                .empresaId(10L)
                .ativo(true)
                .bloqueado(false)
                .funcoes(Set.of(funcao))
                .build();
        when(usuarioRepository.findByEmailIgnoreCase(usuario.getEmail())).thenReturn(Optional.of(usuario));

        UserDetails details = userDetailsService.loadUserByUsername(usuario.getEmail());

        assertThat(details.getAuthorities()).isEmpty();
    }
}
