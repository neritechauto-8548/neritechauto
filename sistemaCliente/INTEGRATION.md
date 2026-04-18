# Guia de Integração Backend

Este documento descreve como integrar o frontend Vue 3 com o backend Spring Boot do NeriTech.

## 📋 Estrutura de Dados Esperada

### 1. Consulta Pública de OS

**Endpoint:** `GET /public/ordem-servico/consulta`

**Query Parameters:**
- `cpf` (string): CPF do cliente (apenas números)
- `numeroOs` (string): Número da ordem de serviço

**Response (200 OK):**
```json
{
  "id": 1,
  "numeroOs": "12345",
  "status": "EM_EXECUCAO",
  "veiculo": "Fiat Uno 1.0 2020",
  "placa": "ABC-1234",
  "dataEntrada": "2025-11-25T08:00:00",
  "previsaoEntrega": "2025-12-02T18:00:00",
  "valorTotal": 800.00
}
```

**Response (404 Not Found):**
```json
{
  "message": "Ordem de serviço não encontrada"
}
```

---

### 2. Login do Cliente

**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "email": "cliente@email.com",
  "senha": "senha123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@email.com",
    "cpf": "123.456.789-00"
  }
}
```

**Response (401 Unauthorized):**
```json
{
  "message": "Credenciais inválidas"
}
```

---

### 3. Histórico de Ordens

**Endpoint:** `GET /cliente/ordens`

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "numeroOs": "12345",
    "status": "EM_EXECUCAO",
    "veiculo": "Fiat Uno 1.0 2020",
    "placa": "ABC-1234",
    "dataEntrada": "2025-11-25T08:00:00",
    "previsaoEntrega": "2025-12-02T18:00:00",
    "valorTotal": 800.00
  },
  {
    "id": 2,
    "numeroOs": "12344",
    "status": "ENTREGUE",
    "veiculo": "Fiat Uno 1.0 2020",
    "placa": "ABC-1234",
    "dataEntrada": "2025-10-15T08:00:00",
    "previsaoEntrega": "2025-10-20T18:00:00",
    "valorTotal": 450.00
  }
]
```

---

### 4. Detalhes da OS

**Endpoint:** `GET /cliente/ordem-servico/{id}`

**Headers:**
```
Authorization: Bearer {token}
```

**Path Parameters:**
- `id` (number): ID da ordem de serviço

**Response (200 OK):**
```json
{
  "id": 1,
  "numeroOs": "12345",
  "status": "EM_EXECUCAO",
  "veiculo": "Fiat Uno 1.0 2020",
  "placa": "ABC-1234",
  "km": 45000,
  "mecanico": "João Silva",
  "dataEntrada": "2025-11-25T08:00:00",
  "previsaoEntrega": "2025-12-02T18:00:00",
  "valorMaoObra": 350.00,
  "valorPecas": 450.00,
  "subtotal": 800.00,
  "valorTotal": 800.00,
  "observacoes": "Cliente relatou barulho estranho no motor.",
  "servicos": [
    {
      "descricao": "Troca de óleo e filtro",
      "tempo": 1.5,
      "valor": 150.00
    }
  ],
  "pecas": [
    {
      "nome": "Óleo sintético 5W30",
      "quantidade": 4,
      "valorUnitario": 45.00,
      "valorTotal": 180.00
    }
  ],
  "fotos": [
    "https://api.neritech.com/uploads/os/1/foto1.jpg",
    "https://api.neritech.com/uploads/os/1/foto2.jpg"
  ],
  "historicoStatus": [
    {
      "status": "AGUARDANDO_APROVACAO",
      "data": "2025-11-25T08:00:00"
    },
    {
      "status": "EM_EXECUCAO",
      "data": "2025-11-26T09:30:00"
    }
  ],
  "historicoAlteracoes": [
    {
      "descricao": "Ordem de serviço criada",
      "data": "2025-11-25T08:00:00"
    },
    {
      "descricao": "Cliente aprovou orçamento",
      "data": "2025-11-26T09:00:00"
    }
  ]
}
```

---

## 🔐 Autenticação

O frontend utiliza **JWT (JSON Web Token)** para autenticação:

1. **Login**: Cliente faz POST em `/auth/login` com email e senha
2. **Token**: Backend retorna token JWT
3. **Armazenamento**: Token é salvo em `localStorage`
4. **Requisições**: Token é enviado no header `Authorization: Bearer {token}`
5. **Expiração**: Se token expirar (401), usuário é redirecionado para login

### Configuração CORS no Backend

Adicione as seguintes configurações no Spring Boot:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // URL do frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

---

## 📊 Enums de Status

O frontend espera os seguintes valores de status:

```java
public enum StatusOrdemServico {
    AGUARDANDO_APROVACAO,
    EM_EXECUCAO,
    AGUARDANDO_PECAS,
    FINALIZADO,
    ENTREGUE,
    CANCELADO
}
```

---

## 🎨 Formato de Datas

- **Backend**: Enviar datas no formato ISO 8601: `2025-11-25T08:00:00`
- **Frontend**: Converte automaticamente para formato brasileiro: `25/11/2025`

---

## 📸 Upload de Fotos

As URLs das fotos devem ser:
- URLs completas (ex: `https://api.neritech.com/uploads/...`)
- Acessíveis publicamente ou com autenticação
- Formatos suportados: JPG, PNG, WebP

---

## ⚠️ Tratamento de Erros

O frontend espera respostas de erro no formato:

```json
{
  "message": "Mensagem de erro amigável",
  "status": 404,
  "timestamp": "2025-11-25T08:00:00"
}
```

### Códigos HTTP Esperados:
- `200` - Sucesso
- `401` - Não autenticado / Token inválido
- `403` - Sem permissão
- `404` - Recurso não encontrado
- `500` - Erro interno do servidor

---

## 🔧 Configuração da URL da API

No arquivo `.env`:

```env
VITE_API_URL=http://localhost:8080/api
```

Para produção:

```env
VITE_API_URL=https://api.neritech.com/api
```

---

## 🧪 Testando a Integração

1. **Inicie o backend** na porta 8080
2. **Configure o .env** com a URL correta
3. **Inicie o frontend**: `npm run dev`
4. **Teste o fluxo**:
   - Consulta rápida em `/consulta`
   - Login em `/login`
   - Histórico em `/historico`
   - Detalhes em `/os/{id}`

---

## 📝 Checklist de Integração

- [ ] Endpoints da API implementados
- [ ] CORS configurado no backend
- [ ] JWT configurado e funcionando
- [ ] Enums de status corretos
- [ ] Formato de datas ISO 8601
- [ ] Upload de fotos funcionando
- [ ] Tratamento de erros padronizado
- [ ] Testes de integração realizados

---

## 🆘 Troubleshooting

### Erro de CORS
- Verifique configuração CORS no backend
- Certifique-se que a URL do frontend está permitida

### Token não funciona
- Verifique se o token está sendo enviado no header
- Confirme que o token não expirou
- Valide a assinatura JWT no backend

### Dados não aparecem
- Verifique console do navegador para erros
- Confirme que a estrutura JSON está correta
- Teste endpoints com Postman/Insomnia

---

Para mais informações, consulte o README.md principal do projeto.
