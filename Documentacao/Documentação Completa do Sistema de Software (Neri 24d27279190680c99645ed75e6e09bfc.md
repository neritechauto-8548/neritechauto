# Documentação Completa do Sistema de Software (NeriTech Auto)

# 📋 1. VISÃO GERAL DO SISTEMA

## 1.1 Introdução e Propósito

### Sobre o Sistema

O **NeriTech Auto** é uma plataforma SaaS (Software como Serviço) desenvolvida especificamente para atender às necessidades operacionais e administrativas de oficinas mecânicas de pequeno, médio e grande porte. O sistema foi projetado para digitalizar e otimizar todos os processos do dia a dia da oficina, desde o primeiro contato com o cliente até o faturamento e análise de resultados.

### Objetivos Principais

- **Automatizar** processos manuais e planilhas obsoletas
- **Centralizar** informações de clientes, veículos e ordens de serviço
- **Otimizar** o controle de estoque e financeiro
- **Melhorar** a experiência do cliente final
- **Aumentar** a produtividade da equipe
- **Reduzir** erros operacionais e retrabalho
- **Facilitar** a tomada de decisões através de relatórios gerenciais

### Público-Alvo

- Oficinas mecânicas de todos os portes
- Centros automotivos e autoelétricas
- Proprietários e gestores de oficinas
- Mecânicos e atendentes administrativos
- Profissionais do setor automotivo

### Diferenciais Competitivos

- ✨ **Interface moderna e intuitiva** - Facilidade de uso mesmo para usuários com baixa familiaridade tecnológica
- 🏗️ **Arquitetura multitenant** - Alta performance e escalabilidade
- 📊 **Relatórios completos** - Dashboards personalizados e análises detalhadas
- 🎯 **Suporte humanizado** - Onboarding assistido e suporte técnico dedicado
- 📱 **Acesso responsivo** - Funciona perfeitamente em qualquer dispositivo

## 1.2 Arquitetura Geral

### Modelo Arquitetural

O sistema adota uma **arquitetura baseada em microserviços**, separando os principais domínios de negócio em serviços independentes e escaláveis:

```
                           ┌─────────────────┐
                           │   Usuário Final │
                           └─────────┬───────┘
                                     │ HTTPS
                                     ▼
                           ┌─────────────────────┐
                           │  Frontend Angular   │ ◄── Vercel (CDN Global)
                           │   (SPA Responsiva)  │     • Build automático
                           │                     │     • SSL/TLS
                           └─────────┬───────────┘     • Cache estático
                                     │ HTTPS REST API
                                     ▼
        ┌──────────────────────────────────────────────────────────────────┐
        │                     NGINX API Gateway                            │
        │  • Load Balancer + Reverse Proxy                                 │
        │  • Autenticação JWT centralizada                                 │
        │  • Rate Limiting (100 req/min por usuário)                       │
        │  • CORS, SSL Termination                                         │
        │  • Request/Response Logging                                      │
        └─────┬────────┬────────┬────────┬────────┬────────┬────────┬──────┘
              │        │        │        │        │        │        │
              ▼        ▼        ▼        ▼        ▼        ▼        ▼
    ┌─────────────┐ ┌────────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌────────────┐  ┌──────────┐
    │   Auth      │ │Customer│ │  OS  │ │Stock │ │ Pay  │ │ Notification│ │ Report   │
    │  Service    │ │Service │ │Service│ │Service│ │Service│ │  Service   │ Service  │
    │ Port: 8001  │ │Port:8002│ │ 8003 │ │ 8004 │ │ 8005 │ │ Port: 8006 │ │Port: 8007│
    └─────────────┘ └────────┘ └──────┘ └──────┘ └──────┘ └────────────┘  └──────────┘
              │        │        │        │        │        │        │        │
              │ JDBC   │ JDBC   │ JDBC   │ JDBC   │ JDBC   │ JDBC   │ JDBC   │ JDBC
              ▼────────▼────────▼────────▼────────▼────────▼────────▼────────▼
    ┌─────────────────────────────────────────────────────────────────────────────────┐
    │                           Banco Supabase                                        │
    │  PostgreSQL 15+ com Multitenancy (tenant_id em todas as tabelas)              │
    │  • Connection Pooling (PgBouncer)                                              │
    │  • Backups automáticos (Point-in-time Recovery)                               │
    │  • Replicação read-only para relatórios                                       │
    │  • Row Level Security (RLS) por tenant                                        │
    │  • Índices otimizados para queries de relatórios                              │
    └─────────────────────────────────────────────────────────────────────────────────┘

```

### Detalhamento dos Microserviços

### 🔐 Auth Service (Porta 8001)

**Responsabilidades:**

- Autenticação de usuários via email/senha
- Geração e validação de tokens JWT
- Gerenciamento de perfis e permissões
- Reset de senhas via email
- Controle de sessões ativas

**Endpoints Principais:**

- `POST /auth/login` - Autenticação
- `POST /auth/refresh` - Renovação de token
- `GET /auth/me` - Dados do usuário logado
- `POST /auth/forgot-password` - Recuperação de senha
- `DELETE /auth/logout` - Logout (invalidar token)

**Tecnologias Específicas:**

- Spring Security + JWT
- BCrypt para hash de senhas
- JavaMail para envio de emails

### 👥 Customer Service (Porta 8002)

**Responsabilidades:**

- CRUD completo de clientes
- Gerenciamento de veículos associados
- Histórico de atendimentos
- Validação de CPF/CNPJ
- Busca avançada com filtros

**Endpoints Principais:**

- `GET /customers` - Listagem paginada
- `POST /customers` - Cadastro de cliente
- `PUT /customers/{id}` - Atualização
- `GET /customers/{id}/vehicles` - Veículos do cliente
- `GET /customers/search` - Busca avançada

**Tecnologias Específicas:**

- Spring Data JPA
- Validation API (Bean Validation)
- MapStruct para mapeamento DTO/Entity

### 🔧 OS Service (Porta 8003)

**Responsabilidades:**

- Criação e gestão de Ordens de Serviço
- Controle de status (Aberta → Em Andamento → Finalizada)
- Gerenciamento de itens (peças/serviços)
- Cálculo automático de valores
- Anexos e observações técnicas

**Endpoints Principais:**

- `POST /os` - Nova ordem de serviço
- `PUT /os/{id}/status` - Atualizar status
- `POST /os/{id}/items` - Adicionar itens
- `GET /os/{id}/timeline` - Histórico de alterações
- `POST /os/{id}/attachments` - Upload de anexos

**Tecnologias Específicas:**

- Spring Boot Starter Web
- MultipartFile para uploads
- State Machine para controle de status

### 📦 Stock Service (Porta 8004)

**Responsabilidades:**

- Controle de estoque de peças
- Movimentações (entrada/saída)
- Alertas de estoque mínimo
- Integração com fornecedores
- Relatórios de giro de estoque

**Endpoints Principais:**

- `GET /stock/products` - Listagem de produtos
- `POST /stock/movements` - Nova movimentação
- `GET /stock/alerts` - Produtos em falta
- `GET /stock/reports/turnover` - Relatório de giro

**Tecnologias Específicas:**

- Spring Data JPA
- Scheduler para verificação de alertas
- Redis para cache de consultas frequentes

### 💰 Payment Service (Porta 8005)

**Responsabilidades:**

- Processamento de pagamentos
- Integração com gateways (PIX, Cartão)
- Controle de contas a pagar/receber
- Emissão de recibos e faturas
- Conciliação bancária

**Endpoints Principais:**

- `POST /payments/process` - Processar pagamento
- `GET /payments/invoices` - Faturas pendentes
- `POST /payments/pix` - Gerar cobrança PIX
- `GET /payments/reports` - Relatórios financeiros

**Tecnologias Específicas:**

- RestTemplate para APIs externas
- Cryptography para dados sensíveis
- Scheduled tasks para conciliação

### 📨 Notification Service (Porta 8006)

**Responsabilidades:**

- Envio de emails transacionais
- Notificações push (futuro)
- Templates personalizáveis
- Agendamento de lembretes
- Histórico de comunicações

**Endpoints Principais:**

- `POST /notifications/email` - Enviar email
- `POST /notifications/schedule` - Agendar notificação
- `GET /notifications/templates` - Templates disponíveis
- `GET /notifications/history` - Histórico de envios

**Tecnologias Específicas:**

- Spring Boot Starter Mail
- Thymeleaf para templates
- RabbitMQ para filas assíncronas

### 📊 Report Service (Porta 8007)

**Responsabilidades:**

- Geração de relatórios em tempo real
- Processamento de grandes volumes de dados
- Exportação em múltiplos formatos (PDF, Excel, CSV)
- Cache inteligente de relatórios
- Dashboards e métricas de negócio
- Relatórios agendados e automáticos

**Endpoints Principais:**

- `POST /reports/generate` - Gerar relatório sob demanda
- `GET /reports/templates` - Templates de relatório disponíveis
- `POST /reports/schedule` - Agendar relatório automático
- `GET /reports/dashboard/{type}` - Dados para dashboards
- `POST /reports/export/{format}` - Exportar em PDF/Excel/CSV
- `GET /reports/cache/{id}` - Recuperar relatório em cache
- `GET /reports/status/{jobId}` - Status de processamento

**Tipos de Relatórios Suportados:**

- **Operacionais**: OS por período, produtividade por mecânico
- **Financeiros**: Receitas, despesas, fluxo de caixa, inadimplência
- **Estoque**: Giro de produtos, alertas, movimentações
- **Clientes**: Base ativa, histórico de atendimentos, fidelização
- **Analíticos**: Tendências, comparativos, projeções

**Tecnologias Específicas:**

- Apache POI (geração Excel)
- iText/OpenPDF (geração PDF)
- JasperReports (relatórios complexos)
- Redis para cache de relatórios
- CompletableFuture para processamento assíncrono
- Quartz Scheduler para relatórios agendados

**Arquitetura Interna do Report Service:**

```
┌─────────────────────────────────────────────────────────────────┐
│                    Report Service (8007)                        │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │   API Layer     │  │  Template       │  │   Export        │ │
│  │  • REST endpoints│  │  Engine         │  │   Engine        │ │
│  │  • Validation   │  │  • Jasper       │  │  • PDF (iText)  │ │
│  │  • Authentication│ │  • Custom SQL   │  │  • Excel (POI)  │ │
│  │  • Rate limiting│  │  • Aggregations │  │  • CSV          │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │  Cache Layer    │  │  Async          │  │  Scheduler      │ │
│  │  • Redis cache  │  │  Processing     │  │  • Quartz       │ │
│  │  • TTL strategy │  │  • Queue mgmt   │  │  • Cron jobs    │ │
│  │  • Invalidation │  │  • Progress     │  │  • Auto reports │ │
│  │  • Compression  │  │    tracking     │  │  • Email delivery│ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │              Data Access Layer                              │ │
│  │  • Read-only replica connection                             │ │
│  │  • Optimized queries for analytics                         │ │
│  │  • Connection pooling                                      │ │
│  │  • Query timeout management                                │ │
│  └─────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘

```

**Cache Strategy:**

- Relatórios estáticos: Cache de 24h
- Dashboards em tempo real: Cache de 5min
- Relatórios personalizados: Cache de 2h
- Invalidação automática quando dados base mudam

### Características da Arquitetura

### 🔧 **Modularidade**

- Cada serviço tem responsabilidade única e bem definida
- Desenvolvimento paralelo por equipes especializadas
- Reutilização de componentes entre serviços
- Isolamento de falhas (circuit breaker pattern)

### ⚡ **Escalabilidade**

- Scale horizontal independente por serviço
- Auto-scaling baseado em métricas (CPU, memória, requests/sec)
- Load balancing automático via NGINX
- Cache distribuído com Redis

### 🛠️ **Manutenibilidade**

- Deploys independentes via containers Docker
- Rollback granular por serviço
- Logs centralizados com correlação por request
- Health checks automáticos

### 🔄 **Comunicação Inter-serviços**

```
Auth Service ──────────► Customer Service (via JWT validation)
    │                           │
    ▼                           ▼
OS Service ◄─────────────► Stock Service (via REST API)
    │                           │
    │                           ▼
    └────────────────────► Report Service ◄────────► Payment Service
                               │                           │
                               ▼                           ▼
                        Notification Service (async via events)

```

**Fluxo de Geração de Relatórios:**

```
1. Frontend ────────► Report Service (solicita relatório)
                            │
2. Report Service ──────────┼─────► Customer Service (dados clientes)
                            │
3. Report Service ──────────┼─────► OS Service (dados ordens)
                            │
4. Report Service ──────────┼─────► Stock Service (dados estoque)
                            │
5. Report Service ──────────┼─────► Payment Service (dados financeiros)
                            │
6. Report Service ──────────┴─────► Notification Service (enviar por email)

```

### 🛡️ **Segurança Distribuída**

- **API Gateway**: Ponto único de entrada com SSL termination
- **JWT**: Tokens compartilhados entre todos os serviços
- **mTLS**: Comunicação criptografada entre serviços internos
- **Rate Limiting**: Proteção contra ataques DDoS
- **OWASP**: Compliance com top 10 vulnerabilidades

### 📊 **Monitoramento e Observabilidade**

```
┌─────────────────────────────────────────────────────────┐
│                    Monitoring Stack                     │
├─────────────────┬─────────────────┬─────────────────────┤
│   Prometheus    │     Grafana     │      ELK Stack      │
│  (Métricas)     │   (Dashboards)  │       (Logs)        │
├─────────────────┼─────────────────┼─────────────────────┤
│• Response time  │• Service health │• Error tracking     │
│• Request count  │• Business KPIs  │• Audit trails       │
│• Error rates    │• Resource usage │• Performance logs   │
│• Memory usage   │• Custom alerts  │• Security events    │
│• Report gen.    │• SLA monitoring │• Report activities  │
│  metrics        │• Report dashb.  │• Cache hit/miss     │
└─────────────────┴─────────────────┴─────────────────────┘

```

**Métricas Específicas do Report Service:**

- Tempo médio de geração por tipo de relatório
- Taxa de cache hit/miss
- Filas de processamento (tamanho, throughput)
- Uso de memória durante processamento pesado
- Frequência de uso por template
- Erros de geração e exportação

### Multitenancy (Multi-inquilino)

O sistema opera no modelo **multitenant com compartilhamento de banco**, implementado através de uma arquitetura sofisticada que garante isolamento total entre diferentes oficinas:

### 🏗️ **Arquitetura Multitenant**

```
┌─────────────────────────────────────────────────────────────────────┐
│                        REQUEST FLOW POR TENANT                      │
└─────────────────────────────────────────────────────────────────────┘

Oficina A (tenant_id: 1001)     Oficina B (tenant_id: 1002)
┌─────────────────┐             ┌─────────────────┐
│ app.oficina-a.  │             │ app.oficina-b.  │
│ neritech.com    │             │ neritech.com    │
└─────────┬───────┘             └─────────┬───────┘
          │                               │
          ▼                               ▼
┌─────────────────────────────────────────────────────────────┐
│                 NGINX Gateway                               │
│  • Extrai tenant_id do subdomínio ou header               │
│  • Adiciona X-Tenant-ID no request                        │
│  • Rate limiting por tenant                               │
└─────────┬───────────────────────────────────────────┬─────┘
          │                                           │
          ▼                                           ▼
┌─────────────────────────────┐         ┌─────────────────────┐
│      Microserviços          │         │   Tenant Context    │
│                             │         │   Interceptor       │
│ @Service                    │◄────────┤                     │
│ @TenantAware                │         │ • Valida tenant_id  │
│ findAllByTenantId(1001)     │         │ • Injeta filtros    │
│                             │         │ • Security check    │
└─────────┬───────────────────┘         └─────────────────────┘
          │
          ▼
┌─────────────────────────────────────────────────────────────┐
│                PostgreSQL Database                          │
│                                                             │
│  SELECT * FROM clientes                                     │
│  WHERE tenant_id = 1001  ← Filtro automático aplicado       │
│                                                             │
│  ┌─────────────┬─────────────┬──────────────┬─────────────┐ │
│  │ tenant_id   │ id          │ nome         │ email       │ │
│  ├─────────────┼─────────────┼──────────────┼─────────────┤ │
│  │ 1001        │ 1           │ João Silva   │ joao@...    │ │
│  │ 1001        │ 2           │ Maria Costa  │ maria@...   │ │
│  │ 1002        │ 1           │ Pedro Lima   │ pedro@...   │ │ ← Isolado
│  │ 1002        │ 2           │ Ana Santos   │ ana@...     │ │ ← Isolado
│  └─────────────┴─────────────┴──────────────┴─────────────┘ │
└─────────────────────────────────────────────────────────────┘

```

### 🔧 **Implementação Técnica**

**1. Identificação do Tenant:**

```java
@Component
public class TenantResolver {

    public String resolveTenant(HttpServletRequest request) {
        // Método 1: Por subdomínio
        String host = request.getServerName();
        if (host.contains(".")) {
            return host.split("\\\\.")[0]; // oficina-a.neritech.com → oficina-a
        }

        // Método 2: Por header customizado
        String tenantHeader = request.getHeader("X-Tenant-ID");
        if (tenantHeader != null) {
            return tenantHeader;
        }

        throw new TenantNotResolvedException("Tenant não identificado");
    }
}

```

**2. Context Holder Thread-Local:**

```java
@Component
public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static String getTenantId() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}

```

**3. Interceptador Automático:**

```java
@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) {
        String tenantId = tenantResolver.resolveTenant(request);
        TenantContext.setTenantId(tenantId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler, Exception ex) {
        TenantContext.clear();
    }
}

```

**4. Filtros Automáticos JPA:**

```java
@Entity
@Table(name = "clientes")
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = "string"))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    // Automatically set on save
    @PrePersist
    public void prePersist() {
        if (tenantId == null) {
            tenantId = TenantContext.getTenantId();
        }
    }
}

```

### 📊 **Características do Modelo**

| Aspecto | Implementação | Benefício |
| --- | --- | --- |
| **Identificação** | Subdomínio + Header HTTP | Flexibilidade na integração |
| **Isolamento** | `tenant_id` em todas as tabelas | Segurança de dados |
| **Performance** | Filtros automáticos via Hibernate | Queries otimizadas |
| **Economia** | Banco compartilhado | Redução de custos |
| **Escalabilidade** | Thread-local context | Suporte a alta concorrência |
| **Manutenção** | Schema único | Atualizações simplificadas |

### 🔒 **Segurança Multitenant**

**Camadas de Proteção:**

1. **API Gateway** - Validação inicial do tenant
2. **JWT Token** - Contém `tenant_id` assinado
3. **Application Layer** - Context interceptors
4. **Database Layer** - Row Level Security (RLS)
5. **Audit Trail** - Logs por tenant

**Exemplo de Token JWT:**

```json
{
  "sub": "usuario@oficina-a.com",
  "tenant_id": "1001",
  "roles": ["ADMIN", "GERENTE"],
  "oficina_name": "Oficina do João",
  "exp": 1640995200
}

```

### ⚡ **Vantagens do Modelo Escolhido**

**✅ Econômicas:**

- Compartilhamento de recursos de infraestrutura
- Redução de custos de banco e servidor
- Manutenção centralizada

**✅ Técnicas:**

- Atualizações simultâneas para todos os tenants
- Backup e recovery centralizados
- Monitoramento unificado

**✅ Escalabilidade:**

- Adição de novos tenants sem impacto
- Load balancing eficiente
- Cache compartilhado inteligente

**✅ Segurança:**

- Isolamento lógico robusto
- Auditoria centralizada
- Compliance facilitado

## 1.3 Tecnologias Utilizadas

### 🔧 Backend (Microserviços)

| Tecnologia | Versão | Propósito |
| --- | --- | --- |
| **Java** | 21 | Linguagem principal |
| **Spring Boot** | 3.x | Framework de desenvolvimento |
| **Spring Security** | Última | Autenticação e autorização |
| **Spring Web** | Última | APIs REST |
| **JPA/Hibernate** | Última | Mapeamento objeto-relacional |
| **PostgreSQL** | Última | Banco de dados relacional |
| **JWT** | - | Tokens de autenticação |
| **Springdoc OpenAPI** | Última | Documentação automática de APIs |

### 🖼️ Frontend

| Tecnologia | Versão | Propósito |
| --- | --- | --- |
| **Angular** | 19 | Framework frontend |
| **TypeScript** | Última | Linguagem tipada |
| **Angular HttpClient** | 19 | Comunicação com APIs |
| **CSS** | Padrão | Estilização (sem frameworks) |
| **RxJS** | Incluso | Programação reativa |

### ☁️ Infraestrutura e DevOps

| Serviço | Provedor | Propósito |
| --- | --- | --- |
| **Frontend Hosting** | Vercel | Hospedagem do Angular SPA |
| **Backend Hosting** | VPS (DigitalOcean/Hetzner) | Microserviços Spring Boot |
| **Banco de Dados** | Supabase | PostgreSQL gerenciado |
| **CI/CD** | GitHub Actions | Automação de deploy |
| **DNS/SSL** | Cloudflare | Gerenciamento de domínio |
| **Monitoramento** | UptimeRobot | Disponibilidade do sistema |

### 📚 Bibliotecas e Dependências

- **Backend**: Maven para gerenciamento de dependências
- **Frontend**: NPM para gerenciamento de pacotes
- **Logs**: SLF4J + Logback
- **Testes**: JUnit 5, Mockito, Jasmine, Karma
- **Versionamento**: Flyway para migrações de banco
- **Relatórios**: Apache POI (Excel), iText/OpenPDF (PDF), JasperReports
- **Cache**: Redis para performance e cache de relatórios
- **Agendamento**: Quartz Scheduler para tarefas automáticas
- **Filas**: RabbitMQ para processamento assíncrono

## 1.4 Requisitos do Sistema

### 🔧 Requisitos Técnicos Mínimos

| Componente | Especificação |
| --- | --- |
| **Navegador** | Chrome 90+, Firefox 88+, Safari 14+, Edge 90+ |
| **Conexão** | Internet banda larga (mínimo 1 Mbps) |
| **Dispositivos** | Desktop, tablet ou smartphone |
| **Resolução** | Mínima: 320px (mobile) |

### ⚡ Requisitos de Performance

- **Tempo de resposta**: APIs REST ≤ 2 segundos (95% das requisições)
- **Carregamento**: Interface ≤ 5 segundos (conexões 4G/fibra)
- **Disponibilidade**: 99.5% de uptime mensal
- **Concorrência**: Suporte a múltiplos usuários simultâneos

### 🔐 Requisitos de Segurança

- **Autenticação**: JWT com expiração de 2 horas
- **Criptografia**: HTTPS obrigatório (TLS 1.3)
- **Proteção**: Anti XSS, CSRF e SQL Injection
- **Backup**: Automatizado diário via Supabase
- **Compliance**: Conformidade com LGPD

### 📱 Requisitos de Usabilidade

- **Responsividade**: Adaptação automática a todos os dispositivos
- **Acessibilidade**: Contraste adequado e navegação por teclado
- **Intuitividade**: Máximo 3 cliques para ações principais
- **Feedback**: Mensagens claras de erro e confirmação
- **Offline**: Notificação quando sem conexão (sem funcionalidade offline)

## 1.5 Glossário de Termos

### A-C

**API (Application Programming Interface)** - Interface de programação que permite comunicação entre diferentes sistemas.

**Angular** - Framework TypeScript para desenvolvimento de aplicações web single-page (SPA).

**CI/CD (Continuous Integration/Continuous Deployment)** - Práticas de integração e entrega contínua de software.

**Cliente** - Pessoa física ou jurídica proprietária de veículos atendidos pela oficina.

### D-J

**Dashboard** - Painel de controle com indicadores e métricas do negócio.

**DTO (Data Transfer Object)** - Objeto usado para transferir dados entre camadas da aplicação.

**JWT (JSON Web Token)** - Padrão para tokens de autenticação compactos e seguros.

### L-O

**LGPD** - Lei Geral de Proteção de Dados Pessoais (Brasil).

**Microserviços** - Arquitetura que divide aplicações em serviços pequenos e independentes.

**Multitenant** - Arquitetura onde múltiplos clientes (oficinas) compartilham a mesma infraestrutura de forma isolada.

**OS (Ordem de Serviço)** - Documento que registra todos os serviços e peças utilizados em um atendimento.

### P-S

**PostgreSQL** - Sistema de gerenciamento de banco de dados relacional open-source.

**REST (Representational State Transfer)** - Estilo arquitetural para APIs web baseado em HTTP.

**SaaS (Software as a Service)** - Modelo de distribuição de software via internet por assinatura.

**SPA (Single Page Application)** - Aplicação web que carrega uma única página HTML e atualiza dinamicamente.

**Spring Boot** - Framework Java para desenvolvimento de aplicações empresariais.

### T-Z

**Tenant** - Inquilino do sistema; cada oficina é um tenant independente.

**TypeScript** - Linguagem que adiciona tipagem estática ao JavaScript.

**UX/UI** - User Experience/User Interface - Experiência e interface do usuário.

**VPS (Virtual Private Server)** - Servidor virtual privado para hospedagem de aplicações.

## 🎯 1.6. ESTRATÉGIA DE PRODUTO

### 1.6.1 Posicionamento Estratégico

### **Visão e Missão do Produto**

- **Visão:** Ser a plataforma SaaS líder no Brasil em gestão de oficinas mecânicas, reconhecida pela inovação, facilidade de uso e impacto direto na lucratividade dos clientes.
- **Missão:** Digitalizar e otimizar todos os processos de oficinas mecânicas, oferecendo uma solução completa que integra gestão operacional, atendimento ao cliente e inteligência de negócios.

### **North Star Metrics (Métricas Estrela Norte)**

- **Ativas por Tenant:** Número de ordens de serviço processadas por mês por cliente.
- **Retenção Mensal de Tenants:** Percentual de oficinas que mantêm assinatura.
- **Tempo Médio de Geração de OS:** Indicador de eficiência operacional.
- **Receita Recorrente Mensal (MRR – Monthly Recurring Revenue).**

### **Product-Market Fit Analysis (Análise de Adequação Produto-Mercado)**

- Forte aderência ao público-alvo (oficinas pequenas a grandes).
- Alta taxa de conversão em demonstrações (onboarding rápido).
- Baixa curva de aprendizado pela interface intuitiva.
- Demandas claras do mercado por integração com estoque, pagamentos e relatórios.

### **Competitive Intelligence (Inteligência Competitiva)**

- **Concorrentes Diretos:**
    1. **MinhaOficina** – gestão de ordens de serviço, lucro por serviço e comissões.
    2. **Oficina Inteligente** – envio de e-mail marketing, SMS e integração com notas fiscais.
    3. **Oficina Integrada** – registro de fluxo de ordens e acompanhamento pelo cliente.
    4. **BomSaldo ERP** – ERP completo com financeiro, estoque, OS e emissão de notas.
    5. **Mecânica Web** – funcionamento online/offline, gestão financeira, estoque e NF-e.
    6. **Mecânica Mais** – controle de OS, orçamentos, agendamentos e histórico de serviços.
    7. **Syscar** – OS, estoque, notas fiscais, relatórios e agendamento.
    8. **Ultracar** – estoque, NF-e, relatórios e agendamento de manutenções.
    9. **Motor SW** – OS, estoque, financeiro, app para aprovação de orçamentos.
- **Diferenciais da NeriTech Auto:**
    - Arquitetura multitenant com alta performance.
    - Relatórios personalizáveis e dashboards analíticos.
    - Suporte humanizado e onboarding assistido.
    - Interface moderna, responsiva e intuitiva.

### **Market Opportunity Assessment (Avaliação de Oportunidade de Mercado)**

- Mais de 100 mil oficinas no Brasil.
- Baixa penetração de SaaS especializados.
- Alta demanda por controle de estoque e gestão financeira.

---

### 1.6.2 Product Strategy Framework (Estrutura Estratégica do Produto)

### **Product Vision e Strategy (Visão e Estratégia do Produto)**

- **Visão:** Plataforma única para centralizar e automatizar todo o ciclo de operação das oficinas.
- **Estratégia:** Crescer base de clientes via modelo SaaS escalável, priorizando integração e UX.

### **OKRs – Objectives and Key Results (Objetivos e Resultados-Chave)**

- **O1:** Aumentar clientes ativos em 50% em 12 meses.
    - KR1: Reduzir churn (<3%).
    - KR2: Expandir canais digitais.
- **O2:** Melhorar performance.
    - KR1: Reduzir tempo de API em 20%.
    - KR2: Cache inteligente em relatórios críticos.

### **Roadmap Estratégico (12-24 meses)**

- 0-6 meses: módulos principais (Clientes, OS, Estoque, Pagamentos, Relatórios).
- 6-12 meses: integração com pagamentos e fornecedores.
- 12-18 meses: IA para previsão de demanda/manutenção.
- 18-24 meses: expansão internacional e integração com marketplaces.

### **Feature Prioritization Framework (Priorização de Funcionalidades)**

- Modelo RICE (Reach, Impact, Confidence, Effort).
- Foco em retenção e receita.

### **Investment Thesis por Área (Tese de Investimento por Área)**

- Infraestrutura: escalabilidade e disponibilidade.
- Produto: impacto direto no dia a dia.
- Suporte: onboarding rápido e proativo.

---

### 1.6.3 Customer Strategy (Estratégia de Cliente)

### **Ideal Customer Profile – ICP (Perfil Ideal de Cliente)**

- Oficinas de 3 a 20 funcionários.
- Necessidade de digitalização e controle.
- Disposição para pagar por eficiência.

### **Buyer Personas (Personas de Comprador)**

1. **Carlos** – dono de oficina pequena, foca no caixa.
2. **Mariana** – gestora, quer relatórios e controle de equipe.
3. **Roberto** – mecânico, busca rapidez e histórico.

### **Customer Journey Mapping (Jornada do Cliente)**

Descoberta → Demonstração → Onboarding → Uso → Expansão → Renovação.

### **Jobs-to-be-Done Framework (Trabalhos a Serem Feitos)**

- "Quando preciso registrar uma OS, quero rapidez e precisão."

### **User Research e Insights (Pesquisa de Usuário e Insights)**

- Pesquisas trimestrais.
- Entrevistas com clientes-chave.

---

### 1.6.4 Go-to-Market Strategy (Estratégia de Entrada no Mercado)

### **Launch Strategy (Estratégia de Lançamento)**

- Webinars, eventos e campanhas digitais.
- Programa beta com 20 oficinas.

### **Pricing Strategy e Monetização (Preço e Monetização)**

- SaaS mensal/anual.
- Planos escalonados.

### **Distribution Channels (Canais de Distribuição)**

- Inside sales.
- Parcerias e marketplaces.

### **Partnership Strategy (Parcerias)**

- Fornecedores e gateways de pagamento.
- Associações de oficinas.

### **Customer Acquisition Strategy (Aquisição de Clientes)**

- SEO, Google Ads e redes sociais.
- Programa de indicação.

---

### 1.6.5 Product Growth Strategy (Crescimento do Produto)

### **Growth Loops e Flywheels (Ciclos de Crescimento e Volantes de Inércia)**

- Indicação e dados.

### **Retention Strategy (Retenção)**

- Suporte proativo e treinamentos.

### **Expansion Revenue Opportunities (Receita por Expansão)**

- Módulos premium e integrações pagas.

### **Viral e Network Effects (Efeitos Virais e de Rede)**

- Funcionalidades colaborativas.

### **Product-Led Growth Tactics (Crescimento Guiado pelo Produto)**

- Trial de 14 dias e freemium.

---

### 1.6.6 Innovation Strategy (Inovação)

### **Technology Radar (Radar de Tecnologias)**

- Backend: Spring Boot + Java 21.
- Frontend: Angular 19.
- Infra: Supabase + Vercel + Cloudflare.

### **Innovation Pipeline (Pipeline de Inovação)**

- Monitoramento de tendências e laboratório interno.

### **R&D Investment Areas (Áreas de P&D)**

- IA para previsões e integrações automáticas.

### **Emerging Technologies Assessment (Avaliação de Tecnologias Emergentes)**

- IA generativa e IoT para manutenção.

### **Future Product Scenarios (Cenários Futuros)**

- Plataforma IoT conectada e gestão de frotas.

---

# 🎯 2. DOCUMENTAÇÃO DE PRODUTO - NERITECH AUTO

## 2.1 Especificações Funcionais

### Funcionalidades Principais

### 🔐 **Módulo de Autenticação e Autorização**

**Funcionalidades:**

- Login seguro com email e senha
- Recuperação de senha via email
- Gerenciamento de perfis de usuário
- Controle de permissões granular
- Sessões com timeout automático
- Auditoria de acessos

**Perfis de Usuário:**

| Perfil | Permissões | Casos de Uso |
| --- | --- | --- |
| **Administrador** | Acesso total ao sistema | Configuração, relatórios gerais, gestão de usuários |
| **Gerente** | Gestão operacional e financeira | Aprovação de orçamentos, relatórios, controle financeiro |
| **Recepcionista** | Atendimento ao cliente | Agendamentos, cadastros, abertura de OS |
| **Mecânico** | Execução de serviços | Atualização de OS, consumo de estoque |
| **Financeiro** | Controle monetário | Faturamento, contas a pagar/receber |

---

### 👥 **Módulo de Clientes e Veículos**

**Funcionalidades de Clientes:**

- Cadastro completo (PF/PJ) com validação de CPF/CNPJ
- Histórico completo de atendimentos
- Sistema de tags e categorização
- Controle de inadimplência
- Comunicação integrada (email/telefone)
- Programa de fidelidade

**Funcionalidades de Veículos:**

- Cadastro detalhado (marca, modelo, ano, placa)
- Registro de quilometragem
- Histórico de manutenções
- Alertas de revisão programada
- Documentação digital (CRLV, seguros)
- Galeria de fotos

**Validações e Integrações:**

- Consulta automática de dados por placa (API DENATRAN)
- Validação de chassi (dígito verificador)
- Histórico de proprietários
- Integração com seguradoras (futuro)

---

### 🔧 **Módulo de Ordens de Serviço (OS)**

**Ciclo de Vida da OS:**

```
Orçamento → OS Aberta → Em Andamento → Aguardando Peça →
Concluída → Entregue → Faturada

```

**Funcionalidades Principais:**

- Criação de orçamentos detalhados
- Conversão orçamento → OS
- Gestão de status em tempo real
- Adição dinâmica de itens/serviços
- Sistema de aprovações
- Anexos e evidências fotográficas
- Assinatura digital do cliente
- Integração com estoque automática

**Tipos de Itens:**

- **Serviços**: Mão de obra com tempo estimado
- **Peças**: Produtos do estoque com preços
- **Terceirizados**: Serviços externos
- **Garantias**: Extensões e coberturas

**Estados da OS:**

| Status | Descrição | Ações Permitidas |
| --- | --- | --- |
| **Orçamento** | Cotação sem compromisso | Editar, Aprovar, Cancelar |
| **Aberta** | OS aprovada, aguardando início | Iniciar, Reagendar |
| **Em Andamento** | Serviço sendo executado | Pausar, Adicionar itens, Finalizar |
| **Aguardando Peça** | Dependente de componente | Receber peça, Cancelar |
| **Concluída** | Serviço finalizado | Entregar, Faturar |
| **Entregue** | Veículo entregue ao cliente | Faturar apenas |
| **Faturada** | Processo completo | Apenas consulta |
| **Cancelada** | OS cancelada | Apenas consulta |

---

### 📅 **Módulo de Agendamentos**

**Funcionalidades:**

- Calendário visual interativo
- Agendamento por mecânico/serviço
- Bloqueio de horários ocupados
- Notificações automáticas
- Reagendamento simplificado
- Lista de espera automatizada

**Tipos de Agendamento:**

- **Manutenção Preventiva**: Revisões programadas
- **Manutenção Corretiva**: Reparos emergenciais
- **Orçamento**: Avaliações sem compromisso
- **Entrega**: Devolução de veículos

**Configurações:**

- Horários de funcionamento personalizáveis
- Duração padrão por tipo de serviço
- Intervalo entre atendimentos
- Feriados e pontos facultativos
- Capacidade máxima por período

---

### 🧰 **Módulo de Estoque e Produtos**

**Funcionalidades de Produtos:**

- Cadastro com código de barras
- Categorização hierárquica
- Múltiplas unidades de medida
- Preços diferenciados (custo/venda)
- Fornecedores principais e alternativos
- Localização no estoque (endereçamento)

**Controle de Movimentações:**

- **Entrada**: Compras, devoluções de clientes
- **Saída**: Vendas, uso em OS, perdas
- **Transferência**: Entre localizações
- **Ajuste**: Inventários e correções

**Alertas e Automações:**

- Estoque mínimo configurável
- Sugestões de reposição
- Relatório de produtos obsoletos
- Integração com fornecedores (EDI futuro)

**Precificação Inteligente:**

- Margem de lucro automática
- Preços promocionais
- Tabelas diferenciadas por perfil cliente
- Histórico de variações de preço

---

### 💰 **Módulo Financeiro**

**Contas a Receber:**

- Faturamento automático pós-OS
- Múltiplas formas de pagamento
- Parcelamento flexível
- Controle de inadimplência
- Negociação e renegociação
- Integração com serviços de cobrança

**Contas a Pagar:**

- Cadastro de fornecedores
- Controle de vencimentos
- Aprovação de pagamentos
- Conciliação bancária
- Rateio de centros de custo

**Formas de Pagamento:**

- **À Vista**: Dinheiro, PIX, débito
- **À Prazo**: Cartão de crédito, duplicatas
- **Financiado**: Parcerias com instituições
- **Misto**: Combinação de modalidades

**Relatórios Financeiros:**

- Fluxo de caixa projetado
- DRE simplificado
- Indicadores de performance
- Análise de inadimplência

---

### 📊 **Módulo de Relatórios e Dashboards**

**Dashboards Executivos:**

- KPIs principais em tempo real
- Gráficos interativos
- Comparativos históricos
- Projeções e tendências
- Alertas configuráveis

**Relatórios Operacionais:**

- Produtividade por mecânico
- Tempo médio de execução por serviço
- Taxa de retrabalho
- Utilização da capacidade

**Relatórios Financeiros:**

- Receitas por período/serviço
- Margem de contribuição
- Custos operacionais
- Análise de rentabilidade por cliente

**Relatórios Gerenciais:**

- Performance geral da oficina
- Satisfação do cliente (NPS)
- Giro de estoque
- Previsões de demanda

**Formatos de Exportação:**

- PDF para apresentações
- Excel para análises
- CSV para integrações
- Dashboards web responsivos

---

### 📨 **Módulo de Notificações**

**Tipos de Notificações:**

- **Email**: Confirmações, lembretes, relatórios
- **SMS**: Alertas urgentes (futuro)
- **WhatsApp**: Comunicação direta (futuro)
- **Push**: Notificações no sistema

**Eventos Automáticos:**

- Agendamento confirmado/cancelado
- OS iniciada/finalizada
- Pagamento processado/vencido
- Estoque baixo/zerado
- Aniversários de clientes

**Templates Personalizáveis:**

- Layout com marca da oficina
- Campos dinâmicos
- Múltiplos idiomas
- Assinatura digital

---

## 2.2 Casos de Uso Detalhados

### 🎯 **UC01: Processo Completo de Atendimento**

**Ator Principal:** Recepcionista

**Objetivo:** Realizar atendimento completo do cliente desde a chegada até a entrega

**Pré-condições:**

- Sistema disponível e usuário autenticado
- Cliente possui cadastro ou será cadastrado

**Fluxo Principal:**

1. **Recepção do Cliente**
    - Cliente chega à oficina
    - Recepcionista identifica cliente no sistema
    - Se novo cliente → executar UC02 (Cadastro de Cliente)
2. **Identificação do Problema**
    - Registrar reclamação/solicitação do cliente
    - Identificar veículo ou cadastrar novo
    - Realizar inspeção preliminar
3. **Criação do Orçamento**
    - Selecionar serviços necessários
    - Adicionar peças previstas
    - Calcular valor total automaticamente
    - Definir prazo de execução
4. **Aprovação do Cliente**
    - Apresentar orçamento detalhado
    - Cliente aprova ou solicita alterações
    - Registrar assinatura digital
    - Converter orçamento em OS
5. **Agendamento da Execução**
    - Verificar disponibilidade de mecânicos
    - Agendar data/hora de início
    - Confirmar disponibilidade de peças
    - Notificar cliente sobre agendamento
6. **Execução do Serviço**
    - Mecânico recebe OS
    - Atualiza status para "Em Andamento"
    - Executa serviços conforme especificado
    - Consome peças do estoque automaticamente
7. **Finalização e Entrega**
    - Mecânico finaliza OS
    - Recepcionista revisa trabalho executado
    - Gera fatura final
    - Entrega veículo ao cliente
    - Processa pagamento

**Fluxos Alternativos:**

- **A1**: Cliente recusa orçamento → Cancelar processo
- **A2**: Peça não disponível → Aguardar fornecimento
- **A3**: Problema adicional encontrado → Criar orçamento complementar
- **A4**: Cliente não comparece → Reagendar ou cancelar

**Pós-condições:**

- OS finalizada e faturada
- Estoque atualizado
- Cliente notificado
- Histórico registrado

---

### 🎯 **UC02: Cadastro Completo de Cliente**

**Ator Principal:** Recepcionista/Gerente

**Objetivo:** Cadastrar novo cliente com todas as informações necessárias

**Pré-condições:**

- Sistema disponível
- Documentos do cliente em mãos

**Fluxo Principal:**

1. **Coleta de Dados Básicos**
    - Nome completo/Razão social
    - CPF/CNPJ (com validação)
    - RG/IE quando aplicável
    - Data de nascimento/Abertura
2. **Informações de Contato**
    - Endereço completo com CEP
    - Telefones (fixo/celular)
    - Email principal
    - Preferências de comunicação
3. **Dados Complementares**
    - Profissão/Atividade
    - Como conheceu a oficina
    - Observações especiais
    - Autorizações LGPD
4. **Cadastro do Veículo Principal**
    - Marca, modelo, ano
    - Placa (com consulta DENATRAN)
    - Chassis, cor, combustível
    - Quilometragem atual
5. **Configurações da Conta**
    - Forma de pagamento preferida
    - Limites de crédito
    - Desconto padrão (se houver)
    - Tags de classificação

**Regras de Validação:**

- CPF/CNPJ válidos e únicos
- Email único no sistema
- Placa no padrão Mercosul/antigo
- Campos obrigatórios preenchidos

**Pós-condições:**

- Cliente cadastrado com sucesso
- Veículo principal vinculado
- Primeira OS pode ser criada

---

### 🎯 **UC03: Gestão de Estoque Avançada**

**Ator Principal:** Responsável pelo Estoque

**Objetivo:** Manter controle preciso do estoque com reposição inteligente

**Fluxo Principal:**

1. **Monitoramento Diário**
    - Verificar alertas de estoque baixo
    - Analisar movimentações do dia
    - Identificar produtos em falta
2. **Análise de Demanda**
    - Consultar histórico de consumo
    - Verificar sazonalidades
    - Projetar necessidades futuras
3. **Processo de Reposição**
    - Gerar sugestão automática de compras
    - Avaliar fornecedores disponíveis
    - Comparar preços e prazos
    - Criar pedido de compra
4. **Recebimento de Mercadorias**
    - Conferir produtos recebidos
    - Validar quantidades e qualidade
    - Registrar entrada no sistema
    - Atualizar localização no estoque
5. **Inventário Periódico**
    - Programar contagens cíclicas
    - Realizar ajustes necessários
    - Investigar divergências
    - Atualizar valores de custo

**Indicadores Monitorados:**

- Giro de estoque por categoria
- Tempo médio de reposição
- Taxa de ruptura (stockout)
- Valor imobilizado em estoque

---

## 2.3 Fluxos de Trabalho

### 🔄 **Fluxo 1: Manutenção Preventiva Programada**

```mermaid
graph TD
    A[Sistema Identifica Revisão Vencida] --> B{Cliente Cadastrado com Email?}
    B -->|Sim| C[Enviar Notificação Automática]
    B -->|Não| D[Gerar Lista para Contato Manual]

    C --> E[Cliente Responde Interesse]
    D --> F[Recepcionista Faz Contato]

    E --> G{Aceita Agendamento?}
    F --> G

    G -->|Sim| H[Agendar Data/Hora]
    G -->|Não| I[Registrar Recusa + Motivo]

    H --> J[Confirmar Disponibilidade Mecânico]
    J --> K[Reservar Peças Necessárias]
    K --> L[Enviar Lembrete 1 Dia Antes]
    L --> M[Cliente Comparece]
    M --> N[Executar Manutenção]
    N --> O[Faturar e Entregar]
    O --> P[Agendar Próxima Revisão]

    I --> Q[Agendar Novo Contato em 30 dias]

```

---

### 🔄 **Fluxo 2: Processo de Orçamento e Aprovação**

```mermaid
graph TD
    A[Cliente Solicita Orçamento] --> B[Recepcionista Cria Orçamento]
    B --> C[Adiciona Serviços Necessários]
    C --> D[Adiciona Peças Estimadas]
    D --> E[Sistema Calcula Total]

    E --> F{Valor > R$ 1000?}
    F -->|Sim| G[Requer Aprovação Gerente]
    F -->|Não| H[Apresentar ao Cliente]

    G --> I{Gerente Aprova?}
    I -->|Sim| H
    I -->|Não| J[Revisar Orçamento]
    J --> C

    H --> K{Cliente Aprova?}
    K -->|Sim| L[Converter em OS]
    K -->|Não| M{Solicita Alterações?}

    M -->|Sim| N[Revisar Itens]
    M -->|Não| O[Arquivar Orçamento]

    N --> C
    L --> P[Agendar Execução]
    P --> Q[Notificar Cliente]

```

---

### 🔄 **Fluxo 3: Controle de Qualidade e Entrega**

```mermaid
graph TD
    A[Mecânico Finaliza Serviço] --> B[Atualiza OS para 'Concluída']
    B --> C[Supervisor Técnico Revisa]

    C --> D{Qualidade OK?}
    D -->|Não| E[Solicitar Correções]
    D -->|Sim| F[Aprovar Finalização]

    E --> G[Retorna para Mecânico]
    G --> H[Corrigir Problemas]
    H --> A

    F --> I[Gerar Fatura Final]
    I --> J[Contatar Cliente para Retirada]
    J --> K{Cliente Comparece?}

    K -->|Não| L[Reagendar Entrega]
    K -->|Sim| M[Apresentar Serviços Executados]

    L --> J
    M --> N{Cliente Aprova Serviços?}
    N -->|Não| O[Verificar Reclamações]
    N -->|Sim| P[Processar Pagamento]

    O --> Q[Resolver Pendências]
    Q --> M

    P --> R[Entregar Veículo]
    R --> S[Solicitar Avaliação NPS]
    S --> T[Agendar Próxima Revisão]

```

---

### 🔄 **Fluxo 4: Processo de Compras e Reposição de Estoque**

```mermaid
graph TD
    A[Sistema Detecta Estoque Baixo] --> B[Gera Alerta Automático]
    B --> C[Responsável Avalia Necessidade]

    C --> D{Produto Essencial?}
    D -->|Sim| E[Compra Imediata]
    D -->|Não| F[Incluir em Próxima Compra]

    E --> G[Consultar Fornecedores Cadastrados]
    G --> H[Comparar Preços e Prazos]
    H --> I[Selecionar Melhor Proposta]
    I --> J[Gerar Pedido de Compra]

    J --> K{Valor > R$ 2000?}
    K -->|Sim| L[Requer Aprovação Gerente]
    K -->|Não| M[Enviar Pedido]

    L --> N{Gerente Aprova?}
    N -->|Sim| M
    N -->|Não| O[Revisar Pedido]
    O --> G

    M --> P[Acompanhar Status Entrega]
    P --> Q[Receber Mercadoria]
    Q --> R[Conferir Quantidade/Qualidade]
    R --> S[Registrar Entrada no Sistema]
    S --> T[Atualizar Preços de Custo]
    T --> U[Armazenar Fisicamente]

    F --> V[Aguardar Próximo Ciclo]
    V --> W[Compra Programada Semanal]
    W --> G

```

---

### 🔄 **Fluxo 5: Processamento de Pagamentos Múltiplos**

```mermaid
graph TD
    A[Cliente Escolhe Forma Pagamento] --> B{Tipo de Pagamento?}

    B -->|Dinheiro| C[Registrar Valor]
    B -->|PIX| D[Gerar QR Code]
    B -->|Cartão| E[Processar no TEF]
    B -->|Misto| F[Dividir Valores]

    C --> G[Calcular Troco]
    G --> H[Confirmar Pagamento]

    D --> I[Aguardar Confirmação Banco]
    I --> J{PIX Confirmado?}
    J -->|Sim| H
    J -->|Não| K[Tentar Novamente ou Trocar Forma]
    K --> A

    E --> L[Inserir/Aproximar Cartão]
    L --> M[Processar Transação]
    M --> N{Transação Aprovada?}
    N -->|Sim| H
    N -->|Não| O[Informar Erro e Tentar Novamente]
    O --> A

    F --> P[Processar Primeira Forma]
    P --> Q[Processar Segunda Forma]
    Q --> R[Validar Soma Total]
    R --> H

    H --> S[Emitir Comprovante]
    S --> T[Atualizar Financeiro]
    T --> U[Baixar Conta a Receber]
    U --> V[Notificar Cliente]

```

---

### 🔄 **Fluxo 6: Gestão de Garantias**

```mermaid
graph TD
    A[OS Finalizada com Garantia] --> B[Registrar Prazo Garantia]
    B --> C[Cliente Relata Problema]
    C --> D{Dentro do Prazo?}

    D -->|Não| E[Informar Expiração]
    D -->|Sim| F[Analisar Tipo Problema]

    E --> G[Criar Nova OS Comercial]

    F --> H{Problema Relacionado?}
    H -->|Não| I[Explicar Não Cobertura]
    H -->|Sim| J[Verificar Condições Uso]

    I --> G

    J --> K{Uso Adequado?}
    K -->|Não| L[Garantia Perdida - Má Uso]
    K -->|Sim| M[Aceitar Garantia]

    L --> N[Documentar Motivo Recusa]
    N --> G

    M --> O[Criar OS Garantia]
    O --> P[Agendar Reparo]
    P --> Q[Executar Serviço]
    Q --> R[Não Cobrar Cliente]
    R --> S[Registrar Custo Interno]
    S --> T[Avaliar Reincidência]
    T --> U[Atualizar Base Conhecimento]

```

---

### 🔄 **Fluxo 7: Processo de Inventário Cíclico**

```mermaid
graph TD
    A[Sistema Programa Inventário] --> B[Gerar Lista Contagem]
    B --> C[Designar Responsável]
    C --> D[Contar Produtos Fisicamente]

    D --> E[Registrar Quantidades]
    E --> F[Sistema Compara com Estoque]
    F --> G{Divergência Encontrada?}

    G -->|Não| H[Finalizar Inventário]
    G -->|Sim| I{Divergência > 5%?}

    I -->|Não| J[Ajustar Automaticamente]
    I -->|Sim| K[Recontar Produto]

    K --> L{Confirma Divergência?}
    L -->|Não| M[Manter Estoque Original]
    L -->|Sim| N[Investigar Causa]

    N --> O{Causa Identificada?}
    O -->|Sim| P[Documentar Motivo]
    O -->|Não| Q[Registrar Divergência S/ Causa]

    P --> R[Ajustar Estoque]
    Q --> R
    J --> R
    M --> H

    R --> S[Calcular Impacto Financeiro]
    S --> T[Gerar Relatório Ajustes]
    T --> U[Aprovar Ajustes > R$ 500]
    U --> V[Atualizar Sistema]
    V --> H

    H --> W[Programar Próximo Inventário]

```

---

### 🔄 **Fluxo 8: Processo de Reclamações e SAC**

```mermaid
graph TD
    A[Cliente Registra Reclamação] --> B[Sistema Gera Protocolo]
    B --> C[Classificar Tipo Reclamação]

    C --> D{Tipo de Reclamação?}
    D -->|Técnica| E[Encaminhar p/ Supervisor]
    D -->|Comercial| F[Encaminhar p/ Gerente]
    D -->|Financeira| G[Encaminhar p/ Financeiro]

    E --> H[Analisar Problema Técnico]
    H --> I{Procede Reclamação?}
    I -->|Sim| J[Agendar Reparo Garantia]
    I -->|Não| K[Explicar Situação]

    F --> L[Avaliar Questão Comercial]
    L --> M{Oferecer Compensação?}
    M -->|Sim| N[Definir Tipo Compensação]
    M -->|Não| O[Justificar Posição]

    G --> P[Revisar Cobrança]
    P --> Q{Cobrança Correta?}
    Q -->|Não| R[Corrigir e Ressarcir]
    Q -->|Sim| S[Explicar Cobrança]

    J --> T[Executar Solução]
    K --> T
    N --> T
    O --> T
    R --> T
    S --> T

    T --> U[Contatar Cliente]
    U --> V{Cliente Satisfeito?}
    V -->|Sim| W[Finalizar Protocolo]
    V -->|Não| X[Escalar p/ Direção]

    X --> Y[Análise Executiva]
    Y --> Z[Decisão Final]
    Z --> T

    W --> AA[Registrar Resolução]
    AA --> BB[Avaliar Processo]
    BB --> CC[Melhorar Procedimento]

```

---

## 2.4 Regras de Negócio

### 🎯 **RN01: Controle de Acesso e Permissões**

**Regras de Autenticação:**

- Senha deve ter mínimo 8 caracteres com letra, número e símbolo
- Bloqueio automático após 5 tentativas incorretas
- Sessão expira em 2 horas de inatividade
- Dois fatores obrigatório para perfil Administrador

**Permissões por Módulo:**

- **Financeiro**: Apenas perfis Gerente e Financeiro
- **Relatórios**: Todos podem visualizar, apenas Admin exporta
- **Configurações**: Exclusivo do Administrador
- **Estoque**: Mecânicos apenas consultam, não alteram

---

### 🎯 **RN02: Validações de Ordens de Serviço**

**Criação de OS:**

- Valor mínimo: R$ 50,00
- Máximo 20 itens por OS
- Serviços devem ter tempo estimado definido
- Peças devem estar disponíveis em estoque
- Cliente deve estar com CPF/CNPJ regular

**Alterações em OS:**

- OS "Em Andamento" só pode ser alterada pelo mecânico responsável
- Acréscimo > 20% requer nova aprovação do cliente
- Remoção de itens já faturados é proibida
- Cancelamento após início requer justificativa

**Finalização:**

- Todas as etapas devem estar concluídas
- Fotos obrigatórias para serviços > R$ 500
- Assinatura digital do cliente é obrigatória
- Mecânico deve registrar observações técnicas

---

---

### 🎯 **RN03: Controle de Estoque**

**Movimentações:**

- Saída automática apenas com OS aprovada
- Entrada requer nota fiscal ou documento hábil
- Transferências devem ser aprovadas por responsável
- Ajustes > 5% requerem justificativa detalhada

**Precificação:**

- Margem mínima de 30% sobre custo
- Preço não pode ser menor que último custo
- Promoções limitadas a 30 dias
- Descontos > 10% requerem aprovação gerencial

**Alertas e Controles:**

- Estoque mínimo = 15 dias de consumo médio
- Produtos sem movimento há 6 meses = Obsoletos
- Validade vencida em 30 dias gera alerta
- Custo médio recalculado a cada entrada

---

### 🎯 **RN04: Regras Financeiras**

**Faturamento:**

- Vencimento padrão: 30 dias para PJ, à vista para PF
- Juros de mora: 1% ao mês + multa 2%
- Desconto à vista: máximo 5%
- Parcelamento: máximo 6x sem juros

**Inadimplência:**

- Bloqueia novos serviços após 60 dias de atraso
- Negativação automática após 90 dias
- Cobrança terceirizada após 120 dias
- Renegociação com desconto máximo de 20%

**Conciliação:**

- Conferência diária obrigatória
- Divergências > R$ 100 investigadas imediatamente
- Fechamento mensal até o 5º dia útil
- Backup financeiro diário automático

---

### 🎯 **RN05: Comunicação com Clientes**

**Notificações Obrigatórias:**

- Confirmação de agendamento (24h antes)
- OS finalizada (imediato)
- Vencimento de fatura (3 dias antes)
- Revisão programada (30 dias antes)

**Preferências de Comunicação:**

- Cliente pode optar por email, SMS ou WhatsApp
- Horário comercial: 8h às 18h, seg-sex
- Comunicações promocionais requerem opt-in
- Direito ao opt-out garantido em todas as mensagens

**LGPD e Privacidade:**

- Consentimento registrado com data/hora
- Dados sensíveis criptografados
- Histórico de acessos auditado
- Exclusão de dados atendida em até 30 dias

---

### 🎯 **RN06: Agendamentos e Capacidade**

**Regras de Agendamento:**

- Antecedência mínima: 2 horas
- Máximo 3 reagendamentos por cliente/mês
- Overbooking permitido: 10% da capacidade
- Feriados e finais de semana configuráveis

**Gestão de Capacidade:**

- Cada mecânico: máximo 3 OS simultâneas
- Serviços complexos: bloqueiam 2 slots
- Tempo buffer: 15 minutos entre atendimentos
- Emergências: 20% da capacidade reservada

**Política de No-Show:**

- 1ª falta: advertência
- 2ª falta: cobrança de taxa R$ 50
- 3ª falta: bloqueio por 30 dias
- Justificativas aceitas com comprovação

---

### 🎯 **RN07: Controle de Garantias**

**Prazos de Garantia:**

- Serviços de motor: 90 dias ou 5.000 km
- Serviços gerais: 60 dias ou 3.000 km
- Peças originais: conforme fabricante
- Peças paralelas: 30 dias ou 2.000 km

**Condições para Garantia:**

- Uso normal do veículo (não competição/corrida)
- Manutenções em dia conforme manual
- Não alteração por terceiros
- Comprovante de troca de óleo regular

**Perda de Garantia:**

- Modificações no sistema reparado
- Uso inadequado comprovado
- Falta de manutenção preventiva
- Acidentes ou sinistros

**Processo de Acionamento:**

- Cliente deve agendar avaliação
- Prazo máximo 7 dias para análise
- Reparo gratuito se procedente
- Registro obrigatório no sistema

---

### 🎯 **RN08: Gestão de Fornecedores**

**Cadastro de Fornecedores:**

- CNPJ ativo e regular obrigatório
- Mínimo 2 anos de atividade
- Referências comerciais verificadas
- Certificações de qualidade (se aplicável)

**Avaliação de Performance:**

- Pontualidade na entrega (meta: 95%)
- Qualidade dos produtos (meta: 98%)
- Atendimento comercial (avaliação mensal)
- Preços competitivos (revisão trimestral)

**Política de Pagamento:**

- Novos fornecedores: pagamento à vista
- Parceiros: prazo até 30 dias
- Volume alto: negociação especial
- Atraso > 10 dias: bloqueio automático

**Gestão de Contratos:**

- Contratos acima R$ 10.000: juridico obrigatório
- Renovação automática se performance OK
- Cláusulas de qualidade e prazo
- Revisão anual de termos e preços

---

### 🎯 **RN09: Sistema de Relatórios**

**Geração de Relatórios:**

- Relatórios simples: geração imediata
- Relatórios complexos: processamento assíncrono
- Cache automático por 2 horas
- Limite: 10 relatórios por usuário/dia

**Exportação:**

- PDF: máximo 500 páginas
- Excel: máximo 50.000 linhas
- CSV: sem limitação de linhas
- Email: arquivos até 10MB

**Agendamento:**

- Apenas perfis Gerente+ podem agendar
- Máximo 5 relatórios agendados por usuário
- Execução apenas em horário noturno
- Email automático com resultado

**Retenção de Dados:**

- Relatórios cached: 24 horas
- Relatórios salvos: 90 dias
- Logs de acesso: 12 meses
- Dados históricos: 7 anos (legal)

---

### 🎯 **RN10: Auditoria e Logs**

**Eventos Auditados:**

- Login/logout de usuários
- Criação/alteração/exclusão de dados críticos
- Acesso a relatórios financeiros
- Alterações de preços e descontos
- Cancelamentos e estornos

**Retenção de Logs:**

- Logs técnicos: 30 dias
- Logs de auditoria: 5 anos
- Logs de segurança: 12 meses
- Backup de logs: semanal

**Integridade dos Dados:**

- Checksums em registros críticos
- Verificação diária automática
- Alerta imediato para inconsistências
- Backup incremental de 6 em 6 horas

**Compliance:**

- Trilha completa de alterações
- Identificação do usuário responsável
- Timestamp com fuso horário
- Motivo obrigatório para alterações críticas

---

### 🎯 **RN11: Backup e Recuperação**

**Política de Backup:**

- Backup completo: semanal (domingo 2h)
- Backup incremental: diário (23h)
- Backup de transações: de hora em hora
- Retenção: 30 backups completos

**Testes de Recuperação:**

- Teste mensal obrigatório
- Tempo máximo recuperação: 4 horas
- Documentação do processo atualizada
- RTO (Recovery Time Objective): 2 horas

**Cenários de Disaster Recovery:**

- Falha de hardware: migração automática
- Corrupção de dados: restore pontual
- Falha total: ativação de backup site
- Teste de DR semestral obrigatório

---

### 🎯 **RN12: Integração com Terceiros**

**APIs Externas Permitidas:**

- Serviços de pagamento homologados
- Consulta CPF/CNPJ (Serasa, SPC)
- Emissão de NF-e via certificadora
- Consulta CEP e endereços

**Limites de Integração:**

- Máximo 1000 calls/hora por API
- Timeout: 30 segundos por requisição
- Retry automático: 3 tentativas
- Fallback manual obrigatório

**Segurança nas Integrações:**

- Comunicação apenas HTTPS
- Tokens com expiração
- IP Whitelisting quando possível
- Log completo de requisições

**Monitoramento:**

- Health check das APIs: 5 minutos
- Alerta automático se indisponível
- Dashboard de status em tempo real
- Relatório mensal de disponibilidade

---

### 🎯 **RN13: Gestão de Usuários e Sessões**

**Criação de Usuários:**

- Email corporativo obrigatório
- Senha temporária na criação
- Troca obrigatória no primeiro acesso
- Termo de uso deve ser aceito

**Controle de Sessões:**

- Máximo 2 sessões simultâneas por usuário
- Logout automático em 2 horas inatividade
- Sessão única para perfil Administrador
- Bloqueio por tentativas inválidas

**Alteração de Perfis:**

- Apenas Administrador pode alterar
- Log obrigatório com justificativa
- Notificação ao usuário afetado
- Revisão trimestral de permissões

**Desativação de Usuários:**

- Funcionário desligado: bloqueio imediato
- Dados mantidos por auditoria
- Transferência de responsabilidades
- Confirmação dupla para reativar

---

### 🎯 **RN14: Qualidade e Satisfação**

**Pesquisa de Satisfação:**

- NPS obrigatório após cada OS > R$ 200
- Envio automático 24h após entrega
- Escala 0-10 com comentários opcionais
- Follow-up para notas ≤ 6

**Metas de Qualidade:**

- NPS médio mensal: ≥ 70
- Taxa retrabalho: ≤ 3%
- Tempo médio reparo: conforme tabela
- Reclamações: ≤ 2% das OS

**Ações Corretivas:**

- NPS < 60: plano ação imediato
- Reincidência reclamação: treinamento
- Meta não atingida: revisão processo
- Cliente detrator: contato pessoal gerência

**Programa de Melhoria:**

- Análise mensal dos indicadores
- Sugestões de clientes registradas
- Implementação de melhorias aprovadas
- Comunicação de mudanças aos clientes

---

### 📋 2.2 ESPECIFICAÇÕES TÉCNICAS - NERITECH AUTO

## A. 🏗️ Diagramas de Arquitetura

### Arquitetura Geral do Sistema

O NeriTech Auto adota uma **arquitetura de microserviços multitenant** com separação clara de responsabilidades e alta escalabilidade.

```mermaid
graph TB
    subgraph "Frontend Layer"
        A[Angular SPA<br/>📱 Responsivo]
    end

    subgraph "CDN & Security"
        B[Vercel CDN<br/>🌐 Global]
        C[Cloudflare<br/>🛡️ DDoS Protection]
    end

    subgraph "API Gateway Layer"
        D[NGINX Gateway<br/>⚡ Load Balancer<br/>🔐 JWT Validation<br/>📊 Rate Limiting]
    end

    subgraph "Microservices Layer"
        E[Auth Service<br/>:8001<br/>🔑 Authentication]
        F[Customer Service<br/>:8002<br/>👥 Clients & Vehicles]
        G[OS Service<br/>:8003<br/>🔧 Work Orders]
        H[Stock Service<br/>:8004<br/>📦 Inventory]
        I[Payment Service<br/>:8005<br/>💰 Financial]
        J[Notification Service<br/>:8006<br/>📧 Communications]
        K[Report Service<br/>:8007<br/>📊 Analytics]
    end

    subgraph "Data Layer"
        L[(PostgreSQL<br/>Supabase<br/>🏢 Multitenant DB)]
        M[Redis<br/>⚡ Cache & Sessions]
    end

    subgraph "External Services"
        N[Payment Gateways<br/>💳 PIX, Cards]
        O[DENATRAN API<br/>🚗 Vehicle Data]
        P[Email Service<br/>📮 SMTP]
        Q[CEP Services<br/>📍 Address]
    end

    A --> B
    B --> C
    C --> D

    D --> E
    D --> F
    D --> G
    D --> H
    D --> I
    D --> J
    D --> K

    E --> L
    F --> L
    G --> L
    H --> L
    I --> L
    J --> L
    K --> L

    E --> M
    F --> M
    G --> M
    H --> M
    I --> M
    J --> M
    K --> M

    I --> N
    F --> O
    J --> P
    F --> Q

```

### Fluxo de Requisição Multitenant

```mermaid
sequenceDiagram
    participant Client as 🖥️ Cliente (oficina-a.com)
    participant Gateway as 🌐 API Gateway
    participant Auth as 🔑 Auth Service
    participant Service as 🔧 Business Service
    participant DB as 🗄️ PostgreSQL

    Client->>Gateway: 1. HTTPS Request + JWT
    Gateway->>Gateway: 2. Extrai tenant_id do subdomínio
    Gateway->>Auth: 3. Valida JWT Token
    Auth-->>Gateway: 4. Token válido + permissions
    Gateway->>Gateway: 5. Injeta X-Tenant-ID header
    Gateway->>Service: 6. Forward request + tenant context
    Service->>Service: 7. TenantContext.setTenantId()
    Service->>DB: 8. SELECT * FROM table WHERE tenant_id = ?
    DB-->>Service: 9. Filtered data
    Service-->>Gateway: 10. Response
    Gateway-->>Client: 11. JSON Response

```

### Comunicação entre Microserviços

```mermaid
graph LR
    subgraph "Synchronous Communication"
        A[OS Service] -->|REST API| B[Customer Service]
        A -->|REST API| C[Stock Service]
        A -->|REST API| D[Payment Service]

        E[Report Service] -->|REST API| A
        E -->|REST API| B
        E -->|REST API| C
        E -->|REST API| D
    end

    subgraph "Asynchronous Communication"
        F[Event Publisher] -.->|RabbitMQ| G[Notification Service]
        A -.->|OS_COMPLETED| F
        D -.->|PAYMENT_PROCESSED| F
        C -.->|STOCK_LOW| F
    end

    subgraph "Security Layer"
        H[mTLS] --> A
        H --> B
        H --> C
        H --> D
        H --> E
    end

```

---

## B. 🗃️ Modelo de Dados

### Diagrama Entidade-Relacionamento Completo

```mermaid
erDiagram
    INQUILINOS ||--o{ USUARIOS : contem
    INQUILINOS ||--o{ CLIENTES : possui
    INQUILINOS ||--o{ PRODUTOS : gerencia
    INQUILINOS ||--o{ ORDENS_SERVICO : processa
    INQUILINOS ||--o{ FORNECEDORES : trabalha_com
    INQUILINOS ||--o{ FUNCIONARIOS : emprega
    INQUILINOS ||--|| CONFIGURACOES_EMPRESA : tem
    INQUILINOS ||--o{ AGENDAMENTOS : controla

    CLIENTES ||--o{ VEICULOS : possui
    CLIENTES ||--o{ ORDENS_SERVICO : solicita
    CLIENTES ||--o{ AGENDAMENTOS : agenda

    VEICULOS ||--o{ ORDENS_SERVICO : objeto_de
    VEICULOS ||--o{ AGENDAMENTOS : para
    VEICULOS ||--o{ HISTORICO_MANUTENCAO : tem

    ORDENS_SERVICO ||--o{ ITENS_ORDEM : contem
    ORDENS_SERVICO ||--|| PAGAMENTOS : gera
    ORDENS_SERVICO ||--o{ GARANTIAS : oferece
    ORDENS_SERVICO ||--o{ ANEXOS_OS : possui

    PRODUTOS ||--o{ ITENS_ORDEM : usado_em
    PRODUTOS ||--o{ MOVIMENTACOES_ESTOQUE : rastreia
    PRODUTOS ||--|| FORNECEDORES_PRODUTOS : fornecido_por

    FORNECEDORES ||--o{ FORNECEDORES_PRODUTOS : fornece
    FORNECEDORES ||--o{ PEDIDOS_COMPRA : recebe
    FORNECEDORES ||--o{ CONTAS_PAGAR : deve_receber

    FUNCIONARIOS ||--o{ ORDENS_SERVICO : executa
    FUNCIONARIOS ||--o{ AGENDAMENTOS : atende
    FUNCIONARIOS ||--o{ COMISSOES : recebe
    FUNCIONARIOS ||--|| CARGOS : possui

    AGENDAMENTOS ||--|| SERVICOS_AGENDAVEIS : do_tipo
    AGENDAMENTOS ||--o{ NOTIFICACOES : gera

    CONFIGURACOES_EMPRESA ||--o{ CONFIGURACOES_NFE : possui
    CONFIGURACOES_EMPRESA ||--o{ CONFIGURACOES_EMAIL : possui

    ORDENS_SERVICO ||--o{ NOTAS_FISCAIS : geram
    NOTAS_FISCAIS ||--o{ ITENS_NOTA_FISCAL : contem

    INQUILINOS {
        uuid id PK
        string nome_fantasia
        string razao_social
        string cnpj UK
        string subdominio UK
        jsonb configuracoes
        enum plano_assinatura
        decimal valor_mensalidade
        date vencimento_assinatura
        timestamp criado_em
        boolean ativo
    }

    USUARIOS {
        uuid id PK
        uuid inquilino_id FK
        string nome_completo
        string email UK
        string senha_hash
        enum perfil
        jsonb permissoes
        string telefone
        string foto_perfil
        timestamp ultimo_acesso
        boolean ativo
        timestamp criado_em
    }

    CLIENTES {
        uuid id PK
        uuid inquilino_id FK
        string nome_razao_social
        string cpf_cnpj UK
        string rg_ie
        string email
        string telefone_principal
        string telefone_secundario
        jsonb endereco
        date data_nascimento_abertura
        decimal limite_credito
        enum tipo_pessoa
        text observacoes
        jsonb preferencias_comunicacao
        boolean ativo
        timestamp criado_em
    }

    VEICULOS {
        uuid id PK
        uuid inquilino_id FK
        uuid cliente_id FK
        string placa UK
        string marca
        string modelo
        integer ano_fabricacao
        integer ano_modelo
        string chassis
        string cor
        enum tipo_combustivel
        integer quilometragem
        string numero_motor
        date data_compra
        jsonb documentacao
        text observacoes
        boolean ativo
        timestamp criado_em
    }

    FUNCIONARIOS {
        uuid id PK
        uuid inquilino_id FK
        uuid cargo_id FK
        string nome_completo
        string cpf UK
        string rg
        string email
        string telefone
        jsonb endereco
        date data_nascimento
        date data_admissao
        date data_demissao
        decimal salario_base
        decimal percentual_comissao
        string especialidades
        jsonb documentos
        enum status_funcionario
        timestamp criado_em
    }

    CARGOS {
        uuid id PK
        uuid inquilino_id FK
        string nome
        text descricao
        jsonb permissoes_sistema
        decimal salario_base_sugerido
        boolean pode_executar_servicos
        boolean pode_atender_clientes
        boolean ativo
        timestamp criado_em
    }

    FORNECEDORES {
        uuid id PK
        uuid inquilino_id FK
        string nome_fantasia
        string razao_social
        string cnpj UK
        string ie
        string email
        string telefone
        jsonb endereco
        string nome_contato
        string telefone_contato
        enum condicoes_pagamento
        integer prazo_entrega_padrao
        decimal limite_credito
        text observacoes
        enum status_fornecedor
        timestamp criado_em
    }

    PRODUTOS {
        uuid id PK
        uuid inquilino_id FK
        string codigo_interno UK
        string codigo_barras
        string nome
        text descricao
        uuid categoria_id FK
        string marca
        string modelo
        string unidade_medida
        decimal preco_custo
        decimal preco_venda
        decimal margem_lucro
        integer quantidade_estoque
        integer estoque_minimo
        integer estoque_maximo
        string localizacao_estoque
        decimal peso
        jsonb especificacoes_tecnicas
        boolean controla_estoque
        boolean ativo
        timestamp criado_em
    }

    CATEGORIAS_PRODUTOS {
        uuid id PK
        uuid inquilino_id FK
        string nome
        text descricao
        uuid categoria_pai_id FK
        string codigo
        boolean ativo
        timestamp criado_em
    }

    FORNECEDORES_PRODUTOS {
        uuid id PK
        uuid inquilino_id FK
        uuid fornecedor_id FK
        uuid produto_id FK
        string codigo_fornecedor
        decimal preco_compra
        integer prazo_entrega
        integer quantidade_minima_pedido
        boolean fornecedor_principal
        timestamp criado_em
    }

    ORDENS_SERVICO {
        uuid id PK
        uuid inquilino_id FK
        string numero_os UK
        uuid cliente_id FK
        uuid veiculo_id FK
        uuid funcionario_responsavel FK
        uuid agendamento_id FK
        enum status_os
        decimal valor_total
        decimal valor_mao_obra
        decimal valor_pecas
        decimal valor_desconto
        text problema_relatado
        text solucao_aplicada
        text observacoes_tecnicas
        jsonb anexos
        date data_entrada
        date data_prevista_conclusao
        date data_conclusao
        timestamp criado_em
        uuid criado_por FK
    }

    ITENS_ORDEM {
        uuid id PK
        uuid inquilino_id FK
        uuid ordem_servico_id FK
        uuid produto_id FK
        uuid servico_id FK
        enum tipo_item
        string descricao
        integer quantidade
        decimal valor_unitario
        decimal valor_total
        decimal percentual_desconto
        text observacoes
        timestamp criado_em
    }

    SERVICOS_CATALOGO {
        uuid id PK
        uuid inquilino_id FK
        string codigo UK
        string nome
        text descricao
        decimal preco_base
        integer tempo_estimado_minutos
        enum categoria_servico
        boolean requer_especializacao
        text materiais_necessarios
        boolean ativo
        timestamp criado_em
    }

    AGENDAMENTOS {
        uuid id PK
        uuid inquilino_id FK
        uuid cliente_id FK
        uuid veiculo_id FK
        uuid funcionario_id FK
        uuid servico_agendavel_id FK
        datetime data_hora_agendada
        datetime data_hora_fim_estimada
        enum status_agendamento
        enum tipo_agendamento
        text observacoes_cliente
        text observacoes_internas
        decimal valor_estimado
        boolean confirmado_cliente
        timestamp lembrete_enviado
        timestamp criado_em
        uuid criado_por FK
    }

    SERVICOS_AGENDAVEIS {
        uuid id PK
        uuid inquilino_id FK
        string nome
        text descricao
        integer duracao_minutos
        decimal preco_base
        enum tipo_servico
        boolean permite_agendamento_online
        integer antecedencia_minima_horas
        boolean ativo
        timestamp criado_em
    }

    CONFIGURACOES_EMPRESA {
        uuid id PK
        uuid inquilino_id FK
        string nome_fantasia
        string razao_social
        string cnpj
        string ie
        string im
        jsonb endereco
        string telefone
        string email
        string site
        string logo_url
        jsonb horario_funcionamento
        string regime_tributario
        decimal percentual_iss
        timestamp criado_em
        timestamp atualizado_em
    }

    CONFIGURACOES_NFE {
        uuid id PK
        uuid inquilino_id FK
        string certificado_digital
        string senha_certificado
        integer numero_serie_nfe
        enum ambiente_nfe
        string codigo_municipio
        boolean emissao_automatica
        string template_danfe
        jsonb configuracoes_extras
        boolean ativo
        timestamp criado_em
    }

    CONFIGURACOES_EMAIL {
        uuid id PK
        uuid inquilino_id FK
        string servidor_smtp
        integer porta_smtp
        string usuario_smtp
        string senha_smtp
        boolean usar_tls
        string email_padrao_envio
        string nome_remetente
        jsonb templates_email
        boolean ativo
        timestamp criado_em
    }

    NOTAS_FISCAIS {
        uuid id PK
        uuid inquilino_id FK
        uuid ordem_servico_id FK
        integer numero_nfe
        integer serie_nfe
        string chave_acesso
        datetime data_emissao
        decimal valor_total
        decimal valor_tributos
        enum status_nfe
        text observacoes
        string arquivo_xml
        string arquivo_pdf
        timestamp criado_em
    }

    ITENS_NOTA_FISCAL {
        uuid id PK
        uuid inquilino_id FK
        uuid nota_fiscal_id FK
        uuid produto_id FK
        uuid servico_id FK
        string descricao
        integer quantidade
        decimal valor_unitario
        decimal valor_total
        string codigo_ncm
        decimal aliquota_icms
        decimal valor_icms
        timestamp criado_em
    }

    MOVIMENTACOES_ESTOQUE {
        uuid id PK
        uuid inquilino_id FK
        uuid produto_id FK
        enum tipo_movimentacao
        integer quantidade
        decimal custo_unitario
        decimal valor_total
        uuid referencia_id
        string tipo_referencia
        text observacoes
        uuid funcionario_id FK
        timestamp criado_em
    }

    PEDIDOS_COMPRA {
        uuid id PK
        uuid inquilino_id FK
        string numero_pedido UK
        uuid fornecedor_id FK
        enum status_pedido
        decimal valor_total
        date data_pedido
        date data_prevista_entrega
        date data_entrega
        text observacoes
        uuid criado_por FK
        uuid aprovado_por FK
        timestamp criado_em
    }

    ITENS_PEDIDO_COMPRA {
        uuid id PK
        uuid inquilino_id FK
        uuid pedido_compra_id FK
        uuid produto_id FK
        integer quantidade_solicitada
        integer quantidade_recebida
        decimal preco_unitario
        decimal valor_total
        text observacoes
        timestamp criado_em
    }

    CONTAS_PAGAR {
        uuid id PK
        uuid inquilino_id FK
        uuid fornecedor_id FK
        uuid pedido_compra_id FK
        string numero_documento
        decimal valor_original
        decimal valor_pago
        decimal valor_desconto
        decimal valor_juros
        date data_vencimento
        date data_pagamento
        enum status_conta
        enum forma_pagamento
        text observacoes
        timestamp criado_em
    }

    CONTAS_RECEBER {
        uuid id PK
        uuid inquilino_id FK
        uuid cliente_id FK
        uuid ordem_servico_id FK
        string numero_documento
        decimal valor_original
        decimal valor_recebido
        decimal valor_desconto
        decimal valor_juros
        date data_vencimento
        date data_recebimento
        enum status_conta
        enum forma_pagamento
        text observacoes
        timestamp criado_em
    }

    PAGAMENTOS {
        uuid id PK
        uuid inquilino_id FK
        uuid ordem_servico_id FK
        uuid conta_receber_id FK
        enum metodo_pagamento
        decimal valor_pagamento
        enum status_pagamento
        string id_transacao
        jsonb resposta_gateway
        decimal taxa_cartao
        integer numero_parcelas
        text observacoes
        timestamp processado_em
        timestamp criado_em
    }

    GARANTIAS {
        uuid id PK
        uuid inquilino_id FK
        uuid ordem_servico_id FK
        uuid produto_id FK
        uuid servico_id FK
        integer prazo_dias
        integer quilometragem_limite
        date data_inicio
        date data_fim
        text condicoes_garantia
        text observacoes
        enum status_garantia
        timestamp criado_em
    }

    HISTORICO_MANUTENCAO {
        uuid id PK
        uuid inquilino_id FK
        uuid veiculo_id FK
        uuid ordem_servico_id FK
        integer quilometragem
        date data_manutencao
        text descricao_servicos
        decimal valor_gasto
        text observacoes
        timestamp criado_em
    }

    COMISSOES {
        uuid id PK
        uuid inquilino_id FK
        uuid funcionario_id FK
        uuid ordem_servico_id FK
        decimal valor_base_calculo
        decimal percentual_comissao
        decimal valor_comissao
        enum status_comissao
        date competencia
        date data_pagamento
        text observacoes
        timestamp criado_em
    }

    ANEXOS_OS {
        uuid id PK
        uuid inquilino_id FK
        uuid ordem_servico_id FK
        string nome_arquivo
        string tipo_arquivo
        integer tamanho_bytes
        string url_arquivo
        text descricao
        uuid enviado_por FK
        timestamp criado_em
    }

    NOTIFICACOES {
        uuid id PK
        uuid inquilino_id FK
        uuid destinatario_id FK
        enum tipo_notificacao
        string assunto
        text conteudo
        jsonb metadados
        enum status_notificacao
        enum canal_envio
        timestamp enviado_em
        timestamp lido_em
        integer tentativas_envio
        timestamp criado_em
    }

    RELATORIOS {
        uuid id PK
        uuid inquilino_id FK
        uuid usuario_id FK
        string tipo_relatorio
        string nome_relatorio
        jsonb parametros
        jsonb dados_resultado
        enum status_processamento
        string caminho_arquivo
        decimal tamanho_arquivo_mb
        timestamp processado_em
        timestamp criado_em
    }

    CATEGORIAS_PRODUTOS ||--o{ PRODUTOS : categoriza
    CATEGORIAS_PRODUTOS ||--o{ CATEGORIAS_PRODUTOS : subcategoria_de

    PEDIDOS_COMPRA ||--o{ ITENS_PEDIDO_COMPRA : contem
    SERVICOS_CATALOGO ||--o{ ITENS_ORDEM : oferecido_como
    CONTAS_RECEBER ||--|| PAGAMENTOS : quitada_por

```

### Estratégia de Particionamento Multitenant

```mermaid
graph TB
    subgraph "Row Level Security (RLS)"
        A[Todas as tabelas contêm tenant_id]
        B[Políticas RLS por tenant]
        C[Filtros automáticos via Hibernate]
    end

    subgraph "Índices Otimizados"
        D[btree: tenant_id + created_at]
        E[btree: tenant_id + status]
        F[btree: tenant_id + customer_id]
    end

    subgraph "Backup e Recovery"
        G[Backup por tenant possível]
        H[Restore granular]
        I[Migração de dados isolada]
    end

    A --> D
    B --> E
    C --> F

```

---

## C. 🔗 APIs e Integrações

### Endpoints Principais por Microserviço

### 🔒 Auth Service (Port 8001)

**Responsabilidades**: Autenticação, autorização, usuários, perfis e permissões

```
# Autenticação
POST   /auth/login              # Autenticação
POST   /auth/refresh            # Renovar token
GET    /auth/me                 # Dados do usuário
POST   /auth/forgot-password    # Recuperar senha
DELETE /auth/logout             # Encerrar sessão
POST   /auth/change-password    # Alterar senha

# Gestão de Usuários
GET    /users                   # Listar usuários
POST   /users                   # Criar usuário
GET    /users/{id}              # Buscar usuário
PUT    /users/{id}              # Atualizar usuário
DELETE /users/{id}              # Remover usuário
PUT    /users/{id}/status       # Ativar/desativar usuário
PUT    /users/{id}/permissions  # Atualizar permissões

# Cargos e Perfis
GET    /roles                   # Listar cargos
POST   /roles                   # Criar cargo
GET    /roles/{id}              # Buscar cargo
PUT    /roles/{id}              # Atualizar cargo
DELETE /roles/{id}              # Remover cargo
GET    /roles/{id}/permissions  # Permissões do cargo

# Configurações de Empresa
GET    /company/settings        # Configurações da empresa
PUT    /company/settings        # Atualizar configurações
GET    /company/nfe-config      # Configurações NFe
PUT    /company/nfe-config      # Atualizar config NFe
GET    /company/email-config    # Configurações de email
PUT    /company/email-config    # Atualizar config email

```

### 👥 Customer Service (Port 8002)

**Responsabilidades**: Clientes, veículos, fornecedores, histórico

```
# Gestão de Clientes
GET    /customers               # Listar clientes
POST   /customers               # Criar cliente
GET    /customers/{id}          # Buscar cliente
PUT    /customers/{id}          # Atualizar cliente
DELETE /customers/{id}          # Remover cliente
GET    /customers/search        # Buscar clientes
PUT    /customers/{id}/status   # Ativar/desativar cliente

# Veículos
GET    /customers/{id}/vehicles # Veículos do cliente
POST   /customers/{id}/vehicles # Adicionar veículo
GET    /vehicles/{id}           # Buscar veículo
PUT    /vehicles/{id}           # Atualizar veículo
DELETE /vehicles/{id}           # Remover veículo
GET    /vehicles/search         # Buscar veículos
GET    /vehicles/{id}/history   # Histórico do veículo

# Histórico de Manutenção
GET    /vehicles/{id}/maintenance     # Histórico de manutenções
POST   /vehicles/{id}/maintenance     # Adicionar manutenção
GET    /maintenance/{id}              # Buscar manutenção
PUT    /maintenance/{id}              # Atualizar manutenção
DELETE /maintenance/{id}              # Remover manutenção

# Fornecedores
GET    /suppliers               # Listar fornecedores
POST   /suppliers               # Criar fornecedor
GET    /suppliers/{id}          # Buscar fornecedor
PUT    /suppliers/{id}          # Atualizar fornecedor
DELETE /suppliers/{id}          # Remover fornecedor
GET    /suppliers/{id}/products # Produtos do fornecedor
POST   /suppliers/{id}/products # Vincular produto
GET    /suppliers/search        # Buscar fornecedores

# Integração Externa
GET    /integration/vehicle-data/{plate}  # Consulta DENATRAN
GET    /integration/cnpj/{cnpj}           # Consulta Receita Federal
GET    /integration/cep/{cep}             # Consulta ViaCEP

```

### 🔧 OS Service (Port 8003)

**Responsabilidades**: Ordens de serviço, agendamentos, serviços, garantias

```
# Ordens de Serviço
GET    /work-orders             # Listar ordens
POST   /work-orders             # Criar ordem
GET    /work-orders/{id}        # Buscar ordem
PUT    /work-orders/{id}        # Atualizar ordem
DELETE /work-orders/{id}        # Remover ordem
PUT    /work-orders/{id}/status # Atualizar status
GET    /work-orders/search      # Buscar ordens
GET    /work-orders/dashboard   # Dashboard ordens

# Itens da Ordem
GET    /work-orders/{id}/items  # Listar itens da ordem
POST   /work-orders/{id}/items  # Adicionar item
PUT    /work-orders/{id}/items/{itemId} # Atualizar item
DELETE /work-orders/{id}/items/{itemId} # Remover item

# Anexos
GET    /work-orders/{id}/attachments    # Listar anexos
POST   /work-orders/{id}/attachments    # Upload anexo
GET    /attachments/{id}                # Download anexo
DELETE /attachments/{id}                # Remover anexo

# Agendamentos
GET    /appointments            # Listar agendamentos
POST   /appointments            # Criar agendamento
GET    /appointments/{id}       # Buscar agendamento
PUT    /appointments/{id}       # Atualizar agendamento
DELETE /appointments/{id}       # Remover agendamento
PUT    /appointments/{id}/status # Confirmar/cancelar
GET    /appointments/calendar   # Visualização calendário
GET    /appointments/available-slots # Horários disponíveis

# Serviços Agendáveis
GET    /appointment-services    # Listar serviços agendáveis
POST   /appointment-services    # Criar serviço agendável
GET    /appointment-services/{id} # Buscar serviço
PUT    /appointment-services/{id} # Atualizar serviço
DELETE /appointment-services/{id} # Remover serviço

# Catálogo de Serviços
GET    /service-catalog         # Listar serviços
POST   /service-catalog         # Criar serviço
GET    /service-catalog/{id}    # Buscar serviço
PUT    /service-catalog/{id}    # Atualizar serviço
DELETE /service-catalog/{id}    # Remover serviço
GET    /service-catalog/categories # Categorias de serviço

# Garantias
GET    /work-orders/{id}/warranties # Garantias da ordem
POST   /work-orders/{id}/warranties # Criar garantia
GET    /warranties/{id}             # Buscar garantia
PUT    /warranties/{id}             # Atualizar garantia
DELETE /warranties/{id}             # Remover garantia
GET    /warranties/expiring         # Garantias vencendo

```

### 📦 Stock Service (Port 8004)

**Responsabilidades**: Produtos, estoque, compras, categorias

```
# Produtos
GET    /products                # Listar produtos
POST   /products                # Criar produto
GET    /products/{id}           # Buscar produto
PUT    /products/{id}           # Atualizar produto
DELETE /products/{id}           # Remover produto
GET    /products/search         # Buscar produtos
GET    /products/barcode/{code} # Buscar por código de barras

# Categorias de Produtos
GET    /categories              # Listar categorias
POST   /categories              # Criar categoria
GET    /categories/{id}         # Buscar categoria
PUT    /categories/{id}         # Atualizar categoria
DELETE /categories/{id}         # Remover categoria
GET    /categories/tree         # Árvore de categorias

# Movimentações de Estoque
GET    /stock/movements         # Listar movimentações
POST   /stock/movements         # Registrar movimento
GET    /stock/movements/{id}    # Buscar movimento
GET    /stock/movements/product/{id} # Movimentos do produto
GET    /stock/current           # Estoque atual
GET    /stock/alerts            # Produtos em falta
PUT    /stock/adjust            # Ajuste de estoque

# Pedidos de Compra
GET    /purchase-orders         # Listar pedidos
POST   /purchase-orders         # Criar pedido
GET    /purchase-orders/{id}    # Buscar pedido
PUT    /purchase-orders/{id}    # Atualizar pedido
DELETE /purchase-orders/{id}    # Cancelar pedido
PUT    /purchase-orders/{id}/status # Atualizar status
POST   /purchase-orders/{id}/receive # Receber mercadoria

# Itens do Pedido de Compra
GET    /purchase-orders/{id}/items    # Listar itens
POST   /purchase-orders/{id}/items    # Adicionar item
PUT    /purchase-orders/{id}/items/{itemId} # Atualizar item
DELETE /purchase-orders/{id}/items/{itemId} # Remover item

# Fornecedores de Produtos (vínculo)
GET    /supplier-products       # Listar vínculos
POST   /supplier-products       # Criar vínculo
PUT    /supplier-products/{id}  # Atualizar vínculo
DELETE /supplier-products/{id}  # Remover vínculo
GET    /supplier-products/best-prices # Melhores preços

# Relatórios de Estoque
GET    /stock/reports/turnover  # Relatório de giro
GET    /stock/reports/valuation # Avaliação de estoque
GET    /stock/reports/movements # Relatório movimentações
GET    /stock/reports/alerts    # Relatório de alertas

```

### 💰 Payment Service (Port 8005)

**Responsabilidades**: Pagamentos, contas a receber/pagar, comissões, notas fiscais

```
# Processamento de Pagamentos
POST   /payments/process        # Processar pagamento
GET    /payments/{id}           # Buscar pagamento
PUT    /payments/{id}/status    # Atualizar status
GET    /payments/methods        # Métodos disponíveis
POST   /payments/refund         # Processar estorno

# PIX
POST   /payments/pix/generate   # Gerar cobrança PIX
GET    /payments/pix/{id}/qrcode # QR Code PIX
POST   /payments/pix/webhook    # Webhook PIX
GET    /payments/pix/status/{id} # Status pagamento PIX

# Cartão de Crédito
POST   /payments/card/process   # Processar cartão
POST   /payments/card/installments # Parcelamento
GET    /payments/card/fees      # Taxas do cartão

# Contas a Receber
GET    /accounts-receivable     # Listar contas
POST   /accounts-receivable     # Criar conta
GET    /accounts-receivable/{id} # Buscar conta
PUT    /accounts-receivable/{id} # Atualizar conta
DELETE /accounts-receivable/{id} # Cancelar conta
POST   /accounts-receivable/{id}/receive # Receber conta
GET    /accounts-receivable/overdue # Contas vencidas
GET    /accounts-receivable/dashboard # Dashboard financeiro

# Contas a Pagar
GET    /accounts-payable        # Listar contas
POST   /accounts-payable        # Criar conta
GET    /accounts-payable/{id}   # Buscar conta
PUT    /accounts-payable/{id}   # Atualizar conta
DELETE /accounts-payable/{id}   # Cancelar conta
POST   /accounts-payable/{id}/pay # Pagar conta
GET    /accounts-payable/overdue # Contas vencidas

# Comissões
GET    /commissions             # Listar comissões
POST   /commissions             # Calcular comissão
GET    /commissions/{id}        # Buscar comissão
PUT    /commissions/{id}        # Atualizar comissão
POST   /commissions/{id}/pay    # Pagar comissão
GET    /commissions/employee/{id} # Comissões do funcionário
GET    /commissions/reports     # Relatórios comissões

# Notas Fiscais
GET    /invoices                # Listar notas fiscais
POST   /invoices                # Emitir nota fiscal
GET    /invoices/{id}           # Buscar nota fiscal
PUT    /invoices/{id}/status    # Atualizar status
POST   /invoices/{id}/cancel    # Cancelar nota fiscal
GET    /invoices/{id}/xml       # Download XML
GET    /invoices/{id}/pdf       # Download PDF
POST   /invoices/batch          # Emissão em lote

# Itens da Nota Fiscal
GET    /invoices/{id}/items     # Listar itens
POST   /invoices/{id}/items     # Adicionar item
PUT    /invoices/{id}/items/{itemId} # Atualizar item
DELETE /invoices/{id}/items/{itemId} # Remover item

```

### 📧 Notification Service (Port 8006)

**Responsabilidades**: Notificações, comunicações, funcionários

```
# Envio de Notificações
POST   /notifications/email     # Enviar email
POST   /notifications/sms       # Enviar SMS
POST   /notifications/whatsapp  # Enviar WhatsApp
POST   /notifications/push      # Enviar push notification
POST   /notifications/schedule  # Agendar notificação
DELETE /notifications/{id}      # Cancelar notificação

# Templates de Notificação
GET    /notifications/templates # Templates disponíveis
POST   /notifications/templates # Criar template
GET    /notifications/templates/{id} # Buscar template
PUT    /notifications/templates/{id} # Atualizar template
DELETE /notifications/templates/{id} # Remover template

# Histórico de Notificações
GET    /notifications/history   # Histórico de envios
GET    /notifications/{id}      # Detalhes da notificação
GET    /notifications/stats     # Estatísticas de envio
GET    /notifications/failed    # Notificações falhadas
POST   /notifications/{id}/retry # Reenviar notificação

# Gestão de Funcionários
GET    /employees               # Listar funcionários
POST   /employees               # Criar funcionário
GET    /employees/{id}          # Buscar funcionário
PUT    /employees/{id}          # Atualizar funcionário
DELETE /employees/{id}          # Remover funcionário
PUT    /employees/{id}/status   # Ativar/desativar
GET    /employees/{id}/work-orders # Ordens do funcionário
GET    /employees/{id}/schedule # Agenda do funcionário

# Configurações de Comunicação
GET    /notifications/settings  # Configurações gerais
PUT    /notifications/settings  # Atualizar configurações
POST   /notifications/test      # Testar configurações
GET    /notifications/webhooks  # Webhooks configurados
POST   /notifications/webhooks  # Criar webhook

```

### 📊 Report Service (Port 8007)

**Responsabilidades**: Relatórios, dashboards, analytics, auditoria

```
# Geração de Relatórios
POST   /reports/generate        # Gerar relatório
GET    /reports/{id}            # Buscar relatório
DELETE /reports/{id}           # Remover relatório
GET    /reports/templates       # Templates disponíveis
POST   /reports/templates       # Criar template
PUT    /reports/templates/{id}  # Atualizar template

# Agendamento de Relatórios
POST   /reports/schedule        # Agendar relatório
GET    /reports/scheduled       # Relatórios agendados
PUT    /reports/scheduled/{id}  # Atualizar agendamento
DELETE /reports/scheduled/{id}  # Cancelar agendamento

# Dashboards
GET    /reports/dashboard/main          # Dashboard principal
GET    /reports/dashboard/financial     # Dashboard financeiro
GET    /reports/dashboard/operational   # Dashboard operacional
GET    /reports/dashboard/customers     # Dashboard clientes
GET    /reports/dashboard/inventory     # Dashboard estoque
GET    /reports/dashboard/employees     # Dashboard funcionários

# Exportação de Dados
POST   /reports/export/pdf      # Exportar PDF
POST   /reports/export/excel    # Exportar Excel
POST   /reports/export/csv      # Exportar CSV
GET    /reports/exports/{id}    # Download arquivo

# Analytics Avançado
GET    /analytics/trends        # Análise de tendências
GET    /analytics/forecasting   # Previsões
GET    /analytics/performance   # Análise performance
GET    /analytics/customer-behavior # Comportamento cliente
GET    /analytics/profitability # Análise lucratividade

# Auditoria e Logs
GET    /audit/logs              # Logs de auditoria
GET    /audit/user-actions      # Ações dos usuários
GET    /audit/data-changes      # Mudanças nos dados
GET    /audit/login-history     # Histórico de logins
GET    /audit/export            # Exportar logs

# Relatórios Customizados
POST   /reports/custom/query    # Query customizada
GET    /reports/custom/builder  # Construtor de relatórios
POST   /reports/custom/save     # Salvar relatório customizado
GET    /reports/custom/list     # Listar relatórios customizados

# Relatórios Financeiros
GET    /payments/reports/revenue # Relatório de receita
GET    /payments/reports/expenses # Relatório de despesas
GET    /payments/reports/cash-flow # Fluxo de caixa
GET    /payments/reports/profit # Relatório de lucro
GET    /payments/reports/taxes  # Relatório de impostos

```

## 🎯 Distribuição por Domínio de Negócio

### Resumo da Arquitetura

```mermaid
graph TB
    subgraph "Auth Domain"
        A1[Users & Permissions]
        A2[Company Settings]
        A3[Authentication]
    end

    subgraph "Customer Domain"
        B1[Clients & Vehicles]
        B2[Suppliers]
        B3[External Integrations]
        B4[Maintenance History]
    end

    subgraph "Operations Domain"
        C1[Work Orders]
        C2[Appointments]
        C3[Services Catalog]
        C4[Warranties]
    end

    subgraph "Inventory Domain"
        D1[Products & Categories]
        D2[Stock Movements]
        D3[Purchase Orders]
        D4[Supplier Relations]
    end

    subgraph "Financial Domain"
        E1[Payments Processing]
        E2[Accounts Receivable/Payable]
        E3[Invoices & NFe]
        E4[Commissions]
    end

    subgraph "Communication Domain"
        F1[Notifications]
        F2[Employee Management]
        F3[Communication Settings]
    end

    subgraph "Analytics Domain"
        G1[Reports Generation]
        G2[Dashboards]
        G3[Analytics]
        G4[Audit Logs]
    end

```

## 📋 Total de Endpoints por Microserviço

| Microserviço | Quantidade de Endpoints | Principais Responsabilidades |
| --- | --- | --- |
| **Auth Service** | 24 endpoints | Autenticação, usuários, configurações empresa |
| **Customer Service** | 25 endpoints | Clientes, veículos, fornecedores, integrações |
| **OS Service** | 32 endpoints | Ordens de serviço, agendamentos, garantias |
| **Stock Service** | 28 endpoints | Produtos, estoque, compras, categorias |
| **Payment Service** | 35 endpoints | Pagamentos, contas, comissões, notas fiscais |
| **Notification Service** | 22 endpoints | Notificações, funcionários, comunicações |
| **Report Service** | 30 endpoints | Relatórios, dashboards, analytics, auditoria |

**Total: 196 endpoints** cobrindo todas as tabelas e funcionalidades do sistema.

## 🔄 Padrões de Comunicação Inter-Serviços

### Dependências Principais:

- **OS Service** → Customer Service (clientes/veículos)
- **OS Service** → Stock Service (produtos/estoque)
- **Payment Service** → OS Service (ordens para pagamento)
- **Notification Service** → Todos (notificações de eventos)
- **Report Service** → Todos (dados para relatórios)

### Diagrama de Integrações Externas

```mermaid
graph TB
    subgraph "NeriTech Auto Core"
        A[Customer Service]
        B[Payment Service]
        C[Notification Service]
        D[Report Service]
    end

    subgraph "External APIs"
        E[DENATRAN API<br/>🚗 Consulta Veículos]
        F[Receita Federal<br/>📋 Validação CNPJ]
        G[ViaCEP API<br/>📍 Endereços]

        H[PIX Gateway<br/>💳 Banco Central]
        I[Credit Card Gateway<br/>💳 Cielo/Stone]
        J[Banking API<br/>🏦 Conciliação]

        K[SMTP Service<br/>📧 SendGrid/AWS SES]
        L[WhatsApp Business<br/>📱 Meta API]
        M[SMS Gateway<br/>📱 Twilio]

        N[NFe Service<br/>📜 SEFAZ]
        O[Backup Service<br/>☁️ AWS S3]
    end

    A --> E
    A --> F
    A --> G

    B --> H
    B --> I
    B --> J

    C --> K
    C --> L
    C --> M

    D --> N
    D --> O

    style E fill:#e1f5fe
    style F fill:#e1f5fe
    style G fill:#e1f5fe
    style H fill:#f3e5f5
    style I fill:#f3e5f5
    style J fill:#f3e5f5
    style K fill:#e8f5e8
    style L fill:#e8f5e8
    style M fill:#e8f5e8

```

### Padrão de Comunicação API

```mermaid
sequenceDiagram
    participant Client as 📱 Frontend
    participant Gateway as 🌐 Gateway
    participant Service as 🔧 Service
    participant External as 🌍 External API
    participant Cache as ⚡ Redis

    Client->>Gateway: Request + JWT
    Gateway->>Gateway: Validate & Add Tenant Context
    Gateway->>Service: Forward Request

    alt Data in Cache
        Service->>Cache: Check Cache
        Cache-->>Service: Return Cached Data
    else Data Not Cached
        Service->>External: API Call
        External-->>Service: Response
        Service->>Cache: Store in Cache
    end

    Service-->>Gateway: Response
    Gateway-->>Client: JSON Response

```

### Formato de Requisição Padrão

```json
{
  "headers": {
    "Authorization": "Bearer eyJhbGciOiJIUzI1NiIs...",
    "Content-Type": "application/json",
    "X-Tenant-ID": "oficina-a",
    "X-Request-ID": "uuid-v4"
  },
  "body": {
    "data": { /* payload específico */ },
    "metadata": {
      "timestamp": "2024-01-15T10:30:00Z",
      "version": "1.0",
      "source": "web-app"
    }
  }
}

```

### Tratamento de Erros Padronizado

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Dados inválidos fornecidos",
    "details": [
      {
        "field": "customer.document",
        "error": "CPF inválido",
        "value": "123.456.789-00"
      }
    ],
    "timestamp": "2024-01-15T10:30:00Z",
    "request_id": "uuid-v4"
  }
}

