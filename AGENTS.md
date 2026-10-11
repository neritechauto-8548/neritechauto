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
