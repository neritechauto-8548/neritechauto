-- Adiciona novos campos de controle de assinatura na tabela assinaturas_empresas
ALTER TABLE assinaturas_empresas 
    ADD COLUMN IF NOT EXISTS trial_ends_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS subscription_ends_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS grace_period_ends_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS stripe_customer_id VARCHAR(255);

-- Atualiza status existentes para o novo padrão (Português)
UPDATE assinaturas_empresas SET status = 'ATIVO' WHERE status IN ('ATIVO', 'ACTIVE', 'active');
UPDATE assinaturas_empresas SET status = 'TESTE' WHERE status IN ('TESTE', 'TRIAL', 'trialing', 'TRIALING');
UPDATE assinaturas_empresas SET status = 'CANCELADO' WHERE status = 'CANCELADO' OR status = 'CANCELED';
UPDATE assinaturas_empresas SET status = 'SUSPENSO' WHERE status = 'SUSPENSO' OR status = 'SUSPENDED';
UPDATE assinaturas_empresas SET status = 'ATRASADO' WHERE status = 'INADIMPLENTE' OR status = 'VENCIDO' OR status = 'PAST_DUE';

