package com.neritech.saas.gestaoUsuarios.controller;

import com.neritech.saas.gestaoUsuarios.dto.LoginRequest;
import com.neritech.saas.gestaoUsuarios.dto.LoginResponse;
import com.neritech.saas.gestaoUsuarios.dto.RecoverPasswordRequest;
import com.neritech.saas.gestaoUsuarios.dto.RefreshTokenRequest;
import com.neritech.saas.gestaoUsuarios.dto.ResetPasswordRequest;
import com.neritech.saas.gestaoUsuarios.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para login, refresh token e gestão de sessão")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica o usuário e retorna tokens de acesso")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletRequest servletRequest
    ) {
        if (request.getIpAddress() == null) {
            request.setIpAddress(servletRequest.getRemoteAddr());
        }
        if (request.getUserAgent() == null) {
            request.setUserAgent(servletRequest.getHeader("User-Agent"));
        }
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Atualizar token", description = "Gera um novo access token a partir de um refresh token válido")
    public ResponseEntity<LoginResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalida a sessão atual")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recover-password")
    @Operation(summary = "Solicitar recuperação de senha", description = "Envia um link de recuperação por e-mail")
    public ResponseEntity<Void> recoverPassword(@RequestBody @Valid RecoverPasswordRequest request) {
        authService.recoverPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Redefinir senha", description = "Atualiza a senha do usuário usando o token enviado por e-mail")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
