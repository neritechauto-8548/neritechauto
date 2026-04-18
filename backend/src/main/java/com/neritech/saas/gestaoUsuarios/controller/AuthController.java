package com.neritech.saas.gestaoUsuarios.controller;

import com.neritech.saas.gestaoUsuarios.dto.LoginRequest;
import com.neritech.saas.gestaoUsuarios.dto.LoginResponse;
import com.neritech.saas.gestaoUsuarios.dto.RefreshTokenRequest;
import com.neritech.saas.gestaoUsuarios.service.AuthService;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.neritech.saas.common.tenancy.TenantContext;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para login, refresh token e gestão de sessão")
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica o usuário e retorna tokens de acesso")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletRequest servletRequest
    ) {
        if (request.getIpAddress() == null) request.setIpAddress(servletRequest.getRemoteAddr());
        if (request.getUserAgent() == null) request.setUserAgent(servletRequest.getHeader("User-Agent"));
        String tenantHeader = servletRequest.getHeader("X-Tenant-Id");
        if (tenantHeader != null && !tenantHeader.isEmpty()) {
            try { TenantContext.setCurrentTenant(Long.parseLong(tenantHeader)); } catch (NumberFormatException ignored) {}
        } else if (request.getEmail() != null) {
            usuarioRepository.findByEmail(request.getEmail()).ifPresent(u -> TenantContext.setCurrentTenant(u.getEmpresaId()));
        }
        try {
            return ResponseEntity.ok(authService.login(request));
        } finally {
            TenantContext.clear();
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Atualizar token", description = "Gera um novo access token a partir de um refresh token válido")
    public ResponseEntity<LoginResponse> refresh(@RequestBody @Valid RefreshTokenRequest request, HttpServletRequest servletRequest) {
        String tenantHeader = servletRequest.getHeader("X-Tenant-Id");
        if (tenantHeader != null && !tenantHeader.isEmpty()) {
            try { TenantContext.setCurrentTenant(Long.parseLong(tenantHeader)); } catch (NumberFormatException ignored) {}
        }
        try {
            return ResponseEntity.ok(authService.refreshToken(request));
        } finally {
            TenantContext.clear();
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalida a sessão atual")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader, HttpServletRequest servletRequest) {
        String tenantHeader = servletRequest.getHeader("X-Tenant-Id");
        if (tenantHeader != null && !tenantHeader.isEmpty()) {
            try { TenantContext.setCurrentTenant(Long.parseLong(tenantHeader)); } catch (NumberFormatException ignored) {}
        }
        try {
            authService.logout(authHeader);
            return ResponseEntity.noContent().build();
        } finally {
            TenantContext.clear();
        }
    }
}
