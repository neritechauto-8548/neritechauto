package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(1L);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve criar usuário sempre no tenant autenticado")
    void deveCriarUsuarioNoTenantAutenticado() {
        UsuarioRequest request = TestDataBuilder.umUsuarioRequest().build();
        when(usuarioRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getSenha())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario salvo = invocation.getArgument(0);
            salvo.setId(99L);
            return salvo;
        });
        when(assinaturaEmpresaRepository.findFirstByEmpresaIdOrderByDataFimDesc(1L))
                .thenReturn(Optional.empty());

        UsuarioResponse response = usuarioService.create(request);

        assertThat(response.getId()).isEqualTo(99L);
        assertThat(response.getEmpresaId()).isEqualTo(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve rejeitar função que não pertença integralmente ao tenant autenticado")
    void deveRejeitarFuncaoDeOutroTenant() {
        UsuarioRequest request = TestDataBuilder.umUsuarioRequest().build();
        request.setFuncoesIds(Set.of(10L, 20L));

        Funcao funcaoDaEmpresaAtual = TestDataBuilder.umaFuncao()
                .comId(10L)
                .comEmpresaId(1L)
                .build();

        when(usuarioRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getSenha())).thenReturn("encodedPassword");
        when(funcaoRepository.findAllByIdInAndEmpresaId(request.getFuncoesIds(), 1L))
                .thenReturn(List.of(funcaoDaEmpresaAtual));

        assertThatThrownBy(() -> usuarioService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("não pertencem à empresa autenticada");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Busca por ID nunca deve sair do tenant atual")
    void findByIdDeveRespeitarTenantAtual() {
        Usuario usuario = TestDataBuilder.umUsuario().comEmpresaId(1L).build();
        when(usuarioRepository.findByIdAndEmpresaId(1L, 1L)).thenReturn(Optional.of(usuario));
        when(assinaturaEmpresaRepository.findFirstByEmpresaIdOrderByDataFimDesc(1L))
                .thenReturn(Optional.empty());

        UsuarioResponse response = usuarioService.findById(1L);

        assertThat(response.getEmpresaId()).isEqualTo(1L);
        verify(usuarioRepository).findByIdAndEmpresaId(1L, 1L);
    }

    @Test
    @DisplayName("Usuário atual deve ser carregado por email e tenant autenticado")
    void currentUserDeveUsarEmailETenant() {
        String email = "joao.silva@test.com";
        Usuario usuario = TestDataBuilder.umUsuario()
                .comEmpresaId(1L)
                .comEmail(email)
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null, List.of()));

        when(usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(email, 1L))
                .thenReturn(Optional.of(usuario));
        when(assinaturaEmpresaRepository.findFirstByEmpresaIdOrderByDataFimDesc(1L))
                .thenReturn(Optional.empty());

        UsuarioResponse response = usuarioService.getCurrentUser();

        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getEmpresaId()).isEqualTo(1L);
        verify(usuarioRepository).findByEmailIgnoreCaseAndEmpresaId(email, 1L);
    }

    @Test
    @DisplayName("Permissões retornadas devem vir apenas de funções ativas persistidas")
    void deveRetornarSomentePermissoesPersistidasDeFuncoesAtivas() {
        var permissao = TestDataBuilder.umaPermissao()
                .comValor("CLIENTE_EDITAR")
                .build();
        Funcao funcao = TestDataBuilder.umaFuncao()
                .comEmpresaId(1L)
                .comPermissao(permissao)
                .build();
        Usuario usuario = TestDataBuilder.umUsuario()
                .comEmpresaId(1L)
                .comFuncao(funcao)
                .build();

        when(usuarioRepository.findByIdAndEmpresaId(1L, 1L)).thenReturn(Optional.of(usuario));
        when(assinaturaEmpresaRepository.findFirstByEmpresaIdOrderByDataFimDesc(1L))
                .thenReturn(Optional.empty());

        UsuarioResponse response = usuarioService.findById(1L);

        assertThat(response.getPermissions()).containsExactly("CLIENTE_EDITAR");
    }

    @Test
    @DisplayName("Deve falhar quando contexto de tenant não existir")
    void deveFalharSemTenant() {
        TenantContext.clear();

        assertThatThrownBy(() -> usuarioService.findAll())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("empresa autenticada");

        verify(usuarioRepository, never()).findAllByEmpresaId(any());
    }
}
