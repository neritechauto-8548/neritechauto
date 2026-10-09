# NeriTech Auto — Auditoria UX/UI e Inventário de Navegação

Data: 2026-09-06  
Branch: `feature/neritech-auto-rebuild`

## Resultado da revisão inicial

O frontend Angular 20 já possui um App Shell compartilhado, tokens NeriTech,
`PageHeader`, navegação por permissões e wrapper `NeriTechIcon` para Tabler.
O menu oficial documentado em `DESIGN.md` é a fonte de verdade e permanece
imutável nesta fase:

1. Gestão de Pátio
2. Home
3. Clientes
4. Operacional
5. Cadastros
6. Movimentação — Orçamentos, Ordens de Serviço, Checklists, Aprovações, Peças e Faturamento
7. Financeiro
8. Fiscal
9. Histórico
10. Gráficos
11. Agendamentos
12. Relatórios

## Divergências encontradas

| Área | Evidência | Tratamento desta fase |
| --- | --- | --- |
| App Shell | `theme/admin-layout` usa `Sidebar` + `Sidemenu`; existe uma sidebar antiga paralela | Manter somente o App Shell ativo como padrão de reconstrução |
| Ícones | telas legadas ainda contêm `pi pi-*` | telas reconstruídas usam exclusivamente `NeriTechIcon`; migração legada será incremental |
| Rotas | `gestao-patio`, `checklists-operacionais`, `aprovacoes`, `pecas-movimentacao`, `faturamento-operacional` e `historico` ainda usam placeholder | Substituir por telas de módulo com estados reais ou indisponibilidade explícita, sem dados fictícios |
| Menu | o JSON local é filtrado pelo backend/permissões e ordenado por `menu-contract.ts` | preservar autorização do backend e aplicar apenas ordenação visual canônica |
| Portal | existe frontend separado em `PortalCliente` | tratar como aplicação mobile-first independente, sem misturar shell interno |

## Inventário de rotas por capacidade

- Acesso: login, recuperação, redefinição e erros 403/404/500.
- Dashboard: gerencial, financeiro, orçamentos e operacional.
- Clientes e veículos: listagem, cadastro, edição, detalhe e histórico/vínculos.
- Orçamentos: lista, criação, itens, revisão, detalhe e conversão.
- Ordens de serviço: lista, criação, cockpit, execução, peças, diagnóstico,
  checklist, evidências, adicionais, histórico, financeiro e fechamento.
- Operacional: estoque, produtos, XML e serviços.
- Financeiro: pagar, receber, caixa, caixas fechados, transferências, notas de
  compra e comissões.
- PDV, fiscal, agenda, relatórios, administração e configurações.

## Critério de implementação

Cada tela reconstruída deve reutilizar o App Shell e os componentes compartilhados,
ter uma ação primária clara, estados de carregamento/vazio/erro/sem permissão,
feedback acessível e comportamento responsivo. Quando o contrato de dados ainda
não estiver disponível, a tela deve indicar indisponibilidade/parcialidade; não
deve inventar indicadores, valores financeiros ou eventos.

## Próximos blocos de execução

1. Consolidar menu e telas de acesso.
2. Fechar clientes, veículos, orçamento e OS como fluxo contínuo.
3. Fechar pátio, estoque e financeiro.
4. Fechar PDV, fiscal, agenda, CRM, marketing, relatórios e administração.
5. Revisar rotas, remover placeholders relevantes, validar build/testes e atualizar este inventário.

## Checkpoint — 2026-09-07

- Removido o `ModulePlaceholder` das rotas de produção.
- Dashboards Financeiro, Orçamentos e Operacional, além de Checklists,
  Aprovações, Peças, Faturamento, Histórico, Gráficos, Aniversários e Suporte,
  agora usam uma superfície canônica responsiva com `PageHeader`, Tabler,
  acessos reais relacionados e estado explícito de dependência da API.
