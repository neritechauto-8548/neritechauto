-- V245: Add address columns to funcionarios table
-- Description: Split employee address into structured fields matching company layout

ALTER TABLE funcionarios ADD COLUMN cep VARCHAR(9);
ALTER TABLE funcionarios ADD COLUMN logradouro VARCHAR(255);
ALTER TABLE funcionarios ADD COLUMN numero VARCHAR(20);
ALTER TABLE funcionarios ADD COLUMN complemento VARCHAR(100);
ALTER TABLE funcionarios ADD COLUMN bairro VARCHAR(100);
ALTER TABLE funcionarios ADD COLUMN cidade VARCHAR(100);
ALTER TABLE funcionarios ADD COLUMN estado CHAR(2);
ALTER TABLE funcionarios ADD COLUMN pais VARCHAR(50) DEFAULT 'Brasil';

COMMENT ON COLUMN funcionarios.cep IS 'CEP residencial do funcionário';
COMMENT ON COLUMN funcionarios.logradouro IS 'Logradouro residencial do funcionário';
COMMENT ON COLUMN funcionarios.numero IS 'Número residencial do funcionário';
COMMENT ON COLUMN funcionarios.complemento IS 'Complemento residencial do funcionário';
COMMENT ON COLUMN funcionarios.bairro IS 'Bairro residencial do funcionário';
COMMENT ON COLUMN funcionarios.cidade IS 'Cidade residencial do funcionário';
COMMENT ON COLUMN funcionarios.estado IS 'Estado (UF) residencial do funcionário';
COMMENT ON COLUMN funcionarios.pais IS 'País residencial do funcionário';
