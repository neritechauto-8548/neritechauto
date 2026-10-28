package com.neritech.saas.security;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.empresa.domain.enums.StatusAssinatura;
import com.neritech.saas.empresa.repository.AssinaturaEmpresaRepository;
import com.neritech.saas.trial.service.TrialPolicy;
import com.neritech.saas.gestaoUsuarios.repository.SessaoUsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final SessaoUsuarioRepository sessaoUsuarioRepository;
    private final AssinaturaEmpresaRepository assinaturaEmpresaRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        Long validatedEmpresaId = null;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            try {
                filterChain.doFilter(request, response);
            } finally {
                // Fluxos públicos (inclusive login/refresh) também podem definir
                // TenantContext no serviço; nunca reutilizar esse valor na thread.
                TenantContext.clear();
            }
            return;
        }

        jwt = authHeader.substring(7);
        
        try {
            // Extract email and tenant from token
            userEmail = jwtService.extractUsername(jwt);
            
            // O claim da empresa só pode entrar no contexto após validar JWT,
            // sessão ativa e pertencimento real do usuário ao tenant.
            Long empresaId = jwtService.extractClaim(jwt, claims -> claims.get("empresaId", Long.class));

            if (userEmail != null && empresaId != null && empresaId > 0
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                boolean validTenantSession = jwtService.isTokenValid(jwt, userDetails)
                        && sessaoUsuarioRepository.findByTokenSessao(jwt)
                            .filter(sessao -> Boolean.TRUE.equals(sessao.getAtivo())
                                    && empresaId.equals(sessao.getEmpresaId())
                                    && sessao.getUsuario() != null
                                    && empresaId.equals(sessao.getUsuario().getEmpresaId())
                                    && userEmail.equalsIgnoreCase(sessao.getUsuario().getEmail()))
                            .isPresent();
                if (validTenantSession) {
                    TenantContext.setCurrentTenant(empresaId);
                    validatedEmpresaId = empresaId;
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // If token is invalid, we just ignore it and let the request continue
            // SecurityConfig will handle whether the request is allowed or not
            logger.warn("JWT validation failed: " + e.getMessage());
        }
        
        try {
            if (validatedEmpresaId != null && !isSubscriptionRoute(request.getServletPath())
                    && assinaturaEmpresaRepository.findFirstByEmpresaIdOrderByDataFimDesc(validatedEmpresaId)
                        .filter(assinatura -> assinatura.getStatus() == StatusAssinatura.TESTE)
                        .filter(TrialPolicy::isExpired).isPresent()) {
                response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, "Período de teste encerrado.");
                return;
            }
            filterChain.doFilter(request, response);
        } finally {
            // Clear tenant context after request
            TenantContext.clear();
        }
    }

    private boolean isSubscriptionRoute(String path) {
        return path.equals("/usuarios/me") || path.equals("/usuarios/menu")
                || path.startsWith("/v1/assinatura/") || path.equals("/v1/assinatura")
                || path.startsWith("/auth/");
    }
}
