-- V216: Remove obrigatoriedade de campos no Agendamento

ALTER TABLE agd_agendamentos ALTER COLUMN veiculo_id DROP NOT NULL;
ALTER TABLE agd_agendamentos ALTER COLUMN tipo_agendamento_id DROP NOT NULL;
