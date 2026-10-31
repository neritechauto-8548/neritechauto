-- Campos da tela Dados Profissionais, com nomenclatura integralmente em português.
ALTER TABLE funcionarios ADD COLUMN IF NOT EXISTS perfil_profissional VARCHAR(50);
CREATE INDEX IF NOT EXISTS idx_funcionarios_empresa_cargo ON funcionarios(empresa_id,cargo_id);
COMMENT ON COLUMN funcionarios.matricula IS 'Identificador interno técnico, não exibido como campo obrigatório de cadastro';
COMMENT ON COLUMN funcionarios.registro_profissional IS 'Número de registro profissional exibido na aba Dados Profissionais';
COMMENT ON COLUMN funcionarios.perfil_profissional IS 'Perfil funcional selecionado na aba Dados Profissionais';
