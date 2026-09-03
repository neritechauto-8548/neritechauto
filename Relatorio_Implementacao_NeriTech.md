# Relatório de Implementação — NeriTech Auto

**Data-base:** 22 de agosto de 2026  
**Branch de reconstrução:** `feature/neritech-auto-rebuild`  
**Fonte de verdade funcional/UX:** `NERITECH — Documentação Oficial` no Notion  
**Status documental (D4):** 100% concluído para a documentação oficial aprovada  
**Status de implementação D5:** **12% geral**  
**Validação executável global:** **ADIADA PARA O FINAL DO PROJETO por decisão de desenvolvimento**

> O percentual D5 passa a medir implementação revisada no código. Compilação, execução de testes, execução do Flyway e CI completo serão feitos em uma rodada final. Até essa rodada, nenhum resultado de runtime deve ser presumido.

---

## 1. Escala oficial de progresso D5

| Faixa | Bloco | Status atual |
| ---: | --- | --- |
| 0–12% | Fundação, segurança, autenticação, multi-tenant, Flyway e base de testes | **Implementação concluída — validação final pendente** |
| 12–18% | Angular shell, menu, navegação, permissões de rota e design system | **Próximo bloco ativo** |
| 18–28% | Clientes, veículos e CRM base | Parcial / ainda não promovido |
| 28–38% | Orçamentos | Parcial / ainda não promovido |
| 38–52% | Ordens de Serviço, checklists e fluxo operacional central | Não promovido |
| 52–62% | Estoque, serviços, kits, áreas e acessórios | Não promovido |
| 62–76% | Financeiro, caixa e compras | Não promovido |
| 76–84% | Fiscal e PDV | Não promovido |
| 84–90% | Agenda, alertas, marketing e CRM | Não promovido |
| 90–95% | Pátio e Portal do Cliente | Não promovido |
| 95–98% | Relatórios, administração, auditoria e assinaturas | Não promovido |
| 98–100% | E2E, hardening final, CI, rastreabilidade e evidências finais | Não iniciado |

---

## 2. Marco 0–12% — fundação implementada

### 2.1 Segurança e autenticação

Implementado no rebuild:

- JWT com identidade derivada do backend e `jti` único por token.
- Access token autenticado somente quando corresponde a uma sessão ativa persistida.
- Logout revoga a sessão ativa e invalida o access token anterior no backend.
- Refresh token possui rotação: cada renovação substitui access e refresh anteriores.
- Refresh token antigo deixa de corresponder à sessão ativa, reduzindo replay.
- Redefinição de senha revoga sessões ativas do usuário.
- Usuário inativo ou bloqueado não pode renovar sessão.
- `TenantContext` é limpo no início/fim do filtro e em fluxos manuais de autenticação.
- Respostas REST específicas para `401` e `403`.
- Segredo JWT inválido/fraco segue política de falha fechada já implementada.
- Logs de autenticação não devem registrar senha, JWT ou refresh token.

### 2.2 Multi-tenancy

Princípio final implementado:

**o navegador nunca estabelece autoridade de tenant.**

- tenant autoritativo deriva da identidade/sessão autenticada no backend;
- `X-Tenant-Id` não concede acesso e, quando presente, deve coincidir com o tenant autenticado;
- query params/body `tenantId` ou `empresaId` podem existir temporariamente por compatibilidade, mas não substituem `TenantContext`;
- `TenantEntity` impede persistência/alteração fora do tenant autenticado;
- consultas críticas migradas usam `find...AndEmpresaId` ou equivalente;
- frontend não injeta tenant obtido de `localStorage` como autoridade.

Fluxos revisados/corrigidos neste marco incluem:

- Dashboard;
- Clientes e Cliente 360;
- Veículos;
- Agendamentos;
- Orçamentos;
- autenticação/sessões;
- fotos de Ordem de Serviço;
- Funcionários e foto de funcionário.

### 2.3 Proteção de mídia sensível

Correção adicional do fechamento de 12%:

- download de foto de Ordem de Serviço deixou de ser endpoint público;
- foto de funcionário deixou de ser endpoint público;
- foto de OS é resolvida por `id + empresa_id`;
- a OS associada ao upload/listagem é resolvida por `id + empresa_id`;
- foto de funcionário depende de funcionário resolvido no tenant autenticado;
- logo pública da empresa e foto pública de produto permanecem como exceções de compatibilidade e devem ser reavaliadas no hardening final.

### 2.4 Orçamentos e idempotência

- criação do orçamento não aceita número comercial autoritativo do navegador;
- tenant é derivado do backend;
- `POST` de criação usa `Idempotency-Key`;
- mesma chave + mesmo request pode retornar o recurso já criado;
- mesma chave + payload diferente gera conflito;
- reserva é escopada por empresa + ator autenticado + chave;
- ator é persistido de forma hash no controle técnico de idempotência;
- CORS passou a permitir `Idempotency-Key`.

