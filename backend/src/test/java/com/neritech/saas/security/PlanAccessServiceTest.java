package com.neritech.saas.security;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanAccessServiceTest {

    @Mock
    private AssinaturaEmpresaRepository assinaturaEmpresaRepository;

    private PlanAccessService service;

    @BeforeEach
    void setUp() {
        service = new PlanAccessService(assinaturaEmpresaRepository);
        TenantContext.setCurrentTenant(10L);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("Plano Pro sem integração NFe não deve acessar fiscal")
    void planoProNaoDeveAcessarFiscal() {
        PlanoAssinatura plano = plano(1, false);
        AssinaturaEmpresa assinatura = assinatura(StatusAssinatura.ATIVO, plano);

        when(assinaturaEmpresaRepository.findFirstByEmpresaIdAndStatusInOrderByDataFimDesc(eq(10L), any()))
                .thenReturn(Optional.of(assinatura));

        assertThat(service.hasCommercialAccess()).isTrue();
        assertThat(service.hasFiscalAccess()).isFalse();
    }

    @Test
    @DisplayName("Plano Ultra nível 2 deve acessar fiscal")
    void planoUltraDeveAcessarFiscal() {
        PlanoAssinatura plano = plano(2, false);
        AssinaturaEmpresa assinatura = assinatura(StatusAssinatura.ATIVO, plano);

        when(assinaturaEmpresaRepository.findFirstByEmpresaIdAndStatusInOrderByDataFimDesc(eq(10L), any()))
                .thenReturn(Optional.of(assinatura));

        assertThat(service.hasCommercialAccess()).isTrue();
        assertThat(service.hasFiscalAccess()).isTrue();
        assertThat(service.hasLevel(2)).isTrue();
    }

    @Test
    @DisplayName("Flag explícita de NFe deve liberar fiscal independentemente do nível")
    void flagNfeDeveLiberarFiscal() {
        PlanoAssinatura plano = plano(1, true);
        AssinaturaEmpresa assinatura = assinatura(StatusAssinatura.ATIVO, plano);

        when(assinaturaEmpresaRepository.findFirstByEmpresaIdAndStatusInOrderByDataFimDesc(eq(10L), any()))
                .thenReturn(Optional.of(assinatura));

        assertThat(service.hasFiscalAccess()).isTrue();
    }

    @Test
    @DisplayName("Trial expirado não deve liberar recursos do plano")
    void trialExpiradoNaoDeveLiberarAcesso() {
        PlanoAssinatura plano = plano(2, true);
        AssinaturaEmpresa assinatura = assinatura(StatusAssinatura.TESTE, plano);
        assinatura.setTrialEndsAt(LocalDateTime.now().minusMinutes(1));

        when(assinaturaEmpresaRepository.findFirstByEmpresaIdAndStatusInOrderByDataFimDesc(eq(10L), any()))
                .thenReturn(Optional.of(assinatura));

        assertThat(service.hasCommercialAccess()).isFalse();
        assertThat(service.hasFiscalAccess()).isFalse();
    }

    @Test
    @DisplayName("Assinatura atrasada só deve funcionar durante a carência")
    void assinaturaAtrasadaDeveRespeitarCarencia() {
        PlanoAssinatura plano = plano(2, true);
        AssinaturaEmpresa assinatura = assinatura(StatusAssinatura.ATRASADO, plano);
        assinatura.setGracePeriodEndsAt(LocalDateTime.now().plusHours(1));

        when(assinaturaEmpresaRepository.findFirstByEmpresaIdAndStatusInOrderByDataFimDesc(eq(10L), any()))
                .thenReturn(Optional.of(assinatura));

        assertThat(service.hasCommercialAccess()).isTrue();
        assertThat(service.hasFiscalAccess()).isTrue();

        assinatura.setGracePeriodEndsAt(LocalDateTime.now().minusMinutes(1));
        assertThat(service.hasCommercialAccess()).isFalse();
        assertThat(service.hasFiscalAccess()).isFalse();
    }

    @Test
    @DisplayName("Sem tenant não deve consultar assinatura nem liberar acesso")
    void semTenantNaoDeveLiberarAcesso() {
        TenantContext.clear();
        assertThat(service.hasCommercialAccess()).isFalse();
        assertThat(service.hasFiscalAccess()).isFalse();
        assertThat(service.hasLevel(2)).isFalse();
    }

    private PlanoAssinatura plano(int nivel, boolean possuiNfe) {
        PlanoAssinatura plano = new PlanoAssinatura();
        plano.setNivel(nivel);
        plano.setPossuiIntegracaoNfe(possuiNfe);
        return plano;
    }

    private AssinaturaEmpresa assinatura(StatusAssinatura status, PlanoAssinatura plano) {
        AssinaturaEmpresa assinatura = new AssinaturaEmpresa();
        assinatura.setStatus(status);
        assinatura.setPlano(plano);
        return assinatura;
    }
}
