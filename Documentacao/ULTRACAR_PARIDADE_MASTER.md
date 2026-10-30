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
