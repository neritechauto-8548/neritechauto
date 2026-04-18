-- Tabela de Usuários
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    nome_completo VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    bloqueado BOOLEAN DEFAULT FALSE,
    data_bloqueio TIMESTAMP,
    motivo_bloqueio VARCHAR(255),
    deve_trocar_senha BOOLEAN DEFAULT FALSE,
    ultimo_acesso TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    CONSTRAINT fk_usuarios_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT uk_usuarios_email_empresa UNIQUE (email, empresa_id)
);

CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_empresa ON usuarios(empresa_id);

-- Tabela de Perfis de Usuário (dados adicionais)
CREATE TABLE perfis_usuario (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    avatar_url VARCHAR(500),
    telefone VARCHAR(20),
    cargo VARCHAR(100),
    departamento VARCHAR(100),
    preferencias JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    CONSTRAINT fk_perfis_usuario_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_perfis_usuario_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT uk_perfis_usuario_usuario UNIQUE (usuario_id)
);

-- Tabela de Funções (Roles)
CREATE TABLE funcoes (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(50) NOT NULL,
    descricao VARCHAR(255),
    sistema BOOLEAN DEFAULT FALSE, -- Se é uma role do sistema que não pode ser apagada
    ativo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    CONSTRAINT fk_funcoes_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT uk_funcoes_nome_empresa UNIQUE (nome, empresa_id)
);

-- Tabela de Permissões
CREATE TABLE permissoes (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL, -- Ex: CLIENTE.CREATE
    descricao VARCHAR(255),
    modulo VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    CONSTRAINT fk_permissoes_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT uk_permissoes_nome_empresa UNIQUE (nome, empresa_id)
);

-- Tabela de Ligação Funções <-> Permissões
CREATE TABLE funcoes_permissoes (
    funcao_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,
    empresa_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    PRIMARY KEY (funcao_id, permissao_id),
    CONSTRAINT fk_funcoes_permissoes_funcao FOREIGN KEY (funcao_id) REFERENCES funcoes(id),
    CONSTRAINT fk_funcoes_permissoes_permissao FOREIGN KEY (permissao_id) REFERENCES permissoes(id),
    CONSTRAINT fk_funcoes_permissoes_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

-- Tabela de Ligação Usuários <-> Funções
CREATE TABLE usuarios_funcoes (
    usuario_id BIGINT NOT NULL,
    funcao_id BIGINT NOT NULL,
    empresa_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    PRIMARY KEY (usuario_id, funcao_id),
    CONSTRAINT fk_usuarios_funcoes_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_usuarios_funcoes_funcao FOREIGN KEY (funcao_id) REFERENCES funcoes(id),
    CONSTRAINT fk_usuarios_funcoes_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

-- Tabela de Sessões de Usuário
CREATE TABLE sessoes_usuario (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    token_sessao VARCHAR(500) NOT NULL, -- Pode ser o JTI ou hash do token
    refresh_token VARCHAR(500),
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    dispositivo_tipo VARCHAR(50), -- Mobile, Desktop, Tablet
    sistema_operacional VARCHAR(50),
    navegador VARCHAR(50),
    data_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acesso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_expiracao TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_sessoes_usuario_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_sessoes_usuario_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE INDEX idx_sessoes_token ON sessoes_usuario(token_sessao);
CREATE INDEX idx_sessoes_refresh ON sessoes_usuario(refresh_token);
CREATE INDEX idx_sessoes_usuario ON sessoes_usuario(usuario_id);

-- Tabela de Logs de Acesso (Auditoria de Login/Logout)
CREATE TABLE logs_acesso (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    usuario_id BIGINT, -- Pode ser nulo se falhar login e não identificar user
    email_tentativa VARCHAR(255),
    tipo_evento VARCHAR(50) NOT NULL, -- LOGIN_SUCCESS, LOGIN_FAIL, LOGOUT, REFRESH_TOKEN
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    detalhes TEXT,
    data_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_logs_acesso_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_logs_acesso_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE INDEX idx_logs_acesso_data ON logs_acesso(data_evento);
CREATE INDEX idx_logs_acesso_usuario ON logs_acesso(usuario_id);

-- Tabela de Tentativas de Login (para Rate Limiting e Bloqueio)
CREATE TABLE tentativas_login (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT, -- Pode ser nulo se não identificar empresa
    email VARCHAR(255) NOT NULL,
    ip_address VARCHAR(45) NOT NULL,
    sucesso BOOLEAN DEFAULT FALSE,
    data_tentativa TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tentativas_login_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_tentativas_login_ip ON tentativas_login(ip_address);
CREATE INDEX idx_tentativas_login_email ON tentativas_login(email);
