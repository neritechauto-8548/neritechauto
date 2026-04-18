-- Seed de dados para a tabela cliente (PF e PJ) com escopo de tenant
-- Este script cria uma empresa seed (se a tabela existir) e insere clientes

DO $$
DECLARE
    v_empresa_id BIGINT := 1; -- valor padrão quando a tabela empresa não existe
BEGIN
    -- Se a tabela empresa existir, garante uma empresa seed e usa seu ID
    IF EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_schema = 'public' AND table_name = 'empresa'
    ) THEN
        INSERT INTO empresa (nome_fantasia, razao_social, cnpj, ativo, data_cadastro)
        VALUES ('Empresa Seed', 'Empresa Seed LTDA', '99999999000100', TRUE, CURRENT_TIMESTAMP)
        ON CONFLICT (cnpj) DO UPDATE
            SET razao_social = EXCLUDED.razao_social,
                nome_fantasia = EXCLUDED.nome_fantasia,
                ativo = TRUE
        RETURNING id INTO v_empresa_id;
    END IF;

    -- Clientes PF
    INSERT INTO cliente (id_empresa, tipo_cliente, nome_completo, cpf, rg, data_nascimento, sexo, estado_civil, profissao, origem_cliente, detalhes_origem, status, observacoes_gerais, data_cadastro)
    VALUES (v_empresa_id, 'PESSOA_FISICA', 'Joao da Silva', '11122233344', '1234567', '1990-05-20', 'M', 'CASADO', 'Engenheiro', 'INDICACAO', 'Indicado por Maria', 'ATIVO', 'Cliente premium', CURRENT_TIMESTAMP)
    ON CONFLICT (cpf) DO NOTHING;

    INSERT INTO cliente (id_empresa, tipo_cliente, nome_completo, cpf, rg, data_nascimento, sexo, estado_civil, profissao, origem_cliente, status, data_cadastro)
    VALUES (v_empresa_id, 'PESSOA_FISICA', 'Maria Oliveira', '55566677788', '7654321', '1988-10-10', 'F', 'SOLTEIRO', 'Analista', 'SITE', 'ATIVO', CURRENT_TIMESTAMP)
    ON CONFLICT (cpf) DO NOTHING;

    INSERT INTO cliente (id_empresa, tipo_cliente, nome_completo, cpf, data_nascimento, sexo, estado_civil, origem_cliente, status, motivo_bloqueio, data_bloqueio, data_cadastro)
    VALUES (v_empresa_id, 'PESSOA_FISICA', 'Pedro Lima', '99900011122', '1985-03-15', 'M', 'DIVORCIADO', 'TELEFONE', 'BLOQUEADO', 'Pendência financeira', '2024-11-01 10:00:00', CURRENT_TIMESTAMP)
    ON CONFLICT (cpf) DO NOTHING;

    INSERT INTO cliente (id_empresa, tipo_cliente, nome_completo, cpf, data_nascimento, sexo, estado_civil, origem_cliente, status, data_cadastro)
    VALUES (v_empresa_id, 'PESSOA_FISICA', 'Ana Santos', '33344455566', '1995-07-22', 'F', 'CASADO', 'PRESENCIAL', 'INATIVO', CURRENT_TIMESTAMP)
    ON CONFLICT (cpf) DO NOTHING;

    INSERT INTO cliente (id_empresa, tipo_cliente, nome_completo, cpf, data_nascimento, sexo, estado_civil, origem_cliente, status, data_cadastro)
    VALUES (v_empresa_id, 'PESSOA_FISICA', 'Carlos Pereira', '77788899900', '1979-12-01', 'M', 'VIUVO', 'REDES_SOCIAIS', 'ATIVO', CURRENT_TIMESTAMP)
    ON CONFLICT (cpf) DO NOTHING;

    INSERT INTO cliente (id_empresa, tipo_cliente, nome_completo, cpf, data_nascimento, sexo, estado_civil, origem_cliente, status, data_cadastro)
    VALUES (v_empresa_id, 'PESSOA_FISICA', 'Luciana Prado', '12312312312', '1992-02-02', 'F', 'UNIAO_ESTAVEL', 'ANUNCIO', 'PROSPECTO', CURRENT_TIMESTAMP)
    ON CONFLICT (cpf) DO NOTHING;

    -- Clientes PJ (nome_completo obrigatório, usar contato responsável)
    INSERT INTO cliente (id_empresa, tipo_cliente, nome_completo, nome_fantasia, razao_social, cnpj, inscricao_estadual, inscricao_municipal, origem_cliente, status, observacoes_gerais, data_cadastro)
    VALUES (v_empresa_id, 'PESSOA_JURIDICA', 'Carlos Andrade', 'Loja XPTO', 'Loja XPTO LTDA', '12345678000195', '123456789', '987654321', 'SITE', 'ATIVO', 'Cliente corporativo', CURRENT_TIMESTAMP)
    ON CONFLICT (cnpj) DO NOTHING;

    INSERT INTO cliente (id_empresa, tipo_cliente, nome_completo, nome_fantasia, razao_social, cnpj, origem_cliente, status, data_cadastro)
    VALUES (v_empresa_id, 'PESSOA_JURIDICA', 'Fernanda Costa', 'Tech House', 'Tech House SA', '22345678000166', 'INDICACAO', 'ATIVO', CURRENT_TIMESTAMP)
    ON CONFLICT (cnpj) DO NOTHING;

    INSERT INTO cliente (id_empresa, tipo_cliente, nome_completo, nome_fantasia, razao_social, cnpj, origem_cliente, status, data_cadastro)
    VALUES (v_empresa_id, 'PESSOA_JURIDICA', 'Roberto Neri', 'Auto Peças Rápidas', 'Auto Peças Rápidas LTDA', '32345678000177', 'PRESENCIAL', 'INATIVO', CURRENT_TIMESTAMP)
    ON CONFLICT (cnpj) DO NOTHING;

    INSERT INTO cliente (id_empresa, tipo_cliente, nome_completo, nome_fantasia, razao_social, cnpj, origem_cliente, status, data_cadastro)
    VALUES (v_empresa_id, 'PESSOA_JURIDICA', 'Paula Sousa', 'Mecânica Moderna', 'Mecânica Moderna Ltda', '42345678000188', 'REDES_SOCIAIS', 'PROSPECTO', CURRENT_TIMESTAMP)
    ON CONFLICT (cnpj) DO NOTHING;
END $$;