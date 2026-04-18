## Visão Geral Atual
- Frontend:
  - Formulário reativo de login em `src/app/routes/sessions/login/login.ts:40` que chama `AuthService.login` e redireciona ao sucesso.
  - Serviço de autenticação em `src/app/core/authentication/auth.service.ts:38` delegando para `LoginService` e preenchendo token em `TokenService`.
  - `LoginService` chama o backend em `/api/auth/login` e normaliza o payload para `Token` (`access_token`, `refresh_token`, `expires_in`) em `src/app/core/authentication/login.service.ts:14-30`.
  - Interceptores ativos via `app.config.ts:43` e `src/app/core/interceptors/index.ts:19-29`: `tenant-interceptor` adiciona `X-Tenant-Id` (`src/app/core/interceptors/tenant-interceptor.ts:6-22`), `token-interceptor` adiciona `Authorization` (`src/app/core/interceptors/token-interceptor.ts:58-66`) e `error-interceptor` trata 401/422 (`src/app/core/interceptors/error-interceptor.ts:67-115`).
  - `BASE_URL` é `/api` em dev (`src/environments/environment.ts:7`) com proxy para `http://localhost:8080` (`proxy.config.js:13-21`), e absoluto em prod (`src/environments/environment.prod.ts:3`).
- Backend:
  - Context-path `/api` em dev/prod (`src/main/resources/application-dev.yml:27-31`, `application-prod.yml:53-57`), endpoints públicos `"/auth/login"` e `"/auth/refresh"` em `src/main/java/com/neritech/saas/security/SecurityConfig.java:42-49`.
  - Filtro JWT extrai `empresaId` do token (`src/main/java/com/neritech/saas/security/JwtAuthenticationFilter.java:47-50`) e valida usuário (`55-65`).
  - Geração de tokens com claims extras (roles, permissões, empresa) em `src/main/java/com/neritech/saas/gestaoUsuarios/service/AuthService.java:59-79` e resposta `LoginResponse` camelCase (`src/main/java/com/neritech/saas/gestaoUsuarios/dto/LoginResponse.java:17-27`).
  - CORS permite `Authorization` e `X-Tenant-Id` (`SecurityConfig.java:82-85`).
  - Erros formatados em `ApiResponse` (`src/main/java/com/neritech/saas/common/dto/ApiResponse.java`).

## Problemas Identificados
- Header `Authorization` é anexado até em `/auth/login`, podendo enviar valor vazio (`token-interceptor.ts:58-66`).
- Tratamento de 422 no frontend espera `errors` como mapa de campos, mas backend retorna lista de strings (`GlobalExceptionHandler.java:31-38`), causando falha ao atribuir erros ao formulário (`login.ts:69-76`).
- `BadCredentialsException` no login tende a virar 500 (genérico) em vez de 401, prejudicando UX e fluxo (`AuthService.java:104-111`, `GlobalExceptionHandler.java:109-114`).
- CORS não define `allowCredentials`, podendo afetar cenários sem proxy; hoje funciona com proxy, mas vale robustez (`SecurityConfig.java:80-88`).

## Correções Planejadas
- Frontend
  - Ajustar `token-interceptor` para:
    - Não anexar `Authorization` se `getBearerToken()` estiver vazio.
    - Pular `/auth/login` e `/auth/refresh` explicitamente.
  - Ajustar `login.ts` para suportar ambos formatos de 422:
    - Se `errors` for `string[]`, mostrar toasts e setar erro geral no formulário.
    - Se for objeto com campos, manter lógica atual.
  - Manter `tenant-interceptor` como fonte única do `X-Tenant-Id` (remover redundância em `LoginService` se necessário após teste; não obrigatório).
- Backend
  - Adicionar handler dedicado para `BadCredentialsException` retornando 401 com `ApiResponse.error("Credenciais inválidas")` em `GlobalExceptionHandler`.
  - Opcional: `CorsConfiguration` com `setAllowCredentials(true)` e `setAllowedOrigins` explícitas no ambiente produtivo.
  - Validar `requestMatchers` com context-path `/api`; manter `"/auth/**"` pois Spring aplica matcher relativo ao contexto, mas adicionar `"/swagger-ui/**"` e `"/api-docs/**"` já está coberto.

## Validação End-to-End
- Dev
  - Com `npm run start` (proxy ativo) e backend em `8080`:
    - Login com credenciais válidas: verificar `POST /api/auth/login` sem `Authorization`, com `X-Tenant-Id`; receber `accessToken/refreshToken` camelCase; token armazenado (`TokenService`).
    - Navegar a rota protegida: requests carregam `Authorization: Bearer ...` e `X-Tenant-Id`; respostas OK.
    - Credenciais inválidas: backend 401; frontend mostra toast "Credenciais inválidas" e permanece em login.
    - 422 validação: backend retorna `errors: string[]`; frontend exibe toasts e marca erro no formulário.
    - Refresh automático: `TokenService.refresh()` dispara próximo ao exp, backend retorna novo `accessToken`.
- Prod
  - Sem proxy, com `environment.prod.baseUrl` absoluto:
    - Verificar CORS e `withCredentials`; validar headers e respostas.

## Entregáveis
- Patches no frontend: `token-interceptor.ts`, `login.ts`.
- Patches no backend: `GlobalExceptionHandler.java` (novo handler 401), opcional `SecurityConfig.java` (CORS credenciais).
- Checklist de testes documentado e execução guiada.

## Observações de Segurança
- Trocar `secretKey` hardcoded por variável segura (`application-prod.yml:49-52` x `JwtService.java:21-22`).
- Confirmar hashing BCrypt (`SecurityConfig.java:75-77`).

Confirma que posso aplicar as alterações e rodar os testes para validar o fluxo completo?