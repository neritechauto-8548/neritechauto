# NERITECH AUTO — Matriz de paridade visual e funcional (branch `master`)

## Escopo e regra de evidência
Este documento registra as referências visuais recebidas e a implementação própria da NERITECH AUTO. Fotografias, marcas, textos proprietários e código-fonte de terceiros não são copiados. **Código publicado não equivale a teste executado nem a fidelidade homologada.**

| Referência recebida | Rota do sistema | Estado | Arquivos-base |
| --- | --- | --- | --- |
| `00 - LOGIN.png` (1792 × 859 aprox.) | `/auth/login` | Interface implementada; homologação pendente | `FrontEnd/src/app/routes/sessions/login/login.{html,scss,ts}` |
| `00 - RECUPERAR SENHA.png` (1792 × 859 aprox.) | `/auth/recover` | Interface implementada; homologação pendente | `FrontEnd/src/app/routes/sessions/recover/recover.{html,scss,ts}` |

## AUTH-001 — Login

**Observação visual:** divisão aproximadamente 50/50; fundo grafite no formulário à esquerda; fotografia de oficina à direita com degradê azul na parte superior e verde inferior; formulário compacto, links azuis, botão verde-vivo, rodapé legal.

**Implementação:** layout split-screen responsivo, tipografia compacta, wordmark NERITECH próprio, imagem já presente em `FrontEnd/public/images/login.png`, fundo e sobreposição cromática, formulário funcional com validação, alternar senha, lembrar e-mail, envio antiduplo clique, estados de erro, links para recuperação, teste grátis, ajuda e privacidade.

**Contrato backend real:** `POST /auth/login` com `{email, senha}`. O campo visual "Usuário" explica que o identificador **atualmente é o e-mail**; o cadastro não possui coluna `login` distinta. Não oferecer autenticação por username arbitrário sem alteração específica e validação de isolamento multiempresa.

**A implementar/verificar:** comparação visual por captura de navegador nas mesmas dimensões, imagem e recorte reais do ambiente de deploy, CI Angular, comportamento em 320/768/1440, zoom 200%, teclado, verificação de sessão e tenant na integração.

## AUTH-002 — Recuperação de senha

**Observação visual:** mesmo shell, título e link para área de acesso, campos `E-mail` e `Login`, ajuda para login esquecido, ação solicitar recuperação, central de ajuda, política de privacidade.

**Implementação:** mesmo shell, e-mail validado, submissão ao backend `POST /auth/recover-password`, resposta anti-enumeração, 429/503/offline/timeout, confirmação genérica apenas após resposta real, link de retorno e ajuda interativa que explica o identificador atual.

**Diferença intencional e explícita:** referência possui dois campos separados (`E-mail` e `Login`). A NERITECH atual só possui e-mail como identificador real; portanto não foi criado campo `Login` sem efeito no servidor. Qualquer paridade de dois identificadores exige migração de dados, regras de unicidade por tenant e especificação de API; isso não está comprovado pelas imagens.

**Segurança preservada:** token opaco com hash armazenado e validade de 30 minutos; recuperação com resposta genérica; limitação de requisições; sem sucesso fictício quando o provedor de e-mail está indisponível.

## Checklist de verificação
- [x] Identificar componentes, rotas, backend e testes existentes.
- [x] Atualizar HTML, SCSS e componente de recuperação na `master`.
- [x] Preservar contratos de autenticação e telas de redefinição de senha.
- [x] Adicionar testes unitários focados para links e ajuda.
- [ ] Executar testes Angular e backend (ainda sem execução comprovada nesta alteração).
- [ ] Confirmar deploy e captura visual em produção ou homologação.
- [ ] Medir diferenças de layout/pixels e ajustar imagem, recorte e responsividade.
- [ ] Homologar o fluxo real com envio SMTP de teste, backend e empresa ativa.
- [ ] Decidir se a NERITECH terá username separado do e-mail.

**Branch de trabalho:** `master`. Não efetuar merge automático para `main` ou `feature/develop`.

## Próximas imagens
Registrar uma linha por nova tela, com todos os estados visuais (listagem, edição, modais, erros), regras comprovadas, impactos de API, testes e divergências. Comparar primeiro com implementações existentes e **não** criar duplicatas.

---

## Lote 02 — HOME / Menu global (imagens de 30/10/2026)

**Arquivos de referência recebidos:** `00 - INICIO.png`, `00.1 - INICIO.png`, `00.2 - INICIO.png`, `01 - Menu.png`, `01.1 - Menu.png`, `01.1 - Menud.png`, `01.1 - Menup.png`, `01.2 - Menu.png`, `01.2 - Menu(1).png`, `01.12 - Menu.png`, `012 - Menu.png`, `013 - Menu.png`, `014 - Menu.png`, `01.3 - Menu superior.png`, `01.3 - Menu superior(1).png`, `01.4 - Menu superior.png`, `01.4 - Menu superior(1).png`.

