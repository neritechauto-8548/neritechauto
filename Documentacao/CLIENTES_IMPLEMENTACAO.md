# NERITECH AUTO — Módulo Clientes

## Objetivo
Implementar cadastro e consulta de clientes na arquitetura oficial, reutilizando três rotas canônicas e evitando uma tela independente de detalhes.

## Rotas canônicas
- `/cliente/listar` — pesquisa, filtros, paginação e ações contextuais.
- `/cliente/cadastro` — criação de pessoa física ou jurídica.
- `/cliente/editar/:uuid` — consulta e edição no mesmo formulário.

A rota base `/cliente` redireciona para `/cliente/listar`. Não criar rota/tela paralela de detalhes.

## Lista de clientes
- Pesquisa por nome, razão social, nome fantasia, CPF/CNPJ e e-mail quando permitido.
- Filtros existentes na UI: tipo de cliente e status.
- Paginação server-side no ambiente conectado; paginação local simulada no modo de homologação.
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
- `GET /v1/clientes` — lista paginada, com parâmetros de busca e filtros.
- `GET /v1/clientes/{id}` — consulta.
- `POST /v1/clientes` — criação.
- `PUT /v1/clientes/{id}` — atualização.
- `DELETE /v1/clientes/{id}` — exclusão.
- `GET/POST /v1/clientes/{clienteId}/contatos`, `GET/PUT/DELETE /v1/clientes/{clienteId}/contatos/{id}`.
- `GET/POST /v1/clientes/{clienteId}/enderecos`, `GET/PUT/DELETE /v1/clientes/{clienteId}/enderecos/{id}`.
- Headers de autenticação e tenant são tratados pelos interceptors existentes; não duplicar a responsabilidade na tela.

## Modo de homologação UX
O modo é controlado por `environment.uxPreview` e deve estar habilitado somente no ambiente de desenvolvimento/homologação. A lista e o formulário exibem aviso explícito de dados demonstrativos.
- Busca, tipo, status e paginação são simulados sobre dados locais.
- Criar, editar, excluir cliente e manter endereços/contatos alteram apenas a memória do serviço durante a sessão.
- A atualização dos dados de demonstração não é persistida após recarregar a aplicação.
- O modo de homologação não deve ser confundido com persistência real; produção deve usar a API.

## Acessibilidade e UX
- Campos com rótulos claros, estados de foco visíveis e mensagens de validação.
- Seções do formulário são tabs acessíveis com `role=tablist`, `role=tab` e `aria-selected`.
- Não depender apenas de cor para status ou erros.
- Feedback de sucesso/erro via serviço de mensagens, sem `alert()` nativo.
- Data deve ser formatada como data local para evitar deslocamento de dia por fuso horário.

## Verificações antes de homologar
- [ ] Build Angular aprovado no commit atual.
- [ ] Busca por nome e documento.
- [ ] Filtros de tipo e status.
- [ ] Paginação anterior/próxima e total de resultados.
- [ ] Criar/editar PF e PJ com documentos válidos.
- [ ] Endereço obrigatório e CEP.
- [ ] Incluir/editar/excluir contato.
- [ ] Reabrir um cliente existente e conferir dados.
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
