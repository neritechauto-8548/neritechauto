# 03.01.03 🧩 Módulos

> **Cópia de consulta do Notion, gerada em 2026-10-14.** Fonte canônica: https://app.notion.com/p/80101fd65a9c833580f1012ec0a45407. Não editar esta cópia como norma principal.
> **Última edição registrada no Notion:** 2026-10-13T11:27:47.000Z.


# 🧩 Arquitetura Oficial de Módulos — NERITECH AUTO
## Princípio
Esta página é a fonte canônica da arquitetura, do menu e das responsabilidades funcionais do NERITECH AUTO.
# 🧭 Estrutura oficial de navegação
## 🔧 Gestão de Pátio
Tela interna do NERITECH AUTO, acessível diretamente pelo menu principal, na mesma aplicação e sessão. Reúne em uma única experiência operacional o Kanban de veículos/OS, etapas e posições, boxes e recursos, apontamentos, alertas e visão de painel TV por meio de áreas, painéis e modos de visualização internos. Não é produto independente, sistema vinculado, link externo ou módulo comercial separado. Cliente, veículo e OS continuam com seus proprietários canônicos, sem duplicação de dados.
## 🏠 Início
Dashboard central em uma única tela com quatro visões:
- Padrão
- Gerencial
- Financeiro
- Orçamento
Consulta e consolida informações. Não possui Ações Rápidas e não executa operações transacionais.
## 👥 Clientes
### Clientes
Cadastro, consulta e gestão de clientes, incluindo visualização dos veículos vinculados.
### CRM
Relacionamento com clientes.
#### Comunicação
- histórico de comunicações;
- registros de contato;
- opções de envio;
- e-mails enviados;
- demais registros de comunicação.
#### Questionamento de pesquisa
Questionários e pesquisas de satisfação/relacionamento.
## 🚗 Oficina
Núcleo da operação da oficina.
### Agenda
Compromissos e agendamentos.
### Orçamentos
Criação, edição, envio, acompanhamento e aprovação.
### Ordens de Serviço
Execução e acompanhamento das OS.
### Diagnósticos
Diagnósticos realizados nos veículos.
### Inspeções
Checklists e inspeções.
### Serviços
Catálogo e execução dos serviços.
### Veículos
Consulta e gestão dos veículos relacionados à operação.
### Entregas
Controle da entrega do veículo.
### Vendas
Venda direta de peças/produtos quando o cliente compra sem necessariamente abrir um serviço. Pertence à Oficina e integra com Estoque & Compras, Financeiro e Fiscal.
### Histórico
Consulta do histórico operacional.
## 📦 Estoque & Compras
### Estoque
Posição e controle do estoque.
### Produtos
Cadastro e gestão do catálogo.
### Kits
Composição e gestão de kits.
### Compras
Processo de compras.
### Cotações
Solicitação, comparação e acompanhamento.
### Inventário
Contagem e conferência.
### Movimentações
Entradas, saídas e demais movimentações de estoque.
## 💰 Financeiro
### Contas
Gestão das contas financeiras.
### Contas a Receber
Títulos e recebimentos.
### Contas a Pagar
Obrigações e pagamentos.
### Lançamentos Bancários
Registros bancários.
### Movimento de Caixa
Entradas, saídas e movimentação de caixa.
### Movimento Bancário
Movimentações e conciliação bancária conforme o escopo financeiro.
### Comissões
Cálculo, acompanhamento e gestão de comissões.
## 🧾 Fiscal
### Documentos Fiscais
Central fiscal.
### NF-e
Notas fiscais eletrônicas.
### NFC-e / Cupom
Documentos fiscais de consumidor.
### NFS-e
Notas fiscais de serviço.
### Manifestação
Manifestações fiscais.
### Inutilização
Inutilização de numeração fiscal.
### NCM
Classificação fiscal de produtos.
### SINTEGRA
Rotinas e informações relacionadas ao SINTEGRA.
## 🤖 Inteligência Artificial
### Assistente
Assistente de IA do NERITECH.
### Automações IA
Automações autorizadas.
A IA respeita tenant, empresa, unidade, usuário, capability e autorização das APIs proprietárias.
## 📊 Gráficos Gerenciais
Camada de análise visual. Não é fonte transacional.
## 📊 Relatórios
Camada de consulta, consolidação e exportação.
- Comercial
- Clientes
- Serviços
- Estoque
- Compras
- Financeiro
- Comissões
- Produtividade
- Fiscal
- Gerenciais
## ⚙️ Administração
- Empresa
- Usuários
- Funcionários
- Cargos
- Permissões
- Áreas
- Fornecedores
- Seguradoras
- Transportadoras
- Configurações
Administração controla cadastros estruturais e configurações administrativas.
# 🔒 Regras arquiteturais
1. O menu oficial segue exatamente esta estrutura.
2. Nenhuma tela cria módulo novo.
3. Nenhuma tela move módulo.
4. Referências externas não alteram a arquitetura.
5. Cada operação possui um owner.
6. Outros módulos consultam dados do owner por APIs/read models autorizados.
7. Gestão de Pátio é uma tela interna única do NERITECH AUTO, no menu principal, com funcionalidades internas e sem aplicação ou produto separado.
8. Início possui uma tela e quatro visões.
9. Vendas pertence à Oficina.
10. Comunicação e e-mails pertencem ao CRM.
11. Não existe módulo separado de “Clientes (veículos)”.
12. Financeiro é responsável pelos fatos financeiros.
13. Estoque & Compras é responsável pelos fatos de estoque e compras.
14. Fiscal é responsável pelos fatos fiscais.
15. IA auxilia os owners e não cria fonte transacional paralela.
16. Gráficos Gerenciais e Relatórios são camadas de consulta/análise.
17. Administração controla configurações e cadastros administrativos.
18. Toda nova tela, prompt, wireframe ou implementação respeita esta estrutura.
# Ownership
**O módulo proprietário executa e altera. As outras superfícies consultam, consolidam ou encaminham.**
# Padrão documental
Cada módulo será documentado por telas no padrão:
**Módulo → Tela → UX/UI → Campos → Tabelas → Colunas → Componentes → Ícones → Regras de negócio → Fluxos → Permissões → APIs → Dados → Integrações → Estados → Critérios de aceite → Especificação para desenvolvimento.**