**Observações comprovadas nas imagens:**
- Topbar branca acima da sidebar, navegação horizontal Aplicativos, Empresas, Usuários, Integrações, Informativos, Master, busca por atalho Ctrl+K, acesso a peças, ajuda, notificações e conta. Não assumir que menus superiores implicam novas APIs.
- Sidebar expandida ~270 px, recolhida ~72 px, check-in/out, abas ativas azul-claro, ícones lineares, grupos com acordeão, rodapé Recolher.
- Submenus observados: Orçamentos & Serviços (Checklist, Diagnósticos, Orçamentos, Ordens de serviço); Clientes (Gerenciar clientes, Histórico de veículos); Gestão de recursos (Cadastros, Seguradoras, Fornecedores, Transportadoras); Peças & Estoque (Produtos, Consulta de peças, Kits, Cotações, Compras, Entregas & Devoluções, Inventários, Correções de estoque, Transferências); Financeiro (Contas, Pagar & Receber, Movimentação, Boletos, Conciliação); Fiscal (Nota fiscal, Manifestação, Sintegra, NCM, Inutilização).
- Home: saudação, aviso, dois widgets Financeiro e Ordens de serviço; botões de atualizar, informações, personalizar cartões e ícones de ajuda. Um exemplo do UltraCar mostra ticket mensal em R$ 3.037,96 — esse valor **não deve** ser copiado para a API nem usado como dado real.

**Código implementado na branch master:**
- `FrontEnd/public/data/menu.json`: árvore reorganizada para refletir as capturas, com rotas existentes preservadas; funções não implementadas permanecem visualmente indisponíveis. O grupo adicional **Mais funcionalidades** conserva 18 rotas anteriores não presentes nas capturas, evitando regressão: 47 de 47 rotas habilitadas anteriormente continuam no menu, sem duplicação.
- `FrontEnd/src/app/theme/header/*`, `sidebar/*`, `sidemenu/*`, `admin-layout/*`: topbar global, menu expandido/recolhido, link real para suporte, estilos e ícones ajustados. Botões de check-in/out **indisponíveis** até existir contrato de marcação de presença e servidor, para não criar registros falsos.
- `FrontEnd/src/app/routes/dashboard/dashboard.{html,scss,ts}`: layout com dois widgets brancos, indicadores reais do endpoint `GET /api/dashboard`, atualização real, saudação por perfil autenticado e seleção visual de componentes (estado local em memória); as quatro visões anteriores continuam acessíveis.
- `FrontEnd/src/app/routes/dashboard/dashboard.spec.ts`: testes adicionados de renderização com dados zero, atualização, personalização e preservação das quatro visões. **Ainda não executados neste ambiente.**

**Mapeamento de dados:** contas a receber pendentes, contas a pagar pendentes, títulos a receber vencidos, receitas/despesas do período, ticket médio geral, veículos atrasados e clientes ativos já existem no `DashboardDTO`. O endpoint **não fornece** ticket médio mensal separado, recebíveis do dia, vencimentos a pagar específicos ou a mesma semântica de "entregas atrasadas" da referência. São lacunas, não zeros verificados.

**Diferenças e pendências obrigatórias de homologação:**
- [ ] Executar `yarn test --watch=false --browsers=ChromeHeadless` com testes focados e `yarn build`.
- [ ] Validar visualmente screenshots em 1920 px, 1440 px, 768 px e 320 px, além de zoom 200%.
- [ ] Comparar teclado, leitor de tela, rolagem e comportamento do menu recolhido/expandido.
- [ ] Definir contrato e testes de check-in/out para colaboradores; ações permanecem desabilitadas.
- [ ] Criar backend para indicadores ainda inexistentes (somente após definir semântica, filtros e multiempresa).
- [ ] Projetar consulta de peças externa, importações, boletos e conciliação sem links falsos.
- [ ] Personalização dos widgets hoje é apenas local à página, sem persistência entre sessões.
- [ ] A busca visualmente chamada "Busca Inteligente" hoje pesquisa **somente páginas e módulos permitidos**, não tem busca semântica por registros.
- [ ] Os screenshots originais ainda não estão armazenados como assets no repositório; são referências recebidas no chat.

**Status:** código de layout/integração enviado para o GitHub; equivalência visual, testes e operações indisponíveis ainda não homologados.

---

## Lote 03 — Topbar, Perfil, Notificações, Aplicativos e CRM (capturas 30/10/2026)

**Referências:** `01.5 - Usuario.png`, `01.6 - Notificacoes configuracao.png`, `01.6 - Notificacoes.png`, `01.7 - Notificacoes configuracao.png`, `01.8 - atalho.png`, `01.9 - atalho.png`, `01.9 - sobre.png`, `01.10 - Plano.png`, `11 aplicativos.png` a `11.5 aplicativos CRM.png`, `01.12 - Menu(1).png`, `012 - Menu(1).png`, `013 - Menu(1).png`, `014 - Menu(1).png`, `Screenshot_1.png`, `00 - INICIO(1).png`.

