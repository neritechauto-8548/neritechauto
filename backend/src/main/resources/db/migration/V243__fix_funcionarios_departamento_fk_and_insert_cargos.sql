-- Corrige a FK de departamento para apontar para departamentos_contabio
ALTER TABLE funcionarios DROP CONSTRAINT IF EXISTS fk_funcionarios_departamento;

ALTER TABLE funcionarios ADD CONSTRAINT fk_funcionarios_departamento_contabio 
    FOREIGN KEY (departamento_id) REFERENCES departamentos_contabio(id);

-- Insere os cargos padrão para todas as empresas existentes (se já não existirem)
INSERT INTO cargos (empresa_id, nome, descricao, ativo)
SELECT id, 'Mecânico', 'Profissional responsável pela manutenção e reparo de veículos', true FROM empresa
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO cargos (empresa_id, nome, descricao, ativo)
SELECT id, 'Gerente de Oficina', 'Responsável por gerenciar as operações diárias da oficina', true FROM empresa
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO cargos (empresa_id, nome, descricao, ativo)
SELECT id, 'Atendente', 'Responsável pelo atendimento inicial ao cliente', true FROM empresa
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO cargos (empresa_id, nome, descricao, ativo)
SELECT id, 'Auxiliar Administrativo', 'Responsável pelas rotinas administrativas e financeiras', true FROM empresa
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO cargos (empresa_id, nome, descricao, ativo)
SELECT id, 'Estoquista', 'Responsável pelo controle de peças e ferramentas do estoque', true FROM empresa
ON CONFLICT (empresa_id, nome) DO NOTHING;
