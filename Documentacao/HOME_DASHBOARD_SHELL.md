# NERITECH AUTO — Início, Dashboard e Application Shell

## Contrato funcional

- A rota canônica de Início é `/home`; `/dashboard` permanece como alias de compatibilidade.
- As quatro visões internas são Padrão, Gerencial, Financeiro e Orçamento. A troca de visão não cria outra rota.
- A arquitetura oficial do menu e dos módulos não deve ser alterada durante refinamentos visuais.
- O shell ativo é `AdminLayout` + `Sidebar` + `Sidemenu` + `Header`. Verificar referências antes de modificar ou remover componentes de navegação.

## Homologação visual sem backend

- `FrontEnd/src/environments/environment.ts` habilita `uxPreview` somente quando solicitado explicitamente por `?uxPreview=1` no ambiente de desenvolvimento; por padrão permanece desativado.
- `FrontEnd/src/environments/environment.prod.ts` mantém `uxPreview: false` para produção.
- `StartupService` fornece o menu canônico de preview quando `uxPreview` está ativo, sem depender da API de menu.
- `DashboardService` fornece dados demonstrativos no mesmo modo. A tela identifica explicitamente esses dados como demonstrativos.
- O preview permite validar navegação, hierarquia visual, responsividade e estados de frontend; não comprova contratos de backend, tenancy, permissões ou cálculos reais. Gestão de Pátio pertence ao menu como **uma tela interna** e continua indisponível na prévia até haver uma rota Angular real. Sem iframe ou link para sistema externo.
- Dados demonstrativos nunca devem ser usados como fonte de produção.

## Shell e navegação
- O menu lateral exibe o branding oficial e mantém o link para `/home` sem recarregamento completo.
- O controle da topbar para abrir/recolher aparece no menu recolhido ou em telas com menu sobreposto; a sidebar expandida mantém seu controle próprio.
- O breadcrumb utiliza `Início` e caminho contextual na mesma linguagem tipográfica do sistema.
- Labels da navegação são traduzidos usando o catálogo i18n; quando uma chave ainda não possui tradução, a interface exibe um nome legível no lugar de `menu.*`.
- A busca global pesquisa os nomes traduzidos e resolve caminhos de submenu completos. Os resultados não alteram a arquitetura nem criam rotas.
- O menu e o cabeçalho devem ser validados com navegação por teclado, estado ativo, recolhimento/expansão e largura mobile.

## Autenticação e recuperação
- A tela de login mantém composição em duas seções desktop, com painel ilustrado à esquerda e formulário à direita; no mobile prioriza o formulário.
- Campos de login têm rótulos, validação de e-mail, erro de conexão distinto de credencial inválida e estado de envio.
- Recuperação de senha usa feedback genérico que não revela se o endereço está cadastrado, valida o e-mail e bloqueia envios simultâneos.
- Estas telas permanecem fora do Application Shell autenticado.

## Identidade visual

- O Dashboard usa identidade própria NERITECH AUTO, com acabamento inspirado em produtos SaaS premium, sem copiar a identidade de outra marca.
- Receita: azul NERITECH. Despesas: âmbar. A paleta deve ser consistente entre gráfico e legenda.
- KPI principal: composição azul/índigo de alto contraste. Indicadores secundários usam acentos diferenciados para operação, conclusão e atenção.
- Fundos, bordas e sombras permanecem discretos; cores devem ter função semântica.
- Estados de carregamento, erro, vazio e preview devem ser distintos e acessíveis.

## Arquivos canônicos

- Dashboard: `FrontEnd/src/app/routes/dashboard/dashboard.ts`
- Template do Dashboard: `FrontEnd/src/app/routes/dashboard/dashboard.html`
- Estilos do Dashboard: `FrontEnd/src/app/routes/dashboard/dashboard.scss`
- Inicialização do menu: `FrontEnd/src/app/core/bootstrap/startup.service.ts`
- Serviço do menu: `FrontEnd/src/app/core/bootstrap/menu.service.ts`
- Shell: `FrontEnd/src/app/theme/admin-layout/`
- Sidebar: `FrontEnd/src/app/theme/sidebar/`
- Navegação: `FrontEnd/src/app/theme/sidemenu/`
- Barra superior: `FrontEnd/src/app/theme/header/`

## Critérios de aceite

- [ ] Validar no navegador que o frontend abre e permite navegar em modo de homologação sem backend disponível.
- [ ] Confirmar exibição da marca, controle de recolhimento/expansão e busca global em desktop e mobile.
- [ ] Confirmar login e recuperação, inclusive erros de validação e de conexão.
- [ ] O aviso de dados demonstrativos aparece somente com `uxPreview` habilitado.
- [ ] Receita e despesas são visualmente distinguíveis e a legenda corresponde às cores do gráfico.
- [ ] A navegação mantém estados ativos legíveis, expansão previsível, recolhimento, tooltips e foco visível.
- [ ] Menu e rotas respeitam a arquitetura oficial.
- [ ] Build Angular e pipeline completo aprovados para o commit final antes de declarar homologação concluída; sucesso do frontend isolado não comprova testes backend.
- [ ] Homologação visual não é confundida com homologação funcional de backend.

## Comportamento das visões

- **Padrão:** resumo da oficina com indicadores de operação, gráfico de receita/despesas, saúde da operação e alertas de atenção.
- **Gerencial:** faturamento, resultado, ordens em andamento, base de clientes e evolução financeira.
- **Financeiro:** faturamento, despesas, resultado, valores vencidos, carteira a receber/a pagar e série mensal.
- **Orçamento:** a tela mantém o tipo de visão oficial, mas o backend hoje oferece estados de Ordens de Serviço (abertas, autorizadas, finalizadas e canceladas) para essa seção. A interface deve identificá-los como **OS**, sem chamá-los de orçamentos nem apresentar uma taxa de conversão comercial não calculada sobre propostas reais. A futura integração com o módulo Orçamentos exige definição de fonte, eventos e denominadores antes da homologação.
- As abas atualizam o conteúdo da mesma rota, sem criar telas redundantes. Os dados vêm do serviço DashboardService; em preview, os números são demonstrativos.
- Os controles de aba expõem role="tab" e aria-selected para comunicar a seleção às tecnologias assistivas.

## Integridade semântica das métricas

- O cartão de OS concluídas no Home Padrão utiliza `fechadosMes` (finalizações do mês) e não `osConcluidas` (contagem geral), mantendo consistência entre valor e legenda.
- A seção de saúde da operação apresenta a quantidade de OS autorizadas no mês em lugar de converter estados de OS em um percentual denominado conversão de orçamentos.
- Os dados da visão Orçamento são provisórios quanto à fonte de domínio: a especificação de UI existe, mas os indicadores de propostas precisam de integração real de Orçamentos para homologação.
