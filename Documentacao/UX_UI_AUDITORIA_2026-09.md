# NeriTech Auto — Auditoria UX/UI e Inventário de Navegação

Data: 2026-09-06  
Branch: `feature/neritech-auto-rebuild`

## Resultado da revisão inicial

O frontend Angular 20 já possui um App Shell compartilhado, tokens NeriTech,
`PageHeader`, navegação por permissões e wrapper `NeriTechIcon` para Tabler.
O menu oficial documentado em `DESIGN.md` é a fonte de verdade e permanece
imutável nesta fase:

1. Gestão de Pátio
2. Home
3. Clientes
4. Operacional
5. Cadastros
6. Movimentação — Orçamentos, Ordens de Serviço, Checklists, Aprovações, Peças e Faturamento
7. Financeiro
8. Fiscal
9. Histórico
10. Gráficos
11. Agendamentos
12. Relatórios

## Divergências encontradas

| Área | Evidência | Tratamento desta fase |
| --- | --- | --- |
| App Shell | `theme/admin-layout` usa `Sidebar` + `Sidemenu`; existe uma sidebar antiga paralela | Manter somente o App Shell ativo como padrão de reconstrução |
| Ícones | telas legadas ainda contêm `pi pi-*` | telas reconstruídas usam exclusivamente `NeriTechIcon`; migração legada será incremental |
| Rotas | `gestao-patio`, `checklists-operacionais`, `aprovacoes`, `pecas-movimentacao`, `faturamento-operacional` e `historico` ainda usam placeholder | Substituir por telas de módulo com estados reais ou indisponibilidade explícita, sem dados fictícios |
| Menu | o JSON local é filtrado pelo backend/permissões e ordenado por `menu-contract.ts` | preservar autorização do backend e aplicar apenas ordenação visual canônica |
| Portal | existe frontend separado em `PortalCliente` | tratar como aplicação mobile-first independente, sem misturar shell interno |

## Inventário de rotas por capacidade

- Acesso: login, recuperação, redefinição e erros 403/404/500.
- Dashboard: gerencial, financeiro, orçamentos e operacional.
- Clientes e veículos: listagem, cadastro, edição, detalhe e histórico/vínculos.
- Orçamentos: lista, criação, itens, revisão, detalhe e conversão.
- Ordens de serviço: lista, criação, cockpit, execução, peças, diagnóstico,
  checklist, evidências, adicionais, histórico, financeiro e fechamento.
- Operacional: estoque, produtos, XML e serviços.
- Financeiro: pagar, receber, caixa, caixas fechados, transferências, notas de
  compra e comissões.
- PDV, fiscal, agenda, relatórios, administração e configurações.

## Critério de implementação

Cada tela reconstruída deve reutilizar o App Shell e os componentes compartilhados,
ter uma ação primária clara, estados de carregamento/vazio/erro/sem permissão,
feedback acessível e comportamento responsivo. Quando o contrato de dados ainda
não estiver disponível, a tela deve indicar indisponibilidade/parcialidade; não
deve inventar indicadores, valores financeiros ou eventos.

## Próximos blocos de execução

1. Consolidar menu e telas de acesso.
2. Fechar clientes, veículos, orçamento e OS como fluxo contínuo.
3. Fechar pátio, estoque e financeiro.
4. Fechar PDV, fiscal, agenda, CRM, marketing, relatórios e administração.
5. Revisar rotas, remover placeholders relevantes, validar build/testes e atualizar este inventário.

## Checkpoint — 2026-09-07

- Removido o `ModulePlaceholder` das rotas de produção.
- Dashboards Financeiro, Orçamentos e Operacional, além de Checklists,
  Aprovações, Peças, Faturamento, Histórico, Gráficos, Aniversários e Suporte,
  agora usam uma superfície canônica responsiva com `PageHeader`, Tabler,
  acessos reais relacionados e estado explícito de dependência da API.
- Eliminados CTA genérico e KPIs com traços que sugeriam uma operação conectada.
- Gestão de Pátio reconstruída sem veículos fictícios; o fluxo de etapas continua
  visível, mas o quadro informa a indisponibilidade do read model tenant-safe.
- Corrigida corrupção estrutural em `patio.ts`, que continha uma cópia indevida
  das rotas da aplicação após o componente.
- Corrigidos contratos TypeScript que bloqueavam o build em Financeiro,
  Fornecedores, Orçamento, PDV e Relatórios.
- Removido fallback inseguro de tenant (`empresaId || 1`) em Contas e Caixa. Os
  dados da empresa usados em Caixa agora derivam do usuário materializado pela
  autenticação; sem vínculo válido, a UI entra em estado degradado.
- `npm run build`: concluído com sucesso em 2026-09-07.
- `npm test -- --watch=false --browsers=ChromeHeadless`: bundle de testes
  compilado; execução não iniciou porque o ambiente não possui binário Chrome.

Pendência real: as visões agregadas citadas acima permanecem dependentes dos
respectivos read models de backend. O frontend não fabrica totais nem registros
enquanto esses contratos não estiverem disponíveis.


## Checkpoint — 2026-09-08 — Acesso

- Login, recuperação e redefinição de senha foram alinhados ao contrato visual Tabler por meio do `NeriTechIcon` local.
- Removidos PrimeIcons das telas de autenticação e do shell de acesso, incluindo estados de carregamento, proteção, ajuda, sucesso, link inválido e visibilidade de senha.
- Preservadas as respostas anti-enumeração de e-mail, bloqueio por tentativas e invalidação de token expirado ou reutilizado.
- Mantidos alvos de toque de 44 px, foco visível, `aria-busy`, mensagens acessíveis e comportamento responsivo.


## Checkpoint — 2026-09-08 — Estados sistêmicos

- Telas 403, 404 e 500 reconstruídas em português com hierarquia, ações e orientação contextual.
- Removida a dependência de Angular Material do componente compartilhado de erro.
- Substituído o visual legado Matero por tokens NeriTech, Tabler, foco visível e layout responsivo.
- A tela 500 oferece nova tentativa real e retorno seguro à Home; 403 não sugere bypass de permissão.


## Checkpoint — 2026-09-08 — PDV seguro

- Rotas de PDV, listagem e detalhe deixaram de expor o fluxo legado que fabricava vendedores, formas de pagamento, consumidor padrão e numeração aleatória.
- Removida da navegação de produção a orquestração de baixa de estoque + criação financeira no navegador, que não garantia atomicidade.
- PDV agora apresenta estados responsivos e acionáveis com acessos reais a Estoque, OS e Contas a Receber.
- Dependência explícita: comando transacional backend para venda e read models tenant-safe para lista/detalhe.


## Checkpoint — 2026-09-08 — Tenancy em Agenda

- Removidos os fallbacks de tenant `7` e `empresaId: 1` dos fluxos de calendário e alertas.
- `ComunicacaoService` deixou de ler ou sobrescrever autoridade de empresa pelo `localStorage`.
- Requisições de mecânicos e comunicações agora dependem da sessão/interceptor e da autorização final do backend.
- A comunicação não recebe mais `empresaId` no payload; isso reduz risco de cross-tenant por manipulação do navegador.
