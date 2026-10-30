-- Lote 05 | cadastro de clientes / dados complementares e decisões legais
ALTER TABLE cliente
    ADD COLUMN IF NOT EXISTS documento_estrangeiro VARCHAR(80),
    ADD COLUMN IF NOT EXISTS pais VARCHAR(80),
    ADD COLUMN IF NOT EXISTS estado_civil VARCHAR(30),
    ADD COLUMN IF NOT EXISTS profissao VARCHAR(120),
    ADD COLUMN IF NOT EXISTS numero_apolice VARCHAR(80),
    ADD COLUMN IF NOT EXISTS limite_credito NUMERIC(14,2),
    ADD COLUMN IF NOT EXISTS como_nos_conheceu VARCHAR(100),
    ADD COLUMN IF NOT EXISTS site VARCHAR(255),
    ADD COLUMN IF NOT EXISTS locadora_veiculos BOOLEAN,
    ADD COLUMN IF NOT EXISTS orgao_publico BOOLEAN,
    ADD COLUMN IF NOT EXISTS retencao_iss BOOLEAN,
    ADD COLUMN IF NOT EXISTS retencao_irrf_csll BOOLEAN,
    ADD COLUMN IF NOT EXISTS alerta_lgpd BOOLEAN,
    ADD COLUMN IF NOT EXISTS autorizacao_lgpd BOOLEAN;
-- Ausência de decisão LGPD permanece NULL (não se presume consentimento).

-- Dados de contato vinculados ao cliente existente e isolados por empresa.
ALTER TABLE contatos_clientes
    ADD COLUMN IF NOT EXISTS nome VARCHAR(160),
    ADD COLUMN IF NOT EXISTS sexo VARCHAR(1),
    ADD COLUMN IF NOT EXISTS data_nascimento DATE,
    ADD COLUMN IF NOT EXISTS grupo_principal BOOLEAN;
