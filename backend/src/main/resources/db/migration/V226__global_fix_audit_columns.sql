-- V226: Global fix for audit columns and missing required columns
-- Description: Renames all English audit columns to Portuguese and ensures all tables have required audit/versioning columns

DO $$
DECLARE
    r RECORD;
    t_name TEXT;
BEGIN
    -- 1. Rename English columns to Portuguese in ALL tables
    
    -- created_at -> data_cadastro
    FOR r IN SELECT table_name FROM information_schema.columns WHERE column_name = 'created_at' AND table_schema = 'public'
    LOOP
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = r.table_name AND column_name = 'data_cadastro' AND table_schema = 'public') THEN
            EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' RENAME COLUMN created_at TO data_cadastro';
        ELSE
            EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' DROP COLUMN IF EXISTS created_at';
        END IF;
    END LOOP;

    -- updated_at -> data_atualizacao
    FOR r IN SELECT table_name FROM information_schema.columns WHERE column_name = 'updated_at' AND table_schema = 'public'
    LOOP
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = r.table_name AND column_name = 'data_atualizacao' AND table_schema = 'public') THEN
            EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' RENAME COLUMN updated_at TO data_atualizacao';
        ELSE
            EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' DROP COLUMN IF EXISTS updated_at';
        END IF;
    END LOOP;

    -- created_by -> criado_por
    FOR r IN SELECT table_name FROM information_schema.columns WHERE column_name = 'created_by' AND table_schema = 'public'
    LOOP
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = r.table_name AND column_name = 'criado_por' AND table_schema = 'public') THEN
            EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' RENAME COLUMN created_by TO criado_por';
        ELSE
            EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' DROP COLUMN IF EXISTS created_by';
        END IF;
    END LOOP;

    -- updated_by -> atualizado_por
    FOR r IN SELECT table_name FROM information_schema.columns WHERE column_name = 'updated_by' AND table_schema = 'public'
    LOOP
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = r.table_name AND column_name = 'atualizado_por' AND table_schema = 'public') THEN
            EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' RENAME COLUMN updated_by TO atualizado_por';
        ELSE
            EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' DROP COLUMN IF EXISTS updated_by';
        END IF;
    END LOOP;

    -- 2. Ensure ALL tables have the standard audit columns and versao
    -- This is important to satisfy BaseEntity requirements in Hibernate
    FOR r IN 
        SELECT table_name 
        FROM information_schema.tables 
        WHERE table_schema = 'public' 
        AND table_type = 'BASE TABLE'
        AND table_name NOT IN ('flyway_schema_history', 'schema_version')
    LOOP
        t_name := r.table_name;
        
        -- data_cadastro
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = t_name AND column_name = 'data_cadastro') THEN
            EXECUTE 'ALTER TABLE ' || quote_ident(t_name) || ' ADD COLUMN data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP';
        END IF;
        
        -- data_atualizacao
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = t_name AND column_name = 'data_atualizacao') THEN
            EXECUTE 'ALTER TABLE ' || quote_ident(t_name) || ' ADD COLUMN data_atualizacao TIMESTAMP';
        END IF;
        
        -- criado_por
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = t_name AND column_name = 'criado_por') THEN
            EXECUTE 'ALTER TABLE ' || quote_ident(t_name) || ' ADD COLUMN criado_por BIGINT';
        END IF;
        
        -- atualizado_por
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = t_name AND column_name = 'atualizado_por') THEN
            EXECUTE 'ALTER TABLE ' || quote_ident(t_name) || ' ADD COLUMN atualizado_por BIGINT';
        END IF;
        
        -- versao
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = t_name AND column_name = 'versao') THEN
            EXECUTE 'ALTER TABLE ' || quote_ident(t_name) || ' ADD COLUMN versao INTEGER DEFAULT 0 NOT NULL';
        END IF;
        
    END LOOP;
END $$;
