# NERITECH AUTO — Prioridade 1 | Evidências de estabilização

**Branch obrigatória:** `feature/develop`  
**Escopo:** aplicação global, autenticação, sessão, permissões, isolamento multiempresa, navegação, estados globais e CI.  
**Regra de homologação:** não declarar 100% sem pipeline aprovado no HEAD da branch e evidência dos cenários essenciais. Nenhum teste de segurança pode ser ignorado para tornar o CI verde.

## Pipeline — evidência já aprovada

- Último commit de código verificado: `54413a006b7b0728c568f50dd6f2fff1a24a5704`.
- GitHub Actions: https://github.com/neritechauto-8548/neritechauto/actions/runs/38402675542
- **Maven: BUILD SUCCESS; 68 testes, zero falhas, zero erros, zero ignorados.**
- **Angular: build aprovado; 67 testes de regressão aprovados (TOTAL: 67 SUCCESS).**
- **Total: 135 de 135 testes executados com sucesso.**
- Marco de referência anterior: `f9715e3d787228c905fd79f18e030eee8cb1399d`, https://github.com/neritechauto-8548/neritechauto/actions/runs/38395977726.
- Commits posteriores de documentação não alteram a evidência de compilação do código verificado, mas qualquer nova alteração de código exige CI atualizado.

## Reforços implementados após o pipeline-base

1. `TenantEntity`: impede persistir registros com `empresaId` diferente do contexto autenticado.
2. `TenantInterceptor`: valida divergência entre cabeçalho e identidade JWT, rejeitando empresa incompatível com HTTP 403.
3. `JwtAuthenticationFilter`: limpa `TenantContext` também em requisições públicas sem bearer, inclusive falha durante login/refresh.
4. `AuthServiceTest`: rejeição da renovação com sessão inativa ou vinculada a outra empresa.
5. `CustomUserDetailsService` e `StartupService`: somente `ADMIN`, `ADMINISTRADOR` e `SUPER_ADMIN` ativam privilégios administrativos implícitos; nomes contendo o texto `ADMIN` não bastam.
6. Testes de permissões, planos, estados globais, rotas e interceptors incluídos na cobertura do workflow.
7. Integração `AuthenticationFlowIntegrationTest`: solicita acesso com JWT de uma empresa e cabeçalho de outra, exigindo HTTP 403.
8. `AuthService` no frontend descarta dados do usuário/empresa anterior ao iniciar novo login e remove identificação da empresa quando a sessão deixa de ser válida.
9. Workflow ampliado para alterações em fontes gerais, configurações de build (`pom.xml`, `package.json`, `angular.json`, `yarn.lock`) e testes de autenticação após troca de usuário.
10. `AuthService` encerra o acesso local mesmo quando a revogação HTTP falha; o perfil tolera respostas acima de 1 segundo (limite de 10 segundos).
11. `StartupService` limpa o menu ao falhar o carregamento e mantém a assinatura de autenticação ativa para novas tentativas.
12. `errorInterceptor` diferencia HTTP 401 de API interna e 401 de serviço externo, preservando a sessão no segundo caso.

## Evidências automatizadas no repositório

- `backend/src/test/java/com/neritech/saas/common/tenancy/TenantContextTest.java`
- `backend/src/test/java/com/neritech/saas/common/tenancy/TenantInterceptorTest.java`
- `backend/src/test/java/com/neritech/saas/security/JwtAuthenticationFilterTest.java`
- `backend/src/test/java/com/neritech/saas/security/CustomUserDetailsServiceTest.java`
- `backend/src/test/java/com/neritech/saas/gestaoUsuarios/service/AuthServiceTest.java`
- `backend/src/test/java/com/neritech/saas/gestaoUsuarios/integration/AuthenticationFlowIntegrationTest.java`
- `backend/src/test/java/com/neritech/saas/gestaoUsuarios/integration/MultitenancyIntegrationTest.java`
- `FrontEnd/src/app/core/bootstrap/startup-permissions.spec.ts`
- `FrontEnd/src/app/core/authentication/permission-guard.spec.ts`
- `FrontEnd/src/app/shared/components/data-view-state/data-view-state.spec.ts`
- `FrontEnd/src/app/core/authentication/auth.service.spec.ts`
- `FrontEnd/src/app/core/authentication/auth-guard.spec.ts`
- `FrontEnd/src/app/core/interceptors/tenant-interceptor.spec.ts`
- `FrontEnd/src/app/core/interceptors/error-interceptor.spec.ts`
- `FrontEnd/src/app/theme/sidemenu/preview-menu.spec.ts`
- `FrontEnd/src/app/theme/widgets/notification-button.spec.ts`

