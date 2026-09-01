package com.neritech.saas.security;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanLimitServiceTest {

    @Mock
    private PlanAccessService planAccessService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    private PlanLimitService service;

    @BeforeEach
    void setUp() {
        service = new PlanLimitService(planAccessService, usuarioRepository, empresaRepository);
        TenantContext.setCurrentTenant(10L);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("Deve permitir novo usuário quando ainda há vaga no plano")
    void devePermitirQuandoHaVaga() {
        PlanoAssinatura plano = plano("NeriTech Pro", 5);
        when(empresaRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(new Empresa()));
        when(planAccessService.getAccessiblePlan()).thenReturn(Optional.of(plano));
        when(usuarioRepository.countByEmpresaIdAndAtivoTrue(10L)).thenReturn(4L);

        assertThatCode(service::assertCanActivateUser).doesNotThrowAnyException();

        verify(empresaRepository).findByIdForUpdate(10L);
        verify(usuarioRepository).countByEmpresaIdAndAtivoTrue(10L);
    }

    @Test
    @DisplayName("Deve bloquear quando o número de usuários ativos atingir o limite")
    void deveBloquearQuandoLimiteAtingido() {
        PlanoAssinatura plano = plano("NeriTech Pro", 5);
        when(empresaRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(new Empresa()));
        when(planAccessService.getAccessiblePlan()).thenReturn(Optional.of(plano));
        when(usuarioRepository.countByEmpresaIdAndAtivoTrue(10L)).thenReturn(5L);

        assertThatThrownBy(service::assertCanActivateUser)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Limite de usuários ativos")
                .hasMessageContaining("NeriTech Pro")
                .hasMessageContaining("5");
    }

    @Test
    @DisplayName("Limite nulo deve ser tratado como ilimitado para compatibilidade")
    void limiteNuloDeveSerIlimitado() {
        PlanoAssinatura plano = plano("Legado", null);
        when(empresaRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(new Empresa()));
        when(planAccessService.getAccessiblePlan()).thenReturn(Optional.of(plano));

        assertThatCode(service::assertCanActivateUser).doesNotThrowAnyException();

        verify(usuarioRepository, never()).countByEmpresaIdAndAtivoTrue(10L);
    }

    @Test
    @DisplayName("Sem assinatura comercial não deve permitir ativar novo usuário")
    void semAssinaturaNaoDevePermitir() {
        when(empresaRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(new Empresa()));
        when(planAccessService.getAccessiblePlan()).thenReturn(Optional.empty());

        assertThatThrownBy(service::assertCanActivateUser)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("assinatura ativa");

        verify(usuarioRepository, never()).countByEmpresaIdAndAtivoTrue(10L);
    }

    @Test
    @DisplayName("Sem tenant deve falhar antes de consultar banco")
    void semTenantDeveFalharAntesDoBanco() {
        TenantContext.clear();

        assertThatThrownBy(service::assertCanActivateUser)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Empresa não identificada");

        verifyNoInteractions(empresaRepository, planAccessService, usuarioRepository);
    }

    private PlanoAssinatura plano(String nome, Integer maxUsuarios) {
        PlanoAssinatura plano = new PlanoAssinatura();
        plano.setNome(nome);
        plano.setMaxUsuarios(maxUsuarios);
        return plano;
    }
}
