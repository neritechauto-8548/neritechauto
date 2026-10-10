# NERITECH AUTO — Sequência de desenvolvimento no Work

**Status:** proposta de consolidação técnica de telas-base, registrada para uso do ChatGPT Work. **Não altera a navegação, as rotas, os IDs de tela ou as responsabilidades funcionais oficiais.**

**Repositório:** `neritechauto-8548/neritechauto`  
**Branch obrigatória:** `feature/develop`  
**Documento de navegação oficial:** [03.01.03 Módulos](https://app.notion.com/p/80101fd65a9c833580f1012ec0a45407)  
**Contrato de UX/UI:** [UI-MASTER-001](https://app.notion.com/p/97401fd65a9c822a9a4b017c39a08518)  
**Especificações por tela:** [03.01.04 Telas & UX](https://app.notion.com/p/98501fd65a9c8379a9a281145568f52a)

## Regra-mãe

**Complexidade no sistema. Simplicidade para o usuário.** 

- **Stripe**: referência central para UX/UI premium, hierarquia, densidade informacional, navegação, formulários, tabelas, filtros, estados e responsividade; preservar identidade própria NERITECH AUTO, sem copiar a marca.
- **Shopmonkey**: fluxo de operação da oficina, atendimento, orçamentos, inspeções, aprovações, OS e comunicação.
- **UltraCar**: profundidade das funcionalidades para oficinas brasileiras, regras, cadastros, estoque, financeiro e processos operacionais.
- **OnMotor**: particularidades do mercado automotivo brasileiro, processos e integrações pertinentes. Usar as referências como orientação de produto, não como licença para copiar interfaces ou implementar funcionalidades fora do escopo oficial.
- Usar Angular e PrimeNG existentes no repositório, seguindo tokens e família **Tabler Icons** definidos na documentação.
- Evitar telas, formulários e componentes duplicados. **Uma tela-base não é necessariamente uma rota:** cada menu, processo, permissão, ação ou jornada mantém sua rota e regra oficial quando exigida.
- Não incorporar Gestão de Pátio à Oficina; continua um sistema independente (Kanban).
- Não alterar a árvore oficial nem tratar consolidação de telas-base como mudança autorizada na arquitetura.
- Não simular êxito de ações sem persistência. Modo de homologação sem backend deve estar marcado como demonstrativo e não contamina produção.
- **Frontend e backend são uma entrega única por tela:** validar endpoints, DTOs, autenticação/autorização, permissões, multiempresa, validações, persistência, transações, tratamento de erro e contratos entre Angular e API. Fazer as correções necessárias no backend, não apenas na interface.
- **Banco de dados:** usar nomes de tabelas, colunas, chaves e índices novos em **português**, preferindo `snake_case`, sem acentos ou espaços (ex.: `cliente`, `data_cadastro`, `empresa_id`). Manter consistência entre JPA/Hibernate, SQL, DTOs e migrações. Auditar nomes legados em inglês e corrigir quando tecnicamente viável, **sem renomear indiscriminadamente nem quebrar dados, referências, consultas, integrações ou APIs**.
- **Flyway (migrações de banco):** criar migração **nova e versionada** quando houver mudança de esquema, incluindo renomeação segura de tabela/coluna, índices, FKs, constraints e eventuais migrações de dados. Não editar migrações já executadas, apagar dados, recriar tabelas em produção, desabilitar validações ou usar `ddl-auto=create/update` como substituto do Flyway. Se exigir operação de risco, documentar plano de migração/rollback e exigir aprovação antes de executar em banco de produção.
- **Compilar e testar:** executar build real do frontend e compilação/testes do backend com as versões configuradas no repositório. Verificar migrações Flyway em banco de teste limpo e banco de teste previamente migrado; executar testes unitários/integrados relevantes e pipeline. Corrigir falhas, inclusive CI, antes de chamar a etapa de funcional/homologada; nunca afirmar sucesso sem evidência.
- Validar build e testes reais antes de declarar homologação. Registrar commit e evidências; atualizar Notion quando homologado.
- **Estes 38 itens são unidades de implementação/reutilização propostas**, não uma contagem de ScreenIds oficiais nem autorização para eliminar telas exigidas pela documentação.

## Ordem de execução — 38 telas-base

| # | Área | Tela-base ou agrupamento de desenvolvimento |
| --- | --- | --- |
| 00 | Aplicação global (não conta como tela) | Sidebar, topbar, branding, menu de usuário, busca, breadcrumb, layout responsivo, tabelas, filtros, formulários, componentes, estados e acesso por teclado |
| 01 | Acesso | Login |
| 02 | Acesso | Recuperação e redefinição de senha (fluxos e rotas distintos, base visual compartilhada) |
| 03 | Início | Dashboard único: Padrão, Gerencial, Financeiro e Orçamento (quatro visões internas) |
| 04 | Clientes | Listagem de clientes — `/cliente/listar` |
| 05 | Clientes | Cadastro / visualizar / editar no mesmo componente — `/cliente/cadastro` e `/cliente/editar/:uuid` |
| 06 | Clientes / CRM | Base de CRM, Comunicação e Questionamento de pesquisa; preservar submenus, rotas e funcionalidades próprias |
| 07 | Administração | Empresa / Filiais |
| 08 | Administração | Usuários / Funcionários / Cargos / Permissões, com visões e autorização próprias |
| 09 | Administração | Fornecedores / Seguradoras / Transportadoras |
| 10 | Administração | Áreas / Configurações |
| 11 | Oficina | Agenda |
| 12 | Oficina | Orçamentos — listagem |
| 13 | Oficina | Orçamentos — editor, serviços/peças e aprovação |
| 14 | Oficina | Ordens de Serviço — listagem |
| 15 | Oficina | OS — detalhes, execução e acompanhamento |
| 16 | Oficina | Diagnósticos / Inspeções; preservar workflows específicos |
| 17 | Oficina | Serviços — catálogo |
| 18 | Oficina | Veículos |
| 19 | Oficina | Entregas |
| 20 | Oficina | Vendas (venda direta/PDV, sem gerar OS artificial) |
| 21 | Oficina | Histórico |
| 22 | Estoque & Compras | Estoque / Movimentações |
| 23 | Estoque & Compras | Produtos / Kits |
| 24 | Estoque & Compras | Compras / Cotações |
| 25 | Estoque & Compras | Inventário |
| 26 | Financeiro | Contas / Lançamentos e Movimento Bancário |
| 27 | Financeiro | Contas a Receber / Contas a Pagar |
| 28 | Financeiro | Movimento de Caixa |
| 29 | Financeiro | Comissões |
| 30 | Fiscal | Documentos fiscais: NF-e / NFC-e-Cupom / NFS-e; modelos e fluxos fiscais específicos |
| 31 | Fiscal | Manifestação |
| 32 | Fiscal | Inutilização |
| 33 | Fiscal | NCM |
| 34 | Fiscal | SINTEGRA |
| 35 | Relatórios | Tela-base compartilhada para Comercial, Clientes, Serviços, Estoque, Compras, Financeiro, Comissões, Produtividade, Fiscal e Gerenciais |
| 36 | Gráficos Gerenciais | Tela única configurável com gráficos e filtros |
| 37 | Inteligência Artificial | Assistente |
| 38 | Inteligência Artificial | Automações IA |

**Gestão de Pátio ↗:** planejamento próprio de um sistema separado, fora das 38 telas-base.

## Documentação obrigatória antes da implementação (quando a tela estiver ausente ou incompleta no Notion)

**Nem todas as telas previstas têm especificação concluída no Notion ou implementação pronta no repositório. Isso não impede o Work de avançar: antes de escrever código, ele deve criar ou completar a especificação canônica daquela tela.**

1. **Localizar primeiro:** pesquisar em **03.01.04 Telas & UX → módulo oficial → tela**, ler o UI-MASTER-001 e a arquitetura congelada 03.01.03; verificar também componentes, rotas, APIs, entidades e migrações existentes na `feature/develop`. Não criar página duplicada nem inventar que uma documentação incompleta está pronta.
2. **Documentar antes de desenvolver:** se a tela não existe, criar a página da tela **dentro do módulo correto no Notion**; se existe mas é superficial, complementar a página já existente. Preservar ScreenId e rotas oficiais, quando definidos. Caso ainda não haja ScreenId estabelecido, marcar a identificação proposta como **pendente de confirmação**, sem alterar o catálogo oficial nem criar novo módulo.
3. **Especificação completa e prática:** objetivo e persona; módulo owner e rota; navegação e ações; hierarquia UX/UI e layout desktop/tablet/mobile; campos (label, tipo, obrigatoriedade, máscara, valores, validações, dependências e mensagens); tabelas (colunas, formato, ordenação, filtros, busca, paginação e ações); componentes/ícones/tokens; loading/vazio/erro/offline/conflito; regras de negócio e cálculos; fluxos; permissões e multiempresa; entidades, tabelas/colunas em português e relacionamentos; APIs/DTOs e integrações; critérios de aceite, acessibilidade e testes. Distinguir **implementado e verificado** de **proposto para implementar**.
4. **Exemplos fornecidos pelo usuário:** é permitido e desejável que o usuário envie **print da tela, imagem, wireframe, protótipo/Figma, vídeo, referência de outro sistema, lista de campos, colunas ou exemplo de fluxo**. O Work deve analisar esse material, incorporá-lo à especificação como referência de UX e de campos, indicar as adaptações necessárias ao NERITECH AUTO e preservar regras e identidade próprias. Exemplo não autoriza copiar marca/código/arte de terceiros nem ignorar o modelo de dados existente.
5. **Sem exemplo:** o Work elabora a proposta com base no UI-MASTER-001, sistema existente e referências **Stripe (UX)**, **Shopmonkey (fluxos)**, **UltraCar (profundidade funcional)** e **OnMotor (Brasil)**. Deve explicitar suposições e dúvidas relevantes; perguntar somente quando faltar uma decisão de negócio que possa causar retrabalho, risco fiscal/financeiro, alteração arquitetural ou perda de dados. Não inventar API, campo obrigatório ou regra legal como se já fosse oficial.
6. **Publicar e seguir:** deixar a documentação pronta e vinculada ao módulo no Notion **antes de implementar**; então desenvolver frontend + backend + Flyway seguro, compilar, testar e homologar. Em cada etapa, informar **link da especificação criada/atualizada** e status real. Se o Notion estiver indisponível ou sem permissão de escrita, registrar a especificação detalhada no repositório como **pendente de sincronização com Notion**, sem afirmar que foi publicada.
7. **Documentação como estado oficial atual:** escrever especificações consolidadas, sem histórico de prompts, rascunhos duplicados ou telas extras. Revisar e atualizar o mesmo documento quando o código real ou as decisões aprovadas exigirem correção.

**Exemplo de pedido do usuário (opcional):** “Tela: Agenda da Oficina. Quero a aparência deste print e os campos Data, Hora, Cliente, Veículo, Serviço, Mecânico, Status e Observações.” O Work deve **conferir a agenda existente**, detalhar validações, filtros, agenda responsiva, estados, permissões, conflitos de horário e contratos reais, registrar a especificação no Notion e só então implementar. O exemplo orienta a proposta; não substitui a revisão das regras de negócio.

## Critério de conclusão por tela

1. **Verificar e, se necessário, criar/completar a especificação da tela no Notion antes de codificar**, considerando prints, protótipos e exemplos de campos enviados pelo usuário. Depois inspecionar frontend, backend, APIs, entidades, esquema do banco, Flyway e testes na `feature/develop`.
2. Implementar **tela e backend funcional juntos**; ajustar modelos, endpoints, DTOs, permissões e validações. Preservar isolamento multiempresa e processos oficiais. Reutilizar componentes com UX Stripe/Shopmonkey/UltraCar/OnMotor segundo a responsabilidade de cada referência.
3. Verificar nomes de tabelas e colunas em português; criar novas migrações Flyway seguras quando necessário e validar compatibilidade com dados e consultas existentes.
4. Executar build Angular e compilação/testes Maven, testar API, integração, banco/Flyway, estados de erro e funcionamento real. Usar preview rotulado apenas para testes visuais quando backend não estiver disponível.
5. Validar no navegador em desktop, tablet e celular, com navegação por teclado. Corrigir problemas encontrados; não alegar validação que não ocorreu.
6. Fazer commit na `feature/develop`, registrar evidências de compilação, testes, migrações e funcionalidade, informar bloqueios e sincronizar documentação Notion. **Não avançar como '100% pronto' enquanto a integração ou os testes falharem.**

## Como pedir no ChatGPT Work

Peça para ler **este arquivo** na `feature/develop` e iniciar pelo item 00 ou por uma tela numerada. A documentação Notion é a fonte de verdade para navegação e escopo, e este arquivo apenas define sequência e reuso de tela-base.

**Prompt mínimo (após conectar GitHub e Notion ao Work):**
> Desenvolva a próxima tela da NERITECH AUTO na `feature/develop` conforme este arquivo. **Se faltar documentação da tela no Notion, crie-a primeiro com campos, regras, UX, APIs e testes, usando meus exemplos quando enviados.** Aplique UI-MASTER-001 e referências Stripe, Shopmonkey, UltraCar e OnMotor; implemente frontend, backend e Flyway seguro, valide builds/testes e faça commit. Preserve a arquitetura oficial e informe links e pendências.