### 2.5 Veículos e integridade de identidade

- placa é normalizada no backend;
- placa é única dentro do tenant;
- chassi/VIN, quando informado, é normalizado e único dentro do tenant;
- mesma identidade pode existir em tenants distintos conforme regra multi-tenant;
- regressão de odômetro é bloqueada no fluxo comum;
- exclusão é lógica/desativação nos fluxos refatorados.

---

## 3. Flyway preparado

Migrations adicionadas neste ciclo:

- `V266__add_orcamento_creation_idempotency.sql` — controle persistente de idempotência da criação de orçamento;
- `V267__enforce_vehicle_identity_per_tenant.sql` — normalização e unicidade de placa/chassi por tenant com preflight;
- `V268__index_active_user_sessions.sql` — índices parciais das sessões ativas.

### Política da V267

A migration não escolhe silenciosamente qual veículo legado deve sobreviver.

Se houver colisão após normalização:

1. a migration falha explicitamente;
2. nenhum registro é apagado ou mesclado automaticamente;
3. a inconsistência precisa ser saneada com contexto de negócio/auditoria;
4. somente depois a migration deve ser reaplicada.

**Importante:** as migrations estão implementadas, mas **não serão executadas agora**. A execução em banco limpo e em cenário de upgrade será feita na validação final do projeto.

---

## 4. CI e estratégia de validação

Workflow: `.github/workflows/ci-rebuild.yml`.

Por decisão de desenvolvimento, o workflow foi alterado para **execução manual (`workflow_dispatch`)** durante a reconstrução.

Isso evita que cada commit dispare automaticamente:

- Maven;
- testes backend;
- testes Angular;
- build Angular.

### Estado atual

- implementação: **12%**;
- revisão estática: **contínua durante o desenvolvimento**;
- Maven: **adiado**;
- Angular tests/build: **adiados**;
- Flyway real: **adiado**;
- CI completo: **adiado**;
- PR: continua **Draft**;
- merge em `main`: **não autorizado nesta etapa**.

### Rodada final obrigatória

Antes do projeto ser considerado pronto para merge/release, executar de forma concentrada:

1. `mvn clean test`;
2. build completo do backend;
3. testes Angular;
4. build Angular de produção;
5. Flyway em banco limpo;
6. Flyway simulando upgrade de banco existente;
7. testes de isolamento entre tenants;
8. autenticação, logout, refresh, replay e revogação;
9. testes de autorização/403;
10. contratos frontend/backend;
11. E2E dos fluxos críticos;
12. correção integral das falhas encontradas;
13. CI verde;
14. somente então avaliar merge do PR para `main`.

---

## 5. Base de testes já escrita

A branch contém cobertura criada/revisada para áreas críticas, incluindo:

- `TenantInterceptorTest`;
- `JwtAuthenticationFilterTest`;
- `JwtServiceTest`;
- `AuthServiceSessionTest`;
- `MultitenancyIntegrationTest`;
- `CustomUserDetailsServiceTest`;
- `UsuarioServiceTest`;
- `RestSecurityHandlerTest`;
- `VeiculoServiceTest`;
- `OrcamentoDraftServiceTest`;
- testes dos serviços de Cliente 360;
- `permission-guard.spec.ts`;
- testes de login/reset;
- testes de agendamento;
- testes de dashboard;
- testes críticos de orçamento no frontend.

> Esses testes fazem parte da implementação, mas não devem ser descritos como aprovados após a decisão de adiar a execução. O resultado real será registrado na validação final.

---

## 6. Próximo marco — 12% → 18%

O próximo bloco ativo é a consolidação do Application Shell e da autorização de navegação:

1. reconciliar `app.routes.ts` com as rotas oficiais;
2. corrigir aliases que não tenham os mesmos guards da rota canônica;
3. reconciliar menu com permissões realmente persistidas;
4. remover qualquer bypass implícito de perfil/`ADMIN` no frontend;
5. garantir comportamento deny-by-default no guard;
6. ocultar grupos de menu sem filhos autorizados;
7. consolidar sidebar/topbar/mobile shell;
8. padronizar Page Header, estados de loading/empty/error/403;
9. preservar as rotas oficiais de Home;
10. documentar o mapa rota → permissão → item de menu.

Não serão inventados novos códigos de permissão persistidos apenas para atender a nova UI. Primeiro será realizado o mapeamento dos códigos existentes.

---

## 7. Regra de progresso durante a reconstrução

Até a rodada final, um avanço percentual significa **implementação revisada e integrada estaticamente**, e não execução bem-sucedida.

O relatório sempre deve separar:

- **implementado**;
- **revisado estaticamente**;
- **pendente de execução**;
- **validado em runtime**.

Assim, o projeto pode continuar evoluindo sem custo de compilação a cada commit, sem transformar ausência de execução em falsa evidência de qualidade.
