ALTER TABLE pedidos_fornecedor DROP CONSTRAINT IF EXISTS chk_status_pedido_fornecedor;
ALTER TABLE pedidos_fornecedor ADD CONSTRAINT chk_status_pedido_fornecedor CHECK (status IN ('PENDENTE', 'ENVIADO', 'RECEBIDO', 'CANCELADO'));
