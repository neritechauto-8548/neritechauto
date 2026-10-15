# CODEX — controle de execução

## Parte 00 — Segurança P0 de Ordens de Serviço

- Branch: `feature/develop` (HEAD inicial `b51c3175d4363f20842e88d10aee9cf3d27dbf81`).
- Commit de código P0: `937d685d55303f2d9019305f6ba12bfedf79d382`.
- ScreenIds: OFC-004 e OFC-005. Correção P0 implementada e teste integrado A/B validado; telas completas não homologadas.
- Contratos consultados: Notion 03.01.03, UI-MASTER-001, D01–D11 pertinentes e seções `CONTRATO EXECUTÁVEL CODEX` de OFC-004/OFC-005.
- Escopo: acesso a OS por ID, consultas por empresa/cliente, relações cliente/veículo/status, itens e fotos diretamente ligados à OS, leitura pública por ID e fallback de empresa no serviço Angular.
- Migração Flyway: não necessária para a correção P0; schema e dados existentes preservados.
- Teste integrado: `mvn -Dtest=OrdemServicoTenantIntegrationTest test` — 1 teste, 0 falhas, 0 erros (PostgreSQL/Testcontainers, JWT A/B; GET/PUT/DELETE, relações cruzadas, item e perfil sem capability).
- Frontend: `yarn build` — aprovado. Backend: `mvn -DskipTests package` — aprovado. CI remoto e QA manual pendentes.
- QA manual pendente: fluxos reais em navegador com dois usuários/empresas, permissões por cargo, upload/download de fotos, integração de itens, responsividade e compatibilidade PDF.
- Bloqueios conhecidos: autenticação do `gh` indisponível neste ambiente; o artefato Jaspersoft OpenPDF retornou 403 e foi substituído no POM pela versão pública equivalente para permitir o build. Relatórios PDF precisam de QA de compatibilidade.
- Próximo número do roteiro: Parte 01. Não iniciada nesta execução.

## BASE-03 — Shell, menu e rotas (15/10/2026, execução incremental)

- Branch: `feature/develop`. Código já confirmado: sidebar original, header, branding, breadcrumb, pesquisa, guards Angular e rotas lazy existentes; sem segunda sidebar ou tela fictícia de Pátio.
- Corrigido: menu.json alinhado aos 11 grupos da fonte Notion 03.01.03, com 43 destinos existentes habilitados e 26 indisponíveis; sem Filiais/CLI-004, mantendo permissão e planos comerciais legados aplicáveis. Preview e produção compartilham o JSON; fallback local da prévia atualizado.
- Contrato: largura 264px aberta/72px recolhida aplicada aos tokens reais; `permissionGuard` nos filhos do shell, orçamento, calendário, caixa e páginas administrativas sensíveis; backend continua autoridade para RBAC/tenant.
- Evidência efetiva: 9 verificações estáticas diretas aprovadas. GitHub Actions `Dashboard Quality` (run 38774778414, commit `5fa73ff5`): teste Node `scripts/navigation-contract.test.cjs`, `yarn build` e testes Angular de regressão do Application Shell **aprovados no job frontend**. Teste Maven em andamento na última consulta; QA manual de navegador ainda não executado.
- Deploy: status de Vercel consultado em commit da execução mostrou falhas por `build-rate-limit`, sem comprovar falha ou êxito de compilação.
- Pendências: conferir guard/perfis com usuários reais, refresh/deep-link, 403/404, foco/teclado/leitor de tela, resoluções 320/375/768/1024/1440 e build/testes completos. **BASE-03 não homologada**.

## BASE-04 — Design system e estados globais (15/10/2026)

- Branch: `feature/develop`. Fonte: UI-MASTER-001 no Notion; inventário real conferido em `FrontEnd/src/app/shared/components/` e interceptores existentes. Sem nova biblioteca ou segunda implementação de tabela, toast, confirmação ou drawer.
- Reuso/alterações: `PageHeader` passou a renderizar título/subtítulo; breadcrumb usa destino real `/home`; tela compartilhada `ErrorCode` e 403 receberam semântica e texto pt-BR. `ConfirmationDialogComponent` mostra mensagem como texto seguro e botões com foco/área de toque.
- `DataViewState` distingue loading, vazio, vazio filtrado, offline, erro, 403, 404, 409 e dados inválidos (422); retry somente por evento explícito, jamais como êxito presumido. `errorInterceptor` mantém `HttpErrorResponse` e erros de campos para UI, mas evita mostrar detalhes internos do servidor no toast (400/409/422); 401/403/404/5xx e `X-Skip-Toast` preservados.
- Tokens semânticos de cores/espaçamento do UI-MASTER aplicados incrementalmente em `styles.scss`, componentes reutilizáveis e seletores PrimeNG de tabelas, inputs, paginação, modal e drawer, sem alterar a árvore/identidade da sidebar. Contrastes dos pares canônicos CTA/texto/superfícies verificados por teste Node. Ícones usam `nt-icon` Tabler já existente.
- Testes Angular novos: `ErrorCode`, `PageHeader`, `ConfirmationDialogComponent`, `DataViewState` 404/409/422, `errorInterceptor` 409/422/offline. Teste Node: `scripts/ui-tokens.test.cjs`. GitHub Actions run 38776683117 (commit `2219c878`): job **frontend concluído com sucesso** — contrato Node, `yarn build` e **84 testes Angular Chrome Headless aprovados (0 falhas)**. Job Maven ainda em andamento na última consulta; CI sem QA manual não homologa a BASE-04.
- Sem homologação visual: faltam ensaios manuais em 320/375/768/1024/1440 px, teclado/leitor de tela, zoom 200%, cenários de API real/offline e sessões de diferentes tenants; CSS e CI não demonstram WCAG 2.2 AA completo. Tabelas e formulários de cada módulo continuam em lotes próprios. **BASE-04 não homologada.**

