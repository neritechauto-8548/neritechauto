-- V229: Remove empresa_id from ManyToMany join tables to allow JPA inserts
-- JPA does not automatically map the empresa_id column on a standard @JoinTable

ALTER TABLE usuarios_funcoes DROP COLUMN IF EXISTS empresa_id;
ALTER TABLE funcoes_permissoes DROP COLUMN IF EXISTS empresa_id;
