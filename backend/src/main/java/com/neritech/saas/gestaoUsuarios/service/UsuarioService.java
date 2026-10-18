package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.common.tenancy.TenantAccess;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.gestaoUsuarios.repository.FuncaoRepository;
import com.neritech.saas.gestaoUsuarios.repository.PermissaoRepository;
import com.neritech.saas.gestaoUsuarios.domain.Funcao;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import com.neritech.saas.empresa.domain.AssinaturaEmpresa;
import com.neritech.saas.empresa.service.StripeService;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FuncaoRepository funcaoRepository;
    private final PermissaoRepository permissaoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AssinaturaEmpresaRepository assinaturaEmpresaRepository;
    private final StripeService stripeService;
    private final EmpresaRepository empresaRepository;
    private final com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository sessaoUsuarioRepository;
    private final com.neritech.saas.gestaoUsuarios.repository.TokenRecuperacaoSenhaRepository tokenRepository;
    private final com.neritech.saas.common.mail.EmailService emailService;
    private final org.springframework.jdbc.core.JdbcTemplate jdbc;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> findAll() {
        Long empresaId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        if (empresaId == null) {
            return Collections.emptyList();
        }
        return usuarioRepository.findAllByEmpresaId(empresaId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<UsuarioResponse> search(String query, Boolean ativo,
            org.springframework.data.domain.Pageable pageable) {
        Long empresaId = TenantAccess.requireCurrent();
        if (!java.util.List.of(20, 50, 100).contains(pageable.getPageSize())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        // PostgreSQL infere um parâmetro String nulo usado em lower/concat como bytea.
        // A string vazia mantém a semântica de "sem filtro" por resultar em LIKE '%%'.
        String normalized = query == null || query.isBlank() ? "" : query.trim();
        return usuarioRepository.searchByTenant(empresaId, normalized, ativo, pageable).map(this::toResponse);
    }

    @Transactional
    public UsuarioResponse create(UsuarioRequest request) {
        return createInternal(request, false);
    }

    @Transactional
    public UsuarioResponse invite(UsuarioRequest request) {
        if (!emailService.isPasswordResetConfigured()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                    "Envio de convites temporariamente indisponível");
        }
        UsuarioResponse response = createInternal(request, true);
        Usuario usuario = usuarioRepository.findByIdAndEmpresaId(response.getId(), TenantAccess.requireCurrent())
                .orElseThrow();
        byte[] random = new byte[32];
        new java.security.SecureRandom().nextBytes(random);
        String rawToken = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(random);
        LocalDateTime expires = LocalDateTime.now().plusHours(48);
        tokenRepository.saveAndFlush(com.neritech.saas.gestaoUsuarios.domain.TokenRecuperacaoSenha.builder()
                .token(hashToken(rawToken)).usuario(usuario).dataExpiracao(expires).build());
        emailService.sendUserInvitationLink(usuario.getEmail(), usuario.getNomeCompleto(), rawToken);
        response.setConviteTokenExpira(expires);
        audit(usuario, "CONVITE", java.util.Map.of("convite", "enviado"));
        return response;
    }

    private UsuarioResponse createInternal(UsuarioRequest request, boolean invitation) {
        Long empresaId = TenantAccess.requireCurrent();
        String email = normalizeEmail(request.getEmail());
        if (usuarioRepository.existsByEmailIgnoreCaseAndEmpresaId(email, empresaId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "E-mail já cadastrado nesta empresa");
        }
        if (!invitation && (request.getSenha() == null || request.getSenha().isBlank())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY, "Senha é obrigatória no cadastro direto");
        }
        String secret = invitation ? java.util.UUID.randomUUID() + "-" + java.util.UUID.randomUUID() : request.getSenha();

        Usuario usuario = Usuario.builder()
                .nomeCompleto(request.getNomeCompleto().trim())
                .email(email)
                .senha(passwordEncoder.encode(secret))
                .ativo(request.isAtivo())
                .bloqueado(request.isBloqueado())
                .deveTrocarSenha(invitation)
                .build();

        com.neritech.saas.gestaoUsuarios.domain.PerfilUsuario perfil = new com.neritech.saas.gestaoUsuarios.domain.PerfilUsuario();
        perfil.setUsuario(usuario);
        perfil.setEmpresaId(com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant());
        perfil.setPreferencias(request.getPreferencias());
        perfil.setCargo(request.getCargo());
        perfil.setDepartamento(request.getDepartamento());
        perfil.setTelefone(request.getTelefone());
        usuario.setPerfil(perfil);

        syncFuncoes(usuario, request.getFuncoesIds());

        usuario = usuarioRepository.save(usuario);
        audit(usuario, "INSERT", java.util.Map.of("email", email, "ativo", usuario.getAtivo()));
        return toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findById(Long id) {
        Long empresaId = com.neritech.saas.common.tenancy.TenantContext.getCurrentTenant();
        return usuarioRepository.findByIdAndEmpresaId(id, empresaId)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou acesso negado"));
    }

    @Transactional
    public UsuarioResponse update(Long id, UsuarioRequest request) {
        Long empresaId = TenantAccess.requireCurrent();
        Usuario usuario = usuarioRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou acesso negado"));

        // A permissão de configurar usuários não autoriza editar administradores,
        // nem mesmo removendo-lhes a função ADMIN. Conferir ANTES de alterar dados.
        if (containsAdminRole(usuario.getFuncoes()) && !callerIsAdmin()) {
            throw new AccessDeniedException("Somente administradores podem alterar contas administrativas");
        }

        if (request.getVersao() != null && !java.util.Objects.equals(request.getVersao(), usuario.getVersao())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "Usuário foi alterado; recarregue antes de salvar");
        }
        String normalizedEmail = normalizeEmail(request.getEmail());
        usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(normalizedEmail, empresaId)
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> { throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.CONFLICT, "E-mail já cadastrado nesta empresa"); });
        boolean wasEnabled = Boolean.TRUE.equals(usuario.getAtivo()) && !Boolean.TRUE.equals(usuario.getBloqueado());
        boolean wasAdmin = containsAdminRole(usuario.getFuncoes());
        java.util.Set<Long> previousRoles = usuario.getFuncoes() == null ? java.util.Set.of()
                : usuario.getFuncoes().stream().map(Funcao::getId).collect(java.util.stream.Collectors.toSet());
        if (wasAdmin && (!request.isAtivo() || request.isBloqueado()
                || !requestedAdmin(request.getFuncoesIds(), empresaId)) && isLastActiveAdmin(empresaId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "Não é possível remover ou bloquear o último administrador");
        }
        usuario.setNomeCompleto(request.getNomeCompleto().trim());
        usuario.setEmail(normalizedEmail);
        usuario.setAtivo(request.isAtivo());
        usuario.setBloqueado(request.isBloqueado());
        usuario.setDataBloqueio(request.isBloqueado() ? LocalDateTime.now() : null);
        
        if (request.getSenha() != null && !request.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        if (usuario.getPerfil() == null) {
            com.neritech.saas.gestaoUsuarios.domain.PerfilUsuario perfil = new com.neritech.saas.gestaoUsuarios.domain.PerfilUsuario();
            perfil.setUsuario(usuario);
            perfil.setEmpresaId(empresaId);
            usuario.setPerfil(perfil);
        }
        usuario.getPerfil().setPreferencias(request.getPreferencias());
        usuario.getPerfil().setCargo(request.getCargo());
        usuario.getPerfil().setDepartamento(request.getDepartamento());
        usuario.getPerfil().setTelefone(request.getTelefone());

        syncFuncoes(usuario, request.getFuncoesIds());

        usuario = usuarioRepository.save(usuario);
        boolean enabled = Boolean.TRUE.equals(usuario.getAtivo()) && !Boolean.TRUE.equals(usuario.getBloqueado());
        java.util.Set<Long> currentRoles = usuario.getFuncoes().stream().map(Funcao::getId)
                .collect(java.util.stream.Collectors.toSet());
        if ((wasEnabled && !enabled) || !previousRoles.equals(currentRoles)) revokeSessions(usuario.getId());
        audit(usuario, "UPDATE", java.util.Map.of("email", normalizedEmail,
                "ativo", usuario.getAtivo(), "bloqueado", usuario.getBloqueado()));
        return toResponse(usuario);
    }

    private void syncFuncoes(Usuario usuario, java.util.Set<Long> funcoesIds) {
        Long empresaId = TenantAccess.requireCurrent();
        // Validate all FKs before mutating the entity. findAllById is global and
        // cannot be trusted to isolate functions by the authenticated tenant.
        List<Funcao> funcoesGerenciadas = java.util.Collections.emptyList();
        if (funcoesIds != null && !funcoesIds.isEmpty()) {
            funcoesGerenciadas = funcaoRepository.findAllById(funcoesIds);
            if (funcoesGerenciadas.size() != funcoesIds.size()
                    || funcoesGerenciadas.stream()
                        .anyMatch(funcao -> !empresaId.equals(funcao.getEmpresaId()))) {
                throw new ResourceNotFoundException("Função não encontrada");
            }
        }
        // Atribuir ROLE_ADMIN (direta ou por alias) exige um ADMIN autenticado,
        // mesmo quando o solicitante possui GERAL_CONFIG_SISTEMA.
        if (containsAdminRole(funcoesGerenciadas) && !callerIsAdmin()) {
            throw new AccessDeniedException("Somente administradores podem conceder funções administrativas");
        }
        if (usuario.getFuncoes() == null) {
            usuario.setFuncoes(new java.util.HashSet<>());
        }
        usuario.getFuncoes().clear();
        usuario.getFuncoes().addAll(funcoesGerenciadas);
    }

    private boolean containsAdminRole(java.util.Collection<Funcao> funcoes) {
        return funcoes != null && funcoes.stream().anyMatch(funcao -> {
            if (funcao == null || funcao.getNome() == null) return false;
            String nome = funcao.getNome().trim().toUpperCase(java.util.Locale.ROOT);
            return "ADMIN".equals(nome) || "ADMINISTRADOR".equals(nome) || "SUPER_ADMIN".equals(nome);
        });
    }

    private boolean callerIsAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && auth.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    @Transactional
    public void delete(Long id) {
        Long empresaId = TenantAccess.requireCurrent();
        Usuario usuario = usuarioRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou acesso negado"));
        if (containsAdminRole(usuario.getFuncoes()) && !callerIsAdmin()) {
            throw new AccessDeniedException("Somente administradores podem inativar contas administrativas");
        }
        if (containsAdminRole(usuario.getFuncoes()) && isLastActiveAdmin(empresaId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "Não é possível inativar o último administrador");
        }
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
        revokeSessions(usuario.getId());
        audit(usuario, "UPDATE", java.util.Map.of("ativo", false));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse getCurrentUser() {
        log.info("Starting getCurrentUser profile load");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Usuário não autenticado");
        }
        
        String email = authentication.getName();
        Long empresaId = TenantAccess.requireCurrent();
        Usuario usuario = usuarioRepository.findByEmailIgnoreCaseAndEmpresaId(email, empresaId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
        
        UsuarioResponse response = toResponse(usuario);
        log.info("Profile load completed for user: {}", email);
        return response;
    }

    private java.util.Set<String> obterPermissoesUsuario(Usuario usuario) {
        if (usuario.getFuncoes() == null) {
            return java.util.Collections.emptySet();
        }

        boolean isAdmin = usuario.getFuncoes().stream()
                .anyMatch(f -> f.getNome() != null && (
                    "ADMIN".equalsIgnoreCase(f.getNome()) ||
                    "ADMINISTRADOR".equalsIgnoreCase(f.getNome()) ||
                    "SUPER_ADMIN".equalsIgnoreCase(f.getNome())
                ));

        if (isAdmin) {
            try {
                return permissaoRepository.findAll().stream()
                        .filter(p -> p.getValor() != null)
                        .map(p -> p.getValor())
                        .collect(java.util.stream.Collectors.toSet());
            } catch (Exception e) {
                log.error("Erro ao carregar todas as permissões para o ADMIN", e);
            }
        }

        return usuario.getFuncoes().stream()
                .filter(f -> f.getPermissoes() != null)
                .flatMap(f -> f.getPermissoes().stream())
                .filter(p -> p.getValor() != null)
                .map(p -> p.getValor())
                .collect(java.util.stream.Collectors.toSet());
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        Long empresaId = usuario.getEmpresaId();
        boolean assinaturaAtiva = false;
        String stripeUrl = null;
        Integer planoNivel = 1;

        if (empresaId != null) {
            java.util.Optional<AssinaturaEmpresa> assinaturaOpt = assinaturaEmpresaRepository
                .findFirstByEmpresaIdOrderByDataFimDesc(empresaId);
            
            if (assinaturaOpt.isPresent()) {
                AssinaturaEmpresa assinatura = assinaturaOpt.get();
                com.neritech.saas.empresa.domain.enums.StatusAssinatura status = assinatura.getStatus();
                
                // Validação de período de graça (Grace Period)
                if (status == com.neritech.saas.empresa.domain.enums.StatusAssinatura.ATRASADO && 
                    assinatura.getGracePeriodEndsAt() != null && 
                    assinatura.getGracePeriodEndsAt().isBefore(java.time.LocalDateTime.now())) {
                    status = com.neritech.saas.empresa.domain.enums.StatusAssinatura.SUSPENSO;
                }

                if (status == com.neritech.saas.empresa.domain.enums.StatusAssinatura.TESTE) {
                    if (com.neritech.saas.trial.service.TrialPolicy.isExpired(assinatura)) {
                        status = com.neritech.saas.empresa.domain.enums.StatusAssinatura.INATIVO;
                    }
                }

                // Validação de assinatura ativa/liberada
                if (status != null) {
                    assinaturaAtiva = (status == com.neritech.saas.empresa.domain.enums.StatusAssinatura.ATIVO || 
                                       status == com.neritech.saas.empresa.domain.enums.StatusAssinatura.TESTE ||
                                       "ACTIVE".equals(status.name()) || 
                                       "TRIAL".equals(status.name()));
                } else {
                    assinaturaAtiva = false;
                }
                
                log.info("Empresa: {} - Status Assinatura: {} - Ativa: {}", empresaId, status, assinaturaAtiva);
                
                planoNivel = (assinatura.getPlano() != null) ? assinatura.getPlano().getNivel() : 1;
                
                UsuarioResponse response = UsuarioResponse.builder()
                        .id(usuario.getId())
                        .empresaId(empresaId)
                        .nomeCompleto(usuario.getNomeCompleto())
                        .email(usuario.getEmail())
                        .ativo(usuario.getAtivo())
                        .bloqueado(usuario.getBloqueado())
                        .ultimoAcesso(usuario.getUltimoAcesso())
                        .conviteTokenExpira(inviteExpiry(usuario.getId()))
                        .versao(usuario.getVersao())
                        .cargo(usuario.getPerfil() != null ? usuario.getPerfil().getCargo() : null)
                        .departamento(usuario.getPerfil() != null ? usuario.getPerfil().getDepartamento() : null)
                        .telefone(usuario.getPerfil() != null ? usuario.getPerfil().getTelefone() : null)
                        .avatarUrl(usuario.getPerfil() != null ? usuario.getPerfil().getAvatarUrl() : null)
                        .preferencias(usuario.getPerfil() != null ? usuario.getPerfil().getPreferencias() : null)
                        .funcoes(usuario.getFuncoes() != null ? usuario.getFuncoes().stream().map(f -> f.getNome()).collect(java.util.stream.Collectors.toSet()) : Collections.emptySet())
                        .funcoesIds(usuario.getFuncoes() != null ? usuario.getFuncoes().stream().map(f -> f.getId()).collect(java.util.stream.Collectors.toSet()) : Collections.emptySet())
                        .permissions(obterPermissoesUsuario(usuario))
                        .assinaturaAtiva(assinaturaAtiva)
                        .subscriptionStatus(status)
                        .planoNivel(planoNivel)
                        .build();

                if (!assinaturaAtiva && stripeService.isConfigured()) {
                    try {
                        String customerId = empresaRepository.findById(empresaId)
                            .map(e -> e.getStripeCustomerId())
                            .orElse(null);
                        
                        if (customerId != null) {
                            response.setStripeUrl(stripeService.createBillingPortalSession(customerId, "https://app.neritechauto.com.br/auth/login"));
                        }
                    } catch (Exception e) {
                        // Log error
                    }
                }
                return response;
            }
        }

        return UsuarioResponse.builder()
                .id(usuario.getId())
                .empresaId(empresaId)
                .nomeCompleto(usuario.getNomeCompleto())
                .email(usuario.getEmail())
                .ativo(usuario.getAtivo())
                .bloqueado(usuario.getBloqueado())
                .ultimoAcesso(usuario.getUltimoAcesso())
                .conviteTokenExpira(inviteExpiry(usuario.getId()))
                .versao(usuario.getVersao())
                .cargo(usuario.getPerfil() != null ? usuario.getPerfil().getCargo() : null)
                .departamento(usuario.getPerfil() != null ? usuario.getPerfil().getDepartamento() : null)
                .telefone(usuario.getPerfil() != null ? usuario.getPerfil().getTelefone() : null)
                .avatarUrl(usuario.getPerfil() != null ? usuario.getPerfil().getAvatarUrl() : null)
                .preferencias(usuario.getPerfil() != null ? usuario.getPerfil().getPreferencias() : null)
                .funcoes(usuario.getFuncoes() != null 
                    ? usuario.getFuncoes().stream().map(f -> f.getNome()).collect(Collectors.toSet()) 
                    : Collections.emptySet())
                .funcoesIds(usuario.getFuncoes() != null 
                    ? usuario.getFuncoes().stream().map(f -> f.getId()).collect(Collectors.toSet()) 
                    : Collections.emptySet())
                .permissions(obterPermissoesUsuario(usuario))
                .planoNivel(planoNivel)
                .assinaturaAtiva(false)
                .subscriptionStatus(com.neritech.saas.empresa.domain.enums.StatusAssinatura.INATIVO)
                .stripeUrl(stripeUrl)
                .build();
    }

    private LocalDateTime inviteExpiry(Long usuarioId) {
        return tokenRepository.findFirstByUsuarioIdAndUsadoFalseOrderByDataExpiracaoDesc(usuarioId)
                .map(com.neritech.saas.gestaoUsuarios.domain.TokenRecuperacaoSenha::getDataExpiracao).orElse(null);
    }

    private void revokeSessions(Long usuarioId) {
        for (var session : sessaoUsuarioRepository.findByUsuarioIdAndAtivoTrue(usuarioId)) {
            session.setAtivo(false);
            sessaoUsuarioRepository.save(session);
        }
    }

    private boolean isLastActiveAdmin(Long empresaId) {
        return usuarioRepository.countActiveAdmins(empresaId) <= 1;
    }

    private boolean requestedAdmin(java.util.Set<Long> ids, Long empresaId) {
        if (ids == null || ids.isEmpty()) return false;
        return funcaoRepository.findAllById(ids).stream().anyMatch(f -> empresaId.equals(f.getEmpresaId())
                && containsAdminRole(java.util.List.of(f)));
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private static String hashToken(String token) {
        try {
            byte[] digest = java.security.MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest);
        } catch (java.security.NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 indisponível", ex);
        }
    }

    private void audit(Usuario usuario, String operation, java.util.Map<String, ?> values) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario actor = auth == null ? null : usuarioRepository
                .findByEmailIgnoreCaseAndEmpresaId(auth.getName(), usuario.getEmpresaId()).orElse(null);
        if (actor == null) throw new AccessDeniedException("Autor da alteração não pertence à empresa");
        try {
            jdbc.update("""
                    insert into logs_alteracoes (empresa_id, tabela_afetada, registro_id, operacao,
                    campos_alterados, valores_novos, usuario_responsavel, contexto_operacao, data_alteracao)
                    values (?, 'usuarios', ?, ?, ?, ?, ?, 'ADM-003', current_timestamp)
                    """, usuario.getEmpresaId(), usuario.getId(), "INSERT".equals(operation) ? "INSERT" : "UPDATE",
                    objectMapper.writeValueAsString(values.keySet()), objectMapper.writeValueAsString(values), actor.getId());
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            throw new IllegalStateException("Falha ao auditar usuário", ex);
        }
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<com.neritech.saas.gestaoUsuarios.dto.UsuarioAlteracaoResponse> history(
            Long usuarioId, int page, int size) {
        Long empresaId = TenantAccess.requireCurrent();
        usuarioRepository.findByIdAndEmpresaId(usuarioId, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (page < 0 || !java.util.List.of(20, 50, 100).contains(size)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY, "Paginação inválida");
        }
        var pageable = org.springframework.data.domain.PageRequest.of(page, size);
        Long total = jdbc.queryForObject("select count(*) from logs_alteracoes where empresa_id=? and tabela_afetada='usuarios' and registro_id=? and contexto_operacao='ADM-003'",
                Long.class, empresaId, usuarioId);
        var content = jdbc.query("""
                select operacao, campos_alterados, valores_novos, usuario_responsavel, data_alteracao
                from logs_alteracoes where empresa_id=? and tabela_afetada='usuarios'
                and registro_id=? and contexto_operacao='ADM-003'
                order by data_alteracao desc, id desc limit ? offset ?
                """, (rs, row) -> new com.neritech.saas.gestaoUsuarios.dto.UsuarioAlteracaoResponse(
                rs.getString("operacao"), rs.getString("campos_alterados"), rs.getString("valores_novos"),
                rs.getLong("usuario_responsavel"), rs.getTimestamp("data_alteracao").toLocalDateTime()),
                empresaId, usuarioId, size, pageable.getOffset());
        return new org.springframework.data.domain.PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Transactional(readOnly = true)
    public java.util.List<Object[]> findNomesCompletosByIdsAndEmpresaId(java.util.Collection<Long> ids, Long empresaId) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return usuarioRepository.findNomesCompletosByIdsAndEmpresaId(ids, empresaId);
    }
}
