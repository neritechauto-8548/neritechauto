# CODEX — controle de execução

## Parte 00 — Segurança P0 de Ordens de Serviço

- Branch: `feature/develop` (HEAD inicial `b51c3175d4363f20842e88d10aee9cf3d27dbf81`).
- Commit de código P0: `937d685d55303f2d9019305f6ba12bfedf79d382`.
- ScreenIds: OFC-004 e OFC-005. Correção P0 implementada e teste integrado A/B validado; telas completas não homologadas.
- Contratos consultados: Notion 03.01.03, UI-MASTER-001, D01–D11 pertinentes e seções `CONTRATO EXECUTÁVEL CODEX` de OFC-004/OFC-005.
- Escopo: acesso a OS por ID, consultas por empresa/cliente, relações cliente/veículo/status, itens e fotos diretamente ligados à OS, leitura pública por ID e fallback de empresa no serviço Angular.
- Migração Flyway: não necessária para a correção P0; schema e dados existentes preservados.
- Teste integrado: `mvn -Dtest=OrdemServicoTenantIntegrationTest test` — 1 teste, 0 falhas, 0 erros (PostgreSQL/Testcontainers, JWT A/B; GET/PUT/DELETE, relações cruzadas, item e perfil sem capability).
- Frontend: `yarn build` — aprovado. Backend: `mvn -DskipTests package` — aprovado. CI remoto e QA manual pendentes.
- QA manual pendente: fluxos reais em navegador com dois usuários/empresas, permissões por cargo, upload/download de fotos, integração de itens, responsividade e compatibilidade PDF.
- Bloqueios conhecidos: autenticação do `gh` indisponível neste ambiente; o artefato Jaspersoft OpenPDF retornou 403 e foi substituído no POM pela versão pública equivalente para permitir o build. Relatórios PDF precisam de QA de compatibilidade.
- Próximo número do roteiro: Parte 01. Não iniciada nesta execução.
