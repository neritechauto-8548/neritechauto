# 03.01.04 🖥️ Telas & UX

> **Cópia de consulta do Notion, gerada em 2026-10-14.** Fonte canônica: https://app.notion.com/p/98501fd65a9c8379a9a281145568f52a. Não editar esta cópia como norma principal.
> **Última edição registrada no Notion:** 2026-10-14T18:02:55.530Z.


## LEIA PRIMEIRO — NORMA EDITORIAL E PRECEDÊNCIA DOCUMENTAL
**Auditoria estrutural realizada em 14/10/2026:** 64 páginas de tela conferidas individualmente, cada uma com **um** `CONTRATO EXECUTÁVEL CODEX` no início e sem truncamento do conteúdo na leitura. Os módulos mantêm 63 páginas-filhas + PATIO-001 na própria página de Gestão de Pátio. ADM-002 Filiais e CLI-004 legada não fazem parte das 64.
### Ordem única de leitura (sem reescrever especificações)
1. **03.01.03 Módulos:** arquitetura/navegação e ownership, sem subproduto do Pátio nem Filiais.
2. **04.01 UI-MASTER-001:** design system vigente; preservar sidebar original com abertura/recolhimento.
3. **Esta página, primeiro bloco ENTRYPOINT + D01–D11:** decisões globais e método de implementação.
4. **Página do ScreenId, seção inicial CONTRATO EXECUTÁVEL CODEX:** requisitos vigentes específicos; os trechos subsequentes continuam sendo complementos, exemplos e histórico de evidências, sem sobrepor regras aprovadas.
5. **Inventário 03.01.04.01:** tabela de auditoria, correspondência de código e rastreabilidade; **não** é repositório alternativo de requisitos nem substitui o ScreenId.
6. **GitHub HEAD feature/develop:** fonte para existência de API, rota, DTO, entidade, migração e tests; não inferir implementação pelos exemplos do Notion.
### Template obrigatório de estrutura em cada página de tela
- **A. Contrato executivo:** ScreenId/owner, objetivo e escopo, campos (rótulo/tipo/validação/obrigatoriedade), colunas/filtros/ações, estados, regras e máquina de status, permissões e empresa/tenant, APIs e DTOs reais/propostos claramente separados, casos de aceite e testes.
- **B. Complementos de evidência:** fotos, benchmark, rótulos observados no vídeo, referências de mercado e dúvidas explicitamente classificadas em observação/proposta/validação.
- **C. Execução e homologação:** rota/componente/backend reais, commit SHA, migração, comandos e resultados dos testes, validação de navegador, status objetivo IMPLEMENTADO/VALIDADO/HOMOLOGADO. Se não houver evidência, deixar NÃO VERIFICADO.
- **D. Registros históricos:** apenas como rastreabilidade; não repetir regras atuais nem instruir Codex a implementar rotas ou UX antigas.
**Regra:** mudanças futuras editam o bloco A existente, não acrescentam outro `CONTRATO EXECUTÁVEL CODEX`. É proibido criar documentação paralela por ScreenId ou repetir especificação inteira no Inventário. Contagem de páginas não é percentual de desenvolvimento.
### Achados organizacionais e ações
- **Conforme:** 64/64 contratos, hierarquia de 11 módulos, títulos e ScreenIds, GRA-001 separado de Relatórios, Pátio interno, remoção de Filiais do menu.
- **Corrigido:** referências operacionais antigas a Filiais foram substituídas por empresa/tenant, inclusive em Oficina; as proibições explícitas de Filiais permanecem para evitar reintrodução. As seis subpáginas do Pátio foram renomeadas como áreas internas, sem novos ScreenIds.
- **Organização editorial executada:** as 64 páginas canônicas agora distinguem contrato A, detalhamento B e registro D; evidências do vídeo possuem seção C quando há material específico. Os dois documentos centrais estão classificados por autoridade, catálogo, rastreabilidade e histórico. Registros antigos foram preservados como evidência não vinculante, sem replicar seus textos nos prompts Codex. **Conferência transversal complementar:** Product Bible, Modelo de Domínio, Jornadas, Regras Globais, Fundação/Guia/Prompt Mestre, Home, Gráficos, Histórico, PDV, Diagnósticos e contratos de APIs/testes de Orçamento, OS e Inspeções foram reconciliados com empresa/tenant autenticado e preservação dos IDs reais (Long/UUID).
- **Pendência técnica:** nomes legados de rotas/APIs, regras legais e estado de código continuam exigindo conferência no GitHub; documentação suficiente para implementação não significa homologação.
### Checklist antes de cada execução Codex
- [ ] ScreenId único e owner definidos
- [ ] Nenhuma referência ativa a Filiais, CLI-004 ou Pátio externo
- [ ] Campos, tabelas, ações, validações e estados da página consultados
- [ ] Rotas reais, DTOs, API, capabilities e tenancy conferidos no repositório
- [ ] Mudança documentada no bloco A, sem apêndice duplicado
- [ ] Resultado de build, testes e evidência registrado sem marcação falsa de 100%
## A — ENTRYPOINT OFICIAL — CODEX AUTÔNOMO \| DOCUMENTAÇÃO 64/64 CONSOLIDADA
**Snapshot de código que deve ser consultado antes de qualquer tarefa:** [Auditoria de Contratos × HEAD — ](https://app.notion.com/p/3ee01fd65a9c81c9bb3dc13d9906d366)[`08685e6ae68c`](https://app.notion.com/p/3ee01fd65a9c81c9bb3dc13d9906d366)[ (14/10/2026)](https://app.notion.com/p/3ee01fd65a9c81c9bb3dc13d9906d366). A matriz identifica 64 ScreenIds, o status estrutural de rotas/controllers e bloqueios P0/P1. Os casos P0 são OS, Orçamento, empresaId arbitrário/fallback no Financeiro e upload de anexo sem persistência comprovada; P1 inclui menu produtivo, guards, Pátio e prefixos HTTP. **A implementação pode começar pelos bloqueios P0; homologação de qualquer recurso exige testes reais.**
**Data de fechamento documental desta rodada:** 14/10/2026. **Situação:** cada uma das **64 páginas canônicas** possui agora seção inicial `CONTRATO EXECUTÁVEL CODEX — ScreenId`, com finalidade, campos/validações, colunas/filtros/ações, regras decididas, ownership/API, permissões/tenant e casos de aceite. **Persistência verificada 64/64** por leitura das páginas. Esse estado significa **PRONTA PARA DESENVOLVIMENTO GUIADO**, não sistema implementado, testado, homologado ou fiscalmente apto.
### Fontes de verdade e links de trabalho
- [03.01.03 Módulos — árvore oficial e ownership](https://app.notion.com/p/80101fd65a9c833580f1012ec0a45407).
- [04.01 UI-MASTER-001 — contrato global de Shell/UX e tokens](https://app.notion.com/p/97401fd65a9c822a9a4b017c39a08518). **A sidebar permanece expandida/recolhida, sem barra compacta.**
- [05.01 Identidade, Autenticação e Sessões — AUTH-001/002/003](https://app.notion.com/p/6b001fd65a9c8368bb1c01e95d984771).
- [05.02 Tenant, Empresa e Entitlements — exceção NERITECH AUTO sem Filiais](https://app.notion.com/p/98e01fd65a9c8380b61001b3ec1639bc).
- [03.01.08 Permissões e matriz RBAC da empresa](https://app.notion.com/p/f7001fd65a9c82e7bb128152eabf7d9d).
- [05.03 Auditoria, Idempotência e Outbox](https://app.notion.com/p/27701fd65a9c8214abb581b6553bdcbf).
- [05.04 Contrato de APIs, Integrações e Webhooks](https://app.notion.com/p/8ec01fd65a9c82cf90d8016690aeb5ef).
- [08.02 CI/CD, Flyway, homologação e rollback](https://app.notion.com/p/41b01fd65a9c82c0b7db016e5d93ec5d).
- [10.01 Modelo SaaS/Planos/Trial com preço configurável](https://app.notion.com/p/2d801fd65a9c83bbb0b2810532611d26).
- [11.02 Landing page pública, fora do menu do produto](https://app.notion.com/p/00401fd65a9c8232a068019c029dbfaa).
- [03.01.04.01 Inventário e Auditoria GitHub × Notion — 64 ScreenIds e lacunas reais](https://app.notion.com/p/3ee01fd65a9c81c9bb3dc13d9906d366).
- [03.01.16.06 Controle D4 → Código → D5 — auditoria editorial das 64 telas (14/10/2026)](https://app.notion.com/p/58d01fd65a9c82f48e1a0114ea246add).
- [Gatilhos técnicos e sequência Work](https://github.com/neritechauto-8548/neritechauto/blob/feature/develop/Documentacao/WORK_SEQUENCIA_TELAS.md).
- [Instruções e limites do Codex na raiz do repositório](https://github.com/neritechauto-8548/neritechauto/blob/feature/develop/AGENTS.md).
### Catálogo fechado (64 ScreenIds; tela interna de Pátio incluída)
- **Início:** HOME-001 (1) → [módulo Início](https://app.notion.com/p/3eb01fd65a9c81c58f58d17285b3ceb4).
- **Clientes e CRM:** CLI-001/002/003/005/006 (5) → [módulo Clientes](https://app.notion.com/p/3eb01fd65a9c81028be6c47baa4f2b69). CLI-004 legado desativado, jamais recriar.
- **Oficina:** OFC-001 a OFC-012 (12) → [módulo Oficina](https://app.notion.com/p/3ee01fd65a9c81b7809ce10c592a931f).
- **Estoque & Compras:** EST-001 a EST-007 (7) → [módulo Estoque & Compras](https://app.notion.com/p/3ee01fd65a9c8127a777e26ce12bc880).
- **Financeiro:** FIN-001 a FIN-007 (7) → [módulo Financeiro](https://app.notion.com/p/3ee01fd65a9c81f1bb5bd8448400040c).
- **Fiscal:** FIS-001 a FIS-008 (8) → [módulo Fiscal](https://app.notion.com/p/3ee01fd65a9c8152bcd3f70a2fccd2c0).
- **IA:** IA-001/002 (2) → [módulo Inteligência Artificial](https://app.notion.com/p/3ee01fd65a9c8105803cc82dd6be25a).
- **Gráficos Gerenciais:** GRA-001 (1) → [módulo Gráficos](https://app.notion.com/p/3ee01fd65a9c8145896dfaad74f0b3c9).
- **Relatórios:** REL-001 a REL-010 (10) → [módulo Relatórios](https://app.notion.com/p/3ee01fd65a9c812ab49ac3f444e79eed).
- **Administração:** ADM-001 e ADM-003 a ADM-011 (10) → [módulo Administração](https://app.notion.com/p/3ee01fd65a9c811b8f28e251b6708b73). ADM-002 Filiais foi excluída; **não** recriar em menu, DTO, schema ou docs.
- **Gestão de Pátio:** PATIO-001 (1) → [uma tela interna](https://app.notion.com/p/3ee01fd65a9c8134adf5c8eac59daead), todos os modos na mesma aplicação.
- **00 Global + AUTH-001/002/003** são contratos transversais fora da contagem de 64.
### Decisões vinculantes para execução autônoma
1. **D01–D11 nesta página** são normas de produto/engenharia delegadas pelo responsável; o Codex não precisa pedir confirmação para UI, campos usuais, validações seguras, layouts, limites padrão, status, priorização, padrões de teste, reuso ou arquitetura reversível já definidos.
2. **O texto inicial de cada ScreenId** é a decisão executável atual para aquela tela; se textos antigos da página divergirem, usar esta decisão, preservar evidência de benchmark somente como fonte e reconciliar DTOs com o repositório antes de codificar.
3. Criar camada nova somente se não houver implementação adequada; a preferência é corrigir rota/componente/serviço existente e manter migrações compatíveis. A referência UltraCar informa profundidade funcional, **não define UX nem hierarquia de menu**.
4. Em falta de API/fornecedor fiscal/bancário/WhatsApp/IA, implementar interface, domínio, contratos, validações locais, adaptador e erros honestos; **não marcar "operacional" um envio, emissão, assinatura, boleto ou pagamento fictício**. Integrações dependem de credenciais/contratos legais reais e são **bloqueio de homologação externa**, não impedimento do restante do desenvolvimento.
5. **Ações irreversíveis ou onerosas** e implantação em produção não são autorizadas por esta decisão geral. Sem provedor/credenciais, não pedir decisão para todas as telas: registrar dependência e continuar o próximo ScreenId seguro.
6. O usuário delegou decisões de produto; quando houver várias escolhas tecnicamente válidas, usar a solução **mais simples, segura, extensível e compatível com o banco** e registrar a escolha na página canônica. Somente riscos legais/financeiros irreversíveis ou que exigem dados externos levam a pendência.
### Pipeline sugerido ao Codex sem gastar tokens desnecessários
`D00 Segurança multiempresa e Auth` → `D01 Shell/menu/rotas/permissões` → `D02 Clientes/CRM` → `D03 Veículos/Agenda/Serviços` → `D04 Orçamentos/OS/Inspeções` → `D05 Gestão de Pátio/Entregas/PDV` → `D06 Estoque/Compras` → `D07 Financeiro` → `D08 Fiscal` → `D09 Gráficos/Relatórios` → `D10 IA` → `D11 Homologação por perfil/empresa/dispositivo`.
Cada entrega: ler `AGENTS.md` → somente ScreenId e owners dependentes → diffs relevantes do repo → aplicar contrato → implementar Angular+Java+Flyway → testes focados + build/CI do lote → atualizar mesma página → informar commit/testes/pendências. Não carregar os 64 documentos completos em um prompt.
### Pendências factuais e limite de conclusão
- O inventário GitHub é uma fotografia da `feature/develop` de 14/10/2026; rotas, controllers e schema podem mudar. **Conferir HEAD antes de cada implementação**. Contratos propostos não provam endpoint existente.
- QA real de login/empresa A/B, 403 por recurso, E2E de cliente→OS→estoque→financeiro→fiscal, UX em dispositivos, integridade das migrações e provedores externos ainda precisam ser executados e evidenciados.
- Para ler diretamente estas páginas, o **ambiente Codex deve ter integração/acesso ao Notion**. Se o ambiente não tiver, configurar acesso ou disponibilizar uma exportação versionada. Links no `AGENTS.md` não dão, por si sós, acesso ao conteúdo privado. Nunca inventar o documento ausente.
**Veredito documental:** contratos dos 64 ScreenIds + Global + Auth foram preparados para orientar o Codex com decisões de produto suficientes para desenvolvimento incremental. **Veredito de software: NÃO HOMOLOGADO**; integração externa e QA real permanecem evidências a produzir, não dúvidas gerais de UX.
# B — PADRÕES OPERACIONAIS DE TELAS E UX — NERITECH AUTO
## Objetivo
Centralizar a especificação das telas do NERITECH AUTO.
Toda tela deve ser suficientemente detalhada para Produto, UX/UI, Arquitetura, Desenvolvimento e QA trabalharem sem depender de interpretação.
## Fonte visual
**UI-MASTER-001** é o Design System e Application Shell do produto.
### Direção oficial de design
A experiência da NERITECH AUTO segue a filosofia **Stripe-first**: simplicidade, precisão, hierarquia e consistência visual. A profundidade operacional é complementada por padrões de Shopmonkey, a cobertura funcional por UltraCar e o contexto automotivo brasileiro por OnMotor.
**Princípio:** complexidade no sistema, simplicidade para o usuário.
Nenhuma tela deve parecer um ERP tradicional apenas porque possui muitos dados. A especificação deve controlar densidade, hierarquia, agrupamento, progressive disclosure, ações primárias e estados para manter a experiência clara.
Antes de criar ou aprofundar telas, consultar [**04.08 — Auditoria de Consistência UX & Design**](https://app.notion.com/p/3ee01fd65a9c814cb841c64433129e45). Enquanto a auditoria e a reconciliação do manifesto não forem concluídas, não tratar o antigo manifesto de 187 experiências ou os antigos dashboards Home como arquitetura vigente.
## Padrão de tela
Além do contrato funcional, cada tela deve demonstrar uma decisão explícita de experiência: **qual é a tarefa principal, qual informação é prioritária, qual ação é primária e qual complexidade pode permanecer contextual/avançada**.
Cada tela deve documentar:
1. Identificação
2. Objetivo
3. Estrutura UX/UI
4. Campos
5. Tabelas e colunas
6. Componentes
7. Ícones
8. Estados
9. Regras de negócio
10. Fluxos
11. Permissões
12. APIs
13. Dados
14. Integrações
15. Critérios de aceite
16. Especificação para desenvolvimento
## Regras de UX
- UX não cria módulos.
- UX não move módulos.
- UX não altera ownership.
- Ações críticas utilizam as APIs do módulo proprietário.
- Estados de loading, vazio, erro, parcial, stale e sem permissão são distintos.
- Nenhuma informação crítica depende somente de cor ou hover.
- WCAG 2.2 AA.
- Desktop, tablet e mobile fazem parte da especificação.
- Toda informação acionável possui destino claro.
## Tabelas
Cada tabela deve definir finalidade, colunas, ordem, tipos, formatação, filtros, busca, ordenação, paginação, ações, estados e comportamento responsivo.
## Formulários
Cada campo deve definir label, tipo, obrigatoriedade, placeholder, máscara, validação, padrão, opções, regras, dependências, comportamento, erro e permissão.
## Desenvolvimento
Toda tela deve terminar com componentes, APIs, entidades, regras, permissões, integrações, eventos, testes, dependências e critérios de aceite.
## Procedimento obrigatório para telas sem especificação ou com documentação incompleta
Nem todas as telas têm documentação concluída. **O Work deve documentar a tela antes de desenvolver o código**, sem exigir que o usuário escreva a especificação técnica.
1. **Localizar e conferir:** consultar o módulo e a tela dentro de Telas & UX, UI-MASTER-001 e a arquitetura oficial 03.01.03. Verificar código e API existentes antes de afirmar campos, rotas ou integrações.
2. **Criar ou completar:** se não houver página da tela, criar dentro do módulo correspondente; se houver página incompleta, enriquecer a existente, sem duplicar. Seguir os 16 itens do Padrão de tela: objetivo, UX/UI, campos com obrigatoriedade e validações, tabelas e colunas, filtros, estados, fluxos, regras, permissões, APIs/DTOs, dados, integrações, componentes, acessibilidade, critérios de aceite e especificação para desenvolvimento. Marcar o que está implementado/verificado e o que é proposta.
3. **Exemplos do usuário:** o usuário pode anexar um print, referência visual, wireframe, protótipo/Figma, vídeo ou uma lista de campos/colunas. O Work deve transformar o exemplo em especificação profissional, avaliar relevância, validar as regras com o domínio e aplicar o design NERITECH AUTO. Um exemplo não substitui os contratos de segurança ou as regras já oficiais.
4. **Na ausência de exemplos:** propor UX baseada em Stripe (experiência), Shopmonkey (fluxos), UltraCar (funcionalidades) e OnMotor (contexto brasileiro), sem copiar identidade de terceiros nem inventar requisitos como se estivessem aprovados. Identificar suposições e perguntar apenas diante de decisões de negócio críticas ou riscos de dados, arquitetura, fiscal e financeiro.
5. **Ordem obrigatória:** publicar a especificação canônica **primeiro**; depois implementar frontend, backend e migrações Flyway versionadas quando necessárias; compilar, executar testes, validar UX e registrar evidências. Não declarar produção homologada sem testes concluídos. Se não for possível escrever no Notion, registrar a pendência, manter especificação versionada no repositório e não afirmar que a página foi publicada.
A ausência de documentação nunca autoriza criar novo módulo ou alterar o menu. Os nomes de tabelas/colunas novos devem seguir o padrão em português e snake_case, com migração segura dos dados existentes.
**Exemplo opcional fornecido pelo usuário:** “Agenda da Oficina: quero esta aparência e os campos Data, Hora, Cliente, Veículo, Serviço, Mecânico, Status e Observações.” O Work deve elaborar regras, validações, conflitos, permissões, responsividade, fluxos e APIs aplicáveis antes de desenvolver.
# Padrão transversal de telas
### Anatomia
- Application Shell;
- header;
- navegação;
- breadcrumb quando aplicável;
- conteúdo principal;
- ações;
- filtros;
- tabelas/cards;
- feedback de estado.
### Interação
- listas preservam filtros ao abrir detalhes quando aplicável;
- ações de criação e edição possuem validação;
- operações longas utilizam página ou stepper;
- modais são utilizados para ações curtas e contextuais;
- ações destrutivas exigem confirmação conforme risco;
- conflitos de concorrência são tratados explicitamente;
- mutations possuem processamento e feedback seguro.
### Dados e segurança
- cada tela possui módulo owner;
- capabilities são validadas pelo backend;
- PII e informações sensíveis respeitam permissões;
- APIs e read models pertencem ao domínio responsável;
- auditoria é definida para ações críticas.
### Responsividade
- Desktop: grid de 12 colunas;
- Tablet: grid de 8 colunas;
- Mobile: grid de 4 colunas;
- nenhuma função essencial depende de hover;
- conteúdo essencial não exige scroll horizontal.
### Acessibilidade
- WCAG 2.2 AA;
- teclado;
- foco visível;
- contraste;
- labels;
- mensagens de erro associadas aos campos;
- alternativa textual para informações visuais;
- suporte a zoom;
- reduced motion.
### Gate de implementação
Uma tela está pronta para desenvolvimento somente quando sua especificação contém objetivo, UX/UI, campos, tabelas, componentes, estados, regras, fluxos, permissões, APIs, dados, integrações e critérios de aceite testáveis. O registro canônico de tokens e shell fica em [04.01 — UI-MASTER-001](https://app.notion.com/p/97401fd65a9c822a9a4b017c39a08518); o MASTER DESIGN é uma representação subordinada para ferramentas, não uma fonte paralela.
### Checklist de aceite UX/UI obrigatório por tela
- [ ] ScreenId e módulo owner conferidos no catálogo vigente; nenhuma referência baseada no manifesto legado.
- [ ] Objetivo, persona, contexto de entrada, ação primária, ações secundárias e destino de saída/drill-down definidos.
- [ ] Campos: tipo, label persistente, formato pt-BR, obrigatoriedade, máscara, validação, dependências, permissão, erro e recuperação.
- [ ] Listas: colunas e ordem, busca, filtros, ordenação, paginação, persistência dos filtros ao voltar, ações por linha e distinção entre vazio inicial e vazio filtrado.
- [ ] Estados aplicáveis documentados: carregando, vazio, erro, parcial, desatualizado, sem permissão, offline e conflito.
- [ ] Tokens e componentes existentes reutilizados; nenhuma cor, fonte, raio, espaçamento ou biblioteca de ícones ad hoc.
- [ ] Desktop, tablet e mobile especificados; tarefa essencial concluível sem hover, arrastar-e-soltar ou scroll horizontal crítico.
- [ ] Acessibilidade: teclado, foco visível e não encoberto, labels e erros associados, contraste verificado, zoom 200%, reduced motion e alternativa textual para gráficos.
- [ ] Segurança: permissões verificadas no backend; dados sensíveis não são enviados ao cliente sem autorização.
- [ ] Critérios de aceite verificáveis por QA e cenários de erro/recuperação incluídos.
- [ ] Evidência visual avaliada por viewport e estado; mockup isolado não comprova implementação pronta.
# 📌 Governança UX por módulo
A arquitetura congelada em **03.01.03 🧩 Módulos** é pré-requisito de toda tela. A especificação visual deve aprofundar a experiência dentro do módulo owner, nunca criar um novo domínio para acomodar uma necessidade visual.
## Contrato de cada tela
Toda tela deve declarar: screenId, módulo owner, rota, objetivo, persona, pré-condições, dados exibidos, ações permitidas, estados, permissões, regras de negócio, dependências, integrações, auditoria, analytics, critérios de aceite e destino de drill-down.
## Regras transversais
- UI-MASTER-001 é a fonte visual canônica.
- Ações devem respeitar o capability retornado pelo backend.
- Loading, vazio, erro, sem permissão, parcial e conflito são estados distintos.
- Nenhuma informação crítica pode depender somente de cor ou hover.
- Home consulta; módulos proprietários executam.
- Histórico, Gráficos e Relatórios não devem duplicar operações.
- IA não pode criar uma experiência paralela que contorne o módulo owner.
- Vendas continua em Oficina.
- Gestão de Pátio é uma tela interna única do NERITECH AUTO; não é sistema ou produto separado.
## Gate UX → Desenvolvimento
Uma tela só avança quando a especificação está coerente com a arquitetura, possui estado completo, contrato de dados, permissões, destino das ações e critérios de aceite. Mockup bonito sem contrato funcional não é considerado pronto para implementação.
[🏠 Início — Módulo / Especificação Completa](https://app.notion.com/p/3eb01fd65a9c81c58f58d17285b3ceb4)
# 🧭 Catálogo oficial de telas por módulo
Esta página é o índice canônico de **Telas & UX** do NERITECH AUTO. A organização segue exclusivamente a arquitetura definida em **03.01.03 🧩 Módulos**.
## Estrutura documental
**NERITECH AUTO → Telas & UX → Módulo → Tela → Especificação completa da tela**
Cada módulo terá uma página própria dentro de **Telas & UX**. As telas daquele módulo serão páginas filhas dessa página, sempre no padrão definido neste documento.
## Acessos e módulos oficiais
- 🔧 Gestão de Pátio — tela interna única (não módulo separado)
- 🏠 Início
- 👥 Clientes
- 🚗 Oficina
- 📦 Estoque & Compras
- 💰 Financeiro
- 🧾 Fiscal
- 🤖 Inteligência Artificial
- 📊 Gráficos Gerenciais
- 📊 Relatórios
- ⚙️ Administração
## Módulos já estruturados
### 🏠 Início
🏠 Início — Módulo / Especificação Completa
**Tela:** TELA-AUTO-HOME-001 — Início / Dashboard
**Visões internas:** Padrão · Gerencial · Financeiro · Orçamento
### 👥 Clientes
👥 Clientes — Módulo / Especificação Completa
**Clientes (três telas canônicas e formulário reutilizado):**
- TELA-AUTO-CLI-001 — Clientes / Lista — `/cliente/listar`
- TELA-AUTO-CLI-002 — Clientes / Cadastro — `/cliente/cadastro`
- TELA-AUTO-CLI-003 — Clientes / Editar / Visualizar — `/cliente/editar/:uuid`
A consulta completa e a edição compartilham a tela CLI-003; não existe rota independente de Detalhes nem ScreenId CLI-004 vigente.
**CRM (dentro de Clientes):**
- TELA-AUTO-CLI-005 — CRM / Comunicação — `/cliente/crm/comunicacao` planejada; rota ainda não registrada no Angular.
- TELA-AUTO-CLI-006 — CRM / Questionamento de pesquisa — `/cliente/crm/pesquisa` registrada no Angular; implementação funcional deve ser homologada.
## Regra de organização
Uma visão interna não vira módulo e não cria uma árvore paralela. No caso de **Início**, as quatro visões permanecem documentadas dentro do mesmo módulo, embora tecnicamente compartilhem a rota /home.
As páginas de **Telas & UX** especificam a experiência. A arquitetura, ownership e menu continuam sendo definidos exclusivamente em **03.01.03 🧩 Módulos**.
[Clientes — Módulo / Especificação Completa](https://app.notion.com/p/3eb01fd65a9c81028be6c47baa4f2b69)
[Oficina — Módulo / Especificação Completa](https://app.notion.com/p/3ee01fd65a9c81b7809ce10c592a931f)
## 🚗 Oficina — contratos canônicos (12 telas)
<mention-page url="https://app.notion.com/p/3ee01fd65a9c81b7809ce10c592a931f">Oficina — Módulo / Especificação Completa</mention-page>
Telas detalhadas:
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81e28cebf78e2846de5c"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81e282a7d0a0877cb060"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81b5aff5d994e4ff5368"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81daa32ddae79d1987b3"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81a08229c975f59736c2"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81159e2ad993f27dbd54"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81a3803ef8994a3a7c61"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81a19de8e5ff1af63e21"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81a880f6cc10b859b759"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81989eb2d03f70c13130"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81dc8c6ad68244433747"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81d1ac06ecd521c95b35"/>
A arquitetura permanece congelada. Esta documentação descreve apenas telas e comportamentos do módulo Oficina; não cria módulos paralelos nem altera ownership.
[Estoque & Compras — Módulo / Especificação Completa](https://app.notion.com/p/3ee01fd65a9c8127a777e26ce12bc880)
## 📦 Estoque & Compras — contratos canônicos (7 telas)
<mention-page url="https://app.notion.com/p/3ee01fd65a9c8127a777e26ce12bc880">Estoque & Compras — Módulo / Especificação Completa</mention-page>
Telas detalhadas:
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81a9aa1ac434991cf066"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81d7a162c3d101c9a69d"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81818504e49b0c32e365"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81ed93b0db7c95783947"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c818baa04f32af859635b"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c817b9c98ece089d11d36"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c813ea2d4eb4cb270e9e8"/>
A arquitetura oficial continua congelada. Os endpoints descritos são contratos conceituais para alinhamento com o backend existente, não afirmações de APIs já implementadas.
[Financeiro — Módulo / Especificação Completa](https://app.notion.com/p/3ee01fd65a9c81f1bb5bd8448400040c)
## 💰 Financeiro — contratos canônicos (7 telas)
<mention-page url="https://app.notion.com/p/3ee01fd65a9c81f1bb5bd8448400040c">Financeiro — Módulo / Especificação Completa</mention-page>
Telas detalhadas:
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c8177b818fe717d65ea77"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c811fb6a3ec5750a14f8e"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c8169bfa8f2216e09231b"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81efa5f2ee5f19cfd2e3"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81febb1ef04efbd2df80"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81d18ef4f863267d9307"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c8170a58dead6f33ab610"/>
Cada tela inclui tabelas e colunas, filtros, formulários, ações, validações, estados, permissões, APIs propostas e critérios de aceite.
[Fiscal — Módulo / Especificação Completa](https://app.notion.com/p/3ee01fd65a9c8152bcd3f70a2fccd2c0)
## 🧾 Fiscal — contratos canônicos (8 telas)
<mention-page url="https://app.notion.com/p/3ee01fd65a9c8152bcd3f70a2fccd2c0">Fiscal — Módulo / Especificação Completa</mention-page>
Telas detalhadas:
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c818d944acf9fd676c334"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81d79c9df04a044cfef1"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c812ba066e64d7d27d3f3"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81c6afa4e814fbe53814"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81aab52bd44904f54f41"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81e48f02f1694a318871"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81488808c20a5c7bcfa2"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c8124827af424335d31b6"/>
[Inteligência Artificial — Módulo / Especificação Completa](https://app.notion.com/p/3ee01fd65a9c8105b803cc82dd6be25a)
[Gráficos Gerenciais — Módulo / Especificação Completa](https://app.notion.com/p/3ee01fd65a9c8145896dfaad74f0b3c9)
[Relatórios — Módulo / Especificação Completa](https://app.notion.com/p/3ee01fd65a9c812ab49ac3f444e79eed)
[Administração — Módulo / Especificação Completa](https://app.notion.com/p/3ee01fd65a9c811b8f28e251b6708b73)
## 🤖 Inteligência Artificial — contratos canônicos (2 telas)
<mention-page url="https://app.notion.com/p/3ee01fd65a9c8105b803cc82dd6be25a">Inteligência Artificial — Módulo / Especificação Completa</mention-page>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c815b9b63e3527bcba3f3"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c814291e9dfef5ca13c0e"/>
## 📊 Gráficos Gerenciais — contrato canônico (1 tela)
<mention-page url="https://app.notion.com/p/3ee01fd65a9c8145896dfaad74f0b3c9">Gráficos Gerenciais — Módulo / Especificação Completa</mention-page>
## 📈 Relatórios — contratos canônicos (10 telas)
<mention-page url="https://app.notion.com/p/3ee01fd65a9c812ab49ac3f444e79eed">Relatórios — Módulo / Especificação Completa</mention-page>
Telas por categoria:
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81039be9e5e15901b95e"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81c9907fd5ea488c83cf"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81ea8c89e6aa5c09be00"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81aea4b2ce23486814b1"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c817d8653fc2790204d0b"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c814eb37af8211904ac47"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c8189b162fa8cf64311b0"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81e59c13c8168e1b0225"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81d1b91ac4d6a271c297"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81459e2dd3271079eccd"/>
## ⚙️ Administração — contratos canônicos (10 telas, sem Filiais)
<mention-page url="https://app.notion.com/p/3ee01fd65a9c811b8f28e251b6708b73">Administração — Módulo / Especificação Completa</mention-page>
Telas:
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c8196859fd73ec54f7413"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81dbab46c713c839a102"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c815c8462e622867daf36"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c8148ad97d5bc3140044e"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c8197a7a8dcfab4654ea0"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81cb92f4cffbf401adcc"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c814f94eeeed0cfa129a8"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c8165890ec4c80fba7014"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c81e88207ca3ffe79cd3d"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c810b80afe8156704c8b5"/>
- <mention-page url="https://app.notion.com/p/3ee01fd65a9c813783cbcbac38c6e8cb"/>
A arquitetura oficial de 03.01.03 permanece inalterada. Todas as páginas são especificações de UX e desenvolvimento; contratos de API e entidades devem ser validados contra o código existente antes da implementação.
[Gestão de Pátio — Tela / Especificação Completa](https://app.notion.com/p/3ee01fd65a9c8134adf5c8eac59daead)
## 🔧 Gestão de Pátio — tela interna
<mention-page url="https://app.notion.com/p/3ee01fd65a9c8134adf5c8eac59daead">Gestão de Pátio — Tela / Especificação Completa</mention-page>
Gestão de Pátio é uma tela interna única do NERITECH AUTO, acessível pelo menu principal na mesma aplicação. Kanban, detalhes, boxes, cronômetros, painel TV e alertas são áreas funcionais internas, sem seis telas/rotas independentes. Clientes, veículos e OS continuam pertencendo aos módulos proprietários.
[03.01.04.01 — Inventário Canônico e Auditoria Tela por Tela](https://app.notion.com/p/3ee01fd65a9c81c9bb3dc13d9906d366)
## 🔎 Inventário canônico e auditoria tela por tela
O inventário vigente, com as 64 especificações identificadas, lacunas por tela, regra de Início, lacuna de Gráficos Gerenciais e gates de liberação está em [03.01.04.01 — Inventário Canônico e Auditoria Tela por Tela](https://app.notion.com/p/3ee01fd65a9c81c9bb3dc13d9906d366). O status permanece parcial até concluir revisão semântica, validação visual e conferência com o repositório.
## 📊 Gráficos Gerenciais — tela canônica
O módulo possui agora uma especificação de tela única: [TELA-AUTO-GRA-001 — Gráficos Gerenciais / Visão geral](https://app.notion.com/p/3ee01fd65a9c81248876f5e4b689b55b). As visões analíticas são internas à mesma superfície e não criam itens de menu. A especificação está sujeita à revisão semântica e validação contra o repositório.
## C — RASTREABILIDADE DO VÍDEO ULTRACAR (EVIDÊNCIA, NÃO NORMA DE UX)
**As evidências de benchmark não substituem o contrato A das telas nem o UI-MASTER-001.**
### Rastreabilidade funcional — vídeo UltraCar / especificações NERITECH
**Fonte audiovisual:** [Vídeo UltraCar (Vimeo)](https://vimeo.com/1137772386/f2badbe941?fl=pl&fe=cm), arquivo enviado pelo usuário, duração 46min02s. **Método:** mapeamento de amostras visuais de 20 em 20 segundos e revisão ampliada de quadros-chave. Marcações de tempo aproximadas, não transcrição integral do áudio. **O** = campo/tela/ação visível; **P** = melhoria proposta; **V** = pendente de validar em regra de negócio/GitHub. Os valores e dados pessoais mostrados no vídeo nunca devem ser copiados para ambiente de testes.
### Relacionamento entre trecho e proprietário canônico
- **02:20–03:20, Indicadores e orçamento por situação:** <mention-page url="https://app.notion.com/p/3eb01fd65a9c81f9bf76d9c525335911"/> — mantém uma tela Início com quatro visões e sem ações rápidas.
- **03:20–04:50, Peças, cadastro, aplicação, saldo e substitutas:** <mention-page url="https://app.notion.com/p/3ee01fd65a9c81d7a162c3d101c9a69d"/> e <mention-page url="https://app.notion.com/p/3ee01fd65a9c81a9aa1ac434991cf066"/>.
- **05:00–07:00, Catálogo de Serviços, preços e áreas:** <mention-page url="https://app.notion.com/p/3ee01fd65a9c81a19de8e5ff1af63e21"/>.
- **07:20–08:20, Calendário, pré-visualização e indicadores de Orçamentos (não Agenda):** <mention-page url="https://app.notion.com/p/3ee01fd65a9c81e282a7d0a0877cb060"/>. As solicitações de agendamento aparecem separadamente no Pátio (32:40), mantendo Agenda como owner.
- **08:20–11:20, Novo orçamento e cadastros rápidos de cliente/veículo:** <mention-page url="https://app.notion.com/p/3ee01fd65a9c81b5aff5d994e4ff5368"/>, <mention-page url="https://app.notion.com/p/3eb01fd65a9c81eb843bd7cf83b85896"/> e <mention-page url="https://app.notion.com/p/3ee01fd65a9c81a880f6cc10b859b759"/>.
- **12:00–15:20, Vistoria, fotos, danos e checklist rápido:** <mention-page url="https://app.notion.com/p/3ee01fd65a9c81a3803ef8994a3a7c61"/> e <mention-page url="https://app.notion.com/p/3ee01fd65a9c81a08229c975f59736c2"/>.
- **15:40–20:40, Peças e serviços da OS, orçamento, desconto, autorização:** <mention-page url="https://app.notion.com/p/3ee01fd65a9c81a08229c975f59736c2"/>; catálogos permanecem em seus próprios owners.
- **20:40–24:00, Financeiro da OS, parcelas, documento fiscal e comunicação:** <mention-page url="https://app.notion.com/p/3ee01fd65a9c8177b818fe717d65ea77"/>, <mention-page url="https://app.notion.com/p/3ee01fd65a9c818d944acf9fd676c334"/> e <mention-page url="https://app.notion.com/p/3eb01fd65a9c81689815eef6a0e269b2"/>.
- **24:00–25:00, Condições de pagamento e catálogos financeiros:** <mention-page url="https://app.notion.com/p/3ee01fd65a9c8177b818fe717d65ea77"/>.
- **25:40–30:40, Relatórios financeiros, OFX, conciliação e DRE:** <mention-page url="https://app.notion.com/p/3ee01fd65a9c814eb37af8211904ac47"/>, <mention-page url="https://app.notion.com/p/3ee01fd65a9c81d18ef4f863267d9307"/> e <mention-page url="https://app.notion.com/p/3ee01fd65a9c81459e2dd3271079eccd"/>.
- **31:00–31:20, Relatórios fiscais e exportação:** <mention-page url="https://app.notion.com/p/3ee01fd65a9c81d1b91ac4d6a271c297"/>.
- **31:20–33:00, Pátio, Kanban, responsáveis e solicitação de agenda:** <mention-page url="https://app.notion.com/p/3ee01fd65a9c8134adf5c8eac59daead">Gestão de Pátio — Tela / Especificação Completa</mention-page> e Agenda canônica.
- **33:40–35:40, Conta a pagar e movimentações de caixa/banco:** <mention-page url="https://app.notion.com/p/3ee01fd65a9c8169bfa8f2216e09231b"/>, <mention-page url="https://app.notion.com/p/3ee01fd65a9c81febb1ef04efbd2df80"/> e Movimento Bancário.
- **36:20–39:40, Revisita de OS, negociação, financeiro e fluxo de caixa:** páginas já indicadas de Oficina / Financeiro / Relatórios; não criar páginas redundantes.
- **40:00–46:02, Planos comerciais UltraCar:** não é evidência de fluxo transacional, campo ou coluna; não migrar planos/preços para a arquitetura de telas NERITECH.
### Gate de continuidade para Codex
1. Abrir 03.01.03 Módulos e UI-MASTER-001 antes de implementar. A sidebar existente permanece expandível/recolhível; sem barra lateral compacta e sem menu novo.
2. Conferir se o campo/coluna já existe no frontend e no DTO/backend na branch `feature/develop`; manter rota real e formulário compartilhado; não duplicar cadastro/tabela.
3. Distinguir O de P e V. **Campo visível no vídeo não equivale a regra legal ou API existente.** Não aplicar regras tributárias, financeiras ou fórmulas de indicadores apenas por aparência de tela.
4. Especificar e testar modelo, máscaras, validações, tabela, filtros, ordenação, paginação, estados, permissões no servidor, idempotência, auditoria, responsividade e WCAG 2.2 AA.
5. Validar transição OS→estoque→Financeiro→Fiscal por APIs dos owners e execução sem duplicidade; registrar evidências reais de build, testes e UX antes de declarar homologação.
### Limites do levantamento
A cobertura de tempo do vídeo foi feita por amostragem visual e aprofundamento de capturas-chave, **não por leitura quadro a quadro de cada segundo nem transcrição da narração**. Campos pouco legíveis ou telas mostradas muito rapidamente exigem nova inspeção antes de serem chamados de 100% extraídos. Este índice permite continuar sem confundir essa limitação com implementação já pronta.
## Referência histórica — auditoria de cobertura do vídeo (não substitui contratos atuais)
**Atualizado após segunda revisão do vídeo em 14/10/2026.** A análise anterior amostrou quadros a cada 20 s; esta revisão aprofundou quadros específicos e confirmou campos/menus que estavam ausentes, inclusive o formulário completo de Serviços, comissões por item de OS, WhatsApp com templates, parcelas financeiras, previsões de contas a pagar, calendário de contas a receber, compras/cotações, gráficos e controle de tempo do Pátio. Isso não equivale a análise de todos os frames nem prova que o NERITECH AUTO já implementa essas funcionalidades.
### Escala de confiança por cobertura do vídeo
- **Visível e detalhado:** campo/tabela/ação legível em quadro ampliado, com documentação adicional na página do owner.
- **Parcial:** somente uma aba, botão, relatório ou resumo apareceu; não deduzir formulário completo nem regra de negócio a partir disso.
- **Não demonstrado:** o vídeo não oferece evidências suficientes; requer outra fonte, código real, documentação de domínio ou decisão de produto.
### Inventário dos módulos canônicos
- **Gestão de Pátio — visível e detalhado em parte:** Kanban, cartões, responsáveis, detalhe de serviços, botões de cronômetro, checklist express, solicitações de agendamento e alternância Lista/Calendário. **Parcial:** recursos/boxes, painel TV, alertas, regras reais de tempo e concorrência.
- **Início — parcial:** indicadores operacionais e financeiros da referência; cálculos exatos, fórmulas, permissionamento e quatro visões são definidos pelo produto NERITECH, não pelo vídeo.
- **Clientes — parcial:** cadastro PF e seu uso contextual no orçamento; CRM/Comunicação contém modal WhatsApp com templates. **Não demonstrado:** fluxo completo PJ, edição, listagem avançada, questionários de pesquisa, opt-outs e automações.
- **Oficina / Agenda — parcial:** solicitações no Pátio (Lista/Calendário), mas o calendário de 07:20 é de Orçamentos, e não uma prova de agenda operacional. Necessário validar formulários de compromisso, conflitos, recorrência e notificações.
- **Oficina / Orçamentos — visível e detalhado em parte:** criação, cliente, veículo, itens, indicadores, calendário, prévia, complementos e autorização. **Parcial:** importação, orçamentos migrados, histórico completo e fluxo de revisão.
- **Oficina / Ordens de Serviço — visível e detalhado em parte:** itens, descontos, negociações, parcelas, relatórios, comunicações, comissões, nota fiscal, checklist, fechar/autorizar e complementos. **Parcial:** transições completas, QR code, assinatura efetiva, devoluções, estornos e integração de pagamentos.
- **Oficina / Diagnósticos — não demonstrado diretamente.** A opção de incluir peça em diagnóstico não comprova tela/fluxo de Diagnósticos.
- **Oficina / Inspeções — parcial:** checklist rápido de entrada, danos/fotos e anexos; os prints externos mostram editor de modelos e estados, mas não sua integração real com a referência UltraCar. **Pendente:** modelo completo, versionamento, resultados, assinatura e uso móvel.
- **Oficina / Serviços — visível e detalhado:** lista e formulário com identificação, área, tempo, unidade, KM manutenção, vida útil, preço e custo fixo; aba Aplicações não examinada integralmente.
- **Oficina / Veículos — visível e detalhado em parte:** busca, associação a cliente, dados gerais e técnicos; **parcial:** documento, laudos, transferência de titularidade e histórico avançado.
- **Oficina / Entregas — parcial:** datas de previsão/saída/entrega aparecem na OS, mas não foi demonstrada uma tela autônoma de Entregas.
- **Oficina / Vendas diretas — não demonstrado.** Não inferir caixa de autopeças a partir de itens da OS.
- **Oficina / Histórico — parcial:** históricos e relatórios contextuais aparecem, mas não a tela dedicada.
- **Estoque & Compras / Estoque e Produtos — visível e detalhado em parte:** cadastro, saldo por empresa, localização, aplicações, substitutas, histórico de compra, etiquetagem e preço. **Parcial:** movimento físico, custo, custo médio, inventário e reserva efetiva.
- **Estoque & Compras / Kits — somente item de menu observado; não demonstrado o formulário.**
- **Estoque & Compras / Compras e Cotações — parcialmente visíveis nos relatórios:** tipos de comparação, status e agrupamentos. **Não demonstrado integralmente:** compra, recebimento, pedido ao fornecedor e negociação.
- **Estoque & Compras / Inventário e Movimentações — não demonstrados como telas operacionais.**
- **Financeiro / Contas e condições de pagamento — parcial:** seleção de contas bancárias, plano, centro de custo, condições e documentos; sem administração bancária completa.
- **Financeiro / Contas a Receber — parcial:** calendário, status aberta/liquidada, agrupamento e dados agregados; baixa detalhada e renegociação pendentes.
- **Financeiro / Contas a Pagar — visível e detalhado em parte:** formulário, lista de previsões, calendário/agrupamento e relatórios; autorização e pagamento completos pendentes.
- **Financeiro / Lançamentos Bancários, Movimento de Caixa e Movimento Bancário — parcial:** extrato com saldos/entradas/saídas, filtros, OFX e sugestão de conciliação; confirmar ownership real de cada visualização.
- **Financeiro / Comissões — parcial:** grade de comissões dentro da OS; tela independente de apuração, aprovação e pagamento não demonstrada integralmente.
- **Fiscal — parcial:** seleção de complemento da OS, tipo de documento fiscal e relatórios por status; **não demonstrados como telas completas** NF-e, NFC-e, NFS-e, Manifestação, Inutilização, NCM e SINTEGRA. Não inferir validade jurídica de opções mostradas.
- **Inteligência Artificial / Assistente e Automações IA — não demonstrados.**
- **Gráficos Gerenciais — parcial:** abas Financeiro/Vendas/Faturamento, subvisões de Faturamento e filtros, mas gráfico sem séries visíveis; fórmulas ainda precisam de definição.
- **Relatórios — parcialmente demonstrados:** Financeiro (pagar, receber, balancete, fluxo de caixa, DRE, boletos), Compras (devoluções, cotações, ABC), Fiscal; outros relatórios do módulo canônico não foram vistos.
- **Administração — não demonstrada integralmente:** empresa e fornecedores aparecem como vínculos/seletores, mas não telas completas de Empresa, Usuários, Funcionários, Cargos, Permissões, Áreas, Fornecedores, Seguradoras, Transportadoras e Configurações.
### Decisões de classificação para evitar duplicidade
1. Calendário de **Orçamentos/OS** pertence a Oficina/Orçamentos; calendário de **títulos** pertence ao Financeiro; calendário de **solicitações** pertence à Agenda exibida também dentro da tela única Gestão de Pátio. Visuais semelhantes não mudam os owners.
2. Modal de **WhatsApp da OS** é ação de CRM/Comunicação integrada; não criar subsistema paralelo de modelos/mensagens.
3. Comissões exibidas na OS não criam novo cadastro de colaboradores ou módulo de remuneração fora de Financeiro.
4. **Condições de pagamento e previsões** são modos/dados internos do Financeiro; não acrescentar tags ou grupos artificiais ao menu lateral.
5. A área de preços comerciais entre 40:00 e 46:02 do vídeo demonstra planos da concorrência, não telas funcionais para copiar.
### Gate para declarar documentação completa para Codex
- [ ] Cada campo mostrado foi verificado em quadro legível ou marcado como **não confirmado**; todos os rótulos, tipos, unidades, máscaras, obrigatoriedade e dependências têm fonte.
- [ ] Todas as listas visíveis têm colunas, filtros, ordenação, paginação, ações de linha e estados documentados; nenhuma inferência disfarçada de fato observado.
- [ ] Cada regra de negócio identifica owner, evento disparador, validação, rollback/compensação, permissão, isolamento entre empresas e auditoria.
- [ ] O catálogo de telas do Notion foi reconciliado com rotas/frontend/DTOs e serviços reais na branch `feature/develop`; telas já existentes são reaproveitadas.
- [ ] Integração OS→Inspeção→Estoque→Financeiro→Fiscal→CRM foi validada em modelo de dados e pelo menos em contrato e casos de teste.
- [ ] Todas as telas demonstradas no vídeo possuem registro de evidências, com exemplos editados para eliminar dados pessoais reais.
- [ ] Telas canônicas **não demonstradas** têm especificação independente, obtida de domínio/repositório e decisões de produto; vídeo sozinho não pode documentá-las integralmente.
- [ ] UX segue UI-MASTER-001 atualizado, identidade NERITECH, WCAG 2.2 AA e **sidebar atual expansível/recolhível**, sem replicar design ou menu externo.
**Estado do levantamento:** funcionalmente enriquecido e gravado, porém **NÃO 100% concluído ou homologado**. A próxima revisão deve priorizar telas com evidências parciais ou não demonstradas e contratos reais do código, sem inventar percentual de completude a partir da quantidade de páginas editadas.
## D — PROTOCOLO E NORMAS DE EXECUÇÃO PARA CODEX
### Protocolo de execução Codex — prontidão para desenvolvimento integral
**Status de preparação:** APTO PARA DESENVOLVIMENTO INCREMENTAL, **NÃO APTO PARA PROMESSA DE ENTREGA 100% AUTÔNOMA OU HOMOLOGAÇÃO GLOBAL**. Este protocolo orienta a execução; não cria novos módulos, telas, rotas, componentes ou uma segunda fonte de especificações. É complementar à [matriz auditada das 64 telas](https://app.notion.com/p/3ee01fd65a9c81c9bb3dc13d9906d366) e à sequência `Documentacao/WORK_SEQUENCIA_TELAS.md` na branch `feature/develop`.
### Ordem de autoridade para o Codex
1. **Árvore funcional e ownership:** [03.01.03 Módulos](https://app.notion.com/p/80101fd65a9c833580f1012ec0a45407); preservar a única tela interna Gestão de Pátio e a exclusão definitiva de Filiais.
2. **Visual e componentes:** [04.01 UI-MASTER-001](https://app.notion.com/p/97401fd65a9c822a9a4b017c39a08518), atualizado após as imagens da referência: superfícies claras, contraste, tabelas e formulário premium. **A sidebar atual deve continuar expandível/recolhível**; não substituir por trilho estreito de ícones.
3. **Contrato funcional por ScreenId:** página canônica da tela em 03.01.04 Telas & UX. O [inventário](https://app.notion.com/p/3ee01fd65a9c81c9bb3dc13d9906d366) faz rastreabilidade, **não substitui o contrato detalhado da tela**.
4. **Código e APIs efetivos:** branch obrigatória `feature/develop`, arquivos do frontend/backend, schema do banco, migrations e testes. Em divergência entre API/rota conceitual da documentação e implementação real, **não copiar o endpoint proposto sem verificação**; reconciliar na página canônica e adaptar com compatibilidade.
5. **Governança técnica e economia de tokens:** ler `AGENTS.md` e `Documentacao/WORK_SEQUENCIA_TELAS.md`. O segundo define sequência de implementação, não um segundo catálogo de ScreenIds.
### Matriz GO / COMPLETAR DOCUMENTAÇÃO / BLOQUEAR
- **GO — implementar:** ScreenId confirmado, owner/rotas existentes reconciliados, campos e colunas conhecidos, regras funcionais verificadas ou explicitamente aprovadas, API/DTO mapeados, acesso/permissões definido e casos mínimos de aceite claros. Desenvolver uma unidade funcional completa por vez.
- **COMPLETAR DOCUMENTAÇÃO ANTES:** página existe mas possui apenas nome, lista de campos ilustrativa, vídeo não demonstrou o fluxo, tabela sem colunas, API meramente conceitual ou estado de erro/validação sem definição. Completar **a mesma página canônica**, marcando **O = evidência observada; P = proposta; V = pendente de validação**, e só então codificar decisões verificáveis.
- **BLOQUEAR E SOLICITAR DECISÃO:** alteração estrutural do menu, regras tributárias legais não confirmadas, modelo de cobrança/autorização da OS, movimentação financeira irreversível, migração destrutiva de dados, isolamento de empresas sem segurança demonstrada, integração com provedor sem contrato/credenciais ou ambiguidades capazes de criar perdas ou cobranças indevidas. Não adotar hipótese silenciosamente.
### Protocolo de uma tarefa de implementação
1. Receber **um ScreenId ou fluxo específico**, a branch, a definição de pronto e a evidência atual. Não carregar as 64 páginas inteiras no mesmo contexto.
2. Ler `AGENTS.md`, arquitetura/menu e o **trecho necessário** do UI-MASTER, a página única da tela e, somente se houver dependência, as páginas owners de origem/destino.
3. Localizar com busca direcionada a rota Angular, componente, template, service, controller, service Java, repository, entidade, DTO, migração e testes existentes. Fazer primeiro a decisão **reutilizar/adaptar vs criar**; criar somente o que faltar e não existir em outro owner.
4. Antes de alterar código, registrar brevemente: o que já existe, lacunas, contratos HTTP reais, permissão do frontend **e** backend, isolamento por empresa, migração necessária, dependências, risco e critérios de aceite. Se houver V crítica, solicitar aprovação.
5. Implementar frontend e backend coerentes; forms/colunas/filtros/paginação, máscaras e mensagens do campo, cálculo monetário no servidor, ações confirmadas, estados carregando/vazio/erro/offline/acesso negado, idempotência, auditoria, responsividade e Tabler Icons conforme UI-MASTER.
6. Não preencher empresa automaticamente com `1` quando faltar tenant. Não confiar em empresaId de header/query/body sem conferir identidade autenticada. Testar leitura/edição/exclusão por ID pertencente à empresa B enquanto autenticado na A, inclusive recursos referenciados.
7. Para esquema SQL, escrever nova migração Flyway, não alterar migração aplicada e não perder dados; operações irreversíveis requerem plano/aceite.
8. Testar build Node 22/Angular e Java 21/Maven, contratos reais, efeitos colaterais em owners, permissões, empresa A/B, retry idempotente, E2E/manual onde possível, teclado, tablet e mobile. **CI verde não comprova visual nem fluxo real de todos os módulos.**
9. Atualizar **a mesma especificação canônica** com rota/API reais e desfecho; registrar status de verificações separado de estado de implementação. Fazer commit na `feature/develop` apenas depois de validação, seguindo a orientação operacional vigente.
### P0 bloqueios concretos da auditoria — corrigir antes de liberar fluxos sensíveis
- **Segurança de OS:** validar leitura/edição/exclusão por ID e escopo de empresa. Na auditoria do commit `9f8e6505df96`, `OrdemServicoService` usava `repository.findById(id)`, enquanto a entidade `OrdemServico` estendia `BaseEntity` (não `TenantEntity`). Interceptor JWT/tenant existente **não dispensa** teste de autorização por recurso e isolamento de relações.
- **Identificação da empresa:** remover fallback `tenantId=1` de `ordem-servico.service.ts` e `financeiro.service.ts`, impedir operação sem contexto válido e validar identidade no servidor.
- **Contrato de Orçamento:** `OrcamentoService` com baseUrl `/api` concatenava `/api/v1/orcamentos` e controller Java declarava `/v1/orcamentos`; conferir URL final, proxy e configuração de produção antes de atualizar.
- **Navegação/guard:** reconciliar `app.routes.ts`, `menu.json`, menu visual de prévia e `data.requiredPermissions` vs `data.permissions`; conectar componentes de Inspeções, Histórico e Gráficos sem criar telas duplicadas, manter o menu oficial e nenhuma Filial.
- **Gestão de Pátio:** `routes/patio/patio.ts` está em estado de estrutura, informa aguardar integração e contém referências de rota distintas de `/os`; não declarar Kanban real até ter API segura e testes.
- **Fiscal e Financeiro:** emitir/conciliar/estornar, boletos, créditos, NF-e/NFC-e/NFS-e, SINTEGRA e manifestos dependem de contratos/verificação legal, serviços reais, tolerância a falhas e idempotência; páginas existentes são especificações, não homologação.
### Contrato obrigatório de entrega do Codex por tarefa
`ScreenId | situação anterior [implementado/parcial/ausente] | reutilização | arquivos tocados | endpoints e DTOs efetivos | migrações | permissões backend/tenant | testes executados (resultado e comando) | validação browser/evidências | SHA/commit | link Notion atualizado | pendências/bloqueios | estado final [implementado / validado / homologado]`.
**Não declarar 100% enquanto não houver evidência funcional real para cada jornada e aprovação dos critérios de aceite.** Teste não executado deve ser registrado como **não executado**, nunca “aprovado”.
### Otimização de contexto e de custo do Codex
- Passar ao Codex o **ScreenId e URL do Notion**, não colar as 64 páginas num único prompt.
- Aproveitar instruções existentes em `AGENTS.md`; pesquisar somente arquivos da tarefa e importar contratos de owners dependentes.
- Fazer pequenos lotes coerentes (um fluxo que cruza frontend, backend e testes) e reutilizar o mesmo chat enquanto durar a mesma entrega.
- Preferir testes focados durante edição e build/pipeline abrangente no fim do lote; não pular testes críticos de segurança para economizar tokens.
- Entrega textual curta, com evidências auditáveis; não repetir código inteiro na resposta.
### Ordem de execução recomendada
**P0** isolamento e autoridade de empresa por recurso → **P1** navegação e rotas/guard/menus compatíveis com o inventário → **P1** contrato Orçamento e fluxo Clientes–Veículo–OS → **P1** núcleo operacional (Serviços, Inspeções, Agenda, Pátio, Vendas, Estoque/Compras) → **P1** Financeiro e Fiscal em integração segura → **P2** Gráficos, Relatórios e IA com dados reais. A sequência detalhada continua em `WORK_SEQUENCIA_TELAS.md`.
### DECISÕES DE PRODUTO E ENGENHARIA — NORMA EXECUTÁVEL PARA CODEX
**Decisão delegada pelo responsável pelo NERITECH AUTO em 14/10/2026.** Esta norma transforma ambiguidades de implementação em defaults claros. **Precedência:** 03.01.03 Módulos define escopo e owner; UI-MASTER-001 define UX; esta norma define políticas transversais; página ScreenId define campos/regras particulares; repositório `feature/develop` determina contratos existentes que exigem reconciliação. Não é autorização para alterar menu, apagar dados, emitir documentos reais ou criar despesas externas sem integração validada.
### D01. Arquitetura, desenvolvimento e não duplicação
- Uma aplicação Angular + backend Spring Boot + banco e migrações Flyway existentes; uma sessão e tenant autenticado. **Pátio é uma tela interna única**, nunca produto/microfront externo.
- Manter sidebar existente expandida 264px/recolhida 72px. Não criar Filiais nem CLI-004 de detalhes. Funcionalidade contextual só se torna rota separada se o catálogo canônico já definiu um ScreenId/rota.
- Prioridade de implementação: **reutilizar componentes e DTOs** \> estender com migração compatível \> criar componente/endpoint novo. Não reescrever aplicações inteiras; corrigir código legado com migração incremental.
- Branch `feature/develop`; commits pequenos por fluxo de ponta a ponta, sem alteração em `main`.
### D02. Regras definitivas de identidade e acesso
- **Uma empresa autenticada por contexto ativo** (tenant_id/empresa_id da sessão), sem conceito de Filiais. Mudança de empresa requer autenticação/contexto autorizado e invalidação de cache.
- Backend é autoridade: **fail closed** se tenant ausente, divergente ou não autorizado; jamais usar empresa `1` como fallback. Recursos lidos, criados, editados, excluídos e exportados exigem validação de pertença ao tenant e checagem de relações externas.
- Aplicar RBAC por capabilities de função (ver, criar, editar, aprovar, cancelar, exportar, configurar, administrar), vinculadas a Cargos/Permissões existentes; nunca considerar visibilidade do botão ou flag do menu como autorização de servidor.
- Perfis de referência: Administrador (gerência da própria empresa), Atendimento, Consultor/Orçamentista, Mecânico, Estoquista/Comprador, Financeiro, Fiscal e Leitura/Auditor. Implementar nomes de autoridade reais existentes e documentar equivalências, não criar strings soltas incompatíveis com o código.
- PII e dados bancários mascarados em listas por necessidade; exibir integralmente só com permissão e finalidade. Logs sem segredo, JWT, senha ou documento completo.
### D03. Identidade de dados, persistência e formulários
- IDs técnicos existentes ficam preservados, inclusive `Long` legado; UUID externo somente se modelo atual suportar ou migração segura aprovada. Nunca expor ID sequencial como token de acesso público.
- Tabelas normalizadas por proprietário (Cliente, Veículo, Produto, Serviço, OS, Financeiro, Fiscal). Referência por ID e **snapshot transacional** de descrição/preço/tributo/termo quando necessário; sem registro duplicado.
- Campos novos: tipo explícito, label/descrição em português, opcionalidade e mensagens de erro; string trim com limite (nome 2–160, descrição 1–500, observações até 5000 caracteres por padrão), data ISO no contrato HTTP e `dd/MM/yyyy` ou `dd/MM/yyyy HH:mm` na UI, moeda BRL pt-BR.
- Telefone em E.164 persistido quando possível, formatado no formulário; CPF/CNPJ com validação de dígitos quando aplicável e pesquisa prévia de duplicidade dentro do tenant; CEP pode preencher endereço com correção manual e fallback offline.
- Se requisito da tela pedir outro limite, prevalece ele. Não obrigar dados sensíveis sem fundamento de negócio/fiscal. Uploads autorizados: JPEG/PNG/WebP/PDF (conforme finalidade), tamanho padrão máximo 10 MB/arquivo com configuração por implantação, antivírus/validação MIME, armazenamento privado, URL autenticada temporária e exclusão auditada.
### D04. Listas/tabelas/UX que o Codex DEVE implementar em todas as telas
- Cabeçalho com título, contexto e CTA primário; barra de pesquisa e filtros relevantes; tabela server-side com ordenação, paginação 20 itens por página (opções 20/50/100), quantidade total, seleção quando aplicável e menu contextual com permissões.
- Persistir personalização de colunas/filtros **por usuário** e restaurar padrão; jamais ocultar identificação/status/ações essenciais sem alternativa. Datas e números alinhados/formatados, status com ícone e texto; exibição mobile em cartões detalhados; teclado e leitor de tela compatíveis WCAG 2.2 AA.
- Estados obrigatórios: carregando, vazio inicial, vazio por filtro, erro, offline, acesso negado, operação em andamento, sucesso confirmado e conflito de versão. Usar `nt-data-view-state`; toast de confirmação somente após API confirmar transação. Retry idempotente; não exibir dados demonstrativos como reais.
- Erro validação inline e resumo focável; modais só para ações curtas; formulários extensos em páginas existentes ou drawers com revisão e prevenção de perda de dados. Ícones exclusivamente Tabler; tipografia e tokens do UI-MASTER-001.
### D05. Cálculos monetários, estoque e comissões
- **BigDecimal no Java, NUMERIC no SQL**, nunca double/float para moeda; duas casas em BRL nos títulos e valores finais, quantidades/tempos com escala conforme unidade, arredondamento HALF_UP no ponto definido; distribuir diferença residual de parcelas na última parcela.
- Total de linha padrão = quantidade × valor_unitário − desconto válido + acréscimo válido. Não aplicar cumulativamente desconto percentual e valor em reais sem uma regra explícita da operação; padrão de entrada permite escolher **um método por item**, com representação derivada do outro, para evitar dupla dedução. Tributos dependem do documento fiscal e não são inferidos do preço.
- OS autorizada congela snapshot e versão; alterações geram complemento/aditivo com aprovação. Estoque só varia por evento registrado e idempotente. Disponível = saldo físico − reservado − bloqueado, se tais conceitos existirem/forem aprovados no modelo real; não permitir saldo negativo por padrão.
- Comissão padrão: apurada apenas em serviço concluído e cobrança efetivamente recebida, usando base líquida do item **sem tributos, descontos e estornos**; regra versionada por funcionário/serviço ou perfil de comissão; lançamento reversor para cancelamento. Se a oficina escolher outra política no futuro, parametrizar, não fixar em código.
### D06. Máquinas de estado canônicas (definições de referência, traduzir para enums existentes)
- **Orçamento:** RASCUNHO → EM_REVISAO → ENVIADO → AUTORIZADO ou RECUSADO ou CANCELADO → CONVERTIDO_EM_OS; expiração opcional com prazo configurável. Autorização requer prova de versão/valor e canal.
- **OS:** ABERTA → AGUARDANDO_APROVACAO (se aplicável) → AUTORIZADA → EM_EXECUCAO → AGUARDANDO_QUALIDADE → PRONTA_ENTREGA → ENTREGUE → FECHADA; CANCELADA via transição permitida, reversão exige motivo e trilha, não apagar evidências. Estados adicionais existentes no sistema devem ser **mapeados** sem quebra de migração.
- **Agenda:** SOLICITADO → CONFIRMADO → EM_ATENDIMENTO → CONCLUIDO; RECUSADO/CANCELADO/NAO_COMPARECEU com motivo. Reagendamento registra histórico.
- **Inspeção:** RASCUNHO → EM_EXECUCAO → EM_REVISAO (opcional) → CONCLUIDA; N/A com motivo conforme regra de item; conclusão congela snapshot.
- **Compra:** RASCUNHO → AGUARDANDO_APROVACAO → APROVADA → PEDIDA → RECEBIDA_PARCIAL/RECEBIDA → ENCERRADA, ou CANCELADA. **Cotação:** RASCUNHO → ENVIADA → RECEBENDO_PROPOSTAS → COMPARADA → SELECIONADA/ENCERRADA.
- **Financeiro:** PREVISTO (não contabilizado) → ABERTO → PARCIALMENTE_LIQUIDADO → LIQUIDADO; VENCIDO é derivado da data, não um estado que apaga status; CANCELADO/ESTORNADO como eventos auditados.
- **Fiscal:** RASCUNHO → VALIDACAO → TRANSMISSAO → AUTORIZADA ou REJEITADA; CANCELADA, CONTINGENCIA e DENEGADA somente se modelo e ente autorizador suportarem. Registro fiscal mantém requisição, retorno e protocolo. **Nunca simular autorização.**
### D07. Consistência de processos e idempotência
- Saga/orquestração por owner entre OS → reserva/consumo de peça → Financeiro → Fiscal → CRM, com IDs de correlação, transações curtas, outbox ou mecanismo confiável de entrega quando necessário e compensações explícitas; nenhuma mudança transversal acontece silenciosamente após uma falha parcial.
- Operações de confirmação, baixa, emissão, importação, webhook, atualização de estoque e envio de mensagens têm chave de idempotência. Cancelar documento não significa excluir histórico. Logs de auditoria guardam ator, tenant, data/hora, ação, entidade, estado anterior/posterior não sensível e motivo.
- Concorrência otimista por versão onde houver edição simultânea (OS, preços, estoque, agenda, financeiro); apresentar conflito 409 com opção segura de recarregar, não sobrescrever silenciosamente.
### D08. Fiscal, bancário e integração com terceiros — DECISÃO SEGURA
- Arquitetura com adaptadores e portas por provedor, configuração de credenciais protegidas por empresa (secret manager), ambientes sandbox/produção separados, logs sanitizados e filas/retry limitado com idempotência.
- **Sem provedor/credenciais/homologação, implementar fluxo completo de configuração, validação local e prévia**, deixando ação externa indisponível com mensagem honesta; nunca produzir NF, boleto, conciliação bancária, assinatura qualificada ou WhatsApp "enviado" falsos.
- Taxas, CFOP/NCM/CST/CSOSN, regime tributário, alíquotas, retenções, layout SINTEGRA, endpoints da SEFAZ/município e regras de cancelamento dependem de dados legais e versão vigente, fonte técnica oficial e contador. Os documentos devem ter campos, estados e adaptador extensível; **não fixar regra tributária inventada**. Escolher na UI apenas modelos suportados pela empresa/provedor.
- Ações externas irreversíveis/onerantes exigem confirmação de credenciais, privilégio, ambiente e intenção; a autonomia delegada cobre decisões de arquitetura e defaults reversíveis, não autoriza gasto contratual nem operação fiscal real sem credenciais e validação.
### D09. Inteligência Artificial e LGPD
- IA como assistente consultivo e automações **opt-in**, nunca como fonte transacional. Leitura apenas via APIs permissionadas de tenant; ferramentas com whitelist, limites de chamadas e auditoria; nenhum prompt permite contornar RBAC.
- Assistente pode resumir OS, sugerir triagem, revisar estoque e elaborar mensagens em **rascunho**, com fonte e indicação de confiança; ações financeiras, fiscais, exclusões, comunicações externas e mudanças de OS autorizada exigem confirmação humana contextual.
- Automações começam em modo rascunho/desligado; gatilhos/eventos definidos, execução idempotente, horário de funcionamento, quiet hours, opt-out e limite de frequência configuráveis. Somente depois de configurar canal/provedor ocorre envio real.
- Registro de base legal e consentimento por finalidade quando necessário; retirada de consentimento impede marketing subsequente, sem apagar dados que devam ser mantidos por obrigação legal ou contrato.
### D10. Métricas e fontes de verdade (padrões reversíveis)
- **Ticket médio realizado** = soma do valor líquido de OS de serviço concluídas/fechadas no período ÷ quantidade dessas OS; denominar "Ticket médio de OS concluídas", evitando misturar cobrança recebida com faturamento fiscal. Separar ticket de orçamento autorizado.
- **Conversão de orçamento** = quantidade de orçamentos autorizados no intervalo por data de decisão ÷ quantidade de orçamentos com decisão (autorizado + recusado) no mesmo intervalo. Os ainda abertos não entram no denominador; mostrar explicação.
- **Entregas atrasadas** = OS não entregues/canceladas com data_promessa menor que agora; atraso histórico é métrica separada. **Recebimentos/Pagamentos realizados** por eventos efetivamente liquidados, projeção por vencimentos; não misturar regimes sem legenda.
- **Lucro/margem/ponto de equilíbrio** só com custos e classificação validados, usando fórmula visível e sem dividir por zero. Dashboards, gráficos e relatórios compartilham *uma única camada de agregações*, com timezone da empresa, filtros equivalentes e tenant.
### D11. QA, entrega e homologação Codex
- Toda tela tem ScreenId, rota existente ou proposta a reconciliar, tipos/validações, colunas/filtros/ordenação/paginação, owners e DTOs, endpoint real verificado ou **PENDENTE DE IMPLEMENTAR**, estados, capabilities, teste de empresa A/B, responsividade/teclado e critérios de aceite. Ao criar API, versionar e testar contrato, não inventar rota no documento como existente.
- **Pronto para Codex** significa que esta norma e a página da tela permitem implementação sem dúvidas de UX/CRUD ordinárias. **Não significa implementado, funcional, CI aprovado ou homologado.**
- Cada entrega inclui comando/resultado de testes, falhas, evidência de uso real, commit SHA e link Notion; não afirmar conclusão 100% sem verificar. O Codex deve usar o inventário 64/64 como fila de QA, não implementar tudo em um commit.

