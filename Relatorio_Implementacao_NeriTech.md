# Relatório de Implementação — NeriTech Auto

**Data-base:** 21 de agosto de 2026  
**Branch de reconstrução:** `feature/neritech-auto-rebuild`  
**Fonte de verdade funcional/UX:** `NERITECH — Documentação Oficial` no Notion  
**Status documental (D4):** 100% concluído para a documentação oficial aprovada  
**Status de implementação verificável (D5):** **11% geral**

> Este relatório mede somente código implementado e evidência verificável. Documentação pronta, código legado existente, mockups, rotas vazias ou telas antigas não contam como implementação D5 concluída.

---

## 1. Escala oficial de progresso D5

| Faixa | Bloco | Status atual |
| ---: | --- | --- |
| 0–12% | Fundação, segurança, autenticação, multi-tenant e base de testes | **Em finalização** |
| 12–18% | Angular shell, menu, navegação, permissões de rota e design system | **Em implementação prioritária** |
| 18–28% | Clientes, veículos e CRM base | Parcial / ainda não promovido |
| 28–38% | Orçamentos | Não promovido |
| 38–52% | Ordens de Serviço, checklists e fluxo operacional central | Não promovido |
| 52–62% | Estoque, serviços, kits, áreas e acessórios | Não promovido |
| 62–76% | Financeiro, caixa e compras | Não promovido |
| 76–84% | Fiscal e PDV | Não promovido |
| 84–90% | Agenda, alertas, marketing e CRM | Não promovido |
| 90–95% | Pátio e Portal do Cliente | Não promovido |
| 95–98% | Relatórios, administração, auditoria e assinaturas | Não promovido |
| 98–100% | E2E, hardening, CI, rastreabilidade e evidências finais | Não iniciado |

---

## 2. Fundação e segurança — implementado

### Tenant e autorização

- Tenant autoritativo deriva da identidade/sessão autenticada no backend.
- `X-Tenant-Id` não concede acesso e só pode coincidir com tenant já autorizado.
- Query params `tenantId`/`empresaId` não são fonte de autoridade nos fluxos corrigidos.
- Entidades tenant-aware falham fechado quando não existe tenant confiável.
- Usuários, funções e clientes possuem consultas tenant-scoped nos pontos já refatorados.
- Permissões efetivas são as persistidas e devolvidas pelo backend; não existe expansão automática de `ADMIN` no frontend.

### Angular

- Interceptor de tenant controlado pelo navegador removido da cadeia ativa.
- Guards funcionais de permissão aplicados às rotas já migradas.
- Sidebar é filtrada pelas permissões efetivas devolvidas pelo backend.
- Grupos sem itens autorizados desaparecem da navegação.

### Erros de segurança

- Respostas JSON padronizadas para `401` e `403` implementadas.
- Segredo JWT ausente, inválido ou fraco faz a aplicação falhar fechado.

---

## 3. Frontend — reorganização conforme documentação oficial

### Autenticação

Implementado:

- `AuthLayout` único para Login, Recuperar Senha e Redefinir Senha.
- Login não armazena senha em `localStorage`.
- Recuperação usa mensagem anti-enumeração.
- Tratamento de token inválido/expirado/reutilizado na UX.
- Regra de nova senha alinhada ao contrato Spring atual: mínimo de 6 caracteres.
- Navegação pública concentrada em `/auth/*`.

### Application Shell

Implementado:

- Sidebar expandida: **264 px**.
- Sidebar compacta: **72 px**.
- Topbar organizada conforme UI Master.
- Breakpoint mobile corrigido para `< 768 px`.
- Gutter responsivo e shell padronizado.
- Contexto de empresa/unidade apresentado como informação de sessão, nunca como autoridade de tenant controlada pelo navegador.
- Busca global e seletores sem contrato funcional permanecem explicitamente indisponíveis em vez de simulados.

### Menu oficial

A árvore de produção foi reorganizada nos 12 grupos oficiais:

1. Gestão de Pátio
2. Home
3. Clientes
4. Operacional
5. Cadastros
6. Movimentação
7. Financeiro
8. Fiscal
9. Histórico
10. Gráficos
11. Agendamentos
12. Relatórios

Rotas de demonstração do template não fazem mais parte da navegação de produção.

### Home

A documentação oficial define quatro experiências e as rotas agora estão preparadas:

- `/home/gerencial`
- `/home/financeiro`
- `/home/orcamentos`
- `/home/operacional`

O menu Home possui os quatro itens correspondentes.

#### Dashboard Gerencial

Implementado neste ciclo:

