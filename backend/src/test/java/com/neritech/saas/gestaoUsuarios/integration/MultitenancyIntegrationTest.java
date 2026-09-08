package com.neritech.saas.gestaoUsuarios.integration;

import com.neritech.saas.AbstractIntegrationTest;
import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
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
        TenantContext.clear();
        usuarioRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("Empresa 1 nao deve localizar usuario da Empresa 2 no escopo da Empresa 1")
    void isolamentoDeDados() {
        TenantContext.setCurrentTenant(1L);
        Usuario u1 = TestDataBuilder.umUsuario()
                .comEmail("user1@empresa1.com")
                .comEmpresaId(1L)
                .build();
        usuarioRepository.saveAndFlush(u1);

        TenantContext.setCurrentTenant(2L);
        Usuario u2 = TestDataBuilder.umUsuario()
                .comEmail("user2@empresa2.com")
                .comEmpresaId(2L)
                .build();
        usuarioRepository.saveAndFlush(u2);

        TenantContext.setCurrentTenant(1L);
        Optional<Usuario> buscaEmpresa1 = usuarioRepository
                .findByEmailIgnoreCaseAndEmpresaId("user2@empresa2.com", 1L);

        assertThat(buscaEmpresa1).isEmpty();
    }

    @Test
    @DisplayName("Entidade nao pode ser criada para tenant diferente do contexto autenticado")
    void deveBloquearPersistenciaCrossTenant() {
        TenantContext.setCurrentTenant(1L);
        Usuario invasor = TestDataBuilder.umUsuario()
                .comEmail("cross@tenant.com")
                .comEmpresaId(2L)
                .build();

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> usuarioRepository.saveAndFlush(invasor))
                .isInstanceOf(Exception.class)
                .hasRootCauseInstanceOf(IllegalStateException.class);
    }
}
