-- Ensure pgcrypto is available for bcrypt hashing
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Create or update master user for empresa 1 with bcrypt('master')
DO $$
DECLARE
    v_usuario_id BIGINT;
    v_funcao_admin_id BIGINT;
BEGIN
    -- Upsert master user ensuring audit columns are satisfied across schemas
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'usuarios' AND column_name = 'data_cadastro'
    ) THEN
        -- Schema with Portuguese audit columns
        INSERT INTO usuarios (empresa_id, nome_completo, email, senha, ativo, bloqueado, deve_trocar_senha, data_cadastro, data_atualizacao)
        VALUES (1, 'Master Admin', 'master@empresa.com', crypt('master', gen_salt('bf', 12)), TRUE, FALSE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
        ON CONFLICT (email, empresa_id) DO UPDATE
            SET senha = crypt('master', gen_salt('bf', 12)),
                ativo = TRUE,
                bloqueado = FALSE,
                data_atualizacao = CURRENT_TIMESTAMP
        RETURNING id INTO v_usuario_id;
    ELSE
        -- Original schema with created_at/updated_at
        INSERT INTO usuarios (empresa_id, nome_completo, email, senha, ativo, bloqueado, deve_trocar_senha, created_at, updated_at)
        VALUES (1, 'Master Admin', 'master@empresa.com', crypt('master', gen_salt('bf', 12)), TRUE, FALSE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
        ON CONFLICT (email, empresa_id) DO UPDATE
            SET senha = crypt('master', gen_salt('bf', 12)),
                ativo = TRUE,
                bloqueado = FALSE,
                updated_at = CURRENT_TIMESTAMP
        RETURNING id INTO v_usuario_id;
    END IF;

    -- Find ADMIN role for empresa 1
    SELECT id INTO v_funcao_admin_id FROM funcoes WHERE empresa_id = 1 AND nome = 'ADMIN' LIMIT 1;

    -- Assign ADMIN role to master user if not already assigned
    IF v_usuario_id IS NOT NULL AND v_funcao_admin_id IS NOT NULL THEN
        INSERT INTO usuarios_funcoes (usuario_id, funcao_id, empresa_id, created_at)
        SELECT v_usuario_id, v_funcao_admin_id, 1, CURRENT_TIMESTAMP
        WHERE NOT EXISTS (
            SELECT 1 FROM usuarios_funcoes 
            WHERE usuario_id = v_usuario_id AND funcao_id = v_funcao_admin_id AND empresa_id = 1
        );
    END IF;
END $$;
