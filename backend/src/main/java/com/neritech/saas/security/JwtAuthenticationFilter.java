package com.neritech.saas.security;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            String userEmail = jwtService.extractUsername(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    Usuario usuario = usuarioRepository.findByEmailIgnoreCase(userEmail)
                            .orElseThrow(() -> new IllegalStateException("Usuario autenticado nao encontrado"));

                    Long tenantFromToken = jwtService.extractClaim(jwt, claims -> claims.get("empresaId", Long.class));
                    Long tenantFromIdentity = usuario.getEmpresaId();

                    if (tenantFromIdentity == null) {
                        throw new IllegalStateException("Usuario autenticado sem empresa ativa");
                    }
                    if (tenantFromToken != null && !tenantFromIdentity.equals(tenantFromToken)) {
                        throw new IllegalStateException("Contexto de empresa do token nao corresponde ao vinculo atual");
                    }

                    // A fonte autoritativa e o vinculo atual do usuario carregado do backend.
                    TenantContext.setCurrentTenant(tenantFromIdentity);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token invalido, expirado ou com contexto inconsistente nunca estabelece identidade/tenant.
            TenantContext.clear();
            SecurityContextHolder.clearContext();
            logger.warn("JWT validation failed: " + e.getClass().getSimpleName());
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