```

---

## D. 🛡️ Protocolos de Segurança

### Fluxo de Autenticação JWT

```mermaid
sequenceDiagram
    participant User as 👤 Usuário
    participant Frontend as 📱 Angular App
    participant Gateway as 🌐 API Gateway
    participant Auth as 🔑 Auth Service
    participant DB as 🗄️ Database
    participant Business as 🔧 Business Service

    User->>Frontend: 1. Login (email/password)
    Frontend->>Gateway: 2. POST /auth/login
    Gateway->>Auth: 3. Forward credentials
    Auth->>DB: 4. Validate user & tenant
    DB-->>Auth: 5. User data + permissions

    Auth->>Auth: 6. Generate JWT token
    Note over Auth: JWT contains:<br/>- user_id<br/>- tenant_id<br/>- roles<br/>- permissions<br/>- exp (2h)

    Auth-->>Gateway: 7. Return JWT + refresh token
    Gateway-->>Frontend: 8. Authentication response
    Frontend->>Frontend: 9. Store tokens securely

    loop Authenticated Requests
        Frontend->>Gateway: 10. API Request + JWT
        Gateway->>Gateway: 11. Validate JWT signature
        Gateway->>Gateway: 12. Extract tenant_id
        Gateway->>Business: 13. Forward + tenant context
        Business->>DB: 14. Query with tenant filter
        DB-->>Business: 15. Tenant-scoped data
        Business-->>Gateway: 16. Response
        Gateway-->>Frontend: 17. JSON Response
    end

    Frontend->>Gateway: 18. Refresh token when needed
    Gateway->>Auth: 19. Validate refresh token
    Auth-->>Gateway: 20. New JWT token