**Regras e UX visualmente observáveis:**
- Perfil: resumo de conta, opções de tema claro/auto/escuro, grupos Conta, Matriz, Sistema, Ajuda; Minha conta, Sair, Configuração, Atalhos (CTRL+/), Central de ajuda, Sobre a plataforma e link de suporte. Não copiar e-mail, nome, número de cliente ou versão do concorrente mostrados nas capturas.
- Notificações: painel lateral direito, duas categorias (Sistema / Solicitações de agendamento), Ver histórico, configurar, estado vazio, Recarregar, Marcar todos como lidos. Modal de configurações: som, balão e permissão do navegador; links de teste, aviso de preferências pessoais.
- Atalhos: CTRL+/; expandir/recolher com [ e ]; Ctrl+K; Shift+N; Shift+O; Ctrl+, e Ctrl+.; Escape. Apenas atalhos implementados constam da modal NERITECH (não copiar atalhos sem integração).
- Sobre: branding NERITECH, site e suporte próprios; versão do concorrente NÃO reutilizada.
- Master: cartão do plano atual. Não inventar nome, nível ou preço sem contrato de assinatura.
- Aplicativos: mega-menu com trilha Sistema e CRM, grupos de módulos e navegação para rotas reais; nenhuma rota arbitrária para recurso não implementado.
- CRM: submenu separado e tela Oportunidades com 5 cartões (aniversariantes, inativos em venda, inativos em OS, próximas revisões, serviços futuros), filtros e acesso à Comunicação. Contagens dependem de consultas específicas ao histórico da empresa.

**Código alterado:**
- `theme/widgets/user-button.{ts,html,scss}`: menu e janelas, atalhos e navegação de conta/suporte; preferências de tema reaproveitam SettingsService.
- `theme/widgets/notification-button.{ts,html,scss}`: drawer, categorias, histórico, recarregar, preferências locais por usuário, solicitação de permissão de navegador, teste de balão e som original; integrações de leitura persistida continuam em `NotificacaoService.findMine` / `marcarComoLida`.
- `theme/header/header.{ts,html,scss}`: Aplicativos/CRM, subpainel Master e fechamento por clique fora/Escape.
- `theme/admin-layout/admin-layout.{ts,html,scss}`: sidebar contextual CRM usando a mesma sessão e rotas. Menu operacional é ocultado apenas nas rotas CRM.
- `routes/cliente/crm/crm.{ts,html,scss}`: painel de oportunidades com busca e filtros de categoria; cadastro e comunicação anteriores são preservados abaixo/do menu CRM.
- `theme/widgets/notification-button.spec.ts` e `theme/header/header.spec.ts`: casos adicionais de comportamento, **sem execução neste ambiente**.

**Limitações explicitamente preservadas:**
- O backend só fornece notificações por `GET /v1/comunicacao/notificacoes/me` e `PATCH /{id}/ler`, não fornece histórico de solicitações separado nem endpoint de preferências; classificação de agendamento é feita por categoria do registro e o histórico usa lista paginada. A operação “Marcar todos” aplica-se apenas aos registros carregados; não foi inventada uma API de operação em lote.
- Preferências (som/balão/permissão do navegador) ficam no armazenamento local por usuário, não sincronizadas entre aparelhos nem substituem push servidor. A aplicação agora detecta notificações novas durante o polling (após a primeira carga), podendo mostrar balão, reproduzir um som original e solicitar uma notificação nativa quando o navegador concedeu permissão. Push remoto com a aba fechada ainda depende de service worker, subscription e endpoint próprios.
- Métricas CRM dependem de consultas de vendas, OS, revisões e comunicação filtradas por empresa. Os cinco valores são exibidos como indisponíveis (`—`), não como zero/contagens falsas, e envio em massa permanece desabilitado.
- A página de assinatura é uma rota real existente; preço, plano contratado e versão do produto só devem ser apresentados após leitura de dados verificados.
- As imagens não confirmam regras internas de criação de campanhas, atalhos de cadastro por fora das telas observadas ou API de preferências.

**Pendências de homologação:**
- [ ] Build Angular e testes dirigidos completos, com evidências.
- [ ] Capturas nas mesmas dimensões de cada referência e diff visual.
- [ ] Teste com autenticação, permissões e isolamento por tenant.
- [ ] API de oportunidades CRM e ações de campanha (com opt-in, controle de consentimento e auditoria).
- [ ] Sincronização de preferências de notificação em backend, se desejada.
- [ ] API de marcação de todas as notificações em lote e indicador global de não lidas quando múltiplas páginas.
- [ ] Implementar CRM Campanhas/Templates/Visão geral somente após contrato de negócio.

---

## Acesso paliativo — demonstração sem login (30/10/2026)

**Objetivo:** permitir visualização da interface da branch `master` sem JWT, sem liberar dados ou operações do sistema real.

**Como entrar:** abrir `/auth/login` e escolher **Acessar demonstração sem login** ou acessar `/home?uxPreview=1` no mesmo domínio do frontend. A prévia é explicitamente iniciada apenas por esse parâmetro na URL; não altera o login normal. **Sair da prévia** recarrega `/auth/login`, sem o parâmetro.

