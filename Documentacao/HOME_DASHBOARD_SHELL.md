# NERITECH AUTO — Início, Dashboard e Application Shell

## Contrato funcional

- A rota canônica de Início é `/home`; `/dashboard` permanece como alias de compatibilidade.
- As quatro visões internas são Padrão, Gerencial, Financeiro e Orçamento. A troca de visão não cria outra rota.
- A arquitetura oficial do menu e dos módulos não deve ser alterada durante refinamentos visuais.
- O shell ativo é `AdminLayout` + `Sidebar` + `Sidemenu` + `Header`. Verificar referências antes de modificar ou remover componentes de navegação.

## Homologação visual sem backend

- `FrontEnd/src/environments/environment.ts` mantém `uxPreview: true` para desenvolvimento.
- `FrontEnd/src/environments/environment.prod.ts` mantém `uxPreview: false` para produção.
- `StartupService` fornece o menu canônico de preview quando `uxPreview` está ativo, sem depender da API de menu.
- `DashboardService` fornece dados demonstrativos no mesmo modo. A tela identifica explicitamente esses dados como demonstrativos.
- O preview permite validar navegação, hierarquia visual, responsividade e estados de frontend; não comprova contratos de backend, tenancy, permissões ou cálculos reais.
- Dados demonstrativos nunca devem ser usados como fonte de produção.

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

- [ ] O frontend abre e permite navegar em modo de homologação sem backend disponível.
- [ ] O aviso de dados demonstrativos aparece somente com `uxPreview` habilitado.
- [ ] Receita e despesas são visualmente distinguíveis e a legenda corresponde às cores do gráfico.
- [ ] A navegação mantém estados ativos legíveis, expansão previsível, recolhimento, tooltips e foco visível.
- [ ] Menu e rotas respeitam a arquitetura oficial.
- [ ] Build Angular e pipeline aprovados antes de declarar homologação concluída.
- [ ] Homologação visual não é confundida com homologação funcional de backend.