- Eliminados CTA genérico e KPIs com traços que sugeriam uma operação conectada.
- Gestão de Pátio reconstruída sem veículos fictícios; o fluxo de etapas continua
  visível, mas o quadro informa a indisponibilidade do read model tenant-safe.
- Corrigida corrupção estrutural em `patio.ts`, que continha uma cópia indevida
  das rotas da aplicação após o componente.
- Corrigidos contratos TypeScript que bloqueavam o build em Financeiro,
  Fornecedores, Orçamento, PDV e Relatórios.
- Removido fallback inseguro de tenant (`empresaId || 1`) em Contas e Caixa. Os
  dados da empresa usados em Caixa agora derivam do usuário materializado pela
  autenticação; sem vínculo válido, a UI entra em estado degradado.
- `npm run build`: concluído com sucesso em 2026-09-07.
- `npm test -- --watch=false --browsers=ChromeHeadless`: bundle de testes
  compilado; execução não iniciou porque o ambiente não possui binário Chrome.

Pendência real: as visões agregadas citadas acima permanecem dependentes dos
respectivos read models de backend. O frontend não fabrica totais nem registros
enquanto esses contratos não estiverem disponíveis.


## Checkpoint — 2026-09-08 — Acesso

- Login, recuperação e redefinição de senha foram alinhados ao contrato visual Tabler por meio do `NeriTechIcon` local.
- Removidos PrimeIcons das telas de autenticação e do shell de acesso, incluindo estados de carregamento, proteção, ajuda, sucesso, link inválido e visibilidade de senha.
- Preservadas as respostas anti-enumeração de e-mail, bloqueio por tentativas e invalidação de token expirado ou reutilizado.
- Mantidos alvos de toque de 44 px, foco visível, `aria-busy`, mensagens acessíveis e comportamento responsivo.


## Checkpoint — 2026-09-08 — Estados sistêmicos

- Telas 403, 404 e 500 reconstruídas em português com hierarquia, ações e orientação contextual.
- Removida a dependência de Angular Material do componente compartilhado de erro.
- Substituído o visual legado Matero por tokens NeriTech, Tabler, foco visível e layout responsivo.
- A tela 500 oferece nova tentativa real e retorno seguro à Home; 403 não sugere bypass de permissão.


## Checkpoint — 2026-09-08 — PDV seguro

- Rotas de PDV, listagem e detalhe deixaram de expor o fluxo legado que fabricava vendedores, formas de pagamento, consumidor padrão e numeração aleatória.
- Removida da navegação de produção a orquestração de baixa de estoque + criação financeira no navegador, que não garantia atomicidade.
- PDV agora apresenta estados responsivos e acionáveis com acessos reais a Estoque, OS e Contas a Receber.
- Dependência explícita: comando transacional backend para venda e read models tenant-safe para lista/detalhe.


## Checkpoint — 2026-09-08 — Tenancy em Agenda

- Removidos os fallbacks de tenant `7` e `empresaId: 1` dos fluxos de calendário e alertas.
- `ComunicacaoService` deixou de ler ou sobrescrever autoridade de empresa pelo `localStorage`.
- Requisições de mecânicos e comunicações agora dependem da sessão/interceptor e da autorização final do backend.
- A comunicação não recebe mais `empresaId` no payload; isso reduz risco de cross-tenant por manipulação do navegador.


## Checkpoint — 2026-09-08 — Calendário

- Calendário da oficina migrou de PrimeIcons para o wrapper Tabler oficial da NeriTech.
- Adicionados estados acessíveis de carregamento e falha com nova tentativa real, sem apagar dados silenciosamente.
- Ações de navegação, filtro, criação, cancelamento, geração de OS e edição preservam rótulo textual e alvo de toque.
- Removido PrimeIcon do diálogo de confirmação e mantida a grade responsiva de mês, semana e dia.


## Checkpoint — 2026-09-08 — Agenda e Alertas

