package com.neritech.saas.gestaoUsuarios.service;

import com.neritech.saas.common.mail.EmailService;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.LogAcesso;
import com.neritech.saas.gestaoUsuarios.domain.SessaoUsuario;
import com.neritech.saas.gestaoUsuarios.domain.TentativaLogin;
import com.neritech.saas.gestaoUsuarios.domain.TokenRecuperacaoSenha;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.LoginRequest;
import com.neritech.saas.gestaoUsuarios.dto.LoginResponse;
import com.neritech.saas.gestaoUsuarios.dto.RefreshTokenRequest;
import com.neritech.saas.gestaoUsuarios.dto.ResetPasswordRequest;
import com.neritech.saas.gestaoUsuarios.repository.LogAcessoRepository;
import com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository;
import com.neritech.saas.gestaoUsuarios.repository.TentativaLoginRepository;
import com.neritech.saas.gestaoUsuarios.repository.TokenRecuperacaoSenhaRepository;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final SessaoUsuarioRepository sessaoUsuarioRepository;
    private final LogAcessoRepository logAcessoRepository;
    private final TentativaLoginRepository tentativaLoginRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRecuperacaoSenhaRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        checkLoginAttempts(request.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            Set<String> roles = resolveRoles(usuario);
            Set<String> permissoes = resolvePermissions(usuario);
            Map<String, Object> extraClaims = buildAccessClaims(usuario, roles, permissoes);

            String accessToken = jwtService.generateToken(extraClaims, userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);
            boolean primeiroAcesso = usuario.getUltimoAcesso() == null;

            TenantContext.setCurrentTenant(usuario.getEmpresaId());
            try {
                createSession(usuario, accessToken, refreshToken, request);
                logLoginAttempt(request, true);
                logAccess(usuario, LogAcesso.TipoEvento.LOGIN_SUCCESS,
                        "Login realizado com sucesso", request.getIpAddress(), request.getUserAgent());

                usuario.setUltimoAcesso(LocalDateTime.now());
                usuarioRepository.save(usuario);

                return LoginResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .usuarioId(usuario.getId())
                        .empresaId(usuario.getEmpresaId())
                        .nomeCompleto(usuario.getNomeCompleto())
                        .email(usuario.getEmail())
                        .roles(roles)
                        .permissoes(permissoes)
                        .expiraEm(accessTokenExpiresAt())
                        .expiresIn(jwtService.getExpirationTime())
                        .primeiroAcesso(primeiroAcesso)
                        .deveTrocarSenha(usuario.getDeveTrocarSenha())
                        .build();
            } finally {
                TenantContext.clear();
            }

        } catch (BadCredentialsException e) {
            recordFailedLogin(request, "Falha de login: credenciais inválidas");
            throw e;
        } catch (Exception e) {
            recordFailedLogin(request, "Erro interno durante autenticação");
            throw e;
        }
    }

    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String currentRefreshToken = request.getRefreshToken();
        String userEmail = jwtService.extractUsername(currentRefreshToken);
        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalArgumentException("Token de refresh inválido");
        }

        SessaoUsuario sessao = sessaoUsuarioRepository.findByRefreshTokenAndAtivoTrue(currentRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Sessão inválida, revogada ou expirada"));

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (sessao.getUsuario() == null || !usuario.getId().equals(sessao.getUsuario().getId())) {
            throw new IllegalArgumentException("Refresh token não pertence ao usuário autenticado");
        }
        if (!usuario.getEmpresaId().equals(sessao.getEmpresaId())) {
            throw new IllegalArgumentException("Refresh token não pertence ao tenant atual do usuário");
        }
        if (!Boolean.TRUE.equals(usuario.getAtivo()) || Boolean.TRUE.equals(usuario.getBloqueado())) {
            throw new IllegalArgumentException("Usuário inativo ou bloqueado");
        }
        if (sessao.getDataExpiracao() != null && sessao.getDataExpiracao().isBefore(LocalDateTime.now())) {
            sessao.setAtivo(false);
            TenantContext.setCurrentTenant(sessao.getEmpresaId());
            try {
                sessaoUsuarioRepository.save(sessao);
            } finally {
                TenantContext.clear();
            }
            throw new IllegalArgumentException("Sessão expirada");
        }

        Set<String> roles = resolveRoles(usuario);
        Set<String> permissoes = resolvePermissions(usuario);
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .disabled(!Boolean.TRUE.equals(usuario.getAtivo()))
                .accountLocked(Boolean.TRUE.equals(usuario.getBloqueado()))
                .authorities(roles.toArray(new String[0]))
                .build();

        if (!jwtService.isTokenValid(currentRefreshToken, userDetails)) {
            throw new IllegalArgumentException("Token de refresh inválido");
        }

        Map<String, Object> extraClaims = buildAccessClaims(usuario, roles, permissoes);
        String newAccessToken = jwtService.generateToken(extraClaims, userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        TenantContext.setCurrentTenant(usuario.getEmpresaId());
        try {
            // Rotação atômica: o access token anterior deixa de corresponder à sessão
            // ativa e o refresh token anterior deixa de ser aceito para novos refreshes.
            sessao.setTokenSessao(newAccessToken);
            sessao.setRefreshToken(newRefreshToken);
            sessao.setUltimoAcesso(LocalDateTime.now());
            sessao.setDataExpiracao(refreshTokenExpiresAt());
            sessaoUsuarioRepository.saveAndFlush(sessao);

            logAccess(usuario, LogAcesso.TipoEvento.REFRESH_TOKEN, "Sessão renovada com rotação de token", null, null);

            return LoginResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .usuarioId(usuario.getId())
                    .empresaId(usuario.getEmpresaId())
                    .nomeCompleto(usuario.getNomeCompleto())
                    .email(usuario.getEmail())
                    .roles(roles)
                    .permissoes(permissoes)
                    .expiraEm(accessTokenExpiresAt())
                    .expiresIn(jwtService.getExpirationTime())
                    .primeiroAcesso(false)
                    .deveTrocarSenha(usuario.getDeveTrocarSenha())
                    .build();
        } finally {
            TenantContext.clear();
        }
    }

    @Transactional
    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String jwt = authHeader.substring(7);
        sessaoUsuarioRepository.findByTokenSessao(jwt).ifPresent(sessao -> {
            TenantContext.setCurrentTenant(sessao.getEmpresaId());
            try {
                sessao.setAtivo(false);
                sessaoUsuarioRepository.save(sessao);
                logAccess(sessao.getUsuario(), LogAcesso.TipoEvento.LOGOUT, "Logout realizado", null, null);
            } finally {
                TenantContext.clear();
            }
        });
    }

    @Transactional
    public void recoverPassword(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElse(null);

        // Resposta anti-enumeração: inexistência do e-mail não muda o contrato externo.
        if (usuario == null) {
            return;
        }

        TenantContext.setCurrentTenant(usuario.getEmpresaId());
        try {
            String token = UUID.randomUUID().toString();

            TokenRecuperacaoSenha tokenEntity = TokenRecuperacaoSenha.builder()
                    .token(token)
                    .usuario(usuario)
                    .dataExpiracao(LocalDateTime.now().plusMinutes(30))
                    .build();

            tokenRepository.saveAndFlush(tokenEntity);
            emailService.sendPasswordResetLink(usuario.getEmail(), usuario.getNomeCompleto(), token);
            logAccess(usuario, LogAcesso.TipoEvento.PASSWORD_RECOVERY_REQUEST,
                    "Solicitação de recuperação de senha iniciada", null, null);
        } finally {
            TenantContext.clear();
        }
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        TokenRecuperacaoSenha tokenEntity = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Token de recuperação inválido ou inexistente"));

        if (Boolean.TRUE.equals(tokenEntity.getUsado())) {
            throw new IllegalArgumentException("Este link de recuperação já foi utilizado");
        }

        if (tokenEntity.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("O link de recuperação expirou. Por favor, solicite um novo");
        }

        Usuario usuario = tokenEntity.getUsuario();
        TenantContext.setCurrentTenant(usuario.getEmpresaId());
        try {
            usuario.setSenha(passwordEncoder.encode(request.getNovaSenha()));
            usuario.setDeveTrocarSenha(false);
            usuarioRepository.saveAndFlush(usuario);

            tokenEntity.setUsado(true);
            tokenRepository.saveAndFlush(tokenEntity);

            // Redefinir a senha revoga todas as sessões existentes do usuário.
            sessaoUsuarioRepository.findByUsuarioIdAndAtivoTrue(usuario.getId()).forEach(sessao -> {
                sessao.setAtivo(false);
                sessaoUsuarioRepository.save(sessao);
            });

            logAccess(usuario, LogAcesso.TipoEvento.PASSWORD_RESET_SUCCESS,
                    "Senha redefinida com sucesso via link de e-mail", null, null);
        } finally {
            TenantContext.clear();
        }
    }

    private void createSession(Usuario usuario, String accessToken, String refreshToken, LoginRequest request) {
        SessaoUsuario sessao = SessaoUsuario.builder()
                .usuario(usuario)
                .tokenSessao(accessToken)
                .refreshToken(refreshToken)
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .dataInicio(LocalDateTime.now())
                .ultimoAcesso(LocalDateTime.now())
                .dataExpiracao(refreshTokenExpiresAt())
                .ativo(true)
                .empresaId(usuario.getEmpresaId())
                .build();
        sessaoUsuarioRepository.saveAndFlush(sessao);
    }

    private Map<String, Object> buildAccessClaims(
            Usuario usuario,
            Set<String> roles,
            Set<String> permissoes) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("empresaId", usuario.getEmpresaId());
        claims.put("userId", usuario.getId());
        claims.put("roles", roles);
        claims.put("permissoes", permissoes);
        return claims;
    }

    private Set<String> resolveRoles(Usuario usuario) {
        if (usuario.getFuncoes() == null) {
            return Set.of();
        }
        return usuario.getFuncoes().stream()
                .filter(f -> Boolean.TRUE.equals(f.getAtivo()))
                .map(f -> "ROLE_" + f.getNome())
                .collect(Collectors.toSet());
    }

    private Set<String> resolvePermissions(Usuario usuario) {
        if (usuario.getFuncoes() == null) {
            return Set.of();
        }
        return usuario.getFuncoes().stream()
                .filter(f -> Boolean.TRUE.equals(f.getAtivo()))
                .filter(f -> f.getPermissoes() != null)
                .flatMap(f -> f.getPermissoes().stream())
                .filter(p -> p.getValor() != null && !p.getValor().isBlank())
                .map(p -> p.getValor())
                .collect(Collectors.toSet());
    }

    private LocalDateTime accessTokenExpiresAt() {
        return LocalDateTime.now().plus(Duration.ofMillis(jwtService.getExpirationTime()));
    }

    private LocalDateTime refreshTokenExpiresAt() {
        return LocalDateTime.now().plus(Duration.ofMillis(jwtService.getRefreshExpirationTime()));
    }

    private void checkLoginAttempts(String email) {
        LocalDateTime limitTime = LocalDateTime.now().minusMinutes(15);
        long attempts = tentativaLoginRepository.countByEmailAndSucessoFalseAndDataTentativaAfter(email, limitTime);
        if (attempts >= 5) {
            throw new IllegalStateException("Muitas tentativas de login. Tente novamente mais tarde.");
        }
    }

    private void recordFailedLogin(LoginRequest request, String details) {
        try {
            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(request.getEmail()).orElse(null);
            if (usuario != null) {
                TenantContext.setCurrentTenant(usuario.getEmpresaId());
            } else {
                TenantContext.clear();
            }
            logLoginAttempt(request, false);
            logAccess(null, LogAcesso.TipoEvento.LOGIN_FAIL, details, request.getIpAddress(), request.getUserAgent());
        } finally {
            TenantContext.clear();
        }
    }

    private void logLoginAttempt(LoginRequest request, boolean success) {
        TentativaLogin tentativa = TentativaLogin.builder()
                .email(request.getEmail())
                .ipAddress(request.getIpAddress() != null ? request.getIpAddress() : "unknown")
                .sucesso(success)
                .dataTentativa(LocalDateTime.now())
                .empresaId(TenantContext.getCurrentTenant())
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