**Limites e proteção:**
- `environment.prod.ts`: ativa `uxPreview` exclusivamente quando `?uxPreview=1` está presente no carregamento inicial (temporário no frontend).
- Guards visuais já existentes permitem renderizar rotas para demonstração, mas não criam token, sessão ou privilégios de servidor.
- `previewReadonlyInterceptor` é o primeiro bloqueio da cadeia HTTP e **interrompe qualquer requisição a API, interna ou externa, de qualquer método**. Permite apenas GET de recursos estáticos do próprio frontend sob `/data/`, `/i18n/`, `/assets/` ou `/images/`. Responde com 503 local para APIs, sem atravessar a rede.
- A homepage demonstra indicadores fictícios já existentes, identificados claramente como demonstração; outras telas exibem estado indisponível caso dependam de backend. Nenhuma gravação ou emissão fiscal é simulada como concluída.
- Banner fixo informa **Modo demonstração · dados fictícios ou indisponíveis · operações bloqueadas**, incluindo link de saída.
- API Java e suas políticas JWT, multiempresa e permissões **não foram alteradas**.
- Se uma rota for recarregada sem `?uxPreview=1`, volta a exigir login. Para abrir uma rota específica direto na prévia, anexar `?uxPreview=1` ao endereço.
- Este modo pode ser aberto por qualquer visitante que conheça o parâmetro, portanto **nenhum dado real, segredo, endpoint administrativo ou conteúdo reservado deve ser embutido nos assets estáticos**.

**Validação necessária:**
- [ ] Executar os testes de `preview-readonly-interceptor.spec.ts`.
- [ ] Testar `/auth/login` e `/home?uxPreview=1` no deploy final em janela anônima.
- [ ] Confirmar no DevTools Network que todas as chamadas `/api/` são bloqueadas localmente, sem HTTP ao servidor.
- [ ] Testar navegação via menu, dashboard, CRM, logout da prévia e recarga sem parâmetro.
- [ ] Retirar esta exceção temporária de `environment.prod.ts` e o botão público antes da disponibilização comercial da versão autenticada.

---

## Lote 04 — Clientes e Veículos (capturas de 30/10/2026)

### Imagens analisadas

`01 - cliente.png`, `FireShot Capture 140`, `142`, `143`, `144`, `Screenshot_1(1)`, `Screenshot_2` a `Screenshot_13`.

### Fluxos efetivamente observados

1. Workspace de Clientes com 3 abas (Clientes, Veículos, Indicadores), breadcrumb, busca e botão de ação verde.
2. Listagem de clientes com cabeçalho, colunas configuráveis (Cliente, CPF/CNPJ, Categoria, Tipo de pessoa, Criação), filtros avançados em drawer e cadastro completo em drawer.
3. Filtros vistos: CPF/CNPJ, Nome fantasia, Contato, Tipo de pessoa, Gênero, Placa, Situação. A API real implementa busca textual, CPF/CNPJ, tipo, status e intervalo de cadastro. **Contato, gênero e placa NÃO são filtros remotos comprovados na API**.
4. Veículos: tabela de modelo, placa, marca, ano e vínculo de cliente, filtro (cliente, marca, modelo, placa) e cadastro com identificação, dados gerais, motor e complementos.
5. Indicadores: dia (30d), semana (90d), mês (12m), ano (5a); distribuição por gênero, médias de idade e séries temporais. Captura referencia nascimentos cadastrados.
6. Etiquetas: impressão, filtros, opções de página, margens, fontes, bordas, personalização visual e impressão/PDF pelo navegador.
7. Importação CSV: escolher domínio, validar arquivo, revisar e só depois confirmar. Não usar importação cega por múltiplos POSTs sem transação e política de duplicidade.
8. Personalização global de relatórios com cabeçalho e identidade da empresa, prévia e configuração persistida por empresa/matriz — **ainda não implementada**.

### Implementação do lote no código

- `FrontEnd/src/app/routes/cliente/clientes-nav.ts`: navegação contextual reutilizada em Clientes, Veículos e Indicadores.
- `cliente/cliente/cliente.{ts,html,scss}`: identidade/layout ajustados, filtros em drawer, consulta server-side, controle de ordenação e visibilidade das colunas (preferência apenas neste navegador), impressão de etiquetas via documento isolado seguro com texto HTML escapado. Cadastro reutiliza `CadastroCliente` existente no drawer PrimeNG, sem duplicar DTO ou API. Exporta modelo CSV **sem afirmar que importa**.
- `cliente/cliente/cliente.service.ts`: prévia respeita filtros avançados simulados em memória, sem alterar cliente real.
- `cliente/indicadores/indicadores.{ts,html,scss}`: nova aba; para bases de até 1.000 registros no intervalo, lê as páginas completas com a API autenticada, calcula idades e categorias apenas com nascimento válido e renderiza barras de registro agrupado. Com mais de dez páginas de cem registros, bloqueia estatísticas parciais e pede API agregada tenant-scoped. Gráficos só são exibidos quando a coleta está completa.
- `veiculo/veiculo/veiculo.{ts,html,scss}`: layout da listagem, busca e filtro por placa/marca/modelo/cliente sobre o conjunto retornado de `/v1/veiculos`; cadastro reutilizado no drawer, ações anteriores preservadas.
- `veiculo/cadastro-veiculo/cadastro-veiculo.ts`: suporte ao modo drawer e eventos de salvar/cancelar; permanece com guard de permissão no frontend e backend.
- `backend/cliente/repository/ClienteSpecification.java`: corrige descarte de filtros avançados quando `busca` também está preenchida (agora são condições independentes e combinadas por AND). Mantém predicado de tenant obrigatório.
- `FrontEnd/public/data/menu.json`: entrada Indicadores em Clientes, sem apagar Histórico de veículos.
- Testes focados adicionados para filtros, colunas, veículos e cálculo sem valores inventados. **Não executados neste ambiente.**