- Central de Agenda e Alertas migrou toda a iconografia visível de Material Symbols/PrimeIcons para Tabler via `NeriTechIcon`.
- Cards de indicadores, busca, filtros, tabela, paginação, menu de ações e diálogo de comunicação agora usam a mesma linguagem visual.
- Mantidos rótulos textuais nas ações e estados vazios; removido `MatIconModule` da tela.


## Checkpoint — 2026-09-08 — Recepção e Check-in

- Criadas as rotas de produção `/recepcao/fila`, `/recepcao/check-in` e
  `/recepcao/check-in/:agendamentoId`, protegidas pelas permissões persistidas
  já existentes `GERAL_USUARIO` e `OS_INCLUIR`.
- A fila de recepção agora possui fluxo operacional, busca, data, origem,
  prioridade e situação, além de estado vazio/indisponível explícito sem
  fabricar clientes, veículos, tempos de espera ou indicadores.
- O check-in foi montado como wizard de quatro etapas: contexto, condição de
  chegada, planejamento e revisão. Foram incluídos origem, vínculo de
  agendamento, cliente, veículo, unidade, queixa, quilometragem, combustível,
  chegada, prioridade, responsável, previsão, checklist e observações.
- Validações aparecem junto aos campos e impedem avanço indevido. O resumo
  preserva o contexto durante o fluxo e a interface informa que check-in não
  cria orçamento, OS ou execução implicitamente.
- A gravação final permanece desabilitada até existir o comando documentado
  `POST /api/v1/receptions/check-ins`, com idempotência, autorização por unidade
  e resposta de próxima ação. A fila depende de read model paginado e tenant-safe.
- O modo demonstração local passou a materializar suas permissões no mesmo
  registro usado pelos guards, permitindo revisar rotas protegidas sem ampliar
  qualquer autoridade no build de produção.
- Especificações direcionadas usadas: `TELA-AUTO-REC-001` e
  `TELA-AUTO-REC-002` da documentação oficial.
- QA visual executado no navegador local em viewport mobile: fluxo completo até
  a revisão, mensagens obrigatórias, resumo, ordem de leitura e ação final
  indisponível foram observados. Breadcrumb duplicado foi removido e o resumo
  foi reposicionado após o formulário no mobile.
- Teste focado de rotas: `1 SUCCESS` em Chrome Headless 151.
- `npm run build`: concluído com sucesso após o lote.


## Checkpoint — 2026-09-08 — Relatório de Contas

- A tela de geração de contas foi reconstruída com períodos, recorte, ordem e
  filtro textual organizados por finalidade, além de ação primária única,
  carregamento e erro recuperável.
- A geração preserva a consulta financeira existente e abre apenas o documento
  retornado pela API. A UI não calcula saldo, valores, situação ou dados do
  relatório localmente.
- O filtro avançado é opt-in e limitado à consulta; não modifica títulos,
  baixas ou registros financeiros. `npm run build`: concluído com sucesso após
  o lote.


## Checkpoint — 2026-09-08 — Cadastro de Agendamento

- O cadastro e a edição de agendamento foram reorganizados em etapas claras de
  cliente/veículo, horário/origem e solicitação/observações, com cabeçalho e
  ações responsivas.
- Foram preservadas a busca de clientes ativos, a filtragem de veículos por
  cliente, validações de horário e data, criação, atualização, confirmação de
  cancelamento e permissões existentes. A tela continua sem criar OS, cobrança
  ou execução implicitamente.
- Removidos Angular Material e PrimeIcons deste fluxo; a iconografia passou ao
  wrapper Tabler local e o estado de carregamento segue explícito.
- `npm run build`: concluído com sucesso após o lote.

Classificação atual: fila e check-in estão **parciais por integração**. A UX e
os campos estão montados; a persistência e a exibição de atendimentos reais
continuam bloqueadas exclusivamente pelos contratos de backend descritos acima.


## Checkpoint — 2026-09-08 — Central de Inspeções

