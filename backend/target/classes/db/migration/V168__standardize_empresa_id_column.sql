DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'cliente' AND column_name = 'id_empresa'
    ) THEN
        IF EXISTS (
            SELECT 1 FROM information_schema.columns 
            WHERE table_name = 'cliente' AND column_name = 'empresa_id'
        ) THEN
            EXECUTE 'ALTER TABLE cliente DROP COLUMN IF EXISTS id_empresa';
        ELSE
            EXECUTE 'ALTER TABLE cliente RENAME COLUMN id_empresa TO empresa_id';
        END IF;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'contatos_clientes' AND column_name = 'id_empresa'
    ) THEN
        IF EXISTS (
            SELECT 1 FROM information_schema.columns 
            WHERE table_name = 'contatos_clientes' AND column_name = 'empresa_id'
        ) THEN
            EXECUTE 'ALTER TABLE contatos_clientes DROP COLUMN IF EXISTS id_empresa';
        ELSE
            EXECUTE 'ALTER TABLE contatos_clientes RENAME COLUMN id_empresa TO empresa_id';
        END IF;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'documentos_clientes' AND column_name = 'id_empresa'
    ) THEN
        IF EXISTS (
            SELECT 1 FROM information_schema.columns 
            WHERE table_name = 'documentos_clientes' AND column_name = 'empresa_id'
        ) THEN
            EXECUTE 'ALTER TABLE documentos_clientes DROP COLUMN IF EXISTS id_empresa';
        ELSE
            EXECUTE 'ALTER TABLE documentos_clientes RENAME COLUMN id_empresa TO empresa_id';
        END IF;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'enderecos_clientes' AND column_name = 'id_empresa'
    ) THEN
        IF EXISTS (
            SELECT 1 FROM information_schema.columns 
            WHERE table_name = 'enderecos_clientes' AND column_name = 'empresa_id'
        ) THEN
            EXECUTE 'ALTER TABLE enderecos_clientes DROP COLUMN IF EXISTS id_empresa';
        ELSE
            EXECUTE 'ALTER TABLE enderecos_clientes RENAME COLUMN id_empresa TO empresa_id';
        END IF;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'modelos_veiculos' AND column_name = 'id_empresa'
    ) THEN
        IF EXISTS (
            SELECT 1 FROM information_schema.columns 
            WHERE table_name = 'modelos_veiculos' AND column_name = 'empresa_id'
        ) THEN
            EXECUTE 'ALTER TABLE modelos_veiculos DROP COLUMN IF EXISTS id_empresa';
        ELSE
            EXECUTE 'ALTER TABLE modelos_veiculos RENAME COLUMN id_empresa TO empresa_id';
        END IF;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'veiculos' AND column_name = 'id_empresa'
    ) THEN
        IF EXISTS (
            SELECT 1 FROM information_schema.columns 
            WHERE table_name = 'veiculos' AND column_name = 'empresa_id'
        ) THEN
            EXECUTE 'ALTER TABLE veiculos DROP COLUMN IF EXISTS id_empresa';
        ELSE
            EXECUTE 'ALTER TABLE veiculos RENAME COLUMN id_empresa TO empresa_id';
        END IF;
    END IF;
END $$;

ALTER INDEX IF EXISTS idx_cliente_id_empresa RENAME TO idx_cliente_empresa_id;
ALTER INDEX IF EXISTS idx_contatos_clientes_id_empresa RENAME TO idx_contatos_clientes_empresa_id;
ALTER INDEX IF EXISTS idx_documentos_clientes_id_empresa RENAME TO idx_documentos_clientes_empresa_id;
