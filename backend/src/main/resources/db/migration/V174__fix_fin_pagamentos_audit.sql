-- V174: Fix audit columns in ALL tables
-- Description: Dynamically populate missing audit columns in all tables that have them to satisfy NOT NULL constraints

DO $$
DECLARE
    r RECORD;
BEGIN
    -- Loop through all TABLES (not views) that have 'data_cadastro' column
    FOR r IN 
        SELECT c.table_name 
        FROM information_schema.columns c
        JOIN information_schema.tables t ON c.table_name = t.table_name AND c.table_schema = t.table_schema
        WHERE c.column_name = 'data_cadastro' 
        AND c.table_schema = 'public'
        AND t.table_type = 'BASE TABLE'
    LOOP
        -- Update null values to current timestamp
        EXECUTE 'UPDATE ' || quote_ident(r.table_name) || ' SET data_cadastro = CURRENT_TIMESTAMP WHERE data_cadastro IS NULL';
        
        -- Make column NOT NULL
        EXECUTE 'ALTER TABLE ' || quote_ident(r.table_name) || ' ALTER COLUMN data_cadastro SET NOT NULL';
        
        RAISE NOTICE 'Fixed data_cadastro for table: %', r.table_name;
    END LOOP;
    
    -- Loop through all TABLES (not views) that have 'empresa_id' column to ensure index exists
    FOR r IN 
        SELECT c.table_name 
        FROM information_schema.columns c
        JOIN information_schema.tables t ON c.table_name = t.table_name AND c.table_schema = t.table_schema
        WHERE c.column_name = 'empresa_id' 
        AND c.table_schema = 'public'
        AND t.table_type = 'BASE TABLE'
    LOOP
        -- Create index if not exists (using a safe name)
        EXECUTE 'CREATE INDEX IF NOT EXISTS ' || quote_ident('idx_' || r.table_name || '_empresa') || ' ON ' || quote_ident(r.table_name) || '(empresa_id)';
    END LOOP;
    
END $$;