- A rota `/checklists-operacionais` deixou de usar a superfície genérica e agora
  possui uma Central de Inspeções própria, com abas exclusivas, busca, filtros de
  unidade, origem, severidade, inspetor, evidência e período, além de controle de
  densidade e ordem operacional explícita.
- A tabela já define as colunas canônicas de vínculo, veículo, template/versão,
  progresso, achados, atualização e próxima ação. Sem o read model, a tela usa
  um estado vazio honesto e não fabrica KPIs, severidade ou progresso.
- Criada `/checklists-operacionais/nova` com todos os campos de preparação:
  origem, vínculo, template, versão capturada, inspetor, revisor, prazo,
  prioridade, visibilidade e orientações. O resumo lateral preserva o contexto.
- O comando final permanece desabilitado até existir criação autoritativa que
  congele template/versão, valide atribuições e aplique idempotência. Criar uma
  inspeção não inicia execução nem preenche respostas implicitamente.
- Especificação direcionada usada: `TELA-AUTO-CHK-001 — Central de Inspeções`.
- Testes focados de checklist e autorização global: `6 SUCCESS` em Chrome
  Headless 151. O teste global foi corrigido para reconhecer os dois formatos
  de permissão aceitos pelo guard (`string` e `string[]`).
- `npm run build`: concluído com sucesso após o lote.

Classificação atual: Central e Nova Inspeção estão **parciais por integração**;
campos, filtros, estados, navegação e responsividade estão montados, enquanto
lista, resumo, drawer e criação real dependem das APIs canônicas de inspeções.


## Checkpoint — 2026-09-08 — Central de Aprovações

- A rota `/aprovacoes` deixou de usar a superfície genérica e passou a entregar
  uma central própria com os nove recortes operacionais documentados, busca,
  unidade, responsável, prioridade, validade e canal.
- Os quatro indicadores principais estão montados com definição acessível e
  valor indisponível (`—`) enquanto a projeção oficial não estiver conectada;
  nenhum total, valor comercial, SLA ou registro foi estimado no navegador.
- A fila define as colunas de orçamento/versão, cliente/veículo, estado,
  validade, responsável e próxima ação, com adaptação responsiva para retirar o
  cabeçalho tabular em tablet/mobile.
- Criada `/aprovacoes/registrar-decisao` com contexto da versão, método, data e
  hora, contato decisor, referência de evidência, decisão por item, resumo,
  follow-up, responsável e atestação explícita.
- Itens comerciais permanecem bloqueados até o snapshot íntegro da versão. A
  ação final não simula assinatura ou decisão do cliente e aguarda API
  idempotente, revisão de concorrência e `allowedActions` do backend.
- Foi preservado o código persistido legado `ORCAMENTO_DESCONTO_APROVAR` no
  guard; capacidades conceituais mais granulares não foram inventadas sem uma
  migração formal de permissões.
- Especificação direcionada usada: `TELA-AUTO-ORC-007 — Central de Aprovações`.
- Testes focados das rotas e autorização global: `6 SUCCESS` em Chrome Headless
  151. `npm run build`: concluído com sucesso após ajuste ao catálogo tipado de
  ícones Tabler local.
- A tentativa de QA visual em aba oculta encontrou a sessão corrente com
  permissão antiga e confirmou o redirecionamento para `/403`; a sessão do
  usuário não foi alterada para fabricar acesso.

Classificação atual: central e registro de decisão estão **parciais por
integração**. A UX, os campos, validações, estados e responsividade estão
montados; fila, KPIs, snapshot e confirmação dependem das APIs canônicas.


## Checkpoint — 2026-09-08 — Relatório de Produtos e Estoque

- O relatório deixou de usar cabeçalho azul legado, Material Icons, grade com
  aparência de planilha e prévia de impressão apoiada em dados não conectados.
- A geração foi reorganizada em escopo e ordenação, com busca, período,
  validação entre datas, limpeza de filtros, carregamento, erro recuperável e
  ação primária única.
