package com.neritech.saas.empresa.controller;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.service.EmpresaService;
import com.neritech.saas.empresa.service.StripeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssinaturaControllerTrialTest {

    @Mock StripeService stripeService;
    @Mock EmpresaService empresaService;
    @InjectMocks AssinaturaController controller;

    @AfterEach
    void clearTenant() {
        TenantContext.clear();
    }

    @Test
    void showsLocalTrialWithoutCallingStripe() {
        TenantContext.setCurrentTenant(7L);
        Empresa empresa = new Empresa();
        PlanoAssinatura plano = new PlanoAssinatura();
        plano.setNome("NeriTech Pro");
        AssinaturaEmpresa assinatura = new AssinaturaEmpresa();
        assinatura.setPlano(plano);
        assinatura.setStatus(StatusAssinatura.TESTE);
        assinatura.setValorMensal(new BigDecimal("79.90"));
        assinatura.setDataInicio(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
        assinatura.setDataFim(assinatura.getDataInicio().plusDays(14));
        assinatura.setTrialEndsAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).plusDays(14));
        when(empresaService.findById(7L)).thenReturn(empresa);
        when(empresaService.findActiveSubscriptionByEmpresa(7L)).thenReturn(Optional.of(assinatura));

        var response = controller.getStatus(7L).getBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("TESTE");
        assertThat(response.isTrial()).isTrue();
        assertThat(response.getDiasRestantesTrial()).isEqualTo(14);
        verifyNoInteractions(stripeService);
    }
}
