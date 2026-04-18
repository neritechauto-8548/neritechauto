-- Cria a extensão no schema public (se necessário)
CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;

DO $$
DECLARE
    v_empresa_id BIGINT := 1;
    v_usuario_id BIGINT;
    v_funcao_id BIGINT;
    has_data_cadastro_funcoes BOOLEAN;
    has_data_cadastro_usuarios BOOLEAN;
    v_salt TEXT;
    v_hash TEXT;
BEGIN
    -- Garante empresa tenant 1
    IF NOT EXISTS (SELECT 1 FROM empresa WHERE id = v_empresa_id) THEN
        INSERT INTO empresa (id, nome_fantasia, razao_social, cnpj, ativo, data_cadastro)
        VALUES (v_empresa_id, 'Empresa Master', 'Empresa Master LTDA', '99999999000100', TRUE, CURRENT_TIMESTAMP);
    END IF;

    -- Gera salt/BCrypt usando função qualificada pelo schema da extensão
    SELECT gen_salt('bf'::text, 12) INTO v_salt;
    SELECT crypt('master', v_salt) INTO v_hash;

    -- Verifica colunas opcionais em funcoes/usuarios
    SELECT EXISTS(
        SELECT 1 FROM information_schema.columns
        WHERE table_schema='public' AND table_name='funcoes' AND column_name='data_cadastro'
    ) INTO has_data_cadastro_funcoes;

    SELECT EXISTS(
        SELECT 1 FROM information_schema.columns
        WHERE table_schema='public' AND table_name='usuarios' AND column_name='data_cadastro'
    ) INTO has_data_cadastro_usuarios;

    -- Cria/garante role ADMIN
    IF has_data_cadastro_funcoes THEN
        INSERT INTO funcoes (empresa_id, nome, descricao, sistema, ativo, data_cadastro)
        VALUES (v_empresa_id, 'ADMIN', 'Perfil administrador', TRUE, TRUE, CURRENT_TIMESTAMP)
        ON CONFLICT (nome, empresa_id) DO UPDATE SET ativo = TRUE
        RETURNING id INTO v_funcao_id;
    ELSE
        INSERT INTO funcoes (empresa_id, nome, descricao, sistema, ativo)
        VALUES (v_empresa_id, 'ADMIN', 'Perfil administrador', TRUE, TRUE)
        ON CONFLICT (nome, empresa_id) DO UPDATE SET ativo = TRUE
        RETURNING id INTO v_funcao_id;
    END IF;

    -- Cria/atualiza usuário master com senha BCrypt
    IF has_data_cadastro_usuarios THEN
        INSERT INTO usuarios (empresa_id, nome_completo, email, senha, ativo, bloqueado, deve_trocar_senha, data_cadastro)
        VALUES (v_empresa_id, 'Master Admin', 'master@empresa.com', v_hash, TRUE, FALSE, FALSE, CURRENT_TIMESTAMP)
        ON CONFLICT (email, empresa_id) DO UPDATE
            SET nome_completo = EXCLUDED.nome_completo,
                senha = EXCLUDED.senha,
                ativo = TRUE,
                bloqueado = FALSE,
                deve_trocar_senha = FALSE;
    ELSE
        INSERT INTO usuarios (empresa_id, nome_completo, email, senha, ativo, bloqueado, deve_trocar_senha)
        VALUES (v_empresa_id, 'Master Admin', 'master@empresa.com', v_hash, TRUE, FALSE, FALSE)
        ON CONFLICT (email, empresa_id) DO UPDATE
            SET nome_completo = EXCLUDED.nome_completo,
                senha = EXCLUDED.senha,
                ativo = TRUE,
                bloqueado = FALSE,
                deve_trocar_senha = FALSE;
    END IF;

    -- Vincula usuário à role ADMIN
    SELECT id INTO v_usuario_id FROM usuarios WHERE email='master@empresa.com' AND empresa_id=v_empresa_id;

    INSERT INTO usuarios_funcoes (usuario_id, funcao_id, empresa_id)
    VALUES (v_usuario_id, v_funcao_id, v_empresa_id)
    ON CONFLICT (usuario_id, funcao_id) DO NOTHING;
END $$;