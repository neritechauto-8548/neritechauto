# 📋 MAPEAMENTO COMPLETO DA API - SISTEMA NERITECH

## 🔍 CONFIGURAÇÃO DO SERVIDOR

**Context Path:** `/api` (configurado em `application-dev.yml` e `application-prod.yml`)

**Base URL Frontend:** `/api` (definido em `environment.ts`)

**URLs Completas Esperadas:**
- Local: `http://localhost:8080/api`
- Swagger: `http://localhost:8080/api/swagger-ui/index.html#/`

---

## ⚠️ INCONSISTÊNCIAS IDENTIFICADAS

### 1. **INCONSISTÊNCIA DE PREFIXOS NOS CONTROLLERS**

Alguns controllers usam `/v1/...` e outros usam `/api/v1/...` ou `/api/...`.

**Problema:** Se o context-path é `/api`, então os controllers devem usar apenas `/v1/...` (sem o prefixo `/api`).

**Controllers com prefixo incorreto:**
- ❌ `/api/v1/situacoes-empresa` → deveria ser `/v1/situacoes-empresa`
- ❌ `/api/v1/departamentos-contabio-empresa` → deveria ser `/v1/departamentos-contabio-empresa`
- ❌ `/api/v1/localizacoes-empresa` → deveria ser `/v1/localizacoes-empresa`
- ❌ `/api/v1/setores-empresa` → deveria ser `/v1/setores-empresa`
- ❌ `/api/financeiro/...` → deveria ser `/v1/financeiro/...`
- ❌ `/api/comunicacao/...` → deveria ser `/v1/comunicacao/...`
- ❌ `/api/rh/...` → deveria ser `/v1/rh/...`

**Controllers corretos:**
- ✅ `/v1/veiculos`
- ✅ `/v1/clientes`
- ✅ `/v1/ordens-servico`
- ✅ `/auth` (sem prefixo, correto)

---

## 📊 MAPEAMENTO DE ENDPOINTS POR MÓDULO

### 🔐 AUTENTICAÇÃO (`/auth`)

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/auth/login` | Login do usuário | ✅ |
| POST | `/auth/refresh` | Refresh token | ✅ |
| POST | `/auth/logout` | Logout | ✅ |

**URL Completa:** `http://localhost:8080/api/auth/...`

---

### 👥 CLIENTES (`/v1/clientes`)

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| GET | `/v1/clientes` | Listar clientes (com paginação e filtros) | ✅ |
| GET | `/v1/clientes/{id}` | Buscar cliente por ID | ✅ |
| POST | `/v1/clientes` | Criar cliente | ✅ |
| PUT | `/v1/clientes/{id}` | Atualizar cliente | ✅ |
| DELETE | `/v1/clientes/{id}` | Excluir cliente | ✅ |

**Filtros disponíveis:**
- `nomeCompleto` (String)
- `razaoSocial` (String)
- `cpf` (String)
- `cnpj` (String)
- `tipoCliente` (Enum: PF, PJ)
- `status` (Enum)

**URL Completa:** `http://localhost:8080/api/v1/clientes/...`

---

### 🚗 VEÍCULOS (`/v1/veiculos`)

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| GET | `/v1/veiculos` | Listar veículos | ✅ |
| GET | `/v1/veiculos?clienteId={id}` | Listar veículos por cliente | ✅ |
| GET | `/v1/veiculos/{id}` | Buscar veículo por ID | ✅ |
| POST | `/v1/veiculos` | Criar veículo | ✅ |
| PUT | `/v1/veiculos/{id}` | Atualizar veículo | ✅ |
| DELETE | `/v1/veiculos/{id}` | Excluir veículo | ✅ |

**Sub-recursos:**
- `/v1/marcas-veiculos` - Marcas de veículos
- `/v1/modelos-veiculos` - Modelos de veículos
- `/v1/anos-modelo` - Anos de modelo
- `/v1/tipos-combustivel` - Tipos de combustível
- `/v1/fotos-veiculos` - Fotos de veículos
- `/v1/documentos-veiculos` - Documentos de veículos
- `/v1/historico-quilometragem` - Histórico de quilometragem
- `/v1/manutencoes-preventivas` - Manutenções preventivas

**URL Completa:** `http://localhost:8080/api/v1/veiculos/...`

---

### 🔧 ORDENS DE SERVIÇO (`/v1/ordens-servico`)

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| GET | `/v1/ordens-servico/empresa/{empresaId}` | Listar OS por empresa | ✅ |
| GET | `/v1/ordens-servico/empresa/{empresaId}/status/{statusId}` | Listar OS por empresa e status | ✅ |
| GET | `/v1/ordens-servico/{id}` | Buscar OS por ID | ✅ |
| POST | `/v1/ordens-servico` | Criar OS | ✅ |
| PUT | `/v1/ordens-servico/{id}` | Atualizar OS | ✅ |
| DELETE | `/v1/ordens-servico/{id}` | Excluir OS | ✅ |

**Sub-recursos:**
- `/v1/orcamentos` - Orçamentos
- `/v1/status-os` - Status de OS
- `/v1/itens-os-produtos` - Itens de OS (produtos)
- `/v1/itens-os-servicos` - Itens de OS (serviços)
- `/v1/ordens-servico/checklists` - Checklists
- `/v1/ordens-servico/it-checklist` - Itens de checklist
- `/v1/diagnosticos` - Diagnósticos

**URL Completa:** `http://localhost:8080/api/v1/ordens-servico/...`

---

### 📦 PRODUTOS E SERVIÇOS

