# NeriTech Auto — Plano Frontend Luna

## Estado geral

Frontend estimado: 95%

Macrobloco atual: 1 — Fundação visual
Módulo atual: inventário e segurança transversal
Tela atual: Cliente 360
Último checkpoint: escopo autenticado consolidado e build de produção verde após saneamento de configurações
Último commit válido: `7d10f8e5`

## Macroblocos

| Ordem | Macrobloco | Estado | Progresso | Próxima ação |
| --- | --- | --- | --- | --- |
| 0 | Inventário | PRONTO | 100% | — |
| 1 | Fundação visual | PARCIAL | 85% | eliminar placeholders visíveis prioritários e validar shell |
| 2 | Clientes | PARCIAL | 80% | revisar estados e detalhe 360 |
| 3 | Veículos | PARCIAL | 75% | revisar estados e histórico |
| 4 | Orçamentos | PARCIAL | 85% | validar fluxo de autorização/conversão |
| 5 | Ordem de Serviço | PARCIAL | 90% | validar fechamento e liberação para faturamento |
| 6 | Operacional / Catálogo | PARCIAL | 65% | integrar superfícies oficiais restantes |
| 7 | Financeiro | PARCIAL | 60% | concluir estados e filtros |
| 8 | PDV | PARCIAL | 55% | validar recebimento e finalização |
| 9 | Fiscal | PARCIAL | 45% | implementar somente contratos disponíveis |
| 10–15 | Agenda, CRM, Relatórios, Configurações, Admin, Portal/Pátio | PARCIAL | variável | retirar lacunas por contrato real |

## Checkpoint atual

- Macrobloco concluído: 0 — inventário rápido.
- Macrobloco atual: 1 — fundação visual/segurança transversal.
- Arquivos principais: `FrontEnd/src/app/app.routes.ts`, `FrontEnd/src/app/routes/**`.
- Funcionalidade concluída por último: menus de ações em listas de aniversários e documentos fiscais possuem rótulos acessíveis; relatórios demonstrativos usam toast não bloqueante.
- Próximo passo exato: concluir a migração visual dos ícones da tela Cliente 360 e revisar cadastro de clientes; depois validar o fluxo de veículos.
- Dependência real: algumas rotas ainda são placeholders por ausência de contrato/read model; build de produção e desenvolvimento verdes. Testes CI compilam e executam, mas hoje fecham em 145 sucessos e 41 falhas, principalmente por providers ausentes e expectativas antigas em specs legados.
- Último commit seguro: `7d10f8e5` (login/logout local sem backend, dashboard offline explícito, fluxos sem dados pessoais/fictícios e orientação clara no acesso local; `npm run build:prod` validado novamente em 04/09/2026).

## Pendências críticas

- Há serviços/telas legados que usam `tenantId`/`empresaId` do navegador; revisar por slice antes de declarar segurança concluída. O contrato atual de formas de pagamento e departamentos de RH ainda exige `empresaId` como query param no backend.
- Push bloqueado pelo remoto GitHub com HTTP 403 para o usuário autenticado; commits permanecem locais e prontos para envio quando a credencial tiver acesso.
- A instalação local via Corepack/npm apresentou conflito de cache Windows (`EBADF/EEXIST`), mas o runtime local recuperado permite executar `npm exec ng`; build Angular validado em 04/09/2026.
- Rotas `gestao-patio`, `aprovacoes`, `pecas-movimentacao`, `faturamento-operacional`, `historico` e `graficos` ainda exibem placeholder por lacuna de contrato/read model.
- Inventário ainda depende das rotas backend legadas `/empresa/{empresaId}`; migração completa de tenancy aguarda contrato equivalente derivado da sessão.

