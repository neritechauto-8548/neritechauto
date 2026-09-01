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
import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import com.neritech.saas.trial.dto.TrialRegisterRequest;
import com.neritech.saas.trial.dto.TrialRegisterResponse;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrialService {

    private static final int TRIAL_DAYS = 180;
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Sao_Paulo");

    private final EmpresaService empresaService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final StripeService stripeService;
    private final EmailService emailService;
    private final FuncionarioRepository funcionarioRepository;
    private final FuncaoRepository funcaoRepository;
    private final AssinaturaEmpresaRepository assinaturaEmpresaRepository;

    @Transactional
    public TrialRegisterResponse registerTrial(TrialRegisterRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }

        String customerId = createStripeCustomer(request);

        // 1. Criar a empresa antes da assinatura Stripe. EmpresaService também
        // provisiona a assinatura local, que será normalizada para o trial oficial.
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(request.getNomeEmpresa());
        empresa.setNomeFantasia(request.getNomeEmpresa());
        empresa.setEmail(request.getEmail());
        empresa.setTelefone(request.getTelefone());
        empresa.setDataAbertura(LocalDate.now());
        empresa.setSegmento(request.getSegmento());
        empresa.setStripeCustomerId(customerId);
        empresa.setCnpj(request.getCnpjOuCpf());

        Empresa savedEmpresa = empresaService.create(empresa);
        AssinaturaEmpresa assinaturaTrial = normalizeLocalTrial(savedEmpresa, customerId);

        // 2. Somente agora criar a assinatura na Stripe, garantindo que empresa e
        // assinatura local já existem e possam ser vinculadas sem duplicidade.
        if (customerId != null) {
            try {
                Subscription stripeSubscription = stripeService.createTrialSubscription(customerId);
                if (stripeSubscription != null) {
                    syncLocalTrialWithStripe(assinaturaTrial, stripeSubscription);
                }
            } catch (Exception e) {
                log.error("Erro ao criar/sincronizar assinatura de trial na Stripe para a empresa {}", savedEmpresa.getId(), e);
                // O trial local continua válido; a integração pode ser reconciliada depois.
            }
        }

        // 3. Gerar senha temporária
        String rawPassword = generateTemporaryPassword();

        // 4. Criar função Admin e associar ao usuário
        Funcao funcaoAdmin = Funcao.builder()
                .empresaId(savedEmpresa.getId())
                .nome("ADMIN")
                .descricao("Administrador do Sistema")
                .sistema(true)
                .ativo(true)
                .build();
        Funcao savedFuncao = funcaoRepository.save(funcaoAdmin);

        Usuario usuario = Usuario.builder()
                .empresaId(savedEmpresa.getId())
                .nomeCompleto(request.getNomeCompleto())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(rawPassword))
                .ativo(true)
                .bloqueado(false)
                .build();
        usuario.getFuncoes().add(savedFuncao);
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // 5. Criar registro de funcionário
        Funcionario funcionario = new Funcionario();
        funcionario.setEmpresaId(savedEmpresa.getId());
        funcionario.setUsuarioId(savedUsuario.getId());
        funcionario.setNomeCompleto(request.getNomeCompleto());
        funcionario.setMatricula("adm-01");
        funcionario.setDataAdmissao(LocalDate.now());
        funcionario.setStatus(StatusFuncionario.ATIVO);

        if (request.getCnpjOuCpf() != null && request.getCnpjOuCpf().replaceAll("\\D", "").length() == 11) {
            funcionario.setCpf(request.getCnpjOuCpf());
        }

        funcionarioRepository.save(funcionario);

        // 6. Enviar e-mail de boas-vindas com a senha
        emailService.sendTrialCredentials(request.getEmail(), request.getNomeCompleto(), rawPassword);

        return TrialRegisterResponse.builder()
                .success(true)
                .message("Cadastro realizado com sucesso. Verifique seu e-mail para acessar o sistema.")
                .email(request.getEmail())
                .build();
    }

    private String createStripeCustomer(TrialRegisterRequest request) {
        try {
            Customer stripeCustomer = stripeService.createCustomer(
                    request.getEmail(),
                    request.getNomeCompleto(),
                    request.getTelefone()
            );
            return stripeCustomer != null ? stripeCustomer.getId() : null;
        } catch (Exception e) {
            log.error("Erro ao criar cliente na Stripe. O cadastro seguirá com trial local.", e);
            return null;
        }
    }

    private AssinaturaEmpresa normalizeLocalTrial(Empresa empresa, String customerId) {
        AssinaturaEmpresa assinatura = assinaturaEmpresaRepository
                .findFirstByEmpresaIdOrderByDataFimDesc(empresa.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Empresa criada sem assinatura local de trial: " + empresa.getId()
                ));

        LocalDateTime trialEndsAt = LocalDateTime.now(BUSINESS_ZONE).plusDays(TRIAL_DAYS);
        assinatura.setStatus(StatusAssinatura.TESTE);
        assinatura.setDataInicio(LocalDate.now(BUSINESS_ZONE));
        assinatura.setDataFim(trialEndsAt.toLocalDate());
        assinatura.setTrialEndsAt(trialEndsAt);
        assinatura.setSubscriptionEndsAt(trialEndsAt);
        assinatura.setStripeCustomerId(customerId);

        return assinaturaEmpresaRepository.save(assinatura);
    }

    private void syncLocalTrialWithStripe(AssinaturaEmpresa assinatura, Subscription stripeSubscription) {
        assinatura.setStripeSubscriptionId(stripeSubscription.getId());
        assinatura.setStripeCustomerId(stripeSubscription.getCustomer());

        if (stripeSubscription.getItems() != null
                && stripeSubscription.getItems().getData() != null
                && !stripeSubscription.getItems().getData().isEmpty()
                && stripeSubscription.getItems().getData().get(0).getPrice() != null) {
            assinatura.setStripeProductId(stripeSubscription.getItems().getData().get(0).getPrice().getProduct());
        }

        if (stripeSubscription.getTrialEnd() != null) {
            LocalDateTime trialEndsAt = Instant.ofEpochSecond(stripeSubscription.getTrialEnd())
                    .atZone(BUSINESS_ZONE)
                    .toLocalDateTime();
            assinatura.setTrialEndsAt(trialEndsAt);
            assinatura.setDataFim(trialEndsAt.toLocalDate());
        }

        if (stripeSubscription.getCurrentPeriodEnd() != null) {
            LocalDateTime subscriptionEndsAt = Instant.ofEpochSecond(stripeSubscription.getCurrentPeriodEnd())
                    .atZone(BUSINESS_ZONE)
                    .toLocalDateTime();
            assinatura.setSubscriptionEndsAt(subscriptionEndsAt);
            assinatura.setDataFim(subscriptionEndsAt.toLocalDate());
        }

        assinatura.setStatus(mapStripeStatus(stripeSubscription.getStatus()));
        assinaturaEmpresaRepository.save(assinatura);
    }

    private StatusAssinatura mapStripeStatus(String stripeStatus) {
        if (stripeStatus == null) {
            return StatusAssinatura.TESTE;
        }

        return switch (stripeStatus) {
            case "trialing" -> StatusAssinatura.TESTE;
            case "active" -> StatusAssinatura.ATIVO;
            case "past_due" -> StatusAssinatura.ATRASADO;
            case "canceled" -> StatusAssinatura.CANCELADO;
            case "unpaid" -> StatusAssinatura.SUSPENSO;
            case "incomplete" -> StatusAssinatura.INCOMPLETO;
            default -> StatusAssinatura.SUSPENSO;
        };
    }

    private String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