**Produtos:**
- `/v1/produtos` - Produtos
- `/v1/categorias-produtos` - Categorias de produtos
- `/v1/produtos-fornecedores` - Produtos por fornecedor

**Serviços:**
- `/v1/servicos` - Serviços
- `/v1/categorias-servicos` - Categorias de serviços
- `/v1/servicos-precos` - Preços de serviços
- `/v1/tempos-servicos` - Tempos de serviços
- `/v1/kits-servicos` - Kits de serviços
- `/v1/especialidades-servicos` - Especialidades de serviços

**Outros:**
- `/v1/tabelas-precos` - Tabelas de preços
- `/v1/unidades-medida` - Unidades de medida
- `/v1/fornecedores` - Fornecedores

**Status:** ⚠️ **VERIFICAR PREFIXOS** - Alguns podem estar com `/api/v1/` incorreto

---

### 💰 FINANCEIRO

**Endpoints identificados:**
- `/api/financeiro/contas-bancarias` - ❌ **INCONSISTENTE** (deveria ser `/v1/financeiro/...`)
- `/api/financeiro/contas-receber` - ❌ **INCONSISTENTE**
- `/api/financeiro/contas-pagar` - ❌ **INCONSISTENTE**
- `/api/financeiro/fluxo-caixa` - ❌ **INCONSISTENTE**
- `/api/financeiro/faturas` - ❌ **INCONSISTENTE**
- `/api/financeiro/formas-pagamento` - ❌ **INCONSISTENTE**
- `/api/financeiro/condicoes-pagamento` - ❌ **INCONSISTENTE**
- E outros...

**Status:** ❌ **PRECISA CORREÇÃO** - Todos devem usar `/v1/financeiro/...`

---

### 🏢 EMPRESA

**Endpoints identificados:**
- `/api/v1/situacoes-empresa` - ❌ **INCONSISTENTE** (deveria ser `/v1/situacoes-empresa`)
- `/api/v1/setores-empresa` - ❌ **INCONSISTENTE**
- `/api/v1/localizacoes-empresa` - ❌ **INCONSISTENTE**
- `/api/v1/departamentos-contabio-empresa` - ❌ **INCONSISTENTE**

**Status:** ❌ **PRECISA CORREÇÃO**

---

### 👨‍💼 RH

**Endpoints identificados:**
- `/api/rh/funcionarios` - ❌ **INCONSISTENTE** (deveria ser `/v1/rh/...`)
- `/api/rh/departamentos` - ❌ **INCONSISTENTE**
- `/api/rh/especialidades` - ❌ **INCONSISTENTE**
- `/api/rh/cargos` - ❌ **INCONSISTENTE**
- E outros...

**Status:** ❌ **PRECISA CORREÇÃO**

---

### 📞 COMUNICAÇÃO

**Endpoints identificados:**
- `/api/comunicacao/questionarios` - ❌ **INCONSISTENTE** (deveria ser `/v1/comunicacao/...`)
- `/api/comunicacao/it-checklist` - ❌ **INCONSISTENTE**
- E outros...

**Status:** ❌ **PRECISA CORREÇÃO**

---

### 📅 AGENDAMENTOS

**Endpoints esperados (baseado no OpenAPIConfig):**
- `/api/agendamentos` - ❌ **INCONSISTENTE** (deveria ser `/v1/agendamentos/...`)
- `/api/agendamentos/tipos` - ❌ **INCONSISTENTE**
- E outros...

**Status:** ❌ **PRECISA CORREÇÃO**

---

## 🎯 PLANO DE CORREÇÃO

### FASE 1: PADRONIZAÇÃO DE URLS

1. **Remover prefixo `/api` de todos os controllers**
   - Todos os controllers devem usar apenas `/v1/...` ou `/auth`
   - O context-path `/api` já adiciona o prefixo automaticamente

2. **Controllers a corrigir:**
   - `SituacaoController`: `/api/v1/situacoes-empresa` → `/v1/situacoes-empresa`
   - `SetorController`: `/api/v1/setores-empresa` → `/v1/setores-empresa`
   - `LocalizacaoController`: `/api/v1/localizacoes-empresa` → `/v1/localizacoes-empresa`
   - `DepartamentoContabioController`: `/api/v1/departamentos-contabio-empresa` → `/v1/departamentos-contabio-empresa`
   - Todos os controllers de `financeiro`: `/api/financeiro/...` → `/v1/financeiro/...`
   - Todos os controllers de `rh`: `/api/rh/...` → `/v1/rh/...`
   - Todos os controllers de `comunicacao`: `/api/comunicacao/...` → `/v1/comunicacao/...`
   - Todos os controllers de `agendamento`: `/api/agendamentos/...` → `/v1/agendamentos/...`

### FASE 2: ATUALIZAÇÃO DO SWAGGER

1. **Atualizar `OpenAPIConfig.java`**
   - Remover prefixos `/api/` dos paths
   - Manter apenas `/v1/...` ou `/auth`

### FASE 3: VALIDAÇÃO DO FRONTEND

1. **Verificar services do frontend**
   - Garantir que todos usam `/v1/...` (sem `/api/`)
   - O baseUrl já adiciona `/api` automaticamente

---

## 📝 PRÓXIMOS PASSOS

1. ✅ Criar script para corrigir todos os `@RequestMapping`
2. ✅ Atualizar `OpenAPIConfig.java`
3. ✅ Verificar e corrigir services do frontend
4. ✅ Testar todos os endpoints após correção
5. ✅ Atualizar documentação

---

**Última atualização:** $(date)

