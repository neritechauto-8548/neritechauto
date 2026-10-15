# NERITECH AUTO — instruções Codex

**Branch obrigatória:** `feature/develop` do repositório `neritechauto-8548/neritechauto`; nunca alterar `main` sem ordem explícita.

## Contratos e escopo
- Fontes canônicas: [módulos/owners](https://app.notion.com/p/80101fd65a9c833580f1012ec0a45407), [UI-MASTER-001](https://app.notion.com/p/97401fd65a9c822a9a4b017c39a08518) e [Telas & UX / D01–D11](https://app.notion.com/p/98501fd65a9c8379a9a281145568f52a).
- Ler somente o `CONTRATO EXECUTÁVEL CODEX` (bloco A) do ScreenId solicitado em `Documentacao/notion/telas/<modulo>/<ScreenId>.md`. Consultar o restante da página e as decisões D01–D11 apenas quando necessário; não abrir todos os 64 documentos.
- `Documentacao/notion/` é cópia de consulta do Notion, **não** sincronização em tempo real. Em divergência, conferir a página canônica; link não concede acesso ao Notion.
- Antes de alterar código, localizar com `rg` somente componentes, rotas, serviços, APIs, entidades, migrações e testes relevantes na `feature/develop`. Código real prevalece sobre API conceitual; não inventar que existe.

## Engenharia e produto
- `FrontEnd/`: Angular 20, Node 22, Yarn. `backend/`: Java 21, Spring Boot, Maven. Reutilizar PrimeNG e componentes existentes; UI-MASTER-001, ícones Tabler e identidade NERITECH AUTO. Stripe orienta qualidade de UX; Shopmonkey, UltraCar e OnMotor orientam processos.
- Prioridade: **reutilizar > estender > criar**. Escopo mínimo por ScreenId; nada de telas/rotas, sidebar, DTOs, documentação ou tabelas duplicados.
- Gestão de Pátio = PATIO-001, **uma tela interna**. Não criar Filiais, CLI-004, segunda sidebar ou sistema externo de Pátio.
- Preservar auth, RBAC real no backend, isolamento por empresa/tenant, auditoria, idempotência e Flyway. Nunca assumir empresa 1, aceitar empresaId arbitrário, editar migração aplicada, excluir dados ou efetuar operação externa onerosa/irreversível sem aprovação.
- Nunca simular êxito de emissão fiscal, pagamento, envio ou persistência. Preview/mock deve ser isolado e explicitamente demonstrativo.
- Testar mudança de forma focada; em homologação/fechamento de lote executar builds, testes integrados e QA aplicáveis. Não alegar execução sem evidência; segurança multiempresa é requisito de aceite.

## Economia de tokens e entregas
- Uma tela ou correção coerente por tarefa. Buscar por arquivo/símbolo e ler apenas trechos necessários; não imprimir árvore completa, logs extensos nem repetir buscas.
- Não pesquisar web/Notion novamente se contrato local suficiente e atual; buscar dependências apenas por necessidade concreta.
- Durante a edição, testes focados; validação integral ao encerrar o lote, sem ignorar risco relevante.
- Sem commit, push, PR ou alteração de documentação fora do escopo, exceto quando explicitamente pedido.
- Resposta final: até **6 linhas**, arquivos alterados, teste efetivo, resultado, pendências e commit (se aplicável). Sem repetir código, prompts ou logs.

**Prioridades P0:** testes cruzados OS entre empresas, fallback empresa, contrato Orçamento/API e navegação/guards. Ver `Documentacao/CODEX_STATUS_EXECUCAO.md`.
