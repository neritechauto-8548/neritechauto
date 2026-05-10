-- Adiciona a coluna condicao_pagamento_id na tabela fin_pagamentos
ALTER TABLE fin_pagamentos ADD COLUMN condicao_pagamento_id BIGINT;

-- Adiciona a restrição de chave estrangeira (FK)
ALTER TABLE fin_pagamentos
ADD CONSTRAINT fk_fin_pagamentos_condicao_pagamento
FOREIGN KEY (condicao_pagamento_id) REFERENCES fin_condicoes_pagamento(id);
