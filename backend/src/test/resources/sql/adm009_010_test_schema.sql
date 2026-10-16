-- Complemento de schema dos testes de integração com spring.flyway.enabled=false.
-- Produção continua usando V274/V275; não substitui nem altera migrações publicadas.
CREATE TABLE IF NOT EXISTS seguradoras_os (
  id BIGSERIAL PRIMARY KEY,
  empresa_id BIGINT NOT NULL REFERENCES empresa(id),
  seguradora_id BIGINT NOT NULL REFERENCES seguradoras(id),
  ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id),
  tipo_vinculo VARCHAR(15) NOT NULL,
  criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uq_adm009_teste_empresa_os UNIQUE (empresa_id, ordem_servico_id)
);
ALTER TABLE pedidos_fornecedor ADD COLUMN IF NOT EXISTS transportadora_id BIGINT;
ALTER TABLE pedidos_fornecedor ADD COLUMN IF NOT EXISTS documento_rastreio VARCHAR(120);
