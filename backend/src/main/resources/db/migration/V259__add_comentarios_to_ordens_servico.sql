-- V259: Add comentarios column to ordens_servico table
ALTER TABLE ordens_servico ADD COLUMN IF NOT EXISTS comentarios JSONB;
