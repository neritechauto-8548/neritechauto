-- Refatoracao da tabela categorias_produtos
ALTER TABLE categorias_produtos DROP CONSTRAINT IF EXISTS fk_categoria_produto_pai;

ALTER TABLE categorias_produtos 
DROP COLUMN IF EXISTS descricao,
DROP COLUMN IF EXISTS categoria_pai_id,
DROP COLUMN IF EXISTS codigo CASCADE,
DROP COLUMN IF EXISTS cor_identificacao,
DROP COLUMN IF EXISTS icone,
DROP COLUMN IF EXISTS margem_lucro_padrao,
DROP COLUMN IF EXISTS comissao_venda_padrao,
DROP COLUMN IF EXISTS controla_estoque,
DROP COLUMN IF EXISTS permite_venda_estoque_negativo,
DROP COLUMN IF EXISTS ordem_exibicao;

-- Refatoracao da tabela unidades_medida
ALTER TABLE unidades_medida 
DROP COLUMN IF EXISTS descricao,
DROP COLUMN IF EXISTS tipo,
DROP COLUMN IF EXISTS fator_conversao_base,
DROP COLUMN IF EXISTS unidade_base,
DROP COLUMN IF EXISTS data_atualizacao,
DROP COLUMN IF EXISTS criado_por,
DROP COLUMN IF EXISTS atualizado_por,
DROP COLUMN IF EXISTS versao;
