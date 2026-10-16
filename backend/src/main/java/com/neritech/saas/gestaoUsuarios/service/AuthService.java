package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.LogAcesso;
import com.neritech.saas.gestaoUsuarios.domain.SessaoUsuario;
import com.neritech.saas.gestaoUsuarios.domain.TentativaLogin;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.LoginRequest;
import com.neritech.saas.gestaoUsuarios.dto.LoginResponse;
import com.neritech.saas.gestaoUsuarios.dto.RefreshTokenRequest;
import com.neritech.saas.gestaoUsuarios.repository.LogAcessoRepository;
import com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository;
import com.neritech.saas.gestaoUsuarios.repository.TentativaLoginRepository;
import com.neritech.saas.gestaoUsuarios.repository.TokenRecuperacaoSenhaRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.gestaoUsuarios.domain.TokenRecuperacaoSenha;
import com.neritech.saas.gestaoUsuarios.dto.ResetPasswordRequest;
import com.neritech.saas.common.mail.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;
import com.neritech.saas.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Locale;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UsuarioRepository usuarioRepository;
    private final SessaoUsuarioRepository sessaoUsuarioRepository;
    private final LogAcessoRepository logAcessoRepository;
    private final TentativaLoginRepository tentativaLoginRepository;
    private final LoginFailureRecorder loginFailureRecorder;
    private final RecoveryRequestThrottle recoveryRequestThrottle;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRecuperacaoSenhaRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 1. Check rate limiting / blocking
        checkLoginAttempts(request.getEmail());

        try {
            // 2. Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            // 3. Generate Tokens
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("empresaId", usuario.getEmpresaId());
            extraClaims.put("userId", usuario.getId());
            
            Set<String> roles = usuario.getFuncoes().stream()
                    .filter(f -> f.getAtivo())
                    .map(f -> "ROLE_" + f.getNome())
                    .collect(Collectors.toSet());
            
            Set<String> permissoes = usuario.getFuncoes().stream()
                    .filter(f -> f.getAtivo())
                    .flatMap(f -> f.getPermissoes().stream())
                    .map(p -> p.getValor())
                    .collect(Collectors.toSet());

            extraClaims.put("roles", roles);
            extraClaims.put("permissoes", permissoes);

            String accessToken = jwtService.generateToken(extraClaims, userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            // 4. Create Session
            TenantContext.setCurrentTenant(usuario.getEmpresaId());
            createSession(usuario, accessToken, refreshToken, request);

            // 5. Log Success
            logLoginAttempt(request, true);
            logAccess(usuario, LogAcesso.TipoEvento.LOGIN_SUCCESS, "Login realizado com sucesso", request.getIpAddress(), request.getUserAgent());

            // 6. Update User Access
            usuario.setUltimoAcesso(LocalDateTime.now());
            usuarioRepository.save(usuario);

            LoginResponse response = LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .usuarioId(usuario.getId())
                    .empresaId(usuario.getEmpresaId())
                    .nomeCompleto(usuario.getNomeCompleto())
                    .email(usuario.getEmail())
                    .roles(roles)
                    .permissoes(permissoes)
                    .expiraEm(LocalDateTime.now().plusDays(1)) // Approx
                    .expiresIn(jwtService.getExpirationTime())
                    .primeiroAcesso(usuario.getUltimoAcesso() == null) // Logic check
                    .deveTrocarSenha(usuario.getDeveTrocarSenha())
                    .build();
            return response;

        } catch (AuthenticationException e) {
            // Persist the failed attempt in an independent transaction: rolling back
            // the login transaction must never reset the brute-force counter.
            try {
                java.util.Optional<Usuario> userOpt = usuarioRepository.findByEmailIgnoreCase(request.getEmail());
                Long tenant = userOpt.map(Usuario::getEmpresaId).orElse(null);
                loginFailureRecorder.record(request, tenant);
            } finally {
                TenantContext.clear();
            }
            throw new BadCredentialsException("Credenciais inválidas");
        } catch (Exception e) {
            try {
                java.util.Optional<Usuario> userOpt = usuarioRepository.findByEmailIgnoreCase(request.getEmail());
                if (userOpt.isPresent()) {
                    TenantContext.setCurrentTenant(userOpt.get().getEmpresaId());
                } else {
                    TenantContext.clear();
                }
                logAccess(null, LogAcesso.TipoEvento.LOGIN_FAIL, "Erro no login: " + e.getMessage(), request.getIpAddress(), request.getUserAgent());
            } finally {
                TenantContext.clear();
            }
            throw e;
        }
    }

    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String userEmail = jwtService.extractUsername(refreshToken);
        
        if (userEmail != null) {
            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            TenantContext.setCurrentTenant(usuario.getEmpresaId());

            // Validate token matches session
            SessaoUsuario sessao = sessaoUsuarioRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new BadCredentialsException("Sessão inválida ou expirada"));

            if (!sessao.getAtivo()) {
                throw new BadCredentialsException("Sessão inativa");
            }
            if (!sessao.getUsuario().getId().equals(usuario.getId()) || !sessao.getEmpresaId().equals(usuario.getEmpresaId())) {
                throw new BadCredentialsException("Sessão inválida");
            }

            // Generate new Access Token
            // Note: We need UserDetails. CustomUserDetailsService loads it.
            // But we can just build it manually or call service.
            // Ideally call UserDetailsService but we are in Service layer.
            // Let's reuse logic or just pass user data if JwtService supports it.
            // JwtService needs UserDetails.
            // We can construct a simple UserDetails or call repository.
            // Since we have 'usuario', we can map it.
            // But CustomUserDetailsService is in 'security' package.
            // Let's just assume JwtService can handle it if we pass UserDetails.
            // We'll re-fetch UserDetails via repository logic (duplicated slightly) or inject UserDetailsService?
            // Circular dependency risk if UserDetailsService uses AuthService (unlikely).
            // Let's just map Usuario to UserDetails here locally or extract logic.
            // For simplicity, I'll just map it here.
            
            // ... (Mapping logic similar to CustomUserDetailsService) ...
            // Actually, JwtService.isTokenValid calls extractUsername.
            // We just need to generate new token.
            
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("empresaId", usuario.getEmpresaId());
             Set<String> roles = usuario.getFuncoes().stream()
                    .filter(f -> f.getAtivo())
                    .map(f -> "ROLE_" + f.getNome())
                    .collect(Collectors.toSet());
            Set<String> permissoes = usuario.getFuncoes().stream()
                    .filter(f -> f.getAtivo())
                    .flatMap(f -> f.getPermissoes().stream())
                    .map(p -> p.getValor())
                    .collect(Collectors.toSet());
            extraClaims.put("roles", roles);
            extraClaims.put("permissoes", permissoes);
            
            // Mock UserDetails for generation
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getSenha())
                    .authorities(roles.toArray(new String[0])) // Simplified
                    .build();

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String newAccessToken = jwtService.generateToken(extraClaims, userDetails);
                
                // Update Session
                sessao.setTokenSessao(newAccessToken);
                sessao.setUltimoAcesso(LocalDateTime.now());
                sessaoUsuarioRepository.save(sessao);
                
                logAccess(usuario, LogAcesso.TipoEvento.REFRESH_TOKEN, "Token atualizado", null, null);

                return LoginResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken) // Keep same refresh token or rotate? Usually rotate.
                        .usuarioId(usuario.getId())
                        .build();
            }
        }
        throw new RuntimeException("Token inválido");
    }

    @Transactional
    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            sessaoUsuarioRepository.findByTokenSessao(jwt).ifPresent(sessao -> {
                sessao.setAtivo(false);
                sessaoUsuarioRepository.save(sessao);
                logAccess(sessao.getUsuario(), LogAcesso.TipoEvento.LOGOUT, "Logout realizado", null, null);
            });
        }
    }

    @Transactional
    public void recoverPassword(String email, String sourceIp) {
        // Fail uniformly for all addresses if SMTP is not configured.
        // A missing provider must never look like a successfully sent message.
        if (!emailService.isPasswordResetConfigured()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Recuperação de acesso temporariamente indisponível");
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        // Persist requests for known AND unknown addresses outside this transaction.
        // 429 is scoped to the request IP, never to the existence of an e-mail.
        if (!recoveryRequestThrottle.checkAndRecord(normalizedEmail, sourceIp)) {
            return;
        }

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(normalizedEmail).orElse(null);
        if (usuario == null) {
            return; // indistinguishable response; do not reveal registered accounts
        }

        TenantContext.setCurrentTenant(usuario.getEmpresaId());
        // Keep the legacy per-user safety gate as additional defense.
        if (tokenRepository.countByUsuarioIdAndDataCriacaoAfter(
                usuario.getId(), LocalDateTime.now().minusMinutes(15)) >= 3) {
            return;
        }

        // Only one outstanding recovery URL per user. Never persist the raw token.
        for (TokenRecuperacaoSenha previous : tokenRepository.findByUsuarioIdAndUsadoFalse(usuario.getId())) {
            previous.setUsado(true);
            tokenRepository.save(previous);
        }

        byte[] random = new byte[32];
        SECURE_RANDOM.nextBytes(random);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(random);
        tokenRepository.saveAndFlush(TokenRecuperacaoSenha.builder()
                .token(hashToken(token))
                .usuario(usuario)
                .dataExpiracao(LocalDateTime.now().plusMinutes(30))
                .build());

        // SMTP send is synchronous so operational failures are observable.
        // A transient address-specific SMTP failure cannot be exposed as HTTP 503
        // without leaking whether the account exists; log it privately.
        try {
            emailService.sendPasswordResetLink(usuario.getEmail(), usuario.getNomeCompleto(), token);
        } catch (org.springframework.mail.MailException ex) {
            log.warn("Falha na entrega de recuperação de acesso: {}", ex.getClass().getSimpleName());
        }
        logAccess(usuario, LogAcesso.TipoEvento.PASSWORD_RECOVERY_REQUEST,
                "Solicitação de recuperação de acesso", sourceIp, null);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String newPassword = request.getNovaSenha();
        if (newPassword == null || newPassword.isBlank() ||
                newPassword.length() < 12 || newPassword.length() > 128 ||
                isPredictablePassword(newPassword)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Use uma frase-senha segura de 12 a 128 caracteres.");
        }
        if (request.getToken() == null || request.getToken().isBlank() ||
                request.getToken().length() > 256) {
            throw new IllegalArgumentException("Link de recuperação inválido");
        }

        // FOR UPDATE serializes concurrent attempts with the same one-time hash.
        TokenRecuperacaoSenha tokenEntity = tokenRepository.findWithLockByToken(hashToken(request.getToken()))
                .orElseThrow(() -> new IllegalArgumentException("Link de recuperação inválido"));

        if (Boolean.TRUE.equals(tokenEntity.getUsado())) {
            throw new IllegalStateException("Este link de recuperação já foi utilizado");
        }
        if (!tokenEntity.getDataExpiracao().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("O link de recuperação expirou. Solicite um novo");
        }

        Usuario usuario = tokenEntity.getUsuario();
        if (usuario == null || usuario.getEmpresaId() == null) {
            throw new IllegalStateException("Link de recuperação inválido");
        }
        TenantContext.setCurrentTenant(usuario.getEmpresaId());
        if (passwordEncoder.matches(newPassword, usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A nova senha deve ser diferente da senha atual.");
        }

        usuario.setSenha(passwordEncoder.encode(newPassword));
        usuario.setDeveTrocarSenha(false);
        usuarioRepository.saveAndFlush(usuario);

        // Atomic with password change: consume this token and any outstanding links.
        tokenEntity.setUsado(true);
        tokenRepository.saveAndFlush(tokenEntity);
        for (TokenRecuperacaoSenha pending : tokenRepository.findByUsuarioIdAndUsadoFalse(usuario.getId())) {
            pending.setUsado(true);
            tokenRepository.save(pending);
        }

        // Access validation and refresh both consult the active session record.
        for (SessaoUsuario sessao : sessaoUsuarioRepository.findByUsuarioIdAndAtivoTrue(usuario.getId())) {
            sessao.setAtivo(false);
            sessaoUsuarioRepository.save(sessao);
        }
        logAccess(usuario, LogAcesso.TipoEvento.PASSWORD_RESET_SUCCESS,
                "Senha redefinida e sessões anteriores revogadas", null, null);
    }

    private boolean isPredictablePassword(String candidate) {
        String compact = candidate.toLowerCase(Locale.ROOT).replace(" ", "");
        if (Set.of("123456789012", "1234567890123456", "password1234",
                "password123456", "qwerty123456", "senha12345678").contains(compact)) {
            return true;
        }
        return candidate.codePoints().distinct().count() == 1;
    }

    private String hashToken(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 indisponível", ex);
        }
    }

    private void createSession(Usuario usuario, String accessToken, String refreshToken, LoginRequest request) {
        SessaoUsuario sessao = SessaoUsuario.builder()
                .usuario(usuario)
                .tokenSessao(accessToken) // Storing full token or hash? Storing full for now as per prompt "token_sessao"
                .refreshToken(refreshToken)
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .dataInicio(LocalDateTime.now())

                .ultimoAcesso(LocalDateTime.now())
                .ativo(true)
                .empresaId(usuario.getEmpresaId()) // Explicitly set if needed, but TenantEntity handles it if context is set
                .build();
        sessaoUsuarioRepository.saveAndFlush(sessao);
    }

    private void checkLoginAttempts(String email) {
        LocalDateTime limitTime = LocalDateTime.now().minusMinutes(15);
        String key = email.trim().toLowerCase(Locale.ROOT);
        long attempts = tentativaLoginRepository.countByEmailAndSucessoFalseAndDataTentativaAfter(key, limitTime);
        if (attempts >= 5) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Muitas tentativas de login. Tente novamente mais tarde.");
        }
    }

    private void logLoginAttempt(LoginRequest request, boolean success) {
        TentativaLogin tentativa = TentativaLogin.builder()
                .email(request.getEmail())
                .ipAddress(request.getIpAddress() != null ? request.getIpAddress() : "unknown")
                .sucesso(success)
                .dataTentativa(LocalDateTime.now())
                .empresaId(TenantContext.getCurrentTenant()) // Might be null if not provided
                .build();
        tentativaLoginRepository.saveAndFlush(tentativa);
    }

    private void logAccess(Usuario usuario, LogAcesso.TipoEvento evento, String detalhes, String ip, String userAgent) {
        LogAcesso log = LogAcesso.builder()
                .usuario(usuario)
                .emailTentativa(usuario != null ? usuario.getEmail() : null)
                .tipoEvento(evento)
                .detalhes(detalhes)
                .ipAddress(ip)
                .userAgent(userAgent)
                .dataEvento(LocalDateTime.now())
                .empresaId(usuario != null ? usuario.getEmpresaId() : TenantContext.getCurrentTenant())
                .build();
        if (log.getEmpresaId() != null) {
            logAcessoRepository.saveAndFlush(log);
        }
    }
}
