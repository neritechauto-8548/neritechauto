package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Permissao;
import com.neritech.saas.gestaoUsuarios.repository.PermissaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PermissaoServiceTest {

    @Mock
    private PermissaoRepository permissaoRepository;

    @InjectMocks
    private PermissaoService permissaoService;

    private Permissao permissao;

    @BeforeEach
    void setUp() {
        permissao = TestDataBuilder.umaPermissao().build();
        TenantContext.setCurrentTenant(1L);
    }

    @Test
    @DisplayName("Deve listar todas as permissões da empresa")
    void deveListarTodasPermissoes() {
        // Arrange
        when(permissaoRepository.findAllByEmpresaId(anyLong())).thenReturn(List.of(permissao));

        // Act
        List<Permissao> permissoes = permissaoService.findAll();

        // Assert
        assertThat(permissoes).hasSize(1);
        assertThat(permissoes.get(0).getNome()).isEqualTo("USUARIO.READ");
    }
}