- O PDF continua sendo produzido exclusivamente pela API existente; estoque,
  preços e totais não são recalculados ou fabricados no navegador.
- A tela passou a usar `PageHeader`, wrapper Tabler local, tokens neutros e
  composição responsiva com contexto operacional do documento.
- `npm run build`: concluído com sucesso após correção dos nomes aceitos pelo
  catálogo tipado de ícones.


## Checkpoint — 2026-09-08 — Relatório de Posição de Estoque

- A tela deixou de usar painel PrimeNG padrão, PrimeIcons e ações genéricas,
  adotando cabeçalho canônico, filtros compactos e uma ação primária dominante.
- Busca, situação e ordenação foram preservadas, com limpeza, carregamento,
  falha recuperável e acesso contextual ao estoque operacional.
- A geração continua delegada à API existente. Quantidades, reservas, valores e
  escopo de empresa/unidade não são recompostos pelo navegador.
- `npm run build`: concluído com sucesso após o lote.


## Checkpoint — 2026-09-08 — Relatório de Recebimentos

- O painel isolado com Material Icons e comandos fictícios de download,
  anotação, expansão e fixação foi substituído por uma análise financeira com
  período, unidade, agrupamento e intervalo personalizado validado.
- Resumo, série temporal e detalhamento permanecem sem valores até existir read
  model financeiro autoritativo; a tela não fabrica receita, ticket ou tendência.
- Exportação fica desabilitada e o acesso a Contas a Receber oferece a próxima
  ação operacional segura.
- `npm run build`: concluído com sucesso após o lote.


## Checkpoint — 2026-09-08 — Relatório de Pagamentos

- Pagamentos recebeu a mesma estrutura financeira de Recebimentos: período,
  intervalo personalizado validado, unidade, agrupamento e resumo compacto.
- Foram removidos painel genérico, menu PrimeNG e comandos demonstrativos de
  download, anotação, impressão, expansão e fixação.
- Totais, quantidade, média e série permanecem indisponíveis até a projeção
  financeira oficial; a próxima ação leva a Contas a Pagar.
- `npm run build`: concluído com sucesso após o lote.


## Checkpoint — 2026-09-08 — Receitas x Despesas

- Removida a agregação financeira feita no navegador a partir de três endpoints,
  incluindo classificação frágil de lançamentos por palavras da descrição.
- A tela agora define granularidade, período, conta e recorte previsto/realizado,
  mas aguarda uma projeção canônica para categorias, totais e resultado líquido.
- Gráfico, tabela e exportação não exibem nem persistem números derivados de
  fontes parciais; os relatórios de recebimentos e pagamentos permanecem como
  acessos relacionados.
- `npm run build`: concluído com sucesso após o lote.


## Checkpoint — 2026-09-08 — Fluxo de Caixa

- Removida a composição local de três fontes e a inferência insegura do saldo
  inicial a partir do primeiro lançamento retornado.
- A tela agora oferece granularidade, período e conta, resumo sem valores
  fabricados e estado explícito para o futuro read model canônico.
- A geração de PDF oficial foi preservada com carregamento, erro recuperável e
  abertura segura do arquivo retornado pelo backend.
- `npm run build`: concluído com sucesso após o lote.


#### Checkpoint — 2026-09-08 — Relatório de Vendas e Serviços

- A geração real de PDF e o payload existente foram preservados.
- O painel PrimeNG padrão foi substituído por cabeçalho canônico e formulário
  dividido entre período/origem e recorte operacional.
- Período mantém o formato contratado `dd/mm/aaaa`, com validação inline; busca
  textual passou a ser opt-in e os filtros são preservados após falha.
- PrimeIcons foram removidos e a composição passou a usar Tabler local, ação
  primária única e comportamento responsivo.
- `npm run build`: concluído com sucesso após o lote.


