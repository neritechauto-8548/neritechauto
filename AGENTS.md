# NERITECH AUTO — instruções vigentes para Codex
Atualizado em 02/11/2026. Este arquivo substitui as instruções anteriores para o desenvolvimento do SISTEMA NERITECH AUTO.

## Escopo, repositório e branch
- Repositório: `neritechauto-8548/neritechauto`.
- Branch de desenvolvimento do sistema: `master`. Nunca modificar `main`, `develop` ou `feature/develop` sem orientação explícita.
- O sistema, a landing page e o portal do cliente são produtos distintos; não misturar suas alterações.
- Trabalhar uma entrega coerente por vez e verificar o código existente antes de criar arquivos ou componentes novos.

## ÚNICA referência vigente para organização de módulos, menu e ordem de execução
- Quadro Trello **NERITECH AUTO — Desenvolvimento das Telas**:
  https://trello.com/b/PgSFFi0U/neritech-auto-desenvolvimento-das-telas
- Primeiro cartão de execução: **01 · BASE — Login, segurança, shell e menu**:
  https://trello.com/c/EXiOl0bm
- Os cartões numerados de execução organizam prioridades e dependências; os cartões de telas detalham seus processos.
- Em conflito entre um arquivo do repositório, contratos antigos do Notion e o Trello atual, **Trello atual + imagens mais recentes aprovadas pelo usuário prevalecem para o produto**, sem dispensar validação do funcionamento e segurança.
- **Não utilizar como especificação vigente** a documentação antiga em `Documentacao/`, `Documentacao/notion/`, roteiros históricos de telas, links antigos de Notion ou a antiga árvore de menu. Esses arquivos são apenas histórico e não devem ditar novas implementações.
- **Não reestruturar o Trello** nem presumir que um cartão está concluído só porque há rota, menu, build verde ou sufixo "-ok".
- Capturas de tela mais recentes fornecidas pelo usuário são a referência visual de cada tela. Não alegar fidelidade pixel a pixel sem comparação real entre captura e navegador.

## Stack existente e implementação
- Frontend do sistema: `FrontEnd/` — Angular 20, Yarn, Node 22, Tailwind e bibliotecas já integradas, conforme código existente.
- Backend: `backend/` — Java 21, Spring Boot, Maven, PostgreSQL e migrações Flyway.
- Antes de editar: localizar componentes, menu, rotas, guards, serviços, controllers, DTOs, entidades, migrações e testes relacionados. Reutilizar ou corrigir primeiro; não duplicar.
- Modelagem de configurações: evitar tabelas genéricas com uma linha por seção e campos de negócio guardados como JSON/linhas dinâmicas, bem como acrescentar colunas isoladas à tabela errada. Antes de persistir uma configuração, procurar a tabela e entidade de domínio existentes; se não houver estrutura adequada, criar tabela(s) próprias, normalizadas e com tipos/FKs/índices coerentes com o domínio. Segredos devem ficar em armazenamento próprio e protegido. Não ampliar o modelo genérico sem justificativa explícita e revisão do impacto.
- Após uma migração de dados verificada, colunas redundantes ou sem uso criadas pelo projeto podem ser removidas com migração revisável; preservar campos de negócio existentes até confirmar dependências e retenção dos dados.
- Banco de dados: tabelas e colunas de negócio em português; backend com nomenclatura de domínio em português, respeitando integrações e APIs compatíveis onde necessário.
- Mudanças de schema por migração Flyway aditiva e revisável; não apagar dados, migrations aplicadas, FKs ou colunas sem analisar impacto e ter autorização para ação destrutiva.
- Funcionalidade de verdade: frontend integrado ao backend e persistência, validações, permissões, rastreabilidade e auditoria. Mock apenas identificado como demonstração e isolado de produção.
- Segurança obrigatória: autenticação, controle de acesso no servidor, isolamento multiempresa/empresa_id, impedimento de acesso cruzado, tratamento correto de exceções e logs sem exposição de segredos.
- Durante desenvolvimento, apresentar mensagens de erro acionáveis e identificação diagnóstica segura sem mascarar falhas; nunca expor credenciais, tokens ou dados sensíveis na interface.

## Critérios de aceite por cartão
1. Conferir a descrição e os checklists do cartão vigente e as imagens atuais.
2. Verificar que layout, campos, estados, interações e rotas correspondem às evidências fornecidas.
3. Validar regras de negócio e funcionamento ponta a ponta, inclusive persistência, permissões, estados vazios, erros, filtros e casos limítrofes.
4. Testar frontend/backend, multiempresa, fluxo por usuário e migrações envolvidas. Validar navegação por teclado, acessibilidade e resoluções 320/768/1440.
5. Reportar o que foi executado com evidência, resultados, arquivos alterados, divergências restantes e impedimentos. Não afirmar testes ou QA visual que não ocorreram.
6. Só solicitar movimentação do cartão para "Em validação"/"Concluído" após evidência suficiente; não mover nem reorganizar cartões sem autorização.

## Primeira entrega
Começar pelo cartão 01 — BASE. Auditar login, sessão, autenticação, menu lateral/superior, roteamento, permissões, isolamento entre empresas, exceções, Flyway e CI em `master`. Corrigir primeiro inconsistências comprovadas de navegação e testes sem reescrever o sistema.

**Importante:** instruções antigas do próprio repositório em desacordo com este arquivo não devem ser reutilizadas como requisitos.
