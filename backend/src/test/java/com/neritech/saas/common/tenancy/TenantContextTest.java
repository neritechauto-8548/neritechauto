package com.neritech.saas.common.tenancy;

import org.junit.jupiter.api.AfterEach;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TenantContextTest {

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("Deve definir e recuperar tenantId")
    void deveDefinirERecuperarTenantId() {
        // Arrange
        Long tenantId = 123L;

        // Act
        TenantContext.setCurrentTenant(tenantId);

        // Assert
        assertThat(TenantContext.getCurrentTenant()).isEqualTo(tenantId);
    }

    @Test
    @DisplayName("Deve limpar tenantId")
    void deveLimparTenantId() {
        // Arrange
        TenantContext.setCurrentTenant(1L);

        // Act
        TenantContext.clear();

        // Assert
        assertThat(TenantContext.getCurrentTenant()).isNull();
    }
    @Test
    @DisplayName("Impede criar registro em empresa diferente da autenticada")
    void deveBloquearCriacaoEmEmpresaDiferente() {
        TenantContext.setCurrentTenant(1L);
        Usuario usuario = new Usuario();
        usuario.setEmpresaId(2L);

        assertThatThrownBy(usuario::prePersistTenant)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("outra empresa");
    }

    @Test
    @DisplayName("Atribui empresa autenticada quando registro não informa empresa")
    void deveAtribuirEmpresaDoContexto() {
        TenantContext.setCurrentTenant(1L);
        Usuario usuario = new Usuario();

        usuario.prePersistTenant();

        assertThat(usuario.getEmpresaId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Permite criar registro com empresa correspondente à autenticada")
    void devePermitirEmpresaDoContexto() {
        TenantContext.setCurrentTenant(1L);
        Usuario usuario = new Usuario();
        usuario.setEmpresaId(1L);

        usuario.prePersistTenant();

        assertThat(usuario.getEmpresaId()).isEqualTo(1L);
    }

}
