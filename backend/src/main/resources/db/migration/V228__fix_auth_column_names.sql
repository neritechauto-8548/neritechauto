-- V228: Fix column names in funcoes and permissoes tables to match Portuguese entities
-- Also ensure ADMIN role exists for company 1 and assign it to master user

DO $$
BEGIN
    -- Fix funcoes table
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'funcoes' AND column_name = 'name') THEN
        ALTER TABLE funcoes RENAME COLUMN name TO nome;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'funcoes' AND column_name = 'description') THEN
        ALTER TABLE funcoes RENAME COLUMN description TO descricao;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'funcoes' AND column_name = 'is_system') THEN
        ALTER TABLE funcoes RENAME COLUMN is_system TO sistema;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'funcoes' AND column_name = 'is_active') THEN
        ALTER TABLE funcoes RENAME COLUMN is_active TO ativo;
    END IF;

    -- Fix permissoes table
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'permissoes' AND column_name = 'name') THEN
        ALTER TABLE permissoes RENAME COLUMN name TO nome;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'permissoes' AND column_name = 'description') THEN
        ALTER TABLE permissoes RENAME COLUMN description TO descricao;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'permissoes' AND column_name = 'module') THEN
        ALTER TABLE permissoes RENAME COLUMN module TO modulo;
    END IF;

END $$;

-- Ensure standard audit columns for agd_agendamentos just in case V226 missed it or had issues
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'agd_agendamentos' AND column_name = 'versao') THEN
        ALTER TABLE agd_agendamentos ADD COLUMN versao INTEGER DEFAULT 0 NOT NULL;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'agd_agendamentos' AND column_name = 'data_cadastro') THEN
        ALTER TABLE agd_agendamentos ADD COLUMN data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'agd_agendamentos' AND column_name = 'data_atualizacao') THEN
        ALTER TABLE agd_agendamentos ADD COLUMN data_atualizacao TIMESTAMP;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'agd_agendamentos' AND column_name = 'criado_por') THEN
        ALTER TABLE agd_agendamentos ADD COLUMN criado_por BIGINT;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'agd_agendamentos' AND column_name = 'atualizado_por') THEN
        ALTER TABLE agd_agendamentos ADD COLUMN atualizado_por BIGINT;
    END IF;
END $$;

-- Data Correction: Ensure ADMIN role and master user link for company 1
DO $$
DECLARE
    v_admin_role_id BIGINT;
    v_master_user_id BIGINT;
BEGIN
    -- 1. Ensure ADMIN role exists for empresa 1
    SELECT id INTO v_admin_role_id FROM funcoes WHERE empresa_id = 1 AND nome = 'ADMIN' LIMIT 1;
    
    IF v_admin_role_id IS NULL THEN
        INSERT INTO funcoes (empresa_id, nome, descricao, sistema, ativo, data_cadastro, data_atualizacao, versao)
        VALUES (1, 'ADMIN', 'Administrador do Sistema', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
        RETURNING id INTO v_admin_role_id;
    END IF;

    -- 2. Find Master User
    SELECT id INTO v_master_user_id FROM usuarios WHERE email = 'master@empresa.com' AND empresa_id = 1 LIMIT 1;

    -- 3. Link them if both exist
    IF v_master_user_id IS NOT NULL AND v_admin_role_id IS NOT NULL THEN
        IF NOT EXISTS (SELECT 1 FROM usuarios_funcoes WHERE usuario_id = v_master_user_id AND funcao_id = v_admin_role_id) THEN
            INSERT INTO usuarios_funcoes (usuario_id, funcao_id, empresa_id)
            VALUES (v_master_user_id, v_admin_role_id, 1);
        END IF;
    END IF;
END $$;
