-- Flyway Migration V196__remove_manutencao_historico_quilometragem.sql
-- Remove tabelas manutencoes_preventivas e historico_quilometragem

DROP TABLE IF EXISTS manutencoes_preventivas;
DROP TABLE IF EXISTS historico_quilometragem;