### Pendências e diferenças declaradas

- [ ] Build Maven/Angular e execução de testes E2E e dirigidos; conferir eventual regressão.
- [ ] Captura da implementação a 1440 px comparada pixel a pixel com as referências.
- [ ] Filtros reais por contatos, gênero e placa no endpoint de Clientes; não fazer pós-filtragem de apenas uma página.
- [ ] Indicadores agregados por banco para qualquer tamanho de base; frontend paginado de até 1.000 é provisório.
- [ ] Impressão de etiquetas: modo multi-página/banco inteiro, inclusão de endereço e regras de papel configurável (atualmente só a página carregada, nome e documento).
- [ ] Importação CSV transacional com serviço de validação, revisão, prevenção de duplicidade e auditoria; o botão disponível atualmente baixa apenas o modelo.
- [ ] Personalização global de relatórios por empresa, uso de marca e identidade autorizadas, log de emissão, prévia PDF; não implementada.
- [ ] Integração externa de placa/chassi (Fraga/Ultra Vision) requer contratação/licença e API própria. Não copiar dados, fotos ou credenciais da referência.
- [ ] Campos avançados de motor/transmissão do cadastro de veículo precisam de entidades e DTOs versionados; atualmente persistem somente os campos suportados pelo contrato real.
- [ ] Homologação multiempresa, 403/422/409, responsividade, teclado, foco e acessibilidade WCAG 2.2 AA.

### Segurança e fidelidade

Nenhum endpoint está aberto anonimamente. O acesso temporário `uxPreview=1` só mostra interface e mantém chamadas HTTP de backend bloqueadas pelo interceptor de prévia. Os números e nomes do UltraCar não são persistidos ou reaproveitados. A API existente segue sujeita a JWT, permissões do backend e isolamento por empresa.

**Status do lote:** execução de código entregue para commit; equivalência total e execução de testes ainda não confirmadas.

---

## Lote 05 — Cadastro de clientes (PF/PJ/contatos) e criação de veículo

**Referências enviadas:** `Screenshot_10(1).png` a `Screenshot_23.png`, com continuidade do lote 04 (filtros de clientes e veículos, indicadores e modais laterais).

### Identificação da referência

- **Clientes → Gerenciar clientes → Cadastro:** painel à direita com altura da janela, cabeçalho, rolagem independente e botões Cancelar/Cadastrar fixos no rodapé.
- Pessoa física: CPF, identificação estrangeira, nome, país, nascimento, RG, estado civil, gênero e observações.
- Pessoa jurídica: CNPJ, documento estrangeiro, razão social, nome fantasia, país, tipo contribuinte, inscrições municipal/estadual e observações.
- Informações complementares: profissão (PF), apólice, limite de crédito, origem do relacionamento, site e locadora de veículos.
- Informações legais: órgão público, retenção ISS, retenção IRRF/CSLL, alerta LGPD em OS, autorização LGPD triestada (sem decisão, sim, não).
- Contato: diálogo com nome obrigatório, gênero, nascimento, grupo principal e tipo/valor de contato; lista de contatos no cadastro.
- **Clientes → Veículos → Novo veículo:** drawer à direita, escolha obrigatória de cliente; busca por placa/chassi; ficha com marca, modelo, ano, combustível, chassi, renavam, KM, dados do motor e complementares.

### Implementação efetiva em `master`

- Evolução em `cadastro-cliente.html/.scss/.ts`: interface em seções com rodapé fixo quando `modalMode`, dados complementares, flags legais e contatos. Formulário completo existente continua sendo reutilizado fora do drawer; `salvar()` mantém create/update/autorização e CPF/CNPJ.
- `Cliente`, `ClienteRequest`, `ClienteResponse`, `ClienteMapper`: 14 propriedades persistidas no mesmo agregado; no campo `autorizacaoLgpd`, o valor `null` significa **sem decisão**; nunca converter para `true` por padrão. Estados legais só registram preferência/cadastro; não representam prova jurídica de consentimento ou execução fiscal.
- `ContatoCliente` e DTOs/mapper: nome, gênero (M/F), nascimento e grupo principal, preservando o telefone/e-mail existente. Reaproveita endpoints de contatos já autorizados e isolados por empresa.
- Migration Flyway `V290__cliente_dados_complementares_legais.sql` adiciona as colunas, sem excluir o histórico nem alterar chaves/índices anteriores.
- `cadastro-veiculo.html/.scss/.ts`: fluxo de criação no drawer com cliente primeiro e demais campos depois; bloqueia salvar dados inválidos e impede salvar enquanto a placa encontrada corresponder a veículo existente.
- Nova consulta **somente interna** `GET /v1/veiculos/interno/placa/{placa}` em `VeiculoController`, `VeiculoService` e `VeiculoService` (Angular), com JWT/permissão de leitura e `TenantAccess.requireCurrent()` + `findByEmpresaIdAndPlaca`. A consulta externa legada `/placa/{placa}` não foi alterada; **o novo drawer não a invoca**.
- Cadastro por chassi e integrações Fraga/Ultra Vision: **não implementados**, sem contrato/credenciais. Nenhuma resposta é fabricada. A tela orienta o preenchimento manual.
- Modo de demonstração `uxPreview`: operações reais e consulta interna não são permitidas, preservando o interceptor de bloqueio HTTP.

