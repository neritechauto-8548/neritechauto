-- NERITECH-0000 - Deletar a empresa 
-- RESPONSAVEL: Alexandre NERI
-- DATA: 15/05/2026


DO $$ 
DECLARE
    target_id CONSTANT bigint := 6; -- <<< COLOQUE O ID DA EMPRESA AQUI
    row_record RECORD;
    delete_stmt TEXT;
BEGIN
    -- 1. Desabilitar triggers para evitar erros de restrição de chave estrangeira durante a deleção em lote
    SET session_replication_role = 'replica';

    RAISE NOTICE 'Iniciando limpeza da empresa ID: %', target_id;

    -- 2. Percorrer todas as tabelas que possuem a coluna 'empresa_id'
    FOR row_record IN 
        SELECT table_schema, table_name 
        FROM information_schema.columns 
        WHERE column_name = 'empresa_id' 
          AND table_schema = 'public'
          AND table_name NOT IN ('empresa') -- Deixamos a tabela mestre por último
    LOOP
        delete_stmt := 'DELETE FROM ' || quote_ident(row_record.table_schema) || '.' || quote_ident(row_record.table_name) || ' WHERE empresa_id = ' || target_id;
        
        BEGIN
            EXECUTE delete_stmt;
            -- RAISE NOTICE 'Executado: %', delete_stmt;
        EXCEPTION WHEN OTHERS THEN
            RAISE NOTICE 'Erro ao deletar da tabela %: %', row_record.table_name, SQLERRM;
        END;
    END LOOP;

   
    -- 5. Deletar a Empresa propriamente dita
    DELETE FROM empresa WHERE id = target_id;

    -- 6. Reabilitar triggers
    SET session_replication_role = 'origin';

    RAISE NOTICE 'Limpeza concluída com sucesso para a empresa ID: %', target_id;

END $$;

