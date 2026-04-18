-- V203: Refatora a tabela servicos mantendo apenas as colunas essenciais.
-- Remove colunas obsoletas e adiciona a coluna 'custo'.

ALTER TABLE servicos
    DROP COLUMN IF EXISTS codigo,
    DROP COLUMN IF EXISTS descricao,
    DROP COLUMN IF EXISTS nivel_dificuldade,
    DROP COLUMN IF EXISTS tipo_cobranca,
    DROP COLUMN IF EXISTS descricao_detalhada,
    DROP COLUMN IF EXISTS tempo_execucao_minutos,
    DROP COLUMN IF EXISTS margem_lucro_percentual,
    DROP COLUMN IF EXISTS requer_elevador,
    DROP COLUMN IF EXISTS requer_equipamento_especial,
    DROP COLUMN IF EXISTS equipamentos_necessarios,
    DROP COLUMN IF EXISTS ferramentas_necessarias,
    DROP COLUMN IF EXISTS materiais_inclusos,
    DROP COLUMN IF EXISTS garantia_dias,
    DROP COLUMN IF EXISTS unidade_cobranca,
    DROP COLUMN IF EXISTS pontos_fidelidade,
    DROP COLUMN IF EXISTS comissao_percentual,
    DROP COLUMN IF EXISTS foto_ilustrativa_url,
    DROP COLUMN IF EXISTS video_explicativo_url,
    DROP COLUMN IF EXISTS checklist_execucao,
    DROP COLUMN IF EXISTS destaque,
    DROP COLUMN IF EXISTS promocional,
    DROP COLUMN IF EXISTS categoria_id;

-- Garante que preco_base seja NOT NULL (pode já existir)
ALTER TABLE servicos
    ALTER COLUMN preco_base SET NOT NULL;

-- Adiciona coluna custo se não existir
ALTER TABLE servicos
    ADD COLUMN IF NOT EXISTS custo DECIMAL(10, 2) NOT NULL DEFAULT 0;
