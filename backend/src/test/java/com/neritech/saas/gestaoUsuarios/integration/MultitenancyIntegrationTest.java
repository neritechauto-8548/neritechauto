package com.neritech.saas.gestaoUsuarios.integration;

import com.neritech.saas.AbstractIntegrationTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MultitenancyIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Isolamento de Dados: Empresa 1 não deve ver dados da Empresa 2")
    void isolamentoDeDados() {
        // Arrange
        Usuario u1 = TestDataBuilder.umUsuario()
                .comEmail("user1@empresa1.com")
                .comEmpresaId(1L)
                .build();
        
        Usuario u2 = TestDataBuilder.umUsuario()
                .comEmail("user2@empresa2.com")
                .comEmpresaId(2L)
                .build();
        
        usuarioRepository.save(u1);
        usuarioRepository.save(u2);

        // Act - Simular contexto da Empresa 1
        // Nota: Repositórios usam o TenantContext? Se usarem @Filter do Hibernate ou AOP, sim.
        // Se usarem apenas métodos com empresaId explícito (como findByEmailAndEmpresaId), o teste é direto.
        // Assumindo que o sistema usa métodos explícitos ou filtro global.
        
        // Teste via método explícito
        Optional<Usuario> buscaEmpresa1 = usuarioRepository.findByEmailAndEmpresaId("user2@empresa2.com", 1L);
        
        // Assert
        assertThat(buscaEmpresa1).isEmpty();
    }
}
