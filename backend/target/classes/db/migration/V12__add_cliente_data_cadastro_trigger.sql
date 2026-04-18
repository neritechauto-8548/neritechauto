-- Migration V12: Trigger para preencher automaticamente data_cadastro na tabela cliente
-- Observação: a coluna já possui DEFAULT CURRENT_TIMESTAMP; a trigger garante preenchimento
-- mesmo quando o valor é enviado como NULL pela aplicação.

-- Função de trigger
CREATE OR REPLACE FUNCTION public.fn_cliente_set_data_cadastro()
RETURNS trigger AS $$
BEGIN
    IF NEW.data_cadastro IS NULL THEN
        NEW.data_cadastro := CURRENT_TIMESTAMP;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Remove trigger anterior se existir para evitar erro em reexecuções
DROP TRIGGER IF EXISTS trg_cliente_set_data_cadastro ON public.cliente;

-- Cria a trigger para INSERT
CREATE TRIGGER trg_cliente_set_data_cadastro
BEFORE INSERT ON public.cliente
FOR EACH ROW
EXECUTE FUNCTION public.fn_cliente_set_data_cadastro();