```

### Camadas de Segurança (Defense in Depth)

```mermaid
graph TB
    subgraph "Layer 1: Network Security"
        A[Cloudflare DDoS Protection<br/>🛡️ WAF Rules<br/>🌍 Global CDN]
        B[TLS 1.3 Encryption<br/>🔐 SSL Certificates<br/>🔒 HSTS Headers]
    end

    subgraph "Layer 2: API Gateway Security"
        C[Rate Limiting<br/>⏱️ 100 req/min/user<br/>🚦 Throttling]
        D[JWT Validation<br/>🔑 Signature Check<br/>⏰ Expiration Control]
        E[CORS Policy<br/>🌐 Origin Validation<br/>🔗 Allowed Methods]
    end

    subgraph "Layer 3: Application Security"
        F[Input Validation<br/>✅ Schema Validation<br/>🚫 SQL Injection Prevention]
        G[Authorization<br/>👮 Role-based Access<br/>🎯 Permission Granular]
        H[Tenant Isolation<br/>🏢 Row Level Security<br/>🔍 Data Filtering]
    end

    subgraph "Layer 4: Data Security"
        I[Database Encryption<br/>🗄️ At Rest Encryption<br/>📡 In Transit Protection]
        J[Backup Encryption<br/>💾 Encrypted Backups<br/>🔐 Key Management]
        K[Audit Logging<br/>📝 All Actions Logged<br/>🕒 Tamper-proof]
    end

    A --> C
    B --> D
    C --> F
    D --> G
    E --> H
    F --> I
    G --> J
    H --> K

