package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.BaseRepositoryTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.Permissao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PermissaoRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Test
    @DisplayName("Deve listar todas as permissões da empresa")
    void deveListarPermissoesDaEmpresa() {
        // Arrange
        Permissao p1 = TestDataBuilder.umaPermissao().comNome("P1").comEmpresaId(1L).build();
        Permissao p2 = TestDataBuilder.umaPermissao().comNome("P2").comEmpresaId(1L).build();
        Permissao p3 = TestDataBuilder.umaPermissao().comNome("P3").comEmpresaId(2L).build();
        
        permissaoRepository.saveAll(List.of(p1, p2, p3));

        // Act
        List<Permissao> permissoes = permissaoRepository.findAllByEmpresaId(1L);

        // Assert
        assertThat(permissoes).hasSize(2);
        assertThat(permissoes).extracting(Permissao::getNome).containsExactlyInAnyOrder("P1", "P2");
    }
}
