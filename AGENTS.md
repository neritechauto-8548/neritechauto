# NeriTech Auto — Instruções Operacionais do Codex

## Missão
Continuar a reconstrução D5 do NeriTech Auto como SaaS profissional para gestão de oficinas. Preserve comportamento existente somente quando compatível com a especificação oficial. Não infle progresso: diferencie implementação, teste, build e evidência real.

## Git — regra absoluta
- Trabalhe somente em `feature/neritech-auto-rebuild`, salvo ordem explícita do usuário.
- Nunca faça commit direto em `main`.
- Não crie branch nova por iniciativa própria.
- Não faça merge para `main`.
- Não reescreva histórico nem faça force-push.
- Commits devem ser focados, pequenos o suficiente para revisão e com mensagem descritiva.

## Economia de contexto — obrigatório
O contexto é recurso escasso. **Não leia toda a documentação em toda tarefa.**

Fluxo padrão:
1. leia este `AGENTS.md`;
2. identifique o slice/tela/fluxo exato da tarefa;
3. inspecione primeiro o código atual relacionado;
4. pesquise somente a especificação/documentação necessária;
5. carregue trechos específicos, não documentos inteiros, quando possível;
6. consulte o Notion somente para lacuna, conflito ou regra/tela específica não representada localmente.

Use `Documentacao/CONTEXTO_TRANSVERSAL_CODEX.md` como **índice e consulta sob demanda**, não como leitura obrigatória integral.

## Fontes de verdade
Hierarquia:
1. decisão explícita do usuário na tarefa atual;
2. Notion oficial `NERITECH — Documentação Oficial` (`3bb27279-1906-815d-b711-d225de4c2b06`);
3. especificação canônica `TELA-*` da tela/módulo atual;
4. `DESIGN.md` para UI/UX;
5. `CONVENCOES_CODIGO.md` para idioma/naming/banco/comentários;
6. `DX.md` quando arquitetura/ambiente exigir;
7. `BRAND.md` somente quando marca/copy/identidade forem relevantes;
8. código atual de `feature/neritech-auto-rebuild` como evidência de implementação;
9. legado apenas como referência.

Se o Notion estiver acessível, **não percorra a árvore inteira**. Busque pelo código `TELA-*`, nome da tela, módulo, regra, entidade ou fluxo específico.

## Idioma oficial do código — obrigatório
A regra completa está em `CONVENCOES_CODIGO.md` e deve ser obedecida.

Todo código de domínio novo ou reconstruído da NeriTech deve usar **português brasileiro** em:
- classes, entidades e DTOs próprios;
- métodos/funções de negócio;
- atributos, variáveis, parâmetros e constantes de domínio;
- enums e valores próprios quando não forem contratos externos;
- tabelas e colunas PostgreSQL (`snake_case`);
- descrição de migrations Flyway;
- comentários, JavaDoc, TSDoc e documentação inline;
- nomes/cenários de testes de domínio;
- mensagens de validação e negócio.

Exceções: palavras-chave da linguagem, APIs de framework/biblioteca, protocolos, headers/claims e contratos externos imutáveis (Stripe, Stone, fiscal etc.). Não traduza o legado globalmente sem migration/versionamento compatível.

## Stack oficial
### Frontend
- Angular 20.x, standalone components.
- PrimeNG 20.x.
- Tailwind CSS para layout, spacing e responsividade.
- Preset semântico próprio NeriTech; Aura default não é identidade do produto.
- Tabler Icons via wrapper local `NeriTechIcon` enquanto a base for Angular 20.
- Não introduzir Lucide, PrimeIcons, Angular Material, Material Symbols ou Font Awesome em UI reconstruída sem restrição explícita de compatibilidade.

### Backend
- Java 21, Spring Boot 3.2.x, Spring Security/JWT.
- PostgreSQL + Flyway + OpenAPI.
- Multi-tenant SaaS.
- JasperReports quando aplicável.

## Contrato visual
Para criar ou alterar substancialmente uma tela:
1. localize a `TELA-*` canônica;
2. consulte apenas as seções relevantes de `DESIGN.md`;
3. consulte Stitch/Figma aprovado quando houver;
4. use código existente apenas como evidência, nunca para sobrepor a especificação.