```

### Comunicação Segura entre Microserviços (mTLS)

```mermaid
graph LR
    subgraph "Service A"
        A1[Business Logic]
        A2[Client Certificate]
        A3[Private Key]
    end

    subgraph "mTLS Channel"
        M1[Certificate Exchange]
        M2[Mutual Authentication]
        M3[Encrypted Communication]
    end

    subgraph "Service B"
        B1[Server Certificate]
        B2[Certificate Validation]
        B3[Business Logic]
    end

    A1 --> A2
    A2 --> M1
    M1 --> B1
    B1 --> B2
    B2 --> B3

    A3 --> M2
    M2 --> M3
    M3 --> B3

```

### Controle de Acesso Baseado em Funções (RBAC)

```mermaid
graph TB
    subgraph "Roles & Permissions Matrix"
        A[👑 Admin<br/>Full Access]
        B[👔 Manager<br/>Operational Control]
        C[👩‍💼 Receptionist<br/>Customer Service]
        D[🔧 Mechanic<br/>Technical Work]
        E[💰 Financial<br/>Money Control]
    end

    subgraph "Permissions"
        P1[Create Users ✅❌❌❌❌]
        P2[Manage Settings ✅✅❌❌❌]
        P3[View All Reports ✅✅❌❌✅]
        P4[Create Work Orders ✅✅✅❌❌]
        P5[Update Order Status ✅✅❌✅❌]
        P6[Process Payments ✅✅❌❌✅]
        P7[View Customer Data ✅✅✅✅❌]
        P8[Manage Stock ✅✅❌❌❌]
    end

    A --> P1
    A --> P2
    A --> P3
    A --> P4
    A --> P5
    A --> P6
    A --> P7
    A --> P8