## Matriz de homologação manual (execução pendente)

Esta matriz é **roteiro de execução** e não comprovação de testes já realizados. Usar ambiente implantado, conectado ao backend verdadeiro, com dados descartáveis e pelo menos duas empresas independentes. Nenhuma credencial, JWT, dado pessoal ou segredo deve ser registrado em prints/logs de evidência.

Preencher no aceite: ambiente/URL `[pendente]` · SHA implantado `[pendente]` · navegadores/dispositivos `[pendente]` · data `[pendente]` · responsável `[pendente]`.

| ID | Cenário obrigatório | Resultado | Evidência |
| --- | --- | --- | --- |
| P1-01 | Login válido/inválido, recuperar senha, expiração e renovação de token, refresh revogado, logout e retorno seguro à URL interna solicitada | Não executado | — |
| P1-02 | Desconectar rede durante logout: token/tenant/perfil locais apagados, tela de login exibida, erro de revogação não ocultado | Não executado | — |
| P1-03 | Perfis distintos (administrador, atendimento, mecânico, financeiro): menu, busca, rotas profundas, acesso por URL direta e resposta HTTP 403 sem privilégio | Não executado | — |
| P1-04 | Empresas A e B: alternar contas na mesma estação; verificar cabeçalho de empresa, JWT, listagens, detalhamento, criação, edição e exclusão, sem vazamento entre empresas | Não executado | — |
| P1-05 | Resposta 401 da API interna encerra sessão; 401 de API externa não desconecta usuário; respostas 404/422/500 geram mensagens adequadas sem expor stack trace | Não executado | — |
| P1-06 | Sidebar recolhida/expandida, breadcrumb, busca com Ctrl/Cmd+K, estados ativos, navegação profunda e 404 de rota inexistente | Não executado | — |
| P1-07 | Estados globais: carregando, vazio, nenhum resultado filtrado, offline, erro, 403 e tentativa de novo carregamento sem falso sucesso | Não executado | — |
| P1-08 | Responsividade em 320, 375, 768, 1024 e 1440 px; zoom de 200%; nenhum conteúdo ou ação essencial deve ficar inacessível | Não executado | — |
| P1-09 | Teclado (Tab/Shift+Tab, Enter, Escape), foco visível, leitura por tecnologia assistiva, nomes acessíveis, contraste e estados ARIA conforme WCAG 2.2 AA | Não executado | — |
| P1-10 | UX Preview funciona só no build de desenvolvimento; build de produção usa autenticação/API reais, sem dados demonstrativos nem bypass de autorização | Não executado | — |

**Critério de aceite:** registrar por cenário o resultado (aprovado/reprovado/bloqueado), navegador, usuário/perfil de teste, versão/commit e evidência redigida/sanitizada. Toda reprovação deve ter issue e ser revalidada depois da correção. Homologar 100% somente quando todas as linhas forem aprovadas e houver responsável pelo aceite formal.

## Pendências antes do aceite final de 100%

- [x] Pipeline Maven + Angular aprovado no último commit de código validado, incluindo os novos testes (68 Maven + 67 Angular).
- [x] Testes automatizados de login, refresh, logout, revogação e expiração aprovados. Os fluxos end-to-end em ambiente implantado seguem pendentes.
- [x] Isolamento entre empresas comprovado por testes negativos de HTTP, sessão e persistência no pipeline.
- [ ] Matriz completa de perfis/permissões com usuários reais em ambiente de homologação validada; a cobertura automatizada básica passou.
- [x] Estados de carregamento, erro, vazio e acesso negado confirmados por testes Angular.
- [ ] Ambiente de homologação (fora dos mocks) validado com credenciais de teste e dados de múltiplas empresas, incluindo UX desktop, tablet e celular.
- [ ] Evidências, resultados e responsável pelo aceite final registrados.

**Status deste documento:** validação automatizada da Prioridade 1 aprovada (135/135), com CI verde no commit `54413a0`. Homologação de ambiente real e aceite humano permanecem pendentes; este documento não certifica produção.
