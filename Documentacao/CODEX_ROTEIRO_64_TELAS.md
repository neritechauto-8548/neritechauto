# NERITECH AUTO — PROMPTS COMPLETOS PARA CODEX (economia de tokens)

**Versão:** 15/10/2026 | **Branch:** `feature/develop` | **Repositório:** `neritechauto-8548/neritechauto`

Este é o **roteiro executável completo**. Cada ação possui um **prompt independente para copiar e colar no Codex Web**. O objetivo é economizar tokens **por execução**: as regras globais ficam em `AGENTS.md`, e cada prompt manda ler **apenas o contrato da tela necessária**. O prompt não duplica a especificação de campos/contratos já disponível no GitHub.

**Regras de uso:** execute **um bloco de prompt por tarefa**, na ordem indicada. Depois de revisar o resultado no Codex, integre as alterações aprovadas à `feature/develop` antes da próxima tarefa que dependa delas. Não cole este documento inteiro no Codex. Cada prompt permite parar e declarar `JÁ VALIDADO` com evidência se estiver pronto; se faltar credencial/provedor marque `BLOQUEADO EXTERNO` e conclua somente a parte local segura. **Documentação 64/64 não significa software 100% implementado.**

**Observação técnica:** `.codex/config.toml` pode atuar no CLI/IDE e não necessariamente no Codex Web. Se houver controles no Web, use modelo econômico e esforço baixo para tarefas simples; aumente raciocínio em segurança, finanças, fiscal e migrações. Economizar tokens não elimina testes fundamentais.

## Mapa de execução

| Etapa | Entregas | Ordem de uso |
| --- | --- | --- |
| 00 | Corrigir base e arquitetura | BASE-01 → BASE-02 → BASE-03 → BASE-04 |
| 01 | Autenticação | AUTH-001 → AUTH-002 → AUTH-003 |
| 02 | Administração | ADM-001 → ADM-003 a ADM-011 |
| 03 | Clientes / CRM | CLI-001 a CLI-003 → CLI-005 → CLI-006 |
| 04 | Oficina base | OFC-008 → OFC-009 → OFC-001 |
| 05 | Orçamentos e OS | OFC-002 → OFC-003 → OFC-004 → OFC-005 |
| 06 | Diagnósticos / inspeções | OFC-006 → OFC-007 |
| 07 | Estoque e compras | EST-001 → EST-007 |
| 08 | Financeiro | FIN-001 → FIN-007 |
| 09 | Pátio / entregas / vendas | PATIO-001 → OFC-010 → OFC-011 → OFC-012 |
| 10 | Fiscal | FIS-001 → FIS-008 |
| 11 | Home / gráficos | HOME-001 → GRA-001 |
| 12 | Relatórios | REL-001 → REL-010 |
| 13 | Inteligência Artificial | IA-001 → IA-002 |
| 14 | Testes integrados e lançamento | QA-01 → QA-09 |

