# 04.01 🎨 NeriTech Auto — UI Master / Application Shell

> **Cópia de consulta do Notion, gerada em 2026-10-14.** Fonte canônica: https://app.notion.com/p/97401fd65a9c822a9a4b017c39a08518. Não editar esta cópia como norma principal.
> **Última edição registrada no Notion:** 2026-10-14T16:25:08.586Z.


## A — CONTRATO EXECUTÁVEL CODEX — APLICAÇÃO GLOBAL 00 \| DECISÃO FINAL
**Escopo:** infraestrutura compartilhada para as 64 telas; não conta como nova tela. Prevalece UI-MASTER-001 com nova direção visual e norma D01–D11 do catálogo.
### Layout e navegação
- **Sidebar NERITECH existente:** mantida, 264px expandida/72px recolhida; persistência de preferência por usuário sem impactar tenant; keyboard/aria/foco; menu e rotas da fonte 03.01.03. Sem barra de ícones estreita, barra dupla, tags artificiais, Filiais ou Gestão de Pátio externa.
- Header com logo e marca, busca global Cmd/Ctrl+K, notificações por dados reais, menu perfil e logout, breadcrumbs com links válidos, estado ativo e navegação de erro acessível; funções não prontas devem aparecer honestamente desabilitadas, sem falso redirecionamento para Home.
- Shell padrão: página com título, contexto, ação primária, pesquisa/filtros, superfície/tabela, paginação e rodapé apenas quando aplicável; densidade para operação de oficina; garantir responsividade 320/375/768/1024/1440px e zoom 200%.
- **Design tokens vigentes:** UI-MASTER-001. Canvas azul-lavanda claro, painéis brancos, azul CTA, border delicado, tipografia legível, Tabler Icons; componentes próprios PrimeNG podem ser adaptados sem recriar biblioteca nem usar primeicons visíveis. Preservar tema original na sidebar.
### Biblioteca de componentes e contrato
- Componentes canônicos: `PageHeader, DataViewState, Table, FilterBar, FormField, Select/Autocomplete, DateTimePicker, Modal/Drawer, Toast, ConfirmDialog, FileUpload, Status, EmptyState, Pagination, Tabs, Skeleton, ErrorBoundary, PermissionGuard`. Reutilizar componentes reais descobertos no repo, não criar duplicado por nome.
- Estados em todo dado assíncrono: loading inicial, loading parcial, vazio inicial, filtro sem resultados, offline, erro recuperável, 403, 404, validação 422 e conflito 409; `nt-data-view-state` com mensagem e CTA de retry real. Toast de sucesso apenas com confirmação persistida; desabilitar botões durante mutação.
- Data table server-side: 20 default, opções 20/50/100; sort whitelist, coluna preferencial e filtros salvos por usuário, busca debounce 300ms, ações contextuais com labels; versão mobile em cards com dados essenciais.
- Campos: label persistente, obrigatoriedade/placeholder diferentes, máscara de CPF/CNPJ/CEP/placa/telefone sem modificar valor bruto inadvertidamente, datas pt-BR e API ISO, Decimal BRL no servidor. Texto/contraste WCAG 2.2 AA, foco não obscurecido e ícone acompanhado de label para ações essenciais.
### Segurança e roteamento real
- Reconciliar `FrontEnd/src/app/app.routes.ts`, arquivos \*.routes.ts, `FrontEnd/public/data/menu.json` e `StartupService` modo preview/produção; a árvore publicada deve corresponder a 03.01.03 sem criar telas duplicadas; perfis ocultam acesso, backend decide autorização por ID/tenant.
- Padronizar `permissionGuard` com `data.requiredPermissions` ou adaptar guard com compatibilidade; nunca depender de `data.permissions` silenciosamente ignorado.
- Reusar login/logout/refresh e `TenantInterceptor` backend, remover fallback de empresa 1. Navegar entre contas invalida cache e tokens antigos, não vazar empresa B em empresa A.
- UX Preview somente desenvolvimento local, explicitamente sinalizado e isolado, nunca como dados homologados; scripts de produção não aceitam `?uxPreview=1` como bypass, autenticação somente pelo servidor.
### Contratos de integração e entrega
- Um endpoint de consulta deve ter DTO, auth, escopo de tenant, teste unitário/integrado e tratamento de erro. Não colocar secrets no app; fallback visual de API indisponível é **erro explicativo**, não mock silencioso.
- P0: testes GET/PUT/DELETE OS de outra empresa, financeira cruzada, role 403. P1: menu/rota de todos ScreenIds, 404, breadcrumbs, deep links, refresh de página, carregamento lazy, títulos e responsividade. P2: acessibilidade, UX visual, e2e jornadas principais e métricas de performance.
- Build: Node 22/Yarn Angular; Java 21/Maven; Flyway novos versionados; testes focused no desenvolvimento e pipeline completo ao final do lote. Registrar SHA, comandos e evidência; **CI verde sem E2E/browser não significa 100% homologado**.
> **Contrato de consistência vigente:** este UI-MASTER define a linguagem visual e o Application Shell, mas não pode alterar a árvore oficial, regras de negócio ou a especificação canônica das telas. A reconstrução integral de UX/UI está governada por [04.08 — Auditoria de Consistência UX & Design](https://app.notion.com/p/3ee01fd65a9c814cb841c64433129e45). Referências: Stripe para design engineering; Shopmonkey, UltraCar e OnMotor para padrões conceituais de operação automotiva. Não copiar identidade visual. Tabler Icons permanece a família única aprovada.
# B — UI-MASTER-001 — Design System e Application Shell
## Objetivo
Definir a linguagem visual única do NERITECH AUTO para todas as telas.
## Princípios
- simples;
- moderno;
- profissional;
- claro;
- consistente;
- alta legibilidade;
- baixa poluição visual;
- foco na informação;
- ações previsíveis;
- complexidade no sistema, simplicidade para o usuário;
- informação progressiva: mostrar primeiro o que é necessário para decidir ou executar;
- densidade funcional sem aparência de ERP pesado;
- cada superfície deve responder claramente: onde estou, o que estou vendo e o que posso fazer;
- reutilização de padrões antes da criação de novos componentes;
- aparência premium sem efeitos decorativos desnecessários.
## Direção visual vigente — capturas de referência
**Decisão de produto (14/10/2026):** adotar nas superfícies internas do NERITECH AUTO a linguagem de componentes vista nas imagens de lista de modelos de inspeção, editor de checklist, menu de inspeção e calendário: conteúdo claro, fundo azul-acizentado suave, controles com bordas delicadas, azul vibrante em ações primárias, hierarquia de página e tabelas legíveis. Referências são conceituais; não copiar marca, layout exato, imagens, logotipos ou implementação de terceiros.
**Exceção expressa e obrigatória:** a barra lateral existente do NERITECH AUTO permanece com o próprio comportamento de abrir/recolher, sem conversão em trilho de ícones compacto e sem segunda sidebar. Alterar as cores e os componentes do conteúdo, não redesenhar a navegação lateral para imitar as referências.
### Papéis das referências
- **Imagens fornecidas — direção visual de superfícies de trabalho:** composição de páginas, botões, filtros, campos, tabelas, dropdowns, checklist, indicadores e agenda.
- **Stripe — qualidade da experiência:** engenharia de interação, acessibilidade, hierarquia, feedback e consistência; não é mais a única referência de aparência.
- **Shopmonkey — fluxo operacional:** agendamento, inspeção, orçamento, comunicação e execução da oficina.
- **UltraCar — profundidade funcional brasileira:** cadastros, processos, campos e regras; exigem verificação própria antes de homologação.
- **OnMotor — adequação ao mercado brasileiro:** contextos, fluxos e linguagem do setor.
### Elementos visuais de referência
- Topbar limpa e branca; título grande e conciso; quantidade total junto ao título quando útil; botão primário à direita.
- Canvas azul-acizentado/lavanda muito claro e painéis brancos com borda discreta; contraste funcional, sem aspecto monocromático.
- Filtros e busca organizados antes da tabela; ação de personalizar colunas; ordenação explícita; menu de três pontos por linha.
- Botão primário azul-vibrante sólido com texto branco; ações secundárias com fundo neutro; seleção em azul claro.
- Inspeções estruturadas por seção e item, notas internas e classificação de resultados; status com ícones e texto, não somente cor.
- Calendário com navegação temporal, seletores de período, blocos de eventos e indicação acessível de horário.
- Layout denso o suficiente para operação real, porém legível, sem faixas enormes vazias nem cartões decorativos.
### Critério de originalidade
A interface final é NERITECH AUTO: identidade, arquitetura, textos em pt-BR, ícones Tabler, componentes próprios e regras canônicas. Não copiar pixels, marca, imagens ou código das referências.
## Filosofia de experiência NERITECH AUTO
### Princípio central
> **Complexidade no sistema. Simplicidade para o usuário.**
A plataforma pode possuir grande profundidade funcional, mas a interface deve revelar apenas a complexidade necessária para o contexto atual.
### Regra de composição
Toda tela deve priorizar nesta ordem:
1. contexto — onde o usuário está;
2. objetivo — o que a tela permite resolver;
3. informação principal — o que precisa ser entendido;
4. ação primária — o próximo passo mais importante;
5. ações secundárias — ações complementares;
6. detalhes avançados — somente quando necessários.
### Padrão de densidade
- evitar excesso de cards;
- evitar bordas e sombras decorativas;
- evitar dezenas de ações simultâneas;
- evitar tabelas com colunas que não ajudam a decisão;
- evitar modais para fluxos longos;
- evitar telas que exigem conhecimento prévio do sistema para serem compreendidas.
### Progressive disclosure
Informações avançadas devem aparecer por contexto, abas, drawers, detalhes expansíveis ou páginas específicas, sem esconder dados críticos nem criar navegação confusa.
### Linguagem visual
A interface deve transmitir **precisão, confiança, tecnologia e simplicidade**, sem parecer uma cópia de Stripe, Linear, Vercel ou qualquer outra referência.
## Application Shell
### Sidebar — PRESERVAR a navegação atual
- **Manter a sidebar existente do NERITECH AUTO**, com uma única estrutura lateral que alterna **expandida e recolhida**, exatamente no padrão funcional atual do sistema.
- **Larguras canônicas preservadas:** expandida **264 px**, recolhida **72 px**; não trocar essas dimensões apenas para reproduzir o exemplo visual.
- O modo expandido exibe ícones e rótulos completos; o recolhido preserva o mesmo menu e acesso à expansão, respeitando acessibilidade por teclado, títulos e foco.
- **Proibido adotar** barra de ícones fixa e estreita como navegação primária, barra lateral dupla, painel secundário de listas ou trilho de navegação inspirado nos prints.
- Preservar a árvore de menu, as rotas existentes, os agrupamentos e estados ativos estabelecidos em 03.01.03 Módulos, sem criar tags visuais adicionais, módulos ou itens duplicados.
- Manter Gestão de Pátio como tela interna da aplicação, nunca link externo ou produto à parte.
- Refinamentos cosméticos podem harmonizar tipografia, seleção, hover e bordas da sidebar com os novos tokens, sem mudar estrutura, comportamento, hierarquia ou navegação.
- Em tablet e mobile, conservar a estratégia responsiva já existente e validar teclado, foco, touch e leitor de tela antes de alterar o comportamento.
### Topbar
Pode conter:
- empresa/unidade;
- busca global;
- notificações;
- ajuda;
- perfil.
#### Contrato de notificações da aplicação global
- **Owner:** Comunicação; o shell apresenta o acesso global, sem criar novo módulo ou página de notificações.
- **Consulta:** `GET /v1/comunicacao/notificacoes/me`, com usuário autenticado e empresa do contexto de segurança, paginação e ordenação por data.
- **Marcação de leitura:** `PATCH /v1/comunicacao/notificacoes/{id}/ler`, idempotente. O servidor confirma a persistência de `lida` e `dataLeitura`; a interface altera contador e estado somente após confirmação.
- **Estados:** carregando, lista sem itens, indisponibilidade da API, usuário sem sessão, operação de leitura em andamento e falha de atualização são distintos. Erro de API não pode aparecer como lista vazia.
- **Acessibilidade:** botão do sino nomeado, lista operável por Tab, Enter e Espaço, erros por regiões de alerta, foco perceptível e contraste conforme WCAG 2.2 AA.
- **Prévia UX:** sem autenticação/backend, painel explicitamente demonstrativo, sem chamar API e sem simular persistência.
- **Segurança:** consultas, leitura e APIs administrativas validam usuário, empresa e permissões no backend; nunca confiar só no menu para autorização.
- **Aceite pendente:** comprovar no navegador desktop/tablet/mobile, com usuário real e contas de empresas diferentes; evidência de CI não substitui QA visual.
### Main
Área principal de conteúdo.
## Grid e composição visual
- Grid responsivo: desktop 12 colunas, tablet 8, mobile 4; respeitar a sidebar atual de 264/72 px.
- **Listagem:** título e total; CTA primário alinhado ao extremo direito; barra de pesquisa/filtros; opções de personalização de colunas; tabela em superfície branca com borda discreta; rodapé de paginação.
- **Edição:** identificação e contexto no topo; ações principais visíveis; campos com labels persistentes; seções coerentes; menus contextuais apenas para ações secundárias.
- **Inspeção:** títulos de seção, notas internas, itens numerados, campo de observação e opções de resultado com texto acessível; controles de adição de item e salvamento de modelo quando autorizado.
- **Agenda:** Hoje/Anterior/Próximo, seleção do período, criação de compromisso, grade de dias/horários e cartões de evento; fuso, horário de início e fim apresentados em formato brasileiro.
- Não replicar o menu de listas dos exemplos: toda navegação deve permanecer no menu canônico NERITECH e nos componentes internos da própria página, sem sidebar extra.
## Tipografia
- títulos objetivos;
- hierarquia clara;
- labels persistentes;
- texto secundário legível;
- valores numéricos com alinhamento consistente.
## Espaçamento
Utilizar escala única do Design System. Componentes não devem criar espaçamentos arbitrários.
## Componentes
A biblioteca deve ser pequena, previsível e reutilizável. Antes de criar um componente novo, verificar se a necessidade pode ser resolvida por composição de componentes existentes.
- Button;
- IconButton;
- Input;
- Select;
- Autocomplete;
- DatePicker;
- Search;
- Filter;
- Badge;
- Status;
- Alert;
- Card;
- Table;
- Pagination;
- Tabs;
- Modal;
- Drawer;
- Dropdown;
- Tooltip;
- Toast;
- Timeline;
- Upload;
- Chart.
## Estados
Todo componente interativo deve definir:
- default;
- hover;
- focus;
- active;
- disabled;
- loading;
- error;
- selected;
- success quando aplicável.
### Estados de dados compartilhados
Para listas, tabelas, grids e painéis de dados, utilizar o componente de infraestrutura `nt-data-view-state` (`FrontEnd/src/app/shared/components/data-view-state/`). Definir estados distintos para carregando, vazio inicial, vazio por filtros, erro de API, offline e sem permissão. Mensagens e ações devem ser contextuais; só mostrar **Tentar novamente** quando existir tratamento real de `(retry)`. Carregamento e vazio não são sucesso de mutação. Preservar acessibilidade (status/alerta, navegação por teclado, contraste e foco visível). O backend é a autoridade para autorização e dados.
## Botões e ações
- Primário: azul de marca sólido, texto branco, borda sutil, foco visível, alvo de clique confortável; apenas uma ação primária por região.
- Secundário: superfície branca ou sutil, borda suave e texto de alto contraste; não disputar hierarquia com o CTA.
- Terciário e links: azul para navegação e ações de baixa ênfase; uso contido, sem multiplicar links em todas as células.
- Destrutivo: estilo de alerta distinto e confirmação proporcional à criticidade.
- Grupo de ações: evitar múltiplos botões concorrentes; ações de linha e pouco frequentes no menu contextual acessível por teclado e toque.
- Todos os controles possuem default, hover, pressed, focused, disabled, loading e tratamento explícito de erro/sucesso.
## Tabelas
- Painel branco com contorno cinza-azulado leve e raio moderado; cabeçalho distinto, sem sombras excessivas; linhas separadas por divisores suaves.
- Ordem de leitura: identificação principal à esquerda; dados operacionais; status; valores e datas alinhados conforme tipo; ações de linha à direita.
- Busca contextual, filtros combináveis, ordenação por coluna, personalização de colunas persistente por usuário, paginação, total de itens e estado de filtros aplicados.
- Ações por linha em menu de três pontos com labels reais e permissões de backend; ações perigosas exigem confirmação.
- Dados numéricos com numerais tabulares; datas em pt-BR; ausência de valor representada por texto acessível, não por célula enganosa.
- Responsividade: no tablet ocultar colunas não essenciais por prioridade; no mobile apresentar cards/linhas adaptadas, mantendo tarefas essenciais sem scroll horizontal obrigatório.
- Estados separados: loading, vazio inicial, vazio filtrado, erro, offline, acesso negado, parcial e desatualizado, com recuperação apropriada.
## Formulários
- Superfícies brancas e campos com bordas cinza-azuladas suaves; altura consistente, sem blocos excessivamente altos; campos multilinha apenas onde o conteúdo exige.
- Label persistente acima do campo; placeholder como exemplo, nunca como único nome; indicação clara de opcionalidade/obrigatoriedade.
- Grupos curtos por tarefa com títulos e espaço vertical controlado; campos lado a lado só quando melhoram comparação e leitura.
- Combobox, DatePicker, autocomplete, textarea, checkbox, radio, upload e controles de inspeção usam os mesmos tokens de borda, foco, erro e disabled.
- Validação inline na saída do campo e submissão, mantendo valores digitados e oferecendo recuperação; salvar/carregar com feedback e prevenção de perda de dados.
- Não reproduzir literalmente textos em inglês ou posições de campos dos exemplos externos.
## Ícones
Utilizar uma biblioteca única, preferencialmente Tabler Icons.
O mesmo significado deve utilizar o mesmo ícone em todo o produto.
Os ícones reutilizáveis do shell usam SVG outline com a assinatura Tabler e são expostos por `nt-icon` (`FrontEnd/src/app/shared/components/neritech-icon/`). Tamanhos 16, 20 e 24 px, traço 2 conforme tokens. Não criar SVGs enormes, preenchidos sem semântica ou uma segunda família de ícones; ícones apenas decorativos devem ficar ocultos para leitores de tela.
## Acessibilidade
- WCAG 2.2 AA;
- teclado;
- foco visível;
- contraste;
- labels;
- aria quando necessário;
- não depender apenas de cor;
- zoom;
- reduced motion.
## Responsividade
Nenhuma função essencial pode depender exclusivamente de hover ou desktop.
## Regra
O Design System organiza a interface, mas não cria, remove, move ou redefine módulos.
## Registro canônico de tokens visuais — NERITECH AUTO
**Autoridade:** esta seção é a referência canônica dos tokens visuais do produto. 04.05 — MASTER DESIGN pode apresentar uma representação técnica para ferramentas, mas não deve criar valores alternativos. Se o código existente divergir, registrar o drift e propor migração controlada; não criar uma segunda paleta.
### Identidade e superfícies — paleta para a nova direção visual
**Tokens de design atualizados como especificação-alvo.** Antes de substituir CSS/PrimeNG/Figma no código, auditar divergências e testar contraste nas combinações reais. Não alterar visual da sidebar além da harmonização cromática sem nova aprovação.
- Primary 50: `#EEF1FF`
- Primary 100: `#DFE5FF`
- Primary 500: `#3154FF`
- Primary 600: `#2442DC` (CTA principal em superfície clara)
- Primary 700: `#1D34B0` (hover/pressed)
- Canvas: `#F2F3FB` (azul-lavanda sutil)
- Panel: `#FFFFFF`
- Subtle surface: `#F7F8FC`
- Border default: `#D9DDEA`
- Text primary: `#182033`
- Text secondary: `#4F5870`
- Text muted: `#687187`
- Success: `#15803D`; success surface: `#F0FDF4`
- Warning: `#B45309`; warning surface: `#FFFBEB`
- Danger: `#B91C1C`; danger surface: `#FEF2F2`
- Info: `#0369A1`; info surface: `#F0F9FF`
- Neutral: `#475569`; neutral surface: `#F8FAFC`
- Active/selected row: `#EEF1FF`; focus ring: `#3154FF`; dividers: `#E4E6F0`
- Tipografia, semântica das cores, foco e contrastes devem ser verificados em WCAG 2.2 AA em componentes reais. Se uma combinação falhar, usar tom mais escuro do mesmo token sem quebrar a paleta canônica.
### Tipografia
- Família: Inter, system-ui, Segoe UI, Roboto, sans-serif.
- Body SM: 13/18 px, peso 400.
- Body MD: 14/20 px, peso 400.
- Body LG: 16/24 px, peso 400.
- Label: 13/18 px, peso 500.
- Caption: 12/16 px, peso 400.
- H1: 28/36 px, peso 700.
- H2: 22/30 px, peso 650.
- H3: 18/26 px, peso 600.
- Usar numerais tabulares para valores financeiros, métricas e colunas numéricas quando isso melhorar comparação.
### Espaçamento, raio e borda
- Escala de espaçamento: 0, 4, 8, 12, 16, 20, 24, 32, 40, 48 e 64 px.
- Raio: sm 6 px; md 8 px; lg 12 px; xl 16 px; pill 999 px.
- Borda padrão: 1 px, `#E2E8F0`.
- Elevação: cartões sem sombra ou sombra muito sutil; dropdown sutil; modal média, sem efeitos decorativos pesados.
### Application Shell e breakpoints
- Sidebar expandida: 264 px; recolhida: 72 px.
- Topbar desktop: 64 px; mobile: 56 px.
- Margem de conteúdo: desktop 32 px; tablet 24 px; mobile 16 px.
- XL: 1440 px ou mais; LG: 1280–1439 px; MD: 1024–1279 px; SM: 768–1023 px; XS: abaixo de 768 px.
- Grid de referência: desktop 12 colunas, tablet 8, mobile 4. O grid adapta o conteúdo; não pode criar scroll horizontal essencial.
### Ícones, movimento e acessibilidade
- Família única: Tabler Icons, estilo outline, grade 24×24, stroke 2, tamanhos usuais 16/20/24 px.
- Não misturar bibliotecas de ícones. Ícones preenchidos só quando tiverem semântica explícita de seleção/estado.
- Movimento sutil, funcional e respeitando reduced motion; nunca adicionar animação decorativa para “parecer moderno”.
- Alvo: WCAG 2.2 AA; teclado, foco visível e não encoberto, contraste, zoom a 200%, status não comunicado só por cor e alternativa textual para gráficos.
- Tema canônico atual: claro. Tema escuro só pode ser introduzido por decisão formal e com tokens semânticos completos.
### Regras de governança dos tokens
1. Tokens semânticos são a interface entre design, Figma, CSS e biblioteca de componentes.
2. O mesmo token não pode ter valores diferentes em Figma, CSS, Tailwind ou biblioteca de componentes.
3. Não adicionar cores ad hoc para resolver uma tela; propor nova semântica apenas quando houver necessidade de produto e revisão de contraste.
4. Valores acima são a base documental existente consolidada; antes de aplicá-los em lote, comparar com o código real e documentar qualquer drift.
5. A confirmação de contraste deve ser feita por combinação real de foreground/background, não presumida apenas pelo nome do token.