Referência conceitual:
`NeriTech Auto = UX operacional Shopmonkey + disciplina visual Stripe + domínio brasileiro de oficinas + identidade NeriTech`.

Regras:
- Minimal Enterprise;
- hierarquia, spacing e tokens consistentes;
- contexto e próxima ação segura devem estar claros;
- uma ação primária dominante por contexto;
- estados loading/vazio/erro/bloqueio/sucesso reais;
- responsividade desktop/tablet/mobile;
- não fabricar KPIs, gráficos, timelines, dinheiro ou dados ausentes no backend;
- preferir componentes/tokens compartilhados a SCSS ad hoc.

## Segurança e tenancy — não negociável
- Tenant vem somente da autenticação backend + vínculo persistido usuário/empresa.
- Browser (`localStorage`, query params, payload ou header) nunca concede autoridade de tenant.
- `X-Tenant-Id`, se mantido, apenas seleciona tenant já autorizado.
- Backend é autoridade final de permissão; guard/menu frontend é UX.
- Deny by default.
- Nunca introduzir `empresaId: 1`, tenant hardcoded, credenciais hardcoded ou bypass ADMIN implícito.
- Cross-tenant não pode vazar existência/dados de outra empresa.
- Minimizar PII em DTOs de leitura e logs.

Para regras transversais adicionais, consulte sob demanda `Documentacao/CONTEXTO_TRANSVERSAL_CODEX.md`.

## Arquitetura frontend
- Shell, menu, topbar e rotas seguem a UI Master/cânone atual.
- `PageHeader` compartilhado é o padrão.
- Rotas Home oficiais: `/home/gerencial`, `/home/financeiro`, `/home/orcamentos`, `/home/operacional`.
- Não expor rotas demo/template em produção.
- Autenticação usa Auth shell reutilizável; nunca persistir senha no navegador.
- UI operacional deve preservar identidade/contexto do objeto, estado, pendência, responsável e próxima ação quando relevantes.
- Backend/read model ausente deve aparecer como indisponível/parcial, nunca como dado fictício.

## Permissões
Códigos persistidos legados continuam válidos até migração formal. Não invente novo código persistido apenas porque uma capacidade conceitual possui outro nome. Faça mapeamento/migration quando necessário e documentado.

## Qualidade de código
- Analise antes de criar; refatore/reuse antes de duplicar.
- Angular strict typing e `strictTemplates` devem continuar válidos.
- Evite `any`; isole fronteiras legadas inevitáveis.
- Evite N+1 e agregações de domínio no navegador quando pertencem ao backend.
- Não apagar/mesclar dados de produção silenciosamente em Flyway.
- Não expor segredos.
- Não fazer refatoração cosmética fora do slice atual.

## Definição de pronto por tarefa
1. Especificação relevante consultada de forma direcionada.
2. Implementação atual inspecionada.
3. Divergência e riscos de tenancy/permissão avaliados.
4. Menor batch coerente de qualidade de produção implementado.
5. Testes focados adicionados/atualizados quando aplicável.
6. Formatter/linter/type-check/test/build relevantes executados quando o ambiente permitir.
7. Falhas introduzidas pela mudança corrigidas.
8. UI comparada com comportamento desktop/tablet/mobile quando aplicável.
9. Não declarar CI/build verde sem evidência observada.
10. Commit somente dos arquivos relacionados.

## Execução autônoma
- Não pare após cada arquivo.
- Não peça confirmação para decisão técnica reversível coberta pela documentação.
- Não peça “continue”.
- Pare somente diante de bloqueio real, ação irreversível não autorizada, conflito material de regra ou falta de credencial/acesso indispensável.

## Relatório de tarefa
Ao final informe objetivamente:
- commits e arquivos principais;
- comportamento implementado;
- testes/build realmente executados e resultado;
- especificação visual/funcional usada;
- implicações de segurança/tenancy;
- pendências reais;
- percentual somente quando sustentado por evidência.

Não faça merge na `main`.
