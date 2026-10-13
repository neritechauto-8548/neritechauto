# NERITECH AUTO — Prioridade 1 | Evidências de estabilização

**Branch obrigatória:** `feature/develop`  
**Escopo:** aplicação global, autenticação, sessão, permissões, isolamento multiempresa, navegação, estados globais e CI.  
**Regra de homologação:** não declarar 100% sem pipeline aprovado no HEAD da branch e evidência dos cenários essenciais. Nenhum teste de segurança pode ser ignorado para tornar o CI verde.

## Pipeline — evidência já aprovada

- Commit-base `f9715e3d787228c905fd79f18e030eee8cb1399d`.
- GitHub Actions: https://github.com/neritechauto-8548/neritechauto/actions/runs/38395977726
- Resultado confirmado: **frontend (build e testes) aprovado; backend Maven aprovado**.
- A contagem histórica do Maven antes dos novos cenários era 55 testes, 0 falhas, 0 erros.
- A aprovação acima **não homologa automaticamente commits posteriores**.

## Reforços implementados após o pipeline-base

1. `TenantEntity`: impede persistir registros com `empresaId` diferente do contexto autenticado.
2. `TenantInterceptor`: valida divergência entre cabeçalho e identidade JWT, rejeitando empresa incompatível com HTTP 403.
3. `JwtAuthenticationFilter`: limpa `TenantContext` também em requisições públicas sem bearer, inclusive falha durante login/refresh.
4. `AuthServiceTest`: rejeição da renovação com sessão inativa ou vinculada a outra empresa.
5. `CustomUserDetailsService` e `StartupService`: somente `ADMIN`, `ADMINISTRADOR` e `SUPER_ADMIN` ativam privilégios administrativos implícitos; nomes contendo o texto `ADMIN` não bastam.
6. Testes de permissões, planos, estados globais, rotas e interceptors incluídos na cobertura do workflow.
7. Integração `AuthenticationFlowIntegrationTest`: solicita acesso com JWT de uma empresa e cabeçalho de outra, exigindo HTTP 403.

## Evidências automatizadas no repositório

- `backend/src/test/java/com/neritech/saas/common/tenancy/TenantContextTest.java`
- `backend/src/test/java/com/neritech/saas/common/tenancy/TenantInterceptorTest.java`
- `backend/src/test/java/com/neritech/saas/security/JwtAuthenticationFilterTest.java`
- `backend/src/test/java/com/neritech/saas/security/CustomUserDetailsServiceTest.java`
- `backend/src/test/java/com/neritech/saas/gestaoUsuarios/service/AuthServiceTest.java`
- `backend/src/test/java/com/neritech/saas/gestaoUsuarios/integration/AuthenticationFlowIntegrationTest.java`
- `backend/src/test/java/com/neritech/saas/gestaoUsuarios/integration/MultitenancyIntegrationTest.java`
- `FrontEnd/src/app/core/bootstrap/startup-permissions.spec.ts`
- `FrontEnd/src/app/core/authentication/auth.service.spec.ts`
- `FrontEnd/src/app/core/authentication/auth-guard.spec.ts`
- `FrontEnd/src/app/core/interceptors/tenant-interceptor.spec.ts`
- `FrontEnd/src/app/core/interceptors/error-interceptor.spec.ts`
- `FrontEnd/src/app/theme/sidemenu/preview-menu.spec.ts`
- `FrontEnd/src/app/theme/widgets/notification-button.spec.ts`

## Pendências antes do aceite final de 100%

- [ ] Pipeline Maven + Angular aprovado **no mesmo HEAD atual**, incluindo todos os testes novos.
- [ ] Integração completa de login, refresh, logout, revogação e expiração aprovada.
- [ ] Isolamento entre empresas comprovado por testes negativos HTTP e persistência.
- [ ] Matriz de perfis/permissões validada na navegação e nas APIs.
- [ ] Estados de carregamento, erro, vazio e acesso negado confirmados.
- [ ] Ambiente de homologação (fora dos mocks) validado com credenciais de teste e dados de múltiplas empresas, incluindo UX desktop, tablet e celular.
- [ ] Evidências, resultados e responsável pelo aceite final registrados.

**Status deste documento:** relatório de evidências, não certificado de homologação. Atualizar o resultado do HEAD antes de concluir a etapa.