#### Checkpoint — 2026-09-08 — Respostas de Questionários

- Removidos cabeçalho azul legado, emojis, Material Icons, PrimeIcons e blocos
  que apresentavam ausência de respostas como indicadores numéricos zerados.
- A nova consulta organiza questionário, visualização, período, unidade e canal,
  com validação de datas e composição responsiva.
- Total, taxa de resposta, NPS, perguntas e comentários aguardam read model
  autoritativo; a interface não confunde indisponibilidade com resultado real.
- `npm run build`: concluído com sucesso após o lote.


#### Checkpoint — 2026-09-08 — Uso do Sistema

- A listagem real de auditoria e seus filtros foram preservados, com migração
  de Angular Material para `PageHeader` e o wrapper Tabler local.
- Removidos indicadores de cadastros, alterações e exclusões que eram inferidos
  por palavras encontradas na descrição dos eventos, sem classificação oficial.
- Carregamento, erro, vazio, busca, período, funcionário, OS e paginação
  continuam disponíveis sem ampliar o escopo retornado pelo backend.
- `npm run build`: concluído com sucesso após o lote.


#### Checkpoint — 2026-09-08 — Relatório de Clientes

- Consulta, filtros, tabela, indicadores retornados e geração Jasper foram
  preservados sem alterar os contratos existentes.
- O cabeçalho próprio foi substituído pelo `PageHeader` canônico e toda a
  iconografia visível migrou de Angular Material para o wrapper Tabler local.
- Estados de carregamento, erro, vazio, limpeza e aplicação de filtros mantêm
  rótulos textuais e ações recuperáveis.
- `npm run build`: concluído com sucesso após o lote.


#### Checkpoint — 2026-09-08 — Assinatura e Faturamento

- Removidos preços, promoção, benefícios e IDs de produto Stripe hardcoded no
  frontend, além do seletor de planos com aparência de landing page.
- A tela agora apresenta somente plano, status, valor, próxima cobrança e trial
  retornados pela API, com loading, falha recuperável e estados semânticos.
- O portal de faturamento oficial foi preservado como ação primária e aberto com
  isolamento de contexto; dados completos de cartão não são solicitados na UI.
- O contrato atual ainda recebe o identificador da empresa materializado na
  sessão. A remoção desse parâmetro da URL exige ajuste coordenado no backend,
  que deve continuar como autoridade final de tenant.
- `npm run build`: concluído com sucesso após o lote.


## Checkpoint — 2026-09-08 — Peças, Reservas e Consumo

- `/pecas-movimentacao` agora possui central própria com abas de necessidades,
  reservas, separação, consumo e histórico, além de busca e filtros por unidade,
  origem, situação e local de estoque.
- A listagem explicita origem, peça/local, quantidades solicitada, disponível e
  reservada, expiração, situação e próxima ação, sem recalcular saldo no browser.
- Criada `/pecas-movimentacao/preparar-reserva` com origem OS/orçamento,
  vínculo, unidade, serviço, peça, quantidade, unidade de medida, local,
  disponibilidade, política, validade e observações.
- Consulta, reserva e consumo permanecem semanticamente separados. O comando
  final aguarda disponibilidade autoritativa, TTL, versão e API idempotente; não
  permite sugerir overselling ou confirmar reserva fictícia.
- Especificação usada: `TELA-AUTO-EST-006 — Reservas para Orçamento e Ordem de Serviço`.
- Teste focado das rotas Home: `1 SUCCESS`; testes de Agenda e autorização
  global: `6 SUCCESS`, ambos em Chrome Headless 151. `npm run build`: concluído
  com sucesso durante o lote.

Classificação atual: central e preparo de reserva estão **parciais por
integração**; campos, estados, navegação e responsividade estão montados.


## Checkpoint — 2026-09-08 — Faturamento Operacional

- `/faturamento-operacional` agora possui uma fila própria com recortes de OS
  liberadas, cobranças preparadas, pagamentos pendentes, fiscal pendente e
  concluídas, com filtros por origem, unidade, situação, vencimento e responsável.
