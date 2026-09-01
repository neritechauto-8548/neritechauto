package com.neritech.saas.trial.service;

import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import com.neritech.saas.empresa.repository.PlanoAssinaturaRepository;
import com.neritech.saas.empresa.service.StripeService;
import com.stripe.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrialSubscriptionServiceTest {

    @Mock private AssinaturaEmpresaRepository assinaturaEmpresaRepository;
    @Mock private PlanoAssinaturaRepository planoAssinaturaRepository;
    @Mock private StripeService stripeService;

    private TrialSubscriptionService service;
    private Empresa empresa;
    private PlanoAssinatura planoPro;

    @BeforeEach
    void setUp() {
        service = new TrialSubscriptionService(assinaturaEmpresaRepository, planoAssinaturaRepository, stripeService);

        empresa = new Empresa();
        empresa.setId(10L);

        planoPro = new PlanoAssinatura();
        planoPro.setId(1L);
        planoPro.setNome("NeriTech Pro");
        planoPro.setNivel(1);
        planoPro.setAtivo(true);
        planoPro.setPrecoMensal(new BigDecimal("79.90"));

        lenient().when(assinaturaEmpresaRepository.save(any(AssinaturaEmpresa.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldCreateLocalTrialWhenStripeSubscriptionIsUnavailable() {
        when(planoAssinaturaRepository.findFirstByNivelAndAtivoTrueOrderByIdAsc(1))
                .thenReturn(Optional.of(planoPro));

        AssinaturaEmpresa result = service.ensureTrialSubscription(empresa, null, null);

        assertSame(empresa, result.getEmpresa());
        assertSame(planoPro, result.getPlano());
        assertEquals(StatusAssinatura.TESTE, result.getStatus());
        assertEquals(new BigDecimal("79.90"), result.getValorMensal());
        assertNotNull(result.getTrialEndsAt());
        assertEquals(result.getTrialEndsAt(), result.getSubscriptionEndsAt());
        assertEquals(LocalDate.now(ZoneId.of("America/Sao_Paulo")), result.getDataInicio());
        assertTrue(result.getDataFim().isAfter(result.getDataInicio()));
    }

    @Test
    void shouldPersistStripeTrialUsingDefaultPlanWhenProductCannotBeResolved() {
        Subscription stripeSubscription = mock(Subscription.class);
        long now = Instant.now().getEpochSecond();
        long trialEnd = Instant.now().plusSeconds(180L * 24 * 60 * 60).getEpochSecond();

        when(stripeSubscription.getId()).thenReturn("sub_trial_123");
        when(stripeSubscription.getStatus()).thenReturn("trialing");
        when(stripeSubscription.getCurrentPeriodStart()).thenReturn(now);
        when(stripeSubscription.getCurrentPeriodEnd()).thenReturn(trialEnd);
        when(stripeSubscription.getTrialEnd()).thenReturn(trialEnd);
        when(assinaturaEmpresaRepository.findByStripeSubscriptionId("sub_trial_123"))
                .thenReturn(Optional.empty());
        when(planoAssinaturaRepository.findFirstByNivelAndAtivoTrueOrderByIdAsc(1))
                .thenReturn(Optional.of(planoPro));

        AssinaturaEmpresa result = service.ensureTrialSubscription(empresa, "cus_123", stripeSubscription);

        assertEquals("sub_trial_123", result.getStripeSubscriptionId());
        assertEquals("cus_123", result.getStripeCustomerId());
        assertEquals(StatusAssinatura.TESTE, result.getStatus());
        assertSame(planoPro, result.getPlano());
        assertNotNull(result.getTrialEndsAt());
    }

    @Test
    void shouldFailFastWhenDefaultTrialPlanDoesNotExist() {
        when(planoAssinaturaRepository.findFirstByNivelAndAtivoTrueOrderByIdAsc(1))
                .thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.ensureTrialSubscription(empresa, null, null)
        );

        assertTrue(exception.getMessage().contains("Plano padrão de trial"));
        verify(assinaturaEmpresaRepository, never()).save(any());
    }

    @Test
    void shouldRejectNonPersistedCompany() {
        Empresa transientEmpresa = new Empresa();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.ensureTrialSubscription(transientEmpresa, null, null)
        );

        assertTrue(exception.getMessage().contains("Empresa persistida"));
        verifyNoInteractions(planoAssinaturaRepository);
        verify(assinaturaEmpresaRepository, never()).save(any());
    }
}
