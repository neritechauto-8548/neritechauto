-- Este script cria uma empresa seed (se a tabela existir) e insere clientes

INSERT INTO empresa (id, nome_fantasia, razao_social, cnpj, ativo, data_cadastro)
VALUES
    (1, 'NeriTech Auto', 'NeriTech Auto', '00000000000000', TRUE, '2026-05-01 10:00:00')
  ON CONFLICT (id) DO NOTHING;