- A estrutura deixa claros origem, cliente, elegibilidade, cobrança, vencimento,
  fiscal e próxima ação, mas não compõe valores, títulos, saldos ou documentos
  no navegador.
- Criada `/faturamento-operacional/preparar-cobranca` com origem, vínculo,
  unidade, pagador, vencimento, canal, responsável e observações, além de um
  snapshot explicitamente aguardando o backend.
- Ação final permanece desabilitada: conclusão operacional não gera cobrança,
  liquidação nem fiscal implicitamente. Geração depende de elegibilidade,
  total autoritativo, regras de vencimento, idempotência e permissões.
- Testes focados de rotas e autorização global: `6 SUCCESS` em Chrome Headless
  151. `npm run build`: concluído com sucesso após o lote.

Classificação atual: fila e preparação de cobrança estão **parciais por
integração**; UX e campos estão montados, enquanto projeção e comandos dependem
dos contratos financeiros oficiais.


## Checkpoint — 2026-09-08 — Histórico Operacional

- `/historico` deixou de usar a superfície genérica e agora oferece uma linha do
  tempo operacional com busca, unidade, categoria, origem e intervalo de datas.
- A UX explicita a natureza imutável dos eventos, a fonte canônica, snapshots
  somente leitura e correções compensatórias, sem sintetizar eventos locais.
- O estado de integração descreve a projeção federada, links de origem,
  paginação por cursor, permissões e fontes parciais/artifacts indisponíveis.
- Especificações direcionadas usadas: `TELA-AUTO-HIS-001` e
  `TELA-AUTO-HIS-003`.
- Testes focados de rota e autorização global: `6 SUCCESS` em Chrome Headless
  151. `npm run build`: concluído com sucesso após o lote.

Classificação atual: linha do tempo está **parcial por integração**; filtros,
estados, navegação e responsividade estão montados, enquanto eventos reais e
snapshots aguardam a projeção canônica tenant-safe.


## Checkpoint — 2026-09-08 — Análises e Gráficos

- `/graficos` agora possui tela analítica própria com seleção de domínio,
  período, unidade e comparação com o período anterior.
- A tela torna definição, freshness, timezone da unidade e escopo autorizado
  pré-requisitos explícitos para qualquer série, tendência, valor ou tabela.
- Os painéis de gráfico e detalhamento usam estados de indisponibilidade
  honestos: nenhum KPI, série, comparação ou dimensão é fabricado no frontend.
- Referência direcionada usada: `TELA-AUTO-REL-002 — Relatórios Operacionais /
  OS / Orçamentos`.
- Testes focados de rota e autorização global: `6 SUCCESS` em Chrome Headless
  151. `npm run build`: concluído com sucesso após o lote.

Classificação atual: análises e gráficos estão **parciais por integração**;
controles, estados e responsividade estão montados, enquanto projeções e
definições analíticas canônicas aguardam o backend.


## Checkpoint — 2026-09-08 — Central de Suporte

- `/suporte` deixou de usar a superfície genérica e agora apresenta tópicos de
  ajuda, contexto seguro e acompanhamento de solicitações em estado explícito
  de integração pendente.
- Criada `/suporte/nova-solicitacao` com assunto, categoria, prioridade,
  referência, descrição e anexo preparado, incluindo validações locais e aviso
  para não compartilhar senha, token ou dados de pagamento.
- O envio fica desabilitado enquanto o canal oficial não validar destino,
  anexos, retenção e autorização; a UI não direciona dados a um canal incerto.
- Testes focados de rotas e autorização global: `6 SUCCESS` em Chrome Headless
  151. `npm run build`: concluído com sucesso após o lote.

Classificação atual: central e solicitação de suporte estão **parciais por
integração**; campos, estados, navegação e responsividade estão montados,
enquanto tickets e anexos reais dependem do canal oficial.


