package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private FuncaoRepository funcaoRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository assinaturaEmpresaRepository;
    @Mock
    private com.neritech.saas.empresa.service.StripeService stripeService;
    @Mock
    private com.neritech.saas.empresa.repository.EmpresaRepository empresaRepository;
    @Mock
    private com.neritech.saas.gestaoUsuarios.repository.PermissaoRepository permissaoRepository;
    @Mock
    private com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository sessaoUsuarioRepository;
    @Mock
    private com.neritech.saas.gestaoUsuarios.repository.TokenRecuperacaoSenhaRepository tokenRepository;
    @Mock
    private com.neritech.saas.common.mail.EmailService emailService;
    @Mock
    private org.springframework.jdbc.core.JdbcTemplate jdbc;
    @Mock
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequest usuarioRequest;

    @BeforeEach
    void setUp() {
        usuario = TestDataBuilder.umUsuario().comId(1L).build();
        usuarioRequest = TestDataBuilder.umUsuarioRequest().build();
        TenantContext.setCurrentTenant(1L);
        var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                usuario.getEmail(), "n/a", java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN")));
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        lenient().when(usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(usuario.getEmail(), 1L))
                .thenReturn(Optional.of(usuario));
    }

    @Test
    @DisplayName("Deve pesquisar sem enviar parâmetro textual nulo ao banco")
    void devePesquisarComFiltroVazioQuandoQueryAusente() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(usuarioRepository.searchByTenant(1L, "", null, pageable)).thenReturn(Page.empty(pageable));

        usuarioService.search(null, null, pageable);

        verify(usuarioRepository).searchByTenant(1L, "", null, pageable);
    }

    @Nested
    @DisplayName("Create")
    class CreateTests {

        @Test
        @DisplayName("Deve criar usuário com sucesso")
        void deveCriarUsuarioComSucesso() {
            // Arrange
            when(usuarioRepository.existsByEmailIgnoreCaseAndEmpresaId(anyString(), eq(1L))).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
            
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
            when(usuarioRepository.existsByEmailIgnoreCaseAndEmpresaId(anyString(), eq(1L))).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.create(usuarioRequest))
                    .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                    .hasMessageContaining("E-mail já cadastrado");
            
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }
    }

    @Nested
    @DisplayName("FindById")
    class FindByIdTests {

        @Test
        @DisplayName("Deve liberar o teste antes do vencimento")
        void deveLiberarTesteVigente() {
            AssinaturaEmpresa assinatura = new AssinaturaEmpresa();
            assinatura.setStatus(StatusAssinatura.TESTE);
            assinatura.setTrialEndsAt(LocalDateTime.now().plusDays(1));
            when(usuarioRepository.findByIdAndEmpresaId(1L, 1L)).thenReturn(Optional.of(usuario));
            when(assinaturaEmpresaRepository.findFirstByEmpresaIdOrderByDataFimDesc(1L))
                    .thenReturn(Optional.of(assinatura));

            UsuarioResponse response = usuarioService.findById(1L);

            assertThat(response.isAssinaturaAtiva()).isTrue();
            assertThat(response.getSubscriptionStatus()).isEqualTo(StatusAssinatura.TESTE);
        }

        @Test
        @DisplayName("Deve encerrar o acesso quando o teste vence")
        void deveBloquearTesteVencido() {
            AssinaturaEmpresa assinatura = new AssinaturaEmpresa();
            assinatura.setStatus(StatusAssinatura.TESTE);
            assinatura.setTrialEndsAt(LocalDateTime.now().minusSeconds(1));
            when(usuarioRepository.findByIdAndEmpresaId(1L, 1L)).thenReturn(Optional.of(usuario));
            when(assinaturaEmpresaRepository.findFirstByEmpresaIdOrderByDataFimDesc(1L))
                    .thenReturn(Optional.of(assinatura));

            UsuarioResponse response = usuarioService.findById(1L);

            assertThat(response.isAssinaturaAtiva()).isFalse();
            assertThat(response.getSubscriptionStatus()).isEqualTo(StatusAssinatura.INATIVO);
        }

        @Test
        @DisplayName("Deve retornar usuário quando id existe")
        void deveRetornarUsuarioQuandoIdExiste() {
            // Arrange
            when(usuarioRepository.findByIdAndEmpresaId(1L, 1L)).thenReturn(Optional.of(usuario));
            
            UsuarioResponse expectedResponse = new UsuarioResponse();
            expectedResponse.setId(1L);
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
