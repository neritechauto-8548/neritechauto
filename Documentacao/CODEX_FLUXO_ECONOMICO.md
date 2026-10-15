# Codex — desenvolvimento econômico (NERITECH AUTO)

**Use:** Codex Web com GitHub `neritechauto-8548/neritechauto`, branch `feature/develop`. O arquivo `AGENTS.md` na raiz contém as regras estáveis; não é necessário repetir suas instruções em todo prompt.

## Fluxo curto
1. Localizar o ScreenId no [catálogo de 64 telas](./notion/README.md).
2. Abrir somente o bloco **A — CONTRATO EXECUTÁVEL CODEX** da tela. Ler B/C e D01–D11 apenas se forem necessários.
3. Conferir arquivos existentes de frontend/backend, rotas, DTOs, migrações e testes; não confiar cegamente em contratos de API conceituais.
4. Implementar uma tela ou correção por tarefa. Reutilizar antes de criar. Preservar RBAC e isolamento multiempresa.
5. Rodar testes focados ao editar; para aceitar/homologar executar builds, testes e QA completos pertinentes, incluindo casos negativos A/B. Não cortar segurança para economizar tokens.
6. Responder em até seis linhas com arquivos, testes executados, pendências e commit apenas se solicitado.

## Prompt reutilizável

> Implemente [ScreenId] na branch `feature/develop`. Siga o `AGENTS.md` e leia apenas o bloco A do contrato em `Documentacao/notion/telas/[módulo]/[ScreenId].md`. Localize e reutilize código existente; confira API, permissões e tenant; faça testes focados; não altere arquivos fora do escopo nem faça commit. Resuma em até 6 linhas.

## Modelo e contexto
- Em ambiente **Codex CLI/IDE**, `.codex/config.toml` sugere esforço baixo, resposta curta e máximo de instruções carregadas. Sua aplicação depende da versão/configuração do Codex.
- No **Codex Web**, escolha o modelo econômico disponível e o nível de raciocínio baixo para tarefas simples, se esses controles estiverem disponíveis. Para RBAC, multiempresa, arquitetura, fiscal e financeiro, eleve esforço conforme risco.
- O espelho Markdown não é atualização em tempo real. Após alteração no Notion, atualize o arquivo da mesma tela antes de executá-la; não crie especificações duplicadas.

**Não fazer:** enviar os 64 documentos como contexto, pedir refatoração geral junto de uma tela, repetir builds completos a cada pequeno ajuste ou tratar falha de API como êxito.