```

### Monitoramento e Auditoria de Segurança

```mermaid
graph TB
    subgraph "Security Events"
        A[Failed Login Attempts<br/>🚫 Brute Force Detection]
        B[Privilege Escalation<br/>⬆️ Role Changes]
        C[Data Access Anomalies<br/>🔍 Unusual Patterns]
        D[API Abuse<br/>📊 Rate Limit Violations]
    end

    subgraph "Monitoring System"
        E[Real-time Alerts<br/>🚨 Immediate Response]
        F[Log Analysis<br/>📊 Pattern Recognition]
        G[Compliance Reports<br/>📋 Regulatory Requirements]
        H[Incident Response<br/>🆘 Automated Actions]
    end

    subgraph "Response Actions"
        I[Account Lockout<br/>🔒 Temporary Suspension]
        J[IP Blocking<br/>🚫 Network Restriction]
        K[Alert Security Team<br/>👮‍♂️ Human Intervention]
        L[Evidence Collection<br/>📋 Forensic Analysis]
    end

    A --> E
    B --> E
    C --> F
    D --> F

    E --> I
    E --> J
    F --> K
    F --> L

    G --> L
    H --> I

```

### Configurações de Segurança por Ambiente

| Aspecto | Desenvolvimento | Homologação | Produção |
| --- | --- | --- | --- |
| **TLS** | Auto-assinado | Let's Encrypt | Certificado Válido |
| **JWT Expiry** | 24 horas | 4 horas | 2 horas |
| **Rate Limiting** | 1000/min | 500/min | 100/min |
| **Logs Level** | DEBUG | INFO | WARN |
| **Database** | Local | Staged | Encrypted |
| **Backups** | Semanal | Diário | 6x/dia |
| **Monitoring** | Básico | Completo | 24/7 |
| **2FA** | Opcional | Recomendado | Obrigatório |

### Compliance e Regulamentações

```mermaid
mindmap
  root((Compliance))
    LGPD
      Consentimento Explícito
      Direito ao Esquecimento
      Portabilidade de Dados
      Minimização de Dados
    OWASP Top 10
      Injection Prevention
      Broken Authentication
      Sensitive Data Exposure
      Security Misconfiguration
    ISO 27001
      Risk Assessment
      Incident Management
      Access Control
      Business Continuity
    PCI DSS
      Card Data Protection
      Secure Network
      Vulnerability Management
      Regular Monitoring