### Hipóteses e diferenças

- "Grupo de contato principal" foi mapeado para `grupoPrincipal`, um dado de cadastro distinto do antigo `principal` (canal preferido). Confirmar semântica no processo de negócio antes de impor exclusividade.
- Campos de motor avançados (cilindrada, número de válvulas, código do motor, potências) e complementares (sistemas de freio, tração, injeção etc.) ainda não têm DTO/persistência próprios nesta entrega. A tela informa a limitação; não simula salvar dados.
- Dados de pessoa jurídica com retenção fiscal são *configuração declaratória*, sem disparar impostos ou NF-e.
- Botão de lookup por chassi informa indisponibilidade; consulta técnica externa depende de autorização, contrato e integração próprios.
- O cadastro completo de cliente continua com outras abas para endereço, documentos e veículos; no modal foi priorizada a organização observada nas imagens.
- Filtros avançados e indicadores do lote 04 preservados; não duplicar as telas.

### QA e status

- Teste unitário de mapper e valores legais triestados adicionado.
- Pendente: executar Maven/Angular, testes de criação/edição, cenários tenant A/B (consulta por placa), validação de migração em banco de homologação, verificação de sessão/permissão/erro, captura visual 1440/768/320, teclado e WCAG 2.2 AA.
- **Não declarar 100% de equivalência** sem testes executados, integração e medição visual.

**Critério de homologação:** dados persistidos e recuperados ao editar; consentimento `null/true/false` preservado; criação com cliente correto; 404 para placa não existente, sem consulta externa; duplicidade protegida no servidor.

---

## Lote 06 — Revisão visual após rejeição de fidelidade (30/10/2026)

O usuário relatou que a implementação anterior **não estava fiel** às imagens. Esta revisão confronta novamente os anexos de 30/10 com os componentes existentes, corrige proporções e preenche o contrato técnico de veículos. **Referência visual não é código-fonte do fornecedor.**

### Referências dimensionais — viewport onde a medida é observável

| Tela | Medida observada | Diretriz aplicada |
|---|---|---|
| Cadastro de cliente no drawer (Screenshot_13(2)) | Drawer começa em x=565/1149; largura aprox. **584 px**; header de **57 px**, rodapé de **48 px**; cartões com recuo de 24/25 px; inputs **32 px**; gap de coluna **12 px** | PrimeNG dialog width 585, body sem segunda rolagem, header + subtitle, footer fixo; form grid 2 cols, labels 11 px |
| Listagem de clientes (FireShot 140) | Tabela ocupa superfície principal; linha de cerca de **51 px**, headers ~**43 px**, comandos acima da tabela | lista de clientes com 5 colunas padrão, categoria como seleção que abre cadastro, badges de tipo, fonte 11 px, CTA verde |
| Colunas de clientes (01-cliente) | Modal **672×489**, cabeçalho ~**58 px**, faixa azul de **42 px**, duas colunas 50/50 separadas por borda | busca em ambas as colunas; seleção e remoção; ordem com botões de mover; coluna Cliente fixa; configuração persistida no navegador |
| Veículos (FireShot 142) | Listagem em linhas ~**84 px**; thumbnails ~**65×65**; detalhes de modelo/placa/marca/cliente | tabela e paginação `Carregar mais` com lista cumulativa |
| Indicadores (FireShot 143) | 2×3 indicadores (seis cartões); linhas de série temporal; dois gráficos inferiores (gênero/idade) | seis KPIs e quatro séries em SVG, distribuição e barras; valores reais por empresa |
| Cadastro de veículo (FireShot 144) | Uma coluna larga de cartões; dentro, grids de duas colunas, hero de foto, blocos obrigatórios/técnicos/opcionais | cliente primeiro; consulta interna por placa; imagem do modelo quando existir; dados gerais, motor, complementares e rodapé fixo |
| Etiquetas (prints 1–14) | Modal aprox. **970×535** com controles à esquerda e visualizador escuro à direita; personalização aprox. **755×612** | acordeões, margens, fonte, alinhamento, espaçamento, orientação; print isolado com controle de campos; salvamento local das preferências |

### Integração e regras

