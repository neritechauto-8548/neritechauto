package com.neritech.saas.trial.service;

import com.neritech.saas.common.mail.EmailService;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.service.EmpresaService;
import com.neritech.saas.empresa.service.StripeService;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.trial.dto.TrialRegisterRequest;
import com.neritech.saas.trial.dto.TrialRegisterResponse;
import com.stripe.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrialService {

    private final EmpresaService empresaService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final StripeService stripeService;
    private final EmailService emailService;
    private final FuncionarioRepository funcionarioRepository;
    private final FuncaoRepository funcaoRepository;

    @Transactional
    public TrialRegisterResponse registerTrial(TrialRegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }

        String customerId = null;
        try {
            // 1. Criar cliente na Stripe
            Customer stripeCustomer = stripeService.createCustomer(request.getEmail(), request.getNomeCompleto(), request.getTelefone());
            if (stripeCustomer != null) {
                customerId = stripeCustomer.getId();
                // Inicia assinatura com 7 dias de trial se tiver configurado preço padrão
                stripeService.createTrialSubscription(customerId);
            }
        } catch (Exception e) {
            log.error("Erro ao integrar com a Stripe", e);
            // Non-blocking for now if stripe is not fully configured
        }

        // 2. Criar a Empresa
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

        // 3. Gerar senha temporária
        String rawPassword = generateTemporaryPassword();

        // 4. Criar Funcao Admin e Associar ao Usuario
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

        // 5. Criar Registro de Funcionário
        Funcionario funcionario = new Funcionario();
        funcionario.setEmpresaId(savedEmpresa.getId());
        funcionario.setUsuarioId(savedUsuario.getId());
        funcionario.setNomeCompleto(request.getNomeCompleto());
        funcionario.setMatricula("adm-01");
        funcionario.setDataAdmissao(LocalDate.now());
        funcionario.setStatus(StatusFuncionario.ATIVO);
        
        // Se o valor informado no registro for um CPF, alimentamos o funcionário, caso contrário fica em branco
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

    private String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