## Checkpoint — 2026-09-08 — PDV

- As quatro rotas do PDV deixaram de usar a superfície genérica: entrada,
  lista de vendas, nova venda de balcão e detalhe de venda possuem UX própria.
- A nova venda possui contexto de cliente/operador, busca de item, carrinho,
  observações e resumo persistente; checkout permanece indisponível sem
  snapshot versionado, disponibilidade, preço e pagamento autoritativos.
- Lista e detalhe deixam explícitos filtros, segregação de OS, escopo de PII,
  estoque, pagamentos e fiscal sem inventar vendas, cliente padrão, número,
  saldo ou confirmação de pagamento.
- Especificações direcionadas usadas: `TELA-AUTO-PDV-001`,
  `TELA-AUTO-PDV-002` e `TELA-AUTO-PDV-003`.
- Testes focados de rotas e autorização global: `6 SUCCESS` em Chrome Headless
  151. `npm run build`: concluído com sucesso após o lote.

Classificação atual: UX do PDV está **parcial por integração**. Todos os
contextos e campos estão montados; criação, checkout, pagamento, estoque,
fiscal e leitura de vendas dependem do contrato transacional e dos read models
tenant-safe do backend.


## Checkpoint — 2026-09-08 — Home e Aniversários

- Dashboards Financeiro, de Orçamentos e Operacional deixaram de usar a
  superfície genérica. Cada contexto agora explicita os indicadores monitorados,
  a origem necessária e os próximos fluxos conectados, com valor indisponível
  enquanto não houver read model oficial; não há total, tendência, aging ou
  projeção calculado no navegador.
- A tela de Aniversários passou a ter filtros de período, unidade, canal e
  consentimento, estrutura responsiva da lista de contato e preparo de
  comunicação. Dados pessoais, resultado e disparo continuam bloqueados até a
  projeção tenant-safe validar escopo, preferência, opt-out, retenção e canal.
- Foi removido o helper residual de rotas que ainda carregava `ModuleWorkspace`;
  não há rota de produção ativa apoiada nessa superfície.
- Referência visual usada: composição de dashboard e comportamento responsivo de
  `DESIGN.md`. Não foi localizada especificação `TELA-*` local para esses quatro
  painéis; a lacuna permanece registrada e não bloqueou a reconstrução visual
  reversível.
- Testes focados de rotas e autorização global: `6 SUCCESS` em Chrome Headless
  151. `npm run build`: concluído com sucesso após o lote.

Classificação atual: os quatro painéis estão **parciais por integração**. UX,
campos, navegação, privacidade e responsividade foram montados; indicadores e
dados reais aguardam projeções e canais autoritativos do backend.


## Checkpoint — 2026-09-08 — Home Gerencial

- O dashboard gerencial conectado preserva a consulta oficial, valores,
  carregamento, falha, links e contexto de sessão já existentes.
- A iconografia visível foi migrada de PrimeIcons para o wrapper Tabler local
  `NeriTechIcon`, incluindo atualização, estados, métricas, alertas e atalhos.
  O indicador continua vindo exclusivamente do backend; nenhuma métrica ou série
  foi introduzida no navegador.
- `npm run build`: concluído com sucesso após a migração.


## Checkpoint — 2026-09-08 — Dashboard Administrativo

- O painel administrativo foi reconstruído com cabeçalho canônico, carregamento
  com skeleton, erro recuperável, atualização explícita e grade responsiva de
  assinaturas e uso da plataforma.
- Foram preservadas a rota, o guard `GERAL_CONFIG_SISTEMA` e a única consulta
  agregada administrativa. A interface não revela oficinas, clientes, veículos
  ou assinaturas individuais, nem calcula MRR ou totais no navegador.
- A falha de leitura deixou de depender de log no console e passou a orientar a
  nova tentativa, sem alterar contexto de acesso.
- `npm run build`: concluído com sucesso após o lote.
