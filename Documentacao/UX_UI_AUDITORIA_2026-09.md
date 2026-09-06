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
