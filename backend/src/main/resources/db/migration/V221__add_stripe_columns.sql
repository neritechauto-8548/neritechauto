-- Add Stripe customer ID to empresa table
ALTER TABLE empresa ADD COLUMN IF NOT EXISTS stripe_customer_id VARCHAR(255);

-- Add Stripe subscription ID to assinaturas_empresas table
ALTER TABLE assinaturas_empresas ADD COLUMN IF NOT EXISTS stripe_subscription_id VARCHAR(255);
ALTER TABLE assinaturas_empresas ADD COLUMN IF NOT EXISTS stripe_product_id VARCHAR(255);
