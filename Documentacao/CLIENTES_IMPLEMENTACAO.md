# NERITECH AUTO — Módulo Clientes

## Objetivo
Implementar cadastro e consulta de clientes na arquitetura oficial, reutilizando três rotas canônicas e evitando uma tela independente de detalhes.

## Rotas canônicas
- `/cliente/listar` — pesquisa, filtros, paginação e ações contextuais.
- `/cliente/cadastro` — criação de pessoa física ou jurídica.
- `/cliente/editar/:uuid` — consulta e edição no mesmo formulário.

A rota base `/cliente` redireciona para `/cliente/listar`. Não criar rota/tela paralela de detalhes.

## CRM na etapa 06

CRM permanece sob **Clientes**, sem tela cadastral duplicada nem módulo novo. `/cliente/crm`, `/cliente/crm/comunicacao` e `/cliente/crm/pesquisa` estão registradas em `cliente.routes.ts`. A Comunicação [CLI-005](https://app.notion.com/p/3eb01fd65a9c81689815eef6a0e269b2) possui histórico paginado **somente leitura** por `GET /v1/comunicacao/envios`, com filtros reais de canal, status, datas e cliente (`clienteId` + empresa autenticada); o formulário único de cliente oferece link ao histórico filtrado. A pesquisa [CLI-006](https://app.notion.com/p/3eb01fd65a9c819f88f7e4449a92cdfe) reutiliza a tela de questionários e perguntas existente. Integrações de envio, retry, respostas, indicadores e homologação com provedores externos continuam pendentes. Páginas legadas `/clientes/...` ou `CLI-004` não são canônicas.

## Situação técnica da Prioridade 3 (implementação em curso)

- Comunicação e Pesquisa utilizam os controllers reais `/v1/comunicacao/envios`, `/v1/comunicacao/questionarios` e `/v1/comunicacao/it-questionario`.
- A classe `TenantAccess` compara empresa solicitada e relacionamento do registro com o `TenantContext` autenticado; há testes automatizados para tentativas de acesso entre empresas.
- `GET` dessas APIs exige `ADMIN` ou `GERAL_USUARIO`; `POST/PUT/DELETE` exige `ADMIN` ou `GERAL_CONFIG_SISTEMA`. A matriz deve ser validada com perfis de negócio reais.
- **Comunicação:** a tela aplica filtros servidor de `clienteId`, `canal`, `status`, `dataInicio` e `dataFim`, por `JpaSpecificationExecutor`, sempre restritos ao tenant. Envio por SMTP executa de forma síncrona; status `ENVIADA` só após aceitação pelo servidor SMTP, nunca como comprovante de entrega. Falhas recebem `FALHOU`; WhatsApp/SMS/outros sem integração rejeitam a operação, sem falsificar `ENVIADA`. Alterações via API não podem forjar `ENTREGUE`. Necessários provedores, entrega verificável e idempotência antes de produção.
- **Pesquisas:** leitura permanece acessível aos perfis permitidos e a interface oculta ações de criação/edição/exclusão se ausentes `ADMIN` ou `GERAL_CONFIG_SISTEMA`; backend mantém verificação de escopo e permissões. Publicação e coleta de respostas ainda são propostas.
- **Contatos e endereços:** controllers usam autorização por perfil; a atualização de contato passou a validar o vínculo com o cliente da URL **antes** de salvar, com teste de regressão.
- `FrontEnd/public/data/menu.json` tem agora CRM → Comunicação e Questionamento de pesquisa dentro de Clientes; a reestruturação do menu principal legado é uma pendência da Application Shell.
- **Veículos:** `GET /v1/veiculos?clienteId=...`, `POST /v1/veiculos` e edição/exclusão existentes no formulário e backend; vínculos agora utilizam cliente com escopo autenticado, busca de veículo por `id + empresaId` e falha fechada sem tenant. Testes unitários impedem listar fora da sessão, vincular a cliente de outra empresa e consultar/alterar/excluir veículo fora do escopo; falta QA E2E com banco e perfis reais.
- **Documentos:** implementados `GET /v1/clientes/{clienteId}/documentos`, `GET /{id}`, `POST /upload`, `GET /{id}/download` e `DELETE /{id}` em `DocumentoClienteController`, com tabela `documentos_clientes` (migração V264), filtro explícito por empresa e cliente, tamanho máximo de 5 MB, formatos PDF/JPG/PNG e download como anexo. O formulário existente agora possui aba Documentos com upload, listagem paginada, download e exclusão. Requer homologação real com PostgreSQL/migrações e políticas de retenção e armazenamento. O frontend ainda contém métodos conceituais `POST/PUT` apenas de metadados que **não devem ser tratados como APIs confirmadas**.
- Não confundir CI verde com homologação visual, configuração dos provedores de envio, testes de integração real ou conclusão de todas as funcionalidades CRM.

## Lista de clientes
- Pesquisa por nome, razão social, nome fantasia, CPF/CNPJ e e-mail quando permitido.
- Filtros existentes na UI: tipo de cliente e status.
- Paginação server-side no ambiente conectado, com índice inicial `page=0` no backend e numeração visual começando em 1; paginação local simulada no modo de homologação.
- Ações de visualizar/editar, agendamento, veículos e OS continuam encaminhando para os módulos proprietários.
- Estados de carregamento, lista vazia e nenhum resultado precisam ser diferenciados.
- A tabela exibe cliente, documento, contato, status e menu de ações.

## Formulário
### Dados gerais
- Tipo de pessoa: PF/PJ.
- Nome completo ou razão social; nome fantasia para PJ.
- CPF/CNPJ com validação.
- E-mail, data de nascimento e campos complementares de PF.
- Inscrições estadual/municipal para PJ.
- Origem, status e observações internas.
### Endereço
- CEP, logradouro, número, complemento, bairro, cidade e UF.
- CEP, logradouro, número, bairro, cidade e UF são validados conforme a regra atual da tela.
### Contatos
- Lista de múltiplos contatos, com inclusão, edição e exclusão.
- O contrato HTTP atual do backend recebe `tipoContato` e `contato`; o modelo de UI pode normalizar o valor em `valor`.

## Contrato de API confirmado no repositório
- `GET /v1/clientes` — lista paginada do `ClienteController`, com parâmetros realmente aceitos: `busca`, `nomeCompleto` (legado), `razaoSocial`, `cpf`, `cnpj`, `tipoCliente`, `status`, `dataInicio`, `dataFim`; paginação Spring `page` (zero-based), `size` e `sort`. Retorna `Page<ClienteResponse>`. Não trocar esses nomes por `search`, `pageSize`, `order`, `type` ou filtros propostos sem contrato implementado.
- `GET /v1/clientes/{id}` — consulta.
- `POST /v1/clientes` — criação.
- `PUT /v1/clientes/{id}` — atualização.
- `DELETE /v1/clientes/{id}` — exclusão.
- `GET/POST /v1/clientes/{clienteId}/contatos`, `GET/PUT/DELETE /v1/clientes/{clienteId}/contatos/{id}`.
- `GET/POST /v1/clientes/{clienteId}/enderecos`, `GET/PUT/DELETE /v1/clientes/{clienteId}/enderecos/{id}`.
- Headers de autenticação e tenant são tratados pelos interceptors existentes; não duplicar a responsabilidade na tela.
- **Identificador:** o caminho Angular usa o nome legado `:uuid`, mas `ClienteController` recebe `@PathVariable Long id`. Validar/converter esse segmento, sem assumir UUID no banco.
- Filtros avançados de origem, cidade, unidade, veículo, pendência cadastral e busca por placa são **evoluções propostas** até haver suporte efetivo no backend. Não simular filtragem server-side inexistente.

## Modo de homologação UX
O modo é controlado por `environment.uxPreview` e deve estar habilitado somente no ambiente de desenvolvimento/homologação. A lista e o formulário exibem aviso explícito de dados demonstrativos.
- Busca, tipo, status e paginação são simulados sobre dados locais.
- Criar, editar, excluir cliente e manter endereços/contatos alteram apenas a memória do serviço durante a sessão.
- Em preview, ações de navegação não devem depender de permissões carregadas do backend; no ambiente real, a validação de permissões permanece obrigatória.
- A atualização dos dados de demonstração não é persistida após recarregar a aplicação.
- O modo de homologação não deve ser confundido com persistência real; produção deve usar a API.

## Layout canônico
- Todas as telas autenticadas utilizam o mesmo AdminLayout, sidebar, breadcrumb e barra superior da NERITECH AUTO.
- Listagem: título e contexto à esquerda, ação principal à direita; pesquisa e filtros em toolbar compacta, com labels acessíveis.
- Cadastro/edição: uma única implementação, com cabeçalho no mesmo padrão da listagem, abas Geral / Endereço / Contatos e ações Salvar, Cancelar e Excluir quando permitido.
- UI-MASTER-001 define a linguagem visual; azul NERITECH (#2563EB) identifica foco e ações primárias, enquanto as cores de status são semânticas.
- Estado de salvamento deve bloquear cliques duplicados até terminar a operação principal e os dados adicionais; falhas liberam o formulário para nova tentativa.
- Erro de carregamento da lista aparece como mensagem recuperável com "Tentar novamente", sem apresentá-lo como lista vazia.
- Requisições antigas de listagem e contatos não devem sobrescrever resultados de filtros ou páginas mais recentes.
- Ícones de navegação e ações principais são SVG no estilo Tabler; bibliotecas de componentes existentes podem continuar servindo os controles funcionais.
- Em modo de homologação, a edição não dispara carregamento de veículos dependente do backend.

## Acessibilidade e UX
- Campos com rótulos claros, estados de foco visíveis e mensagens de validação.
- Seções do formulário são tabs acessíveis com `role=tablist`, `role=tab` e `aria-selected`.
- Não depender apenas de cor para status ou erros.
- Feedback de sucesso/erro via serviço de mensagens, sem `alert()` nativo.
- Data deve ser formatada como data local para evitar deslocamento de dia por fuso horário.

## Verificações antes de homologar
- [ ] Confirmar o build Angular e os testes Maven do commit atual no GitHub Actions; não inferir aprovação de execuções anteriores.
- [ ] Busca por nome e documento.
- [ ] Filtros de tipo e status.
- [ ] Paginação anterior/próxima e total de resultados.
- [ ] Criar/editar PF e PJ com documentos válidos.
- [ ] Endereço obrigatório e CEP.
- [ ] Incluir/editar/excluir contato.
- [ ] Reabrir um cliente existente e conferir dados. Verificar diálogo de inclusão e edição de contatos.
- [ ] Testar salvamentos repetidos: impedir dupla submissão e confirmar liberação após sucesso ou erro.
- [ ] Testar erro de servidor, tentar novamente e ausência de sobreposição por respostas antigas de páginas.
- [ ] Confirmar que modo de preview não faz gravações no backend.
- [ ] Testar permissões e isolamento por tenant no ambiente integrado.
- [ ] Conferir comportamento responsivo e teclado.
- [ ] Validar endpoints e payloads contra os DTOs reais do backend.

## Limitações conhecidas a resolver antes da homologação de produção
- O modo preview mantém as alterações em memória e reinicia os dados ao recarregar.
- A verificação de duplicidade e a regra final de permissões precisam ser confirmadas na API real.
- A carga/gestão de documentos ainda não está completa no formulário atual.
- Os filtros adicionais previstos na especificação (origem, unidade, cidade, veículo e pendência cadastral) não fazem parte da barra de filtros atualmente implementada.
- Não declarar o módulo homologado antes do build e dos testes funcionais no ambiente integrado.
