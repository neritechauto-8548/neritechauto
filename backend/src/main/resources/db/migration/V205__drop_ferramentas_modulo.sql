-- Migration: Drop ferramentas and equipamentos module tables
-- Description: Drops all tables and sequences related to herramientas, equipamentos, calibracoes, manutencoes and reservas.

DROP TABLE IF EXISTS reservas_equipamentos CASCADE;
DROP TABLE IF EXISTS calibracoes CASCADE;
DROP TABLE IF EXISTS manutencoes_equipamentos CASCADE;
DROP TABLE IF EXISTS ferramentas CASCADE;
DROP TABLE IF EXISTS equipamentos CASCADE;

DROP SEQUENCE IF EXISTS seq_reservas_equipamentos;
DROP SEQUENCE IF EXISTS seq_calibracoes;
DROP SEQUENCE IF EXISTS seq_manutencoes_equipamentos;
DROP SEQUENCE IF EXISTS seq_ferramentas;
DROP SEQUENCE IF EXISTS seq_equipamentos;
