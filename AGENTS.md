# NERITECH AUTO — regras para Codex

**Repositorio:** neritechauto-8548/neritechauto. **Branch obrigatoria:** `feature/develop`; nunca editar/commitar em `main` sem ordem expressa.

## Fonte de verdade (ler apenas quando relevante)
- Navegacao/modulos: [Notion 03.01.03](https://app.notion.com/p/80101fd65a9c833580f1012ec0a45407). Nao criar, remover ou reorganizar modulos/rotas por conta propria.
- UX: [Notion UI-MASTER-001](https://app.notion.com/p/97401fd65a9c822a9a4b017c39a08518); telas: [Notion Telas & UX](https://app.notion.com/p/98501fd65a9c8379a9a281145568f52a).
- Ordem de entrega: `Documentacao/WORK_SEQUENCIA_TELAS.md`, so para tarefas de planejamento/sequencia. Etapa 00: `Documentacao/APP_SHELL_00.md`.
- UX premium Stripe-first com identidade propria; fluxos Shopmonkey, profundidade UltraCar e contexto OnMotor. So icones Tabler; reutilizar componentes existentes.

## Implementacao segura
- Frontend: Angular 20, Yarn 1, Node 22 (diretorio `FrontEnd/`); backend: Java 21, Maven, Spring Boot (`backend/`).
- Mudancas pequenas e completas; nao inventar APIs, rotas ou persistencia. Preview sem backend deve ser identificado e isolado de producao.
- Preservar tenant/empresa, autorizacao, contratos de API e Flyway. Nunca editar migracoes ja aplicadas, apagar dados ou executar migracoes destrutivas sem aprovacao.
- Testar o que mudou; para homologacao usar build e testes apropriados. Nao alegar teste visual, integracao ou CI que nao executou.

## Economia de contexto e tokens
- Antes de ler arquivos: localizar com `rg`/busca por nome e inspecionar apenas os trechos necessarios; evitar dumps do repositorio inteiro, logs integrais e leitura de toda a documentacao.
- Nao pesquisar web/Notion nem propor refatoracao, dependencias ou novas telas sem necessidade da tarefa.
- Uma tarefa/escopo por vez; reaproveitar contexto do mesmo chat e nao repetir grandes prompts.
- Durante edicao, priorizar testes focados; executar validacao abrangente quando necessaria para a entrega, nao a cada pequena alteracao.
- Resposta final curta: arquivos alterados, testes executados, commit e pendencias. Sem longos resumos ou repetir codigo, salvo pedido.

## Contrato Codex vigente — 14/10/2026
- **Entrypoint 64/64:** [Notion 03.01.04 Telas & UX — ENTRYPOINT OFICIAL, D01–D11 e Protocolo Codex](https://app.notion.com/p/98501fd65a9c8379a9a281145568f52a). Cada ScreenId canônico possui uma seção inicial `CONTRATO EXECUTÁVEL CODEX`. O [Inventário 64 telas/auditoria da branch](https://app.notion.com/p/3ee01fd65a9c81c9bb3dc13d9906d366) indica faltas de rotas, API e testes.
- **Regra de precedência:** Notion 03.01.03 define módulos/owners; UI-MASTER-001 define UX atual com superfícies claras e **sidebar existente expandível/recolhível**; D01–D11 definem defaults e decisões delegadas; seção executável da tela detalha o ScreenId; código real da `feature/develop` determina contratos que devem ser reconciliados (nunca presumir que APIs conceituais existem).
- **Autonomia delegada:** decidir sem nova consulta os padrões seguros e reversíveis de componentes, campos usuais, listas, estados, validações, UX, DTOs, nomes compatíveis, reuso, testes e organização interna. Registrar escolha na página de tela. Priorizar `reutilizar > estender > criar`; não criar Filiais, CLI-004, segunda sidebar ou sistema separado de Pátio.
- **Limite de autonomia:** não presumir legislação fiscal, credencial/provedor contratado, autorização externa, despesa, implantação irreversível ou exclusão de dados. Implementar fluxo local, adapter e estado não configurado honestos; registrar dependência de provedor e seguir para a próxima entrega segura. Nenhum envio/nota/boleto/pagamento simulado como concluído.
- **Antes de cada tela:** ler apenas D01–D11 pertinentes + a página da tela e owners dependentes; conferir rotas, DTO/controller/entity/Flyway reais; implementar Angular + Java + migrations seguros, permissões backend, tenant, testes e evidências. O processo pode ser incremental, mas nunca declarar implementação ou QA 100% só por documentação/CI genérico.
- **Blocos técnicos P0:** teste negativo de leitura/edição/exclusão de OS por ID de empresa B autenticado como A; eliminar fallback de empresa 1; conferir `OrcamentoService` prefixo de API; unificar menu.json/StartupService/app.routes e `data.requiredPermissions`; conectar Pátio/Inspeções/Gráficos sem telas duplicadas.
- **Acesso documental:** este AGENTS.md contém URLs, **não inclui por si só o conteúdo do Notion**. Codex deve possuir conector/permissão, ou exportação versionada quando não houver acesso; não adivinhar requisitos privados. Se indisponível, aplicar só tarefas cujo contrato já esteja completo e validado localmente e registrar bloqueio de sincronização.
