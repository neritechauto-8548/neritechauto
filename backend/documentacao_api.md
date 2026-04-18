# NeriTech API — Documentação Oficial (v1)

## Visão Geral
- Base path: `/api`
- Versão: `v1` sempre que aplicável (alguns módulos usam prefixos específicos, conforme abaixo)
- Autenticação: JWT via header `Authorization: Bearer <token>`
- Multiempresa: tenant resolvido pelo `empresaId` no token; alguns endpoints exigem `empresaId` explicitamente em query/path
- Paginação (Spring Pageable): respostas paginadas retornam `content`, `totalElements`, `totalPages`, `size`, `number`
- Content-Type: `application/json`

## Tabela de Conteúdos
- [Visão Geral](#visão-geral)
- [1. Autenticação e Usuários](#1-autenticação-e-usuários)
  - [Autenticação](#autenticação)
    - [`POST /api/auth/login`](#post-apiauthlogin)
    - [`POST /api/auth/refresh`](#post-apiauthrefresh)
    - [`POST /api/auth/logout`](#post-apiauthlogout)
  - [Usuários](#usuários)
    - [`GET /api/usuarios`](#get-apiusuarios)
    - [`GET /api/usuarios/{id}`](#get-apiusuariosid)
- [2. Clientes](#2-clientes)
  - [`GET /v1/clientes`](#get-v1clientes)
  - [`GET /v1/clientes/{id}`](#get-v1clientesid)
  - [`POST /v1/clientes`](#post-v1clientes)
  - [`PUT /v1/clientes/{id}`](#put-v1clientesid)
  - [`DELETE /v1/clientes/{id}`](#delete-v1clientesid)
  - [`GET /v1/clientes/{clienteId}/contatos/{id}`](#get-v1clientesclienteidcontatosid)
- [3. Veículos](#3-veículos)
  - [Veículos](#veículos)
    - [`GET /v1/veiculos`](#get-v1veiculos)
    - [`GET /v1/veiculos/{id}`](#get-v1veiculosid)
    - [`POST /v1/veiculos`](#post-v1veiculos)
    - [`PUT /v1/veiculos/{id}`](#put-v1veiculosid)
    - [`DELETE /v1/veiculos/{id}`](#delete-v1veiculosid)
  - [Marcas de Veículos](#marcas-de-veículos)
    - [`GET /v1/marcas-veiculos`](#get-v1marcas-veiculos)
    - [`GET /v1/marcas-veiculos/{id}`](#get-v1marcas-veiculosid)
    - [`PUT /v1/marcas-veiculos/{id}`](#put-v1marcas-veiculosid)
  - [Modelos de Veículos](#modelos-de-veículos)
    - [`POST /v1/modelos-veiculos`](#post-v1modelos-veiculos)
    - [`PUT /v1/modelos-veiculos/{id}`](#put-v1modelos-veiculosid)
    - [`GET /v1/modelos-veiculos/{id}`](#get-v1modelos-veiculosid)
    - [`GET /v1/modelos-veiculos`](#get-v1modelos-veiculos)
    - [`DELETE /v1/modelos-veiculos/{id}`](#delete-v1modelos-veiculosid)
  - [Tipos de Combustível](#tipos-de-combustível)
  - [Documentos de Veículo](#documentos-de-veículo)
  - [Fotos de Veículo](#fotos-de-veículo)
- [4. Ordens de Serviço (OS)](#4-ordens-de-serviço-os)
  - [`POST /api/ordens-servico`](#post-apiordens-servico)
  - [`GET /api/ordens-servico/{id}`](#get-apiordens-servicoid)
  - [`GET /api/ordens-servico/empresa/{empresaId}`](#get-apiordens-servicoempresaempresaid)
  - [`GET /api/ordens-servico/empresa/{empresaId}/status/{statusId}`](#get-apiordens-servicoempresaempresaidstatusstatusid)
  - [`PUT /api/ordens-servico/{id}`](#put-apiordens-servicoid)
  - [`DELETE /api/ordens-servico/{id}`](#delete-apiordens-servicoid)
- [5. Produtos & Serviços](#5-produtos--serviços)
  - [Produtos](#produtos)
    - [`POST /api/v1/produtos`](#post-apiv1produtos)
    - [`GET /api/v1/produtos/{id}`](#get-apiv1produtosid)
    - [`GET /api/v1/produtos?empresaId=...`](#get-apiv1produtosempresaid)
    - [`PUT /api/v1/produtos/{id}`](#put-apiv1produtosid)
    - [`DELETE /api/v1/produtos/{id}`](#delete-apiv1produtosid)
  - [Serviços](#serviços)
    - [`POST /api/v1/servicos`](#post-apiv1servicos)
    - [`GET /api/v1/servicos/{id}`](#get-apiv1servicosid)
    - [`GET /api/v1/servicos?empresaId=...`](#get-apiv1servicosempresaidpage0size10sortnomeasc)
    - [`PUT /api/v1/servicos/{id}`](#put-apiv1servicosid)
    - [`DELETE /api/v1/servicos/{id}`](#delete-apiv1servicosid)
  - [Categorias de Produtos](#categorias-de-produtos)
  - [Categorias de Serviços](#categorias-de-serviços)
  - [Unidades de Medida, Tabelas de Preço, Preços de Serviços, Tempos de Serviços, Kits de Serviços](#unidades-de-medida-tabelas-de-preço-preços-de-serviços-tempos-de-serviços-kits-de-serviços)
- [6. Estoque](#6-estoque)
  - [`POST /api/v1/estoques`](#post-apiv1estoques)
  - [`GET /api/v1/estoques/{id}`](#get-apiv1estoquesid)
  - [`GET /api/v1/estoques/empresa/{empresaId}`](#get-apiv1estoquesempresaempresaid-paginado)
  - [`GET /api/v1/estoques/empresa/{empresaId}/status/{status}`](#get-apiv1estoquesempresaempresaidstatusstatus-paginado)
  - [`GET /api/v1/estoques/empresa/{empresaId}/produto/{produtoId}`](#get-apiv1estoquesempresaempresaidprodutoprodutoid-paginado)
  - [`PUT /api/v1/estoques/{id}`](#put-apiv1estoquesid)
  - [`DELETE /api/v1/estoques/{id}`](#delete-apiv1estoquesid)
- [7. Financeiro](#7-financeiro)
  - [Contas a Receber](#contas-a-receber)
  - [Contas a Pagar](#contas-a-pagar)
  - [Contas Bancárias](#contas-bancárias)
  - [Outros módulos](#outros-módulos-do-financeiro)
- [8. Integração Front-End (Angular)](#8-integração-front-end-angular)
- [8. Fornecedores](#8-fornecedores)
- [9. Configurações do Sistema](#9-configurações-do-sistema)
- [10. Multiempresa / Tenant](#10-multiempresa--tenant)
- [11. Logs & Auditoria](#11-logs--auditoria)
- [Erros Comuns](#erros-comuns)
- [Notas para o Frontend (Angular)](#notas-para-o-frontend-angular)
- [Tabelas Detalhadas de Modelos](#tabelas-detalhadas-de-modelos)
  - [UsuarioResponse](#usuarioresponse)
  - [ClienteResponse](#clienteresponse)
  - [VeiculoResponse](#veiculoresponse)
  - [ModeloVeiculoResponse](#modeloveiculoresponse)
  - [ModeloVeiculoRequest](#modeloveiculorequest)
  - [TipoCombustivelResponse](#tipocombustivelresponse)
  - [DocumentoVeiculoResponse](#documentoveiculoresponse)
  - [DocumentoVeiculoRequest](#documentoveiculorequest)
  - [FotoVeiculoResponse](#fotoveiculoresponse)
  - [FotoVeiculoRequest](#fotoveiculorequest)
  - [ProdutoResponse](#produtoresponse)
  - [ProdutoRequest](#produtorequest)
  - [ServicoResponse](#servicoresponse)
  - [ServicoRequest](#servicorequest)
  - [EstoqueResponse](#estoqueresponse)
  - [ContasReceberResponse](#contasreceberresponse)
  - [ContaBancariaResponse](#contabancariaresponse)
  - [ContaBancariaRequest](#contabancariarequest)

---

## 1. Autenticação e Usuários

### Autenticação

#### POST `/api/auth/login`
- Descrição: autentica usuário e retorna tokens
- Body (LoginRequest)

Parâmetro | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`email` | string | sim | `joao.silva@empresa.com` | E-mail do usuário
`senha` | string | sim | `MeuSegredo@123` | Senha do usuário
`ipAddress` | string | não | `201.17.22.45` | IP do cliente (será inferido se omitido)
`userAgent` | string | não | `Mozilla/5.0 ...` | User-Agent (será inferido se omitido)

Exemplo de envio
```json
{
  "email": "joao.silva@empresa.com",
  "senha": "MeuSegredo@123"
}
```

Resposta 200 (LoginResponse)

Campo | Tipo | Exemplo | Descrição
--- | --- | --- | ---
`accessToken` | string | `eyJhbGci...` | Token JWT de acesso
`refreshToken` | string | `eyJhbGci...` | Token de refresh
`usuarioId` | number | `42` | ID do usuário
`empresaId` | number | `10` | ID do tenant (empresa)
`nomeCompleto` | string | `João da Silva` | Nome
`roles` | array<string> | `["ROLE_ADMIN"]` | Perfis
`permissoes` | array<string> | `["USUARIO.READ"]` | Permissões
`expiraEm` | string | `2025-01-10T10:00:00` | Expiração aproximada
`primeiroAcesso` | boolean | `false` | Flag de primeiro acesso
`deveTrocarSenha` | boolean | `false` | Flag de troca de senha

Exemplo de retorno
```json
{
  "accessToken": "eyJhbGciOiJI...",
  "refreshToken": "eyJhbGciOiJI...",
  "usuarioId": 42,
  "empresaId": 10,
  "nomeCompleto": "João da Silva",
  "roles": ["ROLE_ADMIN"],
  "permissoes": ["USUARIO.READ", "CLIENTE.WRITE"],
  "expiraEm": "2025-12-11T10:00:00",
  "primeiroAcesso": false,
  "deveTrocarSenha": false
}
```

Erros possíveis: 400 (validação), 401 (credenciais inválidas), 403 (bloqueado), 500

Observações
- Enviar sempre `Authorization: Bearer <accessToken>` após login
- O `empresaId` é propagado no JWT e usado para scoping de dados

#### POST `/api/auth/refresh`
- Descrição: gera novo access token a partir de um refresh válido
- Body (RefreshTokenRequest)

Parâmetro | Tipo | Obrigatório | Exemplo
--- | --- | --- | ---
`refreshToken` | string | sim | `eyJhbGci...`

Resposta 200: mesmo modelo de `LoginResponse` (com novo `accessToken`)

Erros: 400, 401 (token inválido/expirado), 500

#### POST `/api/auth/logout`
- Descrição: invalida a sessão atual
- Headers: `Authorization: Bearer <accessToken>`
- Resposta 204

### Usuários

#### GET `/api/usuarios`
- Descrição: lista usuários
- Resposta 200: `array<UsuarioResponse>`

Campo | Tipo | Exemplo
--- | --- | ---
`id` | number | `42`
`empresaId` | number | `10`
`nomeCompleto` | string | `João da Silva`
`email` | string | `joao.silva@empresa.com`
`ativo` | boolean | `true`
`bloqueado` | boolean | `false`
`ultimoAcesso` | string | `2025-12-09T09:10:00`
`funcoes` | array<string> | `["ADMIN"]`
`cargo` | string | `Gerente`
`departamento` | string | `Atendimento`
`telefone` | string | `+55 11 91234-5678`
`avatarUrl` | string | `https://.../avatar.png`

#### GET `/api/usuarios/{id}`
- Path params: `id` (number)
- Resposta 200: `UsuarioResponse`

---

## 2. Clientes

Base: `/v1/clientes`

#### GET `/v1/clientes`
- Descrição: lista clientes com filtros (paginado)
- Query params

Parâmetro | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`nomeCompleto` | string | não | `João da Silva` | Filtro por nome
`razaoSocial` | string | não | `Oficina NeriTech LTDA` | Filtro por razão social
`cpf` | string | não | `123.456.789-00` | CPF
`cnpj` | string | não | `12.345.678/0001-90` | CNPJ
`tipoCliente` | string | não | `PESSOA_FISICA` | PF/PJ
`status` | string | não | `ATIVO` | Status do cliente
`page` | number | não | `0` | Página
`size` | number | não | `20` | Tamanho
`sort` | string | não | `nomeCompleto,asc` | Ordenação

Resposta 200: Page de `ClienteResponse`

Exemplo de retorno (paginado)
```json
{
  "content": [
    {
      "id": 1,
      "empresaId": 10,
      "tipoCliente": "PESSOA_FISICA",
      "nomeCompleto": "João da Silva",
      "cpf": "123.456.789-00",
      "sexo": "M",
      "status": "ATIVO",
      "observacoesGerais": "Cliente fidelizado"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 20,
  "number": 0
}
```

Modelo ClienteResponse (campos principais)

Campo | Tipo | Exemplo
--- | --- | ---
`id` | number | `1`
`empresaId` | number | `10`
`tipoCliente` | string | `PESSOA_FISICA`
`nomeCompleto` | string | `João da Silva`
`nomeFantasia` | string | `Loja XPTO`
`razaoSocial` | string | `Loja XPTO LTDA`
`cpf` | string | `123.456.789-00`
`cnpj` | string | `12.345.678/0001-90`
`rg` | string | `12.345.678-9`
`inscricaoEstadual` | string | `1234567890`
`inscricaoMunicipal` | string | `987654321`
`dataNascimento` | string | `1990-05-20`
`sexo` | string | `M`
`estadoCivil` | string | `CASADO`
`profissao` | string | `Engenheiro`
`origemCliente` | string | `INDICACAO`
`detalhesOrigem` | string | `Indicado por Maria`
`status` | string | `ATIVO`
`motivoBloqueio` | string | `Pendência financeira`
`dataBloqueio` | string | `2024-11-09T12:00:00`
`observacoesGerais` | string | `...`

#### GET `/v1/clientes/{id}`
- Path params: `id` (number)
- Resposta 200: `ClienteResponse`
- Erros: 404

#### POST `/v1/clientes`
- Body (ClienteRequest)

Parâmetro | Tipo | Obrigatório | Exemplo
--- | --- | --- | ---
`tipoCliente` | string | sim | `PESSOA_FISICA`
`nomeCompleto` | string | sim | `João da Silva`
`nomeFantasia` | string | não | `Loja XPTO`
`razaoSocial` | string | não | `Loja XPTO LTDA`
`cpf` | string | não | `123.456.789-00`
`cnpj` | string | não | `12.345.678/0001-90`
`rg` | string | não | `12.345.678-9`
`inscricaoEstadual` | string | não | `1234567890`
`inscricaoMunicipal` | string | não | `987654321`
`dataNascimento` | string | não | `1990-05-20`
`sexo` | string | não | `M`
`estadoCivil` | string | não | `CASADO`
`profissao` | string | não | `Engenheiro`
`origemCliente` | string | não | `INDICACAO`
`detalhesOrigem` | string | não | `Indicado por Maria`
`status` | string | não | `ATIVO`
`motivoBloqueio` | string | não | `Pendência financeira`
`dataBloqueio` | string | não | `2024-11-09T12:00:00`
`observacoesGerais` | string | não | `...`

Exemplo de envio
```json
{
  "tipoCliente": "PESSOA_FISICA",
  "nomeCompleto": "João da Silva",
  "cpf": "123.456.789-00",
  "sexo": "M",
  "status": "ATIVO"
}
```

Resposta 201: `ClienteResponse`

Erros: 400 (validação), 409 (CPF/CNPJ duplicado)

#### PUT `/v1/clientes/{id}`
- Body: `ClienteRequest`
- Resposta 200: `ClienteResponse`
- Erros: 400, 404

#### DELETE `/v1/clientes/{id}`
- Resposta 204
- Erros: 404, 409 (vínculos)

#### GET `/v1/clientes/{clienteId}/contatos/{id}`
- Descrição: obter contato do cliente
- Path params: `clienteId`, `id`
- Resposta 200: `ContatoClienteResponse`

Observações
- Utilizar máscaras de CPF/CNPJ no frontend
- Para paginação, enviar `page`, `size`, `sort`

---

## 3. Veículos

### Veículos
Base: `/v1/veiculos`

#### GET `/v1/veiculos`
- Query params: `clienteId` (number, opcional)
- Resposta 200: `array<VeiculoResponse>`

Modelo VeiculoResponse (campos principais)

Campo | Tipo | Exemplo
--- | --- | ---
`id` | number | `100`
`clienteId` | number | `1`
`clienteNome` | string | `João da Silva`
`marcaId` | number | `10`
`marcaNome` | string | `Volkswagen`
`modeloId` | number | `501`
`modeloNome` | string | `Gol 1.6`
`anoModeloId` | number | `2020`
`anoFabricacao` | number | `2019`
`anoModelo` | number | `2020`
`placa` | string | `ABC1D23`
`renavam` | string | `12345678901`
`chassi` | string | `9BWZZZ377VT004251`
`numeroMotor` | string | `MTR-9988`
`corExterna` | string | `Prata`
`quilometragemAtual` | number | `45780`
`dataUltimaRevisao` | string | `2025-01-05`
`proximaRevisaoKm` | number | `50000`
`proximaRevisaoData` | string | `2025-06-05`
`status` | string | `ATIVO`
`valorEstimado` | number | `35000.00`
`fotoPrincipalUrl` | string | `https://.../gol.jpg`

#### GET `/v1/veiculos/{id}`
- Path: `id`
- Resposta 200: `VeiculoResponse`

#### POST `/v1/veiculos`
- Body (VeiculoRequest)

Parâmetro | Tipo | Obrigatório | Exemplo
--- | --- | --- | ---
`clienteId` | number | sim | `1`
`marcaId` | number | não | `10`
`modeloId` | number | não | `501`
`anoModeloId` | number | não | `2020`
`placa` | string | sim | `ABC1D23`
`renavam` | string | não | `12345678901`
`chassi` | string | não | `9BWZZZ377VT004251`
`numeroMotor` | string | não | `MTR-9988`
`corExterna` | string | não | `Prata`
`quilometragemAtual` | number | não | `45780`
`proximaRevisaoKm` | number | não | `50000`
`proximaRevisaoData` | string | não | `2025-06-05`
`status` | string | não | `ATIVO`
`valorEstimado` | number | não | `35000.00`
`fotoPrincipalUrl` | string | não | `https://.../gol.jpg`

Exemplo de envio
```json
{
  "clienteId": 1,
  "placa": "ABC1D23",
  "quilometragemAtual": 45780,
  "status": "ATIVO"
}
```

Resposta 201: `VeiculoResponse`

#### PUT `/v1/veiculos/{id}`
- Body: `VeiculoRequest`
- Resposta 200: `VeiculoResponse`

#### DELETE `/v1/veiculos/{id}`
- Resposta 204

### Marcas de Veículos

#### GET `/v1/marcas-veiculos`
- Query: `nome` (string, opcional), `ativo` (boolean, opcional)
- Resposta 200 (paginado): Page de `MarcaVeiculoResponse`

#### GET `/v1/marcas-veiculos/{id}`
- Resposta 200: `MarcaVeiculoResponse`

#### PUT `/v1/marcas-veiculos/{id}`
- Body: `MarcaVeiculoRequest`
- Resposta 200: `MarcaVeiculoResponse`

#### PATCH `/v1/marcas-veiculos/{id}/ativar`
- Resposta 204

#### PATCH `/v1/marcas-veiculos/{id}/desativar`
- Resposta 204

### Modelos de Veículos

#### POST `/v1/modelos-veiculos`
- Body: `ModeloVeiculoRequest`
- Resposta 201: `ModeloVeiculoResponse`

#### PUT `/v1/modelos-veiculos/{id}`
- Body: `ModeloVeiculoRequest`
- Resposta 200: `ModeloVeiculoResponse`

#### GET `/v1/modelos-veiculos/{id}`
- Resposta 200: `ModeloVeiculoResponse`

#### GET `/v1/modelos-veiculos`
- Query: `marcaId` (number, opcional); sem filtro retorna todos
- Resposta 200: `array<ModeloVeiculoResponse>`

#### DELETE `/v1/modelos-veiculos/{id}`
- Resposta 204

### Anos-Modelo

#### GET `/v1/anos-modelo?modeloId={modeloId}`
- Descrição: lista anos do modelo
- Resposta 200: `array<AnoModeloResponse>`
- Observação: sem `modeloId` retorna vazio

#### GET `/v1/anos-modelo/{id}`
- Resposta 200: `AnoModeloResponse`

#### POST `/v1/anos-modelo`
- Body: `AnoModeloRequest`
- Resposta 201: `AnoModeloResponse`

#### PUT `/v1/anos-modelo/{id}`
- Body: `AnoModeloRequest`
- Resposta 200: `AnoModeloResponse`

#### DELETE `/v1/anos-modelo/{id}`
- Resposta 204

### Tipos de Combustível

CRUD em `/v1/tipos-combustivel` (POST, PUT `/{id}`, GET `/{id}`, GET, DELETE `/{id}`)

### Documentos de Veículo

CRUD em `/v1/documentos-veiculos` com filtro `veiculoId` em GET

### Fotos de Veículo

CRUD em `/v1/fotos-veiculos` com filtro `veiculoId` em GET

Observações
- Placas devem ser enviadas sem máscara e em caixa alta (`ABC1D23`)
- Integrações com upload devem enviar URLs públicas

---

## 4. Ordens de Serviço (OS)

Base: `/api/ordens-servico`

#### POST `/api/ordens-servico`
- Body (OrdemServicoRequest)

Campo | Tipo | Obrigatório | Exemplo
--- | --- | --- | ---
`empresaId` | number | sim | `10`
`numeroOS` | string | sim | `OS-2024-000123`
`clienteId` | number | não | `1`
`veiculoId` | number | não | `100`
`statusId` | number | não | `3`
`tipoOS` | string | sim | `MANUTENCAO`
`prioridade` | string | não | `ALTA`
`dataAbertura` | string | não | `2025-12-10T09:00:00`
`dataPromessa` | string | não | `2025-12-12T18:00:00`
`quilometragemEntrada` | number | não | `45800`
`nivelCombustivelEntrada` | string | não | `MEIO_TANQUE`
`consultorResponsavelId` | number | não | `12`
`mecanicoResponsavelId` | number | não | `27`
`equipeExecucao` | string | não | `Equipe A`
`problemaRelatado` | string | não | `Vibração ao frear`
`solucaoAplicada` | string | não | `Troca de discos e pastilhas`
`observacoesInternas` | string | não | `Aguardar peças`
`observacoesCliente` | string | não | `Precisa do carro até sexta`
`valorServicos` | number | não | `850.00`
`valorProdutos` | number | não | `450.00`
`valorDesconto` | number | não | `50.00`
`valorAcrescimo` | number | não | `0.00`
`valorTotal` | number | sim | `1250.00`
`formaPagamentoId` | number | não | `5`
`condicaoPagamentoId` | number | não | `2`
`valorEntrada` | number | não | `200.00`
`aprovadoCliente` | boolean | não | `true`
`metodoAprovacao` | string | não | `WHATSAPP`
`garantiaDias` | number | não | `90`

Exemplo de envio
```json
{
  "empresaId": 10,
  "numeroOS": "OS-2024-000123",
  "tipoOS": "MANUTENCAO",
  "valorTotal": 1250.00,
  "clienteId": 1,
  "veiculoId": 100
}
```

Resposta 201: `OrdemServicoResponse` (todos campos calculados/retornados)

#### GET `/api/ordens-servico/{id}`
- Resposta 200: `OrdemServicoResponse`

#### GET `/api/ordens-servico/empresa/{empresaId}`
- Query pageable: `page`, `size`, `sort`
- Resposta 200: Page de `OrdemServicoResponse`

#### GET `/api/ordens-servico/empresa/{empresaId}/status/{statusId}`
- Resposta 200: Page de `OrdemServicoResponse`

#### PUT `/api/ordens-servico/{id}`
- Body: `OrdemServicoRequest`
- Resposta 200: `OrdemServicoResponse`

#### DELETE `/api/ordens-servico/{id}`
- Resposta 204

Observações
- Para listas, sempre enviar `page`, `size`; `sort` suporta múltiplos campos

### Status de OS
- Base: `/api/status-os`
  - `POST /api/status-os` → cria (StatusOSRequest)
  - `GET /api/status-os/{id}` → obtém
  - `GET /api/status-os/empresa/{empresaId}` → paginação por empresa
  - `PUT /api/status-os/{id}` → atualiza
  - `DELETE /api/status-os/{id}` → remove

### Itens de OS — Serviços
- Base: `/api/itens-os-servicos`
  - `POST /api/itens-os-servicos` → cria (ItemOSServicoRequest)
  - `GET /api/itens-os-servicos/{id}` → obtém
  - `GET /api/itens-os-servicos/ordem-servico/{ordemServicoId}` → lista por OS
  - `PUT /api/itens-os-servicos/{id}` → atualiza
  - `DELETE /api/itens-os-servicos/{id}` → remove

### Itens de OS — Produtos
- Base: `/api/itens-os-produtos`
  - `POST /api/itens-os-produtos` → cria (ItemOSProdutoRequest)
  - `GET /api/itens-os-produtos/{id}` → obtém
  - `GET /api/itens-os-produtos/ordem-servico/{ordemServicoId}` → lista por OS
  - `PUT /api/itens-os-produtos/{id}` → atualiza
  - `DELETE /api/itens-os-produtos/{id}` → remove

---

## 5. Produtos & Serviços

### Produtos
Base: `/api/v1/produtos`

#### POST `/api/v1/produtos`
- Body (ProdutoRequest)

Campo | Tipo | Obrigatório | Exemplo
--- | --- | --- | ---
`empresaId` | number | sim | `10`
`categoriaId` | number | não | `8`
`unidadeMedidaId` | number | não | `3`
`codigoInterno` | string | sim | `PD-0001`
`codigoBarras` | string | não | `7891234567890`
`codigoFabricante` | string | não | `FAB-9988`
`nome` | string | sim | `Filtro de óleo`
`descricao` | string | não | `Filtro para motores 1.6`
`marca` | string | não | `Bosch`
`modelo` | string | não | `F123`
`precoCusto` | number | sim | `25.90`
`precoVenda` | number | sim | `49.90`
`estoqueMinimo` | number | não | `10`
`ncm` | string | não | `84099190`
`cest` | string | não | `12.345.67`
`origemProduto` | string | não | `0`
`fotoPrincipalUrl` | string | não | `https://.../filtro.jpg`
`garantiaMeses` | number | não | `6`
`observacoes` | string | não | `...`
`ativo` | boolean | não | `true`

Exemplo de envio
```json
{
  "empresaId": 10,
  "codigoInterno": "PD-0001",
  "nome": "Filtro de óleo",
  "precoCusto": 25.90,
  "precoVenda": 49.90,
  "ativo": true
}
```

Resposta 201: `ProdutoResponse`

#### GET `/api/v1/produtos/{id}`
- Resposta 200: `ProdutoResponse`

#### GET `/api/v1/produtos?empresaId={empresaId}&page=0&size=20`
- Resposta 200: Page de `ProdutoResponse`

#### PUT `/api/v1/produtos/{id}`
- Body: `ProdutoRequest`
- Resposta 200: `ProdutoResponse`

#### DELETE `/api/v1/produtos/{id}`
- Resposta 204

### Serviços
Base: `/api/v1/servicos`

#### POST `/api/v1/servicos`
- Body: `ServicoRequest`
- Resposta 201: `ServicoResponse`
- Regras: `empresaId` existente; `codigo` único por empresa; `categoriaId` opcional mas válido

#### GET `/api/v1/servicos/{id}`
- Resposta 200: `ServicoResponse`
- Erros: 404 (não encontrado)

#### GET `/api/v1/servicos?empresaId={empresaId}&page=0&size=10&sort=nome,asc`
- Resposta 200: `Page<ServicoResponse>`
- Observação: `empresaId` obrigatório na query

#### PUT `/api/v1/servicos/{id}`
- Body: `ServicoRequest`
- Resposta 200: `ServicoResponse`
- Regra: não é permitido alterar `empresaId`; respeitar unicidade de `codigo` por empresa

#### DELETE `/api/v1/servicos/{id}`
- Resposta 204

##### Enums permitidos
- `NivelDificuldade`: `BASICO`, `INTERMEDIARIO`, `AVANCADO`, `ESPECIALISTA`
- `TipoCobranca`: `SERVICO`, `HORA`, `KILOMETRAGEM`, `FIXO`

##### Exemplos cURL
```bash
# Listar paginado
curl "http://localhost:8080/api/v1/servicos?empresaId=1&page=0&size=10&sort=nome,asc" \
  -H "Authorization: Bearer <ACCESS_TOKEN>" -s | jq .

# Obter por ID
curl "http://localhost:8080/api/v1/servicos/42" \
  -H "Authorization: Bearer <ACCESS_TOKEN>" -s | jq .

# Criar
curl -X POST "http://localhost:8080/api/v1/servicos" \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "empresaId": 1,
    "categoriaId": 2,
    "nome": "Alinhamento e balanceamento",
    "codigo": "SRV-001",
    "precoBase": 120.00,
    "tempoExecucaoMinutos": 60,
    "ativo": true
  }' -s | jq .

# Atualizar
curl -X PUT "http://localhost:8080/api/v1/servicos/42" \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "empresaId": 1,
    "categoriaId": 5,
    "nome": "Alinhamento 3D",
    "codigo": "SRV-001",
    "ativo": true
  }' -s | jq .

# Excluir
curl -X DELETE "http://localhost:8080/api/v1/servicos/42" \
  -H "Authorization: Bearer <ACCESS_TOKEN>" -i
```

##### Erros e exemplos de resposta
```json
// 404 - Recurso não encontrado
{
  "timestamp": "2025-12-14T14:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Serviço não encontrado com ID: 42",
  "path": "/api/v1/servicos/42"
}
```
```json
// 400 - Regra de negócio/validação
{
  "timestamp": "2025-12-14T14:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Já existe um serviço com este código para esta empresa",
  "path": "/api/v1/servicos"
}
```

### Categorias de Produtos
Base: `/api/v1/categorias-produtos` (listar por `empresaId`, paginado; CRUD)

#### POST `/api/v1/categorias-produtos`
- Body: `CategoriaProdutoRequest`
- Resposta 201: `CategoriaProdutoResponse`

#### GET `/api/v1/categorias-produtos/{id}`
- Resposta 200: `CategoriaProdutoResponse`

#### GET `/api/v1/categorias-produtos?empresaId={empresaId}&page=0&size=20`
- Resposta 200: Page de `CategoriaProdutoResponse`

#### GET `/api/v1/categorias-produtos/roots?empresaId={empresaId}`
- Resposta 200: `array<CategoriaProdutoResponse>`

#### PUT `/api/v1/categorias-produtos/{id}`
- Body: `CategoriaProdutoRequest`
- Resposta 200: `CategoriaProdutoResponse`

#### DELETE `/api/v1/categorias-produtos/{id}`
- Resposta 204

### Categorias de Serviços
Base: `/api/v1/categorias-servicos` (listar por `empresaId`, paginado; CRUD)

#### POST `/api/v1/categorias-servicos`
- Body: `CategoriaServicoRequest`
- Resposta 201: `CategoriaServicoResponse`

#### GET `/api/v1/categorias-servicos/{id}`
- Resposta 200: `CategoriaServicoResponse`

#### GET `/api/v1/categorias-servicos?empresaId={empresaId}&page=0&size=20`
- Resposta 200: Page de `CategoriaServicoResponse`

#### GET `/api/v1/categorias-servicos/roots?empresaId={empresaId}`
- Resposta 200: `array<CategoriaServicoResponse>`

#### PUT `/api/v1/categorias-servicos/{id}`
- Body: `CategoriaServicoRequest`
- Resposta 200: `CategoriaServicoResponse`

#### DELETE `/api/v1/categorias-servicos/{id}`
- Resposta 204

### Unidades de Medida, Tabelas de Preço, Preços de Serviços, Tempos de Serviços, Kits de Serviços
- Bases: `/api/v1/unidades-medida`, `/api/v1/tabelas-precos`, `/api/v1/servicos-precos`, `/api/v1/tempos-servicos`, `/api/v1/kits-servicos`
- Padrão CRUD (POST, PUT `/{id}`, GET `/{id}`, GET com filtros, DELETE `/{id}`)

#### Unidades de Medida (`/api/v1/unidades-medida`)
- `POST` → cria (UnidadeMedidaRequest)
- `GET /{id}` → obtém (UnidadeMedidaResponse)
- `GET` → paginação
- `GET /ativas` → lista ativas
- `PUT /{id}` → atualiza
- `DELETE /{id}` → remove

#### Tabelas de Preço (`/api/v1/tabelas-precos`)
- `POST` → cria (TabelaPrecoRequest)
- `GET /{id}` → obtém
- `GET ?empresaId={empresaId}` → paginação por empresa
- `PUT /{id}` → atualiza
- `DELETE /{id}` → remove

#### Preços de Serviços (`/api/v1/servicos-precos`)
- `POST` → cria (ServicoPrecoRequest)
- `GET /{id}` → obtém
- `GET /servico/{servicoId}` → paginação por serviço
- `PUT /{id}` → atualiza
- `DELETE /{id}` → remove

#### Tempos de Serviços (`/api/v1/tempos-servicos`)
- `POST` → cria (TempoServicoRequest)
- `GET /{id}` → obtém
- `GET /servico/{servicoId}` → paginação por serviço
- `PUT /{id}` → atualiza
- `DELETE /{id}` → remove

#### Kits de Serviços (`/api/v1/kits-servicos`)
- `POST` → cria (KitServicoRequest)
- `GET /{id}` → obtém
- `GET ?empresaId={empresaId}` → paginação por empresa
- `PUT /{id}` → atualiza
- `DELETE /{id}` → remove

#### Produtos-Fornecedores (`/api/v1/produtos-fornecedores`)
- `POST` → cria (ProdutoFornecedorRequest)
- `GET /{id}` → obtém
- `GET /produto/{produtoId}` → paginação por produto
- `GET /fornecedor/{fornecedorId}` → paginação por fornecedor
- `PUT /{id}` → atualiza
- `DELETE /{id}` → remove

Observações
- Garantir consistência entre `categoriaId`, `unidadeMedidaId`
- Exibir valores em BRL com casas decimais

---

## 6. Estoque

Base: `/api/v1/estoques`

#### POST `/api/v1/estoques`
- Body (EstoqueRequest)

Campo | Tipo | Obrigatório | Exemplo
--- | --- | --- | ---
`empresaId` | number | sim | `10`
`produtoId` | number | sim | `1001`
`quantidadeAtual` | number | não | `50`
`fornecedorId` | number | não | `200`
`precoCustoLote` | number | não | `1230.00`
`notaFiscalNumero` | string | não | `NF-556677`
`certificadoQualidadeUrl` | string | não | `https://.../cert.pdf`
`status` | string | não | `ATIVO`
`motivoBloqueio` | string | não | `Validade próxima`
`observacoes` | string | não | `...`
`usuarioCadastro` | number | não | `42`

Resposta 201: `EstoqueResponse`

#### GET `/api/v1/estoques/{id}`
- Resposta 200: `EstoqueResponse`

#### GET `/api/v1/estoques/empresa/{empresaId}` (paginado)
- Resposta 200: Page de `EstoqueResponse`

#### GET `/api/v1/estoques/empresa/{empresaId}/status/{status}` (paginado)
- Resposta 200: Page de `EstoqueResponse`

#### GET `/api/v1/estoques/empresa/{empresaId}/produto/{produtoId}` (paginado)
- Resposta 200: Page de `EstoqueResponse`

#### PUT `/api/v1/estoques/{id}`
- Body: `EstoqueRequest`
- Resposta 200: `EstoqueResponse`

#### DELETE `/api/v1/estoques/{id}`
- Resposta 204

Observações
- Atualizações devem preservar histórico de movimentações (módulos específicos de movimentação existem)

---

## 7. Financeiro

Prefixo: `/api/financeiro/...` (alguns endpoints sem `v1`)

### Contas a Receber
Base: `/api/financeiro/contas-receber`

- GET `/?empresaId={empresaId}` (paginado)
- GET `/{id}?empresaId={empresaId}`
- POST `/?empresaId={empresaId}` Body: `ContasReceberRequest`
- PUT `/{id}?empresaId={empresaId}` Body: `ContasReceberRequest`
- DELETE `/{id}?empresaId={empresaId}`

Campos principais (ContasReceberRequest)

Campo | Tipo | Obrigatório | Exemplo
--- | --- | --- | ---
`descricao` | string | sim | `Serviço de freios`
`clienteId` | number | sim | `1`
`faturaId` | number | não | `5001`
`dataEmissao` | string | sim | `2025-12-10`
`dataVencimento` | string | sim | `2026-01-10`
`valorOriginal` | number | sim | `1250.00`
`status` | string | não | `ABERTO`
`tipoTitulo` | string | não | `NORMAL`
`formaPagamentoId` | number | não | `5`
`contaBancariaId` | number | não | `12`
`centroCustoId` | number | não | `7`
`planoContasId` | number | não | `21`
`observacoes` | string | não | `...`

Resposta 200/201: `ContasReceberResponse`

### Contas a Pagar
Base: `/api/financeiro/contas-pagar` (padrão similar a Contas a Receber)

### Contas Bancárias
Base: `/api/financeiro/contas-bancarias`

- GET `/?empresaId={empresaId}` (paginado)
- GET `/{id}?empresaId={empresaId}`
- POST `/?empresaId={empresaId}` Body: `ContaBancariaRequest`
- PUT `/{id}?empresaId={empresaId}` Body: `ContaBancariaRequest`
- DELETE `/{id}?empresaId={empresaId}`

Campos principais (ContaBancariaRequest)

Campo | Tipo | Obrigatório | Exemplo
--- | --- | --- | ---
`bancoCodigo` | string | sim | `001`
`bancoNome` | string | sim | `Banco do Brasil`
`agencia` | string | sim | `1234`
`conta` | string | sim | `56789`
`digitoConta` | string | não | `0`
`tipoConta` | string | sim | `CORRENTE`
`titularConta` | string | sim | `NeriTech Serviços`
`cpfCnpjTitular` | string | sim | `12.345.678/0001-90`
`saldoAtual` | number | não | `10000.00`
`principal` | boolean | não | `true`
`ativo` | boolean | não | `true`

Outros módulos do Financeiro
- `fluxo-caixa`, `faturas`, `itens-fatura`, `condicoes-pagamento`, `formas-pagamento`, `centros-custo`, `comissoes`, `conciliacoes`, `lancamentos-contabeis`, `pagamentos`, `parcelas-pagamento`, `plano-contas`, `nfe`, `impostos` — todos seguem padrão REST com escopo por `empresaId`

Observações
- Muitos endpoints exigem `empresaId` em query para escopo
- Valores monetários em decimal (ponto como separador)

---

## 8. Fornecedores

Base: `/api/v1/fornecedores`

- POST: criar (`FornecedorRequest`)
- GET `/{id}`: obter (`FornecedorResponse`)
- GET `/?empresaId={empresaId}&page=0&size=20`: listar (paginado)
- PUT `/{id}`: atualizar
- DELETE `/{id}`: remover

Campos principais (FornecedorRequest)

Campo | Tipo | Obrigatório | Exemplo
--- | --- | --- | ---
`empresaId` | number | sim | `10`
`tipoPessoa` | string | não | `PESSOA_JURIDICA`
`nomeFantasia` | string | sim | `Autopeças Silva`
`razaoSocial` | string | não | `Autopeças Silva LTDA`
`cpf` | string | não | `123.456.789-00`
`cnpj` | string | não | `12.345.678/0001-90`
`inscricaoEstadual` | string | não | `1234567890`
`inscricaoMunicipal` | string | não | `987654321`
`emailPrincipal` | string | não | `contato@autopecas.com`
`telefonePrincipal` | string | não | `11 3333-4444`
`celularPrincipal` | string | não | `11 91234-5678`
`website` | string | não | `https://autopecas.com`
`nomeContato` | string | não | `Maria Souza`
`cargoContato` | string | não | `Vendas`
`emailContato` | string | não | `maria@autopecas.com`
`telefoneContato` | string | não | `11 3555-7788`
`enderecoCompleto` | string | não | `Rua A, 100 - São Paulo/SP`
`cep` | string | não | `01010-010`
`cidade` | string | não | `São Paulo`
`estado` | string | não | `SP`
`prazoPagamentoDias` | number | não | `30`
`limiteCredito` | number | não | `50000.00`
`descontoPadrao` | number | não | `5.0`
`condicoesEspeciais` | string | não | `Frete grátis > R$1000`
`bancoNome` | string | não | `Caixa`
`bancoAgencia` | string | não | `1234`
`bancoConta` | string | não | `98765-0`
`bancoPix` | string | não | `contato@autopecas.com`
`classificacao` | string | não | `PREFERENCIAL`
`observacoes` | string | não | `...`
`ativo` | boolean | não | `true`

---

## 9. Configurações do Sistema

Módulos principais
- Configurações de Oficina: `/api/v1/configuracoes-oficina`
- Configurações da Empresa: `/api/v1/configuracoes-empresa`
- Configurações Fiscais: `/api/v1/configuracoes-fiscais`
- Empresas: `/v1/empresas`

Padrões
- GET `/{id}`: obter configuração
- GET `/empresa/{empresaId}`: obter por empresa
- GET (paginado quando aplicável)
- POST: criar
- PUT `/{id}`: atualizar
- DELETE `/{id}`: remover

Observações
- Usar `empresaId` como escopo quando disponível

---

## 10. Multiempresa / Tenant

- Resolução de tenant via `empresaId` no JWT (setado em `accessToken`)
- Header permitido: `X-Tenant-Id` (para cenários específicos), porém o padrão é pelo JWT
- Impacto: repositórios filtram por `empresaId`; alguns endpoints exigem `empresaId` explicitamente
- Endpoint de Empresas (`/v1/empresas`) para gestão do cadastro de tenants

Exemplo de chamada autenticada
```
GET /api/v1/produtos?empresaId=10
Authorization: Bearer eyJhbGci...
```

---

## 11. Logs & Auditoria

- Logs do Sistema: `/api/v1/logs-sistema`
  - POST: registrar log (`LogSistemaRequest`)
  - GET `/empresa/{empresaId}`: listar por empresa
- Logs de Alterações: `/api/v1/logs-alteracoes`
  - POST: registrar log de alteração (`LogAlteracaoRequest`)
  - GET `/empresa/{empresaId}`: listar por empresa

Observações
- Utilizar para auditoria e rastreabilidade de operações no frontend (ex.: ações de usuários)

---

## Erros Comuns

Código | Motivo | Observações
--- | --- | ---
`400` | Validação | Campos obrigatórios ausentes/formatos inválidos
`401` | Não autenticado | Token ausente/inválido
`403` | Proibido | Sem permissão ou usuário bloqueado
`404` | Não encontrado | Recurso inexistente
`409` | Conflito | Duplicidade, vínculos impedem operação

---

## Notas para o Frontend (Angular)
- Usar tabelas compactas e tipagem estrita ao montar forms e grids
- Paginados: sempre ler e exibir `totalElements`, `totalPages`, `size`, `number`
- Máscaras recomendadas: CPF `999.999.999-99`, CNPJ `99.999.999/9999-99`, CEP `99999-999`, placa Mercosul `AAA1A11`
- Enviar datas em ISO (`YYYY-MM-DD`), data/hora em ISO (`YYYY-MM-DDTHH:mm:ss`)
- Tratar decimal com ponto como separador
- Centralizar `empresaId` do usuário logado (JWT) e passar quando exigido por query/path

### Proxy de desenvolvimento
- Front `http://localhost:4200` → Backend `http://localhost:8080` via prefixo `/api`
- Exemplo `proxy.conf.json`:
```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true,
    "logLevel": "debug"
  }
}
```
- Inicialização: `ng serve --proxy-config proxy.conf.json`

### Exemplo de consumo (HttpClient)
```ts
import { HttpClient, HttpParams } from '@angular/common/http';

// Base resolvida via proxy: /api → 8080
const base = '/api/api/v1/servicos';

// Lista paginada
listar(empresaId: number, page = 0, size = 10, sort = 'nome,asc') {
  const params = new HttpParams()
    .set('empresaId', empresaId)
    .set('page', page)
    .set('size', size)
    .set('sort', sort);
  return this.http.get(`${base}`, { params });
}

// Buscar por ID
obter(id: number) {
  return this.http.get(`${base}/${id}`);
}

// Criar
criar(payload: ServicoRequest) {
  return this.http.post<ServicoResponse>(base, payload);
}

// Atualizar
atualizar(id: number, payload: ServicoRequest) {
  return this.http.put<ServicoResponse>(`${base}/${id}`, payload);
}

// Excluir
excluir(id: number) {
  return this.http.delete(`${base}/${id}`);
}

export interface ServicoRequest {
  empresaId: number;
  categoriaId?: number;
  nome: string;
  codigo: string;
  descricao?: string;
  nivelDificuldade?: 'BASICO' | 'INTERMEDIARIO' | 'AVANCADO' | 'ESPECIALISTA';
  tipoCobranca?: 'SERVICO' | 'HORA' | 'KILOMETRAGEM' | 'FIXO';
  descricaoDetalhada?: string;
  precoBase?: number;
  tempoExecucaoMinutos?: number;
  margemLucroPercentual?: number;
  requerElevador?: boolean;
  requerEquipamentoEspecial?: boolean;
  equipamentosNecessarios?: string;
  ferramentasNecessarias?: string;
  materiaisInclusos?: string;
  garantiaDias?: number;
  unidadeCobranca?: string;
  pontosFidelidade?: number;
  comissaoPercentual?: number;
  fotoIlustrativaUrl?: string;
  videoExplicativoUrl?: string;
  instrucoesExecucao?: string;
  checklistExecucao?: string;
  ativo?: boolean;
  destaque?: boolean;
  promocional?: boolean;
}

export interface ServicoResponse extends ServicoRequest {
  id: number;
  empresaId: number;
  categoriaNome?: string;
}

// Interceptor para anexar JWT
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('accessToken');
    if (token) {
      const authReq = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
      return next.handle(authReq);
    }
    return next.handle(req);
  }
}
```

---

## Tabelas Detalhadas de Modelos

### UsuarioResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `42` | ID do usuário
`empresaId` | number | sim | `10` | ID do tenant
`nomeCompleto` | string | sim | `João da Silva` | Nome do usuário
`email` | string | sim | `joao.silva@empresa.com` | E-mail
`ativo` | boolean | sim | `true` | Usuário ativo
`bloqueado` | boolean | sim | `false` | Usuário bloqueado
`ultimoAcesso` | string | não | `2025-12-09T09:10:00` | Último acesso
`funcoes` | array<string> | não | `["ADMIN"]` | Perfis/funções
`cargo` | string | não | `Gerente` | Cargo
`departamento` | string | não | `Atendimento` | Departamento
`telefone` | string | não | `+55 11 91234-5678` | Telefone
`avatarUrl` | string | não | `https://.../avatar.png` | Avatar

### ClienteResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `1` | ID do cliente
`empresaId` | number | sim | `10` | ID do tenant
`tipoCliente` | string | sim | `PESSOA_FISICA` | Tipo (PF/PJ)
`nomeCompleto` | string | não | `João da Silva` | Nome (PF)
`nomeFantasia` | string | não | `Loja XPTO` | Nome fantasia (PJ)
`razaoSocial` | string | não | `Loja XPTO LTDA` | Razão social (PJ)
`cpf` | string | não | `123.456.789-00` | CPF (PF)
`cnpj` | string | não | `12.345.678/0001-90` | CNPJ (PJ)
`rg` | string | não | `12.345.678-9` | RG (PF)
`inscricaoEstadual` | string | não | `1234567890` | IE (PJ)
`inscricaoMunicipal` | string | não | `987654321` | IM (PJ)
`dataNascimento` | string | não | `1990-05-20` | Data de nascimento
`sexo` | string | não | `M` | Sexo
`estadoCivil` | string | não | `CASADO` | Estado civil
`profissao` | string | não | `Engenheiro` | Profissão
`origemCliente` | string | não | `INDICACAO` | Origem
`detalhesOrigem` | string | não | `Indicado por Maria` | Detalhes da origem
`status` | string | não | `ATIVO` | Status
`motivoBloqueio` | string | não | `Pendência financeira` | Motivo de bloqueio
`dataBloqueio` | string | não | `2024-11-09T12:00:00` | Data de bloqueio
`observacoesGerais` | string | não | `...` | Observações

### VeiculoResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `100` | ID do veículo
`clienteId` | number | sim | `1` | ID do cliente
`clienteNome` | string | não | `João da Silva` | Nome do cliente
`marcaId` | number | não | `10` | Marca
`marcaNome` | string | não | `Volkswagen` | Nome da marca
`modeloId` | number | não | `501` | Modelo
`modeloNome` | string | não | `Gol 1.6` | Nome do modelo
`anoModeloId` | number | não | `2020` | Ano-modelo ID
`anoFabricacao` | number | não | `2019` | Ano de fabricação
`anoModelo` | number | não | `2020` | Ano do modelo
`placa` | string | sim | `ABC1D23` | Placa Mercosul
`renavam` | string | não | `12345678901` | Renavam
`chassi` | string | não | `9BWZZZ377VT004251` | Chassi
`numeroMotor` | string | não | `MTR-9988` | Nº do motor
`corExterna` | string | não | `Prata` | Cor
`quilometragemAtual` | number | não | `45780` | Km atual
`quilometragemCadastro` | number | não | `45000` | Km no cadastro
`dataUltimaRevisao` | string | não | `2025-01-05` | Data última revisão
`proximaRevisaoKm` | number | não | `50000` | Próxima revisão (km)
`proximaRevisaoData` | string | não | `2025-06-05` | Próxima revisão (data)
`status` | string | não | `ATIVO` | Status
`valorEstimado` | number | não | `35000.00` | Valor estimado
`dataValorEstimado` | string | não | `2025-01-05` | Data do valor
`fotoPrincipalUrl` | string | não | `https://.../gol.jpg` | Foto principal

### ModeloVeiculoResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `501` | ID do modelo
`marcaId` | number | sim | `10` | Marca
`marcaNome` | string | não | `Volkswagen` | Nome da marca
`nome` | string | sim | `Gol 1.6` | Nome do modelo
`categoria` | string | não | `HATCH` | Categoria
`segmento` | string | não | `COMPACTO` | Segmento
`numeroPortas` | number | não | `4` | Portas
`numeroLugares` | number | não | `5` | Lugares
`tipoTracao` | string | não | `DIANTEIRA` | Tração

### ModeloVeiculoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`marcaId` | number | sim | `10` | ID da marca
`nome` | string | sim | `Gol 1.6` | Nome do modelo
`categoria` | string | sim | `HATCH` | Categoria do veículo
`segmento` | string | sim | `COMPACTO` | Segmento
`numeroPortas` | number | não | `4` | Número de portas
`numeroLugares` | number | não | `5` | Número de lugares
`tipoTracao` | string | sim | `DIANTEIRA` | Tipo de tração

### TipoCombustivelResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `1` | ID
`nome` | string | sim | `Gasolina` | Nome
`sigla` | string | não | `G` | Sigla
`descricao` | string | não | `Gasolina comum` | Descrição
`ativo` | boolean | não | `true` | Ativo

### DocumentoVeiculoResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `10` | ID
`veiculoId` | number | sim | `100` | Veículo
`tipoDocumento` | string | sim | `CRLV` | Tipo
`numeroDocumento` | string | não | `123456` | Número
`orgaoEmissor` | string | não | `DETRAN` | Órgão
`dataEmissao` | string | não | `2024-01-10` | Emissão
`dataVencimento` | string | não | `2025-01-10` | Vencimento
`status` | string | não | `VALIDO` | Status
`arquivoUrl` | string | não | `https://.../crlv.pdf` | URL do arquivo
`arquivoNome` | string | não | `crlv.pdf` | Nome do arquivo
`arquivoTamanho` | number | não | `24576` | Tamanho (bytes)
`observacoes` | string | não | `...` | Observações

### DocumentoVeiculoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`veiculoId` | number | sim | `100` | ID do veículo
`tipoDocumento` | string | não | `CRLV` | Tipo do documento
`numeroDocumento` | string | não | `123456` | Número do documento
`orgaoEmissor` | string | não | `DETRAN` | Órgão emissor
`dataEmissao` | string | não | `2024-01-10` | Data de emissão
`dataVencimento` | string | não | `2025-01-10` | Data de vencimento
`status` | string | não | `VALIDO` | Status
`arquivoUrl` | string | não | `https://.../crlv.pdf` | URL do arquivo
`arquivoNome` | string | não | `crlv.pdf` | Nome do arquivo
`arquivoTamanho` | number | não | `24576` | Tamanho (bytes)
`observacoes` | string | não | `...` | Observações

### FotoVeiculoResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `15` | ID
`veiculoId` | number | sim | `100` | Veículo
`tipoFoto` | string | não | `PAINEL` | Tipo
`descricao` | string | não | `Painel do veículo` | Descrição
`arquivoUrl` | string | não | `https://.../foto.jpg` | URL
`arquivoNome` | string | não | `foto.jpg` | Nome
`arquivoTamanho` | number | não | `512000` | Tamanho (bytes)
`largura` | number | não | `1920` | Largura (px)
`altura` | number | não | `1080` | Altura (px)
`principal` | boolean | não | `true` | Principal
`ordemExibicao` | number | não | `1` | Ordem
`dataFoto` | string | não | `2025-01-10T12:00:00` | Data da foto
`usuarioUpload` | number | não | `42` | Usuário

### FotoVeiculoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`veiculoId` | number | sim | `100` | ID do veículo
`tipoFoto` | string | não | `PAINEL` | Tipo da foto
`descricao` | string | não | `Painel do veículo` | Descrição
`arquivoUrl` | string | sim | `https://.../foto.jpg` | URL do arquivo
`arquivoNome` | string | não | `foto.jpg` | Nome
`arquivoTamanho` | number | não | `512000` | Tamanho (bytes)
`largura` | number | não | `1920` | Largura (px)
`altura` | number | não | `1080` | Altura (px)
`principal` | boolean | não | `true` | Principal
`ordemExibicao` | number | não | `1` | Ordem
`dataFoto` | string | não | `2025-01-10T12:00:00` | Data/hora da foto
`usuarioUpload` | number | não | `42` | Usuário que fez upload

### ProdutoResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `1001` | ID do produto
`empresaId` | number | sim | `10` | Tenant
`categoriaId` | number | não | `8` | Categoria
`categoriaNome` | string | não | `Filtros` | Nome da categoria
`unidadeMedidaId` | number | não | `3` | Unidade
`unidadeMedidaSigla` | string | não | `UN` | Sigla da unidade
`codigoInterno` | string | sim | `PD-0001` | Código interno
`codigoBarras` | string | não | `7891234567890` | Código de barras
`codigoFabricante` | string | não | `FAB-9988` | Código do fabricante
`nome` | string | sim | `Filtro de óleo` | Nome
`descricao` | string | não | `Filtro para motores 1.6` | Descrição
`descricaoDetalhada` | string | não | `...` | Descrição detalhada
`marca` | string | não | `Bosch` | Marca
`modelo` | string | não | `F123` | Modelo
`aplicacao` | string | não | `Motor 1.6` | Aplicação
`especificacoesTecnicas` | string | não | `...` | Especificações
`pesoLiquido` | number | não | `0.2` | Peso líquido (kg)
`pesoBruto` | number | não | `0.25` | Peso bruto (kg)
`dimensoesComprimento` | number | não | `10.0` | Comprimento (cm)
`dimensoesLargura` | number | não | `8.0` | Largura (cm)
`dimensoesAltura` | number | não | `8.0` | Altura (cm)
`precoCusto` | number | sim | `25.90` | Preço de custo
`precoVenda` | number | sim | `49.90` | Preço de venda
`margemLucroPercentual` | number | não | `48.1` | Margem (%)
`estoqueMinimo` | number | não | `10` | Estoque mínimo
`estoqueMaximo` | number | não | `100` | Estoque máximo
`pontoReposicao` | number | não | `20` | Ponto de reposição
`controlaLote` | boolean | não | `false` | Controla lote
`controlaValidade` | boolean | não | `false` | Controla validade
`diasValidade` | number | não | `180` | Validade (dias)
`ncm` | string | não | `84099190` | NCM
`cest` | string | não | `12.345.67` | CEST
`origemProduto` | string | não | `0` | Origem fiscal
`fotoPrincipalUrl` | string | não | `https://.../filtro.jpg` | Foto principal
`garantiaMeses` | number | não | `6` | Garantia (meses)
`observacoes` | string | não | `...` | Observações
`ativo` | boolean | não | `true` | Ativo
`destaque` | boolean | não | `false` | Destaque
`promocional` | boolean | não | `false` | Promocional
`pontosFidelidade` | number | não | `0` | Pontos
`comissaoVendaPercentual` | number | não | `5.0` | Comissão (%)

### ProdutoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`empresaId` | number | sim | `10` | Tenant
`categoriaId` | number | não | `8` | Categoria
`unidadeMedidaId` | number | não | `3` | Unidade de medida
`codigoInterno` | string | sim | `PD-0001` | Código interno
`codigoBarras` | string | não | `7891234567890` | Código de barras
`codigoFabricante` | string | não | `FAB-9988` | Código do fabricante
`nome` | string | sim | `Filtro de óleo` | Nome
`descricao` | string | não | `Filtro para motores 1.6` | Descrição
`descricaoDetalhada` | string | não | `...` | Descrição detalhada
`marca` | string | não | `Bosch` | Marca
`modelo` | string | não | `F123` | Modelo
`aplicacao` | string | não | `Motor 1.6` | Aplicação
`especificacoesTecnicas` | string | não | `...` | Especificações técnicas
`pesoLiquido` | number | não | `0.2` | Peso (kg)
`pesoBruto` | number | não | `0.25` | Peso bruto (kg)
`dimensoesComprimento` | number | não | `10.0` | Comprimento (cm)
`dimensoesLargura` | number | não | `8.0` | Largura (cm)
`dimensoesAltura` | number | não | `8.0` | Altura (cm)
`precoCusto` | number | sim | `25.90` | Preço de custo
`precoVenda` | number | sim | `49.90` | Preço de venda
`margemLucroPercentual` | number | não | `48.1` | Margem (%)
`estoqueMinimo` | number | não | `10` | Estoque mínimo
`estoqueMaximo` | number | não | `100` | Estoque máximo
`pontoReposicao` | number | não | `20` | Ponto de reposição
`controlaLote` | boolean | não | `false` | Controla lote
`controlaValidade` | boolean | não | `false` | Controla validade
`diasValidade` | number | não | `180` | Validade em dias
`ncm` | string | não | `84099190` | NCM
`cest` | string | não | `12.345.67` | CEST
`origemProduto` | string | não | `0` | Origem fiscal
`fotoPrincipalUrl` | string | não | `https://.../filtro.jpg` | Foto principal
`garantiaMeses` | number | não | `6` | Garantia (meses)
`observacoes` | string | não | `...` | Observações
`ativo` | boolean | não | `true` | Ativo
`destaque` | boolean | não | `false` | Destaque
`promocional` | boolean | não | `false` | Promocional
`pontosFidelidade` | number | não | `0` | Pontos
`comissaoVendaPercentual` | number | não | `5.0` | Comissão (%)

### ServicoResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `2001` | ID do serviço
`empresaId` | number | sim | `10` | Tenant
`categoriaId` | number | não | `2` | Categoria
`categoriaNome` | string | não | `Mecânica` | Nome da categoria
`nome` | string | sim | `Alinhamento e balanceamento` | Nome
`codigo` | string | sim | `SRV-001` | Código
`descricao` | string | não | `...` | Descrição
`nivelDificuldade` | string | não | `MEDIA` | Dificuldade
`tipoCobranca` | string | não | `HORA` | Tipo de cobrança
`descricaoDetalhada` | string | não | `...` | Detalhes
`precoBase` | number | não | `120.00` | Preço base
`tempoExecucaoMinutos` | number | não | `60` | Tempo (min)
`margemLucroPercentual` | number | não | `35.0` | Margem (%)
`requerElevador` | boolean | não | `false` | Requer elevador
`requerEquipamentoEspecial` | boolean | não | `false` | Requer equip. especial
`equipamentosNecessarios` | string | não | `...` | Equipamentos
`ferramentasNecessarias` | string | não | `...` | Ferramentas
`materiaisInclusos` | string | não | `...` | Materiais inclusos
`garantiaDias` | number | não | `90` | Garantia
`unidadeCobranca` | string | não | `UN` | Unidade
`pontosFidelidade` | number | não | `0` | Pontos
`comissaoPercentual` | number | não | `5.0` | Comissão (%)
`fotoIlustrativaUrl` | string | não | `https://.../srv.jpg` | Foto
`videoExplicativoUrl` | string | não | `https://.../srv.mp4` | Vídeo
`instrucoesExecucao` | string | não | `...` | Instruções
`checklistExecucao` | string | não | `...` | Checklist
`ativo` | boolean | não | `true` | Ativo
`destaque` | boolean | não | `false` | Destaque
`promocional` | boolean | não | `false` | Promocional

### ServicoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`empresaId` | number | sim | `10` | Tenant
`categoriaId` | number | não | `2` | Categoria
`nome` | string | sim | `Alinhamento e balanceamento` | Nome
`codigo` | string | sim | `SRV-001` | Código
`descricao` | string | não | `...` | Descrição
`nivelDificuldade` | string | não | `MEDIA` | Dificuldade
`tipoCobranca` | string | não | `HORA` | Tipo de cobrança
`descricaoDetalhada` | string | não | `...` | Detalhes
`precoBase` | number | não | `120.00` | Preço base
`tempoExecucaoMinutos` | number | não | `60` | Tempo (min)
`margemLucroPercentual` | number | não | `35.0` | Margem (%)
`requerElevador` | boolean | não | `false` | Requer elevador
`requerEquipamentoEspecial` | boolean | não | `false` | Equipamento especial
`equipamentosNecessarios` | string | não | `...` | Equipamentos
`ferramentasNecessarias` | string | não | `...` | Ferramentas
`materiaisInclusos` | string | não | `...` | Materiais
`garantiaDias` | number | não | `90` | Garantia
`unidadeCobranca` | string | não | `UN` | Unidade
`pontosFidelidade` | number | não | `0` | Pontos
`comissaoPercentual` | number | não | `5.0` | Comissão (%)
`fotoIlustrativaUrl` | string | não | `https://.../srv.jpg` | Foto
`videoExplicativoUrl` | string | não | `https://.../srv.mp4` | Vídeo
`instrucoesExecucao` | string | não | `...` | Instruções
`checklistExecucao` | string | não | `...` | Checklist
`ativo` | boolean | não | `true` | Ativo
`destaque` | boolean | não | `false` | Destaque
`promocional` | boolean | não | `false` | Promocional

### EstoqueResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `5001` | ID do estoque
`empresaId` | number | sim | `10` | Tenant
`produtoId` | number | sim | `1001` | Produto
`produtoNome` | string | não | `Filtro de óleo` | Nome do produto
`quantidadeAtual` | number | não | `50` | Quantidade
`fornecedorId` | number | não | `200` | Fornecedor
`fornecedorNome` | string | não | `Autopeças Silva` | Nome fornecedor
`precoCustoLote` | number | não | `1230.00` | Preço do lote
`notaFiscalNumero` | string | não | `NF-556677` | NF número
`certificadoQualidadeUrl` | string | não | `https://.../cert.pdf` | Certificado
`status` | string | não | `ATIVO` | Status
`motivoBloqueio` | string | não | `Validade próxima` | Motivo
`observacoes` | string | não | `...` | Observações
`usuarioCadastro` | number | não | `42` | Usuário cadastro

### ContasReceberResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `7001` | ID do título
`empresaId` | number | sim | `10` | Tenant
`descricao` | string | sim | `Serviço de freios` | Descrição
`clienteId` | number | sim | `1` | Cliente
`faturaId` | number | não | `5001` | Fatura
`faturaNumero` | string | não | `FT-2024-0001` | Nº fatura
`dataEmissao` | string | sim | `2025-12-10` | Emissão
`dataVencimento` | string | sim | `2026-01-10` | Vencimento
`dataRecebimento` | string | não | `2026-01-08` | Recebimento
`valorOriginal` | number | sim | `1250.00` | Valor original
`valorRecebido` | number | não | `1250.00` | Valor recebido
`valorJuros` | number | não | `0.00` | Juros
`valorMulta` | number | não | `0.00` | Multa
`valorDesconto` | number | não | `50.00` | Desconto
`saldoDevedor` | number | não | `0.00` | Saldo
`status` | string | não | `PAGO` | Status
`tipoTitulo` | string | não | `NORMAL` | Tipo
`formaPagamentoId` | number | não | `5` | Forma de pagamento
`formaPagamentoNome` | string | não | `Cartão` | Nome forma pag.
`contaBancariaId` | number | não | `12` | Conta bancária
`contaBancariaNome` | string | não | `Banco do Brasil` | Nome conta
`centroCustoId` | number | não | `7` | Centro de custo
`centroCustoNome` | string | não | `Oficina` | Nome centro custo
`planoContasId` | number | não | `21` | Plano de contas
`planoContasNome` | string | não | `Receitas` | Nome plano contas
`observacoes` | string | não | `...` | Observações
`createdAt` | string | não | `2025-12-10T09:00:00` | Criação
`updatedAt` | string | não | `2025-12-10T10:00:00` | Atualização

### ContaBancariaResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `8001` | ID
`empresaId` | number | sim | `10` | Tenant
`bancoCodigo` | string | sim | `001` | Código do banco
`bancoNome` | string | sim | `Banco do Brasil` | Nome do banco
`agencia` | string | sim | `1234` | Agência
`conta` | string | sim | `56789` | Conta
`digitoConta` | string | não | `0` | Dígito da conta
`tipoConta` | string | sim | `CORRENTE` | Tipo de conta
`titularConta` | string | sim | `NeriTech Serviços` | Titular
`cpfCnpjTitular` | string | sim | `12.345.678/0001-90` | documento titular
`saldoAtual` | number | não | `10000.00` | Saldo
`limiteChequeEspecial` | number | não | `5000.00` | Limite
`dataUltimoSaldo` | string | não | `2025-12-10` | Data saldo
`gerenteNome` | string | não | `Carlos` | Gerente
`gerenteTelefone` | string | não | `11 3333-4444` | Telefone gerente
`gerenteEmail` | string | não | `carlos@banco.com` | Email gerente
`principal` | boolean | não | `true` | Conta principal
`ativo` | boolean | não | `true` | Ativo
`observacoes` | string | não | `...` | Observações
`createdAt` | string | não | `2025-12-10T09:00:00` | Criação
`updatedAt` | string | não | `2025-12-10T10:00:00` | Atualização

### ContaBancariaRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`bancoCodigo` | string | sim | `001` | Código do banco
`bancoNome` | string | sim | `Banco do Brasil` | Nome do banco
`agencia` | string | sim | `1234` | Agência
`conta` | string | sim | `56789` | Conta
`digitoConta` | string | não | `0` | Dígito da conta
`tipoConta` | string | sim | `CORRENTE` | Tipo de conta
`titularConta` | string | sim | `NeriTech Serviços` | Titular
`cpfCnpjTitular` | string | sim | `12.345.678/0001-90` | Documento do titular
`saldoAtual` | number | não | `10000.00` | Saldo
`principal` | boolean | não | `true` | Conta principal
`ativo` | boolean | não | `true` | Ativo
`observacoes` | string | não | `...` | Observações

### FornecedorResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `9001` | ID
`empresaId` | number | sim | `10` | Tenant
`tipoPessoa` | string | não | `PESSOA_JURIDICA` | Tipo pessoa
`nomeFantasia` | string | sim | `Autopeças Silva` | Nome fantasia
`razaoSocial` | string | não | `Autopeças Silva LTDA` | Razão social
`cpf` | string | não | `123.456.789-00` | CPF
`cnpj` | string | não | `12.345.678/0001-90` | CNPJ
`inscricaoEstadual` | string | não | `1234567890` | IE
`inscricaoMunicipal` | string | não | `987654321` | IM
`emailPrincipal` | string | não | `contato@autopecas.com` | Email
`telefonePrincipal` | string | não | `11 3333-4444` | Telefone
`celularPrincipal` | string | não | `11 91234-5678` | Celular
`website` | string | não | `https://autopecas.com` | Site
`nomeContato` | string | não | `Maria Souza` | Contato
`cargoContato` | string | não | `Vendas` | Cargo
`emailContato` | string | não | `maria@autopecas.com` | Email contato
`telefoneContato` | string | não | `11 3555-7788` | Telefone contato
`enderecoCompleto` | string | não | `Rua A, 100 - São Paulo/SP` | Endereço
`cep` | string | não | `01010-010` | CEP
`cidade` | string | não | `São Paulo` | Cidade
`estado` | string | não | `SP` | Estado
`prazoPagamentoDias` | number | não | `30` | Prazo
`limiteCredito` | number | não | `50000.00` | Limite crédito
`descontoPadrao` | number | não | `5.0` | Desconto padrão
`condicoesEspeciais` | string | não | `Frete grátis > R$1000` | Condições
`bancoNome` | string | não | `Caixa` | Banco
`bancoAgencia` | string | não | `1234` | Agência
`bancoConta` | string | não | `98765-0` | Conta
`bancoPix` | string | não | `contato@autopecas.com` | Chave PIX
`classificacao` | string | não | `PREFERENCIAL` | Classificação
`observacoes` | string | não | `...` | Observações
`ativo` | boolean | não | `true` | Ativo

### ConfiguracaoOficinaResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `1` | ID
`empresaId` | number | sim | `10` | Tenant
`empresaNome` | string | não | `NeriTech` | Nome empresa
`horarioAberturaSegSex` | string | não | `08:00:00` | Abertura (seg-sex)
`horarioFechamentoSegSex` | string | não | `18:00:00` | Fechamento (seg-sex)
`horarioAberturaSabado` | string | não | `08:00:00` | Abertura (sab)
`horarioFechamentoSabado` | string | não | `12:00:00` | Fechamento (sab)
`funcionaDomingo` | boolean | não | `false` | Abre domingo
`horarioAberturaDomingo` | string | não | `00:00:00` | Abertura (dom)
`horarioFechamentoDomingo` | string | não | `00:00:00` | Fechamento (dom)
`tempoAgendamentoPadrao` | number | não | `60` | Tempo padrão (min)
`antecedenciaMinimaAgendamento` | number | não | `2` | Antecedência (h)
`permiteAgendamentoOnline` | boolean | não | `true` | Agendamento online
`enviaLembreteAgendamento` | boolean | não | `true` | Envia lembrete
`tempoLembreteHoras` | number | não | `24` | Lembrete (h)
`margemLucroPadrao` | number | não | `30.0` | Margem padrão (%)
`diasGarantiaPadrao` | number | não | `90` | Garantia padrão
`moeda` | string | não | `BRL` | Moeda
`formatoData` | string | não | `YYYY-MM-DD` | Formato data
`formatoHora` | string | não | `HH:mm:ss` | Formato hora
`timezone` | string | não | `America/Sao_Paulo` | Timezone
`dataCadastro` | string | não | `2025-01-01T10:00:00` | Cadastro
`dataAtualizacao` | string | não | `2025-01-10T10:00:00` | Atualização

### UnidadeMedidaRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`nome` | string | sim | `Unidade` | Nome da unidade
`sigla` | string | sim | `UN` | Sigla
`descricao` | string | não | `Unidade padrão` | Descrição
`tipo` | string | não | `QUANTIDADE` | Tipo de unidade
`fatorConversaoBase` | number | não | `1.0` | Fator de conversão
`unidadeBase` | boolean | não | `true` | Define como base
`ativo` | boolean | não | `true` | Ativo

### UnidadeMedidaResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `3` | ID
`nome` | string | sim | `Unidade` | Nome
`sigla` | string | sim | `UN` | Sigla
`descricao` | string | não | `Unidade padrão` | Descrição
`tipo` | string | não | `QUANTIDADE` | Tipo de unidade
`fatorConversaoBase` | number | não | `1.0` | Fator
`unidadeBase` | boolean | não | `true` | Base
`ativo` | boolean | não | `true` | Ativo

### TabelaPrecoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`empresaId` | number | sim | `10` | Tenant
`nome` | string | sim | `Tabela Padrão` | Nome da tabela
`descricao` | string | não | `Tabela padrão de venda` | Descrição
`tipoTabela` | string | não | `PADRAO` | Tipo da tabela
`grupoClienteId` | number | não | `3` | Grupo de clientes
`margemLucroPadrao` | number | não | `30.0` | Margem padrão (%)
`descontoMaximoPermitido` | number | não | `10.0` | Desconto máximo (%)
`dataInicio` | string | não | `2025-01-01` | Início de vigência
`dataFim` | string | não | `2025-12-31` | Fim de vigência
`ativo` | boolean | não | `true` | Ativo
`padrao` | boolean | não | `true` | Define como padrão
`observacoes` | string | não | `...` | Observações

### TabelaPrecoResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `1` | ID
`empresaId` | number | sim | `10` | Tenant
`nome` | string | sim | `Tabela Padrão` | Nome
`descricao` | string | não | `Tabela padrão de venda` | Descrição
`tipoTabela` | string | não | `PADRAO` | Tipo
`grupoClienteId` | number | não | `3` | Grupo de clientes
`margemLucroPadrao` | number | não | `30.0` | Margem padrão (%)
`descontoMaximoPermitido` | number | não | `10.0` | Desconto máximo (%)
`dataInicio` | string | não | `2025-01-01` | Início
`dataFim` | string | não | `2025-12-31` | Fim
`ativo` | boolean | não | `true` | Ativo
`padrao` | boolean | não | `true` | Padrão
`observacoes` | string | não | `...` | Observações

### MarcaVeiculoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`nome` | string | sim | `Volkswagen` | Nome da marca
`logoUrl` | string | não | `https://.../logo.png` | URL do logo
`website` | string | não | `https://www.vw.com.br` | Website

### ConfiguracaoEmpresaResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `1` | ID
`empresaId` | number | sim | `10` | Tenant
`empresaNome` | string | não | `NeriTech` | Nome empresa
`regimeTributario` | string | não | `SIMPLES_NACIONAL` | Regime
`codigoCnaePrincipal` | string | não | `4520001` | CNAE principal
`codigoCnaeSecundario` | string | não | `4520002` | CNAE secundário
`capitalSocial` | number | não | `100000.00` | Capital social
`porteEmpresa` | string | não | `ME` | Porte
`tipoEstabelecimento` | string | não | `MATRIZ` | Tipo
`situacaoCadastral` | string | não | `ATIVA` | Situação
`dataSituacaoCadastral` | string | não | `2024-01-01` | Data sit. cadastral
`motivoSituacaoCadastral` | string | não | `...` | Motivo
`situacaoEspecial` | string | não | `...` | Situação especial
`dataSituacaoEspecial` | string | não | `...` | Data sit. especial
`dataCadastro` | string | não | `2024-01-01T10:00:00` | Cadastro
`dataAtualizacao` | string | não | `2024-01-10T10:00:00` | Atualização

### ConfiguracaoFiscalResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `1` | ID
`empresaId` | number | sim | `10` | Tenant
`empresaNome` | string | não | `NeriTech` | Nome empresa
`regimeTributario` | string | não | `SIMPLES_NACIONAL` | Regime
`anexoSimples` | string | não | `ANEXO_I` | Anexo Simples
`aliquotaIss` | number | não | `0.05` | ISS
`aliquotaIcms` | number | não | `0.18` | ICMS
`aliquotaPis` | number | não | `0.0165` | PIS
`aliquotaCofins` | number | não | `0.076` | COFINS
`aliquotaCsll` | number | não | `0.09` | CSLL
`aliquotaIrpj` | number | não | `0.15` | IRPJ
`codigoMunicipioPrestacao` | string | não | `3550308` | Código IBGE do município
`cfopVendaDentroEstado` | string | não | `5102` | CFOP dentro do estado
`cfopVendaForaEstado` | string | não | `6102` | CFOP fora do estado
`cfopServico` | string | não | `5933` | CFOP serviço
`serieNfe` | number | não | `1` | Série da NF-e
`numeracaoNfe` | number | não | `1234` | Numeração atual
`ambienteNfe` | string | não | `PRODUCAO` | Ambiente da NF-e
`validadeCertificado` | string | não | `2026-01-01` | Validade do certificado
`dataCadastro` | string | não | `2024-01-01T10:00:00` | Cadastro
`dataAtualizacao` | string | não | `2024-01-10T10:00:00` | Atualização

### OrdemServicoResponse (campos principais)

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `6001` | ID da OS
`empresaId` | number | sim | `10` | Tenant
`numeroOS` | string | sim | `OS-2024-000123` | Número da OS
`clienteId` | number | não | `1` | Cliente
`veiculoId` | number | não | `100` | Veículo
`statusId` | number | não | `3` | Status
`tipoOS` | string | sim | `MANUTENCAO` | Tipo da OS
`prioridade` | string | não | `ALTA` | Prioridade
`dataAbertura` | string | não | `2025-12-10T09:00:00` | Abertura
`dataPromessa` | string | não | `2025-12-12T18:00:00` | Promessa
`dataInicioExecucao` | string | não | `2025-12-11T09:00:00` | Início execução
`dataFimExecucao` | string | não | `2025-12-11T15:00:00` | Fim execução
`dataEntrega` | string | não | `2025-12-12T18:30:00` | Entrega
`quilometragemEntrada` | number | não | `45800` | Km entrada
`quilometragemSaida` | number | não | `45920` | Km saída
`nivelCombustivelEntrada` | string | não | `MEIO_TANQUE` | Combustível entrada
`nivelCombustivelSaida` | string | não | `CHEIO` | Combustível saída
`consultorResponsavelId` | number | não | `12` | Consultor
`mecanicoResponsavelId` | number | não | `27` | Mecânico
`equipeExecucao` | string | não | `Equipe A` | Equipe
`problemaRelatado` | string | não | `Vibração ao frear` | Problema
`solucaoAplicada` | string | não | `Troca de discos e pastilhas` | Solução
`observacoesInternas` | string | não | `Aguardar peças` | Observações internas
`observacoesCliente` | string | não | `Precisa do carro até sexta` | Observações cliente
`valorServicos` | number | não | `850.00` | Valor serviços
`valorProdutos` | number | não | `450.00` | Valor produtos
`valorDesconto` | number | não | `50.00` | Desconto
`valorAcrescimo` | number | não | `0.00` | Acréscimo
`valorTotal` | number | sim | `1250.00` | Valor total
`formaPagamentoId` | number | não | `5` | Forma pagamento
`condicaoPagamentoId` | number | não | `2` | Condição pagamento
`valorEntrada` | number | não | `200.00` | Entrada
`valorFinanciado` | number | não | `0.00` | Financiado
`aprovadoCliente` | boolean | não | `true` | Aprovado pelo cliente
`dataAprovacaoCliente` | string | não | `2025-12-10T10:00:00` | Data aprovação
`metodoAprovacao` | string | não | `WHATSAPP` | Método
`garantiaDias` | number | não | `90` | Garantia
`dataVencimentoGarantia` | string | não | `2026-03-12` | Vencimento garantia
`nfeEmitida` | boolean | não | `false` | NF-e emitida
`numeroNFe` | string | não | `...` | Número NF-e
`notaAvaliacaoCliente` | number | não | `5` | Nota cliente
`tempoTotalExecucaoMinutos` | number | não | `360` | Duração total (min)
`margemLucroRealizada` | number | não | `35.0` | Margem realizada (%)
`dataCadastro` | string | não | `2025-12-10T09:00:00` | Cadastro
`dataAtualizacao` | string | não | `2025-12-10T10:00:00` | Atualização
`versao` | number | não | `1` | Versão

### AnoModeloRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`modeloId` | number | sim | `501` | ID do modelo
`anoFabricacao` | number | sim | `2019` | Ano de fabricação
`anoModelo` | number | sim | `2020` | Ano do modelo
`codigoFipe` | string | não | `009099-0` | Código FIPE
`valorFipe` | number | não | `35000.00` | Valor FIPE
`dataValorFipe` | string | não | `2025-01-01` | Data do valor
`ativo` | boolean | não | `true` | Ativo

### AnoModeloResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `2020` | ID
`modeloId` | number | sim | `501` | ID do modelo
`modeloNome` | string | não | `Gol 1.6` | Nome do modelo
`anoFabricacao` | number | sim | `2019` | Ano de fabricação
`anoModelo` | number | sim | `2020` | Ano do modelo
`codigoFipe` | string | não | `009099-0` | Código FIPE
`valorFipe` | number | não | `35000.00` | Valor FIPE
`dataValorFipe` | string | não | `2025-01-01` | Data do valor
`ativo` | boolean | não | `true` | Ativo

### TipoCombustivelRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`nome` | string | sim | `Gasolina` | Nome
`sigla` | string | sim | `G` | Sigla
`descricao` | string | não | `Gasolina comum` | Descrição
`ativo` | boolean | não | `true` | Ativo

### MarcaVeiculoResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `10` | ID da marca
`empresaId` | number | sim | `10` | Tenant
`nome` | string | sim | `Volkswagen` | Nome
`logoUrl` | string | não | `https://.../logo.png` | Logo
`website` | string | não | `https://www.vw.com.br` | Site
`ativo` | boolean | não | `true` | Ativo
`createdDate` | string | não | `2025-01-01T10:00:00` | Criação
`lastModifiedDate` | string | não | `2025-01-10T10:00:00` | Atualização
`createdBy` | number | não | `42` | Usuário criador
`lastModifiedBy` | number | não | `42` | Usuário atualização
`version` | number | não | `1` | Versão

### TempoServicoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`servicoId` | number | sim | `2001` | ID do serviço
`tipoVeiculoId` | number | não | `3` | Tipo de veículo
`mecanicoEspecialidade` | string | não | `Freios` | Especialidade
`tempoMinimoMinutos` | number | sim | `45` | Tempo mínimo
`tempoMaximoMinutos` | number | sim | `90` | Tempo máximo
`tempoMedioMinutos` | number | sim | `60` | Tempo médio
`complexidadeFator` | number | não | `1.2` | Fator de complexidade
`observacoes` | string | não | `...` | Observações

### TempoServicoResponse

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`id` | number | sim | `3001` | ID
`servicoId` | number | sim | `2001` | Serviço
`servicoNome` | string | não | `Alinhamento e balanceamento` | Nome serviço
`tipoVeiculoId` | number | não | `3` | Tipo veículo
`mecanicoEspecialidade` | string | não | `Freios` | Especialidade
`tempoMinimoMinutos` | number | não | `45` | Tempo mínimo
`tempoMaximoMinutos` | number | não | `90` | Tempo máximo
`tempoMedioMinutos` | number | não | `60` | Tempo médio
`complexidadeFator` | number | não | `1.2` | Fator de complexidade
`observacoes` | string | não | `...` | Observações

### KitServicoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`empresaId` | number | sim | `10` | Tenant
`nome` | string | sim | `Kit Freios` | Nome do kit
`descricao` | string | não | `Troca discos e pastilhas` | Descrição
`precoKit` | number | sim | `850.00` | Preço do kit
`precoOriginal` | number | não | `920.00` | Preço original
`descontoPercentual` | number | não | `7.6` | Desconto (%)
`tempoExecucaoTotal` | number | não | `180` | Tempo total (min)
`garantiaDias` | number | não | `90` | Garantia
`categoriaPrincipalId` | number | não | `2` | Categoria principal
`aplicavelTiposVeiculo` | string | não | `HATCH,SEDAN` | Tipos veículo
`observacoes` | string | não | `...` | Observações
`ativo` | boolean | não | `true` | Ativo
`destaque` | boolean | não | `false` | Destaque

### CategoriaProdutoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`empresaId` | number | sim | `10` | Tenant
`nome` | string | sim | `Filtros` | Nome
`descricao` | string | não | `Linha de filtros` | Descrição
`categoriaPaiId` | number | não | `1` | Categoria pai
`codigo` | string | não | `FLT` | Código
`corIdentificacao` | string | não | `#FFAA00` | Cor
`icone` | string | não | `fa-filter` | Ícone
`margemLucroPadrao` | number | não | `30.0` | Margem (%)
`comissaoVendaPadrao` | number | não | `5.0` | Comissão (%)
`controlaEstoque` | boolean | não | `true` | Controla estoque
`permiteVendaEstoqueNegativo` | boolean | não | `false` | Permite venda negativo
`ordemExibicao` | number | não | `1` | Ordem
`ativo` | boolean | não | `true` | Ativo

### CategoriaServicoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`empresaId` | number | sim | `10` | Tenant
`nome` | string | sim | `Mecânica` | Nome
`descricao` | string | não | `Serviços mecânicos` | Descrição
`categoriaPaiId` | number | não | `1` | Categoria pai
`codigo` | string | não | `MEC` | Código
`corIdentificacao` | string | não | `#00AAFF` | Cor
`icone` | string | não | `fa-wrench` | Ícone
`ordemExibicao` | number | não | `1` | Ordem
`ativo` | boolean | não | `true` | Ativo

### LoteProdutoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`produtoId` | number | sim | `1001` | Produto
`numeroLote` | string | sim | `L2024-0001` | Nº do lote
`dataFabricacao` | string | não | `2024-01-10` | Fabricação
`dataValidade` | string | não | `2025-01-10` | Validade
`quantidadeInicial` | number | sim | `100` | Qtde inicial

### DevolucaoProdutoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`produtoId` | number | sim | `1001` | Produto
`ordemServicoId` | number | não | `6001` | OS vinculada
`clienteId` | number | não | `1` | Cliente
`quantidadeDevolvida` | number | sim | `2` | Qtde devolvida
`motivoDevolucao` | string | não | `DEFEITO` | Motivo
`descricaoMotivo` | string | não | `Peça com falha` | Descrição do motivo
`condicaoProduto` | string | não | `USADO` | Condição
`acaoTomada` | string | não | `REEMBOLSO` | Ação tomada
`valorDevolvido` | number | não | `49.90` | Valor devolvido
`aprovadoPor` | number | não | `42` | Aprovado por
`observacoes` | string | não | `...` | Observações
`fotosProdutoUrl` | string | não | `https://.../foto.jpg` | Fotos
`usuarioResponsavel` | number | não | `42` | Usuário

### NcmProdutoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`codigo` | string | sim | `84099190` | Código NCM
`descricao` | string | sim | `Partes e acessórios` | Descrição
`aliquotaIpi` | number | não | `0.00` | IPI
`ativo` | boolean | não | `true` | Ativo

### CstProdutoRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`codigo` | string | sim | `00` | Código CST
`descricao` | string | sim | `Tributação integral` | Descrição
`tipo` | string | não | `ICMS` | Tipo
`ativo` | boolean | não | `true` | Ativo

### InventarioRequest

Campo | Tipo | Obrigatório | Exemplo | Descrição
--- | --- | --- | --- | ---
`empresaId` | number | sim | `10` | Tenant
`codigo` | string | sim | `INV-2025-0001` | Código inventário
`descricao` | string | sim | `Inventário anual` | Descrição
`tipoInventario` | string | não | `GERAL` | Tipo
`dataInicio` | string | sim | `2025-01-01` | Início
`dataFim` | string | não | `2025-01-15` | Fim
`status` | string | não | `EM_ANDAMENTO` | Status
`localizacoesIncluidas` | string | não | `A1,B2` | Localizações
`categoriasIncluidas` | string | não | `Filtros,Óleos` | Categorias
`produtosIncluidos` | string | não | `1001,1002` | Produtos
`usuariosResponsaveis` | string | não | `42,77` | Usuários
`observacoes` | string | não | `...` | Observações