```

---

## 📊 Métricas de Segurança e Performance

### Indicadores de Segurança Monitorados

| Métrica | Objetivo | Frequência | Alerta |
| --- | --- | --- | --- |
| **Tentativas de Login Falhadas** | < 5% | Tempo Real | > 10/min |
| **Tokens Expirados** | < 1% | Hora | > 5% |
| **API Response Time** | < 500ms | Minuto | > 2s |
| **Disponibilidade** | > 99.5% | Minuto | < 99% |
| **Vulnerabilidades** | 0 Critical | Diário | > 0 |

### Dashboard de Segurança

```mermaid
graph TB
    subgraph "Security Dashboard"
        A[🔐 Authentication Status<br/>✅ Active Sessions: 1,247<br/>🚫 Failed Logins: 12<br/>⚠️ Suspicious IPs: 3]

        B[🛡️ API Security<br/>📊 Rate Limit Usage: 67%<br/>🔒 SSL Score: A+<br/>🌐 CORS Violations: 0]

        C[🗄️ Data Protection<br/>🔐 Encryption: Active<br/>💾 Backup Status: ✅<br/>📋 Compliance: 98%]

        D[🚨 Incidents<br/>⚡ Real-time Alerts: 2<br/>📊 Resolved Today: 8<br/>⏱️ MTTR: 15 min]
    end

```

---

[3. DOCUMENTAÇÃO DE DESENVOLVIMENTO](https://www.notion.so/3-DOCUMENTA-O-DE-DESENVOLVIMENTO-24e272791906809598abc0fa17c8f8df?pvs=21)