- V291 adiciona os 18 campos manuais de veículo ao banco, à entidade e aos DTOs: geração, frota, tipo, acessórios, cilindrada, quantidade de válvulas/cilindros, nome/código do motor, potências CV, linha, entrada de ar, freios, tração, injeção, configuração e freios dianteiro/traseiro.
- O mapeador MapStruct usa o mesmo contrato; conservar um construtor retrocompatível para consumidores do DTO `VeiculoResponse` e `VeiculoRequest` que ainda não fornecem tais campos.
- `cadastro-veiculo` envia agora todos os campos opcionais, inclusive direção, câmbio, ar, portas, antes omitidos do `requestBody`.
- A pesquisa interna por placa continua com isolamento de `empresaId` e sem consultar a API externa legada.
- A configuração de etiquetas aplica-se **apenas aos clientes da página carregada**, e o controle Salvar PDF abre a impressão do navegador (opção Salvar como PDF); não inventar PDF gerado por API.
- Filtros de estado, cidade, marca, modelo, aniversário, recomendações foram explicitamente **desabilitados**, pois dependem de consulta específica de toda a base e não podem ser simulados. Tipo, situação, gênero e período de cadastro filtram os dados carregados.
- Dados identificadores da empresa (logo, razão social, CNPJ, endereço) no cabeçalho de relatórios não foram ativados sem leitura do cadastro real da empresa.

### Controle de qualidade / impedimentos

- Revisão do contrato e do diff é **estática**; sem acesso a um navegador autenticado de homologação e sem execução local do Angular, não afirmar match pixel a pixel, WCAG ou responsividade aprovada.
- O workflow de backend do commit `e3319264` falhou; o log apontou anotação `@Transactional` duplicada na consulta de placa (corrigida neste lote) **e dezenas de incompatibilidades pré-existentes de `RelatorioImpressaoController`, `AdminDashboardService` com DTOs/entidades financeiras**. Não tratar CI verde sem que esses erros adicionais sejam resolvidos.
- Necessário verificar Angular `ng build`, Maven, migrar banco de teste, comparar screenshots em 1366/768/320, rodar testes e validar locatário/permissões.
- Nenhum logotipo, código-fonte ou imagem do concorrente é incorporado. Marcas e dados exibidos continuam NERITECH e da oficina autenticada.

### Checklist de aceite visual

- [ ] Um drawer apenas, scroll único; cabeçalho e rodapé imóveis em viewport 599 px
- [ ] Cadastro físico/jurídico exibe os mesmos campos e requer os mesmos obrigatórios da captura
- [ ] Modal de colunas 672×489 e listagem de clientes com as proporções conferidas no navegador
- [ ] Indicadores 6 KPIs + série temporal + dois painéis inferiores com dados reais
- [ ] Cadastro técnico persistido sem perda dos novos campos e sem enriquecimento externo fictício
- [ ] Etiquetas acompanham ajustes de margem, fonte e grid; impressão testada
- [ ] Build Angular/Maven e deploy aprovados

---

## Lote 07 — Revisão por campo dos anexos Screenshot_5(2)–Screenshot_23(1), 30/10/2026

### 1. Margens e conteúdo de etiquetas

| Campo | Comportamento | Padrão |
|---|---|---|
| Todos os lados (mm) | Aplica valor aos quatro lados; aceita 10,00 em formato brasileiro | 10,00 |
| Superior / Inferior / Esquerda / Direita | Controle independente da margem da folha | 10,00 |
| Alinhamento | Esquerda, centralizado, direita; aplica na impressão | Esquerda |
| Texto (pt) | Fonte configurável entre 7–18 pontos | 9 |
| Caracteres do nome | Trunca nome de maneira determinística | 40 |
| Colunas do endereço | Apresentado e bloqueado até consulta/concatenação autorizada de endereços | 2 |
| Altura automática / altura fixa | Controle habilita altura quando o modo automático está desligado | Automática |
| Borda | Opção liga/desliga borda de cada etiqueta | Ligada |
| Personalização | Nome, CNPJ, endereço/contatos reais da empresa autenticada, data/hora, divisor, tamanho e cor | Nome/CNPJ/Emissão |
| Logo | Carrega da API real quando a empresa possui logo; caso não haja, mostra inicial corporativa; visual sem imagem falsificada | Ligada |
| Usuário que emitiu | Apresentado, mas desativado até a identificação auditável do emissor | Desativado |

**Não alegar paridade com os relatórios do concorrente.** A personalização atual vale para etiquetas de clientes; a funcionalidade multi-relatório não está integrada. Emissão de PDF via diálogo de impressão do navegador, opção Salvar como PDF.

### 2. Filtro avançado de clientes

Sete campos na ordem e grid 2 colunas da captura: CPF/CNPJ, Nome fantasia, Contato, Tipo de pessoa, Gênero, Placa, Situação. O backend recebe filtros independentes `nomeFantasia`, `contato`, `sexo`, `placa` com subconsultas por `empresaId` para contatos e veículos. O botão Resetar limpa também esses filtros, preservando as regras de autorização.

### 3. Cadastro de cliente

- PF: CPF, documento estrangeiro, nome, país, nascimento, RG, estado civil, gênero, observações;
- PJ: CNPJ, documento estrangeiro, razão social, país, nome fantasia, tipo de contribuinte, observações; contribuição ICMS sugerida por padrão somente ao alternar para PJ sem valor previamente selecionado;
- Complementares PF: profissão, apólice, crédito, origem, site, locadora;
- Complementares PJ: inscrição municipal/estadual, apólice/crédito, origem em linha completa, site e locadora;
- Legais: órgão público, ISS, IRRF/CSLL, alerta LGPD, autorização LGPD;
- Contatos: botão abre segundo drawer de 585px, campos nome*, sexo, nascimento, grupo principal e inclusão do meio de contato (tipo/valor); confirmação exige ambos. Para edição, reutiliza registro existente; para criação, salva lista em memória até API confirmar cadastro.

