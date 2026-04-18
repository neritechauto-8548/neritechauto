package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuncaoServiceTest {

    @Mock
    private FuncaoRepository funcaoRepository;

    @InjectMocks
    private FuncaoService funcaoService;

    private Funcao funcao;

    @BeforeEach
    void setUp() {
        funcao = TestDataBuilder.umaFuncao().build();
        TenantContext.setCurrentTenant(1L);
    }

    @Test
    @DisplayName("Deve listar todas as funções da empresa")
    void deveListarTodasFuncoes() {
        // Arrange
        when(funcaoRepository.findAllByEmpresaId(anyLong())).thenReturn(List.of(funcao));

        // Act
        List<Funcao> funcoes = funcaoService.findAll();

        // Assert
        assertThat(funcoes).hasSize(1);
        assertThat(funcoes.get(0).getNome()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Deve buscar função por nome")
    void deveBuscarFuncaoPorNome() {
        // Arrange
        when(funcaoRepository.findByNomeAndEmpresaId("ADMIN", 1L)).thenReturn(Optional.of(funcao));

        // Act
        Optional<Funcao> found = funcaoService.findByNome("ADMIN");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo("ADMIN");
    }
}
