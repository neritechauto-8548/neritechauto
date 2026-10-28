package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.BaseRepositoryTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FuncaoRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private FuncaoRepository funcaoRepository;

    @Test
    @DisplayName("Deve encontrar função por nome e empresaId")
    void deveEncontrarFuncaoPorNome() {
        // Arrange
        Funcao funcao = TestDataBuilder.umaFuncao().build();
        funcaoRepository.save(funcao);

        // Act
        Optional<Funcao> found = funcaoRepository.findByNomeAndEmpresaId(funcao.getNome(), funcao.getEmpresaId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo(funcao.getNome());
    }

    @Test
    @DisplayName("Deve listar todas as funções da empresa")
    void deveListarFuncoesDaEmpresa() {
        // Arrange
        Funcao f1 = TestDataBuilder.umaFuncao().comNome("ADMIN").comEmpresaId(1L).build();
        Funcao f2 = TestDataBuilder.umaFuncao().comNome("USER").comEmpresaId(1L).build();
        Funcao f3 = TestDataBuilder.umaFuncao().comNome("ADMIN").comEmpresaId(2L).build();
        
        // Criar cada empresa no seu próprio contexto, sem violar o bloqueio cross-tenant.
        com.neritech.saas.common.tenancy.TenantContext.setCurrentTenant(1L);
        funcaoRepository.saveAllAndFlush(List.of(f1, f2));
        com.neritech.saas.common.tenancy.TenantContext.setCurrentTenant(2L);
        funcaoRepository.saveAndFlush(f3);
        com.neritech.saas.common.tenancy.TenantContext.setCurrentTenant(1L);

        // Act
        List<Funcao> funcoes = funcaoRepository.findAllByEmpresaId(1L);

        // Assert
        assertThat(funcoes).hasSize(2);
        assertThat(funcoes).extracting(Funcao::getNome).containsExactlyInAnyOrder("ADMIN", "USER");
    }
}
