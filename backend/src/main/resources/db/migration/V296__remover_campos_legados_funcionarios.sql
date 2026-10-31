-- Contrato visual de Funcionários (14 capturas): exclusão expressamente autorizada.
-- Campos técnicos e vínculos com OS, Financeiro, segurança e auditoria permanecem.
-- A view criada pela V105 usa telefone_principal e email_pessoal, que serão removidos.
-- Não usar CASCADE: somente a view conhecida é recriada, sem excluir objetos independentes.
-- Esta versão está pendente no banco informado (versão atual 295).
DROP VIEW IF EXISTS vw_funcionarios_ativos;

ALTER TABLE funcionarios
  DROP COLUMN IF EXISTS estado_civil,
  DROP COLUMN IF EXISTS nacionalidade,
  DROP COLUMN IF EXISTS naturalidade,
  DROP COLUMN IF EXISTS nome_mae,
  DROP COLUMN IF EXISTS nome_pai,
  DROP COLUMN IF EXISTS profissao,
  DROP COLUMN IF EXISTS vale_alimentacao,
  DROP COLUMN IF EXISTS plano_saude,
  DROP COLUMN IF EXISTS plano_odontologico,
  DROP COLUMN IF EXISTS endereco_completo,
  DROP COLUMN IF EXISTS pais,
  DROP COLUMN IF EXISTS telefone_principal,
  DROP COLUMN IF EXISTS telefone_emergencia,
  DROP COLUMN IF EXISTS contato_emergencia_nome,
  DROP COLUMN IF EXISTS contato_emergencia_parentesco,
  DROP COLUMN IF EXISTS email_pessoal,
  DROP COLUMN IF EXISTS banco_codigo,
  DROP COLUMN IF EXISTS banco_agencia,
  DROP COLUMN IF EXISTS banco_conta,
  DROP COLUMN IF EXISTS banco_tipo_conta,
  DROP COLUMN IF EXISTS titulo_eleitor,
  DROP COLUMN IF EXISTS certificado_reservista,
  DROP COLUMN IF EXISTS cnh_numero,
  DROP COLUMN IF EXISTS cnh_categoria,
  DROP COLUMN IF EXISTS cnh_validade,
  DROP COLUMN IF EXISTS comissao_percentual;


-- Restaurar a consulta de RH sem campos removidos do contrato visual.
-- Mantém as colunas essenciais e o filtro original por status ATIVO.
CREATE VIEW vw_funcionarios_ativos AS
SELECT
    f.id,
    f.empresa_id,
    f.matricula,
    f.nome_completo,
    f.cpf,
    f.data_admissao,
    f.status,
    c.nome AS cargo_nome,
    d.nome AS departamento_nome,
    f.salario_base
FROM funcionarios f
LEFT JOIN cargos c ON f.cargo_id = c.id
LEFT JOIN departamentos d ON f.departamento_id = d.id
WHERE f.status = 'ATIVO';

COMMENT ON VIEW vw_funcionarios_ativos IS
  'Funcionários ativos com cargo e departamento, sem contatos legados removidos na V296';
