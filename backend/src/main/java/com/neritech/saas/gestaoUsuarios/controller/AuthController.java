package com.neritech.saas.gestaoUsuarios.controller;

import com.neritech.saas.gestaoUsuarios.dto.LoginRequest;
import com.neritech.saas.gestaoUsuarios.dto.LoginResponse;
import com.neritech.saas.gestaoUsuarios.dto.RefreshTokenRequest;
import com.neritech.saas.gestaoUsuarios.dto.RecoverPasswordRequest;
import com.neritech.saas.gestaoUsuarios.dto.ResetPasswordRequest;
import com.neritech.saas.gestaoUsuarios.service.AuthService;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
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
        // Client-supplied JSON metadata must not spoof the request identity.
        request.setIpAddress(servletRequest.getRemoteAddr());
        request.setUserAgent(servletRequest.getHeader("User-Agent"));
        if (request.getEmail() != null) {
            usuarioRepository.findByEmailIgnoreCase(request.getEmail()).ifPresent(u -> TenantContext.setCurrentTenant(u.getEmpresaId()));
        }
        try {
            return ResponseEntity.ok(authService.login(request));
        } finally {
            TenantContext.clear();
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Atualizar token", description = "Gera um novo access token a partir de um refresh token válido")
    public ResponseEntity<LoginResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            return ResponseEntity.ok(authService.refreshToken(request));
        } finally {
            TenantContext.clear();
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalida a sessão atual")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            authService.logout(authHeader);
            return ResponseEntity.noContent().build();
        } finally {
            TenantContext.clear();
        }
    }

    @PostMapping("/recover-password")
    @Operation(summary = "Solicitar recuperação de senha", description = "Envia um link de recuperação por e-mail")
    public ResponseEntity<Void> recoverPassword(@RequestBody @Valid RecoverPasswordRequest request) {
        try {
            authService.recoverPassword(request.getEmail());
            return ResponseEntity.ok().build();
        } finally {
            TenantContext.clear();
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Redefinir senha", description = "Atualiza a senha do usuário usando o token enviado por e-mail")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        try {
            authService.resetPassword(request);
            return ResponseEntity.ok().build();
        } finally {
            TenantContext.clear();
        }
    }
}
