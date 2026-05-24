ALTER TABLE configuracoes_oficina ADD COLUMN possui_intervalo BOOLEAN DEFAULT FALSE;
ALTER TABLE configuracoes_oficina ADD COLUMN inicio_intervalo TIME;
ALTER TABLE configuracoes_oficina ADD COLUMN fim_intervalo TIME;
