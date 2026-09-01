package com.neritech.saas.trial.service;

import com.neritech.saas.common.mail.EmailService;
import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import com.neritech.saas.empresa.service.EmpresaService;
import com.neritech.saas.empresa.service.StripeService;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import com.neritech.saas.trial.dto.TrialRegisterRequest;
import com.neritech.saas.trial.dto.TrialRegisterResponse;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrialServiceTest {

    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Sao_Paulo");

    @Mock
    private EmpresaService empresaService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private StripeService stripeService;
    @Mock
    private EmailService emailService;
    @Mock
    private FuncionarioRepository funcionarioRepository;
    @Mock
    private FuncaoRepository funcaoRepository;
    @Mock
    private AssinaturaEmpresaRepository assinaturaEmpresaRepository;

    @InjectMocks
    private TrialService trialService;

    private TrialRegisterRequest request;
    private Empresa savedEmpresa;
    private AssinaturaEmpresa localTrial;

    @BeforeEach
    void setUp() {
        request = new TrialRegisterRequest();
        request.setNomeCompleto("Cliente Teste");
        request.setEmail("cliente@teste.com.br");
        request.setTelefone("81999999999");
        request.setNomeEmpresa("Oficina Teste");
        request.setCnpjOuCpf("52998224725");
        request.setSegmento("OFICINA");

        savedEmpresa = new Empresa();
        savedEmpresa.setId(10L);
        savedEmpresa.setNomeFantasia("Oficina Teste");
        savedEmpresa.setRazaoSocial("Oficina Teste");

        localTrial = new AssinaturaEmpresa();
        localTrial.setEmpresa(savedEmpresa);
        localTrial.setStatus(StatusAssinatura.ATIVO);
        localTrial.setDataInicio(LocalDate.now(BUSINESS_ZONE));
        localTrial.setDataFim(LocalDate.now(BUSINESS_ZONE).plusDays(15));

        when(usuarioRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
        when(empresaService.create(any(Empresa.class))).thenReturn(savedEmpresa);
        when(assinaturaEmpresaRepository.findFirstByEmpresaIdOrderByDataFimDesc(10L))
                .thenReturn(Optional.of(localTrial));
        when(assinaturaEmpresaRepository.save(any(AssinaturaEmpresa.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encoded-password");
        when(funcaoRepository.save(any(Funcao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario savedUsuario = mock(Usuario.class);
        when(savedUsuario.getId()).thenReturn(20L);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(savedUsuario);
        when(funcionarioRepository.save(any(Funcionario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void deveCriarEmpresaAntesDaAssinaturaStripeEVincularOMesmoTrialLocal() throws Exception {
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn("cus_trial_1");
        when(stripeService.createCustomer(eq(request.getEmail()), eq(request.getNomeCompleto()), eq(request.getTelefone())))
                .thenReturn(customer);

        long stripeTrialEnd = Instant.parse("2030-01-01T12:00:00Z").getEpochSecond();
        Subscription stripeSubscription = mock(Subscription.class);
        when(stripeSubscription.getId()).thenReturn("sub_trial_1");
        when(stripeSubscription.getCustomer()).thenReturn("cus_trial_1");
        when(stripeSubscription.getStatus()).thenReturn("trialing");
        when(stripeSubscription.getTrialEnd()).thenReturn(stripeTrialEnd);
        when(stripeSubscription.getCurrentPeriodEnd()).thenReturn(stripeTrialEnd);
        when(stripeSubscription.getItems()).thenReturn(null);
        when(stripeService.createTrialSubscription("cus_trial_1")).thenReturn(stripeSubscription);

        TrialRegisterResponse response = trialService.registerTrial(request);

        InOrder order = inOrder(stripeService, empresaService);
        order.verify(stripeService).createCustomer(request.getEmail(), request.getNomeCompleto(), request.getTelefone());
        order.verify(empresaService).create(any(Empresa.class));
        order.verify(stripeService).createTrialSubscription("cus_trial_1");

        assertTrue(response.isSuccess());
        assertEquals(StatusAssinatura.TESTE, localTrial.getStatus());
        assertEquals("cus_trial_1", localTrial.getStripeCustomerId());
        assertEquals("sub_trial_1", localTrial.getStripeSubscriptionId());
        assertNotNull(localTrial.getTrialEndsAt());
        assertEquals(
                Instant.ofEpochSecond(stripeTrialEnd).atZone(BUSINESS_ZONE).toLocalDate(),
                localTrial.getDataFim()
        );
    }

    @Test
    void deveManterTrialLocalDe180DiasQuandoStripeFalhar() throws Exception {
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn("cus_trial_2");
        when(stripeService.createCustomer(eq(request.getEmail()), eq(request.getNomeCompleto()), eq(request.getTelefone())))
                .thenReturn(customer);
        when(stripeService.createTrialSubscription("cus_trial_2"))
                .thenThrow(new RuntimeException("Stripe indisponível"));

        TrialRegisterResponse response = trialService.registerTrial(request);

        assertTrue(response.isSuccess());
        assertEquals(StatusAssinatura.TESTE, localTrial.getStatus());
        assertEquals("cus_trial_2", localTrial.getStripeCustomerId());
        assertNull(localTrial.getStripeSubscriptionId());
        assertNotNull(localTrial.getTrialEndsAt());
        assertEquals(LocalDate.now(BUSINESS_ZONE).plusDays(180), localTrial.getDataFim());
    }
}
