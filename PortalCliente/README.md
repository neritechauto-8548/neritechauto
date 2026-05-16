# NeriTech - Sistema de Consulta de Ordem de Serviço

Sistema moderno de consulta de ordens de serviço para clientes, desenvolvido com Vue 3, inspirado no portal da Ultracar.

## 🚀 Funcionalidades

- ✅ **Consulta Rápida**: Consulta pública de OS sem necessidade de login (CPF + Número da OS)
- ✅ **Portal do Cliente**: Autenticação segura para acesso ao histórico completo
- ✅ **Histórico de Ordens**: Visualização de todas as ordens de serviço do cliente
- ✅ **Detalhamento Completo**: Informações detalhadas incluindo:
  - Status em tempo real com timeline visual
  - Serviços executados
  - Peças aplicadas
  - Galeria de fotos com lightbox
  - Histórico de alterações
  - Breakdown de valores

## 🎨 Design

- Interface moderna estilo SaaS
- Cores principais: Azul (#005CFF), Branco, Cinza neutro
- Totalmente responsivo (Mobile, Tablet, Desktop)
- Animações suaves e micro-interações
- Ícones Lucide modernos
- Tipografia Inter (Google Fonts)

## 🛠️ Tecnologias

- **Vue 3** (Composition API)
- **Vue Router 4** (SPA routing)
- **Axios** (HTTP requests)
- **Vite** (Build tool)
- **CSS Vanilla** (Design system customizado)

## 📦 Instalação

1. Clone o repositório:
```bash
cd sistemaCliente
```

2. Instale as dependências:
```bash
npm install
```

3. Configure as variáveis de ambiente:
```bash
# Copie o arquivo de exemplo
cp .env.example .env

# Edite o arquivo .env e configure a URL da API
VITE_API_URL=http://localhost:8080/api
```

## 🚀 Executar

### Desenvolvimento
```bash
npm run dev
```
Acesse: http://localhost:3000

### Build para Produção
```bash
npm run build
```

### Preview da Build
```bash
npm run preview
```

## 📁 Estrutura do Projeto

```
src/
├── api/
│   └── osService.js          # Serviço de API com Axios
├── router/
│   └── index.js              # Configuração de rotas
├── views/
│   ├── ConsultaRapida.vue    # Consulta pública
│   ├── Login.vue             # Portal do cliente
│   ├── Historico.vue         # Lista de ordens
│   └── DetalheOS.vue         # Detalhes da ordem
├── components/
│   ├── OsCard.vue            # Card de ordem
│   ├── OsStatusTimeline.vue  # Timeline de status
│   ├── OsServicoItem.vue     # Item de serviço
│   ├── OsPecaItem.vue        # Item de peça
│   └── OsFotoViewer.vue      # Galeria de fotos
├── App.vue                   # Componente raiz
├── main.js                   # Entry point
└── style.css                 # Estilos globais
```

## 🔌 Endpoints da API

O sistema espera os seguintes endpoints no backend:

### Consulta Pública
```
GET /public/ordem-servico/consulta?cpf={cpf}&numeroOs={numeroOs}
```

### Autenticação
```
POST /auth/login
Body: { email, senha }
Response: { token, user }
```

### Histórico do Cliente
```
GET /cliente/ordens
Headers: Authorization: Bearer {token}
```

### Detalhes da OS
```
GET /cliente/ordem-servico/{id}
Headers: Authorization: Bearer {token}
```

## 🔐 Autenticação

O sistema utiliza JWT (JSON Web Token) para autenticação:
- Token armazenado em `localStorage`
- Interceptor Axios adiciona token automaticamente
- Redirecionamento automático para login em caso de token inválido

## 📱 Responsividade

- **Mobile**: < 768px
- **Tablet**: 768px - 1024px
- **Desktop**: > 1024px

## 🎯 Rotas

- `/` - Redireciona para `/consulta`
- `/consulta` - Consulta rápida (pública)
- `/login` - Portal do cliente
- `/historico` - Histórico de ordens (requer autenticação)
- `/os/:id` - Detalhes da ordem (requer autenticação)

## 🌟 Destaques UX/UI

- **Loading States**: Feedback visual durante requisições
- **Error Handling**: Mensagens de erro amigáveis
- **Validação de Formulários**: Validação em tempo real
- **CPF Masking**: Máscara automática no campo CPF
- **Timeline Visual**: Acompanhamento visual do status
- **Lightbox de Fotos**: Galeria profissional com navegação
- **Print-Friendly**: Tela de detalhes otimizada para impressão

## 📝 Licença

Copyright © 2025 NeriTech. Todos os direitos reservados.
