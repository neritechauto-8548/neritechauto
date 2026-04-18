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
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final SessaoUsuarioRepository sessaoUsuarioRepository;
    private final LogAcessoRepository logAcessoRepository;
    private final TentativaLoginRepository tentativaLoginRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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
            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
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
                    .map(p -> p.getNome())
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

        } catch (BadCredentialsException e) {
            try {
                usuarioRepository.findByEmail(request.getEmail()).ifPresent(u -> TenantContext.setCurrentTenant(u.getEmpresaId()));
                logLoginAttempt(request, false);
                logAccess(null, LogAcesso.TipoEvento.LOGIN_FAIL, "Falha de login: credenciais inválidas para " + request.getEmail(), request.getIpAddress(), request.getUserAgent());
            } finally {
                TenantContext.clear();
            }
            throw e;
        } catch (Exception e) {
            try {
                usuarioRepository.findByEmail(request.getEmail()).ifPresent(u -> TenantContext.setCurrentTenant(u.getEmpresaId()));
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
            Usuario usuario = usuarioRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            // Validate token matches session
            SessaoUsuario sessao = sessaoUsuarioRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new RuntimeException("Sessão inválida ou expirada"));

            if (!sessao.getAtivo()) {
                throw new RuntimeException("Sessão inativa");
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
                    .map(p -> p.getNome())
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
        sessaoUsuarioRepository.save(sessao);
    }

    private void checkLoginAttempts(String email) {
        LocalDateTime limitTime = LocalDateTime.now().minusMinutes(15);
        long attempts = tentativaLoginRepository.countByEmailAndSucessoFalseAndDataTentativaAfter(email, limitTime);
        if (attempts >= 5) {
            throw new RuntimeException("Muitas tentativas de login. Tente novamente mais tarde.");
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
        tentativaLoginRepository.save(tentativa);
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
            logAcessoRepository.save(log);
        }
    }
}