- Page Header canônico.
- KPIs usando somente valores retornados pelo backend atual.
- Estados de loading e erro.
- Área de atenção com drill-down para módulos fonte.
- Resumo operacional do mês.
- Atalhos para OS, Orçamentos, Clientes e Financeiro.
- Estado explícito de read model pendente para histórico temporal.
- Remoção de tendências, metas e gráficos simulados do Angular.
- Remoção de histórico financeiro mockado do Spring Boot.

Ainda pendente para aderência D4 completa:

- `generatedAt` / freshness.
- filtros por período/unidade/comparação.
- read model gerencial dedicado.
- conversão de orçamento oficial.
- funil canônico.
- alertas agregados P0/P1 completos.
- gráficos com série temporal real e alternativa acessível.
- capability mapping final (`dashboard.managerial.read`, etc.) reconciliada com permissões persistidas atuais.

#### Dashboard Financeiro / Orçamentos / Operacional

As rotas finais já existem, mas o conteúdo permanece marcado como **MAPEAMENTO PENDENTE** até os respectivos read models serem implementados. Não há números fictícios nesses destinos.

---

## 4. Correção de segurança específica do Dashboard

Antes deste ciclo, o Dashboard ainda possuía um fluxo legado onde:

1. o Angular lia `tenantId`/`empresaId` do `localStorage`;
2. enviava `empresaId` como query param;
3. o `DashboardController` aceitava esse valor e podia sobrescrever o `TenantContext`.

Esse fluxo foi removido.

Agora:

- Angular chama apenas `GET /api/dashboard`.
- O controller lê exclusivamente `TenantContext.getCurrentTenant()`.
- ausência de tenant confiável retorna `401`.
- foram adicionados testes para impedir regressão desse comportamento.

---

## 5. Testes adicionados / mantidos

Cobertura já criada no rebuild inclui, entre outros:

- `TenantInterceptorTest`
- `JwtAuthenticationFilterTest`
- `JwtServiceTest`
- `MultitenancyIntegrationTest`
- `CustomUserDetailsServiceTest`
- `UsuarioServiceTest`
- `RestSecurityHandlerTest`
- `VeiculoServiceTest`
- `permission-guard.spec.ts`
- `DashboardControllerTest`
- `dashboard.service.spec.ts`

> Existência de teste no repositório não significa que o teste passou no CI atual. O resultado só será promovido quando houver execução observável.

---

## 6. Validação e CI

Workflow existente: `.github/workflows/ci-rebuild.yml`

- Backend: Java 21 + Maven tests.
- Frontend: Node 22 + Yarn + `ng build`.
- Trigger configurado para pushes em `feature/**` com alterações em `backend/**` ou `FrontEnd/**`.

### Situação observada no commit atual

Os status visíveis da Vercel falharam por **build rate limit da conta (`upgradeToPro=build-rate-limit`)**, não por evidência de erro de compilação.

Ainda não existe, nesta sessão, evidência observável de execução concluída do workflow GitHub Actions para promover este lote como CI verde.

Portanto:

- código: **implementado**;
- revisão estática: **em andamento**;
- Angular build: **não confirmado**;
- Maven tests: **não confirmado**;
- Vercel: **bloqueado por limite de builds**.

---

## 7. Bloqueios antes de 12% D5

Para fechar integralmente o marco de fundação (12%) ainda faltam principalmente:

1. migração Flyway nova e segura para integridade de Veículos por tenant;
2. verificação final de sessão/refresh e revogação;
3. execução observável dos testes backend;
4. execução observável do build Angular;
5. confirmação de que nenhum fluxo restante usa tenant controlado pelo navegador como autoridade.

---

## 8. Prioridade corrente

A prioridade deliberada neste momento é **organizar primeiro o frontend conforme a documentação oficial**, antes de expandir módulos de negócio.

Ordem atual:

1. Auth Shell — Login / Recuperar / Redefinir.
2. Application Shell — Topbar / Sidebar / responsividade.
3. Menu e rotas oficiais.
4. Home / Dashboard.
5. padrão reutilizável de Page Header, filtros, cards, tabelas, estados e formulários.
6. Clientes e Veículos já sobre essa fundação visual.
7. Orçamentos e OS na sequência.

---

## 9. Regra de promoção

Nenhum módulo será marcado como concluído somente porque existe código legado ou uma tela visualmente pronta.

Para promoção D5 devem existir, conforme aplicável:

- aderência à documentação oficial;
- tenant isolation;
- autorização backend;
- UX e responsividade;
- estados loading/empty/error/403;
- testes positivos e negativos;
- build/CI verificável;
- rastreabilidade entre especificação e implementação.
