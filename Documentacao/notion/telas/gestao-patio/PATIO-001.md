# Gestão de Pátio — Tela interna única

> Espelho consultivo do Notion (2026-10-15). Fonte oficial: https://app.notion.com/p/3ee01fd65a9c8134adf5c8eac59daead
> Não sincronizado automaticamente; a página canônica prevalece.

## A — CONTRATO EXECUTÁVEL CODEX — PATIO-001 \| DECISÃO DE PRODUTO DEFINITIVA
**Status documental:** ESPECIFICAÇÃO PRONTA PARA EXECUÇÃO INCREMENTAL, NÃO É TELA HOMOLOGADA. Uma **única tela interna do NERITECH AUTO**, na mesma aplicação e sessão. Seção atual prevalece em divergências com referências antigas. **Proibido criar segundo produto, microfrontend, barra lateral dupla, seis ScreenIds, rotas separadas ou Filiais.**
### Objetivo e componentes internos
Uma superfície com navegação interna por modos **Kanban**, **Lista**, **Agenda/Calendário**, **Detalhe da OS**, **Boxes/Recursos**, **Painel TV/Alertas**. Cronômetros/apontamentos, checklists e serviços aparecem no detalhe da mesma tela. O modo TV é visualização em tela cheia e somente leitura por sessão autorizada.
### Modelo de dados e origem
- Cartão operacional = OS existente `osId,numeroOS,clienteId,veiculoId,placa,modelo,statusOs,etapaPatio,responsavel,area,box,prioridade,entrada,previsaoEntrega,servicosConcluidos,servicosTotal,bloqueios,atualizadoEm,versao`; referências a clientes e veículos via owners, não copiar cadastro.
- Etapa = `id,nome,ordem,corToken,transicoesPermitidas,perfilPermitido,limiteWip opcional`. **Etapas iniciais propostas:** Entrada → Diagnóstico → Aguardando Aprovação → Em Execução → Pronto. Estado de entrega/fechamento continua em OFC-005/010. Mapear enums de OS, nunca migrar silenciosamente dados existentes.
- Atividade/serviço = `osId,itemServicoId,responsavelId,status,tempoPrevisto,apontamentos,observacoes`; serviço é owner Oficina/OS.
- Apontamento = `id,osId,itemId,funcionarioId,inicio,fim,segundosAtivos,pausas,origem,ator,estado,versao`. Tempos calculados pelo servidor a partir dos intervalos, não de um cronômetro JavaScript local.
- Box/recurso = `id,nome,capacidade,ativo,ocupacao,localizacao` dentro da empresa, não Filial. Agenda administra reserva e conflito; Pátio apenas consulta/solicita mudança autorizada.
### Colunas, cartões, filtros e ações
- Kanban cartão: **Placa, Modelo, Cliente, Nº OS, Status, Responsável, Progresso concluídos/total, Previsão, Atraso, Box e Bloqueio**; arrastar e soltar mais ação alternativa acessível via menu "Mover para etapa".
- Lista: **Nº OS, Placa, Cliente, Veículo, Responsável, Etapa, Área, Box, Entrada, Entrega prevista, Progresso, Atraso, Prioridade, Estado, Ações** com sort, paginação 20/50/100.
- Filtros persistidos por usuário: período, data-base, placa, cliente, OS, prioridade, responsável, equipe/área, box, etapa, atrasado, status e serviço. Busca por placa/Nº/cliente com debounce.
- Detalhe: abas **Serviços, Apontamentos, Inspeções/Checklist, Observações, Histórico e Evidências**; editar OS abre a rota canônica `/os` correspondente, sem novo CRUD.
- Boxes/Recursos: quadro por box com ocupado/livre/reservado, responsável, período e conflito, sem inferir capacidade de pessoal de uma sala.
- Painel TV: evita dados pessoais completos; usa placa mascarada se política da empresa pedir, etapa e contadores; alertas de atraso, OS bloqueada e WIP excedido, com permissão.
### Regras de transição e cronômetro
- Etapas são configuráveis por administrador, mas a sequência proposta é o padrão inicial; movimentar só transição permitida e após servidor confirmar, senão reverter e mostrar motivo.
- Início/pausa/retomada/conclusão de tarefa é **idempotente**, não permite múltiplos timers ativos para o mesmo colaborador no mesmo item; status real por serviço preservado em caso de reload. Duas pessoas podem trabalhar na mesma OS quando tarefas diferentes.
- Conclusão de atividade exige justificativa se houver pendência de qualidade crítica. Aprovação do orçamento e entrega da OS continuam em seus owners. Contagem e atraso no Pátio são projeções de dados reais, nunca estoque paralelo.
- Atualização em tempo real via evento/polling com reconciliação versionada; padrão inicial polling de 30s quando modo em foco, com refresh manual e backoff. Sem infraestrutura push, não fingir comunicação em tempo real.
- Acesso por tenant, empresa e perfil; mecânico visualiza itens atribuídos, consultor administra atribuição, gerente controla etapas/boxes, financeiro só vê dados autorizados. Toda mutação grava ator, data/hora, versão, motivo e eventual evidência.
### API, migração e implementação por Codex
- Na auditoria de `feature/develop` em 14/10, `FrontEnd/src/app/routes/patio/patio.ts` era uma estrutura estática informando "Quadro aguardando integração operacional", sem API real; link interno `/ordens-servico` divergia da rota `/os`. Corrigir pelo fluxo existente, **não recriar todo o frontend**.
- Reutilizar endpoints de OS, serviços, agenda, inspeções e RH; criar apenas projection/query API tenant-safe para cartões, etapas, progressos, timers e boxes se faltar; verificar schema e Flyway antes de migração nova.
- Kanban responde com `cards, columns, count, updatedAt, pagination`, timer com comandos `start/pause/resume/finish` e `idempotencyKey`; rotas são **propostas de DTO**, não fatos de API já publicada.
### Critérios de aceite que o Codex deve comprovar
1. Criar OS autorizada na Oficina e refletir uma única vez no Pátio, sem dados fictícios.
2. Mover cartão autorizadamente, recusar etapa inválida e aplicar rollback visual.
3. Dois operadores tentam mover a mesma OS: só transição válida mantém versão, conflito 409 recuperável.
4. Iniciar, pausar, recarregar, retomar e concluir serviço: tempo ativo consistente, sem sobrecontagem ou dependência de relógio do navegador.
5. Reservar box em Agenda e ver disponibilidade no Pátio sem duplicação de reserva; conflito real não some com refresh.
6. Checklist/inspeções exibem execução pertencente à mesma OS, sem duplicar cadastro.
7. Perfil mecânico/gestor/atendimento e empresas A/B não acessam OS alheia; navegação por teclado, responsividade, estados loading/vazio/erro/offline/403 e zoom 200%.
8. Modo TV funciona somente com sessão/autorização e não vaza dados privados em tela compartilhada.
**UX:** somente UI-MASTER-001, sidebar atual que abre/recolhe, Tabler Icons, fundos claros e contraste; não copiar menus ou ícones da referência visual.
## B — Detalhamento complementar
**Regra editorial:** preservar campos, tabelas e fluxos abaixo; aplicar decisões somente quando compatíveis com o contrato A, D01–D11 e APIs verificadas no repositório.
## Decisão arquitetural
Gestão de Pátio é uma **tela interna única** do NERITECH AUTO, acessada pelo menu principal dentro da mesma aplicação e sessão. Não é um produto, microfrontend independente, link externo ou sistema vinculado. A tela concentra o Kanban e seus recursos em painéis, drawers, abas e modos internos, sem criar seis telas independentes. A propriedade de cliente, veículo e ordem de serviço permanece nos respectivos domínios canônicos.
## Objetivo
Permitir acompanhar visualmente a localização e a etapa dos veículos no pátio, com quadro Kanban operacional, movimentações entre posições e trilha de eventos. O escopo final das etapas e posições deve ser configurável pela operação autorizada, preservando a arquitetura de módulos do NERITECH AUTO.
## Contrato de tela única — navegação e UX
- **Identificação funcional:** `TELA-AUTO-PATIO-001 — Gestão de Pátio`. As referências `PATIO-001..006` descrevem áreas internas da mesma tela, não ScreenIds de seis rotas independentes.
- **Acesso:** item `Gestão de Pátio` no menu principal, navegando por uma rota interna existente ou a ser definida na aplicação, sem seta de link externo, iframe ou abertura de outro produto.
- **Estrutura:** Application Shell oficial (sidebar, topbar, breadcrumb, identidade NERITECH AUTO e design tokens de UI-MASTER-001), título contextual, filtros de unidade/etapa/estado/responsável e Kanban como visão padrão.
- **Áreas internas:** detalhes da OS em drawer/painel; etapas, boxes e recursos em painel de configuração autorizado; cronômetros e apontamentos em painel contextual; alertas como área filtrável; modo TV como visualização própria da mesma experiência, com proteção de dados.
- **Comportamento:** troca de visão ou painel sem novo login, sem reinicializar sessão e sem reconstruir estados globais. Links para a OS e veículo abrem as rotas internas dos respectivos owners, mantendo contexto e retorno ao pátio.
- **Estados:** loading, sem dados, erro, sem permissão, offline/degradado, conflito de movimentação, atualização concorrente e recuperação por retry. Toda transição depende de autorização e confirmação real no backend; Toasts e confirmações seguem o design system global.
- **Responsividade:** desktop com Kanban amplo; tablet com colunas roláveis; celular com lista agrupada e ações acessíveis. Drag-and-drop não é o único modo de movimentar veículos.
## Superfícies previstas
- Quadro de pátio: cartões de veículos distribuídos em colunas/etapas configuradas.
- Detalhe do veículo no pátio: localização, timestamps, OS vinculada, observações operacionais e histórico.
- Configuração de posições/etapas: regras de movimentação e capacidade em área ou painel da própria tela, conforme permissões.
- Histórico de movimentações: quem moveu, de/para, quando e motivo.
## Dados proprietários e integração
- Cliente, veículo e OS permanecem sob os módulos proprietários do NERITECH AUTO.
- Gestão de Pátio mantém apenas estado específico de localização/fluxo de pátio e referências aos IDs proprietários.
- Não duplicar nome/documento de cliente, cadastro técnico do veículo, status de OS, saldo de estoque ou status financeiro.
- A tela deve usar identificadores estáveis, serviços/APIs autenticados do NERITECH AUTO e validação de tenant/empresa/capability no backend.
- Se uma referência de OS/veículo estiver indisponível, mostrar estado de integração com retry; não inventar dados.
- O item do menu navega para a rota interna da Gestão de Pátio; falhas de carregamento exibem estado de erro com tentativa de recuperação, sem redirecionamento externo.
## Eventos e consistência
Eventos conceituais: `yard.vehicle_checked_in`, `yard.position_changed`, `yard.stage_changed`, `yard.vehicle_checked_out`. Toda movimentação registra ator, data/hora, origem/destino, motivo e referência. Repetições são idempotentes. O status de pátio não altera automaticamente status da OS sem uma jornada de integração explicitamente autorizada.
## Segurança e aceite
Usar a autenticação e sessão existentes do NERITECH AUTO, com tenant/empresa validados no backend, permissões por ação, auditoria, expiração de sessão e isolamento entre empresas. Testar veículo sem OS, OS encerrada, API indisponível, movimento concorrente, posição cheia quando houver capacidade, usuário sem permissão e saída do veículo. A navegação é interna, mantendo Gestão de Pátio como item próprio do menu principal e como **uma única tela**, sem recriar uma aplicação separada nem duplicar a lista de OS do módulo Oficina.
<page url="https://app.notion.com/p/6bb01fd65a9c8317b35f018a8c5bb52c">Gestão de Pátio — Componentes e Fluxos Internos</page>
## Gestão de Pátio — aprofundamento do quadro de acompanhamento
**Fonte:** vídeo UltraCar fornecido pelo usuário (46min02s). **O — visível no vídeo; P — proposta NERITECH; V — validação pendente.** Não copiar UX ou URLs da UltraCar; manter UI-MASTER-001 atualizado, sidebar própria expandível/recolhível, módulos canônicos e contratos existentes.
### Evidências visuais (O 31:20–33:00)
- Quadro operacional com filtros de responsável/orçamentista; lista de responsáveis ativa/inativa/todos; seleção por cliente, placa e ordenação por data de entrega.
- Colunas observadas no quadro: **Pendentes**, **Entrada**, **OS em Execução**; os cartões mostram placa, veículo/modelo, cliente, contador de serviços concluídos/total, data de entrega e indicação de atraso/antecipação. A apresentação evidencia responsáveis e progresso.
- Ação Nova Atividade; detalhe de cartão em painel contextual para tarefas/serviços e responsáveis; navegação por coluna e calendário.
- Há superfície de "Solicitações de agendamento" com Lista e Calendário e estados pendentes, recusados e cancelados, de fonte operacional distinta da Agenda.
### Contrato NERITECH (P / V)
- **Decisão mantida:** UMA tela interna Gestão de Pátio, mesma aplicação, sessão e navegação; nenhuma subaplicação, novo produto, módulo paralelo ou segunda sidebar por causa do benchmark.
- Modos internos: Kanban, lista/tabela, calendário, detalhe da OS, atividades/etapas, boxes e recursos, cronômetros/apontamentos, painel TV e alertas, conforme a documentação canônica já definida; os modos não viram novos itens na árvore do menu.
- Kartão: os_id, placa, veículo, cliente (respeitar PII e permissões), etapa_id, responsável, total_serviços, concluidos, prazo_entrega, desvio_prazo, prioridade, bloqueios, atualizado_em, versão.
- Kanban consome os dados reais de OS/Agenda, permite movimentação apenas de estados/etapas permitidos; validação no backend e concorrência otimista. Arrastar e soltar nunca é a única forma de mudar etapa.
- "Novo apontamento", atribuição de atividade e mudança de box podem ocorrer na tela, mas owners permanecem Oficina e dados de recursos/administração; registrar início, pausa, retomada, término, usuário, tempo e justificativa.
- Não assumir que "pendentes", "entrada" e "execução" são os únicos estados; mapear state machine oficial no GitHub.
### Filtros, colunas e interações
- Filtros: Data/período, cliente, placa, profissional/orçamentista, equipe/área, box, etapa, entrega prevista, atrasado, serviço e situação da OS; preservar filtros ao abrir detalhes.
- Alternância Kanban/lista/calendário não deve deslocar dados nem reproduzir barra de ícones da UltraCar.
- Lista acessível: Nº OS, Placa, Modelo, Cliente, Etapa, Serviços concluídos/total, Responsável, Box, Entrada, Previsão de entrega, Atraso, Status e Ações.
- Atraso tem valor numérico e legenda, não apenas vermelho; horários em pt-BR e timezone da operação.
### Eventos e aceite
- Atualizações e alterações rastreáveis; eventos de OS não geram pátio duplicado. Evitar cartões fantasmas, contagens erradas ou perda da seleção em atualização em tempo real.
- Testes: OS criada/atualizada/refletida no pátio, troca de estado autorizada, dois usuários concorrendo pela mesma etapa, filtro por responsável, atraso ao ultrapassar prazo, uso via teclado/mobile e dado de empresa isolado.
## C — Evidências e referências
**O vídeo e as capturas documentam comportamentos observados, não definem a UX nem comprovam APIs.**
## Auditoria complementar — detalhe de cartão no Pátio (32:00–32:20)
**Origem:** vídeo UltraCar enviado (46min02s); carimbo de tempo aproximado. **O = observado**, **P = proposta NERITECH**, **V = confirmação pendente no código/regra**. Não copiar layout externo; UI-MASTER-001 e sidebar NERITECH prevalecem.
**O — detalhe em painel/modal:** cabeçalho **Placa/Veículo/Cliente**, vínculo **Orçamento Nº**, botão externo de consulta, acesso **Checklist da OS** e CTA **Novo checklist express / Gerar checklist**. Abas **Serviços**, **Observações**, **Histórico**. Lista de serviços dividida por pessoa/setor (ex.: Comercial e funcionária), com **Adicionar serviços**; cada linha mostra título de serviço, situação cadastrada, tempo transcorrido e referência de duração prevista, além de **pausar**, **retomar/iniciar** e **concluir**.
**P — extensão na tela ÚNICA do Pátio:** reutilizar OS/serviços/inspeções existentes por componentes internos. Pátio não cria nova OS ou cadastro de checklist duplicado; aciona APIs do owner correto. Cronômetro/apontamento tem sessão única ativa por colaborador/tarefa conforme regra acordada.
**Dicionário de apontamento proposto:** `apontamento_id`, `os_id`, `servico_id`, `colaborador_id`, `inicio`, `fim`, `duracao_segundos`, `pausas`, `duracao_prevista`, `status`, `origem`, `versao`, `alterado_por`. Exibir tempo humano sem revelar valor inválido da demonstração como regra; `tempo_total` recalculado no backend e reconciliado com sessões.
**P — permissões:** colaborador só inicia/aponta serviço autorizado e não manipula horas de outro sem capability; conclusão pode exigir inspeção/qualidade a validar (**V**). Acesso ao histórico somente por empresa e perfil.
**QA:** clique em iniciar/pausar/concluir com debounce e confirmação do servidor; multiusuário concorrendo na mesma tarefa, mudança de dispositivo, relógio local incorreto, excesso de 24h, arredondamento, falta de conexão, inatividade, OS fechada, segurança e WCAG.
## D — Implementação e homologação (registro a preencher pelo Codex)
- **Situação nesta revisão:** documentação organizada; implementação e homologação desta tela NÃO foram verificadas nesta auditoria editorial.
- **Evidências obrigatórias:** rota Angular e componente reais; controller, DTO, entidade e migração aplicável; capabilities e testes entre empresas; comandos/resultados de testes, CI e SHA; validação visual, responsiva e acessível; pendências.
- **Critério:** somente marcar HOMOLOGADA após comprovar os cenários de aceite do contrato A. Sem integração externa confirmada, registrar indisponibilidade operacional sem simular sucesso.
