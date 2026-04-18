-- Migration: Drop cotacao module tables
-- Description: Drops all tables and sequences related to cotacoes, itens_cotacao, respostas_cotacao, and comparativos_cotacao.

DROP TABLE IF EXISTS comparativos_cotacao CASCADE;
DROP TABLE IF EXISTS respostas_cotacao CASCADE;
DROP TABLE IF EXISTS itens_cotacao CASCADE;
DROP TABLE IF EXISTS cotacoes CASCADE;

DROP SEQUENCE IF EXISTS comparativos_cotacao_seq;
DROP SEQUENCE IF EXISTS respostas_cotacao_seq;
DROP SEQUENCE IF EXISTS itens_cotacao_seq;
DROP SEQUENCE IF EXISTS cotacoes_seq;
