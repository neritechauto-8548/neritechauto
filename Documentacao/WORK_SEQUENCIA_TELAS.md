# NERITECH AUTO — Sequência de desenvolvimento no Work

**Status:** proposta de consolidação técnica de telas-base, registrada para uso do ChatGPT Work. **Não altera a navegação, as rotas, os IDs de tela ou as responsabilidades funcionais oficiais.**

**Repositório:** `neritechauto-8548/neritechauto`  
**Branch obrigatória:** `feature/develop`  
**Documento de navegação oficial:** [03.01.03 Módulos](https://app.notion.com/p/80101fd65a9c833580f1012ec0a45407)  
**Contrato de UX/UI:** [UI-MASTER-001](https://app.notion.com/p/97401fd65a9c822a9a4b017c39a08518)  
**Especificações por tela:** [03.01.04 Telas & UX](https://app.notion.com/p/98501fd65a9c8379a9a281145568f52a)

## Regra-mãe

**Complexidade no sistema. Simplicidade para o usuário.** 

- UX visual: inspiração Stripe, identidade própria NERITECH AUTO; operação: Shopmonkey + UltraCar; particularidades brasileiras: OnMotor.
- Usar Angular e PrimeNG existentes no repositório, seguindo tokens e família **Tabler Icons** definidos na documentação.
- Evitar telas, formulários e componentes duplicados. **Uma tela-base não é necessariamente uma rota:** cada menu, processo, permissão, ação ou jornada mantém sua rota e regra oficial quando exigida.
- Não incorporar Gestão de Pátio à Oficina; continua um sistema independente (Kanban).
- Não alterar a árvore oficial nem tratar consolidação de telas-base como mudança autorizada na arquitetura.
- Não simular êxito de ações sem persistência. Modo de homologação sem backend deve estar marcado como demonstrativo e não contamina produção.
- Validar build e testes reais antes de declarar homologação. Registrar commit e evidências; atualizar Notion quando homologado.
- **Estes 38 itens são unidades de implementação/reutilização propostas**, não uma contagem de ScreenIds oficiais nem autorização para eliminar telas exigidas pela documentação.

## Ordem de execução — 38 telas-base

| # | Área | Tela-base ou agrupamento de desenvolvimento |
| --- | --- | --- |
| 00 | Aplicação global (não conta como tela) | Sidebar, topbar, branding, menu de usuário, busca, breadcrumb, layout responsivo, tabelas, filtros, formulários, componentes, estados e acesso por teclado |
| 01 | Acesso | Login |
| 02 | Acesso | Recuperação e redefinição de senha (fluxos e rotas distintos, base visual compartilhada) |
| 03 | Início | Dashboard único: Padrão, Gerencial, Financeiro e Orçamento (quatro visões internas) |
| 04 | Clientes | Listagem de clientes — `/cliente/listar` |
| 05 | Clientes | Cadastro / visualizar / editar no mesmo componente — `/cliente/cadastro` e `/cliente/editar/:uuid` |
| 06 | Clientes / CRM | Base de CRM, Comunicação e Questionamento de pesquisa; preservar submenus, rotas e funcionalidades próprias |
| 07 | Administração | Empresa / Filiais |
| 08 | Administração | Usuários / Funcionários / Cargos / Permissões, com visões e autorização próprias |
| 09 | Administração | Fornecedores / Seguradoras / Transportadoras |
| 10 | Administração | Áreas / Configurações |
| 11 | Oficina | Agenda |
| 12 | Oficina | Orçamentos — listagem |
| 13 | Oficina | Orçamentos — editor, serviços/peças e aprovação |
| 14 | Oficina | Ordens de Serviço — listagem |
| 15 | Oficina | OS — detalhes, execução e acompanhamento |
| 16 | Oficina | Diagnósticos / Inspeções; preservar workflows específicos |
| 17 | Oficina | Serviços — catálogo |
| 18 | Oficina | Veículos |
| 19 | Oficina | Entregas |
| 20 | Oficina | Vendas (venda direta/PDV, sem gerar OS artificial) |
| 21 | Oficina | Histórico |
| 22 | Estoque & Compras | Estoque / Movimentações |
| 23 | Estoque & Compras | Produtos / Kits |
| 24 | Estoque & Compras | Compras / Cotações |
| 25 | Estoque & Compras | Inventário |
| 26 | Financeiro | Contas / Lançamentos e Movimento Bancário |
| 27 | Financeiro | Contas a Receber / Contas a Pagar |
| 28 | Financeiro | Movimento de Caixa |
| 29 | Financeiro | Comissões |
| 30 | Fiscal | Documentos fiscais: NF-e / NFC-e-Cupom / NFS-e; modelos e fluxos fiscais específicos |
| 31 | Fiscal | Manifestação |
| 32 | Fiscal | Inutilização |
| 33 | Fiscal | NCM |
| 34 | Fiscal | SINTEGRA |
| 35 | Relatórios | Tela-base compartilhada para Comercial, Clientes, Serviços, Estoque, Compras, Financeiro, Comissões, Produtividade, Fiscal e Gerenciais |
| 36 | Gráficos Gerenciais | Tela única configurável com gráficos e filtros |
| 37 | Inteligência Artificial | Assistente |
| 38 | Inteligência Artificial | Automações IA |

**Gestão de Pátio ↗:** planejamento próprio de um sistema separado, fora das 38 telas-base.

## Critério de conclusão por tela

1. Ler a especificação correspondente no Notion e verificar código/rotas/APIs reais na `feature/develop`.
2. Reaproveitar AppShell e componentes globais; aplicar UX consistente, estados de carregamento/erro/vazio, acessibilidade, responsividade e permissões.
3. Implementar operações verdadeiras. Caso backend indisponível, criar/usar somente preview de UX claramente rotulado.
4. Executar build/testes e corrigir falhas relacionadas. Validar visualmente no navegador quando disponível; não declarar uma evidência não obtida.
5. Fazer commit na `feature/develop`, registrar arquivos modificados, resultados e pendências, sincronizar a documentação oficial da tela no Notion.

## Como pedir no ChatGPT Work

Peça para ler **este arquivo** na `feature/develop` e iniciar pelo item 00 ou por uma tela numerada. A documentação Notion é a fonte de verdade para navegação e escopo, e este arquivo apenas define sequência e reuso de tela-base.

**Prompt mínimo (após conectar GitHub e Notion ao Work):**
> Desenvolva a próxima tela da NERITECH AUTO conforme `Documentacao/WORK_SEQUENCIA_TELAS.md` na branch `feature/develop`. Consulte o Notion oficial, preserve a arquitetura, aplique UI-MASTER-001, execute testes, faça commit e informe as evidências e pendências.
