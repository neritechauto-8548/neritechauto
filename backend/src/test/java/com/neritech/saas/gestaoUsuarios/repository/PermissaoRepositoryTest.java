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
    @DisplayName("Deve listar todas as permissões")
    void deveListarTodasPermissoes() {
        // Arrange
        Permissao p1 = TestDataBuilder.umaPermissao().comChave("P1").comValor("VALOR_P1").build();
        Permissao p2 = TestDataBuilder.umaPermissao().comChave("P2").comValor("VALOR_P2").build();
        
        permissaoRepository.saveAll(List.of(p1, p2));

        // Act
        List<Permissao> permissoes = permissaoRepository.findAll();

        // Assert
        assertThat(permissoes).hasSizeGreaterThanOrEqualTo(2);
        assertThat(permissoes).extracting(Permissao::getChave).contains("P1", "P2");
    }

    @Test
    @DisplayName("Deve encontrar permissão por valor")
    void deveEncontrarPorValor() {
        // Arrange
        Permissao p = TestDataBuilder.umaPermissao().comValor("BUSCA_VALOR").build();
        permissaoRepository.save(p);

        // Act
        java.util.Optional<Permissao> found = permissaoRepository.findByValor("BUSCA_VALOR");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getValor()).isEqualTo("BUSCA_VALOR");
    }
}