### BASE-04 — Continuação de correções focadas (15/10/2026)

- Defeito no diálogo confirmado por inspeção: interpolação inválida dentro da tag `p` corrigida para conteúdo textual Angular, mantendo segurança de escape. Serviço de confirmação compartimentado por solicitação, cancelando a anterior quando uma nova a substitui; resposta por `ReplaySubject` protege inscrições após a decisão. Testes cobrem sobreposição de confirmações e observadores tardios.
- Contrato HTTP real inspecionado: `ApiResponse<T>` do backend contém `data/message/code/timestamp/errors`; `ApiErrorResponse` de 422 contém `errors` como mapa de campos. `apiInterceptor` só desembrulha envelopes reconhecidos; corpo 200 com código/erros de falha não é mais sucesso silencioso e vira `HttpErrorResponse` preservando payload. `errorInterceptor` agora envolve o interceptor de API e centraliza toast seguro; sem alterar endpoints, entidades, migrations ou autorização.
- Novos testes Angular: sobreposição/inscrição tardia de confirmação, envelopes com/sem `code`, sucesso e objeto de domínio, `X-Skip-Toast` e mapa de validação Spring 422.
- **Evidência verificada:** GitHub Actions `Dashboard Quality` run `38778209479` no commit `441c0e880ee42144a807b359c15e063c5bcd545d`: `node --test` aprovado, `yarn build` aprovado e **92 testes Angular ChromeHeadless aprovados, 0 falhas** (logs do job frontend `116571595158`). Na última consulta, job Maven ainda em execução. O workflow pode ser marcado cancelado se execuções mais novas competirem.
- **Não homologada**: pendem navegador real com backend online/offline, 320/768/1440 px, zoom 200%, foco/teclado/leitor de tela, auditoria WCAG 2.2 AA e controle A/B de empresa/permissões; resultados de build/Karma não atestam esses cenários.

### BASE-04 — Handler global e confirmação acessível (15/10/2026)

- Corrigida uma divergência comprovada em `GlobalExceptionHandler`: payload HTTP 409 deixava expostos detalhes SQL e respostas 400/500 incluíam mensagens internas. Agora conflitos mantêm motivo seguro sem valores SQL, erros inesperados usam descrição pública genérica e rastros detalhados vão para log interno. `ConstraintViolationException` de Bean Validation retorna 422, não 409. Permanecem inalterados endpoints, auth, tenant, auditoria e migrações.
- Testes Java focados: `GlobalExceptionHandlerTest` valida status 400/409/422/500 e ausência de valores sensíveis nas respostas, inclusive transação/rollback.
- `ConfirmationDialogComponent` recebeu título via `header` nativo PrimeNG, fechamento explícito e por Escape/ícone cancelando a pendência via `onHide`. Testes Angular verificam cancelamento e impedem segundo resultado após confirmação. A tentativa inicial de usar `ariaLabelledBy` falhou no build Angular, foi removida e substituída pelo contrato compatível.
- Testes de CI do commit funcional `060a0ed9b26545c0c3342411ec4e46edbb2dadeb` solicitados via workflow `Dashboard Quality`, run `38780182505`; **consultar conclusão final antes de afirmar aprovação**. O último CI integral anterior verde foi run `38778564408` no commit `28b0b03`.
- QA ainda pendente: navegador contra backend real/indisponível, fluxos reais com contas A/B, perfis, foco e teclado efetivos, zoom 200%, WCAG 2.2 AA e largura 320/768/1440; build/tests não equivalem a homologação.

### BASE-04 — Estados em tema escuro e foco (15/10/2026)

- Lacuna confirmada no componente compartilhado `DataViewState`: título `#0f172a` e descrição `#64748b` fixos permaneciam escuros mesmo com a classe `.dark`, prejudicando legibilidade. Substituídos por `--nt-text` e `--nt-text-secondary`; ícone crítico, ação de retry e estado de linha selecionada receberam tokens compatíveis com tema escuro.
- Botão retry: alvo mínimo de 44 px, contorno de foco de 3 px, quebra de conteúdo no estreitamento e cores semânticas claras/escuras; botão de confirmação do tipo warning escurecido para contraste de texto branco. Não foram criadas bibliotecas, segundas telas, novas APIs ou caminhos de menu.
- Teste Node `FrontEnd/scripts/ui-tokens.test.cjs` ampliado para verificar pares de contraste claro/escuro ≥ 4,5:1 e contratos CSS (alvo, foco e quebra), incluído no workflow já existente. Commits: `04867e3` (código) e `5edf33e` (correção de expressão regular do teste).
- CI: GitHub Actions workflow `Dashboard Quality` run `38781255275` e `38781247766` para o commit `5edf33e`; conferir status final antes de alegar testes compilados. Os testes de Node da BASE-04 foram aprovados nos jobs frontend quando consultados. A etapa **não está homologada**: pendem QA real com backend ativo/inativo, teclado completo, zoom do navegador 200%, largura real 320/768/1440 e leitores de tela/WCAG 2.2 AA.
