# NERITECH AUTO — ROTEIRO CODEX MÍNIMO EM TOKENS (64/64)

**Snapshot:** 15/10/2026 · **Branch:** `feature/develop` · **Repositório:** `neritechauto-8548/neritechauto`.
**Objetivo:** implementar o que falta, sem refazer o que já foi comprovado, até que o produto esteja validado. **64 documentos ≠ 64 funcionalidades prontas.**

## Como usar gastando o mínimo

1. Na sessão Codex Web, selecione o repositório e a branch acima. Não cole a documentação no chat: `AGENTS.md` e os contratos já estão no GitHub.
2. **Envie um prompt de cada vez**, na ordem abaixo. Para tela, envie apenas `Faça CLI-001` (substitua pelo ScreenId da linha). Nunca envie uma sequência inteira como um só prompt.
3. `Faça <ScreenId>` = conferir implementação real da `feature/develop`; **corrigir/completar somente pendências** de frontend, backend, permissões, tenant, dados/DTOs, estados e testes definidos pelo bloco A daquele ScreenId. Não refazer funcionalidades já verificadas.
4. Uma tela por chamada. Teste focado a cada mudança; QA de módulo após o grupo, e suíte de integração/CI real na homologação. **Economizar tokens não dispensa segurança nem testes obrigatórios.**
5. Resultado do Codex: até 6 linhas, indicando mudanças, testes executados e pendências. Sem commit/push/PR automáticos; integre o diff aprovado à `feature/develop` **antes da próxima tarefa dependente**, senão ela pode usar código desatualizado.
6. Se estiver comprovadamente pronta, responder **`JÁ VALIDADO`** com evidência, sem refatorar. Se bloqueada por provedor/credencial, implementar a parte local segura, registrar `BLOQUEADO EXTERNO`, nunca simular operação.
7. Se um contrato Markdown estiver desatualizado frente ao Notion, atualizar somente sua cópia correspondente após verificar a fonte; não reler o catálogo inteiro nem criar documentação paralela.

## Comandos transversais (uma vez, por necessidade)

**01 — P0:** Corrija o próximo bloqueio P0 ainda não resolvido em Documentacao/CODEX_STATUS_EXECUCAO.md e no código atual. Não refaça a segurança da OS já testada, salvo regressão. Uma correção por execução; teste focado.

**02 — Aplicação Global 00:** Finalize somente a Aplicação Global 00: sidebar existente, topbar, menu, guards, rotas, estados e responsividade. Leia Documentacao/APP_SHELL_00.md, AGENTS.md e valide contra o código real. Testes focados; sem telas duplicadas.

**03 — Auth:** usar `Faça AUTH-001`, depois `Faça AUTH-002`, depois `Faça AUTH-003`. **Contrato local específico:** `Documentacao/notion/global/autenticacao.md`. Não são parte das 64 telas de negócio.

## Ordem completa de ScreenIds (um prompt por ID, nunca todos de uma vez)

| Etapa | Módulo / objetivo | Comandos em ordem — copiar `Faça ID` para cada ID |
|---|---|---|
| 04 | Administração e controle de acesso | `Faça ADM-001` → `Faça ADM-003` → `Faça ADM-004` → `Faça ADM-005` → `Faça ADM-006` → `Faça ADM-007` → `Faça ADM-008` → `Faça ADM-009` → `Faça ADM-010` → `Faça ADM-011` |
| 05 | Clientes e CRM | `Faça CLI-001` → `Faça CLI-002` → `Faça CLI-003` → `Faça CLI-005` → `Faça CLI-006` |
| 06 | Base operacional da Oficina | `Faça OFC-008` → `Faça OFC-009` → `Faça OFC-001` |
| 07 | Orçamentos | `Faça OFC-002` → `Faça OFC-003` |
| 08 | Ordens de Serviço | `Faça OFC-004` → `Faça OFC-005` |
| 09 | Diagnósticos e inspeções | `Faça OFC-006` → `Faça OFC-007` |
| 10 | Estoque e Compras | `Faça EST-001` → `Faça EST-002` → `Faça EST-003` → `Faça EST-004` → `Faça EST-005` → `Faça EST-006` → `Faça EST-007` |
| 11 | Financeiro | `Faça FIN-001` → `Faça FIN-002` → `Faça FIN-003` → `Faça FIN-004` → `Faça FIN-005` → `Faça FIN-006` → `Faça FIN-007` |
| 12 | Pátio, entregas, histórico e venda direta | `Faça PATIO-001` → `Faça OFC-010` → `Faça OFC-011` → `Faça OFC-012` |
| 13 | Fiscal | `Faça FIS-001` → `Faça FIS-002` → `Faça FIS-003` → `Faça FIS-004` → `Faça FIS-005` → `Faça FIS-006` → `Faça FIS-007` → `Faça FIS-008` |
| 14 | Dashboards e gráficos | `Faça HOME-001` → `Faça GRA-001` |
| 15 | Relatórios | `Faça REL-001` → `Faça REL-002` → `Faça REL-003` → `Faça REL-004` → `Faça REL-005` → `Faça REL-006` → `Faça REL-007` → `Faça REL-008` → `Faça REL-009` → `Faça REL-010` |
| 16 | Inteligência Artificial | `Faça IA-001` → `Faça IA-002` |

## Checkpoint entre módulos (se necessário)

Ao terminar cada linha da tabela, use apenas quando houver integração entre telas: `QA do módulo [nome]. Teste os fluxos deste módulo, auth/tenant/permissões e integração com os módulos já concluídos. Corrija apenas falhas reais; informe evidências em até 6 linhas.`

**17 — QA final:** Faça auditoria final de todos os ScreenIds por cobertura de rotas, API, permissões/tenant, fluxos ponta a ponta, builds, CI, Flyway, WCAG, responsividade e testes reais. Corrija apenas falhas demonstradas em lotes pequenos; não declare 100% sem evidências. Liste bloqueios externos.

**18 — Preparação de produção, depois do QA:** `Revise configuração de ambiente, migrações, variáveis/segredos, observabilidade, backup/restauração, rollback, proteção de dados e segurança. Gere checklist de lançamento e evidências; não publique em produção, nem execute ações irreversíveis, sem autorização.`

## Critérios de encerramento sem falsos 100%

- Para cada tela: regra do bloco A satisfeita **no código**; build/testes apropriados executados; integração API/DTO/permissões/tenant testada; navegação e estados verificados; pendências explícitas, não escondidas.
- Para cada área: fluxo real entre telas testado com dados persistidos e usuários/empresas diferentes; erro, 403 e validações negativas contemplados.
- Fiscal, pagamentos, WhatsApp, SMS, e-mail, IA e outros provedores **somente operacionais** após credenciais/contratos, validação legal e teste real. Componentes/adapter locais sem provedor **não** significam homologação externa.
- Não registrar `100% homologado` enquanto restarem pendências essenciais, QA não executado ou integrações externas não validadas. Os documentos 64/64 são especificações, não prova do software.

## Referências, sem leitura integral automática

- Regras estáveis: `AGENTS.md`.
- Índice dos 64 contratos: `Documentacao/notion/README.md`.
- AUTH-001/002/003: `Documentacao/notion/global/autenticacao.md`.
- Evidências e status já registrados: `Documentacao/CODEX_STATUS_EXECUCAO.md` (Parte 00 de segurança OS foi implementada/testada naquele registro; **não repetir sem regressão**).
- Sequência anterior por telas-base: `Documentacao/WORK_SEQUENCIA_TELAS.md` (referência histórica de agrupamento; este documento define a ordem de comandos, sem alterar a arquitetura canônica).
