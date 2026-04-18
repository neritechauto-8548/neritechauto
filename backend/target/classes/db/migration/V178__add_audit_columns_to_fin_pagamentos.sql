ALTER TABLE fin_pagamentos 
ADD COLUMN IF NOT EXISTS data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE fin_pagamentos
ADD COLUMN IF NOT EXISTS criado_por BIGINT;

-- data_atualizacao, atualizado_por, and versao seem to exist based on V114, but "versao" was defined as INTEGER DEFAULT 0.
-- BaseEntity defines versao as NOT NULL INTEGER DEFAULT 0 (in code). 
-- Let's ensure they are consistent.

ALTER TABLE fin_pagamentos
ALTER COLUMN versao SET DEFAULT 0;

ALTER TABLE fin_pagamentos
ALTER COLUMN versao SET NOT NULL;
