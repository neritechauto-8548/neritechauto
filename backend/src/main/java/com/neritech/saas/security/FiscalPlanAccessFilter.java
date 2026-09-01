package com.neritech.saas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class FiscalPlanAccessFilter extends OncePerRequestFilter {

    private static final Pattern ORDEM_SERVICO_NFE_PATH = Pattern.compile(
            "^/v1/ordens-servico/(?:\\d+/nfe(?:/.*)?|nfe/\\d+(?:/.*)?)$"
    );

    private final PlanAccessService planAccessService;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return !isFiscalPath(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Deixe o Spring Security responder 401 para requisições sem autenticação.
        // A regra comercial só entra em cena para um usuário já autenticado.
        if (authentication != null
                && authentication.isAuthenticated()
                && !planAccessService.hasFiscalAccess()) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "O módulo Fiscal não está disponível no plano atual."
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isFiscalPath(String requestUri) {
        if (requestUri == null) {
            return false;
        }

        return requestUri.equals("/v1/fiscal")
                || requestUri.startsWith("/v1/fiscal/")
                || ORDEM_SERVICO_NFE_PATH.matcher(requestUri).matches();
    }
}
