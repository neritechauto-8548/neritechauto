# Documentação Técnica Completa - API NeriTech SaaS

Esta documentação consolida todos os endpoints, DTOs e informações de integração do sistema NeriTech.

## Índice
1.  [Administração e Usuários](#1-administração-e-usuários)
2.  [Recursos Humanos (RH)](#2-recursos-humanos-rh)
3.  [Clientes e Veículos](#3-clientes-e-veículos)
4.  [Produtos, Serviços e Fornecedores](#4-produtos-serviços-e-fornecedores)
5.  [Categorias de Produtos](#5-categorias-de-produtos)
6.  [Agendamentos e Ordens de Serviço](#6-agendamentos-e-ordens-de-serviço)
7.  [Financeiro](#7-financeiro)
8.  [Estoque](#8-estoque)
9.  [Módulos Auxiliares (Comunicação, Cotação, Garantia)](#9-módulos-auxiliares)
10. [Fiscal e Ferramentas](#10-fiscal-e-ferramentas)

---

# 1. Administração e Usuários

## 1.1. Gestão de Usuários
**Base URL:** `/api/usuarios`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista todos os usuários. |
| **GET** | `/{id}` | Busca usuário por ID. |
| **POST** | `/` | Cria novo usuário. |
| **PUT** | `/{id}` | Atualiza usuário. |
| **DELETE** | `/{id}` | Inativa usuário. |

### Payloads
**Request: `UsuarioRequest`**
*   `nomeCompleto` (String, Obrigatório)
*   `email` (String, Obrigatório, Validado)
*   `senha` (String, Opcional na atualização)
*   `ativo` / `bloqueado` (Boolean)
*   `cargo` / `departamento` / `telefone` (String)
*   `funcoesIds` (Set<Long>)

**Response: `UsuarioResponse`**
*   Retorna dados completos + `ultimoAcesso` e `avatarUrl`.

## 1.2. Autenticação
**Base URL:** `/api/auth`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **POST** | `/login` | Autentica usuário. Body: `email`, `senha`. |
| **POST** | `/refresh` | Renova token. Body: `refreshToken`. |
| **POST** | `/logout` | Invalida sessão. Header: `Authorization`. |

## 1.3. Gestão de Empresas (Multi-tenant)
**Base URL:** `/v1/empresas`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista empresas. Params: `cnpj`, `razaoSocial`. |
| **GET** | `/{id}` | Busca empresa por ID. |
| **POST** | `/` | Cria nova empresa (Tenant). |
| **PUT** | `/{id}` | Atualiza dados da empresa. |
| **DELETE** | `/{id}` | Remove empresa. |

### Payloads
**Request: `EmpresaRequest`**
*   `nomeFantasia` / `razaoSocial` (String)
*   `cnpj` (String, Obrigatório, 14 dígitos)
*   `email` / `telefone` / `site`
*   `inscricaoEstadual` / `municipal`

## 1.4. Configurações da Empresa
**Base URL:** `/api/v1/configuracoes-empresa`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/empresa/{empresaId}` | Busca configurações da empresa. |
| **POST** | `/` | Cria configuração inicial. |
| **PUT** | `/{id}` | Atualiza configurações. |

---

# 2. Recursos Humanos (RH)

## 2.1. Gestão de Pessoas

### Funcionários
**Base URL:** `/api/rh/funcionarios`

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| GET | `/` | Lista funcionários (paginado). Params: `empresaId`, `page`, `size`. |
| GET | `/{id}` | Busca funcionário por ID. |
| POST | `/` | Cria novo funcionário. |
| PUT | `/{id}` | Atualiza funcionário. |
| DELETE | `/{id}` | Remove funcionário. |

**Payload: `FuncionarioRequest`**
*   `empresaId` (Long, Obrigatório)
*   `matricula` (String, Obrigatório, Max 20)
*   `nomeCompleto` (String, Obrigatório)
*   `cpf` (String, Obrigatório)
*   `dataNascimento` (LocalDate)
*   `sexo` (ENUM: MASCULINO, FEMININO, OUTRO)
*   `estadoCivil` (ENUM: SOLTEIRO, CASADO, DIVORCIADO, VIUVO, UNIAO_ESTAVEL)
*   `cargoId` (Long)
*   `departamentoId` (Long)
*   `dataAdmissao` (LocalDate, Obrigatório)
*   `salarioBase` (BigDecimal)
*   `emailPessoal` (String, Email válido)
*   `fotoFuncionarioUrl` (String, URL da imagem)
*   *Outros campos:* `rg`, `naturalidade`, `nomeMae`, `escolaridade`, `tipoContrato`, `enderecoCompleto`, `telefonePrincipal`.

### Mecânicos
**Base URL:** `/api/rh/mecanicos`
*Mecânicos estendem Funcionários (requer `funcionarioId` existente).*

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| GET | `/` | Lista mecânicos. |
| POST | `/` | Cadastra funcionário como mecânico. |

**Payload: `MecanicoRequest`**
*   `funcionarioId` (Long, Obrigatório. ID do funcionário existente)
*   `codigoMecanico` (String, Obrigatório)
*   `nivelExperiencia` (ENUM: ESTAGIARIO, JUNIOR, PLENO, SENIOR, ESPECIALISTA)
*   `especialidadesPrincipais` (String)
*   `capacidadeDiagnostico` (ENUM: BAIXA, MEDIA, ALTA)
*   `ativoExecucao` (Boolean)

### Documentos
**Base URL:** `/api/rh/documentos`

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| GET | `/funcionario/{id}` | Lista documentos de um funcionário. |
| POST | `/` | Upload/Registro de documento. |

**Payload: `DocumentoFuncionarioRequest`**
*   `funcionarioId` (Long, Obrigatório)
*   `tipoDocumento` (ENUM: RG, CPF, CNH, CTPS, TITULO_ELEITOR, RESERVISTA, CERTIDAO_NASCIMENTO, CERTIDAO_CASAMENTO, COMPROVANTE_RESIDENCIA, DIPLOMA, CERTIFICADO, ATESTADO_MEDICO, EXAME_ADMISSIONAL, EXAME_PERIODICO, CONTRATO_TRABALHO, ADITIVO_CONTRATUAL, AVISO_FERIAS, RECIBO_FERIAS, FOLHA_PONTO, HOLERITE, OUTROS)
*   `numeroDocumento` (String)
*   `arquivoUrl` (String, Link para o arquivo)
*   `dataValidade` (LocalDate)

## 2.2. Estrutura Organizacional

### Departamentos
**Base URL:** `/api/rh/departamentos`

**Payload: `DepartamentoRequest`**
*   `empresaId` (Long, Obrigatório)
*   `nome` (String, Obrigatório)
*   `departamentoPaiId` (Long, Opcional - para sub-departamentos)
*   `gerenteId` (Long, Opcional - ID do funcionário gestor)
*   `centroCusto` (String)

### Cargos
**Base URL:** `/api/rh/cargos`

**Payload: `CargoRequest`**
*   `empresaId` (Long, Obrigatório)
*   `nome` (String, Obrigatório)
*   `codigoCbo` (String)
*   `salarioBaseMinimo` / `salarioBaseMaximo` (BigDecimal)
*   `temComissao` (Boolean)
*   `metaVendasMensal` (BigDecimal)

## 2.3. Jornada e Frequência

### Escalas de Trabalho
**Base URL:** `/api/rh/escalas-trabalho`

**Payload: `EscalaTrabalhoRequest`**
*   `empresaId` (Long, Obrigatório)
*   `funcionarioId` (Long, Obrigatório)
*   `horarioTrabalhoId` (Long, Vincula a um horário definido)
*   `tipoEscala` (ENUM: SEGUNDA_SEXTA, SEGUNDA_SABADO, 12X36, 6X1, 5X2, 4X2, PLANTAO, OUTRA)
*   `dataInicio` (LocalDate, Obrigatório)
*   `diasTrabalho` / `diasFolga` (Integer)

### Horários de Trabalho
**Base URL:** `/api/rh/horarios-trabalho`

**Payload: `HorarioTrabalhoRequest`**
*   `empresaId` (Long, Obrigatório)
*   `nome` (String, Ex: "Comercial 08:00-18:00")
*   `tipoHorario` (ENUM: FIXO, FLEXIVEL, TURNO)
*   `horaEntradaSeg` / `horaSaidaSeg` (LocalTime - Repete para todos os dias da semana)
*   `intervaloAlmocoMinutos` (Integer)

### Faltas e Atrasos
**Base URL:** `/api/rh/faltas-atrasos`

**Payload: `FaltaAtrasoRequest`**
*   `funcionarioId` (Long, Obrigatório)
*   `tipoOcorrencia` (ENUM: FALTA_INJUSTIFICADA, FALTA_JUSTIFICADA, ATRASO, SAIDA_ANTECIPADA, SUSPENSAO)
*   `dataOcorrencia` (LocalDate, Obrigatório)
*   `minutosAtraso` (Integer)
*   `anexoComprovanteUrl` (String, Para atestados)
*   `descontoAplicado` (Boolean)

### Férias
**Base URL:** `/api/rh/ferias`

**Payload: `FeriasFuncionarioRequest`**
*   `funcionarioId` (Long, Obrigatório)
*   `periodoAquisitivoInicio` / `Fim` (LocalDate, Obrigatório)
*   `diasDireito` (Integer, Obrigatório)
*   `dataInicioFerias` / `dataFimFerias` (LocalDate)
*   `status` (ENUM: A_PROGRAMAR, PROGRAMADA, EM_ANDAMENTO, GOZADA, CANCELADA, VENDIDA_PARCIALMENTE)
*   `abonoPecuniario` (Boolean, "Vender férias")

## 2.4. Desenvolvimento e Performance

### Avaliações de Desempenho
**Base URL:** `/api/rh/avaliacoes`

**Payload: `AvaliacaoFuncionarioRequest`**
*   `funcionarioId` (Long, Obrigatório)
*   `avaliadorId` (Long, Obrigatório)
*   `tipoAvaliacao` (ENUM: EXPERIENCIA_45_DIAS, EXPERIENCIA_90_DIAS, PERIODICA_ANUAL, DESEMPENHO_PROJETO, FEEDBACK_PONTUAL, AUTO_AVALIACAO)
*   `notaProdutividade`, `notaQualidade`, `notaPontualidade` (BigDecimal)
*   `notaFinal` (BigDecimal, Obrigatório)
*   `recomendacao` (ENUM: PROMOVER, AUMENTO_SALARIAL, MANTER_CARGO, TREINAMENTO_INTENSIVO, PLANO_RECUPERACAO, DESLIGAMENTO)

### Treinamentos
**Base URL:** `/api/rh/treinamentos`

**Payload: `TreinamentoRequest`**
*   `empresaId` (Long, Obrigatório)
*   `nomeTreinamento` (String, Obrigatório)
*   `tipoTreinamento` (ENUM: TECNICO, COMPORTAMENTAL, SEGURANCA_TRABALHO, PRODUTO, SISTEMA, ONBOARDING, LIDERANCA)
*   `instrutorInternoId` (Long) ou `instrutorExterno` (String)
*   `dataInicio` / `dataFim` (LocalDate)
*   `cargaHoraria` (Integer)
*   `certificacaoEmitida` (Boolean)

### Certificações
**Base URL:** `/api/rh/certificacoes`

**Payload: `CertificacaoRequest`**
*   `funcionarioId` (Long, Obrigatório)
*   `nomeCertificacao` (String, Obrigatório)
*   `entidadeCertificadora` (String, Obrigatório)
*   `dataValidade` (LocalDate)
*   `arquivoCertificadoUrl` (String)

### Especialidades
**Base URL:** `/api/rh/especialidades` e `/api/rh/funcionarios-especialidades`

**Payload: `EspecialidadeRequest` (Cadastro do Tipo)**
*   `nome` (String, Obrigatório)
*   `nivelComplexidade` (ENUM: BAIXA, MEDIA, ALTA, MUITO_ALTA)

**Payload: `FuncionarioEspecialidadeRequest` (Vínculo)**
*   `funcionarioId` (Long)
*   `especialidadeId` (Long)
*   `nivelDominio` (ENUM: BASICO, INTERMEDIARIO, AVANCADO, ESPECIALISTA)

## 2.5. Financeiro RH

### Comissões
**Base URL:** `/api/rh/comissoes`

**Payload: `ComissaoRequest`**
*   `funcionarioId` (Long, Obrigatório)
*   `tipoComissao` (ENUM: VENDAS_PRODUTOS, SERVICOS_REALIZADOS, INDICACAO, META_GLOBAL, BONUS_PERFORMANCE)
*   `valorBase` (BigDecimal)
*   `percentualComissao` (BigDecimal)
*   `valorComissao` (BigDecimal, Obrigatório)
*   `statusPagamento` (ENUM: PENDENTE, APROVADO, PAGO, CANCELADO)
*   `dataCompetencia` (LocalDate, Mês de referência)

---

# 3. Clientes e Veículos

## 3.1. Clientes
**Base URL:** `/v1/clientes`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista clientes com filtros. Params: `nomeCompleto`, `razaoSocial`, `cpf`, `cnpj`, `tipoCliente`, `status`, `page`, `size`. |
| **GET** | `/{id}` | Busca cliente por ID. |
| **POST** | `/` | Cria novo cliente. |
| **PUT** | `/{id}` | Atualiza cliente. |
| **DELETE** | `/{id}` | Remove cliente. |

### Payloads
**Request: `ClienteRequest`**
*   `tipoCliente` (ENUM: PESSOA_FISICA, PESSOA_JURIDICA)
*   `nomeCompleto` / `nomeFantasia` / `razaoSocial`
*   `cpf` / `cnpj` / `rg` / `inscricaoEstadual`
*   `dataNascimento` (LocalDate)
*   `estadoCivil` (ENUM)
*   `sexo` (ENUM)
*   `enderecoCompleto` (String)

**Response: `ClienteResponse`**
*   Retorna todos os dados cadastrais + ID.

## 3.2. Veículos
**Base URL:** `/v1/veiculos`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista veículos. Param opcional: `clienteId` para filtrar por dono. |
| **GET** | `/{id}` | Busca veículo por ID. |
| **POST** | `/` | Cadastra veículo. |
| **PUT** | `/{id}` | Atualiza veículo. |
| **DELETE** | `/{id}` | Remove veículo. |

### Payloads
**Request: `VeiculoRequest`**
*   `clienteId` (Long, Obrigatório)
*   `marcaId`, `modeloId`, `anoModeloId` (Long)
*   `placa` (String, Obrigatório)
*   `renavam`, `chassi`
*   `quilometragemAtual` (Integer)
*   `status` (ENUM: ATIVO, INATIVO, EM_MANUTENCAO)

**Response: `VeiculoResponse`**
*   Inclui nomes de marca/modelo (`marcaNome`, `modeloNome`) para exibição fácil.

---

# 4. Produtos, Serviços e Fornecedores

## 4.1. Produtos
**Base URL:** `/api/v1/produtos`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista produtos da empresa. Params: `empresaId`. |
| **GET** | `/{id}` | Busca produto por ID. |
| **POST** | `/` | Cria produto. |
| **PUT** | `/{id}` | Atualiza produto. |
| **DELETE** | `/{id}` | Remove produto. |

### Payloads
**Request: `ProdutoRequest`**
*   `empresaId` (Long, Obrigatório)
*   `categoriaId` / `unidadeMedidaId` (Long)
*   `nome` (String, Obrigatório)
*   `codigoInterno` (String, Obrigatório)
*   `precoCusto` / `precoVenda` (BigDecimal, Obrigatório)
*   `margemLucroPercentual` (BigDecimal)
*   `controlaLote` / `controlaValidade` (Boolean)
*   `estoqueMinimo` / `estoqueMaximo`

**Response: `ProdutoResponse`**
*   Dados completos do produto.

## 4.2. Serviços
**Base URL:** `/api/v1/servicos`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista serviços da empresa. Params: `empresaId`. |
| **GET** | `/{id}` | Busca serviço por ID. |
| **POST** | `/` | Cria serviço. |
| **PUT** | `/{id}` | Atualiza serviço. |
| **DELETE** | `/{id}` | Remove serviço. |

### Payloads
**Request: `ServicoRequest`**
*   `empresaId` (Long, Obrigatório)
*   `nome` (String, Obrigatório)
*   `codigo` (String, Obrigatório)
*   `precoBase` (BigDecimal)
*   `tempoExecucaoMinutos` (Integer)
*   `comissaoPercentual` (BigDecimal)

**Response: `ServicoResponse`**
*   Dados completos do serviço.

## 4.3. Fornecedores
**Base URL:** `/api/v1/fornecedores`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista fornecedores. Params: `empresaId`. |
| **GET** | `/{id}` | Busca fornecedor. |
| **POST** | `/` | Cria fornecedor. |
| **PUT** | `/{id}` | Atualiza fornecedor. |
| **DELETE** | `/{id}` | Remove fornecedor. |

### Payloads
**Request: `FornecedorRequest`**
*   `empresaId` (Long, Obrigatório)
*   `nomeFantasia` (String, Obrigatório)
*   `cpf` / `cnpj`
*   `emailPrincipal` / `telefonePrincipal`
*   `tipoPessoa` (ENUM: FISICA, JURIDICA)
*   `classificacao` (ENUM: A, B, C)

## 4.4. Unidades de Medida
**Base URL:** `/api/v1/unidades-medida`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista unidades de medida (paginado). |
| **GET** | `/ativas` | Lista unidades de medida ativas. |
| **GET** | `/{id}` | Busca unidade de medida por ID. |
| **POST** | `/` | Cria unidade de medida. |
| **PUT** | `/{id}` | Atualiza unidade de medida. |
| **DELETE** | `/{id}` | Remove unidade de medida. |

### Payloads
**Request: `UnidadeMedidaRequest`**
*   `nome` (String, Obrigatório, Max 50)
*   `sigla` (String, Obrigatório, Max 10)
*   `descricao` (String)
*   `tipo` (ENUM: PESO, VOLUME, COMPRIMENTO, UNIDADE, TEMPO, OUTRO)
*   `fatorConversaoBase` (BigDecimal)
*   `unidadeBase` (Boolean)
*   `ativo` (Boolean)

**Response: `UnidadeMedidaResponse`**
*   Todos os campos acima + ID.

---

# 5. Categorias de Produtos
**Base URL:** `/api/v1/categorias-produtos`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista todas as categorias de uma empresa (paginado). Params: `empresaId` (Obrigatório), `page`, `size`. |
| **GET** | `/roots` | Lista apenas categorias raiz (sem pai). Params: `empresaId`. |
| **GET** | `/{id}` | Busca os detalhes de uma categoria específica por ID. |
| **POST** | `/` | Cria uma nova categoria de produto. |
| **PUT** | `/{id}` | Atualiza uma categoria existente. |
| **DELETE** | `/{id}` | Exclui uma categoria. |

### Payloads
**Request: `CategoriaProdutoRequest`**
*   `empresaId` (Long, Obrigatório)
*   `nome` (String, Obrigatório)
*   `categoriaPaiId` (Long, Opcional)
*   `descricao` (String)
*   `controlaEstoque` (Boolean)

**Response: `CategoriaProdutoResponse`**
*   Retorna todos os dados da categoria, incluindo `categoriaPaiNome`.

---

# 6. Agendamentos e Ordens de Serviço

## 6.1. Agendamento
**Base URL:** `/api/agendamentos`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/{id}` | Busca agendamento por ID. |
| **GET** | `/empresa/{empresaId}` | Lista todos agendamentos da empresa. |
| **POST** | `/` | Cria novo agendamento. |
| **PUT** | `/{id}` | Atualiza agendamento. |
| **DELETE** | `/{id}` | Remove agendamento. |

### Payloads
**Request: `AgendamentoRequest`**
*   `empresaId` (Long, Obrigatório)
*   `clienteId` / `veiculoId` (Long, Obrigatório)
*   `dataAgendamento` (LocalDate, Obrigatório)
*   `horaInicio` / `horaFim` (LocalTime, Obrigatório)
*   `tipoAgendamentoId` (Long)
*   `status` (ENUM: PENDENTE, CONFIRMADO, EM_ATENDIMENTO, CONCLUIDO, CANCELADO, NO_SHOW)
*   `servicosSolicitados` (String)

**Response: `AgendamentoResponse`**
*   Retorna dados completos + `ordemServicoGeradaId` caso tenha virado OS.

## 6.2. Ordens de Serviço (OS)
**Base URL:** `/api/ordens-servico`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/{id}` | Busca OS por ID. |
| **GET** | `/empresa/{empresaId}` | Lista OS da empresa (paginado). |
| **GET** | `/empresa/{empresaId}/status/{statusId}` | Lista OS por status. |
| **POST** | `/` | Cria nova OS. |
| **PUT** | `/{id}` | Atualiza OS. |
| **DELETE** | `/{id}` | Remove OS. |

### Payloads
**Request: `OrdemServicoRequest`**
*   `empresaId` (Long, Obrigatório)
*   `numeroOS` (String, Obrigatório)
*   `clienteId` / `veiculoId` (Long)
*   `statusId` (Long, Status personalizado)
*   `tipoOS` (ENUM: ORCAMENTO, VENDA_PRODUTO, SERVICO_RAPIDO, MANUTENCAO_PREVENTIVA, CORRETIVA, GARANTIA, RETORNO)
*   `prioridade` (ENUM: BAIXA, NORMAL, ALTA, URGENTE)
*   `problemaRelatado` (String)
*   `valorTotal` (BigDecimal, Obrigatório)

**Response: `OrdemServicoResponse`**
*   `dataAbertura` / `dataPromessa` / `dataEntrega`
*   `valorServicos` / `valorProdutos`
*   `aprovadoCliente` (Boolean)

---

# 7. Financeiro

## 7.1. Contas a Pagar
**Base URL:** `/api/financeiro/contas-pagar`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista títulos a pagar. Params: `empresaId`. |
| **GET** | `/{id}` | Busca título por ID. |
| **POST** | `/` | Cria título a pagar. |
| **PUT** | `/{id}` | Atualiza título. |
| **DELETE** | `/{id}` | Remove título. |

### Payloads
**Request: `ContasPagarRequest`**
*   `descricao` (String, Obrigatório)
*   `fornecedorId` (Long, Obrigatório)
*   `dataEmissao` / `dataVencimento` (LocalDate, Obrigatório)
*   `valorOriginal` (BigDecimal, Obrigatório)
*   `status` (ENUM: PENDENTE, PAGO, CANCELADO, VENCIDO)
*   `tipoTitulo` (ENUM: BOLETO, NOTA_FISCAL, DARF, GPS, SALARIO, OUTROS)
*   `planoContasId` / `centroCustoId`

## 7.2. Contas a Receber
**Base URL:** `/api/financeiro/contas-receber`

### Endpoints
*Similares a Contas a Pagar*

### Payloads
**Request: `ContasReceberRequest`**
*   `descricao` (String, Obrigatório)
*   `clienteId` (Long, Obrigatório)
*   `dataEmissao` / `dataVencimento` (LocalDate, Obrigatório)
*   `valorOriginal` (BigDecimal, Obrigatório)
*   `status` (ENUM)

## 7.3. Cadastros Auxiliares

### Centro de Custo (`/api/financeiro/centros-custo`)
**Request: `CentroCustoRequest`**
*   `nome` / `codigo` (Obrigatório)
*   `tipo` (ENUM: RECEITA, DESPESA, MISTO)
*   `orcamentoMensal` (BigDecimal)

### Contas Bancárias (`/api/financeiro/contas-bancarias`)
**Request: `ContaBancariaRequest`**
*   `bancoNome` / `bancoCodigo` (Obrigatório)
*   `agencia` / `conta` (Obrigatório)
*   `tipoConta` (ENUM: CORRENTE, POUPANCA, INVESTIMENTO, CAIXA_INTERNO)
*   `titularConta` / `cpfCnpjTitular`
*   `saldoAtual` (BigDecimal)

---

# 8. Estoque

## 8.1. Estoque Atual
**Base URL:** `/api/v1/estoques`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/empresa/{empresaId}` | Lista estoque atual de todos produtos. |
| **GET** | `/empresa/{empresaId}/produto/{produtoId}` | Histórico/Estoques do produto. |
| **POST** | `/` | Cria entrada inicial de estoque. |
| **PUT** | `/{id}` | Ajuste manual de estoque. |

### Payloads
**Request: `EstoqueRequest`**
*   `produtoId` (Long, Obrigatório)
*   `localizacaoId` (Long)
*   `quantidade` (BigDecimal)
*   `quantidadeMinima` / `Maxima`

## 8.2. Movimentações (Histórico)
**Base URL:** `/api/v1/movimentacoes-estoque`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **POST** | `/` | Registra nova movimentação (Entrada/Saída). |
| **GET** | `/empresa/{empresaId}` | Lista histórico geral. |
| **GET** | `/empresa/{empresaId}/produto/{produtoId}` | Extrato do produto. |

### Payloads
**Request: `MovimentacaoEstoqueRequest`**
*   `empresaId` (Long, Obrigatório)
*   `produtoId` (Long, Obrigatório)
*   `tipoMovimentacao` (ENUM: ENTRADA, SAIDA, TRANSFERENCIA, AJUSTE_ENTRADA, AJUSTE_SAIDA)
*   `quantidade` (BigDecimal, Obrigatório)
*   `valorUnitario` (BigDecimal)
*   `motivo` (String)
*   `documentoNumero` (Para NF)
*   `ordemServicoId` (Se vinculado a OS)

---

# 9. Módulos Auxiliares

## 9.1. Comunicação & Notificações
**Base URL:** `/api/comunicacao`

### Notificações (`/api/comunicacao/notificacoes`)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista notificações. Params: `empresaId`. |
| **GET** | `/usuario/{usuarioId}` | Notificações do usuário. |
| **POST** | `/` | Cria notificação. |

**Request: `NotificacaoSistemaRequest`**
*   `usuarioDestinatarioId` (Long, Obrigatório)
*   `titulo` / `mensagem` (String, Obrigatório)
*   `tipoNotificacao` (ENUM: INFO, ALERT, ERROR, SUCCESS)
*   `prioridade` (ENUM: BAIXA, NORMAL, ALTA)

### WhatsApp (`/api/comunicacao/config/whatsapp`)
Endpoints para configurar integração com WhatsApp Business API.

**Request: `ConfiguracaoWhatsappRequest`**
*   `numeroWhatsapp` (String, Obrigatório)
*   `provedorServico` (String)
*   `tokenAcesso` (String)

## 9.2. Cotações
**Base URL:** `/api/v1/cotacoes`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista cotações. |
| **GET** | `/status` | Filtra por status. |
| **POST** | `/` | Cria cotação. |
| **PUT** | `/{id}` | Atualiza cotação. |

**Request: `CotacaoRequest`**
*   `numeroCotacao` (String, Obrigatório)
*   `tipoCotacao` (ENUM: COMPRA, VENDA)
*   `dataSolicitacao` / `dataLimiteResposta`
*   `responsavelCotacaoId` (Long)

## 9.3. Garantias
**Base URL:** `/api/garantias`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista garantias. |
| **POST** | `/` | Registra nova garantia vinculada a OS. |

**Request: `GarantiaRequest`**
*   `ordemServicoId` (Long, Obrigatório)
*   `clienteId` / `veiculoId` (Long)
*   `dataInicio` / `dataFim` (LocalDate)
*   `diasGarantia` (Integer)
*   `valorCoberturaGarantia` (BigDecimal)

---

# 10. Fiscal e Ferramentas

## 10.1. Configuração Fiscal (NF-e)
**Base URL:** `/api/fiscal/configuracoes-nfe`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/` | Lista configurações de NF-e. |
| **POST** | `/` | Cria configuração de NF-e. |
| **PUT** | `/{id}` | Atualiza configuração de NF-e. |

### Payloads
**Request: `ConfiguracaoNfeRequest`**
*   `ambiente` (ENUM: HOMOLOGACAO, PRODUCAO)
*   `serie` (Integer)
*   `proximoNumero` (Long)
*   `certificadoDigitalId` (Long)

## 10.2. Ferramentas e Equipamentos
**Base URL:** `/api/v1/ferramentas`

### Endpoints
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/empresa/{empresaId}` | Lista ferramentas da empresa. |
| **GET** | `/{id}` | Busca ferramenta por ID. |
| **POST** | `/` | Cadastra nova ferramenta. |
| **PUT** | `/{id}` | Atualiza cadastro de ferramenta. |

### Payloads
**Request: `FerramentaRequest`**
*   `codigoFerramenta` / `nome` (Obrigatório)
*   `tipoFerramenta` (ENUM: MANUAL, ELETRICA, PNEUMATICA, HIDRAULICA, ESPECIAL, DIAGNOSTICO)
*   `status` (ENUM: DISPONIVEL, EM_USO, MANUTENCAO, CALIBRACAO, PERDIDA, DESCARTADA)
*   `requerCalibracao` (Boolean)
*   `responsavelAtualId` (Long)
