-- Adiciona a coluna ordem_servico_id na tabela fin_pagamentos para vincular pagamentos diretamente às OS
ALTER TABLE fin_pagamentos ADD COLUMN ordem_servico_id BIGINT;

-- Adiciona a restrição de chave estrangeira (opcional, mas recomendado)
ALTER TABLE fin_pagamentos
    ADD CONSTRAINT fk_fin_pagamentos_ordem_servico
    FOREIGN KEY (ordem_servico_id)
    REFERENCES ordens_servico (id)
    ON DELETE SET NULL;
