/** Catalogo da Visao geral: ids sao um contrato entre frontend e backend. */
export type WidgetType = 'grafico' | 'indicador' | 'listagem';
export interface WidgetLayout { id: string; tipo: WidgetType; colunas: number; }
export interface WidgetCatalogItem {
  id: string; nome: string; descricao: string; icone: string;
  /** Nao indica integracao operacional com o concorrente, mas fonte de dados confiavel no DTO local. */
  dadosDisponiveis: boolean;
}
export interface WidgetGroup {
  id: string; nome: string; descricao: string; itens: WidgetCatalogItem[];
}
export const WIDGET_GROUPS: WidgetGroup[] = [
  {id:'os',nome:'Indicadores de OS',descricao:'Componentes relacionados aos dados de ordens de serviço e indicadores operacionais.',itens:[
    {id:'ordens',nome:'Ordens de serviço',descricao:'Acompanhe indicadores e gráficos relacionados às ordens de serviço.',icone:'clipboard-list',dadosDisponiveis:true},
    {id:'financeiro-os',nome:'Financeiro',descricao:'Visualize dados financeiros relacionados às ordens de serviço.',icone:'wallet',dadosDisponiveis:true},
    {id:'gerencial',nome:'Gerencial',descricao:'Monitore informações gerenciais para tomada de decisão.',icone:'chart-no-axes-combined',dadosDisponiveis:true},
    {id:'padrao',nome:'Padrão',descricao:'Use uma configuração padrão para acompanhar a visão geral.',icone:'layout-grid',dadosDisponiveis:true},
  ]},
  {id:'faturamento',nome:'Faturamento',descricao:'Acompanhamento de faturamento, ticket médio e passagem.',itens:[
    {id:'faturamento-geral',nome:'Visão geral de faturamento',descricao:'Acompanhe o faturamento consolidado no período selecionado.',icone:'chart-area',dadosDisponiveis:true},
    {id:'ticket-medio',nome:'Ticket médio',descricao:'Visualize a evolução do ticket médio por tipo de OS.',icone:'circle-dollar-sign',dadosDisponiveis:true},
    {id:'faturamento-passagem',nome:'Faturamento x passagem x ticket médio',descricao:'Relação entre faturamento, passagem de veículos e ticket médio.',icone:'chart-column',dadosDisponiveis:true},
  ]},
  {id:'financeiro',nome:'Financeiro',descricao:'Gráficos gerenciais do financeiro.',itens:[
    {id:'fluxo-caixa',nome:'Fluxo de caixa',descricao:'Acompanhe o fluxo de caixa por período, situação e contas.',icone:'wallet',dadosDisponiveis:false},
    {id:'custo-vendas',nome:'Custo x vendas',descricao:'Comparativo entre vendas e custo no período selecionado.',icone:'badge-dollar-sign',dadosDisponiveis:false},
    {id:'despesas-receitas',nome:'Despesas x receitas',descricao:'Compare despesas e receitas no período selecionado.',icone:'scale',dadosDisponiveis:true},
    {id:'faturamento-despesas',nome:'Faturamento, despesas e receitas',descricao:'Visão consolidada de faturamento, despesas e receitas no período.',icone:'calculator',dadosDisponiveis:true},
  ]},
  {id:'operacional',nome:'Operacional',descricao:'Comparativos operacionais por período.',itens:[
    {id:'comparativo-seguradoras',nome:'Comparativo de seguradoras',descricao:'Compare seguradoras por valor ou quantidade de vendas.',icone:'shield-check',dadosDisponiveis:false},
    {id:'custo-fixo-variavel',nome:'Custo fixo x variável',descricao:'Acompanhe a relação entre custos fixos e variáveis no período.',icone:'landmark',dadosDisponiveis:false},
  ]},
  {id:'produtivo',nome:'Produtivo',descricao:'Indicadores de produção e movimentação de veículos.',itens:[
    {id:'pecas-servicos',nome:'Peças x serviços',descricao:'Comparativo de compras de peças e serviços por período.',icone:'wrench',dadosDisponiveis:false},
    {id:'passagem-veiculos',nome:'Passagem de veículos',descricao:'Acompanhe a passagem de veículos pela granularidade selecionada.',icone:'car-front',dadosDisponiveis:true},
    {id:'desempenho-equipe',nome:'Desempenho da equipe',descricao:'Compare a produção de orçamentistas e técnicos no período.',icone:'users-round',dadosDisponiveis:false},
  ]},
  {id:'clientes',nome:'Clientes',descricao:'Indicadores de relacionamento e retenção de clientes.',itens:[
    {id:'clientes-sem-movimentacao',nome:'Clientes sem movimentação',descricao:'Clientes sem ordem de serviço no período, para contato e retenção.',icone:'user-round-x',dadosDisponiveis:false},
  ]},
];
export const WIDGET_ITEMS=WIDGET_GROUPS.flatMap(group=>group.itens);
export const DEFAULT_LAYOUT:WidgetLayout[]=[
  {id:'financeiro-os',tipo:'listagem',colunas:1},
  {id:'ordens',tipo:'indicador',colunas:1},
];
export const WIDGET_PRESETS = [
  {id:'operacional',nome:'Visão Operacional',icone:'settings',descricao:'KPIs de ordens de serviço, passagem de veículos, peças x serviços e ticket médio.',ids:['ordens','passagem-veiculos','ticket-medio']},
  {id:'financeira',nome:'Visão Financeira',icone:'wallet',descricao:'Recebíveis e pagáveis do mês, fluxo de caixa, despesas x receitas e faturamento.',ids:['financeiro-os','despesas-receitas','faturamento-geral']},
  {id:'gerencial',nome:'Visão Gerencial',icone:'chart-no-axes-combined',descricao:'Indicadores estratégicos, custo x vendas, custo fixo x variável e visão consolidada.',ids:['gerencial','faturamento-geral','ordens']},
  {id:'comercial',nome:'Visão Comercial',icone:'briefcase-business',descricao:'Foco em ticket médio, comparativo de seguradoras e relação faturamento x passagem.',ids:['ticket-medio','faturamento-passagem','ordens']},
] as const;
