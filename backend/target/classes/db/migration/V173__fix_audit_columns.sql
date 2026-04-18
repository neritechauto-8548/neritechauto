-- V173: Fix audit columns naming and add missing audit columns
-- Description: Rename audit columns in marcas_veiculos from English to Portuguese
--              and add missing audit columns to itens_os_produtos and itens_os_servicos

-- Fix marcas_veiculos table - use DO blocks to check if columns exist before renaming
DO $$
BEGIN
    -- Handle created_date -> data_cadastro
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'marcas_veiculos' AND column_name = 'created_date') THEN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'marcas_veiculos' AND column_name = 'data_cadastro') THEN
            -- Both exist, drop the old one
            ALTER TABLE marcas_veiculos DROP COLUMN created_date;
        ELSE
            -- Only old exists, rename it
            ALTER TABLE marcas_veiculos RENAME COLUMN created_date TO data_cadastro;
        END IF;
    END IF;
END $$;

DO $$
BEGIN
    -- Handle last_modified_date -> data_atualizacao
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'marcas_veiculos' AND column_name = 'last_modified_date') THEN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'marcas_veiculos' AND column_name = 'data_atualizacao') THEN
            ALTER TABLE marcas_veiculos DROP COLUMN last_modified_date;
        ELSE
            ALTER TABLE marcas_veiculos RENAME COLUMN last_modified_date TO data_atualizacao;
        END IF;
    END IF;
END $$;

DO $$
BEGIN
    -- Handle created_by -> criado_por
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'marcas_veiculos' AND column_name = 'created_by') THEN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'marcas_veiculos' AND column_name = 'criado_por') THEN
            ALTER TABLE marcas_veiculos DROP COLUMN created_by;
        ELSE
            ALTER TABLE marcas_veiculos RENAME COLUMN created_by TO criado_por;
        END IF;
    END IF;
END $$;

DO $$
BEGIN
    -- Handle last_modified_by -> atualizado_por
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'marcas_veiculos' AND column_name = 'last_modified_by') THEN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'marcas_veiculos' AND column_name = 'atualizado_por') THEN
            ALTER TABLE marcas_veiculos DROP COLUMN last_modified_by;
        ELSE
            ALTER TABLE marcas_veiculos RENAME COLUMN last_modified_by TO atualizado_por;
        END IF;
    END IF;
END $$;

-- Change criado_por and atualizado_por from VARCHAR to BIGINT if they exist and are not already BIGINT
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name = 'marcas_veiculos' AND column_name = 'criado_por' 
               AND data_type != 'bigint') THEN
        -- First, clean up any non-numeric values (set them to 1 - default system user)
        UPDATE marcas_veiculos 
        SET criado_por = '1' 
        WHERE criado_por IS NOT NULL 
          AND criado_por !~ '^[0-9]+$';
        
        -- Then convert to BIGINT
        ALTER TABLE marcas_veiculos ALTER COLUMN criado_por TYPE BIGINT USING criado_por::BIGINT;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name = 'marcas_veiculos' AND column_name = 'atualizado_por' 
               AND data_type != 'bigint') THEN
        -- Clean up any non-numeric values (set them to 1 - default system user)
        UPDATE marcas_veiculos 
        SET atualizado_por = '1' 
        WHERE atualizado_por IS NOT NULL 
          AND atualizado_por !~ '^[0-9]+$';
        
        -- Then convert to BIGINT
        ALTER TABLE marcas_veiculos ALTER COLUMN atualizado_por TYPE BIGINT USING atualizado_por::BIGINT;
    END IF;
END $$;

-- Add empresa_id column to marcas_veiculos
ALTER TABLE marcas_veiculos 
    ADD COLUMN IF NOT EXISTS empresa_id BIGINT;

-- Add missing audit columns to itens_os_produtos
ALTER TABLE itens_os_produtos 
    ADD COLUMN IF NOT EXISTS data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN IF NOT EXISTS data_atualizacao TIMESTAMP,
    ADD COLUMN IF NOT EXISTS criado_por BIGINT,
    ADD COLUMN IF NOT EXISTS atualizado_por BIGINT,
    ADD COLUMN IF NOT EXISTS empresa_id BIGINT;

-- Update existing rows in itens_os_produtos to have data_cadastro
UPDATE itens_os_produtos 
SET data_cadastro = CURRENT_TIMESTAMP 
WHERE data_cadastro IS NULL;

-- Make data_cadastro NOT NULL after populating existing rows (only if column exists and is nullable)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name = 'itens_os_produtos' AND column_name = 'data_cadastro' 
               AND is_nullable = 'YES') THEN
        ALTER TABLE itens_os_produtos ALTER COLUMN data_cadastro SET NOT NULL;
    END IF;
END $$;

-- Add missing audit columns to itens_os_servicos
ALTER TABLE itens_os_servicos 
    ADD COLUMN IF NOT EXISTS data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN IF NOT EXISTS data_atualizacao TIMESTAMP,
    ADD COLUMN IF NOT EXISTS criado_por BIGINT,
    ADD COLUMN IF NOT EXISTS atualizado_por BIGINT,
    ADD COLUMN IF NOT EXISTS empresa_id BIGINT;

-- Update existing rows in itens_os_servicos to have data_cadastro
UPDATE itens_os_servicos 
SET data_cadastro = CURRENT_TIMESTAMP 
WHERE data_cadastro IS NULL;

-- Make data_cadastro NOT NULL after populating existing rows (only if column exists and is nullable)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name = 'itens_os_servicos' AND column_name = 'data_cadastro' 
               AND is_nullable = 'YES') THEN
        ALTER TABLE itens_os_servicos ALTER COLUMN data_cadastro SET NOT NULL;
    END IF;
END $$;

-- Add indexes for empresa_id
CREATE INDEX IF NOT EXISTS idx_marcas_veiculos_empresa ON marcas_veiculos(empresa_id);
CREATE INDEX IF NOT EXISTS idx_itens_os_produtos_empresa ON itens_os_produtos(empresa_id);
CREATE INDEX IF NOT EXISTS idx_itens_os_servicos_empresa ON itens_os_servicos(empresa_id);

-- Add comments (these will only work if columns exist)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name = 'marcas_veiculos' AND column_name = 'data_cadastro') THEN
        COMMENT ON COLUMN marcas_veiculos.data_cadastro IS 'Data de cadastro do registro';
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name = 'marcas_veiculos' AND column_name = 'data_atualizacao') THEN
        COMMENT ON COLUMN marcas_veiculos.data_atualizacao IS 'Data da última atualização';
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name = 'marcas_veiculos' AND column_name = 'criado_por') THEN
        COMMENT ON COLUMN marcas_veiculos.criado_por IS 'ID do usuário que criou o registro';
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name = 'marcas_veiculos' AND column_name = 'atualizado_por') THEN
        COMMENT ON COLUMN marcas_veiculos.atualizado_por IS 'ID do usuário que atualizou o registro';
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name = 'marcas_veiculos' AND column_name = 'empresa_id') THEN
        COMMENT ON COLUMN marcas_veiculos.empresa_id IS 'ID da empresa (multi-tenancy)';
    END IF;
END $$;