### 4. Criação do veículo

- Etapa 1: cliente vinculado, buscado por nome/documento na API da empresa, listagem rolável e ação Cadastrar cliente abre drawer aninhado; após confirmação, vincula automaticamente o ID persistido.
- Etapa 2: consulta de veículo antes de cadastro; provedor Fraga exposto como integração pendente, não simulado. Busca interna por placa responde encontrado/não encontrado/erro/carregando, e existe bloqueio contra duplicidade.
- Etapa 3: Dados gerais em pares: placa/chassi, marca/modelo, ano fabricação/ano modelo, combustível/cor, geração/renavam, média de KM/KM, frota/tipo, acessórios. Ano fabricação limita as opções reais de ano/modelo da API.
- Etapa 4: Dados do motor: cilindrada, válvulas, cilindros, nome/código, potências, número do motor.
- Etapa 5: Dados complementares: direção, transmissão, linha, entrada de ar, freios, tração, injeção, configuração, freios dianteiro/traseiro, portas, ar-condicionado e observações.
- NERITECH Vision por imagem: botão desativado e indicado como integração futura, sem fingir dados externos ou reconhecimento de imagem.

### Revisão e QA ainda obrigatórios

- [ ] Compilar Angular e Spring Boot após este commit (pipeline anterior já falhava por outros módulos).
- [ ] Homologar painel 585px em 599/768/1440 e zoom 200%; validar teclado, foco, scroll e máscara do modal aninhado.
- [ ] Conferir todos os campos em comparação visual **lado a lado nas mesmas dimensões**.
- [ ] Validar consultas de contato/placa, gênero e nome fantasia com tenants A/B e registros reais.
- [ ] Verificar impressão de etiquetas com margens 10,00 e altura, além de logotipo e cabeçalho de empresa autorizada.
- [ ] Completar integrações licenciadas Fraga/visão e relatórios em lote quando houver contrato/API; não simular sucesso.


---

## Lote 08 — Histórico de veículos e modal de relatórios (30/10/2026)

**Referências anexadas:** Screenshot_24.png, FireShot Capture 146, Screenshot_1(3), Screenshot_2(2), Screenshot_3(2). As capturas são referência visual; funcionalidades que elas não demonstram foram implementadas a partir do modelo real do sistema, sem afirmar equivalência operacional com o concorrente.

**Navegação:** `/cliente/historico-veiculos` (lista), `/cliente/historico-veiculos/:id` (detalhes). A entrada existente do menu Clientes foi direcionada à nova rota; a lista de cadastro de veículos permanece no módulo Veículos.

**Listagem:** título, breadcrumb, busca por cliente/modelo/placa, ordenação alfabética A/Z e por placa, filtros de pesquisa, tabela Cliente/Placa, carregamento adicional por página de 10 e diálogo de colunas. Cliente é obrigatório e não removível; preferências de colunas salvas localmente por empresa, sem modificar segurança da API.

**Detalhes:** identificação do cliente (nome/documento), veículo (placa, marca/modelo, ano, combustível e cilindrada), pesquisa de OS e itens, ordenação por OS/entrada, selecionador Fechados ou Fechados / Autorizados, vazio real caso não haja OS, paginação e visualização do PDF. O botão de impressão depende de OS existentes, sem conteúdo fictício.

**Relatórios:** modal com categorias de referência; apenas Histórico de veículos é executável neste lote. As três opções operam sobre dados efetivos: somente OS autorizadas, exibir identificador do produtivo (se houver) e exibir valores reais. PDF gerado no Spring Boot com OpenPDF, com erros HTTP e limite de volume, não um arquivo simulado. A busca da lista também restringe a emissão quando há termo pesquisado. Não foi implementada conexão com outros relatórios que as imagens apenas exibem como itens de navegação.

**API:** `GET /v1/historicos-veiculos`, `GET /v1/historicos-veiculos/:id`, `GET /v1/historicos-veiculos/:id/ordens`, `GET /v1/historicos-veiculos/relatorio`. Sem novas tabelas: leitura de Veiculo, Cliente, OrdemServico, ItemOSServico e ItemOSProduto; predicados de tenant obrigatórios. O backend impede uso de IDs de outros clientes/veículos e busca a OS pelo `empresaId` do JWT autenticado, nunca por empresaId fornecido no parâmetro.

**Limitações verificáveis:** nomes de comissionados não estão resolvidos por esta tela (exibe ID do responsável, não inventa nome); o relatório PDF limita 100 veículos e 700 OS e retorna 422 ao exceder; as demais categorias do modal são informativas até integração autorizada. A imagem do concorrente não fornece contrato de impressão, logo há uma implementação NERITECH própria, não afirmação de correspondência de template Jasper.

**QA:** teste unitário do controlador cobre empresa ausente, ID estrangeiro, paginação inválida e consulta vazia; build Angular/Maven, testes integrados A/B e comparação pixel a pixel permanecem dependentes de resultados da CI e homologação visual da tela renderizada.