**Fonte canônica:** [Notion 03.01.03 Módulos](https://app.notion.com/p/80101fd65a9c833580f1012ec0a45407), [UI-MASTER-001](https://app.notion.com/p/97401fd65a9c822a9a4b017c39a08518), [Telas & UX](https://app.notion.com/p/98501fd65a9c8379a9a281145568f52a). **Espelho de consulta:** `Documentacao/notion/README.md`. Fonte operacional do que existe: HEAD da `feature/develop`. Não inventar API nem sincronização Notion automática.


## 00 — Segurança e infraestrutura (comece aqui; pule o que estiver comprovadamente resolvido)

### BASE-01 — Segurança P0 e multiempresa

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE BASE-01 — Segurança P0 e multiempresa.
Siga AGENTS.md; consulte Documentacao/CODEX_STATUS_EXECUCAO.md para pendências já conhecidas e Documentacao/APP_SHELL_00.md apenas em mudanças de shell, complementando com D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Analise apenas as pendências P0 ainda comprovadas na branch e no Documentacao/CODEX_STATUS_EXECUCAO.md. Preserve o teste de isolamento da OS já aprovado. Corrija fallback empresa 1, empresaId arbitrário, consultas/mutações cruzadas, JWT/roles e upload sem persistência nas rotas vulneráveis identificadas. Uma falha comprovada por execução; se forem independentes, entregue somente a mais crítica.
VALIDAR: Teste leitura, alteração, exclusão e FKs entre empresas A/B; 403/404 corretos, rollback e regressão focada.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### BASE-02 — Contrato HTTP de Orçamentos

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE BASE-02 — Contrato HTTP de Orçamentos.
Siga AGENTS.md; consulte Documentacao/CODEX_STATUS_EXECUCAO.md para pendências já conhecidas e Documentacao/APP_SHELL_00.md apenas em mudanças de shell, complementando com D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Verifique caminho real do OrcamentoController, serviços Angular, proxy, baseUrl e autenticação. Corrija somente divergências comprovadas de prefixo /v1/orcamentos e serialização DTO; preserve endpoints e clientes legados funcionais. Não reescreva módulo ou crie API paralela.
VALIDAR: Teste listar, criar/editar e tratar 401/403/422/409; sem regressão em OS.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### BASE-03 — Shell, menu e rotas

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE BASE-03 — Shell, menu e rotas.
Siga AGENTS.md; consulte Documentacao/CODEX_STATUS_EXECUCAO.md para pendências já conhecidas e Documentacao/APP_SHELL_00.md apenas em mudanças de shell, complementando com D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua aplicação global reutilizando sidebar atual (264 px aberta/72 px recolhida), header, branding, breadcrumb, pesquisa, estados e links do menu oficial 03.01.03. Harmonize menu.json, StartupService, app.routes e data.requiredPermissions/permissionGuard; preserve Pátio como tela interna e remova Filiais/CLI-004. Não crie outra sidebar.
VALIDAR: Teste permissões em perfis, refresh/deep-link, rotas desconhecidas, teclado e 320/768/1440 px.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### BASE-04 — Design system e estados globais

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE BASE-04 — Design system e estados globais.
Siga AGENTS.md; consulte Documentacao/CODEX_STATUS_EXECUCAO.md para pendências já conhecidas e Documentacao/APP_SHELL_00.md apenas em mudanças de shell, complementando com D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Padronize componentes compartilhados da UI-MASTER-001 realmente existentes: PageHeader, filtros, tabela, formulário, loading/skeleton, vazio, 403/404, offline, toast, confirmação, drawer e paginação. Adapte PrimeNG, Tabler e tokens sem instalar biblioteca nova nem refazer todas as telas; harmonize erros de API reais.
VALIDAR: Teste estados com backend real/indisponível, teclado, foco, zoom 200%, WCAG 2.2 AA e responsividade.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 01 — Autenticação e sessão

### AUTH-001 — Login

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE AUTH-001 — Login.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/global/autenticacao.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua login em duas colunas desktop (imagem automotiva ampla à esquerda, formulário à direita), mobile responsivo, e-mail, senha, mostrar senha, erros e estados. Integre autenticação e tokens reais; preserve acessibilidade, rate-limit e sessão segura. Sem login mock em produção.
VALIDAR: Teste senha inválida, sessão expirada, token refresh/autorização, 401 e teclado.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### AUTH-002 — Recuperar senha

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE AUTH-002 — Recuperar senha.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/global/autenticacao.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua /auth/recover com formulário premium consistente, e-mail, mensagens seguras que não revelem existência da conta, token/link de recuperação de uso único, expiração e tratamento de indisponibilidade do envio. Use backend já implementado antes de criar outro.
VALIDAR: Teste e-mail inexistente, abuso/rate-limit, expiração e retorno acessível ao login.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### AUTH-003 — Redefinir senha e sessão

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE AUTH-003 — Redefinir senha e sessão.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/global/autenticacao.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua /auth/reset-password: verificar token e expiração, exigir política de senha real, confirmar troca, invalidar token consumido e sessões necessárias, confirmar feedback só após backend; alinhar com login/recuperação sem tela paralela.
VALIDAR: Teste token inválido/usado, senha fraca, tentativas paralelas, expiração e novo login.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 02 — Administração

### ADM-001 — Empresa

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE ADM-001 — Empresa.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/administracao/ADM-001.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Reaproveite a tela Empresa existente. Conclua Identificação, Endereço, Contatos, Fiscal, Preferências e Marca: razão/nome, CPF-CNPJ aplicável, inscrições, regime, CEP, logo, horários, fuso e tema; validações e histórico versionado. Restrinja tudo à empresa da sessão, preserve notas fiscais já autorizadas e guarde segredos fora do cadastro comum.
VALIDAR: Alterar empresa A sem acessar B; validar CNPJ/CEP, logo inválido, mudanças fiscais auditadas, 403 e conflitos.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### ADM-003 — Usuários

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE ADM-003 — Usuários.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/administracao/ADM-003.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua listagem, convite/cadastro, edição, inativação, vínculo à empresa, associação a cargos e perfis e histórico. Use autenticação existente; valide e-mail, duplicidade, mudança de status e expiração de convite. Não crie segundo cadastro de usuário.
VALIDAR: Teste criação repetida, convite vencido, revogação de acesso, usuário sem permissão e isolamento A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### ADM-004 — Funcionários

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE ADM-004 — Funcionários.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/administracao/ADM-004.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Reutilize cadastro de funcionários existente; organize identificação, contato, cargo, função, vínculos operacionais, comissionamento quando permitido, situação e disponibilidade. Integre Oficina/Agenda/Apontamentos sem duplicar usuário ou criar Filiais; trate dados sensíveis com autorização.
VALIDAR: Teste funcionário inativo, vínculo de usuário, atribuição em OS, filtro e autorização entre empresas.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### ADM-005 — Cargos

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE ADM-005 — Cargos.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/administracao/ADM-005.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua catálogo de cargos e suas atribuições, status, vínculos e edição segura. Distinga cargo organizacional de capability de segurança; não conceda permissões implicitamente. Preserve colaboradores vinculados e faça auditoria das alterações.
VALIDAR: Teste cargo em uso, desativação, duplicidade de nome, alteração concorrente e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### ADM-006 — Permissões

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE ADM-006 — Permissões.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/administracao/ADM-006.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Implemente matriz RBAC por perfil/capability real: leitura, criação, edição, aprovação, exclusão lógica e exportação conforme contrato. Harmonize data.requiredPermissions/permissionGuard e @PreAuthorize no backend. Negue por padrão, impeça escalada e retirada do último admin.
VALIDAR: Teste URL direta, 403 real, tentativa de autoelevação, token antigo após revogação, usuário/empresa B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### ADM-007 — Áreas

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE ADM-007 — Áreas.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/administracao/ADM-007.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua cadastro e manutenção das áreas organizacionais/operacionais existentes, ativos e vínculos a funcionários, boxes/recursos quando oficialmente previstos. Sem Filiais. Reuse entidade/rotas verificadas; não duplicar área com setor ou posto.
VALIDAR: Teste nome repetido, área inativa em uso, vínculos A/B e permissões.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### ADM-008 — Fornecedores

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE ADM-008 — Fornecedores.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/administracao/ADM-008.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua lista, cadastro, edição, contatos, documentos, endereços, condições de compra, situação e histórico de fornecedores. Integre Compras, Cotações e notas sem replicar cadastros; valide identificadores e autorização.
VALIDAR: Teste CNPJ duplicado, fornecedor inativo, cotação vinculada, 403 e dados entre empresas.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### ADM-009 — Seguradoras

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE ADM-009 — Seguradoras.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/administracao/ADM-009.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua cadastro de seguradoras, contatos, identificação, dados de convênio/apólice apenas quando previstos, status e vínculo com fluxo de atendimento/OS. Não invente integrações com seguradoras externas.
VALIDAR: Teste vínculo legítimo, validações documentais, inativação, A/B e acesso por perfil.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### ADM-010 — Transportadoras

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE ADM-010 — Transportadoras.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/administracao/ADM-010.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua transportadoras, contatos, documentos, endereços e referências logísticas reais; integração com compras/entregas somente quando contrato exigir. Reaproveite o cadastro existente e permita ativação/inativação auditada.
VALIDAR: Teste identificadores inválidos, duplicidade, vínculo em documento e acesso A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### ADM-011 — Configurações

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE ADM-011 — Configurações.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/administracao/ADM-011.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Consolide preferências gerais da aplicação e parâmetros permitidos: locale, notificações, operação, limites e integrações configuráveis conforme contrato. Mostre estado não configurado em provedores; segredos no backend. Não transforme configurações em novo módulo ou copie o cadastro Empresa.
VALIDAR: Teste persistência por empresa, parâmetro inválido, reinício/sessão, usuário sem capability e auditoria.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 03 — Clientes e CRM

### CLI-001 — Clientes — lista

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE CLI-001 — Clientes — lista.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/clientes-crm/CLI-001.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua rota existente /cliente/listar: busca, filtros por tipo/status, tabela paginada server-side (20/50/100), ordenação, acesso a cadastro/editar e imagem/indicador de veículos quando disponível. API deve usar parâmetros reais busca, tipoCliente, status, page, size e sort. Nenhuma CLI-004.
VALIDAR: Teste resultado vazio, filtros combinados, paginação, cliente de empresa B e navegação para edição.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### CLI-002 — Clientes — cadastro

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE CLI-002 — Clientes — cadastro.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/clientes-crm/CLI-002.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua /cliente/cadastro reutilizando formulário atual. Campos pessoa física/jurídica, documentos, nome, contato, endereço, preferências e vínculo de veículos de acordo com contrato; máscaras pt-BR, validação backend e prevenção de duplicidade por empresa.
VALIDAR: Teste CPF/CNPJ inválido, campos condicionais, salvar duas vezes, veículo vinculado e resposta 422/409.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### CLI-003 — Clientes — editar/visualizar

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE CLI-003 — Clientes — editar/visualizar.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/clientes-crm/CLI-003.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua /cliente/editar/:uuid reutilizando mesmo componente de cadastro, carregando identificador real do backend sem assumir que UUID textual é a chave SQL. Permita edição autorizada, abas/histórico e veículos vinculados; preserve relações com OS e orçamento.
VALIDAR: Teste cliente inexistente, empresa B, edição concorrente, histórico e rota direta.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### CLI-005 — CRM — Comunicação

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE CLI-005 — CRM — Comunicação.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/clientes-crm/CLI-005.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua /cliente/crm/comunicacao com timeline real por cliente/OS, modelos de mensagem, preview das variáveis, filtro canal/status/período, histórico paginado, envio/retry apenas com provedor e consentimento corretos; nunca declarar WhatsApp externo como entregue.
VALIDAR: Teste telefone/e-mail inválido, opt-out, template incompleto, falha de provedor, retry idempotente e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### CLI-006 — CRM — Questionamento de pesquisa

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE CLI-006 — CRM — Questionamento de pesquisa.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/clientes-crm/CLI-006.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua /cliente/crm/pesquisa: modelos versionados de questionário, perguntas NPS/CSAT/escalas, aplicação vinculada a cliente/OS/entrega, coleta interna e indicadores agregados conforme permissão. Não enviar externamente sem canal configurado.
VALIDAR: Teste publicação e versão imutável, escala inválida, resposta repetida, dados pessoais/opt-in, empresa B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 04 — Oficina: catálogos, veículos e agenda

### OFC-008 — Oficina — catálogo de serviços

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-008 — Oficina — catálogo de serviços.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-008.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Reaproveite catálogo Serviços com código, descrição, categoria, tempo previsto, preço, impostos informativos e status conforme contrato. Ofereça busca, edição e desativação; permita que Orçamentos/OS selecionem serviços sem copiar tabela de cadastro.
VALIDAR: Teste preço/tempo inválido, serviço inativo em orçamento, filtros, permissões e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### OFC-009 — Oficina — veículos

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-009 — Oficina — veículos.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-009.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Finalize consulta/gestão operacional de veículos já relacionados a clientes: placa, chassi quando previsto, marca/modelo/ano, quilometragem, fotos e histórico OS, com permissões. Reuse entidade proprietária de veículo e vínculo correto cliente-empresa.
VALIDAR: Teste placa repetida conforme regra, troca de cliente autorizada, upload, histórico e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### OFC-001 — Oficina — Agenda

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-001 — Oficina — Agenda.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-001.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua agenda dia/semana/mês, eventos por cliente/veículo/serviço, colaborador, box/recurso, período, status, lembretes e filtros. Use contratos reais e valide conflitos de horários no servidor, timezone da empresa e permissão.
VALIDAR: Teste horário sobreposto, reagendamento concorrente, filtro de profissional, cancelamento e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 05 — Orçamentos e Ordens de Serviço

### OFC-002 — Orçamentos — listagem

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-002 — Orçamentos — listagem.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-002.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua lista e acompanhamento de orçamentos: número, cliente, veículo, status, valores, validade, versão, responsável, busca/filtros, ordenação e acesso ao editor existente. Reconcile prefixos Angular/Controller; não criar rota duplicada.
VALIDAR: Teste filtro por status, orçamento de outra empresa, paginação, versões/cancelamento e 403.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### OFC-003 — Orçamentos — criar/editar e aprovação

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-003 — Orçamentos — criar/editar e aprovação.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-003.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua editor com cliente, veículo, consultor, peças/serviços, quantidades/tempo, preços, descontos e total no backend, fotos, complementos, envio e aceite rastreável por versão. Confirme /v1/orcamentos e proxy real; aprovação não baixa estoque nem emite nota automaticamente.
VALIDAR: Teste desconto extremo, itens inválidos, estoque faltante, autorização de versão antiga, 409 e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### OFC-004 — Ordens de Serviço — lista

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-004 — Ordens de Serviço — lista.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-004.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua listagem/quadro de OS com estados, número, cliente, veículo, previsão, responsável e ações permitidas; integrar filtros, busca e detalhes. Preserve o P0 já corrigido contra acesso por ID de outra empresa, reexecutando somente teste de regressão necessário.
VALIDAR: Teste GET/PUT/DELETE cruzados A/B, paginação, filtros, acesso direto e capabilities.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### OFC-005 — Ordens de Serviço — execução

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-005 — Ordens de Serviço — execução.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-005.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Finalize detalhe operacional da OS com contexto, itens, execução, diagnósticos, inspeções, apontamentos, fotos, complementos e histórico, aprovações versionadas e regras de fechamento. Os owners de Estoque, Financeiro e Fiscal fazem as respectivas mutações; não criar segunda OS.
VALIDAR: Teste autorização antiga, complementos, itens concorrentes, fotos, mudança de status, A/B e estorno.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 06 — Diagnósticos e inspeções

### OFC-006 — Diagnósticos

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-006 — Diagnósticos.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-006.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua registro de sintomas, testes, medições, causas, recomendações, responsável, evidências/fotos e vínculo à OS/veículo, com revisão e estados autorizados. Reaproveite diagnóstico existente; não confundir com checklist de Inspeções.
VALIDAR: Teste diagnóstico sem veículo/OS válido, anexos, versão, acesso por cargo e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### OFC-007 — Inspeções

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-007 — Inspeções.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-007.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Finalize modelos e execução de checklist por seção e item, condições, evidências/fotos, notas, resultado e revisão; vincule à OS e permita relatório/compartilhamento autorizado. Preserve separação entre modelo reutilizável e inspeção aplicada.
VALIDAR: Teste item obrigatório, anexos, modelo versionado, checklist incompleto, 403 e acesso de outra empresa.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 07 — Estoque e compras

### EST-001 — Estoque — visão e saldos

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE EST-001 — Estoque — visão e saldos.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/estoque-compras/EST-001.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua saldo por produto/local, disponibilidade, reservado, mínimo, valorização permitida, filtros e alertas; consumir movimentações reais, não criar saldo paralelo. Reaproveite estoque e conferência por empresa.
VALIDAR: Teste saldo negativo, reserva concorrente, total reconciliado com EST-007 e dados A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### EST-002 — Produtos — cadastro e lista

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE EST-002 — Produtos — cadastro e lista.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/estoque-compras/EST-002.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua catálogo de produtos/peças com SKU/código, descrição, unidade, categoria, custo, preço, códigos fiscais quando aplicáveis, estoque mínimo, imagens e situação. Integre PDV, OS e compras mantendo preço e controle de permissões.
VALIDAR: Teste SKU duplicado, imagem inválida, imposto condicionado, item inativo e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### EST-003 — Kits

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE EST-003 — Kits.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/estoque-compras/EST-003.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Finalize composição versionada de kits com produtos/quantidades/unidades, custo e preço derivados das regras oficiais; impedir kit circular e disponibilidade fictícia. Integre orçamento/OS/PDV apenas conforme owner e forma de explosão definida no contrato.
VALIDAR: Teste itens duplicados, componentes faltantes, preço/estoque inconsistentes e empresa B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### EST-004 — Compras — pedidos e recebimento

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE EST-004 — Compras — pedidos e recebimento.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/estoque-compras/EST-004.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua pedido de compra com fornecedor, itens, condições, aprovação, status, recebimento parcial/total, divergência de preço/quantidade e atualização de estoque por evento único. Integre Financeiro sem duplicar obrigações.
VALIDAR: Teste recebimento repetido, parcial, cancelamento pós-entrada, estoque/fornecedor de outra empresa.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### EST-005 — Cotações

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE EST-005 — Cotações.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/estoque-compras/EST-005.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua solicitação de cotação a fornecedores, itens, comparação de propostas, prazos, impostos, condições e decisão de compra documentada. Não declarar envio externo concluído sem confirmação de integração; converter em pedido sem duplicar itens.
VALIDAR: Teste propostas empatadas, moeda/desconto, fornecedor inativo, seleção e acesso A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### EST-006 — Inventário — contagem

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE EST-006 — Inventário — contagem.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/estoque-compras/EST-006.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua inventário geral/rotativo, snapshot de saldos, contagens e recontagens, divergências, justificativas, aprovação e ajuste único por EST-007. Reconciliar movimentações ocorridas durante contagem; não congelar venda global automaticamente.
VALIDAR: Teste leituras repetidas, movimentação concorrente, segunda contagem, aprovação segregada e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### EST-007 — Movimentações de estoque

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE EST-007 — Movimentações de estoque.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/estoque-compras/EST-007.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Finalize livro auditável de entrada, saída, reserva, transferência autorizada, ajuste e reversão, sempre originado do owner correto e com referência/idempotência. Ordenação e filtros por produto, local, data, origem e operador; não editar movimentos históricos.
VALIDAR: Teste dupla baixa, saldo negativo indevido, reversão, concorrência e empresa B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 08 — Financeiro

### FIN-001 — Contas financeiras

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIN-001 — Contas financeiras.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/financeiro/FIN-001.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua abas Contas bancárias, Plano de Contas, Centros de Custo, Tipos de Documento e Condições de Pagamento, reutilizando cadastros reais. Use decimal exato e dados financeiros só com capability; proíba exclusão de conta com movimento.
VALIDAR: Teste identificadores inválidos, relações em uso, empresas A/B e auditoria.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIN-002 — Contas a Receber

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIN-002 — Contas a Receber.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/financeiro/FIN-002.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua títulos, parcelas, vencimentos, status, recebimento parcial/total, descontos/multa/juros conforme regra, baixas e estornos idempotentes. OS/venda alimentam origem autorizada; não duplicar receita ou confundir faturamento com caixa.
VALIDAR: Teste baixa duplicada, pagamento parcial, reversão, datas e origem de outra empresa.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIN-003 — Contas a Pagar

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIN-003 — Contas a Pagar.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/financeiro/FIN-003.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua obrigações a fornecedores, parcelas, vencimentos, aprovação por alçada, pagamentos e estornos, com valor exato e trilha. Compras fornece origem; não dar baixa por tentativa de provedor sem confirmação real.
VALIDAR: Teste pagamento duplo, bloqueio por alçada, parcial, atraso e acesso A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIN-004 — Lançamentos Bancários

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIN-004 — Lançamentos Bancários.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/financeiro/FIN-004.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua lançamentos de entrada/saída por conta, data, natureza, origem, anexo e status de conciliação. Diferencie lançamento manual de transação importada e título. Não altere valor conciliado sem desconciliação auditada; estorno por evento compensatório.
VALIDAR: Teste importação repetida OFX, conciliação concorrente, reversão, anexo e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIN-005 — Movimento de Caixa

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIN-005 — Movimento de Caixa.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/financeiro/FIN-005.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua abertura, entradas, saídas, sangrias, reforços, fechamento e apuração de diferenças com responsável, horário e histórico. Controle concorrência e não permita movimentar caixa fechado; vincule recebimentos reais com idempotência.
VALIDAR: Teste dois operadores, fechamento duplicado, diferença de caixa, devolução e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIN-006 — Movimento Bancário e Conciliação

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIN-006 — Movimento Bancário e Conciliação.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/financeiro/FIN-006.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua extratos/transações bancárias, importação autorizada (OFX ou integração real disponível), pareamento com lançamentos e títulos, conciliado/pendente, desfazer com trilha e filtros. Nunca inferir pagamento confirmado de mera sugestão de match.
VALIDAR: Teste importação repetida, duplicidade, divergência de valor, concorrência e isolamento de contas.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIN-007 — Comissões

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIN-007 — Comissões.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/financeiro/FIN-007.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua regras versionadas, apuração por colaborador/item/OS e recebimento, aprovação, liquidação e reversão. Aplicar regra vigente para pagamento parcial, serviços concluídos e estorno; funcionário só consulta sua comissão conforme capability.
VALIDAR: Teste dois técnicos no item, desconto posterior, regra alterada, recebimento parcial e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 09 — Pátio, entregas, histórico e PDV

### PATIO-001 — Gestão de Pátio — tela interna

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE PATIO-001 — Gestão de Pátio — tela interna.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/gestao-patio/PATIO-001.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Implemente UMA tela interna com modos Kanban, Lista, Agenda, Detalhe OS, Boxes/Recursos, cronômetros/apontamentos, painel TV e alertas. Cartões consultam OS existente; mover etapa valida transição backend; timers persistidos, idempotentes e reidratados após reload, com alternativa acessível ao drag-and-drop. Sem sistema externo ou seis ScreenIds.
VALIDAR: Teste movimentação concorrente, timer duplo, box ocupado, atualização/polling, TV com PII protegida e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### OFC-010 — Entregas de veículos

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-010 — Entregas de veículos.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-010.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua fila de veículos prontos e vistoria comparativa entrada/saída, km/tanque, acessórios, fotos, pendências, responsável, termo e confirmação de entrega rastreável. Só liberar OS elegível; não criar prova de assinatura qualificada sem provedor.
VALIDAR: Teste divergência de vistoria, pendência crítica, confirmação repetida, cliente/OS de outra empresa.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### OFC-011 — Histórico da oficina

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-011 — Histórico da oficina.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-011.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua timeline consultiva por cliente, veículo e OS: serviços, peças, eventos, orçamentos, inspeções e entregas permitidos. Reuse eventos reais e links canônicos; sem duplicar dados transacionais nem expor histórico de outra empresa.
VALIDAR: Teste período, paginação, dados ausentes, filtros combinados, usuário sem autorização e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### OFC-012 — Vendas diretas — balcão

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE OFC-012 — Vendas diretas — balcão.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/oficina/OFC-012.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua PDV sem exigir OS: carrinho de produtos/SKU, disponibilidade, quantidade, desconto, total e pagamento autorizado; confirmar venda idempotentemente, integrar EST/FIN/FIS por seus owners e reverter cancelamento de modo auditável. Reuse rotas /pdv existentes.
VALIDAR: Teste concorrência estoque, desconto, venda duplicada, estorno, consumidor sem CPF e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 10 — Fiscal

### FIS-001 — Documentos Fiscais

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIS-001 — Documentos Fiscais.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/fiscal/FIS-001.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua central fiscal: lista unificada de documentos por modelo, série/número, chave, destinatário, status e ambiente; ações de consulta, rascunho, download e monitoramento. Consultar providers reais, sem autorizar documento fictício.
VALIDAR: Teste filtros, falha integração, XML restrito, doc de outra empresa e permissão por ação.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIS-002 — NF-e

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIS-002 — NF-e.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/fiscal/FIS-002.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua editor/validador NF-e modelo 55 com emitente, destinatário, CFOP/NCM, itens, base/impostos, certificado, numeração, transmissão e protocolo somente com integração SEFAZ habilitada. Imutabilidade de XML autorizado e cancelamento conforme legislação verificada.
VALIDAR: Teste certificado ausente, timeout/retry sem duplicar nota, rejeição, protocolo real, 403 e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIS-003 — NFC-e / Cupom

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIS-003 — NFC-e / Cupom.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/fiscal/FIS-003.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua emissão local e fluxo de NFC-e/cupom de venda do PDV, tributação e pagamentos conforme modelo fiscal/UF, contingência se legalmente suportada e consulta de protocolo real. Sem provedor não afirmar emissão nem recibo autorizado.
VALIDAR: Teste cancelamento, offline, duplicidade de numeração, venda sem autorização fiscal e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIS-004 — NFS-e

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIS-004 — NFS-e.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/fiscal/FIS-004.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua rascunho, validação e controle de NFS-e de serviço com município/inscrição, tomador, serviço, valores/impostos e status de envio. Provedor municipal/ambiente deve ser configurado e validado; não supor layout nacional uniforme.
VALIDAR: Teste município não integrado, imposto inválido, transmissão falha, cancelamento e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIS-005 — Manifestação do Destinatário

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIS-005 — Manifestação do Destinatário.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/fiscal/FIS-005.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua consulta/importação controlada de documentos de entrada e eventos de manifestação: ciência, confirmação, desconhecimento ou operação não realizada conforme requisitos do contrato e SEFAZ, sem marcar evento como efetivado sem protocolo.
VALIDAR: Teste chave inválida, evento repetido, prazo aplicável, timeout e documento de outra empresa.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIS-006 — Inutilização de numeração

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIS-006 — Inutilização de numeração.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/fiscal/FIS-006.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua solicitação de inutilização de faixa/série/modelo/ano com justificativa e validação de números realmente não utilizados, autorização por perfil e protocolo externo real. Nunca alterar sequência já emitida ou inventar confirmação.
VALIDAR: Teste faixa parcialmente usada, números repetidos, erro de provedor, auditoria e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIS-007 — NCM / classificação fiscal

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIS-007 — NCM / classificação fiscal.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/fiscal/FIS-007.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua catálogo/consulta de NCM com código, descrição, validade e seleção para produto, usando fonte oficial e versionamento de referência. Não inventar alíquotas ou códigos; manter histórico fiscal de documentos anteriores.
VALIDAR: Teste código inexistente, vencido, classificação sem permissão, produto legado e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### FIS-008 — SINTEGRA

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE FIS-008 — SINTEGRA.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/fiscal/FIS-008.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua geração e validação do arquivo SINTEGRA conforme layout oficial aplicável à UF/período, registros/totais e erros por linha, download autorizado e histórico. Gerar arquivo não significa transmitir obrigação.
VALIDAR: Teste layout vencido, IE inválida, lacunas fiscais, totais divergentes e empresa B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 11 — Home e gráficos

### HOME-001 — Início — quatro dashboards

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE HOME-001 — Início — quatro dashboards.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/inicio/HOME-001.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua uma única /home com seletores Padrão, Gerencial, Financeiro e Orçamento, filtros por período e KPIs/tabelas reais. Não adicionar Ações Rápidas. Integrar /api/dashboard real, timezone da empresa, fórmulas por owner e permissão financeira separada; indisponibilidade não vira zero.
VALIDAR: Teste cada KPI contra lista de origem, troca de visões, períodos, ausência de acesso e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### GRA-001 — Gráficos Gerenciais

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE GRA-001 — Gráficos Gerenciais.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/graficos-gerenciais/GRA-001.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua uma única página de gráficos com visões Vendas, Financeiro, Faturamento e Operação, granularidade e comparação, séries auditáveis, tooltip de fórmula/fonte e tabela acessível equivalente. Ative ligação de rota real sem duplicar componente.
VALIDAR: Teste agregações conciliadas com HOME/Relatórios, gráfico vazio, filtros, dados financeiros restritos e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 12 — Relatórios

### REL-001 — Relatórios Comerciais

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE REL-001 — Relatórios Comerciais.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/relatorios/REL-001.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua filtros por período, status, consultor, origem, negócio/OS/venda e totalizadores de vendas/ticket/conversão; base no owner correto. Prévia e exportação CSV/PDF devem refletir a mesma consulta, sem somar cancelados como fechados.
VALIDAR: Teste comparação prévia/exportação, duplicidade por versão, denominador e empresa B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### REL-002 — Relatórios de Clientes

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE REL-002 — Relatórios de Clientes.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/relatorios/REL-002.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua relatórios autorizados de base de clientes, perfil, novos/ativos/inativos, vínculo de veículos, frequência e retenção conforme contrato; filtros por período/segmento e exportação com proteção LGPD.
VALIDAR: Teste anonimização por perfil, contagem sem duplicados, exportação idêntica e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### REL-003 — Relatórios de Serviços

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE REL-003 — Relatórios de Serviços.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/relatorios/REL-003.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua análise de serviços executados, quantidade, tempo, valores, responsável, produtividade/retorno autorizados e filtros por OS/período. Diferencie planejado, executado e recebido; usar snapshots existentes.
VALIDAR: Teste OS cancelada, serviço duplicado, totalizadores, CSV/PDF e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### REL-004 — Relatórios de Estoque

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE REL-004 — Relatórios de Estoque.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/relatorios/REL-004.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua saldo, giro, movimentações, custos autorizados e níveis críticos com período/local/produto e reconciliação ao EST-001/007. A consulta é leitura, não gera ajuste de estoque.
VALIDAR: Teste saldo x livro, movimentos tardios, valorização protegida e exportação A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### REL-005 — Relatórios de Compras

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE REL-005 — Relatórios de Compras.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/relatorios/REL-005.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua relatório de pedidos, fornecedores, itens, valores, entregas, prazo, divergências e evolução de compras por período e status; origem em EST-004/005 sem contar recebimentos duplicados.
VALIDAR: Teste pedidos cancelados/parciais, totais, CSV/PDF e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### REL-006 — Relatórios Financeiros

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE REL-006 — Relatórios Financeiros.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/relatorios/REL-006.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua contas a pagar/receber, realizado x previsto, caixa, inadimplência e conciliação por período/conta/status segundo capabilities. Não misture título emitido com receita recebida; usar decimal exato.
VALIDAR: Teste baixas parciais, estornos, regime temporal, dados restritos e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### REL-007 — Relatórios de Comissões

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE REL-007 — Relatórios de Comissões.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/relatorios/REL-007.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua relatórios de comissões por colaborador, regra, OS, base, prevista/aprovada/paga e competência; reconciliar FIN-007 e efeitos de estorno sem contar duas vezes.
VALIDAR: Teste colaboração múltipla, parcial, aprovações, exportação e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### REL-008 — Relatórios de Produtividade

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE REL-008 — Relatórios de Produtividade.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/relatorios/REL-008.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua produtividade de técnicos/serviços/boxes por tempo apontado real, ociosidade autorizada, previsto x executado e filtros por período/equipe; não usar cronômetro local não persistido.
VALIDAR: Teste intervalos sobrepostos, pausa, dados faltantes, perfil restrito e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### REL-009 — Relatórios Fiscais

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE REL-009 — Relatórios Fiscais.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/relatorios/REL-009.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua consultas e exportação autorizadas de documentos/emissão/cancelamentos/erros por modelo, série, período e situação. Não declarar documento transmitido ou obrigação cumprida por relatório.
VALIDAR: Teste filtro de status real, protocolo ausente, XML restrito, totalização e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### REL-010 — Relatórios Gerenciais

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE REL-010 — Relatórios Gerenciais.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/relatorios/REL-010.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua visão consolidada operacional/comercial/financeira com fórmulas explícitas, drill-down autorizado e filtros coerentes; reutilize fontes dos owners e motor compartilhado de relatórios em vez de nova infraestrutura.
VALIDAR: Teste reconciliar HOME/GRA, métricas sem fonte, permissões, exportação e A/B.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 13 — Inteligência Artificial

### IA-001 — Assistente IA

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE IA-001 — Assistente IA.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/ia/IA-001.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua interface e backend seguro de assistente contextual, com permissão por consulta e ferramentas/ações somente dos owners autorizados; auditoria, limites e respostas com fonte. Provedor ausente = estado configurável, não resposta fictícia.
VALIDAR: Teste prompt injection, dados A/B, pedido proibido, limites, rate-limit e indisponibilidade.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```

### IA-002 — Automações IA

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE IA-002 — Automações IA.
Siga AGENTS.md e leia apenas o bloco A do contrato Documentacao/notion/telas/ia/IA-002.md (e D01–D11/UI-MASTER somente onde pertinente). Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Conclua criador de automações com evento, filtro, condição, template, canal, quiet hours, limite de frequência, dry-run, ativação e logs. Padrão OFF_DRAFT; processamento idempotente e opt-out. Nunca executar pagamentos/notas sem revisão autorizada.
VALIDAR: Teste evento duplicado, pausa, opt-out, provider falhou, excesso de envios e tenant.
ENTREGA: frontend e backend integrados quando aplicável, rotas reais, validações e estados loading/vazio/erro/403/409, permissões no servidor, tenant e auditoria. UI-MASTER, Tabler, responsividade e WCAG 2.2 AA. Reutilizar > estender > criar. Testes focados agora; não confundir build com homologação. Sem commit/push/PR. Resumo em até 6 linhas: arquivos, testes, pendências.
```


## 14 — Qualidade, integração e lançamento (prompts independentes)

### QA-01 — Administração + Auth

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE QA-01 — Administração + Auth.
Siga AGENTS.md; use Documentacao/notion/README.md apenas para localizar as telas da jornada, leia somente seus blocos A pertinentes e consulte D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Valide jornada criar empresa/usuário/funcionário, atribuir cargos/permissões, autenticar e revogar acesso. Teste perfis, alteração de usuário próprio, acesso por URL e dados de duas empresas. Corrija somente falhas observadas, sem redesenho geral.
VALIDAR: Apresente evidências de execução, resultados e bloqueios; corrija apenas falhas comprovadas.
Não maquie bloqueios: QA não executado = NÃO VERIFICADO. Não faça commit/push/PR. Informe arquivos, testes e pendências em até 6 linhas.
```

### QA-02 — Clientes → Oficina → Pátio

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE QA-02 — Clientes → Oficina → Pátio.
Siga AGENTS.md; use Documentacao/notion/README.md apenas para localizar as telas da jornada, leia somente seus blocos A pertinentes e consulte D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Teste integração persistida cliente + veículo → agenda → orçamento com itens → aprovação → OS → diagnóstico/inspeção → pátio/cronômetro → entrega. Verifique versões e permissões, falhas de rede e reentrada. Corrija somente contratos quebrados.
VALIDAR: Apresente evidências de execução, resultados e bloqueios; corrija apenas falhas comprovadas.
Não maquie bloqueios: QA não executado = NÃO VERIFICADO. Não faça commit/push/PR. Informe arquivos, testes e pendências em até 6 linhas.
```

### QA-03 — Estoque → Compras → PDV → Financeiro

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE QA-03 — Estoque → Compras → PDV → Financeiro.
Siga AGENTS.md; use Documentacao/notion/README.md apenas para localizar as telas da jornada, leia somente seus blocos A pertinentes e consulte D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Teste entradas de compras, reservas, saídas, venda de balcão, título a receber/pagar, baixa, estorno e comissão, sem lançamentos duplicados. Verifique idempotência, concorrência, saldo e empresas A/B.
VALIDAR: Apresente evidências de execução, resultados e bloqueios; corrija apenas falhas comprovadas.
Não maquie bloqueios: QA não executado = NÃO VERIFICADO. Não faça commit/push/PR. Informe arquivos, testes e pendências em até 6 linhas.
```

### QA-04 — Fiscal e integrações externas

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE QA-04 — Fiscal e integrações externas.
Siga AGENTS.md; use Documentacao/notion/README.md apenas para localizar as telas da jornada, leia somente seus blocos A pertinentes e consulte D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Teste validações fiscais, modelo correto, XML e certificados usando ambiente homologado e provedor autorizado quando disponível. Sem credenciais, valide apenas o fluxo local e estado BLOQUEADO EXTERNO; não simule emissão ou obrigação cumprida.
VALIDAR: Apresente evidências de execução, resultados e bloqueios; corrija apenas falhas comprovadas.
Não maquie bloqueios: QA não executado = NÃO VERIFICADO. Não faça commit/push/PR. Informe arquivos, testes e pendências em até 6 linhas.
```

### QA-05 — Dashboard, gráficos e relatórios

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE QA-05 — Dashboard, gráficos e relatórios.
Siga AGENTS.md; use Documentacao/notion/README.md apenas para localizar as telas da jornada, leia somente seus blocos A pertinentes e consulte D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Reconcile KPIs com consultas dos owners e prévia com CSV/PDF; verifique fórmulas, períodos, locale, filtros, somas e capabilities. Corrija números inconsistentes e relatórios com exportação divergente.
VALIDAR: Apresente evidências de execução, resultados e bloqueios; corrija apenas falhas comprovadas.
Não maquie bloqueios: QA não executado = NÃO VERIFICADO. Não faça commit/push/PR. Informe arquivos, testes e pendências em até 6 linhas.
```

### QA-06 — Segurança e isolamento final

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE QA-06 — Segurança e isolamento final.
Siga AGENTS.md; use Documentacao/notion/README.md apenas para localizar as telas da jornada, leia somente seus blocos A pertinentes e consulte D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Faça matriz real de 2 empresas/3 perfis em GET/POST/PUT/DELETE e referências cruzadas de recursos; valide autenticação, revogação, RBAC, arquivos e logs. Use testes de integração direcionados e registre evidências de 403/404.
VALIDAR: Apresente evidências de execução, resultados e bloqueios; corrija apenas falhas comprovadas.
Não maquie bloqueios: QA não executado = NÃO VERIFICADO. Não faça commit/push/PR. Informe arquivos, testes e pendências em até 6 linhas.
```

### QA-07 — Frontend e acessibilidade

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE QA-07 — Frontend e acessibilidade.
Siga AGENTS.md; use Documentacao/notion/README.md apenas para localizar as telas da jornada, leia somente seus blocos A pertinentes e consulte D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Valide todas as rotas e telas realmente implementadas em desktop/tablet/mobile, zoom 200%, Tab/Enter/Escape, contraste, foco, validações, toasts, loading, vazio, offline e erro. Corrija por módulo, sem declarar QA visual que não executou.
VALIDAR: Apresente evidências de execução, resultados e bloqueios; corrija apenas falhas comprovadas.
Não maquie bloqueios: QA não executado = NÃO VERIFICADO. Não faça commit/push/PR. Informe arquivos, testes e pendências em até 6 linhas.
```

### QA-08 — CI, builds, banco e estabilidade

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE QA-08 — CI, builds, banco e estabilidade.
Siga AGENTS.md; use Documentacao/notion/README.md apenas para localizar as telas da jornada, leia somente seus blocos A pertinentes e consulte D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Execute Node 22/Yarn build/testes relevantes, Java 21/Maven, Flyway em banco limpo e migrado, testes integrados e pipeline. Corrija causas reais de falha; não desative testes ou validações para ficar verde. Evidencie comandos e resultados.
VALIDAR: Apresente evidências de execução, resultados e bloqueios; corrija apenas falhas comprovadas.
Não maquie bloqueios: QA não executado = NÃO VERIFICADO. Não faça commit/push/PR. Informe arquivos, testes e pendências em até 6 linhas.
```

### QA-09 — Preparação de lançamento

```text
Você é o Codex implementando NERITECH AUTO. Repositório neritechauto-8548/neritechauto, branch feature/develop. Execute SOMENTE QA-09 — Preparação de lançamento.
Siga AGENTS.md; use Documentacao/notion/README.md apenas para localizar as telas da jornada, leia somente seus blocos A pertinentes e consulte D01–D11/UI-MASTER quando necessário. Procure no código real componentes, rotas, controllers, DTOs, entidades, migrações e testes antes de editar; não repita implementação já comprovada.
FAZER: Audite variáveis/segredos, logs, métricas, rate limit, LGPD, backup/restore, migrations, rollback, deploy e plano de contingência. Gere checklist com GO/NO-GO e dependências externas. Não faça deploy, migração em produção nem cobrança real sem autorização.
VALIDAR: Apresente evidências de execução, resultados e bloqueios; corrija apenas falhas comprovadas.
Não maquie bloqueios: QA não executado = NÃO VERIFICADO. Não faça commit/push/PR. Informe arquivos, testes e pendências em até 6 linhas.
```

## Como usar em caso de erro (um prompt complementar, somente se necessário)

```text
Na feature/develop, corrija exclusivamente o erro da última execução no(s) arquivo(s) que você alterou. Não refaça a tarefa, não reavalie 64 telas. Mostre causa raiz, correção mínima e teste de regressão focado. Não faça commit; resposta até 6 linhas.
```

## Critério real de 100%

Só declarar funcionalidade homologada quando houve evidência de **API, persistência, RBAC, tenant, estados, build/testes pertinentes e QA navegador**; integrações externas fiscais/financeiras/mensageria/IA exigem serviço autorizado e resultados reais. Se falta provedor, registre dependência sem fingir emissão/envio/pagamento. Sem autorização, não implantar em produção, alterar dados reais, gerar cobrança ou executar migração destrutiva.

**Conservação de contexto:** o próprio prompt deve resolver a ação. Não abrir toda a documentação, não repetir prompts enormes de sistema, não solicitar commit automático e não executar full build a cada campo pequeno. A sequência completa existe para o usuário escolher **um bloco por vez**, não para ser carregada em uma única sessão de Codex.
