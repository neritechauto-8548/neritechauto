# NERITECH AUTO — Application Shell (Etapa 00)

## Escopo do shell ativo

- Navegação principal: `theme/admin-layout`, `theme/sidebar/sidebar.ts`, `theme/sidemenu` e `theme/header`.
- Branding, menu do usuário e breadcrumb: `theme/widgets`.
- Busca mobile e desktop com atalhos e responsividade. A autenticação funciona por padrão também em desenvolvimento; a prévia UX é ativada explicitamente com `?uxPreview=1` apenas no build de desenvolvimento. O build de produção mantém a prévia desativada.
- Serviços, formulários, filtros, tabelas e telas de domínio preservam a arquitetura congelada no Notion.

## Correspondência com as etapas 01–06

Etapas 01 e 02 usam as rotas públicas `/auth/login`, `/auth/recover` e `/auth/reset-password` no `AuthLayout`; a Etapa 03 usa `/home` no `AdminLayout`. Etapas 04–06 seguem as rotas Clientes e CRM registradas em `cliente.routes.ts`. A Etapa 00 define componentes e comportamento compartilhados, sem ScreenId próprio. O detalhamento funcional reside nas páginas oficiais do [Notion Telas & UX](https://app.notion.com/p/98501fd65a9c8379a9a281145568f52a); este arquivo mantém somente o contrato técnico do shell.

## Rotas de produto

O arquivo `app.routes.ts` registra apenas o Application Shell, autenticação, perfil/conta, estados de erro e os módulos existentes da NERITECH AUTO. Rotas de vitrine de componentes herdadas do template (`design`, `material`, `media`, `forms`, `tables`, `permissions`, `utilities`) **não integram a navegação do produto**. O código de demonstração pode permanecer no repositório enquanto for útil para desenvolvimento, mas não deve gerar telas extras acessíveis na aplicação.

Rotas desconhecidas apresentam a página 404. Respostas 404 de uma API não substituem a página atual. Ao receber 401 em uma operação protegida, o cliente limpa a sessão local e encaminha para o login; após autenticar, retorna à URL interna solicitada. Não há empresa padrão para renovação de token.

Não recriar a sidebar antiga `SidebarMenuComponent`: usar exclusivamente `theme/sidebar/sidebar.ts` e respeitar o menu oficial do Notion. Não criar atalho de rota inexistente para simular módulo ainda não implementado.

## Paridade documental e lacunas técnicas de navegação

A referência de arquitetura/menu é [03.01.03 Módulos](https://app.notion.com/p/80101fd65a9c833580f1012ec0a45407), o contrato de UI é [UI-MASTER-001](https://app.notion.com/p/97401fd65a9c822a9a4b017c39a08518), e as especificações de telas ficam em [03.01.04 Telas & UX](https://app.notion.com/p/98501fd65a9c8379a9a281145568f52a). O presente Markdown **não substitui** essas fontes nem autoriza reconfigurar o menu.

**Situação documental das etapas 00–06:** referências, ScreenIds, rotas e distinção entre requisitos aprovados e contratos ainda propostos estão reconciliados. **Isso não significa paridade de implementação**. Na `feature/develop` verificada:
- O menu **de produção** vem de `FrontEnd/public/data/menu.json`; na BASE-03 de 15/10/2026 sua árvore foi alinhada aos 11 grupos da fonte 03.01.03, sem agrupamento `operacional`, sem `Filiais`/CLI-004 e sem ícones PrimeIcons no JSON. São 43 destinos habilitados com caminho conferido em rotas Angular e 26 itens indisponíveis por ainda não terem destino validado. A mudança não equivale a homologação em navegador.
- Gestão de Pátio permanece **uma única tela interna**; possui entrada desabilitada no menu de produção, ainda sem rota Angular/tela própria implementada. Não apontar para aplicação externa nem para uma tela falsa.
- Em Clientes, `/cliente/listar`, `/cliente/cadastro`, `/cliente/editar/:uuid`, `/cliente/crm`, `/cliente/crm/pesquisa` e `/cliente/crm/comunicacao` estão registrados no Angular. A integração de cada função com APIs continua sujeita a testes próprios.
- Login, recuperação, redefinição de senha e Home têm rotas Angular registradas. Isso **não equivale** a testes E2E ou aceite visual em ambiente implantado.

**Gate:** a Prioridade 2 documental pode ser considerada encerrada **no escopo 00–06 e na decisão Gestão de Pátio**, sem nova documentação. As rotas ainda ausentes continuam desabilitadas no menu e pendentes de implementação; as rotas presentes continuam sujeitas a testes de navegação, permissões, integração e homologação, sem criar documentação paralela.

## Gate de homologação

- `yarn build` deve passar no pipeline Angular;
- O tema normativo atual é **claro**, conforme UI-MASTER-001; tema escuro ou chave de alternância não é requisito da Etapa 00 sem aprovação de tokens e regra de produto.
- `mvn -B clean test -DskipTests=false` deve passar com banco PostgreSQL de testes;
- CI verde **não substitui QA visual** de 320, 375, 768, 1024 e 1440 px nem navegação por teclado;
- confirmar perfil, permissões, navegação e comportamento sem backend apenas em UX Preview;
- manter pendências explícitas até a validação real no navegador.

## Acessibilidade da busca e do menu (continuação 2026-10-11)

A pesquisa do shell navega apenas pelos itens visíveis no menu disponível à sessão, usa `role=combobox` / `listbox`, setas, Enter, Escape e Ctrl/Cmd+K, com estado vazio. Destinos externos sem URL HTTP(S) válida não são oferecidos na pesquisa. Essa filtragem é UX, **não substitui a autorização do backend**.

A sidebar abre automaticamente o ancestral da rota ativa, inclusive quando o segmento de um grupo é vazio. Os submenus indicam `aria-expanded`, descendentes recolhidos ficam `inert` para Tab e os links/botões têm nome acessível mesmo com ícones isolados.

**Critérios de aceite pendentes de QA manual:** teclado e leitor de tela; rotas profundas; acesso sem backend em UX Preview; 320/375/768/1024/1440 px; tema claro canônico, contraste e zoom. A alternância para tema escuro não integra o aceite vigente. Build e testes unitários, mesmo verdes, não autorizam declarar a Etapa 00 homologada sem essas verificações no navegador.

## Notificações globais: contrato funcional e isolamento

- O sino consulta `GET /v1/comunicacao/notificacoes/me` usando o usuário autenticado, sem aceitar um `usuarioId` escolhido pelo navegador. O servidor recupera a empresa pelo contexto de autenticação e exige correspondência do destinatário e da empresa no repositório.
- Ao marcar leitura, usar `PATCH /v1/comunicacao/notificacoes/{id}/ler`; o servidor só altera notificações do destinatário atual, persiste `lida` e `dataLeitura` e mantém a operação idempotente. A interface reduz o contador apenas após confirmação de `lida: true` na resposta.
- Endpoints administrativos legados (`/usuario/{usuarioId}`, `/{id}`, criação, atualização e exclusão) exigem papel ADMIN, com verificação adicional do escopo de empresa no service. O menu operacional não deve chamar esses endpoints.
- O painel apresenta **carregando / sem dados / erro de rede / usuário não autenticado** como situações distintas; falha de API nunca pode aparecer como “nenhuma notificação”.
- No modo `uxPreview`, o painel exibe um aviso explícito de prévia e não chama a API nem registra ações falsas. Em produção, a atualização de notificações ocorre por polling com uma única assinatura por usuário ativo.
- A lista usa controles do tipo button navegáveis por Tab/Enter/Espaço, além de feedback acessível dos erros. A antiga ação “Ver todas as notificações” foi retirada do menu por apontar para uma rota que não existe em `app.routes.ts` ou no módulo de Configurações.
- Os testes de regressão cobrem filtragem por destinatário/empresa, persistência de leitura, rejeição de outras identidades, modo preview e estados de erro. O pipeline executa compilação Angular, testes do shell no Chrome Headless e testes Maven.

**Gate:** após o pipeline ficar verde, faltará homologação visual real em 320/375/768/1024/1440 px, checagem de contraste, leitor de tela, navegação completa com teclado, trocas de sessão/tenant e ensaio integrado com autenticação real. Nenhuma dessas verificações pode ser marcada como concluída apenas pelo CI.

## Navegação funcional na prévia UX — rotas auditadas (2026-10-11)

A árvore do menu permanece idêntica à arquitetura canônica; a prévia UX **não inventa páginas**. Cada opção com implementação Angular confirmada em `app.routes.ts` e em suas rotas lazy aponta para a URL real. Exemplos: Agenda `/agendamento/calendario`, Orçamentos `/orcamento`, Ordens de Serviço `/os`, Serviços `/produtos-servicos/servicos`, Veículos `/veiculo`, Vendas `/pdv`, Estoque `/produtos-servicos/estoque`, Inventário `/configuracoes/inventario`, NF-e `/fiscal/nfe` e Inutilização `/fiscal/nfe/inutilizar-numeracao`.

Itens oficiais sem rota operacional no frontend atual continuam visíveis **somente como indisponíveis**, com `aria-disabled`, sem `href` ou `routerLink`, e são excluídos da busca global. Gestão de Pátio deve abrir uma tela interna quando a rota e o contrato canônicos forem implementados; não deve apontar para sistema externo. Essa indicação diz respeito à disponibilidade de navegação, **não** à homologação funcional dos módulos correspondentes nem à integração real com APIs.

Os testes `theme/sidemenu/preview-menu.spec.ts`, `sidemenu.spec.ts` e `header.spec.ts` cobrem o mapeamento, os destinos externos inválidos e a busca. Qualquer nova tela habilitada na prévia deve ser confrontada com uma rota Angular realmente registrada antes de liberar o clique. Continua pendente inspeção manual com leitor de tela e em todas as larguras do gate.

## BASE-03 — verificação incremental (15/10/2026)

- O shell reutiliza sidebar/header/branding/breadcrumb/busca existentes. Largura declarada 264px aberta/72px recolhida em `styles.scss`, `admin-layout.scss` e `sidebar.scss`; sem segunda sidebar.
- UX Preview carrega o mesmo arquivo estático `/data/menu.json` sem exigir token ou backend, mantendo fallback local para falha no recurso estático. Produção segue o filtro de permissões por usuário/plano. Destinos futuros mantêm `disabled` e não participam da pesquisa global.
- `app.routes.ts` aplica `permissionGuard` aos descendentes do layout com `data.requiredPermissions` quando declarado; Orçamentos exige `GERAL_ORCAMENTO`. Agenda/Calendário exige `GERAL_AGENDAMENTO_VISUALIZAR`, Caixa exige `FIN_VIS_CAIXA` e rotas administrativas sensíveis exigem `GERAL_CONFIG_SISTEMA`. São barreiras UX; autorização e tenant permanecem responsabilidade do backend.
- Conferências estáticas efetuadas sobre arquivos da branch: ordem dos módulos, 43 rotas habilitadas/26 desabilitadas, exclusão de Filiais/CLI-004, declaração de guards e tokens 264/72. Teste reproduzível: `cd FrontEnd && node --test scripts/navigation-contract.test.cjs`; incluído no workflow `Dashboard Quality` e aprovado no job frontend do run 38774778414, junto com `yarn build` e testes Angular do shell. Permissão da rota lazy de Orçamentos reforçada com `canActivate: [permissionGuard]`.
- Pendências de aceite: build/testes Angular em Node 22, integração auth/API, 320/375/768/1024/1440 px, refresh/deep-link, 404, teclado/leitor de tela, permissões reais de cargos A/B, foco/contraste e QA visual. **BASE-03 não homologada.**
