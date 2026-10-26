package com.neritech.saas.trial.service;

import com.neritech.saas.common.mail.EmailService;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.service.EmpresaService;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.trial.dto.TrialRegisterRequest;
import com.neritech.saas.trial.dto.TrialRegisterResponse;
import lombok.RequiredArgsConstructor;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.domain.enums.StatusFuncionario;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class TrialService {

    private final EmpresaService empresaService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final FuncionarioRepository funcionarioRepository;
    private final FuncaoRepository funcaoRepository;
    private final JdbcTemplate jdbcTemplate;

    @Value("${trial.legal.version:}")
    private String versaoLegalVigente;
    @Value("${trial.legal.terms-url:}")
    private String termosUrl;
    @Value("${trial.legal.privacy-url:}")
    private String privacidadeUrl;

    @Transactional
    public TrialRegisterResponse registerTrial(TrialRegisterRequest request) {
        if (versaoLegalVigente == null || versaoLegalVigente.isBlank()
                || !termosUrl.startsWith("https://") || !privacidadeUrl.startsWith("https://")) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Cadastro indisponível até a publicação dos termos legais.");
        }
        if (!versaoLegalVigente.equals(request.getVersaoLegal())) {
            throw new IllegalArgumentException("Os termos foram atualizados. Recarregue o formulário.");
        }
        String emailNormalizado = request.getEmail().trim();
        if (usuarioRepository.existsByEmailIgnoreCase(emailNormalizado)) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }
        String celular = request.getCelular().replaceAll("\\D", "");
        String telefone = request.getTelefone().replaceAll("\\D", "");
        if (!celular.matches("[1-9][0-9]9[0-9]{8}") || !telefone.matches("[1-9][0-9][0-9]{8,9}")) {
            throw new IllegalArgumentException("Telefone ou celular inválido.");
        }

        // O teste local começa no cadastro; Stripe é usado quando a empresa escolhe um plano.
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(request.getNomeEmpresa());
        empresa.setNomeFantasia(request.getNomeEmpresa());
        empresa.setEmail(emailNormalizado);
        empresa.setTelefone(telefone);
        empresa.setCelularResponsavel(celular);
        empresa.setDataAbertura(LocalDate.now());
        empresa.setSegmento(request.getSegmento());
        
        empresa.setCnpj(request.getCnpjOuCpf().replaceAll("[^a-zA-Z0-9]", "").toUpperCase(java.util.Locale.ROOT));
        Empresa savedEmpresa = empresaService.createTrial(empresa, request.getPlanoId());

        // Criar função Admin e associar ao usuário
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
                .email(emailNormalizado)
                .senha(passwordEncoder.encode(request.getSenha()))
                .ativo(true)
                .bloqueado(false)
                .build();
        usuario.getFuncoes().add(savedFuncao);
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Criar registro de funcionário
        Funcionario funcionario = new Funcionario();
        funcionario.setEmpresaId(savedEmpresa.getId());
        funcionario.setUsuarioId(savedUsuario.getId());
        funcionario.setNomeCompleto(request.getNomeCompleto());
        funcionario.setMatricula("adm-01");
        funcionario.setDataAdmissao(LocalDate.now());
        funcionario.setStatus(StatusFuncionario.ATIVO);
        
        // Se o valor informado no registro for um CPF, alimentamos o funcionário, caso contrário fica em branco
        if (request.getCnpjOuCpf() != null && request.getCnpjOuCpf().replaceAll("\\D", "").length() == 11) {
            funcionario.setCpf(empresa.getCnpj());
        }
        
        funcionarioRepository.save(funcionario);
        jdbcTemplate.update("INSERT INTO trial_aceites_legais (empresa_id, usuario_id, versao, celular, aceito_em) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)",
                savedEmpresa.getId(), savedUsuario.getId(), versaoLegalVigente, celular);

        if (emailService.isPasswordResetConfigured()) {
            String email = emailNormalizado;
            String nome = request.getNomeCompleto();
            String oficina = request.getNomeEmpresa();
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() {
                    try { emailService.sendTrialWelcome(email, nome, oficina); }
                    catch (Exception failure) { log.warn("Trial welcome delivery unavailable after account commit"); }
                }
            });
        }

        return TrialRegisterResponse.builder()
                .success(true)
                .message("Cadastro realizado. Acesse com seu e-mail e a senha definida no formulário.")
                .email(emailNormalizado)
                .build();
    }

}
