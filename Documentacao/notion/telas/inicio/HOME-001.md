# TELA-AUTO-HOME-001 — Início / Dashboard

> **Cópia de consulta do Notion, gerada em 2026-10-14.** Fonte canônica: https://app.notion.com/p/3eb01fd65a9c81f9bf76d9c525335911. Não editar esta cópia como norma principal.
> **Última edição registrada no Notion:** 2026-10-14T17:13:17.268Z.


## A — CONTRATO EXECUTÁVEL CODEX — HOME-001 \| DECISÃO DE PRODUTO
**Vigência:** 14/10/2026. **Status:** especificação aprovada para implementação incremental, NÃO homologação do código. Em caso de divergência conceitual com rascunhos abaixo, prevalecem estas decisões, a norma D01–D11 de 03.01.04 e o contrato real conferido na `feature/develop`.
### Propósito e tela
Início — quatro dashboards. Única tela com modos Padrão, Gerencial, Financeiro e Orçamento; sem Ações Rápidas.
### Campos / tipos / dependências
modoDashboard \[enum, Padrão por default, obrigatório\]; periodoInicio/Fim \[data, fim\>=início, padrão mês atual\]; compararPeriodo \[boolean, não\]; empresa \[contexto autenticado, sem dropdown de Filiais\]; filtroConsultor/Equipe \[se capacidade\].
### Tabelas, filtros e ações
Padrão: OS, cliente, veículo, entrada, entrega prevista, responsável, situação, valor; Gerencial: série período, passagem, serviço, ticket, tempo; Financeiro: conta/título, vencimento, valor, recebido/pago, atraso; Orçamento: Nº, cliente, veículo, status, total, criado/autorizado. Cada visão 20/50/100, filtros server-side e links de consulta.
### Regras de negócio decididas
Fonte de cada KPI somente APIs proprietárias; ticket e conversão D10; contagem mês pelo timezone da empresa. Modal de ajuda explica fórmula; não mostrar número 0 para API indisponível. Não incluir CTAs de criar nas visões.
### API, ownership e persistência
GET /api/dashboard existente conforme controller; demais agregados a mapear ou implementar em versão real usando DTO unificado DashboardDTO, filtros data/mode; nunca dados mock em produção.
### Permissões e isolamento
GERAL_USUARIO ou capabilities de leitura do owner; dados financeiros só a FIN_VIS_\* real; cards restritos substituídos por estado 'Sem acesso'.
### Critérios de aceite testáveis
Comparar valor KPI com lista de origem no mesmo período; modos independentes, troca de data, empresa A/B, zero vs erro, 200% zoom e alternativa tabular dos gráficos.
**UX obrigatório:** UI-MASTER-001, sidebar expansível/recolhível, Tabler, estados carregando/vazio/filtrado/erro/offline/403/sucesso real, WCAG 2.2 AA, smartphone e desktop. Rotas reais devem ser reconciliadas com Angular; nunca inferir API implementada a partir deste documento.
> **Contrato de arquitetura e UX:** esta é a única tela canônica de Início, ScreenId `TELA-AUTO-HOME-001`, rota `/home`. As quatro páginas filhas são especificações das visões internas (Padrão, Gerencial, Financeiro e Orçamento), não telas/rotas independentes. Não criar Ações Rápidas. Aplicar o registro de tokens e shell de [04.01 — UI-MASTER-001](https://app.notion.com/p/97401fd65a9c822a9a4b017c39a08518); não usar ScreenIds do manifesto legado do MASTER DESIGN.
- **Módulo owner:** 🏠 Início
- **ScreenId:** `TELA-AUTO-HOME-001`
- **Rota:** `/home`
- **Natureza:** uma única tela física de dashboard
- **Visões internas:** Padrão, Gerencial, Financeiro e Orçamento
- **Design System:** UI-MASTER-001
## B — Detalhamento complementar
**Regra editorial:** preservar campos, tabelas e fluxos abaixo; aplicar decisões somente quando compatíveis com o contrato A, D01–D11 e APIs verificadas no repositório.
## Objetivo
Apresentar o estado do negócio de forma rápida, clara e acionável, sem transformar o dashboard em uma fonte transacional paralela.
## Estrutura correta
**Módulo → Tela → Visão → especificação da visão**
A tela é única. As quatro visões são estados/configurações internas da mesma tela e não representam quatro rotas nem quatro módulos.
## Estrutura UX/UI
Application Shell → Header → DashboardSelector → filtros globais → conteúdo da visão → freshness.
### Seletor de visão
Opções exatamente:
1. Padrão
2. Gerencial
3. Financeiro
4. Orçamento
A troca ocorre dentro de `/home`.
### Filtros globais
- Período
- Comparação
- Empresa: definida pela sessão autenticada, sem seletor de Filiais/unidades operacionais
Filtros específicos permanecem dentro da visão correspondente.
## Regras transversais
- Não existe Ações Rápidas.
- O Home consulta, consolida e encaminha.
- O módulo proprietário executa e altera.
- Drill-down sempre abre o owner correto.
- Erro, sem dados, parcial, stale e sem permissão são estados distintos.
- Valores financeiros exigem capability apropriada.
- Tenant/empresa, usuário e capability são revalidados no backend; não há unidade operacional.
- Desktop, tablet e mobile fazem parte da especificação.
- WCAG 2.2 AA.
## Contrato de dados
O Home utiliza snapshots/read models dos módulos proprietários. Não cria fatos transacionais paralelos.
## Contrato de implementação atual
### Frontend
- Rota canônica: `/home`.
- A composição visual do Home segue o **NERITECH AUTO Design System**: UX inspirado em produtos SaaS de referência, mas identidade própria da NERITECH.
- Hierarquia visual: contexto → visão → filtros → indicadores → performance → panorama operacional → leitura complementar.
- O Home usa quatro KPIs principais na visão Padrão: Receita, OS em andamento, OS concluídas e Ticket médio.
- A área principal prioriza evolução da operação e panorama da oficina, evitando excesso de cards e decoração.
- Componentes usam superfícies neutras, bordas leves, tipografia compacta, azul NERITECH como ação e ícones SVG outline sem biblioteca adicional na tela.
- A assinatura NERITECH é aplicada por linguagem visual, proporção, contexto automotivo e comportamento; não é uma cópia visual de Stripe.
- Entrada após autenticação: `/home`.
- `/dashboard` permanece somente como alias de compatibilidade.
- A tela física é única e possui exatamente quatro visões internas: Padrão, Gerencial, Financeiro e Orçamento.
- As quatro visões compartilham a mesma estrutura visual e os mesmos padrões de interação; muda apenas o contexto dos indicadores.
- Não existe Ações Rápidas.
- Filtros suportados no frontend: Hoje, Últimos 7 dias, Últimos 30 dias, Este mês e Personalizado; comparação por período anterior ou mesmo período do ano anterior. Não criar seletor de Filiais ou unidades operacionais; a empresa é resolvida pela sessão.
- A interface diferencia carregamento, erro técnico, sem permissão, dados parciais e nenhum dado disponível.
- A atualização preserva a visão atual e não grava senha nem usa o cliente como autoridade de tenant.
### Backend
- Endpoint canônico: `GET /api/v1/dashboards/home`.
- Aliases de compatibilidade: `GET /api/dashboard` e `GET /dashboard`.
- O tenant é resolvido pelo `TenantContext` preenchido a partir da sessão autenticada; o `empresaId` não é aceito como parâmetro de escopo do Dashboard.
- O backend recebe período, comparação e, para período personalizado, `startDate` e `endDate`.
- O histórico do gráfico é calculado a partir de agregações reais de financeiro e serviços; não são usados valores demonstrativos.
- O contrato retorna período aplicado, comparação, timestamp de geração e estado explícito de disponibilidade dos dados.
- Métricas financeiras e operacionais permanecem somente leitura; o Home não cria nem altera fatos transacionais.
- O backend rejeita filtros inválidos com HTTP 400 e solicitações sem tenant autenticado com HTTP 403.
### Validação
- Existe teste automatizado do serviço para período personalizado, período inválido e estado sem dados.
- A validação de build/deploy deve ser feita pelo pipeline disponível do repositório; indisponibilidade ou rate limit do provedor de CI não deve ser interpretado como falha funcional do Dashboard.
## Componentes principais
ApplicationShell, PageHeader, DashboardSelector, FilterBar, DateRangePicker, AuthenticatedCompanyContext, ComparisonSelect, KPICard, Chart, DataTable, StatusBadge, RefreshButton, DrillDown, Skeleton, EmptyState, ErrorState e ForbiddenState.
## Critérios de aceite
- `/home` abre na visão Padrão.
- As quatro visões permanecem na mesma tela.
- A rota não muda ao trocar de visão.
- Não existe quinta visão.
- Não existe Ações Rápidas.
- Nenhuma operação transacional é executada pelo Home.
- KPIs possuem definição, origem e freshness.
- Drill-down abre o módulo proprietário.
- Permissões são validadas no backend.
- Estados críticos são tratados separadamente.
## Visões desta tela
As especificações detalhadas das quatro visões ficam nas páginas filhas desta tela.
[📊 Visão 01 — Dashboard Padrão](https://app.notion.com/p/3eb01fd65a9c81dda1bef53d956bd345)
[📈 Visão 02 — Dashboard Gerencial](https://app.notion.com/p/3eb01fd65a9c81139f2feacee4156399)
[💰 Visão 03 — Dashboard Financeiro](https://app.notion.com/p/3eb01fd65a9c81c7a2e5c64566255d51)
[💵 Visão 04 — Dashboard Orçamento](https://app.notion.com/p/3eb01fd65a9c8116a33cc857985a8154)
## 18. Especificação detalhada da tela canônica
### Contrato visual
A tela deve preservar a mesma estrutura de shell nas quatro visões:
**Page Header → DashboardSelector → filtros globais → conteúdo da visão → atualização/freshness**.
O seletor é um controle único com quatro valores fixos:
- Padrão;
- Gerencial;
- Financeiro;
- Orçamento.
### Responsividade
**Desktop:** KPIs em grade; tabelas completas; gráficos lado a lado quando houver espaço.
**Tablet:** redução de colunas e empilhamento de gráficos.
**Mobile:** KPIs em uma coluna ou carrossel acessível; filtros em drawer; tabelas convertidas para lista/card; nenhuma tabela exige zoom horizontal da página inteira.
### Acessibilidade
- foco visível;
- ordem de tabulação coerente;
- labels acessíveis;
- gráficos com resumo textual;
- status não dependentes somente de cor;
- tooltip disponível por teclado;
- atualização não rouba foco;
- mensagens de erro anunciadas por tecnologia assistiva.
### Contrato de estado
Cada bloco recebe estado próprio. Um erro em Financeiro não deve transformar OS ou Orçamentos em erro.
### Contrato de origem
Cada bloco deve possuir owner explícito no código/read model. O frontend não pode calcular novamente métricas proprietárias quando o contrato exigir valor oficial do owner.
### Segurança
Valores financeiros, produtividade e demais indicadores sensíveis devem ser filtrados pelo backend. Ocultar componente no frontend não é autorização.
### Critérios finais
- quatro visões exatamente;
- uma única rota;
- zero mutações transacionais;
- filtros consistentes;
- estados independentes;
- freshness explícito;
- drill-down seguro;
- leitura mobile completa.
# Revisão técnica aprofundada — contrato de implementação v2
> **Status:** complemento técnico obrigatório. Esta especificação detalha contratos ainda insuficientes para implementação. Os nomes de tabelas, campos e rotas abaixo são propostas conceituais até serem comparados com o código e o banco existentes. Não criar tabelas duplicadas caso já exista entidade equivalente.
## 1. Decisões fechadas desta tela
- Rota única: `/home`.
- Visões permitidas: `STANDARD`, `MANAGERIAL`, `FINANCIAL`, `ESTIMATES`.
- Visão inicial: `STANDARD`.
- O seletor não cria novas rotas e não permite personalizar/criar visões.
- Não existe bloco “Ações rápidas”.
- A tela é read-only: nenhuma criação, edição, aprovação, cancelamento, baixa ou exclusão transacional.
- Toda navegação de detalhe abre o módulo proprietário.
- Falha em uma fonte não deve invalidar automaticamente os demais blocos.
- O backend deve autorizar cada métrica e cada campo sensível; ocultar a interface não substitui autorização.
## 2. Contrato de layout por breakpoint
<table header-row="true">
<tr>
<td>Região</td>
<td>Desktop ≥1280 px</td>
<td>Tablet 768–1279 px</td>
<td>Mobile \<768 px</td>
</tr>
<tr>
<td>Header</td>
<td>título à esquerda; atualizar e freshness à direita</td>
<td>título e ações com quebra controlada</td>
<td>título em cima; atualizar como ícone com nome acessível</td>
</tr>
<tr>
<td>Seletor</td>
<td>quatro opções em linha</td>
<td>quatro opções em linha ou controle segmentado rolável dentro do componente</td>
<td>controle segmentado acessível, sem rolagem horizontal da página</td>
</tr>
<tr>
<td>Filtros</td>
<td>período e comparação na mesma linha quando couber</td>
<td>filtros quebram em duas linhas</td>
<td>botão “Filtros”; drawer com aplicar e limpar</td>
</tr>
<tr>
<td>KPIs</td>
<td>grade de 4 colunas como padrão; quantidade real conforme largura</td>
<td>grade de 2 colunas</td>
<td>uma coluna ou duas apenas quando cada cartão mantiver legibilidade</td>
</tr>
<tr>
<td>Gráficos</td>
<td>dois por linha quando comparáveis</td>
<td>um por linha quando necessário</td>
<td>um por linha, resumo textual disponível</td>
</tr>
<tr>
<td>Tabelas</td>
<td>tabela completa</td>
<td>ocultar somente colunas secundárias documentadas</td>
<td>cartões/lista com os campos prioritários; sem scroll horizontal da página</td>
</tr>
<tr>
<td>Erro parcial</td>
<td>aviso junto ao bloco afetado</td>
<td>igual</td>
<td>aviso junto ao bloco afetado</td>
</tr>
</table>
**Comportamento dos filtros:** alterar um filtro marca os blocos dependentes como atualizando, preserva os valores anteriores enquanto aguarda a resposta e aplica o novo conjunto de dados somente após resposta válida. Botão “Limpar” restaura os padrões definidos, não valores arbitrários. O estado do filtro deve ser mantido ao trocar de visão na mesma sessão, exceto filtros exclusivos de uma visão.
## 3. Definição dos KPIs — não implementar fórmulas implícitas
Toda métrica deve ter identificador estável, definição funcional aprovada, fórmula versionada, unidade, janela temporal, regra de inclusão/exclusão, fonte owner, política de permissão e destino de detalhe.
<table header-row="true">
<tr>
<td>metricId proposto</td>
<td>Nome</td>
<td>Definição a implementar</td>
<td>Unidade</td>
<td>Owner</td>
<td>Pendência de regra</td>
</tr>
<tr>
<td>`work_orders_in_progress`</td>
<td>OS em andamento</td>
<td>quantidade de OS cujo status pertence ao conjunto oficial de estados ativos no período de referência</td>
<td>OS</td>
<td>Oficina</td>
<td>mapear enum real de status</td>
</tr>
<tr>
<td>`estimates_pending_decision`</td>
<td>Orçamentos aguardando decisão</td>
<td>quantidade de orçamentos enviados ao cliente e ainda sem decisão final válida</td>
<td>orçamentos</td>
<td>Oficina</td>
<td>confirmar estados oficiais e expiração</td>
</tr>
<tr>
<td>`estimates_open_value`</td>
<td>Valor de orçamento em aberto</td>
<td>soma do valor vigente dos orçamentos incluídos no conjunto de estados aberto, sem duplicar versões anteriores</td>
<td>BRL</td>
<td>Oficina</td>
<td>definir se parcial aprovado entra no total</td>
</tr>
<tr>
<td>`delayed_deliveries`</td>
<td>Entregas atrasadas</td>
<td>entregas pendentes com previsão anterior ao instante atual no fuso da empresa</td>
<td>entregas</td>
<td>Oficina</td>
<td>confirmar fonte da previsão</td>
</tr>
<tr>
<td>`accounts_receivable_open`</td>
<td>Contas a receber em aberto</td>
<td>saldo residual das parcelas não liquidadas, excluindo títulos cancelados e estornados</td>
<td>BRL</td>
<td>Financeiro</td>
<td>alinhar enum e política de crédito</td>
</tr>
<tr>
<td>`accounts_payable_open`</td>
<td>Contas a pagar em aberto</td>
<td>saldo residual das obrigações não liquidadas, excluindo registros cancelados/estornados</td>
<td>BRL</td>
<td>Financeiro</td>
<td>alinhar enum e política de crédito</td>
</tr>
<tr>
<td>`revenue_realized`</td>
<td>Faturamento realizado</td>
<td>valor conforme definição financeira oficial aprovada, sem somar orçamento a receita</td>
<td>BRL</td>
<td>Financeiro</td>
<td>escolher regime e fonte contábil</td>
</tr>
<tr>
<td>`average_ticket`</td>
<td>Ticket médio</td>
<td>faturamento definido para o período dividido pela quantidade de documentos/transações elegíveis</td>
<td>BRL</td>
<td>Financeiro</td>
<td>aprovar denominador e tratamento de estorno</td>
</tr>
<tr>
<td>`estimate_conversion_rate`</td>
<td>Conversão de orçamentos</td>
<td>quantidade de orçamentos elegíveis convertidos em OS dividida pela coorte elegível definida</td>
<td>percentual</td>
<td>Oficina</td>
<td>aprovar coorte por criação ou decisão</td>
</tr>
<tr>
<td>`vehicles_serviced`</td>
<td>Veículos atendidos</td>
<td>contagem distinta de veículos com atendimento elegível no período</td>
<td>veículos</td>
<td>Oficina</td>
<td>definir status que caracteriza atendimento</td>
</tr>
</table>
**Bloqueio de implementação:** os itens “Pendência de regra” não podem ser resolvidos por suposição do desenvolvedor. O responsável pelo produto precisa aprovar a definição e registrar a decisão antes de considerar o KPI pronto. Não misturar caixa, competência, faturamento fiscal, valor de orçamento e recebimento.
### Regras para cálculos
- Valores monetários são calculados no servidor com tipo decimal apropriado; não usar ponto flutuante binário para cálculo financeiro.
- Percentuais retornam numerador e denominador junto ao valor para permitir auditoria e tooltip explicativo.
- Métricas temporais usam o fuso horário da empresa autenticada, com limites inclusivos/exclusivos definidos no contrato da API.
- A comparação usa a mesma definição, escopo e filtros da janela principal.
- Um denominador igual a zero retorna estado `NO_DATA` ou `NOT_APPLICABLE`, nunca percentual zero sem explicação.
- Dados parcialmente disponíveis retornam estado `PARTIAL` e identificam quais fontes falharam.
- O frontend não recalcula valores oficiais com base nas linhas atualmente carregadas.
## 4. Contrato de filtros
<table header-row="true">
<tr>
<td>Campo API</td>
<td>Tipo</td>
<td>Obrigatório</td>
<td>Validação no servidor</td>
<td>Comportamento</td>
</tr>
<tr>
<td>`dashboardType`</td>
<td>enum</td>
<td>sim</td>
<td>aceitar somente os quatro valores documentados</td>
<td>seleciona a visão</td>
</tr>
<tr>
<td>`from`</td>
<td>data ISO `YYYY-MM-DD`</td>
<td>sim</td>
<td>data válida e limite máximo de intervalo configurado</td>
<td>início do período no fuso da empresa</td>
</tr>
<tr>
<td>`to`</td>
<td>data ISO `YYYY-MM-DD`</td>
<td>sim</td>
<td>maior ou igual a `from`</td>
<td>fim do período conforme convenção documentada</td>
</tr>
<tr>
<td>Empresa autenticada (não é parâmetro HTTP)</td>
<td>UUID</td>
<td>depende do escopo</td>
<td>confirmar tenant/empresa e capability da sessão autenticada</td>
<td>limita dados à empresa autorizada, sem filtro de unidade</td>
</tr>
<tr>
<td>`compareFrom`</td>
<td>data ISO</td>
<td>não</td>
<td>exige `compareTo`; validar intervalo</td>
<td>início da comparação</td>
</tr>
<tr>
<td>`compareTo`</td>
<td>data ISO</td>
<td>não</td>
<td>exige `compareFrom`; validar intervalo</td>
<td>fim da comparação</td>
</tr>
<tr>
<td>`timezone`</td>
<td>identificador IANA</td>
<td>não recomendado pelo cliente</td>
<td>preferir configuração confiável do servidor/empresa autenticada</td>
<td>evita cliente escolher fuso arbitrário</td>
</tr>
<tr>
<td>`cursor`</td>
<td>string opaca</td>
<td>para paginação</td>
<td>validar cursor emitido pelo servidor</td>
<td>paginação estável</td>
</tr>
<tr>
<td>`limit`</td>
<td>inteiro</td>
<td>não</td>
<td>limitar a faixa permitida pelo servidor</td>
<td>tamanho da página</td>
</tr>
</table>
Não confiar em `tenantId`, `companyId`, capabilities ou permissões enviados pelo navegador. Esses valores devem vir da sessão autenticada e da política de autorização.
## 5. Contrato de API proposto
### GET `/api/v1/dashboards/home`
Finalidade: devolver o resumo agregado para uma visão. Este endpoint é uma proposta, não confirmação de rota existente.
**Request de exemplo**
```javascript
GET /api/v1/dashboards/home?dashboardType=STANDARD&from=2026-09-01&to=2026-09-30
Authorization: Bearer <token>
Accept: application/json
```
**Response de exemplo**
```json
{
  "requestId": "req_example",
  "dashboardType": "STANDARD",
  "context": {
    "from": "2026-09-01",
    "to": "2026-09-30",
    "timezone": "America/Recife",
    "companyScope": "authenticated-session"
  },
  "generatedAt": "2026-10-01T12:30:00Z",
  "freshness": "FRESH",
  "sections": [
    {
      "sectionId": "work_orders",
      "owner": "OFICINA",
      "state": "SUCCESS",
      "metrics": [
        {
          "metricId": "work_orders_in_progress",
          "label": "OS em andamento",
          "value": 18,
          "unit": "COUNT",
          "definitionVersion": 1,
          "generatedAt": "2026-10-01T12:30:00Z",
          "state": "SUCCESS",
          "drillDown": {
            "target": "OFICINA_WORK_ORDERS",
            "filters": { "statusGroup": "IN_PROGRESS" }
          }
        }
      ]
    }
  ],
  "warnings": []
}
```
Os identificadores e valores acima são apenas exemplos de formato, não dados reais. O contrato final deve ser alinhado com o backend existente.
### Contrato de estado por bloco
Valores possíveis propostos: `SUCCESS`, `NO_DATA`, `PARTIAL`, `STALE`, `FORBIDDEN`, `ERROR`, `NOT_APPLICABLE`.
- `SUCCESS`: resposta válida para o escopo solicitado.
- `NO_DATA`: fonte consultada com sucesso, sem registros elegíveis.
- `PARTIAL`: dados incompletos; informar quais fontes faltam.
- `STALE`: dado anterior mantido, com timestamp da última atualização válida.
- `FORBIDDEN`: usuário sem autorização; não revelar valores protegidos.
- `ERROR`: consulta falhou; preservar dados anteriores se existirem e indicar tentativa de recuperação.
- `NOT_APPLICABLE`: métrica não se aplica ao contexto, por exemplo denominador inexistente.
### Erros de API
<table header-row="true">
<tr>
<td>HTTP</td>
<td>Código de domínio sugerido</td>
<td>Comportamento de interface</td>
</tr>
<tr>
<td>400</td>
<td>`INVALID_FILTER`</td>
<td>indicar filtro inválido; preservar filtros editáveis</td>
</tr>
<tr>
<td>401</td>
<td>`UNAUTHENTICATED`</td>
<td>executar fluxo padrão de sessão expirada</td>
</tr>
<tr>
<td>403</td>
<td>`FORBIDDEN`</td>
<td>ocultar valores protegidos e mostrar acesso restrito</td>
</tr>
<tr>
<td>404</td>
<td>`UNIT_NOT_FOUND`</td>
<td>explicar que o contexto autenticado da empresa não está disponível</td>
</tr>
<tr>
<td>409</td>
<td>`CONTEXT_CHANGED`</td>
<td>recarregar permissões/contexto e pedir nova consulta</td>
</tr>
<tr>
<td>422</td>
<td>`INVALID_PERIOD`</td>
<td>apontar o campo que precisa ser corrigido</td>
</tr>
<tr>
<td>429</td>
<td>`RATE_LIMITED`</td>
<td>informar limite e permitir nova tentativa conforme política</td>
</tr>
<tr>
<td>503</td>
<td>`SOURCE_UNAVAILABLE`</td>
<td>mostrar erro parcial ou global conforme fonte afetada</td>
</tr>
<tr>
<td>500</td>
<td>`INTERNAL_ERROR`</td>
<td>mensagem segura, requestId para suporte, sem stack trace</td>
</tr>
</table>
## 6. Modelo de leitura — proposta, não duplicação de transações
O Home não é proprietário das entidades operacionais. Caso seja necessário materializar dados, usar um read model explicitamente separado das tabelas transacionais e documentar a estratégia de atualização.
### DashboardMetricDefinition
- `metric_id`: string, PK lógica estável.
- `label`: string, nome exibido.
- `description`: texto, definição compreensível para o usuário.
- `formula_version`: inteiro positivo.
- `owner_module`: enum de módulo proprietário.
- `value_unit`: enum, por exemplo COUNT, BRL, PERCENT, HOURS.
- `required_capability`: string.
- `drill_down_target`: referência permitida para destino.
- `is_active`: boolean.
- `created_at`, `updated_at`: timestamp UTC.
### DashboardSnapshot (somente se justificado por performance)
- `id`: UUID, PK.
- `tenant_id`: UUID, obrigatório.
- `company_id`: UUID, obrigatório.
- Não criar `unit_id`: contexto de empresa/tenant vem da sessão autenticada.
- `dashboard_type`: enum fechado.
- `period_from`, `period_to`: data.
- `payload_version`: inteiro.
- `payload`: JSON validado pelo schema da versão.
- `generated_at`: timestamp UTC.
- `expires_at`: timestamp UTC.
- `state`: enum de freshness.
- Índice composto sugerido: tenant + company + unit + dashboard_type + period_from + period_to + generated_at.
- Política de retenção e invalidação deve ser definida; não armazenar dados pessoais desnecessários no payload.
Não criar preferências persistidas, snapshots ou tabelas auxiliares sem verificar se o projeto já possui mecanismo equivalente. Se o sistema não precisar de persistência, calcular via serviço de leitura é aceitável.
## 7. Tabela de OS — contrato operacional da interface
<table header-row="true">
<tr>
<td>Coluna</td>
<td>Campo do DTO</td>
<td>Formatação</td>
<td>Ordenação</td>
<td>Regra de acesso</td>
</tr>
<tr>
<td>OS</td>
<td>`workOrder.displayNumber`</td>
<td>identificador clicável</td>
<td>número/data conforme decisão de produto</td>
<td>abrir somente se permitido</td>
</tr>
<tr>
<td>Cliente</td>
<td>`customer.displayName`</td>
<td>nome de exibição</td>
<td>busca textual</td>
<td>respeitar acesso a dados pessoais</td>
</tr>
<tr>
<td>Veículo</td>
<td>`vehicle.makeModel` e `vehicle.plateMasked`</td>
<td>modelo e placa conforme política</td>
<td>busca por placa/modelo</td>
<td>nunca incluir veículo de outro tenant</td>
</tr>
<tr>
<td>Responsável</td>
<td>`assignee.displayName`</td>
<td>nome</td>
<td>filtro por responsável</td>
<td>não expor campos internos</td>
</tr>
<tr>
<td>Status</td>
<td>`status.label`</td>
<td>texto + badge acessível</td>
<td>status configurável</td>
<td>usar enum oficial do owner</td>
</tr>
<tr>
<td>Previsão</td>
<td>`delivery.expectedAt`</td>
<td>fuso da empresa autenticada</td>
<td>ascendente por padrão</td>
<td>vazio deve ser “Não definida”</td>
</tr>
<tr>
<td>Valor</td>
<td>`financial.displayAmount`</td>
<td>BRL pt-BR</td>
<td>não ordenar se sem autorização</td>
<td>campo omitido/indisponível sem capability</td>
</tr>
<tr>
<td>Atualizada em</td>
<td>`updatedAt`</td>
<td>data e hora local</td>
<td>opcional</td>
<td>origem do servidor</td>
</tr>
</table>
- Busca e filtros devem ser executados no servidor; não filtrar apenas a página atual.
- Paginação deve ter ordenação determinística com desempate por ID.
- Se um registro for alterado após a consulta, ao abrir o detalhe o módulo owner recarrega a versão atual.
- Não exibir ações em massa nesta tabela.
- O clique em linha e o clique no link da OS devem ter o mesmo destino acessível; botões internos não podem gerar navegação dupla.
- Estado vazio deve refletir filtros atuais e oferecer “Limpar filtros” quando houver filtro ativo.
## 8. Eventos e observabilidade
Eventos analíticos permitidos, sem payload excessivo:
- `dashboard_view_opened`
- `dashboard_type_changed`
- `dashboard_filter_applied`
- `dashboard_refresh_requested`
- `dashboard_metric_opened`
- `dashboard_section_failed`
Cada evento deve incluir requestId/sessionId pseudonimizado quando disponível, tipo de visão, empresa autorizada, timestamp e resultado. Não incluir nome do cliente, placa, valor de conta individual, token, segredo ou payload completo do dashboard em logs.
## 9. Matriz de testes obrigatória
<table header-row="true">
<tr>
<td>ID</td>
<td>Cenário</td>
<td>Resultado esperado</td>
</tr>
<tr>
<td>HOME-001</td>
<td>abrir `/home` autenticado</td>
<td>visão Padrão e filtros padrão documentados</td>
</tr>
<tr>
<td>HOME-002</td>
<td>selecionar cada uma das quatro visões</td>
<td>mesma rota; conteúdo e capability adequados</td>
</tr>
<tr>
<td>HOME-003</td>
<td>tentar enviar tipo de visão desconhecido</td>
<td>backend rejeita com erro de filtro</td>
</tr>
<tr>
<td>HOME-004</td>
<td>período inicial posterior ao final</td>
<td>erro associado ao filtro; nenhuma consulta inválida</td>
</tr>
<tr>
<td>HOME-005</td>
<td>usuário sem capability financeira</td>
<td>valores financeiros não chegam no payload protegido</td>
</tr>
<tr>
<td>HOME-006</td>
<td>tentar acessar identificador de recurso de outra empresa manipulando request</td>
<td>backend retorna acesso negado</td>
</tr>
<tr>
<td>HOME-007</td>
<td>uma fonte de dados indisponível</td>
<td>blocos independentes continuam; bloco falho marcado</td>
</tr>
<tr>
<td>HOME-008</td>
<td>todas as fontes indisponíveis</td>
<td>erro global compreensível, sem zeros falsos</td>
</tr>
<tr>
<td>HOME-009</td>
<td>consulta sem registros</td>
<td>estado NO_DATA, não ERROR</td>
</tr>
<tr>
<td>HOME-010</td>
<td>snapshot antigo e atualização falha</td>
<td>preservar último dado válido e marcar stale</td>
</tr>
<tr>
<td>HOME-011</td>
<td>abrir drill-down</td>
<td>destino owner correto e filtros compatíveis preservados</td>
</tr>
<tr>
<td>HOME-012</td>
<td>período com mudança de horário/fuso</td>
<td>limites calculados pelo fuso da empresa autenticada</td>
</tr>
<tr>
<td>HOME-013</td>
<td>paginação com muitos registros</td>
<td>ordenação estável, sem duplicatas entre páginas</td>
</tr>
<tr>
<td>HOME-014</td>
<td>zoom 200% e viewport mobile</td>
<td>funções essenciais acessíveis sem scroll horizontal global</td>
</tr>
<tr>
<td>HOME-015</td>
<td>teclado/leitor de tela</td>
<td>seletor, filtros, tabela, gráfico e erros operáveis</td>
</tr>
<tr>
<td>HOME-016</td>
<td>troca de permissão durante sessão</td>
<td>próxima consulta revalida acesso</td>
</tr>
<tr>
<td>HOME-017</td>
<td>repetidos cliques em Atualizar</td>
<td>requisições controladas; interface não duplica blocos</td>
</tr>
<tr>
<td>HOME-018</td>
<td>API retorna campo financeiro não autorizado</td>
<td>teste de contrato falha; dado não pode ser renderizado</td>
</tr>
</table>
## 10. Definição de pronto — sem exceção
Esta tela não deve ser considerada pronta para desenvolvimento enquanto não houver:
- definições de KPIs aprovadas para todas as métricas exibidas;
- estados oficiais de OS, orçamento e entrega mapeados com os enums reais;
- contrato final de API comparado com o código existente;
- autorização validada por testes de backend;
- DTOs tipados e exemplos de sucesso, vazio, parcial e erro;
- regras de cache/freshness e invalidação definidas;
- destinos reais de drill-down confirmados;
- decisões de produto pendentes resolvidas e registradas;
- testes automatizados de cálculo, autorização, integração e responsividade.
**Regra de qualidade documental:** exemplos não são decisões de produto; propostas de API não são endpoints existentes; nomes de entidades conceituais não autorizam criar tabelas sem auditoria do esquema atual.
## Contrato de implementação do frontend
### Rota e navegação
- Rota canônica: `/home`.
- Entrada após autenticação: `/home`.
- `/dashboard` é apenas alias de compatibilidade e redireciona para `/home`.
- A tela física permanece única.
- Existem exatamente quatro visões internas: Padrão, Gerencial, Financeiro e Orçamento.
- A troca de visão não cria rota adicional.
- Não existe Ações Rápidas.
### UX/UI
- UI-MASTER-001 é o contrato visual.
- Linguagem Stripe-first: hierarquia, espaçamento, simplicidade, superfícies limpas e ações discretas.
- Canvas `#F8FAFC`, panel `#FFFFFF`, border `#E2E8F0`, Primary 500 `#2563EB`, Primary 600 `#1D4ED8`.
- Grid: 12 colunas desktop, 8 tablet, 4 mobile.
- Cards somente quando ajudam a decisão.
- Filtros próximos ao conteúdo.
- Loading com skeleton.
- Erro com recuperação.
- Freshness explícita.
- WCAG 2.2 AA.
- Não utilizar PrimeIcons; ações próprias utilizam SVG outline compatível com o contrato Tabler.
### Composição
1. Page Header.
2. DashboardSelector.
3. FilterBar.
4. KPIs.
5. Gráfico principal.
6. Painel contextual da visão.
7. Freshness/fonte dos indicadores.
### Dados
A implementação de frontend consome o contrato canônico `GET /api/v1/dashboards/home` e o `DashboardDTO` tipado. Os aliases antigos permanecem somente para compatibilidade. As métricas exibidas vêm de agregações reais do backend; o frontend não inventa valores demonstrativos.
### Próximo contrato técnico obrigatório
A fronteira de integração permanece explícita:
- período e comparação já são aplicados no backend;
- tenant e usuário são resolvidos pela sessão autenticada;
- estado de dados disponíveis, parciais e sem permissão já possui contrato;
- não existe conceito de unidade operacional/Filial neste produto; não implementar vínculo, seletor ou filtro `unitId` no Dashboard;
- drill-downs usam os módulos proprietários;
- tabelas server-side específicas de cada visão entram quando houver dados tabulares oficiais definidos para a visão.
### Regra de segurança
O frontend não é autoridade de escopo. Empresa/tenant e capabilities decorrem da sessão autenticada; IDs fornecidos pelo cliente nunca ampliam autorização no backend.
### Regra definitiva
**O Início apresenta o estado do negócio. Os módulos proprietários continuam sendo a fonte de verdade.**
## Contrato de homologação visual e identidade aplicada
### Modo de homologação do frontend
- O ambiente de desenvolvimento utiliza `environment.uxPreview = true` para permitir validar o shell, o menu canônico e o Dashboard sem depender de uma API disponível.
- Nesse modo, o Dashboard recebe dados demonstrativos do `DashboardService`; a interface deve identificar claramente que os valores são demonstrativos e não representam dados reais de uma oficina.
- O ambiente de produção mantém `environment.uxPreview = false` e consulta os dados pelo serviço/API. O modo de preview não deve ser habilitado em produção.
- A homologação visual deve validar carregamento, estado de erro com nova tentativa e estado sem dados, além do layout com os quatro seletores internos.
- A demonstração visual não substitui validação dos contratos de backend, tenancy, autorização, cálculos financeiros nem testes de produção.
### Identidade visual do Dashboard
- O Dashboard mantém identidade própria da NERITECH AUTO, com referência de qualidade de interação em produtos SaaS premium, sem copiar a identidade visual de outra marca.
- A receita utiliza azul NERITECH; despesas utilizam âmbar, com legenda e cores consistentes no gráfico.
- O KPI principal utiliza uma composição azul/índigo com contraste alto; os indicadores secundários usam acentos cromáticos distintos para operação, conclusão e atenção.
- Superfícies, bordas, sombras e fundos permanecem discretos para preservar a leitura dos dados. Cor deve indicar hierarquia ou significado, não ser decoração sem função.
- Estados de carregamento, erro, vazio e dados demonstrativos precisam ser distinguíveis e acessíveis; cor não é o único meio de transmitir status.
### Shell e navegação
- O shell autenticado ativo é composto por `AdminLayout`, `Sidebar`, `Sidemenu` e `Header`; alterações no menu devem respeitar esse caminho ativo.
- O menu de homologação é inicializado pelo `StartupService` quando `uxPreview` está habilitado. A navegação de preview não depende de resposta de menu do backend.
- O menu canônico e as rotas oficiais permanecem inalterados. Não criar módulos, telas ou rotas novas apenas para acomodar ajustes visuais.
- A navegação deve manter estados ativos legíveis, expansão previsível de grupos, recolhimento com tooltips, foco visível e comportamento responsivo.
- Componentes legados de menu não devem ser removidos ou reutilizados sem verificação de referências no código.
### Aceite técnico desta camada
- [ ] O frontend abre e permite navegar em modo de homologação sem backend disponível.
- [ ] O aviso de dados demonstrativos aparece somente quando `uxPreview` está habilitado.
- [ ] O dashboard usa cores distintas e consistentes para receita e despesas.
- [ ] O shell mantém a arquitetura oficial e não duplica a navegação.
- [ ] Build Angular e pipeline do repositório aprovados antes de declarar homologação concluída.
- [ ] A homologação de UX não é confundida com homologação funcional de backend.
## C — Evidências e referências
**O vídeo e as capturas documentam comportamentos observados, não definem a UX nem comprovam APIs.**
## Benchmark de indicadores operacionais — extração do vídeo
**Evidência:** vídeo UltraCar fornecido pelo usuário (46min02s). **O = observado na captura; P = proposta funcional para NERITECH AUTO; V = confirmar com regras e APIs do projeto.** Não migrar a navegação nem reproduzir a identidade da referência.
### Métricas visíveis no Home da referência (O 02:20–03:20)
- **Total ticket médio**, **Ticket médio no mês**, **Entregas para o mês**, **Entregas em atraso**.
- Painéis por situação do orçamento/OS com categorias visíveis **Aberto, Autorizado, Cancelado, Fechado**, quantidade de registros e total de orçamentos em cada categoria/período.
- **Relatório de Peças e Serviços** com série histórica e filtros; **Entradas de veículos no mês** e **Saídas de veículos no mês**.
- Indicadores financeiros de consulta no cabeçalho da referência: **Parcelas a Receber Vencidas**, **A Receber no período**, **A Pagar no período** e **Parcelas a Pagar Vencidas**.
### Tradução para o Home canônico NERITECH (P / V)
- Manter **UMA tela Início com quatro visões Padrão, Gerencial, Financeiro e Orçamento**, sem criar tela nova, sem ações rápidas no Home e sem tornar o Home dono de lançamentos.
- **Padrão**: operações resumidas (entradas, saídas, entregas próximas/atrasadas, OS por status).
- **Gerencial**: ticket médio com numerador/denominador documentados, evolução de peças/serviços e produtividade por período.
- **Financeiro**: títulos a receber/pagar por vencimento, vencidos, fluxo realizado/projetado como leitura dos owners financeiros.
- **Orçamento**: quantidade por status, valor estimado/autorizado e conversão; exibir denominadores e excluir cancelados segundo política aprovada, sem misturar OS concluída com orçamento fechado sem regra.
### Indicadores — campos e definições pendentes (V)
- Definir claramente "ticket médio": receita efetiva ou orçamento autorizado? período de autorização, pagamento ou entrega? fórmula não está comprovada no vídeo.
- "Entrega em atraso": comparação da data prevista com data efetiva e status; não contar como atrasado após entrega, salvo histórico de atraso explicitamente apresentado.
- Entradas e saídas mensais devem utilizar eventos efetivos e timezone da empresa, não contagem de linhas sem data confiável.
- Não tratar valores exibidos na demonstração como dados reais do NERITECH.
### QA
- Cada KPI com fonte, recorte temporal, tooltip textual com fórmula, link contextual para a consulta no owner, estado indisponível e indicador de atualização; sem CTA transacional e sem extrapolação incorreta de valores.
## D — Implementação e homologação (registro a preencher pelo Codex)
- **Situação nesta revisão:** documentação organizada; implementação e homologação desta tela NÃO foram verificadas nesta auditoria editorial.
- **Evidências obrigatórias:** rota Angular e componente reais; controller, DTO, entidade e migração aplicável; capabilities e testes entre empresas; comandos/resultados de testes, CI e SHA; validação visual, responsiva e acessível; pendências.
- **Critério:** somente marcar HOMOLOGADA após comprovar os cenários de aceite do contrato A. Sem integração externa confirmada, registrar indisponibilidade operacional sem simular sucesso.

